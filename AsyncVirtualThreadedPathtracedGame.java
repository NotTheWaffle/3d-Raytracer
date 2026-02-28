import Game.Game;
import Game.Input;
import Math.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;

public class AsyncVirtualThreadedPathtracedGame extends Game{
	
	// this is only for rasterized rendering
	private final float[][] zBuffer;
	// this is distance per 16 ms
	private final float speed = .02f;
	private final float rotSpeed = .03f;
	// these represent the entire model
	private final Viewport camera;
	private final Environment env;

	private boolean raytrace;
	private final Pixel[][] pixelBuffer;

	private List<Thread> threads;

	public AsyncVirtualThreadedPathtracedGame(Viewport camera, Environment env){
		super(camera.screenWidth, camera.screenHeight);

		this.env = env;
		this.camera = camera;
	
		zBuffer = new float[width][height];

		pixelBuffer = new Pixel[height][width];
		for (Pixel[] row : pixelBuffer) {
			for (int x = 0; x < row.length; x++) {
				row[x] = new Pixel();
			}
		}
	}

	@Override
	public String name(){
		return "Pathtraced 3d";
	}
	
	@Override
	public void tick(double dt){
		float relativeSpeed = (float) (this.speed * dt/16.0f);
		float relativeRotSpeed = (float) (this.rotSpeed * dt/16.0f);

		Transform transform = camera;
		if (input.keys['1']) transform = env.physicalObjects.get(1).transform;

		if (input.keys['W']) 			{resetPixelBuffer(); transform.move(0, 0, relativeSpeed);}
		if (input.keys['A']) 			{resetPixelBuffer(); transform.move(-relativeSpeed, 0, 0);}
		if (input.keys['S']) 			{resetPixelBuffer(); transform.move(0, 0, -relativeSpeed);}
		if (input.keys['D']) 			{resetPixelBuffer(); transform.move(relativeSpeed, 0, 0);}
		if (input.keys[' ']) 			{resetPixelBuffer(); transform.move(0, relativeSpeed, 0);}
		if (input.keys[Input.SHIFT]) 	{resetPixelBuffer(); transform.move(0, -relativeSpeed, 0);}

		if (input.keys[Input.UP_ARROW]) 	{resetPixelBuffer(); transform.turnX( relativeRotSpeed);}
		if (input.keys[Input.DOWN_ARROW]) 	{resetPixelBuffer(); transform.turnX(-relativeRotSpeed);}
		if (input.keys[Input.LEFT_ARROW]) 	{resetPixelBuffer(); transform.turnY( relativeRotSpeed);}
		if (input.keys[Input.RIGHT_ARROW]) 	{resetPixelBuffer(); transform.turnY(-relativeRotSpeed);}
		if (input.keys['Q']) 				{resetPixelBuffer(); transform.turnZ(-relativeRotSpeed);}
		if (input.keys['E']) 				{resetPixelBuffer(); transform.turnZ( relativeRotSpeed);}
		
		if (input.keys['[']) {
			if (raytrace){
				resetPixelBuffer();
			} else {
				raytrace = true;
				resetPixelBuffer();
				beginPathtracing(16);
			}
		}
		if (input.keys[']']) {
			raytrace = false;
			stopPathtracing();
		}
	}

	private void resetPixelBuffer(){
		if (!raytrace) return;
		for (Pixel[] row : pixelBuffer) {
			for (Pixel element : row) {
				element.clear();
			}
		}
	}

	
	@Override
	public void generateFrame(){
		if (input.keys['K'] && nextFrame != null){
			try {
				File outputfile = new File("saved.png");
				ImageIO.write(nextFrame, "png", outputfile);
			} catch (IOException e) {
				System.out.println("Failed to save screenshot");
			}
		}
		if (raytrace){
			renderPathtraced();
		} else {
			nextFrame = renderRasterized();
		}
	}

	private BufferedImage renderRasterized(){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		for (int x = 0; x < width; x++) {
			Arrays.fill(zBuffer[x], Float.POSITIVE_INFINITY);
		}
		
		for (PhysicalObject object : env.physicalObjects){
			object.renderRasterized(raster, zBuffer, camera);
		}
		for (PhysicalObject object : env.physicalObjects){
			if (object instanceof Mesh mesh){
			//	mesh.bvh.renderWireframe(raster, zBuffer, camera, 5);
			}
		}
		return image;
	}
	private void renderPathtraced(){
		if (nextFrame == null) nextFrame = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = nextFrame.getRaster();
		for (int y = 0; y < pixelBuffer.length; y++){
			for (int x = 0; x < pixelBuffer[y].length; x++){
				raster.setPixel(x, y, pixelBuffer[y][x].getColor());
			}
		}
	}



	private void beginPathtracing(int threadCount){
		stopPathtracing();
		int partitions = (int) Math.sqrt(threadCount);
		for (int x = 0; x < partitions; x++){
			for (int y = 0; y < partitions; y++){
				final int x_f = x;
				final int y_f = y;
				Thread t = new Thread(() -> {
					Random random = ThreadLocalRandom.current();
					while (!Thread.currentThread().isInterrupted()){
						pathtrace(x_f, y_f, camera.screenWidth, camera.screenHeight, partitions, partitions, random);
					}
				});
				threads.add(t);
			}
		}
		threads.forEach(t -> Thread.startVirtualThread(t));
		System.out.println("Started "+ threads.size()+" virtual threads");
	}
	private void stopPathtracing(){
		if (threads == null) threads = new ArrayList<>();
		if (threads.isEmpty()) return;

		threads.forEach(t -> t.interrupt());
		System.out.println("Dispatched "+threads.size()+" threads");
		threads.clear();
	}

	private void pathtrace(int x1, int y1, int x2, int y2, int dx, int dy, Random random){
		Vec3 origin = camera.translation;
		for (int x = x1; x < x2; x += dx){
			for (int y = y1; y < y2; y += dy){
				Vec3 vector;
				if (camera.focus == 0){
					vector = camera.rot.mul((new Vec3(x-camera.cx, camera.cy-y, camera.focalLength)).normalize());
				} else {
					origin = camera.translation.add(new Vec3((random.nextFloat()-.5f)*camera.focus, (random.nextFloat()-.5f)*camera.focus, (random.nextFloat()-.5f)*camera.focus));
					Vec3 pixelPoint = camera.translation.add(camera.rot.mul(new Vec3(x-camera.cx, camera.cy-y, camera.focalLength).mul(camera.focusDistance/camera.focalLength)));
					vector = pixelPoint.sub(origin).normalize();
				}
				
				float[] col = Ray.trace(origin, vector, env, 10, random);
				
				pixelBuffer[y][x].addSample(col);
			}
		}
	}
}