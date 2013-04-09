package com.sever.ramsandgoats.util;

public class PhysicsWorldManager {

	public static PhysicsWorld mWorld;

	public static void createWorld() {
		mWorld = new PhysicsWorld();
		mWorld.create();
	}
}
