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
	
	public Transform turnX(double pitch){
		rot = Mat3.multiply(rot, rotationX(-pitch));
		inv = rot.transpose();
		return this;
	}
	public Transform turnY(double yaw){
		rot = Mat3.multiply(rot, rotationY(-yaw));
		inv = rot.transpose();
		return this;
	}
	public Transform turnZ(double roll){
		rot = Mat3.multiply(rot, rotationZ(-roll));
		inv = rot.transpose();
		return this;
	}
	public Transform move(double forward, double right, double up){
		Vec3 fixed = rot.transform(new Vec3(forward, right, up));
		translation = translation.add(fixed);
		return this;
	}

	public Transform rotateX(double pitch){
		rot = Mat3.multiply(rotationX(pitch), rot);
		inv = rot.transpose();
		return this;
	}
	public Transform rotateY(double yaw){
		rot = Mat3.multiply(rotationY(yaw), rot);
		inv = rot.transpose();
		return this;
	}
	public Transform rotateZ(double roll){
		rot = Mat3.multiply(rotationZ(roll), rot);
		inv = rot.transpose();
		return this;
	}
	public Transform translate(double x, double y, double z){
		translation = translation.add(new Vec3(x, y, z));
		return this;
	}
	
	private static Mat3 rotationX(double pitch) {
		double c = Math.cos(pitch);
		double s = Math.sin(pitch);
		return new Mat3(
			1, 0, 0,
			0, c, -s,
			0, s, c
		);
	}

	private static Mat3 rotationY(double yaw) {
		double c = Math.cos(yaw);
		double s = Math.sin(yaw);
		return new Mat3(
			c, 0, s,
			0, 1, 0,
			-s, 0, c
		);
	}

	private static Mat3 rotationZ(double roll) {
		double c = Math.cos(roll);
		double s = Math.sin(roll);
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
