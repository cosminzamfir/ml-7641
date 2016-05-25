package ml.assignments.assignment1;

import java.util.Map.Entry;

import ml.assignments.CommandLineOptions;
import ml.assignments.MLAssignmentUtils;
import ml.assignments.assignment1.SVMTests.KernelFunction;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.RBFKernel;

public class LibSVMTuner extends AbstractClassifierTuner {

	public LibSVMTuner() {
		classifier = new LibSVM();
	}

	public void run(CommandLineOptions options) throws Exception {
		initialize(options);

		KernelFunction function = KernelFunction.Liniar;
		classifier = MLAssignmentUtils.buildLibSVM(function, options);
		singleRun("Kernel=" + function);

		function = KernelFunction.Quadratic;
		classifier = MLAssignmentUtils.buildLibSVM(function, options);
		singleRun("Kernel=" + function);

		function = KernelFunction.Cubic;
		classifier = MLAssignmentUtils.buildLibSVM(function, options);
		singleRun("Kernel=" + function);

		function = KernelFunction.Radial;
		classifier = MLAssignmentUtils.buildLibSVM(function, options);
		double initialGamma = 1d / dataSet.numAttributes();
		for (int i = 0; i < 5; i++) {
			double gamma = initialGamma + i * 0.3;
			((LibSVM) classifier).setGamma(gamma);
			singleRun("Kernel=" + function + ";gamma=" + gamma);
		}

		function = KernelFunction.Sigmoid;
		classifier = MLAssignmentUtils.buildLibSVM(function, options);
		singleRun("Kernel=" + function);

	}

	public static void main(String[] args) throws Exception {
		LibSVMTuner tuner = new LibSVMTuner();
		CommandLineOptions options = CommandLineOptions.instance(args);
		tuner.run(options);
		tuner.getBestResult();
	}
}
