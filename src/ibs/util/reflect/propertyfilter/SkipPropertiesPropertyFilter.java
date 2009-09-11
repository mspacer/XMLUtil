package ibs.util.reflect.propertyfilter;

import ibs.util.reflect.propertyfilter.acceptor.RegexPropertyNameAcceptor;

/**
 * Реализация {@link ClassloaderFilter}, загружающая информацию
 * для фильтрации из файлов с расширением <code>skip.properties</code>,
 * находящихся в CLASSPATH. Фильтр интерпертирует содержимое файлов,
 * как набор регулярных выражений, соответствие которым по имени
 * исключает свойства. Фильтры передаются по наследству.
 * @author mkarajani
 */
public final class SkipPropertiesPropertyFilter extends ClassloaderFilter {
	public SkipPropertiesPropertyFilter() {
		super("skip.properties", true, new RegexPropertyNameAcceptor(true));
	}
}
