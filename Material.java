
import java.awt.Color;
/**
 * fake enums
 */
public class Material {
	public final double[] emissionColor;
	public final double emissionStrength;

	public final double[] reflectionColor;
	public final double specularity;
	public final double specularityChance;

	public final boolean transparent;
	public final double refractiveIndex;
	public final double absorption;

	/** A white material that emits no light. Fully diffuse reflections.*/
	public static final Material SOLID = solid(Color.WHITE);
	/** A black material that emits white light. No Reflections.*/
	public static final Material LIGHT = light(Color.WHITE);
	/** A material that reflects all light perfectly specularly.*/
	public static final Material MIRROR = new Material(0, Color.BLACK, Color.WHITE, 1, 1, false, 0, 0);
	/** A white material that emits no light. Reflects perfectly specularly 10% of incoming light.*/
	public static final Material PLASTIC = plastic(Color.WHITE);
	/** A material that emits no light. Reflects all light somewhat specularly.*/
	public static final Material METAL = metal(Color.WHITE);
	/** A white material that emits no light. Refracts all light with an index of 1.5 (glass).*/
	public static final Material GLASS = glass(Color.WHITE, 1.5);

	private Material(double emissionStrength, Color emissionColor, Color reflectionColor, double specularity, double specularityChance, boolean transparent, double refractiveIndex, double absorption){
		this.emissionStrength = emissionStrength;
		this.emissionColor = new double[] {emissionColor.getRed()/255.0, emissionColor.getGreen()/255.0, emissionColor.getBlue()/255.0};
		
		this.reflectionColor = new double[] {reflectionColor.getRed()/255.0, reflectionColor.getGreen()/255.0, reflectionColor.getBlue()/255.0};
		this.specularity = specularity;
		this.specularityChance = specularityChance;

		this.transparent = transparent;
		this.refractiveIndex = refractiveIndex;
		this.absorption = absorption;
	}
	public Material(double[] color, double strength){
		this.emissionStrength = strength;
		this.emissionColor = color;

		this.reflectionColor = new double[] {0, 0, 0};
		this.specularity = 0;
		this.specularityChance = 0;

		this.transparent = false;
		this.refractiveIndex = 1;
		this.absorption = 0;
	}
	public static Material solid(Color color){
		return new Material(0, Color.BLACK, color, 0, 0, false, 0, 0);
	}
	public static Material plastic(Color color){
		return new Material(0, Color.BLACK, color, 1, .1, false, 0, 0);
	}
	public static Material metal(Color color){
		return new Material(0, Color.BLACK, color, .5, 1, false, 0, 0);
	}
	public static Material light(Color color){
		return new Material(1, color, Color.BLACK, 0, 0, false, 0, 0);
	}
	public static Material glass(Color color, double refractiveIndex){
		return new Material(0, Color.BLACK, color, 1, 0, true, refractiveIndex, 0);
	}
	public static Material frostedGlass(Color color, double refractiveIndex, double frostiness){
		return new Material(0, Color.BLACK, color,frostiness, 0, true, refractiveIndex, 0);
	}
}