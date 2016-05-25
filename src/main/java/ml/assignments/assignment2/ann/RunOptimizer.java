package ml.assignments.assignment2.ann;

import java.util.Map;

import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.ga.StandardGeneticAlgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunOptimizer {
	
	private static final Logger log = LoggerFactory.getLogger(RunOptimizer.class);
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		if (args.length != 3) {
			log.info("there should be 3 args: Alg: [rhc, sa, ga], #iterations, #step for print ");
			log.info("To run ga, you need extra 3 params, population, crossover, mutation: ga,200,100,10");
			System.exit(0);
		}
		String algName = args[0];
		int iterations = Integer.valueOf(args[1]);
		int step = Integer.valueOf(args[2]);
		
		AlgOptimization opt = new AlgOptimization();
		
		OptimizationAlgorithm alg;
		if (algName.equals("rhc")) {
			alg = new RandomizedHillClimbing(opt.getPb());
		} else if (algName.equals("sa")) {
			alg = new SimulatedAnnealing(1E11, .95, opt.getPb());
		} else if (algName.startsWith("ga")) {
			String[] split = args[0].split(",");
			if (split.length != 4) throw new RuntimeException("Example of GA run: ga,200,100,10");
			log.info("GA with params {} {} {}", split[1], split[2], split[3]);
			alg = new StandardGeneticAlgorithm(Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]), opt.getPb());
		} else {
			throw new IllegalArgumentException("alg must be one of [rhc, sa, ga]");
		}
		
		opt.init(alg, iterations, step);
		
		log.info("Start {}, using {} iterations and {} steps", algName, iterations, step);
		opt.train();
		Map<Integer, AccuracyResult> trainingAccuracy = opt.getTrainingAccuracyMap();
		Map<Integer, AccuracyResult> testAccuracy = opt.getTestAccuracyMap();
		
		log.info("final results========================");
		AccuracyResult startTrainingAccuracy = trainingAccuracy.get(0);
		AccuracyResult finalTrainingAccuracy = trainingAccuracy.get(iterations+1);
		
		AccuracyResult startTestAccuracy = testAccuracy.get(0);
		AccuracyResult finalTestAccuracy = testAccuracy.get(iterations+1);
		
		log.info("start training accuracy: {}", startTrainingAccuracy);
		log.info("final training accuracy: {}", finalTrainingAccuracy);
		
		log.info("start test accuracy: {}", startTestAccuracy);
		log.info("final test accuracy: {}", finalTestAccuracy);
		
		log.info("End {}", algName);
		
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
		System.out.println("Done in " + (System.currentTimeMillis() - startTime)/1000 + " seconds.");
	}

}
