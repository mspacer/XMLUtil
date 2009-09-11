package ibs.util.reflect.propertyfilter;

import ibs.util.reflect.propertyfilter.acceptor.PropertyAcceptor;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Базовый класс для реализаций {@link PropertyFilter},
 * использующих в качестве исходной информации для фильтрации
 * объекты типа {@link Properties}
 * @author mkarajani
 */
public abstract class PropertiesPropertyFilter implements PropertyFilter {
	private static final Map properties = new HashMap();
	private final PropertyAcceptor acceptor;

	/**
	 * Конструктор для наследников
	 * @param acceptor Компонент, принимающий решения относительно
	 * принятия свойств
	 * @see PropertyAcceptor
	 */
	protected PropertiesPropertyFilter(PropertyAcceptor acceptor) {
		this.acceptor = acceptor;
	}

	public final boolean accept(Class clazz, PropertyDescriptor property) {
		return acceptor.accept(getProperties(clazz), property.getName());
	}
	
	protected synchronized final Properties getProperties(Class clazz) {
		Properties result;
		Map implementationCache;
		if(properties.containsKey(getClass())) {
			implementationCache = (Map) properties.get(getClass());
		} else {
			implementationCache = new HashMap();
			properties.put(getClass(), implementationCache);
		}
		if(implementationCache.containsKey(clazz)) {
			result = (Properties) implementationCache.get(clazz);
		} else {
			result = loadProperties(clazz);
			implementationCache.put(clazz, result);
		}
		return result;
	}

	/**
	 * Загружает объект типа {@link Properties}
	 * @param clazz Тип для Java Bean
	 * @return Загруженный объект
	 */
	protected abstract Properties loadProperties(Class clazz);
	
	protected static class PropertiesCreator {
		public Properties create(Class clazz) {
			return new Properties();
		}
	}
	
	protected class InheritedPropertiesCreator extends PropertiesCreator {
		public Properties create(Class clazz) {
			Properties result = super.create(clazz);
			Class superclazz = clazz.getSuperclass();
			if(null != superclazz) {
				result.putAll(getProperties(superclazz));
			}
			return result;
		}
	}
}
