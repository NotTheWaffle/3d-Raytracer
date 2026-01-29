public class Pixel {
	public double[] runningColor;
	public int samples;
	public Pixel(double[] color, int weight){
		this.runningColor = color;
		this.samples = weight;
	}
	public Pixel(){
		this(new double[]{0, 0, 0}, 0);
	}
	public void addSample(double[] color, int weight){
		this.runningColor[0] = (this.runningColor[0]*this.samples + color[0] * weight)/(this.samples+weight);
		this.runningColor[1] = (this.runningColor[1]*this.samples + color[1] * weight)/(this.samples+weight);
		this.runningColor[2] = (this.runningColor[2]*this.samples + color[2] * weight)/(this.samples+weight);
		this.samples += weight;
	}
	public void clear(){
		runningColor[0] = runningColor[1] = runningColor[2] = 0;
		samples = 0;
	}
}
