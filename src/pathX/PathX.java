package pathX;

import pathX.ui.PathXMiniGame;
import properties_manager.PropertiesManager;
import static pathX.PathXConstants.*;

public class PathX {
	static PathXMiniGame miniGame = new PathXMiniGame();
	
	public static void main(String[] args) {
		 try{
			// LOAD THE SETTINGS FOR STARTING THE APP
			PropertiesManager props = PropertiesManager.getPropertiesManager();
			props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
			props.loadProperties(PROPERTIES_FILE_NAME,PROPERTIES_SCHEMA_FILE_NAME);
			// THEN WE'LL LOAD THE GAME FLAVOR AS SPECIFIED BY THE PROPERTIES
			// FILE
			String gameFlavorFile = props.getProperty(PathXPropertyType.FILE_GAME_PROPERTIES);
			props.loadProperties(gameFlavorFile,PROPERTIES_SCHEMA_FILE_NAME);
			// NOW WE CAN LOAD THE UI, WHICH WILL USE ALL THE FLAVORED CONTENT
			String appTitle = props.getProperty(PathXPropertyType.TEXT_TITLE_BAR_GAME);
			miniGame.initMiniGame(appTitle, FPS, WINDOW_WIDTH, WINDOW_HEIGHT);
			
			// GET THE PROPER WINDOW DIMENSIONS
			miniGame.startGame();
		}catch (Exception e) {
		}
	}

	
	/**
	 * SortingHatPropertyType represents the types of data that will need to be
	 * extracted from XML files.
	 */
	public enum PathXPropertyType{
		  /* SETUP FILE NAMES */
        FILE_GAME_PROPERTIES, // data/pathX/pathX_properties.xml

        /* DIRECTORY PATHS FOR FILE LOADING */
        PATH_AUDIO,
        PATH_IMG,
        PATH_LEVEL_IMG,
        PATH_LEVEL_FILE,
		
		// All the images used
		IMAGE_GAME_SPLASH,
        IMAGE_GAME_SETTING,         	
        IMAGE_GAME_LEVEL_SELECT,  
        IMAGE_GAME_PLAY,		
        IMAGE_GAME_HELP,
        IMAGE_GAME_INFO_DIALOG,         
        IMAGE_BUTTON_PLAY, 			
        IMAGE_BUTTON_RESET,		       
		IMAGE_BUTTON_SETTING,			
		IMAGE_BUTTON_HELPS,				
		IMAGE_BUTTON_HOME,	
		IMAGE_BUTTON_QUIT,			
        IMAGE_BUTTON_INFO_DIALOG_CLOSE,
        IMAGE_WINDOW_ICON,
        IMAGE_MAP,
        IMAGE_LOCKED_SITE,
        IMAGE_FAILED_SITE,
        IMAGE_SUCCESS_SITE,
        IMAGE_SCROLL_RIGHT,
        IMAGE_SCROLL_LEFT,
        IMAGE_SCROLL_UP,
        IMAGE_SCROLL_DOWN,
        IMAGE_ZOMBIE,
        IMAGE_BANDIT,
        IMAGE_COP,
        IMAGE_CAR,
        IMAGE_GAME_MAP,
		IMAGE_DESTINATION,      
		IMAGE_CLOSE_NODE,
        IMAGE_OPEN_NODE,
        IMAGE_BUTTON_START,
        IMAGE_BUTTON_PAUSE,
        IMAGE_BUTTON_TRY,
        IMAGE_BUTTON_LEAVE,
        IMAGE_BUTTON_SPEED,
        IMAGE_BUTTON_MUTE,
        IMAGE_BUTTON_UNMUTE,
        IMAGE_SPECIAL_SHEET,
        IMAGE_SPECIAL_LOCKED,
        
        //text file for help Info
        TEXT_HELP_INFO,
        HTML_HELP_INFO,
        
        //All the text used
        TEXT_TITLE_BAR_GAME,
        ACCOUNT_BALANCE,
        GOAL_AMOUNT,
        //ALL the audio files
        
        //All the options
        LEVEL_OPTIONS_FILE,
        LEVEL_OPTIONS_NAME,
        LEVEL_OPTIONS_SITE_INFO,
        LEVEL_OPTIONS_X,
        LEVEL_OPTIONS_Y,
	}
}
