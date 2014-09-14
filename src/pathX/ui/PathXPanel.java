package pathX.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;







import pathX.PathX.PathXPropertyType;
import pathX.data.Bandit;
import pathX.data.Car;
import pathX.data.Cop;
import pathX.data.Intersect;
import pathX.data.PathXDataModel;
import pathX.data.Road;
import pathX.data.Xiluo;
import properties_manager.PropertiesManager;
import mini_game.Sprite;
import mini_game.SpriteType;
import static pathX.PathXConstants.*;

public class PathXPanel extends JPanel{
    // THIS IS ACTUALLY OUR Sorting Hat APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER
    private PathXMiniGame game;
    
    // WE'LL USE THIS TO FORMAT SOME TEXT FOR DISPLAY PURPOSES
    private NumberFormat numberFormatter;
    //this scrollPanel is to display help information
    private JScrollPane scrollPane;
    //this is to display the dialog text
    public StringBuffer dialogText;
    private JEditorPane helpPane;
    
    //As Prof did, use this to render nodes and Roads
    Ellipse2D.Double recyclableCircle;
    Line2D.Double recyclableLine; 
    GeneralPath recyclableTriangle;
    int triangleXPoints[] = {-60/2,  -60/2,  60/2};//60 width of the triangle
    int triangleYPoints[] = {60/2, -60/2, 0};

    
	
	public PathXPanel(PathXMiniGame initMini){
		game = initMini;
		recyclableCircle = new Ellipse2D.Double(0, 0,NODE_RADIUS*2,NODE_RADIUS*2);//radius is 16
		recyclableLine = new Line2D.Double(0, 0, 0, 0);
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
        this.setLayout(null);       
        initialHelpInfo();
        this.add(scrollPane);
        
     // MAKING THE TRIANGLE FOR ONE WAY STREETS IS A LITTLE MORE INVOLVED
        recyclableTriangle =  new GeneralPath(GeneralPath.WIND_EVEN_ODD,
                                                triangleXPoints.length);
        recyclableTriangle.moveTo(triangleXPoints[0], triangleYPoints[0]);
        for (int index = 1; index < triangleXPoints.length; index++) 
        {
            recyclableTriangle.lineTo(triangleXPoints[index], triangleYPoints[index]);
        };
        recyclableTriangle.closePath();
	}
	public void initialHelpInfo(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String htmlHelpInfo = props.getProperty(PathXPropertyType.HTML_HELP_INFO);
        
        //************
        // WE'LL DISPLAY ALL HELP INFORMATION USING HTML
        helpPane = new JEditorPane();
        helpPane.setEditable(false);
        scrollPane = new JScrollPane(helpPane);
                        
        // NOW LOAD THE HELP HTML
        helpPane.setContentType("text/html");
        
        // LOAD THE HELP PAGE
        String fileHTML;
		try {
			fileHTML = game.getFileManager().loadTextFile(htmlHelpInfo);
			helpPane.setText(fileHTML); 
			helpPane.setFont(FONT_TEXT_HELP_DISPLAY);
			helpPane.setEditable(false);
	        scrollPane = new JScrollPane(helpPane);
		} catch (IOException e) {e.printStackTrace();}
		 scrollPane.setBounds(TRANS_WIN_LT_X, TRANS_WIN_LT_Y, TRANS_WIN_RB_X, TRANS_WIN_RB_Y);
	     scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	     scrollPane.setVisible(false);
        
        // LET OUR HELP SCREEN JOURNEY AROUND THE WEB VIA HYPERLINK
        HelpHyperlinkListener hhl = new HelpHyperlinkListener();
        helpPane.addHyperlinkListener(hhl);        
        
	}
	
