package com.sever.ramsandgoats.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.annotation.SuppressLint;
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

import com.sever.ramsandgoats.GameActivity;
import com.sever.ramsandgoats.IntroActivity;
import com.sever.ramsandgoats.sprites.FreeSprite;
import com.sever.ramsandgoats.sprites.GroundBoxSprite;
import com.sever.ramsandgoats.sprites.SoccerBallSprite;
import com.sever.ramsandgoats.sprites.SoccerPlayerSprite;
import com.sever.ramsandgoats.util.BitmapManager;
import com.sever.ramsandgoats.util.Constants;
import com.sever.ramsandgoats.util.PhysicsWorldManager;
import com.sever.ramsandgoats.util.SpriteBmp;

@SuppressLint("WrongCall")
public class GameView extends SurfaceView implements SurfaceHolder.Callback, GameViewI {
	public GameLoopThread gameLoopThread;

	private Context context;
	public boolean threadStarted = false;
	private Runnable r;
	public long timePause;
	public long timeResumed;
	public long delayInSeconds;
	public SurfaceHolder holder;

	public ConcurrentLinkedQueue<FreeSprite> freeSprites = new ConcurrentLinkedQueue<FreeSprite>();

	public boolean paused;
	public boolean finishGame = true;

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

	public ConcurrentLinkedQueue<FreeSprite> groundSprites = new ConcurrentLinkedQueue<FreeSprite>();

	public FreeSprite addGroundBoxStatic(float x, float y, float hx, float hy) {
		FreeSprite sprite = new GroundBoxSprite(this, null, x, y, hx, hy);
		groundSprites.add(sprite);
		return sprite;
	}

	protected void createSprites() {
		// ground
		addGroundBoxStatic(Constants.upperBoundxScreen * 0.5f, Constants.lowerBoundyScreen - Constants.setAsBoxhyScreen, Constants.upperBoundxScreen, Constants.setAsBoxhyScreen);
		// ceiling
		addGroundBoxStatic(Constants.upperBoundxScreen * 0.5f, Constants.upperBoundyScreen + Constants.setAsBoxhyScreen, Constants.upperBoundxScreen, Constants.setAsBoxhyScreen);
		// leftwall
		addGroundBoxStatic(Constants.lowerBoundxScreen - Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f, Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f);
		// rightwall
		addGroundBoxStatic(Constants.upperBoundxScreen + Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f, Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f);

		// left goal post
		addGroundBoxStatic(0, 270, 50, 10);
		// right goal post
		addGroundBoxStatic(1024, 270, 50, 10);

		// addPlanet(this.getWidth() * 0.5f, this.getHeight() * 0.75f);
		// addBall(400, 150);
		// addBall(150, 400);
		// addBox(150, 150);
		// addBox2(500, 500);
		// addBox2(500, 150);
		addPlayer(200, 210, true);
		addBall(500, 210);
		addPlayer(900, 210, false);
	}

	public ConcurrentLinkedQueue<FreeSprite> playerSprite = new ConcurrentLinkedQueue<FreeSprite>();

