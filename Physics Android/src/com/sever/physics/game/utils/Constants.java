package com.sever.physics.game.utils;

import com.sever.physic.PhysicsApplication;

public class Constants {

	public static float extraHeight = PhysicsApplication.deviceHeight * 1.0f;
	public static float extraWidth = PhysicsApplication.deviceWidth * 1.0f;
	public static float extraWidthOffset = 0;
	public static float extraHeightOffset = 0;
	public static int quakePower = 0;
	public static int quakePowerMax = 10;
	public static boolean quakePending = false;
	public static final int FPS_Intro = 24;
	public static final int FPS = 17;
	public static float timeStep = 1.0f / (1000 / Constants.FPS);
	public static int iterations = 40;

	public static final float pixelpermeter = 10.0f;

	public static final float boundhxyScreen = 500.0f;
	public static final float boundhxy = boundhxyScreen / pixelpermeter;

	public static final float lowerBoundyScreen = 20f;
	public static final float lowerBoundy = lowerBoundyScreen / pixelpermeter;

	public static final float lowerBoundxScreen = 0f;
	public static final float lowerBoundx = lowerBoundxScreen / pixelpermeter;

	public static float upperBoundxScreen = PhysicsApplication.deviceWidth + extraWidth;
	public static final float upperBoundx = upperBoundxScreen / pixelpermeter;

	public static float upperBoundyScreen = PhysicsApplication.deviceHeight + extraHeight;
	public static int enemyKilledCount;
	public static int scoreStage;
	public static int scoreTotal;
	public static int scoreTimeBonus;
	public static int scoreLifeBonus;
	public static int scoreStageBonus;
	public static int scoreToPassTheStage;
	public static int playerKilledCount;
	public static final float upperBoundy = upperBoundyScreen / pixelpermeter;

	public static final float setAsBoxhxScreen = 500.0f;
	public static final float setAsBoxhx = setAsBoxhxScreen / pixelpermeter;

	public static final float setAsBoxhyScreen = 500.0f;
	public static final float setAsBoxhy = setAsBoxhyScreen / pixelpermeter;

	public static final float gravityx = 0.0f * pixelpermeter;
	public static final float gravityy = -2.0f * pixelpermeter;
	public static final float gravityPushPlayer = 10.0f * pixelpermeter;
	public static final float gravityPushEnemy = 5.0f * pixelpermeter;
	public static final float gravityPushExplosive = -500.0f * pixelpermeter;
	public static final float gravityPullPlayer = 2.0f * pixelpermeter;
	public static final float gravityPullEnemy = 30.0f * pixelpermeter;
	public static final float gravityPullExplosive = 10.0f * pixelpermeter;
	public static final float gravityThrottle = 0.5f * pixelpermeter;
	public static final float gravityPullFieldRadiusPlayer = 500 / Constants.pixelpermeter;
	public static final float gravityPullFieldRadiusEnemy = 100 / Constants.pixelpermeter;
	public static final float gravityPullFieldRadiusExplosive = 5000 / Constants.pixelpermeter;
	public static final float gravityPullFieldRadiusPlanet = 5000 / Constants.pixelpermeter;
	public static final float gravityPlanetPull = 40.0f * pixelpermeter;
	public static final float gravityPlanetPush = -15.0f * pixelpermeter;

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
