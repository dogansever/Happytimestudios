package com.sever.physic;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sever.physics.game.GameView;
import com.sever.physics.game.PlayerSprite;

public class PhysicsActivity extends Activity {

	public static PhysicsWorld mWorld;
	private Handler mHandler;
	private ArrayList<View> ballViews = new ArrayList<View>();
	protected boolean TOUCHED;
	protected View TOUCHED_VIEW;
	protected int downx;
	protected int downy;
	protected long downTime;
	public static Bitmap bmpBack;
	public static Bitmap bmpBall;
	public static Bitmap bmpBox;
	public static Bitmap bmpBox2;
	public static Bitmap planet1;
	public static Bitmap barrel;
	public static Bitmap player;
	public static PhysicsActivity context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		recallDeviceMetrics();
		bmpBack = createScaledBitmap(R.drawable.space, 0, 0);
		bmpBall = createScaledBitmap(R.drawable.basketball20, 0, 0);
		bmpBox = createScaledBitmap(R.drawable.crate30x30dark, 0, 0);
		bmpBox2 = createScaledBitmap(R.drawable.crate30x30light, 0, 0);
		planet1 = createScaledBitmap(R.drawable.planet200x200, 150, 150);
		barrel = createScaledBitmap(R.drawable.barrel, 45, 75);
		player = createScaledBitmap(R.drawable.playerx2x2, 0, 0);

		createWorld();
		setContentView(R.layout.main);
		RelativeLayout relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayoutGameView);
		relativeLayout1.addView(new GameView(this));

		// Start Regular Update
		mHandler = new Handler();
		setButtonHandlers();
	}

	private void setButtonHandlers() {

		Button up = (Button) findViewById(R.id.button1);
		Button down = (Button) findViewById(R.id.button2);
		Button left = (Button) findViewById(R.id.button3);
		Button right = (Button) findViewById(R.id.button4);
		Button power = (Button) findViewById(R.id.button5);
		up.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					throttleUp(v);
					break;
				case MotionEvent.ACTION_MOVE:
					throttleUp(v);
					break;
				case MotionEvent.ACTION_UP:
					break;
				default:
					break;
				}
				return true;
			}
		});
		down.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					throttleDown(v);
					break;
				case MotionEvent.ACTION_MOVE:
					throttleDown(v);
					break;
				case MotionEvent.ACTION_UP:
					break;
				default:
					break;
				}
				return true;
			}
		});
		left.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					throttleLeft(v);
					break;
				case MotionEvent.ACTION_MOVE:
					throttleLeft(v);
					break;
				case MotionEvent.ACTION_UP:
					break;
				default:
					break;
				}
				return true;
			}
		});
		right.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					throttleRight(v);
					break;
				case MotionEvent.ACTION_MOVE:
					throttleRight(v);
					break;
				case MotionEvent.ACTION_UP:
					break;
				default:
					break;
				}
				return true;
			}
		});
		power.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					((PlayerSprite) getGameView().playerSprite).powerUp();
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					((PlayerSprite) getGameView().playerSprite).powerDown();
					break;
				default:
					break;
				}
				return true;
			}
		});

	}

	private Bitmap createScaledBitmap(int decodeResource, int dstWidth, int dstHeight) {
		if (dstWidth <= 0 || dstHeight <= 0) {
			return BitmapFactory.decodeResource(getResources(), decodeResource);
		}
		return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), decodeResource), dstWidth, dstHeight, false);
	}

	private void createWorld() {
		mWorld = new PhysicsWorld();
		mWorld.create();
		// mWorld.addBall(40, 5);
		// mWorld.addPlayer(10, 55);
		// mWorld.addPlayer(70, 55);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mHandler.post(update);
	}

	// downx = ((AbsoluteLayout.LayoutParams) imageView.getLayoutParams()).x;
	// downy = ((AbsoluteLayout.LayoutParams) imageView.getLayoutParams()).y;
	// downTime = arg1.getEventTime();
	// Vec2 position = new Vec2(downx + arg1.getX(), downy + arg1.getY());
	// long downTime2 = arg1.getEventTime();
	// long diffTime = (downTime2 - downTime) / 100;
	// float speedx = arg1.getX() / diffTime;
	// float speedy = -arg1.getY() / diffTime;
	// int i = absoluteLayout1.indexOfChild(imageView);
	// float angle = mWorld.bodies.get(i).getAngle();
	// position = mWorld.fromScreen(position);
	// mWorld.bodies.get(i).setXForm(position, angle);
	// mWorld.bodies.get(i).setLinearVelocity(new Vec2(speedx, speedy));

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

	public static float deviceWidth;
	public static float deviceHeight;

	public void recallDeviceMetrics() {
		try {
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			deviceWidth = metrics.widthPixels;
			deviceHeight = metrics.heightPixels;
		} catch (Exception e) {
		}
	}

	public void updateScreen() {
		// TODO Auto-generated method stub

	}

	private GameView getGameView() {
		RelativeLayout relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayoutGameView);
		return (GameView) relativeLayout1.getChildAt(0);
	}

	public void throttleUp(View view) {
		((PlayerSprite) getGameView().playerSprite).throttleUp();
	}

	public void throttleDown(View view) {
		((PlayerSprite) getGameView().playerSprite).throttleDown();
	}

	public void throttleLeft(View view) {
		((PlayerSprite) getGameView().playerSprite).throttleLeft();
	}

	public void throttleRight(View view) {
		((PlayerSprite) getGameView().playerSprite).throttleRight();
	}

}