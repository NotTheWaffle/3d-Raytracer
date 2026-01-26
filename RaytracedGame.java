import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.List;

public class RaytracedGame extends Game{

	private final double focalLength;

	private final double[][] zBuffer;

	public final double speed = .02;
	public final double rotSpeed = .03;

	private final Vec3 light;

	private Transform cam;

	Environment env;
	
	int cx = width / 2;
	int cy = height / 2;

	public RaytracedGame(int width, int height, double fov, Environment env){
		super(width, height);
		this.light = new Vec3(0, 1, -1).normalize();
		
		this.env = env;
		
		
		cam = new Transform();
		cam.rotateZ(Math.PI);
		cam.translate(0, 0, 1);
		
		this.focalLength = (double) height / (2 * Math.tan(fov/2));
		zBuffer = new double[width][height];
	}

	@Override
	public String name(){
		return "Raytraced 3d";
	}
	public static long logicTime;
	@Override
	public void tick(){
		long start = System.nanoTime();
		if (input.keys['W']) 				cam.translate(0, 0, -speed);
		if (input.keys['A']) 				cam.translate(speed, 0, 0);
		if (input.keys['S']) 				cam.translate(0, 0, speed);
		if (input.keys['D']) 				cam.translate(-speed, 0, 0);
		if (input.keys[' ']) 				cam.translate(0, -speed, 0);
		if (input.keys[Input.SHIFT]) 		cam.translate(0, speed, 0);
	
		if (input.keys[Input.UP_ARROW]) 	cam.rotateX( rotSpeed);
		if (input.keys[Input.DOWN_ARROW]) 	cam.rotateX(-rotSpeed);
		if (input.keys[Input.LEFT_ARROW]) 	cam.rotateY( rotSpeed);
		if (input.keys[Input.RIGHT_ARROW]) 	cam.rotateY(-rotSpeed);
		if (input.keys['Q']) 				cam.rotateZ(-rotSpeed);
		if (input.keys['E']) 				cam.rotateZ( rotSpeed);
		logicTime = System.nanoTime()-start;
	}

	public double getX(double x, double y, double z) {
		return focalLength * (x / z);
	}
	public double getY(double x, double y, double z) {
		return focalLength * (y / z);
	}

	private void clearZBuffer() {
		for (int x = 0; x < width; x++) {
			Arrays.fill(zBuffer[x], Double.POSITIVE_INFINITY);
		}
	}
	
	@Override
	public void updateFrame(Graphics2D g2d){
		long renderStart = System.nanoTime();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();

		clearZBuffer();
	
		env.points.clear();
		List<Point> points = env.points;
		final int incr = 16 ;
		
		points.add(new Point(cam.translation, .01, Color.white));

		Vec3 origin = cam.translation;
		for (int x = 0; x < width; x+=incr){
			for (int y = 0; y < height; y+=incr){
				double px = (double)(x-cx);
				double py = (double)(cy-y);

				Vec3 vector = cam.inv.transform(new Vec3(px, py, -focalLength).normalize());

				Point p = new Point(vector.add(origin),.01,
					new Color((256*x)/width, (256*y)/height, 0)
				);
				points.add(p);
				
				
				Vec3 intersect = null;
				for (Triangle tri : env.mesh.triangles()){
					Vec3 inter = tri.getIntersection(origin, vector);
					if (inter == null) continue;
					if (intersect == null || origin.dist(intersect) > origin.dist(inter)){
						intersect = inter;
					}
				}
				if (intersect == null) continue;
				int magnitude = (int) (255 * cam.translation.dist(intersect));
				magnitude = Math.max(0, Math.min(255, magnitude));
				int[] color = {
					magnitude,
					0,
					0,
					255,
				};
				for (int dx = 0; dx < incr; dx++){
					for (int dy = 0; dy < incr; dy++){
						raster.setPixel(x+dx, y+dy, color);
					}
				}
			}
		}


		//for (Point point : points){
		//	point.render(raster, focalLength, cx, cy, zBuffer, cam);
		//}

		g2d.drawImage(image, 0, 0, null);
		
		long renderTime = System.nanoTime()-renderStart;
		g2d.drawString("Render (ms):"+renderTime/1_000_000.0,0,20);
		g2d.drawString("Logic  (ms):"+logicTime/1_000_000.0,0,40);
		g2d.drawString(cam.toString(), 0, 60);
	}
}