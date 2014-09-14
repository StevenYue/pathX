package pathX.ui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Timer;

import pathX.PathX.PathXPropertyType;
import pathX.data.PathXDataModel;
import pathX.file.PathXFileManager;
import properties_manager.PropertiesManager;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import static pathX.PathXConstants.*;

public class PathXMiniGame extends MiniGame {
	private String currentScreenState;
	private PathXEventHandler eventHandler;
	private PathXFileManager fileManager;
	//this siteName will contain all the level names, x&y coordinates with respect to 
	//the origin of the MAP, And also,each site info
	private ArrayList<String> siteNames;
	private ArrayList<String> siteX;
	private ArrayList<String> siteY;
	protected ArrayList<String> siteInfos;
	protected int rightSiteInfo = -1;
    Timer[] timer = new Timer[8];
	private ScrollMH levelScrollMH;
	private ScrollMH gameScrollMH;
	private speedControlML speedControlML;
	protected Sprite speedControlSprite;

	public PathXFileManager getFileManager() {
		return fileManager;
	}
	public void setFileManager(PathXFileManager fileManager) {
		this.fileManager = fileManager;
	}
	public boolean isCurrentScreenState(String testScreenState){
		return testScreenState.equals(currentScreenState);
	}
	@Override
	public void initAudioContent() {
		// TODO Auto-generated method stub
		/**
		 * Leave blank at this stage
		 */
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		data = new PathXDataModel(this);
		fileManager = new PathXFileManager(this);
		((PathXDataModel)data).loadAllLevels();
	}

