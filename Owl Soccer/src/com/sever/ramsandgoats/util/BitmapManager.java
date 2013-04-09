package com.sever.ramsandgoats.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sever.ramsandgoats.GameActivity;
import com.sever.ramsandgoats.GameApplication;
import com.sever.ramsandgoats.R;

public class BitmapManager {

	static BitmapManager self = null;

	public static Bitmap bmpStadium;
	public static Bitmap bmpStadiumMask;
	public static Bitmap bmpStadiumCheering;
	public static Bitmap bmpSky;
	public static Bitmap bmpBall;
	public static Bitmap bmpPlayerIdle;
	public static Bitmap bmpPlayerKicking;
	public static Bitmap bmpPlayerRunning;
	public static Bitmap bmpPlayerTricking;
	public static Bitmap bmpPlayerJumpingUp;
	public static Bitmap bmpPlayerJumpingDown;
	public static Bitmap bmpButtonGoLeft;
	public static Bitmap bmpButtonGoRight;

	static {

	}

	public static BitmapManager getManager() {
		if (self == null)
			self = new BitmapManager();
		return self;
	}

	public void initBitmaps() {
		bmpStadium = createScaledBitmap(R.drawable.stadium1280x720, (int) GameApplication.deviceWidth, (int) GameApplication.deviceHeight);
		bmpStadiumMask = createScaledBitmap(R.drawable.stadium1280x720mask, (int) GameApplication.deviceWidth, (int) GameApplication.deviceHeight);
		bmpStadiumCheering = createScaledBitmap(R.drawable.stadium1280x720cheering, (int) GameApplication.deviceWidth * 2, (int) GameApplication.deviceHeight);
		bmpSky = createScaledBitmap(R.drawable.sky1280x200, 0, 0);
		bmpBall = createScaledBitmap(R.drawable.football, 0, 0);
		bmpPlayerIdle = createScaledBitmap(R.drawable.player_idle_x2x1, 0, 0);
		bmpPlayerRunning = createScaledBitmap(R.drawable.player_walking_x4x1, 0, 0);
		bmpPlayerKicking = createScaledBitmap(R.drawable.player_kicking_x4x1, 0, 0);
		bmpPlayerTricking = createScaledBitmap(R.drawable.player_walking_x4x1, 0, 0);
		bmpPlayerJumpingUp = createScaledBitmap(R.drawable.player_jumping_up_x4x1, 0, 0);
		bmpPlayerJumpingDown = createScaledBitmap(R.drawable.player_jumping_down_x4x1, 0, 0);

		bmpButtonGoLeft = createScaledBitmap(R.drawable.buttongoleft, 0, 0);
		bmpButtonGoRight = createScaledBitmap(R.drawable.buttongoright, 0, 0);
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
		btmp = BitmapFactory.decodeResource(GameActivity.context.getResources(), decodeResource);
		if (dstWidth <= 0 || dstHeight <= 0) {
			dstWidth = btmp.getWidth();
			dstHeight = btmp.getHeight();
		}

		float perc = getPerc();
		return Bitmap.createScaledBitmap(btmp, (int) (dstWidth * perc), (int) (dstHeight * perc), false);
	}

	public float getPerc() {
		float perc = GameApplication.deviceHeight / 600.0f;
		if (GameApplication.deviceHeight < 480)
			return perc;
		return 1.0f;
	}

	public Bitmap createScaledBitmap(int decodeResource, float scale) {
		if (scale <= 0) {
			return BitmapFactory.decodeResource(GameActivity.context.getResources(), decodeResource);
		}

		Bitmap bmp = BitmapFactory.decodeResource(GameActivity.context.getResources(), decodeResource);
		return Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scale), (int) (bmp.getHeight() * scale), false);
	}

}
