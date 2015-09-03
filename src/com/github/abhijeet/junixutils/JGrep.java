/**
 * 
 */
package com.github.abhijeet.junixutils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;

/**
 * @author abhijeet.burle
 *
 */
public class JGrep {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.out.println("Usage : JGrep <pattern> <path> <filepattern>");
			System.exit(1);
		} else {
			long start = System.currentTimeMillis();
			JGrep.process(args[0],args[1],args[2]);
			long end = System.currentTimeMillis();
			System.out.println("Time [" + (end - start) + "]");
		}
	}

	public static void process(String pattern,String filePath, String fileExtensionPattern) throws IOException {
		Files.walk(new File(filePath).toPath())
				// .filter(p -> !p.toString()
				// .contains(File.separator + "."))
				.filter(p -> p.toString().endsWith(fileExtensionPattern)).parallel().forEach(path -> {
					try {
						// When filteredLines is closed, it closes underlying
						// stream as well as underlying file.
						try (Stream<String> filteredLines = Files.lines(path, StandardCharsets.ISO_8859_1)
								// .onClose(() -> System.out.println("[print grep summary for the "+path.toAbsolutePath()+" file"))
								.parallel().filter(s -> s.contains(pattern))) {
							System.out.println(path);
							filteredLines.parallel().forEach(s -> System.out.println(s));
						}
					} catch (Throwable ioexp) {
						System.err.println(path);
						ioexp.printStackTrace(System.err);
					}
				});
	}
}
