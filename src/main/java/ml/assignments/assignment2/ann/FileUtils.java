package ml.assignments.assignment2.ann;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileUtils {
	
	public static void main(String[] args) throws IOException {
		System.out.println("start");
		//changeSeparator("sat-train.txt", "sat-train-new.txt");
		//changeSeparator("sat-test.txt", "sat-test-new.txt");
		normalizeAttributes("sat-train-new.txt", "sat-train-norm.txt");
		normalizeAttributes("sat-test-new.txt", "sat-test-norm.txt");
		System.out.println("end");
	}
	
	public static void changeSeparator(String inputFile, String outputFile) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(inputFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
        String line = br.readLine();
        while (line != null) {
        	String[] strings = line.split(" ");
        	bw.newLine();
        	int i = 0;
        	for (String str : strings) {
        		bw.write(str);
        		if (i < 36) {
        			bw.write(",");
        		}
        		i++;
        	}
        	line = br.readLine();
        }
        bw.flush();
        bw.close();
        br.close();
	}
	
	public static void normalizeAttributes(String inputFile, String outputFile) throws IOException {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(inputFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
        String line = br.readLine();
        while (line != null) {
        	String[] strings = line.split(",");
        	String[] normStrings = normalize(strings);
        	bw.newLine();
        	int i = 0;
        	for (String str : normStrings) {
        		bw.write(str);
        		if (i < 36) {
        			bw.write(",");
        		}
        		i++;
        	}
        	line = br.readLine();
        }
        bw.flush();
        bw.close();
        br.close();
	}

	private static String[] normalize(String[] strings) {
		if (strings.length != 37) throw new RuntimeException("input file is wrong, size: " + strings.length);
		String[] res = new String[strings.length];
		double sum = 0;
		for (int i = 0; i < 36; i++) {
			sum += Double.valueOf(strings[i]);
		}
		for (int i = 0; i < 36; i++) {
			double d = Double.valueOf(strings[i])/sum;
			res[i] = Double.toString(d);
		}
		res[36] = strings[36];
		return res;
	}

}
