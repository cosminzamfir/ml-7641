package ml.assignments.assignment4.labitinth;


public class RunLabFinder {

	public static String outputPath = "c:/work/data/transfer/lab/";
	static int start = 23;
	static int end = 23;
	static int step = 2;
	static ExecutionResults results = new ExecutionResults();

	public static void main(String[] args) {
		for (int size = start; size <= end; size+=step) {
			System.out.println("==================== " + size + " ===============================");
			run(size, false, false, false, true);
		}
		System.out.println(results);
	}

	private static void run(int size, boolean runBFS, boolean runVi, boolean runPi, boolean runQ) {
		ExecutionResults results = new ExecutionResults();
		LabFinder finder = new LabFinder(size);
		if(runBFS) {
			results.newResult(finder.bfs(outputPath));
		}
		if (runVi) {
			results.newResult(finder.valueIteration(outputPath));
		}
		if (runPi) {
			results.newResult(finder.policyIteration(outputPath));
		}
		if (runQ) {
			results.newResult(finder.qLearning(outputPath));
		}
		System.out.println("=================== Resultst ========================");
		//finder.visualize(outputPath);
	}
}
