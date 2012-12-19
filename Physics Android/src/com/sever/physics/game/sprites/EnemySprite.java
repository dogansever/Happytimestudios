package com.sever.physics.game.sprites;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.graphics.Canvas;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;
import com.sever.physics.game.utils.WeaponTypes;

public class EnemySprite extends ActiveSprite {

	private static final int BULLET_FIRE_WAIT_TIME_MAX = 50;
	public boolean powerOn;
	public boolean scatter;
	public boolean fueling;
	public int fuel;
	public int fuel_AGG = +1;
	public final int fuel_MAX = 100;
	WeaponTypes wt;

	public EnemySprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
		this.facingRigth = gameView.getPlayerSprite().x > x;
		this.BULLET_FIRE_WAIT_TIME = BULLET_FIRE_WAIT_TIME_MAX;
		FADE_LIFE = 50;
		this.spriteList = spriteList;
		addSprite(x, y);
		addLifeBarSprite();
		velocity_MAX = 30;
	}

	public void activateBomb() {
		wt = WeaponTypes.BOMB_BIG;
	}

	public void activateMissile() {
		wt = WeaponTypes.MISSILE;
	}

	public void fireMissile() {
		FreeSprite bullet = gameView.addMissile(x + (facingRigth ? 1 : -1) * width * 0.9f, y + height * 0.9f);
		firePower = firePower_MAX;
		bullet.getBody().setLinearVelocity(getVelocityVec(fireMultiplierMissile, gameView.getPlayerSprite()));
		// bullet.shiftLockOnME();
	}

	public void fireGrenade() {
		float yt = (getBody().getLinearVelocity().y < 0 ? 1 : -1) * height * 0.75f;
		float xt = (facingRigth ? 1 : -1) * width * 0.75f;
		FreeSprite bullet = gameView.addGrenade(x + xt, y + yt);
		// applyForce(bullet,
		// gameView.getPlayerSprite().getBody().getPosition(), 500);
		Vec2 v = getFireVec();
		bullet.getBody().setLinearVelocity(v);
	}

	private Vec2 getFireVec() {
		float VELX;
		float VELY;
		Vec2 v;
		try {
			// Vec2 positionSrc = getBody().getPosition();
			// Vec2 positionTarget =
			// gameView.getPlayerSprite().getBody().getPosition();
			// float VelCurx = getBody().getLinearVelocity().x;
			// float VelCury = getBody().getLinearVelocity().y;
			float VelPlayerx = gameView.getPlayerSprite().getBody().getLinearVelocity().x;
			float VelPlayery = gameView.getPlayerSprite().getBody().getLinearVelocity().y;

			VELX = (gameView.getPlayerSprite().x - this.x) / (Constants.pixelpermeter * 10);
			VELY = (gameView.getPlayerSprite().y - this.y) / (Constants.pixelpermeter * 10);
			// VELY = VELY * 3;
			// VELX += VELX * new Random().nextFloat();
			// System.out.println("VELX:" + VELX + ",VELY:" + VELY);
			// System.out.println("VelPlayerx:" + VelPlayerx + ",VelPlayery:" +
			// VelPlayery);
			v = new Vec2(VELX + VelPlayerx, VELY + VelPlayery);
		} catch (Exception e) {
			e.printStackTrace();
			VELX = 30;
			VELY = 30;
			v = new Vec2((facingRigth ? 1 : -1) * VELX, VELY);
		}
		// System.out.println("Vec2 x:" + v.x + ",y:" + v.y);
		return v;
	}

	void addSprite(float x, float y) {
		createDynamicBody(x, y);
		createShape();
	}

	public void onDraw(Canvas canvas) {
		if (life <= 0) {
			explodeAndDie();
		} else {

			BULLET_FIRE_WAIT_TIME--;
			if (BULLET_FIRE_WAIT_TIME == 0) {
				BULLET_FIRE_WAIT_TIME = BULLET_FIRE_WAIT_TIME_MAX + new Random().nextInt(BULLET_FIRE_WAIT_TIME_MAX);
				fire();
			}
			moveToPlayer();
			lifeBarSprite.onDraw(canvas);
		}
		// throttleLeave();
		super.onDraw(canvas);
	}

	private void fire() {
		if (gameView.idle)
			return;

		if (wt == WeaponTypes.MISSILE) {
			fireMissile();
		} else if (wt == WeaponTypes.BOMB_BIG) {
			fireGrenade();
		} else {
			fireMissile();
		}
	}

	private void moveToPlayer() {
		// if (gameView.idle) {
		// return;
		// }

		float minDistanceBetween = 200;
		float targetx = gameView.getPlayerSprite().x;
		float targety = gameView.getPlayerSprite().y;
		// System.out.println("targetx:" + targetx + ",x:" + x + "    targety:"
		// + targety + ",y:" + y);

		// if player is higher fly
		if (targety - y > minDistanceBetween) {
			throttleUp();
		} else if (targety - y < -minDistanceBetween) {
			throttleDown();
		}

		// move to player
		if (targetx - x > minDistanceBetween) {
			throttleRight();
		} else if (targetx - x < -minDistanceBetween) {
			throttleLeft();
		}

	}

	public boolean throttleHold() {
		// if (fueling) {
		// if (fuel == fuel_MAX) {
		// fueling = false;
		// } else {
		// return false;
		// }
		// }
		//
		// fuel_AGG = -1;
		// fuel = fuel + fuel_AGG;
		// if (fuel <= 0) {
		// fuel = 0;
		// fueling = true;
		// throttleOffBmp();
		// return false;
		// }
		return true;
	}

	public void throttleLeave() {
		throttleOffBmp();
		fuel_AGG = 3;
		fuel = fuel + fuel_AGG;
		if (fuel >= fuel_MAX) {
			fuel = fuel_MAX;
			return;
		}
	}

	public void throttlexBmp() {
		throttleBmp();
		spriteBmp.currentRow = 0;
	}

	public void throttleyBmp() {
		throttleBmp();
		spriteBmp.currentRow = 1;
	}

	public void throttleBmp() {
		spriteBmp.setBmpIndex(1);
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
	}

	public void throttleOffBmp() {
		spriteBmp.setBmpIndex(0);
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		spriteBmp.currentRow = 0;
	}

	public void createShape() {
		CircleDef circle = new CircleDef();
		circle.radius = getWidthPhysical() * 0.5f;
		circle.friction = 1.0f;// zero being completely frictionless
		circle.restitution = 0.2f;// zero being not bounce at all
		circle.density = 10.0f;

		PolygonDef playerDef = new PolygonDef();
		playerDef.setAsBox(getWidthPhysical() * 0.5f, getHeightPhysical() * 0.5f);
		playerDef.friction = 1.0f;
		playerDef.restitution = 0.2f;
		playerDef.density = 10.0f;

		// Assign shape to Body
		getBody().createShape(playerDef);

		// getBody().createShape(circle);
		getBody().setMassFromShapes();
		getBody().setBullet(true);

	}

	public void push(FreeSprite sprite) {
		sprite.makeVisible();
		spriteBmp.currentRow = 0;
		spriteBmp.currentFrame = 0;
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
		spriteBmp.currentRow = 1;
		spriteBmp.currentFrame = 0;
		if (range <= CLOSE_FIELD_RADIUS) {
			// sprite.makeInvisible();
			body.setAngularVelocity(0);
			body.setLinearVelocity(new Vec2(0, 0));
		} else if (sprite.isVisible()) {
			applyForce(sprite, positionSrc, FIELD_RADIUS, Constants.gravityPullEnemy);
		}
	}

	public WeaponTypes getWt() {
		return wt;
	}

	public void setWt(WeaponTypes wt) {
		this.wt = wt;
	}

	
}
