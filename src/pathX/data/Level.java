package pathX.data;

import java.util.ArrayList;

public class Level {
	private String levelName;
	private int payRoll;
	private int numOfP;
	private int numOfB;
	private int numOfC;
	public String levelImageName;
	public String levelBGImageName;
	public ArrayList<Road> allRoads;
	public ArrayList<Intersect> allIntersects;
	private Intersect startLocation;
	private Intersect endLocation;
	public ArrayList<Integer> copStartLocations;

	public Level(){}
	public Level(String name, int money, int numofP, int numofB,int numofC){
		this.levelName = name;
		this.payRoll = money;
		this.numOfB = numofB;
		this.numOfP = numofP;
		this.numOfC = numofC;
		allIntersects = new ArrayList<Intersect>();
		allRoads = new ArrayList<Road>();
		copStartLocations = new ArrayList<Integer>();
	}

	
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public int getPayRoll() {
		return payRoll;
	}

	public void setPayRoll(int payRoll) {
		this.payRoll = payRoll;
	}

	public int getNumOfP() {
		return numOfP;
	}

	public void setNumOfP(int numOfP) {
		this.numOfP = numOfP;
	}

	public int getNumOfB() {
		return numOfB;
	}

	public void setNumOfB(int numOfB) {
		this.numOfB = numOfB;
	}

	public int getNumOfC() {
		return numOfC;
	}

	public void setNumOfC(int numOfC) {
		this.numOfC = numOfC;
	}

	public String getLevelImageName() {
		return levelImageName;
	}

	public void setLevelImageName(String levelImageName) {
		this.levelImageName = levelImageName;
	}

	public ArrayList<Road> getAllRoads() {
		return allRoads;
	}

	public void setAllRoads(ArrayList<Road> allRoads) {
		this.allRoads = allRoads;
	}

	public ArrayList<Intersect> getAllIntersects() {
		return allIntersects;
	}

	public void setAllIntersects(ArrayList<Intersect> allIntersects) {
		this.allIntersects = allIntersects;
	}

	public Intersect getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Intersect startLocation) {
		this.startLocation = startLocation;
	}

	public Intersect getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(Intersect endLocation) {
		this.endLocation = endLocation;
	}

	public String getLevelBGImageName() {
		return levelBGImageName;
	}

	public void setLevelBGImageName(String levelBGImageName) {
		this.levelBGImageName = levelBGImageName;
	}

	
}
