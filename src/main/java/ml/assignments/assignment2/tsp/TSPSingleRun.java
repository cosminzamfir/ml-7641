package ml.assignments.assignment2.tsp;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import ml.assignments.MLAssignmentUtils;
import ml.assignments.assignment2.ExecutionResult;
import ml.assignments.assignment2.ExecutionResults;
import ml.assignments.assignment2.MaxIterationsTrainer;
import opt.OptimizationAlgorithm;

public abstract class TSPSingleRun {

	private static final int startN = MLAssignmentUtils.getStartN();
	private static final int endN = MLAssignmentUtils.getEndN();
	private static final int step = MLAssignmentUtils.getStep();
	private static final int TRIALS = MLAssignmentUtils.getTrials();
	private static Map<Integer, double[][]> points = new LinkedHashMap<Integer, double[][]>(); 

	public void run() {
		System.out.println(getDescription());
		System.out.println("N,BestDistance,TimeToBestDistance,IterationsToBestDistance");
		for (int n = startN; n <= endN; n = n + step) {
			ExecutionResults results = new ExecutionResults();
			for (int i = 0; i < TRIALS; i++) {
				results.addResult(runOnce(n));
			}
			System.out.println(n + "," + 1 / results.getOptimalFitness() + "," + results.getTimeToBestIteration() + "," + results.getBestIteration());
		}
	}

	public abstract OptimizationAlgorithm getOptimizationAlgorithm(double[][] points);

	public String getDescription() {
		return getOptimizationAlgorithm(generatePoints(10)).getClass().getSimpleName();
	}

	public ExecutionResult runOnce(int n) {
		double[][] points = generatePoints(n);
		OptimizationAlgorithm oa = getOptimizationAlgorithm(points);
		MaxIterationsTrainer trainer = new MaxIterationsTrainer(oa, 0, 0, 0);
		trainer.train();
		return new ExecutionResult(trainer);
	}

	private double[][] generatePoints(int n) {
		if(points.containsKey(n)) {
			return points.get(n);
		}
		Random random = new Random();
		// create the random points
		double[][] res = new double[n][2];
		for (int i = 0; i < res.length; i++) {
			res[i][0] = random.nextDouble();
			res[i][1] = random.nextDouble();
		}
		
		points.put(n, res);
		return res;
	}
}
