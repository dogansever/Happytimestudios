package com.sever.android.main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class StartActivity extends Activity {

	public static StartActivity context = null;
	public static Bitmap bmpBack;
	public static Bitmap bmpProgress;
	public static Bitmap bmpProgressHead;
	public static Bitmap bmpProgressLife;
	public static Bitmap bmpProgressOwl;
	public static Bitmap levelBmp;
	public static Bitmap hitChancesBmp;
	public static Bitmap missBmp;
	public static Bitmap hitBmp;
	public static Bitmap jammedBmp;
	public static Bitmap warningBmp;
	public static Bitmap stageBmp;
	public static HashMap<Owls, Bitmap> bmpOwl = new HashMap<Owls, Bitmap>();
	public static HashMap<Owls, Bitmap> bmpOwl_Jammed = new HashMap<Owls, Bitmap>();
	public static HashMap<Owls, Bitmap> bmpOwlAttacked = new HashMap<Owls, Bitmap>();
	public static ArrayList<Bitmap> bmpZombie = new ArrayList<Bitmap>();
	public static ArrayList<Bitmap> bmpZombieDie = new ArrayList<Bitmap>();
	public static ArrayList<Bitmap> bmpZombieAttack = new ArrayList<Bitmap>();

	public static DBWriteUtil dbDBWriteUtil;
	public static int deviceWidth;
	public static int deviceHeight;
	public static int deviceDensityDpi;
	private boolean init;

	public static final int FIRE_BARETTA = 1;
	public static final int FIRE_SNIPER = 2;
	public static final int FIRE_MACHINEGUN = 3;
	public static final int FIRE_FAILED = 4;
	public static final int ZOMBIE_WALKS = 5;
	public static final int ZOMBIE_MOANS = 6;
	public static final int ZOMBIE_ATTACKED = 7;
	public static final int INTRO = 8;
	public static final int MENU = 9;
	public static final int SUNNY_DAY = 10;
	public static final int ZOMBIE_BACK_FROM_DEAD = 11;
	public static final int MUSIC_BOX = 12;

	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	public Typeface face;
	private static MediaPlayer mp1;
	private Timer timerAnimation;
	public final Handler mHandler = new Handler();

	public static void recallDeviceMetrics() {
		try {
			DisplayMetrics metrics = new DisplayMetrics();
			StartActivity.context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			StartActivity.deviceWidth = metrics.widthPixels;
			StartActivity.deviceHeight = metrics.heightPixels;
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	private void initSounds() {
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		soundPoolMap = new HashMap<Integer, Integer>();

		// soundPoolMap.put(INTRO, soundPool.load(context,
		// R.raw.crickets_at_night, 1));
		// soundPoolMap.put(MENU, soundPool.load(context, R.raw.nightime, 1));
		soundPoolMap.put(FIRE_BARETTA, soundPool.load(context, R.raw.barreta, 1));
		soundPoolMap.put(FIRE_SNIPER, soundPool.load(context, R.raw.sniper_rifle, 1));
		soundPoolMap.put(FIRE_MACHINEGUN, soundPool.load(context, R.raw.machine_gun_mp5, 1));
		soundPoolMap.put(FIRE_FAILED, soundPool.load(context, R.raw.pop_clip_in, 1));
		soundPoolMap.put(ZOMBIE_WALKS, soundPool.load(context, R.raw.zombie_walks, 1));
		soundPoolMap.put(ZOMBIE_MOANS, soundPool.load(context, R.raw.zombie_moans, 1));
		soundPoolMap.put(ZOMBIE_ATTACKED, soundPool.load(context, R.raw.zombie_gets_attacked, 1));
		// soundPoolMap.put(SUNNY_DAY, soundPool.load(context, R.raw.sunny_day,
		// 1));
		soundPoolMap.put(ZOMBIE_BACK_FROM_DEAD, soundPool.load(context, R.raw.zombie_back_from_dead, 1));
		soundPoolMap.put(MUSIC_BOX, soundPool.load(context, R.raw.music_box, 1));
	}

	public void stopSound(int sound) {
		if (!MenuActivity.soundOn)
			return;
		soundPool.stop(soundPoolMap.get(sound));
	}

	public void playSound(int sound, boolean... b) {
		try {
			if (!MenuActivity.soundOn)
				return;
			/*
			 * Updated: The next 4 lines calculate the current volume in a scale
			 * of 0.0 to 1.0
			 */
			AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = streamVolumeCurrent / streamVolumeMax;

			/* Play the sound with the correct volume */
			if (b.length == 0) {
				soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f);
			} else if (b[0]) {
				soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 1, 1f);
			}
		} catch (Exception e) {
		}
	}

	// public void stopIntroSound() {
	// stopSound(INTRO);
	// }
	//
	// public void playIntroSound() {
	// playSound(INTRO, true);
	// }

	// public void stopMenuSound() {
	// stopSound(MENU);
	// }
	//
	// public void playMenuSound() {
	// playSound(MENU, true);
	// }

	// public void stopLevelCompleteSound() {
	// stopSound(SUNNY_DAY);
	// }
	//
	// public void playLevelCompleteSound() {
	// playSound(SUNNY_DAY, true);
	// }

	public void playLevelFailedSound() {
		playSound(ZOMBIE_BACK_FROM_DEAD);
	}

	public void playMusicSound() {
		playSound(MUSIC_BOX);
	}

	public void zombieWalksSound() {
		playSound(ZOMBIE_WALKS);
	}

	public void zombieMoansSound() {
		playSound(ZOMBIE_MOANS);
	}

	public void zombieAttackedSound() {
		playSound(ZOMBIE_ATTACKED);
	}

	public void fireFailedSound() {
		playSound(FIRE_FAILED);
	}

	public void fireMachineGunSound() {
		playSound(FIRE_MACHINEGUN);
	}

	public void fireSniperSound() {
		playSound(FIRE_SNIPER);
	}

	public void fireBarettaSound() {
		playSound(FIRE_BARETTA);
	}

	private void initResources() {
		initSounds();
		face = Typeface.createFromAsset(getAssets(), "FEASFBRG.TTF");
		// initBitmaps();
		init = true;
	}

	public void releaseBitmaps() {
		// game
		warningBmp = null;
		hitBmp = null;
		missBmp = null;
		jammedBmp = null;
		hitChancesBmp = null;
		bmpProgressLife = null;
		bmpProgressOwl = null;
		bmpProgress = null;
		bmpProgressHead = null;
		bmpBack = null;

		bmpZombieDie.clear();
		bmpZombie.clear();
		bmpZombieAttack.clear();
		bmpOwl.clear();
		bmpOwl_Jammed.clear();
		bmpOwlAttacked.clear();
	}

	public void releaseBitmapsMenu() {
		levelBmp = null;
		stageBmp = null;
	}

	public void initBitmapsMenu() {
		if (levelBmp == null)
			levelBmp = BitmapFactory.decodeResource(getResources(), R.drawable.menus);
		if (stageBmp == null)
			stageBmp = BitmapFactory.decodeResource(getResources(), R.drawable.stages);
	}

	public void initBitmaps() {
		// game
		warningBmp = BitmapFactory.decodeResource(getResources(), R.drawable.warning_anim);
		hitBmp = BitmapFactory.decodeResource(getResources(), R.drawable.hit_anim);
		missBmp = BitmapFactory.decodeResource(getResources(), R.drawable.miss_anim);
		jammedBmp = BitmapFactory.decodeResource(getResources(), R.drawable.jammed_anim);
		hitChancesBmp = BitmapFactory.decodeResource(getResources(), R.drawable.hitchances);
		bmpProgressLife = BitmapFactory.decodeResource(getResources(), R.drawable.progress_life);
		bmpProgressOwl = BitmapFactory.decodeResource(getResources(), R.drawable.progress_owl);
		bmpProgress = BitmapFactory.decodeResource(getResources(), R.drawable.progress);
		bmpProgressHead = BitmapFactory.decodeResource(getResources(), R.drawable.progress_head);
		bmpBack = BitmapFactory.decodeResource(getResources(), R.drawable.stage01);

		bmpZombieDie.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie01die));
		bmpZombieDie.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie02die));
		bmpZombieDie.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie03die));
		bmpZombieDie.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie04die));
		bmpZombieDie.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie05die));
		bmpZombieDie.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie06die));
		bmpZombie.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie01));
		bmpZombie.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie02));
		bmpZombie.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie03));
		bmpZombie.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie04));
		bmpZombie.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie05));
		bmpZombie.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie06));
		bmpZombieAttack.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie01attack));
		bmpZombieAttack.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie02attack));
		bmpZombieAttack.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie03attack));
		bmpZombieAttack.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie04attack));
		bmpZombieAttack.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie05attack));
		bmpZombieAttack.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie06attack));

		bmpOwl.put(Owls.owlMachineGun, BitmapFactory.decodeResource(getResources(), R.drawable.owl1firing));
		bmpOwl.put(Owls.owlBaretta, BitmapFactory.decodeResource(getResources(), R.drawable.owl2firing));
		bmpOwl.put(Owls.owlSniper, BitmapFactory.decodeResource(getResources(), R.drawable.owl3firing));
		bmpOwl_Jammed.put(Owls.owlMachineGun, BitmapFactory.decodeResource(getResources(), R.drawable.owl1idle));
		bmpOwl_Jammed.put(Owls.owlBaretta, BitmapFactory.decodeResource(getResources(), R.drawable.owl2idle));
		bmpOwl_Jammed.put(Owls.owlSniper, BitmapFactory.decodeResource(getResources(), R.drawable.owl3idle));
		bmpOwlAttacked.put(Owls.owlMachineGun, BitmapFactory.decodeResource(getResources(), R.drawable.owl1attacked));
		bmpOwlAttacked.put(Owls.owlBaretta, BitmapFactory.decodeResource(getResources(), R.drawable.owl2attacked));
		bmpOwlAttacked.put(Owls.owlSniper, BitmapFactory.decodeResource(getResources(), R.drawable.owl3attacked));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("onCreate:" + this);
		super.onCreate(savedInstanceState);
		init = false;
		context = this;
		recallDeviceMetrics();
		dbDBWriteUtil = new DBWriteUtil(this);
		dbDBWriteUtil.addOrUpdateScore("0", "0", "1", "1", "" + new Date().getTime());
		setContentView(R.layout.start);
		startIntroSound();
		hideTouch();
		// Thread t = new Thread() {
		// public void run() {
		initResources();
		startShowTouch();
		// }
		// };
		// t.start();

		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);
		relativeLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (!init)
					return false;

				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					Intent intent = new Intent(StartActivity.this, MenuActivity.class);
					startActivity(intent);
				}
				return false;
			}
		});
	}

	@Override
	protected void onResume() {
		System.out.println("onResume:" + this);
		super.onResume();
		draw();
		if (init) {
			if (MenuActivity.soundOn) {
				startIntroSound();
			} else {
				stopIntroSound();
			}
		}
		printMemory();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		clearDrawables();
		try {
			timerAnimation.cancel();
		} catch (Exception e) {
		}
		StartActivity.printMemory();
	}

	@Override
	protected void onPause() {
		System.out.println("onPause:" + this);
		super.onPause();
		clearDrawables();
		stopIntroSound();
	}

	@Override
	protected void onStop() {
		System.out.println("onStop:" + this);
		super.onStop();
		clearDrawables();
		printMemory();
	}

	private void draw() {
		RelativeLayout relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
		relativeLayout1.setBackgroundResource(R.drawable.cover);
		ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView1.setImageResource(R.drawable.touchtocontinue);
	}

	private void startShowTouch() {
		final Runnable r = new Runnable() {
			public void run() {
				startBlink();
			}
		};
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				mHandler.post(r);
			}
		};
		timerAnimation = new Timer();
		timerAnimation.schedule(task, 100);
	}

	@Override
	public void onBackPressed() {
	}

	private void startBlink() {
		final Runnable r = new Runnable() {
			public void run() {
				blink();
			}
		};
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				mHandler.post(r);
			}
		};
		Calendar calendar = Calendar.getInstance();
		// calendar.add(Calendar.MILLISECOND, 5000);
		timerAnimation = new Timer();
		timerAnimation.schedule(task, calendar.getTime(), 1000);
	}

	protected void blink() {
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		if (imageView.getVisibility() == View.VISIBLE) {
			imageView.setVisibility(View.INVISIBLE);
		} else {
			imageView.setVisibility(View.VISIBLE);
		}
	}

	protected void hideTouch() {
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		imageView.setVisibility(View.INVISIBLE);
	}

	protected void showTouch() {
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		imageView.setVisibility(View.VISIBLE);
	}

	private void clearDrawables() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);
		relativeLayout.setBackgroundDrawable(null);
		ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView1.setImageDrawable(null);
	}

	public static void startIntroSound() {
		if (mp1 == null) {
			mp1 = MediaPlayer.create(StartActivity.context, R.raw.crickets_at_night);
			mp1.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp1.setLooping(true);
		}
		if (!mp1.isPlaying())
			mp1.start();
	}

	public static void stopIntroSound() {
		mp1.pause();
	}

	public static void printMemory() {
		Double allocated = new Double(Debug.getNativeHeapAllocatedSize()) / new Double((1048576));
		Double available = new Double(Debug.getNativeHeapSize() / 1048576.0);
		Double free = new Double(Debug.getNativeHeapFreeSize() / 1048576.0);
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);

		System.out.println("debug. =================================");
		System.out.println("debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free) ");
	}
}
