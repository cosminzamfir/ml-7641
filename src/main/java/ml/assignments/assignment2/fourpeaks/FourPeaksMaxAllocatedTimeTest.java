package ml.assignments.assignment2.fourpeaks;

import opt.OptimizationAlgorithm;

public class FourPeaksMaxAllocatedTimeTest {

	public static void main(String[] args) {

		new FourPeaksMaxAllocatedTime() {

			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(int n) {
				return FourPeaksUtils.buildRHC(n);
			}
		}.run();
	
		new FourPeaksMaxAllocatedTime() {
		
			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(int n) {
				return FourPeaksUtils.buildSA(n);
			}
		}.run();

		
		new FourPeaksMaxAllocatedTime() {
			
			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(int n) {
				return FourPeaksUtils.buildGA(n);
			}
		}.run();

		
		new FourPeaksMaxAllocatedTime() {
			
			@Override
			public OptimizationAlgorithm getOptimizationAlgorithm(int n) {
				return FourPeaksUtils.buildMIMIC(n);
			}
		}.run();

	}



}
