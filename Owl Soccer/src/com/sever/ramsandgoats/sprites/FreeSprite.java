package com.sever.ramsandgoats.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.sever.ramsandgoats.GameApplication;
import com.sever.ramsandgoats.game.GameView;
import com.sever.ramsandgoats.util.Constants;
import com.sever.ramsandgoats.util.PhysicsWorldManager;
import com.sever.ramsandgoats.util.SpriteBmp;

public class FreeSprite {

	public int FADE_LIFE = 100;
	public GameView gameView;
	public float x = 0;
	public float y = 0;
	public float width;
	public float widthExplosion;
	public float height;
	public float angle;
	public boolean noRotation = false;
	public boolean invisible = false;
	public boolean noPositionUpdate = false;
	public boolean fades = false;
	public boolean explodes = false;
	public boolean implodes = false;
	public boolean facingRigth = false;
	public boolean manualFrameSet = false;
	public boolean manualAngleSet = false;
	public boolean dynamic;
	public boolean hasbody = false;
	public SpriteBmp spriteBmp;

	protected ConcurrentLinkedQueue<FreeSprite> spriteList;
	protected Body body;

	public void freeBitmaps() {
		if (spriteBmp != null)
			spriteBmp.freeBitmaps();
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
		dynamic = false;
		manualFrameSet = true;
		hasbody = true;
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x / Constants.pixelpermeter, y / Constants.pixelpermeter);
		this.body = PhysicsWorldManager.mWorld.world.createStaticBody(bodyDef);
		if (body != null)
			PhysicsWorldManager.mWorld.bodies.add(body);
	}

	public void killSprite() {
		// System.out.println("Killing:" + this);
		spriteList.remove(this);
		PhysicsWorldManager.mWorld.bodies.remove(body);
		PhysicsWorldManager.mWorld.world.destroyBody(body);
		destroyShape();
		freeBitmaps();
	}

	public void destroySprite() {
		spriteList.remove(this);
		PhysicsWorldManager.mWorld.bodies.remove(body);
		PhysicsWorldManager.mWorld.world.destroyBody(body);
		destroyShape();
		freeBitmaps();
	}

	public void createDynamicBody(float x, float y) {
		try {
			dynamic = true;
			hasbody = true;
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(x / Constants.pixelpermeter, y / Constants.pixelpermeter);
			this.body = PhysicsWorldManager.mWorld.world.createDynamicBody(bodyDef);
			if (body != null)
				PhysicsWorldManager.mWorld.bodies.add(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void destroyShape() {
		if (body != null && body.getShapeList() != null)
			body.destroyShape(getBody().getShapeList());
	}

	// public void shiftLockOnME() {
	// // System.out.println("shiftLockOnME:" + this);
	// if (gameView.getPlayerSprite() != null)
	// ((PlayerSprite) gameView.getPlayerSprite()).sprite = this;
	// }

	// public void releaseShiftLockOnMe() {
	// // System.out.println("releaseShiftLockOnMe:");
	// if (gameView.getPlayerSprite() != null)
	// ((PlayerSprite) gameView.getPlayerSprite()).sprite = null;
	// }

	public Body getBody() {
		return body;
	}

	private void updateBitmapIfNecessary() {
		try {
			if (!manualFrameSet && ++spriteBmp.BMP_FPS_CURRENT % spriteBmp.BMP_FPS == 0) {
				spriteBmp.BMP_FPS_CURRENT = 0;
				spriteBmp.currentFrame = ++spriteBmp.currentFrame % spriteBmp.BMP_COLUMNS;
				
			}

			if (!manualFrameSet || spriteBmp.bmpFrame == null) {
				int srcX = (int) (spriteBmp.currentFrame * width);
				int srcY = (int) (spriteBmp.currentRow * height);
				Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
				Rect dst = new Rect((int) (x), (int) (y), (int) (x + width), (int) (y + height));
				Paint p = new Paint();
				spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.getBitmap(), src.left, src.top, (int) width, (int) height);
			}
			
			if (!spriteBmp.repeat && spriteBmp.currentFrame == spriteBmp.BMP_COLUMNS - 1) {
				spriteBmp.repeat = true;
				spriteBmp.setBmpIndex(0);
				this.width = spriteBmp.getWidth();
				this.height = spriteBmp.getHeight();
				spriteBmp.currentRow = 0;
				spriteBmp.currentFrame = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// bmpFrame = bmp;
	}

	private void updatePosition() {
		Body body = getBody();
		if (body != null) {
			this.x = body.getPosition().x * Constants.pixelpermeter;
			this.y = body.getPosition().y * Constants.pixelpermeter;
			if (manualAngleSet) {
			} else {
				this.angle = body.getAngle();
			}
		}
	}

	public void onDraw(Canvas canvas) {
		try {
			if (noPositionUpdate) {
			} else {
				updatePosition();
				if (checkOutOfBounds()) {
					killSprite();
					return;
				}
			}

			updateBitmapIfNecessary();

			stabilizeVelocity();
			if (fades && FADE_LIFE >= 0) {
				if (FADE_LIFE-- == 0) {
					killSprite();
					return;
				}
			}

			if (spriteBmp.getBitmap() != null && isVisible()) {
				Matrix m = new Matrix();

				if (noRotation) {
				} else {
					m.postRotate((float) Math.toDegrees(angle), width * 0.5f, height * 0.5f);
				}

				Vec2 translate = getBitmapDrawingXY();

				if (Constants.checkForQuake()) {
					m.postTranslate(translate.x - Constants.extraWidthOffset + Constants.getQuakePower(), translate.y + Constants.extraHeightOffset + Constants.getQuakePower());
				} else {
					Constants.endQuake();
					m.postTranslate(translate.x - Constants.extraWidthOffset, translate.y + Constants.extraHeightOffset);
				}
				// canvas.drawColor(Color.TRANSPARENT);
				if (facingRigth) {
					Matrix mirrorMatrix = new Matrix();
					mirrorMatrix.preScale(-1.0f, 1.0f);
					spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.bmpFrame, 0, 0, spriteBmp.bmpFrame.getWidth(), spriteBmp.bmpFrame.getHeight(), mirrorMatrix, false);
				}

				canvas.drawBitmap(spriteBmp.bmpFrame, m, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stabilizeVelocity() {
		// TODO Auto-generated method stub

	}

	public boolean checkOutOfBounds() {
		return body != null && (this.x < 0 - width || this.x > Constants.upperBoundxScreen + width || this.y > Constants.upperBoundyScreen * 1.1 || this.y < 0 - width);
	}

	public boolean isCollision(float x2, float y2) {
		Vec2 click = fromScreen(x2, y2);
		return click.x > x - width * 0.5f && click.x < x + width * 0.5f && click.y > y - height * 0.5f && click.y < y + height * 0.5f;
	}

	public Vec2 fromScreen(float x2, float y2) {
		Vec2 pos = new Vec2();
		pos.x = x2;
		pos.y = GameApplication.deviceHeight - y2;
		return pos;
	}

	public Vec2 getBitmapDrawingXY() {
		Vec2 pos = new Vec2();
		pos.x = x - width * 0.5f;
		pos.y = GameApplication.deviceHeight - y - height * 0.5f;
		return pos;
	}

	public float spacing(float x, float y) {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float getDistanceFrom(FreeSprite sprite) {
		Body body = sprite.getBody();
		Vec2 positionTarget = body.getPosition();
		Vec2 positionSrc = this.getBody().getPosition();
		float range = spacing(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
		return range;
	}

	public void applyForce2Sprite(FreeSprite sprite, Vec2 positionSrc, float FIELD_RADIUS, float pullG, Vec2 force) {
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

	public boolean applyForce(FreeSprite sprite, Vec2 positionSrc, float FIELD_RADIUS, float pullG) {
		Body body = sprite.getBody();
		Vec2 positionTarget = body.getPosition();
		float range = spacing(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
		if (range <= FIELD_RADIUS) {
			Vec2 force = new Vec2(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
			force.normalize(); // force direction always point to source
			force.set(force.mul(body.getMass() * pullG * (FIELD_RADIUS - range) / FIELD_RADIUS));
			body.applyForce(force, body.getWorldCenter());
			return true;
		}
		return false;
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

	public void aimAt(FreeSprite target) {
		float tx = target.x - x;
		float ty = target.y - y;
		angle = (float) Math.atan2(ty, tx);
		if (facingRigth)
			angle = (float) Math.toRadians(360 - Math.toDegrees(angle));
		else
			angle = (float) Math.toRadians(180 - Math.toDegrees(angle));

	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public void addPointerSprite() {
	}

	public boolean isManualFrameSet() {
		return manualFrameSet;
	}

	public void setManualFrameSet(boolean manualFrameSet) {
		this.manualFrameSet = manualFrameSet;
	}
}
