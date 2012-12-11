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
import android.os.Handler;

import com.sever.physic.PhysicsActivity;
import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;
import com.sever.physics.game.utils.Weapon;
import com.sever.physics.game.utils.WeaponTypes;
import com.sever.physics.game.utils.WeaponsManager;

public class PlayerSprite extends FreeSprite {

	public Weapon weapon = WeaponsManager.getManager().firstWeapon();
	public boolean powerOn;
	public boolean powerPush;
	public boolean scatter;
	public int fuel;
	public int fuel_AGG = +1;
	public final int fuel_MAX = 100;
	public int firePower;
	public int firePowerOld;
	public int firePower_AGG = +1;
	public final int firePower_MAX = 50;
	public final int fireMultiplierBullet = 150;
	public final int fireMultiplierBomb = 50;

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
		addSprite(x, y);
	}

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		throttleLeave();
	}

	public boolean throttleHold() {
		if (fuel == 0) {
			return false;
		}
		fuel_AGG = -1;
		fuel = fuel + fuel_AGG;
		return true;
	}

	public void throttleLeave() {
		if (fuel == fuel_MAX) {
			return;
		}
		fuel_AGG = 1;
		fuel = fuel + fuel_AGG;
	}

	public void fireHold() {
		gameView.getPowerBarSprite().makeVisible();
		firePower += firePower_AGG;
		if (firePower >= firePower_MAX) {
			firePower = firePower_MAX;
		}
		// firePower = firePower + firePower_AGG % firePower_MAX;
		// if (firePower >= firePower_MAX) {
		// firePower_AGG = -1;
		// } else if (firePower == 0) {
		// firePower_AGG = 1;
		// }
	}

	public void fire() {
		if (weapon.getType() == WeaponTypes.BULLET) {
			fireBullet();
		} else if (weapon.getType() == WeaponTypes.BOMB_BIG) {
			fireGrenade();
		} else if (weapon.getType() == WeaponTypes.BOMB) {
			fireGrenadeSmall();
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
		FreeSprite bullet = gameView.addGrenadeImploding(x + (facingRigth ? 1 : -1) * width * 0.9f, y + height * 0.9f);
		bullet.getBody().setLinearVelocity(getVelocityVec(fireMultiplierBomb));
	}

	public void fireGrenadeTriple() {
		try {
			FreeSprite bullet = gameView.addGrenadeTriple(x + (facingRigth ? 1 : -1) * width * 0.9f, y + height * 0.9f);
			bullet.getBody().setLinearVelocity(getVelocityVec(fireMultiplierBomb, firePowerOld));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireGrenadeSmall() {
		FreeSprite bullet = gameView.addGrenadeSmall(x + (facingRigth ? 1 : -1) * width * 0.9f, y + height * 0.9f);
		bullet.getBody().setLinearVelocity(getVelocityVec(fireMultiplierBomb));
	}

	public void fireGrenade() {
		FreeSprite bullet = gameView.addGrenade(x + (facingRigth ? 1 : -1) * width * 0.9f, y + height * 0.9f);
		bullet.getBody().setLinearVelocity(getVelocityVec(fireMultiplierBomb));
	}

	public void fireBullet() {
		FreeSprite bullet = gameView.addBullet(x + (facingRigth ? 1 : -1) * width * 0.9f, y);
		bullet.getBody().setLinearVelocity(getVelocityVec(fireMultiplierBullet));
	}

	private Vec2 getVelocityVec(int fireMultiplier) {
		float fangle = ((FireArrowSprite) gameView.getFireArrowSprite()).getAngle();
		Vec2 force = new Vec2((float) Math.cos(Math.toRadians(fangle)), (float) Math.sin(Math.toRadians(fangle)));
		force.normalize();

		float xt = (facingRigth ? 1 : -1) * (firePower * fireMultiplier / firePower_MAX) * force.x;
		float yt = (firePower * fireMultiplier / firePower_MAX) * force.y;
		Vec2 v = new Vec2(xt, yt);

		return v;
	}

	private Vec2 getVelocityVec(int fireMultiplier, int firePowerx) {
		float fangle = ((FireArrowSprite) gameView.getFireArrowSprite()).getAngle();
		Vec2 force = new Vec2((float) Math.cos(Math.toRadians(fangle)), (float) Math.sin(Math.toRadians(fangle)));
		force.normalize();

		float xt = (facingRigth ? 1 : -1) * (firePowerx * fireMultiplier / firePower_MAX) * force.x;
		float yt = (firePowerx * fireMultiplier / firePower_MAX) * force.y;
		Vec2 v = new Vec2(xt, yt);

		// System.out.println("force():(" + force.x + "," + force.y + ")");
		// System.out.println("getVelocityVec():(" + xt + "," + yt + ")");
		return v;
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
		getBody().createShape(playerDef);

		// getBody().createShape(circle);
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

	public void superJump() {
		throttle(0, 50);
	}

	public void throttleUp() {
		throttle(0);
	}

	public void throttleDown() {
		throttle(1);
	}

	public void throttleLeft() {
		facingRigth = false;
		throttle(2);
	}

	public void throttleRight() {
		facingRigth = true;
		throttle(3);
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

	public void throttle(int direction, int... jumpPower) {
		if (!throttleHold()) {
			return;
		}
		float jp = 1;
		if (jumpPower.length == 1) {
			jp = jumpPower[0];
		}

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
		force.set(force.mul((float) (body.getMass() * Constants.gravityThrottle * jp)));
		body.applyImpulse(force, body.getWorldCenter());

		checkVelocity();
		// System.out.println("!!!Kicked it!!!:" + index + ", force:x:" +
		// force.x + ", y:" + force.y);
	}

	private void checkVelocity() {
		float max = 50;
		System.out.println("getBody().getLinearVelocity():(x,y) (" + getBody().getLinearVelocity().x + "," + getBody().getLinearVelocity().y + ")");
		if (getBody().getLinearVelocity().x < -max) {
			getBody().setLinearVelocity(new Vec2(max, getBody().getLinearVelocity().y));
		} else if (getBody().getLinearVelocity().x > max) {
			getBody().setLinearVelocity(new Vec2(max, getBody().getLinearVelocity().y));
		}

		if (getBody().getLinearVelocity().y < -max) {
			getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x, max));
		} else if (getBody().getLinearVelocity().y > max) {
			getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x, max));
		}
		;
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
}
