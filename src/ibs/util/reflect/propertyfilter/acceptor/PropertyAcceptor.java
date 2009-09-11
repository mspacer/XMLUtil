package ibs.util.reflect.propertyfilter.acceptor;

import ibs.util.reflect.propertyfilter.PropertiesPropertyFilter;

import java.beans.PropertyDescriptor;
import java.util.Properties;

/**
 * ������� ���������, ��������������� ��� ����������������� �
 * �������-����������� {@link PropertiesPropertyFilter}
 * @author mkarajani
 */
public abstract class PropertyAcceptor {
	private final boolean invert;
	
	/**
	 * �����������, ������������ ������������� ���������� ��������
	 * �������� ������� {@link #check(Properties, String)}
	 * @param invert ���� <code>false</code>, �� �����
	 * {@link #check(Properties, String)} ���������� ������� ��������
	 * ��������. ���� <code>true</code>, �� - ������� ����������.
	 */
	protected PropertyAcceptor(boolean invert) {
		this.invert = invert;
	}

	/**
	 * �������� ����� ����������. ������������ ��� ������������� � 
	 * {@link PropertiesPropertyFilter#accept(Class, PropertyDescriptor)}.
	 * @param properties ����������� ��������
	 * @param propertyName �������� ������������ ��������
	 * @return ������� � �������� ��������
	 */
	public final boolean accept(Properties properties, String propertyName) {
		return invert ^ check(properties, propertyName);
	}
	
	/**
	 * �������� �������� �� ������������ �������
	 * @param properties ����������� ��������
	 * @param propertyName �������� ������������ ��������
	 * @see #PropertyAcceptor(boolean)
	 * @see #accept(Properties, String)
	 * @return ��������� �������� �� ������������
	 */
	protected abstract boolean check(Properties properties, String propertyName);
}
