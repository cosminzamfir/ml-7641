package ml.assignments;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ml.assignments.assignment1.ClassifierRunner;
import weka.classifiers.Classifier;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;

public class Booster implements Classifier {

	private Map<ClassifierRunner, Double> classifiersAucMapping;
	
	public Booster(Map<ClassifierRunner, Double> classifiersConfidenceMapping) {
		this.classifiersAucMapping = classifiersConfidenceMapping;
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
	}

	@Override
	public double classifyInstance(Instance instance) throws Exception {
		// map classification to confidence
		TreeMap<Double, Double> classifications = new TreeMap<Double, Double>();
		Set<ClassifierRunner> classifiers = classifiersAucMapping.keySet();
		for (ClassifierRunner classifierRunner : classifiers) {
			Double value = classifierRunner.getClassifier().classifyInstance(instance);
			if(classifications.get(value) != null) {
				classifications.put(value, classifications.get(value) + classifiersAucMapping.get(classifierRunner));
			} else {
				classifications.put(value, classifications.get(value));
			}
		}
		return classifications.lastKey();
	}

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		double totalWeight = 0;
		double res[] = null;
		for (ClassifierRunner runner : classifiersAucMapping.keySet()) {
			double[] dist = runner.getClassifier().distributionForInstance(instance);
			double weight = classifiersAucMapping.get(runner);
			totalWeight += weight;
			if(res == null) {
				res = new double[dist.length];
			}
			for (int i = 0; i < dist.length; i++) {
				res[i] = res[i] + dist[i]*weight;
			}
		}
		for (int i = 0; i < res.length; i++) {
			res[i] = res[i] / totalWeight;
		}
		return res;
	}

	@Override
	public Capabilities getCapabilities() {
		return null;
	}

}
