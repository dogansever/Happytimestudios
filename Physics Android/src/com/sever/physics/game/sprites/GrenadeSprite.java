package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;

import com.sever.physic.PhysicsActivity;
import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SoundEffectsManager;
import com.sever.physics.game.utils.SpriteBmp;
import com.sever.physics.game.utils.WeaponTypes;
import com.sever.physics.game.utils.WeaponsManager;

public class GrenadeSprite extends FreeSprite {

	public boolean powerOn;
	private float velx;
	private float vely;

	public GrenadeSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, SpriteBmp spriteBmp, float x, float y, WeaponTypes wt) {
		try {
			this.width = spriteBmp.getWidth();
			this.height = spriteBmp.getHeight();
			this.spriteBmp = spriteBmp;
			this.widthExplosion = WeaponsManager.getManager().getWeaponByType(wt).getExplosionRadius();
			this.gameView = gameView;
			this.x = x;
			this.y = y;
			this.spriteList = spriteList;
			this.wt = wt;

			FADE_LIFE = WeaponsManager.getManager().getWeaponByType(wt).getLifeTimeInMiliseconds();
			makeExplodes();
			makeFades();
			addSprite(x, y);
		} catch (Exception e) {
			killSprite();
		}
	}

	public boolean readyToExplode() {
		checkSuddenChangeInDirection();
		boolean ready = (FADE_LIFE == 0 && spriteBmp.bmpIndex == 0);
		if (ready) {
			explodeBmp();
		}
		return ready;
	}

	private void checkSuddenChangeInDirection() {
		try {
			if (implodes)
				return;
			if (getBody() == null)
				return;

			float diffx = Math.abs(getBody().getLinearVelocity().x - velx);
			float diffy = Math.abs(getBody().getLinearVelocity().y - vely);
			float diffxMax = wt == WeaponTypes.MISSILE_LOCKING ? 20.0f : 20.0f;

			// System.out.println("velx:" + getBody().getLinearVelocity().x +
			// ",diffx:" + diffx + ", diffy:" + diffy);
			if (spriteBmp.bmpIndex == 0 && velx != 0 && (diffx >= diffxMax || diffy >= diffxMax)) {
				System.out.println("SuddenChangeInDirection detected! diffx:" + diffx + ", diffy:" + diffy);
				FADE_LIFE = 0;
			} else {
				velx = getBody().getLinearVelocity().x;
				vely = getBody().getLinearVelocity().y;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void explodeBmp() {
		FADE_LIFE = (int) (WeaponsManager.getManager().getWeaponByType(wt).getLifeTimeInMiliseconds() * 0.25);
		spriteBmp.setBmpIndex(1);
		noPositionUpdate = true;
		noRotation = true;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		angle = 0;
		SoundEffectsManager.getManager().playEXPLODE_BOMB(PhysicsActivity.context);
	}

	void addSprite(float x, float y) {
		createDynamicBody(x, y);
		createShape();
		getBody().setAngularVelocity((float) (Math.random() * 90));
	}

	public void createShape() {
		CircleDef circle = new CircleDef();
		circle.radius = getWidthPhysical() * 0.5f;
		circle.friction = 1.0f;// zero being completely frictionless
		circle.restitution = 0.0f;// zero being not bounce at all
		circle.density = 0.5f;

		// PolygonDef playerDef = new PolygonDef();
		// playerDef.setAsBox(width * 0.5f / Constants.pixelpermeter, height *
		// 0.5f / Constants.pixelpermeter);
		// playerDef.friction = 1.0f;
		// playerDef.restitution = 0.2f;
		// playerDef.density = 10.0f;

		// Assign shape to Body
		// getBody().createShape(playerDef);

		getBody().createShape(circle);
		getBody().setMassFromShapes();
		getBody().setBullet(true);

	}

	public void push(FreeSprite sprite) {
		float FIELD_RADIUS = getWidthExplosionPhysical() * 3.0f;
		boolean result = applyForce(sprite, getPosition(), FIELD_RADIUS, Constants.gravityPushExplosive);
		Constants.startQuake();

		if (result && sprite instanceof ActiveSprite) {
			((ActiveSprite) sprite).lifeLost(WeaponsManager.getManager().getWeaponByType(wt).getDamageLife());
		}
	}

	private Vec2 getPosition() {
		return new Vec2(x / Constants.pixelpermeter, y / Constants.pixelpermeter);
	}

	public void pull(FreeSprite sprite) {
		if (!implodes)
			return;

		float FIELD_RADIUS = Constants.gravityPullFieldRadiusExplosive;
		Vec2 positionSrc = this.getBody().getPosition();
		applyForce(sprite, positionSrc, FIELD_RADIUS, Constants.gravityPullEnemy);
	}

}
