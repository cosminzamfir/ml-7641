package ml.assignments.assignment4.line;

import org.apache.commons.lang.ArrayUtils;

import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;

public class Reward implements RewardFunction {

	private int maxMoves;
	private int targetLineSize;

	public Reward(int maxMoves, int targetLineSize) {
		super();
		this.maxMoves = maxMoves;
		this.targetLineSize = targetLineSize;
	}
	
	@Override
	public double reward(State s, GroundedAction a, State sprime) {
		ObjectInstance agent = sprime.getFirstObjectOfClass(GridDomain.CLASS_AGENT);
		String history = agent.getStringValForAttribute(GridDomain.STATE_MOVE_HISTORY);
		if(isFiveInLine(history, targetLineSize)) {
			return 100;
		}
		if(agent.getIntValForAttribute(GridDomain.STATE_CURRENT_MOVE) >= maxMoves) {
			return -100;
		}
		return 0;
		
	}

	public static boolean isFiveInLine(String history, int targetLineSize) {
		if(history.isEmpty()) {
			return false;
		}
		String[] tokens = history.split(";");
		int size = tokens.length;
		String lastX = tokens[size-1].split(",") [0];
		String lastY = tokens[size-1].split(",") [1];
		int lineSize=0;
		for (int i = size-1; i >= 0; i--) {
			if(lastX.equals(tokens[i].split(",")[0])) {
				lineSize++;
			}
			if(lineSize == targetLineSize) {
				return true;
			}
		}
	
		lineSize=0;
		for (int i = size-1; i >= 0; i--) {
			if(lastY.equals(tokens[i].split(",")[1])) {
				lineSize++;
			}
			if(lineSize == 5) {
				return true;
			}
		}
		return false;
	}
	
}
