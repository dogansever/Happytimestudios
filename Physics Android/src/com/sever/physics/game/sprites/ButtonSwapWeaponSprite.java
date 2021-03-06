package com.sever.physics.game.sprites;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.sever.physics.game.GameViewImp;
import com.sever.physics.game.utils.BitmapManager;
import com.sever.physics.game.utils.SpriteBmp;
import com.sever.physics.game.utils.WeaponsManager;

public class ButtonSwapWeaponSprite extends FreeSprite {

	public float xstick = 0;
	public float ystick = 0;
	public boolean active;

	public ButtonSwapWeaponSprite(ConcurrentLinkedQueue<FreeSprite> spriteList, GameViewImp gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
		this.manualFrameSet = true;
		this.spriteList = spriteList;
		WeaponsManager.refreshSwapWeaponButtonBitmap();
	}

	public void deactivate() {
		active = false;
	}

	public void activate() {
		active = true;
	}

	public boolean checkButtonTouch(float xn, float yn) {
		if (gameView.endOfGame) {
			return false;
		}

		if (PlayerSprite.loadingTimeInFPS > 0) {
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

		((PlayerSprite) gameView.getPlayerSprite()).weapon = WeaponsManager.getManager().nextAvailableWeapon();
		WeaponsManager.refreshSwapWeaponButtonBitmap();
	}

	public void onUp(float xn, float yn) {
		deactivate();
	}

	public void onDraw(Canvas canvas) {
		if (gameView.idle) {
			return;
		}
		if (gameView.endOfGame) {
			return;
		}

		spriteBmp.currentRow = 0;
		spriteBmp.currentFrame = 0;
		draw(canvas);
	}

	private void draw(Canvas canvas) {
		try {
			float percentage = ((PlayerSprite) gameView.getPlayerSprite()).getLoadingTimePercentage();
			int heightCalc = (int) (height * percentage);
			int srcX = (int) (spriteBmp.currentFrame * width);
			int srcY = (int) (spriteBmp.currentRow * height);
			Rect src = new Rect(srcX, srcY, (int) (srcX + width), (int) (srcY + heightCalc));
			if (heightCalc > 0) {
				spriteBmp.bmpFrame = Bitmap.createBitmap(BitmapManager.weaponSwapButton, src.left, src.top, (int) width, heightCalc);
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
