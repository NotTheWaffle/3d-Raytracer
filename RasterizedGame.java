import Game.Game;
import Game.Input;
import Math.Vec3;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class RasterizedGame extends Game{

	private final float[][] zBuffer;

	public final float speed = .02f;
	public final float rotSpeed = .03f;

	private final Viewport camera;

	private final Environment env;
	

	public RasterizedGame(Viewport camera, Environment env){
		super(camera.screenWidth, camera.screenHeight);
		
		this.env = env;
		
		this.camera = camera;

		zBuffer = new float[width][height];
	}

	@Override
	public String name(){
		return "Rasterized 3d";
	}
	public static long logicTime;
	@Override
	public void tick(double dt){
		long start = System.nanoTime();
		float relativeSpeed = (float) (this.speed * dt/16.0);
		float relativeRotSpeed = (float) (this.rotSpeed * dt/16.0);

		if (input.keys['W']) 			{camera.moveZ( relativeSpeed);}
		if (input.keys['A']) 			{camera.moveX(-relativeSpeed);}
		if (input.keys['S']) 			{camera.moveZ(-relativeSpeed);}
		if (input.keys['D']) 			{camera.moveX( relativeSpeed);}
		if (input.keys[' ']) 			{camera.moveY( relativeSpeed);}
		if (input.keys[Input.SHIFT]) 	{camera.moveY(-relativeSpeed);}

		if (input.keys[Input.UP_ARROW]) 	{camera.turnX( relativeRotSpeed);}
		if (input.keys[Input.DOWN_ARROW]) 	{camera.turnX(-relativeRotSpeed);}
		if (input.keys[Input.LEFT_ARROW]) 	{camera.turnY( relativeRotSpeed);}
		if (input.keys[Input.RIGHT_ARROW]) 	{camera.turnY(-relativeRotSpeed);}
		if (input.keys['Q']) 				{camera.turnZ(-relativeRotSpeed);}
		if (input.keys['E']) 				{camera.turnZ( relativeRotSpeed);}

		logicTime = System.nanoTime()-start;
	}

	private void clearZBuffer() {
		for (int x = 0; x < width; x++) {
			Arrays.fill(zBuffer[x], Float.POSITIVE_INFINITY);
		}
	}
	
	@Override
	public void generateFrame(){
		long renderStart = System.nanoTime();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		clearZBuffer();

		
		for (PhysicalObject physicalObjects : env.physicalObjects){
			physicalObjects.renderRasterized(raster, zBuffer, camera);
		}
		new Point(new Vec3(0, 0, 0), 1).render(raster, zBuffer, camera);
		Graphics2D g2d = image.createGraphics();
		long renderTime = System.nanoTime()-renderStart;
		g2d.drawString("Render (ms):"+renderTime/1_000_000.0,0,20);
		g2d.drawString("Logic  (ms):"+logicTime/1_000_000.0,0,40);
	
		g2d.drawString(Math.random()+"", 0, 100);
		g2d.drawString("Cam Pos:"+camera.translation.toString(), 0, 60);
		g2d.drawString("Cam Rot:"+camera.rot.toString(), 0, 80);
	}
}