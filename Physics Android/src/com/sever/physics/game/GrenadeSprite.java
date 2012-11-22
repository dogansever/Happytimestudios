package com.sever.physics.game;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;

import com.sever.physic.Constants;

public class GrenadeSprite extends FreeSprite {

	public boolean powerOn;
	public boolean scatter;

	public GrenadeSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, Bitmap bmp, float x, float y, int bmpColumns, int bmpRows) {
		BMP_COLUMNS = bmpColumns;
		BMP_ROWS = bmpRows;
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight() / BMP_ROWS;
		this.bmp = bmp;
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.spriteList = spriteList;
		this.fades = true;
		this.explodes = true;
		addSprite(x, y);
	}

	void addSprite(float x, float y) {
		createDynamicBody(x, y);
		createShape();
	}

	public void createShape() {
		CircleDef circle = new CircleDef();
		circle.radius = getWidthPhysical() * 0.5f;
		circle.friction = 1.0f;// zero being completely frictionless
		circle.restitution = 0.0f;// zero being not bounce at all
		circle.density = 0.5f;

		// PolygonDef playerDef = new PolygonDef();
		// playerDef.setAsBox(width * 0.5f / Constants.pixelpermeter, height *
		// 0.5f / Constants.pixelpermeter);
		// playerDef.friction = 1.0f;
		// playerDef.restitution = 0.2f;
		// playerDef.density = 10.0f;

		// Assign shape to Body
		// getBody().createShape(playerDef);

		getBody().createShape(circle);
		getBody().setMassFromShapes();
		getBody().setBullet(true);

	}

	public void push(FreeSprite sprite) {
		float FIELD_RADIUS = getWidthPhysical() * 5.0f;
		applyForce(sprite, getBody().getPosition(), FIELD_RADIUS, Constants.gravityPushExplosive);
	}

	public void pull(FreeSprite sprite) {
		if (!implodes)
			return;

		float FIELD_RADIUS = Constants.gravityPullFieldRadiusExplosive;
		Vec2 positionSrc = this.getBody().getPosition();
		applyForce(sprite, positionSrc, FIELD_RADIUS, Constants.gravityPullEnemy);
	}

}
