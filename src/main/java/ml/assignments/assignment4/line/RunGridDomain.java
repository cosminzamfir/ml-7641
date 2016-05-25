package ml.assignments.assignment4.line;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.states.State;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

public class RunGridDomain {

	public static void main(String[] args) {
		int maxMoves = 5;
		double probability = 1;
		int sizeX = 5;
		int sizeY = 5;
		int targetLineSize=3;
				
		GridDomain gridDomain = new GridDomain(sizeX, sizeY, maxMoves, probability);
		Domain domain = gridDomain.generateDomain();
		State initialState = gridDomain.createInitialState(domain);
		Reward reward = new Reward(maxMoves, targetLineSize);
		TerminalState terminalState = new TerminalState(maxMoves, targetLineSize);
		
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		ValueIteration planner = new ValueIteration(domain, reward, terminalState, 1, hashingFactory, 1, 100);
		planner.toggleDebugPrinting(true);
		Policy policy = planner.planFromState(initialState);
		EpisodeAnalysis episodeAnalysis = policy.evaluateBehavior(initialState, reward, terminalState);
		System.out.println(episodeAnalysis);
	}
}
