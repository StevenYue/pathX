package pathX.data;

import static pathX.PathXConstants.*;

import java.util.ArrayList;
import java.util.Collections;

import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;

public class Car extends Sprite{
	private Level currentLevel;
	private Float speed;
	private float initSpeed;
	private Intersect finalNode;
	private Intersect currentNode;
	private Intersect targetNode;
	private boolean stopSign;
	private float targetReachedRangeScale = 1;
	private boolean desReached;
	private int numOfBites;
	private int numOfRobbed;
	public float vvv;

	
	public Car(SpriteType initSpriteType, float initX, float initY,
			float initVx, float initVy, String initState,Level cLevel) {
		super(initSpriteType, initX, initY, initVx, initVy, initState);
		// TODO Auto-generated constructor stub
		currentLevel = cLevel;
		speed = initVx;
		initSpeed = initVx;
		vvv = initVx;
		currentNode = cLevel.getStartLocation();
		targetNode = cLevel.getStartLocation();
		finalNode = cLevel.getStartLocation();
		stopSign = true;
		desReached = true;
		numOfBites = 0;
		
	}	
	/**
	 * Getter and Setter methods
	 */
	public Float getSpeed() {return speed;}
	public void setSpeed(Float speed) {this.speed = speed;}
	public float getInitSpeed() {return initSpeed;}
	public void setInitSpeed(float speed){this.initSpeed = speed;}
	public int getNumOfBites() {
		if(numOfBites>5) return 5;
		else return numOfBites;
	}
	public int getNumOfRobbed(){
		if(numOfRobbed>10) return 10;
		else return numOfRobbed;
	}
	public void increaseNumOfBites() {++this.numOfBites;}
	public void increaseNumOfRobbed() {++this.numOfRobbed;}
	
	
	
	
	public void choseDes(int id){
		if(id!=-1){
//			System.out.println("ID:"+id);
			if(currentLevel.allIntersects.get(id).isOpen()){
				if(currentNode.getId()==targetNode.getId()){ //this means the car 
					finalNode = currentLevel.allIntersects.get(id);//currently stops at a node
					pathFinding(currentNode,id);
					desReached = false;
				}else{//this case means car is on the way
					if(currentNode==currentLevel.allIntersects.get(id)&&
							!isRoadOneWay(currentNode.getId(), targetNode.getId())){
						moveFromeNodeToNode(targetNode, currentNode);
						Intersect temp = currentNode;
						currentNode = targetNode;
						targetNode = temp;
						path.clear();
						finalNode = targetNode;
//						path.add(currentNode.getId());-
//						path.add(targetNode.getId());
//						finalNode = targetNode;
//						desReached = true;
//						stopSign = false;
//						System.out.println("move back");
					}
					if(targetNode==currentLevel.allIntersects.get(id)){
						moveFromeNodeToNode(currentNode, targetNode);
						path.clear();
						finalNode = targetNode;
					}
				}
				
			}
		}
	}
	


	//Let us try to use this DijKstra Algorithm
	//First need several variables
	private float[] disTo; // this array keeps the info of starting node to all the other
	private Road[] edgeTo; // this one keep the latest road that leads to the node
	private IndexMinPQ<Float> pq;
	private ArrayList<Integer> path;
	private void pathFinding(Intersect curNode,int fNodeId){
		int sizeOfNodes = currentLevel.allIntersects.size();
		disTo = new float[sizeOfNodes];
		edgeTo = new Road[sizeOfNodes];
		pq = new IndexMinPQ<Float>(sizeOfNodes);
		path = new ArrayList<Integer>();
		for(int i=0;i<sizeOfNodes;i++)
			disTo[i] = Float.POSITIVE_INFINITY;
		disTo[curNode.getId()] = 0;
		
		pq.insert(curNode.getId(),(float)0);
		while(!pq.isEmpty()){
			int v = pq.delMin();
			ArrayList<Road> roads = getAdjacentRoads(v);
			for(Road r:roads){
				relax(r,v);
			}
		}
		int tempID = fNodeId;
		while(tempID!=curNode.getId()){
			for(Road r:edgeTo){
				if(r!=null){
//					System.out.println(r.getId1()+" "+r.getId2());
					if(r.getId1()==tempID) {
						tempID = r.getId2();
						path.add(r.getId1());
					}
				}
			}	
		}
		path.add(curNode.getId());
		Collections.reverse(path);
		
	}
	
