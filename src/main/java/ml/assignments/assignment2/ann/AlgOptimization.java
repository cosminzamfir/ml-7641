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
import opt.OptimizationAlgorithm;
import opt.example.NeuralNetworkOptimizationProblem;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import shared.SumOfSquaresError;
import util.linalg.Vector;

/*
 * nohup ./optimization rhc 50000 500 > rhc.log &
 * nohup ./optimization sa 50000 500 > sa.log &
 * nohup ./optimization ga,40,20,5 50000 500 > ga.log &
 */
public class AlgOptimization {
	
	private static Logger log = LoggerFactory.getLogger(AlgOptimization.class);

	protected ErrorMeasure measure;
	protected BackPropagationNetworkFactory factory;
    protected BackPropagationNetwork network;
    protected NeuralNetworkOptimizationProblem pb;
        
    protected OptimizationAlgorithm alg;
    protected int iterations;
    protected int step;
    
    protected final Map<Integer, AccuracyResult> trainingAccuracyMap = new HashMap<>();
    protected final Map<Integer, AccuracyResult> testAccuracyMap = new HashMap<>();
    
    public AlgOptimization() {
    	measure = new SumOfSquaresError();
		factory = new BackPropagationNetworkFactory();
	    network = factory.createClassificationNetwork(
                new int[] {inputLayer, hiddenLayer, outputLayer});
	    pb = new NeuralNetworkOptimizationProblem(trainingSet, network, measure);
    }
    
    public void init(OptimizationAlgorithm alg, int iterations, int step) {
    	this.alg = alg;
 	    this.iterations = iterations;
 	    this.step = step;
    }
    
    public void train() {
        log.info("\nError results for {}\n---------------------------", 
        		alg.getClass().getSimpleName());

        // set the first values
        AccuracyResult trainingAcc = accuracyArgMax(trainingSet);
        AccuracyResult testAcc = accuracyArgMax(testSet);
        trainingAccuracyMap.put(0, trainingAcc);
        testAccuracyMap.put(0, testAcc);
        
        // iterate
        for(int i = 1; i <= iterations; i++) {
        	Instance cur1 = alg.getOptimal();
            alg.train();
            Instance cur2 = alg.getOptimal();
            boolean progress = (cur1 != cur2);
            if (progress) {
	            trainingAcc = accuracyArgMax(trainingSet);
	            testAcc = accuracyArgMax(testSet);
            } else {
            	log.info("no progress, using earlier accuracy results...");
            }
            trainingAccuracyMap.put(i, trainingAcc);
            testAccuracyMap.put(i, testAcc);
            log.info("{}:{}",i, trainingAcc);
            log.info("{}:{}",i, testAcc);
        }
        
        // set the optimal values
        Instance optimal = alg.getOptimal();
        network.setWeights(optimal.getData());
        trainingAcc = accuracyArgMax(trainingSet);
        testAcc = accuracyArgMax(testSet);
        trainingAccuracyMap.put((iterations+1), trainingAcc);
        testAccuracyMap.put((iterations+1), testAcc);
    }
    
    public AccuracyResult accuracyAllOutputs(DataSet dataSet) {
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
    		
    		/*
    		log.info("instance: {}", actual);
    		log.info("predicted: {}", predicted);
    		*/
    		
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
    	return new AccuracyResult(dataSet.size(), correct, incorrect, error);
	}

    public AccuracyResult accuracyArgMax(DataSet dataSet) {
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
    		
    		if (actual.argMax() == predicted.argMax()) correct++;
    		else incorrect++;
    		
    	}
    	return new AccuracyResult(dataSet.size(), correct, incorrect, error);
	}

	public ErrorMeasure getMeasure() {
		return measure;
	}

	public BackPropagationNetwork getNetwork() {
		return network;
	}

	public NeuralNetworkOptimizationProblem getPb() {
		return pb;
	}

	public OptimizationAlgorithm getAlg() {
		return alg;
	}

	public int getIterations() {
		return iterations;
	}

	public int getStep() {
		return step;
	}

	public Map<Integer, AccuracyResult> getTrainingAccuracyMap() {
		return trainingAccuracyMap;
	}

	public Map<Integer, AccuracyResult> getTestAccuracyMap() {
		return testAccuracyMap;
	}
    
}
