package ml.assignments.assignment2.knapsack;

import opt.OptimizationAlgorithm;

public class KSSingleRunTest {

	
	static boolean RHC = false;
	static boolean SA = false;
	static boolean GA = true;
	static boolean MIMIC = false;

	public static void main(String[] args) {

		if(RHC)
		new KSSingleRun() {

			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(int[] copies, double[] weights, double[] volumes, double maxVolume) {
				return KSUtils.buildRHC(copies, weights, volumes, maxVolume);
			}
		}.run();
	
		if(SA)
		new KSSingleRun() {
		
			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(int[] copies, double[] weights, double[] volumes, double maxVolume) {
				return KSUtils.buildSA(copies, weights, volumes, maxVolume);
			}
		}.run();

		
		if(GA)
		new KSSingleRun() {
			
			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(int[] copies, double[] weights, double[] volumes, double maxVolume) {
				return KSUtils.buildGA(copies, weights, volumes, maxVolume);
			}
		}.run();

		
		if(MIMIC)
		new KSSingleRun() {
			
			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(int[] copies, double[] weights, double[] volumes, double maxVolume) {
				return KSUtils.buildMIMIC(copies, weights, volumes, maxVolume);
			}
		}.run();

	}



}
