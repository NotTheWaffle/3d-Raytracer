
import Math.FloatMath;
import Math.Vec3;


public class Viewport extends Transform {
	public final float fov;
	public final float focalLength;

	public final float focusDistance;
	public final float focus;

	public final int screenWidth;
	public final int screenHeight;
	public final int cx;
	public final int cy;

	public Viewport(float fov, int screenWidth, int screenHeight){
		this(fov, 0, 0, screenWidth, screenHeight);
	}
	public Viewport(float fov, float focusDistance, float focus, int screenWidth, int screenHeight){
		super();
		this.fov = (float) Math.max(Math.min(fov, Math.PI), 0.0);
		this.focalLength = (screenWidth / (2f * FloatMath.tan(this.fov/2f)));

		this.focusDistance = focusDistance;
		this.focus = focus;
		
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.cx = screenWidth/2;
		this.cy = screenHeight/2;
	}

	public float getX(Vec3 p){
		return ((focalLength * p.x / p.z)+cx);
	}
	public float getY(Vec3 p){
		return (cy-(focalLength * p.y / p.z));
	}

	public Viewport moveX(float x){
		move(x, 0, 0);
		return this;
	}
	public Viewport moveY(float y){
		move(0, y, 0);
		return this;
	}
	public Viewport moveZ(float z){
		move(0, 0, z);
		return this;
	}
	
	public Viewport translateX(float x){
		translate(x, 0, 0);
		return this;
	}
	public Viewport translateY(float y){
		translate(0, y, 0);
		return this;
	}
	public Viewport translateZ(float z){
		translate(0, 0, z);
		return this;
	}
}
