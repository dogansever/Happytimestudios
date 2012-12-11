package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;

public class PlanetSprite extends FreeSprite {

	public PlanetSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.spriteList = spriteList;
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