	@Override
	public void initGUIControls() {
		// Use these to load everything
		BufferedImage img;
        SpriteType sT;
        Sprite s;
        
        
        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);        
        String windowIconFile = props.getProperty(PathXPropertyType.IMAGE_WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);
        
        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new PathXPanel(this);
        
        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = GAME_SPLASH_STATE;
        sT = new SpriteType(BACKGROUND_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_GAME_SPLASH));
        sT.addState(GAME_SPLASH_STATE, img);
        img = loadImageWithColorKey(imgPath + props.getProperty(PathXPropertyType.IMAGE_GAME_PLAY),COLOR_KEY);
        sT.addState(GAME_PLAY_STATE, img);
        img = loadImageWithColorKey(imgPath + props.getProperty(PathXPropertyType.IMAGE_GAME_LEVEL_SELECT), COLOR_KEY);
        sT.addState(GAME_LEVEL_SELECT_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_GAME_SETTING));
        sT.addState(GAME_SETTING_STATE, img);
        img = loadImageWithColorKey(imgPath + props.getProperty(PathXPropertyType.IMAGE_GAME_HELP),COLOR_KEY);
        sT.addState(GAME_HELP_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, GAME_SPLASH_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);
        
        //Load the Map, which will be the map type
        sT = new SpriteType(MAP_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_MAP));
        sT.addState(MAP_STATE, img);
        s = new Sprite(sT,MAP_ORIGIN_BASE_X,MAP_ORIGIN_BASE_Y,0,0,MAP_STATE);
        guiDecor.put(MAP_TYPE,s);

        //*****Add controls on the splash screen******
        //*****Which means these 5 buttons should be visible when created
        // first the PLAY BUTTON
       String playButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PLAY);
       sT = new SpriteType(PLAY_BUTTON_TYPE);
       img = loadImage(imgPath + playButton);
       sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
       s = new Sprite(sT, PLAY_BUTTON_X, PLAY_BUTTON_Y, 0, 0, PathXTileState.VISIBLE_STATE.toString());
       guiButtons.put(PLAY_BUTTON_TYPE, s);
       
       // second the Reset Button
       String resetButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_RESET);
       sT = new SpriteType(RESET_BUTTON_TYPE);
       img = loadImage(imgPath + resetButton);
       sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
       s = new Sprite(sT, RESET_BUTTON_X, RESET_BUTTON_Y, 0, 0, PathXTileState.VISIBLE_STATE.toString());
       guiButtons.put(RESET_BUTTON_TYPE, s);
       
       // third Setting button
       String settingButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SETTING);
       sT = new SpriteType(SETTING_BUTTON_TYPE);
       img = loadImage(imgPath + settingButton);
       sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
       s = new Sprite(sT, SETTING_BUTTON_X, SETTING_BUTTON_Y, 0, 0, PathXTileState.VISIBLE_STATE.toString());
       guiButtons.put(SETTING_BUTTON_TYPE, s);
       
       //4th Help Button
       String helpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HELPS);
       sT = new SpriteType(HELP_BUTTON_TYPE);
       img = loadImage(imgPath + helpButton);
       sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
       s = new Sprite(sT, HELP_BUTTON_X, HELP_BUTTON_Y, 0, 0, PathXTileState.VISIBLE_STATE.toString());
       guiButtons.put(HELP_BUTTON_TYPE, s);
       
       //5th quit Button
       String quitButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_QUIT);
       sT = new SpriteType(QUIT_BUTTON_TYPE);
       img = loadImageWithColorKey(imgPath + quitButton,COLOR_KEY);
       sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
       s = new Sprite(sT, QUIT_BUTTON_X, QUIT_BUTTON_Y, 0, 0, PathXTileState.VISIBLE_STATE.toString());
       guiButtons.put(QUIT_BUTTON_TYPE, s);
       
       //*****initialize other buttons, invisible on splash*****
       //*****but needed for other screens*****
       //Home button
       String homeButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HOME);
       sT = new SpriteType(HOME_BUTTON_TYPE);
       img = loadImageWithColorKey(imgPath + homeButton,COLOR_KEY);
       sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
       s = new Sprite(sT, HOME_BUTTON_X, HOME_BUTTON_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
       guiButtons.put(HOME_BUTTON_TYPE, s);
       
       //Quit1 Button
       String quit1Button = props.getProperty(PathXPropertyType.IMAGE_BUTTON_QUIT);
       sT = new SpriteType(QUIT1_BUTTON_TYPE);
       img = loadImageWithColorKey(imgPath + quit1Button,COLOR_KEY);
       sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
       s = new Sprite(sT, QUIT1_BUTTON_X, QUIT1_BUTTON_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
       guiButtons.put(QUIT1_BUTTON_TYPE, s);
       
       //Home1 Button
       String home1Button = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HOME);
       sT = new SpriteType(HOME1_BUTTON_TYPE);
       img = loadImageWithColorKey(imgPath + home1Button,COLOR_KEY);
       sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
       s = new Sprite(sT, HOME1_BUTTON_X, HOME1_BUTTON_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
       guiButtons.put(HOME1_BUTTON_TYPE, s);
       
       //now load four scroll buttons [up,down,left,right]
       String[] scrollImages ={props.getProperty(PathXPropertyType.IMAGE_SCROLL_UP),
    		   props.getProperty(PathXPropertyType.IMAGE_SCROLL_DOWN),
    		   props.getProperty(PathXPropertyType.IMAGE_SCROLL_LEFT),
    		   props.getProperty(PathXPropertyType.IMAGE_SCROLL_RIGHT)};
       for(int i=0;i<4;i++){
           sT = new SpriteType(SCROLL_BUTTON_TYPE[i]);
           img = loadImageWithColorKey(imgPath + scrollImages[i],COLOR_KEY);
           sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
           s = new Sprite(sT, SCROLL_BUTTON_X[i], SCROLL_BUTTON_Y[i], 0, 0, PathXTileState.INVISIBLE_STATE.toString());
           guiButtons.put(SCROLL_BUTTON_TYPE[i], s);
           sT = new SpriteType(SCROLL1_BUTTON_TYPE[i]);
           img = loadImageWithColorKey(imgPath + scrollImages[i],COLOR_KEY);
           sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
           s = new Sprite(sT, SCROLL_BUTTON_X[i], SCROLL_BUTTON_Y[i], 0, 0, PathXTileState.INVISIBLE_STATE.toString());
           guiButtons.put(SCROLL1_BUTTON_TYPE[i], s);
       }  

       //In the future this part will be used to load all the sites on the map
       BufferedImage imgLockedSite,imgFailedSite,imgSuccessSite;
       String lockedSite = props.getProperty(PathXPropertyType.IMAGE_LOCKED_SITE);
       String failedSite = props.getProperty(PathXPropertyType.IMAGE_FAILED_SITE);
       String successSite = props.getProperty(PathXPropertyType.IMAGE_SUCCESS_SITE);
       imgLockedSite = loadImageWithColorKey(imgPath + lockedSite, COLOR_KEY);
       imgFailedSite = loadImageWithColorKey(imgPath + failedSite, COLOR_KEY);
       imgSuccessSite = loadImageWithColorKey(imgPath + successSite, COLOR_KEY);

       //Later on, once I have all the levels, I will use loop to load all the sites
       siteNames = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS_NAME);
       siteX = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS_X);
       siteY = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS_Y);
       siteInfos = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS_SITE_INFO);
       for(int i=0;i<siteNames.size();i++){
    	   sT = new SpriteType(siteNames.get(i));
           sT.addState(PathXSiteState.LOCKEDSTATE.toString(), imgLockedSite);
           sT.addState(PathXSiteState.FAILEDSTATE.toString(), imgFailedSite);
           sT.addState(PathXSiteState.SUCCESSSTATE.toString(), imgSuccessSite);
           s = new Sprite(sT,Float.valueOf(siteX.get(i))+Map_Current_Origin_X,
        		   Float.valueOf(siteY.get(i))+Map_Current_Origin_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
//           s = new Sprite(sT, 0, 0, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
           s.setEnabled(false);
           guiButtons.put(siteNames.get(i), s);
       }
       activateButtons(siteNames, PathXSiteState.FAILEDSTATE.toString(),3);
       
       // load the level info dialog and close button
       String levelInfoDialog = props.getProperty(PathXPropertyType.IMAGE_GAME_INFO_DIALOG);
       sT = new SpriteType(INFO_DIALOG_TYPE);
       img = loadImage(imgPath + levelInfoDialog);
       sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
       s = new Sprite(sT, LEVEL_INFO_DIALOG_X, LEVEL_INFO_DIALOG_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
       guiDialogs.put(INFO_DIALOG_TYPE, s);
       
       // close try and leave button
       String dialogCloseButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_INFO_DIALOG_CLOSE);
       sT = new SpriteType(INFO_DIALOG_CLOSE_TYPE);
       img = loadImage(imgPath + dialogCloseButton);
       sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
       s = new Sprite(sT, LEVEL_INFO_DIALOG_CLOSE_BUTTON_X,LEVEL_INFO_DIALOG_CLOSE_BUTTON_Y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
       s.setEnabled(false);
       guiButtons.put(INFO_DIALOG_CLOSE_TYPE, s);
       //try again button
       String tryAgainButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_TRY);
       sT = new SpriteType(TRY_DIALOG_TYPE);
       img = loadImage(imgPath + tryAgainButton);
       sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
       s = new Sprite(sT, TRY_BUTTON_X,TRY_BUTTON_y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
       s.setEnabled(false);
       guiButtons.put(TRY_DIALOG_TYPE, s);
       //LEAVE TOWN BUTTON
       String leaveTownButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_LEAVE);
       sT = new SpriteType(LEAVE_DIALOG_TYPE);
       img = loadImage(imgPath + leaveTownButton);
       sT.addState(PathXTileState.VISIBLE_STATE.toString(), img);
       s = new Sprite(sT, LEAVE_BUTTON_X,LEAVE_BUTTON_y, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
       s.setEnabled(false);
       guiButtons.put(LEAVE_DIALOG_TYPE, s);
       
       //Start and Pause button
       String startButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_START);
       sT = new SpriteType(PAUSE_BUTTON_TYPE);
       img = loadImageWithColorKey(imgPath + startButton,COLOR_KEY);
       sT.addState(START_STATE, img);
       String pauseButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PAUSE);
       img = loadImageWithColorKey(imgPath + pauseButton,COLOR_KEY);
       sT.addState(PAUSE_STATE, img);
       s = new Sprite(sT, 16,160, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
       guiButtons.put(PAUSE_BUTTON_TYPE, s); 
       
       //Speed Control button
       String speedControlB = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SPEED);
       img = loadImageWithColorKey(imgPath + speedControlB,COLOR_KEY);
       sT = new SpriteType(SPEED_CONTROL_BUTTON);
       sT.addState(PathXTileState.VISIBLE_STATE.toString(),img);
       this.speedControlSprite = new Sprite(sT, 300,332, 0, 0, PathXTileState.INVISIBLE_STATE.toString());
       
       //mute & unmute button
       BufferedImage imageMute,imageUnmute;
       String unmuteButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_UNMUTE);
       String muteButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_MUTE);
       imageUnmute = loadImage(imgPath + unmuteButton);
       imageMute = loadImage(imgPath + muteButton);
       sT = new SpriteType(SOUND_CONTROL_TYPE);
       sT.addState(UNMUTE_STATE,imageUnmute);
       sT.addState(MUTE_STATE, imageMute);
       s = new Sprite(sT, 190,190, 0, 0,UNMUTE_STATE);
       s.setEnabled(false);
       guiButtons.put(SOUND_CONTROL_TYPE, s);
       sT = new SpriteType(MUSIC_CONTROL_TYPE);
       sT.addState(UNMUTE_STATE,imageUnmute);
       sT.addState(MUTE_STATE, imageMute);
       s = new Sprite(sT, 190,240, 0, 0,UNMUTE_STATE);
       s.setEnabled(false);
       guiButtons.put(MUSIC_CONTROL_TYPE, s);
       
       //Load all Special Controls here
       initAllSpecials();
	}
	
	/**
	 * this method will load all the specials;
	 */
	 private void initAllSpecials(){
		 // NOW LOAD ALL THE TILES FROM A SPRITE SHEET
		 PropertiesManager props = PropertiesManager.getPropertiesManager();
		 SpriteType sT;
	     String tilesSpriteSheetFile = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_SHEET);
	     String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
	     ArrayList<BufferedImage> tileImages = loadSpriteSheetImagesWithColorKey(imgPath + tilesSpriteSheetFile,
	                16, 4, 4, 0, 0, COLOR_KEY);
