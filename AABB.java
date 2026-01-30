public class AABB {
	public double maxX, maxY, maxZ, minX, minY, minZ;
	public AABB(){
		maxX = minX = Double.NaN;
		maxY = minY = Double.NaN;
		maxZ = minZ = Double.NaN;
	}
	public AABB(double x, double y, double z){
		minX = maxX = x;
		minY = maxY = y;
		minZ = maxZ = z;
	}
	public void addPoint(double x, double y, double z){
		if (x > maxX) maxX = x;
		if (x < minX) minX = x;
		
		if (y > maxY) maxY = y;
		if (y < maxY) maxY = y;

		if (z > maxZ) maxZ = z;
		if (z < minZ) minZ = z;
	}
}
