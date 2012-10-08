package com.sever.loc.main;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.view.MotionEvent;

public class Test extends ActivityInstrumentationTestCase2 {

	private Instrumentation inst;

	public Test(String pkg, Class activityClass) {
		super(pkg, activityClass);
		// TODO Auto-generated constructor stub
	}

	public void simulateClick() {
		System.out.println("simulateClick");
		try {
			inst = new Instrumentation();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		long downTime = SystemClock.uptimeMillis();
		// event time MUST be retrieved only by this way!
		long eventTime = SystemClock.uptimeMillis();

		float xStart = FirstActivity.deviceWidth -20;
		float yStart = FirstActivity.deviceHeight - 20;
		MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xStart, yStart, 0);
		inst.sendPointerSync(event);
		// eventTime = SystemClock.uptimeMillis() + 1000;
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, xStart, yStart, 0);
		inst.sendPointerSync(event);
	}
}
