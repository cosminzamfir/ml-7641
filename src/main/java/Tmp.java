import ml.assignments.MLAssignmentUtils;
import weka.core.Instances;

public class Tmp {

	public static void main(String[] args) {
		Instances train = MLAssignmentUtils.loadCSV("santader/train.original.csv", true);
		train = MLAssignmentUtils.setNominalClass(train);
		train = MLAssignmentUtils.removeAttributes(train, "1");
		for (int i = 0; i < train.numAttributes(); i++) {
		}
	}
}
