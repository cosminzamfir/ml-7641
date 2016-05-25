package ml.assignments.assignment3;

import chart.ScatterChartGeneratedDataSet;
import ml.assignments.Function1;
import ml.assignments.MLAssignmentUtils;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;

public class KMeansGeneratedDataSetTest {

	public static void main(String[] args) throws Exception {

		SimpleKMeans kMeans = new SimpleKMeans();
		Instances instances = MLAssignmentUtils.buildInstancesFromResource("test-function.arff");
		Instances instancesNoClass = MLAssignmentUtils.removeAttributes(instances, String.valueOf(instances.numAttributes()));
		
		kMeans.setNumClusters(2);
		
		EuclideanDistance distanceFunction = new EuclideanDistance(instances);
		distanceFunction.setDontNormalize(true);
		kMeans.setDistanceFunction(distanceFunction);
		kMeans.setPreserveInstancesOrder(true);
		
		kMeans.buildClusterer(instancesNoClass);
		ClusterEvaluator.evaluate(kMeans, distanceFunction, instancesNoClass);
		
		ClusterEvaluation evaluation = new ClusterEvaluation();
		evaluation.setClusterer(kMeans);
		evaluation.evaluateClusterer(instancesNoClass);
		System.out.println(evaluation.clusterResultsToString());
		
		new ScatterChartGeneratedDataSet().showClusters(kMeans, instances);
		
	}

	static class TestFunctionDistance extends EuclideanDistance {
		@Override
		public double distance(Instance first, Instance second, double cutOffValue) {
			return distance(first, second);
		}
		
		@Override
		public void postProcessDistances(double[] distances) {
		}
		
		@Override
		public double distance(Instance first, Instance second) {
			double threshold = 2;
			Function1 function = new Function1();
			double f1 = function.evaluate(first.toDoubleArray());
			double f2 = function.evaluate(second.toDoubleArray());
			return (f1 - threshold)*(f2 -threshold) >= 0 ? 1 : 0;
		}
		
		@Override
		protected double updateDistance(double currDist, double diff) {
			return currDist;
		}
	}

}
