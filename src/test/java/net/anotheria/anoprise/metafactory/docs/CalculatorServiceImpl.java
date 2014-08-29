package net.anotheria.anoprise.metafactory.docs;

public class CalculatorServiceImpl implements CalculatorService{

	@Override
	public int plus(int a, int b) {
		System.out.println("On impl called "+a+" + "+b);
		return a+b;
	}
	
}
