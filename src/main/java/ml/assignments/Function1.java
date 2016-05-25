package ml.assignments;

import ml.model.function.MultivariableFunction;

public class Function1 extends MultivariableFunction {

	@Override
	public double evaluate(double... x) {
		double res = 0;
		for (int i = 0; i < 4; i++) {
			res = res + x[i]* x[i];
		}
		for (int i = 4; i < 8; i++) {
			res = res + x[i]* x[i] * x[i];
		}
		for (int i = 8; i < 12; i++) {
			res = res + x[i]* x[i] * x[i] * x[i];
		}
		
		for (int i = 12; i < 16; i++) {
			res = res + Math.sin(x[i]);
		}
		for (int i = 16; i < 19; i++) {
			res = res + x[i]* x[i+1];
		}
		return res;
	}
}
