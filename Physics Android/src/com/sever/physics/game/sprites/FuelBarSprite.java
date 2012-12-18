package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.graphics.Canvas;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.SpriteBmp;

public class FuelBarSprite extends FreeSprite {

	public FuelBarSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
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
		spriteBmp.currentFrame = spriteBmp.BMP_COLUMNS * (fm - f) / fm;
		spriteBmp.currentRow = 0;
		x = ((PlayerSprite) gameView.getPlayerSprite()).x + ((PlayerSprite) gameView.getPlayerSprite()).spriteBmp.getWidth(0) * 0.65f
				* (((PlayerSprite) gameView.getPlayerSprite()).facingRigth ? -1 : 1);
		y = ((PlayerSprite) gameView.getPlayerSprite()).y;
		super.onDraw(canvas);
	}

}
