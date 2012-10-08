package com.sever.loc.main;

import android.content.Context;
import android.graphics.Canvas;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class CustomizedMyLocationOverlay extends MyLocationOverlay {

	public CustomizedMyLocationOverlay(Context context, MapView mapView) {
		super(context, mapView);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void drawCompass(Canvas canvas, float bearing) {
//		canvas.translate(0, 250);
		super.drawCompass(canvas, bearing);
	}

	@Override
	protected void drawMyLocation(Canvas canvas, MapView mapView, Location lastFix, GeoPoint myLocation, long when) {
		// TODO Auto-generated method stub
		super.drawMyLocation(canvas, mapView, lastFix, myLocation, when);
	}

	@Override
	public synchronized void onSensorChanged(int sensor, float[] values) {
		// TODO Auto-generated method stub
		super.onSensorChanged(sensor, values);
	}

}
