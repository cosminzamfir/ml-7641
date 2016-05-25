package ml.assignments.assignment1;

import ml.assignments.CommandLineOptions;

import java.util.Map.Entry;

import weka.classifiers.Classifier;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;

public class BoostingTuner extends AbstractClassifierTuner {

    public AdaBoostM1 adaBoost;

    public BoostingTuner() {
        classifier = new AdaBoostM1();
        adaBoost = (AdaBoostM1) classifier;
    }

    public void run(CommandLineOptions options) throws Exception {

        initialize(options);
        
        Classifier baseLearner = new DecisionStump(); 
        adaBoost.setClassifier(baseLearner);
        singleRun("baseLearner=decisionStump");

        //lower confidence, more pruning: http://ww.samdrazin.com/classes/een548/project2report.pdf. Default is 0.25
        baseLearner = new J48();
        adaBoost.setClassifier(baseLearner);
        boolean pruning = false;
        ((J48) baseLearner).setUnpruned(!pruning);
        singleRun("baseLearner=J48;pruning=false");
        
        
        float confidence = 0;
        for (int i = 0; i < 10; i++) {
            confidence = (float) (i * 0.1);
            ((J48) baseLearner).setConfidenceFactor(confidence);
            singleRun("baseLearner=J48;pruning=false;confidenceFactor=" + confidence);
        }
    }

    public static void main(String[] args) throws Exception {
        BoostingTuner tuner = new BoostingTuner();
        CommandLineOptions options = CommandLineOptions.instance(args);
        tuner.run(options);
        tuner.getBestResult();
    }
}
