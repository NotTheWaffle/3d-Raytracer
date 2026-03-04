
import Game.Game;
import Game.Window;
import Math.FloatMath;

//TODO add multi importance sampling
public class Main {
	public static void main(String[] args){	
		int size = 1024;
		Viewport camera = new Viewport(FloatMath.PI*.5f, 0, 0, size, size);
		camera.translate(0, 0, -1);


		Environment env = new Environment(true);
		env.add(MeshLoader.loadObj("Models/cube.obj", new Transform(), 1, Material.SOLID));
		//env.add(new Sphere(1, Material.GLASS));
		env.addFloor();

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