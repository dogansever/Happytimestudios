package com.sever.physic;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.sever.physics.game.GameView;
import com.sever.physics.game.sprites.PlayerSprite;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.PhysicsWorld;
import com.sever.physics.game.utils.Weapon;
import com.sever.physics.game.utils.WeaponTypes;

public class PhysicsActivity extends Activity {

	public static PhysicsWorld mWorld;
	// private Handler mHandler;

	public static Timer timer = null;
	public final Handler mHandler = new Handler();
	public static int count = 0;
	private ArrayList<View> ballViews = new ArrayList<View>();
	protected boolean TOUCHED;
	protected View TOUCHED_VIEW;
	protected int downx;
	protected int downy;
	protected long downTime;
	private GameView gameView;
	public static Bitmap bmpBack;
	public static Bitmap bmpBall;
	public static Bitmap bmpBox;
	public static Bitmap bmpBox2;
	public static Bitmap planet1;
	public static Bitmap barrel;
	public static Bitmap player;
	public static Bitmap playerThrottle;
	public static Bitmap enemy;
	public static Bitmap enemypointer;
	public static Bitmap enemyThrottle;
	public static Bitmap enemyexploding;
	public static Bitmap enemyBurning;
	public static Bitmap ground;
	public static Bitmap missile;
	public static Bitmap missileLight;
	public static Bitmap bomb;
	public static Bitmap bombsmall;
	public static Bitmap bombtriple;
	public static Bitmap bombexploding;
	public static Bitmap bomb2;
	public static Bitmap powerBar;
	public static Bitmap lifeBar;
	public static Bitmap fuelBar;
	public static Bitmap joystick;
	public static Bitmap fireButton;
	public static Bitmap weaponSwapButton;
	public static Bitmap fireArrow;
	public static Bitmap hook;
	public static PhysicsActivity context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		context = this;
		bmpBack = createScaledBitmap(R.drawable.space, (int) PhysicsApplication.deviceWidth, (int) PhysicsApplication.deviceHeight);
		bmpBall = createScaledBitmap(R.drawable.bullet, 20, 20);
		bmpBox = createScaledBitmap(R.drawable.crate50x50dark, 40, 40);
		bmpBox2 = createScaledBitmap(R.drawable.crate50x50light, 40, 40);
		planet1 = createScaledBitmap(R.drawable.planet200x200, 50, 50);
		barrel = createScaledBitmap(R.drawable.barrel, 45, 75);
		player = createScaledBitmap(R.drawable.playerx2x2, (int) (340 * 0.4f), (int) (296 * 0.4f));
		playerThrottle = createScaledBitmap(R.drawable.playerthrottlex2x2, (int) (340 * 0.4f), (int) (296 * 0.4f));
		enemy = createScaledBitmap(R.drawable.enemy2x1, (int) (90 * 0.8f), (int) (42 * 0.8f));
		enemyThrottle = createScaledBitmap(R.drawable.enemythrottlex2x2, (int) (90 * 0.8f), (int) (84 * 0.8f));
		enemyBurning = createScaledBitmap(R.drawable.enemyburningx2x2, (int) (90 * 0.8f), (int) (84 * 0.8f));
		enemyexploding = createScaledBitmap(R.drawable.enemyexplosionx3x1, 0, 0);
		ground = createScaledBitmap(R.drawable.crate50x50dark, 800, 50);
		bomb = createScaledBitmap(R.drawable.bombx2x1, 90, 45);
		bombsmall = createScaledBitmap(R.drawable.bombx2x1, 60, 30);
		missile = createScaledBitmap(R.drawable.missilex4x1, 200, 30);
		missileLight = createScaledBitmap(R.drawable.missilex4x1, 140, 21);
		bombtriple = createScaledBitmap(R.drawable.bombx2x1, 50, 25);
		bombexploding = createScaledBitmap(R.drawable.bombexplodingx4x1, 0, 0);
		bomb2 = createScaledBitmap(R.drawable.bombx4x1, 120, 30);
		powerBar = createScaledBitmap(R.drawable.powerbar, 45, 60);
		lifeBar = createScaledBitmap(R.drawable.powerbar, 45, 60);
		fuelBar = createScaledBitmap(R.drawable.fuelbar, 60, 45);
		joystick = createScaledBitmap(R.drawable.joystick, 0, 0);
		fireButton = createScaledBitmap(R.drawable.buttonred, 400, 200);
		fireArrow = createScaledBitmap(R.drawable.firearrowx10, 500, 50);
		hook = createScaledBitmap(R.drawable.hook, 0, 0);
		enemypointer = createScaledBitmap(R.drawable.enemypointer, 0, 0);
		weaponSwapButton = context.createScaledBitmap(R.drawable.buttongun2, 100, 100);

