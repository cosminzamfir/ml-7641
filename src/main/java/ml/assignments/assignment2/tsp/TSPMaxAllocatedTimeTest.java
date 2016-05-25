package ml.assignments.assignment2.tsp;

import opt.OptimizationAlgorithm;

public class TSPMaxAllocatedTimeTest {

	static boolean RHC = true;
	static boolean SA = true;
	static boolean GA = true;
	static boolean MIMIC = true;

	public static void main(String[] args) {

		if(RHC)
		new TSPMaxAllocatedTime() {

			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(double[][] points) {
				return TSPUtils.buildRHC(points);
			}
		}.run();
	
		if(SA)
		new TSPMaxAllocatedTime() {
		
			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(double[][] points) {
				return TSPUtils.buildSA(points);
			}
		}.run();

		
		if(GA)
		new TSPMaxAllocatedTime() {
			
			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(double[][] points) {
				return TSPUtils.buildGA(points);
			}
		}.run();

		
		if(MIMIC)
		new TSPMaxAllocatedTime() {
			
			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(double[][] points) {
				return TSPUtils.buildMIMIC(points);
			}
		}.run();

	}



}
