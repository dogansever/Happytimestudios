package com.sever.physics.game.sprites;

import java.util.Date;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;

import com.sever.physic.IntroActivity;
import com.sever.physic.PhysicsActivity;
import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.SpriteBmp;

public class BonusLifeBarSprite extends FreeSprite {

	private long time;
	private long timeStart;
	private long time_MAX = 60 * 10 * 1000;

	public BonusLifeBarSprite(GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
		this.manualFrameSet = true;
	}

	public float getBonusScoreSeconds() {
		return ((float) time) / 1000;
	}

	public float getBonusLifePercentage() {
		return ((float) time) / ((float) time_MAX);
	}

	public void resetTimer() {
		time = time_MAX;
		timeStart = new Date().getTime();
	}

	public void onDraw(Canvas canvas) {
		time = time_MAX - (new Date().getTime() - timeStart);
		time = time <= 0 ? 0 : time;
		long sec = (time / 1000) % 60;
		long min = time / (1000 * 60);
		spriteBmp.currentFrame = 0;
		spriteBmp.currentRow = (int) (spriteBmp.BMP_ROWS * (time) / time_MAX);
		spriteBmp.currentRow = spriteBmp.currentRow == spriteBmp.BMP_ROWS ? spriteBmp.currentRow - 1 : spriteBmp.currentRow;
		Vec2 translate = getBitmapDrawingXY();
		drawText("" + min + ":" + String.format("%02d", (int) sec), canvas, translate.x, translate.y);
		draw(canvas);
	}

	private void draw(Canvas canvas) {
		int srcX = (int) (spriteBmp.currentFrame * width);
		int srcY = (int) (spriteBmp.currentRow * height);
		Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
		spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.getBitmap(), src.left, src.top, (int) width, (int) height);

		Matrix m = new Matrix();
		Vec2 translate = getBitmapDrawingXY();
		m.postTranslate(translate.x, translate.y);
		canvas.drawBitmap(spriteBmp.bmpFrame, m, null);
	}

	private void drawText(String text, Canvas canvas, float x, float y) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		paint.setTypeface(IntroActivity.tf);
		paint.setColor(Color.WHITE);
		paint.setTextSize(20);
		canvas.drawText(text, x, y, paint);
	}
}