	public void paintComponent(Graphics g){
		try{
            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            game.beginUsingData();
            Graphics2D g2 = (Graphics2D) g;
            // CLEAR THE PANEL
            super.paintComponent(g);
            //in order to avoid confusing about rendering sequence
            //let's do with five cases
            scrollPane.setVisible(false);
            if(game.getCurrentScreenState() == GAME_LEVEL_SELECT_STATE){
            	renderMap(g);
            	renderSite(g);
            	renderSiteInfo(g);
            	renderBackground(g);
            	renderGUIControls(g);
            	renderBalance(g);
            }
            if(game.getCurrentScreenState()== GAME_HELP_STATE){
            	renderBackground(g);
				renderGUIControls(g);
				scrollPane.setVisible(true);
            }
            if(game.getCurrentScreenState()==GAME_SPLASH_STATE){
            	// RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
				renderBackground(g);
				// AND THE BUTTONS AND DECOR
				renderGUIControls(g);
            }
        	if(game.getCurrentScreenState()==GAME_SETTING_STATE){
				// RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
				renderBackground(g);
				// AND THE BUTTONS AND DECOR
				renderGUIControls(g);
				renderSpeedControl(g);
				renderMusicSoundControl(g);
				
        	}
        	if(game.getCurrentScreenState()==GAME_PLAY_STATE){
        		renderGameMap(g);
        		renderRoads(g2);
        		renderNodes(g2);
        		renderXCB(g);
        		renderBackground(g);
        		renderGUIControls(g);
        		renderCurLevelMoney(g);
        		renderAllSpecial(g);
        		renderDialog(g);
        		renderDialogText(g);
        	}
        }
        finally{
            // RELEASE THE LOCK
            game.endUsingData();    
        }
	}
	
	
	/**
	 * the following part of different rendering methods is for rendering different
	 * types of things one the canvas
	 */
	//render the fucking Xiluo,Cops and Bandits
	private void renderXCB(Graphics g){
		PathXDataModel data = ((PathXDataModel)game.getDataModel());
		ArrayList<Xiluo> Ss = data.getXiluos();
		for(Xiluo s:Ss){
				s.setX(s.getX()+data.getMap_Current_Origin_X());
				s.setY(s.getY()+data.getMap_Current_Origin_Y());
			renderSprite(g, s);
				s.setX(s.getX()-data.getMap_Current_Origin_X());
				s.setY(s.getY()-data.getMap_Current_Origin_Y());
		}
		ArrayList<Cop> Cs = data.getCops();
		for(Cop s:Cs){
				s.setX(s.getX()+data.getMap_Current_Origin_X());
				s.setY(s.getY()+data.getMap_Current_Origin_Y());
			renderSprite(g, s);
				s.setX(s.getX()-data.getMap_Current_Origin_X());
				s.setY(s.getY()-data.getMap_Current_Origin_Y());
		}
		ArrayList<Bandit> Bs = data.getBandits();
		for(Bandit s:Bs){
				s.setX(s.getX()+data.getMap_Current_Origin_X());
				s.setY(s.getY()+data.getMap_Current_Origin_Y());
			renderSprite(g, s);
				s.setX(s.getX()-data.getMap_Current_Origin_X());
				s.setY(s.getY()-data.getMap_Current_Origin_Y());
		}
		Car c = data.gerCar();
		c.setX(c.getX()+data.getMap_Current_Origin_X());
		c.setY(c.getY()+data.getMap_Current_Origin_Y());
		renderSprite(g, c);
		c.setX(c.getX()-data.getMap_Current_Origin_X());
		c.setY(c.getY()-data.getMap_Current_Origin_Y());
	}
	
