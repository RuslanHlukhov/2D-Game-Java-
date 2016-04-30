import java.awt.Color;
import java.awt.Graphics2D;

public class Bullets {
	private double x;
	private double y;
	private int r;
	
	private double dx;
	private double dy;
	private double rad;
	
	private Color color1;
	private int speed;
	
	public Bullets(double angle, double x2, double y2){
		this.x=x2;
		this.y=y2;
		r=3;
		
		rad=Math.toRadians(angle);
		setDx(Math.cos(rad));
		setDy(Math.sin(rad));
		
		color1=Color.YELLOW;
		
		speed=10;
	}
	public boolean remove(){
		if(y<0){
			return true;
		}
		return false;
	}
	
	public double getx(){return x;}
	public double gety(){return y;}
	public double getr(){return r;}
	
	 public void update(){
		 y -= speed;
	 }
	 
	 public void draw(Graphics2D g){
		 g.setColor(color1);
		 g.fillOval((int)x, (int) y, r, 2*r);
	 }
	public double getDx() {
		return dx;
	}
	public void setDx(double dx) {
		this.dx = dx;
	}
	public double getDy() {
		return dy;
	}
	public void setDy(double dy) {
		this.dy = dy;
	}
	
}
