package ml.assignments.assignment1;

import java.util.Date;
import java.util.Random;

import ml.assignments.CommandLineOptions;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 * Run a classifier algo on a given data set, given trainingSize and testSize and return the {@link Evaluation}
 * @author eh2zamf
 *
 */
public class ClassifierRunner {

	private Classifier classifier;
	private CommandLineOptions commandLineOptions;

	public ClassifierRunner(Classifier classifier, CommandLineOptions commandLineOptions) {
		super();
		this.classifier = classifier;
		this.commandLineOptions = commandLineOptions;
	}
	
	public void buildModel(Instances trainingDataSet) throws Exception {
		System.out.println(new Date() + ":" + "Buillding " + classifier.getClass().getName() + " classifier. Training size: " + trainingDataSet.size());
		classifier.buildClassifier(trainingDataSet);
		System.out.println(new Date() + ":" + "Building classifier done.");
	}
	
	public Evaluation evaluateModel(Instances training, Instances test) throws Exception {
		System.out.println(new Date() + ":" + "Validating " + classifier.getClass().getName() + " classifier.");
		Evaluation evaluation = new Evaluation(training);
		evaluation.evaluateModel(classifier, test);
		System.out.println(new Date() + ":" + "Evaluation done.");
		return evaluation;
	}
	
	public Evaluation crossValidate(Instances training) throws Exception {
		Evaluation crossValidation;
		System.out.println(new Date() + ":" + "Cross-validating " + classifier.getClass().getName() + " classifier.");
		crossValidation = new Evaluation(training);
		crossValidation.crossValidateModel(classifier, training, 10, new Random());
		System.out.println(new Date() + ":" + "Cross-validation done.");
		return crossValidation;
	}

	public Classifier getClassifier() {
		return classifier;
	}
}
