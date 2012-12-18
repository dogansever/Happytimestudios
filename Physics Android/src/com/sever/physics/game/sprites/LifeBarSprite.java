package com.sever.physics.game.sprites;

import android.graphics.Canvas;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.SpriteBmp;

public class LifeBarSprite extends FreeSprite {

	private ActiveSprite activeSprite;

	public LifeBarSprite(ActiveSprite activeSprite, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
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

	public void onDraw(Canvas canvas) {
		spriteBmp.currentFrame = 0;
		spriteBmp.currentRow = spriteBmp.BMP_ROWS * activeSprite.life / activeSprite.life_MAX;
		spriteBmp.currentRow = spriteBmp.currentRow == spriteBmp.BMP_ROWS ? spriteBmp.currentRow - 1 : spriteBmp.currentRow;
		x = activeSprite.x;
		y = activeSprite.y + activeSprite.height * 0.5f;
		super.onDraw(canvas);
	}

}
