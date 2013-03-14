package com.sever.physics.game.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.sever.physic.IntroActivity;
import com.sever.physic.PhysicsActivity;
import com.sever.physic.PhysicsApplication;
import com.sever.physic.R;

public class BitmapManager {

	static BitmapManager self = null;

	public static Bitmap bmpBack;
	public static Bitmap bmpBall;
	public static Bitmap bmpBox;
	public static Bitmap bmpFirstAidKit;
	public static Bitmap bmpBox2;
	public static Bitmap planet1;
	public static Bitmap barrel;
	public static Bitmap player;
	public static Bitmap playerThrottle;
	public static Bitmap playerexploding;
	public static Bitmap playerportalin;
	public static Bitmap playerportalout;
	// public static Bitmap enemy;
	public static Bitmap enemypointer;
	public static Bitmap firstAidKitpointer;
	// public static Bitmap enemyThrottle;
	// public static Bitmap enemyexploding;
	// public static Bitmap enemyBurning;
	public static Bitmap ground;
	public static Bitmap ground2;
	public static Bitmap missile;
	public static Bitmap missileLight;
	public static Bitmap missileLocking;
	public static Bitmap bomb;
	public static Bitmap bombsmall;
	public static Bitmap bombtriple;
	public static Bitmap bombcapsule;
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
	public static Bitmap smoke;
	public static Bitmap portal;
	static {

	}

	public static BitmapManager getManager() {
		if (self == null)
			self = new BitmapManager();
		return self;
	}

	public void initBitmaps() {
		bmpBack = createScaledBitmap(R.drawable.space, (int) PhysicsApplication.deviceWidth, (int) PhysicsApplication.deviceHeight);
		bmpBall = createScaledBitmap(R.drawable.ammominex1x1, 0, 0);
		bmpBox = createScaledBitmap(R.drawable.crate, 40, 40);
		bmpBox2 = createScaledBitmap(R.drawable.crate, 0, 0);
		planet1 = createScaledBitmap(R.drawable.planet, 0, 0);
		barrel = createScaledBitmap(R.drawable.barrel, 0, 0);
		player = createScaledBitmap(R.drawable.playerx2x2, 0, 0);
		playerThrottle = createScaledBitmap(R.drawable.playerthrottlex2x2, 0, 0);
		playerexploding = createScaledBitmap(R.drawable.explosion02x3x1, 0, 0);
		playerportalin = createScaledBitmap(R.drawable.portalinx7x1, 0, 0);
		playerportalout = createScaledBitmap(R.drawable.portaloutx7x1, 0, 0);
		ground = createScaledBitmap(R.drawable.ground01, (int) 0, 0);
		ground2 = createScaledBitmap(R.drawable.ground03, (int) 0, 0);
		missile = createScaledBitmap(R.drawable.ammomissilex2x1, 0, 0);
		missileLight = createScaledBitmap(R.drawable.ammomissilelightx2x1, 0, 0);
		missileLocking = createScaledBitmap(R.drawable.ammomissilelockingx2x1, 0, 0);
		bomb = createScaledBitmap(R.drawable.ammobombbigx2x1, 0, 0);
		bombsmall = createScaledBitmap(R.drawable.ammobombx2x1, 0, 0);
		bombtriple = createScaledBitmap(R.drawable.ammobombtriplex2x1, 0, 0);
		bombcapsule = createScaledBitmap(R.drawable.ammobombcapsulex2x1, 0, 0);
		bombtimer = createScaledBitmap(R.drawable.ammobombtimerx2x1, 0, 0);
		bombexploding = createScaledBitmap(R.drawable.explosion01x4x1, 0, 0);
		powerBar = createScaledBitmap(R.drawable.powerbar, (int) (225 * 0.4f), (int) (20 * 0.4f));
		lifeBar = createScaledBitmap(R.drawable.progressbarslife, (int) (225 * 0.4f), (int) (20 * 0.4f));
		fuelBar = createScaledBitmap(R.drawable.fuelbar, 60, 45);
		joystick = createScaledBitmap(R.drawable.joystick, 0, 0);
		fireButton = createScaledBitmap(R.drawable.buttonsfire, 0, 0);
		fireArrow = createScaledBitmap(R.drawable.firearrowx10, 0, 0);
		// hook = createScaledBitmap(R.drawable.hook, 0, 0);
		enemypointer = createScaledBitmap(R.drawable.enemypointer, 0, 0);
		weaponSwapButton = createScaledBitmap(R.drawable.weaponsmine, 0, 0);
		portalButton = createScaledBitmap(R.drawable.buttonsportal, 0, 0);
		stagePassBar = createScaledBitmap(R.drawable.progressbarsstage, 0, 0);
		lifeBarBonus = createScaledBitmap(R.drawable.progressbarstime, 0, 0);
		smoke = createScaledBitmap(R.drawable.smokex2x1, 0, 0);
		bmpFirstAidKit = createScaledBitmap(R.drawable.firstaidkitx2x1, 0, 0);
		firstAidKitpointer = createScaledBitmap(R.drawable.firstaidkitpointerx2x1, 0, 0);
		portal = createScaledBitmap(R.drawable.portalx4x1, 0, 0);
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
		Bitmap btmp = null;
		btmp = BitmapFactory.decodeResource(PhysicsActivity.context.getResources(), decodeResource);
		if (dstWidth <= 0 || dstHeight <= 0) {
			dstWidth = btmp.getWidth();
			dstHeight = btmp.getHeight();
		}

		float perc = getPerc();
		return Bitmap.createScaledBitmap(btmp, (int) (dstWidth * perc), (int) (dstHeight * perc), false);
	}

	public float getPerc() {
		float perc = PhysicsApplication.deviceHeight / 600.0f;
		if (PhysicsApplication.deviceHeight < 480)
			return perc;
		return 1.0f;
	}

	public Bitmap createScaledBitmap(int decodeResource, float scale) {
		if (scale <= 0) {
			return BitmapFactory.decodeResource(PhysicsActivity.context.getResources(), decodeResource);
		}

		Bitmap bmp = BitmapFactory.decodeResource(PhysicsActivity.context.getResources(), decodeResource);
		return Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale), (int) (bmp.getHeight() * scale), false);
	}

}
