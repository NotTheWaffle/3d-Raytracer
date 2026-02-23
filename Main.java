
import Game.Game;
import Game.Window;
import Math.Vec3;


public class Main {
	public static void main(String[] args){
		int size = 512;
		
		Environment env = new Environment(true);
	//	env.add(MeshLoader.loadObj("Models/"+"cube"+".obj", new Transform(), 2, Material.glass(Color.WHITE, 1.5), true));
	//	env.add(new RectangularPrism(0, 0, 0, 1, 1, 1, Material.glass(Color.WHITE, 1.5), 0));
		env.add(new Sphere(new Vec3(0, 0, 0), 1, Material.GLASS));


	//	env.addSphereTest();
	//	env.addCornellBox(2, 2.1);
	//	env.addHueSpheres(8, 1);
		env.add(new RectangularPrism(0, -1.5, 0, 20, 1, 20, Material.SOLID, 0));




		Viewport camera = new Viewport(Math.PI*.5, 1, .005, 512, 512);
	//	camera.rotateX(-Math.PI/2);
		
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