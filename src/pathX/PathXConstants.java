package pathX;

import java.awt.Color;
import java.awt.Font;

/**
 * This class stores all the constants used by The Sorting Hat application. We'll
 * do this here rather than load them from files because many of these are
 * derived from each other.
 * 
 * @author Richard McKenna
 */

public class PathXConstants {
	 // WE NEED THESE CONSTANTS JUST TO GET STARTED
    // LOADING SETTINGS FROM OUR XML FILES
    public static String PROPERTY_TYPES_LIST = "property_types.txt";
    public static String PROPERTIES_FILE_NAME = "properties.xml";
    public static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";
    public static String LEVEL_SCHEMA_FILE_NAME = "PathXLevelSchema.xsd";
    public static String PATH_DATA = "./data/";
    
    // THESE ARE THE TYPES OF CONTROLS, WE USE THESE CONSTANTS BECAUSE WE'LL
    // STORE THEM BY TYPE, SO THESE WILL PROVIDE A MEANS OF IDENTIFYING THEM
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    //A Map type is needed to rendering and scrolling
    public static final String MAP_TYPE = "MAP_TYPE";
   //game map type for the game background
    public static final String GAME_MAP_TYPE = "GAME_MAP_TYPE";
    //We will need info dialog type for the siteinfo 
    public static final String INFO_DIALOG_TYPE = "INFO_DIALOG_TYPE";

    // IN-GAME UI CONTROL TYPES
    public static final String PLAY_BUTTON_TYPE = "PLAY_BUTTON_TYPE";
    public static final String RESET_BUTTON_TYPE = "RESET_BUTTON_TYPE";
    public static final String SETTING_BUTTON_TYPE = "SETTING_BUTTON_TYPE";
    public static final String HELP_BUTTON_TYPE = "HELP_BUTTON_TYPE"; 
    public static final String QUIT_BUTTON_TYPE = "QUIT_BUTTON_TYPE";
    public static final String HOME_BUTTON_TYPE = "HOME_BUTTON_TYPE";
    public static final String QUIT1_BUTTON_TYPE = "QUIT1_BUTTON_TYPE";
    public static final String HOME1_BUTTON_TYPE = "HOME1_BUTTON_TYPE";
    public static final String[] SCROLL_BUTTON_TYPE = {"SCROLL_UP_BUTTON_TYPE","SCROLL_DOWN_BUTTON_TYPE","SCROLL_LEFT_BUTTON_TYPE","SCROLL_RIGHT_BUTTON_TYPE"};
    public static final String[] SCROLL1_BUTTON_TYPE = {"SCROLL1_UP_BUTTON_TYPE","SCROLL1_DOWN_BUTTON_TYPE","SCROLL1_LEFT_BUTTON_TYPE","SCROLL1_RIGHT_BUTTON_TYPE"};
    public static final String PAUSE_BUTTON_TYPE = "PAUSE_BUTTON_TYPE";
    public static final String SOUND_CONTROL_TYPE = "SOUND_CONTROL_TYPE";
    public static final String MUSIC_CONTROL_TYPE = "MUSIC_CONTROL_TYPE";
    public static final String SPEED_CONTROL_BUTTON = "SPEED_CONTROL_BUTTON";
    public static final String SPECIAL_TYPE = "SPECIAL_TYPE";
    
    //THIS pause button needs two state
    public static final String START_STATE = "START_STATE";
    public static final String PAUSE_STATE = "PAUSE_STATE";
    //sound and music button two states
    public static final String UNMUTE_STATE = "UNMUTE_STATE";
    public static final String MUTE_STATE = "MUTE_STATE";
    
    //no "Button" cause this button is special
    public static final String INFO_DIALOG_CLOSE_TYPE="INFO_DIALOG_CLOSE_TYPE";
    public static final String TRY_DIALOG_TYPE = "TRY_DIALOG_TYPE";
    public static final String LEAVE_DIALOG_TYPE = "LEAVE_DIALOG_TYPE";

