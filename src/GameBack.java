import java.awt.Color;
import java.awt.Graphics2D;

public class GameBack {
	private Color color;
	//Конструктор
	public GameBack(){
		color= Color.BLACK;
	}
	//функции
	public void update(){
		
	}
	public void draw(Graphics2D g){
		g.setColor(color);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
	}
}
