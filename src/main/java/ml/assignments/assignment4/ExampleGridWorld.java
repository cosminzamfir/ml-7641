package ml.assignments.assignment4;

import java.io.ObjectInput;
import java.util.ArrayList;
import java.util.List;

import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.Attribute.AttributeType;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TransitionProbability;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.FullActionModel;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.singleagent.common.SimpleAction;

public class ExampleGridWorld implements DomainGenerator {

	public static final String ATTX = "x";
	public static final String ATTY = "y";
	public static final String CLASSAGENT = "agent";
	public static final String CLASSLOCATION = "location";

	//ordered so first dimension is x
	protected int[][] map = new int[][] {
			{ 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1 },
			{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 } };

	@Override
	public Domain generateDomain() {
		SADomain domain = new SADomain();
		Attribute xatt = new Attribute(domain, ATTX, AttributeType.INT);
		xatt.setLims(0, 10);

		Attribute yatt = new Attribute(domain, ATTY, AttributeType.INT);
		yatt.setLims(0, 10);

		ObjectClass agentClass = new ObjectClass(domain, CLASSAGENT);
		agentClass.addAttribute(xatt);
		agentClass.addAttribute(yatt);

		ObjectClass locationClass = new ObjectClass(domain, CLASSLOCATION);
		locationClass.addAttribute(xatt);
		locationClass.addAttribute(yatt);
		return domain;
	}

	protected class Movement extends SimpleAction implements FullActionModel {

		//0 - norht; 1 - south; 2 - east; 3 - west
		protected double[] directionProbs = new double[4];

		public Movement(String actionName, Domain domain, int direction) {
			super(actionName, domain);
			for (int i = 0; i < 4; i++) {
				if (i == direction) {
					directionProbs[i] = 0.8;
				} else {
					directionProbs[i] = 0.2 / 3;
				}
			}
		}

		@Override
		public List<TransitionProbability> getTransitions(State s, GroundedAction groundedAction) {
			//get agent and current position
			ObjectInstance agent = s.getFirstObjectOfClass(CLASSAGENT);
			int curX = agent.getIntValForAttribute(ATTX);
			int curY = agent.getIntValForAttribute(ATTY);

			List<TransitionProbability> tps = new ArrayList<TransitionProbability>(4);
			TransitionProbability noChangeTransition = null;
			for (int i = 0; i < this.directionProbs.length; i++) {
				int[] newPos = this.moveResult(curX, curY, i);
				if (newPos[0] != curX || newPos[1] != curY) {
					//new possible outcome
					State ns = s.copy();
					ObjectInstance nagent = ns.getFirstObjectOfClass(CLASSAGENT);
					nagent.setValue(ATTX, newPos[0]);
					nagent.setValue(ATTY, newPos[1]);

					//create transition probability object and add to our list of outcomes
					tps.add(new TransitionProbability(ns, this.directionProbs[i]));
				}
				else {
					//this direction didn't lead anywhere new
					//if there are existing possible directions
					//that wouldn't lead anywhere, aggregate with them
					if (noChangeTransition != null) {
						noChangeTransition.p += this.directionProbs[i];
					}
					else {
						//otherwise create this new state and transition
						noChangeTransition = new TransitionProbability(s.copy(),
								this.directionProbs[i]);
						tps.add(noChangeTransition);
					}
				}
			}

			return tps;
		}

		@Override
		protected State performActionHelper(State s, GroundedAction groundedAction) {
			ObjectInstance agent = s.getFirstObjectOfClass(CLASSAGENT);
			int curX = agent.getIntValForAttribute(ATTX);
			int curY = agent.getIntValForAttribute(ATTY);
			Double r = Math.random();
			double sumProb = 0;
			int dir = 0;
			for (int i = 0; i < this.directionProbs.length; i++) {
				sumProb += this.directionProbs[i];
				if (r < sumProb) {
					dir = i;
					break;
				}
			}
			int[] newPos = this.moveResult(curX, curY, dir);
			agent.setValue(ATTX, newPos[0]);
			agent.setValue(ATTY, newPos[1]);

			return s;
		}

		protected int[] moveResult(int curX, int curY, int direction) {
			int xDelta = 0;
			int yDelta = 0;
			if (direction == 0) {
				yDelta = 1;
			} else if (direction == 1) {
				yDelta = -1;
			} else if (direction == 2) {
				xDelta = 1;
			} else if (direction == 3) {
				xDelta = -1;
			}
			int nx = curX + xDelta;
			int ny = curY + yDelta;

			int width = ExampleGridWorld.this.map.length;
			int heigth = ExampleGridWorld.this.map[0].length;
			if (nx < 0 || nx >= width || ny < 0 || ny >= heigth || ExampleGridWorld.this.map[nx][ny] == 1) {
				nx = curX;
				ny = curY;
			}
			return new int[] { nx, ny };
		}

	}
}
