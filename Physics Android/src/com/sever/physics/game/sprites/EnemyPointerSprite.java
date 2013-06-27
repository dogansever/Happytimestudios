package com.sever.physics.game.sprites;

import android.graphics.Canvas;

import com.sever.physic.PhysicsApplication;
import com.sever.physics.game.GameViewImp;
import com.sever.physics.game.utils.Constants;
import com.sever.physics.game.utils.SpriteBmp;

public class EnemyPointerSprite extends FreeSprite {

	private ActiveSprite enemySprite;

	public EnemyPointerSprite(ActiveSprite activeSprite, GameViewImp gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.manualFrameSet = true;
		this.enemySprite = activeSprite;
	}

	public void onDraw(Canvas canvas) {
		try {
			if (gameView.idle)
				return;
			boolean hideFlagx;
			boolean hideFlagy;

			spriteBmp.currentFrame = 0;
			spriteBmp.currentRow = 0;
			float tx = enemySprite.x - gameView.getPlayerSprite().x;
			float ty = enemySprite.y - gameView.getPlayerSprite().y;
			float td = tx / ty;

			// x = gameView.getPlayerSprite().x + tx * 0.5f - width;
			// y = gameView.getPlayerSprite().y + ty * 0.5f - height;
			if (tx > 0) {// enemy at the right
				x = PhysicsApplication.deviceWidth + Constants.extraWidthOffset - width / 2;
				hideFlagx = enemySprite.x - enemySprite.width < x;
			} else {
				x = Constants.extraWidthOffset + width / 2;
				hideFlagx = enemySprite.x + enemySprite.width > x;
			}
			float tx2 = x - gameView.getPlayerSprite().x;
			y = tx2 * ty / tx + gameView.getPlayerSprite().y;

			if (ty > 0) {
				float y1 = PhysicsApplication.deviceHeight + Constants.extraHeightOffset - height / 2;
				if (y > y1) {
					y = y1;
					float ty2 = y - gameView.getPlayerSprite().y;
					x = ty2 * tx / ty + gameView.getPlayerSprite().x;
				}

				hideFlagy = enemySprite.y - enemySprite.height < y;

			} else {
				float y1 = Constants.extraHeightOffset + height / 2;
				if (y < y1) {
					y = y1;
					float ty2 = y - gameView.getPlayerSprite().y;
					x = ty2 * tx / ty + gameView.getPlayerSprite().x;
				}

				hideFlagy = enemySprite.y + enemySprite.height > y;
			}

			angle = (float) Math.atan2(ty, tx);
			angle = (float) Math.toRadians(360 - Math.toDegrees(angle));
			// LogUtil.log("angle:" + Math.toDegrees(angle));
			if (hideFlagx && hideFlagy)
				return;

			super.onDraw(canvas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
