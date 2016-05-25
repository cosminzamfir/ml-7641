package ml.assignments.assignment3;

import ml.assignments.MLAssignmentUtils;
import weka.attributeSelection.PrincipalComponents;
import weka.core.Instance;
import weka.core.Instances;

public class WekaPCARobotTest {

	public static void main(String[] args) throws Exception {
		
		PrincipalComponents pc = new PrincipalComponents();
		pc.setCenterData(true);
		Instances dataSet = MLAssignmentUtils.buildInstancesFromResource("robot-moves.arff"); 
		pc.setTransformBackToOriginal(true);
		pc.buildEvaluator(dataSet);
		System.out.println(pc);
		Instance instance = dataSet.get(0);
		System.out.println("Original instance:\n" + instance);
		System.out.println("Converted instance:\n" + pc.convertInstance(instance));
	}
}
