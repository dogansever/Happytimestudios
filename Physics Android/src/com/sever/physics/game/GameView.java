package com.sever.physics.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

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
import com.sever.physics.game.sprites.BonusLifeBarSprite;
import com.sever.physics.game.sprites.BoxSprite;
import com.sever.physics.game.sprites.ButtonFireSprite;
import com.sever.physics.game.sprites.ButtonPortalSprite;
import com.sever.physics.game.sprites.ButtonSwapWeaponSprite;
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
import com.sever.physics.game.utils.BitmapManager;
import com.sever.physics.game.utils.Constants;
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
	public ConcurrentLinkedQueue<FreeSprite> staticSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> groundSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> enemySprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> playerSprite = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> nophysicsSprite = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> effectsSprite = new ConcurrentLinkedQueue<FreeSprite>();
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

	float newStagePointx = 0;
	float newStagePointy = 0;
	private int alphaBonusLife = 0;
	private int alphaBonusTime = 0;
	private int alphaBonusStage = 0;
	private int alphaStageEndGameOver = 0;
	private String newWeaponUnlockedMessage = "";
	private String newStageUpMessage = "";

	public FreeSprite getBonusLifeBarSprite() {
		return (FreeSprite) this.nophysicsSprite.toArray()[3];
	}

	public FreeSprite getPortalButtonSprite() {
		return (FreeSprite) this.nophysicsSprite.toArray()[5];
	}

	public FreeSprite getSwapWeaponButtonSprite() {
		return (FreeSprite) this.nophysicsSprite.toArray()[2];
	}

	public FreeSprite getFireButtonSprite() {
		return (FreeSprite) this.nophysicsSprite.toArray()[1];
	}

	public FreeSprite getJoystickSprite() {
		return (FreeSprite) this.nophysicsSprite.toArray()[0];
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
		SoundEffectsManager.getManager().playLAUNCH_ROCKET();
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
		SoundEffectsManager.getManager().playLAUNCH_ROCKET();
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
		SoundEffectsManager.getManager().playLAUNCH_ROCKET();
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
		SoundEffectsManager.getManager().playTHROW_BOMB();
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
		SoundEffectsManager.getManager().playTHROW_BOMB();
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
		SoundEffectsManager.getManager().playTHROW_BOMB();
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
		SoundEffectsManager.getManager().playTHROW_BOMB();
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
		SoundEffectsManager.getManager().playTHROW_BOMB();
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
		Bitmap bmpx = BitmapManager.getManager().createScaledBitmap(BitmapManager.ground, w, h);
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(bmpx);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new StaticBoxSprite(this, spriteBmp, x, y);
		staticSprites.add(sprite);
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
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 2 });
		colsrows.add(new int[] { 2, 2 });
		colsrows.add(new int[] { 3, 1 });
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
	}

	public void createJoint2Player(float x, float y) {
		FreeSprite b1 = addHookStatic(x, y);
		new Joint().createRevolute(b1, getPlayerSprite());
	}

	private void createJointStuff() {
		addBoxStatic(Constants.upperBoundxScreen * 0.5f, 25.0f, (int) (Constants.upperBoundxScreen), 50);

		addBoxStatic(950, 275, 50, 50);
		addBoxStatic(125, 200, 50, 50);
		addBoxStatic(125, 450, 50, 50);

		addBoxStatic(325, 350, 50, 50);
		addBoxStatic(1125, 350, 50, 50);
		addBoxStatic(825, 550, 50, 50);

		addBoxStatic(Constants.upperBoundxScreen - 25, 125, 50, 50);
		addBoxStatic(Constants.upperBoundxScreen - 25, 75, 50, 50);
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
		addBox2(x, y);
		x = new Random().nextFloat() * Constants.upperBoundxScreen;
		y = new Random().nextFloat() * Constants.upperBoundyScreen;
		addBox(x, y);
		x = new Random().nextFloat() * Constants.upperBoundxScreen;
		y = new Random().nextFloat() * Constants.upperBoundyScreen;
		addBarrel(x, y);
	}

	private void createNophysicSprites() {
		addJoystick(120, 60);
		float perc = BitmapManager.getManager().getPerc();
		addFireButton(PhysicsApplication.deviceWidth - 100 * perc, 120 * perc);
		addSwapWeaponButton(PhysicsApplication.deviceWidth - 50 * perc, 300 * perc);
		addBonusLifeBarSprite(PhysicsApplication.deviceWidth - 100 * perc, PhysicsApplication.deviceHeight - 50 * perc);
		addStagePassBarSprite(PhysicsApplication.deviceWidth - 250 * perc, PhysicsApplication.deviceHeight - 50 * perc);
		addPortalButton(PhysicsApplication.deviceWidth - 50 * perc, 450 * perc);
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
				if(canvas == null)
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

				drawText("STAGE: " + (StageManager.getManager().currentStage + 1), canvas, 10, 25);
				drawText("SCORE: " + Constants.scoreTotal, canvas, 10, 50);
				drawText(newWeaponUnlockedMessage, canvas, 10, 75, 255 * NEXT_STAGE_WAIT_TIME / NEXT_STAGE_WAIT_TIME_MAX <= 0 ? 0 : 255 * NEXT_STAGE_WAIT_TIME / NEXT_STAGE_WAIT_TIME_MAX,
						Paint.Align.LEFT, Color.GREEN);
				drawText(newStageUpMessage, canvas, 10, 100, 255 * NEXT_STAGE_WAIT_TIME / NEXT_STAGE_WAIT_TIME_MAX <= 0 ? 0 : 255 * NEXT_STAGE_WAIT_TIME / NEXT_STAGE_WAIT_TIME_MAX, Paint.Align.LEFT,
						Color.YELLOW);

				int xd = 100;
				// drawText(staticSprites, canvas, 10, xd = xd + 25);
				// drawText(nophysicsSprite, canvas, 10, xd = xd + 25);
				// drawText(drawBackground, canvas, 10, xd = xd + 25);
				// drawText(updateStr, canvas, 10, xd = xd + 25);
				// drawText(PlayerSprite, canvas, 10, xd = xd + 25);
				// drawText(explosiveSprites, canvas, 10, xd = xd + 25);
				// drawText(freeSprites, canvas, 10, xd = xd + 25);
				// drawText(enemySprites, canvas, 10, xd = xd + 25);
				// drawText("total:" + (System.currentTimeMillis() - tstart),
				// canvas, 10, xd = xd + 25);
				drawText("FPS:" + GameLoopThread.framesCountAvg, canvas, 10, xd = xd + 25);

				drawText("+" + Constants.scoreLifeBonus, canvas, newStagePointx - Constants.extraWidthOffset, -25 + PhysicsApplication.deviceHeight - newStagePointy + Constants.extraHeightOffset
						- getPlayerSprite().height - (255 - alphaBonusLife), alphaBonusLife, Paint.Align.CENTER);
				drawText("+" + Constants.scoreTimeBonus, canvas, 25 + newStagePointx - Constants.extraWidthOffset, -50 + PhysicsApplication.deviceHeight - newStagePointy + Constants.extraHeightOffset
						- getPlayerSprite().height - (255 - alphaBonusTime), alphaBonusTime - 20, Paint.Align.CENTER);
				drawText("+" + Constants.scoreStageBonus, canvas, 50 + newStagePointx - Constants.extraWidthOffset, -75 + PhysicsApplication.deviceHeight - newStagePointy
						+ Constants.extraHeightOffset - getPlayerSprite().height - (255 - alphaBonusStage), alphaBonusStage - 40, Paint.Align.CENTER);
				drawText("Total Score " + Constants.scoreTotal, canvas, newStagePointx - Constants.extraWidthOffset, 0 + PhysicsApplication.deviceHeight - newStagePointy + Constants.extraHeightOffset
						- getPlayerSprite().height - (255 - alphaStageEndGameOver), alphaStageEndGameOver, Paint.Align.CENTER);

				drawTextStageStart(canvas);
				drawTextStageGameOver(canvas);

				if (alphaStageEndGameOver > 0)
					alphaStageEndGameOver -= 5;
				if (alphaBonusStage > 0)
					alphaBonusStage -= 5;
				if (alphaBonusTime > 0)
					alphaBonusTime -= 5;
				if (alphaBonusLife > 0)
					alphaBonusLife -= 5;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private void draw(Canvas canvas, ConcurrentLinkedQueue<FreeSprite> freeSprites2) {
		for (Iterator<FreeSprite> it2 = freeSprites2.iterator(); it2.hasNext();) {
			FreeSprite spritefree = it2.next();
			try {
				spritefree.onDraw(canvas);
			} catch (Exception e) {
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
				} else if (!joystickEnabled) {
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
				if (idle && GAMEOVER_WAIT_TIME == 0) {
					finishGame = true;
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
			checkSpaceTrash();
			checkGameStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkPhysicalEffects() {

		// draw statics, pull frees
		// for (Iterator<FreeSprite> it = staticSprites.iterator();
		// it.hasNext();) {
		// FreeSprite spriteStatic = it.next();
		// applyPullOn(spriteStatic, freeSprites);
		// applyPullOn(spriteStatic, explosiveSprites);
		// applyPullOn(spriteStatic, enemySprites);
		// }

		// draw explosives, push enemies
		for (Iterator<FreeSprite> it = explosiveSprites.iterator(); it.hasNext();) {
			GrenadeSprite spriteExplosive = (GrenadeSprite) it.next();
			boolean ready = spriteExplosive.readyToExplode();
			applyPullPushOn(spriteExplosive, freeSprites, ready);
			applyPullPushOn(spriteExplosive, enemySprites, ready);
			applyPullPushOn(spriteExplosive, playerSprite, ready);
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

	private void checkSpaceTrash() {
		// System.out.println("checkSpaceTrash():freeSprites.size():" +
		// freeSprites.size());
		if (freeSprites.size() < 5) {
			sendSpaceTrash();
		}
	}

	private void sendEnemies() {
		StageManager.getManager().currentStage = Integer.parseInt((String) IntroActivity.dbDBWriteUtil.getBestScore(1));
		Constants.scoreTotal = Integer.parseInt((String) IntroActivity.dbDBWriteUtil.getBestScore(0));
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

		if (!idle && endOfGame) {
			// ((ButtonSwapWeaponSprite)
			// getSwapWeaponButtonSprite()).deactivate();
			// ((ButtonFireSprite) getFireButtonSprite()).deactivate();
		} else if (!idle && !((PlayerSprite) getPlayerSprite()).isAlive()) {
			alphaStageEndGameOver = 255;
			GAMEOVER_WAIT_TIME = 100;
			newStagePointx = (float) (getPlayerSprite().x + +Math.random() * 20);
			newStagePointy = (float) (getPlayerSprite().y + +Math.random() * 20);
			idleGame();
		} else if (!waitingForNextStage && Constants.scoreToPassTheStage <= Constants.scoreStage) {
			waitingForNextStage = true;
			NEXT_STAGE_WAIT_TIME = NEXT_STAGE_WAIT_TIME_MAX;
			newStagePointx = (float) (getPlayerSprite().x + +Math.random() * 20);
			newStagePointy = (float) (getPlayerSprite().y + +Math.random() * 20);
			alphaBonusTime = alphaBonusStage = alphaBonusLife = 255;

			Constants.scoreLifeBonus = (int) (((PlayerSprite) getPlayerSprite()).getBonusScoreLife() * 10);
			Constants.scoreTimeBonus = (int) (((BonusLifeBarSprite) getBonusLifeBarSprite()).getBonusScoreSeconds() * 1);
			Constants.scoreStageBonus = (StageManager.getManager().currentStage + 1) * 1000;
			Constants.scoreTotal += Constants.scoreTimeBonus + Constants.scoreLifeBonus + Constants.scoreStageBonus;

			int levelPrev = Integer.parseInt((String) IntroActivity.dbDBWriteUtil.getBestScore(1));
			IntroActivity.dbDBWriteUtil.updateScoreIfNewBestAchieved("" + Constants.scoreTotal, "" + new Date().getTime(), "" + (StageManager.getManager().currentStage + 1));
			((PlayerSprite) getPlayerSprite()).collectBonusLife();
			newWeaponUnlockedMessage = "";
			if (WeaponsManager.getManager().isNewWeaponUnlocked((StageManager.getManager().currentStage + 1) > levelPrev)) {
				newWeaponUnlockedMessage = "NEW WEAPON UNLOCKED!";
			}
			newStageUpMessage = "";
			if ((StageManager.getManager().currentStage + 1) > levelPrev) {
				newStageUpMessage = "STAGE UP!";
			}

		} else if (waitingForNextStage) {
			NEXT_STAGE_WAIT_TIME--;
			if (NEXT_STAGE_WAIT_TIME <= 0) {
				waitingForNextStage = false;
				// NEXT_STAGE_WAIT_TIME = NEXT_STAGE_WAIT_TIME_MAX;
				nextStage();
				((BonusLifeBarSprite) getBonusLifeBarSprite()).resetTimer();
			}
		} else if (finishGame) {
			finishGame();
		}
	}

	private void nextStage() {
		for (FreeSprite enmy : enemySprites) {
			enmy.destroySprite();
		}

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

	private void finishGame() {
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
}
