package com.sever.physics.game;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import android.graphics.Bitmap;

import com.sever.physic.Constants;
import com.sever.physic.PhysicsActivity;

public class PlanetSprite extends FreeSprite {

	public PlanetSprite(GameView gameView, Bitmap bmp, float x, float y) {
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight();
		this.bmp = bmp;
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		addPlanet(x, y);
	}

	// public void onDraw(Canvas canvas) {
	// update();
	// Matrix m = new Matrix();
	// m.postRotate(angle, width / 2, height / 2);
	// m.postTranslate(x, gameView.getHeight() - y);
	// canvas.drawBitmap(bmp, m, null);
	//
	// }

	void addPlanet(float x, float y) {
		// Create Dynamic Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		PhysicsActivity.mWorld.bodies.add(PhysicsActivity.mWorld.world.createStaticBody(bodyDef));

		// Create Shape with Properties
		CircleDef circle = new CircleDef();
		circle.radius = width * 0.5f / Constants.pixelpermeter;
		circle.friction = 1.0f;
		circle.restitution = 0.0f;
		circle.density = 100.0f;

		this.index = PhysicsActivity.mWorld.bodies.size() - 1;
		// getBody().createShape(circle);
		// getBody().setMassFromShapes();

	}

	public void pull(FreeSprite sprite, Vec2 positionSrc) {
		float FIELD_RADIUS = width * 2 / Constants.pixelpermeter;

		pull(sprite.getBody(), positionSrc, FIELD_RADIUS, Constants.gravityPlanet);

	}

	public void pull(Body body, Vec2 positionSrc, float FIELD_RADIUS, float pullG) {
		Vec2 positionTarget = body.getPosition();
		float range = spacing(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
		if (range <= FIELD_RADIUS) {
			// System.out.println("!!!Pulled it!!!:" + index);
			Vec2 force = new Vec2(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
			force.normalize(); // force direction always point to source
			force.set(force.mul(body.getMass() * pullG * (FIELD_RADIUS - range) / FIELD_RADIUS));
			body.applyForce(force, body.getWorldCenter());
		}
	}

}
