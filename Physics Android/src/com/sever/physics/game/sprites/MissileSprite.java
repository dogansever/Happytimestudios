package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.collision.PolygonDef;

import android.graphics.Canvas;

import com.sever.physics.game.GameViewImp;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;
import com.sever.physics.game.utils.WeaponTypes;

public class MissileSprite extends GrenadeSprite {

	public static int smokeTimeInFPS;
	public static int smokeTimeInFPSMax = (int) (Constants.FPS * 0.1f);

	public MissileSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameViewImp gameView, SpriteBmp spriteBmp, float x, float y, WeaponTypes wt, boolean facingRigth) {
		super(spriteList, gameView, spriteBmp, x, y, wt);
		this.facingRigth = facingRigth;
		noRotation = false;
		manualAngleSet = true;
		smokeTimeInFPS = smokeTimeInFPSMax;
	}

	public void createShape() {
		PolygonDef playerDef = new PolygonDef();
		playerDef.setAsBox(getWidthPhysical() * 0.5f, getHeightPhysical() * 0.25f);
		playerDef.friction = 1.0f;
		playerDef.restitution = 0.0f;
		playerDef.density = 0.5f;

		getBody().createShape(playerDef);

		// getBody().setXForm(getBody().getLocalCenter(), 45);
		getBody().setMassFromShapes();
		getBody().setBullet(true);

	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (smokeTimeInFPS-- == 0) {
			FreeSprite smoke = gameView.addSmoke(x , y);
			smoke.setAngle(angle);
			smokeTimeInFPS = smokeTimeInFPSMax;
		}
	}

}
