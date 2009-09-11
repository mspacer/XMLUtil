package ibs.util.reflect.propertyfilter;

import java.beans.PropertyDescriptor;

/**
 * ���������� {@link PropertyFilter}, ����������� �����������
 * ������� � ����
 * @author mkarajani
 */
public abstract class FilterChain implements PropertyFilter {
	private final FilterChain _parent = this;
 
	/**
	 * �������� ������� ������ � <code>filter</code> � ����: ������� ������
	 * ���������� �������, � ��������� ��� <code>filter</code> - �������
	 * @param filter ��������� ������, ������������ ������ ��������
	 * ������� ������� ����
	 * @return ������ ����� ���� ��������
	 */
	public final FilterChain chain(final PropertyFilter filter) {
		return _parent.contains(filter)
			? _parent // ������ �� ������������ ���������
			: new FilterChain() {
				public boolean accept(Class clazz, PropertyDescriptor property) {
					return filter.accept(clazz, property) && _parent.accept(clazz, property);
				}

				protected boolean contains(PropertyFilter filter) {
					return super.contains(filter) || _parent.contains(filter);
				}
			};
	}
	
	/**
	 * ��������� ���������� �� � ������� ���� ������ <code>filter</code> 
	 * @param filter ����������� ������
	 * @return ��������� ��������
	 */
	protected boolean contains(PropertyFilter filter) {
		return equals(filter);
	}
}
