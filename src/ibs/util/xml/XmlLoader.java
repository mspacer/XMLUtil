package ibs.util.xml;

import ibs.util.io.StreamUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Загружает XML-файл в строку с возможностью переиспользования
 * частей XML-файлов.<br>
 * Переиспользование обеспечивается с помощью XML-директивы
 * <code>&lt;?include?&gt;</code>, которую и обрабатывает
 * утилита при загрузке файла. Формат директивы следующий:<br>
 * <code>&lt;?include file="<i>file_path</i>"
 * <i>( attr_name="attr_value" ... )</i>  ?&gt;</code>, где<ul>
 * <li><code><i>file_path</i></code> - путь к вкладываемому файлу.
 * Может быть как абсолютный, так и относительный.
 * Относительный путь обрабатывается относительно файла,
 * в котором вызвана текущая директива.</li>
 * <li><code><i>attr_name</i></code> - название атрибута для автозамены
 * во вкладываемом файле.</li>
 * <li><code><i>attr_value</i></code> - значение атрибута.</li></ul>
 * Директива может принимать любое количество дополнительных атрибутов
 * и заменяет во вкладываемом файле все вхождения
 * <code>$<i>attr_name</i></code> на соответствующее значение
 * <code><i>attr_value</i></code> в порядке их следования в директиве.<br>
 * Пример:<br>
 * В файле <code>foo.xml</code> присутствует следующая директива:
 * <code>&lt;?include file="include/bar.inc" foo1="bar1" foo2="bar2"
 * ?&gt;</code>. Утилита сделает следующие действия: <ol>
 * <li>Загрузит содержимое файла <code>include/foo.inc</code></li>
 * <li>В содержимом загруженного файла заменит все вхождения
 * <code>$foo1</code> на <code>bar1</code></li>
 * <li>В полученном содержимом заменит все вхождения
 * <code>$foo2</code> на <code>bar2</code></li>
 * <li>В файле <code>foo.xml</code> заменит описанную директиву
 * на полученное в предыдущих пунктах содержимое</li>
 * </ol>
 * Директивы и ссылки на атрибуты, не соответствующие описанному формату,
 * не обрабатываются и остаются в XML-файле без изменений.
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
		// экранируем "задние" ссылки
		return input.replaceAll("\\$", "\\\\\\$");
	}
	
	/**
	 * Загружает XML из файла в строку
	 * @param filePath путь к файлу
	 * @param encoding кодировка содержимого файла
	 * @return XML
	 * @throws IOException
	 */
	public static String load(String filePath, String encoding) throws IOException {
		return load(new File(filePath), encoding);
	}
	
	
	

}
