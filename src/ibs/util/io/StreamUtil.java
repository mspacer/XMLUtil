package ibs.util.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Утилита для работы с потоками
 * @author mkarajani
 */
public final class StreamUtil {
	private static final int BUFFER_SIZE = 2048;

	private StreamUtil() {
	}

	/**
	 * Загружает входной поток в {@link ByteArrayOutputStream}
	 * @param input Входной поток
	 * @return Загруженный поток
	 * @throws IOException
	 */
	public static ByteArrayOutputStream load(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output;
	}

	/**
	 * Копирует данные из входного потока в выходной
	 * @param input Входной поток
	 * @param output Выходной поток
	 * @throws IOException
	 */
	public static void copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		int count;
		while ((count = input.read(buffer, 0, BUFFER_SIZE)) != -1) {
			output.write(buffer, 0, count);
		}
		output.flush();
	}
	
	/**
	 * Загружает содержимое входного потока в строку
	 * @param input Входной поток
	 * @param encoding Кодировка выходной строки
	 * @return Загруженную строку
	 * @throws IOException
	 */
	public static String load(InputStream input, String encoding) throws IOException {
		return load(input).toString(encoding);
	}
}