	private void renderNodes(Graphics2D g2){
		PathXDataModel data = ((PathXDataModel)game.getDataModel());
		ArrayList<Intersect> nodesToRender = data.getCurrentIntersects();
		for(Intersect node:nodesToRender){
			if(node.getId()==0){// 0 for starting
				Image im = data.getStringImage();
				float x = node.getX() + data.getMap_Current_Origin_X() - 32;//the image is
				float y = node.getY() + data.getMap_Current_Origin_Y() - 32;//64 x 64
				g2.drawImage(im, (int)x, (int)y, 64, 64,null);
			}
			else if(node.getId()==1){ // 1 for ending
				Image im = data.getTargetImage();
				float x = node.getX() + data.getMap_Current_Origin_X() - 32;//the image is
				float y = node.getY() + data.getMap_Current_Origin_Y() - 32;//64 x 64
				g2.drawImage(im, (int)x, (int)y, 64, 64,null);
			}else{renderNode(g2, node);} // all the other nodes
		}
	}
	private void renderNode(Graphics2D g2,Intersect node){
		PathXDataModel data = ((PathXDataModel)game.getDataModel());
		Stroke s = new BasicStroke(4);
		g2.setStroke(s);
        recyclableCircle.x = node.getX() + data.getMap_Current_Origin_X() - NODE_RADIUS;
        recyclableCircle.y = node.getY() + data.getMap_Current_Origin_Y() - NODE_RADIUS;
		if(node.isOpen())	g2.setColor(Color.GREEN);//open Node Green
		else 				g2.setColor(Color.RED);// Close Node Red
		g2.fill(recyclableCircle);
		g2.setColor(Color.BLACK);
        g2.draw(recyclableCircle);
		
	}	
	private void renderRoads(Graphics2D g2){
		PathXDataModel data = ((PathXDataModel)game.getDataModel());
		ArrayList<Road> roadsToRender = data.getCurrentRoads();
		for(Road road:roadsToRender){
			renderRoad(g2,road);
		}
		
	}
	private void renderRoad(Graphics2D g2,Road road){
		PathXDataModel data = ((PathXDataModel)game.getDataModel());
		int strokeNum = road.getSpeed()/10;
		if (strokeNum < 1) strokeNum = 1;
        if (strokeNum > 10) strokeNum = 10;
        Stroke s = new BasicStroke(2*strokeNum);
        g2.setStroke(s);
        recyclableLine.x1 = road.getX1() + data.getMap_Current_Origin_X();
        recyclableLine.y1 = road.getY1() + data.getMap_Current_Origin_Y();
        recyclableLine.x2 = road.getX2() + data.getMap_Current_Origin_X();
        recyclableLine.y2 = road.getY2() + data.getMap_Current_Origin_Y();
        g2.setColor(Color.BLACK);
        g2.draw(recyclableLine);
        if(road.isOneWay()){
        	renderOneWaySignalsOnRecyclableLine(g2);
        }
	}
	 private void renderOneWaySignalsOnRecyclableLine(Graphics2D g2)
	    {
	        // CALCULATE THE ROAD LINE SLOPE
	        double diffX = recyclableLine.x2 - recyclableLine.x1;
	        double diffY = recyclableLine.y2 - recyclableLine.y1;
	        double slope = diffY/diffX;
	        
	        // AND THEN FIND THE LINE MIDPOINT
	        double midX = (recyclableLine.x1 + recyclableLine.x2)/2.0;
	        double midY = (recyclableLine.y1 + recyclableLine.y2)/2.0;
	        
	        // GET THE RENDERING TRANSFORM, WE'LL RETORE IT BACK
	        // AT THE END
	        AffineTransform oldAt = g2.getTransform();
	        
	        // CALCULATE THE ROTATION ANGLE
	        double theta = Math.atan(slope);
	        if (recyclableLine.x2 < recyclableLine.x1)
	            theta = (theta + Math.PI);
	        
	        // MAKE A NEW TRANSFORM FOR THIS TRIANGLE AND SET IT
	        // UP WITH WHERE WE WANT TO PLACE IT AND HOW MUCH WE
	        // WANT TO ROTATE IT
	        AffineTransform at = new AffineTransform();        
	        at.setToIdentity();
	        at.translate(midX, midY);
	        at.rotate(theta);
	        g2.setTransform(at);
	        
	        // AND RENDER AS A SOLID TRIANGLE
	        g2.fill(recyclableTriangle);
	        
	        // RESTORE THE OLD TRANSFORM SO EVERYTHING DOESN'T END UP ROTATED 0
	        g2.setTransform(oldAt);
	    }
	 
	private void renderAllSpecial(Graphics g){
		String string;
		for(int i=0;i<16;i++){
			if(game.getGUIButtons().get(SPECIAL_TYPE+i).getState()==PathXTileState.MOUSE_OVER_STATE.toString()){
				string = SPECIAL_NAMES[i];
			}else{
				string = "";
			}
			renderSprite(g, game.getGUIButtons().get(SPECIAL_TYPE+i));
			g.setColor(Color.BLUE);
			g.setFont(SPECIAL_TEXT_DISPLAY);
			g.drawString(string, (int)game.getGUIButtons().get(SPECIAL_TYPE+i).getX(), 
					(int)game.getGUIButtons().get(SPECIAL_TYPE+i).getY());
		}
			
	}
	 
	private void renderSpeedControl(Graphics g){
		renderSprite(g,game.speedControlSprite);
		PathXDataModel data = ((PathXDataModel)game.getDataModel());
		String s = Float.toString(data.getGameSpeed()).substring(0,3);
		g.setColor(Color.BLACK);
		g.setFont(FONT_TEXT_DISPLAY);
		g.drawString(s, 255, 317);
//		g.drawImage(game.speedControlButtonImage,50,50,null);
	}
	private void renderMusicSoundControl(Graphics g){
		Sprite s = game.getGUIButtons().get(MUSIC_CONTROL_TYPE);
		renderSprite(g, s);
		s = game.getGUIButtons().get(SOUND_CONTROL_TYPE);
		renderSprite(g, s);
	}
	 
