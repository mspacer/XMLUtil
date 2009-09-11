package ibs.util.xml;

import ibs.util.lang.StringUtil;
import ibs.util.reflect.propertyfilter.PropertyFilter;
import ibs.util.xml.Navigator.ChildrenIterator;
import ibs.util.xml.Navigator.TextReader;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ximpleware.ModifyException;
import com.ximpleware.NavException;

final class XmlFormHelper {
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final Logger logger = Logger.getLogger(XmlFormHelper.class);
	private static final XmlFormHelper instance = new XmlFormHelper();
	private static final Object[] args = {};
	private final Map propertiesMap = new HashMap();
	private static final List ignoredPackages = Arrays.asList(new String[]{
			"java.lang", "java.util", "java.sql", "java.math"
	});

	private XmlFormHelper() {
	}

	public static XmlFormHelper getInstance() {
		return instance;
	}

	private static Object readProperty(Object bean, PropertyDescriptor property) {
		Object result = null;
		Method getter = property.getReadMethod();
		if(null != getter) {
			try {
				result = getter.invoke(bean, args);
			} catch (Exception e) {
				logger.error("#readProperty", e);
			}
		}
		return result;
	}
	
	Object write(Object bean, PropertyFilter filter) {
		if(null == bean) {
			return null;
		}
		Class clazz = bean.getClass();
		PropertyDescriptor[] properties = getProperties(clazz);
		if(null == properties || 0 == properties.length) {
			return objectToString(bean);
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < properties.length; i++) {
			PropertyDescriptor property = properties[i];
			if(null == property.getReadMethod() || !filter.accept(clazz, property)) {
				continue;
			}
			String propertyName = property.getName();
			Object value = readProperty(bean, property);
			if(null != value) {
				if(value.getClass().isArray()) {
					Object[] values = (Object[]) value;
					for (int j = 0; j < values.length; j++) {
						XmlCreator.createElement(buffer, propertyName, write(values[j], filter));
					}

				} else {
					XmlCreator.createElement(buffer, propertyName, write(value, filter));
				}
			} else {
				XmlCreator.createElement(buffer, propertyName, null);
			}
		}
		return buffer;
	}

	private boolean isEmpty(PropertyDescriptor[] properties) {
		return null == properties || 0 == properties.length;
	}
	
	Object read(Navigator nav, Class clazz, PropertyFilter filter) throws InstantiationException, IllegalAccessException, NavException, IllegalArgumentException, InvocationTargetException, ModifyException, UnsupportedEncodingException {
		PropertyDescriptor[] properties = getProperties(clazz);
		if(isEmpty(properties)) {
			return createObject(nav.reader(), clazz);
		}
		return read(nav, clazz.newInstance(), properties, filter);	
	}
	
	Object read(Navigator nav, Object result, PropertyDescriptor[] properties, PropertyFilter filter) throws InstantiationException, IllegalAccessException, NavException, IllegalArgumentException, InvocationTargetException, ModifyException, UnsupportedEncodingException {
		for (int i = 0; i < properties.length; i++) {
			PropertyDescriptor property = properties[i];
			Class propertyType = property.getPropertyType();
			if(!filter.accept(result.getClass(), property)) {
				continue;
			}
			boolean read = false;
			String propertyName = property.getName();
			Method setter = property.getWriteMethod();
			if(null != setter) {
				Object value = null;
				if(propertyType.isArray()) {
					Class componentType = propertyType.getComponentType();
					Vector values = new Vector();
					ChildrenIterator iterator = nav.iterator(propertyName);
					while(iterator.hasNext()) {
						values.add(read(iterator.next(), componentType, filter));
						read = true;
					}
					value = Array.newInstance(componentType, values.size());
					values.copyInto((Object[]) value);


				} else {
					if(nav.hasChild(propertyName)) {
						value = read(nav, propertyType, filter);
						read = true;
					}
				}

				setter.invoke(result, new Object[] {value});
			} else {
				Object value = readProperty(result, property);
				if(null != value) { // чтение структурирующих свойств
					PropertyDescriptor[] subproperties = getProperties(value.getClass());
					if(!isEmpty(subproperties) && nav.hasChild(propertyName)) {
						read(nav, value, subproperties, filter);
						read = true;
					}
				}
			}

			if(read) {
				nav.parent();
			}
		}
		return result;
	}

	private synchronized PropertyDescriptor[] getProperties(Class clazz) {
		PropertyDescriptor[] result;
		if(propertiesMap.containsKey(clazz)) {
			result = (PropertyDescriptor[]) propertiesMap.get(clazz);
		} else {
			Package _package = clazz.getPackage();
			String packageName = null != _package ? _package.getName() : null;
			if(ignoredPackages.contains(packageName)) {
				result = null;
			} else {
				try {
					result = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
				} catch (IntrospectionException e) {
					logger.error("#getProperties", e);
					result = null;
				}
			}
			propertiesMap.put(clazz, result);
		}

		return result;
	}

	private Object createObject(TextReader reader, Class clazz) throws NavException {
		Object result = null;
		if(reader.hasText()) {
			String value = reader.getText();
			if(String.class.equals(clazz)) {
				result = value; 
			} else if(Date.class.isAssignableFrom(clazz)) {
				Date parsedDate = null;
				try {
					parsedDate = dateFormat.parse(value);
				} catch (java.text.ParseException e) {
					logger.error("Unparsable date ["+value+"]");
				}
				if(null != parsedDate) {
					try {
						result = clazz.newInstance();
					} catch (Exception e) {
						logger.error("No #ctor() found for "+clazz, e);
					}
					if(null != result) {
						((Date)result).setTime(parsedDate.getTime());
					}
				}
			} else if(value.length() > 0) {
				Constructor ctor = null;
				try {
					ctor = clazz.getDeclaredConstructor(new Class[]{String.class});
				} catch (Exception e) {
					logger.error("No #ctor(String) found for "+clazz, e);
				}
				if(ctor != null) {
					try {
						result = ctor.newInstance(new Object[]{value});
					} catch (Exception e) {
						logger.error("Error while invoke #ctor(String) for "+clazz, e);
					}
				}
			}
		}
		return result;
	}
	
	private String objectToString(Object object) {
		String result;
		Class clazz = object.getClass();
		if(Date.class.isAssignableFrom(clazz)) {
			result = dateFormat.format((Date)object);
		} else {
			result = object.toString();
		}
		return StringUtil.escapeXml(result);
	}
}
