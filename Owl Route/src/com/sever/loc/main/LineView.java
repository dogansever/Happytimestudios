package com.sever.loc.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

public class LineView extends View {
	public static float STROKEWIDTH = 8;
	Paint paint = new Paint();
	private Point p1;
	private Point p2;
	private Bitmap bitmap;

	public LineView(Context context, Point p1, Point p2) {
		super(context);
		this.p1 = p1;
		this.p2 = p2;
		paint.setColor(Color.BLUE);
		paint.setAlpha(128);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(STROKEWIDTH);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.star);
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (LocationMeterActivity.MYLOCATION_LOCK) {
			float r = LocationMeterActivity.deviceWidth / 2 - bitmap.getWidth() / 2;
			float blend = r / (float) Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
			float left = p1.x + blend * (p2.x - p1.x) - bitmap.getWidth() / 2;
			float top = p1.y + blend * (p2.y - p1.y) - bitmap.getHeight() / 2;
			paint.setAlpha(255);
			canvas.drawBitmap(bitmap, left, top, paint);
		} else {
			paint.setAlpha(128);
			canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
		}
		// canvas.drawLine(20, 0, 0, 20, paint);
	}

}