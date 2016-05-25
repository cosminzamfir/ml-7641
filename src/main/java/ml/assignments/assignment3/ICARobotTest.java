package ml.assignments.assignment3;

import ml.assignments.MLAssignmentUtils;
import shared.DataSet;
import shared.Instance;
import shared.filt.IndependentComponentAnalysis;

public class ICARobotTest {

	public static void main(String[] args) {
		Instance[] instances = MLAssignmentUtils.initializeRobotDataSet(100);
		DataSet dataSet = new DataSet(instances);
		IndependentComponentAnalysis ica = new IndependentComponentAnalysis(dataSet);
		
		System.out.println(instances[0]);
		ica.filter(dataSet);
		
		System.out.println(ica.getProjection());
		System.out.println(instances[0]);
		ica.reverse(dataSet);
		System.out.println(instances[0]);
	}
}
