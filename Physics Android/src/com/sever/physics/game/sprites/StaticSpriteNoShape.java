package com.sever.physics.game.sprites;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.SpriteBmp;

public class StaticSpriteNoShape extends StaticBoxSprite {

	public StaticSpriteNoShape(GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		super(gameView, spriteBmp, x, y);
	}

	void addSprite(float x, float y) {
		createStaticBody(x, y);
	}

}
