package com.sever.physics.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sever.physic.IntroActivity;
import com.sever.physic.PhysicsActivity;
import com.sever.physic.PhysicsApplication;
import com.sever.physics.game.pojo.TextDrawingPojo;
import com.sever.physics.game.sprites.BonusLifeBarSprite;
import com.sever.physics.game.sprites.BoxSprite;
import com.sever.physics.game.sprites.ButtonFireSprite;
import com.sever.physics.game.sprites.ButtonPortalSprite;
import com.sever.physics.game.sprites.ButtonSwapWeaponSprite;
import com.sever.physics.game.sprites.CapsuleSprite;
import com.sever.physics.game.sprites.EnemySprite;
import com.sever.physics.game.sprites.FreeSprite;
import com.sever.physics.game.sprites.GrenadeImplodeSprite;
import com.sever.physics.game.sprites.GrenadeSprite;
import com.sever.physics.game.sprites.GroundBoxSprite;
import com.sever.physics.game.sprites.JoystickSprite;
import com.sever.physics.game.sprites.MissileLockingSprite;
import com.sever.physics.game.sprites.MissileSprite;
import com.sever.physics.game.sprites.PlanetSprite;
import com.sever.physics.game.sprites.PlayerSprite;
import com.sever.physics.game.sprites.SmokeSprite;
import com.sever.physics.game.sprites.StagePassBarSprite;
import com.sever.physics.game.sprites.StaticBoxSprite;
import com.sever.physics.game.sprites.StaticSpriteNoShape;
import com.sever.physics.game.utils.BitmapManager;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.GameStates;
import com.sever.physics.game.utils.Joint;
import com.sever.physics.game.utils.SoundEffectsManager;
import com.sever.physics.game.utils.SpriteBmp;
import com.sever.physics.game.utils.StageManager;
import com.sever.physics.game.utils.WeaponTypes;
import com.sever.physics.game.utils.WeaponsManager;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, GameViewI {
	public GameLoopThread gameLoopThread;
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
	public ConcurrentLinkedQueue<TextDrawingPojo> textDrawingPojoList = new ConcurrentLinkedQueue<TextDrawingPojo>();
	public int score = 0;
	public int point = 0;
	private int scoreHigh = 0;
	static final int PLAY_AREA_PADDING_TOP = 10;
	static final int PLAY_AREA_PADDING_BOTTOM = 10;
	private static int shiftWidth = 1;
	private Context context;
	public boolean threadStarted = false;
	private Runnable r;
	public long timePause;
	public long timeResumed;
	public long delayInSeconds;
	public SurfaceHolder holder;
	private FreeSprite victim;
	public boolean finishGame;
	public boolean endOfGame;
	public boolean waitingForNextStage;
	public int GAMEOVER_ALPHA = 0;
	public int GAMEOVER_ALPHA_INCREMENT = 5;
	public int GAMEOVER_ALPHA_MIN = 55;
	public int GAMEOVER_ALPHA_MAX = 255;
	public int GAMEOVER_WAIT_TIME = Constants.FPS * 5;
	public int NEXT_STAGE_WAIT_TIME = 100;
	public int NEXT_STAGE_WAIT_TIME_MAX = Constants.FPS * 5;
	public boolean idle;
	private int buttonFirePointerId;
	private int buttonSwapWeaponPointerId;
	private boolean joystickEnabled;
	private boolean buttonSwapEnabled;
	private boolean buttonFireEnabled;
	public static boolean success;
	public static boolean newGame;
	public static boolean BOSSTIME;
	public GameStates gameState;

	float newStagePointx = 0;
	float newStagePointy = 0;
	private String newWeaponUnlockedMessage = "";
	private String newStageUpMessage = "";

	public FreeSprite getBonusLifeBarSprite() {
		for (Object fs : this.nophysicsSprite.toArray()) {
			if (fs instanceof BonusLifeBarSprite)
				return (FreeSprite) fs;
		}
		return null;
	}

	public FreeSprite getPortalButtonSprite() {
		for (Object fs : this.nophysicsSprite.toArray()) {
			if (fs instanceof ButtonPortalSprite)
				return (FreeSprite) fs;
		}
		return null;

	}

	public FreeSprite getSwapWeaponButtonSprite() {
		for (Object fs : this.nophysicsSprite.toArray()) {
			if (fs instanceof ButtonSwapWeaponSprite)
				return (FreeSprite) fs;
		}
		return null;
	}

	public FreeSprite getFireButtonSprite() {
		for (Object fs : this.nophysicsSprite.toArray()) {
			if (fs instanceof ButtonFireSprite)
				return (FreeSprite) fs;
		}
		return null;
	}

	public FreeSprite getJoystickSprite() {
		for (Object fs : this.nophysicsSprite.toArray()) {
			if (fs instanceof JoystickSprite)
				return (FreeSprite) fs;
		}
		return null;
	}

	public FreeSprite getFireArrowSprite() {
		return ((PlayerSprite) getPlayerSprite()).fireArrowSprite;
	}

	public FreeSprite getPowerBarSprite() {
		return ((PlayerSprite) getPlayerSprite()).powerBarSprite;
	}

	public FreeSprite getPlayerSprite() {
		try {
			return this.playerSprite.element();
		} catch (Exception e) {
		}
		return null;
	}

	public int getPoint() {
		return point;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public FreeSprite addJoystick(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.joystick);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new JoystickSprite(nophysicsSprite, this, spriteBmp, x, y);
		nophysicsSprite.add(sprite);
		return sprite;
	}

	public FreeSprite addFireButton(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.fireButton);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new ButtonFireSprite(nophysicsSprite, this, spriteBmp, x, y);
		nophysicsSprite.add(sprite);
		return sprite;
	}

	public FreeSprite addSwapWeaponButton(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.weaponSwapButton);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new ButtonSwapWeaponSprite(nophysicsSprite, this, spriteBmp, x, y);
		nophysicsSprite.add(sprite);
		return sprite;
	}

	public FreeSprite addPortalButton(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.portalButton);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new ButtonPortalSprite(nophysicsSprite, this, spriteBmp, x, y);
		nophysicsSprite.add(sprite);
		return sprite;
	}

	public FreeSprite addMissileLocking(float x, float y, boolean facingRight) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.missileLocking);
		bmp.add(BitmapManager.bombexploding);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new MissileLockingSprite(explosiveSprites, this, spriteBmp, x, y, WeaponTypes.MISSILE_LOCKING, facingRight);
		explosiveSprites.add(sprite);
		SoundEffectsManager.getManager().playLAUNCH_ROCKET(context);
		return sprite;
	}

	public FreeSprite addMissileLockingToEnemy(float x, float y, boolean facingRight, FreeSprite target) {
		FreeSprite sprite = addMissileLocking(x, y, facingRight);
		((MissileLockingSprite) sprite).setTarget(target);
		return sprite;
	}

	public FreeSprite addMissileLight(float x, float y, boolean facingRight) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.missileLight);
		bmp.add(BitmapManager.bombexploding);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new MissileSprite(explosiveSprites, this, spriteBmp, x, y, WeaponTypes.MISSILE_LIGHT, facingRight);
		explosiveSprites.add(sprite);
		SoundEffectsManager.getManager().playLAUNCH_ROCKET(context);
		return sprite;
	}

	public FreeSprite addMissile(float x, float y, boolean facingRight) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.missile);
		bmp.add(BitmapManager.bombexploding);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new MissileSprite(explosiveSprites, this, spriteBmp, x, y, WeaponTypes.MISSILE, facingRight);
		explosiveSprites.add(sprite);
		SoundEffectsManager.getManager().playLAUNCH_ROCKET(context);
		return sprite;
	}

	public FreeSprite addBullet(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.bmpBall);
		bmp.add(BitmapManager.bombexploding);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new GrenadeSprite(explosiveSprites, this, spriteBmp, x, y, WeaponTypes.BULLET);
		explosiveSprites.add(sprite);
		SoundEffectsManager.getManager().playTHROW_BOMB(context);
		return sprite;
	}

	public FreeSprite addSmoke(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.smoke);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new SmokeSprite(effectsSprite, this, spriteBmp, x, y);
		effectsSprite.add(sprite);
		return sprite;
	}

	public FreeSprite addGrenadeTriple(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.bombtriple);
		bmp.add(BitmapManager.bombexploding);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new GrenadeSprite(explosiveSprites, this, spriteBmp, x, y, WeaponTypes.BOMB_TRIPLE);
		explosiveSprites.add(sprite);
		SoundEffectsManager.getManager().playTHROW_BOMB(context);
		return sprite;
	}

	public FreeSprite addCapsule(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.bombcapsule);
		bmp.add(BitmapManager.bombexploding);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new CapsuleSprite(explosiveSprites, this, spriteBmp, x, y, WeaponTypes.BOMB_CAPSULES);
		explosiveSprites.add(sprite);
		SoundEffectsManager.getManager().playTHROW_BOMB(context);
		return sprite;
	}

	public FreeSprite addGrenadeSmall(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.bombsmall);
		bmp.add(BitmapManager.bombexploding);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new GrenadeSprite(explosiveSprites, this, spriteBmp, x, y, WeaponTypes.BOMB);
		explosiveSprites.add(sprite);
		SoundEffectsManager.getManager().playTHROW_BOMB(context);
		return sprite;
	}

	public FreeSprite addGrenade(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.bomb);
		bmp.add(BitmapManager.bombexploding);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new GrenadeSprite(explosiveSprites, this, spriteBmp, x, y, WeaponTypes.BOMB_BIG);
		explosiveSprites.add(sprite);
		SoundEffectsManager.getManager().playTHROW_BOMB(context);
		return sprite;
	}

	public FreeSprite addGrenadeImploding(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.bombtimer);
		bmp.add(BitmapManager.bombexploding);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 4, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new GrenadeImplodeSprite(explosiveSprites, this, spriteBmp, x, y, WeaponTypes.BOMB_IMPLODING);
		explosiveSprites.add(sprite);
		SoundEffectsManager.getManager().playTHROW_BOMB(context);
		return sprite;
	}

	public FreeSprite addFirstAidKit(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.bmpFirstAidKit);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		BoxSprite sprite = new BoxSprite(collectSprites, this, spriteBmp, x, y);
		sprite.addPointerSprite();
		collectSprites.add(sprite);
		return sprite;
	}

	public FreeSprite addBox(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.bmpBox);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new BoxSprite(freeSprites, this, spriteBmp, x, y);
		freeSprites.add(sprite);
		return sprite;
	}

	public void addBox2(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.bmpBox2);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		freeSprites.add(new BoxSprite(freeSprites, this, spriteBmp, x, y));
	}

	public void addBarrel(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.barrel);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		freeSprites.add(new BoxSprite(freeSprites, this, spriteBmp, x, y));
	}

	public void addPlanetStatic(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.planet1);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		staticSprites.add(new PlanetSprite(staticSprites, this, spriteBmp, x, y));
	}

	public FreeSprite addHookStatic(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.hook);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new StaticBoxSprite(this, spriteBmp, x, y);
		staticSprites.add(sprite);
		return sprite;
	}

	public FreeSprite addBoxStatic(float x, float y, int w, int h) {
		Bitmap bmpx = BitmapManager.getManager().createScaledBitmap(BitmapManager.bmpBox2, w, h);
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(bmpx);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new StaticBoxSprite(this, spriteBmp, x, y);
		staticSprites.add(sprite);
		return sprite;
	}

	public FreeSprite addGroundStaticNoShape(float x, float y, int w, int h, Bitmap bmpa) {
		Bitmap bmpx = BitmapManager.getManager().createScaledBitmap(bmpa, w, h);
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(bmpx);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new StaticSpriteNoShape(this, spriteBmp, x, y);
		staticSprites.add(sprite);
		return sprite;
	}

	public FreeSprite addPortalStatic(float x, float y, int w, int h) {
		Bitmap bmpx = BitmapManager.getManager().createScaledBitmap(BitmapManager.portal, w, h);
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(bmpx);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new StaticSpriteNoShape(this, spriteBmp, x, y);
		portalSprites.add(sprite);
		return sprite;
	}

	public FreeSprite addGroundBoxStatic(float x, float y, float hx, float hy) {
		FreeSprite sprite = new GroundBoxSprite(this, null, x, y, hx, hy);
		groundSprites.add(sprite);
		return sprite;
	}

	public void addBonusLifeBarSprite(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.lifeBarBonus);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new BonusLifeBarSprite(this, spriteBmp, x, y);
		nophysicsSprite.add(sprite);
	}

	public void addStagePassBarSprite(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.stagePassBar);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new StagePassBarSprite(this, spriteBmp, x, y);
		nophysicsSprite.add(sprite);
	}

	public void addPlayer(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(BitmapManager.player);
		bmp.add(BitmapManager.playerThrottle);
		bmp.add(BitmapManager.playerexploding);
		bmp.add(BitmapManager.playerportalout);
		bmp.add(BitmapManager.playerportalin);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 2 });
		colsrows.add(new int[] { 2, 2 });
		colsrows.add(new int[] { 3, 1 });
		colsrows.add(new int[] { 7, 1 });
		colsrows.add(new int[] { 7, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		playerSprite.add(new PlayerSprite(playerSprite, this, spriteBmp, x, y));
		((PlayerSprite) getPlayerSprite()).weapon = WeaponsManager.getManager().firstAvailableWeapon();
		((PlayerSprite) getPlayerSprite()).setFly(true);
		// ((PlayerSprite) getPlayerSprite()).hoverOnOff();
	}

	public void addEnemy(float x, float y, WeaponTypes wt, Boolean fly) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(WeaponsManager.getManager().getEnemyBitmapByWT(wt, fly));
		bmp.add(WeaponsManager.getManager().getEnemyThrottleBitmapByWT(wt, fly));
		bmp.add(WeaponsManager.getManager().getEnemyExplodingBitmapByWT(wt, fly));
		bmp.add(WeaponsManager.getManager().getEnemyBurningBitmapByWT(wt, fly));
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		colsrows.add(new int[] { 2, 2 });
		colsrows.add(new int[] { 3, 1 });
		colsrows.add(new int[] { 2, 2 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		EnemySprite e = new EnemySprite(enemySprites, this, spriteBmp, x, y);
		e.setWt(wt);
		e.setFly(fly);
		enemySprites.add(e);
	}

	protected void createSprites() {
		// ground
		addGroundBoxStatic(Constants.upperBoundxScreen * 0.5f, Constants.lowerBoundyScreen - Constants.setAsBoxhyScreen, Constants.upperBoundxScreen, Constants.setAsBoxhyScreen);
		// ceiling
		addGroundBoxStatic(Constants.upperBoundxScreen * 0.5f, Constants.upperBoundyScreen + Constants.setAsBoxhyScreen, Constants.upperBoundxScreen, Constants.setAsBoxhyScreen);
		// leftwall
		addGroundBoxStatic(Constants.lowerBoundxScreen - Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f, Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f);
		// rightwall
		addGroundBoxStatic(Constants.upperBoundxScreen + Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f, Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f);

		// addPlanet(this.getWidth() * 0.5f, this.getHeight() * 0.75f);
		// addBall(400, 150);
		// addBall(150, 400);
		// addBox(150, 150);
		// addBox2(500, 500);
		// addBox2(500, 150);
		createTrash();
		addPlayer(600, 100);
		createJointStuff();
		createNophysicSprites();
		sendEnemies();
		((BonusLifeBarSprite) getBonusLifeBarSprite()).resetTimer();
		BOSSTIME = false;
	}

	public void createJoint2Player(float x, float y) {
		FreeSprite b1 = addHookStatic(x, y);
		new Joint().createRevolute(b1, getPlayerSprite());
	}

	private void createJointStuff() {

		addGroundStaticNoShape(Constants.upperBoundxScreen * 0.5f, 47.5f, (int) (Constants.upperBoundxScreen), 95, BitmapManager.ground2);
		addGroundStaticNoShape(Constants.upperBoundxScreen * 0.5f, 12.5f, (int) (Constants.upperBoundxScreen), 25, BitmapManager.ground);

		ArrayList<Vec2> list = StageManager.getManager().stageBoxDesign();
		for (Vec2 vec2 : list) {
			addBoxStatic(vec2.x, vec2.y, 50, 50);
		}

		list = StageManager.getManager().stagePlanetDesign();
		for (Vec2 vec2 : list) {
			addPlanetStatic(vec2.x, vec2.y);

		}

		list = StageManager.getManager().stagePortalDesign();
		addPortalStatic(list.get(0).x, list.get(0).y, 200, 50).facingRigth = false;
		addPortalStatic(list.get(1).x, list.get(1).y, 200, 50).facingRigth = true;

		// FreeSprite b1 = addGround(450, 400, 50, 50);
		// FreeSprite b2 = addBox(500, 200);
		// new Joint().createRevolute(b1, b2);
		// FreeSprite b3 = addBox(600, 500);
		// FreeSprite b4 = addBox(800, 400);
		// new Joint().createPulley(b3, b4, 100, 100);
	}

	private void createTrash() {
		sendSpaceTrash();
	}

	private void sendSpaceTrash() {
		float x = new Random().nextFloat() * Constants.upperBoundxScreen;
		float y = new Random().nextFloat() * Constants.upperBoundyScreen;
		addBox(x, y);
		x = new Random().nextFloat() * Constants.upperBoundxScreen;
		y = new Random().nextFloat() * Constants.upperBoundyScreen;
		addBox(x, y);
		x = new Random().nextFloat() * Constants.upperBoundxScreen;
		y = new Random().nextFloat() * Constants.upperBoundyScreen;
		addBarrel(x, y);
	}

	private void createNophysicSprites() {
		addJoystick(120, 60);
		// float perc = 1;
		float perc = BitmapManager.getManager().getPerc();
		addBonusLifeBarSprite(PhysicsApplication.deviceWidth - 100 * perc, PhysicsApplication.deviceHeight - 50 * perc);
		addStagePassBarSprite(PhysicsApplication.deviceWidth - 270 * perc, PhysicsApplication.deviceHeight - 50 * perc);
		addPortalButton(PhysicsApplication.deviceWidth - 75 * perc, 330 * perc);
		addSwapWeaponButton(PhysicsApplication.deviceWidth - 350 * perc, 50 * perc);
		addFireButton(PhysicsApplication.deviceWidth - 120 * perc, 120 * perc);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		System.out.println("surfaceDestroyed");
		boolean retry = true;
		gameLoopThread.setRunning(false);
		timePause = new Date().getTime();
		while (retry) {
			try {
				gameLoopThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
		threadStarted = false;
		System.out.println("surfaceDestroyed:end");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("surfaceCreated");
		if (gameLoopThread.getState() == Thread.State.TERMINATED) {
			gameLoopThread = new GameLoopThread(this, holder);
			threadStarted = true;
			gameLoopThread.setRunning(true);
			gameLoopThread.start();
			resume();
		} else {
			threadStarted = true;
			gameLoopThread.setRunning(true);
			createSprites();
			gameLoopThread.start();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		System.out.println("surfaceChanged");
		if (!threadStarted) {
			threadStarted = true;
			gameLoopThread.setRunning(true);
			gameLoopThread.start();
		}
	}

	public GameView(final Context context) {
		super(context);
		this.context = context;
		holder = getHolder();
		holder.addCallback(this);
		gameLoopThread = new GameLoopThread(this, holder);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		try {
			synchronized (getHolder()) {
				if (canvas == null)
					return;

				long tstart = System.currentTimeMillis();
				long t = System.currentTimeMillis();

				Paint paint = new Paint();
				paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
				canvas.drawPaint(paint);

				update();
				String updateStr = "update:" + (System.currentTimeMillis() - t);

				t = System.currentTimeMillis();
				drawBackground(canvas);
				String drawBackground = "drawBackground:" + (System.currentTimeMillis() - t);

				// draw portalSprites
				t = System.currentTimeMillis();
				draw(canvas, portalSprites);
				String portalSprites = "portalSprites:" + (System.currentTimeMillis() - t);

				// draw statics, pull frees
				t = System.currentTimeMillis();
				draw(canvas, staticSprites);
				String staticSprites = "staticSprites:" + (System.currentTimeMillis() - t);

				// draw player
				t = System.currentTimeMillis();
				getPlayerSprite().onDraw(canvas);
				String PlayerSprite = "PlayerSprite:" + (System.currentTimeMillis() - t);

				// draw frees, push pull frees
				t = System.currentTimeMillis();
				draw(canvas, freeSprites);
				String freeSprites = "freeSprites:" + (System.currentTimeMillis() - t);

				// draw frees, push pull frees
				t = System.currentTimeMillis();
				draw(canvas, collectSprites);
				String collectSprites = "collectSprites:" + (System.currentTimeMillis() - t);

				// draw enemies
				t = System.currentTimeMillis();
				draw(canvas, enemySprites);
				String enemySprites = "enemySprites:" + (System.currentTimeMillis() - t);

				// draw explosives, push enemies
				t = System.currentTimeMillis();
				draw(canvas, explosiveSprites);
				String explosiveSprites = "explosiveSprites:" + (System.currentTimeMillis() - t);

				t = System.currentTimeMillis();
				draw(canvas, effectsSprite);
				String effectsSprite = "effectsSprite:" + (System.currentTimeMillis() - t);

				t = System.currentTimeMillis();
				draw(canvas, nophysicsSprite);
				String nophysicsSprite = "nophysicsSprite:" + (System.currentTimeMillis() - t);

				if (!endOfGame) {
					drawText("STAGE: " + (StageManager.getManager().currentStage + 1), canvas, 10, 25);
					drawText("SCORE: " + Constants.scoreTotal, canvas, 10, 50);
					drawText(newWeaponUnlockedMessage, canvas, 10, 75, 255 * NEXT_STAGE_WAIT_TIME / NEXT_STAGE_WAIT_TIME_MAX <= 0 ? 0 : 255 * NEXT_STAGE_WAIT_TIME / NEXT_STAGE_WAIT_TIME_MAX,
							Paint.Align.LEFT, NEXT_STAGE_WAIT_TIME % 2 == 0 ? Color.GREEN : Color.YELLOW);
					drawText(newStageUpMessage, canvas, 10, 100, 255 * NEXT_STAGE_WAIT_TIME / NEXT_STAGE_WAIT_TIME_MAX <= 0 ? 0 : 255 * NEXT_STAGE_WAIT_TIME / NEXT_STAGE_WAIT_TIME_MAX,
							Paint.Align.LEFT, NEXT_STAGE_WAIT_TIME % 2 == 0 ? Color.RED : Color.YELLOW);
				}

				int xd = 100;
				// drawText(staticSprites, canvas, 10, xd = xd + 25);
				// drawText(nophysicsSprite, canvas, 10, xd = xd + 25);
				// drawText(drawBackground, canvas, 10, xd = xd + 25);
				// drawText(updateStr, canvas, 10, xd = xd + 25);
				// drawText(PlayerSprite, canvas, 10, xd = xd + 25);
				// drawText(explosiveSprites, canvas, 10, xd = xd + 25);
				// drawText(freeSprites, canvas, 10, xd = xd + 25);
				// drawText(enemySprites, canvas, 10, xd = xd + 25);
				// drawText(collectSprites, canvas, 10, xd = xd + 25);
				// drawText(effectsSprite, canvas, 10, xd = xd + 25);
				// drawText(portalSprites, canvas, 10, xd = xd + 25);
				// drawText("total:" + (System.currentTimeMillis() - tstart),
				// canvas, 10, xd = xd + 25);
				// drawText("FPS:" + GameLoopThread.framesCountAvg, canvas, 10,
				// xd = xd + 25);

				if (!endOfGame) {
					drawTextStageStart(canvas);
					drawTextStageGameOver(canvas);
				} else {
					drawTextStageGameEnd(canvas);
				}

				for (TextDrawingPojo tdpojo : textDrawingPojoList) {
					if (tdpojo.alpha > 0) {
						drawText(tdpojo, canvas);
						tdpojo.alpha -= 5;
					} else {
						textDrawingPojoList.remove(tdpojo);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addTextDrawingPojo(TextDrawingPojo tdpojo) {
		textDrawingPojoList.add(tdpojo);
	}

	private void drawTextStageGameOver(Canvas canvas) {
		if (!idle) {
			return;
		}
		if (GAMEOVER_ALPHA <= GAMEOVER_ALPHA_MIN) {
			GAMEOVER_ALPHA = GAMEOVER_ALPHA_MIN;
			GAMEOVER_ALPHA_INCREMENT = 5;
		} else if (GAMEOVER_ALPHA >= GAMEOVER_ALPHA_MAX) {
			GAMEOVER_ALPHA = GAMEOVER_ALPHA_MAX;
			GAMEOVER_ALPHA_INCREMENT = -5;
		}
		GAMEOVER_ALPHA += GAMEOVER_ALPHA_INCREMENT;
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setTypeface(IntroActivity.tf);
		paint.setColor(Color.RED);
		int alpha = GAMEOVER_ALPHA;
		paint.setAlpha(alpha <= 0 ? 0 : alpha);
		paint.setTextSize(50);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText("GAME OVER", PhysicsApplication.deviceWidth * 0.5f, PhysicsApplication.deviceHeight * 0.5f, paint);
		paint.setTextSize(20);
		if (GAMEOVER_WAIT_TIME-- <= 0) {
			GAMEOVER_WAIT_TIME = 0;
			canvas.drawText("(Touch to Go to Main Menu)", PhysicsApplication.deviceWidth * 0.5f, PhysicsApplication.deviceHeight * 0.5f + 50, paint);
		}
	}

	private void drawTextStageGameEnd(Canvas canvas) {
		if (GAMEOVER_ALPHA <= GAMEOVER_ALPHA_MIN) {
			GAMEOVER_ALPHA = GAMEOVER_ALPHA_MIN;
			GAMEOVER_ALPHA_INCREMENT = 5;
		} else if (GAMEOVER_ALPHA >= GAMEOVER_ALPHA_MAX) {
			GAMEOVER_ALPHA = GAMEOVER_ALPHA_MAX;
			GAMEOVER_ALPHA_INCREMENT = -5;
		}
		GAMEOVER_ALPHA += GAMEOVER_ALPHA_INCREMENT;
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setTypeface(IntroActivity.tf);
		paint.setColor(Color.YELLOW);
		int alpha = GAMEOVER_ALPHA;
		paint.setAlpha(alpha <= 0 ? 0 : alpha);
		paint.setTextSize(50);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText("MISSION COMPLETE", PhysicsApplication.deviceWidth * 0.5f, PhysicsApplication.deviceHeight * 0.5f, paint);
		paint.setTextSize(20);
		canvas.drawText("Score " + Constants.scoreTotal, PhysicsApplication.deviceWidth * 0.5f, PhysicsApplication.deviceHeight * 0.5f + 25, paint);
		if (GAMEOVER_WAIT_TIME-- <= 0) {
			GAMEOVER_WAIT_TIME = 0;
			canvas.drawText("(Touch to Go to Main Menu)", PhysicsApplication.deviceWidth * 0.5f, PhysicsApplication.deviceHeight * 0.5f + 50, paint);
		}
	}

	private void drawTextStageStart(Canvas canvas) {
		if (!waitingForNextStage) {
			return;
		}
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setTypeface(IntroActivity.tf);
		paint.setColor(Color.RED);
		int alpha = 255 * NEXT_STAGE_WAIT_TIME / NEXT_STAGE_WAIT_TIME_MAX;
		paint.setAlpha(alpha <= 0 ? 0 : alpha);
		paint.setTextSize(50);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText("Stage:" + (StageManager.getManager().currentStage + 2), PhysicsApplication.deviceWidth * 0.5f, PhysicsApplication.deviceHeight * 0.5f, paint);
	}

	private void drawText(String text, Canvas canvas, float x, float y, int alpha, Align align, int... color) {
		if (alpha <= 0)
			return;

		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setTypeface(IntroActivity.tf);
		paint.setColor(color.length == 0 ? Color.WHITE : color[0]);
		paint.setAlpha(alpha <= 0 ? 0 : alpha);
		paint.setTextSize(20);
		paint.setTextAlign(align);
		canvas.drawText(text, x, y, paint);
	}

	private void drawText(String text, Canvas canvas, float x, float y) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		paint.setTypeface(IntroActivity.tf);
		paint.setColor(Color.WHITE);
		paint.setTextSize(25);
		// paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(text, x, y, paint);
	}

	private void drawText(TextDrawingPojo tdpojo, Canvas canvas) {
		if (tdpojo.alpha <= 0)
			return;

		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setTypeface(IntroActivity.tf);
		paint.setColor(tdpojo.color);
		paint.setAlpha(tdpojo.alpha <= 0 ? 0 : tdpojo.alpha);
		paint.setTextSize(20);
		paint.setTextAlign(tdpojo.align);

		canvas.drawText(tdpojo.text, tdpojo.x - Constants.extraWidthOffset, PhysicsApplication.deviceHeight - tdpojo.y + Constants.extraHeightOffset - getPlayerSprite().height - (255 - tdpojo.alpha),
				paint);

		tdpojo.alpha -= 5;
	}

	private void draw(Canvas canvas, ConcurrentLinkedQueue<FreeSprite> freeSprites2) {
		for (Iterator<FreeSprite> it2 = freeSprites2.iterator(); it2.hasNext();) {
			FreeSprite spritefree = it2.next();
			try {
				spritefree.onDraw(canvas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void applyPullPushOn(FreeSprite spriteExplosive, ConcurrentLinkedQueue<FreeSprite> freeSprites2, boolean ready) {
		for (Iterator<FreeSprite> it2 = freeSprites2.iterator(); it2.hasNext();) {
			FreeSprite spritefree = it2.next();
			applySinglePullPushOn(spriteExplosive, spritefree, ready);
		}
	}

	private void applySinglePullPushOn(FreeSprite spriteExplosive, FreeSprite spritefree, boolean ready) {
		try {
			if (!ready) {
				spriteExplosive.pull(spritefree);
			} else {
				spriteExplosive.push(spritefree);
			}
		} catch (Exception e) {
		}
	}

	private void applyPullOn(FreeSprite spriteStatic, ConcurrentLinkedQueue<FreeSprite> freeSprites2) {
		for (Iterator<FreeSprite> it2 = freeSprites2.iterator(); it2.hasNext();) {
			FreeSprite spritefree = it2.next();
			try {
				spriteStatic.pull(spritefree, spriteStatic.getBody().getPosition());
			} catch (Exception e) {
			}
		}
	}

	private void drawBackground(Canvas canvas) {

		Rect src = new Rect(shiftWidth, 0, BitmapManager.bmpBack.getWidth(), BitmapManager.bmpBack.getHeight());
		Rect dst = new Rect(0, 0, getWidth() - shiftWidth, getHeight());
		canvas.drawBitmap(BitmapManager.bmpBack, src, dst, null);

		src = new Rect(0, 0, shiftWidth, BitmapManager.bmpBack.getHeight());
		dst = new Rect(getWidth() - shiftWidth, 0, getWidth(), getHeight());
		canvas.drawBitmap(BitmapManager.bmpBack, src, dst, null);
		shiftWidth++;
		if (BitmapManager.bmpBack.getWidth() == shiftWidth) {
			shiftWidth = 0;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return tester(event);
		// return handleTouch(event);
	}

	private boolean tester(MotionEvent m) {
		int xLimitForJoystickTouch = (int) (PhysicsApplication.deviceWidth * 0.5f);
		int pointerCount = m.getPointerCount();

		for (int i = 0; i < pointerCount; i++) {
			int x = (int) m.getX(i);
			int y = (int) m.getY(i);
			int id = m.getPointerId(i);
			int action = m.getActionMasked();
			int actionIndex = m.getActionIndex();
			String actionString;

			switch (action) {
			case MotionEvent.ACTION_DOWN:
				actionString = "DOWN";
			case MotionEvent.ACTION_POINTER_DOWN:
				actionString = "DOWN PNTR";
				if (((ButtonSwapWeaponSprite) getSwapWeaponButtonSprite()).checkButtonTouch(x, PhysicsApplication.deviceHeight - y)) {
					actionString = "DOWN ButtonSwapWeaponSprite";
					buttonSwapEnabled = true;
					buttonSwapWeaponPointerId = id;
					// buttonFirePointerId = -1;
					((ButtonSwapWeaponSprite) getSwapWeaponButtonSprite()).onDown(x, PhysicsApplication.deviceHeight - y);
				} else if (((ButtonPortalSprite) getPortalButtonSprite()).checkButtonTouch(x, PhysicsApplication.deviceHeight - y)) {
					actionString = "DOWN ButtonSwapWeaponSprite";
					buttonSwapEnabled = true;
					buttonSwapWeaponPointerId = id;
					((ButtonPortalSprite) getPortalButtonSprite()).onDown(x, PhysicsApplication.deviceHeight - y);
				} else if (((ButtonFireSprite) getFireButtonSprite()).checkButtonTouch(x, PhysicsApplication.deviceHeight - y)) {
					actionString = "DOWN ButtonFireSprite";
					buttonFireEnabled = true;
					buttonFirePointerId = id;
					// buttonSwapWeaponPointerId = -1;
					((ButtonFireSprite) getFireButtonSprite()).onDown(x, PhysicsApplication.deviceHeight - y);
				} else if (xLimitForJoystickTouch >= x && !joystickEnabled && !endOfGame) {
					actionString = "DOWN JoystickSprite";
					joystickEnabled = true;
					// buttonFirePointerId = -1;
					// buttonSwapWeaponPointerId = -1;
					((JoystickSprite) getJoystickSprite()).onDown(x, PhysicsApplication.deviceHeight - y);
				}
				break;
			case MotionEvent.ACTION_UP:
				actionString = "UP";
			case MotionEvent.ACTION_POINTER_UP:
				actionString = "UP PNTR";
				if (endOfGame && GAMEOVER_WAIT_TIME == 0) {
					finishGame = true;
					break;
				}
				if (idle && GAMEOVER_WAIT_TIME == 0) {
					finishGame = true;
					break;
					// ((PlayerSprite) getPlayerSprite()).setAlive(true);
					// ((PlayerSprite) getPlayerSprite()).restartSprite(x +
					// Constants.extraWidthOffset,
					// PhysicsApplication.deviceHeight - y +
					// Constants.extraHeightOffset);
				}

				if (buttonFireEnabled && id == buttonFirePointerId) {
					actionString = "UP ButtonFireSprite";
					((ButtonFireSprite) getFireButtonSprite()).onUp(x, PhysicsApplication.deviceHeight - y);
					buttonFireEnabled = false;
					// buttonFirePointerId = -1;
				} else if (buttonSwapEnabled && id == buttonSwapWeaponPointerId) {
					actionString = "UP ButtonSwapWeaponSprite";
					((ButtonSwapWeaponSprite) getSwapWeaponButtonSprite()).onUp(x, PhysicsApplication.deviceHeight - y);
					((ButtonPortalSprite) getPortalButtonSprite()).onUp(x, PhysicsApplication.deviceHeight - y);
					buttonSwapEnabled = false;
					// buttonSwapWeaponPointerId = -1;
				} else if (joystickEnabled && (actionIndex == 0)) {
					actionString = "UP JoystickSprite";
					((JoystickSprite) getJoystickSprite()).onUp(x, PhysicsApplication.deviceHeight - y);
					joystickEnabled = false;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				actionString = "MOVE";
				if (buttonFireEnabled && id == buttonFirePointerId) {
					actionString = "MOVE ButtonFireSprite";
					((ButtonFireSprite) getFireButtonSprite()).onMove(x, PhysicsApplication.deviceHeight - y);
				} else if (buttonSwapEnabled && id == buttonSwapWeaponPointerId) {
					actionString = "MOVE ButtonSwapWeaponSprite";
				} else if (joystickEnabled) {
					actionString = "MOVE JoystickSprite";
					((JoystickSprite) getJoystickSprite()).onMove(x, PhysicsApplication.deviceHeight - y);
				}
				break;
			default:
				actionString = "";
			}

			String touchStatus = "Action: " + actionString + " Index: " + actionIndex + " ID: " + id + " X: " + x + " Y: " + (PhysicsApplication.deviceHeight - y);
			// System.out.println(touchStatus);
		}
		return true;
	}

	public void update() {
		try {
			// PhysicsActivity.context.updateScore();
			PhysicsActivity.context.mWorld.update();
			checkPhysicalEffects();
			checkCollecItems();
			checkPortalPointsNearby();
			checkSpaceTrash();
			checkGameStatus();
			checkForGrenadeTriple();
			checkForGrenadeCapsule();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkForGrenadeTriple() {
		if (PhysicsActivity.context.onGrenadeTriple) {
			if (PhysicsActivity.context.waitGrenadeTripleInFPSTicking-- == 0) {
				if (PhysicsActivity.context.countGrenadeTriple++ < 3) {
					((PlayerSprite) getPlayerSprite()).fireGrenadeTriple();
					PhysicsActivity.context.waitGrenadeTripleInFPSTicking = PhysicsActivity.context.waitGrenadeTripleInFPS;
				} else {
					PhysicsActivity.context.onGrenadeTriple = false;
				}
			}
		}
	}

	public void checkForGrenadeCapsule() {
		if (PhysicsActivity.context.onGrenadeCapsule) {
			if (PhysicsActivity.context.waitGrenadeCapsuleInFPSTicking-- == 0) {
				if (PhysicsActivity.context.countGrenadeCapsule++ < 5) {
					((PlayerSprite) getPlayerSprite()).fireCapsule();
					PhysicsActivity.context.waitGrenadeCapsuleInFPSTicking = PhysicsActivity.context.waitGrenadeCapsuleInFPS;
					PhysicsActivity.context.angleGrenadeCapsule += 45.0f * 0.25f;
				} else {
					PhysicsActivity.context.onGrenadeCapsule = false;
				}
			}
		}
	}

	private void checkPhysicalEffects() {

		// draw statics, pull frees
		for (Iterator<FreeSprite> it = staticSprites.iterator(); it.hasNext();) {
			FreeSprite spriteStatic = it.next();
			if (spriteStatic instanceof PlanetSprite) {
				applyPullOn(spriteStatic, freeSprites);
				applyPullOn(spriteStatic, enemySprites);
				applyPullOn(spriteStatic, playerSprite);
				applyPullOn(spriteStatic, collectSprites);
			}
		}

		// draw explosives, push enemies
		for (Iterator<FreeSprite> it = explosiveSprites.iterator(); it.hasNext();) {
			GrenadeSprite spriteExplosive = (GrenadeSprite) it.next();
			boolean ready = spriteExplosive.readyToExplode();
			applyPullPushOn(spriteExplosive, freeSprites, ready);
			applyPullPushOn(spriteExplosive, enemySprites, ready);
			applyPullPushOn(spriteExplosive, playerSprite, ready);
			applyPullPushOn(spriteExplosive, collectSprites, ready);
		}

		// draw frees, push pull frees
		// for (Iterator<FreeSprite> it2 = freeSprites.iterator();
		// it2.hasNext();) {
		// FreeSprite spritefree = it2.next();
		// try {
		// if (((PlayerSprite) getPlayerSprite()).powerOn) {
		// getPlayerSprite().pull(spritefree);
		// } else if (!((PlayerSprite) getPlayerSprite()).powerOn &&
		// ((PlayerSprite) getPlayerSprite()).scatter) {
		// getPlayerSprite().push(spritefree);
		// }
		// if (((PlayerSprite) getPlayerSprite()).powerPush) {
		// ((PlayerSprite) getPlayerSprite()).pushPower(spritefree);
		// }
		// } catch (Exception e) {
		// }
		// }
		// ((PlayerSprite) getPlayerSprite()).powerPush = false;
		// if (getPlayerSprite() != null && (!((PlayerSprite)
		// getPlayerSprite()).powerOn && ((PlayerSprite)
		// getPlayerSprite()).scatter)) {
		// ((PlayerSprite) getPlayerSprite()).scatter = false;
		// }

	}

	private void checkCollecItems() {

		// draw explosives, push enemies
		for (Iterator<FreeSprite> it = collectSprites.iterator(); it.hasNext();) {
			FreeSprite fs = it.next();
			if (getPlayerSprite().getDistance(fs) < 10) {
				int gain = (int) (((PlayerSprite) getPlayerSprite()).life_MAX * 0.25f);
				((PlayerSprite) getPlayerSprite()).lifeGain(gain);
				fs.killSprite();

				float killpointx = (float) (getPlayerSprite().x + Math.random() * 20);
				float killpointy = (float) (getPlayerSprite().y + Math.random() * 20);
				Constants.scoreTotal += gain;

				addTextDrawingPojo(new TextDrawingPojo("+" + gain, killpointx, killpointy, 255));
			}
		}

		// draw frees, push pull frees
		// for (Iterator<FreeSprite> it2 = freeSprites.iterator();
		// it2.hasNext();) {
		// FreeSprite spritefree = it2.next();
		// try {
		// if (((PlayerSprite) getPlayerSprite()).powerOn) {
		// getPlayerSprite().pull(spritefree);
		// } else if (!((PlayerSprite) getPlayerSprite()).powerOn &&
		// ((PlayerSprite) getPlayerSprite()).scatter) {
		// getPlayerSprite().push(spritefree);
		// }
		// if (((PlayerSprite) getPlayerSprite()).powerPush) {
		// ((PlayerSprite) getPlayerSprite()).pushPower(spritefree);
		// }
		// } catch (Exception e) {
		// }
		// }
		// ((PlayerSprite) getPlayerSprite()).powerPush = false;
		// if (getPlayerSprite() != null && (!((PlayerSprite)
		// getPlayerSprite()).powerOn && ((PlayerSprite)
		// getPlayerSprite()).scatter)) {
		// ((PlayerSprite) getPlayerSprite()).scatter = false;
		// }

	}

	private void checkPortalPointsNearby() {

		// draw explosives, push enemies
		for (Iterator<FreeSprite> it = portalSprites.iterator(); it.hasNext();) {
			FreeSprite fs = it.next();
			if (getPlayerSprite().getDistance(fs) < 1.5f) {
				((PlayerSprite) getPlayerSprite()).portalSpriteAuto();
			}
		}

	}

	private void checkSpaceTrash() {
		// System.out.println("checkSpaceTrash():freeSprites.size():" +
		// freeSprites.size());
		if (freeSprites.size() < 5) {
			sendSpaceTrash();
		}
	}

	private void sendEnemies() {
		if (newGame) {
			StageManager.getManager().currentStage = 0;
			Constants.scoreTotal = 0;
		} else {
			StageManager.getManager().currentStage = Integer.parseInt((String) IntroActivity.dbDBWriteUtil.getBestScore(1));
			Constants.scoreTotal = Integer.parseInt((String) IntroActivity.dbDBWriteUtil.getBestScore(0));
		}
		// StageManager.getManager().currentStage = 14;
		addStageEnemies();
	}

	private void addStageEnemies() {
		ArrayList<Integer> stageData = StageManager.getManager().getStage();
		int enemyLevel;
		for (int i = 0; i < stageData.size() - 1; i++) {
			enemyLevel = i + 1;
			for (int j = 0; j < stageData.get(i); j++) {
				addEnemy(WeaponsManager.getManager().getWTByEnemyLevel(enemyLevel), WeaponsManager.getManager().getFlyByEnemyLevel(enemyLevel));
			}
		}
		Constants.scoreToPassTheStage = stageData.get(stageData.size() - 1);

	}

	public void addEnemy(WeaponTypes wt, Boolean fly) {
		addEnemy(50 + new Random().nextInt((int) Constants.upperBoundxScreen - 50), Constants.upperBoundyScreen, wt, fly);
	}

	private void checkGameStatus() {
		if (endOfGame) {
			// StageManager.getManager().currentStage = -1;
			// Constants.scoreTotal = 0;
			// endOfGame = false;
			// nextStage();

			addTextDrawingPojo(new TextDrawingPojo("MISSION COMPLETE", (float) (Math.random() * Constants.upperBoundxScreen), (float) (Math.random() * Constants.upperBoundyScreen), 255));
			addTextDrawingPojo(new TextDrawingPojo("M", (float) (Math.random() * Constants.upperBoundxScreen), (float) (Math.random() * Constants.upperBoundyScreen), 255));
			addTextDrawingPojo(new TextDrawingPojo("I", (float) (Math.random() * Constants.upperBoundxScreen), (float) (Math.random() * Constants.upperBoundyScreen), 255));
			addTextDrawingPojo(new TextDrawingPojo("S", (float) (Math.random() * Constants.upperBoundxScreen), (float) (Math.random() * Constants.upperBoundyScreen), 255));
			addTextDrawingPojo(new TextDrawingPojo("O", (float) (Math.random() * Constants.upperBoundxScreen), (float) (Math.random() * Constants.upperBoundyScreen), 255));
			addTextDrawingPojo(new TextDrawingPojo("N", (float) (Math.random() * Constants.upperBoundxScreen), (float) (Math.random() * Constants.upperBoundyScreen), 255));
			addTextDrawingPojo(new TextDrawingPojo("C", (float) (Math.random() * Constants.upperBoundxScreen), (float) (Math.random() * Constants.upperBoundyScreen), 255));
			addTextDrawingPojo(new TextDrawingPojo("P", (float) (Math.random() * Constants.upperBoundxScreen), (float) (Math.random() * Constants.upperBoundyScreen), 255));
			addTextDrawingPojo(new TextDrawingPojo("L", (float) (Math.random() * Constants.upperBoundxScreen), (float) (Math.random() * Constants.upperBoundyScreen), 255));
			addTextDrawingPojo(new TextDrawingPojo("E", (float) (Math.random() * Constants.upperBoundxScreen), (float) (Math.random() * Constants.upperBoundyScreen), 255));
			addTextDrawingPojo(new TextDrawingPojo("T", (float) (Math.random() * Constants.upperBoundxScreen), (float) (Math.random() * Constants.upperBoundyScreen), 255));

		} else if (!idle && !((PlayerSprite) getPlayerSprite()).isAlive()) {
			GAMEOVER_WAIT_TIME = 100;
			newStagePointx = (float) (getPlayerSprite().x + +Math.random() * 20);
			newStagePointy = (float) (getPlayerSprite().y + +Math.random() * 20);

			addTextDrawingPojo(new TextDrawingPojo("Total Score " + (Constants.scoreTotal - Constants.scoreStage), newStagePointx, newStagePointy, 255));

			idleGame();
			SoundEffectsManager.getManager().playGAME_OVER(context);
		} else if (!waitingForNextStage && Constants.scoreToPassTheStage <= Constants.scoreStage) {

			if (!BOSSTIME && (StageManager.getManager().currentStage + 1) % 5 == 0) {
				for (FreeSprite enmy : enemySprites) {
					enmy.destroySprite();
				}
				BOSSTIME = true;
				if (StageManager.getManager().currentStage + 1 == 5) {
					addEnemy(WeaponTypes.BOSS1, true);
				} else if (StageManager.getManager().currentStage + 1 == 10) {
					addEnemy(WeaponTypes.BOSS2, true);
				} else if (StageManager.getManager().currentStage + 1 == 15) {
					addEnemy(WeaponTypes.BOSS3, true);
				}
				Constants.scoreStage = Constants.scoreToPassTheStage - 1;
				return;
			}

			if (isGameEnd()) {
				endOfGame = true;
				GAMEOVER_WAIT_TIME = 100;
			} else {
				waitingForNextStage = true;
			}
			NEXT_STAGE_WAIT_TIME = NEXT_STAGE_WAIT_TIME_MAX;
			newStagePointx = (float) (getPlayerSprite().x + +Math.random() * 20);
			newStagePointy = (float) (getPlayerSprite().y + +Math.random() * 20);

			Constants.scoreLifeBonus = (int) (((PlayerSprite) getPlayerSprite()).getBonusScoreLife() * 10);
			Constants.scoreTimeBonus = (int) (((BonusLifeBarSprite) getBonusLifeBarSprite()).getBonusScoreSeconds() * 1);
			Constants.scoreStageBonus = (StageManager.getManager().currentStage + 1) * 1000;
			Constants.scoreTotal += Constants.scoreTimeBonus + Constants.scoreLifeBonus + Constants.scoreStageBonus;

			addTextDrawingPojo(new TextDrawingPojo("+" + Constants.scoreLifeBonus, newStagePointx + 10, newStagePointy + 35, 255));
			addTextDrawingPojo(new TextDrawingPojo("+" + Constants.scoreTimeBonus, newStagePointx + 20, newStagePointy + 60, 255));
			addTextDrawingPojo(new TextDrawingPojo("+" + Constants.scoreStageBonus, newStagePointx + 30, newStagePointy + 85, 255));

			int levelPrev = Integer.parseInt((String) IntroActivity.dbDBWriteUtil.getBestScore(1));
			IntroActivity.dbDBWriteUtil.updateScoreIfNewBestAchieved("" + Constants.scoreTotal, "" + new Date().getTime(), "" + (StageManager.getManager().currentStage + 1));
			((PlayerSprite) getPlayerSprite()).collectBonusLife();
			newWeaponUnlockedMessage = "";
			WeaponTypes wt = WeaponsManager.getManager().isNewWeaponUnlocked((StageManager.getManager().currentStage + 1) > levelPrev);
			if (wt != null) {
				String str = WeaponsManager.getWTInfo(wt);
				newWeaponUnlockedMessage = "NEW WEAPON UNLOCKED! " + str.subSequence(0, str.indexOf("("));
				SoundEffectsManager.getManager().playNEW_WEAPON(context);
			}
			newStageUpMessage = "";
			if ((StageManager.getManager().currentStage + 1) > levelPrev) {
				newStageUpMessage = "STAGE UP!";
				SoundEffectsManager.getManager().playSTAGE_UP(context);
			}

			if (!BOSSTIME) {
				for (FreeSprite enmy : enemySprites) {
					enmy.destroySprite();
				}
			}

		} else if (waitingForNextStage) {
			NEXT_STAGE_WAIT_TIME--;
			if (NEXT_STAGE_WAIT_TIME <= 0) {
				waitingForNextStage = false;
				BOSSTIME = false;
				// NEXT_STAGE_WAIT_TIME = NEXT_STAGE_WAIT_TIME_MAX;
				nextStage();
				((BonusLifeBarSprite) getBonusLifeBarSprite()).resetTimer();
				SoundEffectsManager.getManager().playSTAGE_START(context);
			}
		}

		if (finishGame) {
			finishGame();
		}
	}

	public FreeSprite getClosestEnemy() {
		for (FreeSprite enmy : enemySprites) {
			if (!((EnemySprite) enmy).killed)
				return enmy;
		}
		return null;
	}

	private boolean isGameEnd() {
		return StageManager.getManager().currentStage + 1 == StageManager.stageList.size();
	}

	private void nextStage() {

		if (!StageManager.getManager().incrementStage()) {
			endOfGame = true;
			return;
		}
		addStageEnemies();
		Constants.scoreStage = 0;
	}

	public void idleGame() {
		idle = true;
	}

	public void resumeIdleGame() {
		idle = false;
	}

	public void stopGame() {
		gameLoopThread.setRunning(false);
	}

	public void finishGame() {
		SoundEffectsManager.stopSound();
		System.out.println("finishGame()");
		gameLoopThread.setRunning(false);
		releaseBitmaps();
		Intent intent = new Intent(context, IntroActivity.class);
		context.startActivity(intent);
		((PhysicsActivity) context).finish();
	}

	private void releaseBitmaps() {
		releaseBitmapsOn(freeSprites);
		releaseBitmapsOn(staticSprites);
		releaseBitmapsOn(enemySprites);
		releaseBitmapsOn(explosiveSprites);
		releaseBitmapsOn(groundSprites);
	}

	private void releaseBitmapsOn(ConcurrentLinkedQueue<FreeSprite> freeSprites2) {
		for (FreeSprite z : freeSprites2) {
			z.freeBitmaps();
		}
		freeSprites2.clear();
	}

	public void togglepauseresume() {
		if (gameLoopThread.isSleeping())
			resume();
		else
			pause();
	}

	public void pause() {
		try {
			if (gameLoopThread.isSleeping())
				return;
			synchronized (gameLoopThread) {
				timePause = new Date().getTime();
				gameLoopThread.setSleeping(true);
			}
		} catch (Exception e) {
		}
	}

	public void resume() {
		try {
			if (!gameLoopThread.isSleeping())
				return;

			synchronized (gameLoopThread) {
				timeResumed = new Date().getTime();
				gameLoopThread.setSleeping(false);
				gameLoopThread.notify();
			}
		} catch (Exception e) {
		}
		if (r != null) {
			synchronized (r) {
				r.notify();
			}
		}
	}

	public boolean updateScore(WeaponTypes wt, boolean fly) {
		int bns = WeaponsManager.getManager().getBonusByEnemyWT(wt, fly);
		Constants.scoreTotal += bns;
		Constants.scoreStage += bns;
		return Constants.scoreToPassTheStage > Constants.scoreStage;
	}

	public boolean sentFirstAidKit() {
		return new Random().nextFloat() * 100.0f < 40.0f;
		// return true;
	}

}
