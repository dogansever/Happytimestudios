package com.sever.physics.game.sprites;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.graphics.Bitmap;

import com.sever.physic.PhysicsActivity;
import com.sever.physics.game.utils.BitmapManager;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SoundEffectsManager;
import com.sever.physics.game.utils.SpriteBmp;
import com.sever.physics.game.utils.WeaponTypes;
import com.sever.physics.game.utils.WeaponsManager;

public class ActiveSprite extends FreeSprite {

	public int velocity_MAX = 50;
	public float frictionConstant = 0.85f;//1.0f no friction
	public int life_AGG = +1;
	public int life_MAX = 100;
	public int life = life_MAX;
	public LifeBarSprite lifeBarSprite;
	public FuelBarSprite fuelBarSprite;
	public PowerBarSprite powerBarSprite;
	public FireArrowSprite fireArrowSprite;

	public int fuel;
	public int fuel_AGG = +1;
	public final int fuel_MAX = 100;
	public int firePower;
	public int firePowerOld;
	public int firePower_AGG = +1;
	public final int firePower_MAX = 50;
	public final int fireMultiplierBullet = 150;
	public static final int fireMultiplierMissile = 180;
	public static final int fireMultiplierMissileLocking = 120;
	public final int fireMultiplierBomb = 80;

	public boolean triggerOn;
	public boolean freeFalling;
	public WeaponTypes wt;
	private boolean fly;

	public int smokeFreqMAXInFPS = (int) (Constants.FPS * 0.5f);
	public int smokeFreqInFPS = smokeFreqMAXInFPS;

	public void setFly(boolean fly) {
		this.fly = fly;
	}

	public boolean getFly() {
		return fly;
	}

	public void freefallAndExplodeAndDie() {
		freeFallForAWhile();
		if (!freeFalling)
			explodeBmp();
	}

	private void freeFallForAWhile() {
		if (fades)
			return;

		if (!freeFalling) {
			FADE_LIFE = 50;
			getBody().setLinearVelocity(new Vec2(0, 0));
			spriteBmp.setBmpIndex(3);
			this.width = spriteBmp.getWidth();
			this.height = spriteBmp.getHeight();
			SoundEffectsManager.getManager().playEXPLODE_ROBOT(PhysicsActivity.context);
		}

		if (FADE_LIFE-- <= 0) {
			freeFalling = false;
		} else {
			freeFalling = true;
		}
	}

	public void explodeBmp() {
		if (!fades)
			FADE_LIFE = 50;
		fades = true;
		spriteBmp.setBmpIndex(2);
		noPositionUpdate = true;
		noRotation = true;
		facingRigth = false;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		angle = 180;
		destroyShape();
	}

