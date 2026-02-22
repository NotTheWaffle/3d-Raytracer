import Game.Game;
import Game.Input;
import Math.Vec3;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class RasterizedGame extends Game{

	private final double[][] zBuffer;

	public final double speed = .02;
	public final double rotSpeed = .03;

	private final Viewport camera;

	private final Environment env;
	

	public RasterizedGame(Viewport camera, Environment env){
		super(camera.screenWidth, camera.screenHeight);
		
		this.env = env;
		
		this.camera = camera;

		zBuffer = new double[width][height];
	}

	@Override
	public String name(){
		return "Rasterized 3d";
	}
	public static long logicTime;
	@Override
	public void tick(double dt){
		long start = System.nanoTime();
		double relativeSpeed = this.speed * dt/16.0;
		double relativeRotSpeed = this.rotSpeed * dt/16.0;

		if (input.keys['W']) 			{camera.translateZ( relativeSpeed);}
		if (input.keys['A']) 			{camera.translateX(-relativeSpeed);}
		if (input.keys['S']) 			{camera.translateZ(-relativeSpeed);}
		if (input.keys['D']) 			{camera.translateX( relativeSpeed);}
		if (input.keys[' ']) 			{camera.translateY( relativeSpeed);}
		if (input.keys[Input.SHIFT]) 	{camera.translateY(-relativeSpeed);}

		if (input.keys[Input.UP_ARROW]) 	{camera.rotateX( relativeRotSpeed);}
		if (input.keys[Input.DOWN_ARROW]) 	{camera.rotateX(-relativeRotSpeed);}
		if (input.keys[Input.LEFT_ARROW]) 	{camera.rotateY( relativeRotSpeed);}
		if (input.keys[Input.RIGHT_ARROW]) 	{camera.rotateY(-relativeRotSpeed);}
		if (input.keys['Q']) 				{camera.rotateZ(-relativeRotSpeed);}
		if (input.keys['E']) 				{camera.rotateZ( relativeRotSpeed);}

		logicTime = System.nanoTime()-start;
	}

	private void clearZBuffer() {
		for (int x = 0; x < width; x++) {
			Arrays.fill(zBuffer[x], Double.POSITIVE_INFINITY);
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
		g2d.drawString("Cam Pos:"+camera.transform.translation.toString(), 0, 60);
		g2d.drawString("Cam Rot:"+camera.transform.rot.toString(), 0, 80);
	}
}