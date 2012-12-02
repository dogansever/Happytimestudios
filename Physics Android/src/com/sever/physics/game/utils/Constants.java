package com.sever.physics.game.utils;

import com.sever.physic.IntroActivity;

public class Constants {
	public static final int FPS_Intro = 24;
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

	public static final float upperBoundxScreen = IntroActivity.deviceWidth;
	public static final float upperBoundx = upperBoundxScreen / pixelpermeter;

	public static final float upperBoundyScreen = IntroActivity.deviceHeight;
	public static final float upperBoundy = upperBoundyScreen / pixelpermeter;

	public static final float setAsBoxhxScreen = 500.0f;
	public static final float setAsBoxhx = setAsBoxhxScreen / pixelpermeter;

	public static final float setAsBoxhyScreen = 500.0f;
	public static final float setAsBoxhy = setAsBoxhyScreen / pixelpermeter;

	public static final float gravityx = 0.0f * pixelpermeter;
	public static final float gravityy = -2.0f * pixelpermeter;
	public static final float gravityPushPlayer = 10.0f * pixelpermeter;
	public static final float gravityPushEnemy = 5.0f * pixelpermeter;
	public static final float gravityPushExplosive = -1000.0f * pixelpermeter;
	public static final float gravityPullPlayer = 2.0f * pixelpermeter;
	public static final float gravityPullEnemy = 2.0f * pixelpermeter;
	public static final float gravityPullExplosive = 10.0f * pixelpermeter;
	public static final float gravityThrottle = 0.2f * pixelpermeter;
	public static final float gravityPullFieldRadiusPlayer = 500 / Constants.pixelpermeter;
	public static final float gravityPullFieldRadiusEnemy = 100 / Constants.pixelpermeter;
	public static final float gravityPullFieldRadiusExplosive = 500 / Constants.pixelpermeter;
	public static final float gravityPlanetPull = 10.0f * pixelpermeter;
	public static final float gravityPlanetPush = -15.0f * pixelpermeter;
}
