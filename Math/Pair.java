package Math;

import java.util.Objects;

public class Pair<T0, T1>{
	public final T0 t0;
	public final T1 t1;
	public Pair(T0 t0, T1 t1){
		this.t0 = t0;
		this.t1 = t1;
	}
	@Override
	public int hashCode(){
		return Objects.hashCode(t0) ^ Objects.hashCode(t1);
	}
	@Override
	public boolean equals(Object o){
		if (o == this) return true;
		return (o instanceof Pair p && Objects.equals(p.t0, t0) && Objects.equals(p.t1, t1));
	}
	@Override
	public String toString(){
		return "("+Objects.toString(t0)+", "+Objects.toString(t1)+")";
	}
}
