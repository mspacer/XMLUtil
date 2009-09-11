package ibs.util.reflect.propertyfilter;

import ibs.util.xml.XmlFormBean;

import java.beans.PropertyDescriptor;

/**
 * Объявление компонента для фильтрации свойств Java Bean  
 * @see XmlFormBean 
 * @author mkarajani
 */
public interface PropertyFilter {
	/**
	 * Фильтр, принимающий все свойства
	 */
	PropertyFilter EMPTY = new PropertyFilter() {
		public boolean accept(Class clazz, PropertyDescriptor property) {
			return true;
		}
	};

	/**
	 * Метод, решающий принимать свойство или нет
	 * @param clazz Тип для Java Bean
	 * @param property Описание свойства
	 * @return Результат решения о принятии свойства
	 */
	boolean accept(Class clazz, PropertyDescriptor property);
}
