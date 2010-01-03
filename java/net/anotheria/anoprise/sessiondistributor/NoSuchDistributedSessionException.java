package net.anotheria.anoprise.sessiondistributor;

public class NoSuchDistributedSessionException extends  SessionDistributorServiceException{
	public NoSuchDistributedSessionException(String name){
		super("No such distributed session: "+name);
	}
}