	public void addFireArrow() {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.fireArrow);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 10, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		fireArrowSprite = new FireArrowSprite(this, gameView, spriteBmp, x, y);
	}

	public void addFuelBar() {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.fuelBar);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 10, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		fuelBarSprite = new FuelBarSprite(this, gameView, spriteBmp, x, y);
	}

	public void addPowerBar() {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.powerBar);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		powerBarSprite = new PowerBarSprite(this, gameView, spriteBmp, x, y);
	}

	public void addLifeBarSprite() {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.lifeBar);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		lifeBarSprite = new LifeBarSprite(this, gameView, spriteBmp, x, y);
	}

	public void lifeLost(int lost) {
		life -= lost;
	}

	public void lifeGain(int lost) {
		life += lost;
		life = life > life_MAX ? life_MAX : life;
		SoundEffectsManager.getManager().playPOWER_UP(PhysicsActivity.context);
	}

	public void checkVelocity() {
		float max = velocity_MAX;
		// System.out.println("getBody().getLinearVelocity():(x,y) (" +
		// getBody().getLinearVelocity().x + "," +
		// getBody().getLinearVelocity().y + ")");
		if (getBody().getLinearVelocity().x < -max) {
			getBody().setLinearVelocity(new Vec2(-max, getBody().getLinearVelocity().y));
		} else if (getBody().getLinearVelocity().x > max) {
			getBody().setLinearVelocity(new Vec2(max, getBody().getLinearVelocity().y));
		}

		if (getBody().getLinearVelocity().y < -max) {
			getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x, -max));
		} else if (getBody().getLinearVelocity().y > max) {
			getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x, max));
		}
		;
	}

	public void stabilizeVelocity() {
		getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x * frictionConstant, getBody().getLinearVelocity().y * frictionConstant));
	}

	public void throttle(int direction, float... f) {
		if (!throttleHold()) {
			return;
		}
		float jp = 1;
		if (f.length == 1) {
			jp = f[0];
		}

		Vec2 force = null;
		switch (direction) {
		case 0:
			// up
			force = new Vec2(0.0f, 1.0f);
			throttleyBmp();
			break;
		case 1:
			// down
			force = new Vec2(0.0f, -1.0f);
			// throttleyBmp();
			break;
		case 2:
			// left
			force = new Vec2(-1.0f, 0.0f);
			throttlexBmp();
			break;
		case 3:
			// right
			force = new Vec2(1.0f, 0.0f);
			throttlexBmp();
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

	public boolean throttleHold() {
		return false;
	}

	public void throttlexBmp() {
	}

	public void throttleyBmp() {
	}

	public void throttleBmp() {
	}

	public void throttleOffBmp() {
	}

	public void throttleUp() {
		if (!fly)
			return;
		throttle(0);
		if (smokeFreqInFPS-- == 0) {
			if (wt == WeaponTypes.BOSS3) {
				// gameView.addSmoke(x - width* 0.4f, y - height * 0.3f);
				// gameView.addSmoke(x + width* 0.4f, y - height * 0.3f);
				// gameView.addSmoke(x - width* 0.4f, y - height * 0.3f);
				// gameView.addSmoke(x + width* 0.4f, y - height * 0.3f);
			} else {
				gameView.addSmoke(x, y - height * 0.4f);
			}
			smokeFreqInFPS = smokeFreqMAXInFPS;
		}
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

	public boolean isMissileFacingRight(FreeSprite target) {
		Vec2 positionTarget = target.getBody().getPosition();
		Vec2 positionSrc = body.getPosition();
		return positionTarget.x > positionSrc.x;
	}

	public Vec2 getPositionVecNormalized(FreeSprite target) {
		Vec2 positionTarget = target.getBody().getPosition();
		Vec2 positionSrc = body.getPosition();
		Vec2 force = new Vec2(positionTarget.x - positionSrc.x, positionTarget.y - positionSrc.y);
		force.normalize();

		return force;
	}

	public Vec2 getVelocityVec(int fireMultiplier, FreeSprite target) {
		Vec2 positionTarget = target.getBody().getPosition();
		Vec2 positionSrc = body.getPosition();
		Vec2 force = new Vec2(positionTarget.x - positionSrc.x, positionTarget.y - positionSrc.y);
		force.normalize();

		firePower = WeaponsManager.getManager().getWeaponByType(getWt()).fireAtMaxSpeed ? firePower_MAX : firePower;
		float xt = (firePower * fireMultiplier / firePower_MAX) * force.x;
		float yt = (firePower * fireMultiplier / firePower_MAX) * force.y;
		Vec2 v = new Vec2(xt, yt);

		// System.out.println("getVelocityVec: x:" + v.x + ", y:" + v.y);
		return v;
	}

	public Vec2 getVelocityVecWithAngle(int fireMultiplier, float angleIn360) {
		float fangle = angleIn360;
		Vec2 force = new Vec2((float) Math.cos(Math.toRadians(fangle)), (float) Math.sin(Math.toRadians(fangle)));
		force.normalize();

		firePower = WeaponsManager.getManager().getWeaponByType(getWt()).fireAtMaxSpeed ? firePower_MAX : firePower;
		float xt = (PhysicsActivity.facingRigth ? 1 : -1) * (firePower * fireMultiplier / firePower_MAX) * force.x;
		float yt = (firePower * fireMultiplier / firePower_MAX) * force.y;
		Vec2 v = new Vec2(xt, yt);

		return v;
	}

	public Vec2 getVelocityVec(int fireMultiplier) {
		float fangle = ((FireArrowSprite) gameView.getFireArrowSprite()).getAngle();
		Vec2 force = new Vec2((float) Math.cos(Math.toRadians(fangle)), (float) Math.sin(Math.toRadians(fangle)));
		force.normalize();

		firePower = WeaponsManager.getManager().getWeaponByType(getWt()).fireAtMaxSpeed ? firePower_MAX : firePower;
		float xt = (facingRigth ? 1 : -1) * (firePower * fireMultiplier / firePower_MAX) * force.x;
		float yt = (firePower * fireMultiplier / firePower_MAX) * force.y;
		Vec2 v = new Vec2(xt, yt);

		return v;
	}

	public Vec2 getVelocityVec(int fireMultiplier, int firePowerx) {
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

	public WeaponTypes getWt() {
		return wt;
	}

}
