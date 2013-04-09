package com.sever.ramsandgoats.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.sever.ramsandgoats.game.GameView;
import com.sever.ramsandgoats.util.BitmapManager;
import com.sever.ramsandgoats.util.SpriteBmp;

public class ButtonGoLeftSprite extends FreeSprite {

	public float xstick = 0;
	public float ystick = 0;
	public boolean active;

	public ButtonGoLeftSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
		this.manualFrameSet = true;
		this.spriteList = spriteList;
	}

	public void deactivate() {
		active = false;
	}

	public void activate() {
		active = true;
	}

	public boolean checkButtonTouch(float xn, float yn) {
		if (gameView.finishGame) {
			return false;
		}

		if (gameView.paused) {
			return false;
		}

		if (active)
			return true;

		if (xn > x - width && xn < x + width && yn > y - height && yn < y + height)
			return true;
		return false;
	}

	public void onDown(float xn, float yn) {
		if (!checkButtonTouch(xn, yn))
			return;

		activate();

	}

	public void onMove(float xn, float yn) {
		((SoccerPlayerSprite) gameView.getPlayer()).moveBackward();
	}

	public void onUp(float xn, float yn) {
		deactivate();
	}

	public void onDraw(Canvas canvas) {
		if (gameView.finishGame) {
			return;
		}
		if (gameView.paused) {
			return;
		}

		spriteBmp.currentRow = 0;
		spriteBmp.currentFrame = 0;
		draw(canvas);
	}

	private void draw(Canvas canvas) {
		try {
			int heightCalc = (int) (height);
			int srcX = (int) (spriteBmp.currentFrame * width);
			int srcY = (int) (spriteBmp.currentRow * height);
			Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + heightCalc));
			if (heightCalc > 0) {
				spriteBmp.bmpFrame = Bitmap.createBitmap(BitmapManager.bmpButtonGoLeft, src.left, src.top, (int) width, heightCalc);
				Matrix m = new Matrix();
				Vec2 translate = getBitmapDrawingXY();
				m.postTranslate(translate.x, translate.y);
				canvas.drawBitmap(spriteBmp.bmpFrame, m, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
