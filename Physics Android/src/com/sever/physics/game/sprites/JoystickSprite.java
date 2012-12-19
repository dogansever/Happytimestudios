package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.SpriteBmp;

public class JoystickSprite extends FreeSprite {

	public float xstick = 0;
	public float ystick = 0;

	public JoystickSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
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
		try {
			if (gameView.idle) {
				return;
			}

			this.xstick = xn;
			this.ystick = yn;
			if (x - xstick < -width * 0.2f) {
				((PlayerSprite) gameView.getPlayerSprite()).throttleRight();
			}
			if (x - xstick > width * 0.2f) {
				((PlayerSprite) gameView.getPlayerSprite()).throttleLeft();
			}
			if (y - ystick < -width * 0.2f) {
				((PlayerSprite) gameView.getPlayerSprite()).throttleUp();
			}
			if (y - ystick > width * 0.2f) {
				((PlayerSprite) gameView.getPlayerSprite()).throttleDown();
			}
		} catch (Exception e) {
		}
	}

	public void onUp(float xn, float yn) {
		makeInvisible();
	}

	public void onDraw(Canvas canvas) {

		if (gameView.idle) {
			return;
		}

		spriteBmp.currentRow = 0;
		spriteBmp.currentFrame = 0;
		draw(canvas);

		float xtemp = x;
		float ytemp = y;
		x = Math.abs(xstick - x) < width / 2 ? xstick : x + (xstick - x < 0 ? -1 : 1) * width / 2;
		y = Math.abs(ystick - y) < height / 2 ? ystick : y + (ystick - y < 0 ? -1 : 1) * height / 2;
		spriteBmp.currentFrame = 1;
		draw(canvas);
		x = xtemp;
		y = ytemp;
	}

	private void draw(Canvas canvas) {
		int srcX = (int) (spriteBmp.currentFrame * width);
		int srcY = (int) (spriteBmp.currentRow * height);
		Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
		Rect dst = new Rect((int) (x), (int) (y), (int) (x + width), (int) (y + height));
		Paint p = new Paint();
		spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.getBitmap(), src.left, src.top, (int) width, (int) height);

		if (isVisible()) {
			Matrix m = new Matrix();
			Vec2 translate = getBitmapDrawingXY();
			m.postTranslate(translate.x, translate.y);
			canvas.drawBitmap(spriteBmp.bmpFrame, m, null);
		}
	}

}
