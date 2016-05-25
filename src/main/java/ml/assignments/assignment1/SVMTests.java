package ml.assignments.assignment1;

import java.util.ArrayList;
import java.util.List;

import ml.assignments.CommandLineOptions;
import ml.assignments.GeneralChart;
import ml.assignments.MLAssignmentUtils;
import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 * Plot the error in the training set and the error in the test set as a function of the training set.
 * @author eh2zamf
 *
 */
public class SVMTests {

	public static enum KernelFunction {
		Liniar, Quadratic, Cubic, _4GradePolynomial, _5GradePolynomial, _6GradePolynomial, Radial, Sigmoid
	}

	static int initialTrainingSize = 100;
	static int runs = 30;
	static int step = 10;
	static int testSize = 100;
	static boolean useLibSVM = false;
	static List<double[][]> accuracyVersusTrainingSizeByKernel = new ArrayList<>();
	static List<KernelFunction> kernelFunctions = new ArrayList<>();
	static List<String> titles = new ArrayList<>();
	static String dataSetName = "circles.arff";

	static {
		kernelFunctions.add(KernelFunction.Liniar);
		kernelFunctions.add(KernelFunction.Quadratic);
		kernelFunctions.add(KernelFunction.Cubic);
		//		kernelFunctions.add(KernelFunction._4GradePolynomial);
		//		kernelFunctions.add(KernelFunction._5GradePolynomial);
		//		kernelFunctions.add(KernelFunction._6GradePolynomial);
		kernelFunctions.add(KernelFunction.Radial);
		for (KernelFunction function : kernelFunctions) {
			titles.add(function.toString());
		}
	}

	public static void main(String[] args) throws Exception {

		CommandLineOptions options = CommandLineOptions.instance(args);
		initialize();
		Instances dataSet = MLAssignmentUtils.buildInstancesFromResource(dataSetName);
		dataSet = MLAssignmentUtils.shufle(dataSet);

		ClassifierRunner runner = null;
		for (int k = 0; k < kernelFunctions.size(); k++) {
			KernelFunction function = kernelFunctions.get(k);
			runner = new ClassifierRunner(useLibSVM ? MLAssignmentUtils.buildLibSVM(function, options) : MLAssignmentUtils.buildSMOSVM(function, options), options);
			for (int i = 0; i < runs; i++) {
				int trainingSize = initialTrainingSize + i * step;
				Instances training = new Instances(dataSet, 0, trainingSize);
				Instances test = new Instances(dataSet, dataSet.size() - testSize, testSize);
				runner.buildModel(training);
				Evaluation eval = runner.evaluateModel(training, test);
				accuracyVersusTrainingSizeByKernel.get(k)[i][0] = trainingSize;
				accuracyVersusTrainingSizeByKernel.get(k)[i][1] = eval.pctCorrect();
			}
		}
		new GeneralChart("Accuracy versus Training Size by kernel for " + MLAssignmentUtils.toString(runner.getClassifier()),
				accuracyVersusTrainingSizeByKernel, titles, "Training size", "Accuracy");

	}

	private static void initialize() {
		for (int i = 0; i < kernelFunctions.size(); i++) {
			accuracyVersusTrainingSizeByKernel.add(new double[runs][2]);
		}
	}
}
