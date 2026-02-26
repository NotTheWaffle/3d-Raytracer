
import Math.Vec3;
import java.awt.image.WritableRaster;

public abstract class PhysicalObject {
	public final static float EPSILON = 1e-4f;
	public final Material material;

	public PhysicalObject(Material material){
		this.material = material;
	}
	public abstract Intersection getIntersection(Vec3 origin, Vec3 direction);
	public abstract void renderRasterized(WritableRaster raster, float[][] zBuffer, Viewport camera);
}