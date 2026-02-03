
import Math.Vec3;
import java.awt.Color;
import java.awt.image.WritableRaster;

/**
 * An abstract object which represents the Physical parts of an object
 */
public abstract class PhysicalObject {
	public final Color reflectionColor;
	public final Color emissionColor;
	public final double luminosity;
	public final double specularity;
	public final double transparency;

	public PhysicalObject(Color color, Material material){
		switch (material){
			case SOLID -> {
				this.luminosity = 0;
				this.emissionColor = Color.BLACK;
				this.reflectionColor = color;
				this.specularity = 0;
				this.transparency = 0;
			}
			case LIGHT -> {
				this.luminosity = 1;
				this.emissionColor = color;
				this.reflectionColor = Color.BLACK;
				this.specularity = 0;
				this.transparency = 0;
			}
			case MIRROR -> {
				this.luminosity = 0;
				this.emissionColor = Color.BLACK;
				this.reflectionColor = Color.WHITE;
				this.specularity = 1;
				this.transparency = 0;
			}
			case GLASS -> {
				this.luminosity = 0;
				this.emissionColor = Color.BLACK;
				this.reflectionColor = color;
				this.specularity = 0;
				this.transparency = 1;
			}
			case null -> {
				this.luminosity = 0;
				this.emissionColor = Color.BLACK;
				this.reflectionColor = Color.WHITE;
				this.specularity = 0;
				this.transparency = 0;
			}
		}
	}
	public abstract Intersection getIntersection(Vec3 origin, Vec3 direction);
	public abstract void render(WritableRaster raster, double focalLength, int cx, int cy, double[][] zBuffer, Transform cam);
}