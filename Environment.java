
import Math.Vec3;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Environment{
	
	public final PhysicalObject sun = new Sphere(null, 0, Material.LIGHT);
	public final Vec3 sunVec = new Vec3(0, 1, 1).normalize();
	public final List<PhysicalObject> physicalObjects;
	public Environment(){
		physicalObjects = new ArrayList<>();
	}
	public Environment(PhysicalObject... mesh){
		this();
		physicalObjects.addAll(Arrays.asList(mesh));
	}
	public void addStanfordBox(double innerWidth, double outerWidth){
		//bottom
		add(new RectangularPrism(-outerWidth/2, outerWidth/2, -outerWidth/2, -innerWidth/2, -outerWidth/2, outerWidth/2, Material.solid(Color.RED)));
		//left
		add(new RectangularPrism(-outerWidth/2, -innerWidth/2, -innerWidth/2, innerWidth/2, -outerWidth/2, innerWidth/2, Material.solid(Color.BLUE)));
		//right
		add(new RectangularPrism(innerWidth/2, outerWidth/2, -innerWidth/2, innerWidth/2, -outerWidth/2, innerWidth/2, Material.solid(Color.GREEN)));
		//back
		add(new RectangularPrism(-outerWidth/2, outerWidth/2, -innerWidth/2, innerWidth/2, outerWidth/2, innerWidth/2, Material.solid(Color.WHITE)));
		//ceiling
		add(new RectangularPrism(-outerWidth/2, outerWidth/2, outerWidth/2, innerWidth/2, -outerWidth/2, outerWidth/2, Material.solid(Color.WHITE)));
		//light
		add(new RectangularPrism(-innerWidth/4, innerWidth/4, (innerWidth/2-innerWidth/16), innerWidth/2, -innerWidth/4, innerWidth/4, Material.LIGHT));
	}
	public void addSphereTest(){
		add(new Sphere(new Vec3(0, 0, 2.5), 1, Material.solid(Color.RED)));
		add(new Sphere(new Vec3(2.5, 0, 0), 1, Material.solid(Color.BLUE)));
		add(new Sphere(new Vec3(-2.5, 0, 0), 1, Material.solid(Color.GREEN)));
		add(new RectangularPrism(0, -1.5, 0, 20, 1, 20, Material.SOLID, 0));
	}
	public void add(PhysicalObject object){
		physicalObjects.add(object);
	}
}