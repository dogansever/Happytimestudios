package com.sever.physics.game.utils;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.sever.physic.PhysicsActivity;
import com.sever.physic.R;

public class SoundEffectsManager {

	static SoundEffectsManager self = null;
	
	public static final Integer EXPLODE_BOMB = 1;
	public static final Integer EXPLODE_ROBOT = 2;
	public static final Integer LAUNCH_ROCKET = 3;
	public static final Integer THROW_BOMB = 4;
	public static final Integer PLAYER_THROW_BOMB = 5;
	public static final Integer ROBOT_THROTTLE = 6;
	public static final Integer POWER_UP = 7;
	public static final Integer STAGE_UP = 8;
	public static final Integer PAUSE_MENU = 9;
	public static final Integer BUTTON_CLICK = 10;
	public static final Integer STAGE_START = 11;
	public static final Integer GAME_OVER = 12;
	public static final Integer NEW_WEAPON = 13;

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

	public void playSound(Context context, int sound, boolean... loop) {
		try {
			AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = 0.25f * streamVolumeCurrent / streamVolumeMax;

			/* Play the sound with the correct volume */
			if (loop.length == 0) {
				soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f);
			} else if (loop[0]) {
				soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 1, 1f);
			}
		} catch (Exception e) {
		}
	}

	public static void startGeneralAmbianceSound(Context context, int resId) {
		LogUtil.log("startGeneralAmbianceSound:" + resId);
		if (mp1 == null) {
			mp1 = MediaPlayer.create(context, resId);
			mp1.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp1.setVolume(0.2f, 0.2f);
			mp1.setLooping(true);
		}
		if (!mp1.isPlaying())
			mp1.start();
	}

	public static void startGameEndAmbianceSound(Context context) {
		LogUtil.log("startIngameAmbianceSound");
		startGeneralAmbianceSound(context, R.raw.meditatewithchoir);
	}

	public static void startBossTimeAmbianceSound(Context context) {
		LogUtil.log("startBossTimeAmbianceSound");
		startGeneralAmbianceSound(context, R.raw.toppriority);
	}

	public static void startGameOverAmbianceSound(Context context) {
		LogUtil.log("startGameOverAmbianceSound");
		startGeneralAmbianceSound(context, R.raw.peopleinexile);
	}

	public static void startIngameAmbianceSound(Context context) {
		LogUtil.log("startIngameAmbianceSound");
		startGeneralAmbianceSound(context, R.raw.accelerator);
	}

	public static void startIntroAmbianceSound(Context context) {
		LogUtil.log("startIntroAmbianceSound");
		startGeneralAmbianceSound(context, R.raw.acoustica);
	}

	public static void stopSound() {
		LogUtil.log("stopSound");
		if (mp1 != null) {
			mp1.pause();
			mp1.stop();
			mp1.release();
			mp1 = null;
		}
	}

}
