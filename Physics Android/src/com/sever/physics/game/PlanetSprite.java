package com.sever.physics.game;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;

import com.sever.physic.Constants;

public class PlanetSprite extends FreeSprite {

	public PlanetSprite(GameView gameView, Bitmap bmp, float x, float y) {
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight();
		this.bmp = bmp;
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		addSprite(x, y);
	}

	void addSprite(float x, float y) {
		createStaticBody(x, y);
		createShape();
	}

	public void createShape() {
		CircleDef circle = new CircleDef();
		circle.radius = getWidthPhysical() * 0.5f;
		circle.friction = 1.0f;
		circle.restitution = 0.0f;
		circle.density = 100.0f;
		// getBody().createShape(circle);
		// getBody().setMassFromShapes();
	}

	public void push(FreeSprite sprite, Vec2 positionSrc) {
		float FIELD_RADIUS = getWidthPhysical() * 0.5f;
		applyForce(sprite, positionSrc, FIELD_RADIUS, Constants.gravityPlanetPush);
	}

	public void pull(FreeSprite sprite, Vec2 positionSrc) {
		float FIELD_RADIUS = getWidthPhysical() * 5;
		applyForce(sprite, positionSrc, FIELD_RADIUS, Constants.gravityPlanetPull);
	}

}
