package ml.assignments.assignment4.labitinth;

import java.util.List;

import utils.Utils;
import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.auxiliary.StateReachability;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;
import burlap.behavior.singleagent.planning.deterministic.uninformed.dfs.DFS;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.valuefunction.QFunction;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.domain.singleagent.gridworld.GridWorldVisualizer;
import burlap.oomdp.auxiliary.common.SinglePFTF;
import burlap.oomdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.oomdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.singleagent.environment.Environment;
import burlap.oomdp.singleagent.environment.SimulatedEnvironment;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;
import burlap.oomdp.visualizer.Visualizer;

public class LabFinder {

	private static int size = 10;

	GridWorldDomain gridWorldDomain;
	Domain domain;
	RewardFunction rewardFunction;
	TerminalFunction terminalFunction;
	StateConditionTest goalCondition;
	State initialState;
	HashableStateFactory hashingFactory;
	Environment env;
	double discountFactor = 0.99;

	public LabFinder(int size) {
		this.size = size;
		//create the domain
		gridWorldDomain = new GridWorldDomain(size, size);
		setupLabirinth(gridWorldDomain);
		//gwdg.setMapToFourRooms();
		domain = gridWorldDomain.generateDomain();

		//define the task
		terminalFunction = new SinglePFTF(domain.getPropFunction(GridWorldDomain.PFATLOCATION));
		rewardFunction = new Reward(terminalFunction);
		goalCondition = new TFGoalCondition(terminalFunction);

		//set up the initial state of the task
		initialState = GridWorldDomain.getOneAgentNLocationState(domain, 1);
		GridWorldDomain.setAgent(initialState, 0, 0);
		GridWorldDomain.setLocation(initialState, 0, size - 1, size - 1);

		//set up the state hashing system for tabular algorithms
		hashingFactory = new SimpleHashableStateFactory();

		//set up the environment for learning algorithms
		env = new SimulatedEnvironment(domain, rewardFunction, terminalFunction, initialState);
	}

	public void visualize(String outputPath) {
		Visualizer v = GridWorldVisualizer.getVisualizer(gridWorldDomain.getMap());
		new EpisodeSequenceVisualizer(v, domain, outputPath);
	}

	public ExecutionResult bfs(String outputPath) {
		ExecutionResult result = new ExecutionResult("BFS", size);
		DeterministicPlanner planner = new BFS(domain, goalCondition, hashingFactory);
		result.start();
		Policy policy = planner.planFromState(initialState);
		result.end();
		return result;
	}

	public ExecutionResult valueIteration(String outputPath) {
		ExecutionResult result = new ExecutionResult("ValueIteration", size);
		ValueIteration vi = new ValueIteration(domain, rewardFunction, terminalFunction, discountFactor, hashingFactory, 0.001, 1000);
		result.start();
		Policy policy = vi.planFromState(initialState);
		result.end();
		result.setIterations(vi.getIterations());

		EpisodeAnalysis analysis = policy.evaluateBehavior(env);
		result.setResultQuality(analysis.getDiscountedReturn(discountFactor));
		return result;
	}

	public ExecutionResult policyIteration(String outputPath) {
		ExecutionResult result = new ExecutionResult("PolicyIteration", size);
		PolicyIteration pi = new PolicyIteration(domain, rewardFunction, terminalFunction, discountFactor, hashingFactory, 0.001, 1000, 1000);
		result.start();
		Policy policy = pi.planFromState(initialState);
		result.end();
		result.setIterations(pi.getTotalValueIterations());

		EpisodeAnalysis analysis = policy.evaluateBehavior(env);
		result.setResultQuality(analysis.getDiscountedReturn(discountFactor));
		return result;
	}

	public ExecutionResult qLearning(String outputPath) {
		ExecutionResult result = new ExecutionResult("QLearning", size);
		QLearning agent = new QLearning(domain, discountFactor, hashingFactory, 0, 1);
		agent.setRf(rewardFunction);
		agent.setTf(terminalFunction);

		result.start();
		agent.setMaximumEpisodesForPlanning(1000);
		agent.planFromState(initialState);
		Utils.print("QMatrix:");
		Utils.print(agent.getQMatrix());
		
		result.end();
		Policy policy = new GreedyQPolicy((QFunction) agent);
		simpleValueFunctionVis((ValueFunction)agent, new GreedyQPolicy((QFunction)agent));
		EpisodeAnalysis analysis = policy.evaluateBehavior(env);
		result.setResultQuality(analysis.getDiscountedReturn(discountFactor));
		result.setIterations(agent.getIterations());
		return result;

	}

	public EpisodeAnalysis evaluatePolicyAndWriteEpisode(Policy policy, String outputPath, String algo) {
		EpisodeAnalysis analysis = policy.evaluateBehavior(initialState, rewardFunction, terminalFunction);
		analysis.writeToFile(outputPath + algo);
		return analysis;
	}

	public void simpleValueFunctionVis(ValueFunction valueFunction, Policy p) {
		List<State> allStates = StateReachability.getReachableStates(initialState, (SADomain) domain, hashingFactory);
		ValueFunctionVisualizerGUI gui = GridWorldDomain.getGridWorldValueFunctionVisualization(allStates, valueFunction, p);
		gui.initGUI();
	}

	public void dfs(String outputPath) {
		DeterministicPlanner planner = new DFS(domain, goalCondition, hashingFactory);
		Policy p = planner.planFromState(initialState);
		p.evaluateBehavior(initialState, rewardFunction, terminalFunction).writeToFile(outputPath + "dfs");
	}

	public void setupRandom(GridWorldDomain domain) {
		int walls = size * size / 5;
		for (int i = 0; i < walls; i++) {
			int x = (int) (Math.random() * size);
			int y = (int) (Math.random() * size);
			if (Math.random() > 0.5) {
				domain.horizontalWall(x, x, y);
			} else {
				domain.verticalWall(y, y, x);
			}
		}
	}

	public void setupLabirinth(GridWorldDomain domain) {
		int index = 0;
		for (int i = 1; i < size; i = i + 2) {
			if (index % 2 == 0) {
				domain.horizontalWall(0, size - 2, i);
			} else {
				domain.horizontalWall(1, size - 1, i);
			}
			index++;
		}
	}
}