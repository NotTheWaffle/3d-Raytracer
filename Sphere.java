
import Math.FloatMath;
import Math.Vec3;
import java.awt.image.WritableRaster;

public class Sphere extends PhysicalObject{
	public final Vec3 point;
	public final float radius;

	public Sphere(Vec3 point, float radius, Material material){
		super(material);
		this.point = point;
		this.radius = radius;
	}

	@Override
	public void renderRasterized(WritableRaster raster, float[][] zBuffer, Viewport camera) {
		Vec3 p = camera.applyTo(this.point);
		if (p.z < 0) return;
		
		int screenX = (int) camera.getX(p);
		int screenY = (int) camera.getY(p);


		int projectedRadius = Math.max(1, (int) (camera.focalLength * (this.radius / p.z)));

		int minX = Math.max(0, screenX-projectedRadius);
		int maxX = Math.min(zBuffer.length - 1, screenX+projectedRadius);
		int minY = Math.max(0, screenY-projectedRadius);
		int maxY = Math.min(zBuffer[0].length - 1, screenY+projectedRadius);
		
		int[] rgb = {
			(int) (255 * (material.reflectionColor[0]+material.emissionColor[0])),
			(int) (255 * (material.reflectionColor[1]+material.emissionColor[1])),
			(int) (255 * (material.reflectionColor[2]+material.emissionColor[2])),
			255
		};
		
		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				int dx = x-screenX;
				int dy = y-screenY;
				if (dx * dx + dy * dy < projectedRadius * projectedRadius) {
					if (p.z < zBuffer[x][y]) {
						zBuffer[x][y] = p.z;
						raster.setPixel(x, y, rgb);
					}
				}
			}
		}
	}
	
	@Override
	public Intersection getIntersection(Vec3 rayOrigin, Vec3 rayVector){
		Vec3 l = rayOrigin.sub(point);
		
		float b = 2 * rayVector.dot(l);
		float c = l.dot(l) - radius*radius;


		float discriminant = b*b - 4*c;

		if (discriminant < 0) return null;
		float sqrtD = FloatMath.sqrt(discriminant);

		float t0 = (-b - sqrtD) / 2;
		float t1 = (-b + sqrtD) / 2;

		float t = Float.POSITIVE_INFINITY;
		
		if (t0 > EPSILON && t0 < t) t = t0;
		if (t1 > EPSILON && t1 < t) t = t1;

		if (t == Float.POSITIVE_INFINITY) return null;
		Vec3 intersectionPoint = rayOrigin.add(rayVector.mul(t));
		Vec3 normal = intersectionPoint.sub(point).normalize();
		
		return new Intersection(intersectionPoint, this.material, normal, normal.dot(rayVector) > 0);
	}
}