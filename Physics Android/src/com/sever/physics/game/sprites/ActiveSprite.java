package com.sever.physics.game.sprites;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.graphics.Bitmap;

import com.sever.physic.PhysicsActivity;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;

public class ActiveSprite extends FreeSprite {

	public int life_AGG = +1;
	public final int life_MAX = 100;
	public int life = life_MAX;
	LifeBarSprite lifeBarSprite;

	public void setLifeBarSprite() {
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
		float max = 50;
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
}
