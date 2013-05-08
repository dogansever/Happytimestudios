package com.sever.main;

import android.app.Application;

public class MathApplication extends Application {


	@Override
	public void onCreate() {
		super.onCreate();
		try {
			// playtomic = Playtomic.getInstance(951600, "39cbdfcad51749f0",
			// "70da48a7c1c04f398b53a45bbc1285", this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
