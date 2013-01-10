package com.sever.physic;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.BitmapManager;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.PhysicsWorld;
import com.sever.physics.game.utils.SoundEffectsManager;

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
	public static PhysicsActivity context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		context = this;

		SoundEffectsManager.getManager().initSounds();
		BitmapManager.getManager().initBitmaps();

		createWorld();
		setContentView(R.layout.main);
		RelativeLayout relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayoutGameView);
		gameView = new GameView(this);
		relativeLayout1.addView(gameView);

		// Start Regular Update
		// mHandler = new Handler();
		clearScore();
	}

	private void clearScore() {
		Constants.scoreStage = 0;
		Constants.scoreTotal = 0;
		Constants.scoreToPassTheStage = 0;
		Constants.enemyKilledCount = 0;
		Constants.playerKilledCount = 0;
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

	public GameView getGameView() {
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