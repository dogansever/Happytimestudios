package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.sever.physics.game.GameView;

import android.graphics.Bitmap;

public class BulletSprite extends BallSprite {

	public BulletSprite(ConcurrentLinkedQueue<FreeSprite> sprites, GameView gameView, Bitmap bmp, float x, float y) {
		super(sprites, gameView, bmp, x, y);
		setDensity(20);
		makeFades();
		FADE_LIFE = 50;
	}

}
