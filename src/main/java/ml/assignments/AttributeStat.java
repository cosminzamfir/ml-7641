package ml.assignments;

import java.text.NumberFormat;

import org.apache.commons.lang3.StringUtils;

import weka.core.Attribute;

public class AttributeStat {

	private static NumberFormat nf = NumberFormat.getInstance();
	static {
		nf.setMinimumFractionDigits(3);
		nf.setMaximumFractionDigits(3);
	}

	private Attribute attribute;
	private double mean;
	private double std;
	private double maxDeviation;
	private double zeroValuesPercentage;
	private int technicalIndex;
	private int userIndex;
	private double correlationToClass;

	public AttributeStat(int technicalIndex, Attribute attribute, double mean, double std, double maxDeviation, double zeroValuesPercentage) {
		super();
		this.attribute = attribute;
		this.mean = mean;
		this.std = std;
		this.maxDeviation = maxDeviation;
		this.zeroValuesPercentage = zeroValuesPercentage;
		this.technicalIndex = technicalIndex;
		this.userIndex = technicalIndex + 1;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getStd() {
		return std;
	}

	public void setStd(double std) {
		this.std = std;
	}

	public double getMaxDeviation() {
		return maxDeviation;
	}

	public void setMaxDeviation(double maxDeviation) {
		this.maxDeviation = maxDeviation;
	}

	public double getZeroValuesPercentage() {
		return zeroValuesPercentage;
	}

	public void setZeroValuesPercentage(double zeroValuesPercentage) {
		this.zeroValuesPercentage = zeroValuesPercentage;
	}
	
	public int getTechnicalIndex() {
		return technicalIndex;
	}
	
	public int getUserIndex() {
		return userIndex;
	}
	
	public double getCorrelationToClass() {
		return correlationToClass;
	}
	
	public void setCorrelationToClass(double correlationToClass) {
		this.correlationToClass = correlationToClass;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(attribute.name() + "[");
		sb.append("mean=" + format(mean));
		sb.append(",std=" + format(std));
		sb.append(",maxDev=" + format(maxDeviation));
		sb.append("zeroValuesPers=" + format(zeroValuesPercentage));
		return sb.toString();
	}

	private String format(double d) {
		return StringUtils.leftPad(nf.format(d), 10);
	}
}
