package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.sever.physics.game.GameView;

public class FuelBarSprite extends FreeSprite {

	public FuelBarSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, Bitmap bmp, float x, float y, int bmpColumns, int bmpRows) {
		BMP_COLUMNS = bmpColumns;
		BMP_ROWS = bmpRows;
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight() / BMP_ROWS;
		this.bmp = bmp;
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
		this.manualFrameSet = true;
		this.spriteList = spriteList;
	}

	public void onDraw(Canvas canvas) {
		int f = ((PlayerSprite) gameView.getPlayerSprite()).fuel;
		int fm = ((PlayerSprite) gameView.getPlayerSprite()).fuel_MAX;
		currentFrame = BMP_COLUMNS * (fm - f) / fm;
		currentRow = 0;
		super.onDraw(canvas);
	}

}
