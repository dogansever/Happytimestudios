package com.sever.ramsandgoats.sprites;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.graphics.Canvas;

import com.sever.ramsandgoats.game.GameView;
import com.sever.ramsandgoats.util.Constants;
import com.sever.ramsandgoats.util.SpriteBmp;

public class SoccerPlayerSprite extends FreeSprite {

	public int jumpCountCurrent = 0;
	public int jumpCountMAX = 2;

	public SoccerPlayerSprite(ConcurrentLinkedQueue<FreeSprite> playerSprite, GameView gameView, SpriteBmp spriteBmp, float x, float y) {
		this.spriteBmp = spriteBmp;
		this.width = spriteBmp.getWidth();
		this.height = spriteBmp.getHeight();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.noRotation = true;
		this.spriteList = spriteList;
		addSprite(x, y);
		setBmpIdle();
	}

	void addSprite(float x, float y) {
		createDynamicBody(x, y);
		createShape();
	}

	public void createShape() {
		CircleDef circle = new CircleDef();
		circle.radius = getWidthPhysical() * 0.5f;
		circle.friction = 1.0f;// zero being completely frictionless
		circle.restitution = 0.0f;// zero being not bounce at all
		circle.density = 10.0f;

		PolygonDef playerDef = new PolygonDef();
		playerDef.setAsBox(getWidthPhysical() * 0.5f, getHeightPhysical() * 0.5f);
		playerDef.friction = 1.0f;
		playerDef.restitution = 0.2f;
		playerDef.density = 10.0f;

		// Assign shape to Body
		// getBody().createShape(playerDef);

		getBody().createShape(circle);
		getBody().setMassFromShapes();
		getBody().setBullet(true);

	}

	public void doBallKick() {
		push(gameView.getBall());
		setBmpKicking();
	}

	public void doBallTrick() {
		pushUp(gameView.getBall());
		setBmpTricking();
	}

	public void doIdle() {
		setBmpIdle();
	}

	public void doJump() {
		if (++jumpCountCurrent <= jumpCountMAX) {
			setBmpJumpingUp();
			throttleUp();
		} else {
			checkJumpLandingStatus();
		}
	}

	public void checkJumpLandingStatus() {
		System.out.println("y:" + y);
		System.out.println("height:" + height);
		System.out.println("Constants.lowerBoundyScreen:" + Constants.lowerBoundyScreen);
		if (y - height <= Constants.lowerBoundyScreen) {
			jumpCountCurrent = 0;
		}
	}

	public void checkJumpingStatus() {
		if (spriteBmp.bmpIndex == 4 || spriteBmp.bmpIndex == 5 || Math.abs(this.y) > 150) {
			if (Math.abs(this.y) < 150) {
				doIdle();
			} else if (getBody().m_linearVelocity.y <= 0) {
				setBmpJumpingDown();
			} else {
				setBmpJumpingUp();
			}
		}
	}

	public void moveForward() {
		setBmpRunning();
		if (facingRigth) {
			if (this.x < Constants.upperBoundxScreen - Constants.penaltyAreaWidth) {
				throttleRight();
			}
		} else {
			if (this.x > Constants.penaltyAreaWidth) {
				throttleLeftCPU();
			}
		}
	}

	public void moveBackward() {
		setBmpRunning();
		if (facingRigth) {
			throttleLeft();
		} else {
			throttleRightCPU();
		}

	}

	public void setBmpJumpingUp() {
		if (spriteBmp.bmpIndex != 4) {
			spriteBmp.setBmpIndex(4);
			this.width = spriteBmp.getWidth();
			this.height = spriteBmp.getHeight();
			spriteBmp.currentRow = 0;
			spriteBmp.currentFrame = 0;
		}
	}

	public void setBmpJumpingDown() {
		if (spriteBmp.bmpIndex != 5) {
			spriteBmp.setBmpIndex(5);
			this.width = spriteBmp.getWidth();
			this.height = spriteBmp.getHeight();
			spriteBmp.currentRow = 0;
			spriteBmp.currentFrame = 0;
		}
	}

