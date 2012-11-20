package com.sever.physic;

public class Constants {
	public static final int FPS = 30;
	public static float timeStep = 1.0f / (1000 / Constants.FPS);
	public static int iterations = 10;

	public static final float pixelpermeter = 10.0f;

	public static final float boundhxyScreen = 500.0f;
	public static final float boundhxy = boundhxyScreen / pixelpermeter;

	public static final float lowerBoundyScreen = 0f;
	public static final float lowerBoundy = lowerBoundyScreen / pixelpermeter;

	public static final float lowerBoundxScreen = 0f;
	public static final float lowerBoundx = lowerBoundxScreen / pixelpermeter;

	public static final float upperBoundxScreen = PhysicsActivity.deviceWidth;
	public static final float upperBoundx = upperBoundxScreen / pixelpermeter;

	public static final float upperBoundyScreen = PhysicsActivity.deviceHeight;
	public static final float upperBoundy = upperBoundyScreen / pixelpermeter;

	public static final float setAsBoxhxScreen = 500.0f;
	public static final float setAsBoxhx = setAsBoxhxScreen / pixelpermeter;

	public static final float setAsBoxhyScreen = 500.0f;
	public static final float setAsBoxhy = setAsBoxhyScreen / pixelpermeter;

	public static final float gravityx = 0.0f / pixelpermeter;
	public static final float gravityy = -1.0f * pixelpermeter;
	public static final float gravityPushPlayer = 10.0f * pixelpermeter;
	public static final float gravityPullPlayer = 2.0f * pixelpermeter;
	public static final float gravityThrottle = 0.1f * pixelpermeter;
	public static final float gravityPullFieldRadiusPlayer = 300 / Constants.pixelpermeter;
	public static final float gravityPlanetPull = 1.5f * pixelpermeter;
	public static final float gravityPlanetPush = -15.0f * pixelpermeter;
}
