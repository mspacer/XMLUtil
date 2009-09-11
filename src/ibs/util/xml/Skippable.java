package ibs.util.xml;

import ibs.util.reflect.propertyfilter.DefaultFilter;

/**
 * Интерфейс-маркер. Свойства с типом, реализующим интерфейс {@link Skippable}, не учавствуют в XML-сериализации Java Bean
 * @author mkarajani
 * @see DefaultFilter
 * @see XmlFormBean
 */
public interface Skippable {
}
