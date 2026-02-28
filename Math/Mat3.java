package Math;

public class Mat3 {
	public final float m00, m01, m02;
	public final float m10, m11, m12;
	public final float m20, m21, m22;

	public static final Mat3 IDENTITY = new Mat3(1);
	
	public Mat3(){
		this(
			0, 0, 0,
			0, 0, 0,
			0, 0, 0
		);
	}
	public Mat3(float d){
		this(
			d, 0, 0,
			0, d, 0,
			0, 0, d
		);
	}
	public Mat3(float d00, float d01, float d02,
				float d10, float d11, float d12,
				float d20, float d21, float d22 ){
		m00 = d00; m01 = d01; m02 = d02;
		m10 = d10; m11 = d11; m12 = d12;
		m20 = d20; m21 = d21; m22 = d22;
	}
	public Mat3 mul(Mat3 b) {
		return new Mat3(
			m00*b.m00 + m01*b.m10 + m02*b.m20,
			m00*b.m01 + m01*b.m11 + m02*b.m21,
			m00*b.m02 + m01*b.m12 + m02*b.m22,

			m10*b.m00 + m11*b.m10 + m12*b.m20,
			m10*b.m01 + m11*b.m11 + m12*b.m21,
			m10*b.m02 + m11*b.m12 + m12*b.m22,

			m20*b.m00 + m21*b.m10 + m22*b.m20,
			m20*b.m01 + m21*b.m11 + m22*b.m21,
			m20*b.m02 + m21*b.m12 + m22*b.m22
		);
	}
	public static Mat3 multiply(Mat3 a, Mat3 b) {
		return new Mat3(
			a.m00*b.m00 + a.m01*b.m10 + a.m02*b.m20,
			a.m00*b.m01 + a.m01*b.m11 + a.m02*b.m21,
			a.m00*b.m02 + a.m01*b.m12 + a.m02*b.m22,

			a.m10*b.m00 + a.m11*b.m10 + a.m12*b.m20,
			a.m10*b.m01 + a.m11*b.m11 + a.m12*b.m21,
			a.m10*b.m02 + a.m11*b.m12 + a.m12*b.m22,

			a.m20*b.m00 + a.m21*b.m10 + a.m22*b.m20,
			a.m20*b.m01 + a.m21*b.m11 + a.m22*b.m21,
			a.m20*b.m02 + a.m21*b.m12 + a.m22*b.m22
		);
	}
	public Vec3 mul(Vec3 v) {
		return new Vec3(
			v.x * m00 + v.y * m01 + v.z * m02,
			v.x * m10 + v.y * m11 + v.z * m12,
			v.x * m20 + v.y * m21 + v.z * m22
		);
	}
	public Mat3 transpose(){
		return new Mat3(
			this.m00, this.m10, this.m20,
			this.m01, this.m11, this.m21,
			this.m02, this.m12, this.m22
		);
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		int bits;

		bits = Float.floatToIntBits(m00);
		result = 31 * result + (int) (bits);
		bits = Float.floatToIntBits(m01);
		result = 31 * result + (int) (bits);
		bits = Float.floatToIntBits(m02);
		result = 31 * result + (int) (bits);

		bits = Float.floatToIntBits(m10);
		result = 31 * result + (int) (bits);
		bits = Float.floatToIntBits(m11);
		result = 31 * result + (int) (bits);
		bits = Float.floatToIntBits(m12);
		result = 31 * result + (int) (bits);

		bits = Float.floatToIntBits(m20);
		result = 31 * result + (int) (bits);
		bits = Float.floatToIntBits(m21);
		result = 31 * result + (int) (bits);
		bits = Float.floatToIntBits(m22);
		result = 31 * result + (int) (bits);

		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Mat3)) {
			return false;
		}

		Mat3 mat = (Mat3) obj;

		return
			m00 == mat.m00 && m01 == mat.m01 && m02 == mat.m02 &&
			m10 == mat.m10 && m11 == mat.m11 && m12 == mat.m12 &&
			m20 == mat.m20 && m21 == mat.m21 && m22 == mat.m22;
	}
	@Override
	public String toString(){
		String s = String.format("%2.1f, %2.1f, %2.1f    \n%2.1f, %2.1f, %2.1f    \n%2.1f, %2.1f, %2.1f", m00, m01, m02, m10, m11, m12, m20, m21, m22);
		return s;
	}
}