package ibs.util.reflect.propertyfilter;

import java.beans.PropertyDescriptor;

/**
 * Реализация {@link PropertyFilter}, позволяющая выстраивать
 * фильтры в цепь
 * @author mkarajani
 */
public abstract class FilterChain implements PropertyFilter {
	private final FilterChain _parent = this;
 
	/**
	 * Связвает текущий фильтр и <code>filter</code> в цепь: текущий фильтр
	 * становится хвостом, а контейнер для <code>filter</code> - головой
	 * @param filter Связуемый фильтр, определяющий логику принятия
	 * свойств головой цепи
	 * @return Голова новой цепи фильтров
	 */
	public final FilterChain chain(final PropertyFilter filter) {
		return _parent.contains(filter)
			? _parent // защита от рекурсивного замыкания
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
	 * Проверяет содержится ли в текущей цепи фильтр <code>filter</code> 
	 * @param filter Проверяемый фильтр
	 * @return Результат проверки
	 */
	protected boolean contains(PropertyFilter filter) {
		return equals(filter);
	}
}