    // DIALOG TYPES
    public static final String WIN_DIALOG_TYPE = "WIN_DIALOG_TYPE";
    public static final String STATS_DIALOG_TYPE = "STATS_DIALOG_TYPE";
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE Five
    public static final String GAME_SPLASH_STATE = "GAME_SPLASH_STATE";
    public static final String GAME_LEVEL_SELECT_STATE = "GAME_LEVEL_SELECT_STATE";
    public static final String GAME_PLAY_STATE = "GAME_PLAY_STATE";  
    public static final String GAME_HELP_STATE = "GAME_HELP_STATE";
    public static final String GAME_SETTING_STATE = "GAME_SETTING_STATE";
    //two Map States Scroll map and Game Background
    public static final String MAP_STATE = "MAP_STATE";
    public static final String GAME_MAP_STATE = "GAME_MAP_STATE";

    // ANIMATION SPEED
    public static final int FPS = 30;

    // UI CONTROL SIZE AND POSITION SETTINGS
    public static final int WINDOW_WIDTH = 640+4;
    public static final int WINDOW_HEIGHT = 480+30;
    //position of transparent window
    public static final int TRANS_WIN_LT_X = 10;
    public static final int TRANS_WIN_LT_Y = 100;
    public static final int TRANS_WIN_RB_X = 620;
    public static final int TRANS_WIN_RB_Y = 375;
        
    
    // FOR MOVING TILES AROUND
    public static final int MAX_TILE_VELOCITY = 20;
    
    // UI CONTROLS POSITIONS IN THE splash SCREEN
    public static final int PLAY_BUTTON_X = 60;
    public static final int PLAY_BUTTON_Y = 400;
    public static final int RESET_BUTTON_X = 200;
    public static final int RESET_BUTTON_Y = 400;
    public static final int SETTING_BUTTON_X = 340;
    public static final int SETTING_BUTTON_Y = 400;
    public static final int HELP_BUTTON_X = 480;
    public static final int HELP_BUTTON_Y = 400;
    public static final int QUIT_BUTTON_X = 590;
    public static final int QUIT_BUTTON_Y = 20;
    public static final int HOME_BUTTON_X = 550;
    public static final int HOME_BUTTON_Y = 20;
    public static final int QUIT1_BUTTON_X = 83;
    public static final int QUIT1_BUTTON_Y = 115;
    public static final int HOME1_BUTTON_X = 27;
    public static final int HOME1_BUTTON_Y = 115;
    
    //Coordinates for four Scroll Buttons
    public static final int[] SCROLL_BUTTON_X = {60,60,25,95};
    public static final int[] SCROLL_BUTTON_Y = {370,420,395,395};
    
    // positions of the level information dialog and close button
    public static final int LEVEL_INFO_DIALOG_X = 90;
    public static final int LEVEL_INFO_DIALOG_Y = 26;
    public static final int LEVEL_INFO_DIALOG_CLOSE_BUTTON_X = 235;
    public static final int LEVEL_INFO_DIALOG_CLOSE_BUTTON_Y = 320;
    public static final int TRY_BUTTON_X = 120;
    public static final int TRY_BUTTON_y = 320;
    public static final int LEAVE_BUTTON_X = 335;
    public static final int LEAVE_BUTTON_y = 320;
    
    
    //Position Coordinate to Cut the Map
    public static final int MAP_ORIGIN_BASE_X = 10;
    public static final int MAP_ORIGIN_BASE_Y = -225;
    public static final int MAP_END_BASE_X = -470;
    public static final int MAP_END_BASE_Y = 100;
    //position Coordinate for Game Map
    public static final int GAME_MAP_ORIGIN_BASE_X = 139;
    public static final int GAME_MAP_ORIGIN_BASE_Y = -81;
    public static final int GAME_MAP_END_BASE_X = -116;
    public static final int GAME_MAP_END_BASE_Y = 9;
    
