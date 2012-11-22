package com.sever.physics.game;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.graphics.Bitmap;

import com.sever.physic.Constants;

public class EnemySprite extends FreeSprite {

	public boolean powerOn;
	public boolean scatter;

	public EnemySprite(GameView gameView, Bitmap bmp, float x, float y, int bmpColumns, int bmpRows) {
		BMP_COLUMNS = bmpColumns;
		BMP_ROWS = bmpRows;
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight() / BMP_ROWS;
		this.bmp = bmp;
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
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
		circle.restitution = 0.2f;// zero being not bounce at all
		circle.density = 10.0f;

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
		sprite.makeVisible();
		currentRow = 0;
		currentFrame = 0;
		float FIELD_RADIUS = getWidthPhysical();
		Vec2 positionTarget = sprite.getBody().getPosition();
		Vec2 positionSrc = this.getBody().getPosition();
		float range = spacing(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
		if (range <= FIELD_RADIUS) {
			Body body = sprite.getBody();
			body.setAngularVelocity(0);
			body.setLinearVelocity(new Vec2(0, 0));
			Vec2 force = new Vec2(1.0f, 0.0f);
			force.normalize(); // force direction always point to source
			force.set(force.mul((float) (body.getMass() * Constants.gravityPushEnemy)));
			body.applyImpulse(force, body.getWorldCenter());
		}

		// applyForce(sprite.getBody(), positionSrc, FIELD_RADIUS,
		// Constants.gravityPushPlayer, force);
	}

	public void pull(FreeSprite sprite) {
		float FIELD_RADIUS = Constants.gravityPullFieldRadiusEnemy;
		float CLOSE_FIELD_RADIUS = getWidthPhysical();
		Body body = sprite.getBody();
		Vec2 positionTarget = body.getPosition();
		Vec2 positionSrc = this.getBody().getPosition();
		float range = spacing(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
		// if (CLOSE_FIELD_RADIUS <= range && range <= FIELD_RADIUS) {
		// // System.out.println("!!!Pulled it!!!:" + index);
		// Vec2 force = new Vec2(positionSrc.x - positionTarget.x, positionSrc.y
		// - positionTarget.y + height / Constants.pixelpermeter);
		// force.normalize(); // force direction always point to source
		// force.set(force.mul(body.getMass() * Constants.gravityPullPlayer *
		// (FIELD_RADIUS - range) / FIELD_RADIUS));
		// body.applyForce(force, body.getWorldCenter());
		// }
		currentRow = 1;
		currentFrame = 0;
		if (range <= CLOSE_FIELD_RADIUS) {
			// sprite.makeInvisible();
			body.setAngularVelocity(0);
			body.setLinearVelocity(new Vec2(0, 0));
		} else if (sprite.isVisible()) {
			applyForce(sprite, positionSrc, FIELD_RADIUS, Constants.gravityPullEnemy);
		}
	}

	public void throttleUp() {
		throttle(0);
	}

	public void throttleDown() {
		throttle(1);
	}

	public void throttleLeft() {
		throttle(2);
	}

	public void throttleRight() {
		throttle(3);
	}

	public void powerUp() {
		powerOn = true;
		scatter = true;
	}

	public void powerDown() {
		powerOn = false;
	}

	public void throttle(int direction) {
		Vec2 force = null;
		switch (direction) {
		case 0:
			// up
			force = new Vec2(0.0f, 1.0f);
			break;
		case 1:
			// down
			force = new Vec2(0.0f, -1.0f);
			break;
		case 2:
			// left
			force = new Vec2(-1.0f, 0.0f);
			break;
		case 3:
			// right
			force = new Vec2(1.0f, 0.0f);
			break;

		default:
			break;
		}
		Body body = getBody();
		force.normalize(); // force direction always point to source
		force.set(force.mul((float) (body.getMass() * Constants.gravityThrottle)));
		body.applyImpulse(force, body.getWorldCenter());

		// System.out.println("!!!Kicked it!!!:" + index + ", force:x:" +
		// force.x + ", y:" + force.y);
	}
}
