package com.sever.physics.game.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sever.physic.PhysicsActivity;
import com.sever.physic.PhysicsApplication;
import com.sever.physic.R;

public class BitmapManager {

	static BitmapManager self = null;

	public static Bitmap bmpBack;
	public static Bitmap bmpBall;
	public static Bitmap bmpBox;
	public static Bitmap bmpBox2;
	public static Bitmap planet1;
	public static Bitmap barrel;
	public static Bitmap player;
	public static Bitmap playerThrottle;
	public static Bitmap playerexploding;
	// public static Bitmap enemy;
	public static Bitmap enemypointer;
	// public static Bitmap enemyThrottle;
	// public static Bitmap enemyexploding;
	// public static Bitmap enemyBurning;
	public static Bitmap ground;
	public static Bitmap missile;
	public static Bitmap missileLight;
	public static Bitmap missileLocking;
	public static Bitmap bomb;
	public static Bitmap bombsmall;
	public static Bitmap bombtriple;
	public static Bitmap bombexploding;
	public static Bitmap bombtimer;
	public static Bitmap powerBar;
	public static Bitmap lifeBar;
	public static Bitmap lifeBarBonus;
	public static Bitmap stagePassBar;
	public static Bitmap fuelBar;
	public static Bitmap joystick;
	public static Bitmap fireButton;
	public static Bitmap weaponSwapButton;
	public static Bitmap portalButton;
	public static Bitmap fireArrow;
	public static Bitmap hook;
	static {

	}

	public static BitmapManager getManager() {
		if (self == null)
			self = new BitmapManager();
		return self;
	}

	public void initBitmaps() {
		bmpBack = createScaledBitmap(R.drawable.space, (int) PhysicsApplication.deviceWidth, (int) PhysicsApplication.deviceHeight);
		bmpBall = createScaledBitmap(R.drawable.ammominex1x1, 40, 40);
		bmpBox = createScaledBitmap(R.drawable.crate, 40, 40);
		bmpBox2 = createScaledBitmap(R.drawable.crate, 40, 40);
		planet1 = createScaledBitmap(R.drawable.planet200x200, 50, 50);
		barrel = createScaledBitmap(R.drawable.barrel, 45, 75);
		player = createScaledBitmap(R.drawable.playerx2x2, (int) (200 * 0.8f), (int) (200 * 0.8f));
		playerThrottle = createScaledBitmap(R.drawable.playerthrottlex2x2, (int) (200 * 0.8f), (int) (200 * 0.8f));
		playerexploding = createScaledBitmap(R.drawable.explosion02x3x1, 0, 0);
		ground = createScaledBitmap(R.drawable.crate, (int) PhysicsApplication.deviceWidth, 50);
		missile = createScaledBitmap(R.drawable.ammomissilex2x1, 120, 30);
		missileLight = createScaledBitmap(R.drawable.ammomissilelightx2x1, 120, 15);
		missileLocking = createScaledBitmap(R.drawable.ammomissilelockingx2x1, 200, 30);
		bomb = createScaledBitmap(R.drawable.ammobombbigx2x1, 100, 50);
		bombsmall = createScaledBitmap(R.drawable.ammobombx2x1, 60, 30);
		bombtriple = createScaledBitmap(R.drawable.ammobombtriplex2x1, 50, 25);
		bombtimer = createScaledBitmap(R.drawable.ammobombtimerx2x1, 120, 30);
		bombexploding = createScaledBitmap(R.drawable.explosion01x4x1, 0, 0);
		powerBar = createScaledBitmap(R.drawable.powerbar, 45, 60);
		lifeBar = createScaledBitmap(R.drawable.progressbarslife, (int) (225 * 0.4f), (int) (20 * 0.4f));
		fuelBar = createScaledBitmap(R.drawable.fuelbar, 60, 45);
		joystick = createScaledBitmap(R.drawable.joystick, 0, 0);
		fireButton = createScaledBitmap(R.drawable.buttonsfire, 380, 220);
		fireArrow = createScaledBitmap(R.drawable.firearrowx10, 500, 50);
		// hook = createScaledBitmap(R.drawable.hook, 0, 0);
		enemypointer = createScaledBitmap(R.drawable.enemypointer, 0, 0);
		weaponSwapButton = createScaledBitmap(R.drawable.weaponsmine, 100, 100);
		portalButton = createScaledBitmap(R.drawable.buttonsportal, 100, 100);
		stagePassBar = createScaledBitmap(R.drawable.progressbarsstage, (int) (394 * 0.75f), (int) (44 * 0.75f));
		lifeBarBonus = createScaledBitmap(R.drawable.progressbarstime, (int) (394 * 0.75f), (int) (44 * 0.75f));
	}

	public Bitmap createScaledBitmap(Bitmap bmp, float scale) {
		if (scale <= 0) {
			return bmp;
		}
		return Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale), (int) (bmp.getHeight() * scale), false);
	}

	public Bitmap createScaledBitmap(Bitmap bmp, int dstWidth, int dstHeight) {
		if (dstWidth <= 0 || dstHeight <= 0) {
			return bmp;
		}
		return Bitmap.createScaledBitmap(bmp, dstWidth, dstHeight, false);
	}

	public Bitmap createScaledBitmap(int decodeResource, int dstWidth, int dstHeight) {
		if (dstWidth <= 0 || dstHeight <= 0) {
			return BitmapFactory.decodeResource(PhysicsActivity.context.getResources(), decodeResource);
		}

		float perc = getPerc();
		return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(PhysicsActivity.context.getResources(), decodeResource), (int) (dstWidth * perc), (int) (dstHeight * perc), false);
	}

	public float getPerc() {
		return PhysicsApplication.deviceHeight / 600.0f;
	}

	public Bitmap createScaledBitmap(int decodeResource, float scale) {
		if (scale <= 0) {
			return BitmapFactory.decodeResource(PhysicsActivity.context.getResources(), decodeResource);
		}

		Bitmap bmp = BitmapFactory.decodeResource(PhysicsActivity.context.getResources(), decodeResource);
		return Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale), (int) (bmp.getHeight() * scale), false);
	}

}
