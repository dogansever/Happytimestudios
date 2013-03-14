package com.sever.physics.game.pojo;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class TextDrawingPojo {
	public String text;
	public float x;
	public float y;
	public float textSize = 25;
	public int alpha;
	public int color = Color.WHITE;
	public Align align = Paint.Align.CENTER;

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
}
