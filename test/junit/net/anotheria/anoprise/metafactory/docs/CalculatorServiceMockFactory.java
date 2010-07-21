package net.anotheria.anoprise.metafactory.docs;

import net.anotheria.anoprise.metafactory.ServiceFactory;

public class CalculatorServiceMockFactory implements ServiceFactory<CalculatorService>{

	@Override
	public CalculatorService create() {
		return new CalculatorServiceMock();
	}

}
