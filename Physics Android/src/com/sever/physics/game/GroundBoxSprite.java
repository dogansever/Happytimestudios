package com.sever.physics.game;

import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

import android.graphics.Bitmap;

import com.sever.physic.PhysicsActivity;

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
		BodyDef wall = new BodyDef();
		wall.position.set(new Vec2(x, y));
		PhysicsActivity.mWorld.bodies.add(PhysicsActivity.mWorld.world.createStaticBody(wall));
		this.index = PhysicsActivity.mWorld.bodies.size() - 1;

		PolygonDef wallDef = new PolygonDef();
		wallDef.setAsBox(hx, hy);
		wallDef.friction = 1.0f;
		wallDef.restitution = 0.2f;

		getBody().createShape(wallDef);
		getBody().computeMass();
	}

}
