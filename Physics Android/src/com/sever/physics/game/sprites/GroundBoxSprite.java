package com.sever.physics.game.sprites;

import org.jbox2d.collision.PolygonDef;

import com.sever.physics.game.GameViewImp;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;

public class GroundBoxSprite extends FreeSprite {

	public GroundBoxSprite(GameViewImp gameView, SpriteBmp spriteBmp, float x, float y, float hx, float hy) {
		this.width = hx * 2;
		this.height = hy * 2;
		this.spriteBmp = spriteBmp;
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		addGroundBox(x, y, hx, hy);
	}

	void addGroundBox(float x, float y, float hx, float hy) {
		createStaticBody(x, y);
		createShape(hx, hy);
		// BodyDef wall = new BodyDef();
		// wall.position.set(new Vec2(x, y));
		// PhysicsActivity.mWorld.bodies.add(PhysicsActivity.mWorld.world.createStaticBody(wall));
		// this.index = PhysicsActivity.mWorld.bodies.size() - 1;

	}

	public void createShape(float hx, float hy) {
		PolygonDef wallDef = new PolygonDef();
		wallDef.setAsBox(hx / Constants.pixelpermeter, hy / Constants.pixelpermeter);
		wallDef.friction = 1.0f;
		wallDef.restitution = 0.2f;

		getBody().createShape(wallDef);
		getBody().computeMass();
	}
}
