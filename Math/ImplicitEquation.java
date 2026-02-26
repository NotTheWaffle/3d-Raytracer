package Math;

public class ImplicitEquation{
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