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
	private static final Integer EXPLODE_BOMB = 1;
	private static final Integer EXPLODE_ROBOT = 2;
	private static final Integer LAUNCH_ROCKET = 3;
	private static final Integer THROW_BOMB = 4;
	private static final Integer PLAYER_THROW_BOMB = 5;
	private static final Integer ROBOT_THROTTLE = 6;
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
		soundPoolMap.put(EXPLODE_BOMB, soundPool.load(PhysicsActivity.context, R.raw.bomb01, 1));
		soundPoolMap.put(EXPLODE_ROBOT, soundPool.load(PhysicsActivity.context, R.raw.robot_explode, 1));
		soundPoolMap.put(LAUNCH_ROCKET, soundPool.load(PhysicsActivity.context, R.raw.rocketlaunching01, 1));
		soundPoolMap.put(THROW_BOMB, soundPool.load(PhysicsActivity.context, R.raw.throwbomb, 1));
		soundPoolMap.put(PLAYER_THROW_BOMB, soundPool.load(PhysicsActivity.context, R.raw.robotthrowing, 1));
		soundPoolMap.put(ROBOT_THROTTLE, soundPool.load(PhysicsActivity.context, R.raw.robot, 1));
	}

	public void stopSound(int sound) {
		soundPool.stop(soundPoolMap.get(sound));
	}

	public void playSound(int sound, boolean... b) {
		try {
			AudioManager mgr = (AudioManager) PhysicsActivity.context.getSystemService(Context.AUDIO_SERVICE);
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

	public static void startIntroSound() {
		if (mp1 == null) {
			mp1 = MediaPlayer.create(PhysicsActivity.context, R.raw.intro_sonarping);
			mp1.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp1.setLooping(true);
		}
		if (!mp1.isPlaying())
			mp1.start();
	}

	public static void stopIntroSound() {
		mp1.pause();
	}

	public void playEXPLODE_BOMB() {
		playSound(EXPLODE_BOMB);
	}

	public void playEXPLODE_ROBOT() {
		playSound(EXPLODE_ROBOT);
	}

	public void playLAUNCH_ROCKET() {
		playSound(this.LAUNCH_ROCKET);
	}

	public void playTHROW_BOMB() {
		playSound(this.THROW_BOMB);
	}

	public void playROBOT_THROTTLE() {
		playSound(this.ROBOT_THROTTLE);
	}

	public void playPLAYER_THROW_BOMB() {
		playSound(this.PLAYER_THROW_BOMB);
	}
}
