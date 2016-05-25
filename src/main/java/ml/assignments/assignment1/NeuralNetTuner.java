package ml.assignments.assignment1;

import ml.assignments.CommandLineOptions;
import weka.classifiers.functions.MultilayerPerceptron;

import java.util.Map.Entry;

public class NeuralNetTuner extends AbstractClassifierTuner {

	public MultilayerPerceptron ann;
	private double[] learningRates = { 0.2, 0.4, 0.8 };
	private double[] momenta = { 0, 0.3, 0.6 };
	private int minHiddenUnits = 5;
	private int maxHiddenUnits;

	public NeuralNetTuner() {
		classifier = new MultilayerPerceptron();
		ann = (MultilayerPerceptron) classifier;
	}

	public void run(CommandLineOptions options) throws Exception {
		initialize(options);
		maxHiddenUnits = dataSet.numAttributes() + 2;
		for (int hiddenUnits = minHiddenUnits; hiddenUnits < maxHiddenUnits; hiddenUnits+=2) {
			for (double momentum : momenta) {
				for (double learningRate : learningRates) {
					ann.setHiddenLayers(String.valueOf(hiddenUnits));
					ann.setMomentum(momentum);
					ann.setLearningRate(learningRate);
					singleRun("hiddenUnits=" + hiddenUnits + ";momentum=" + momentum + ";learningRate=" + learningRate);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		NeuralNetTuner tuner = new NeuralNetTuner();
		CommandLineOptions options = CommandLineOptions.instance(args);
		tuner.run(options);
		tuner.getBestResult();
	}
}
