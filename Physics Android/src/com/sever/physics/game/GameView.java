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
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sever.physic.IntroActivity;
import com.sever.physic.PhysicsActivity;
import com.sever.physic.StageEndActivity;
import com.sever.physics.game.sprites.BallSprite;
import com.sever.physics.game.sprites.BoxSprite;
import com.sever.physics.game.sprites.BulletSprite;
import com.sever.physics.game.sprites.EnemySprite;
import com.sever.physics.game.sprites.FireArrowSprite;
import com.sever.physics.game.sprites.FreeSprite;
import com.sever.physics.game.sprites.FuelBarSprite;
import com.sever.physics.game.sprites.GrenadeImplodeSprite;
import com.sever.physics.game.sprites.GrenadeSprite;
import com.sever.physics.game.sprites.GroundBoxSprite;
import com.sever.physics.game.sprites.JoystickSprite;
import com.sever.physics.game.sprites.PlanetSprite;
import com.sever.physics.game.sprites.PlayerSprite;
import com.sever.physics.game.sprites.PowerBarSprite;
import com.sever.physics.game.sprites.StaticBoxSprite;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.Joint;
import com.sever.physics.game.utils.SpriteBmp;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	public GameLoopThread gameLoopThread;
	public ConcurrentLinkedQueue<FreeSprite> explosiveSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> freeSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> staticSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> groundSprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> enemySprites = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> playerSprite = new ConcurrentLinkedQueue<FreeSprite>();
	public ConcurrentLinkedQueue<FreeSprite> nophysicsSprite = new ConcurrentLinkedQueue<FreeSprite>();
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
	public static boolean success;

	public FreeSprite getJoystickSprite() {
		return (FreeSprite) this.nophysicsSprite.toArray()[2];
	}

	public FreeSprite getFireArrowSprite() {
		return (FreeSprite) this.nophysicsSprite.toArray()[3];
	}

	public FreeSprite getPowerBarSprite() {
		return (FreeSprite) this.nophysicsSprite.toArray()[0];
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

	public FreeSprite addFireArrow(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.fireArrow);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 10, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new FireArrowSprite(nophysicsSprite, this, spriteBmp, x, y);
		nophysicsSprite.add(sprite);
		return sprite;
	}

	public FreeSprite addJoystick(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.joystick);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new JoystickSprite(nophysicsSprite, this, spriteBmp, x, y);
		nophysicsSprite.add(sprite);
		return sprite;
	}

	public FreeSprite addFuelBar(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.fuelBar);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 10, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new FuelBarSprite(nophysicsSprite, this, spriteBmp, x, y);
		nophysicsSprite.add(sprite);
		return sprite;
	}

	public FreeSprite addPowerBar(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.powerBar);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 10 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new PowerBarSprite(nophysicsSprite, this, spriteBmp, x, y);
		nophysicsSprite.add(sprite);
		return sprite;
	}

	public FreeSprite addBullet(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.bmpBall);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new BulletSprite(freeSprites, this, spriteBmp, x, y);
		freeSprites.add(sprite);
		return sprite;
	}

	protected FreeSprite addBall(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.bmpBall);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new BallSprite(freeSprites, this, spriteBmp, x, y);
		freeSprites.add(sprite);
		return sprite;
	}

	protected FreeSprite addBox(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.bmpBox);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new BoxSprite(freeSprites, this, spriteBmp, x, y);
		freeSprites.add(sprite);
		return sprite;
	}

	public FreeSprite addGrenadeTriple(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.bombtriple);
		bmp.add(PhysicsActivity.bombexploding);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new GrenadeSprite(explosiveSprites, this, spriteBmp, x, y);
		explosiveSprites.add(sprite);
		return sprite;
	}

	public FreeSprite addGrenadeSmall(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.bombsmall);
		bmp.add(PhysicsActivity.bombexploding);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new GrenadeSprite(explosiveSprites, this, spriteBmp, x, y);
		explosiveSprites.add(sprite);
		return sprite;
	}

	public FreeSprite addGrenade(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.bomb);
		bmp.add(PhysicsActivity.bombexploding);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new GrenadeSprite(explosiveSprites, this, spriteBmp, x, y);
		explosiveSprites.add(sprite);
		return sprite;
	}

	public FreeSprite addGrenadeImploding(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.bomb2);
		bmp.add(PhysicsActivity.bombexploding);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 4, 1 });
		colsrows.add(new int[] { 4, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new GrenadeImplodeSprite(explosiveSprites, this, spriteBmp, x, y);
		explosiveSprites.add(sprite);
		return sprite;
	}

	protected void addBox2(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.bmpBox2);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		freeSprites.add(new BoxSprite(freeSprites, this, spriteBmp, x, y));
	}

	protected void addBarrel(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.barrel);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		freeSprites.add(new BoxSprite(freeSprites, this, spriteBmp, x, y));
	}

	protected void addPlanet(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.planet1);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		staticSprites.add(new PlanetSprite(staticSprites, this, spriteBmp, x, y));
	}

	protected FreeSprite addHook(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.hook);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new StaticBoxSprite(this, spriteBmp, x, y);
		staticSprites.add(sprite);
		return sprite;
	}

	protected FreeSprite addGround(float x, float y, int w, int h) {
		Bitmap bmpx = PhysicsActivity.context.createScaledBitmap(PhysicsActivity.ground, w, h);
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(bmpx);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 1, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		FreeSprite sprite = new StaticBoxSprite(this, spriteBmp, x, y);
		staticSprites.add(sprite);
		return sprite;
	}

	protected FreeSprite addGroundBox(float x, float y, float hx, float hy) {
		FreeSprite sprite = new GroundBoxSprite(this, null, x, y, hx, hy);
		groundSprites.add(sprite);
		return sprite;
	}

	private void addPlayer(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.player);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 2 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		playerSprite.add(new PlayerSprite(playerSprite, this, spriteBmp, x, y));
	}

	private void addEnemy(float x, float y) {
		ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
		bmp.add(PhysicsActivity.enemy);
		ArrayList<int[]> colsrows = new ArrayList<int[]>();
		colsrows.add(new int[] { 2, 1 });
		SpriteBmp spriteBmp = new SpriteBmp(bmp, colsrows);
		enemySprites.add(new EnemySprite(enemySprites, this, spriteBmp, x, y));
	}

	protected void createSprites() {
		// ground
		addGroundBox(Constants.upperBoundxScreen * 0.5f, Constants.lowerBoundyScreen - Constants.setAsBoxhyScreen, Constants.upperBoundxScreen, Constants.setAsBoxhyScreen);
		// // ceiling
		// addGroundBox(Constants.upperBoundxScreen * 0.5f,
		// Constants.upperBoundyScreen + Constants.setAsBoxhyScreen,
		// Constants.upperBoundxScreen, Constants.setAsBoxhyScreen);
		// // leftwall
		// addGroundBox(Constants.lowerBoundxScreen -
		// Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f,
		// Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f);
		// // rightwall
		// addGroundBox(Constants.upperBoundxScreen +
		// Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f,
		// Constants.setAsBoxhxScreen, Constants.upperBoundyScreen * 0.5f);

		// addPlanet(this.getWidth() * 0.5f, this.getHeight() * 0.75f);
		// addBall(400, 150);
		// addBall(150, 400);
		// addBox(150, 150);
		// addBox2(500, 500);
		// addBox2(500, 150);
		createTrash();
		addPlayer(700, 500);
		createJointStuff();
		createNophysicSprites();
	}

	public void createJoint2Player(float x, float y) {
		FreeSprite b1 = addHook(x, y);
		new Joint().createRevolute(b1, getPlayerSprite());
	}

	private void createJointStuff() {
		addGround(this.getWidth() * 0.5f, -25.0f, (int) (this.getWidth() * 1.0), 150);
		addGround(950, 300, 50, 50);
		// FreeSprite b1 = addGround(450, 400, 50, 50);
		// FreeSprite b2 = addBox(500, 200);
		// new Joint().createRevolute(b1, b2);
		// FreeSprite b3 = addBox(600, 500);
		// FreeSprite b4 = addBox(800, 400);
		// new Joint().createPulley(b3, b4, 100, 100);
	}

	private void createTrash() {
		// addBox2(250, 250);
		addBox2(250, 200);
		// addBox2(250, 150);
		addBox2(300, 200);
		// addBox2(300, 150);
		addBox(350, 150);
		addBarrel(450, 100);
		// addBarrel(400, 100);
		addBarrel(350, 100);
		// addBarrel(300, 100);
		addBarrel(250, 100);
		// addBarrel(200, 100);
		addBarrel(150, 100);

	}

	private void sendSpaceTrash(int x, int y) {
		addBox2(250 + x, 250 + y);
		// addBox2(250 + x, 200 + y);
		addBox2(250 + x, 150 + y);
		// addBox2(300 + x, 200 + y);
		addBox2(300 + x, 150 + y);
		addBox(350 + x, 150 + y);
		addBarrel(450 + x, 100 + y);
		// addBarrel(400 + x, 100 + y);
		addBarrel(350 + x, 100 + y);
		// addBarrel(300 + x, 100 + y);
		addBarrel(250 + x, 100 + y);
		// addBarrel(200 + x, 100 + y);
		addBarrel(150 + x, 100 + y);
	}

	private void createNophysicSprites() {
		addPowerBar(950, 150);
		addFuelBar(250, 50);
		addJoystick(120, 60);
		addFireArrow(120, 60);
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
		update();
		// paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
		// canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

		try {
			synchronized (getHolder()) {

				Paint paint = new Paint();
				paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
				canvas.drawPaint(paint);

				drawBackground(canvas);

				if (getPlayerSprite() == null)
					return;

				// draw statics, pull frees
				for (Iterator<FreeSprite> it = staticSprites.iterator(); it.hasNext();) {
					FreeSprite spriteStatic = it.next();
					spriteStatic.onDraw(canvas);
				}

				// draw player
				getPlayerSprite().onDraw(canvas);

				// draw explosives, push enemies
				for (Iterator<FreeSprite> it = explosiveSprites.iterator(); it.hasNext();) {
					FreeSprite spriteExplosive = it.next();
					try {
						spriteExplosive.onDraw(canvas);
					} catch (Exception e) {
					}
				}

				// draw frees, push pull frees
				for (Iterator<FreeSprite> it2 = freeSprites.iterator(); it2.hasNext();) {
					FreeSprite spritefree = it2.next();
					try {
						spritefree.onDraw(canvas);
					} catch (Exception e) {
					}
				}
				// draw enemies
				for (Iterator<FreeSprite> it2 = enemySprites.iterator(); it2.hasNext();) {
					FreeSprite spriteEnemy = it2.next();
					try {
						spriteEnemy.onDraw(canvas);
					} catch (Exception e) {
					}
				}

				for (Iterator<FreeSprite> it = nophysicsSprite.iterator(); it.hasNext();) {
					FreeSprite spriteStatic = it.next();
					try {
						spriteStatic.onDraw(canvas);
					} catch (Exception e) {
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
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

		Rect src = new Rect(shiftWidth, 0, PhysicsActivity.bmpBack.getWidth(), PhysicsActivity.bmpBack.getHeight());
		Rect dst = new Rect(0, 0, getWidth() - shiftWidth, getHeight());
		canvas.drawBitmap(PhysicsActivity.bmpBack, src, dst, null);

		src = new Rect(0, 0, shiftWidth, PhysicsActivity.bmpBack.getHeight());
		dst = new Rect(getWidth() - shiftWidth, 0, getWidth(), getHeight());
		canvas.drawBitmap(PhysicsActivity.bmpBack, src, dst, null);
		shiftWidth++;
		if (PhysicsActivity.bmpBack.getWidth() == shiftWidth) {
			shiftWidth = 0;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			float x2 = event.getX();
			float y2 = event.getY();
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				((JoystickSprite) getJoystickSprite()).onMove(x2, IntroActivity.deviceHeight - y2);
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				((JoystickSprite) getJoystickSprite()).onDown(x2, IntroActivity.deviceHeight - y2);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				((JoystickSprite) getJoystickSprite()).onUp(x2, IntroActivity.deviceHeight - y2);
			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				((JoystickSprite) getJoystickSprite()).onUp(x2, IntroActivity.deviceHeight - y2);
			} else {
			}
		} catch (Exception e) {
		}
		return true;
	}

	public void update() {
		try {
			PhysicsActivity.context.mWorld.update();
			checkPhysicalEffects();
			checkEnemyCount();
			checkSpaceTrash();
			checkGameEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkPhysicalEffects() {

		if (getPlayerSprite() == null)
			return;

		// draw statics, pull frees
		for (Iterator<FreeSprite> it = staticSprites.iterator(); it.hasNext();) {
			FreeSprite spriteStatic = it.next();
			applyPullOn(spriteStatic, freeSprites);
			applyPullOn(spriteStatic, explosiveSprites);
			applyPullOn(spriteStatic, enemySprites);
		}

		// draw explosives, push enemies
		for (Iterator<FreeSprite> it = explosiveSprites.iterator(); it.hasNext();) {
			FreeSprite spriteExplosive = it.next();
			boolean ready = spriteExplosive.readyToExplode();
			applyPullPushOn(spriteExplosive, freeSprites, ready);
			applyPullPushOn(spriteExplosive, enemySprites, ready);
			applyPullPushOn(spriteExplosive, playerSprite, ready);
		}

		// draw frees, push pull frees
		for (Iterator<FreeSprite> it2 = freeSprites.iterator(); it2.hasNext();) {
			FreeSprite spritefree = it2.next();
			try {
				if (((PlayerSprite) getPlayerSprite()).powerOn) {
					getPlayerSprite().pull(spritefree);
				} else if (!((PlayerSprite) getPlayerSprite()).powerOn && ((PlayerSprite) getPlayerSprite()).scatter) {
					getPlayerSprite().push(spritefree);
				}
				if (((PlayerSprite) getPlayerSprite()).powerPush) {
					((PlayerSprite) getPlayerSprite()).pushPower(spritefree);
				}
			} catch (Exception e) {
			}
		}
		((PlayerSprite) getPlayerSprite()).powerPush = false;
		if (getPlayerSprite() != null && (!((PlayerSprite) getPlayerSprite()).powerOn && ((PlayerSprite) getPlayerSprite()).scatter)) {
			((PlayerSprite) getPlayerSprite()).scatter = false;
		}

	}

	private void checkSpaceTrash() {
		// System.out.println("checkSpaceTrash():freeSprites.size():" +
		// freeSprites.size());
		if (freeSprites.size() <= 5) {
			sendSpaceTrash(0, 250);
		}
	}

	private void checkEnemyCount() {
		if (enemySprites.size() < 2) {
			addEnemy(50 + new Random().nextInt(100), 350);
		}
	}

	private void checkGameEnd() {
		if (playerSprite.size() == 0) {
			// success = true;
			finishGame();
		}
	}

	public void stopGame() {
		gameLoopThread.setRunning(false);
	}

	private void finishGame() {
		System.out.println("finishGame()");
		gameLoopThread.setRunning(false);
		releaseBitmaps();
		Intent intent = new Intent(context, StageEndActivity.class);
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
}
