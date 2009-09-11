package ibs.util.reflect.propertyfilter;

import ibs.util.reflect.propertyfilter.acceptor.PropertyAcceptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * ������� ����� ��� ���������� {@link PropertiesPropertyFilter},
 * ����������� ������� ���� {@link Properties} �� �������� �����������
 * � CLASSPATH ����������
 * @author mkarajani
 */
public abstract class ClassloaderFilter extends PropertiesPropertyFilter {
	private final String fileExt;
	private final PropertiesCreator propertiesCreator;
	private static final Logger logger = Logger.getLogger(ClassloaderFilter.class);

	/**
	 * ����������� ��� �����������
	 * @param fileExt ���������� ��������, �� ������� ����� ���������
	 * ������� ���� {@link Properties}
	 * @param inherit ����, ������������, ����������� ���������� �������
	 * Java Bean ��� ���
	 * @param acceptor ���������, ����������� ������� ������������
	 * �������� ������� 
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
