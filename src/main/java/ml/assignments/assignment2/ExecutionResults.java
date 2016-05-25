package ml.assignments.assignment2;

import java.util.ArrayList;
import java.util.List;

public class ExecutionResults {

	private List<ExecutionResult> results = new ArrayList<>();

	public void addResult(ExecutionResult executionResult) {
		this.results.add(executionResult);
	}
	
	public int getRuns() {
		int res = 0;
		for (ExecutionResult executionResult : results) {
			res += executionResult.getRuns();
		}
		return res/results.size();
	}

	public int getIterations() {
		int res = 0;
		for (ExecutionResult executionResult : results) {
			res += executionResult.getIterations();
		}
		return res/results.size();
	}
	
	public double getOptimalFitness() {
		double res = 0;
		for (ExecutionResult executionResult : results) {
			res += executionResult.getOptimalFitness();
		}
		return res/results.size();
	}
	
	public long getExecutionTime() {
		long res = 0;
		for (ExecutionResult executionResult : results) {
			res += executionResult.getExecutionTime();
		}
		return res/results.size();
	}

	public int getBestIteration() {
		int res = 0;
		for (ExecutionResult executionResult : results) {
			res += executionResult.getBestIteration();
		}
		return res/results.size();
	}

	public long getTimeToBestIteration() {
		long res = 0;
		for (ExecutionResult executionResult : results) {
			res += executionResult.getTimeToBestIteration();
		}
		return res/results.size();
	}
	
}
