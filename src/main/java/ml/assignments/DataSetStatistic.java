package ml.assignments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import weka.core.Instances;

public class DataSetStatistic {

	private List<AttributeStat> attrStats = new ArrayList<AttributeStat>();
	private int classIndex;

	public DataSetStatistic(Instances dataSet) {
		for (int attrIndex = 0; attrIndex < dataSet.numAttributes(); attrIndex++) {
			double sum = 0;
			for (int instanceIndex = 0; instanceIndex < dataSet.numInstances(); instanceIndex++) {
				sum += dataSet.instance(instanceIndex).value(attrIndex);
			}
			double mean = sum / dataSet.numInstances();
			double std = 0;
			double maxDeviation = Double.NEGATIVE_INFINITY;
			double zeroValues = 0;
			for (int instanceIndex = 0; instanceIndex < dataSet.numInstances(); instanceIndex++) {
				double value = dataSet.instance(instanceIndex).value(attrIndex);
				double deviation = value - mean;
				std += Math.pow(deviation, 2);
				if (deviation > maxDeviation) {
					maxDeviation = deviation;
				}
				if (value == 0) {
					zeroValues++;
				}
			}
			std = Math.sqrt(std) / dataSet.numInstances();
			AttributeStat attributeStat = new AttributeStat(attrIndex, dataSet.attribute(attrIndex), mean, std, maxDeviation, zeroValues * 100/ dataSet.numInstances());
			attrStats.add(attributeStat);
			if(dataSet.classIndex() == attrIndex) {
				classIndex = attrIndex;
			}
		}
		//setCorrelationToClass();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (AttributeStat attributeStat : attrStats) {
			sb.append(attributeStat.toString()).append("\n");
		}
		return sb.toString();
	}

	public List<AttributeStat> getAttrStats() {
		return attrStats;
	}

	public String getAttributesListWithMeanDiffLessThan(DataSetStatistic other, double threshold) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < attrStats.size(); i++) {
			double thisMean = this.getAttrStats().get(i).getMean();
			double otherMean = other.getAttrStats().get(i).getMean();
			if(thisMean * otherMean < 0) {
				continue;
			}
			double min = Math.min(Math.abs(thisMean), Math.abs(otherMean));
			double max = Math.max(Math.abs(thisMean), Math.abs(otherMean));
			if(min/max > threshold) {
				sb.append(this.getAttrStats().get(i).getUserIndex() + ",");
			}
		}
		return StringUtils.removeEnd(sb.toString(), ",");
	}
	
	public String getAttributesListWithSmallDeviationOrMostlyZeroValues(double thresholdDeviation, double thresholdZeroValues) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < attrStats.size(); i++) {
			AttributeStat attributeStat = attrStats.get(i);
			if (attributeStat.getMaxDeviation() <= thresholdDeviation || attributeStat.getZeroValuesPercentage() > thresholdZeroValues) {
				sb.append(attributeStat.getUserIndex() + ",");
			}
		}
		System.out.println("Attributes with small deviation or almost all zero: " + sb.toString());
		return StringUtils.removeEnd(sb.toString(), ",");
	}

}
