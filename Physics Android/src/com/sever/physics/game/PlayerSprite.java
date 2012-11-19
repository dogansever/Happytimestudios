package com.sever.physics.game;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import android.graphics.Bitmap;

import com.sever.physic.Constants;
import com.sever.physic.PhysicsActivity;

public class PlayerSprite extends FreeSprite {

	public boolean powerOn;
	public boolean scatter;

	public PlayerSprite(GameView gameView, Bitmap bmp, float x, float y) {
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight();
		this.bmp = bmp;
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
		addPlayer(x, y);
	}

	// public void onDraw(Canvas canvas) {
	// update();
	// Matrix m = new Matrix();
	// m.postRotate(angle, width / 2, height / 2);
	// m.postTranslate(x, gameView.getHeight() - y);
	// canvas.drawBitmap(bmp, m, null);
	//
	// }

	void addPlayer(float x, float y) {
		// Create Dynamic Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		PhysicsActivity.mWorld.bodies.add(PhysicsActivity.mWorld.world.createDynamicBody(bodyDef));

		CircleDef circle = new CircleDef();
		circle.radius = width * 0.5f / Constants.pixelpermeter;
		circle.friction = 1.0f;
		circle.restitution = 0.2f;
		circle.density = 10.0f;

		// PolygonDef playerDef = new PolygonDef();
		// playerDef.setAsBox(width * 0.5f / Constants.pixelpermeter, height *
		// 0.5f / Constants.pixelpermeter);
		// playerDef.friction = 1.0f;
		// playerDef.restitution = 0.2f;
		// playerDef.density = 10.0f;

		// Assign shape to Body
		this.index = PhysicsActivity.mWorld.bodies.size() - 1;
		// getBody().createShape(playerDef);
		getBody().createShape(circle);
		getBody().setMassFromShapes();
		getBody().setBullet(true);

	}

	public void push(FreeSprite sprite) {
		float FIELD_RADIUS = width / Constants.pixelpermeter;
		Vec2 positionTarget = sprite.getBody().getPosition();
		Vec2 positionSrc = this.getBody().getPosition();
		float range = spacing(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
		if (range <= FIELD_RADIUS) {
			sprite.doScatter();
		}
	}

	public void pull(FreeSprite sprite) {
		float CLOSE_FIELD_RADIUS = width / Constants.pixelpermeter;
		Body body = sprite.getBody();
		Vec2 positionTarget = body.getPosition();
		Vec2 positionSrc = this.getBody().getPosition();
		float range = spacing(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
		if (CLOSE_FIELD_RADIUS <= range && range <= Constants.gravityPullFieldRadius) {
			// System.out.println("!!!Pulled it!!!:" + index);
			Vec2 force = new Vec2(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y + height / Constants.pixelpermeter);
			force.normalize(); // force direction always point to source
			force.set(force.mul(body.getMass() * Constants.gravityPull * (Constants.gravityPullFieldRadius - range) / Constants.gravityPullFieldRadius));
			body.applyForce(force, body.getWorldCenter());
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
