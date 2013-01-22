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
	public static Bitmap bomb;
	public static Bitmap bombsmall;
	public static Bitmap bombtriple;
	public static Bitmap bombexploding;
	public static Bitmap bomb2;
	public static Bitmap powerBar;
	public static Bitmap lifeBar;
	public static Bitmap lifeBarBonus;
	public static Bitmap stagePassBar;
	public static Bitmap fuelBar;
	public static Bitmap joystick;
	public static Bitmap fireButton;
	public static Bitmap weaponSwapButton;
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
		bmpBall = createScaledBitmap(R.drawable.bullet, 20, 20);
		bmpBox = createScaledBitmap(R.drawable.crate50x50dark, 40, 40);
		bmpBox2 = createScaledBitmap(R.drawable.crate50x50light, 40, 40);
		planet1 = createScaledBitmap(R.drawable.planet200x200, 50, 50);
		barrel = createScaledBitmap(R.drawable.barrel, 45, 75);
		player = createScaledBitmap(R.drawable.playerx2x2, (int) (340 * 0.4f), (int) (296 * 0.4f));
		playerThrottle = createScaledBitmap(R.drawable.playerthrottlex2x2, (int) (340 * 0.4f), (int) (296 * 0.4f));
		playerexploding = createScaledBitmap(R.drawable.enemyexplosionx3x1, 0, 0);
		// enemy = createScaledBitmap(R.drawable.enemy2x1, (int) (90 * 1.0f),
		// (int) (42 * 1.0f));
		// enemyThrottle = createScaledBitmap(R.drawable.enemythrottlex2x2,
		// (int) (90 * 1.0f), (int) (84 * 1.0f));
		// enemyBurning = createScaledBitmap(R.drawable.enemyburningx2x2, (int)
		// (90 * 1.0f), (int) (84 * 1.0f));
		// enemyexploding = createScaledBitmap(R.drawable.enemyexplosionx3x1, 0,
		// 0);
		ground = createScaledBitmap(R.drawable.crate50x50dark, 800, 50);
		bomb = createScaledBitmap(R.drawable.bombx2x1, 90, 45);
		bombsmall = createScaledBitmap(R.drawable.bombx2x1, 60, 30);
		missile = createScaledBitmap(R.drawable.missilex4x1, 200, 30);
		missileLight = createScaledBitmap(R.drawable.missilex4x1, 140, 21);
		bombtriple = createScaledBitmap(R.drawable.bombx2x1, 50, 25);
		bombexploding = createScaledBitmap(R.drawable.bombexplodingx4x1, 0, 0);
		bomb2 = createScaledBitmap(R.drawable.bombx4x1, 120, 30);
		powerBar = createScaledBitmap(R.drawable.powerbar, 45, 60);
		lifeBar = createScaledBitmap(R.drawable.progresslife, (int) (225 * 0.4f), (int) (20 * 0.4f));
		lifeBarBonus = createScaledBitmap(R.drawable.progressbonustime, (int) (394 * 0.75f), (int) (44 * 0.75f));
		fuelBar = createScaledBitmap(R.drawable.fuelbar, 60, 45);
		joystick = createScaledBitmap(R.drawable.joystick, 0, 0);
		fireButton = createScaledBitmap(R.drawable.buttonred, 400, 200);
		fireArrow = createScaledBitmap(R.drawable.firearrowx10, 500, 50);
		hook = createScaledBitmap(R.drawable.hook, 0, 0);
		enemypointer = createScaledBitmap(R.drawable.enemypointer, 0, 0);
		weaponSwapButton = createScaledBitmap(R.drawable.buttongun2, 100, 100);
		stagePassBar = createScaledBitmap(R.drawable.progress, (int) (394 * 0.75f), (int) (44 * 0.75f));
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
		return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(PhysicsActivity.context.getResources(), decodeResource), dstWidth, dstHeight, false);
	}

	public Bitmap createScaledBitmap(int decodeResource, float scale) {
		if (scale <= 0) {
			return BitmapFactory.decodeResource(PhysicsActivity.context.getResources(), decodeResource);
		}

		Bitmap bmp = BitmapFactory.decodeResource(PhysicsActivity.context.getResources(), decodeResource);
		return Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale), (int) (bmp.getHeight() * scale), false);
	}

}
