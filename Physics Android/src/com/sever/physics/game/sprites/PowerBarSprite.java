package com.sever.physics.game.sprites;

import android.graphics.Canvas;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.SpriteBmp;

public class PowerBarSprite extends FreeSprite {

	private ActiveSprite activeSprite;
	public PowerBarSprite(ActiveSprite activeSprite, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
		this.manualFrameSet = true;
		this.invisible = true;
		this.activeSprite = activeSprite;
	}

	public void onDraw(Canvas canvas) {
		spriteBmp.currentFrame = 0;
		spriteBmp.currentRow = spriteBmp.BMP_ROWS * ((PlayerSprite) gameView.getPlayerSprite()).firePower / ((PlayerSprite) gameView.getPlayerSprite()).firePower_MAX;
		spriteBmp.currentRow = spriteBmp.currentRow == spriteBmp.BMP_ROWS ? spriteBmp.currentRow - 1 : spriteBmp.currentRow;
		x = ((PlayerSprite) gameView.getPlayerSprite()).x;
		y = ((PlayerSprite) gameView.getPlayerSprite()).y + ((PlayerSprite) gameView.getPlayerSprite()).height * 0.65f;
		super.onDraw(canvas);
	}

}
