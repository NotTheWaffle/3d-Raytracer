package Game;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Game {
	public final Input input;

	protected BufferedImage nextFrame;

	public final int width;
	public final int height;

	protected Game(int width, int height){
		input = new Input(this);
		this.width = width;
		this.height = height;
	}
	public String name(){
		return "Default";
	}
	public void tick(double dt){
		
	}
	public void generateFrame(){
		
	}
	public final void updateFrame(Graphics2D g2d){
		g2d.drawImage(nextFrame, 0, 0, null);
	}
}