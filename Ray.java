

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
					intersection = new Intersection(Vec3.ZERO_VEC, Material.light(new Color(67, 67, 67)), Vec3.ZERO_VEC, false);
				//	break;
				}
			}
			
			
			Material material = intersection.material;


			// find next ray
			Vec3 nextDirection;
			boolean specularReflection = random.nextDouble() < material.specularityChance;
			if (random.nextDouble() < 1-material.transparency){
				Vec3 diffuseDirection = getDiffuseReflectionVector(intersection.normal, random);
				
				if (specularReflection){
					Vec3 specularDirection = getSpecularReflectionVector(rayDirection, intersection.normal);
					nextDirection = lerp(diffuseDirection, specularDirection, material.specularity).normalize();
				} else {
					nextDirection = diffuseDirection.normalize();
				}
			} else {
				// took this from sebastian lague cuz im stupid at physics
				Vec3 I = rayDirection.normalize();
				Vec3 N = intersection.normal.normalize();

				double iorA = 1.0;
				double iorB = material.refractiveIndex;

				double d = I.dot(N);

				if (d > 0) {
					N = N.mul(-1.0);
					double temp = iorA;
					iorA = iorB;
					iorB = temp;
				}

				double cosI = -I.dot(N);
				double eta = iorA/iorB;

				double sinT2 = eta * eta * (1.0 - cosI * cosI);
				
				

				if (sinT2 >= 1.0){
					nextDirection = getSpecularReflectionVector(I, N);
				} else {
					double cosT = Math.sqrt(1.0 - sinT2);
			
					double r0 = (iorA - iorB) / (iorA + iorB);
					r0 *= r0;

					double c = (iorA <= iorB) ? cosI : cosT;
					double reflectance = r0 + (1.0 - r0) * Math.pow(1 - c, 5.0);

					if (random.nextDouble() < reflectance){
						nextDirection = getSpecularReflectionVector(I, N);
					} else {
						nextDirection = getRefractionVector(I, N, eta, cosI, cosT);
					}
				}
			}
			rayOrigin = intersection.pos.add(rayDirection.mul(EPSILON));
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
	private static Vec3 getSpecularReflectionVector(Vec3 rayDirection, Vec3 normal){
		return rayDirection.sub(normal.mul(2 * rayDirection.dot(normal))).normalize();
	}
	private static Vec3 getDiffuseReflectionVector(Vec3 normal, Random random){
		return normal.add(Vec3.random(random)).normalize();
	}
	private static Vec3 getRefractionVector(Vec3 rayDirection, Vec3 normal, double eta, double cosI, double cosT){
		return rayDirection.mul(eta).add(normal.mul(eta * cosI - cosT)).normalize();
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
