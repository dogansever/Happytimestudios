package com.sever.physics.game.utils;

import android.app.Activity;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.sever.physic.IntroActivity;
import com.sever.physic.R;
import com.sever.physic.Test;

public class AdUtil {

	static AdUtil self = null;

	public static AdUtil getAdUtil() {
		if (self == null)
			self = new AdUtil();
		return self;
	}

	public AdView adView;

	public void createAd(Activity a) {
		System.out.println("createAd");
		adView = new AdView(a, AdSize.BANNER, "a15121f41d20fd7");
		LinearLayout layout = (LinearLayout) a.findViewById(R.id.linearLayoutAdview);
		layout.removeAllViews();
		layout.addView(adView);
		adView.loadAd(new AdRequest());
	}

	public void destroyAd() {
		System.out.println("destroyAd");
		adView.destroy();
	}

	public void simClick() {
		Thread t = new Thread() {
			public void run() {
				new Test("com.sever.physic", IntroActivity.class).simulateClick();
			}
		};
		t.start();
	}
}
