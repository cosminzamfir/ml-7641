package ml.assignments.assignment2.fourpeaks;

import ml.assignments.MLAssignmentUtils;
import ml.assignments.assignment2.ExecutionResult;
import ml.assignments.assignment2.ExecutionResults;
import ml.assignments.assignment2.MaxIterationsTrainer;
import opt.OptimizationAlgorithm;

public abstract class FourPeaksOptimalFitness {

	private static final int startN = MLAssignmentUtils.getStartN();
	private static final int endN = MLAssignmentUtils.getEndN();
	private static final int step = MLAssignmentUtils.getStep();
	private static final int TRIALS = MLAssignmentUtils.getTrials();
	
	public void run() {
		System.out.println(getDescription());
		System.out.println("N,Runs,Iterations,Time");
		for (int n = startN; n <= endN; n=n+step) {
			ExecutionResults results = new ExecutionResults();
			for (int i = 0; i < TRIALS; i++) {
				results.addResult(runOnce(n));
			}
		System.out.println(n + "," + results.getRuns() + "," + results.getIterations() + "," + results.getExecutionTime());
		}
	}
	
	public abstract OptimizationAlgorithm getOptimizationAlgorithm(int n);
	
	public String getDescription() {
		return getOptimizationAlgorithm(10).getClass().getSimpleName();
	}
	
	public ExecutionResult runOnce(int n) {
		int t = n/10;
		double targetFitness = computeMax(n, t);
		int iterations = 0;
		long start = System.currentTimeMillis();
		int runs = 0;
		boolean done = false;
		while(!done) {
			OptimizationAlgorithm oa = getOptimizationAlgorithm(n);
			MaxIterationsTrainer trainer = new MaxIterationsTrainer(oa, 0, 0, targetFitness);
			trainer.train();
			runs++;
			iterations += trainer.getCurrentIteration();
			if(trainer.getOptimalFitness() == targetFitness) {
				done = true;
			}
		}
		return new ExecutionResult(runs, iterations, targetFitness, System.currentTimeMillis() - start, 0);
		
	}
	
	private static int computeMax(int N, int T) {
		return 2 * N - T - 1;
	}
}
