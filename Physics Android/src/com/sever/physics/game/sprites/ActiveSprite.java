package com.sever.physics.game.sprites;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.graphics.Bitmap;

import com.sever.physic.PhysicsActivity;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;

public class ActiveSprite extends FreeSprite {

	public int velocity_MAX = 50;
	public int life_AGG = +1;
	public final int life_MAX = 100;
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
	public final int fireMultiplierMissile = 150;
	public final int fireMultiplierBomb = 50;

	@Override
	public void explodeAndDie() {
		super.explodeAndDie();
		explodeBmp();
	}

	public void explodeBmp() {
		fades = true;
		spriteBmp.setBmpIndex(2);
		noPositionUpdate = true;
		noRotation = true;
		facingRigth = false;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		angle = 180;
	}
	
	public void addFireArrow() {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.fireArrow);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 10, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		fireArrowSprite = new FireArrowSprite(this, gameView, spriteBmp, x, y);
	}

	public void addFuelBar() {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.fuelBar);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 10, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		fuelBarSprite = new FuelBarSprite(this, gameView, spriteBmp, x, y);
	}

	public void addPowerBar() {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.powerBar);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 10 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		powerBarSprite = new PowerBarSprite(this, gameView, spriteBmp, x, y);
	}

	public void addLifeBarSprite() {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.lifeBar);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 10 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		lifeBarSprite = new LifeBarSprite(this, gameView, spriteBmp, x, y);
	}

	public void lifeLost(int lost) {
		life -= lost;
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

	public Vec2 getVelocityVec(int fireMultiplier, FreeSprite target) {
		Vec2 positionTarget = target.getBody().getPosition();
		Vec2 positionSrc = body.getPosition();
		Vec2 force = new Vec2(positionTarget.x - positionSrc.x, positionTarget.y - positionSrc.y);
		force.normalize();

		float xt = (firePower * fireMultiplier / firePower_MAX) * force.x;
		float yt = (firePower * fireMultiplier / firePower_MAX) * force.y;
		Vec2 v = new Vec2(xt, yt);

		System.out.println("getVelocityVec: x:" + v.x + ", y:" + v.y);
		return v;
	}

	public Vec2 getVelocityVec(int fireMultiplier) {
		float fangle = ((FireArrowSprite) gameView.getFireArrowSprite()).getAngle();
		Vec2 force = new Vec2((float) Math.cos(Math.toRadians(fangle)), (float) Math.sin(Math.toRadians(fangle)));
		force.normalize();

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
}
