package net.anotheria.anoprise.inmemorymirror;

public class TestData implements Mirrorable<String>{
	private String key;
	private String value;
	
	public TestData(String aValue){
		value = aValue;
	}
	
	public TestData(String aKey, String aValue){
		key = aKey; 
		value = aValue;
	}
	
	@Override
	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override public String toString(){
		return getKey()+" = "+getValue();
	}

	public void setId(String string) {
		key = string;
		
	}
	
	

}
