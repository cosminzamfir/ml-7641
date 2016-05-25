package ml.assignments.assignment3;

import ml.assignments.MLAssignmentUtils;
import shared.DataSet;
import shared.Instance;
import shared.filt.PrincipalComponentAnalysis;
import util.linalg.Matrix;

public class PCAGeneratedDataSetTest {
    
    public static void main(String[] args) {
    	Instance[] instances =  MLAssignmentUtils.initializeGeneratedDataSet(5400);
        DataSet set = new DataSet(instances);
        System.out.println("Before PCA: the data set");
        //System.out.println(set);
        PrincipalComponentAnalysis filter = new PrincipalComponentAnalysis(set, 3);
        
        System.out.println("===============================================");
        System.out.println("Eigenvalues");
        System.out.println(filter.getEigenValues());
        
        
        System.out.println("===============================================");
        System.out.println("The projection matrix");
        System.out.println(filter.getProjection().transpose());
        
        filter.filter(set);
        System.out.println("===============================================");
        System.out.println("After PCA: the data set");
        //System.out.println(set);
        
        Matrix reverse = filter.getProjection().transpose();
        for (int i = 0; i < set.size(); i++) {
            Instance instance = set.get(i);
            instance.setData(reverse.times(instance.getData()).plus(filter.getMean()));
        }
        System.out.println("===============================================");
        System.out.println("After reconstructing");
        //System.out.println(set);
     
        //TODO - compute the overall error after reconstruction
    }

}
