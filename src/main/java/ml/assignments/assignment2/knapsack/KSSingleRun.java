package ml.assignments.assignment2.knapsack;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import ml.assignments.MLAssignmentUtils;
import ml.assignments.assignment2.ExecutionResult;
import ml.assignments.assignment2.ExecutionResults;
import ml.assignments.assignment2.MaxIterationsTrainer;
import opt.OptimizationAlgorithm;

public abstract class KSSingleRun {

	private static final int startN = MLAssignmentUtils.getStartN();
	private static final int endN = MLAssignmentUtils.getEndN();
	private static final int step = MLAssignmentUtils.getStep();
	private static final int TRIALS = MLAssignmentUtils.getTrials();
	private static Map<Integer,double[]> weights = new LinkedHashMap<Integer, double[]>();
	private static Map<Integer,double[]> volumes = new LinkedHashMap<Integer, double[]>();
	
	private static Random random = new Random();
	static {
		System.out.println("Dynamic programing");
		System.out.println("N,BestValue,TimeToBestValue");
		for (int n = startN; n <= endN; n+=5) {
			KnapSack knapSack = new KnapSack(generateWeights(n), generateVolumes(n), (int)getMaxVolume(n), n);
			long start = System.currentTimeMillis();
			System.out.println(n + "," + knapSack.run() + "," + (System.currentTimeMillis() - start));
		}
	}

	public void run() {
		System.out.println(getDescription());
		System.out.println("N,BestValue,TimeToBestValue,IterationsToBestValue");
		for (int n = startN; n <= endN; n = n + step) {
			ExecutionResults results = new ExecutionResults();
			for (int i = 0; i < TRIALS; i++) {
				results.addResult(runOnce(n));
			}
			System.out.println(n + "," + results.getOptimalFitness() + "," + results.getTimeToBestIteration() + "," + results.getBestIteration());
		}
	}

	public abstract OptimizationAlgorithm getOptimizationAlgorithm(int[] copies, double[] weights, double[] volumes, double maxVolume );

	public String getDescription() {
		return getOptimizationAlgorithm(generateCopies(4), generateWeights(4), generateVolumes(4),getMaxVolume(4)).getClass().getSimpleName();
	}

	public ExecutionResult runOnce(int n) {
		OptimizationAlgorithm oa = getOptimizationAlgorithm(generateCopies(n), generateWeights(n), generateVolumes(n), getMaxVolume(n));
		MaxIterationsTrainer trainer = new MaxIterationsTrainer(oa, 0, 0, 0);
		trainer.train();
		return new ExecutionResult(trainer);
	}

	private int[] generateCopies(int N) {
		int[] copies = new int[N];
		Arrays.fill(copies, KSUtils.COPIES_EACH);
		return copies;
	}

	private static double[] generateWeights(int N) {
        if(weights.containsKey(N)) {
        	return weights.get(N);
        }
		double[] res = new double[N];
        for (int i = 0; i < N; i++) {
            res[i] = 1 + (int)(random.nextDouble() * KSUtils.MAX_WEIGHT);
        }
        weights.put(N,res);
        return res;
	}

	private static double[] generateVolumes(int N) {
        if(volumes.containsKey(N)) {
        	return volumes.get(N);
        }
		double[] res = new double[N];
        for (int i = 0; i < N; i++) {
            res[i] = 1 + (int)(random.nextDouble() * KSUtils.MAX_VOLUME);
        }
        volumes.put(N, res);
        return res;
	}
	
	private static double getMaxVolume(int N) {
	        return KSUtils.MAX_VOLUME * N * KSUtils.COPIES_EACH * .4;
	}
}
