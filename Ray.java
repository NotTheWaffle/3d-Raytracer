

import Math.Vec3;
import java.util.Random;

public final class Ray {
	private Ray(){}
	public static int[] getColor(Vec3 origin, Vec3 direction, Environment env, int depth){
		int[] color = {0, 0, 0, 255};
		if (depth == 0) {color[0] = color[1] = color[2] = 25; return color;}
		Intersection intersection = null;
		for (PhysicalObject p : env.lights){
			Intersection localIntersection = p.getIntersection(origin, direction);
			if (localIntersection == null) continue;
			if (intersection == null || origin.dist(intersection.pos) > origin.dist(localIntersection.pos)){
				intersection = localIntersection;
			}
		}
		
		for (Triangle tri : env.mesh.triangles){
			Intersection localIntersection = tri.getIntersection(origin, direction);
			if (localIntersection == null) continue;
			if (intersection == null || origin.dist(intersection.pos) > origin.dist(localIntersection.pos)){
				intersection = localIntersection;
			}
		}
		if (intersection == null) {
			color[0] = color[1] = color[2] = 0;
		} else if (intersection.object.material == Material.LIGHT){
			color[0] = color[1] = color[2] = 255;
		} else if (intersection.object.material == Material.SOLID){
			int detail = 50;
			Random random = new Random();
			for (int i = 0; i < detail; i++){
				Vec3 nextVec = new Vec3(1-2*random.nextDouble(), 1-2*random.nextDouble(), 1-2*random.nextDouble());
				if (nextVec.dot(intersection.normal) < 0) nextVec = nextVec.mul(-1);
				if (random.nextDouble() < .1) nextVec = direction.sub(intersection.normal.mul(2 * direction.dot(intersection.normal))).normalize();
				int[] col = getColor(intersection.pos, nextVec, env, depth-1);
				color[0] += col[0];
				color[1] += col[1];
				color[2] += col[2];
			}
			color[0]/=detail;
			color[1]/=detail;
			color[2]/=detail;
			if (color[0] < 5){
				color[0] = color[1] = color[2] = random.nextInt(5, 10);
			}
		}
		return color;
	}

}
