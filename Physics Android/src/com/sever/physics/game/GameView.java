package com.sever.physics.game;

import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sever.physic.IntroActivity;
import com.sever.physic.PhysicsActivity;
import com.sever.physic.StageEndActivity;
import com.sever.physics.game.sprites.BallSprite;
import com.sever.physics.game.sprites.BoxSprite;
import com.sever.physics.game.sprites.BulletSprite;
import com.sever.physics.game.sprites.EnemySprite;
import com.sever.physics.game.sprites.FreeSprite;
import com.sever.physics.game.sprites.GrenadeImplodeSprite;
import com.sever.physics.game.sprites.GrenadeSprite;
import com.sever.physics.game.sprites.GroundBoxSprite;
import com.sever.physics.game.sprites.PlanetSprite;
import com.sever.physics.game.sprites.PlayerSprite;
import com.sever.physics.game.sprites.StaticBoxSprite;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.Joint;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	public GameLoopThread gameLoopThread;
	public ConcurrentLinkedQueue<FreeSprite> explosiveSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> freeSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> staticSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> groundSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> enemySprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> playerSprite = new ConcurrentLinkedQueue<FreeSprite>();
	public int score = 0;
	public int point = 0;
	private int scoreHigh = 0;
	static final int PLAY_AREA_PADDING_TOP = 10;
	static final int PLAY_AREA_PADDING_BOTTOM = 10;
	private static int shiftWidth = 1;
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

	public FreeSprite addBullet(float x, float y) {
		FreeSprite sprite = new BulletSprite(freeSprites, this, PhysicsActivity.bmpBall, x, y);
		freeSprites.add(sprite);
		return sprite;
	}

	protected FreeSprite addBall(float x, float y) {
		FreeSprite sprite = new BallSprite(freeSprites, this, PhysicsActivity.bmpBall, x, y);
		freeSprites.add(sprite);
		return sprite;
	}

	protected FreeSprite addBox(float x, float y) {
		FreeSprite sprite = new BoxSprite(freeSprites, this, PhysicsActivity.bmpBox, x, y);
		freeSprites.add(sprite);
		return sprite;
	}

	public FreeSprite addGrenade(float x, float y) {
		FreeSprite sprite = new GrenadeSprite(explosiveSprites, this, PhysicsActivity.bomb, x, y, 2, 2);
		explosiveSprites.add(sprite);
		return sprite;
	}

	public FreeSprite addGrenadeImploding(float x, float y) {
		FreeSprite sprite = new GrenadeImplodeSprite(explosiveSprites, this, PhysicsActivity.bomb2, x, y, 4, 1);
		explosiveSprites.add(sprite);
		return sprite;
	}

	protected void addBox2(float x, float y) {
		freeSprites.add(new BoxSprite(freeSprites, this, PhysicsActivity.bmpBox2, x, y));
	}

	protected void addBarrel(float x, float y) {
		freeSprites.add(new BoxSprite(freeSprites, this, PhysicsActivity.barrel, x, y));
	}

	protected void addPlanet(float x, float y) {
		staticSprites.add(new PlanetSprite(staticSprites, this, PhysicsActivity.planet1, x, y));
	}

	protected FreeSprite addGround(float x, float y, int w, int h) {
		Bitmap bmp = PhysicsActivity.context.createScaledBitmap(PhysicsActivity.ground, w, h);
		FreeSprite sprite = new StaticBoxSprite(this, bmp, x, y);
		staticSprites.add(sprite);
		return sprite;
	}

	protected FreeSprite addGroundBox(float x, float y, float hx, float hy) {
		FreeSprite sprite = new GroundBoxSprite(this, null, x, y, hx, hy);
		groundSprites.add(sprite);
		return sprite;
	}

	private void addPlayer(float x, float y) {
		playerSprite.add(new PlayerSprite(playerSprite, this, PhysicsActivity.player, x, y, 2, 2));
	}

	private void addEnemy(float x, float y) {
		enemySprites.add(new EnemySprite(enemySprites, this, PhysicsActivity.enemy, x, y, 2, 1));
	}

	protected void createSprites() {
		// ground
		addGroundBox(Constants.upperBoundxScreen * 0.5f, Constants.lowerBoundyScreen - Constants.setAsBoxhyScreen, Constants.upperBoundxScreen, Constants.setAsBoxhyScreen);
		// // ceiling
		// addGroundBox(Constants.upperBoundxScreen * 0.5f,
		// Constants.upperBoundyScreen + Constants.setAsBoxhyScreen,
		// Constants.upperBoundxScreen, Constants.setAsBoxhyScreen);
		// // leftwall
		// addGroundBox(Constants.lowerBoundxScreen -
		// Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f,
		// Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f);
		// // rightwall
		// addGroundBox(Constants.upperBoundxScreen +
		// Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f,
		// Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f);

		addGround(this.getWidth() * 0.5f, -25.0f, (int) (this.getWidth() * 1.0), 150);
		FreeSprite b1 = addGround(450, 400, 50, 50);
		addGround(950, 300, 50, 50);

		// addPlanet(this.getWidth() * 0.5f, this.getHeight() * 0.75f);
		// addBall(400, 150);
		// addBall(150, 400);
		// addBox(150, 150);
		// addBox2(500, 500);
		// addBox2(500, 150);
		addBox2(250, 250);
		addBox2(250, 200);
		addBox2(250, 150);
		addBox2(300, 200);
		addBox2(300, 150);
		addBox(350, 150);
		FreeSprite b2 = addBox(500, 200);
		new Joint().createRevolute(b1, b2);
		FreeSprite b3 = addBox(600, 500);
		FreeSprite b4 = addBox(800, 400);
		new Joint().createPulley(b3, b4, 100, 100);

		addBarrel(450, 100);
		addBarrel(400, 100);
		addBarrel(350, 100);
		addBarrel(300, 100);
		addBarrel(250, 100);
		addBarrel(200, 100);
		addBarrel(150, 100);

		addPlayer(700, 500);
		addEnemy(150, 250);

		// FreeSprite w1 = addBall(400, 150);
		// FreeSprite w2 = addBall(450, 150);
		// FreeSprite c1 = addBox(400, 100);
		//
		// new Joint().createRevolute(w1, c1);
		// new Joint().createRevolute(w2, c1);
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

				drawBackground(canvas);

				// draw statics, pull frees
				for (Iterator<FreeSprite> it = staticSprites.iterator(); it.hasNext();) {
					FreeSprite spriteStatic = it.next();
					spriteStatic.onDraw(canvas);
					applyPullOn(spriteStatic, freeSprites);
					applyPullOn(spriteStatic, explosiveSprites);
					applyPullOn(spriteStatic, enemySprites);
				}

				// draw explosives, push enemies
				for (Iterator<FreeSprite> it = explosiveSprites.iterator(); it.hasNext();) {
					FreeSprite spriteExplosive = it.next();
					spriteExplosive.onDraw(canvas);

					applyPullPushOn(spriteExplosive, freeSprites);
					applyPullPushOn(spriteExplosive, enemySprites);
					applyPullPushOn(spriteExplosive, playerSprite);
				}

				// draw player
				playerSprite.element().onDraw(canvas);

				if (playerSprite.size() == 0)
					return;
				
				// draw frees, push pull frees
				for (Iterator<FreeSprite> it2 = freeSprites.iterator(); it2.hasNext();) {
					FreeSprite spritefree = it2.next();
					try {
						if (((PlayerSprite) playerSprite.element()).powerOn) {
							playerSprite.element().pull(spritefree);
						} else if (!((PlayerSprite) playerSprite.element()).powerOn && ((PlayerSprite) playerSprite.element()).scatter) {
							playerSprite.element().push(spritefree);
						}
						spritefree.onDraw(canvas);
					} catch (Exception e) {
					}
				}
				// draw enemies
				for (Iterator<FreeSprite> it2 = enemySprites.iterator(); it2.hasNext();) {
					FreeSprite spriteEnemy = it2.next();
					try {
						spriteEnemy.onDraw(canvas);
					} catch (Exception e) {
					}
				}

				if (!((PlayerSprite) playerSprite.element()).powerOn && ((PlayerSprite) playerSprite.element()).scatter) {
					((PlayerSprite) playerSprite.element()).scatter = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void applyPullPushOn(FreeSprite spriteExplosive, ConcurrentLinkedQueue<FreeSprite> freeSprites2) {
		for (Iterator<FreeSprite> it2 = freeSprites2.iterator(); it2.hasNext();) {
			FreeSprite spritefree = it2.next();
			applySinglePullPushOn(spriteExplosive, spritefree);
		}
	}

	private void applySinglePullPushOn(FreeSprite spriteExplosive, FreeSprite spritefree) {
		try {
			if (!spriteExplosive.readyToExplode()) {
				spriteExplosive.pull(spritefree);
			} else {
				spriteExplosive.push(spritefree);
			}
		} catch (Exception e) {
		}
	}

	private void applyPullOn(FreeSprite spriteStatic, ConcurrentLinkedQueue<FreeSprite> freeSprites2) {
		for (Iterator<FreeSprite> it2 = freeSprites2.iterator(); it2.hasNext();) {
			FreeSprite spritefree = it2.next();
			try {
				spriteStatic.pull(spritefree, spriteStatic.getBody().getPosition());
			} catch (Exception e) {
			}
		}
	}

	private void drawBackground(Canvas canvas) {

		Rect src = new Rect(shiftWidth, 0, PhysicsActivity.bmpBack.getWidth(), PhysicsActivity.bmpBack.getHeight());
		Rect dst = new Rect(0, 0, getWidth() - shiftWidth, getHeight());
		canvas.drawBitmap(PhysicsActivity.bmpBack, src, dst, null);

		src = new Rect(0, 0, shiftWidth, PhysicsActivity.bmpBack.getHeight());
		dst = new Rect(getWidth() - shiftWidth, 0, getWidth(), getHeight());
		canvas.drawBitmap(PhysicsActivity.bmpBack, src, dst, null);
		shiftWidth++;
		if (PhysicsActivity.bmpBack.getWidth() == shiftWidth) {
			shiftWidth = 0;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x2 = event.getX();
		float y2 = event.getY();
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// addBarrel(x2, (PhysicsActivity.deviceHeight - y2));
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			addEnemy(x2, IntroActivity.deviceHeight - y2);
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
		if (playerSprite.size() == 0) {
			// success = true;
			finishGame();
		}
	}

	public void stopGame() {
		gameLoopThread.setRunning(false);
	}

	private void finishGame() {
		System.out.println("finishGame()");
		gameLoopThread.setRunning(false);
		releaseBitmaps();
		Intent intent = new Intent(context, StageEndActivity.class);
		context.startActivity(intent);
		((PhysicsActivity) context).finish();
	}

	private void releaseBitmaps() {
		releaseBitmapsOn(freeSprites);
		releaseBitmapsOn(staticSprites);
		releaseBitmapsOn(enemySprites);
		releaseBitmapsOn(explosiveSprites);
		releaseBitmapsOn(groundSprites);
	}

	private void releaseBitmapsOn(ConcurrentLinkedQueue<FreeSprite> freeSprites2) {
		for (FreeSprite z : freeSprites2) {
			z.freeBitmaps();
		}
		freeSprites2.clear();
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
