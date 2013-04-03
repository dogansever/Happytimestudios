package com.sever.android.main.game;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

@SuppressLint("WrongCall")
public class GameLoopThread extends Thread {
	public static final int FPS = 20;
	private GameView view;
	private boolean running = false;
	private boolean sleeping = false;

	private SurfaceHolder holder;
	public GameLoopThread(GameView view,SurfaceHolder holder) {
		this.holder = holder;
		this.view = view;
	}

	public void setRunning(boolean run) {
		running = run;
	}

	public boolean isRunning() {
		return running;
	}

	@Override
	public void run() {
		long ticksPS = 1000 / FPS;
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
					view.onDraw(c);
				}
			} finally {
				if (c != null) {
					holder.unlockCanvasAndPost(c);
				}
			}
			sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
			try {
				if (sleepTime > 0)
					sleep(sleepTime);
				else
					sleep(10);
			} catch (Exception e) {
			}
		}
	}

	public boolean isSleeping() {
		return sleeping;
	}

	public void setSleeping(boolean sleeping) {
		this.sleeping = sleeping;
	}
}
