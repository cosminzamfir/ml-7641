package ml.assignments.assignment2.fourpeaks;

import ml.assignments.assignment2.ExecutionResult;
import ml.assignments.assignment2.ExecutionResults;
import ml.assignments.assignment2.MaxIterationsTrainer;
import opt.OptimizationAlgorithm;

public abstract class FourPeaksMaxAllocatedTime {

	private static final int startN = 10;
	private static final int endN = 10;
	private static final int step = 5;
	private static final int TRIALS = 2;
	private static final long MAX_TRAINING_TIME = 30000;
	
	public void run() {
		System.out.println(getDescription());
		System.out.println("N,MaxFitness");
		for (int n = startN; n <= endN; n=n+step) {
			ExecutionResults results = new ExecutionResults();
			for (int i = 0; i < TRIALS; i++) {
				results.addResult(runOnce(n));
			}
		System.out.println(n + "," + results.getOptimalFitness());
		}
	}
	
	public abstract OptimizationAlgorithm getOptimizationAlgorithm(int n);
	
	public String getDescription() {
		return getOptimizationAlgorithm(10).getClass().getSimpleName();
	}
	
	public ExecutionResult runOnce(int n) {
		long start = System.currentTimeMillis();
		boolean done = false;
		double bestFitness = 0;
		while(!done) {
			OptimizationAlgorithm oa = getOptimizationAlgorithm(n);
			long timeLeft = MAX_TRAINING_TIME - (System.currentTimeMillis() - start);
			MaxIterationsTrainer trainer = new MaxIterationsTrainer(oa, 0, timeLeft, 0);
			trainer.train();
			if(trainer.getOptimalFitness() > bestFitness) {
				bestFitness = trainer.getOptimalFitness();
			}
			long now = System.currentTimeMillis();
			if(now - start > MAX_TRAINING_TIME) {
				done = true;
			}
		}
		return new ExecutionResult(0, 0, bestFitness, 0, 0);
		
	}
}
