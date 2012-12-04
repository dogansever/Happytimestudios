package com.sever.physic;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sever.physics.game.GameView;
import com.sever.physics.game.sprites.FireArrowSprite;
import com.sever.physics.game.sprites.PlayerSprite;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.PhysicsWorld;
import com.sever.physics.game.utils.Weapons;

public class PhysicsActivity extends Activity {

	public static PhysicsWorld mWorld;
	private Handler mHandler;
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
	public static Bitmap enemy;
	public static Bitmap ground;
	public static Bitmap bomb;
	public static Bitmap bomb2;
	public static Bitmap powerBar;
	public static Bitmap fuelBar;
	public static Bitmap joystick;
	public static Bitmap fireArrow;
	public static PhysicsActivity context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		context = this;
		bmpBack = createScaledBitmap(R.drawable.space, (int) IntroActivity.deviceWidth, (int) IntroActivity.deviceHeight);
		bmpBall = createScaledBitmap(R.drawable.basketball20, 0, 0);
		bmpBox = createScaledBitmap(R.drawable.crate30x30dark, 0, 0);
		bmpBox2 = createScaledBitmap(R.drawable.crate30x30light, 0, 0);
		planet1 = createScaledBitmap(R.drawable.planet200x200, 50, 50);
		barrel = createScaledBitmap(R.drawable.barrel, 30, 50);
		player = createScaledBitmap(R.drawable.playerx2x2, 150, 148);
		enemy = createScaledBitmap(R.drawable.enemy2x1, 60, 30);
		ground = createScaledBitmap(R.drawable.crate50x50dark, 800, 50);
		bomb = createScaledBitmap(R.drawable.bombx2x2, 50, 45);
		bomb2 = createScaledBitmap(R.drawable.bombx4x1, 100, 25);
		powerBar = createScaledBitmap(R.drawable.powerbar, 0, 0);
		fuelBar = createScaledBitmap(R.drawable.fuelbar, 0, 0);
		joystick = createScaledBitmap(R.drawable.joystick, 0, 0);
		fireArrow = createScaledBitmap(R.drawable.firearrow, 50, 50);

		createWorld();
		setContentView(R.layout.main);
		RelativeLayout relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayoutGameView);
		gameView = new GameView(this);
		relativeLayout1.addView(gameView);

		// Start Regular Update
		mHandler = new Handler();
		setButtonHandlers();
	}

	private void setButtonHandlers() {

		Button power = (Button) findViewById(R.id.button5);
		Button fire = (Button) findViewById(R.id.button6);
		Button bomb = (Button) findViewById(R.id.Button05);
		Button bombImploding = (Button) findViewById(R.id.Button06);
		Button bullet = (Button) findViewById(R.id.Button07);

		power.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						((PlayerSprite) getGameView().getPlayerSprite()).powerUp();
						break;
					case MotionEvent.ACTION_MOVE:
						break;
					case MotionEvent.ACTION_UP:
						((PlayerSprite) getGameView().getPlayerSprite()).powerDown();
						break;
					default:
						break;
					}
				} catch (Exception e) {
				}
				return true;
			}
		});
		fire.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					float x2 = event.getX();
					float y2 = event.getY();
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						((PlayerSprite) getGameView().getPlayerSprite()).fireHold();
						((FireArrowSprite) getGameView().getFireArrowSprite()).onDown(x2, IntroActivity.deviceHeight - y2);
						break;
					case MotionEvent.ACTION_MOVE:
						((PlayerSprite) getGameView().getPlayerSprite()).fireHold();
						((FireArrowSprite) getGameView().getFireArrowSprite()).onMove(x2, IntroActivity.deviceHeight - y2);
						break;
					case MotionEvent.ACTION_UP:
						((PlayerSprite) getGameView().getPlayerSprite()).fire();
						((FireArrowSprite) getGameView().getFireArrowSprite()).onUp(x2, IntroActivity.deviceHeight - y2);
						break;
					default:
						break;
					}
				} catch (Exception e) {
				}
				return true;
			}
		});

		bullet.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						break;
					case MotionEvent.ACTION_MOVE:
						break;
					case MotionEvent.ACTION_UP:
						((PlayerSprite) getGameView().getPlayerSprite()).weapon = Weapons.BULLET;
						break;
					default:
						break;
					}
				} catch (Exception e) {
				}
				return true;
			}
		});

		bomb.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						break;
					case MotionEvent.ACTION_MOVE:
						break;
					case MotionEvent.ACTION_UP:
						((PlayerSprite) getGameView().getPlayerSprite()).weapon = Weapons.BOMB;
						break;
					default:
						break;
					}
				} catch (Exception e) {
				}
				return true;
			}
		});

		bombImploding.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						break;
					case MotionEvent.ACTION_MOVE:
						break;
					case MotionEvent.ACTION_UP:
						((PlayerSprite) getGameView().getPlayerSprite()).weapon = Weapons.BOMB_IMPLODING;
						break;
					default:
						break;
					}
				} catch (Exception e) {
				}
				return true;
			}
		});

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
		mHandler.post(update);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeCallbacks(update);
	}

	private Runnable update = new Runnable() {
		public void run() {
			mWorld.update();
			mHandler.postDelayed(update, (long) (Constants.timeStep * 1000));
		}
	};

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