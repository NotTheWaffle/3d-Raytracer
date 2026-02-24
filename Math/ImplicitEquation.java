package Math;

public class ImplicitEquation{
	TriFunction<Double, Double, Double, Double> func;
	public ImplicitEquation(TriFunction<Double, Double, Double, Double> func){
		this.func = func;
	}
	public double apply(double x, double y, double z){
		return func.apply(x, y, z);
	}
	@FunctionalInterface
	public interface TriFunction<A, B, C, R> {
		R apply(A a, B b, C c);
	}

}