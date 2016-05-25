package ml.assignments;

import java.util.ArrayList;
import java.util.List;

import ml.model.function.LinearMultivariableFunction;
import ml.model.function.MultivariableFunction;
import ml.model.function.PolynomialMultivariableFunction;
import ml.utils.Utils;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class DataSetBuilder {

	/**The size of the input vector */
	private int inputDimension;

	/** For each dimension, a 2 elements array: {minValue, maxValue} */
	private double[][] ranges;

	/** The function F:InputVector -> R used to separate the examples */
	private MultivariableFunction separationFunction;

	/** The threshold value. If F(x) < threshold -> category 1, else category 2 */
	private double threshold;

	/** The zone between F(x) - margin to F(x) to margin contains no examples*/
	private double margin;

	/**The number of examples to generate */
	private int examples;

	public static Instances newLinearSeparableNormalDataSet(int inputDimensions, double threshold, double margin, int examples, double... coefficients) {
		DataSetBuilder res = new DataSetBuilder();
		MultivariableFunction function = new LinearMultivariableFunction(coefficients);
		return res.separationFunction(function).inputDimension(inputDimensions).threshold(threshold).margin(margin).examples(examples).build();
	}

	public static Instances newQuadraticSeparableNormalDataSet(int inputDimensions, double threshold, double margin, int examples, double[][] coefficients) {
		DataSetBuilder res = new DataSetBuilder();
		MultivariableFunction function = new PolynomialMultivariableFunction(coefficients);
		return res.inputDimension(inputDimensions).threshold(threshold).margin(margin).examples(examples).separationFunction(function).build();
	}
	
	public static DataSetBuilder defaultBuilder(int inputDimensions, int examples, double threshold, double margin) {
		DataSetBuilder res = new DataSetBuilder();
		res.inputDimension(inputDimensions).threshold(threshold).margin(margin).examples(examples);
		return res;
	}

	private Attribute[] attributes;

	public DataSetBuilder() {
	}
	
	public DataSetBuilder inputDimension(int value) {
		this.inputDimension = value;
		setNormalRanges();
		return this;
	}

	private void setNormalRanges() {
		double[][] ranges = new double[inputDimension][2];
		for (int i = 0; i < ranges.length; i++) {
			ranges[i][0] = -1;
			ranges[i][1] = 1;
		}
		ranges(ranges);
	}

	public DataSetBuilder separationFunction(MultivariableFunction value) {
		this.separationFunction = value;
		return this;
	}

	public DataSetBuilder threshold(double value) {
		this.threshold = value;
		return this;
	}

	public DataSetBuilder margin(double value) {
		this.margin = value;
		return this;
	}

	public DataSetBuilder examples(int value) {
		this.examples = value;
		return this;
	}

	public DataSetBuilder ranges(double[][] value) {
		this.ranges = value;
		return this;
	}

	private double getRandomValue(int dimensionIndex) {
		double min = ranges[dimensionIndex][0];
		double max = ranges[dimensionIndex][1];
		return Utils.randomDouble(min, max);
	}

	private double[] getRandomValues() {
		double[] res = new double[inputDimension];
		for (int i = 0; i < res.length; i++) {
			res[i] = getRandomValue(i);
		}
		return res;
	}

	public Instances build() {
		buildAttributes();
		ArrayList<Attribute> attrs = new ArrayList<>();
		for (Attribute attribute : attributes) {
			attrs.add(attribute);
		}
		Instances res = new Instances("Random_" + inputDimension + "_dimensions_SeparationFunction_" + separationFunction.toString(), attrs, examples);
		for (int i = 0; i < examples; i++) {
			Instance instance = buildInstance(res);
			res.add(instance);
		}
		res.setClassIndex(inputDimension);
		return res;
	}

	private void buildAttributes() {
		attributes = new Attribute[inputDimension + 1];
		for (int i = 0; i < attributes.length - 1; i++) {
			attributes[i] = new Attribute("x" + (i + 1), i);
		}
		List<String> classes = new ArrayList<>();
		classes.add("0");
		classes.add("1");
		attributes[inputDimension] = new Attribute("Class", classes, inputDimension);
	}

	private Instance buildInstance(Instances parent) {
		Instance res = new DenseInstance(inputDimension + 1);
		res.setDataset(parent);
		boolean done = false;
		double[] values;
		while (!done) {
			values = getRandomValues();
			double functionValue = separationFunction.evaluate(values);
			System.out.println("Xs: " + Utils.toString(values) + "; function=" + functionValue);
			if (functionValue <= threshold - margin || functionValue >= threshold + margin) {
				if (functionValue <= threshold - margin) {
					res.setValue(inputDimension, "0");
				} else {
					res.setValue(inputDimension, "1");
				}
				for (int i = 0; i < values.length; i++) {
					res.setValue(i, values[i]);
				}
				done = true;
			}
		}
		return res;
	}

	public static void main(String[] args) {
		DataSetBuilder builder = DataSetBuilder.defaultBuilder(20, 25000, 2, 0.1);
		builder.separationFunction(new Function1());
		Instances dataSet = builder.build();
		MLAssignmentUtils.write("test-function-1.arff", dataSet);
	}
}
