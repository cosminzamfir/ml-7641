import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ml.assignments.Booster;
import ml.assignments.CommandLineOptions;
import ml.assignments.MLAssignmentUtils;
import ml.assignments.Task;
import ml.assignments.TaskRunner;
import ml.assignments.assignment1.ClassifierRunner;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.LogitBoost;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;

public class Main {

	private static final double positivesProportion = 0.5;
	private static Instances testSet;
	private static Instances positives = MLAssignmentUtils.loadCSV("santander/train.positive.csv", true);
	private static Instances negatives = MLAssignmentUtils.loadCSV("santander/train.negative.csv", true);
	private static Map<ClassifierRunner, Double> classifierMappings = new ConcurrentHashMap<ClassifierRunner, Double>();
	private static int testPositivesSize;
	private static int testNegativesSize;

	public static void main(String[] args) throws Exception {

		CommandLineOptions options = CommandLineOptions.instance(args);
		int numClassifiers = options.getRuns();
		initializeTestSet(options);

		List<ClassifierTask> tasks = new ArrayList<Main.ClassifierTask>();
		for (int i = 0; i < numClassifiers; i++) {
			tasks.add(new ClassifierTask(options));
		}
		new TaskRunner(tasks).run();

		Booster booster = new Booster(classifierMappings);
		Evaluation eval = new Evaluation(testSet);
		eval.evaluateModel(booster, testSet);
		System.out.println("Final confusion matrix:" + eval.toMatrixString());
		System.out.println("Final accuracy:" + eval.pctCorrect());
		System.out.println("Final AUC:" + getAuc(eval));
		writeSubmissionFile(booster);
	}

	/**
	 * 
	 * @param options
	 */
	private static void initializeTestSet(CommandLineOptions options) {
		int testSetSize = options.getTestSize();
		double p = (double) positives.numInstances() / negatives.numInstances();
		testPositivesSize = (int) (testSetSize * p);
		testNegativesSize = testSetSize - testPositivesSize;
		System.out.println("Test size:" + testSetSize + ".Positives:" + testPositivesSize + ".Negatives:" + testNegativesSize);
		Instances pos = MLAssignmentUtils.last(positives, testPositivesSize);
		Instances neg = MLAssignmentUtils.last(negatives, testNegativesSize);
		testSet = MLAssignmentUtils.merge(pos, neg);
		testSet = MLAssignmentUtils.setNominalClass(testSet);
	}

	private static Instances initializeTrainingTest(CommandLineOptions options) throws Exception {

		int trainingSize = options.getTrainingSize();
		int trainingSizePositives = (int) (trainingSize * positivesProportion);
		int trainingSizeNegatives = trainingSize - trainingSizePositives;
		System.out.println("Training size:" + trainingSize + ". Positives:" + trainingSizePositives + ". Negatives: " + trainingSizeNegatives);

		Instances pos = new Instances(positives, 0, positives.numInstances() - testPositivesSize - 1);
		Instances neg = new Instances(negatives, 0, negatives.numInstances() - testNegativesSize - 1);
		pos = MLAssignmentUtils.shufle(pos);
		neg = MLAssignmentUtils.shufle(neg);

		Instances trainingPositives = new Instances(pos, 0, trainingSizePositives);
		Instances trainingNegatives = new Instances(neg, 0, trainingSizeNegatives);
		Instances res = MLAssignmentUtils.merge(trainingPositives, trainingNegatives);
		res = MLAssignmentUtils.setNominalClass(res);
		return res;
	}

	private static double getAuc(Evaluation eval) {
		double[][] confusionMatrix = eval.confusionMatrix();
		double auc = 0;
		for (int i = 0; i < confusionMatrix.length; i++) {
			double[] row = confusionMatrix[i];
			double correct = row[i];
			double total = MLAssignmentUtils.sum(row);
			auc += correct / total;
		}
		return auc / confusionMatrix.length;
	}

	private static void writeSubmissionFile(weka.classifiers.Classifier classifier) throws Exception {
		Instances testSet = MLAssignmentUtils.loadCSV("santander/test.csv", true);
		Instances testSetIds = MLAssignmentUtils.loadCSV("santander/test.ids.csv", false);
		testSet = addClassAttribute(testSet);
		testSet.setClassIndex(testSet.numAttributes() - 1);

		String submissionFile = System.getProperty("output") != null ? System.getProperty("output") :
				MLAssignmentUtils.ROOT_FOLDER + "src/main/resources/santander/submission.csv";
		MLAssignmentUtils.writeToFile(submissionFile, "ID,TARGET\n", false);
		for (int instanceIndex = 0; instanceIndex < testSet.numInstances(); instanceIndex++) {
			double value = classifier.classifyInstance(testSet.get(instanceIndex));
			double id = testSetIds.get(instanceIndex).value(0);
			//System.out.println(id + "," + value);
			MLAssignmentUtils.writeToFile(submissionFile, (int) id + "," + (int) value + "\n", true);
		}
	}

	private static Instances addClassAttribute(Instances testSetFinal) throws Exception {
		Add filter;
		Instances newData = new Instances(testSetFinal);
		filter = new Add();
		filter.setAttributeIndex("last");
		filter.setNominalLabels("0,1");
		filter.setAttributeName("Class");
		filter.setInputFormat(newData);
		newData = Filter.useFilter(newData, filter);
		return newData;
	}

	private static class ClassifierTask implements Task {

		public ClassifierTask(CommandLineOptions options) {
			super();
			this.options = options;
		}

		private CommandLineOptions options;

		@Override
		public Object execute() {
			try {
				long start = System.currentTimeMillis();
				Instances trainingSet = initializeTrainingTest(options);

				LogitBoost classifier = buildLogigBoost();
				ClassifierRunner runner = new ClassifierRunner(options.getClassifier(), options);
				//ClassifierRunner runner = new ClassifierRunner(classifier, options);
				
				
				Instances tmp = MLAssignmentUtils.loadCSV("santander/train.csv", true);
				Instances tmpTrain = MLAssignmentUtils.setNominalClass(tmp);
				tmpTrain = new Instances(tmpTrain,0,66000);
				runner.buildModel(tmpTrain);
				
				Instances tmpTest = new Instances(tmp,66000,10000);
				Evaluation eval = runner.evaluateModel(tmpTrain, tmpTest);
				System.out.println(eval.toMatrixString());
				System.out.println("Accuracy:" + eval.pctCorrect());
				System.out.println("AUC:" + getAuc(eval));
				long end = System.currentTimeMillis();
				System.out.println("Iteration done in " + (end - start) / 1000 + " seconds");
				classifierMappings.put(runner, getAuc(eval));
				classifierMappings.put(runner, 1.0);

				return null;
			} catch (Exception e) {
				throw new RuntimeException("Error", e);
			}
		}

		private LogitBoost buildLogigBoost() {
			weka.classifiers.meta.LogitBoost lb = new weka.classifiers.meta.LogitBoost();
			weka.classifiers.trees.REPTree rep = new weka.classifiers.trees.REPTree();
			rep.setMaxDepth(6);
			lb.setClassifier(rep);
			lb.setNumIterations(100);
			lb.setShrinkage(0.1);
			return lb;
		}
	}
}