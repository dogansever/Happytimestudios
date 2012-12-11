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

public class StageEndView extends SurfaceView implements SurfaceHolder.Callback {
	public StageEndLoopThread stageEndLoopThread;
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
		stageEndLoopThread.setRunning(false);
		while (retry) {
			try {
				stageEndLoopThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
		threadStarted = false;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (stageEndLoopThread.getState() == Thread.State.TERMINATED) {
			stageEndLoopThread = new StageEndLoopThread(this, holder);
			threadStarted = true;
			stageEndLoopThread.setRunning(true);
			stageEndLoopThread.start();
			resume();
		} else {
			threadStarted = true;
			stageEndLoopThread.setRunning(true);
			stageEndLoopThread.start();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (!threadStarted) {
			threadStarted = true;
			stageEndLoopThread.setRunning(true);
			stageEndLoopThread.start();
		}
	}

	public StageEndView(final Context context) {
		super(context);
		this.context = context;
		holder = getHolder();
		holder.addCallback(this);
		stageEndLoopThread = new StageEndLoopThread(this, holder);
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
		stageEndLoopThread.setRunning(false);
	}

	private void releaseBitmaps() {
	}

	public void pause() {
		try {
			if (stageEndLoopThread.isSleeping())
				return;
			synchronized (stageEndLoopThread) {
				stageEndLoopThread.setSleeping(true);
			}
		} catch (Exception e) {
		}
	}

	public void resume() {
		try {
			if (!stageEndLoopThread.isSleeping())
				return;

			synchronized (stageEndLoopThread) {
				stageEndLoopThread.setSleeping(false);
				stageEndLoopThread.notify();
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
