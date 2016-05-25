package ml.assignments.assignment4.line;

import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;

public class TerminalState implements TerminalFunction {

	private int maxMoves;
	private int targetLineSize;

	public TerminalState(int maxMoves, int targetLineSize) {
		super();
		this.maxMoves = maxMoves;
		this.targetLineSize =  targetLineSize;
	}
	
	@Override
	public boolean isTerminal(State s) {
		ObjectInstance agent = s.getFirstObjectOfClass(GridDomain.CLASS_AGENT);
		int currentMove = agent.getIntValForAttribute(GridDomain.STATE_CURRENT_MOVE);
		if(currentMove == maxMoves) {
			return true;
		}
		String history = agent.getStringValForAttribute(GridDomain.STATE_MOVE_HISTORY);
		return Reward.isFiveInLine(history,targetLineSize);
	}
	
	
}
