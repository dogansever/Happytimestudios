package com.sever.physics.game.utils;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import com.sever.physic.PhysicsApplication;

public class StageManager {

	static StageManager self = null;
	private static ArrayList<ArrayList<Integer>> stageList;

	static {
		stageList = new ArrayList<ArrayList<Integer>>();
		// -----------------------gML,gM,gMLo,fB,fBBig,fML,fM,fMLo
		stageList.add(prepareStage(1, 0, 0, 0, 0, 0, 0, 0, 1));
		stageList.add(prepareStage(1, 1, 0, 0, 0, 0, 0, 0, 1));
		stageList.add(prepareStage(1, 0, 0, 1, 0, 0, 0, 0, 1));
		stageList.add(prepareStage(1, 1, 0, 1, 0, 0, 0, 0, 1));
		stageList.add(prepareStage(1, 0, 0, 1, 1, 0, 0, 0, 1));
		stageList.add(prepareStage(1, 0, 0, 2, 0, 1, 0, 0, 1));
		stageList.add(prepareStage(2, 0, 0, 1, 1, 1, 0, 0, 1));
		stageList.add(prepareStage(2, 0, 0, 0, 1, 2, 0, 0, 1));
		stageList.add(prepareStage(0, 0, 0, 0, 1, 1, 1, 0, 1));
		stageList.add(prepareStage(0, 0, 0, 0, 1, 2, 1, 0, 1));
		stageList.add(prepareStage(0, 0, 0, 2, 0, 0, 0, 1, 1));
		stageList.add(prepareStage(0, 0, 0, 0, 0, 1, 2, 0, 1));
		stageList.add(prepareStage(0, 0, 0, 0, 0, 0, 1, 2, 1));
		stageList.add(prepareStage(0, 0, 0, 0, 0, 1, 0, 3, 1));
		stageList.add(prepareStage(0, 0, 0, 0, 0, 0, 1, 4, 1));
	}

	private static ArrayList<Integer> prepareStage(int i, int j, int k, int l, int m, int n, int o, int p, int q) {
		ArrayList<Integer> stage1 = new ArrayList<Integer>();
		stage1.add(i);
		stage1.add(j);
		stage1.add(k);
		stage1.add(l);
		stage1.add(m);
		stage1.add(n);
		stage1.add(o);
		stage1.add(p);
		stage1.add(q);
		return stage1;
	}

	public int currentStage;

	public ArrayList<Integer> getStage() {
		if (currentStage >= stageList.size()) {
			currentStage = 0;
		}
		return stageList.get(currentStage);
	}

	public ArrayList<Integer> getStage(int i) {
		return stageList.get(i);
	}

	public static StageManager getManager() {
		if (self == null)
			self = new StageManager();
		return self;
	}

	public boolean incrementStage() {
		currentStage++;
		return currentStage < stageList.size();
	}

	public ArrayList<Vec2> stagePlanetDesign() {
		ArrayList<Vec2> list = new ArrayList<Vec2>();
		Vec2 coords;

		coords = new Vec2(PhysicsApplication.deviceWidth * 0.5f, PhysicsApplication.deviceHeight * 0.5f);
		list.add(coords);
		coords = new Vec2(PhysicsApplication.deviceWidth + Constants.extraWidth * 0.5f, PhysicsApplication.deviceHeight * 0.5f);
		list.add(coords);
		coords = new Vec2(PhysicsApplication.deviceWidth, PhysicsApplication.deviceHeight + Constants.extraHeight * 0.5f);
		list.add(coords);

		return list;
	}

	public ArrayList<Vec2> stagePortalDesign() {
		ArrayList<Vec2> list = new ArrayList<Vec2>();
		float boxSize = 50;
		Vec2 coords;

		coords = new Vec2(boxSize * 0.5f, PhysicsApplication.deviceHeight);
		list.add(coords);
		coords = new Vec2(Constants.upperBoundxScreen - boxSize * 0.5f, PhysicsApplication.deviceHeight);
		list.add(coords);
		return list;
	}

	public ArrayList<Vec2> stageBoxDesign() {
		ArrayList<Vec2> list = new ArrayList<Vec2>();
		float boxSize = 50;
		Vec2 coords;

		coords = new Vec2(boxSize * 0.5f, PhysicsApplication.deviceHeight - boxSize);
		list.add(coords);
		coords = new Vec2(boxSize * 0.5f, PhysicsApplication.deviceHeight + boxSize);
		list.add(coords);
		coords = new Vec2(Constants.upperBoundxScreen - boxSize * 0.5f, PhysicsApplication.deviceHeight - boxSize);
		list.add(coords);
		coords = new Vec2(Constants.upperBoundxScreen - boxSize * 0.5f, PhysicsApplication.deviceHeight + boxSize);
		list.add(coords);

		coords = new Vec2(PhysicsApplication.deviceWidth * 0.5f, PhysicsApplication.deviceHeight + Constants.extraHeight * 0.5f);
		list.add(coords);
		coords = new Vec2(PhysicsApplication.deviceWidth + Constants.extraWidth * 0.5f, PhysicsApplication.deviceHeight + Constants.extraHeight * 0.5f);
		list.add(coords);

		coords = new Vec2(PhysicsApplication.deviceWidth * 0.75f, PhysicsApplication.deviceHeight * 0.75f);
		list.add(coords);
		coords = new Vec2(PhysicsApplication.deviceWidth + Constants.extraWidth * 0.25f, PhysicsApplication.deviceHeight * 0.75f);
		list.add(coords);

		coords = new Vec2(PhysicsApplication.deviceWidth, PhysicsApplication.deviceHeight);
		list.add(coords);

		return list;
	}

}
