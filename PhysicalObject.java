
import Math.Vec3;
import java.awt.image.WritableRaster;

public abstract class PhysicalObject {
	public Material material;
	public PhysicalObject(Material material){
		this.material = material;
	}
	public abstract Intersection getIntersection(Vec3 origin, Vec3 direction);
	public abstract void render(WritableRaster raster, double focalLength, int cx, int cy, double[][] zBuffer, Transform cam);
}