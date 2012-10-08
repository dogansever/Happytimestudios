package com.sever.android.main.sprite;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sever.android.main.GameGameActivity;
import com.sever.android.main.GameLoopThread;
import com.sever.android.main.GameView;
import com.sever.android.main.StartActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;

public class ZombieSprite {
	// direction = 0 up, 1 left, 2 down, 3 right,
	// animation = 3 back, 1 left, 0 front, 2 right
	// int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
	private static final int BMP_ROWS = 1;
	private static final int BMP_COLUMNS = 8;
	private static final int BMP_COLUMNS_KILL = 8;
	private static final int BMP_COLUMNS_ATTACK = 8;
	private static final int MAX_SPEED = 2;
	private static final int MIN_SPEED = (int) (StartActivity.deviceWidth / 480.0f);
	private static final int WAIT_AMOUNT = 10;
	private int WAIT_AMOUNT_NOW = 0;
	private GameView gameView;
	private Bitmap bmp;
	private Bitmap bmpWalk;
	private Bitmap bmpDie;
	private Bitmap bmpAttack;
	public int x = 0;
	private int y = 0;
	private int xSpeed;
	private int ySpeed = 0;
	private int currentFrame = 0;
	private int width;
	private int height;
	private boolean killed = false;
	private boolean attacking = false;
	private boolean walking = true;
	private int life = GameLoopThread.FPS * 2;
	private int resurrect = 0;
	private ConcurrentLinkedQueue<ZombieSprite> sprites;
	private int index;
	private int hitAmount;

	public boolean isKilled() {
		return killed;
	}

	public void killed() {
		if (--resurrect > 0) {
			x -= xSpeed * 10;
			currentFrame = 0;
			return;
		}

		if (killed)
			return;
		GameGameActivity.RIGHTCOUNT++;
		gameView.incrementScore(index);
		GameGameActivity.context.showPointText(index);
		GameGameActivity.context.updateScore();
		killed = true;
		this.bmp = bmpDie;
		currentFrame = 0;
		this.width = bmp.getWidth() / BMP_COLUMNS_KILL;
		this.height = bmp.getHeight() / BMP_ROWS;
		xSpeed = 0;

		((StartActivity) StartActivity.context).zombieAttackedSound();

	}

	public ZombieSprite(GameView gameView, Bitmap[] bmp, int i, ConcurrentLinkedQueue<ZombieSprite> sprites6) {
		this.sprites = sprites6;
		this.index = i;
		this.width = bmp[0].getWidth() / BMP_COLUMNS;
		this.height = bmp[0].getHeight() / BMP_ROWS;
		this.gameView = gameView;
		this.bmp = bmp[0];
		this.bmpWalk = bmp[0];
		this.bmpDie = bmp[1];
		this.bmpAttack = bmp[2];

		Random rnd = new Random();
		x = 0;
		int rowY = gameView.rowQueue.get(i);
		if (rowY != -1) {
			y = (int) (rowY - height * bmpPercentage);
		} else {
			y = 0;
		}
		// xSpeed = rnd.nextInt(MAX_SPEED) + MIN_SPEED;
		xSpeed = MIN_SPEED;
	}

	public boolean checkReachedLimit() {
		if (x >= (int) (740.0f * StartActivity.deviceWidth / 1200.0f) - width * bmpPercentage / 2) {
			attack();
			return true;
		} else {
			attackCancel();
			return false;
		}
	}

	public void attackCancel() {
		if (attacking)
			attacking = false;
		if (this.bmp == bmpAttack)
			this.bmp = bmpWalk;
	}

	private void attack() {
		if (!killed && (currentFrame % 4 == 0))
			gameView.attacked();
		if (attacking)
			return;

		// xSpeed = 0;
		attacking = true;
		this.bmp = bmpAttack;
		currentFrame = 0;
		this.width = bmp.getWidth() / BMP_COLUMNS_ATTACK;
		this.height = bmp.getHeight() / BMP_ROWS;

		((StartActivity) StartActivity.context).zombieMoansSound();
	}

	public ZombieSprite moveBack(float x) {
		if (GameView.hitCount >= GameView.LIFE_COUNT * 0.75) {
			return this;
		}
		hitAmount = ++hitAmount % 5;
		if (hitAmount == 0) {
			GameView.hitCount++;
		}
		if (killed)
			return this;

		if (x >= (int) (740.0f * StartActivity.deviceWidth / 1200.0f) - width * bmpPercentage / 2) {
			return this;
		}

		this.x = (int) (x - width * bmpPercentage / 2);
		if (attacking) {
			attacking = false;
			this.bmp = bmpWalk;
			// currentFrame = 0;
			// xSpeed = 0;
		}
		return this;
	}

	public ZombieSprite makeFallBack() {
		x -= xSpeed * 20;
		// currentFrame = 0;
		return this;
	}

	public ZombieSprite makeZombieRun() {
		walking = false;
		xSpeed *= 10;
		return this;
	}

	public ZombieSprite makeZombieTough() {
		walking = true;
		xSpeed *= 1;
		resurrect = 3;
		return this;
	}

	private void update() {
		if (killed) {
			if (currentFrame == BMP_COLUMNS_KILL - 1) {
				if (--life < 1) {
					synchronized (sprites) {
						sprites.remove(this);
						freeBitmaps();
					}
				}
			} else {
				currentFrame = ++currentFrame % BMP_COLUMNS_KILL;
			}

			return;
		}
		// if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
		// xSpeed = 0;
		// }

		if (attacking) {
			// x = x + xSpeed;
			currentFrame = ++currentFrame % BMP_COLUMNS;
		} else if (walking) {
			if (currentFrame == BMP_COLUMNS - 2) {
				WAIT_AMOUNT_NOW = WAIT_AMOUNT;
				currentFrame = ++currentFrame % BMP_COLUMNS;
			} else {
			}
			if (WAIT_AMOUNT_NOW == 0) {
				x = x + xSpeed;
				currentFrame = ++currentFrame % BMP_COLUMNS;
			} else {
				WAIT_AMOUNT_NOW--;
			}
		} else {
			x = x + xSpeed;
			currentFrame = ++currentFrame % BMP_COLUMNS;
		}
	}

	public void freeBitmaps() {
		bmp = null;
		bmpWalk = null;
		bmpAttack = null;
		bmpDie = null;
	}

	private float bmpPercentage = (float) (StartActivity.deviceHeight / 800.0f);

	public void onDraw(Canvas canvas) {
		update();
		int srcX = currentFrame * width;
		int srcY = getAnimationRow() * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, (int) (x + width * bmpPercentage), (int) (y + height * bmpPercentage));
		Paint p = new Paint();
		canvas.drawColor(Color.TRANSPARENT);
		canvas.drawBitmap(bmp, src, dst, null);
	}

	private int getAnimationRow() {
		// double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
		// int direction = (int) Math.round(dirDouble) % BMP_ROWS;
		return 0;
	}

	public boolean isCollision(float x2, float y2) {
		return x2 > x && x2 < x + width * bmpPercentage && y2 > y && y2 < y + height * bmpPercentage;
	}
}
