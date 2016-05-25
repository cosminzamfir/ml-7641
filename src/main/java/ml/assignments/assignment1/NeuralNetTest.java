package ml.assignments.assignment1;

import java.util.ArrayList;
import java.util.List;

import ml.assignments.CommandLineOptions;
import ml.assignments.GeneralChart;
import ml.assignments.MLAssignmentUtils;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

public class NeuralNetTest {

	static int initialHiddenLayers = 5;
	static double[][] accuracies ;
	static double[][] crossValidatedAccuracies;

	public static void main(String[] args) throws Exception {
		CommandLineOptions options = CommandLineOptions.instance(args);
		initArrays(options);
		MultilayerPerceptron classifier = MLAssignmentUtils.buildNeuralNet(options);
		ClassifierRunner runner = new ClassifierRunner(classifier, options);
		Instances dataSet = MLAssignmentUtils.buildInstancesFromResource(options.getDataSetName());
		dataSet = MLAssignmentUtils.shufle(dataSet);
		int size = dataSet.size();
		
		Instances training = new Instances(dataSet, 0, options.getTrainingSize());
		Instances test = new Instances(dataSet, size - options.getTestSize(), options.getTestSize());
		for (int i = 0; i < options.getRuns(); i ++) {
			int hiddentUnits = options.getInitialSize() + i;
			classifier.setHiddenLayers(String.valueOf(hiddentUnits));
			runner.buildModel(training);
			Evaluation testSetEvaluation = runner.evaluateModel(training, test);
			accuracies[i][0] = hiddentUnits;
			accuracies[i][1] = testSetEvaluation.pctIncorrect();
			
			Evaluation crossValidation = runner.crossValidate(training);
			crossValidatedAccuracies[i][0] = hiddentUnits;
			crossValidatedAccuracies[i][1] = crossValidation.pctIncorrect();
			
		}
		List<double[][]> data = new ArrayList<>();
		List<String> titles = new ArrayList<>();
		data.add(accuracies);
		data.add(crossValidatedAccuracies);
		titles.add("Test error rate as function of hidden units");
		titles.add("Cross-validation error rate as function of hidden units");
		String title = classifier.getClass().getSimpleName() + " - error rates versus hidden units : " + options.getDataSetName();
		new GeneralChart(title, data, titles, "Hidden Units", "Accuracy");
	}

	private static void initArrays(CommandLineOptions options) {
		accuracies = new double[options.getRuns()][2];
		crossValidatedAccuracies = new double[options.getRuns()][2];
		
	}
}
