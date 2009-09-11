package ibs.util.reflect.propertyfilter.acceptor;

import java.util.Properties;

/**
 * Компонент, предназначеный для фильтрации свойств по имени
 * @see PropertyAcceptor
 * @author mkarajani
 */
public class PropertyNameAcceptor extends PropertyAcceptor {
	public PropertyNameAcceptor(boolean invert) {
		super(invert);
	}

	protected boolean check(Properties properties, String propertyName) {
		return properties.containsKey(propertyName);
	}
}
