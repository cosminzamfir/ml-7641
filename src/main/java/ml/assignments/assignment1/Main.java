package ml.assignments.assignment1;

import ml.assignments.CommandLineOptions;
import ml.assignments.MLAssignmentUtils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class Main {

	public static void main(String[] args) throws Exception {
		CommandLineOptions options = CommandLineOptions.instance(args);
		String dataSetName = options.getDataSetName();
		int trainingSize = options.getTrainingSize();
		int testSize = options.getTestSize();
		Instances dataSet = MLAssignmentUtils.buildInstancesFromResource(dataSetName);
		dataSet = MLAssignmentUtils.shufle(dataSet);
		//MLAssignmentUtils.write(options.getDataSetName() + ".shuffled", dataSet);
		Instances training = new Instances(dataSet, 0, trainingSize);
		Instances test = new Instances(dataSet, dataSet.size() - testSize, testSize);

		ClassifierRunner runner = new ClassifierRunner(options.getClassifier(), options);
		runner.buildModel(training);
		Classifier classifier = runner.getClassifier();
		System.out.println("===================== Classifier ==========================");
		System.out.println(classifier);

		Evaluation eval = runner.evaluateModel(training, training);
		System.out.println("=============== Evaluation on training set =========================");
		System.out.println(eval.toSummaryString(true));
		System.out.println(eval.toMatrixString());
		//System.out.println("True positive:" + eval.truePositiveRate(2));
		//System.out.println("True negative:" + eval.trueNegativeRate(1));
		//System.out.println("AUC:" + eval.truePositiveRate(2) * eval.trueNegativeRate(1));

		eval = runner.evaluateModel(training, test);
		System.out.println("=============== Evaluation on test set =========================");
		System.out.println(eval.toSummaryString(true));
		System.out.println(eval.toMatrixString());
		System.out.println(eval.pctIncorrect() + ".......");
		//System.out.println("True positive:" + eval.truePositiveRate(2));
		//System.out.println("True negative:" + eval.trueNegativeRate(1));
		//System.out.println("AUC:" + eval.truePositiveRate(2) * eval.trueNegativeRate(1));


		if (options.crossValidate()) {
			Evaluation crossValidation = runner.crossValidate(training);
			System.out.println("============== Cross - validation =========================");
			System.out.println(crossValidation.toSummaryString(true));
			System.out.println(crossValidation.toMatrixString());
		}

	}
}
