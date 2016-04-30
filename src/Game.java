
public class Game {
	private int score;
	private String name;
	
	public Game(int score, String name){
		this.score = score;
		this.name = name;
	}
	public int Score(){
		return score;
	}
	public String Name(){
		return name;
	}
	public void setScore(int score){
		this.score=score;
	}
	public void setName(String name){
		this.name=name;
	}
}
