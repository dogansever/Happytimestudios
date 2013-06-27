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

	private static final String adUnitId = "a151a46ea1cd8a4";
	static AdUtil self = null;

	public static AdUtil getAdUtil() {
		if (self == null)
			self = new AdUtil();
		return self;
	}

	public AdView adView;

	public void createAd(Activity a) {
		LogUtil.log("createAd");
		adView = new AdView(a, AdSize.BANNER, adUnitId);
		LinearLayout layout = (LinearLayout) a.findViewById(R.id.linearLayoutAdview);
		layout.removeAllViews();
		layout.addView(adView);
		adView.loadAd(new AdRequest());
	}

	public void destroyAd() {
		LogUtil.log("destroyAd");
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
