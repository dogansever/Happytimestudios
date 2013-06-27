package com.sever.physics.game.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.sever.physics.game.GameViewImp;
import com.sever.physics.game.utils.SpriteBmp;

public class FuelBarSprite extends FreeSprite {

	private ActiveSprite activeSprite;

	public FuelBarSprite(ActiveSprite activeSprite, GameViewImp gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
		this.manualFrameSet = true;
		this.activeSprite = activeSprite;
	}

	public void makeRedAlert() {
		spriteBmp.currentRow = 1;
	}

	public void makeGreenAlert() {
		spriteBmp.currentRow = 0;
	}

	public void onDraw(Canvas canvas) {
		if (gameView.idle)
			return;

		int f = ((PlayerSprite) gameView.getPlayerSprite()).fuel;
		int fm = ((PlayerSprite) gameView.getPlayerSprite()).fuel_MAX;
		spriteBmp.currentFrame = spriteBmp.BMP_COLUMNS * (fm - f) / fm;
		spriteBmp.currentFrame = spriteBmp.currentFrame >= spriteBmp.BMP_COLUMNS ? spriteBmp.BMP_COLUMNS - 1 : spriteBmp.currentFrame;
		// spriteBmp.currentRow = 0;
		x = ((PlayerSprite) gameView.getPlayerSprite()).x + ((PlayerSprite) gameView.getPlayerSprite()).spriteBmp.getWidth(0) * 0.65f
				* (((PlayerSprite) gameView.getPlayerSprite()).facingRigth ? -1 : 1);
		y = ((PlayerSprite) gameView.getPlayerSprite()).y;

		int srcX = (int) (spriteBmp.currentFrame * width);
		int srcY = (int) (spriteBmp.currentRow * height);
		Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
		spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.getBitmap(), src.left, src.top, (int) (width), (int) height);

		super.onDraw(canvas);
	}

}
