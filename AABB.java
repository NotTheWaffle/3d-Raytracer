
import Math.Vec3;
import java.util.List;

public final class AABB{
	public float maxX, maxY, maxZ, minX, minY, minZ;
	public AABB(){
		maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;
		minX = minY = minZ = Float.POSITIVE_INFINITY;
	}
	public AABB(float x, float y, float z){
		minX = maxX = x;
		minY = maxY = y;
		minZ = maxZ = z;
	}
	public AABB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ){
		this.minX = Math.min(minX, maxX);
		this.maxX = Math.max(minX, maxX);
		this.minY = Math.min(minY, maxX);
		this.maxY = Math.max(minY, maxX);
		this.minZ = Math.min(minZ, maxX);
		this.maxZ = Math.max(minZ, maxX);
	}
	public AABB(List<Vec3> points){
		this();
		for (Vec3 p : points){
			addPoint(p);
		}
	}
	public AABB addTriangles(List<Triangle> tris){
		for (Triangle tri : tris){
			addPoint(tri.p1);
			addPoint(tri.p2);
			addPoint(tri.p3);
		}
		return this;
	}
	public AABB addTriangles(Triangle[] tris){
		for (Triangle tri : tris){
			addPoint(tri.p1);
			addPoint(tri.p2);
			addPoint(tri.p3);
		}
		return this;
	}

	public float testIntersection(final Vec3 origin, final Vec3 direction){
		float txenter = (minX-origin.x)/direction.x;
		float txexit = (maxX-origin.x)/direction.x;

		if (txenter > txexit){
			final float temp = txenter;
			txenter = txexit;
			txexit = temp;
		}
		
		float tyenter = (minY-origin.y)/direction.y;
		float tyexit = (maxY-origin.y)/direction.y;

		if (tyenter > tyexit){
			final float temp = tyenter;
			tyenter = tyexit;
			tyexit = temp;
		}
		
		float tzenter = (minZ-origin.z)/direction.z;
		float tzexit = (maxZ-origin.z)/direction.z;

		if (tzenter > tzexit){
			final float temp = tzenter;
			tzenter = tzexit;
			tzexit = temp;
		}

		final float tenter = Math.max(txenter, Math.max(tyenter, tzenter));
		final float texit = Math.min(txexit, Math.min(tyexit, tzexit));

		if (tenter <= texit && texit > 0){
			return Math.max(tenter, 0);
		}
		return -1;
	}
	
	public AABB addPoint(float x, float y, float z){
		if (x > maxX) maxX = x;
		if (x < minX) minX = x;

		if (y > maxY) maxY = y;
		if (y < minY) minY = y;

		if (z > maxZ) maxZ = z;
		if (z < minZ) minZ = z;
		return this;
	}
	public AABB addPoint(Vec3 vec){
		return addPoint(vec.x, vec.y, vec.z);
	}
	public AABB addPoint(Point point){
		return addPoint(point.pos.x, point.pos.y, point.pos.z);
	}
}
