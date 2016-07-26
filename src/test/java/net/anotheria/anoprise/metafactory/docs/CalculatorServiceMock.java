package net.anotheria.anoprise.metafactory.docs;

import java.util.HashMap;
import java.util.Map;

public class CalculatorServiceMock implements CalculatorService{

	private Map<String, Integer> fixtures = new HashMap<>();
	
	public CalculatorServiceMock() {
		fixtures.put("2+2", 4);
	}
	
	@Override
	public int plus(int a, int b) {
		System.out.println("On mock called "+a+" + "+b);
		Integer ret = fixtures.get(a+"+"+b);
		return ret == null ? 0 : ret;
	}

}
