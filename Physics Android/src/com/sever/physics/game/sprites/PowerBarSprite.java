package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.sever.physics.game.GameView;

public class PowerBarSprite extends FreeSprite {

	public PowerBarSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, Bitmap bmp, float x, float y, int bmpColumns, int bmpRows) {
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
		this.invisible = true;
		this.spriteList = spriteList;
	}

	public void onDraw(Canvas canvas) {
		currentFrame = 0;
		currentRow = BMP_ROWS * ((PlayerSprite) gameView.getPlayerSprite()).firePower / ((PlayerSprite) gameView.getPlayerSprite()).firePower_MAX;
		x = ((PlayerSprite) gameView.getPlayerSprite()).x;
		y = ((PlayerSprite) gameView.getPlayerSprite()).y + ((PlayerSprite) gameView.getPlayerSprite()).height * 0.5f;
		super.onDraw(canvas);
	}

}
