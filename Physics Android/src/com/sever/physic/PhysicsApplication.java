package com.sever.physic;

import android.app.Application;
import android.util.DisplayMetrics;

import com.sever.physics.game.utils.LogUtil;

public class PhysicsApplication extends Application {

	public static float deviceWidth;
	public static float deviceHeight;

	@Override
	public void onCreate() {
		LogUtil.log("onCreate:" + this);
		super.onCreate();
		recallDeviceMetrics();
	}

	public void recallDeviceMetrics() {
		try {
			LogUtil.log("Getting display metrics...");
			DisplayMetrics metrics = this.getApplicationContext().getResources().getDisplayMetrics();
			deviceWidth = metrics.widthPixels;
			deviceHeight = metrics.heightPixels;
			LogUtil.log("Getting display metrics success: w:" + deviceWidth + ", h:" + deviceHeight);
		} catch (Exception e) {
			LogUtil.log("Getting display metrics error!");
		}
	}

	@Override
	public void onLowMemory() {
		LogUtil.log("onLowMemory:" + this);
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		LogUtil.log("onTerminate:" + this);
		super.onTerminate();
	}

}
