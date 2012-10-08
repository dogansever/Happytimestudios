package com.sever.android.main.sprite;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.sever.android.main.GameView;
import com.sever.android.main.StartActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class ProgressSprite extends FreeSprite {
	private int x;
	private int y;
	private Bitmap bmp;
	private Bitmap bmpHead;
	private int life = 15;
	private boolean immortal = true;
	private static final int BMP_COLUMNS = 2;
	private ConcurrentLinkedQueue<FreeSprite> sprites;
	private int width;
	private GameView gameView;

	public float getWidth() {
		return width * bmpPercentage;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	private int height;
	private float percentage = 0.50f;
	public static float bmpPercentage = (float) (StartActivity.deviceHeight / 800.0f);

	public ProgressSprite(ConcurrentLinkedQueue<FreeSprite> sprites, GameView gameView, int x, int y, Bitmap bmp, Bitmap bmpHead) {
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight();
		this.x = x;
		this.y = y;
		// this.x = Math.min(Math.max(x - bmp.getWidth() / 2, 0),
		// gameView.getWidth() - bmp.getWidth());
		// this.y = Math.min(Math.max(y - bmp.getHeight() / 2, 0),
		// gameView.getHeight() - bmp.getHeight());
		this.bmp = bmp;
		this.bmpHead = bmpHead;
		this.sprites = sprites;
		this.gameView = gameView;

	}

	public void onDraw(Canvas canvas) {
		update();

		percentage = gameView.getProgressPercentage();
		int srcX = 0;
		int srcX2 = (int) (percentage * width);
		Rect src = new Rect(srcX, 0, srcX2, height);
		Rect dst = new Rect(x, y, (int) (x + srcX2 * bmpPercentage), (int) (y + height * bmpPercentage));
		canvas.drawBitmap(bmp, src, dst, null);

		srcX = 1 * width + (int) (percentage * width);
		srcX2 = 2 * width;
		src = new Rect(srcX, 0, srcX2, height);
		dst = new Rect((int) (x + percentage * width * bmpPercentage), y, (int) (x + 0.5f * srcX2 * bmpPercentage),
				(int) (y + height * bmpPercentage));
		canvas.drawBitmap(bmp, src, dst, null);

		srcX = 0;
		srcX2 = (int) (bmpHead.getWidth());
		src = new Rect(0, 0, bmpHead.getWidth(), bmpHead.getHeight());
		dst = new Rect((int) (x + percentage * width * bmpPercentage - bmpHead.getWidth() * bmpPercentage * 0.5f), y, (int) (x + percentage * width
				* bmpPercentage + bmpHead.getWidth() * bmpPercentage * 0.5f), (int) (y + bmpHead.getHeight() * bmpPercentage));
		canvas.drawBitmap(bmpHead, src, dst, null);
	}

	public void freeBitmaps() {
		bmp = null;
		bmpHead = null;
	}

	private void update() {
		if (!immortal) {
			if (--life < 1) {
				sprites.remove(this);
			}
		}
	}
}