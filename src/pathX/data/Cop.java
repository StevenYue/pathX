package pathX.data;

import static pathX.PathXConstants.*;

import java.util.ArrayList;
import java.util.Random;

import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;

public class Cop extends Sprite{
	private Level currentLevel;
	private float speed;
	private float initSpeed;
	protected Intersect currentNode;
	protected Intersect targetNode;
	private boolean stopSign;
	private float targetReachedRangeScale = 1;
	private int[] copLoop;
	private boolean cCW;
	private Random random;
	
	public Cop(SpriteType initSpriteType, float initX, float initY,float initVx,
			float initVy, String initState,Level cLevel,int [] cLoop,int startNode) {
		super(initSpriteType, initX, initY, initVx, initVy, initState);
		// TODO Auto-generated constructor stub
		currentLevel = cLevel;
		speed = initVx;
		initSpeed = initVx;
		currentNode = cLevel.allIntersects.get(startNode);
		targetNode = cLevel.allIntersects.get(startNode);
		stopSign = true;
		copLoop = cLoop;
		random = new Random();
	}
//	private ArrayList<Intersect> getAllNodes(){ return currentLevel.allIntersects;}
	
	
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
	
//	private ArrayList<Integer> getAdjacentNodes(int curNodeID){
//		ArrayList<Integer> result = new ArrayList<Integer>();
//		ArrayList<Road> allRoads = currentLevel.getAllRoads();
//		ArrayList<Intersect> allNodes = currentLevel.getAllIntersects();
//		for(Road r:allRoads){
//			if(r.getId1()==curNodeID){
//				if(allNodes.get(r.getId2()).isOpen())
//					result.add(r.getId2());
//			}	
//			if(r.getId2()==curNodeID){
//				if(allNodes.get(r.getId1()).isOpen())
//					if(!r.isOneWay())
//						result.add(r.getId1());
//			}	
//		}
//		return result;
//	}
	//implement this method to let the sprite make decision itself
	private Intersect getTargetNode(Intersect curNode){
		int index = -1;
		cCW = random.nextBoolean();
		for(int i=0;i<copLoop.length;i++){
			if(copLoop[i]==curNode.getId()){
				index = i;
				break;
			}
		}
		if(cCW){ //counter clockwise
			if(index==(copLoop.length-1))
				return currentLevel.allIntersects.get(copLoop[0]);
			else
				return currentLevel.allIntersects.get(copLoop[index+1]);
		}else{//clock wise
			if(index==0)
				return currentLevel.allIntersects.get(copLoop[copLoop.length-1]);
			else
				return currentLevel.allIntersects.get(copLoop[index-1]);
		}
	}
	
    public void update(MiniGame game){
    	if(targetReached(targetNode)&&stopSign){
//    		System.out.println("Fuck");
    		targetNode = getTargetNode(currentNode);
    		moveFromeNodeToNode(currentNode, targetNode);
    	}
    	else{
//    		System.out.println("vX:"+vX+"  vY:"+vY);
    		x += vX;
    		y += vY;
    	}
    }
}