	// Method for DijKastra algorithm
	private void relax(Road r,int fromID){
		int toID;
		if(r.getId1()==fromID) toID = r.getId2();
		else toID = r.getId1();  // first figure out which is to which is from
		if(disTo[toID]>disTo[fromID]+r.getTime()){
			disTo[toID] = disTo[fromID] + r.getTime();
			r.setId1(toID);
			r.setId2(fromID);
			edgeTo[toID] = r;
			if(pq.contains(toID)) pq.decreaseKey(toID, disTo[toID]);
			else pq.insert(toID, disTo[toID]);
		}
	}
	
	private ArrayList<Road> getAdjacentRoads(int curNodeID){
		ArrayList<Road> resultTemp = new ArrayList<Road>();
		ArrayList<Road> allRoads = currentLevel.getAllRoads();
		ArrayList<Intersect> allNodes = currentLevel.getAllIntersects();
		for(Road r:allRoads){
			if(r.getId1()==curNodeID){
				if(allNodes.get(r.getId2()).isOpen())
					resultTemp.add(r);
			}	
			if(r.getId2()==curNodeID){
				if(allNodes.get(r.getId1()).isOpen())
					if(!r.isOneWay())
						resultTemp.add(r);
			}	
		}
		ArrayList<Road> result = new ArrayList<Road>();
		for(Road rr:resultTemp){
			Road rrr = new Road(rr.getX1(),rr.getY1(),rr.getX2(),rr.getY2(),rr.isOneWay(),rr.getSpeed());
			rrr.setId1(rr.getId1());
			rrr.setId2(rr.getId2());
			result.add(rrr);
		}
		return result;
	}
	
	private boolean isRoadOneWay(int id1,int id2){
		ArrayList<Road> allRoads = currentLevel.getAllRoads();
		boolean result = false;
		for(Road r:allRoads){
			if(r.getId1()==id1){
				if(r.getId2()==id2){
					result = r.isOneWay();
					break;
				}
			}
		}
		return result;
	}
	
	private float getRoadSpeed(int id1,int id2){
//		System.out.println("id1:"+id1+" id2:"+id2);
		ArrayList<Road> allRoads = currentLevel.getAllRoads();
		float resultSpeed = 0;
		for(Road r:allRoads){
			if(r.getId1()==id1){
				if(r.getId2()==id2){
					resultSpeed = this.initSpeed*(r.getSpeed()/ROAD_BASE_SPEED);
					break;
				}
			}
			if(r.getId2()==id1){
				if(r.getId1()==id2){
					resultSpeed = this.initSpeed*(r.getSpeed()/ROAD_BASE_SPEED);
					break;
				}
			}
		}
		targetReachedRangeScale = resultSpeed/initSpeed;
		return resultSpeed;
	}
	private void moveFromeNodeToNode(Intersect in1,Intersect in2){
		speed = getRoadSpeed(in1.getId(), in2.getId());
		
		float length = (float) Math.sqrt(Math.pow(in2.getY()-in1.getY(),2) + Math.pow(in2.getX()-in1.getX(),2));
		float sin = (in2.getY()-in1.getY())/length;
		float cos = (in2.getX()-in1.getX())/length;
//		System.out.println("Legth:"+length+" sin:"+sin+" cos:"+cos);
		vX = speed*cos;
		vY = speed*sin;
//		System.out.println("vX"+vX+" vY"+vY);
		stopSign = false;
	}
	
