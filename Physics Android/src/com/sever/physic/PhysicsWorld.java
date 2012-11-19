package com.sever.physic;

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
		world.step(Constants.timeStep, Constants.iterations);

		// Print info of latest body
		if (bodies.size() > 0) {
			// Vec2 position = bodies.get(bodies.size() - 1).getPosition();
			// float angle = bodies.get(bodies.size() - 1).getAngle();
			// Log.v("Physics Test", "Pos: (" + position.x + ", " + position.y +
			// "), Angle: " + angle);
			PhysicsActivity.context.updateScreen();
		}
	}

}
