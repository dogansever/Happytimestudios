package com.sever.physic;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sever.physics.game.GameViewImp;
import com.sever.physics.game.utils.BitmapManager;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.LogUtil;
import com.sever.physics.game.utils.PhysicsWorld;
import com.sever.physics.game.utils.SoundEffectsManager;

public class PhysicsActivity extends Activity {

	public static PhysicsActivity context;
	public static PhysicsWorld mWorld;
	private GameViewImp gameView;
	private Dialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		LogUtil.log("onCreate:" + this);
		super.onCreate(savedInstanceState);
		context = this;

		SoundEffectsManager.getManager().initSounds();
		BitmapManager.getManager().initBitmaps();

		createWorld();
		setContentView(R.layout.main);
		RelativeLayout relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayoutGameView);
		gameView = new GameViewImp(this);
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

	public GameViewImp getGameView() {
		// RelativeLayout relativeLayout1 = (RelativeLayout)
		// findViewById(R.id.relativeLayoutGameView);
		// return (GameView) relativeLayout1.getChildAt(0);
		return gameView;
	}

	@Override
	protected void onDestroy() {
		LogUtil.log("onDestroy:" + this);
		// SoundEffectsManager.stopSound();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if (getGameView().idle)
			return;

		SoundEffectsManager.getManager().playSound(PhysicsActivity.this, SoundEffectsManager.PAUSE_MENU);
		getGameView().togglepauseresume();
		showMenu();
	}

	public void showMenu() {
		dialog = new Dialog(context, R.style.ThemeDialogCustom);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.pausemenu);
		dialog.setCancelable(false);

		Button cont = (Button) dialog.findViewById(R.id.Button01);
		cont.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
				getGameView().togglepauseresume();
			}
		});
		Button leave = (Button) dialog.findViewById(R.id.Button04);
		leave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoundEffectsManager.stopSound();
				SoundEffectsManager.getManager().playSound(PhysicsActivity.this, SoundEffectsManager.BUTTON_CLICK);
				dialog.cancel();
				getGameView().togglepauseresume();
				getGameView().finishGame = true;
				getGameView().finishGame();
			}
		});
		dialog.show();
	}

}