package pathX.data;

public class Intersect {
	private int id;
	private boolean open;
	private int x;
	private int y;
	private boolean inVisible;
	public Intersect(int id,boolean open,int x,int y){
		this.id = id;
		this.open = open;
		this.x = x;
		this.y = y;
		this.inVisible = true;
	}
	
	
	public boolean isInVisible() {
		return inVisible;
	}


	public void setInVisible(boolean inVisible) {
		this.inVisible = inVisible;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
}
