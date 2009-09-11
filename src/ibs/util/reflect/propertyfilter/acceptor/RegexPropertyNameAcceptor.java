package ibs.util.reflect.propertyfilter.acceptor;

import java.util.Iterator;
import java.util.Properties;

/**
 * ���������, �������������� ��� ���������� ������� �� �����
 * � ������������ � ��������� ����������� �����������
 * @see PropertyAcceptor
 * @author mkarajani
 */
public class RegexPropertyNameAcceptor extends PropertyNameAcceptor {
	public RegexPropertyNameAcceptor(boolean invert) {
		super(invert);
	}

	protected boolean check(Properties properties, String propertyName) {
		boolean result = super.check(properties, propertyName);
		if(!result) {
			Iterator iterator = properties.keySet().iterator();
			while (iterator.hasNext()) {
				if(propertyName.matches((String) iterator.next())) {
					result = true;
					break;
				}
			}
		}
		return result;
	}
}
