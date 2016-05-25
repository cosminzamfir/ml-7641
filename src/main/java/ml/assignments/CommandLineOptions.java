package ml.assignments;

import java.util.ArrayList;
import java.util.List;

import ml.assignments.assignment1.SVMTests.KernelFunction;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;

public class CommandLineOptions {

	public static enum Option {

		CLASS_INDEX("-classIndex", "last|first", "What attribute is the class index?", "last"),
		CLASSIIFIER("-c", "Classifier", "<dt|knn|ann|libsvm|smo|boost>"),
		CLASSIIFIERS("-cs", "Classifiers", "one or more of dt|knn|ann|libsvm|smo|boost", "dt,knn,ann,smo,boost"),
		BASE_LEARNER("-baseLearner", "The base learner for Boosting", "ds|dt|knn|ann|libsvm|smo", "ds"),
		PRUNING("-pruning", "Use pruning for decision tree?", "<true|false>", true),
		CONFIDENCE_J48("-confidenceFactor", "Confidence factor for J48 pruning", "numeric", 0.25),
		LEARNING_RATE("-learningRate", "For neural nets", "numeric", 0.3),
		MOMENTUM("-momentum", "For neural nets", "numeric", 0.2),
		KERNEL_FUNCTION("-kernel", "The kernel function", "<Liniar|Quadratic|Cubic|Radial|Sigmoid>", KernelFunction.Quadratic),
		MAX_ITERATINS("-maxIterations", "For boosting", "numeric", 10),
		HIDDEN_UNITS("-hiddenUnits", "For neural nets", "Comma delimited string of numbers/symbols. Each token -> one hidden layer with so many nodes", "a"),
		K_NEIGHBORS("-kn", "The size of k-neighbours", "numeric", 10),
		RUNS("-runs", "The number of runs, for looping, e.g. for different training size", "numeric", 20),
		INITIAL_SIZE("-initialSize", "The initial size, for looping, e.g. for different training size", "numeric", 100),
		STEP_SIZE("-stepSize", "The step size, for looping, e.g. for different training size", "numeric", 50),
		DATA_SET_FILE("-dataSet", "The name of the data set file. Must be in the classpath", "string", "none"),
		TEST_SIZE("-t", "The size of the test data set", "numeric", 1000),
		TRAINING_SIZE("-T", "The size of the training data set", "numeric", 1000),
		DISTANCE_WEIGHT("-distanceWeight", "The distance weighting function for KNN", "<" + IBk.WEIGHT_NONE + "(none)|" + IBk.WEIGHT_INVERSE + "(inverse)|"
				+ IBk.WEIGHT_SIMILARITY + "(similarity)|", IBk.WEIGHT_NONE),
		CROSS_VALIDATE("-crossValidate", "Perform cross validation?. Default true", "true|false", true),
		GAMMA("-gamma", "Gamma parameter for SMO.", "numeric", 1d),
		ACTIVATION_FUNCTION("-activation", "The activation function for ANN", "sigmoid|tanh", "sigmoid"),
		HELP("-help", "Help !", "no params");

		String key;
		String description;
		String usage;
		Object defaultValue;

		private Option(String key, String description, String values) {
			this.key = key;
			this.description = description;
			this.usage = values;
		}

		private Option(String key, String description, String values, Object defaultValue) {
			this.key = key;
			this.description = description;
			this.usage = values;
			this.defaultValue = defaultValue;
		}

		public String key() {
			return key;
		}

		public String description() {
			return description;
		}

		public String usage() {
			return usage;
		}

		public Object defaultValue() {
			return defaultValue;
		}

	}

	public static boolean PRUNING_DEF = true;
	public static double LEARNING_RATE_DEF = 0.3;
	public static double MOMENTUM_DEF = 0.02;
	public static KernelFunction KERNEL_FUNCTION_DEF = KernelFunction.Quadratic;
	public static int MAX_ITERATINS_DEF = 10;
	public static String HIDDEN_UNITS_DEF = "10";
	public static int K_NEIGHBORS_DEF = 10;

	public static int RUNS_DEF = 50;
	public static int INITIAL_SIZSE_DEF = 100;
	public static int STEP_SIZE_DEF = 40;
	public static String DATA_SET_FILE_DEF = "robot-moves.arff";

	private static CommandLineOptions instance = null;
	private List<KeyValue> params = new ArrayList<>();

	private static class KeyValue {

		public KeyValue(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}

		private String key;
		private String value;
	}

	public static CommandLineOptions instance(String[] args) {
		if (instance != null) {
			return instance;
		}
		if (args.length == 1 && args[0].equals(Option.HELP.key())) {
			printUsage();
			System.exit(0);
		}
		if (args.length % 2 == 1) {
			throw new RuntimeException("key - value pairs expected. This requires even number of args");
		}
		CommandLineOptions res = new CommandLineOptions();
		for (int i = 0; i < args.length; i = i + 2) {
			String key = args[i];
			String value = args[i + 1];
			checkKey(key);
			res.params.add(new KeyValue(key, value));
		}
		instance = res;
		return instance;
	}

	public static CommandLineOptions getInstance() {
		return instance;
	}

	private static void checkKey(String key) {
		for (Option option : Option.values()) {
			if (option.key().equalsIgnoreCase(key)) {
				return;
			}
		}
		throw new RuntimeException("Unknown option: " + key);
		
	}

	public boolean hasOption(String key) {
		for (KeyValue keyValue : params) {
			if (keyValue.key.equals(key)) {
				return true;
			}
		}
		return false;
	}

