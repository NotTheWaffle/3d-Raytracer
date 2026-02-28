import Math.Vec3;

public class Intersection {
	protected Vec3 pos;
	protected Material material;
	protected Vec3 normal;
	protected boolean backface;
	public Intersection(Vec3 pos, Material material, Vec3 normal, boolean backface){
		this.pos = pos;
		this.material = material;
		this.normal = normal;
		this.backface = backface;
	}
}