package Math;

public class ImplicitEquation{
	public static ImplicitEquation torus(float R, float r){
		return new ImplicitEquation((x, y, z) -> {
				float s = (x*x + y*y + z*z + R*R - r*r);
				return (s*s - 4 * R * R * (x * x + y * y));
		});
	}
	TriFunction<Float, Float, Float, Float> func;
	public ImplicitEquation(TriFunction<Float, Float, Float, Float> func){
		this.func = func;
	}
	public float apply(float x, float y, float z){
		return func.apply(x, y, z);
	}
	@FunctionalInterface
	public interface TriFunction<A, B, C, R> {
		R apply(A a, B b, C c);
	}

}