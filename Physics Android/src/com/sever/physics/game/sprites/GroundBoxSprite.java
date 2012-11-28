package com.sever.physics.game.sprites;

import org.jbox2d.collision.PolygonDef;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.Constants;

import android.graphics.Bitmap;

public class GroundBoxSprite extends FreeSprite {

	public GroundBoxSprite(GameView gameView, Bitmap bmp, float x, float y, float hx, float hy) {
		this.width = hx * 2;
		this.height = hy * 2;
		this.bmp = bmp;
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