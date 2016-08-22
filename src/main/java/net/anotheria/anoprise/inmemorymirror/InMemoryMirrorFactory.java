package net.anotheria.anoprise.inmemorymirror;

/**
 * <p>InMemoryMirrorFactory class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class InMemoryMirrorFactory {
	/**
	 * <p>createMirror.</p>
	 *
	 * @param config a {@link java.lang.String} object.
	 * @param support a {@link net.anotheria.anoprise.inmemorymirror.InMemorySupport} object.
	 * @param <K> a K object.
	 * @param <V> a V object.
	 * @return a {@link net.anotheria.anoprise.inmemorymirror.InMemoryMirror} object.
	 */
	public static <K,V extends Mirrorable<K>> InMemoryMirror<K, V> createMirror(String config, InMemorySupport<K, V> support){
		
		InMemoryMirrorImpl<K, V> mirror = new InMemoryMirrorImpl<K, V>(config, support);
		//TODO add to configuration manager
		return mirror;
	}
}
