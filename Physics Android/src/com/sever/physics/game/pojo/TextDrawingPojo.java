package com.sever.physics.game.pojo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

import com.sever.physic.IntroActivity;
import com.sever.physic.PhysicsApplication;
import com.sever.physics.game.utils.Constants;

public class TextDrawingPojo {
	public String text;
	public float x;
	public float y;
	public float textSize = 25;
	public int alpha = 255;
	public int alphaDelta = 0;
	public int color = Color.WHITE;
	public int color2 = Color.WHITE;
	public Align align = Paint.Align.CENTER;
	public Paint paint;
	public int duration = 0;
	public int durationSpent = 0;
	public boolean noalpha;

	public TextDrawingPojo(String text, float x, float y, int alpha, int color, Align align) {
		this(text, x, y, alpha);
		this.color = color;
		this.align = align;
	}

	public TextDrawingPojo(String text, float x, float y, int alpha) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.alpha = alpha;
	}

	public TextDrawingPojo(String text, float x, float y, boolean noalpha) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.noalpha = noalpha;
	}
	
	public TextDrawingPojo(String text, float x, float y, boolean noalpha, int color, int color2) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.noalpha = noalpha;
		this.color = color;
		this.color2 = color2;
	}

	public TextDrawingPojo(String text, int x, int y, int alpha, Align align, int duration, int color, int color2) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.alpha = alpha;
		this.align = align;
		this.duration = duration;
		this.color = color;
		this.color2 = color2;
		// TODO Auto-generated constructor stub
	}

	public void onDraw(Canvas canvas) {
		if (alpha <= 0)
			return;

		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setTypeface(IntroActivity.tf);
		paint.setColor(durationSpent % 2 == 0 ? color : color2);
		paint.setAlpha(alpha <= 0 ? 0 : alpha);
		paint.setTextSize(20);
		paint.setTextAlign(align);

		canvas.drawText(text, x - Constants.extraWidthOffset, PhysicsApplication.deviceHeight - y + Constants.extraHeightOffset - (255 - alpha), paint);

		// if (duration != 0) {
		durationSpent++;
		// }

		if (!noalpha) {
			if (alphaDelta == 0) {
				alpha = 255 * (duration - durationSpent) / duration;
				alpha = alpha < 0 ? 0 : alpha;
			} else {
				alpha -= alphaDelta;
			}
		} else {
			// one time
//			alpha = 0;
		}
	}
}
