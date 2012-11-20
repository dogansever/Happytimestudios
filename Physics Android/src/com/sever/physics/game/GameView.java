package com.sever.physics.game;

import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sever.physic.Constants;
import com.sever.physic.PhysicsActivity;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	public GameLoopThread gameLoopThread;
	public ConcurrentLinkedQueue<FreeSprite> freeSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> staticSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> groundSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public PlayerSprite playerSprite;
	public int score = 0;
	public int point = 0;
	private int scoreHigh = 0;
	static final int PLAY_AREA_PADDING_TOP = 10;
	static final int PLAY_AREA_PADDING_BOTTOM = 10;
	private Context context;
	public boolean threadStarted = false;
	public Timer timerAnimation;
	private Runnable r;
	private boolean justcametolife = false;
	public long timePause;
	public long timeResumed;
	public long delayInSeconds;
	private long timeFired;
	public SurfaceHolder holder;
	private FreeSprite victim;
	private Vec2 pointUp;
	private Vec2 pointDown;
	public static boolean success;

	public void cancelTimer() {
		timerAnimation.cancel();
	}

	public int getPoint() {
		return point;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	protected void addBall(float x, float y) {
		freeSprites.add(new BallSprite(this, PhysicsActivity.bmpBall, x, y));
	}

	protected void addBox(float x, float y) {
		freeSprites.add(new BoxSprite(this, PhysicsActivity.bmpBox, x, y));
	}

	protected void addBox2(float x, float y) {
		freeSprites.add(new BoxSprite(this, PhysicsActivity.bmpBox2, x, y));
	}

	protected void addBarrel(float x, float y) {
		freeSprites.add(new BoxSprite(this, PhysicsActivity.barrel, x, y));
	}

	protected void addPlanet(float x, float y) {
		staticSprites.add(new PlanetSprite(this, PhysicsActivity.planet1, x, y));
	}

	protected void addGroundBox(float x, float y, float hx, float hy) {
		groundSprites.add(new GroundBoxSprite(this, null, x, y, hx, hy));
	}

	private void addPlayer(float x, float y) {
		playerSprite = new PlayerSprite(this, PhysicsActivity.player, x, y, 2, 2);
	}

	protected void createSprites() {
		// ground
		addGroundBox(Constants.upperBoundxScreen * 0.5f, Constants.lowerBoundyScreen - Constants.setAsBoxhyScreen, Constants.upperBoundxScreen, Constants.setAsBoxhyScreen);
		// ceiling
		addGroundBox(Constants.upperBoundxScreen * 0.5f, Constants.upperBoundyScreen + Constants.setAsBoxhyScreen, Constants.upperBoundxScreen, Constants.setAsBoxhyScreen);
		// leftwall
		addGroundBox(Constants.lowerBoundxScreen - Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f, Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f);
		// rightwall
		addGroundBox(Constants.upperBoundxScreen + Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f, Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f);

		addPlanet(this.getWidth() * 0.5f, this.getHeight() * 0.75f);
		addBall(400, 50);
		addBall(50, 400);
		addBox(50, 50);
		addBox(50, 500);
		addBox2(500, 500);
		addBox2(500, 50);
		addBarrel(500, 50);
		addBarrel(500, 50);
		addPlayer(800, 100);

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		System.out.println("surfaceDestroyed");
		boolean retry = true;
		gameLoopThread.setRunning(false);
		timePause = new Date().getTime();
		while (retry) {
			try {
				gameLoopThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
		threadStarted = false;
		System.out.println("surfaceDestroyed:end");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("surfaceCreated");
		if (gameLoopThread.getState() == Thread.State.TERMINATED) {
			gameLoopThread = new GameLoopThread(this, holder);
			threadStarted = true;
			gameLoopThread.setRunning(true);
			gameLoopThread.start();
			resume();
		} else {
			threadStarted = true;
			gameLoopThread.setRunning(true);
			createSprites();
			gameLoopThread.start();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		System.out.println("surfaceChanged");
		if (!threadStarted) {
			threadStarted = true;
			gameLoopThread.setRunning(true);
			gameLoopThread.start();
		}
	}

	public GameView(final Context context) {
		super(context);
		this.context = context;
		holder = getHolder();
		holder.addCallback(this);
		gameLoopThread = new GameLoopThread(this, holder);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		update();
		// paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
		// canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

		try {
			synchronized (getHolder()) {

				Paint paint = new Paint();
				paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
				canvas.drawPaint(paint);

				// Rect dst = new Rect(0, 0, getWidth(), getHeight());
				// canvas.drawBitmap(PhysicsActivity.bmpBack, null, dst, null);

				for (Iterator<FreeSprite> it = staticSprites.iterator(); it.hasNext();) {
					FreeSprite spriteStatic = it.next();
					spriteStatic.onDraw(canvas);
					for (Iterator<FreeSprite> it2 = freeSprites.iterator(); it2.hasNext();) {
						FreeSprite spritefree = it2.next();
						try {
							spriteStatic.pull(spritefree, spriteStatic.getBody().getPosition());
						} catch (Exception e) {
						}
					}
				}

				playerSprite.onDraw(canvas);
				for (Iterator<FreeSprite> it2 = freeSprites.iterator(); it2.hasNext();) {
					FreeSprite spritefree = it2.next();
					try {
						if (playerSprite.powerOn) {
							playerSprite.pull(spritefree);
						} else if (!playerSprite.powerOn && playerSprite.scatter) {
							playerSprite.push(spritefree);
						}
						spritefree.onDraw(canvas);
					} catch (Exception e) {
					}
				}

				if (!playerSprite.powerOn && playerSprite.scatter) {
					playerSprite.scatter = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x2 = event.getX();
		float y2 = event.getY();
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			addBarrel(x2, (PhysicsActivity.deviceHeight - y2));
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			// pointUp = new Vec2(x2, y2);
			// if (victim != null)
			// victim.kickout(pointUp);
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// pointDown = new Vec2(x2, y2);
			// victim = null;
			// for (Iterator<FreeSprite> it = freeSprites.iterator();
			// it.hasNext();) {
			// FreeSprite sprite = it.next();
			// if (sprite.isCollision(x2, y2)) {
			// victim = sprite;
			// System.out.println("!!!Its a hit!!!:" + victim.index);
			// return true;
			// }
			// }
			// System.out.println("!!!Its a miss!!!");

		} else {
		}
		return true;
	}

	public void update() {
		try {
			synchronized (getHolder()) {
				checkGameEnd();
			}
		} catch (Exception e) {
		}
	}

	private void checkGameEnd() {
		// success = true;
		// finishGame();
	}

	public void stopGame() {
		gameLoopThread.setRunning(false);
	}

	private void finishGame() {
		gameLoopThread.setRunning(false);
	}

	public void pause() {
		try {
			if (gameLoopThread.isSleeping())
				return;
			synchronized (gameLoopThread) {
				timePause = new Date().getTime();
				gameLoopThread.setSleeping(true);
			}
		} catch (Exception e) {
		}
	}

	public void resume() {
		try {
			if (!gameLoopThread.isSleeping())
				return;

			synchronized (gameLoopThread) {
				timeResumed = new Date().getTime();
				gameLoopThread.setSleeping(false);
				gameLoopThread.notify();
			}
		} catch (Exception e) {
		}
		if (r != null) {
			synchronized (r) {
				justcametolife = true;
				r.notify();
			}
		}
	}
}
