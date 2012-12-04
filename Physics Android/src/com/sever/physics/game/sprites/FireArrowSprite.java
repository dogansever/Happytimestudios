package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.sever.physics.game.GameView;

public class FireArrowSprite extends FreeSprite {

	public float ystick1 = 0;
	public float ystick2 = 0;

	public FireArrowSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, Bitmap bmp, float x, float y, int bmpColumns, int bmpRows) {
		BMP_COLUMNS = bmpColumns;
		BMP_ROWS = bmpRows;
		this.width = bmp.getWidth() / BMP_COLUMNS;
		this.height = bmp.getHeight() / BMP_ROWS;
		this.bmp = bmp;
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.manualFrameSet = true;
		this.invisible = true;
		this.spriteList = spriteList;
	}

	public void onDown(float xn, float yn) {
		this.ystick1 = yn;
		this.ystick2 = yn;
		makeVisible();
		// angle = 0;
	}

	public void onMove(float xn, float yn) {
		try {
			this.ystick2 = yn;
			if (ystick1 - ystick2 < 0) {
				angle++;
			} else if (ystick1 - ystick2 > 0) {
				angle--;
			}

			if (angle < -45) {
				angle = -45;
			} else if (angle > 45) {
				angle = 45;
			}
			getAngle();
			ystick1 = ystick2;
		} catch (Exception e) {
		}
	}

	public void onUp(float xn, float yn) {
		makeInvisible();
	}

	public void onDraw(Canvas canvas) {
		currentRow = 0;
		currentFrame = 0;
		draw(canvas);
	}

	private void draw(Canvas canvas) {
		x = ((PlayerSprite) gameView.getPlayerSprite()).x + ((PlayerSprite) gameView.getPlayerSprite()).width * (((PlayerSprite) gameView.getPlayerSprite()).facingRigth ? 1 : -1);
		y = gameView.getPlayerSprite().y;
		int srcX = (int) (currentFrame * width);
		int srcY = (int) (currentRow * height);
		Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
		bmpFrame = Bitmap.createBitmap(bmp, src.left, src.top, (int) width, (int) height);

		if (bmp != null && isVisible()) {
			Matrix m = new Matrix();
			if (gameView.getPlayerSprite().facingRigth) {
				m.postRotate(360-angle, 0, height);
			} else {
				m.postRotate(angle, width, height);
			}
			Vec2 translate = getBitmapDrawingXY();
			m.postTranslate(translate.x, translate.y);
			if (gameView.getPlayerSprite().facingRigth) {
				Matrix mirrorMatrix = new Matrix();
				mirrorMatrix.preScale(-1.0f, 1.0f);
				bmpFrame = Bitmap.createBitmap(bmpFrame, 0, 0, bmpFrame.getWidth(), bmpFrame.getHeight(), mirrorMatrix, false);
			}

			canvas.drawBitmap(bmpFrame, m, null);
		}
	}

	public float getAngle() {
		System.out.println("getAngle():" + (angle + 45));
		return angle + 45;
	}

}
