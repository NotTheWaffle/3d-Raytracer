
import Math.Vec3;
import java.awt.image.WritableRaster;

/**
 * An abstract object which represents the Physical parts of an object
 */
public abstract class PhysicalObject {
	public final static double EPSILON = 1e-8;
	public final Material material;

	public PhysicalObject(Material material){
		this.material = material;
	}
	public abstract Intersection getIntersection(Vec3 origin, Vec3 direction);
	public abstract void renderRasterized(WritableRaster raster, double[][] zBuffer, Viewport camera);
}