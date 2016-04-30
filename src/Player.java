import java.awt.*;
public class Player {
	private int x;
	private int y;
	private int r;
	
	private int dx;//Коефициєнт смещения
	private int dy;//Коефициєнт смещения
	
	private int speed;
	
	boolean left;
	boolean right;
	boolean up;
	boolean down;
	
	public  boolean firing;
	public long firingTime;
	public long firingDelay;
	
	public boolean recovering;
	public long recoveryTimer;
	
	
	private int lives;
	private Color color1;
	private Color color2;
	
	private int score;
	private int type;
	private int rank;
	public  String name;
	
	private int powerLevel;
	private int power;
	private int [] requiredPower = {1,2,3,4,5};
	
	public Player(String Name,int Score){
		x = GamePanel.WIDTH/2;
		y = GamePanel.HEIGHT/2;
		name=Name;
		r = 7;
		
		speed=7;
		
		dx=0;
		dy=0;
		
		lives = 3;
		
		color1 = Color.WHITE;
		color2 = Color.RED;
		
		firing = false;
		firingTime = System.nanoTime();
		firingDelay = 200;
		
		recovering =false;
		recoveryTimer = 0;
		
		score=0;
}
	//Функции
	public int getx(){return x;}
	public int gety(){return y;}
	public int getr(){return r;}
	
	public String getName(){return name;}
	
	public int getScore(){return score;}
	
	public int getLives(){return lives;}
	
	
	public boolean isDead(){return lives<=0;}
	public boolean isRecovering(){return recovering;}
	
	public void setLeft(boolean b){left=b;}
	public void setRight(boolean b){right=b;}
	public void setUp(boolean b){up=b;}
	public void setDown(boolean b){down=b;}
	public void setName(String name){this.name=name;}
	
	public void setFiring(boolean b){firing=b;}
	public void addScore(int i){score +=i;}
	
	public void gainLife(){
		lives++;
	}
	
	public void loseLife(){
		lives--;
		recovering = true;
		recoveryTimer = System.nanoTime();
	}
	
	public void increasePower(int i){
		power +=i;
		if(powerLevel==4){
			if(power>requiredPower[powerLevel]){
				power = requiredPower[powerLevel];
			}
			return;
		}
		if(power>= requiredPower[powerLevel]){
			power-= requiredPower [powerLevel];
			powerLevel++;
		}
	}
	
	public int getPowerLevel(){return powerLevel;}
	public int getPower(){return power;}
	public int getRequiredPower(){return requiredPower[powerLevel];}
	
	public void update(){
		if(left){
			dx=-speed;
		}
		if(right){
			dx= speed;
		}
		if(up){
		dy=-speed;	
		}
		if(down){
			dy=speed;
		}
		x +=dx;
		y+=dy;
		
		if(x<r) x=r;
		if(y<r) y=r;
		if(x>GamePanel.WIDTH-r) x = GamePanel.WIDTH-r;
		if(y>GamePanel.HEIGHT-r) y = GamePanel.HEIGHT-r;
		
		dx=0;
		dy=0;
		
		if(firing){
			long elapsed = (System.nanoTime()-firingTime)/1000000;
			
			if(elapsed>firingDelay){
				
				firingTime = System.nanoTime();
				
				
				if(powerLevel<2){
					GamePanel.bullets.add(new Bullets(270, x,y));
				}
				else if(powerLevel<4){
					GamePanel.bullets.add(new Bullets(270, x+5,y));
					GamePanel.bullets.add(new Bullets(270, x-5,y));
				}
				else{
					GamePanel.bullets.add(new Bullets(270, x,y));
					GamePanel.bullets.add(new Bullets(275, x+5,y));
					GamePanel.bullets.add(new Bullets(265, x-5,y));
				}
				
			}
		}
		if(recovering){
		
		long elapsed = (System.nanoTime() - recoveryTimer) / 1000000;
		if(elapsed>2000){
			recovering = false;
			recoveryTimer=0;
		}
		}
	}
	public void draw(Graphics2D g){
		if(recovering){
			g.setColor(color2);
			g.fillOval((int)(x -r),(int )(y-r), 2*r, 2*r);
			
			g.setStroke(new BasicStroke(3));
			g.setColor(color2.darker());
			g.drawOval((int)(x -r),(int )(y-r), 2*r, 2*r);
			g.setStroke(new BasicStroke(1));
		}
		else{
		g.setColor(color1);
		g.fillOval((int)(x -r),(int )(y-r), 2*r, 2*r);
		
		g.setStroke(new BasicStroke(3));
		g.setColor(color1.darker());
		g.drawOval((int)(x -r),(int )(y-r), 2*r, 2*r);
		g.setStroke(new BasicStroke(1));
		}
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}

	
}
