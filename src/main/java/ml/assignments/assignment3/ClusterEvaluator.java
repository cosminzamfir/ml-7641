package ml.assignments.assignment3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ml.assignments.MLAssignmentUtils;
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.DenseInstance;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;

public class ClusterEvaluator {


	public static void evaluate(SimpleKMeans kMeans, DistanceFunction distanceFunction, Instances dataSet) throws Exception {
		evaluate(kMeans.getClusterCentroids(), kMeans.getAssignments(), distanceFunction, dataSet);
	}

	public static void evaluate(EM em, DistanceFunction distanceFunction, Instances dataSet) throws Exception {
		int[] clusterAssignments = new int[dataSet.numInstances()];
		for (int i = 0; i < dataSet.numInstances(); i++) {
			clusterAssignments[i] = em.clusterInstance(dataSet.get(i));
		}
		Instances centroids = getCentroids(clusterAssignments, dataSet);
		evaluate(centroids, clusterAssignments, distanceFunction, dataSet);
	}

	public static void evaluate(Instances centroids, int[] clusterAssignments, DistanceFunction distanceFunction, Instances dataSet) throws Exception {
		
		HashMap<String, Double> interClusterDistances = new LinkedHashMap<String, Double>();
		int clusters = centroids.numInstances();
		
		for (int i = 0; i < clusters; i++) {
			for (int j = i+1; j < clusters; j++) {
				double distance = distanceFunction.distance(centroids.get(i), centroids.get(j));
				interClusterDistances.put(i + "->" + j, distance);
			}
		}
		
		System.out.println("Average InterCluster distance:    " + MLAssignmentUtils.average(interClusterDistances));
		System.out.println("All InterCluster distances:       " + interClusterDistances);
		
		
		double[] avgIntraClusterDistances = new double[clusters];
		double[] maxIntraClusterDistances = new double[clusters];
		
		for (int i = 0; i < avgIntraClusterDistances.length; i++) {
			avgIntraClusterDistances[i] = 0;
			maxIntraClusterDistances[i] = Double.NEGATIVE_INFINITY;
		}
		
		for (int i = 0; i < clusterAssignments.length; i++) {
			int cluster = clusterAssignments[i];
			double distance = distanceFunction.distance(dataSet.get(i), centroids.get(cluster));
			avgIntraClusterDistances[cluster] += distance;
			
			if(distance > maxIntraClusterDistances[cluster]) {
				maxIntraClusterDistances[cluster] = distance;
			}
		}
		for (int i = 0; i < avgIntraClusterDistances.length; i++) {
			avgIntraClusterDistances[i] = avgIntraClusterDistances[i] / MLAssignmentUtils.countIf(clusterAssignments, i);
		}
		System.out.println("Average IntraCluster distances:   " + MLAssignmentUtils.toString(avgIntraClusterDistances));
		System.out.println("Maximum IntraCluster distances:   " + MLAssignmentUtils.toString(maxIntraClusterDistances));
	}


	private static Instances getCentroids(int[] clusterAssignments, Instances dataSet) {
		Map<Integer, List<Instance>> clusters = new TreeMap<>();
		Instances res = new Instances(dataSet);
		res.removeAll(res);
		
		for (int i = 0; i < clusterAssignments.length; i++) {
			Integer clusterAssignment = clusterAssignments[i];
			if(clusters.containsKey(clusterAssignment)) {
				clusters.get(clusterAssignment).add(dataSet.get(i));
			} else {
				List<Instance> l = new ArrayList<Instance>();
				l.add(dataSet.get(i));
				clusters.put(clusterAssignment, l);
			}
		}
		for (Integer key : clusters.keySet()) {
			res.add(getCentroid(clusters.get(key)));
		}
		return res;
	}


	private static Instance getCentroid(List<Instance> instances) {
		Instance first = new DenseInstance(instances.get(0));
		double[] avgs = new double[first.numAttributes()];
		for (int instanceIndex = 0; instanceIndex < instances.size(); instanceIndex++) {
			for (int attrIndex = 0; attrIndex < first.numAttributes(); attrIndex++) {
				avgs[attrIndex] += instances.get(instanceIndex).value(attrIndex);
			}
		}
		for (int attrindex = 0; attrindex < avgs.length; attrindex++) {
			avgs[attrindex] /= instances.size();
			first.setValue(attrindex, avgs[attrindex]);
		}
		return first;
	}
}
