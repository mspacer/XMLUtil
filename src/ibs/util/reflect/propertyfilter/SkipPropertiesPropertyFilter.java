package ibs.util.reflect.propertyfilter;

import ibs.util.reflect.propertyfilter.acceptor.RegexPropertyNameAcceptor;

/**
 * ���������� {@link ClassloaderFilter}, ����������� ����������
 * ��� ���������� �� ������ � ����������� <code>skip.properties</code>,
 * ����������� � CLASSPATH. ������ �������������� ���������� ������,
 * ��� ����� ���������� ���������, ������������ ������� �� �����
 * ��������� ��������. ������� ���������� �� ����������.
 * @author mkarajani
 */
public final class SkipPropertiesPropertyFilter extends ClassloaderFilter {
	public SkipPropertiesPropertyFilter() {
		super("skip.properties", true, new RegexPropertyNameAcceptor(true));
	}
}