//	     System.out.println(tileImages.get(0).getHeight()+" "
//	    		 +tileImages.get(0).getWidth());

	        for (int i = 0; i < tileImages.size(); i++){
	            BufferedImage img = tileImages.get(i);
	            int col = i%4;
	            int row = i/4;
	            // WE'LL MAKE A NEW SPRITE TYPE FOR EACH GROUP OF SIMILAR LOOKING TILES
	            sT = new SpriteType(SPECIAL_TYPE + i );

	            // LET'S GENERATE AN IMAGE FOR EACH STATE FOR EACH SPRITE
	            sT.addState(PathXTileState.VISIBLE_STATE.toString(), img); 
	            sT.addState(PathXTileState.MOUSE_OVER_STATE.toString(),img);
	            sT.addState(PathXTileState.INVISIBLE_STATE.toString(), img);
	            
	            //now new A sprite
	            Sprite s = new Sprite(sT,14+31*col,211+row*36,0,0,PathXTileState.INVISIBLE_STATE.toString());
	            s.setEnabled(false);
	            guiButtons.put(SPECIAL_TYPE + i ,s);
	         }
	    }

	
	public String getCurrentScreenState() {
		return currentScreenState;
	}
	@Override
	public void initGUIHandlers() {
		// TODO Auto-generated method stub
		eventHandler = new PathXEventHandler(this);
		
		//register setting Button listener
		guiButtons.get(SETTING_BUTTON_TYPE).setActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae) {
						eventHandler.respondToSettingScreen();
					}});
		//register Reset Button listener
		
		//register Help Button listener
		guiButtons.get(HELP_BUTTON_TYPE).setActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				eventHandler.respondToHelpScreen();
			}});
		//register Play Button Listener
		guiButtons.get(PLAY_BUTTON_TYPE).setActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				eventHandler.respondToLevelSelectScreen();
			}});
		
		//Right Now there is only One Site, in the future 
		//All the sites listener will be set here,using for loop or whatever
		//register for level select Listener
		//should use this handler*************************** further concerns needed
		for(int i=0;i<siteNames.size();i++){
			final int num = i;
			guiButtons.get(siteNames.get(i)).setActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					eventHandler.respondToGamePlayScreen(num);
				}});
		}
		
		//register Home Button listener
		guiButtons.get(HOME_BUTTON_TYPE).setActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				eventHandler.respondToHomeScreen();
			}});
		guiButtons.get(HOME1_BUTTON_TYPE).setActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				eventHandler.respondToHomeScreen();
			}});
		//register Quit Button Listener
		guiButtons.get(QUIT_BUTTON_TYPE).setActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				eventHandler.respondToQuit();
			}});
		guiButtons.get(QUIT1_BUTTON_TYPE).setActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				eventHandler.respondToLevelSelectScreen();
			}});
		//register Info Dialog Close button Listener
		guiButtons.get(INFO_DIALOG_CLOSE_TYPE).setActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventHandler.respondToInfoCloseButtonPressed();
			}});
		//Try Again Button
		guiButtons.get(TRY_DIALOG_TYPE).setActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventHandler.respondToTryAgainButtonPressed();
			}});
		//leave town Button
		guiButtons.get(LEAVE_DIALOG_TYPE).setActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventHandler.respondToLeaveTownButtonPressed();
			}});
		
		
		//register Start and Pause button
		guiButtons.get(PAUSE_BUTTON_TYPE).setActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				eventHandler.respondToStartPauseButtonPressed();
			}});
		
