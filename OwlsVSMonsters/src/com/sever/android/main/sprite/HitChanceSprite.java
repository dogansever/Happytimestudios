package com.sever.android.main.sprite;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.sever.android.main.GameView;
import com.sever.android.main.StartActivity;

public class HitChanceSprite extends FreeSprite {
	private static final int BMP_COLUMNS = 3;
	private GameView gameView;
	private Bitmap bmp;
	public int x = 0;
	private int y = 0;
	private int currentFrame = 0;
	private int width;
	private int height;
	private ConcurrentLinkedQueue<FreeSprite> sprites;

	public void freeBitmaps() {
		bmp = null;
	}

	public HitChanceSprite(ConcurrentLinkedQueue<FreeSprite> sprites, GameView gameView, int index, Bitmap bmp) {
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight();
		this.bmp = bmp;
		this.sprites = sprites;
		this.gameView = gameView;
		this.x = (int) (810.0f * StartActivity.deviceWidth / 1200.0f);
		int rowY = gameView.rowQueue.get(index);
		this.y = (int) (rowY - height * bmpPercentage);
	}

	private void update() {
	}

	private float bmpPercentage = (float) (StartActivity.deviceHeight / 800.0f);

	public int getHitChance() {
		return currentFrame;
	}

	public void setHitChanceFar() {
		currentFrame = 0;
	}

	public void setHitChanceMedium() {
		currentFrame = 1;
	}

	public void setHitChanceClose() {
		currentFrame = 2;
	}

	public void onDraw(Canvas canvas) {
		update();
		int srcX = currentFrame * width;
		int srcY = 0;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, (int) (x + width * bmpPercentage), (int) (y + height * bmpPercentage));
		canvas.drawBitmap(bmp, src, dst, null);
	}

}
