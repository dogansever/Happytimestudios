package com.sever.physic;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.view.MotionEvent;

import com.sever.physics.game.utils.LogUtil;


public class Test extends ActivityInstrumentationTestCase2 {

	private Instrumentation inst;

	public Test(String pkg, Class activityClass) {
		super(pkg, activityClass);
		// TODO Auto-generated constructor stub
	}

	public void simulateClick() {
		LogUtil.log("simulateClick");
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

		float xStart = PhysicsApplication.deviceWidth -20;
		float yStart = PhysicsApplication.deviceHeight - 20;
		MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xStart, yStart, 0);
		inst.sendPointerSync(event);
		// eventTime = SystemClock.uptimeMillis() + 1000;
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, xStart, yStart, 0);
		inst.sendPointerSync(event);
	}
	

}
