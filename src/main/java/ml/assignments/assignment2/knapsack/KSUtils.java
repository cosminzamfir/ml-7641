package ml.assignments.assignment2.knapsack;

import java.util.Arrays;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.KnapsackEvaluationFunction;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

public class KSUtils {

    public static final int COPIES_EACH = 1;
    /** The maximum volume for a single element */
    public static final double MAX_VOLUME = 50000;
    /** The maximum weight for a single element */
    public static final double MAX_WEIGHT = 50000;



	public static OptimizationAlgorithm buildRHC(int[] copies, double[] weights, double[] volumes, double maxVolume) {
        int[] ranges = getRanges(weights.length);
        EvaluationFunction ef = new KnapsackEvaluationFunction(weights, volumes, maxVolume, copies);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
    	return new RandomizedHillClimbing(hcp);
    }
    
	public static OptimizationAlgorithm buildSA(int[] copies, double[] weights, double[] volumes, double maxVolume) {
        int[] ranges = getRanges(weights.length);
        EvaluationFunction ef = new KnapsackEvaluationFunction(weights, volumes, maxVolume, copies);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        return new SimulatedAnnealing(100, .95, hcp);
    }
    
    public static OptimizationAlgorithm buildGA(int[] copies, double[] weights, double[] volumes, double maxVolume) {
        int[] ranges = getRanges(weights.length);
        EvaluationFunction ef = new KnapsackEvaluationFunction(weights, volumes, maxVolume, copies);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new UniformCrossOver();
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        return new StandardGeneticAlgorithm(200, 150, 25, gap);
    }
    
    public static OptimizationAlgorithm buildMIMIC(int[] copies, double[] weights, double[] volumes, double maxVolume) {
        int[] ranges = getRanges(weights.length);
        EvaluationFunction ef = new KnapsackEvaluationFunction(weights, volumes, maxVolume, copies);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        Distribution df = new DiscreteDependencyTree(.1, ranges); 
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        return new MIMIC(200, 100, pop);
    }
    
    private static int[] getRanges(int noItems) {
    	int[] ranges = new int[noItems];
    	Arrays.fill(ranges, COPIES_EACH +1);
    	return ranges;
    }
    
}
