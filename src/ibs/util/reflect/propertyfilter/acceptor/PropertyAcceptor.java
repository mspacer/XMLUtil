package ibs.util.reflect.propertyfilter.acceptor;

import ibs.util.reflect.propertyfilter.PropertiesPropertyFilter;

import java.beans.PropertyDescriptor;
import java.util.Properties;

/**
 * Базовый компонент, предназначенный для переиспользования в
 * классах-наследниках {@link PropertiesPropertyFilter}
 * @author mkarajani
 */
public abstract class PropertyAcceptor {
	private final boolean invert;
	
	/**
	 * Конструктор, определяющий интерпретацию результата проверки
	 * свойства методом {@link #check(Properties, String)}
	 * @param invert Если <code>false</code>, то метод
	 * {@link #check(Properties, String)} определяет условие принятия
	 * свойства. Если <code>true</code>, то - условие непринятия.
	 */
	protected PropertyAcceptor(boolean invert) {
		this.invert = invert;
	}

	/**
	 * Основной метод компонента. Предназначен для использования в 
	 * {@link PropertiesPropertyFilter#accept(Class, PropertyDescriptor)}.
	 * @param properties Загруженные свойства
	 * @param propertyName Название проверяемого свойства
	 * @return Решение о принятии свойства
	 */
	public final boolean accept(Properties properties, String propertyName) {
		return invert ^ check(properties, propertyName);
	}
	
	/**
	 * Проверка свойства на соответствие фильтру
	 * @param properties Загруженные свойства
	 * @param propertyName Название проверяемого свойства
	 * @see #PropertyAcceptor(boolean)
	 * @see #accept(Properties, String)
	 * @return Результат проверки на соответствие
	 */
	protected abstract boolean check(Properties properties, String propertyName);
}
