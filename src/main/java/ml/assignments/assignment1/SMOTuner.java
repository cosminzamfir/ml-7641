package ml.assignments.assignment1;

import ml.assignments.CommandLineOptions;
import ml.assignments.MLAssignmentUtils;
import ml.assignments.assignment1.SVMTests.KernelFunction;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.RBFKernel;

import java.util.Map.Entry;

public class SMOTuner extends AbstractClassifierTuner {

    public SMOTuner() {
        classifier = new SMO();
    }

    public void run(CommandLineOptions options) throws Exception {
        initialize(options);

        KernelFunction function = KernelFunction.Liniar;
        classifier = MLAssignmentUtils.buildSMOSVM(function, options);
        singleRun("Kernel=" + function);

        function = KernelFunction.Quadratic;
        classifier = MLAssignmentUtils.buildSMOSVM(function, options);
        singleRun("Kernel=" + function);

        function = KernelFunction.Cubic;
        classifier = MLAssignmentUtils.buildSMOSVM(function, options);
        singleRun("Kernel=" + function);

        function = KernelFunction.Radial;
        classifier = MLAssignmentUtils.buildSMOSVM(function, options);
        double initialGamma = 1d/dataSet.numAttributes();
        for (int i = 0; i < 5; i++) {
			double gamma = initialGamma + i * 0.3;
			((RBFKernel) ((SMO) classifier).getKernel()).setGamma(gamma);
	        singleRun("Kernel=" + function + ";gamma=" + gamma);
		}
    }

    public static void main(String[] args) throws Exception {
        SMOTuner tuner = new SMOTuner();
        CommandLineOptions options = CommandLineOptions.instance(args);
        tuner.run(options);
        tuner.getBestResult();
    }
}
