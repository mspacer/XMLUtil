package ibs.util.xml;

import ibs.util.io.StreamUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ��������� XML-���� � ������ � ������������ �����������������
 * ������ XML-������.<br>
 * ����������������� �������������� � ������� XML-���������
 * <code>&lt;?include?&gt;</code>, ������� � ������������
 * ������� ��� �������� �����. ������ ��������� ���������:<br>
 * <code>&lt;?include file="<i>file_path</i>"
 * <i>( attr_name="attr_value" ... )</i>  ?&gt;</code>, ���<ul>
 * <li><code><i>file_path</i></code> - ���� � ������������� �����.
 * ����� ���� ��� ����������, ��� � �������������.
 * ������������� ���� �������������� ������������ �����,
 * � ������� ������� ������� ���������.</li>
 * <li><code><i>attr_name</i></code> - �������� �������� ��� ����������
 * �� ������������ �����.</li>
 * <li><code><i>attr_value</i></code> - �������� ��������.</li></ul>
 * ��������� ����� ��������� ����� ���������� �������������� ���������
 * � �������� �� ������������ ����� ��� ���������
 * <code>$<i>attr_name</i></code> �� ��������������� ��������
 * <code><i>attr_value</i></code> � ������� �� ���������� � ���������.<br>
 * ������:<br>
 * � ����� <code>foo.xml</code> ������������ ��������� ���������:
 * <code>&lt;?include file="include/bar.inc" foo1="bar1" foo2="bar2"
 * ?&gt;</code>. ������� ������� ��������� ��������: <ol>
 * <li>�������� ���������� ����� <code>include/foo.inc</code></li>
 * <li>� ���������� ������������ ����� ������� ��� ���������
 * <code>$foo1</code> �� <code>bar1</code></li>
 * <li>� ���������� ���������� ������� ��� ���������
 * <code>$foo2</code> �� <code>bar2</code></li>
 * <li>� ����� <code>foo.xml</code> ������� ��������� ���������
 * �� ���������� � ���������� ������� ����������</li>
 * </ol>
 * ��������� � ������ �� ��������, �� ��������������� ���������� �������,
 * �� �������������� � �������� � XML-����� ��� ���������.
 * @author mkarajani
 */
public final class XmlLoader {
	private static final Pattern includePattern = Pattern.compile("<\\?\\s*include\\s+file\\s*=\\s*\"([^\"]+)\"((?:\\s+\\w+\\s*=\\s*\"[^\"]+\")*)\\s*\\?>");
	private static final Pattern paramPattern = Pattern.compile("\\s+(\\w+)\\s*=\\s*\"([^\"]+)\"");

	private XmlLoader() {
	}
	
	private static String load(File file, String encoding) throws IOException {
		FileInputStream input = new FileInputStream(file);
		String output = StreamUtil.load(input, encoding);
		input.close();
		Matcher matcher = includePattern.matcher(output);
		File parentFile = file.getAbsoluteFile().getParentFile();
		StringBuffer result = new StringBuffer();
		while(matcher.find()) {
			String path = matcher.group(1);
			File include = new File(path);
			if(!include.isAbsolute()) {
				include = new File(parentFile, path);
			}
			String pattern = load(include, encoding);
			Matcher params = paramPattern.matcher(matcher.group(2));
			while(params.find()) {
				pattern = pattern.replaceAll("\\$"+params.group(1)+"(?!\\w+)", regexEscape(params.group(2)));
			}
			matcher.appendReplacement(result, regexEscape(pattern));
		}
		matcher.appendTail(result);
		return result.toString();
	}
	
	private static String regexEscape(String input) {
		// ���������� "������" ������
		return input.replaceAll("\\$", "\\\\\\$");
	}
	
	/**
	 * ��������� XML �� ����� � ������
	 * @param filePath ���� � �����
	 * @param encoding ��������� ����������� �����
	 * @return XML
	 * @throws IOException
	 */
	public static String load(String filePath, String encoding) throws IOException {
		return load(new File(filePath), encoding);
	}
	
	
	

}
