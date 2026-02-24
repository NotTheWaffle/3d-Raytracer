
import Math.Vec3;


public class Viewport extends Transform {
	public final double fov;
	public final double focalLength;

	public final double focusDistance;
	public final double focus;

	public final int screenWidth;
	public final int screenHeight;
	public final int cx;
	public final int cy;

	public Viewport(double fov, int screenWidth, int screenHeight){
		this(fov, 0, 0, screenWidth, screenHeight);
	}
	public Viewport(double fov, double focusDistance, double focus, int screenWidth, int screenHeight){
		super();
		this.fov = Math.max(Math.min(fov, Math.PI), 0.0);
		this.focalLength = (double) screenWidth / (2 * Math.tan(this.fov/2));

		this.focusDistance = focusDistance;
		this.focus = focus;
		
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.cx = screenWidth/2;
		this.cy = screenHeight/2;
	}

	public double getX(Vec3 p){
		return ((focalLength * p.x / p.z)+cx);
	}
	public double getY(Vec3 p){
		return (cy-(focalLength * p.y / p.z));
	}

	public Viewport moveX(double x){
		move(x, 0, 0);
		return this;
	}
	public Viewport moveY(double y){
		move(0, y, 0);
		return this;
	}
	public Viewport moveZ(double z){
		move(0, 0, z);
		return this;
	}
	
	public Viewport translateX(double x){
		translate(x, 0, 0);
		return this;
	}
	public Viewport translateY(double y){
		translate(0, y, 0);
		return this;
	}
	public Viewport translateZ(double z){
		translate(0, 0, z);
		return this;
	}
}
