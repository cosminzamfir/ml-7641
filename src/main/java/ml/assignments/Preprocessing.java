package ml.assignments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

public class Preprocessing {

	private static DataSetStatistic trainStats;
	private static Instances trainSet;
	private static Instances testSet;
	private static Instances positiveSet;
	private static Instances negativeSet;
	private static Instances testIds;

	public static void main(String[] args) throws Exception {
		runTrainingSet();
		runTestSet();
		removeFeaturesWithSimilarMean();
		write();
	}

	private static void write() {
		MLAssignmentUtils.writeCSV("santander/train.positive.csv", positiveSet);
		MLAssignmentUtils.writeCSV("santander/train.negative.csv", negativeSet);
		MLAssignmentUtils.writeCSV("santander/train.csv", trainSet);
		MLAssignmentUtils.writeCSV("santander/test.csv", testSet);
		MLAssignmentUtils.writeCSV("santander/test.ids.csv", testIds);
	}

	private static void removeFeaturesWithSimilarMean() {
		DataSetStatistic positiveStat = new DataSetStatistic(positiveSet);
		DataSetStatistic negativeStat = new DataSetStatistic(negativeSet);
		for (int i = 0; i < positiveStat.getAttrStats().size(); i++) {
			System.out.println(positiveStat.getAttrStats().get(i));
			System.out.println(negativeStat.getAttrStats().get(i));
			System.out.println();
		}
		String filter = positiveStat.getAttributesListWithMeanDiffLessThan(negativeStat, 0.7);
		System.out.println(filter);
		trainSet = MLAssignmentUtils.removeAttributes(trainSet, filter);
		testSet = MLAssignmentUtils.removeAttributes(testSet, filter);
		positiveSet = MLAssignmentUtils.removeAttributes(positiveSet, filter);
		negativeSet = MLAssignmentUtils.removeAttributes(negativeSet, filter);
	}

	private static void runTrainingSet() throws Exception {
		trainSet = MLAssignmentUtils.loadCSV("santander/train.original.csv", true);
		trainSet.setClassIndex(trainSet.numAttributes() - 1);
		trainSet = MLAssignmentUtils.removeAttributes(trainSet, "1");
		System.out.println("Instances:" + trainSet.numInstances());
		System.out.println("Attributes:" + trainSet.numAttributes());
		trainSet = fix9999Values(trainSet);
		trainSet = MLAssignmentUtils.setNominalClass(trainSet);

		trainStats = new DataSetStatistic(trainSet);
		trainSet = MLAssignmentUtils.removeAttributes(trainSet, trainStats.getAttributesListWithSmallDeviationOrMostlyZeroValues(0, 99));

		trainSet = MLAssignmentUtils.normalize(trainSet);

		positiveSet = new Instances(trainSet);
		for (Iterator<Instance> iterator = positiveSet.iterator(); iterator.hasNext();) {
			Instance instance = iterator.next();
			if (instance.classValue() == 0) {
				iterator.remove();
			}
		}

		negativeSet = new Instances(trainSet);
		for (Iterator<Instance> iterator = negativeSet.iterator(); iterator.hasNext();) {
			Instance instance = iterator.next();
			if (instance.classValue() == 1) {
				iterator.remove();
			}
		}
	}

	/**
	 * Read the test.csv
	 * Write as test.arff
	 * Write as test.noid.arff
	 * @throws IOException 
	 */
	private static void runTestSet() throws IOException {
		testSet = MLAssignmentUtils.loadCSV("santander/test.original.csv", true);
		testIds = MLAssignmentUtils.keepOnlyAttributes(testSet, "1");
		testSet = MLAssignmentUtils.removeAttributes(testSet, "1");
		testSet = fix9999Values(testSet);

		testSet = MLAssignmentUtils.removeAttributes(testSet, trainStats.getAttributesListWithSmallDeviationOrMostlyZeroValues(0, 99));
		testSet = MLAssignmentUtils.normalize(testSet);
	}

	private static Instances fix9999Values(Instances dataSet) {
		int fixed = 0;
		for (int attrIndex = 0; attrIndex < dataSet.numAttributes(); attrIndex++) {
			for (int instanceIndex = 0; instanceIndex < dataSet.numInstances(); instanceIndex++) {
				if (strangeValue(dataSet.instance(instanceIndex).value(attrIndex))) {
					//System.out.println("Strange value replaced: " + dataSet.instance(instanceIndex).value(attrIndex));
					dataSet.instance(instanceIndex).setValue(attrIndex, 0);
					fixed++;
				}
			}
		}
		System.out.println("Fixed: " + fixed + " values");
		return dataSet;
	}

	private static boolean strangeValue(double value) {
		if (value == 9999999999d) {
			return true;
		}
		return false;
	}
}
