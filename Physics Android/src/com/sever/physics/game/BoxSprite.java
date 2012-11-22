package com.sever.physics.game;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.collision.PolygonDef;

import android.graphics.Bitmap;

public class BoxSprite extends FreeSprite {


	public BoxSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, Bitmap bmp, float x, float y) {
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
		PolygonDef playerDef = new PolygonDef();
		playerDef.setAsBox(getWidthPhysical() * 0.5f, getHeightPhysical() * 0.5f);
		playerDef.friction = 1.0f;
		playerDef.restitution = 0.2f;
		playerDef.density = 10.0f;

		// Assign shape to Body
		getBody().createShape(playerDef);
		getBody().setMassFromShapes();
		getBody().setBullet(true);
		// getBody().setAngularVelocity(360f);
		// getBody().setLinearVelocity(new Vec2(10, 20));

	}
}
