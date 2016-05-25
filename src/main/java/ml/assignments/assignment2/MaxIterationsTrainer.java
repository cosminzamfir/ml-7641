package ml.assignments.assignment2;

import java.util.HashMap;
import java.util.Map;

import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.MIMIC;
import shared.Instance;
import shared.Trainer;
import util.Utils;

/**
 */
public class MaxIterationsTrainer implements Trainer {
    
    private static final Map<Class, Integer> giveUpThreshold = new HashMap<>();
	private OptimizationAlgorithm trainer;
    
    private int maxIterations;
    private long maxTrainingTime;
    private double targetFitness;
    
    private long executionTime;
    private int currentIteration = 0;
    private int bestIteration;
    private double bestFitness = Double.NEGATIVE_INFINITY;
    private int noImprovementConsecutiveIterations = 0;
	private long timeToBestIteration;
    
    static {
    	giveUpThreshold.put(RandomizedHillClimbing.class, 5000);
    	giveUpThreshold.put(SimulatedAnnealing.class, 5000);
    	giveUpThreshold.put(StandardGeneticAlgorithm.class, 5000);
    	giveUpThreshold.put(MIMIC.class, 5000);
    }
    
    public MaxIterationsTrainer(OptimizationAlgorithm trainer, int maxIterations, long maxTrainingTime, double targetFitness) {
    	this.trainer = trainer;
    	this.maxIterations = maxIterations;
    	this.maxTrainingTime = maxTrainingTime;
    	this.targetFitness = targetFitness;
    }
    
    private int getGiveUpThrehold() {
    	return giveUpThreshold.get(trainer.getClass());
    }

    public double train() {
        long startTime = System.currentTimeMillis();
    	double fitness = 0;
    	while(true) {
            currentIteration ++;
            trainer.train();
            fitness = (trainer.getOptimizationProblem().value(trainer.getOptimal()));
            //System.out.println(trainer + ". current best fitness: " + fitness);
            if(fitness > bestFitness) {
            	bestIteration = currentIteration;
            	timeToBestIteration = System.currentTimeMillis() - startTime;
            	bestFitness = fitness;
            	noImprovementConsecutiveIterations = 0;
            } else {
            	noImprovementConsecutiveIterations ++;
            }
            
            if(noImprovementConsecutiveIterations > getGiveUpThrehold()) {
            	executionTime = System.currentTimeMillis() - startTime;
            	Utils.debug("Exit " + trainer.getClass().getSimpleName() + ". No improvements after " + getGiveUpThrehold() + " iterations.");
            	return bestFitness;
            }
            
            if(targetFitness > 0 && fitness == targetFitness) {
            	executionTime = System.currentTimeMillis() - startTime;
            	
            	Utils.debug("Exit " + trainer.getClass().getSimpleName() + ". Target fitness reached.");
            	return targetFitness;
            }
            if(maxTrainingTime>0 && System.currentTimeMillis() - startTime > maxTrainingTime) {
            	executionTime = System.currentTimeMillis() - startTime;
            	Utils.debug("Exit " + trainer.getClass().getSimpleName() + ". Max training time reached.");
            	return bestFitness;
            }
            if(maxIterations > 0 && currentIteration >= maxIterations) {
            	executionTime = System.currentTimeMillis() - startTime;
            	Utils.debug("Exit " + trainer.getClass().getSimpleName() + ". Max iterations reached.");
            	return bestFitness;
            }
        }
    }
    
    public int getCurrentIteration() {
		return currentIteration;
	}
    
    public long getExecutionTime() {
		return executionTime;
	}
    
    public double getOptimalFitness() {
    	return bestFitness;
    }
    
    public int getBestIteration() {
		return bestIteration;
	}

	public long getTimeToBestIteration() {
		return timeToBestIteration;
	}
	
	public Instance getOptimal() {
		return trainer.getOptimal();
	}
}
