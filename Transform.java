import Math.FloatMath;
import Math.Mat3;
import Math.Vec3;

public class Transform {
	public Vec3 translation;
	public Mat3 rot;
	public Mat3 inv;
	public Transform(){
		this.translation = new Vec3(0, 0, 0);
		this.rot = Mat3.IDENTITY;
		this.inv = rot.transpose();
	}

	public Vec3 applyTo(Vec3 point){
		return inv.transform(point.sub(translation));
	}
	
	public Transform turnX(float pitch){
		rot = Mat3.multiply(rot, rotationX(-pitch));
		inv = rot.transpose();
		return this;
	}
	public Transform turnY(float yaw){
		rot = Mat3.multiply(rot, rotationY(-yaw));
		inv = rot.transpose();
		return this;
	}
	public Transform turnZ(float roll){
		rot = Mat3.multiply(rot, rotationZ(-roll));
		inv = rot.transpose();
		return this;
	}
	public Transform move(float forward, float right, float up){
		Vec3 fixed = rot.transform(new Vec3(forward, right, up));
		translation = translation.add(fixed);
		return this;
	}

	public Transform rotateX(float pitch){
		rot = Mat3.multiply(rotationX(pitch), rot);
		inv = rot.transpose();
		return this;
	}
	public Transform rotateY(float yaw){
		rot = Mat3.multiply(rotationY(yaw), rot);
		inv = rot.transpose();
		return this;
	}
	public Transform rotateZ(float roll){
		rot = Mat3.multiply(rotationZ(roll), rot);
		inv = rot.transpose();
		return this;
	}
	public Transform translate(float x, float y, float z){
		translation = translation.add(new Vec3(x, y, z));
		return this;
	}
	
	private static Mat3 rotationX(float pitch) {
		float c = FloatMath.cos(pitch);
		float s = FloatMath.sin(pitch);
		return new Mat3(
			1, 0, 0,
			0, c, -s,
			0, s, c
		);
	}

	private static Mat3 rotationY(float yaw) {
		float c = FloatMath.cos(yaw);
		float s = FloatMath.sin(yaw);
		return new Mat3(
			c, 0, s,
			0, 1, 0,
			-s, 0, c
		);
	}

	private static Mat3 rotationZ(float roll) {
		float c = FloatMath.cos(roll);
		float s = FloatMath.sin(roll);
		return new Mat3(
			c, -s, 0,
			s, c, 0,
			0, 0, 1
		);
	}

	@Override
	public String toString(){
		return translation.toString()+"\n"+rot.toString();
	}
}
