
import Math.Vec3;
import java.awt.Color;
import java.awt.image.WritableRaster;

public class Point {
	public Vec3 pos;
	public final Color color;
	public final int radius;
	public Point(Vec3 p, int radius){
		this.pos = p;
		this.color = new Color((int)(Math.random()*16777216));
		this.radius = radius;
	}
	public Point(Vec3 p, int radius, Color c){
		this.pos = p;
		this.color = c;
		this.radius = radius;
	}
	public void render(WritableRaster raster, double focalLength, int cx, int cy, double[][] zBuffer, Transform cam) {
		Vec3 p = cam.applyTo(this.pos);
		if (p.z < 0) return;

		
		int screenX = (int)( focalLength * p.x / p.z) + cx;
		int screenY = (int)(-focalLength * p.y / p.z) + cy;

		int minX = Math.max(0, screenX-radius);
		int maxX = Math.min(zBuffer.length - 1, screenX+radius);
		int minY = Math.max(0, screenY-radius);
		int maxY = Math.min(zBuffer[0].length - 1, screenY+radius);
		int[] rgb = {
			color.getRed(),
			color.getGreen(),
			color.getBlue(),
			255
		};
		
		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				int dx = x-screenX;
				int dy = y-screenY;
				if (dx * dx + dy * dy < radius * radius) {

					if (true || p.z < zBuffer[x][y]) {
						zBuffer[x][y] = p.z;
						raster.setPixel(x, y, rgb);
					}
				}
			}
		}
	}
	public static Vec3 project(Vec3 point, Transform cam, double focalLength){
		Vec3 projected = cam.applyTo(point);
		return new Vec3(
			focalLength * projected.x / projected.z,
			focalLength * projected.y / projected.z,
			0
		);
	}
}
