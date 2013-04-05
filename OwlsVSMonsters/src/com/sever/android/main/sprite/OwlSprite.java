package com.sever.android.main.sprite;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sever.android.main.GameGameActivity;
import com.sever.android.main.StartActivity;
import com.sever.android.main.game.GameView;
import com.sever.android.main.utils.Owls;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;

public class OwlSprite {
	// direction = 0 up, 1 left, 2 down, 3 right,
	// animation = 3 back, 1 left, 0 front, 2 right
	// int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
	private static final int BMP_ROWS = 1;
	private static final int BMP_COLUMNS = 8;
	private static final int BMP_COLUMNS_KILL = 8;
	private static final int BMP_COLUMNS_ATTACK = 8;
	private static final int MAX_SPEED = 2;
	private static final int MIN_SPEED = 1;
	private static final int WAIT_AMOUNT = 10;
	private int WAIT_AMOUNT_NOW = 0;
	private GameView gameView;
	private Bitmap bmp;
	private Bitmap bmpJammed;
	private Bitmap bmpAttacked;
	private Bitmap bmpFire;
	public int x = 0;
	private int y = 0;
	private int xSpeed = 0;
	private int ySpeed = 0;
	private int currentFrame = 0;
	private int width;
	private int height;
	private boolean firing = false;
	private boolean jammed = false;
	private boolean attacked = false;
	private int life = 15;
	private ConcurrentLinkedQueue<OwlSprite> sprites;
	public Owls owl;
	private int index;

	public void freeBitmaps() {
		bmp = null;
		bmpJammed = null;
		bmpAttacked = null;
		bmpFire = null;
	}

	public void jammedEnd() {
		if (jammed) {
			jammed = false;
			this.bmp = bmpFire;
			currentFrame = BMP_COLUMNS - 1;
		}
	}

	public void jammed() {
		if (!jammed) {
			jammed = true;
			this.bmp = bmpJammed;
			currentFrame = 0;
		}
	}

	public void attackedEnd() {
		if (attacked) {
			attacked = false;
			this.bmp = bmpFire;
			currentFrame = BMP_COLUMNS - 1;
		}
	}

	public void attacked() {
		if (!attacked) {
			attacked = true;
			this.bmp = bmpAttacked;
			currentFrame = 0;
			gameView.createFreeSpritesWarning(index);
		}
	}

	public void fire() {
		firing = true;
		jammed = false;
		this.bmp = bmpFire;
		currentFrame = 0;
		// this.width = bmp.getWidth() / BMP_COLUMNS_KILL;
		// this.height = bmp.getHeight() / BMP_ROWS;
		// xSpeed = 0;

		if (owl == Owls.owlBaretta) {
			((StartActivity) StartActivity.context).fireBarettaSound();
		} else if (owl == Owls.owlMachineGun) {
			((StartActivity) StartActivity.context).fireMachineGunSound();
		} else if (owl == Owls.owlSniper) {
			((StartActivity) StartActivity.context).fireSniperSound();
		}
	}

	public OwlSprite(GameView gameView, Bitmap[] bmp, int index, ConcurrentLinkedQueue<OwlSprite> sprites6, Owls o) {
		this.owl = o;
		this.index = index;
		this.sprites = sprites6;
		this.width = bmp[0].getWidth() / BMP_COLUMNS;
		this.height = bmp[0].getHeight() / BMP_ROWS;
		this.gameView = gameView;
		this.bmp = bmp[0];
		this.bmpFire = bmp[0];
		this.bmpAttacked = bmp[1];
		this.bmpJammed = bmp[2];
		currentFrame = BMP_COLUMNS - 1;
		Random rnd = new Random();
		this.x = (int) (680.0f * StartActivity.deviceWidth / 1200.0f);
		int rowY = gameView.rowQueue.get(index);
		y = (int) (rowY - height * bmpPercentage);
		// xSpeed = rnd.nextInt(MAX_SPEED) + MIN_SPEED;
		// xSpeed = MIN_SPEED;
	}

	private void update() {
		if (currentFrame == BMP_COLUMNS - 2) {
			currentFrame = ++currentFrame % BMP_COLUMNS;
			firing = false;
		} else {
		}

		if (firing) {
			this.bmp = bmpFire;
			currentFrame = ++currentFrame % BMP_COLUMNS;
		} else {
		}

		if (jammed) {
			this.bmp = bmpJammed;
			currentFrame = ++currentFrame % BMP_COLUMNS;
		} else {
		}

		if (attacked) {
			this.bmp = bmpAttacked;
			currentFrame = ++currentFrame % BMP_COLUMNS;
		} else {
		}
	}

	private float bmpPercentage = (float) (StartActivity.deviceHeight / 800.0f);
	private int alpha = 0;
	private int color = 0;
	private String text = "";

	public void doJammed() {
		alpha = 255;
		text = "JAMMED!";
		color = Color.RED;
	}

	public void doHit() {
		alpha = 255;
		text = "HIT!";
		color = Color.GREEN;
	}

	public void doMiss() {
		if (alpha == 255 && text.equals("HIT!")) {
		} else {
			alpha = 255;
			text = "MISS!";
			color = Color.YELLOW;
		}
	}

	public void onDraw(Canvas canvas) {
		update();
		int srcX = currentFrame * width;
		int srcY = getAnimationRow() * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, (int) (x + width * bmpPercentage), (int) (y + height * bmpPercentage));
		canvas.drawBitmap(bmp, src, dst, null);

		alpha -= alpha / 8;
		float xtxt = x + width * bmpPercentage * 0.75f;
		float ytxt = y + height * bmpPercentage * 0.5f - (255 - alpha) / 5;
		int sizetxt = 20;
		gameView.drawText(text, canvas, xtxt, ytxt, color, alpha, sizetxt);
	}

	private int getAnimationRow() {
		double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
		int direction = (int) Math.round(dirDouble) % BMP_ROWS;
		// return DIRECTION_TO_ANIMATION_MAP[direction];
		return 0;
	}

}
