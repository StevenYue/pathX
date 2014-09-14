package pathX.data;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import static pathX.PathXConstants.*;
import pathX.PathX.PathXPropertyType;
import pathX.ui.PathXMiniGame;
import pathX.ui.PathXTileState;
import properties_manager.PropertiesManager;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.Sprite;
import mini_game.SpriteType;

public class PathXDataModel extends MiniGameDataModel {
	private PathXMiniGame game;
	private ArrayList<Level> allLevels;
	private ArrayList<String> levelFileNames;
	private Level currentLevel;
	public int curLevelNum;
	private StringBuffer dialogText;
	private StringBuffer dialogWinText;
	private ArrayList<Road> currentRoads;
	private ArrayList<Intersect> currentIntersects;
	private Image targetImage;
	private Image startingImage;
	private ArrayList<Xiluo> Xiluos;
	private ArrayList<Cop> Cops;
	private ArrayList<Bandit> Bandits;
	private Car Car;
	private DragAction dragAction;
	private float gameSpeed;
	private boolean caughtByCop;
	private int curLevelMoney;
	private int totalBalance;
	
	
	
	//ALl the getter && setter methods
	public Level getCurrentLevel() {return currentLevel;}
	public void setCurrentLevel(Level currentLevel) {this.currentLevel = currentLevel;}
	public ArrayList<Level> getAllLevels() {return allLevels;}
	public void setAllLevels(ArrayList<Level> allLevels) {this.allLevels = allLevels;}
	public StringBuffer getDialogText() {return dialogText;}
	public void setDialogText(StringBuffer dialogText) {this.dialogText = dialogText;}
	public StringBuffer getDialogWinText() {return dialogWinText;}
	public void setDialogWinText(StringBuffer dialogWinText) {this.dialogWinText = dialogWinText;}
	public ArrayList<Road> getCurrentRoads() {return currentRoads;}
	public void setCurrentRoads(ArrayList<Road> currentRoads) {this.currentRoads = currentRoads;}
	public ArrayList<Intersect> getCurrentIntersects() {return currentIntersects;}
	public void setCurrentIntersects(ArrayList<Intersect> currentIntersects) {this.currentIntersects = currentIntersects;}
	public Image getTargetImage() {return targetImage;}
	public Image getStringImage() {return startingImage;}
	public ArrayList<Xiluo> getXiluos(){return Xiluos;}
	public ArrayList<Cop> getCops(){return Cops;}
	public ArrayList<Bandit> getBandits(){return Bandits;}
	public Car gerCar(){return Car;}
	public DragAction getDragAction(){ return dragAction;}
	public int gerCurLevelMoney(){return curLevelMoney;}
	public int getTotalBalance(){return totalBalance;}
	public float getGameSpeed() {return gameSpeed;}
	public void setGameSpeed(float gameSpeed) {this.gameSpeed = gameSpeed;}
	
	
	
	
	public void updataDialogText(){
		String s = "Awesome!!!"+"\n"+"\n"+currentLevel.getLevelName()+" FC"+"\n"
				+"has been robbed by you,"+"\n"+"make your getaway and fill your"+"\n"
				+"pocket with $"+currentLevel.getPayRoll()+".";
		dialogText = new StringBuffer(s);
	}
	public void updateDialogWinText(){
		String s = "Congratulations!!!"+"\n"+"\n"+"A very successful getaway from"+"\n"+
				currentLevel.getLevelName()+" FC"+"\n"+"with $"+curLevelMoney+
				" as your plunder.";
		dialogWinText = new StringBuffer(s);
	}
	
	public PathXDataModel(PathXMiniGame initGame){
		game = initGame;
		allLevels = new ArrayList<Level>();
		levelFileNames = new ArrayList<String>();
		dragAction = new DragAction();
		gameSpeed = 2;
		caughtByCop = false;
		totalBalance = 0;
		
	}
	
