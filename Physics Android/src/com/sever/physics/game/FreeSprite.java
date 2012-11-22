package com.sever.physics.game;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.sever.physic.Constants;
import com.sever.physic.PhysicsActivity;

public class FreeSprite {

	public int BMP_COLUMNS = 1;
	public int BMP_ROWS = 1;
	public int BMP_FPS = 6;
	public int BMP_FPS_CURRENT = 0;
	public int FADE_LIFE = 100;
	public GameView gameView;
	public Bitmap bmp;
	public Bitmap bmpFrame;

	public float x = 0;
	public float y = 0;
	public float width;
	public float height;
	// public int index;
	public float angle;
	public boolean noRotation = false;
	public boolean invisible = false;
	public boolean noupdate = false;
	public boolean fades = false;
	public boolean explodes = false;
	public boolean implodes = false;
	public int currentFrame = 0;
	public int currentRow = 0;

	protected ConcurrentLinkedQueue<FreeSprite> spriteList;
	protected Body body;

	public void freeBitmaps() {
		bmp = null;
		bmpFrame = null;
	}

	public void push(FreeSprite sprite) {
	}

	public void pull(FreeSprite sprite) {

	}

	public void pull(FreeSprite sprite, Vec2 positionSrc) {

	}

	void addSprite(float x, float y) {

	}

	public void createShape() {

	}

	public void createStaticBody(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x / Constants.pixelpermeter, y / Constants.pixelpermeter);
		this.body = PhysicsActivity.mWorld.world.createStaticBody(bodyDef);
		PhysicsActivity.mWorld.bodies.add(body);
	}

	public void killSprite() {
		spriteList.remove(this);
		destroyShape();
		PhysicsActivity.mWorld.bodies.remove(body);
	}

	public void createDynamicBody(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x / Constants.pixelpermeter, y / Constants.pixelpermeter);
		this.body = PhysicsActivity.mWorld.world.createDynamicBody(bodyDef);
		PhysicsActivity.mWorld.bodies.add(body);
	}

	public void destroyShape() {
		if (getBody().getShapeList() != null)
			getBody().destroyShape(getBody().getShapeList());
	}

	protected Body getBody() {
		int index = PhysicsActivity.mWorld.bodies.indexOf(body);
		return PhysicsActivity.mWorld.bodies.get(index);
	}

	protected void update() {
		updatePosition();
		updateBitmap();
	}

	private void updateBitmap() {
		if (++BMP_FPS_CURRENT % BMP_FPS == 0)
			currentFrame = ++currentFrame % BMP_COLUMNS;

		int srcX = (int) (currentFrame * width);
		int srcY = (int) (currentRow * height);
		Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
		Rect dst = new Rect((int) (x), (int) (y), (int) (x + width), (int) (y + height));
		Paint p = new Paint();
		bmpFrame = Bitmap.createBitmap(bmp, src.left, src.top, (int) width, (int) height);
		// bmpFrame = bmp;
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

	public void onDraw(Canvas canvas) {
		update();
		if (fades && FADE_LIFE >= 0) {
			if (FADE_LIFE-- == 0) {
				killSprite();
				return;
			}
		}
		if (bmp != null && isVisible()) {
			Matrix m = new Matrix();
			if (!noRotation)
				m.postRotate((float) Math.toDegrees(angle), width * 0.5f, height * 0.5f);
			Vec2 translate = getBitmapDrawingXY();
			m.postTranslate(translate.x, translate.y);
			// canvas.drawColor(Color.TRANSPARENT);
			canvas.drawBitmap(bmpFrame, m, null);
		}
	}

	public boolean isCollision(float x2, float y2) {
		Vec2 click = fromScreen(x2, y2);
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

	public void applyForce(FreeSprite sprite, Vec2 positionSrc, float FIELD_RADIUS, float pullG, Vec2 force) {
		Body body = sprite.getBody();
		Vec2 positionTarget = body.getPosition();
		float range = spacing(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
		if (range <= FIELD_RADIUS) {
			force.normalize(); // force direction always point to source
			force.set(force.mul(body.getMass() * pullG));
			body.applyForce(force, body.getWorldCenter());
			System.out.println("applyForce:" + force);
		}
	}

	public void applyForce(FreeSprite sprite, Vec2 positionSrc, float FIELD_RADIUS, float pullG) {
		Body body = sprite.getBody();
		Vec2 positionTarget = body.getPosition();
		float range = spacing(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
		if (range <= FIELD_RADIUS) {
			Vec2 force = new Vec2(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
			force.normalize(); // force direction always point to source
			force.set(force.mul(body.getMass() * pullG * (FIELD_RADIUS - range) / FIELD_RADIUS));
			body.applyForce(force, body.getWorldCenter());
		}
	}

	public boolean isVisible() {
		return !invisible;
	}

	public void makeInvisible() {
		invisible = true;
	}

	public void makeVisible() {
		invisible = false;
	}

	public void makeFades() {
		fades = true;
	}

	public boolean readyToExplode() {
		return FADE_LIFE == 0;
	}

	public float getWidthPhysical() {
		return width / Constants.pixelpermeter;
	}

	public float getHeightPhysical() {
		return height / Constants.pixelpermeter;
	}

	public void setDensity(float d) {
		getBody().getShapeList().m_density = d;
	}

	public void setFriction(float f) {
		getBody().getShapeList().m_friction = f;
	}

	public void setRestitution(float r) {
		getBody().getShapeList().m_restitution = r;
	}

}
