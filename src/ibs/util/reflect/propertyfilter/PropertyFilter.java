package ibs.util.reflect.propertyfilter;

import ibs.util.xml.XmlFormBean;

import java.beans.PropertyDescriptor;

/**
 * ���������� ���������� ��� ���������� ������� Java Bean  
 * @see XmlFormBean 
 * @author mkarajani
 */
public interface PropertyFilter {
	/**
	 * ������, ����������� ��� ��������
	 */
	PropertyFilter EMPTY = new PropertyFilter() {
		public boolean accept(Class clazz, PropertyDescriptor property) {
			return true;
		}
	};

	/**
	 * �����, �������� ��������� �������� ��� ���
	 * @param clazz ��� ��� Java Bean
	 * @param property �������� ��������
	 * @return ��������� ������� � �������� ��������
	 */
	boolean accept(Class clazz, PropertyDescriptor property);
}
