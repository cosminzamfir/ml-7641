package ml.assignments.assignment1;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import ml.assignments.CommandLineOptions;
import ml.assignments.MLAssignmentUtils;
import weka.classifiers.lazy.IBk;
import weka.core.SelectedTag;

public class KNNTunner extends AbstractClassifierTuner {

	private Map<Integer, String> distanceWeights = new LinkedHashMap();
	private int[] knns = { 1, 2, 4, 6, 8, 10, 20 };

	public KNNTunner() {
		distanceWeights.put(IBk.WEIGHT_NONE, "None");
		distanceWeights.put(IBk.WEIGHT_INVERSE, "Inverse");
		distanceWeights.put(IBk.WEIGHT_SIMILARITY, "Similarity");
	}

	public void run(CommandLineOptions options) throws Exception {
		classifier = MLAssignmentUtils.buildKNearestNeibor(options);
		initialize(options);
		for (int knn : knns) {
			for (Integer distanceWeight : distanceWeights.keySet()) {
				((IBk) classifier).setKNN(knn);
				((IBk) classifier).setDistanceWeighting(new SelectedTag(distanceWeight, IBk.TAGS_WEIGHTING));
				singleRun("k=" + knn  + ";DistanceWeight=" + distanceWeights.get(distanceWeight) );
			}
		}

	}

	public static void main(String[] args) throws Exception {
		KNNTunner tuner = new KNNTunner();
		CommandLineOptions options = CommandLineOptions.instance(args);
		tuner.run(options);
		tuner.getBestResult();
	}
}
