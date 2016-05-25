package ml.assignments.assignment1;

import java.util.LinkedHashMap;

import ml.assignments.CommandLineOptions;
import ml.assignments.MLAssignmentUtils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public abstract class AbstractClassifierTuner {

	protected double trainingPerc = 0.7;
	protected double testPec = 0.3;
	protected Instances dataSet;
	protected Instances training;
	protected Instances test;
	protected Classifier classifier;
	protected LinkedHashMap<String, Double> results = new LinkedHashMap<>(); //map errorRate to Description
	
	public abstract void run(CommandLineOptions options) throws Exception;

	public void initialize(CommandLineOptions options) throws Exception {
		dataSet = MLAssignmentUtils.buildInstancesFromResource(options.getDataSetName());
		dataSet = MLAssignmentUtils.shufle(dataSet);
		//            training = new Instances(dataSet, 0, (int) (dataSet.size() * trainingPerc));
		//            test = new Instances(dataSet, training.size(), dataSet.size() - training.size());
		training = new Instances(dataSet, 0, options.getTrainingSize());
		test = new Instances(dataSet, dataSet.size() - options.getTestSize(), options.getTestSize());
	}

	public double singleRun(String description) throws Exception {
		classifier.buildClassifier(training);
		Evaluation evaluation = new Evaluation(training);
		
		evaluation.evaluateModel(classifier, test);
		double errorRate = evaluation.pctIncorrect();
		System.out.println(classifier.getClass().getSimpleName() + ":" + description + " - errorRate:" + errorRate);
		results.put(description, errorRate);
		return errorRate;
	}

	public String getBestResult() {
		double bestErrorRate = Double.POSITIVE_INFINITY;
		String bestParams = null;
		System.out.println("==========================================================");
		System.out.println("Results for " + this.getClass().getSimpleName());
		System.out.println("==========================================================");
		for (String desc : results.keySet()) {
			System.out.println(desc + " : errorRate = " + results.get(desc));
			if (results.get(desc) < bestErrorRate) {
				bestErrorRate = results.get(desc);
				bestParams = desc;
			}
		}
		String res = bestParams + " - errorRate = " + bestErrorRate;
		System.out.println("==========================================================");
		System.out.println("Best result for " + this.getClass().getSimpleName() + " - " + res);
		return res;
	}

}
