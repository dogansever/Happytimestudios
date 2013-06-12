package com.sever.physics.game.utils;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

import com.sever.physic.IntroActivity;
import com.sever.physics.game.pojo.TextDrawingPojo;

public class TextDrawingManager {

	static TextDrawingManager self = null;

	public static TextDrawingManager getManager() {
		if (self == null)
			self = new TextDrawingManager();
		return self;
	}

	public ConcurrentLinkedQueue<TextDrawingPojo> textDrawingPojoList = new ConcurrentLinkedQueue<TextDrawingPojo>();
	public ConcurrentLinkedQueue<TextDrawingPojo> textDrawingPojoListStatic = new ConcurrentLinkedQueue<TextDrawingPojo>();

	public void onDraw(Canvas canvas) {
		for (TextDrawingPojo tdpojo : textDrawingPojoList) {
			tdpojo.alphaDelta = 5;
			if (tdpojo.alpha > 0) {
				tdpojo.onDraw(canvas);
			} else {
				textDrawingPojoList.remove(tdpojo);
			}
		}
		for (TextDrawingPojo tdpojo : textDrawingPojoListStatic) {
			if (tdpojo.alpha > 0) {
				tdpojo.onDraw(canvas);
			} else {
				textDrawingPojoListStatic.remove(tdpojo);
			}
		}
	}

	public void addTextDrawingPojoStatic(TextDrawingPojo tdpojo) {
		textDrawingPojoListStatic.add(tdpojo);
	}

	public void addTextDrawingPojo(TextDrawingPojo tdpojo) {
		textDrawingPojoList.add(tdpojo);
	}

	public static int colorIndex = 0;

	public static void drawText(String text, Canvas canvas, float x, float y, int alpha, Align align, int... color) {
		if (alpha <= 0)
			return;

		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setTypeface(IntroActivity.tf);
		paint.setColor(color.length == 0 ? Color.WHITE : color.length == 1 ? color[0] : color[colorIndex++ % 2]);
		paint.setAlpha(alpha <= 0 ? 0 : alpha);
		paint.setTextSize(20);
		paint.setTextAlign(align);
		canvas.drawText(text, x, y, paint);
	}

	public void drawText(String text, Canvas canvas, float x, float y) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		paint.setTypeface(IntroActivity.tf);
		paint.setColor(Color.WHITE);
		paint.setTextSize(25);
		// paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(text, x, y, paint);
	}
}
