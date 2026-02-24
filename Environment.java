
import Math.Vec3;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Environment{
	public final Vec3 sunvec = new Vec3(0, 1, 1);;
	
	private final double[] sunColor = {1, 0.81876767, 0.53333336};
	private final double[] groundColour = {0.35, 0.30, 0.35};
	private final double[] skyColorHorizon = {1.0, 1.0, 1.0};
	private final double[] skyColorZenith = {0.08, 0.37, 0.73};

	private double sunInverseRadius = 200;	//bigger = smaller
	private double sunIntensity = .25;		//bigger = brighter
	private boolean background;
	private boolean sun;
	

	public final List<PhysicalObject> physicalObjects;
	public Environment(){
		this(false);
	}
	public Environment(boolean environment){
		physicalObjects = new ArrayList<>();
		this.background = environment;
		this.sun = !background;
	}
	public void addCornellBox(double innerWidth, double outerWidth){
		//bottom
		add(new RectangularPrism(-outerWidth/2, outerWidth/2, -outerWidth/2, -innerWidth/2, -outerWidth/2, outerWidth/2, Material.solid(Color.white)));
		//left
		add(new RectangularPrism(-outerWidth/2, -innerWidth/2, -innerWidth/2, innerWidth/2, -outerWidth/2, innerWidth/2, Material.solid(Color.RED)));
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
		addFloor();
	}
	public void addFloor(){
		add(new RectangularPrism(0, -1.5, 0, 20, 1, 20, Material.SOLID, 0));
	}
	public void addHueSpheres(int count, double radius){
		for (int i = 0; i < count; i++){
			Color color = new Color(Color.HSBtoRGB((float)i/count, 1, 1));
			add(new Sphere(new Transform().rotateY(i*2*Math.PI/count).applyTo(new Vec3(3, 0, 0)), radius, Material.light(color)));
		}
	}
	public void add(PhysicalObject object){
		physicalObjects.add(object);
	}
	public Intersection getBackgroundEnvironment(Vec3 rayDirection){
		if (background){
			return new Intersection(Vec3.ZERO_VEC, new Material(computeSkyColor(rayDirection), 1), Vec3.ZERO_VEC, false);
		} else if (sun){
			if (rayDirection.dist(sunvec) < .7){
				return new Intersection(Vec3.ZERO_VEC, Material.LIGHT, Vec3.ZERO_VEC, false);
			}
		}
		return null;
		
	}
	public double[] computeSkyColor(Vec3 dir) {
		// smoothstep(0, 0.4, dir.y)
		double t0 = (dir.y) / 0.4;
		t0 = Math.max(0.0, Math.min(1.0, t0));
		t0 = t0 * t0 * (3.0 - 2.0 * t0);
		double skyGradientT = Math.pow(t0, 0.35);

		// lerp(skyColourHorizon, skyColourZenith, skyGradientT)
		double[] skyGradient = new double[3];
		skyGradient[0] = skyColorHorizon[0] + (skyColorZenith[0] - skyColorHorizon[0]) * skyGradientT;
		skyGradient[1] = skyColorHorizon[1] + (skyColorZenith[1] - skyColorHorizon[1]) * skyGradientT;
		skyGradient[2] = skyColorHorizon[2] + (skyColorZenith[2] - skyColorHorizon[2]) * skyGradientT;

		// smoothstep(-0.01, 0, dir.y)
		double t1 = (dir.y + 0.01) / 0.01;
		t1 = Math.max(0.0, Math.min(1.0, t1));
		t1 = t1 * t1 * (3.0 - 2.0 * t1);
		double groundToSkyT = t1;
		double sunMask = (groundToSkyT >= 1.0) ? 1.0 : 0.0;

		double sun = Math.pow(Math.max(0.0, dir.dot(sunvec)), 1000.0 / sunInverseRadius) * sunIntensity;

		double[] composite = new double[3];
		composite[0] = groundColour[0] + (skyGradient[0] - groundColour[0]) * groundToSkyT + sun * sunColor[0] * sunMask;
		composite[1] = groundColour[1] + (skyGradient[1] - groundColour[1]) * groundToSkyT + sun * sunColor[1] * sunMask;
		composite[2] = groundColour[2] + (skyGradient[2] - groundColour[2]) * groundToSkyT + sun * sunColor[2] * sunMask;
		
		if (composite[0] > 1) composite[0] = 1;
		if (composite[1] > 1) composite[1] = 1;
		if (composite[2] > 1) composite[2] = 1;

		return composite;
	}
}