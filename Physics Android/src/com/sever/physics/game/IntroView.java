package com.sever.physics.game;

import android.content.Context;
import android.content.Intent;
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

public class IntroView extends SurfaceView implements SurfaceHolder.Callback {
	public IntroLoopThread introLoopThread;
	private Context context;
	public boolean threadStarted = false;
	private Runnable r;
	public SurfaceHolder holder;
	private static int shiftWidth = 1;
	private static int shiftWidth2 = 1;
	private static int shiftWidth3 = 1;

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		introLoopThread.setRunning(false);
		while (retry) {
			try {
				introLoopThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
		threadStarted = false;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (introLoopThread.getState() == Thread.State.TERMINATED) {
			introLoopThread = new IntroLoopThread(this, holder);
			threadStarted = true;
			introLoopThread.setRunning(true);
			introLoopThread.start();
			resume();
		} else {
			threadStarted = true;
			introLoopThread.setRunning(true);
			introLoopThread.start();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (!threadStarted) {
			threadStarted = true;
			introLoopThread.setRunning(true);
			introLoopThread.start();
		}
	}

	public IntroView(final Context context) {
		super(context);
		this.context = context;
		holder = getHolder();
		holder.addCallback(this);
		introLoopThread = new IntroLoopThread(this, holder);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		update();
		try {
			synchronized (getHolder()) {
				Paint paint = new Paint();
				paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
				canvas.drawPaint(paint);
				drawBackground(canvas);
//				drawBackground2(canvas);
//				drawBackground3(canvas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void drawBackground(Canvas canvas) {
		Rect src = new Rect(shiftWidth, 0, IntroActivity.bmpIntro.getWidth(), IntroActivity.bmpIntro.getHeight());
		Rect dst = new Rect(0, 0, getWidth() - shiftWidth, getHeight());
		canvas.drawBitmap(IntroActivity.bmpIntro, src, dst, null);
		src = new Rect(0, 0, shiftWidth, IntroActivity.bmpIntro.getHeight());
		dst = new Rect(getWidth() - shiftWidth, 0, getWidth(), getHeight());
		canvas.drawBitmap(IntroActivity.bmpIntro, src, dst, null);
		shiftWidth++;
		if (IntroActivity.bmpIntro.getWidth() == shiftWidth) {
			shiftWidth = 0;
		}
	}

	private void drawBackground2(Canvas canvas) {
		Rect src = new Rect(shiftWidth2, 0, IntroActivity.bmpIntro2.getWidth(), IntroActivity.bmpIntro2.getHeight());
		Rect dst = new Rect(0, getHeight() - 50, getWidth() - shiftWidth2, getHeight());
		canvas.drawBitmap(IntroActivity.bmpIntro2, src, dst, null);
		src = new Rect(0, 0, shiftWidth2, IntroActivity.bmpIntro2.getHeight());
		dst = new Rect(getWidth() - shiftWidth2, getHeight() - 50, getWidth(), getHeight());
		canvas.drawBitmap(IntroActivity.bmpIntro2, src, dst, null);
		shiftWidth2 += 3;
		if (IntroActivity.bmpIntro2.getWidth() <= shiftWidth2) {
			shiftWidth2 = 0;
		}

	}

	private void drawBackground3(Canvas canvas) {
		Rect src = new Rect(shiftWidth3, 0, IntroActivity.bmpIntro2.getWidth(), IntroActivity.bmpIntro2.getHeight());
		Rect dst = new Rect(0, 0, getWidth() - shiftWidth3, 50);
		canvas.drawBitmap(IntroActivity.bmpIntro2, src, dst, null);
		src = new Rect(0, 0, shiftWidth3, IntroActivity.bmpIntro2.getHeight());
		dst = new Rect(getWidth() - shiftWidth3, 0, getWidth(), 50);
		canvas.drawBitmap(IntroActivity.bmpIntro2, src, dst, null);
		shiftWidth3 += 2;
		if (IntroActivity.bmpIntro2.getWidth() <= shiftWidth3) {
			shiftWidth3 = 0;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x2 = event.getX();
		float y2 = event.getY();
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
		} else {
		}
		return true;
	}

	public void update() {
		try {
			synchronized (getHolder()) {
			}
		} catch (Exception e) {
		}
	}

	public void stopGame() {
		introLoopThread.setRunning(false);
	}

	public void finishIntro() {
		introLoopThread.setRunning(false);
		releaseBitmaps();
		Intent intent = new Intent(context, PhysicsActivity.class);
		context.startActivity(intent);
		((IntroActivity) context).finish();
	}

	private void releaseBitmaps() {
	}

	public void pause() {
		try {
			if (introLoopThread.isSleeping())
				return;
			synchronized (introLoopThread) {
				introLoopThread.setSleeping(true);
			}
		} catch (Exception e) {
		}
	}

	public void resume() {
		try {
			if (!introLoopThread.isSleeping())
				return;

			synchronized (introLoopThread) {
				introLoopThread.setSleeping(false);
				introLoopThread.notify();
			}
		} catch (Exception e) {
		}
		if (r != null) {
			synchronized (r) {
				r.notify();
			}
		}
	}
}
