package ibs.util.xml;

import ibs.util.reflect.propertyfilter.DefaultFilter;
import ibs.util.reflect.propertyfilter.FilterChain;
import ibs.util.reflect.propertyfilter.PropertyFilter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import com.ximpleware.VTDException;

/**
 * ������ ��� ������������ �� Java Bean � XML � �������
 * @author mkarajani
 */
public class XmlFormBean {
	private static final String ENCODING = "utf8";
	private final String rootName;
	private byte[] xml;
	private Object bean;
	private final FilterChain filters;
	
	private static byte[] getBytes(String xml) throws UnsupportedEncodingException {
		return null == xml ? null : xml.trim().getBytes(ENCODING);
	}

	/**
	 * �������� ������ �� ����������� Java Bean
	 * @param rootName �������� ����-�����
	 * @param bean Java Bean
	 * @param filter ������ ������� (��. {@link PropertyFilter})
	 * @throws UnsupportedEncodingException �� �������������� ��������� utf8
	 */
	public XmlFormBean(String rootName, Object bean, PropertyFilter filter) throws UnsupportedEncodingException {
		this.rootName = rootName;
		this.bean = bean;
		this.filters = DefaultFilter.withDefault(filter);
		update();
	}
	
	/**
	 * �������� ������ �� ����������� Java Bean
	 * @param rootName �������� ����-�����
	 * @param bean Java Bean
	 * @throws UnsupportedEncodingException �� �������������� ��������� utf8
	 */
	public XmlFormBean(String rootName, Object bean) throws UnsupportedEncodingException {
		this(rootName, bean, PropertyFilter.EMPTY);
	}
	
	/**
	 * �������� ������ �� ����������� XML
	 * @param xml XML
	 * @param clazz ����� ��� Java Bean
	 * @throws UnsupportedEncodingException
	 * @throws VTDException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public XmlFormBean(String xml, Class clazz) throws UnsupportedEncodingException, VTDException, InstantiationException, IllegalAccessException, InvocationTargetException {
		this(xml, clazz, PropertyFilter.EMPTY);
	}
	
	/**
	 * �������� ������ �� ����������� XML
	 * @param xml XML
	 * @param clazz ����� ��� Java Bean
	 * @param filter ������ ������� (��. {@link PropertyFilter})
	 * @throws UnsupportedEncodingException
	 * @throws VTDException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public XmlFormBean(String xml, Class clazz, PropertyFilter filter) throws UnsupportedEncodingException, VTDException, InstantiationException, IllegalAccessException, InvocationTargetException  {
		this(getBytes(xml), clazz, filter);
	}

	/**
	 * �������� ������ �� ����������� XML
	 * @param xml XML
	 * @param clazz ����� ��� Java Bean
	 * @param filter ������ ������� (��. {@link PropertyFilter})
	 * @throws VTDException
	 * @throws UnsupportedEncodingException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */

	public XmlFormBean(byte[] xml, Class clazz, PropertyFilter filter) throws VTDException, UnsupportedEncodingException, InstantiationException, IllegalAccessException, InvocationTargetException  {
		this.xml = xml;
		Navigator nav = new Navigator(xml); 
		if(!nav.hasRoot()) {
			throw new RuntimeException("Invalid XML ["+getXmlString()+"]");
		}
		this.rootName = nav.elementName();
		this.filters = DefaultFilter.withDefault(filter);
		this.bean = XmlFormHelper.getInstance().read(nav, clazz, filters);
		
	}

	/**
	 * @return Java Bean
	 */
	public Object getBean() {
		return bean;
	}

	/**
	 * @return XML
	 * @throws UnsupportedEncodingException �� �������������� ��������� utf8
	 */
	public final String getXmlString() throws UnsupportedEncodingException {
		return null == xml ? null : new String(xml, ENCODING);
	}

	/**
	 * @return XML
	 */
	public byte[] getXml() {
		return xml;
	}
	
	/**
	 * ���������� XML �� ����������� Java Bean
	 * @throws UnsupportedEncodingException �� �������������� ��������� utf8
	 */
	public final void update() throws UnsupportedEncodingException {
		xml = getBytes(XmlCreator.createElement(rootName, XmlFormHelper.getInstance().write(bean, filters)));
	}

}
