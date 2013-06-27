package com.sever.physics.game.utils;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import com.sever.physics.game.sprites.FreeSprite;

public class GameViewUtils {

	public static float angleGrenadeCapsule = 0;
	public static int countGrenadeCapsule = 0;
	public static int waitGrenadeCapsuleInFPS = (int) (Constants.FPS * 0.05f);
	public static int waitGrenadeCapsuleInFPSTicking;
	public static boolean onGrenadeCapsule;
	public static int countGrenadeTriple = 0;
	public static int waitGrenadeTripleInFPS = (int) (Constants.FPS * 0.2f);
	public static int waitGrenadeTripleInFPSTicking;
	public static boolean onGrenadeTriple;
	public static boolean facingRigth;
	public static Vec2 velocityVec;

	public static final int PLAY_AREA_PADDING_TOP = 10;
	public static final int PLAY_AREA_PADDING_BOTTOM = 10;
	public static int shiftWidth = 1;
	public int GAMEOVER_ALPHA = 0;
	public int GAMEOVER_ALPHA_INCREMENT = 5;
	public int GAMEOVER_ALPHA_MIN = 55;
	public int GAMEOVER_ALPHA_MAX = 255;
	public int GAMEOVER_WAIT_TIME = Constants.FPS * 5;
	public int NEXT_STAGE_WAIT_TIME = 100;
	public int NEXT_STAGE_WAIT_TIME_MAX = Constants.FPS * 5;

	public ConcurrentLinkedQueue<FreeSprite> explosiveSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> freeSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> collectSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> portalSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> staticSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> groundSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> enemySprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> playerSprite = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> nophysicsSprite = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> effectsSprite = new ConcurrentLinkedQueue<FreeSprite>();

	public int getAlpha(int time, int timemax) {
		return (255 * time / timemax) <= 0 ? 0 : 255 * time / timemax;
	}
}
