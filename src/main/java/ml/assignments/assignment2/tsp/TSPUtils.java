package ml.assignments.assignment2.tsp;

import java.util.Arrays;

import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.SwapNeighbor;
import opt.example.TravelingSalesmanCrossOver;
import opt.example.TravelingSalesmanEvaluationFunction;
import opt.example.TravelingSalesmanRouteEvaluationFunction;
import opt.example.TravelingSalesmanSortEvaluationFunction;
import opt.ga.CrossoverFunction;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.SwapMutation;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

public class TSPUtils {

    public static OptimizationAlgorithm buildRHC(double[][] points) {
        Distribution odd = new DiscretePermutationDistribution(points.length);
    	TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanRouteEvaluationFunction(points);
    	NeighborFunction nf = new SwapNeighbor();
    	HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
    	return new RandomizedHillClimbing(hcp);
    }
    
    public static OptimizationAlgorithm buildSA(double[][] points) {
        Distribution odd = new DiscretePermutationDistribution(points.length);
    	TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanRouteEvaluationFunction(points);
    	NeighborFunction nf = new SwapNeighbor();
    	HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        return new SimulatedAnnealing(1E12, .95, hcp);
    }
    
    public static OptimizationAlgorithm buildGA(double[][] points) {
        TravelingSalesmanEvaluationFunction ef = new TravelingSalesmanRouteEvaluationFunction(points);
        Distribution odd = new DiscretePermutationDistribution(points.length);
        MutationFunction mf = new SwapMutation();
        CrossoverFunction cf = new TravelingSalesmanCrossOver(ef);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        return new StandardGeneticAlgorithm(200, 150, 20, gap);
    }
    
    public static OptimizationAlgorithm buildMIMIC(double[][] points) {
        int N = points.length;
    	int[] ranges = new int[N];
        Arrays.fill(ranges, N);
        TravelingSalesmanSortEvaluationFunction ef = new TravelingSalesmanSortEvaluationFunction(points);
        // for mimic we use a sort encoding.
        DiscreteUniformDistribution odd = new  DiscreteUniformDistribution(ranges);
        Distribution df = new DiscreteDependencyTree(.1, ranges); 
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        return new MIMIC(200, 100, pop);
    }
}
