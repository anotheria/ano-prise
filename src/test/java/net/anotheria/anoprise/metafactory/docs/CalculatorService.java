package net.anotheria.anoprise.metafactory.docs;

import net.anotheria.anoprise.metafactory.Service;

/**
 * Calculator service for the simple test. It only supports one method.
 */
public interface CalculatorService extends Service{
	int plus(int a, int b);
}
