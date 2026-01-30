// unusued, an attempt to speed up random number generation
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public interface Rand {
	public double nextDouble();
	public static class JavaRand implements Rand{
		private final Random random;
		public JavaRand(Random random){
			this.random = random;
		}
		@Override
		public double nextDouble(){
			return random.nextDouble();
		}
	}
	public static class BareRand implements Rand{
		private static final long multiplier = 0x5DEECE66DL;
		private static final long addend = 0xBL;
		private static final long mask = (1L << 48) - 1;
		private static final double DOUBLE_UNIT = 0x1.0p-53;
		private final AtomicLong seed;
		public BareRand(){
			this.seed = new AtomicLong(initialScramble(System.nanoTime()));
		}
		private static long initialScramble(long seed) {
			return (seed ^ multiplier) & mask;
		}
		public double nextDouble() {
			return (((long)(next(Double.PRECISION - 27)) << 27) + next(27)) * DOUBLE_UNIT;
		}
		protected int next(int bits) {
			long oldseed, nextseed;
			AtomicLong seed = this.seed;
			do {
				oldseed = seed.get();
				nextseed = (oldseed * multiplier + addend) & mask;
			} while (!seed.compareAndSet(oldseed, nextseed));
			return (int)(nextseed >>> (48 - bits));
		}
	}
}
