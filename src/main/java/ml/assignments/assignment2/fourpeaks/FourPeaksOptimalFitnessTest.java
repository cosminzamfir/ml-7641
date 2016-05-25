package ml.assignments.assignment2.fourpeaks;

import opt.OptimizationAlgorithm;

public class FourPeaksOptimalFitnessTest {

	static boolean RHC = false;
	static boolean SA = true;
	static boolean GA = true;
	static boolean MIMIC = true;
	
	public static void main(String[] args) {

		if (RHC) 
		new FourPeaksOptimalFitness() {

			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(int n) {
				return FourPeaksUtils.buildRHC(n);
			}
		}.run();

		if (SA) 
		new FourPeaksOptimalFitness() {

			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(int n) {
				return FourPeaksUtils.buildSA(n);
			}
		}.run();

		if (GA) 
		new FourPeaksOptimalFitness() {

			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(int n) {
				return FourPeaksUtils.buildGA(n);
			}
		}.run();

		if (MIMIC) 
		new FourPeaksOptimalFitness() {

			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(int n) {
				return FourPeaksUtils.buildMIMIC(n);
			}
		}.run();

	}

}
