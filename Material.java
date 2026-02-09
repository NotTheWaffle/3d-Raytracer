
import java.awt.Color;
/**
 * fake enums
 */
public class Material {
	public final double[] reflectionColor;
	public final double[] emissionColor;
	public final double emissionStrength;
	public final double specularity;
	public final double transparency;
	public final double specularityChance;
	/**
	 * A white material that emits no light. Fully diffuse reflections.
	 */
	public static final Material SOLID = solid(Color.WHITE);
	/**
	 * A black material that emits white light. No Reflections.
	 */
	public static final Material LIGHT = new Material(1, Color.WHITE, Color.BLACK, 0, 0, 0);
	/**
	 * A material that reflects all light perfectly specularly.
	 */
	public static final Material MIRROR = new Material(0, Color.BLACK, Color.WHITE, 1, 1, 0);
	/**
	 * A material that is not yet implemented.
	 */
	public static final Material GLASS = glass(Color.WHITE);
	/**
	 * A white material that emits no light. Reflects perfectly diffusely 10% of incoming light.
	 */
	public static final Material PLASTIC = plastic(Color.WHITE);
	/**
	 * A material that emits no light. Reflects all light somewhat specularly.
	 */
	public static final Material METAL = metal(Color.WHITE);

	public Material(double emissionStrength, Color emissionColor, Color reflectionColor, double specularity, double specularityChance, double transparency){
		this.emissionStrength = emissionStrength;
		this.emissionColor =new double[] {emissionColor.getRed()/255.0, emissionColor.getGreen()/255.0, emissionColor.getBlue()/255.0};
		
		this.reflectionColor = new double[] {reflectionColor.getRed()/255.0, reflectionColor.getGreen()/255.0, reflectionColor.getBlue()/255.0};
		this.specularity = specularity;
		this.specularityChance = specularityChance;

		this.transparency = transparency;
	}
	public static Material solid(Color color){
		return new Material(0, Color.BLACK, color, 0, 0, 0);
	}
	public static Material glass(Color color){
		return new Material(0, Color.BLACK, color, 0, 0, 1);
	}
	public static Material plastic(Color color){
		return new Material(0, Color.BLACK, color, 1, .1, 0);
	}
	public static Material metal(Color color){
		return new Material(0, Color.BLACK, color, .5, 1, 0);
	}
}