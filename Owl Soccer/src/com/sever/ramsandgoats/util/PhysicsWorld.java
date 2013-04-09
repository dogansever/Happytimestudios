package com.sever.ramsandgoats.util;

import java.util.ArrayList;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class PhysicsWorld {
	public ArrayList<Body> bodies = new ArrayList<Body>();
	public AABB worldAABB;
	public World world;

	public void create() {
		// Step 1: Create Physics World Boundaries
		worldAABB = new AABB();
		worldAABB.lowerBound.set(new Vec2(Constants.lowerBoundx - Constants.boundhxy, Constants.lowerBoundy - Constants.boundhxy));
		worldAABB.upperBound.set(new Vec2(Constants.upperBoundx + Constants.boundhxy, Constants.upperBoundy + Constants.boundhxy));

		// Step 2: Create Physics World with Gravity
		Vec2 gravity = new Vec2(Constants.gravityx, Constants.gravityy);
		boolean doSleep = true;
		world = new World(worldAABB, gravity, doSleep);
	}

	public void update() {
		// Update Physics World
		try {
			world.step(Constants.timeStep, Constants.iterations);
		} catch (Exception e) {
		}

	}

}
