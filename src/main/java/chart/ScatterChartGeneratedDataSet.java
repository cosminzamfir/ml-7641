package chart;

import ml.assignments.MLAssignmentUtils;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class ScatterChartGeneratedDataSet extends AbstractAnalysis {

	private static String root = "C:/work/data/ml.omcs/assignments/3/images/";
	private static int k = 0;

	public static void main(String[] args) throws Exception {
		new ScatterChartGeneratedDataSet().run();
	}

	public void run() throws Exception {
		Instances dataSet = MLAssignmentUtils.buildInstancesFromResource("test-function.arff");
		int size = dataSet.numInstances();
		Coord3d[] points = new Coord3d[size];
		Color[] colors = new Color[size];
		float a = 1f;
		for (int i = 0; i < size; i++) {
			double x = dataSet.get(i).value(k);
			double y = dataSet.get(i).value(k + 1);
			double z = dataSet.get(i).value(k + 2);
			points[i] = new Coord3d(x, y, z);
			colors[i] = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), a);
		}

		Scatter scatter = new Scatter(points, colors);
		scatter.setWidth(4);
		chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
		chart.getScene().add(scatter);

		AnalysisLauncher.open(new ScatterChartGeneratedDataSet());
		ChartLauncher.screenshot(chart, root + "test.function.scatter.plot_" + k + ".png");
	}

	public void showClusters(SimpleKMeans kMeans, Instances dataSet) throws Exception {
		int size = dataSet.numInstances();
		Coord3d[] points = new Coord3d[size];
		Color[] colors = new Color[size];
		for (int i = 0; i < size; i++) {
			double x = dataSet.get(i).value(k);
			double y = dataSet.get(i).value(k + 1);
			double z = dataSet.get(i).value(k + 2);
			points[i] = new Coord3d(x, y, z);
			colors[i] = getColor(kMeans.getAssignments()[i]);
		}

		Scatter scatter = new Scatter(points, colors);
		chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
		chart.getScene().add(scatter);
		AnalysisLauncher.open(this);
		ChartLauncher.screenshot(chart, root + "test.function.scatter.plot_" + k + ".png");
	}
	
	public void showEMClusters(EM em, Instances dataSet) throws Exception {
		int size = dataSet.numInstances();
		Coord3d[] points = new Coord3d[size];
		Color[] colors = new Color[size];
		for (int i = 0; i < size; i++) {
			double x = dataSet.get(i).value(k);
			double y = dataSet.get(i).value(k + 1);
			double z = dataSet.get(i).value(k + 2);
			points[i] = new Coord3d(x, y, z);
			colors[i] = getColor(em.clusterInstance(dataSet.get(i)));
		}

		Scatter scatter = new Scatter(points, colors);
		scatter.setWidth(4);
		chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
		chart.getScene().add(scatter);
		AnalysisLauncher.open(this);
		ChartLauncher.screenshot(chart, root + "test.function.em.scatter.plot_" + k + ".png");
	}


	private Color getColor(int cluster) {
		return Color.COLORS[cluster];
	}

	@Override
	public void init() throws Exception {
	}
}
