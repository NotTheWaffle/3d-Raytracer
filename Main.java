
import Game.Game;
import Game.Window;
import java.awt.Color;


public class Main {
	public static void main(String[] args){
		int size = 512;
		
		Environment env = new Environment(true);
	//	env.add(MeshLoader.loadObj("Models/"+"cube"+".obj", new Transform(), 1, Material.GLASS, true));
		env.add(new RectangularPrism(0, 0, 0, 1, 1, 1, Material.glass(Color.WHITE, 1), 0));

		env.addSphereTest();
	//	env.addCornellBox(2, 2.1);

	//	env.add(new Sphere(new Vec3(0, 0, 0), .5, Material.GLASS));

	//	env.addHueSpheres(8, 1);
		Viewport camera = new Viewport(Math.PI*.5, 1, .001, 512, 512);
	//	camera.rotateX(-Math.PI/2);
		
		runGame(new AsyncVirtualThreadedPathtracedGame(camera, env));
	}
	
	public static Thread startGame(final Game game){
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