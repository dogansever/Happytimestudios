package com.sever.physics.game;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.collision.CircleDef;

import android.graphics.Bitmap;

public class BallSprite extends FreeSprite {

	public BallSprite(ConcurrentLinkedQueue<FreeSprite> freeSprites, GameView gameView, Bitmap bmp, float x, float y) {
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight();
		this.bmp = bmp;
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.spriteList = spriteList;
		addSprite(x, y);
	}

	void addSprite(float x, float y) {
		createDynamicBody(x, y);
		createShape();
	}

	public void createShape() {
		CircleDef circle = new CircleDef();
		circle.radius = getWidthPhysical() * 0.5f;
		circle.friction = 1.0f;
		circle.restitution = 0.2f;
		circle.density = 10.0f;

		getBody().createShape(circle);
		getBody().setMassFromShapes();
		getBody().setBullet(true);
		// getBody().setAngularVelocity(360f);
		// getBody().setLinearVelocity(new Vec2(15, 15));
	}
}
