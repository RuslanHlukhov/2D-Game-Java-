import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Menu {
	//Переменные
			private int buttonWidth;
			private int buttonHeight;
			private Color color1;
			private String s;
			private int transp = 0;
			
			
			//Конструктор
			public Menu(){
				buttonWidth = 120;
				buttonHeight = 60;
				
				color1 = Color.white;
				s = "Play";
			}
			//Функции
			public void update(){
				if(GamePanel.mouseX > GamePanel.WIDTH/2 - buttonWidth/2 && 
						GamePanel.mouseX < GamePanel.WIDTH + buttonWidth/2 && 
						GamePanel.mouseY > GamePanel.HEIGHT/2 - buttonHeight/2 &&
						GamePanel.mouseY < GamePanel.HEIGHT/2 + buttonHeight/2){
					transp = 60;
					if(GamePanel.leftMouse){
						GamePanel.state = GamePanel.STATES.PLAY;
					}
				} else{
					transp =0;
				}
			}
			
			public void deaw(Graphics2D g){
				g.setColor(color1);
				g.setStroke(new BasicStroke(3));//Толщина рамки
				g.drawRect(GamePanel.WIDTH/2-buttonWidth/2,
						GamePanel.HEIGHT/2-buttonHeight/2, buttonWidth, buttonHeight);
				g.setColor(new Color(255, 255, 255, transp));
				g.fillRect(GamePanel.WIDTH/2-buttonWidth/2,
						GamePanel.HEIGHT/2-buttonHeight/2, buttonWidth, buttonHeight); 
				g.setStroke(new BasicStroke(1));//Возвращаем толщину рамки
				g.setColor(color1);
				g.setFont(new Font("Consolas", Font.BOLD, 40));
				long leoght = (int)g.getFontMetrics().getStringBounds(s,g).getWidth();
			
				g.drawString(s, (int)(GamePanel.WIDTH/2-leoght/2),
				(int)(GamePanel.HEIGHT/2 + buttonHeight/4));
			}
			
}
