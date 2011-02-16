package ibs.util.reflect.propertyfilter;

import ibs.util.xml.Skippable;
import ibs.util.xml.XmlFormBean;

import java.beans.PropertyDescriptor;

/**
 * Реализация фильтра {@link PropertyFilter}, используемая
 * в {@link XmlFormBean} по умолчанию. Пропускает свойства с типами,
 * реализующими интерфейс {@link Skippable}.
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
	 * Создаёт фильтр по умолчанию и связывает его в цепь
	 * с <code>filter</code>  
	 * @param filter 
	 * @return Цепь из <code>filter</code> и фильтра по умолчанию
	 * @see FilterChain#chain(PropertyFilter) 
	 */
	public static FilterChain withDefault(PropertyFilter filter) {
		return new DefaultFilter().chain(filter);
	}
}
