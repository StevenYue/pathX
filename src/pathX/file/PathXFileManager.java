package pathX.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import pathX.data.Intersect;
import pathX.data.Level;
import pathX.data.Road;
import pathX.ui.PathXMiniGame;
import xml_utilities.XMLUtilities;
import static pathX.PathXConstants.*;

public class PathXFileManager {
	public static String NEW_LINE = "\n";
	PathXMiniGame game;
    private XMLUtilities xmlUtil;
	
	
	public PathXFileManager(PathXMiniGame initGame){
		game = initGame;
		xmlUtil = new XMLUtilities();
	}

	public String loadTextFile(String filePath) throws IOException{
        String textToReturn = "";
        // OPEN A STREAM TO READ THE TEXT FILE
        FileReader fr = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(fr);
        // READ THE FILE, ONE LINE OF TEXT AT A TIME
        String inputLine = reader.readLine();
        while (inputLine != null){
            // APPEND EACH LINE TO THE STRING
            textToReturn += inputLine + NEW_LINE;
            // READ THE NEXT LINE
            inputLine = reader.readLine();
        }
        // RETURN THE TEXT
        return textToReturn; 
	}
	
    public Level loadLevel(File levelFile, File levelSchema){
    	Level level = new Level();
    	try{
            // FIRST LOAD ALL THE XML INTO A TREE
            Document doc = xmlUtil.loadXMLDocument(levelFile.getAbsolutePath(), 
                                                    levelSchema.getAbsolutePath());
            // FIRST LOAD THE LEVEL INFO
            Node levelNode = doc.getElementsByTagName(LEVEL_NODE).item(0);
            NamedNodeMap attributes = levelNode.getAttributes();
            String levelName = attributes.getNamedItem(NAME_ATT).getNodeValue();
            String bgImageName = attributes.getNamedItem(IMAGE_ATT).getNodeValue();
            level.setLevelBGImageName(bgImageName);
            level.setLevelName(levelName);
            // THEN LET'S LOAD THE LIST OF ALL THE REGIONS
            level.allIntersects = loadIntersectionsList(doc);
            ArrayList<Intersect> intersections = level.allIntersects;
            
            // AND NOW CONNECT ALL THE REGIONS TO EACH OTHER
            level.allRoads = loadRoadsList(doc, level);
            
            // LOAD THE START INTERSECTION
            Node startIntNode = doc.getElementsByTagName(START_INTERSECTION_NODE).item(0);
            attributes = startIntNode.getAttributes();
            String startIdText = attributes.getNamedItem(ID_ATT).getNodeValue();
            int startId = Integer.parseInt(startIdText);
            String startImageName = attributes.getNamedItem(IMAGE_ATT).getNodeValue();
            level.setStartLocation(intersections.get(startId));
            level.setLevelImageName(startImageName);
   
            
            // LOAD THE DESTINATION
            Node destIntNode = doc.getElementsByTagName(DESTINATION_INTERSECTION_NODE).item(0);
            attributes = destIntNode.getAttributes();
            String destIdText = attributes.getNamedItem(ID_ATT).getNodeValue();
            int destId = Integer.parseInt(destIdText);
            level.setEndLocation(intersections.get(destId));
            
            // LOAD THE MONEY
            Node moneyNode = doc.getElementsByTagName(MONEY_NODE).item(0);
            attributes = moneyNode.getAttributes();
            String moneyText = attributes.getNamedItem(AMOUNT_ATT).getNodeValue();
            int money = Integer.parseInt(moneyText);
            level.setPayRoll(money);
            
            
            // LOAD THE NUMBER OF POLICE and position of these Cops
            Node policeNode = doc.getElementsByTagName(POLICE_NODE).item(0);
            attributes = policeNode.getAttributes();
            String policeText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numPolice = Integer.parseInt(policeText);
            level.setNumOfP(numPolice);
            String policePosText = attributes.getNamedItem(POS_ATT).getNodeValue();
            String[] sS = policePosText.split(",");
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for(String s:sS){
            	temp.add(Integer.parseInt(s));
            }
            level.copStartLocations = temp;
            
            // LOAD THE NUMBER OF BANDITS
            Node banditsNode = doc.getElementsByTagName(BANDITS_NODE).item(0);
            attributes = banditsNode.getAttributes();
            String banditsText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numBandits = Integer.parseInt(banditsText);
            level.setNumOfB(numBandits);
            
            // LOAD THE NUMBER OF ZOMBIES
            Node zombiesNode = doc.getElementsByTagName(ZOMBIES_NODE).item(0);
            attributes = zombiesNode.getAttributes();
            String zombiesText = attributes.getNamedItem(NUM_ATT).getNodeValue();
            int numZombies = Integer.parseInt(zombiesText);
            level.setNumOfC(numZombies);            
        }
        catch(Exception e){}
        // LEVEL LOADED PROPERLY
        return level;
    }
    
