package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.SpriteBmp;

public class FireArrowSprite extends FreeSprite {

	public float ystick1 = 0;
	public float ystick2 = 0;

	public FireArrowSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
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
				angle += 3;
			} else if (ystick1 - ystick2 > 0) {
				angle -= 3;
			}

			if (angle < -90) {
				angle = -90;
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
			m.postTranslate(translate.x, translate.y);
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
