

import Math.Vec3;
import java.awt.Color;
import java.awt.image.WritableRaster;
import java.util.Random;

public final class Ray {
	public final static double EPSILON = PhysicalObject.EPSILON;
	private Ray(){}
	private final static Material err = Material.light(Color.GREEN);
	public static double[] trace(Vec3 rayOrigin, Vec3 rayDirection, Environment env, int maxDepth, Random random){
		double[] rayColor = {1.0, 1.0, 1.0};
		double[] incomingLight = {0.0, 0.0, 0.0};
		
		for (int i = 0; i < maxDepth; i++){
			// find nearest intersection
			Intersection intersection = null;
			for (PhysicalObject p : env.physicalObjects){
				Intersection localIntersection = p.getIntersection(rayOrigin, rayDirection);
				if (localIntersection == null) continue;
				if (intersection == null || rayOrigin.dist(intersection.pos) > rayOrigin.dist(localIntersection.pos)){
					intersection = localIntersection;
				}
			}

			// background light
			if (intersection == null) {
				if (rayDirection.dist(env.sunVec) < .75){
					intersection = new Intersection(Vec3.ZERO_VEC, env.sun, Vec3.ZERO_VEC, false);
				} else {
					break;
				}
			}
			
			
			Material material = intersection.material;


			// find next ray
			Vec3 nextDirection;
			boolean specularReflection = random.nextDouble() < material.specularityChance;
			if (random.nextDouble() < 1-material.transparency){
				Vec3 diffuseDirection = intersection.normal.add(Vec3.random(random)).normalize();
				
				if (specularReflection){
					Vec3 specularDirection = rayDirection.sub(intersection.normal.mul(2 * rayDirection.dot(intersection.normal))).normalize();
					nextDirection = lerp(diffuseDirection, specularDirection, material.specularity).normalize();
				} else {
					nextDirection = diffuseDirection.normalize();
				}
			} else {
				// took this from sebastian lague cuz im stupid at physics
				Vec3 I = rayDirection.normalize();
				Vec3 N = intersection.normal.normalize();

				double iorA = 1;
				double iorB = material.refractiveIndex;

				double d = I.dot(N);

				if (d > 0) {
					N = N.mul(-1);
					double temp = iorA;
					iorA = iorB;
					iorB = temp;
				}

				double cosI = -I.dot(N);
				double eta = iorA/iorB;

				double sinT2 = eta * eta * (1 - cosI * cosI);
				
				Vec3 reflect = I.sub(N.mul(2 * I.dot(N))).normalize();
				Vec3 refract = null;
				double reflectance;

				if (sinT2 > 1){
					reflectance = 1;
				} else {
					double cosT = Math.sqrt(Math.max(0.0, 1.0 - sinT2));
					refract = I.mul(eta).add(N.mul(eta * cosI - cosT)).normalize();
			
					double r0 = (iorA - iorB) / (iorA + iorB);
					r0 *= r0;
					reflectance = r0 + (1 - r0) * Math.pow(1 - cosI, 5);
				}
				if (random.nextDouble() < reflectance){
				//	System.out.println(reflectance);
					nextDirection = reflect;
				} else {
					nextDirection = refract;
				}
			}
			rayOrigin = intersection.pos;
			rayDirection = nextDirection;

			
			//calculate colors
			// emissionStrengh * emissionColor = emitted light, multiply with ray color to get the intersection of the colors
			if (!specularReflection){
				incomingLight[0] += (material.emissionStrength * material.emissionColor[0]) * rayColor[0];
				incomingLight[1] += (material.emissionStrength * material.emissionColor[1]) * rayColor[1];
				incomingLight[2] += (material.emissionStrength * material.emissionColor[2]) * rayColor[2];
				
				rayColor[0] *= material.reflectionColor[0];
				rayColor[1] *= material.reflectionColor[1];
				rayColor[2] *= material.reflectionColor[2];
			}

			if (rayColor[0] < .01 && rayColor[1] < .01 && rayColor[2] < .01){
				break;
			}
		}
		return incomingLight;
	}

	public static Vec3 lerp(final Vec3 start, final Vec3 end, final double a){
		final double ia = 1-a;
		return new Vec3(
			start.x * ia + end.x * a,
			start.y * ia + end.y * a,
			start.z * ia + end.z * a
		);
	}


	public static void render(Vec3 start, Vec3 end, WritableRaster raster, double[][] zBuffer, Viewport camera) {
		Vec3 projectedStart = camera.applyTo(start);
		Vec3 projectedEnd = camera.applyTo(end);
		
		
		if (projectedStart.z < 0 || projectedEnd.z < 0) return;

		
		double x1 = camera.getX(projectedStart);
		double y1 = camera.getY(projectedStart);

		double x2 = camera.getX(projectedEnd);
		double y2 = camera.getY(projectedEnd);

		if (x1 > x2){
			double temp = x1;
			x1 = x2;
			x2 = temp;
			temp = y1;
			y1 = y2;
			y2 = temp;
		}

		double c = Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
		double dx = (x2-x1)/c;
		double dy = (y2-y1)/c;

		int width = raster.getWidth();
		int height = raster.getHeight();

		int[] color = {
			255,
			255,
			255,
			255
		};
		
		for (double x = x1, y = y1; x < x2; x+=dx, y+=dy){
			if (x < 0 || (int)x >= width || y < 0 || (int)y >= height){
				break;
			}
			raster.setPixel((int)x, (int)y, color);
		}
		for (double x = x2, y = y2; x > x1; x-=dx, y-=dy){
			if (x < 0 || (int)x >= width || y < 0 || y >= height){
				break;
			}
			raster.setPixel((int)x, (int)y, color);
		}
	}
}
