package com.sever.physics.game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.sever.physic.Constants;
import com.sever.physic.PhysicsActivity;

public class FreeSprite {

	public static final int BMP_COLUMNS = 1;
	public GameView gameView;
	public Bitmap bmp;

	public float x = 0;
	public float y = 0;
	public float width;
	public float height;
	public int index;
	public float angle;
	public boolean noRotation;

	public void freeBitmaps() {
	}

	public void pull(FreeSprite sprite) {

	}

	public void pull(FreeSprite sprite, Vec2 positionSrc) {

	}

	protected Body getBody() {
		return PhysicsActivity.mWorld.bodies.get(index);
	}

	protected void update() {
		updatePosition();
	}

	protected void kickout(Vec2 positionSrc) {
		Body body = getBody();
		Vec2 positionTarget = body.getPosition();
		Vec2 force = new Vec2(positionSrc.x / Constants.pixelpermeter - positionTarget.x, positionTarget.y - positionSrc.y / Constants.pixelpermeter);
		force.normalize(); // force direction always point to source
		force.set(force.mul((float) (body.getMass() * -10.0 * Constants.gravityy)));
		body.applyImpulse(force, body.getWorldCenter());
		// System.out.println("!!!Kicked it!!!:" + index + ", force:x:" +
		// force.x + ", y:" + force.y);
	}

	protected void doScatter() {
		Body body = getBody();
		Vec2 positionTarget = body.getPosition();
		Vec2 force = new Vec2(1.0f, 0.0f);
		force.normalize(); // force direction always point to source
		force.set(force.mul((float) (body.getMass() * Constants.gravityPush)));
		body.applyImpulse(force, body.getWorldCenter());
		System.out.println("!!!Scattered!!!:" + index + ", force:x:" + force.x + ", y:" + force.y);
	}

	private void updatePosition() {
		Body body = getBody();
		this.x = body.getPosition().x * Constants.pixelpermeter;
		this.y = body.getPosition().y * Constants.pixelpermeter;
		this.angle = body.getAngle();
		Vec2 linVel = body.getLinearVelocity();
		// System.out.println("index:" + index + ",x:" + x + ",y:" + y +
		// ",linVel:x:" + linVel.x + ",y:" + linVel.y + ",angle:" + angle +
		// ",width:" + width + ",height:" + height);
	}

	public void onDraw(Canvas canvas) {
		update();
		if (bmp != null) {
			Matrix m = new Matrix();
			if (!noRotation)
				m.postRotate((float) Math.toDegrees(angle), width * 0.5f, height * 0.5f);
			Vec2 translate = getBitmapDrawingXY();
			m.postTranslate(translate.x, translate.y);
			canvas.drawBitmap(bmp, m, null);
		}
	}

	public boolean isCollision(float x2, float y2) {
		Vec2 click = fromScreen(x2, y2);
		System.out.println("isCollision?index" + index + ",x:" + x + ",y:" + y + ",click.x:" + click.x + ",click.y:" + click.y + ",width:" + width + ",height:" + height);
		return click.x > x - width * 0.5f && click.x < x + width * 0.5f && click.y > y - height * 0.5f && click.y < y + height * 0.5f;
	}

	public Vec2 fromScreen(float x2, float y2) {
		Vec2 pos = new Vec2();
		pos.x = x2;
		pos.y = PhysicsActivity.deviceHeight - y2;
		return pos;
	}

	public Vec2 getBitmapDrawingXY() {
		Vec2 pos = new Vec2();
		pos.x = x - width * 0.5f;
		pos.y = PhysicsActivity.deviceHeight - y - height * 0.5f;
		return pos;
	}

	public float spacing(float x, float y) {
		return (float) Math.sqrt(x * x + y * y);
	}
}