	public boolean targetReached(Intersect target){
//		System.out.println("XX:"+x+"  YY:"+y);
		if(Math.abs(target.getX()-(x+CB_RADIUS))<(initSpeed*targetReachedRangeScale)&& 
				Math.abs(target.getY()-(y+CB_RADIUS))<(initSpeed*targetReachedRangeScale)){
			this.x = target.getX()-CB_RADIUS;
			this.y = target.getY()-CB_RADIUS;
			stopSign = true;
			currentNode = target;
			if(target == finalNode) desReached = true;
			return true;
		}else
			return false;
	}
	
	
	//implement this method to let the sprite make decision itself
	private Intersect getTargetNode(Intersect curNode){
		int index = -1;
		for(int i=0;i<path.size();i++){
			if(path.get(i)==curNode.getId()){
				index = i;
				break;
			}
		}
		desReached = true;
		if(curNode.getId()==path.get(path.size()-1)) 
			return currentLevel.allIntersects.get(path.get(index));
		else	
			return currentLevel.allIntersects.get(path.get(index+1));
	}
	
    public void update(MiniGame game){
    	if(targetReached(finalNode)&&desReached){		
//    		System.out.println("Des:"+finalNode.getId());
    	}else{
        	if((targetReached(targetNode)&&stopSign)||!desReached){
//        		System.out.println("Fuck");
        		targetNode = getTargetNode(currentNode);
//        		System.out.println(currentNode.getId());
//        		System.out.println(targetNode.getId());
        		moveFromeNodeToNode(currentNode, targetNode);
        	}
        	else{
//        		System.out.println("vX:"+vX+" vY:"+vY);
        		x += vX;
        		y += vY;
        	}
    	}
    }
    
    
    private boolean SpritesNotOntheSameRoad(Sprite s){
    	switch(s.getSpriteType().getSpriteTypeID()){
    	case XILUO_TYPE:
    		if(currentNode.getId()!=targetNode.getId()&&((Xiluo)s).currentNode.getId()!=((Xiluo)s).targetNode.getId())
    			if(!((currentNode.getId()==((Xiluo)s).currentNode.getId()&&
    				targetNode.getId()==((Xiluo)s).targetNode.getId())||
    				(currentNode.getId()==((Xiluo)s).targetNode.getId()&&
    				targetNode.getId()==((Xiluo)s).currentNode.getId())))
    				return true;break;
    	case COP_TYPE:
    		if(currentNode.getId()!=targetNode.getId()&&((Cop)s).currentNode.getId()!=((Cop)s).targetNode.getId())
    			if(!((currentNode.getId()==((Cop)s).currentNode.getId()&&
    				targetNode.getId()==((Cop)s).targetNode.getId())||
    				(currentNode.getId()==((Cop)s).targetNode.getId()&&
    				targetNode.getId()==((Cop)s).currentNode.getId()))) 
    				return true;break;
    	case BANDIT_TYPE:
    		if(currentNode.getId()!=targetNode.getId()&&((Bandit)s).currentNode.getId()!=((Bandit)s).targetNode.getId())
    			if(!((currentNode.getId()==((Bandit)s).currentNode.getId()&&
    			targetNode.getId()==((Bandit)s).targetNode.getId())||
    			(currentNode.getId()==((Bandit)s).targetNode.getId()&&
    			targetNode.getId()==((Bandit)s).currentNode.getId())))
    				return true;break;
    	default:break;  	
    	}
    	return false;
    }
    
    public float calculateDistanceToSprite(Sprite targetSprite)
    {
    	if(!SpritesNotOntheSameRoad(targetSprite)){
    		  float targetSpriteCenterX = targetSprite.getX() + targetSprite.getAABBx() 
    	        		+ (targetSprite.getAABBwidth() / 2);
    	        float targetSpriteCenterY = targetSprite.getY() + targetSprite.getAABBy()
    	        		+ (targetSprite.getAABBheight() / 2);

    	        float centerX = x + aabbX + (aabbWidth / 2);
    	        float centerY = y + aabbY + (aabbHeight / 2);

    	        float deltaX = targetSpriteCenterX - centerX;
    	        float deltaY = targetSpriteCenterY - centerY;

    	        return (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY))-
    	        		(aabbWidth / 2) - (targetSprite.getAABBwidth() / 2);
    	}
    	else return Float.MAX_VALUE;
    }
    
}


