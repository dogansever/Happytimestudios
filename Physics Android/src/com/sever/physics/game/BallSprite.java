package com.sever.physics.game;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;

import android.graphics.Bitmap;

import com.sever.physic.Constants;
import com.sever.physic.PhysicsActivity;

public class BallSprite extends FreeSprite {

	public BallSprite(GameView gameView, Bitmap bmp, float x, float y) {
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight();
		this.bmp = bmp;
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		addBall(x, y);
	}

	// public void onDraw(Canvas canvas) {
	// update();
	// Matrix m = new Matrix();
	// m.postRotate(angle, width / 2, height / 2);
	// m.postTranslate(x, gameView.getHeight() - y);
	// canvas.drawBitmap(bmp, m, null);
	//
	// }

	void addBall(float x, float y) {
		// Create Dynamic Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		PhysicsActivity.mWorld.bodies.add(PhysicsActivity.mWorld.world.createDynamicBody(bodyDef));

		// Create Shape with Properties
		CircleDef circle = new CircleDef();
		circle.radius = width * 0.5f / Constants.pixelpermeter;
		circle.friction = 1.0f;
		circle.restitution = 0.2f;
		circle.density = 1.0f;

		this.index = PhysicsActivity.mWorld.bodies.size() - 1;
		getBody().createShape(circle);
		getBody().setMassFromShapes();
		getBody().setBullet(true);
		// getBody().setAngularVelocity(360f);
//		getBody().setLinearVelocity(new Vec2(15, 15));

	}
}
