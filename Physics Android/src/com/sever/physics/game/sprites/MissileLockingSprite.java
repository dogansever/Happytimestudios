package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import android.graphics.Canvas;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.SpriteBmp;
import com.sever.physics.game.utils.WeaponTypes;
import com.sever.physics.game.utils.WeaponsManager;

public class MissileLockingSprite extends MissileSprite {

	public FreeSprite target;

	public MissileLockingSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, SpriteBmp spriteBmp, float x, float y, WeaponTypes wt, boolean facingRigth) {
		super(spriteList, gameView, spriteBmp, x, y, wt, facingRigth);
	}

	@Override
	public void onDraw(Canvas canvas) {
		moveToTarget();
		super.onDraw(canvas);
	}

	private void moveToTarget() {
		if (gameView.idle) {
			return;
		}

		if (noPositionUpdate) {
			return;
		}

		if (target == null) {
			this.aimAt(gameView.getPlayerSprite());
			getBody().setLinearVelocity(getVelocityVec(gameView.getPlayerSprite()));
		} else {
			this.aimAt(target);
			getBody().setLinearVelocity(getVelocityVec(target));

		}
	}

	public Vec2 getVelocityVec(FreeSprite target) {
		// System.out.println("getLinearVelocity(): x:" +
		// getBody().getLinearVelocity().x + ", y:" +
		// getBody().getLinearVelocity().x);
		Vec2 force = new Vec2(target.x - this.x, target.y - this.y);
		force.normalize();

		float xt = ActiveSprite.fireMultiplierMissileLocking * force.x;
		float yt = ActiveSprite.fireMultiplierMissileLocking * force.y;
		Vec2 v = new Vec2(xt, yt);

		// System.out.println("MissileLockingSprite getVelocityVec: x:" + v.x +
		// ", y:" + v.y);
		return v;
	}

	public FreeSprite getTarget() {
		return target;
	}

	public void setTarget(FreeSprite target) {
		this.target = target;
	}

}
