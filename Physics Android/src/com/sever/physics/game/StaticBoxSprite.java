package com.sever.physics.game;

import org.jbox2d.collision.PolygonDef;

import android.graphics.Bitmap;

public class StaticBoxSprite extends FreeSprite {

	public StaticBoxSprite(GameView gameView, Bitmap bmp, float x, float y) {
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight();
		this.bmp = bmp;
		this.gameView = gameView;
		this.noupdate = true;
		this.noRotation = true;
		this.x = x;
		this.y = y;
		addSprite(x, y);
	}

	void addSprite(float x, float y) {
		createStaticBody(x, y);
		createShape();
	}

	public void createShape() {
		PolygonDef playerDef = new PolygonDef();
		playerDef.setAsBox(getWidthPhysical() * 0.5f, getHeightPhysical() * 0.5f);
		playerDef.friction = 1.0f;
		playerDef.restitution = 0.0f;
		playerDef.density = 100.0f;

		// Assign shape to Body
		getBody().createShape(playerDef);
		getBody().setMassFromShapes();
		// getBody().setAngularVelocity(360f);
		// getBody().setLinearVelocity(new Vec2(10, 20));

	}
}
