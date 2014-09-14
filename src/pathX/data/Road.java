package pathX.data;

public class Road {
	private int x1,y1;
	private int x2,y2;
	private int id1,id2;
	private boolean oneWay;
	private int speed;
	private float time;
	public Road(){}
	public Road(int x1,int y1,int x2,int y2,boolean oneway,int speed){
		this.x1 = x1;this.y1 = y1;
		this.x2 = x2;this.y2 = y2;
		this.oneWay = oneway;
		this.speed = speed;
		float dis1 = (float) (Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
		this.time = dis1/speed;
	}
	public float getTime(){return time;}
	public int getId1() {
		return id1;
	}
	public void setId1(int id1) {
		this.id1 = id1;
	}
	public int getId2() {
		return id2;
	}
	public void setId2(int id2) {
		this.id2 = id2;
	}
	public int getX1() {
		return x1;
	}
	public void setX1(int x1) {
		this.x1 = x1;
	}
	public int getY1() {
		return y1;
	}
	public void setY1(int y1) {
		this.y1 = y1;
	}
	public int getX2() {
		return x2;
	}
	public void setX2(int x2) {
		this.x2 = x2;
	}
	public int getY2() {
		return y2;
	}
	public void setY2(int y2) {
		this.y2 = y2;
	}
	public boolean isOneWay() {
		return oneWay;
	}
	public void setOneWay(boolean oneWay) {
		this.oneWay = oneWay;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
}