	public String getValue(Option option) {
		for (KeyValue keyValue : params) {
			if (keyValue.key.equals(option.key())) {
				return keyValue.value;
			}
		}
		if (option.defaultValue() != null) {
			return (String) option.defaultValue();
		}
		throw new RuntimeException("Option " + option + " not present");
	}

	public Integer getIntValue(Option option) {
		for (KeyValue param : params) {
			if (param.key.equals(option.key())) {
				try {
					return Integer.valueOf(param.value);
				} catch (Exception e) {
					throw new RuntimeException("Invalid format for integer option " + option + ": " + param.value);
				}
			}
		}
		if (option.defaultValue() != null) {
			return (Integer) option.defaultValue();
		}
		throw new RuntimeException("Option " + option + " not present");
	}

	public Double getDoubleValue(Option option) {
		for (KeyValue param : params) {
			if (param.key.equals(option.key())) {
				try {
					return Double.valueOf(param.value);
				} catch (Exception e) {
					throw new RuntimeException("Invalid format for double option " + option + ": " + param.value);
				}
			}
		}
		if (option.defaultValue() != null) {
			return (Double) option.defaultValue();
		}
		throw new RuntimeException("Option " + option + " not present");
	}

	public boolean getBooleanValue(Option option) {
		for (KeyValue keyValue : params) {
			if (keyValue.key.equals(option.key())) {
				try {
					return Boolean.valueOf(keyValue.value);
				} catch (Exception e) {
					throw new RuntimeException("Invalid format for boolean option " + option + ": " + keyValue.value);
				}
			}
		}
		if (option.defaultValue() != null) {
			return (boolean) option.defaultValue();
		}
		throw new RuntimeException("Option " + option + " not present");
	}

	public Classifier getClassifier() {
		String value = getValue(Option.CLASSIIFIER);
		return builClassifier(value);
	}

	public List<Classifier> getClassifiers() {
		String s = getValue(Option.CLASSIIFIERS);
		String[] tokens = s.split(",");
		List<Classifier> res = new ArrayList<>();
		for (String token : tokens) {
			res.add(builClassifier(token));
		}
		return res;
	}

	public Classifier getBaseLearner() {
		String value = getValue(Option.BASE_LEARNER);
		return builClassifier(value);
	}

	private Classifier builClassifier(String value) {
		if (value.equalsIgnoreCase("ds")) {
			return MLAssignmentUtils.buildDecisionStump(this);
		}
		if (value.equalsIgnoreCase("dt")) {
			return MLAssignmentUtils.buildDecisionTree(this);
		}
		if (value.equalsIgnoreCase("knn")) {
			return MLAssignmentUtils.buildKNearestNeibor(this);
		}
		if (value.equalsIgnoreCase("ann")) {
			return MLAssignmentUtils.buildNeuralNet(this);
		}
		if (value.equalsIgnoreCase("libsvm")) {
			return MLAssignmentUtils.buildLibSVM(this);
		}
		if (value.equalsIgnoreCase("smo")) {
			return MLAssignmentUtils.buildSMOSVM(this);
		}
		if (value.equalsIgnoreCase("boost")) {
			return MLAssignmentUtils.buildBoosting(this);
		}
		throw new RuntimeException("Unknown classifier symbol. Supported: dt,knn,ann,libsvn,smo,boost");
	}

	public int getKNeibors() {
		return getIntValue(Option.K_NEIGHBORS);
	}

	public double getLearningRate() {
		return getDoubleValue(Option.LEARNING_RATE);
	}

	public double getMomentum() {
		return getDoubleValue(Option.MOMENTUM);
	}

	public String getHiddenUnits() {
		return getValue(Option.HIDDEN_UNITS);
	}

	public int getRuns() {
		return getIntValue(Option.RUNS);
	}

	public int getStepSize() {
		return getIntValue(Option.STEP_SIZE);
	}

	public int getInitialSize() {
		return getIntValue(Option.INITIAL_SIZE);
	}

	public int getTestSize() {
		return getIntValue(Option.TEST_SIZE);
	}

	public int getTrainingSize() {
		return getIntValue(Option.TRAINING_SIZE);
	}

	public String getDataSetName() {
		return getValue(Option.DATA_SET_FILE);
	}

	public boolean isPruning(boolean defaultValue) {
		return getBooleanValue(Option.PRUNING);
	}

	public int getDistanceWeight() {
		return getIntValue(Option.DISTANCE_WEIGHT);
	}

	public boolean crossValidate() {
		return getBooleanValue(Option.CROSS_VALIDATE);
	}

	public String getActivationFunction() {
		return getValue(Option.ACTIVATION_FUNCTION);
	}

	public KernelFunction getKernelFunction() {
		String value = getValue(Option.KERNEL_FUNCTION);
		if (KernelFunction.valueOf(value) == null) {
			throw new RuntimeException("Unknown kernel function. Supported values: " + KernelFunction.values());
		}
		return KernelFunction.valueOf(getValue(Option.KERNEL_FUNCTION));
	}

	public static void printUsage() {
		System.out.println("Command line options:");
		for (Option option : Option.values()) {
			p(option);
		}
	}

	public String getClassIndex() {
		return getValue(Option.CLASS_INDEX);
	}

	private static void p(Option o) {
		if(o == Option.HELP) {
			System.out.println("        " + o.key() + " - Pring this help screen");
			return;
		}
		System.out.println("        " + o.key() + " - " + o.description() + " - values: " + o.usage + " - default: " + (o.defaultValue() == null ? "none"
				: o.defaultValue));
	}

	public double getGamma() {
		return getDoubleValue(Option.GAMMA);
	}

	public float getConfidenceFactor() {
		return getDoubleValue(Option.CONFIDENCE_J48).floatValue();
	}
}
