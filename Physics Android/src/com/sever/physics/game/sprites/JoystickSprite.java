package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.sever.physics.game.GameView;

public class JoystickSprite extends FreeSprite {

	public float xstick = 0;
	public float ystick = 0;

	public JoystickSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, Bitmap bmp, float x, float y, int bmpColumns, int bmpRows) {
		BMP_COLUMNS = bmpColumns;
		BMP_ROWS = bmpRows;
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight() / BMP_ROWS;
		this.bmp = bmp;
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
		this.manualFrameSet = true;
		this.invisible = true;
		this.spriteList = spriteList;
	}

	public void onDown(float xn, float yn) {
		this.x = xn;
		this.y = yn;
		this.xstick = xn;
		this.ystick = yn;
		makeVisible();
	}

	public void onMove(float xn, float yn) {
		this.xstick = xn;
		this.ystick = yn;
		if (x - xstick < -width * 0.3f) {
			((PlayerSprite) gameView.getPlayerSprite()).throttleRight();
		}
		if (x - xstick > width * 0.3f) {
			((PlayerSprite) gameView.getPlayerSprite()).throttleLeft();
		}
		if (y - ystick < -width * 0.3f) {
			((PlayerSprite) gameView.getPlayerSprite()).throttleUp();
		}
		if (y - ystick > width * 0.3f) {
			((PlayerSprite) gameView.getPlayerSprite()).throttleDown();
		}
	}

	public void onUp(float xn, float yn) {
		makeInvisible();
	}

	public void onDraw(Canvas canvas) {
		currentRow = 0;
		currentFrame = 0;
		draw(canvas);

		float xtemp = x;
		float ytemp = y;
		x = xstick;
		y = ystick;
		currentFrame = 1;
		draw(canvas);
		x = xtemp;
		y = ytemp;
	}

	private void draw(Canvas canvas) {
		int srcX = (int) (currentFrame * width);
		int srcY = (int) (currentRow * height);
		Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
		Rect dst = new Rect((int) (x), (int) (y), (int) (x + width), (int) (y + height));
		Paint p = new Paint();
		bmpFrame = Bitmap.createBitmap(bmp, src.left, src.top, (int) width, (int) height);

		if (bmp != null && isVisible()) {
			Matrix m = new Matrix();
			Vec2 translate = getBitmapDrawingXY();
			m.postTranslate(translate.x, translate.y);
			canvas.drawBitmap(bmpFrame, m, null);
		}
	}

}