	private void renderGameMap(Graphics g){
		Sprite map = game.getGUIDecor().get(GAME_MAP_TYPE);
		renderSprite(g, map);
	}
	private void renderMap(Graphics g){
		Sprite map = game.getGUIDecor().get(MAP_TYPE);
		renderSprite(g, map);
	}
	private void renderBackground(Graphics g) {
		// THERE IS ONLY ONE CURRENTLY SET
		Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
		renderSprite(g, bg);
	}
	private void renderCurLevelMoney(Graphics g){
		PathXDataModel data = ((PathXDataModel)game.getDataModel());
		g.setColor(Color.YELLOW);
		g.setFont(FONT_TEXT_DISPLAY);
		g.drawString("FC "+data.getCurrentLevel().getLevelName(), 145, 35);
		g.drawString("$"+Integer.toString(data.gerCurLevelMoney()), 145, 65);
	}
	
	//this method is to render the text on Dialog
	private void renderDialogText(Graphics g){
		String s = dialogText.toString();
		String[] ss = s.split("\n");
		for(int i=0;i<ss.length;i++){
			g.setColor(Color.BLACK);
			g.setFont(FONT_TEXT_DISPLAY);
			g.drawString(ss[i], 120, 75+i*30);
		}
	}
	private void renderDialog(Graphics g){
		Sprite dialog = game.getGUIDialogs().get(INFO_DIALOG_TYPE);
		renderSprite(g, dialog);
		Sprite closeButton = game.getGUIButtons().get(INFO_DIALOG_CLOSE_TYPE);
		renderSprite(g, closeButton);
		Sprite tryButton = game.getGUIButtons().get(TRY_DIALOG_TYPE);
		renderSprite(g, tryButton);
		Sprite leaveButton = game.getGUIButtons().get(LEAVE_DIALOG_TYPE);
		renderSprite(g, leaveButton);
	}
	private void renderSite(Graphics g){
		Collection<Sprite> buttonSprites = game.getGUIButtons().values();
		for (Sprite s : buttonSprites) {
			if(s.getSpriteType().getSpriteTypeID().contains("SITE")){
				renderSprite(g, s);
			}
		}
	}
	private void renderSiteInfo(Graphics g){
		if(game.rightSiteInfo!= -1){
			String s = game.siteInfos.get(game.rightSiteInfo);
			g.setColor(Color.BLUE);
			g.setFont(FONT_TEXT_DISPLAY);
			g.drawString(s, 290, 460);
        }
	}
	private void renderBalance(Graphics g){
		PathXDataModel data = ((PathXDataModel)game.getDataModel());
		g.setColor(Color.BLACK);
		g.setFont(FONT_BALANCE_TEXT_DISPLAY);
		g.drawString("Balance:$"+data.getTotalBalance(), 180, 35);
		g.drawString("Goal:$10,000", 180, 70);
	}
	private void renderGUIControls(Graphics g) {
		// GET EACH DECOR IMAGE ONE AT A TIME
		Collection<Sprite> decorSprites = game.getGUIDecor().values();
		for (Sprite s : decorSprites) {
			if (s.getSpriteType().getSpriteTypeID() != BACKGROUND_TYPE&&
					s.getSpriteType().getSpriteTypeID() != MAP_TYPE&&
					s.getSpriteType().getSpriteTypeID() != GAME_MAP_TYPE)
				renderSprite(g, s);
		}
		// AND NOW RENDER THE BUTTONS
		Collection<Sprite> buttonSprites = game.getGUIButtons().values();
		for (Sprite s : buttonSprites) {
			if(s.getSpriteType().getSpriteTypeID().contains("BUTTON"))
				renderSprite(g, s);
		}
	}
	
	private void renderSprite(Graphics g, Sprite s) {
		// ONLY RENDER THE VISIBLE ONES
		if (!s.getState().equals(PathXTileState.INVISIBLE_STATE.toString())) {
			SpriteType bgST = s.getSpriteType();
//			System.out.println(bgST.getSpriteTypeID()+"  "+ s.getState());
			Image img = bgST.getStateImage(s.getState());
			g.drawImage(img, (int) s.getX(), (int) s.getY(), bgST.getWidth(),bgST.getHeight(), null);
		}
	}
	
}
