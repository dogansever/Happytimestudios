package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;

public class FireArrowSprite extends FreeSprite {

	public float ystick1 = 0;
	public float ystick2 = 0;
	private ActiveSprite activeSprite;

	public FireArrowSprite(ActiveSprite activeSprite, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.manualFrameSet = true;
		this.invisible = true;
		this.activeSprite = activeSprite;
	}

	public void onDown(float xn, float yn) {
		this.ystick1 = yn;
		this.ystick2 = yn;
		makeVisible();
		// angle = 0;
	}

	public void onMove(float xn, float yn) {
		try {
			float angleStep = 3;
			float angleMin = -45;
			float angleMax = 45;
			this.ystick2 = yn;
			if (ystick1 - ystick2 < 0) {
				angle -= angleStep;
			} else if (ystick1 - ystick2 > 0) {
				angle += angleStep;
			}

			if (angle < -45 + angleMin) {
				angle = -45 + angleMin;
			} else if (angle > -45 + angleMax) {
				angle = -45 + angleMax;
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
		spriteBmp.currentRow = 0;
		// currentFrame = 0;
		spriteBmp.currentFrame = spriteBmp.BMP_COLUMNS * ((PlayerSprite) gameView.getPlayerSprite()).firePower / ((PlayerSprite) gameView.getPlayerSprite()).firePower_MAX;
		spriteBmp.currentFrame = spriteBmp.currentFrame == spriteBmp.BMP_COLUMNS ? spriteBmp.currentFrame - 1 : spriteBmp.currentFrame;
		draw(canvas);
	}

	private void draw(Canvas canvas) {
		x = ((PlayerSprite) gameView.getPlayerSprite()).x + ((PlayerSprite) gameView.getPlayerSprite()).width * (((PlayerSprite) gameView.getPlayerSprite()).facingRigth ? 1 : -1);
		y = gameView.getPlayerSprite().y;
		int srcX = (int) (spriteBmp.currentFrame * width);
		int srcY = (int) (spriteBmp.currentRow * height);
		Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
		spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.getBitmap(), src.left, src.top, (int) width, (int) height);

		if (spriteBmp.getBitmap() != null && isVisible()) {
			Matrix m = new Matrix();
			if (gameView.getPlayerSprite().facingRigth) {
				m.postRotate(360 - angle, 0, height);
			} else {
				m.postRotate(angle, width, height);
			}
			Vec2 translate = getBitmapDrawingXY();
			m.postTranslate(translate.x - Constants.extraWidthOffset, translate.y + Constants.extraHeightOffset);
			if (gameView.getPlayerSprite().facingRigth) {
				Matrix mirrorMatrix = new Matrix();
				mirrorMatrix.preScale(-1.0f, 1.0f);
				spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.bmpFrame, 0, 0, spriteBmp.bmpFrame.getWidth(), spriteBmp.bmpFrame.getHeight(), mirrorMatrix, false);
			}

			canvas.drawBitmap(spriteBmp.bmpFrame, m, null);
		}
	}

	public float getAngle() {
		// System.out.println("getAngle():" + (angle + 45));
		return angle + 45;
	}

}
