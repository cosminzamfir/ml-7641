package ml.assignments.assignment1;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import ml.assignments.MLAssignmentUtils;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class Visualizer {

	public static void main(String[] args) throws Exception {
		Instances dataSet = MLAssignmentUtils.buildInstancesFromResource("adult.arff");
		dataSet.setClassIndex(dataSet.numAttributes() - 1);
		J48 classfier = new J48();
		classfier.buildClassifier(dataSet);
		
		TreeVisualizer tv = new TreeVisualizer(
				null, classfier.graph(), new PlaceNode2());
				JFrame jf = new JFrame("Weka Classifier Tree Visualizer: J48");
				jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				jf.setSize(1200, 800);
				jf.getContentPane().setLayout(new BorderLayout());
				jf.getContentPane().add(tv, BorderLayout.CENTER);
				jf.setVisible(true);
				// adjust tree
				tv.fitToScreen();
	}
}
