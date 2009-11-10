package net.anotheria.anoprise.dualcrud;

public class ItemNotFoundException extends CrudServiceException {
	public ItemNotFoundException(String id){
		super("Not found: "+id);
	}
}
