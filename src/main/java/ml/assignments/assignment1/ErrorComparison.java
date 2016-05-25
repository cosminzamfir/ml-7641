package ml.assignments.assignment1;

import java.util.ArrayList;
import java.util.List;

import ml.assignments.CommandLineOptions;
import ml.assignments.GeneralChart;
import ml.assignments.MLAssignmentUtils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 * Plot the error in the training set and the error in the test set as a function of the training set.
 * @author eh2zamf
 *
 */
public class ErrorComparison {

	static double[][] trainingErrorRateVersusTrainingSize;
	static double[][] testErrorRateVersusTrainingSize;

	public static void main(String[] args) throws Exception {
		CommandLineOptions options = CommandLineOptions.instance(args);
		initArrays(options);
		Classifier classifier = options.getClassifier();
		
		ClassifierRunner runner = new ClassifierRunner(classifier, options);
		Instances dataSet = MLAssignmentUtils.buildInstancesFromResource(options.getDataSetName());
		dataSet = MLAssignmentUtils.shufle(dataSet);

		int size = dataSet.size();
		Instances test = new Instances(dataSet, size - options.getTestSize(), options.getTestSize());

		for (int i = 0; i < options.getRuns(); i++) {
			int trainingSize = options.getInitialSize() + i * options.getStepSize();
			Instances training = new Instances(dataSet, 0, trainingSize);
			runner.buildModel(training);
			Evaluation testSetEvalution = runner.evaluateModel(training, test);
			Evaluation trainingSetEvaluation = runner.evaluateModel(training, training);
			trainingErrorRateVersusTrainingSize[i][0] = trainingSize;
			trainingErrorRateVersusTrainingSize[i][1] = trainingSetEvaluation.pctIncorrect();

			testErrorRateVersusTrainingSize[i][0] = trainingSize;
			testErrorRateVersusTrainingSize[i][1] = testSetEvalution.pctIncorrect();
		}
		List<double[][]> data = new ArrayList<>();
		List<String> titles = new ArrayList<>();
		data.add(trainingErrorRateVersusTrainingSize);
		data.add(testErrorRateVersusTrainingSize);
		titles.add("Training error rate");
		titles.add("Test error rate");
		String title = MLAssignmentUtils.toString(classifier) + " - error rates versus training size: " + options.getDataSetName();
		new GeneralChart(title, data, titles, "Training size", "Error rate", true);
	}
	
	private static void initArrays(CommandLineOptions options) {
		trainingErrorRateVersusTrainingSize = new double[options.getRuns()][2];
		testErrorRateVersusTrainingSize = new double[options.getRuns()][2];
	}

}
