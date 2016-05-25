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
public class LearningCurve {

	static List<double[][]> trainingError  = new ArrayList<>();
	static List<double[][]> testError = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		CommandLineOptions options = CommandLineOptions.instance(args);
		initArrays(options);
		List<Classifier> classifiers = options.getClassifiers();
		configureClassifiers(classifiers);

		List<double[][]> data = new ArrayList<>();
		List<String> titles = new ArrayList<>();

		for (int classifierIndex = 0; classifierIndex < classifiers.size(); classifierIndex ++) {

			ClassifierRunner runner = new ClassifierRunner(classifiers.get(classifierIndex), options);
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
				trainingError.get(classifierIndex)[i][0] = trainingSize;
				trainingError.get(classifierIndex)[i][1] = trainingSetEvaluation.pctIncorrect();

				testError.get(classifierIndex)[i][0] = trainingSize;
				testError.get(classifierIndex)[i][1] = testSetEvalution.pctIncorrect();
			}
			data.add(trainingError.get(classifierIndex));
			data.add(testError.get(classifierIndex));
			titles.add(classifiers.get(classifierIndex).getClass().getSimpleName() + " train error");
			titles.add(classifiers.get(classifierIndex).getClass().getSimpleName() + " test error");
		}
		String title = "Learning curve - " + options.getDataSetName();
		new GeneralChart(title, data, titles, "Training size", "Error rate", true);
	}

	/**
	 * Set the best params found during tuning
	 * @param classifiers
	 */
	private static void configureClassifiers(List<Classifier> classifiers) {
		// TODO Auto-generated method stub

	}

	private static void initArrays(CommandLineOptions options) {
		for (int i = 0; i < options.getClassifiers().size(); i++) {
			trainingError.add(new double[options.getRuns()][2]);
			testError.add(new double[options.getRuns()][2]);
		}
	}

}
