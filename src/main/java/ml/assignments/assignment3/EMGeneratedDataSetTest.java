package ml.assignments.assignment3;

import ml.assignments.MLAssignmentUtils;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import chart.ScatterChartGeneratedDataSet;

public class EMGeneratedDataSetTest {

	public static void main(String[] args) throws Exception {
		EM em = new EM();
		Instances instances = MLAssignmentUtils.buildInstancesFromResource("test-function.arff");
		
		//remove the class attribute
		Instances instancesNoClass = MLAssignmentUtils.removeAttributes(instances, String.valueOf(instances.numAttributes()));
		
		em.setNumClusters(2);
		em.buildClusterer(instancesNoClass);
		
		ClusterEvaluator.evaluate(em, new EuclideanDistance(instances), instancesNoClass);
		
		ClusterEvaluation evaluation = new ClusterEvaluation();
		evaluation.setClusterer(em);
		evaluation.evaluateClusterer(instances);
		System.out.println(evaluation.clusterResultsToString());
		
		new ScatterChartGeneratedDataSet().showEMClusters(em, instancesNoClass);
	}

}
