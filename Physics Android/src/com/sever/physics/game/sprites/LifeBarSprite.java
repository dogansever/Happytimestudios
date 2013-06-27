package com.sever.physics.game.sprites;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.sever.physics.game.GameViewImp;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;

public class LifeBarSprite extends FreeSprite {

	private ActiveSprite activeSprite;

	public LifeBarSprite(ActiveSprite activeSprite, GameViewImp gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
		this.manualFrameSet = true;
		this.activeSprite = activeSprite;
	}

	public void onDraw(Canvas canvas) {
		// spriteBmp.currentFrame = 0;
		// spriteBmp.currentRow = spriteBmp.BMP_ROWS * activeSprite.life /
		// activeSprite.life_MAX;
		// spriteBmp.currentRow = spriteBmp.currentRow == spriteBmp.BMP_ROWS ?
		// spriteBmp.currentRow - 1 : spriteBmp.currentRow;
		x = activeSprite.x - activeSprite.width * 0.0f;
		y = activeSprite.y + activeSprite.height * 0.75f;
		super.onDraw(canvas);

		spriteBmp.currentRow = 0;

		float percentage = ((float) activeSprite.life) / ((float) activeSprite.life_MAX);
		// percentage = 0.5f;

		spriteBmp.currentFrame = 0;
		int srcX = (int) (spriteBmp.currentFrame * width);
		int srcY = (int) (spriteBmp.currentRow * height);
		Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + height));
		spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.getBitmap(), src.left, src.top, (int) (width), (int) height);

		Matrix m = new Matrix();
		Vec2 translate = getBitmapDrawingXY();
		if (Constants.checkForQuake()) {
			m.postTranslate(translate.x - Constants.extraWidthOffset + Constants.getQuakePower(), translate.y + Constants.extraHeightOffset + Constants.getQuakePower());
		} else {
			Constants.endQuake();
			m.postTranslate(translate.x - Constants.extraWidthOffset, translate.y + Constants.extraHeightOffset);
		}
		// m.postTranslate(translate.x, translate.y);
		canvas.drawBitmap(spriteBmp.bmpFrame, m, null);

		if ((int) (percentage * width) > 0) {

			spriteBmp.currentFrame = 1;
			srcX = (int) (spriteBmp.currentFrame * width);
			srcY = (int) (spriteBmp.currentRow * height);
			src = new Rect(srcX, srcY, (int) (srcX + percentage * width), (int) (srcY + height));
			spriteBmp.bmpFrame = Bitmap.createBitmap(spriteBmp.getBitmap(), src.left, src.top, (int) (percentage * width), (int) height);

			m = new Matrix();
			translate = getBitmapDrawingXY();
			if (Constants.checkForQuake()) {
				m.postTranslate(translate.x - Constants.extraWidthOffset + Constants.getQuakePower(), translate.y + Constants.extraHeightOffset + Constants.getQuakePower());
			} else {
				Constants.endQuake();
				m.postTranslate(translate.x - Constants.extraWidthOffset, translate.y + Constants.extraHeightOffset);
			}
			// m.postTranslate(translate.x, translate.y);
			canvas.drawBitmap(spriteBmp.bmpFrame, m, null);
		}
	}

}
