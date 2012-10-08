package com.sever.physic;

import java.util.ArrayList;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

public class PhysicsWorld {

	public int targetFPS = 50;
	public float timeStep = 1.0f / (1000 / targetFPS);
	public int iterations = 5;

	public ArrayList<Body> bodies = new ArrayList<Body>();

	public AABB worldAABB;
	public World world;
	public BodyDef groundBodyDef;
	public PolygonDef groundShapeDef;

	public void create() {
		// Step 1: Create Physics World Boundaries
		worldAABB = new AABB();
		worldAABB.lowerBound.set(new Vec2((float) 0.0, (float) 0.0));
		worldAABB.upperBound.set(new Vec2((float) 48.0, (float) 64.0));

		// Step 2: Create Physics World with Gravity
		Vec2 gravity = new Vec2((float) 0.0, (float) -10.0);
		boolean doSleep = true;
		world = new World(worldAABB, gravity, doSleep);

		// Step 3: Create Ground Box
		groundBodyDef = new BodyDef();
		groundBodyDef.position.set(new Vec2((float) 24.0, (float) 2.0));
		Body groundBody = world.createStaticBody(groundBodyDef);
		groundShapeDef = new PolygonDef();
		groundShapeDef.setAsBox((float) 24.0, (float) 1.0);
		groundShapeDef.friction = 10.0f;
		groundShapeDef.restitution = 0.5f;
		groundBody.createShape(groundShapeDef);

		// Step 3: Create Ground Box
		BodyDef rightWall = new BodyDef();
		rightWall.position.set(new Vec2((float) 48.0, (float) 32.0));
		Body rightWallBody = world.createStaticBody(rightWall);
		PolygonDef rightWallDef = new PolygonDef();
		rightWallDef.setAsBox((float) 1.0, (float) 32.0);
		rightWallDef.friction = 1.0f;
		rightWallDef.restitution = 0.5f;
		rightWallBody.createShape(rightWallDef);
		// Step 3: Create Ground Box
		BodyDef leftWall = new BodyDef();
		leftWall.position.set(new Vec2((float) 0.0, (float) 32.0));
		Body leftWallBody = world.createStaticBody(leftWall);
		PolygonDef leftWallDef = new PolygonDef();
		leftWallDef.setAsBox((float) 1.0, (float) 32.0);
		leftWallDef.friction = 1.0f;
		leftWallDef.restitution = 0.5f;
		leftWallBody.createShape(leftWallDef);
	}

	public void addBall() {
		addBall((float) 6.0 + bodies.size(), (float) 24.0);
	}

	public void addBall(float x, float y) {
		// Create Dynamic Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodies.add(world.createDynamicBody(bodyDef));

		// Create Shape with Properties
		CircleDef circle = new CircleDef();
		circle.radius = (float) 1.0;
		circle.density = (float) 10.0;
		circle.restitution = 0.5f;
		circle.friction = 5.0f;

		// Assign shape to Body
		bodies.get(bodies.size() - 1).createShape(circle);
		bodies.get(bodies.size() - 1).setMassFromShapes();

	}

	public void update() {
		// Update Physics World
		world.step(timeStep, iterations);

		// Print info of latest body
		if (bodies.size() > 0) {
			// Vec2 position = bodies.get(bodies.size() - 1).getPosition();
			// float angle = bodies.get(bodies.size() - 1).getAngle();
			// Log.v("Physics Test", "Pos: (" + position.x + ", " + position.y +
			// "), Angle: " + angle);
			PhysicsActivity.context.updateScreen();
		}
	}

	public Vec2 toScreen(Vec2 pos) {
		pos.x = pos.x * 10;
		pos.y = PhysicsActivity.deviceHeight - (pos.y * 10);
		return pos;
	}

	public Vec2 fromScreen(Vec2 pos) {
		pos.x = pos.x / 10;
		pos.y = (PhysicsActivity.deviceHeight - pos.y) / 10;
		return pos;
	}
}
