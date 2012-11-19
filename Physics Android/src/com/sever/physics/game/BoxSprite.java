package com.sever.physics.game;

import org.jbox2d.collision.PolygonDef;
import org.jbox2d.dynamics.BodyDef;

import android.graphics.Bitmap;

import com.sever.physic.Constants;
import com.sever.physic.PhysicsActivity;

public class BoxSprite extends FreeSprite {

	public BoxSprite(GameView gameView, Bitmap bmp, float x, float y) {
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight();
		this.bmp = bmp;
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		addBox(x, y);
	}

	void addBox(float x, float y) {
		// Create Dynamic Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		PhysicsActivity.mWorld.bodies.add(PhysicsActivity.mWorld.world.createDynamicBody(bodyDef));

		PolygonDef playerDef = new PolygonDef();
		playerDef.setAsBox(width * 0.5f / Constants.pixelpermeter, height * 0.5f / Constants.pixelpermeter);
		playerDef.friction = 1.0f;
		playerDef.restitution = 0.2f;
		playerDef.density = 1.0f;

		// Assign shape to Body
		this.index = PhysicsActivity.mWorld.bodies.size() - 1;
		getBody().createShape(playerDef);
		getBody().setMassFromShapes();
		getBody().setBullet(true);
		// getBody().setAngularVelocity(360f);
		// getBody().setLinearVelocity(new Vec2(10, 20));

	}

}
