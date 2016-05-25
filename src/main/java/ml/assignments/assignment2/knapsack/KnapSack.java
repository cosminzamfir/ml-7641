package ml.assignments.assignment2.knapsack;

import java.util.HashMap;
import java.util.Map;

import util.Utils;

/**
 * Input: 
 * 		- an array of items; each item has a value and a cost
 * 	    - a maxCost as an integer value
 * Output: 
 * 		- the subset of items with totalCost < maxCost which maximizes the total value
 * @author eh2zamf
 *
 */
public class KnapSack {


	private double[] values;
	private double[] costs;
	private int maxCost;
	int n;
	Map<String, Integer> cache = new HashMap<String, Integer>();
	
	public KnapSack(double[] values, double[] costs, int maxCost, int n) {
		super();
		this.values = values;
		this.costs = costs;
		this.maxCost = maxCost;
		this.n = n;
	}

	/**
	 * Order the items n to 1 - no specific ordering required
	 * The optimal solution (considering all n items) can be decomposed as below: Notation: Opt(i,cost) = optimal solution considering first i items with totalCost <= cost
	 *    - item n does not belong to the optimal solution -> Opt(n,maxCost) = Opt(n-1, maxCost)
	 *    - item n does belong to the optimal solution -> Opt(n,maxCost)= value(n) + Opt(n-1, maxCost - cost(n))
	 *  Recurrence relation: Opt(i,cost) = Max[Opt(i-1), cost], value(i) + Opt(i-1,cost-cost(i))
	 *  Implementation: 
	 *     - use a 2-dimension array: [1..n][1..maxCost]
	 *     - initialize iteration 0 with 0
	 *     - at each iteration [1..n] compute the optimal solution for each value from [1..maxCost]
	 */
	public int run() {
		int[][] dArray = new int[maxCost + 1][values.length + 1];
		for (int i = 0; i < dArray.length; i++) {
			dArray[i][0] = 0;
		}
		for (int i = 1; i <= values.length; i++) {
			for (int j = 0; j <= maxCost; j++) {
				// current item not included in the knapsack
				int option1 = dArray[j][i - 1];
				if (j - costs[i - 1] < 0) {
					dArray[j][i] = option1;
				} else {

					// current item included
					int option2 = (int) (values[i - 1]
							+ dArray[(int) (j - costs[i - 1])][i - 1]);
					dArray[j][i] = Math.max(option1, option2);
				}
			}
		}
		return dArray[maxCost][values.length];
	}

	public int runRecursive() {
		return compute(n - 1, maxCost);
	}

	private int compute(int i, int localMaxCost) {
		if (localMaxCost == 0) {
			return 0;
		}

		if (i == 0) {
			return (int) (costs[i] > localMaxCost ? 0 : values[i]);
		}

		String key = i + ":" + localMaxCost;
		if (cache.containsKey(key)) {
			System.out.println("Getting cached value for " + key);
			return cache.get(key);
		}
		Utils.debug("Computing value for " + key);
		int option1 = compute(i - 1, localMaxCost);
		int option2 = (int) (localMaxCost - costs[i] >= 0 ? values[i]
				+ compute(i - 1, (int) (localMaxCost - costs[i])) : 0);
		int res = Math.max(option1, option2);
		cache.put(key, res);
		return res;
	}
}
