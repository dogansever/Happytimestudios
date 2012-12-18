package com.sever.physic;

import android.app.Application;
import android.util.DisplayMetrics;

public class PhysicsApplication extends Application {

	public static float deviceWidth;
	public static float deviceHeight;
	
	@Override
	public void onCreate() {
		super.onCreate();
		recallDeviceMetrics();
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
}
