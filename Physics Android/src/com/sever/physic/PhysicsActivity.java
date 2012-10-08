package com.sever.physic;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ImageView;

public class PhysicsActivity extends Activity {

	PhysicsWorld mWorld;
	private Handler mHandler;
	private ArrayList<View> ballViews = new ArrayList<View>();
	protected boolean TOUCHED;
	private AbsoluteLayout absoluteLayout1;
	protected View TOUCHED_VIEW;
	protected int downx;
	protected int downy;
	protected long downTime;
	public static PhysicsActivity context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.main);
		absoluteLayout1 = (AbsoluteLayout) findViewById(R.id.absoluteLayout1);
		absoluteLayout1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mWorld.addBall(7, 30);
				addBall();
			}
		});
		// absoluteLayout1.setClickable(false);
		// absoluteLayout1.setFocusable(false);
		recallDeviceMetrics();
		mWorld = new PhysicsWorld();
		mWorld.create();

		mWorld.addBall(6, 20);
		mWorld.addBall(7, 30);
		addBall();
		addBall();

		// Start Regular Update
		mHandler = new Handler();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mHandler.post(update);
	}

	private void addBall() {
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.basketball20);
		// imageView.setFocusable(true);
		// imageView.setFocusableInTouchMode(true);
		absoluteLayout1.addView(imageView);
		imageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				System.out.println("imageView:" + arg1.getX() + "," + arg1.getY());

				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					System.out.println("imageView:ACTION_DOWN:" + arg1.getX() + "," + arg1.getY());
					TOUCHED = true;
					TOUCHED_VIEW = arg0;
					ImageView imageView = (ImageView) TOUCHED_VIEW;
					downx = ((AbsoluteLayout.LayoutParams) imageView.getLayoutParams()).x;
					downy = ((AbsoluteLayout.LayoutParams) imageView.getLayoutParams()).y;
					downTime = arg1.getEventTime();

				} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
					System.out.println("imageView:ACTION_UP:" + arg1.getX() + "," + arg1.getY());
					if (TOUCHED) {
						ImageView imageView = (ImageView) TOUCHED_VIEW;
						Vec2 position = new Vec2(downx + arg1.getX(), downy + arg1.getY());
						long downTime2 = arg1.getEventTime();
						long diffTime = (downTime2 - downTime) / 100;
						float speedx = arg1.getX() / diffTime;
						speedx = speedx > 20 ? 20 : speedx;
						float speedy = -arg1.getY() / diffTime;
						speedy = speedy > 20 ? 20 : speedy;
						System.out.println("diffTime:" + diffTime);
						System.out.println("-arg1.getX():" + -arg1.getX() + ",-arg1.getY():" + -arg1.getY());
						System.out.println("speedx:" + speedx + ",speedy:" + speedy);
						int i = absoluteLayout1.indexOfChild(imageView);
						float angle = mWorld.bodies.get(i).getAngle();
						position = mWorld.fromScreen(position);
						mWorld.bodies.get(i).setXForm(position, angle);
						mWorld.bodies.get(i).setLinearVelocity(new Vec2(speedx, speedy));
					}
					TOUCHED = false;
					TOUCHED_VIEW = null;
				} else if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
					System.out.println("absoluteLayout1ACTION_MOVE:" + arg1.getX() + "," + arg1.getY());
					if (TOUCHED) {
						ImageView imageView = (ImageView) TOUCHED_VIEW;
						Vec2 position = new Vec2(downx + arg1.getX(), downy + arg1.getY());
						imageView.setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
								(int) position.x, (int) position.y));
					}
				}
				return true;
			}
		});

	}

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
			// e.printStackTrace();
		}
	}

	public void updateScreen() {
		AbsoluteLayout absoluteLayout1 = (AbsoluteLayout) findViewById(R.id.absoluteLayout1);
		int cnt = absoluteLayout1.getChildCount();
		for (int i = 0; i < cnt; i++) {
			ImageView imageView = (ImageView) absoluteLayout1.getChildAt(i);
			if (TOUCHED_VIEW == imageView)
				continue;
			Vec2 position = mWorld.bodies.get(i).getPosition();
			position = mWorld.toScreen(position);
			float angle = mWorld.bodies.get(i).getAngle();
			imageView.setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, (int) position.x - 20,
					(int) position.y - 20));
			Log.v("Physics Test", "Pos " + i + ": (" + position.x + ", " + position.y + "), Angle: " + angle);
		}
	}
}