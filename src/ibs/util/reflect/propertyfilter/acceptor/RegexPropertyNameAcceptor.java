package ibs.util.reflect.propertyfilter.acceptor;

import java.util.Iterator;
import java.util.Properties;

/**
 * Компонент, предназначеный для фильтрации свойств по имени
 * в соответствии с заданными регулярными выражениями
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
