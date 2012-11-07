package com.sever.physics.game;

import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sever.physic.PhysicsActivity;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	public GameLoopThread gameLoopThread;
	public ConcurrentLinkedQueue<FreeSprite> freeSprites = new ConcurrentLinkedQueue<FreeSprite>();
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

	protected void createFreeSprites() {
		freeSprites.add(new PlayerSprite(this, 0, PhysicsActivity.bmpBall));
		freeSprites.add(new PlayerSprite(this, 1, PhysicsActivity.bmpPlayer));
		freeSprites.add(new PlayerSprite(this, 2, PhysicsActivity.bmpPlayer));
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
			createFreeSprites();
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
		// Paint paint = new Paint();
		// paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		// canvas.drawPaint(paint);
		// paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
		// canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

		try {
			Rect dst = new Rect(0, 0, getWidth(), getHeight());
			canvas.drawBitmap(PhysicsActivity.bmpBack, null, dst, null);

			synchronized (getHolder()) {
				for (Iterator<FreeSprite> it = freeSprites.iterator(); it.hasNext();) {
					FreeSprite sprite = it.next();
					try {
						sprite.onDraw(canvas);
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			float x2 = event.getX();
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x2 = event.getX();
			float y2 = event.getY();
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
