package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.sever.physics.game.GameView;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;

public class SmokeSprite extends FreeSprite {

	public int FADE_LIFEMax = (int) (Constants.FPS * 1.0f);

	public SmokeSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.spriteList = spriteList;
		fades = true;
		FADE_LIFE = FADE_LIFEMax;
	}

	private void updateBitmap() {
		if (!manualFrameSet && ++spriteBmp.BMP_FPS_CURRENT % spriteBmp.BMP_FPS == 0)
			spriteBmp.currentFrame = ++spriteBmp.currentFrame % spriteBmp.BMP_COLUMNS;

		int srcX = (int) (spriteBmp.currentFrame * width);
		int srcY = (int) (spriteBmp.currentRow * height);
		Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
		Rect dst = new Rect((int) (x), (int) (y), (int) (x + width), (int) (y + height));
		Paint p = new Paint();
		spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.getBitmap(), src.left, src.top, (int) width, (int) height);
		// bmpFrame = bmp;
	}

	public void onDraw(Canvas canvas) {
		updateBitmap();

		if (fades && FADE_LIFE >= 0) {
			if (FADE_LIFE-- == 0) {
				spriteList.remove(this);
				freeBitmaps();
				return;
			}
		}
		if (spriteBmp.getBitmap() != null && isVisible()) {
			Matrix m = new Matrix();
			if (!noRotation)
				m.postRotate((float) Math.toDegrees(angle), width * 0.5f, height * 0.5f);
			Vec2 translate = getBitmapDrawingXY();
			if (Constants.checkForQuake()) {
				m.postTranslate(translate.x - Constants.extraWidthOffset + Constants.getQuakePower(), translate.y + Constants.extraHeightOffset + Constants.getQuakePower());
			} else {
				Constants.endQuake();
				m.postTranslate(translate.x - Constants.extraWidthOffset, translate.y + Constants.extraHeightOffset);
			}
			if (facingRigth) {
				Matrix mirrorMatrix = new Matrix();
				mirrorMatrix.preScale(-1.0f, 1.0f);
				spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.bmpFrame, 0, 0, spriteBmp.bmpFrame.getWidth(), spriteBmp.bmpFrame.getHeight(), mirrorMatrix, false);
			}

			Paint paint = new Paint();
			paint.setStyle(Style.FILL);
			int alpha = (int) (255.0f * FADE_LIFE / FADE_LIFEMax);
			paint.setAlpha(alpha <= 0 ? 0 : alpha);
			canvas.drawBitmap(spriteBmp.bmpFrame, m, paint);
		}
	}
}