	public void addPlayer(float x, float y, boolean facingRight) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.bmpPlayerIdle);
		bmp.add(BitmapManager.bmpPlayerRunning);
		bmp.add(BitmapManager.bmpPlayerKicking);
		bmp.add(BitmapManager.bmpPlayerTricking);
		bmp.add(BitmapManager.bmpPlayerJumpingUp);
		bmp.add(BitmapManager.bmpPlayerJumpingDown);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		colsrows.add(new int[] { 4, 1 });
		colsrows.add(new int[] { 4, 1 });
		colsrows.add(new int[] { 4, 1 });
		colsrows.add(new int[] { 4, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite player = new SoccerPlayerSprite(playerSprite, this, spriteBmp, x, y);
		player.facingRigth = facingRight;
		playerSprite.add(player);
	}

	public void addBall(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.bmpBall);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		playerSprite.add(new SoccerBallSprite(playerSprite, this, spriteBmp, x, y));
	}

	private static int shiftWidth = 1;

	private void drawBackgroundSky(Canvas canvas) {

		Rect src = new Rect(shiftWidth, 0, BitmapManager.bmpSky.getWidth(), BitmapManager.bmpSky.getHeight());
		Rect dst = new Rect(0, 0, getWidth() - shiftWidth, BitmapManager.bmpSky.getHeight());
		canvas.drawBitmap(BitmapManager.bmpSky, src, dst, null);

		src = new Rect(0, 0, shiftWidth, BitmapManager.bmpSky.getHeight());
		dst = new Rect(getWidth() - shiftWidth, 0, getWidth(), BitmapManager.bmpSky.getHeight());
		canvas.drawBitmap(BitmapManager.bmpSky, src, dst, null);
		shiftWidth++;
		if (BitmapManager.bmpSky.getWidth() == shiftWidth) {
			shiftWidth = 0;
		}
	}

	private void drawBackgroundStadium(Canvas canvas) {
		Rect src = new Rect(0, 0, BitmapManager.bmpStadium.getWidth(), BitmapManager.bmpStadium.getHeight());
		Rect dst = new Rect(getWidth() - BitmapManager.bmpStadium.getWidth(), getHeight() - BitmapManager.bmpStadium.getHeight(), getWidth(), getHeight());
		canvas.drawBitmap(BitmapManager.bmpStadium, src, dst, null);

	}

	private void drawBackgroundStadiumMask(Canvas canvas) {
		Rect src = new Rect(0, 0, BitmapManager.bmpStadiumMask.getWidth(), BitmapManager.bmpStadiumMask.getHeight());
		Rect dst = new Rect(getWidth() - BitmapManager.bmpStadiumMask.getWidth(), getHeight() - BitmapManager.bmpStadiumMask.getHeight(), getWidth(), getHeight());
		canvas.drawBitmap(BitmapManager.bmpStadiumMask, src, dst, null);

	}

	public int CHEERTIMEINFPSMAX = (int) (Constants.FPS * 0.25f);
	public int CHEERTIMEINFPS = CHEERTIMEINFPSMAX;
	int index = 0;

	private void drawBackgroundStadiumCheering(Canvas canvas) {
		if (--CHEERTIMEINFPS == 0) {
			CHEERTIMEINFPS = CHEERTIMEINFPSMAX;
			index = ++index % 2;
		}
		Rect src = new Rect((int) (BitmapManager.bmpStadiumCheering.getWidth() * 0.5f * (index)), 0, (int) (BitmapManager.bmpStadiumCheering.getWidth() * 0.5f * (index + 1)),
				BitmapManager.bmpStadiumCheering.getHeight());
		Rect dst = new Rect(getWidth() - (int) (BitmapManager.bmpStadiumCheering.getWidth() * 0.5f), getHeight() - BitmapManager.bmpStadiumCheering.getHeight(), getWidth(), getHeight());
		canvas.drawBitmap(BitmapManager.bmpStadiumCheering, src, dst, null);

	}

	private void draw(Canvas canvas, ConcurrentLinkedQueue<FreeSprite> freeSprites2) {
		for (Iterator<FreeSprite> it2 = freeSprites2.iterator(); it2.hasNext();) {
			FreeSprite spritefree = it2.next();
			try {
				spritefree.onDraw(canvas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		update();
		try {
			synchronized (getHolder()) {
				if (canvas == null)
					return;

				long tstart = System.currentTimeMillis();
				long t = System.currentTimeMillis();

				Paint paint = new Paint();
				paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
				canvas.drawPaint(paint);

				drawBackgroundSky(canvas);
				drawBackgroundStadium(canvas);
				drawBackgroundStadiumCheering(canvas);

				draw(canvas, playerSprite);

				drawBackgroundStadiumMask(canvas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void update() {
		PhysicsWorldManager.mWorld.update();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return tester(event);
		// return handleTouch(event);
	}

	private boolean tester(MotionEvent m) {
		int pointerCount = m.getPointerCount();

		for (int i = 0; i < pointerCount; i++) {
			int x = (int) m.getX(i);
			int y = (int) m.getY(i);
			int id = m.getPointerId(i);
			int action = m.getActionMasked();
			int actionIndex = m.getActionIndex();
			String actionString;

			switch (action) {
			case MotionEvent.ACTION_DOWN:
				actionString = "DOWN";
			case MotionEvent.ACTION_POINTER_DOWN:
				actionString = "DOWN PNTR";
				break;
			case MotionEvent.ACTION_UP:
				actionString = "UP";
			case MotionEvent.ACTION_POINTER_UP:
				actionString = "UP PNTR";
				break;
			case MotionEvent.ACTION_MOVE:
				actionString = "MOVE";
				break;
			default:
				actionString = "";
			}

		}
		return true;
	}

	public void stopGame() {
		gameLoopThread.setRunning(false);
	}

	public void finishGame() {
		System.out.println("finishGame()");
		gameLoopThread.setRunning(false);
		releaseBitmaps();
		Intent intent = new Intent(context, IntroActivity.class);
		context.startActivity(intent);
		((GameActivity) context).finish();
	}

	private void releaseBitmaps() {
	}

	public void togglepauseresume() {
		if (gameLoopThread.isSleeping())
			resume();
		else
			pause();
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
				r.notify();
			}
		}
	}

	public FreeSprite getPlayer() {
		return (FreeSprite) playerSprite.toArray()[0];
	}

	public FreeSprite getBall() {
		return (FreeSprite) playerSprite.toArray()[1];
	}

}