		createWorld();
		setContentView(R.layout.main);
		RelativeLayout relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayoutGameView);
		gameView = new GameView(this);
		relativeLayout1.addView(gameView);

		// Start Regular Update
		// mHandler = new Handler();
		setButtonHandlers();
		clearScore();
	}

	private void clearScore() {
		Constants.score = 0;
		Constants.scorePass = 0;
		Constants.enemyKilledCount = 0;
		Constants.playerKilledCount = 0;
	}

	private void setButtonHandlers() {
		// Button fire = (Button) findViewById(R.id.button6);
		// final Button jump = (Button) findViewById(R.id.Button01);
		// jump.setBackgroundResource(R.drawable.buttonhoveroff);
		// final Button bullet = (Button) findViewById(R.id.Button07);
		// bullet.setBackgroundResource(R.drawable.buttongun2);

		// jump.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (((PlayerSprite) getGameView().getPlayerSprite()).hoverOnOff()) {
		// jump.setBackgroundResource(R.drawable.buttonhoveron);
		// } else {
		// jump.setBackgroundResource(R.drawable.buttonhoveroff);
		// }
		// }
		// });

		// fire.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// try {
		//
		// float x2 = event.getX();
		// float y2 = event.getY();
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// if (((PlayerSprite) getGameView().getPlayerSprite()).weapon.getType()
		// == WeaponTypes.SUPER_SHOCK_GUN) {
		// } else if (((PlayerSprite)
		// getGameView().getPlayerSprite()).weapon.getType() ==
		// WeaponTypes.SHOCK_GUN) {
		// ((PlayerSprite) getGameView().getPlayerSprite()).powerUp();
		// } else {
		// ((PlayerSprite) getGameView().getPlayerSprite()).fireHold();
		// if (WeaponsManager.getManager().getWeaponByType(((PlayerSprite)
		// getGameView().getPlayerSprite()).weapon.getType()).automatic) {
		// ((PlayerSprite) getGameView().getPlayerSprite()).fireStart();
		// }
		// ((FireArrowSprite) getGameView().getFireArrowSprite()).onDown(x2,
		// PhysicsApplication.deviceHeight - y2);
		// }
		// break;
		// case MotionEvent.ACTION_MOVE:
		// if (((PlayerSprite) getGameView().getPlayerSprite()).weapon.getType()
		// == WeaponTypes.SUPER_SHOCK_GUN) {
		// } else if (((PlayerSprite)
		// getGameView().getPlayerSprite()).weapon.getType() ==
		// WeaponTypes.SHOCK_GUN) {
		// } else {
		// ((PlayerSprite) getGameView().getPlayerSprite()).fireHold();
		// ((FireArrowSprite) getGameView().getFireArrowSprite()).onMove(x2,
		// PhysicsApplication.deviceHeight - y2);
		// }
		// break;
		// case MotionEvent.ACTION_UP:
		// case MotionEvent.ACTION_CANCEL:
		// if (((PlayerSprite) getGameView().getPlayerSprite()).weapon.getType()
		// == WeaponTypes.SUPER_SHOCK_GUN) {
		// ((PlayerSprite) getGameView().getPlayerSprite()).powerPush();
		// } else if (((PlayerSprite)
		// getGameView().getPlayerSprite()).weapon.getType() ==
		// WeaponTypes.SHOCK_GUN) {
		// ((PlayerSprite) getGameView().getPlayerSprite()).powerDown();
		// } else {
		// if (WeaponsManager.getManager().getWeaponByType(((PlayerSprite)
		// getGameView().getPlayerSprite()).weapon.getType()).automatic) {
		// ((PlayerSprite) getGameView().getPlayerSprite()).fireCease();
		// } else {
		// ((PlayerSprite) getGameView().getPlayerSprite()).fireStart();
		// }
		// ((FireArrowSprite) getGameView().getFireArrowSprite()).onUp(x2,
		// PhysicsApplication.deviceHeight - y2);
		// }
		// break;
		// default:
		// break;
		// }
		// } catch (Exception e) {
		// }
		// return true;
		// }
		// });

		// bullet.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		//
		// try {
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// break;
		// case MotionEvent.ACTION_MOVE:
		// break;
		// case MotionEvent.ACTION_UP:
		// ((PlayerSprite) getGameView().getPlayerSprite()).weapon =
		// WeaponsManager.getManager().nextWeapon();
		// refreshButtons();
		// break;
		// default:
		// break;
		// }
		// } catch (Exception e) {
		// }
		// return true;
		// }
		// });

	}

	public static void refreshSwapWeaponButtonBitmap() {
		Weapon weapon = ((PlayerSprite) context.getGameView().getPlayerSprite()).weapon;
		WeaponTypes type = weapon.getType();
		int h = 100;
		int w = 100;
		if (type == WeaponTypes.BULLET) {
			if (weapon.isAvailable())
				weaponSwapButton = context.createScaledBitmap(R.drawable.buttongun2, w, h);
			else
				weaponSwapButton = context.createScaledBitmap(R.drawable.buttongun, w, h);
		} else if (type == WeaponTypes.BOMB) {
			if (weapon.isAvailable())
				weaponSwapButton = context.createScaledBitmap(R.drawable.bombsmall, w, h);
			else
				weaponSwapButton = context.createScaledBitmap(R.drawable.bombsmall2, w, h);
		} else if (type == WeaponTypes.BOMB_TRIPLE) {
			if (weapon.isAvailable())
				weaponSwapButton = context.createScaledBitmap(R.drawable.bombtriple, w, h);
			else
				weaponSwapButton = context.createScaledBitmap(R.drawable.bombtriple2, w, h);
		} else if (type == WeaponTypes.BOMB_BIG) {
			if (weapon.isAvailable())
				weaponSwapButton = context.createScaledBitmap(R.drawable.buttonbomb2, w, h);
			else
				weaponSwapButton = context.createScaledBitmap(R.drawable.buttonbomb, w, h);
		} else if (type == WeaponTypes.BOMB_IMPLODING) {
			if (weapon.isAvailable())
				weaponSwapButton = context.createScaledBitmap(R.drawable.buttonbombimploding2, w, h);
			else
				weaponSwapButton = context.createScaledBitmap(R.drawable.buttonbombimploding, w, h);
		} else if (type == WeaponTypes.MISSILE) {
			if (weapon.isAvailable())
				weaponSwapButton = context.createScaledBitmap(R.drawable.buttonmissile2, w, h);
			else
				weaponSwapButton = context.createScaledBitmap(R.drawable.buttonmissile, w, h);
		} else if (type == WeaponTypes.MISSILE_LIGHT) {
			if (weapon.isAvailable())
				weaponSwapButton = context.createScaledBitmap(R.drawable.buttonmissilelight2, w, h);
			else
				weaponSwapButton = context.createScaledBitmap(R.drawable.buttonmissilelight, w, h);
		}
	}

	public Bitmap createScaledBitmap(Bitmap bmp, int dstWidth, int dstHeight) {
		if (dstWidth <= 0 || dstHeight <= 0) {
			return bmp;
		}
		return Bitmap.createScaledBitmap(bmp, dstWidth, dstHeight, false);
	}

	public Bitmap createScaledBitmap(int decodeResource, int dstWidth, int dstHeight) {
		if (dstWidth <= 0 || dstHeight <= 0) {
			return BitmapFactory.decodeResource(getResources(), decodeResource);
		}
		return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), decodeResource), dstWidth, dstHeight, false);
	}

	private void createWorld() {
		mWorld = new PhysicsWorld();
		mWorld.create();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// mHandler.post(update);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// mHandler.removeCallbacks(update);
	}

	// private Runnable update = new Runnable() {
	// public void run() {
	// mWorld.update();
	// mHandler.postDelayed(update, (long) (Constants.timeStep * 1000));
	// }
	// };

	public void updateScreen() {
	}

	private GameView getGameView() {
		// RelativeLayout relativeLayout1 = (RelativeLayout)
		// findViewById(R.id.relativeLayoutGameView);
		// return (GameView) relativeLayout1.getChildAt(0);
		return gameView;
	}

	@Override
	protected void onDestroy() {
		System.out.println("onDestroy:" + this);
		super.onDestroy();
	}

}