package ml.assignments.assignment4.labitinth;

import java.util.ArrayList;
import java.util.List;

public class ExecutionResults {

	private static List<ExecutionResult> results = new ArrayList<ExecutionResult>();
	
	public void newResult(ExecutionResult result) {
		results.add(result);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(ExecutionResult.header()).append("\n");
		for (ExecutionResult result : results) {
			sb.append(result).append("\n");
		}
		return sb.toString();
	}

}
