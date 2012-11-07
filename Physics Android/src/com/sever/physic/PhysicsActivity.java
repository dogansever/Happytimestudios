package com.sever.physic;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.sever.physics.game.GameView;

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
	public static Bitmap bmpPlayer;
	public static PhysicsActivity context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		recallDeviceMetrics();
		bmpBack = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
		bmpBall = BitmapFactory.decodeResource(getResources(), R.drawable.basketball20);
		bmpPlayer = BitmapFactory.decodeResource(getResources(), R.drawable.basketball20);

		createWorld();
		setContentView(R.layout.main);
		RelativeLayout relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
		relativeLayout1.addView(new GameView(this));


		// Start Regular Update
		mHandler = new Handler();
	}

	private void createWorld() {
		mWorld = new PhysicsWorld();
		mWorld.create();
		mWorld.addBall(25, 20);
		mWorld.addPlayer(10, 5);
		mWorld.addPlayer(50, 5);
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
			mHandler.postDelayed(update, (long) (mWorld.timeStep * 1000));
		}
	};

	public static int deviceWidth;
	public static int deviceHeight;

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

}