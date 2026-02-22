import Math.Vec3;

public class Intersection {
	public final Vec3 pos;
	public final Material material;
	public final Vec3 normal;
	public final boolean backface;
	public Intersection(Vec3 pos, Material material, Vec3 normal, boolean backface){
		this.pos = pos;
		this.material = material;
		this.normal = normal;
		this.backface = backface;
	}
}