package com.sever.ramsandgoats.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;

import com.sever.ramsandgoats.game.GameView;
import com.sever.ramsandgoats.util.SpriteBmp;

public class SoccerBallSprite extends FreeSprite {
	public SoccerBallSprite(ConcurrentLinkedQueue<FreeSprite> playerSprite, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = false;
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
		circle.friction = 1.0f;// zero being completely frictionless
		circle.restitution = 0.5f;// zero being not bounce at all
		circle.density = 1.0f;

		PolygonDef playerDef = new PolygonDef();
		playerDef.setAsBox(getWidthPhysical() * 0.5f, getHeightPhysical() * 0.5f);
		playerDef.friction = 1.0f;
		playerDef.restitution = 0.2f;
		playerDef.density = 10.0f;

		// Assign shape to Body
		// getBody().createShape(playerDef);

		getBody().createShape(circle);
		getBody().setMassFromShapes();
		getBody().setBullet(true);

	}

	public float frictionConstantx = 0.99f;// 1.0f no friction
	public float frictionConstanty = 1.00f;// 1.0f no friction
	public boolean nostabilize = true;

	public void stabilizeVelocity() {
		if (nostabilize)
			return;
		getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x * frictionConstantx, getBody().getLinearVelocity().y * frictionConstanty));
		System.out.println("stabilizeVelocity:(" + getBody().getLinearVelocity().x + "," + getBody().getLinearVelocity().y + ")");
	}
}