	public void setBmpIdle() {
		if (spriteBmp.bmpIndex != 0) {
			spriteBmp.setBmpIndex(0);
			this.width = spriteBmp.getWidth();
			this.height = spriteBmp.getHeight();
			spriteBmp.currentRow = 0;
			spriteBmp.currentFrame = 0;
		}
	}

	public void setBmpRunning() {
		if (spriteBmp.bmpIndex != 1) {
			spriteBmp.setBmpIndex(1);
			this.width = spriteBmp.getWidth();
			this.height = spriteBmp.getHeight();
			spriteBmp.currentRow = 0;
			spriteBmp.currentFrame = 0;
		}
	}

	public void setBmpKicking() {
		if (spriteBmp.bmpIndex != 2) {
			spriteBmp.setBmpIndex(2);
			this.width = spriteBmp.getWidth();
			this.height = spriteBmp.getHeight();
			spriteBmp.currentRow = 0;
			spriteBmp.currentFrame = 0;
			spriteBmp.repeat = false;
		}
	}

	public void setBmpTricking() {
		if (spriteBmp.bmpIndex != 3) {
			spriteBmp.setBmpIndex(3);
			this.width = spriteBmp.getWidth();
			this.height = spriteBmp.getHeight();
			spriteBmp.currentRow = 0;
			spriteBmp.currentFrame = 0;
			spriteBmp.repeat = false;
		}
	}

	public void throttlexBmp() {
		// throttleBmp();
		// spriteBmp.currentRow = 0;
	}

	public void throttleyBmp() {
		// throttleBmp();
		// spriteBmp.currentRow = 1;
	}

	public void throttleBmp() {
		// spriteBmp.setBmpIndex(1);
		// this.width = spriteBmp.getWidth();
		// this.height = spriteBmp.getHeight();
		// spriteBmp.BMP_FPS = 3;
	}

