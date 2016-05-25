package ml.assignments.assignment2.ann;

import static ml.assignments.assignment2.ann.DataSetup.hiddenLayer;
import static ml.assignments.assignment2.ann.DataSetup.inputLayer;
import static ml.assignments.assignment2.ann.DataSetup.nrLabels;
import static ml.assignments.assignment2.ann.DataSetup.outputLayer;
import static ml.assignments.assignment2.ann.DataSetup.testSet;
import static ml.assignments.assignment2.ann.DataSetup.trainingSet;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;
import opt.RandomizedHillClimbing;
import opt.example.NeuralNetworkOptimizationProblem;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import shared.SumOfSquaresError;
import util.linalg.Vector;

public class RHCOptimization {
	
	private static Logger log = LoggerFactory.getLogger(RHCOptimization.class);

	private ErrorMeasure measure;
	private BackPropagationNetworkFactory factory;
    private BackPropagationNetwork network;
    private NeuralNetworkOptimizationProblem pb;
    private RandomizedHillClimbing alg;
    
    private final Map<Integer, AccuracyResult> trainingAccuracyMap = new HashMap<>();
    private final Map<Integer, AccuracyResult> testAccuracyMap = new HashMap<>();
    private final int iterations;
    
    public RHCOptimization(int iterations) {
    	this.iterations = iterations;
    	init();
    }

	private void init() {
		measure = new SumOfSquaresError();
		factory = new BackPropagationNetworkFactory();
	    network = factory.createClassificationNetwork(
                new int[] {inputLayer, hiddenLayer, outputLayer});
	    pb = new NeuralNetworkOptimizationProblem(trainingSet, network, measure);
	    alg = new RandomizedHillClimbing(pb);
	}
	
	public void train() {
        log.info("\nError results for RHC \n---------------------------");

        // set the first values
        AccuracyResult trainingAcc = accuracy(trainingSet);
        AccuracyResult testAcc = accuracy(testSet);
        trainingAccuracyMap.put(0, trainingAcc);
        testAccuracyMap.put(0, testAcc);
        
        // iterate
        for(int i = 1; i <= iterations; i++) {
        	Instance cur1 = alg.getOptimal();
            alg.train();
            Instance cur2 = alg.getOptimal();
            boolean progress = (cur1 != cur2);
            if (progress) {
	            trainingAcc = accuracy(trainingSet);
	            testAcc = accuracy(testSet);
            } else {
            	log.info("no progress, using earlier accuracy results...");
            }
            trainingAccuracyMap.put(i, trainingAcc);
            testAccuracyMap.put(i, testAcc);
            log.info("{}", trainingAcc);
            log.info("{}", testAcc);
        }
        
        // set the optimal values
        Instance optimal = alg.getOptimal();
        network.setWeights(optimal.getData());
        trainingAcc = accuracy(trainingSet);
        testAcc = accuracy(testSet);
        trainingAccuracyMap.put((iterations+1), trainingAcc);
        testAccuracyMap.put((iterations+1), testAcc);
        
    }
	
	public AccuracyResult accuracy(DataSet dataSet) {
    	int correct = 0;
    	int incorrect = 0;
    	double error = 0;
    	for (int j = 0; j < dataSet.size(); j++) {
    		Instance instance = dataSet.get(j);
    		network.setInputValues(instance.getData());
    		network.run();
    		Vector predicted = network.getOutputValues();
    		error += measure.value(new Instance(predicted), instance);
    		Vector actual = instance.getLabel().getData();
    		boolean ok = true;
    		for (int i = 0; i < nrLabels; i++) {
    			double predictedValue = predicted.get(i);
    			double actualValue = actual.get(i);
    			if (Math.abs(predictedValue - actualValue) >= 0.5) {
    				ok = false;
    			}
    		}
    		if (ok) correct++;
    		else incorrect++;
    	}
    	//log.info("correct: {}, incorrect: {}, error: {}", correct, incorrect, error);
    	return new AccuracyResult(dataSet.size(), correct, incorrect, error);
	}
	
	public Map<Integer, AccuracyResult> getTrainingAccuracy() {
		return trainingAccuracyMap;
	}
	
	public Map<Integer, AccuracyResult> getTestAccuracy() {
		return testAccuracyMap;
	}
	
	public int getIterations() {
		return iterations;
	}
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("2 params needed: #iterations and step size for excel printing");
			System.exit(0);
		}
		int iterations = Integer.valueOf(args[0]);
		int step = Integer.valueOf(args[1]);
		
		RHCOptimization rhc = new RHCOptimization(iterations);
		log.info("Start RHC, using {} iterations", iterations);
		rhc.train();
		Map<Integer, AccuracyResult> trainingAccuracy = rhc.getTrainingAccuracy();
		Map<Integer, AccuracyResult> testAccuracy = rhc.getTestAccuracy();
		
		log.info("final results========================");
		AccuracyResult startTrainingAccuracy = trainingAccuracy.get(0);
		AccuracyResult finalTrainingAccuracy = trainingAccuracy.get(iterations+1);
		
		AccuracyResult startTestAccuracy = testAccuracy.get(0);
		AccuracyResult finalTestAccuracy = testAccuracy.get(iterations+1);
		
		log.info("start training accuracy: {}", startTrainingAccuracy);
		log.info("final training accuracy: {}", finalTrainingAccuracy);
		
		log.info("start test accuracy: {}", startTestAccuracy);
		log.info("final test accuracy: {}", finalTestAccuracy);
		
		log.info("End RHC");
		
		///////////////////////////////////////////////////
		System.out.println("Excel data start @@@@@@");
		
		System.out.println("training accuracy start #####");
		int next = 1;
		while (next < trainingAccuracy.size()) {
			AccuracyResult trainingRes = trainingAccuracy.get(next);			
			System.out.println(next + "," + trainingRes.toCommaString());
			next += step;
		}
		System.out.println("training accuracy end #####");
		
		System.out.println("test accuracy start #####");
		next = 1;
		while (next < testAccuracy.size()) {
			AccuracyResult testRes = testAccuracy.get(next);			
			System.out.println(next + "," + testRes.toCommaString());
			next += step;
		}
		System.out.println("test accuracy end #####");
		
		System.out.println("Excel data end @@@@@");
	}
	
}
