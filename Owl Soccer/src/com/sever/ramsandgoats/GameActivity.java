package com.sever.ramsandgoats;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sever.ramsandgoats.game.GameView;
import com.sever.ramsandgoats.sprites.SoccerPlayerSprite;
import com.sever.ramsandgoats.util.BitmapManager;
import com.sever.ramsandgoats.util.PhysicsWorldManager;
import com.sever.ramsandgoats.util.SoundEffectsManager;

public class GameActivity extends Activity {

	public GameView gameView;
	private Dialog dialog;
	public static GameActivity context;

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
				gameView.togglepauseresume();
			}
		});
		Button leave = (Button) dialog.findViewById(R.id.Button04);
		leave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SoundEffectsManager.stopSound();
				SoundEffectsManager.getManager().playBUTTON_CLICK(GameActivity.this);
				dialog.cancel();
				gameView.togglepauseresume();
				gameView.finishGame = true;
				gameView.finishGame();
			}
		});
		dialog.show();
	}

	@Override
	public void onBackPressed() {
		System.out.println("onBackPressed:" + this);
		super.onBackPressed();

		if (gameView.paused)
			return;

		SoundEffectsManager.getManager().playPAUSE_MENU(GameActivity.this);
		gameView.togglepauseresume();
		showMenu();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		context = this;

		SoundEffectsManager.getManager().initSounds();
		BitmapManager.getManager().initBitmaps();

		PhysicsWorldManager.createWorld();
		setContentView(R.layout.main);
		RelativeLayout relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayoutGameView);
		gameView = new GameView(this);
		relativeLayout1.addView(gameView);
		prepareGameButtons();
	}

	private void prepareGameButtons() {
		// TODO Auto-generated method stub
		Button buttonGoLeft = (Button) findViewById(R.id.button1);
		buttonGoLeft.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					((SoccerPlayerSprite) gameView.getPlayer()).moveBackward();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					((SoccerPlayerSprite) gameView.getPlayer()).doIdle();
				}
				return false;
			}
		});
		Button buttonGoRight = (Button) findViewById(R.id.Button01);
		buttonGoRight.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					((SoccerPlayerSprite) gameView.getPlayer()).moveForward();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					((SoccerPlayerSprite) gameView.getPlayer()).doIdle();
				}
				return false;
			}
		});
		Button buttonKick = (Button) findViewById(R.id.Button02);
		buttonKick.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					((SoccerPlayerSprite) gameView.getPlayer()).doBallKick();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
//					((SoccerPlayerSprite) gameView.getPlayer()).doIdle();
				}
				return false;
			}
		});
		Button buttonJump = (Button) findViewById(R.id.Button03);
		buttonJump.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					((SoccerPlayerSprite) gameView.getPlayer()).doJump();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
//					((SoccerPlayerSprite) gameView.getPlayer()).doIdle();
				}
				return false;
			}
		});
		Button buttonTrick = (Button) findViewById(R.id.Button04);
		buttonTrick.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					((SoccerPlayerSprite) gameView.getPlayer()).doBallTrick();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
//					((SoccerPlayerSprite) gameView.getPlayer()).doIdle();
				}
				return false;
			}
		});
	}

	@Override
	protected void onDestroy() {
		System.out.println("onDestroy:" + this);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		System.out.println("onPause:" + this);
		super.onPause();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		System.out.println("onPostCreate:" + this);
		super.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onPostResume() {
		System.out.println("onPostResume:" + this);
		super.onPostResume();
	}

	@Override
	protected void onRestart() {
		System.out.println("onRestart:" + this);
		super.onRestart();
	}

	@Override
	protected void onResume() {
		System.out.println("onResume:" + this);
		super.onResume();
	}

	@Override
	protected void onStart() {
		System.out.println("onStart:" + this);
		super.onStart();
	}

	@Override
	protected void onStop() {
		System.out.println("onStop:" + this);
		super.onStop();
	}

}
