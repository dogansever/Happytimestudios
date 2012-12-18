package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.collision.PolygonDef;

import android.graphics.Canvas;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.SpriteBmp;
import com.sever.physics.game.utils.WeaponTypes;

public class MissileSprite extends GrenadeSprite {

	public MissileSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, SpriteBmp spriteBmp, float x, float y, WeaponTypes wt) {
		super(spriteList, gameView, spriteBmp, x, y, wt);
		facingRigth = gameView.getPlayerSprite().facingRigth;
	}

	public void createShape() {
		PolygonDef playerDef = new PolygonDef();
		playerDef.setAsBox(getWidthPhysical() * 0.5f, getHeightPhysical() * 0.25f);
		playerDef.friction = 1.0f;
		playerDef.restitution = 0.0f;
		playerDef.density = 0.5f;

		getBody().createShape(playerDef);

		// getBody().setXForm(getBody().getLocalCenter(), 45);
		getBody().setMassFromShapes();
		getBody().setBullet(true);

	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

}
