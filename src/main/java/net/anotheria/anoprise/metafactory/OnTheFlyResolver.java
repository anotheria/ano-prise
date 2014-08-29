package net.anotheria.anoprise.metafactory;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.Collection;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 12.12.12 14:51
 */
public class OnTheFlyResolver {
	public static <T> Collection<Class<? extends T>> resolveOnTheFly(Class<T> interfaceClass){
		Reflections reflections = new Reflections(
				new ConfigurationBuilder()
				.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(interfaceClass.getPackage().getName())))
				.setUrls(ClasspathHelper.forPackage(interfaceClass.getPackage().getName()))
				.setScanners(new SubTypesScanner())
		);
		return reflections.getSubTypesOf(interfaceClass);
	}
}
