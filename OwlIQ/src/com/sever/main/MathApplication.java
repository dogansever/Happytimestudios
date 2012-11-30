package com.sever.main;

import android.app.Application;

import com.playtomic.android.api.Playtomic;

public class MathApplication extends Application {

	private Playtomic playtomic = null;

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			playtomic = Playtomic.getInstance(951569, "1cd0455d0d204888", "3b5baaa262504d2dbe12fd6b1926ad", this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Playtomic.Log().view();
	}

	public Playtomic getPlaytomic() {
		return playtomic;
	}

	public void setPlaytomic(Playtomic playtomic) {
		this.playtomic = playtomic;
	}
}
