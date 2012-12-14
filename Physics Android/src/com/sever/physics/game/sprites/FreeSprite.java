package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.sever.physic.IntroActivity;
import com.sever.physic.PhysicsActivity;
import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;
import com.sever.physics.game.utils.WeaponTypes;

public class FreeSprite {

	public int FADE_LIFE = 100;
	public int BULLET_FIRE_WAIT_TIME = 100;
	public GameView gameView;

	public float x = 0;
	public float y = 0;
	public float width;
	public float widthExplosion;
	public float height;
	// public int index;
	public float angle;
	public boolean noRotation = false;
	public boolean invisible = false;
	public boolean noupdate = false;
	public boolean fades = false;
	public boolean explodes = false;
	public boolean implodes = false;
	public boolean facingRigth = false;
	public boolean manualFrameSet = false;
	public SpriteBmp spriteBmp;
	public WeaponTypes wt;

	protected ConcurrentLinkedQueue<FreeSprite> spriteList;
	protected Body body;

	public void freeBitmaps() {
		if (spriteBmp != null)
			spriteBmp.freeBitmaps();
	}

	public void fireGrenadeImploding() {
	}

	public void fireGrenade() {
	}

	public void fireBullet() {
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
		// System.out.println("Killing:" + this);
		spriteList.remove(this);
		PhysicsActivity.mWorld.bodies.remove(body);
		PhysicsActivity.mWorld.world.destroyBody(body);
		destroyShape();
		freeBitmaps();
	}

	public void createDynamicBody(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x / Constants.pixelpermeter, y / Constants.pixelpermeter);
		this.body = PhysicsActivity.mWorld.world.createDynamicBody(bodyDef);
		PhysicsActivity.mWorld.bodies.add(body);
	}

	public void destroyShape() {
		if (body != null && body.getShapeList() != null)
			body.destroyShape(getBody().getShapeList());
	}

	public Body getBody() {
		// if (body == null)
		// System.out.println("getBody():" + body + " " +
		// this.getClass().getName());
		return body;
		// int index = PhysicsActivity.mWorld.bodies.indexOf(body);
		// return PhysicsActivity.mWorld.bodies.get(index);
	}

	private void updateBitmap() {
		if (!manualFrameSet && ++spriteBmp.BMP_FPS_CURRENT % spriteBmp.BMP_FPS == 0)
			spriteBmp.currentFrame = ++spriteBmp.currentFrame % spriteBmp.BMP_COLUMNS;

		int srcX = (int) (spriteBmp.currentFrame * width);
		int srcY = (int) (spriteBmp.currentRow * height);
		Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
		Rect dst = new Rect((int) (x), (int) (y), (int) (x + width), (int) (y + height));
		Paint p = new Paint();
		spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.getBitmap(), src.left, src.top, (int) width, (int) height);
		// bmpFrame = bmp;
	}

	private void updatePosition() {
		Body body = getBody();
		if (body != null && !noupdate) {
			this.x = body.getPosition().x * Constants.pixelpermeter;
			this.y = body.getPosition().y * Constants.pixelpermeter;
			this.angle = body.getAngle();
		}
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
		updatePosition();
		if (checkOutOfBounds()) {
			killSprite();
			return;
		}
		updateBitmap();

		if (fades && FADE_LIFE >= 0) {
			if (FADE_LIFE-- == 0) {
				killSprite();
				return;
			}
		}
		if (spriteBmp.getBitmap() != null && isVisible()) {
			Matrix m = new Matrix();
			if (!noRotation)
				m.postRotate((float) Math.toDegrees(angle), width * 0.5f, height * 0.5f);
			Vec2 translate = getBitmapDrawingXY();
			m.postTranslate(translate.x, translate.y);
			// canvas.drawColor(Color.TRANSPARENT);
			if (facingRigth) {
				Matrix mirrorMatrix = new Matrix();
				mirrorMatrix.preScale(-1.0f, 1.0f);
				spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.bmpFrame, 0, 0, spriteBmp.bmpFrame.getWidth(), spriteBmp.bmpFrame.getHeight(), mirrorMatrix, false);
			}

			canvas.drawBitmap(spriteBmp.bmpFrame, m, null);
		}
	}

	public boolean checkOutOfBounds() {
		return this.x < 0 - width || this.x > Constants.upperBoundxScreen + width || this.y > Constants.upperBoundyScreen * 1.5 || this.y < 0 - width;
	}

	public boolean isCollision(float x2, float y2) {
		Vec2 click = fromScreen(x2, y2);
		return click.x > x - width * 0.5f && click.x < x + width * 0.5f && click.y > y - height * 0.5f && click.y < y + height * 0.5f;
	}

	public Vec2 fromScreen(float x2, float y2) {
		Vec2 pos = new Vec2();
		pos.x = x2;
		pos.y = IntroActivity.deviceHeight - y2;
		return pos;
	}

	public Vec2 getBitmapDrawingXY() {
		Vec2 pos = new Vec2();
		pos.x = x - width * 0.5f;
		pos.y = IntroActivity.deviceHeight - y - height * 0.5f;
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
			// System.out.println("applyForce:" + force);
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

	public void applyForce(FreeSprite sprite, Vec2 positionSrc, float pullG) {
		Body body = sprite.getBody();
		Vec2 positionTarget = body.getPosition();
		Vec2 force = new Vec2(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
		force.normalize(); // force direction always point to source
		force.set(force.mul(body.getMass() * pullG));
		body.applyForce(force, body.getWorldCenter());
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

	public void makeExplodes() {
		explodes = true;
	}

	public void explodeBmp() {

	}

	public boolean readyToExplode() {
		boolean ready = (FADE_LIFE == 0 && spriteBmp.bmpIndex == 0);
		if (ready) {
			explodeBmp();
		}
		return ready;
	}

	public float getWidthExplosionPhysical() {
		return widthExplosion / Constants.pixelpermeter;
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
