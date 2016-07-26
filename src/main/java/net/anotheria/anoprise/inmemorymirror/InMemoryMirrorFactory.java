package net.anotheria.anoprise.inmemorymirror;

public class InMemoryMirrorFactory {
	public static <K,V extends Mirrorable<K>> InMemoryMirror<K, V> createMirror(String config, InMemorySupport<K, V> support){

        //TODO add to configuration manager
		return new InMemoryMirrorImpl<>(config, support);
	}
}
