package com.sever.ramsandgoats;

import android.app.Application;
import android.util.DisplayMetrics;

import com.playtomic.android.api.Playtomic;

public class GameApplication extends Application {

	public static float deviceWidth;
	public static float deviceHeight;

	private Playtomic playtomic = null;

	@Override
	public void onCreate() {
		System.out.println("onCreate:" + this);
		super.onCreate();
		recallDeviceMetrics();
		try {
//			playtomic = Playtomic.getInstance(972663, "bf963398a35f44c4", "6450286da97f4521836d20a7c664da", this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void recallDeviceMetrics() {
		try {
			System.out.println("Getting display metrics...");
			DisplayMetrics metrics = this.getApplicationContext().getResources().getDisplayMetrics();
			deviceWidth = metrics.widthPixels;
			deviceHeight = metrics.heightPixels;
			System.out.println("Getting display metrics success: w:" + deviceWidth + ", h:" + deviceHeight);
		} catch (Exception e) {
			System.out.println("Getting display metrics error!");
		}
	}

	public Playtomic getPlaytomic() {
		return playtomic;
	}

	public void setPlaytomic(Playtomic playtomic) {
		this.playtomic = playtomic;
	}

	@Override
	public void onLowMemory() {
		System.out.println("onLowMemory:" + this);
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		System.out.println("onTerminate:" + this);
		super.onTerminate();
	}

}
