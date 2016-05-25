package ml.assignments.assignment4.line;

import java.util.ArrayList;
import java.util.List;

import ml.utils.Utils;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TransitionProbability;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.FullActionModel;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.common.SimpleAction;

public class Move extends SimpleAction implements FullActionModel {

	private int maxX;
	private int maxY;
	private Direction direction;
	/*
	 * The probability to move on the right direction
	 */
	private double probabilty;

	public Move(Domain domain, String actionName, Direction direction, int maxX, int maxY, double probability) {
		super(actionName, domain);
		this.domain = domain;
		this.direction = direction;
		this.maxX = maxX;
		this.maxY = maxY;
		this.probabilty = probability;
	}

	@Override
	public boolean applicableInState(State s, GroundedAction groundedAction) {
		return true;
	}

	@Override
	protected State performActionHelper(State s, GroundedAction groundedAction) {
		ObjectInstance agent = s.getFirstObjectOfClass(GridDomain.CLASS_AGENT);
		double p = Math.random();
		if (p <= probabilty) {
			move(direction, agent);
		} else {
			List<Direction> others = new ArrayList<>();
			for (Direction d : Direction.values()) {
				if(d != direction) {
					others.add(d);
				}
			} 
			move(Utils.randomElement(others), agent);
		}
		return s;
	}

	private void move(Direction direction, ObjectInstance agent) {
		int x = agent.getIntValForAttribute(GridDomain.STATE_CURRENT_X);
		int y = agent.getIntValForAttribute(GridDomain.STATE_CURRENT_Y);
		if (direction == Direction.NORTH && y < maxY) {
			agent.setValue(GridDomain.STATE_CURRENT_Y, y + 1);
			updateHistory(agent);
		} else if (direction == Direction.SOUTH && y > 0) {
			agent.setValue(GridDomain.STATE_CURRENT_Y, y - 1);
			updateHistory(agent);
		} else if (direction == Direction.EAST && x < maxX) {
			agent.setValue(GridDomain.STATE_CURRENT_X, x + 1);
			updateHistory(agent);
		} else if (direction == Direction.WEST && x > 0) {
			agent.setValue(GridDomain.STATE_CURRENT_X, x - 1);
			updateHistory(agent);
		}
		agent.setValue(GridDomain.STATE_CURRENT_MOVE, agent.getIntValForAttribute(GridDomain.STATE_CURRENT_MOVE)+1);
		System.out.println(GridDomain.getAgentState(agent));
	}

	private void updateHistory(ObjectInstance agent) {
		int x = agent.getIntValForAttribute(GridDomain.STATE_CURRENT_X);
		int y = agent.getIntValForAttribute(GridDomain.STATE_CURRENT_Y);
		String s = x + "," + y + ";";
		agent.setValue(GridDomain.STATE_MOVE_HISTORY, agent.getStringValForAttribute(GridDomain.STATE_MOVE_HISTORY) + s);
	}

	@Override
	public List<TransitionProbability> getTransitions(State s, GroundedAction groundedAction) {
		List<TransitionProbability> res = new ArrayList<>();
		State ns = s.copy();
		ObjectInstance agent = s.getFirstObjectOfClass(GridDomain.CLASS_AGENT);
		move(direction, agent);
		res.add(new TransitionProbability(ns, probabilty));
		for (Direction d : Direction.values()) {
			if(d != direction) {
				ns = s.copy();
				agent = s.getFirstObjectOfClass(GridDomain.CLASS_AGENT);
				move(d,agent);
				res.add(new TransitionProbability(ns, (1-probabilty)/3));
			}
		}
		return res;
	}
}
