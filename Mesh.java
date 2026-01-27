
import Math.Vec3;
import java.awt.image.WritableRaster;

public class Mesh extends PhysicalObject{
	public final Triangle[] triangles;
	public final Vec3[] verticies;
	public Mesh(Vec3[] verticies, Triangle[] triangles){
		super(null);
		this.triangles = triangles;
		this.verticies = verticies;
	}
	@Override
	public void render(WritableRaster raster, double focalLength, int cx, int cy, double[][] zBuffer, Transform cam){
		for (Triangle tri : triangles){
			tri.render(raster, focalLength, cx, cy, zBuffer, cam);
		}
	}
	@Override
	public Intersection getIntersection(Vec3 origin, Vec3 direction){
		Intersection intersection = null;
		for (Triangle tri : triangles){
			Intersection localIntersection = tri.getIntersection(origin, direction);
			if (localIntersection == null) continue;
			if (intersection == null || origin.dist(intersection.pos) > origin.dist(localIntersection.pos)){
				intersection = localIntersection;
			}
		}
		return intersection;
	}
}