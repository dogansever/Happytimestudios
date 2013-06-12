package com.sever.ramsandgoats.util;

import com.sever.ramsandgoats.GameApplication;

public class Constants {
	public static final int FPS = 20;
	public static float timeStep = 1.0f / (1000 / Constants.FPS);
	public static int iterations = 40;

	public static final float pixelpermeter = 10.0f;

	public static float extraHeight = GameApplication.deviceHeight * 0.0f;
	public static float extraWidth = GameApplication.deviceWidth * 0.0f;
	public static float extraWidthOffset = 0;
	public static float extraHeightOffset = 0;
	public static float penaltyAreaWidth = 250;

	public static final float boundhxyScreen = 500.0f;
	public static final float boundhxy = boundhxyScreen / pixelpermeter;

	public static final float lowerBoundyScreen = 110f;
	public static final float lowerBoundy = lowerBoundyScreen / pixelpermeter;

	public static final float lowerBoundxScreen = 0f;
	public static final float lowerBoundx = lowerBoundxScreen / pixelpermeter;

	public static float upperBoundxScreen = GameApplication.deviceWidth + extraWidth;
	public static final float upperBoundx = upperBoundxScreen / pixelpermeter;

	public static float upperBoundyScreen = GameApplication.deviceHeight + extraHeight;

	public static final float upperBoundy = upperBoundyScreen / pixelpermeter;

	public static final float setAsBoxhxScreen = 500.0f;
	public static final float setAsBoxhx = setAsBoxhxScreen / pixelpermeter;

	public static final float setAsBoxhyScreen = 500.0f;
	public static final float setAsBoxhy = setAsBoxhyScreen / pixelpermeter;

	public static final float gravityx = 0.0f * pixelpermeter;
	public static final float gravityy = -20.0f * pixelpermeter;
	public static final float gravityThrottle = 0.5f * pixelpermeter;
	public static final float gravityPushPlayer = 50.0f * pixelpermeter;

	public static int quakePower = 0;
	public static int quakePowerMax = 10;
	public static boolean quakePending = false;

	public static void startQuake() {
		Constants.quakePower = Constants.quakePowerMax;
		Constants.quakePending = true;
	}

	public static void endQuake() {
		Constants.quakePower = Constants.quakePowerMax;
		Constants.quakePending = false;
	}

	public static boolean checkForQuake() {
		return Constants.quakePending && Constants.quakePower-- > 0;
	}

	public static int getQuakePower() {
		return Constants.quakePower % 2 == 0 ? -1 * Constants.quakePower : 1 * Constants.quakePower;
	}
}
