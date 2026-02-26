
import Game.Game;
import Game.Window;


public class Main {
	public static void main(String[] args){
		int size = 256;
		Viewport camera = new Viewport(Math.PI*.5, 1, .005, 256, 256);
		Environment env = new Environment(true);

		float a = 1;
		float b = 4;
		float c = a * b;
		
		env.addFloor();


		env.add(MeshLoader.loadObj("Models/tf2/heavy.obj", new Transform().rotateX(Math.PI/2).move(0, 0, .3), 1, Material.MIRROR));

		env.add(MeshLoader.loadObj("Models/tf2/medic.obj", new Transform().rotateX(Math.PI/2).rotateZ(Math.PI), 1, Material.MIRROR));

	//	env.addSphereTest();
		
		
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