package ml.assignments.assignment2;

public class ExecutionResult {

	private int runs;
	private int iterations;
	private double optimalFitness;
	private long executionTime;
	private int bestIteration;
	private long timeToBestIteration;
	
	public ExecutionResult(MaxIterationsTrainer trainer) {
		this.iterations = trainer.getCurrentIteration();
		this.optimalFitness = trainer.getOptimalFitness();
		this.executionTime = trainer.getExecutionTime();
		this.bestIteration = trainer.getBestIteration();
		this.timeToBestIteration = trainer.getTimeToBestIteration();
	}
	
	
	
	public ExecutionResult(int runs, int iterations, double optimalFitness, long executionTime, int bestIteration) {
		super();
		this.runs = runs;
		this.iterations = iterations;
		this.optimalFitness = optimalFitness;
		this.executionTime = executionTime;
		this.bestIteration = bestIteration;
	}


	public int getRuns() {
		return runs;
	}
	
	public int getIterations() {
		return iterations;
	}
	
	public double getOptimalFitness() {
		return optimalFitness;
	}
	
	public long getExecutionTime() {
		return executionTime;
	}
	
	public int getBestIteration() {
		return bestIteration;
	}
	
	public long getTimeToBestIteration() {
		return timeToBestIteration;
	}
	
}
