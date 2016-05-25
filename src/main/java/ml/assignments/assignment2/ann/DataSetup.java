package ml.assignments.assignment2.ann;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import shared.DataSet;
import shared.Instance;

public class DataSetup {

	public final static int nrAttributes = 20;
	public final static int nrLabels = 2;
	
	/*public static Instance[] trainInstances = initializeInstances(4435, "sat-train-new.txt");
	public static Instance[] testInstances = initializeInstances(2000, "sat-test-new.txt");*/
	
	public static Instance[] trainInstances = initializeInstances(10000, "test-function-training.txt");
	public static Instance[] testInstances = initializeInstances(4000, "test-function-test.txt");

	public static int inputLayer = nrAttributes, hiddenLayer = (nrAttributes + nrLabels)/2, outputLayer = nrLabels; 
    public static int trainingIterations = 30000;
    
    public static DataSet trainingSet = new DataSet(trainInstances);
    public static DataSet testSet = new DataSet(testInstances);
    
    public static Instance[] initializeInstances(int size, String filePath) {

        double[][][] attributes = new double[size][][];

        try {
        	InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes[i] = new double[2][];
                attributes[i][0] = new double[nrAttributes]; // 36 attributes
                attributes[i][1] = new double[nrLabels];

                for(int j = 0; j < nrAttributes; j++)
                    attributes[i][0][j] = Double.parseDouble(scan.next());

                int label = Integer.valueOf(scan.next());
                for (int j = 0; j < nrLabels; j++) {
                	if (j == (label-1)) {
                		attributes[i][1][j] = 1;
                	} else {
                		attributes[i][1][j] = 0;
                	}
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Instance[] instances = new Instance[attributes.length];

        for(int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            instances[i].setLabel(new Instance(attributes[i][1]));
        }

        return instances;
    }
	
}
