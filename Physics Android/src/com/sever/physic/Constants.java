package com.sever.physic;

public class Constants {
	public static final int FPS = 60;
	public static float timeStep = 1.0f / (1000 / Constants.FPS);
	public static int iterations = 10;

	public static final float pixelpermeter = 10.0f;
	public static final float boundhxy = 500 / pixelpermeter;
	public static final float lowerBoundy = 0f;
	public static final float lowerBoundx = 0f;
	// float upperBoundx = 102.4f;
	public static final float upperBoundx = PhysicsActivity.deviceWidth / pixelpermeter;
	// float upperBoundy = 60.0f;
	public static final float upperBoundy = PhysicsActivity.deviceHeight / pixelpermeter;
	public static final float setAsBoxhx = 500.0f / pixelpermeter;
	public static final float setAsBoxhy = 500.0f / pixelpermeter;
	public static final float gravityx = 0.0f / pixelpermeter;
	public static final float gravityy = -1.0f * pixelpermeter;
	public static final float gravityPush = 10.0f * pixelpermeter;
	public static final float gravityPull = 2.0f * pixelpermeter;
	public static final float gravityThrottle = 0.5f * pixelpermeter;
	public static final float gravityPullFieldRadius = 300 / Constants.pixelpermeter;
	public static final float gravityPlanet = 2.0f * pixelpermeter;
}
