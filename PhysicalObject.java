
import Math.Vec3;
import java.awt.Color;
import java.awt.image.WritableRaster;

public abstract class PhysicalObject {
	public Color reflectionColor;
	public Color emissionColor;
	public double luminosity;
	public double specularity;
	public double transparency;

	public PhysicalObject(Material material, Color color){
		switch (material){
			case SOLID -> {
				this.luminosity = 0;
				this.emissionColor = Color.black;
				this.reflectionColor = color;
				this.specularity = 0;
				this.transparency = 0;
			}
			case LIGHT -> {
				this.luminosity = 1;
				this.emissionColor = color;
				this.reflectionColor = Color.black;
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

			}
		}
	}
	public abstract Intersection getIntersection(Vec3 origin, Vec3 direction);
	public abstract void render(WritableRaster raster, double focalLength, int cx, int cy, double[][] zBuffer, Transform cam);
}