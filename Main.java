
import Game.Game;
import Game.Window;


public class Main {
	public static void main(String[] args){
		int size = 512;
		
		Environment env = new Environment(true);
		Material mat = Material.GLASS;
		
	//	env.add(MeshLoader.loadObj("Models/tf2/"+"engineer"+".obj", new Transform().rotateX(-Math.PI/2).translate(3, 0, 3), 2, mat, true));
	//	env.add(MeshLoader.loadObj("Models/tf2/"+"heavy"+".obj"   , new Transform().rotateX(-Math.PI/2).translate(3, 0, 0), 2, mat, true));
	//	env.add(MeshLoader.loadObj("Models/tf2/"+"demo"+".obj"    , new Transform().rotateX(-Math.PI/2).translate(3, 0, -3), 2, mat, true));
	//	
	//	env.add(MeshLoader.loadObj("Models/tf2/"+"scout"+".obj"   , new Transform().rotateX(-Math.PI/2).translate(0, 0, 3), 2, mat, true));
	//	env.add(MeshLoader.loadObj("Models/tf2/"+"pyro"+".obj"    , new Transform().rotateX(-Math.PI/2).translate(0, 0, 0), 2, mat, true));
	//	env.add(MeshLoader.loadObj("Models/tf2/"+"soldier"+".obj" , new Transform().rotateX(-Math.PI/2).translate(0, 0, -3), 2, mat, true));
	//	
	//	env.add(MeshLoader.loadObj("Models/tf2/"+"spy"+".obj"     , new Transform().rotateX(-Math.PI/2).translate(-3, 0,  3), 2, mat, true));
	//	env.add(MeshLoader.loadObj("Models/tf2/"+"medic"+".obj"   , new Transform().rotateX(-Math.PI/2).translate(-3, 0, 0), 2, mat, true));
	//	env.add(MeshLoader.loadObj("Models/tf2/"+"sniper"+".obj"  , new Transform().rotateX(-Math.PI/2).translate(-3, 0, -3), 2, mat, true));
		
	int max = 5;
		for (int count = 0; count < max; count+=1){
			double theta = 2*Math.PI*count/max;
		//	env.add(MeshLoader.loadObj("Models/tf2/"+"heavy"+".obj"   , new Transform().move(0, 0, 0).rotateX(-Math.PI/2).rotateY(theta), 2, Material.MIRROR, true));
		}
		env.add(MeshLoader.loadObj("Models/dragon60k.obj", new Transform().move(0,0,1), 2, mat));
	//	env.add(MeshLoader.loadObj("Models/tf2/"+"scout"+".obj"   , new Transform().rotateX(-Math.PI/2).translate(0, 0, 3), 2, Material.MIRROR, true));
	//	env.add(MeshLoader.loadObj("Models/tf2/"+"scout"+".obj"   , new Transform().rotateX(-Math.PI/2).rotateY(Math.PI).translate(0, 0, 2.5), 2, Material.MIRROR, true));
	

		env.addSphereTest();
	//	env.addCornellBox(2, 2.1);
	//	env.addHueSpheres(8, 1);
	//	env.add(new RectangularPrism(0, -1.5, 0, 20, 1, 20, Material.SOLID, 0));




		Viewport camera = new Viewport(Math.PI*.5, 1, .005, 1024, 1024);
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