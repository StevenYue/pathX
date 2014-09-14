package pathX.ui;

import java.awt.event.KeyEvent;

import pathX.data.Level;
import pathX.data.PathXDataModel;
import static pathX.PathXConstants.*;

public class PathXEventHandler {
	private PathXMiniGame game;
	
	public PathXEventHandler(PathXMiniGame initial){
		game = initial;
	}
	public void respondToSettingScreen(){
		game.switchToSetttingScreen();
	}
	public void respondToHomeScreen(){
		game.switchToSplashScreen();
	}
	public void respondToQuit(){
		System.exit(0);
	}
	public void respondToHelpScreen(){
		game.switchToHelpScreen();
	}
	public void respondToLevelSelectScreen(){
		if(game.getCurrentScreenState()==GAME_PLAY_STATE){
			((PathXDataModel)game.getDataModel()).pause();
			((PathXDataModel)game.getDataModel()).quitGameAsloss();
		}
		game.switchToLevelSelectScreen();
	}
	public void respondToGamePlayScreen(int num){
		Level curLevel = ((PathXDataModel)game.getDataModel()).getAllLevels().get(num);
		((PathXDataModel)game.getDataModel()).curLevelNum = num;
		((PathXDataModel)game.getDataModel()).setCurrentLevel(curLevel);
		((PathXDataModel)game.getDataModel()).reset(game);
		game.switchToGamePlayScreen();
	}
	public void respondToInfoCloseButtonPressed(){
//		((PathXDataModel)game.getDataModel()).beginGame();
//		((PathXDataModel)game.getDataModel()).pause();
		game.shutDialog();
	}
	public void respondToStartPauseButtonPressed(){
		if(game.getGUIButtons().get(PAUSE_BUTTON_TYPE).getState()==START_STATE){
			game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setState(PAUSE_STATE);
			if(!((PathXDataModel)game.getDataModel()).inProgress()){
				((PathXDataModel)game.getDataModel()).beginGame();
//				System.out.println("fuck");
			}
			((PathXDataModel)game.getDataModel()).unpause();
		}else{
			game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setState(START_STATE);
			((PathXDataModel)game.getDataModel()).pause();
		}
		
	}
	public void respondToKeyPress(int keyCode){
		if(game.isCurrentScreenState(GAME_PLAY_STATE)){
			switch(keyCode){	
			case KeyEvent.VK_D: ((PathXDataModel)game.getDataModel()).scrollRight();break;	
			case KeyEvent.VK_A: ((PathXDataModel)game.getDataModel()).scrollLeft();break;
			case KeyEvent.VK_S: ((PathXDataModel)game.getDataModel()).scrollDown();break;
			case KeyEvent.VK_W: ((PathXDataModel)game.getDataModel()).scrollUp();break;
			default: break;
			}
		}	
		if(game.isCurrentScreenState(GAME_LEVEL_SELECT_STATE)){
			switch(keyCode){
			case KeyEvent.VK_N:game.unlockNextLevel();break;
			case KeyEvent.VK_I:((PathXDataModel)game.getDataModel()).increaseTotalBalance();break;
			default:break;
			}
				
		}
		
	}
	public void respondToScrollButtonPressed(int i) {
		switch(i){
		case 3: game.scrollRight();break;
		case 2: game.scrollLeft();break;
		case 1: game.scrollDown();break;
		case 0: game.scrollUp();break;
		case 4: ((PathXDataModel)game.getDataModel()).scrollUp();break;
		case 5: ((PathXDataModel)game.getDataModel()).scrollDown();break;
		case 6: ((PathXDataModel)game.getDataModel()).scrollLeft();break;
		case 7: ((PathXDataModel)game.getDataModel()).scrollRight();break;	
		default: break;
		}
	}
	public void respondToTryAgainButtonPressed() {
		game.shutDialog();
		((PathXDataModel)game.getDataModel()).reset(game);
		game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setState(START_STATE);
		game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setEnabled(true);
		game.deactivateButtons(TRY_DIALOG_TYPE, false);
		game.deactivateButtons(LEAVE_DIALOG_TYPE, false);
	}
	public void respondToLeaveTownButtonPressed() {
		game.switchToLevelSelectScreen();
	}
	public void responseToMusicControlPressed() {
		if(game.getGUIButtons().get(MUSIC_CONTROL_TYPE).getState()==MUTE_STATE)
			game.getGUIButtons().get(MUSIC_CONTROL_TYPE).setState(UNMUTE_STATE);
		else 
			game.getGUIButtons().get(MUSIC_CONTROL_TYPE).setState(MUTE_STATE);
	}
	public void responseToSoundControlPressed() {
		if(game.getGUIButtons().get(SOUND_CONTROL_TYPE).getState()==MUTE_STATE)
			game.getGUIButtons().get(SOUND_CONTROL_TYPE).setState(UNMUTE_STATE);
		else 
			game.getGUIButtons().get(SOUND_CONTROL_TYPE).setState(MUTE_STATE);
	}
	public void respondToSpecialPressed(int num) {
		System.out.println(num);
	}
}
