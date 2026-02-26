
import Game.Game;
import Game.Window;
import Math.FloatMath;
import java.awt.Color;


public class Main {
	public static void main(String[] args){
		int size = 256;
		Viewport camera = new Viewport(FloatMath.PI*.5f, 1, .005f, 512, 512);
		Environment env = new Environment(true);

		env.addFloor();

		env.add(MeshLoader.loadObj("Models/open_cube.obj", new Transform(), 1, Material.solid(Color.darkGray)));
	//	env.add(new RectangularPrism(0, 0, 0, 1, 1, 1, Material.GLASS, 0));
	//	env.add(new Sphere(new Vec3(0, 0, 0), .2f, Material.LIGHT));
		
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