package ml.assignments.assignment4.labitinth;

public class ExecutionResult {

	private String algorithm;
	private int problemSize;
	private int iterations;
	private double resultQuality;
	private long start;
	private long end;

	public ExecutionResult(String algorithm, int problemSize, int iterations, double resultQuality) {
		super();
		this.algorithm = algorithm;
		this.problemSize = problemSize;
		this.iterations = iterations;
		this.resultQuality = resultQuality;
	}

	public ExecutionResult(String algorithm, int problemSize) {
		this.algorithm = algorithm;
		this.problemSize = problemSize;
	}
	
	public void start() {
		start = System.currentTimeMillis();
	}
	
	public void end() {
		end = System.currentTimeMillis();
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public int getProblemSize() {
		return problemSize;
	}

	public void setProblemSize(int problemSize) {
		this.problemSize = problemSize;
	}

	public long getTime() {
		return end - start;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public double getResultQuality() {
		return resultQuality;
	}

	public void setResultQuality(double resultQuality) {
		this.resultQuality = resultQuality;
	}
	
	@Override
	public String toString() {
		return algorithm + "," + problemSize + "," + getTime() + "," + iterations + "," + resultQuality; 
	}

	public static Object header() {
		return "Algorithm,ProblemSize,Time,Iterations,ResultQuality";
	}

}
