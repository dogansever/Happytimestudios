package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;

public class GrenadeSprite extends FreeSprite {

	public boolean powerOn;

	public GrenadeSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		try {
			this.width = spriteBmp.getWidth();
			this.height = spriteBmp.getHeight();
			this.spriteBmp = spriteBmp;
			this.widthExplosion = spriteBmp.getWidth();
			this.gameView = gameView;
			this.x = x;
			this.y = y;
			this.spriteList = spriteList;

			FADE_LIFE = Constants.FPS * 5;
			makeExplodes();
			makeFades();
			addSprite(x, y);
		} catch (Exception e) {
			killSprite();
		}
	}

	public void explodeBmp() {
		FADE_LIFE = Constants.FPS;
		spriteBmp.setBmpIndex(1);
		noupdate = true;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		angle = 0;
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
		float FIELD_RADIUS = getWidthExplosionPhysical() * 5.0f;
		applyForce(sprite, getPosition(), FIELD_RADIUS, Constants.gravityPushExplosive);
	}

	private Vec2 getPosition() {
		return new Vec2(x / Constants.pixelpermeter, y / Constants.pixelpermeter);
	}

	public void pull(FreeSprite sprite) {
		if (!implodes)
			return;

		float FIELD_RADIUS = Constants.gravityPullFieldRadiusExplosive;
		Vec2 positionSrc = this.getBody().getPosition();
		applyForce(sprite, positionSrc, FIELD_RADIUS, Constants.gravityPullEnemy);
	}

}