	public void loadAllLevels(){
		   PropertiesManager props = PropertiesManager.getPropertiesManager();
//	       String levelImgPath = props.getProperty(PathXPropertyType.PATH_LEVEL_IMG);
	       String levelFilePath = props.getProperty(PathXPropertyType.PATH_LEVEL_FILE);
	       File levelSchema = new File(PATH_DATA + LEVEL_SCHEMA_FILE_NAME); 
	       levelFileNames = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS_FILE);
	       for(int i=0;i<levelFileNames.size();i++){
	    	   File levelFile = new File(levelFilePath + levelFileNames.get(i));
		       allLevels.add(game.getFileManager().loadLevel(levelFile,levelSchema));
	       }
	}

	/**
	 * this method will initial the current level, for all game related issues
	 */
	public void initCurrentLevel(){
		currentRoads = new ArrayList<Road>();
		currentIntersects = new ArrayList<Intersect>();
		Xiluos = new ArrayList<Xiluo>();
		Cops = new ArrayList<Cop>();
		Bandits = new ArrayList<Bandit>();
		curLevelMoney = currentLevel.getPayRoll();
		unLocktheSpecials();
		BufferedImage img;
        SpriteType sT;
        Sprite s;
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG); 
        //First, Let's load the GameMap, right now I am just loading from image file
        //cause right now, all the background is the same, Kaifeng, if there are different 
        //backgrounds in the future, we need first to load those images through filemanager
        //and then get the image through here.
		sT = new SpriteType(GAME_MAP_TYPE);
        img = game.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_GAME_MAP));
        sT.addState(GAME_MAP_STATE, img);
        s = new Sprite(sT,GAME_MAP_ORIGIN_BASE_X,GAME_MAP_ORIGIN_BASE_Y,0,0,GAME_MAP_STATE);
        game.getGUIDecor().put(GAME_MAP_TYPE,s);
        
        //Now lets load all the intersects and roads of the current level 
        currentRoads = currentLevel.allRoads;
        currentIntersects = currentLevel.allIntersects;
        //now load the Target image, the GREAT Italia
        img = game.loadImageWithColorKey(imgPath+props.getProperty(PathXPropertyType.IMAGE_DESTINATION), COLOR_KEY);
        targetImage = img;
        //load the starting image here
        String levelImgPath = props.getProperty(PathXPropertyType.PATH_LEVEL_IMG);
        img = game.loadImageWithColorKey(levelImgPath+currentLevel.getLevelImageName(), COLOR_KEY);
        startingImage = img;
        
        //Now load Xiluo, Cop, Bandit, and your car
        img = game.loadImageWithColorKey(imgPath+props.getProperty(PathXPropertyType.IMAGE_ZOMBIE), COLOR_KEY);
        sT = new SpriteType(XILUO_TYPE);
        sT.addState(XILUO_STATE, img);
        for(int i=0;i<currentLevel.getNumOfC();i++){
        	float x = currentLevel.getEndLocation().getX()-XILUO_RADIUS;//18 is half length of Xiluo Image
        	float y = currentLevel.getEndLocation().getY()-XILUO_RADIUS;
        	Xiluos.add(new Xiluo(sT,x,y,(float)0.75*gameSpeed,(float)0.75*gameSpeed,XILUO_STATE,currentLevel));
        }
        img = game.loadImageWithColorKey(imgPath+props.getProperty(PathXPropertyType.IMAGE_COP), COLOR_KEY);
        sT = new SpriteType(COP_TYPE);
        sT.addState(COP_STATE, img);
        for(int i=0;i<currentLevel.getNumOfP();i++){
        	int copS = currentLevel.copStartLocations.get(i);
        	float x = currentLevel.allIntersects.get(copS).getX()-CB_RADIUS;//18 is half length of Xiluo Image
        	float y = currentLevel.allIntersects.get(copS).getY()-CB_RADIUS;
        	Cops.add(new Cop(sT,x,y,(float)0.5*gameSpeed,(float)0.5*gameSpeed,COP_STATE,currentLevel,COP_LOOP[curLevelNum],copS));
        }
        img = game.loadImageWithColorKey(imgPath+props.getProperty(PathXPropertyType.IMAGE_BANDIT), COLOR_KEY);
        sT = new SpriteType(BANDIT_TYPE);
        sT.addState(BANDIT_STATE, img);
        for(int i=0;i<currentLevel.getNumOfB();i++){
        	float x = currentLevel.getEndLocation().getX()-CB_RADIUS;//18 is half length of Xiluo Image
        	float y = currentLevel.getEndLocation().getY()-CB_RADIUS;
        	Bandits.add(new Bandit(sT,x,y,(float)0.5*gameSpeed,(float)0.5*gameSpeed,BANDIT_STATE,currentLevel));
        }
        
        //Now load of main actor, CAR!!!!
        img = game.loadImageWithColorKey(imgPath+props.getProperty(PathXPropertyType.IMAGE_CAR), COLOR_KEY);
        sT = new SpriteType(CAR_TYPE);
        sT.addState(CAR_STATE, img);
        float x = currentLevel.getStartLocation().getX()-CB_RADIUS;//18 is half length of Xiluo Image
        float y = currentLevel.getStartLocation().getY()-CB_RADIUS;
        Car = new Car(sT,x,y,gameSpeed,gameSpeed,CAR_STATE,currentLevel);
	}
	
	
	//These four methods are for the game screen scrolling
	//methods and variables for scrolling
	private float Map_Current_Origin_X = GAME_MAP_ORIGIN_BASE_X;
	private float Map_Current_Origin_Y = GAME_MAP_ORIGIN_BASE_Y;
	//for these two coordinates we aslo need getter method
	//for the Node and road rendering
	public float getMap_Current_Origin_X() {return Map_Current_Origin_X;}
	public float getMap_Current_Origin_Y() {return Map_Current_Origin_Y;}
	public void scrollRight(){
		if(Map_Current_Origin_X> GAME_MAP_END_BASE_X){
			Map_Current_Origin_X -= 5;
			game.getGUIDecor().get(GAME_MAP_TYPE).setX(Map_Current_Origin_X);
		}
		else {
			game.getGUIDecor().get(GAME_MAP_TYPE).setX(Map_Current_Origin_X);
		}
	}
	public void scrollLeft(){
		if(Map_Current_Origin_X< GAME_MAP_ORIGIN_BASE_X){
			Map_Current_Origin_X += 5;
			game.getGUIDecor().get(GAME_MAP_TYPE).setX(Map_Current_Origin_X);
		}
		else {
			game.getGUIDecor().get(GAME_MAP_TYPE).setX(Map_Current_Origin_X);
		}

	}
	public void scrollUp(){
		if(Map_Current_Origin_Y < GAME_MAP_END_BASE_Y){
			Map_Current_Origin_Y += 5;
			game.getGUIDecor().get(GAME_MAP_TYPE).setY(Map_Current_Origin_Y);
		}
		else {
			game.getGUIDecor().get(GAME_MAP_TYPE).setY(Map_Current_Origin_Y);
		}
	} 
	public void scrollDown(){
		if(Map_Current_Origin_Y > GAME_MAP_ORIGIN_BASE_Y){
			Map_Current_Origin_Y -= 5;
			game.getGUIDecor().get(GAME_MAP_TYPE).setY(Map_Current_Origin_Y);
		}
		else {
			game.getGUIDecor().get(GAME_MAP_TYPE).setY(Map_Current_Origin_Y);
		}
	}
	
	
	
	
	private int clickOnNode(int x,int y){
		for(Intersect n:currentIntersects)
			if(n.getX()+NODE_RADIUS>x&&x>n.getX()-NODE_RADIUS&&
					n.getY()+NODE_RADIUS>y&&y>n.getY()-NODE_RADIUS)
				return n.getId();
		return -1;
	}
	
	
	@Override
	public void checkMousePressOnSprites(MiniGame game, int x, int y) {
		// TODO Auto-generated method stub
		if(inProgress()){
			if(x>138&&x<630&&y>9&&y<468){ // constrain the click on certain area
				int mapX = (int) (x-Map_Current_Origin_X);
				int mapY = (int) (y-Map_Current_Origin_Y);
//				System.out.println(clickOnNode(mapX, mapY));
				Car.choseDes(clickOnNode(mapX, mapY));
			}
		}
	}

	private void resetXCB(){
		float x = currentLevel.getEndLocation().getX()-XILUO_RADIUS;//17.5 is half length of Xiluo Image
    	float y = currentLevel.getEndLocation().getY()-XILUO_RADIUS;
		for(Xiluo xi:Xiluos){
			xi.setX(x);
			xi.setY(y);
		}
		for(Bandit b:Bandits){
			x = currentLevel.getEndLocation().getX()-CB_RADIUS;
	    	y = currentLevel.getEndLocation().getY()-CB_RADIUS;
			b.setX(x);
			b.setY(y);
		}
		for(int i=0;i<Cops.size();i++){
			int copS = currentLevel.copStartLocations.get(i);
			x = currentLevel.allIntersects.get(copS).getX()-CB_RADIUS;
			y = currentLevel.allIntersects.get(copS).getY()-CB_RADIUS;
			Cops.get(i).setX(x);
			Cops.get(i).setY(y);
		}
		Car.setX(currentLevel.getStartLocation().getX()-CB_RADIUS);
		Car.setY(currentLevel.getStartLocation().getY()-CB_RADIUS);

	}
	private void resetGameMap(){
		Map_Current_Origin_X = GAME_MAP_ORIGIN_BASE_X;
		Map_Current_Origin_Y = GAME_MAP_ORIGIN_BASE_Y;
		game.getGUIDecor().get(GAME_MAP_TYPE).setX(GAME_MAP_ORIGIN_BASE_X);
		game.getGUIDecor().get(GAME_MAP_TYPE).setY(GAME_MAP_ORIGIN_BASE_Y);
	}
	@Override
	public void reset(MiniGame game) {
		// TODO Auto-generated method stub
		updataDialogText();
		initCurrentLevel();
		resetXCB();
		resetGameMap();
		caughtByCop = false;
//		beginGame();
	}

	private void collisionDetection(){
		for(Cop c:Cops)
			if(Car.calculateDistanceToSprite(c)<1){ // easiest one, just terminate everything
				System.out.println("Game lose!!");
				caughtByCop = true;
//				endGameAsLoss();
			}
		for(Xiluo x:Xiluos){
			if(Car.calculateDistanceToSprite(x)>10) x.setCollidable(true);
			if(Car.calculateDistanceToSprite(x)<(2*gameSpeed)&&x.isCollidable()){  // this tolerance should 
				x.setCollidable(false);	 							// be scaled according to		
				System.out.println("Biten by Xiluo!!");				// to the game speed
				System.out.println(Car.getInitSpeed());
				Car.increaseNumOfBites();
				Car.setInitSpeed(Car.vvv*(1-Car.getNumOfBites()*(float)0.2));
				Car.setVx(Car.getVx()*(1-Car.getNumOfBites()*(float)0.2));
				Car.setVy(Car.getVy()*(1-Car.getNumOfBites()*(float)0.2));
			}
			
		}
			
		//tolerance of this should be scaled properly according to current speed	
		for(Bandit b:Bandits){
			if(Car.calculateDistanceToSprite(b)>10) b.setCollidable(true);
			if(Car.calculateDistanceToSprite(b)<(2*gameSpeed)&&b.isCollidable()){
				b.setCollidable(false);
				Car.increaseNumOfRobbed();
				curLevelMoney = (int) (currentLevel.getPayRoll()*(1-Car.getNumOfRobbed()*0.1));
				System.out.println("Robbed by Bandit!!");
			}
		}
			
	}
	private void updataXCB(MiniGame game){
		for(Xiluo x:Xiluos)
			x.update(game);
		for(Cop c:Cops)
			c.update(game);
		for(Bandit b:Bandits)
			b.update(game);
		Car.update(game);
	}
	@Override
	public void updateAll(MiniGame game) {
		// TODO Auto-generated method stub
		try{
			game.beginUsingData();
			updataXCB(game);
			collisionDetection();
			if(caughtByCop) this.endGameAsLoss();
			if(Car.targetReached(currentLevel.getEndLocation())) this.endGameAsWin();
			
		}finally{
			game.endUsingData();
		}
	}

	public void quitGameAsloss(){
		super.endGameAsLoss();
	}
	public void endGameAsLoss(){
		super.endGameAsLoss();
		game.caughtByCopGUIupdate();
		totalBalance *= 0.9;
		pause();
	}
	public void endGameAsWin(){
		super.endGameAsWin();
		updateDialogWinText();
		game.winGUIupdate();
		totalBalance += curLevelMoney;
		pause();
	}
	
	
	@Override
	public void updateDebugText(MiniGame game) {
		// TODO Auto-generated method stub
		
	}
	
	//For Drag Action, a private class
	private class DragAction extends MouseAdapter{
		private boolean pressed = false;
		 public void mousePressed(MouseEvent e) {
			 if(e.getX()>138&&e.getX()<630&&
					 e.getY() > 9&& e.getY() < 468) { // constrain the click on certain area
				int mapX = (int) (e.getX() - Map_Current_Origin_X);
				int mapY = (int) (e.getY() - Map_Current_Origin_Y);
				// System.out.println(clickOnNode(mapX, mapY));
				if(Car.containsPoint(mapX, mapY)){
					pressed = true;
//					System.out.println("Pressed on Car");
				}
			}
		 }
		 public void mouseReleased(MouseEvent e) {
			 if(e.getX()>138&&e.getX()<630&&
					 e.getY() > 9&& e.getY() < 468){
				 int mapX = (int) (e.getX() - Map_Current_Origin_X);
				 int mapY = (int) (e.getY() - Map_Current_Origin_Y);
				 if(pressed)
					 Car.choseDes(clickOnNode(mapX, mapY));
				 pressed = false; 
			 }	 
		 }
		 public void mouseDragged(MouseEvent e){
//			 System.out.println("Dragged");
		 }
	}

	/**
	 * following methods are all for cheating
	 */
	public void increaseTotalBalance() {
		if(totalBalance<=9900)
			totalBalance += 100;
	}
	
	/**
	 * this method is to unlock the corresponding specials
	 */
	private void unLocktheSpecials(){
		if(curLevelNum<16)
		for(int i=0;i<curLevelNum+1;i++){
			game.getGUIButtons().get(SPECIAL_TYPE+i).setState(PathXTileState.VISIBLE_STATE.toString());
			game.getGUIButtons().get(SPECIAL_TYPE+i).setEnabled(true);
		}
	}
	
}
