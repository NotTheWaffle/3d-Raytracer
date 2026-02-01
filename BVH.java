
import java.awt.image.WritableRaster;
import java.util.List;

public class BVH {
	public static final int MAX_TRIANGLES = 20;
	public static final int MAX_DEPTH = 10;
	private BVH node0;
	private BVH node1;
	private List<Triangle> triangles;
	private final AABB bounds;
	public BVH(List<Triangle> triangles){
		bounds = new AABB();
		bounds.addTriangles(triangles);
		if (triangles.size() <= MAX_TRIANGLES){
			return;
		}
		
		
	}
	public void render(WritableRaster raster, double focalLength, int cx, int cy, double[][] zBuffer, Transform cam) {
		
	}
}
