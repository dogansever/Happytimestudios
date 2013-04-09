package com.sever.ramsandgoats.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.sever.ramsandgoats.util.Constants;

public class GameLoopThread extends Thread {
	private GameView view;
	private boolean running = false;
	private boolean sleeping = false;

	private SurfaceHolder holder;

	public GameLoopThread(GameView view, SurfaceHolder holder) {
		this.holder = holder;
		this.view = view;
	}

	public void setRunning(boolean run) {
		running = run;
	}

	public boolean isRunning() {
		return running;
	}

	private long now;
	private int framesCount = 0;
	public static int framesCountAvg = 0;
	private long framesTimer = 0;

	@Override
	public void run() {
		long ticksPS = 1000 / Constants.FPS;
		long startTime;
		long sleepTime;
		while (running) {
			synchronized (this) {
				while (sleeping) {
					try {
						wait();
					} catch (Exception e) {
					}
				}
			}
			Canvas c = null;
			startTime = System.currentTimeMillis();
			try {
				c = holder.lockCanvas();
				synchronized (holder) {
					printCurrentFPS();
					view.onDraw(c);
				}
			} finally {
				if (c != null) {
					holder.unlockCanvasAndPost(c);
				}
			}
			sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
			// System.out.println("sleepTime:" + sleepTime + " ms");
			try {
				if (sleepTime > 0)
					sleep(sleepTime);
				// else
				// sleep(10);
			} catch (Exception e) {
			}
		}
	}

	private void printCurrentFPS() {
		now = System.currentTimeMillis();
		framesCount++;
		if (now - framesTimer > 1000) {
			framesTimer = now;
			framesCountAvg = framesCount;
			System.out.println("framesCountAvg:" + framesCountAvg);
			framesCount = 0;
		}
	}

	public boolean isSleeping() {
		return sleeping;
	}

	public void setSleeping(boolean sleeping) {
		this.sleeping = sleeping;
	}
}
