package com.sever.android.main.sprite;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.sever.android.main.GameView;
import com.sever.android.main.StartActivity;

public class WarningSprite extends FreeSprite {
	private static final int BMP_COLUMNS = 4;
	private GameView gameView;
	private Bitmap bmp;
	public int x = 0;
	private int y = 0;
	private int currentFrame = 0;
	private int width;
	private int height;
	private ConcurrentLinkedQueue<FreeSprite> sprites;
	private int life = 3;

	public WarningSprite(ConcurrentLinkedQueue<FreeSprite> sprites, GameView gameView, int index, Bitmap bmp) {
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
		if (currentFrame == BMP_COLUMNS - 1) {
			if (--life < 1) {
				synchronized (sprites) {
					sprites.remove(this);
				}
			}
		} else {
		}
		currentFrame = ++currentFrame % BMP_COLUMNS;
	}

	private float bmpPercentage = (float) (StartActivity.deviceHeight / 800.0f);

	public void onDraw(Canvas canvas) {
		update();
		int srcX = currentFrame * width;
		int srcY = 0;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, (int) (x + width * bmpPercentage), (int) (y + height * bmpPercentage));
		canvas.drawBitmap(bmp, src, dst, null);
	}

	public void freeBitmaps() {
		bmp = null;
	}
}
