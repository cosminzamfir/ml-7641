package ml.assignments.assignment2.ann;

public class AccuracyResult {

	public int dataSize;
	public int correct;
	public int incorrect;
	public double error;
	public double accuracy; 
	
	public AccuracyResult(int dataSize, int correct, int incorrect, double error) {
		super();
		this.dataSize = dataSize;
		this.correct = correct;
		this.incorrect = incorrect;
		this.error = error;
		this.accuracy = ((double)((correct) * 100))/dataSize;
	}

	@Override
	public String toString() {
		return "[dataSize=" + dataSize + ", correct=" + correct + ", accuracy=" + accuracy 
				+ ", error=" + error +  "]";
	}

	public String toCommaString() {
		return new StringBuilder("").append(dataSize).append(",").append(correct).append(",")
				.append(accuracy).append(",").append(error).toString();
	}
	
	
}