	public void pushUp(FreeSprite sprite) {
		sprite.makeVisible();
		float FIELD_RADIUS = getWidthPhysical() * 1.0f;
		Vec2 positionTarget = sprite.getBody().getPosition();
		Vec2 positionSrc = this.getBody().getPosition();
		float range = spacing(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
		if (range <= FIELD_RADIUS) {
			Body body = sprite.getBody();
			body.setAngularVelocity((float) (Math.random() * 45));
			// body.setLinearVelocity(new Vec2(0, 0));
			Vec2 force = new Vec2(0.0f, 1.0f);
			// Vec2 force = new Vec2(positionTarget.x - positionSrc.x,
			// positionTarget.y - positionSrc.y);
			force.normalize(); // force direction always point to source
			force.set(force.mul((float) (body.getMass() * Constants.gravityPushPlayer * 0.1f)));
			body.applyImpulse(force, body.getWorldCenter());
		}

		// applyForce(sprite.getBody(), positionSrc, FIELD_RADIUS,
		// Constants.gravityPushPlayer, force);
	}

	public void push(FreeSprite sprite) {
		sprite.makeVisible();
		float FIELD_RADIUS = getWidthPhysical() * 1.0f;
		Vec2 positionTarget = sprite.getBody().getPosition();
		Vec2 positionSrc = this.getBody().getPosition();
		float range = spacing(positionSrc.x - positionTarget.x, positionSrc.y - positionTarget.y);
		if (range <= FIELD_RADIUS) {
			Body body = sprite.getBody();
			// body.setAngularVelocity((float) (Math.random() * 90));
			// body.setLinearVelocity(new Vec2(0, 0));
			// Vec2 force = new Vec2((facingRigth ? 1 : -1) * 1.0f, 0.0f);
			Vec2 force = new Vec2(positionTarget.x - positionSrc.x, positionTarget.y - positionSrc.y);
			force.normalize(); // force direction always point to source
			force.set(force.mul((float) (body.getMass() * Constants.gravityPushPlayer)));
			Vec2 v = body.getWorldCenter();
			v.set(v.x, v.y - new Random().nextInt((int) (sprite.getHeightPhysical() * 0.5)));
			body.applyImpulse(force, v);
		}

		// applyForce(sprite.getBody(), positionSrc, FIELD_RADIUS,
		// Constants.gravityPushPlayer, force);
	}

	public float frictionConstantx = 0.5f;// 1.0f no friction
	public float frictionConstanty = 1.00f;// 1.0f no friction

	public void stabilizeVelocity() {
		getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x * frictionConstantx, getBody().getLinearVelocity().y * frictionConstanty));
	}

	public void throttle(int direction, float... f) {
		float jp = 1;
		if (f.length == 1) {
			jp = f[0];
		}

		Vec2 force = null;
		switch (direction) {
		case 0:
			// up
			force = new Vec2(0.0f, 1.0f);
			throttleyBmp();
			break;
		case 1:
			// down
			force = new Vec2(0.0f, -1.0f);
			// throttleyBmp();
			break;
		case 2:
			// left
			force = new Vec2(-1.0f, 0.0f);
			throttlexBmp();
			break;
		case 3:
			// right
			force = new Vec2(1.0f, 0.0f);
			throttlexBmp();
			break;

		default:
			break;
		}
		Body body = getBody();
		force.normalize(); // force direction always point to source
		force.set(force.mul((float) (body.getMass() * Constants.gravityThrottle * jp)));
		body.applyImpulse(force, body.getWorldCenter());

		checkVelocity();
		// System.out.println("!!!Kicked it!!!:" + index + ", force:x:" +
		// force.x + ", y:" + force.y);
	}

	public void throttleUp() {
		throttle(0, 14);
	}

	public void throttleDown() {
		throttle(1);
	}

	public void throttleLeft() {
		throttle(2);
	}

	public void throttleLeftCPU() {
		throttle(2, 7);
	}

	public void throttleRight() {
		throttle(3);
	}

	public void throttleRightCPU() {
		throttle(3, 7);
	}

	public int velocity_MAX = 50;

	public void checkVelocity() {
		float max = velocity_MAX;
		// System.out.println("getBody().getLinearVelocity():(x,y) (" +
		// getBody().getLinearVelocity().x + "," +
		// getBody().getLinearVelocity().y + ")");
		if (getBody().getLinearVelocity().x < -max) {
			getBody().setLinearVelocity(new Vec2(-max, getBody().getLinearVelocity().y));
		} else if (getBody().getLinearVelocity().x > max) {
			getBody().setLinearVelocity(new Vec2(max, getBody().getLinearVelocity().y));
		}

		if (getBody().getLinearVelocity().y < -max) {
			getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x, -max));
		} else if (getBody().getLinearVelocity().y > max) {
			getBody().setLinearVelocity(new Vec2(getBody().getLinearVelocity().x, max));
		}
		;
	}

	@Override
	public void onDraw(Canvas canvas) {
		// setBmpIdle();
		checkJumpingStatus();

		// CPU Player
		if (!facingRigth) {
			checkIfBehindTheBall();
		}

		super.onDraw(canvas);
	}

	private void checkIfBehindTheBall() {
		if (gameView.getBall().x >= this.x) {
			moveBackward();
		} else if (gameView.getBall().y > this.y && gameView.getBall().getBody().getLinearVelocity().x > 5) {
			doJump();
			moveBackward();
		} else if (gameView.getBall().x > this.x && gameView.getBall().y < this.y) {
			doJump();
			moveBackward();
		} else {
			moveForward();
		}

		if (gameView.getBall().x > this.x && getDistanceFrom(gameView.getBall()) < getWidthPhysical() * 1.0f) {
			doBallTrick();
		} else if (getDistanceFrom(gameView.getBall()) < getWidthPhysical() * 1.0f && (gameView.getBall().y < this.y) && (gameView.getBall().x < this.x)) {
			doBallKick();
		}
	}
}
