package com.sever.physics.game.sprites;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.graphics.Canvas;

import com.sever.physic.PhysicsActivity;
import com.sever.physic.PhysicsApplication;
import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;
import com.sever.physics.game.utils.Weapon;
import com.sever.physics.game.utils.WeaponTypes;
import com.sever.physics.game.utils.WeaponsManager;

public class PlayerSprite extends ActiveSprite {

	public static Weapon weapon = WeaponsManager.getManager().firstAvailableWeapon();
	public boolean hoverOn;
	public boolean powerOn;
	public boolean powerPush;
	public boolean scatter;
	public boolean alive = true;
	public FreeSprite sprite;
	public static int loadingTimeInFPS;
	public static int portalTimeInFPS;
	public static int portalTimeInFPSMax = Constants.FPS * 20;

	public PlayerSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
		this.spriteList = spriteList;
		fuel = fuel_MAX;
		alive = true;
		addSprite(x, y);
		addLifeBarSprite();
		addFuelBar();
		addPowerBar();
		addFireArrow();
		FADE_LIFE = 80;
		velocity_MAX = 80;
	}

	public float getBonusScoreLife() {
		return life;
	}

	public void collectBonusLife() {
		life += ((BonusLifeBarSprite) gameView.getBonusLifeBarSprite()).getBonusLifePercentage() * life_MAX;
		life = life > life_MAX ? life_MAX : life;
	}

	public WeaponTypes getWt() {
		wt = weapon.getType();
		return wt;
	}

	public void restartSprite(float x, float y) {
		PhysicsActivity.mWorld.bodies.remove(body);
		PhysicsActivity.mWorld.world.destroyBody(body);
		destroyShape();
		life = life_MAX;
		this.x = x;
		this.y = y;
		noPositionUpdate = false;
		weapon = WeaponsManager.getManager().firstAvailableWeapon();
		addSprite(x, y);
		gameView.resumeIdleGame();
		this.shiftLockOnME();
	}

	public void portalSprite() {
		PhysicsActivity.mWorld.bodies.remove(body);
		PhysicsActivity.mWorld.world.destroyBody(body);
		destroyShape();
		this.x = Constants.upperBoundxScreen - x;
		this.y = Constants.upperBoundyScreen - y;
		noPositionUpdate = false;
		addSprite(x, y);
		this.shiftLockOnME();
		portalTimeInFPS = portalTimeInFPSMax;
	}

	public void onDraw(Canvas canvas) {
		if (!gameView.idle && life <= 0) {
			((PlayerSprite) this).setAlive(false);
			explodeBmp();
			// killSprite();
			return;
		}

		if (!gameView.idle) {
			super.onDraw(canvas);
			lifeBarSprite.onDraw(canvas);
//			fuelBarSprite.onDraw(canvas);
			if (!WeaponsManager.getManager().getWeaponByType(weapon.getType()).fireAtMaxSpeed){				
				powerBarSprite.onDraw(canvas);
			}
			fireArrowSprite.onDraw(canvas);
			shiftCheck();
			hoverCheck();
			fireTry();
			if (loadingTimeInFPS > 0) {
				loadingTimeInFPS--;
			}
			if (portalTimeInFPS > 0) {
				portalTimeInFPS--;
			}
		}
		if (gameView.idle && fades && FADE_LIFE > 0) {
			super.onDraw(canvas);
		}

	}

	private void hoverCheck() {
		if (!hoverOn) {
			throttleLeave();
		} else if (getBody().getLinearVelocity().y < -5)
			throttle(0, 1.5f);
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

	private void shiftCheck() {
		if (sprite == null)
			shiftLockOnME();

		// System.out.println("shiftCheck starting...x:" + this.x);
		if (sprite.x > PhysicsApplication.deviceWidth * 0.5) {
			Constants.extraWidthOffset = sprite.x - PhysicsApplication.deviceWidth * 0.5f;
			if (Constants.extraWidthOffset < 0)
				Constants.extraWidthOffset = 0;
			if (Constants.extraWidthOffset > Constants.extraWidth)
				Constants.extraWidthOffset = Constants.extraWidth;
		} else {
			Constants.extraWidthOffset = 0;
		}
		if (sprite.y > PhysicsApplication.deviceHeight * 0.5) {
			Constants.extraHeightOffset = sprite.y - PhysicsApplication.deviceHeight * 0.5f;
			if (Constants.extraHeightOffset < 0)
				Constants.extraHeightOffset = 0;
			if (Constants.extraHeightOffset > Constants.extraHeight)
				Constants.extraHeightOffset = Constants.extraHeight;
		} else {
			Constants.extraHeightOffset = 0;
		}
		// System.out.println("shiftCheck ending...x:" + this.x +
		// ",extraWidthOffset:" + Constants.extraWidthOffset);
		// System.out.println("shiftCheck ending...y:" + this.y +
		// ",extraHeightOffset:" + Constants.extraHeightOffset);
	}

	public boolean throttleHold() {
//		fuel_AGG = -1;
//		fuel = fuel + fuel_AGG;
//		if (fuel <= 0) {
//			fuel = 0;
//			throttleOffBmp();
//			return false;
//		}
		return true;
	}

	public void throttleLeave() {
		throttleOffBmp();
//		fuel_AGG = 5;
//		fuel = fuel + fuel_AGG;
//		if (fuel >= fuel_MAX) {
//			fuel = fuel_MAX;
//			return;
//		}
	}

	public void fireHold() {
		if (WeaponsManager.getManager().getWeaponByType(weapon.getType()).fireAtMaxSpeed) {
			firePower = firePower_MAX;
			gameView.getPowerBarSprite().makeInvisible();
			return;
		}

		gameView.getPowerBarSprite().makeVisible();
		firePower += firePower_AGG;
		if (firePower >= firePower_MAX) {
			firePower = firePower_MAX;
		}
	}

	public void fireStart() {
		if (!WeaponsManager.getManager().getWeaponByType(weapon.getType()).automatic) {
			fire();
		} else {
			triggerOn = true;
			loadingTimeInFPS = 0;
		}
	}

	public void fireCease() {
		triggerOn = false;
	}

	public float getLoadingTimePercentage() {
		return 1.0f - ((float) loadingTimeInFPS) / ((float) WeaponsManager.getManager().getWeaponByType(weapon.getType()).loadingTimeInFPS);
	}

	public float getPortalTimePercentage() {
		return 1.0f - ((float) portalTimeInFPS) / ((float) portalTimeInFPSMax);
	}

	public void fireTry() {
		if (triggerOn && loadingTimeInFPS-- <= 0) {
			fire();
			loadingTimeInFPS = WeaponsManager.getManager().getWeaponByType(weapon.getType()).loadingTimeInFPS;
		}
	}

	public void fire() {
		if (gameView.idle) {
			return;
		}
		if (loadingTimeInFPS > 0) {
			return;
		} else {
			loadingTimeInFPS = WeaponsManager.getManager().getWeaponByType(weapon.getType()).loadingTimeInFPS;
		}

		if (weapon.getType() == WeaponTypes.BULLET) {
			fireBullet();
		} else if (weapon.getType() == WeaponTypes.BOMB_BIG) {
			fireGrenade();
		} else if (weapon.getType() == WeaponTypes.BOMB) {
			fireGrenadeSmall();
		} else if (weapon.getType() == WeaponTypes.MISSILE) {
			fireMissile();
		} else if (weapon.getType() == WeaponTypes.MISSILE_LIGHT) {
			fireMissileLight();
		} else if (weapon.getType() == WeaponTypes.BOMB_TRIPLE) {
			firePowerOld = firePower;
			fireGrenadeTripleThread();
			// fireGrenadeTriple();
		} else if (weapon.getType() == WeaponTypes.BOMB_IMPLODING) {
			fireGrenadeImploding();
		}
		firePower = 0;
		firePower_AGG = 1;
		gameView.getPowerBarSprite().makeInvisible();
	}

	public void fireGrenadeImploding() {
		try {
			FreeSprite bullet = gameView.addGrenadeImploding(x + (facingRigth ? 1 : -1) * width * 0.9f, y + height * 0.0f);
			bullet.getBody().setLinearVelocity(getVelocityVec(fireMultiplierBomb));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireGrenadeTriple() {
		try {
			FreeSprite bullet = gameView.addGrenadeTriple(x + (facingRigth ? 1 : -1) * width * 0.9f, y + height * 0.0f);
			bullet.getBody().setLinearVelocity(getVelocityVec(fireMultiplierBomb, firePowerOld));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireMissile() {
		try {
			FreeSprite bullet = gameView.addMissile(x + (facingRigth ? 1 : -1) * width * 0.9f, y + height * 0.0f, facingRigth);
			bullet.setAngle((float) Math.toRadians(!facingRigth ? fireArrowSprite.getAngle() : 360 - fireArrowSprite.getAngle()));
			bullet.getBody().setLinearVelocity(getVelocityVec(fireMultiplierMissile));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// bullet.shiftLockOnME();
	}

	public void fireMissileLight() {
		try {
			FreeSprite bullet = gameView.addMissileLight(x + (facingRigth ? 1 : -1) * width * 0.9f, y + height * 0.0f, facingRigth);
			bullet.setAngle((float) Math.toRadians(!facingRigth ? fireArrowSprite.getAngle() : 360 - fireArrowSprite.getAngle()));
			bullet.getBody().setLinearVelocity(getVelocityVec(fireMultiplierMissile));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// bullet.shiftLockOnME();
	}

	public void fireGrenadeSmall() {
		try {
			FreeSprite bullet = gameView.addGrenadeSmall(x + (facingRigth ? 1 : -1) * width * 0.9f, y + height * 0.0f);
			bullet.getBody().setLinearVelocity(getVelocityVec(fireMultiplierBomb));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireGrenade() {
		try {
			FreeSprite bullet = gameView.addGrenade(x + (facingRigth ? 1 : -1) * width * 0.9f, y + height * 0.0f);
			bullet.getBody().setLinearVelocity(getVelocityVec(fireMultiplierBomb));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireBullet() {
		try {
			FreeSprite bullet = gameView.addBullet(x + (!facingRigth ? 1 : -1) * width * 1.0f, y);
			bullet.getBody().setLinearVelocity(new Vec2(0, 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		circle.density = 10.0f;

		PolygonDef playerDef = new PolygonDef();
		playerDef.setAsBox(getWidthPhysical() * 0.5f, getHeightPhysical() * 0.5f);
		playerDef.friction = 1.0f;
		playerDef.restitution = 0.2f;
		playerDef.density = 10.0f;

		// Assign shape to Body
		// getBody().createShape(playerDef);

		getBody().createShape(circle);
		getBody().setMassFromShapes();
		getBody().setBullet(true);

	}

	public void pushPower(FreeSprite sprite) {
		sprite.makeVisible();
		Body body = sprite.getBody();
		body.setAngularVelocity(0);
		body.setLinearVelocity(new Vec2(0, 0));
		Vec2 positionTarget = body.getPosition();
		Vec2 positionSrc = this.getBody().getPosition();
		Vec2 force = new Vec2(positionTarget.x - positionSrc.x, positionTarget.y - positionSrc.y);
		force.normalize(); // force direction always point to source
		force.set(force.mul((float) (body.getMass() * Constants.gravityPushPlayer)));
		body.applyImpulse(force, body.getWorldCenter());
	}

	public void push(FreeSprite sprite) {
		sprite.makeVisible();
		float FIELD_RADIUS = getWidthPhysical() * 2.0f;
		Vec2 positionTarget = sprite.getBody().getPosition();
		Vec2 positionSrc = this.getBody().getPosition();
		float range = spacing(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
		if (range <= FIELD_RADIUS) {
			Body body = sprite.getBody();
			body.setAngularVelocity(0);
			body.setLinearVelocity(new Vec2(0, 0));
			Vec2 force = new Vec2((facingRigth ? 1 : -1) * 1.0f, 0.0f);
			force.normalize(); // force direction always point to source
			force.set(force.mul((float) (body.getMass() * Constants.gravityPushPlayer)));
			body.applyImpulse(force, body.getWorldCenter());
		}

		// applyForce(sprite.getBody(), positionSrc, FIELD_RADIUS,
		// Constants.gravityPushPlayer, force);
	}

	public void pull(FreeSprite sprite) {
		float FIELD_RADIUS = Constants.gravityPullFieldRadiusPlayer;
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
		if (range <= CLOSE_FIELD_RADIUS) {
			// sprite.makeInvisible();
			body.setAngularVelocity(0);
			body.setLinearVelocity(new Vec2(0, 0));
		} else if (sprite.isVisible()) {
			applyForce(sprite, positionSrc, FIELD_RADIUS, Constants.gravityPullPlayer);
		}
	}

	public boolean hoverOnOff() {
		hoverOn = !hoverOn;
		return hoverOn;
	}

	public void powerPush() {
		powerPush = true;
	}

	public void powerUp() {
		spriteBmp.currentRow = 1;
		spriteBmp.currentFrame = 0;
		powerOn = true;
		scatter = true;
	}

	public void powerDown() {
		spriteBmp.currentRow = 0;
		spriteBmp.currentFrame = 0;
		powerOn = false;
	}

	protected void fireGrenadeTripleThread() {
		PhysicsActivity.context.count = 0;
		final Runnable updatePercentage = new Runnable() {
			public void run() {
				if (PhysicsActivity.context.count++ < 3) {
					fireGrenadeTriple();
				} else {
					PhysicsActivity.context.timer.cancel();
				}
			}
		};
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				updatePercentage.run();
				// PhysicsActivity.context.mHandler.post(updatePercentage);
			}
		};
		PhysicsActivity.context.timer = new Timer();
		PhysicsActivity.context.timer.schedule(task, new Date(), 200);
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	@Override
	public void throttle(int direction, float... f) {
		if (gameView.idle) {
			return;
		}
		super.throttle(direction, f);
	}

}