//		//Scroll Buttons
//		for(int i=0;i<4;i++){
//			final int num = i;
//			guiButtons.get(SCROLL_BUTTON_TYPE[i]).setActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					eventHandler.respondToScrollButtonPressed(num);	
//				}});
//		}
		
		// KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        this.setKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ke)
            {   
                eventHandler.respondToKeyPress(ke.getKeyCode());    
            }
        });
		 
        /**
         * this part of Code is kind special, cause I want to add a mouse pressed listener
         * to the canvas, so that when you press the scroll button, the thing will keep
         * scrolling without keep clicking the mouse. Time needed
         */
        // set up four timers for scrolling up down left right
        for(int  i=0;i<8;i++){
        	final int num = i;
        	timer[i] = new Timer(20, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					eventHandler.respondToScrollButtonPressed(num);
				}
			});
        }
        levelScrollMH = new ScrollMH(0);
        gameScrollMH = new ScrollMH(1);
        speedControlML = new speedControlML();
        
        guiButtons.get(MUSIC_CONTROL_TYPE).setActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventHandler.responseToMusicControlPressed();
			}});
        guiButtons.get(SOUND_CONTROL_TYPE).setActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventHandler.responseToSoundControlPressed();
			}});
        
        for(int i=0;i<16;i++){
        	final int num = i;
        	guiButtons.get(SPECIAL_TYPE+i).setActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					eventHandler.respondToSpecialPressed(num);
				}});
        }
	}
        
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateGUI() {
		//First to see which site is cursor currently on
		for(int i=0;i<siteNames.size();i++){
			// we got make sure only render the site Info when cursor is within
			// the specified area
			if(data.getLastMouseX()<630&&data.getLastMouseX()>10
					&&data.getLastMouseY()>100&&data.getLastMouseY()<475){
				if (guiButtons.get(siteNames.get(i)).containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
					rightSiteInfo = i;
					break;
				} else {
					rightSiteInfo = -1;
				}
			}
		}
		
		
		for(int i=0;i<16;i++){
			if (guiButtons.get(SPECIAL_TYPE+i).getState().equals(PathXTileState.VISIBLE_STATE.toString())){
                if (guiButtons.get(SPECIAL_TYPE+i).containsPoint(data.getLastMouseX(), data.getLastMouseY())){
                	guiButtons.get(SPECIAL_TYPE+i).setState(PathXTileState.MOUSE_OVER_STATE.toString());
                }
            }
            // ARE WE EXITING A BUTTON?
            else if (guiButtons.get(SPECIAL_TYPE+i).getState().equals(PathXTileState.MOUSE_OVER_STATE.toString())){
                 if (!guiButtons.get(SPECIAL_TYPE+i).containsPoint(data.getLastMouseX(), data.getLastMouseY())){
                	 guiButtons.get(SPECIAL_TYPE+i).setState(PathXTileState.VISIBLE_STATE.toString());
                }
            }	
		}
		
	}
	
	public void switchToSetttingScreen(){
		// CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SETTING_STATE);
        canvas.addMouseMotionListener(speedControlML);
		canvas.addMouseListener(speedControlML);
        
        // DEACTIVATE THE TOOLBAR CONTROLS
        deactivateButtons(PLAY_BUTTON_TYPE,false);
        deactivateButtons(RESET_BUTTON_TYPE,false);
        deactivateButtons(SETTING_BUTTON_TYPE,false);
        deactivateButtons(HELP_BUTTON_TYPE,false);

        //Activate home button
        activateButtons(HOME_BUTTON_TYPE,true);
        guiButtons.get(SOUND_CONTROL_TYPE).setEnabled(true);
        guiButtons.get(MUSIC_CONTROL_TYPE).setEnabled(true);
        speedControlSprite.setState(PathXTileState.VISIBLE_STATE.toString());
        speedControlSprite.setEnabled(true);
        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = GAME_SETTING_STATE;
        
	}
	public void switchToHelpScreen(){
		// CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_HELP_STATE);
        
        // DEACTIVATE THE TOOLBAR CONTROLS
        deactivateButtons(PLAY_BUTTON_TYPE,false);
        deactivateButtons(RESET_BUTTON_TYPE,false);
        deactivateButtons(SETTING_BUTTON_TYPE,false);
        deactivateButtons(HELP_BUTTON_TYPE,false);

        //Activate home button
        activateButtons(HOME_BUTTON_TYPE,true);
        
        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = GAME_HELP_STATE;
	}
	public void switchToLevelSelectScreen(){
		canvas.addMouseListener(levelScrollMH);
		canvas.removeMouseListener(gameScrollMH);
		canvas.removeMouseMotionListener(((PathXDataModel)data).getDragAction());
		canvas.removeMouseListener(((PathXDataModel)data).getDragAction());
		// CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_LEVEL_SELECT_STATE);
        
        // DEACTIVATE THE TOOLBAR CONTROLS
        deactivateButtons(PLAY_BUTTON_TYPE,false);
        deactivateButtons(RESET_BUTTON_TYPE,false);
        deactivateButtons(SETTING_BUTTON_TYPE,false);
        deactivateButtons(HELP_BUTTON_TYPE,false);
        deactivateButtons(HOME1_BUTTON_TYPE,false);
        deactivateButtons(QUIT1_BUTTON_TYPE,false);
        deactivateButtons(PAUSE_BUTTON_TYPE,false);
        deactivateButtons(TRY_DIALOG_TYPE, false);
        deactivateButtons(LEAVE_DIALOG_TYPE, false);
        activateButtons(SOUND_CONTROL_TYPE, false);
        activateButtons(MUSIC_CONTROL_TYPE, false);

        //Activate home button
        activateButtons(HOME_BUTTON_TYPE,true);
        activateButtons(QUIT_BUTTON_TYPE,true);
        //**//future implementation will use loop to activate all sites
        for(String s:siteNames){
        	if(guiButtons.get(s).getState()!=PathXSiteState.LOCKEDSTATE.toString())
        		guiButtons.get(s).setEnabled(true);
        }
        	
        //activate scroll buttons
        activateButtons(SCROLL_BUTTON_TYPE,true);
        //deactivate scroll1 buttons
        deactivateButtons(SCROLL1_BUTTON_TYPE,false); 

        //empty this text
        ((PathXPanel)canvas).dialogText = new StringBuffer("");


        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = GAME_LEVEL_SELECT_STATE;
	}
	public void switchToGamePlayScreen(){
		canvas.removeMouseListener(levelScrollMH);
		// CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_PLAY_STATE);
        //set up the dialog string to render
        ((PathXPanel)canvas).dialogText = ((PathXDataModel)data).getDialogText();
        
        // DEACTIVATE THE CONTROLS
        deactivateButtons(PLAY_BUTTON_TYPE,false);
        deactivateButtons(RESET_BUTTON_TYPE,false);
        deactivateButtons(SETTING_BUTTON_TYPE,false);
        deactivateButtons(HELP_BUTTON_TYPE,false);
        for(String s:siteNames)
        	guiButtons.get(s).setEnabled(false);
        deactivateButtons(HOME_BUTTON_TYPE,false);
        deactivateButtons(QUIT_BUTTON_TYPE,false);
       
        //******SPecial
        //Activate home1 and quit1 button, have to disable home1 and quit1
        //when there is still dialog
        activateButtons(HOME1_BUTTON_TYPE, false);
        activateButtons(QUIT1_BUTTON_TYPE, false);

        guiButtons.get(PAUSE_BUTTON_TYPE).setState(START_STATE);
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);
        //Activate info dialog and close button
        activateButtons(INFO_DIALOG_CLOSE_TYPE,true);
        guiDialogs.get(INFO_DIALOG_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
        guiDialogs.get(INFO_DIALOG_TYPE).setEnabled(true);
        
        //deactivate scroll1 buttons
        activateButtons(SCROLL1_BUTTON_TYPE,false);
        //deactivate scroll buttons
        deactivateButtons(SCROLL_BUTTON_TYPE,false);
        
        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = GAME_PLAY_STATE;
	}
	//this method got call when close button is pressed
	public void shutDialog(){
		canvas.addMouseListener(gameScrollMH);
		canvas.addMouseMotionListener(((PathXDataModel)data).getDragAction());
		canvas.addMouseListener(((PathXDataModel)data).getDragAction());
		activateButtons(SCROLL1_BUTTON_TYPE, true);
		deactivateButtons(INFO_DIALOG_CLOSE_TYPE, false);
        guiDialogs.get(INFO_DIALOG_TYPE).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiDialogs.get(INFO_DIALOG_TYPE).setEnabled(false);
        guiButtons.get(HOME1_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(QUIT1_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(true);
        ((PathXPanel)canvas).dialogText = new StringBuffer("");
	}
	
	public void switchToSplashScreen(){
		canvas.removeMouseListener(levelScrollMH); 
		canvas.removeMouseListener(gameScrollMH);
		canvas.removeMouseMotionListener(((PathXDataModel)data).getDragAction());
		canvas.removeMouseListener(((PathXDataModel)data).getDragAction());
		canvas.removeMouseMotionListener(speedControlML);
		canvas.removeMouseListener(speedControlML);
		
		// CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SPLASH_STATE);
        
        // activate the splash controls
        activateButtons(PLAY_BUTTON_TYPE,true);
        activateButtons(RESET_BUTTON_TYPE,true);
        activateButtons(SETTING_BUTTON_TYPE,true);
        activateButtons(HELP_BUTTON_TYPE,true);
        activateButtons(QUIT_BUTTON_TYPE,true);
        
        //Deactivate home, home1 and quit1 button
        deactivateButtons(HOME_BUTTON_TYPE,false);
        deactivateButtons(HOME1_BUTTON_TYPE,false);
        deactivateButtons(QUIT1_BUTTON_TYPE,false);
        deactivateButtons(PAUSE_BUTTON_TYPE,false);
        guiButtons.get(SOUND_CONTROL_TYPE).setEnabled(false);
        guiButtons.get(MUSIC_CONTROL_TYPE).setEnabled(false);
        speedControlSprite.setState(PathXTileState.INVISIBLE_STATE.toString());
        speedControlSprite.setEnabled(false);
        
        //**//future implementation will use loop to deactivate all site
        for(String s:siteNames)
        	guiButtons.get(s).setEnabled(false);
        //Deactivate Scroll Buttons and Scroll1 Buttons
        deactivateButtons(SCROLL1_BUTTON_TYPE,false);
        deactivateButtons(SCROLL_BUTTON_TYPE,false);
        // MAKE THE CURRENT SCREEN THE MENU SCREEN
        currentScreenState = GAME_SPLASH_STATE;
	}

	//methods and variables for scrolling
	private float Map_Current_Origin_X = MAP_ORIGIN_BASE_X;
	private float Map_Current_Origin_Y = MAP_ORIGIN_BASE_Y;
	public void scrollRight(){
		if(Map_Current_Origin_X> MAP_END_BASE_X){
			Map_Current_Origin_X -= 8;
			guiDecor.get(MAP_TYPE).setX(Map_Current_Origin_X);
			for(int i=0;i<siteNames.size();i++)
				guiButtons.get(siteNames.get(i)).setX(Map_Current_Origin_X+Float.valueOf(siteX.get(i)));
			
		}
		else {
			guiDecor.get(MAP_TYPE).setX(Map_Current_Origin_X);
			for(int i=0;i<siteNames.size();i++)
				guiButtons.get(siteNames.get(i)).setX(Map_Current_Origin_X+Float.valueOf(siteX.get(i)));
		}
	}
	public void scrollLeft(){
		if(Map_Current_Origin_X< MAP_ORIGIN_BASE_X){
			Map_Current_Origin_X += 8;
			guiDecor.get(MAP_TYPE).setX(Map_Current_Origin_X);
			for(int i=0;i<siteNames.size();i++)
				guiButtons.get(siteNames.get(i)).setX(Map_Current_Origin_X+Float.valueOf(siteX.get(i)));
			
		}
		else {
			guiDecor.get(MAP_TYPE).setX(Map_Current_Origin_X);
			for(int i=0;i<siteNames.size();i++)
				guiButtons.get(siteNames.get(i)).setX(Map_Current_Origin_X+Float.valueOf(siteX.get(i)));
		}
	}
	public void scrollUp(){
		if(Map_Current_Origin_Y < MAP_END_BASE_Y){
			Map_Current_Origin_Y += 5;
			guiDecor.get(MAP_TYPE).setY(Map_Current_Origin_Y);
			for(int i=0;i<siteNames.size();i++)
				guiButtons.get(siteNames.get(i)).setY(Map_Current_Origin_Y+Float.valueOf(siteY.get(i)));
			
		}
		else {
			guiDecor.get(MAP_TYPE).setY(Map_Current_Origin_Y);
			for(int i=0;i<siteNames.size();i++)
				guiButtons.get(siteNames.get(i)).setY(Map_Current_Origin_Y+Float.valueOf(siteY.get(i)));
		}
	} 
	public void scrollDown(){
		if(Map_Current_Origin_Y > MAP_ORIGIN_BASE_Y){
			Map_Current_Origin_Y -= 5;
			guiDecor.get(MAP_TYPE).setY(Map_Current_Origin_Y);
			for(int i=0;i<siteNames.size();i++)
				guiButtons.get(siteNames.get(i)).setY(Map_Current_Origin_Y+Float.valueOf(siteY.get(i)));
			
		}
		else {
			guiDecor.get(MAP_TYPE).setY(Map_Current_Origin_Y);
			for(int i=0;i<siteNames.size();i++)
				guiButtons.get(siteNames.get(i)).setY(Map_Current_Origin_Y+Float.valueOf(siteY.get(i)));
		}
	}
	public void activateButtons(String s,boolean enable){
		guiButtons.get(s).setState(PathXTileState.VISIBLE_STATE.toString());
        guiButtons.get(s).setEnabled(enable);
	}
	public void deactivateButtons(String s,boolean enable){
		guiButtons.get(s).setState(PathXTileState.INVISIBLE_STATE.toString());
        guiButtons.get(s).setEnabled(enable);
	}
	public void activateButtons(String[] ss,boolean enable){
		for(String s:ss){
			guiButtons.get(s).setState(PathXTileState.VISIBLE_STATE.toString());
			guiButtons.get(s).setEnabled(enable);
		}
	}
	public void deactivateButtons(String[] ss,boolean enable){
		for(String s:ss){
			guiButtons.get(s).setState(PathXTileState.INVISIBLE_STATE.toString());
			guiButtons.get(s).setEnabled(enable);
		}
	}
	public void activateButtons(ArrayList<String> ss,String state, int amount){
		for(int i=0;i<amount;i++){
			guiButtons.get(ss.get(i)).setState(state);
			guiButtons.get(ss.get(i)).setEnabled(true);
		}
		for(int i=amount;i<ss.size();i++){
			guiButtons.get(ss.get(i)).setState(PathXSiteState.LOCKEDSTATE.toString());
			guiButtons.get(ss.get(i)).setEnabled(false);
		}
	}
	public void deactivateButtons(ArrayList<String> ss){
		for(String s:ss){
			guiButtons.get(s).setState(PathXTileState.INVISIBLE_STATE.toString());
			guiButtons.get(s).setEnabled(false);
		}
	}
	
	// the reason to deal this right here is because everything is easy access here
	public void caughtByCopGUIupdate(){
		guiDialogs.get(INFO_DIALOG_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
		activateButtons(TRY_DIALOG_TYPE, true);
		activateButtons(LEAVE_DIALOG_TYPE, true);
		activateButtons(QUIT1_BUTTON_TYPE, false);
		activateButtons(HOME1_BUTTON_TYPE, false);
		guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);
		canvas.removeMouseListener(gameScrollMH);
		canvas.removeMouseMotionListener(((PathXDataModel)data).getDragAction());
		canvas.removeMouseListener(((PathXDataModel)data).getDragAction());
		((PathXPanel)canvas).dialogText = new StringBuffer(CAUGHT_BY_COP_TEXT);
	}
	public void winGUIupdate(){
		PathXDataModel data = ((PathXDataModel)this.data);
		guiDialogs.get(INFO_DIALOG_TYPE).setState(PathXTileState.VISIBLE_STATE.toString());
		activateButtons(TRY_DIALOG_TYPE, true);
		activateButtons(LEAVE_DIALOG_TYPE, true);
		activateButtons(QUIT1_BUTTON_TYPE, false);
		activateButtons(HOME1_BUTTON_TYPE, false);
		guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);
		canvas.removeMouseListener(gameScrollMH);
		canvas.removeMouseMotionListener(data.getDragAction());
		canvas.removeMouseListener(data.getDragAction());
		((PathXPanel)canvas).dialogText = new StringBuffer(data.getDialogWinText());
		guiButtons.get(siteNames.get(data.curLevelNum)).setState(PathXSiteState.SUCCESSSTATE.toString());
		guiButtons.get(siteNames.get(data.curLevelNum)).setEnabled(true);
		if(data.curLevelNum<17){
			guiButtons.get(siteNames.get(data.curLevelNum+3)).setState(PathXSiteState.FAILEDSTATE.toString());
			guiButtons.get(siteNames.get(data.curLevelNum+3)).setEnabled(true);
		}
	}	
	
	private class speedControlML extends MouseAdapter{
		private boolean pressed = false;
		public void mousePressed(MouseEvent e){
			if(speedControlSprite.containsPoint(e.getX(),e.getY())){
//				System.out.println("pressed");
				pressed = true;
			}
		}

		public void mouseReleased(MouseEvent e) {
			if(pressed){
//				System.out.println("released");
				pressed = false;
			}
		}

		public void mouseDragged(MouseEvent e){
			if(pressed){
				if(e.getX()<=500&&e.getX()>=100){
//					System.out.println("dragged");
					((PathXDataModel)data).setGameSpeed(1 + (float)(e.getX()-100)/200);
					speedControlSprite.setX(e.getX());
				}
			}
		}

		   
		
	}
	
	
	
	private class ScrollMH extends MouseAdapter{
		private int n;
		//since the position of both sets of scroll buttons are the same,
		//so add the argument for the construction method
		//0 for level scroll buttons,
		//1 for game screen scroll buttons,
		
		public ScrollMH(int num){
			this.n = num;
		}
		public void mousePressed(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			if(x>60&&x<90&&y>370&&y<400){
				if(!timer[n*4+0].isRunning())
					timer[n*4+0].start();
			}
			if(x>60&&x<90&&y>420&&y<450){
				if(!timer[n*4+1].isRunning())
					timer[n*4+1].start();
			}
			if(x>25&&x<55&&y>395&&y<425){
				if(!timer[n*4+2].isRunning())
					timer[n*4+2].start();
			}
			if(x>95&&x<125&&y>395&&y<425){
				if(!timer[n*4+3].isRunning())
					timer[n*4+3].start();
			}
		}
	    public void mouseReleased(MouseEvent e) {
	    	for(int i=n*4;i<n*4+4;i++){
	    		if(timer[i].isRunning())
	    			timer[i].stop();
	    	}
	    }
	}




/**
 * the following methods are all cheatings
 */
	public void unlockNextLevel() {
		String tempS = null;
		for(String s:siteNames){
			if(guiButtons.get(s).getState()==PathXSiteState.LOCKEDSTATE.toString()){
				tempS = s;
				break;
			}
		}	
		if(tempS!=null){
			guiButtons.get(tempS).setState(PathXSiteState.FAILEDSTATE.toString());	
			guiButtons.get(tempS).setEnabled(true);
		}
	}
}

