package ml.assignments.assignment3;

import chart.ScatterChartRobot;
import ml.assignments.MLAssignmentUtils;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.core.EuclideanDistance;
import weka.core.Instances;

public class EMRobotTest {

	public static void main(String[] args) throws Exception {
		EM em = new EM();
		Instances instances = MLAssignmentUtils.buildInstancesFromResource("robot-moves.arff");
		
		//remove the class attribute
		Instances instancesNoClass = MLAssignmentUtils.removeAttributes(instances, String.valueOf(instances.numAttributes()));
		
		em.setNumClusters(4);
		em.buildClusterer(instancesNoClass);
		
		ClusterEvaluator.evaluate(em, new EuclideanDistance(instances), instancesNoClass);
		
		ClusterEvaluation evaluation = new ClusterEvaluation();
		evaluation.setClusterer(em);
		evaluation.evaluateClusterer(instances);
		System.out.println(evaluation.clusterResultsToString());
		
		new ScatterChartRobot().showEMClusters(em, instancesNoClass);
		
	}

}
