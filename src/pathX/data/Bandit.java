package pathX.data;

import static pathX.PathXConstants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;



import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;

public class Bandit extends Sprite{
	private Level currentLevel;
	private float speed;
	private float initSpeed;
	private Intersect finalNode;
	protected Intersect currentNode;
	protected Intersect targetNode;
	private boolean stopSign;
	private float targetReachedRangeScale = 1;
	private Random random;
	private boolean collidable;

	
	public Bandit(SpriteType initSpriteType, float initX, float initY,
			float initVx, float initVy, String initState,Level cLevel) {
		super(initSpriteType, initX, initY, initVx, initVy, initState);
		// TODO Auto-generated constructor stub
		currentLevel = cLevel;
		speed = initVx;
		initSpeed = initVx;
		currentNode = cLevel.getEndLocation();
		targetNode = cLevel.getEndLocation();
		finalNode = cLevel.getEndLocation();
		stopSign = true;
		random = new Random();
		collidable = true;
//		pickUpDes(currentNode);
	}	
	
	
	
	
	public boolean isCollidable() {	return collidable;}
	public void setCollidable(boolean collidable) {	this.collidable = collidable;}




	private Integer[][] disToAllNodes(Intersect curNode){ // first column distance
		ArrayList<Intersect> allNodes = currentLevel.allIntersects;
		Integer[][] result = new Integer[allNodes.size()][2]; 
		for(int i=0;i<allNodes.size();i++){
			if(allNodes.get(i).isOpen()){
				int num = (int) (Math.pow(curNode.getX()-allNodes.get(i).getX(), 2)+
						Math.pow(curNode.getY()-allNodes.get(i).getY(),2));
				result[i][0] = num; 
				result[i][1] = allNodes.get(i).getId(); 
			}else{
				result[i][0] = -1; 
				result[i][1] = allNodes.get(i).getId(); 
			}
		}
		Arrays.sort(result, new Comparator<Integer[]>() {
			public int compare(Integer[] o1, Integer[] o2) {
				return Integer.compare(o2[0], o1[0]);
			}
		});
		return result;
	}
	private Intersect pickUpDes(Intersect curNode){
		int num = random.nextInt(3);
		Integer[][] disS = disToAllNodes(curNode);
		int nodeID = disS[num][1]; // second column is Node ID
		finalNode = currentLevel.allIntersects.get(nodeID);
		pathFinding(curNode,nodeID);
		return finalNode;	
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
//			System.out.println("why Second!!!");
//			System.out.println("FinalNode Id:"+fNodeId);
//			System.out.println("CurrentNode Id:"+curNode.getId());
			for(Road r:edgeTo){
				if(r!=null){
//					System.out.println(r.getId1()+"   "+r.getId2());
					if(r.getId1()==tempID) {
						tempID = r.getId2();
						path.add(r.getId1());
					}
				}
			}	
		}
		path.add(curNode.getId());
		Collections.reverse(path);
//		for(int i=0;i<path.size();i++)
//			System.out.println(path.get(i));
		
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
	
	private boolean targetReached(Intersect target){
//		System.out.println("XX:"+x+"  YY:"+y);
		if(Math.abs(target.getX()-(x+CB_RADIUS))<(initSpeed*targetReachedRangeScale)&& 
				Math.abs(target.getY()-(y+CB_RADIUS))<(initSpeed*targetReachedRangeScale)){
			this.x = target.getX()-CB_RADIUS;
			this.y = target.getY()-CB_RADIUS;
			currentNode = target;
			stopSign = true;
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
			return currentLevel.allIntersects.get(path.get(index+1));
	}
	
    public void update(MiniGame game){
    	if(targetReached(finalNode)){
    		finalNode = pickUpDes(currentNode);
//    		System.out.println("Des:"+finalNode.getId());
    	}else{
        	if(targetReached(targetNode)&&stopSign){
        		targetNode = getTargetNode(currentNode);
//        		System.out.println(targetNode.getId());
        		moveFromeNodeToNode(currentNode, targetNode);
        	}
        	else{
        		x += vX;
        		y += vY;
        	}
    	}
    }
}

