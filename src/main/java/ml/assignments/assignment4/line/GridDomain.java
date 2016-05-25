package ml.assignments.assignment4.line;

import burlap.oomdp.auxiliary.DomainGenerator;
import burlap.oomdp.core.Attribute;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.ObjectClass;
import burlap.oomdp.core.Attribute.AttributeType;
import burlap.oomdp.core.objects.MutableObjectInstance;
import burlap.oomdp.core.objects.ObjectInstance;
import burlap.oomdp.core.states.MutableState;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.SADomain;

public class GridDomain implements DomainGenerator {

	public static final String SIZE_X = "sizeX";
	public static final String SIZE_Y = "sizeY";
	
	public static final String STATE_CURRENT_MOVE = "currentMove";
	public static final String STATE_CURRENT_X= "currentX";
	public static final String STATE_CURRENT_Y = "currentY";
	//"x1,y1;x2,y2;...;xn.yn
	public static final String STATE_MOVE_HISTORY = "moveHistory";
	
	public static final String CLASS_AGENT = "agent";
	public static final String ACTION_MOVE = "move";
	
	private int sizeX;
	private int sizeY;
	private int maxMoves;
	private double probability;
	
	public GridDomain(int sizeX, int sizeY, int maxMoves, double probMove) {
		super();
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.maxMoves = maxMoves;
		this.probability = probMove;
	}
	
	@Override
	public Domain generateDomain() {
		 Domain domain = new SADomain();
		 
		 Attribute currentX = new Attribute(domain, STATE_CURRENT_X, AttributeType.INT);
		 currentX.setLims(1, sizeX);
		 Attribute currentY = new Attribute(domain, STATE_CURRENT_Y, AttributeType.INT);
		 currentY.setLims(1, sizeY);
		 Attribute history = new Attribute(domain, STATE_MOVE_HISTORY, AttributeType.STRING);
		 Attribute currentIteration = new Attribute(domain, STATE_CURRENT_MOVE, AttributeType.INT);
		 currentIteration.setLims(0,maxMoves);
		 
		 ObjectClass agent = new ObjectClass(domain, CLASS_AGENT);
		 agent.addAttribute(currentX);
		 agent.addAttribute(currentY);
		 agent.addAttribute(currentIteration);
		 agent.addAttribute(history);
		 
		 
		 for (Direction direction : Direction.values()) {
			new Move(domain, ACTION_MOVE + "_" + direction, direction, sizeX, sizeY, probability);
		}
		
		return domain;
	}
	
	public State createInitialState(Domain domain) {
		State s = new MutableState();
		ObjectInstance agent = new MutableObjectInstance(domain.getObjectClass(CLASS_AGENT), "agentx0");
		agent.setValue(STATE_CURRENT_X, 1);
		agent.setValue(STATE_CURRENT_Y, 1);
		agent.setValue(STATE_MOVE_HISTORY, "");
		agent.setValue(STATE_CURRENT_MOVE, 0);
		s.addObject(agent);
		return s;
	}
	
	public static String getAgentState(ObjectInstance agent) {
		int x = agent.getIntValForAttribute(GridDomain.STATE_CURRENT_X);
		int y = agent.getIntValForAttribute(GridDomain.STATE_CURRENT_Y);
		return "Pos[" + x + "," + y + "] - History:" + agent.getStringValForAttribute(STATE_MOVE_HISTORY);
	}
	

}