    // PRIVATE HELPER METHOD FOR LOADING INTERSECTIONS INTO OUR LEVEL
    private ArrayList<Intersect> loadIntersectionsList(Document doc){
        // FIRST GET THE REGIONS LIST
        Node intersectionsListNode = doc.getElementsByTagName(INTERSECTIONS_NODE).item(0);
        ArrayList<Intersect> intersections = new ArrayList<Intersect>();
        
        // AND THEN GO THROUGH AND ADD ALL THE LISTED REGIONS
        ArrayList<Node> intersectionsList = xmlUtil.getChildNodesWithName(intersectionsListNode, INTERSECTION_NODE);
        for (int i = 0; i < intersectionsList.size(); i++){
            // GET THEIR DATA FROM THE DOC
            Node intersectionNode = intersectionsList.get(i);
            NamedNodeMap intersectionAttributes = intersectionNode.getAttributes();
            String idText = intersectionAttributes.getNamedItem(ID_ATT).getNodeValue();
            String openText = intersectionAttributes.getNamedItem(OPEN_ATT).getNodeValue();
            String xText = intersectionAttributes.getNamedItem(X_ATT).getNodeValue();
            String yText = intersectionAttributes.getNamedItem(Y_ATT).getNodeValue();
            int id = Integer.parseInt(idText);
            boolean oneWay = Boolean.parseBoolean(openText);
            int x = Integer.parseInt(xText);
            int y = Integer.parseInt(yText);
            // NOW MAKE AND ADD THE INTERSECTION
            Intersect newIntersect = new Intersect(id,oneWay, x, y);
            intersections.add(newIntersect);
        }
        return intersections;
    }

    // PRIVATE HELPER METHOD FOR LOADING ROADS INTO OUR LEVEL
    private ArrayList<Road> loadRoadsList(Document doc, Level level){
        // FIRST GET THE REGIONS LIST
        Node roadsListNode = doc.getElementsByTagName(ROADS_NODE).item(0);
        ArrayList<Road> roads = new ArrayList<Road>();
        ArrayList<Intersect> nodes = level.allIntersects;
        
        // AND THEN GO THROUGH AND ADD ALL THE LISTED REGIONS
        ArrayList<Node> roadsList = xmlUtil.getChildNodesWithName(roadsListNode, ROAD_NODE);
        for (int i = 0; i < roadsList.size(); i++){
            // GET THEIR DATA FROM THE DOC
            Node roadNode = roadsList.get(i);
            NamedNodeMap roadAttributes = roadNode.getAttributes();
            String id1Text = roadAttributes.getNamedItem(INT_ID1_ATT).getNodeValue();
            int int_id1 = Integer.parseInt(id1Text);
            String id2Text = roadAttributes.getNamedItem(INT_ID2_ATT).getNodeValue();
            int int_id2 = Integer.parseInt(id2Text);
            String oneWayText = roadAttributes.getNamedItem(ONE_WAY_ATT).getNodeValue();
            boolean oneWay = Boolean.parseBoolean(oneWayText);
            String speedLimitText = roadAttributes.getNamedItem(SPEED_LIMIT_ATT).getNodeValue();
            int speedLimit = Integer.parseInt(speedLimitText);
            // NOW MAKE AND ADD THE ROAD
            int x1 = nodes.get(int_id1).getX();
            int y1 = nodes.get(int_id1).getY();
            int x2 = nodes.get(int_id2).getX();
            int y2 = nodes.get(int_id2).getY();
            Road newRoad = new Road(x1,y1,x2,y2,oneWay, speedLimit);
            newRoad.setId1(int_id1);
            newRoad.setId2(int_id2);
            roads.add(newRoad);
        }
        return roads;
    }
}
