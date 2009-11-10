package net.anotheria.anoprise.dualcrud;

public class TestCrudsaveable implements CrudSaveable{
	private String id;
	private String content;
	
	public TestCrudsaveable(String anId, String aContent){
		id = anId;
		content = aContent;
	}
	
	public String getOwnerId(){
		return id;
	}
	
	@Override public String toString(){
		return id + "=" +content;
	}
	
	public String getContent(){
		return content;
	}
	
	public String getId(){
		return id;
	}
	
	public boolean equals(Object o){
		return o instanceof TestCrudsaveable && 
		 	((TestCrudsaveable)o).id.equals(id) && ((TestCrudsaveable)o).content.equals(content);  
	}
}
