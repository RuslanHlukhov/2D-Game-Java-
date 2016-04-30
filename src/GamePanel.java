import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.awt.event.*;


public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8312014768180584368L;
	public static int WIDTH = 600;
	public static int HEIGHT = 600;
	
	public static enum STATES{
		MENU,
		PLAY
	} 
	public static STATES state = STATES.MENU;
	private Thread thread;
	private boolean running;
	public static Menu menu;
	
	private BufferedImage image;
	private Graphics2D g;
	
	private int FPS=30;
	private double averageFPS;
	public static GameBack background;
	
	public static Player player;String playerName;
	public void setPlayerName(String Name){playerName=Name;}
	public static ArrayList<Bullets> bullets;
	public static ArrayList<Enemy> enemies;
	public static ArrayList<PowerUP> powerups;
	public static ArrayList<Explosion> explosions;
	public static ArrayList<Text> texts;
	
	private long waveStartTimer;
	private long waveStartTimerDiff;
	private int waveNumber;
	private boolean waveStart;
	private int waveDelay=2000;
	
	
	public static boolean leftMouse;
	static int mouseX;
	static int mouseY;
	
	private long slowDownTimer;
	private long slowDownTimerDiff;
	private int slowDownLength=6000;
	
	
	public GamePanel(){
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
	}
	public void addNotify(){
		super.addNotify();
		if(thread==null){
			thread = new Thread();
			thread.start();
		}
		
		
	}
	public void run(){
		running=true;
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		leftMouse = false;
		background = new GameBack();
		player = new Player(playerName, FPS);
		bullets = new ArrayList<Bullets>();
		enemies = new ArrayList<Enemy>();
		powerups = new ArrayList<PowerUP>();
		explosions = new ArrayList<Explosion>();
		texts = new ArrayList<Text>();
		menu = new Menu();
		
		
		waveStartTimer=0;
		waveStartTimerDiff=0;
		waveStart=true;
		waveNumber=0;
		
		long startTime;
		long URDTImeMillist;
		long waitTime;
		long totalTime=0;
		
		int frameCount = 0;
		int maxFrameCount=30;
		
		long targerTime=1000/FPS;
	//Запускаем цикл
			//Рисуем меню
			
	

		while(running){
			
			startTime = System.nanoTime();
			

			if(state.equals(STATES.MENU)){
				background.update();
				background.draw(g);
				menu.update();//Обновление меню
				menu.deaw(g);
				gameDraw();
			}
			if(state.equals(STATES.PLAY)){
			gameUpdate();
			gameRender();
			gameDraw();
			}
			URDTImeMillist = (System.nanoTime()-startTime)/1000000;
			
			waitTime = targerTime - URDTImeMillist;
			
			try{
				Thread.sleep(waitTime);
			}
			catch(Exception e){	
			}
			totalTime +=System.nanoTime()-startTime;
			frameCount++;
			if(frameCount == maxFrameCount){
				setAverageFPS(1000.0/((totalTime/frameCount)/1000000));
				frameCount=0;
				totalTime=0;
			}
		}
		
		g.setColor(new Color(0,100,255));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		String s = "Г Р У    С К І Н Ч Е Н О   -   В И   П О М Е Р Л И";
		int length =(int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, (WIDTH-length) /2,HEIGHT/2);
		s = "Фінальний рахунок: " + player.getScore();
		g.drawString(s, (WIDTH-length) /2 + 75,HEIGHT/2 + 30);
		gameDraw();
		
		}
	
	private void gameUpdate(){
		//новая волна врагов
		if(waveStartTimer==0 && enemies.size()==0){
			waveNumber++;
			waveStart = false;
			waveStartTimer = System.nanoTime();
		}
		else{
			waveStartTimerDiff = (System.nanoTime() - waveStartTimer) / 1000000;
			if(waveStartTimerDiff>waveDelay){
				waveStart = true;
				waveStartTimer = 0;
				waveStartTimerDiff = 0;
			}
		}
		if(waveStart && enemies.size()==0){
			createNewEnemies();
		}
		
		//обновление игрока
		player.update();
		//Обновление пуль
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
			boolean remove = bullets.get(i).remove();//Прошли метод посмотрели выпадает ли у, если выпадает то ремув становит тру
			if(remove){
				bullets.remove(i);//Удаление пули
				i--;
			}
		}
		//обновление врагов
		for(int i=0; i<enemies.size();i++){
			enemies.get(i).update();		
			}
		
		//обновление плюшек
		for (int i = 0; i < powerups.size(); i++) {
			boolean remove = powerups.get(i).update();
			if(remove){
				powerups.remove(i);
				i--;
			}
			
				}
		for(int i=0; i<explosions.size();i++){
			boolean remove = explosions.get(i).update();
			if(remove){
				explosions.remove(i);
				i--;
			}
			
		}
		
		for (int i = 0; i < texts.size(); i++) {
			boolean remove = texts.get(i).update();
			if(remove){
				texts.remove(i);
				i--;
			}
		}
		
		for(int i=0; i<bullets.size();i++){
			
			Bullets b = bullets.get(i);
			double bx = b.getx();
			double by = b.gety();
			double br = b.getr();
			
			for (int j=0;j<enemies.size();j++){
			
			Enemy e = enemies.get(j);	
			double ex = e.getx();
			double ey = e.gety();
			double er = e.getr();
			
			double dx = bx-ex;
			double dy = by-ey;
			double dist = Math.sqrt(dx*dx+dy*dy);
			
			if(dist<br+er){
				e.hit();
				bullets.remove(i);
				i--;
				break;
			}
			}
			
		}
		
		
		for(int i=0; i<enemies.size();i++){
			
			
			if(enemies.get(i).isDead()){
				
			Enemy e = enemies.get(i);
			
			//плюшки игрока
			double rand = Math.random();
			if(rand<0.001) powerups.add(new PowerUP(1, e.getx(), e.gety()));
			else if(rand<0.020) powerups.add(new PowerUP(3, e.getx(), e.gety()));
			else if(rand<0.120) powerups.add(new PowerUP(2, e.getx(),e.gety()));
			else if(rand<0.130) powerups.add(new PowerUP(4, e.getx(),e.gety()));
			
			else powerups.add(new PowerUP(4, e.getx(), e.gety()));
			
			player.addScore(e.getType()+e.getRank());
			enemies.remove(i);
			
			
			i--;
			
			e.explode();
			explosions.add(new Explosion(e.getx(), e.gety(), e.getr(), e.getr()+30));
			}
		}
		//смерь игрока
		if(player.isDead()){
			running= false;
		}
		
		//Жизни игрока
		if(!player.isRecovering()){
			int px = player.getx();
			int py = player.gety();
			int pr = player.getr();
			for(int i=0; i<enemies.size();i++){
				
				Enemy e = enemies.get(i);
				double ex = e.getx();
				double ey = e.gety();
				double er = e.getr();
				
				
						
				double dx = px-ex;
				double dy = py-ey;
				double dist = Math.sqrt(dx*dx + dy*dy);
				
				if(dist<pr+er){
					player.loseLife();
				}
			}
		}
		//Коллекция Плюшек
		int px = player.getx();
		int py = player.gety();
		int pr = player.getr();
		for(int i =0; i<powerups.size(); i++){
			PowerUP p = powerups.get(i);
			double x = p.getx();
			double y = p.gety();
			double r = p.getr();
			double dx = px-x;
			double dy = py-y;
			double dist = Math.sqrt(dx*dx+dy*dy);
			
			
			if(dist < pr+r){
				
				int type = p.getType();
				
				if(type ==1){
					player.gainLife();
					texts.add(new Text(player.getx(), player.gety(), 2000, "Extra Life"));
				}
				if(type==2){
					player.increasePower(1);
					texts.add(new Text(player.getx(), player.gety(), 2000, "Power"));
				}
				if(type==3){
					player.increasePower(2);
					texts.add(new Text(player.getx(), player.gety(), 2000, "Double Power"));
				}
				if(type==4){
					slowDownTimer = System.nanoTime();
					for(int j=0; j<enemies.size();j++){
						enemies.get(j).setSlow(true);
					}
					texts.add(new Text(player.getx(), player.gety(), 2000, "Slow Down"));
				}
				
				powerups.remove(i);
				i--;
			}
		}
		if(slowDownTimer !=0){
			slowDownTimerDiff = (System.nanoTime()-slowDownTimer) /1000000;
		if(slowDownTimerDiff>slowDownLength){
			slowDownTimer=0;
			for(int j=0; j<enemies.size();j++){
				enemies.get(j).setSlow(false);
			}
			}
		}	
	}
	
	private void gameRender(){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		
		if(slowDownTimer !=0){
		g.setColor(new Color(255,255,255,64));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		}
		//Рисование игрока
		player.draw(g);
		//Рисование пуль
		for(int i=0; i<bullets.size();i++){
		 bullets.get(i).draw(g);
		}
		//рисовние врагов
		for(int i=0; i<enemies.size();i++){
			enemies.get(i).draw(g);		
			}
		for (int i = 0; i < powerups.size(); i++) {
			powerups.get(i).draw(g);
		}
		for(int i=0;i<explosions.size();i++){
			explosions.get(i).draw(g);
		}
		
		for (int i = 0; i < texts.size(); i++) {
			texts.get(i).draw(g);
		}
		
		//Puсование номера волны
		if(waveStartTimer!=0){
			g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			String s = "- Х В И Л Я   " + waveNumber + "   -";
			int length =(int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			int alpha = (int) (255 * Math.sin(3.14 * waveStartTimerDiff/waveDelay));
			if(alpha>255) alpha =255;
			g.setColor(new Color (255,255,255,alpha));
			g.drawString(s, WIDTH/2-length/2, HEIGHT/2);
		}
		//Рисуем жизни игрока
		for(int i=0; i<player.getLives();i++){
			g.setColor(Color.WHITE);
			g.fillOval(20+ (20*i), 20, player.getr()*2, player.getr()*2);
			g.setStroke(new BasicStroke(3));
			
			g.setColor(Color.RED.darker());
			g.fillOval(20+ (20*i), 20, player.getr()*2, player.getr()*2);
			g.setStroke(new BasicStroke(1));	
		}
		
		//рисование плюшек
		g.setColor(Color.GREEN);
		g.fillRect(20, 40, player.getPower()*8, 8);
		g.setColor(Color.GREEN.darker());
		g.setStroke(new BasicStroke(2));
		for(int i=0; i< player.getRequiredPower(); i++){
		g.drawRect(20+8*i, 40, 8, 8);
		}		
		g.setStroke(new BasicStroke(1));
		
			
		
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Cothic", Font.PLAIN, 14));
		g.drawString("Рахунок: " + player.getScore(), WIDTH-100, 30);
		g.drawString("Имя: " + player.getName(), WIDTH-200, 30);
		
		if(slowDownTimer !=0){
			g.setColor(Color.WHITE);
			g.drawRect(20, 60, 100, 0);
			g.fillRect(20, 60, (int)(100-100.0*slowDownTimerDiff/slowDownLength), 8);
		}
		
	}
	private void gameDraw(){
		Graphics g2 = this.getGraphics();
		g2.drawImage(image,0,0,null);
		g2.dispose();
	}
	
	private void createNewEnemies() {
		enemies.clear();
		@SuppressWarnings("unused")
		Enemy e;
		
		if(waveNumber==1){
			for(int i=0; i<4;i++){
				enemies.add(new Enemy(1,1));
			}
		}
		if(waveNumber==2){
			for(int i=0; i<8;i++){
				enemies.add(new Enemy(1,1));
			}
	
		}
		if(waveNumber==3){
			for(int i=0; i<8;i++){
				enemies.add(new Enemy(1,1));
			}
			
		}
		if(waveNumber == 4){
			enemies.add(new Enemy(1,3));
			enemies.add(new Enemy(1,4));
			for(int i=0; i<4;i++){
				enemies.add(new Enemy(2,1));
				}
			}
		if(waveNumber==5){
			enemies.add(new Enemy(1,4));
			enemies.add(new Enemy(1,3));
			enemies.add(new Enemy(2,3));
		}
		if(waveNumber==6){
			enemies.add(new Enemy(1,3));
			for(int i=0; i<4;i++){
				enemies.add(new Enemy(2,1));
				enemies.add(new Enemy(3,1));
				}
		}
		if(waveNumber==7){
			enemies.add(new Enemy(1,3));
			enemies.add(new Enemy(2,3));
			enemies.add(new Enemy(3,3));
		}
		if(waveNumber==8){
			enemies.add(new Enemy(1,4));
			enemies.add(new Enemy(2,4));
			enemies.add(new Enemy(3,4));
		}
		if(waveNumber==9){
			running=false;
		}
	}
	public void keyPressed(KeyEvent key) {
		int keyCode = key.getKeyCode();
		if(keyCode==KeyEvent.VK_A){
			player.left = true ;
		}
		
		if(keyCode==KeyEvent.VK_D){
			player.right =true;
		}
		if(keyCode==KeyEvent.VK_W){
			player.up=true;
		}
		if(keyCode==KeyEvent.VK_S){
			player.down=true;
		}
		if(keyCode==KeyEvent.VK_SPACE){
			player.firing=true;
		}
		if(keyCode == KeyEvent.VK_ESCAPE){
			GamePanel.state = GamePanel.STATES.MENU; 
		}
	}
	public void keyReleased(KeyEvent key) {
		int keyCode = key.getKeyCode();
		if(keyCode==KeyEvent.VK_A){
			player.left=false;
		}
		
		if(keyCode==KeyEvent.VK_D){
			player.right=false;
		}
		if(keyCode==KeyEvent.VK_W){
			player.up =false;
		}
		if(keyCode==KeyEvent.VK_S){
			player.down=false;
		}
		if(keyCode==KeyEvent.VK_SPACE){
			player.firing=false;
		}
		
	}
	@Override
	public void keyTyped(KeyEvent key) {
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent key) {
		if(key.getButton()==MouseEvent.BUTTON1){
			GamePanel.player.firing = true;
			GamePanel.leftMouse = true;
			}
		
	}
	@Override
	public void mouseReleased(MouseEvent key) {
		if(key.getButton()==MouseEvent.BUTTON1){
			GamePanel.player.firing = false;
			GamePanel.leftMouse = false;
			
		}
		
	}
	@Override
	public void mouseDragged(MouseEvent key) {
		GamePanel.mouseX = key.getX();
		GamePanel.mouseY= key.getY(); 
		
	}
	@Override
	public void mouseMoved(MouseEvent key) {
		GamePanel.mouseX = key.getX();
		GamePanel.mouseY = key.getY();
		
	}
	public double getAverageFPS() {
		return averageFPS;
	}
	public void setAverageFPS(double averageFPS) {
		this.averageFPS = averageFPS;
	}	
	}


