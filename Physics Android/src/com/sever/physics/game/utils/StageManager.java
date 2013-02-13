package com.sever.physics.game.utils;

import java.util.ArrayList;

public class StageManager {

	static StageManager self = null;
	private static ArrayList<ArrayList<Integer>> stageList;

	static {
		stageList = new ArrayList<ArrayList<Integer>>();
		// grML,gtM,grMLo,flyB,flyBBig,flyML,flyM,flyMLo
		stageList.add(prepareStage(1, 0, 0, 0, 0, 0, 0, 0, 30));
		stageList.add(prepareStage(1, 1, 0, 0, 0, 0, 0, 0, 80));
		stageList.add(prepareStage(1, 0, 0, 1, 0, 0, 0, 0, 100));
		stageList.add(prepareStage(1, 1, 0, 1, 0, 0, 0, 0, 200));
		stageList.add(prepareStage(1, 0, 0, 1, 1, 0, 0, 0, 250));
		stageList.add(prepareStage(1, 0, 0, 2, 0, 1, 0, 0, 250));
		stageList.add(prepareStage(2, 0, 0, 1, 1, 1, 0, 0, 300));
		stageList.add(prepareStage(2, 0, 0, 0, 1, 2, 0, 0, 300));
		stageList.add(prepareStage(0, 0, 0, 0, 1, 1, 1, 0, 350));
		stageList.add(prepareStage(0, 0, 0, 0, 1, 2, 1, 0, 1350));
		stageList.add(prepareStage(0, 0, 0, 2, 0, 0, 0, 1, 1400));
		stageList.add(prepareStage(0, 0, 0, 0, 0, 1, 2, 0, 1400));
		stageList.add(prepareStage(0, 0, 0, 0, 0, 0, 1, 2, 1450));
		stageList.add(prepareStage(0, 0, 0, 0, 0, 1, 0, 3, 1500));
		stageList.add(prepareStage(0, 0, 0, 0, 0, 0, 1, 4, 5000));
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

}
