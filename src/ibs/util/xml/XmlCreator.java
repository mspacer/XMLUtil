package ibs.util.xml;

import ibs.util.lang.StringUtil;

final class XmlCreator {
	private XmlCreator() {
	}

	public static StringBuffer createElement(StringBuffer buffer, String name, Object value) {
		String _name = StringUtil.escapeXml(name); 
		buffer.append("<").append(_name);

		if(null == value) {
			buffer.append("/>");
		} else {
			buffer.append(">").append(value).append("</").append(_name).append(">");
		}

		return buffer;
	}


	public static String createElement(String name, Object value) {
		return createElement(new StringBuffer(), name, value).toString();
	}
}
