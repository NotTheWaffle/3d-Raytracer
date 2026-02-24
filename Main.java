
import Game.Game;
import Game.Window;
import java.awt.Color;


public class Main {
	public static void main(String[] args){
		Environment env = new Environment(true);
		Material mat = Material.solid(Color.DARK_GRAY);

		env.add(MeshLoader.loadObj("Models/dragon1mil.obj", new Transform(), 2, mat));

	//	env.addSphereTest();
	//	env.addCornellBox(2, 2.1);
	//	env.addHueSpheres(8, 1);
		env.addFloor();
		
		int size = 256;
		Viewport camera = new Viewport(Math.PI*.5, 1, .005, 512, 512);
		
		runGame(new AsyncVirtualThreadedPathtracedGame(camera, env));
	}
	
	public static Thread startGame(Game game){
		Thread thread = new Thread(() -> runGame(game));
		thread.start();
		return thread;
	}
	public static void runGame(final Game game){
		final Window window = new Window(game);
		long lastTime = System.nanoTime();
		while (true){
			final long now = System.nanoTime();
			final double deltaTime = (now - lastTime) / 1_000_000.0;
			lastTime = now;
			game.tick(deltaTime);
			game.generateFrame();
			window.render();
		}
	}
}