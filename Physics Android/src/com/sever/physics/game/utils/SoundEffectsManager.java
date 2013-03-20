package com.sever.physics.game.utils;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.sever.physic.PhysicsActivity;
import com.sever.physic.PhysicsApplication;
import com.sever.physic.R;

public class SoundEffectsManager {

	static SoundEffectsManager self = null;
	private static final Integer EXPLODE_BOMB = 1;
	private static final Integer EXPLODE_ROBOT = 2;
	private static final Integer LAUNCH_ROCKET = 3;
	private static final Integer THROW_BOMB = 4;
	private static final Integer PLAYER_THROW_BOMB = 5;
	private static final Integer ROBOT_THROTTLE = 6;
	private static final Integer POWER_UP = 7;
	private static final Integer STAGE_UP = 8;
	private static final Integer PAUSE_MENU = 9;
	private static final Integer BUTTON_CLICK = 10;
	private static final Integer STAGE_START = 11;
	private static final Integer GAME_OVER = 12;
	private static final Integer NEW_WEAPON = 13;

	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	private static MediaPlayer mp1;

	static {

	}

	public static SoundEffectsManager getManager() {
		if (self == null)
			self = new SoundEffectsManager();
		return self;
	}

	public void initSounds() {
		PhysicsActivity.context.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		soundPoolMap = new HashMap<Integer, Integer>();

		// soundPoolMap.put(INTRO, soundPool.load(context,
		// R.raw.crickets_at_night, 1));
		// soundPoolMap.put(MENU, soundPool.load(context, R.raw.nightime, 1));
		soundPoolMap.put(EXPLODE_BOMB, soundPool.load(PhysicsActivity.context, R.raw.balloonpopping, 1));
		soundPoolMap.put(EXPLODE_ROBOT, soundPool.load(PhysicsActivity.context, R.raw.hithurt, 1));
		soundPoolMap.put(LAUNCH_ROCKET, soundPool.load(PhysicsActivity.context, R.raw.spinjump, 1));
		soundPoolMap.put(THROW_BOMB, soundPool.load(PhysicsActivity.context, R.raw.bananapeelslipzip, 1));
		soundPoolMap.put(PLAYER_THROW_BOMB, soundPool.load(PhysicsActivity.context, R.raw.bananapeelslipzip, 1));
		soundPoolMap.put(ROBOT_THROTTLE, soundPool.load(PhysicsActivity.context, R.raw.pindrop, 1));
		soundPoolMap.put(POWER_UP, soundPool.load(PhysicsActivity.context, R.raw.powerup, 1));
		soundPoolMap.put(STAGE_UP, soundPool.load(PhysicsActivity.context, R.raw.powerup, 1));
		soundPoolMap.put(PAUSE_MENU, soundPool.load(PhysicsActivity.context, R.raw.largebubble, 1));
		soundPoolMap.put(BUTTON_CLICK, soundPool.load(PhysicsActivity.context, R.raw.mariojumping, 1));
		soundPoolMap.put(GAME_OVER, soundPool.load(PhysicsActivity.context, R.raw.explosion, 1));
		soundPoolMap.put(STAGE_START, soundPool.load(PhysicsActivity.context, R.raw.robot, 1));
		soundPoolMap.put(NEW_WEAPON, soundPool.load(PhysicsActivity.context, R.raw.robot, 1));
	}

	public void stopSound(int sound) {
		soundPool.stop(soundPoolMap.get(sound));
	}

	public void playSound(Context context, int sound, boolean... b) {
		try {
			AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = 0.25f * streamVolumeCurrent / streamVolumeMax;

			/* Play the sound with the correct volume */
			if (b.length == 0) {
				soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f);
			} else if (b[0]) {
				soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 1, 1f);
			}
		} catch (Exception e) {
		}
	}

	public static void startGameEndAmbianceSound(Context context) {
		System.out.println("startIngameAmbianceSound");
		if (mp1 == null) {
			mp1 = MediaPlayer.create(context, R.raw.meditatewithchoir);
			mp1.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp1.setVolume(0.2f, 0.2f);
			mp1.setLooping(true);
		}
		if (!mp1.isPlaying())
			mp1.start();
	}
	
	public static void startBossTimeAmbianceSound(Context context) {
		System.out.println("startIngameAmbianceSound");
		if (mp1 == null) {
			mp1 = MediaPlayer.create(context, R.raw.toppriority);
			mp1.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp1.setVolume(0.2f, 0.2f);
			mp1.setLooping(true);
		}
		if (!mp1.isPlaying())
			mp1.start();
	}

	public static void startGameOverAmbianceSound(Context context) {
		System.out.println("startIngameAmbianceSound");
		if (mp1 == null) {
			mp1 = MediaPlayer.create(context, R.raw.peopleinexile);
			mp1.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp1.setVolume(0.2f, 0.2f);
			mp1.setLooping(true);
		}
		if (!mp1.isPlaying())
			mp1.start();
	}

	public static void startIngameAmbianceSound(Context context) {
		System.out.println("startIngameAmbianceSound");
		if (mp1 == null) {
			mp1 = MediaPlayer.create(context, R.raw.accelerator);
			mp1.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp1.setVolume(0.2f, 0.2f);
			mp1.setLooping(true);
		}
		if (!mp1.isPlaying())
			mp1.start();
	}

	public static void startIntroAmbianceSound(Context context) {
		System.out.println("startIntroAmbianceSound");
		if (mp1 == null) {
			mp1 = MediaPlayer.create(context, R.raw.acoustica);
			mp1.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp1.setVolume(0.2f, 0.2f);
			mp1.setLooping(true);
		}
		if (!mp1.isPlaying())
			mp1.start();
	}

	public static void stopSound() {
		System.out.println("stopSound");
		if (mp1 != null) {
			mp1.pause();
			mp1.stop();
			mp1.release();
			mp1 = null;
		}
	}

	public void playNEW_WEAPON(Context context) {
		playSound(context, NEW_WEAPON);
	}

	public void playSTAGE_START(Context context) {
		playSound(context, STAGE_START);
	}

	public void playGAME_OVER(Context context) {
		playSound(context, GAME_OVER);
	}

	public void playBUTTON_CLICK(Context context) {
		playSound(context, BUTTON_CLICK);
	}

	public void playPAUSE_MENU(Context context) {
		playSound(context, PAUSE_MENU);
	}

	public void playPOWER_UP(Context context) {
		playSound(context, POWER_UP);
	}

	public void playSTAGE_UP(Context context) {
		playSound(context, STAGE_UP);
	}

	public void playEXPLODE_BOMB(Context context) {
		playSound(context, EXPLODE_BOMB);
	}

	public void playEXPLODE_ROBOT(Context context) {
		playSound(context, EXPLODE_ROBOT);
	}

	public void playLAUNCH_ROCKET(Context context) {
		playSound(context, this.LAUNCH_ROCKET);
	}

	public void playTHROW_BOMB(Context context) {
		playSound(context, this.THROW_BOMB);
	}

	public void playROBOT_THROTTLE(Context context) {
		playSound(context, this.ROBOT_THROTTLE);
	}

	public void playPLAYER_THROW_BOMB(Context context) {
		playSound(context, this.PLAYER_THROW_BOMB);
	}
}
