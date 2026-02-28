
import Math.Vec3;
import java.awt.image.WritableRaster;

public abstract class PhysicalObject {
	public final static float EPSILON = 1e-4f;
	public final Material material;
	public final Transform transform;

	public PhysicalObject(Material material){
		this(material, new Transform());
	}
	public PhysicalObject(Material material, Transform transform){
		this.material = material;
		this.transform = transform;
	}
	public final Intersection getTransformedintersection(Vec3 rayOrigin, Vec3 rayDirection){
		Intersection intersection = getLocalIntersection(transform.applyTo(rayOrigin), transform.inv.mul(rayDirection));
		if (intersection == null) return null;
		intersection.pos = transform.unapplyTo(intersection.pos);
		intersection.normal = transform.rot.mul(intersection.normal);
		return intersection;
	}
	protected abstract Intersection getLocalIntersection(Vec3 rayOrigin, Vec3 rayDirection);
	public abstract void renderRasterized(WritableRaster raster, float[][] zBuffer, Viewport camera);
}