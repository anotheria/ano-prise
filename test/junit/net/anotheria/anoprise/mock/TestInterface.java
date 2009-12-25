package net.anotheria.anoprise.mock;

public interface TestInterface {
	String askService(String param);
	
	void notImplementedMethod();
	
	void voidDummyMethod();
	
	void throwExceptionOnCall() throws TestException;
}
