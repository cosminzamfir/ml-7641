package ml.assignments.assignment3;

import ml.assignments.MLAssignmentUtils;
import shared.DataSet;
import shared.Instance;
import shared.filt.RandomizedProjectionFilter;

public class RCARobotTest {
    
    public static void main(String[] args) {
        Instance[] instances = MLAssignmentUtils.initializeGeneratedDataSet(5400);
		DataSet dataSet = new DataSet(instances);
        
        //System.out.println(set);
        RandomizedProjectionFilter filter = new RandomizedProjectionFilter(24, 24);
        filter.filter(dataSet);
    }

}
