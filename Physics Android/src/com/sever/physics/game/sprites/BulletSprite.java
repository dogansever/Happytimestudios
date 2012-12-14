package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.SpriteBmp;
import com.sever.physics.game.utils.WeaponTypes;

public class BulletSprite extends BallSprite {

	public BulletSprite(ConcurrentLinkedQueue<FreeSprite> sprites, GameView gameView, SpriteBmp spriteBmp, float x, float y, WeaponTypes wt) {
		super(sprites, gameView, spriteBmp, x, y, wt);
		setDensity(50);
		makeFades();
	}

}
