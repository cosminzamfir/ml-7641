package ml.assignments.assignment1;

import java.util.ArrayList;
import java.util.List;

import ml.assignments.CommandLineOptions;

public class Tuner {

	public static void main(String[] args) throws Exception {
		
		CommandLineOptions options = CommandLineOptions.instance(args);
		List<AbstractClassifierTuner> tuners = new ArrayList();
		DecisionTreeTuner dt = new DecisionTreeTuner();
		BoostingTuner bossting = new BoostingTuner();
		NeuralNetTuner ann = new NeuralNetTuner();
		SMOTuner smo = new SMOTuner();
		KNNTunner knn = new KNNTunner();

		tuners.add(dt);
		tuners.add(bossting);
		tuners.add(ann);
		tuners.add(smo);
		tuners.add(knn);

		List<String> bestResults = new ArrayList<>();
		for (AbstractClassifierTuner tuner : tuners) {
			tuner.run(options);
		}
		for (AbstractClassifierTuner tuner : tuners) {
			bestResults.add(tuner.getBestResult());
		}

		System.out.println("==========================================================");
		System.out.println("Best configuration for each algorithm: ");
		System.out.println("==========================================================");
		for (int i = 0; i < tuners.size(); i++) {
			System.out.println(tuners.get(i).getClass().getSimpleName() + " - " + bestResults.get(i));
			
		}
		System.out.println("==========================================================");

	}
}
