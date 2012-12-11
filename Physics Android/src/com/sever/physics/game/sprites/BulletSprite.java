package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.SpriteBmp;

public class BulletSprite extends BallSprite {

	public BulletSprite(ConcurrentLinkedQueue<FreeSprite> sprites, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		super(sprites, gameView, spriteBmp, x, y);
		setDensity(50);
		makeFades();
		FADE_LIFE = 50;
	}

}
