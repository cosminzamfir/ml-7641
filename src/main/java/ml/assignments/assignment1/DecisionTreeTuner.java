package ml.assignments.assignment1;

import ml.assignments.CommandLineOptions;

import java.util.Map.Entry;

import weka.classifiers.trees.J48;

public class DecisionTreeTuner extends AbstractClassifierTuner {

    public J48 j48;
    double[] confidences = {0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.4, 0.45};

    public DecisionTreeTuner() {
        classifier = new J48();
        j48 = (J48) classifier;
    }

    public void run(CommandLineOptions options) throws Exception {

        initialize(options);
        boolean pruning = false;
        j48.setUnpruned(!pruning);
        singleRun("pruning=false");

        //lower confidence, more pruning: http://ww.samdrazin.com/classes/een548/project2report.pdf. Default is 0.25
        pruning = true;
        j48.setUnpruned(!pruning);
        for (double confidence : confidences) {
            j48.setConfidenceFactor((float) confidence);
            singleRun("pruning=true;confidenceFactor=" + confidence);
        }
    }

    public static void main(String[] args) throws Exception {
        DecisionTreeTuner tuner = new DecisionTreeTuner();
        CommandLineOptions options = CommandLineOptions.instance(args);
        tuner.run(options);
        tuner.getBestResult();
    }
}
