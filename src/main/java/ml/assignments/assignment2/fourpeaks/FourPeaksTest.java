package ml.assignments.assignment2.fourpeaks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import ml.assignments.MLAssignmentUtils;
import ml.assignments.assignment2.ExecutionResult;
import ml.assignments.assignment2.MaxIterationsTrainer;
import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.SingleCrossOver;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class FourPeaksTest {
	private static int REPETITIONS = 3;
	private static int MAX_FITNESS;
	private static int MAX_ITERATIIONS = 1200000;
	private static int START_N = 10;
	private static int END_N = 120;
	private static int STEP = 5;

	private static String outputDir = System.getProperty("outputDir") == null ? "c:/work/transfer/" : System.getProperty("outputDir");
	private static String now = new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(new Date());
	private static String fileName = outputDir + "fourPeaksTest." + now + ".csv";
	
	public static void main(String[] args) {
		reachMaxFitness();
		//findBestFitnessGivenExecutionTime();
	}

	private static int computeMax(int N, int T) {
		return 2 * N - T - 1;
	}

	private static List<OptimizationAlgorithm> buildOptimizationAlgorithms(int n) {
		List<OptimizationAlgorithm> res = new ArrayList<>();
		res.add(FourPeaksUtils.buildRHC(n));
		res.add(FourPeaksUtils.buildSA(n));
		res.add(FourPeaksUtils.buildGA(n));
		res.add(FourPeaksUtils.buildMIMIC(n));

		return res;

	}

	public static void reachMaxFitness() {
		MLAssignmentUtils.writeToFile(fileName, "sep=,", false);
		MLAssignmentUtils.writeToFile(fileName, "Algorithm,N,BestFitness,BestFoundFitness,Iterations,IterationExecutionTime", true);
		for (int n = START_N; n <= END_N; n = n + STEP) {
			System.out.println();
			int t = n / 10;
			MAX_FITNESS = computeMax(n, t);
			for (int i = 0; i < REPETITIONS; i++) {
				List<OptimizationAlgorithm> algos = buildOptimizationAlgorithms(n);
				for (OptimizationAlgorithm optimizationAlgorithm : algos) {
					MaxIterationsTrainer trainer = new MaxIterationsTrainer(optimizationAlgorithm, MAX_ITERATIIONS, 0, MAX_FITNESS);
					ExecutionResult result = execute(trainer);
					print(optimizationAlgorithm, n, result.getOptimalFitness(), result.getBestIteration(), result.getExecutionTime());
				}
			}
		}
	}

	public static void findBestFitnessGivenExecutionTime() {
		MLAssignmentUtils.writeToFile(fileName, "sep=,", false);
		MLAssignmentUtils.writeToFile(fileName, "Algorithm,N,BestFoundFitness,Iterations,IterationExecutionTime", true);
		for (int n = START_N; n <= END_N; n = n + STEP) {
			System.out.println();
			long maxTime = n * 1000;
			for (int i = 0; i < REPETITIONS; i++) {
				List<OptimizationAlgorithm> algos = buildOptimizationAlgorithms(n);
				for (OptimizationAlgorithm optimizationAlgorithm : algos) {
					MaxIterationsTrainer trainer = new MaxIterationsTrainer(optimizationAlgorithm, 0, maxTime, 0);
					ExecutionResult result = execute(trainer);
					print(optimizationAlgorithm, n, result.getOptimalFitness(), result.getBestIteration(), result.getExecutionTime());
				}
			}
		}
	}

	private static ExecutionResult execute(MaxIterationsTrainer trainer) {
		trainer.train();
		return new ExecutionResult(trainer);
	}

	private static void print(OptimizationAlgorithm oa, int problemSize, double maxFitnessReached, int currentIteration, long executionTime) {
		String s = oa.getClass().getSimpleName() + "," + problemSize + "," + maxFitnessReached + "," + currentIteration + "," + executionTime;
		System.out.println(s);
		MLAssignmentUtils.writeToFile(fileName, s, true);
	}
}
