package ml.assignments.assignment2.fourpeaks;

import java.util.Arrays;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.FourPeaksEvaluationFunction;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.SingleCrossOver;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

public class FourPeaksUtils {

	public static RandomizedHillClimbing buildRHC(int n) {
		int t = n/10;
		int[] ranges = new int[n];
		Arrays.fill(ranges, 2);
		EvaluationFunction ef = new FourPeaksEvaluationFunction(t);
		Distribution odd = new DiscreteUniformDistribution(ranges);
		NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
		HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
		RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
		return rhc;
	}
	
	public static SimulatedAnnealing buildSA(int n) {
		int t = n/10;
		int[] ranges = new int[n];
		Arrays.fill(ranges, 2);
		EvaluationFunction ef = new FourPeaksEvaluationFunction(t);

		Distribution odd = new DiscreteUniformDistribution(ranges);
		NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
		HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);

		SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);

		return sa;
	}
	
	public static StandardGeneticAlgorithm buildGA(int n) {
		int t = n/10;
		int[] ranges = new int[n];
		Arrays.fill(ranges, 2);
		EvaluationFunction ef = new FourPeaksEvaluationFunction(t);

		Distribution odd = new DiscreteUniformDistribution(ranges);
		MutationFunction mf = new DiscreteChangeOneMutation(ranges);
		CrossoverFunction cf = new SingleCrossOver();
		GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
		StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
		return ga;
	}
	
	public static MIMIC buildMIMIC(int n) {
		int t = n/10;
		int[] ranges = new int[n];
		Arrays.fill(ranges, 2);
		EvaluationFunction ef = new FourPeaksEvaluationFunction(t);

		Distribution odd = new DiscreteUniformDistribution(ranges);
		Distribution df = new DiscreteDependencyTree(.1, ranges);
		ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
		MIMIC mimic = new MIMIC(200, 20, pop);

		return mimic;

	}

}
