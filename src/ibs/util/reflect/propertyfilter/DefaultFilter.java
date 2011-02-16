package ibs.util.reflect.propertyfilter;

import ibs.util.xml.Skippable;
import ibs.util.xml.XmlFormBean;

import java.beans.PropertyDescriptor;

/**
 * ���������� ������� {@link PropertyFilter}, ������������
 * � {@link XmlFormBean} �� ���������. ���������� �������� � ������,
 * ������������ ��������� {@link Skippable}.
 * @author mkarajani
 */
public final class DefaultFilter extends FilterChain {
	private DefaultFilter() {
	}

	public boolean accept(Class clazz, PropertyDescriptor property) {
		Class propertyType = property.getPropertyType();
		return null != propertyType && !Skippable.class.isAssignableFrom(propertyType);
	}

	/**
	 * ������ ������ �� ��������� � ��������� ��� � ����
	 * � <code>filter</code>  
	 * @param filter 
	 * @return ���� �� <code>filter</code> � ������� �� ���������
	 * @see FilterChain#chain(PropertyFilter) 
	 */
	public static FilterChain withDefault(PropertyFilter filter) {
		return new DefaultFilter().chain(filter);
	}
}
