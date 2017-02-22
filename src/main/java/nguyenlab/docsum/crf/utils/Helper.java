package nguyenlab.docsum.crf.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Helper {
	public static File[] listDocComFiles(File dir) throws IOException {
		Map<String, Boolean> seen = new ConcurrentHashMap<String, Boolean>();
		return Files.walk(dir.toPath()).map(p -> p.toFile()).filter(f -> {
			return seen.putIfAbsent(f.getName(), Boolean.TRUE) == null && f.isFile() && f.getName().endsWith(".txt");
		}).toArray(File[]::new);
	}
}