    // THESE ARE USED FOR FORMATTING THE TIME OF GAME
    public static final long MILLIS_IN_A_SECOND = 1000;
    public static final long MILLIS_IN_A_MINUTE = 1000 * 60;
    public static final long MILLIS_IN_AN_HOUR  = 1000 * 60 * 60;

    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color COLOR_DEBUG_TEXT = Color.BLACK;
    public static final Color COLOR_TEXT_DISPLAY = new Color (10, 160, 10);
    public static final Color COLOR_STATS = new Color(0, 60, 0);
    public static final Color COLOR_ALGORITHM_HEADER = Color.WHITE;

    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font FONT_TEXT_DISPLAY = new Font("ARIAL BLACK", Font.BOLD, 20);
    public static final Font SPECIAL_TEXT_DISPLAY = new Font("ARIAL BLACK", Font.BOLD, 15);
    public static final Font FONT_BALANCE_TEXT_DISPLAY	 = new Font("ARIAL BLACK", Font.BOLD, 26);
    public static final Font FONT_TEXT_HELP_DISPLAY = new Font("TIMES NEW ROMAN", Font.BOLD, 20);
    public static final Font FONT_DEBUG_TEXT = new Font(Font.MONOSPACED, Font.BOLD, 18);
    public static final Font FONT_STATS = new Font(Font.MONOSPACED, Font.BOLD, 20);

    /**
     * the following part of constant is purely for loading the levels
     */
    // CONSTANTS FOR LOADING DATA FROM THE XML FILES
    // THESE ARE THE XML NODES
    public static final String LEVEL_NODE = "level";
    public static final String INTERSECTIONS_NODE = "intersections";
    public static final String INTERSECTION_NODE = "intersection";
    public static final String ROADS_NODE = "roads";
    public static final String ROAD_NODE = "road";
    public static final String START_INTERSECTION_NODE = "start_intersection";
    public static final String DESTINATION_INTERSECTION_NODE = "destination_intersection";
    public static final String MONEY_NODE = "money";
    public static final String POLICE_NODE = "police";
    public static final String BANDITS_NODE = "bandits";
    public static final String ZOMBIES_NODE = "zombies";

    // AND THE ATTRIBUTES FOR THOSE NODES
    public static final String NAME_ATT = "name";
    public static final String IMAGE_ATT = "image";
    public static final String ID_ATT = "id";
    public static final String X_ATT = "x";
    public static final String Y_ATT = "y";
    public static final String OPEN_ATT = "open";
    public static final String INT_ID1_ATT = "int_id1";
    public static final String INT_ID2_ATT = "int_id2";
    public static final String SPEED_LIMIT_ATT = "speed_limit";
    public static final String ONE_WAY_ATT = "one_way";
    public static final String AMOUNT_ATT = "amount";
    public static final String NUM_ATT = "num";
    public static final String POS_ATT = "pos";
    
    //Numbers for the intersects and roads, everything about game GUI
    public static final int NODE_RADIUS = 16;
    public static final float XILUO_RADIUS = (float) 17.5;
    public static final float CB_RADIUS = (float) 16;
    
    public static final String XILUO_TYPE = "XILUO_TYPE";
    public static final String XILUO_STATE = "XILUO_STATE";
    public static final String COP_TYPE = "COP_TYPE";
    public static final String COP_STATE = "COP_STATE";
    public static final String BANDIT_TYPE = "BANDIT_TYPE";
    public static final String BANDIT_STATE = "BANDIT_STATE";
    public static final String CAR_TYPE = "CAR_TYPE";
    public static final String CAR_STATE = "CAR_STATE";
    
    public static final float ROAD_BASE_SPEED = (float)30;
    
    //Cop Loop for cop go around a loop.I know it might be stupid and
    //less professional, but this is definitely efficient and steableCop
    public static final int[][] COP_LOOP = {{6,2,3,4,7,5,1},  //first row for Benfica
    										{13,12,11,14,1},				//real Mardrid
    										{5,9,1},};			//Liverpool
    
    public static final String[] SPECIAL_NAMES = {"Make Light Green","Make Light Red",
    	"Decrease Speed Limit", "Increase Speed Limit","Increase Player Speed","Flat Tire",
    	"Empty Gas Tank","Close Road","Close Intersection","Open Intersection","Steal",
    	"Mind Control","Intangibility","Mindless Terror","Flying","Invincibility"};
    
    //String Display
    public static final String CAUGHT_BY_COP_TEXT = "TOO BAD!!!"+"\n"+"\n"
    			+"You were caught by cop,"+"\n"+"and lost 10% of your balance.";
}
