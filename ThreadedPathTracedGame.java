import Game.Game;
import Game.Input;
import Math.*;
import java.awt.Color;
import java.awt.Graphics2D;
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

public class ThreadedPathTracedGame extends Game{
	
	// this is only for rasterized rendering
	private final float[][] zBuffer;
	// this is distance per 16 ms
	public final float speed = .02f;
	public final float rotSpeed = .03f;
	// these represent the entire model
	private final Viewport camera;
	private final Environment env;

	public boolean raytrace = false;
	public final Pixel[][] pixelBuffer;
	
	public static long logicTime;

	public ThreadedPathTracedGame(Viewport camera, Environment env){
		super(camera.screenWidth, camera.screenHeight);
		
		this.env = env;
		this.camera = camera;
		camera.translate(0, 0, -1);
	
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
		return "Raytraced 3d";
	}
	
	@Override
	public void tick(double dt){
		long start = System.nanoTime();
		float relativeSpeed = (float) (this.speed * dt/16.0);
		float relativeRotSpeed = (float) (this.rotSpeed * dt/16.0);

		if (input.keys['W']) 			{resetPixelBuffer(); camera.moveZ( relativeSpeed);}
		if (input.keys['A']) 			{resetPixelBuffer(); camera.moveX(-relativeSpeed);}
		if (input.keys['S']) 			{resetPixelBuffer(); camera.moveZ(-relativeSpeed);}
		if (input.keys['D']) 			{resetPixelBuffer(); camera.moveX( relativeSpeed);}
		if (input.keys[' ']) 			{resetPixelBuffer(); camera.moveY( relativeSpeed);}
		if (input.keys[Input.SHIFT]) 	{resetPixelBuffer(); camera.moveY(-relativeSpeed);}

		if (input.keys[Input.UP_ARROW]) 	{resetPixelBuffer(); camera.turnX( relativeRotSpeed);}
		if (input.keys[Input.DOWN_ARROW]) 	{resetPixelBuffer(); camera.turnX(-relativeRotSpeed);}
		if (input.keys[Input.LEFT_ARROW]) 	{resetPixelBuffer(); camera.turnY( relativeRotSpeed);}
		if (input.keys[Input.RIGHT_ARROW]) 	{resetPixelBuffer(); camera.turnY(-relativeRotSpeed);}
		if (input.keys['Q']) 				{resetPixelBuffer(); camera.turnZ(-relativeRotSpeed);}
		if (input.keys['E']) 				{resetPixelBuffer(); camera.turnZ( relativeRotSpeed);}

		if (input.keys['[']) 	{raytrace = true; resetPixelBuffer();}
		if (input.keys[']']) 	raytrace = false;

		logicTime = System.nanoTime()-start;
	}

	private void clearZBuffer() {
		for (int x = 0; x < width; x++) {
			Arrays.fill(zBuffer[x], Float.POSITIVE_INFINITY);
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

	private BufferedImage renderRasterized(){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		clearZBuffer();
		
		for (PhysicalObject object : env.physicalObjects){
			object.renderRasterized(raster, zBuffer, camera);
		}
		for (PhysicalObject object : env.physicalObjects){
			if (object instanceof Mesh mesh){
				mesh.bvh.renderWireframe(raster, zBuffer, camera);
			}
		}
		return image;
	}

	private BufferedImage renderRaytraced(int threadCount, int samples){
		WritableRaster raster = nextFrame.getRaster();
		int threadSqrt = (int)Math.sqrt(threadCount);
		List<Thread> threads = new ArrayList<>();
		for (int x = 0; x < threadSqrt; x++){
			for (int y = 0; y < threadSqrt; y++){
				final int x_f = x;
				final int y_f = y;
				Thread t = Thread.ofVirtual().factory().newThread(() -> {
					raytraceRange(width*x_f/threadSqrt, height*y_f/threadSqrt, width*(x_f+1)/threadSqrt, height*(y_f+1)/threadSqrt, raster, samples);
				});
				t.start();
				
				threads.add(t);
			}
		}
		try {
			for (Thread t : threads){
				t.join();
			}
		} catch (InterruptedException e){}
		return nextFrame;
	}
	@Override
	public void generateFrame(){
		long renderStart = System.nanoTime();
		if (input.keys['K'] && nextFrame != null){
			try {
				File outputfile = new File("saved.png");
				ImageIO.write(nextFrame, "png", outputfile);
			} catch (IOException e) {}
		}
		if (raytrace){
			nextFrame = renderRaytraced(25, 4);
		} else {
			nextFrame = renderRasterized();
		}
		
		Graphics2D g2d = nextFrame.createGraphics();
		long renderTime = System.nanoTime() - renderStart;
		g2d.setColor(Color.WHITE);
		g2d.drawString("Render (ms):"+renderTime/1_000_000.0,0,20);
		g2d.drawString("Samples: "+pixelBuffer[0][0].samples, 0, 40);
		g2d.drawString(camera.toString(), 0, 60);
	}
	
	private void raytraceRange(int x1, int y1, int x2, int y2, WritableRaster raster, final int samples){
		Random random = ThreadLocalRandom.current();
		Vec3 origin = camera.translation;
		for (int x = x1; x < x2; x += 1){
			for (int y = y1; y < y2; y += 1){
				Vec3 vector;
				if (camera.focus == 0){
					vector = camera.rot.transform((new Vec3(x-camera.cx, camera.cy-y, camera.focalLength)).normalize());
				} else {
					origin = camera.translation.add(new Vec3((random.nextFloat()-.5f)*camera.focus, (random.nextFloat()-.5f)*camera.focus, (random.nextFloat()-.5f)*camera.focus));
					Vec3 pixelPoint = camera.translation.add(camera.rot.transform(new Vec3(x-camera.cx, camera.cy-y, camera.focalLength).mul(camera.focusDistance/camera.focalLength)));
					vector = pixelPoint.sub(origin).normalize();
				}
				
				Pixel pixel = pixelBuffer[y][x];
				int[] color = new int[3];
				
				for (int i = 0; i < samples; i++){
					float[] col = Ray.trace(origin, vector, env, 10, random);
					color[0] += (int) (255.0 * col[0]);
					color[1] += (int) (255.0 * col[1]);
					color[2] += (int) (255.0 * col[2]);
				}

				pixel.addSample(color, samples);
				raster.setPixel(x, y, pixel.getColor());
			}
		}
	}
}