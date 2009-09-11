package ibs.util.reflect.propertyfilter;

import ibs.util.reflect.propertyfilter.acceptor.PropertyAcceptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Базовый класс для реализаций {@link PropertiesPropertyFilter},
 * загружающий объекты типа {@link Properties} из ресурсов находящихся
 * в CLASSPATH приложения
 * @author mkarajani
 */
public abstract class ClassloaderFilter extends PropertiesPropertyFilter {
	private final String fileExt;
	private final PropertiesCreator propertiesCreator;
	private static final Logger logger = Logger.getLogger(ClassloaderFilter.class);

	/**
	 * Конструктор для наследников
	 * @param fileExt Расширение ресурсов, из которых будут грузиться
	 * объекты типа {@link Properties}
	 * @param inherit Флаг, определяющий, наследовать фильтрацию свойств
	 * Java Bean или нет
	 * @param acceptor Компонент, принимающий решения относительно
	 * принятия свойств 
	 */
	protected ClassloaderFilter(String fileExt, boolean inherit, PropertyAcceptor acceptor) {
		super(acceptor);
		this.fileExt = fileExt;
		this.propertiesCreator = inherit ? new InheritedPropertiesCreator() : new PropertiesCreator();
	}

	protected final Properties loadProperties(Class clazz) {
		Properties result = propertiesCreator.create(clazz);
		ClassLoader classLoader = clazz.getClassLoader();
		if(null != classLoader) {
			InputStream stream = classLoader.getResourceAsStream(clazz.getName().replace('.', '/')+'.'+fileExt);
			if(null != stream) {
				try {
					result.load(stream);
					stream.close();
				} catch (IOException e) {
					logger.error("#getProperties", e);
				}
			}
		}
		return result;
	}
}
