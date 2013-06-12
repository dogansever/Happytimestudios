package com.sever.physics.game.utils;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.PulleyJointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import com.sever.physic.PhysicsActivity;
import com.sever.physics.game.sprites.FreeSprite;

public class Joint {

	public Joint() {
	}

	public void createPulley(FreeSprite b1, FreeSprite b2, float hy1, float hy2) {
		PulleyJointDef jointDef = new PulleyJointDef();
		Vec2 anchor1 = b1.getBody().getWorldCenter();
		Vec2 anchor2 = b2.getBody().getWorldCenter();
		Vec2 ga1 = new Vec2(anchor1.x, anchor1.y - hy1 / Constants.pixelpermeter);
		Vec2 ga2 = new Vec2(anchor2.x, anchor2.y - hy2 / Constants.pixelpermeter);
		float r = 1.0f;
		jointDef.initialize(b1.getBody(), b2.getBody(), ga1, ga2, anchor1, anchor2, r);
		jointDef.maxLength1 = 2 * hy1 / Constants.pixelpermeter;
		jointDef.maxLength2 = 2 * hy2 / Constants.pixelpermeter;
		PhysicsActivity.mWorld.world.createJoint(jointDef);
	}

	public void createRevolute(FreeSprite b1, FreeSprite b2) {
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.initialize(b1.getBody(), b2.getBody(), b1.getBody().getWorldCenter());
		jointDef.maxMotorTorque = 1.0f;
		jointDef.enableMotor = true;
		PhysicsActivity.mWorld.world.createJoint(jointDef);
	}

	public void createDistanceJoint(FreeSprite b1, FreeSprite b2) {
		DistanceJointDef jointDef = new DistanceJointDef();
		Vec2 anchor1 = b1.getBody().getWorldCenter();
		Vec2 anchor2 = b2.getBody().getWorldCenter();
		jointDef.initialize(b1.getBody(), b2.getBody(), anchor1, anchor2);
		PhysicsActivity.mWorld.world.createJoint(jointDef);
	}

}
