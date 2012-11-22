package com.sever.physics.game;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.collision.PolygonDef;

import android.graphics.Bitmap;

public class GrenadeImplodeSprite extends GrenadeSprite {

	public boolean powerOn;
	public boolean scatter;

	public GrenadeImplodeSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, Bitmap bmp, float x, float y, int bmpColumns, int bmpRows) {
		super(spriteList, gameView, bmp, x, y, bmpColumns, bmpRows);
		implodes = true;
		BMP_FPS = FADE_LIFE / 3;
	}

	public void createShape() {
		// CircleDef circle = new CircleDef();
		// circle.radius = getWidthPhysical() * 0.5f;
		// circle.friction = 1.0f;// zero being completely frictionless
		// circle.restitution = 0.0f;// zero being not bounce at all
		// circle.density = 0.5f;

		PolygonDef playerDef = new PolygonDef();
		playerDef.setAsBox(getWidthPhysical() * 0.5f, getHeightPhysical() * 0.5f);
		playerDef.friction = 1.0f;
		playerDef.restitution = 0.0f;
		playerDef.density = 0.5f;

		getBody().createShape(playerDef);

		// getBody().createShape(circle);
		getBody().setMassFromShapes();
		getBody().setBullet(true);

	}
}
