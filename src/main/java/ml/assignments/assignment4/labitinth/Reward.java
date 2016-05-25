package ml.assignments.assignment4.labitinth;

import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;


public class Reward implements RewardFunction {

	TerminalFunction terminalFunction;
	
	public Reward(TerminalFunction terminalFunction){
		this.terminalFunction = terminalFunction;
	}
	
	@Override
	public double reward(State s, GroundedAction a, State sprime) {
		if(terminalFunction.isTerminal(sprime)) {
			return 100;
		}
		return 0;
		
	}

}
