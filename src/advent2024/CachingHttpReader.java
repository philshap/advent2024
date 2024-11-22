package advent2024;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class CachingHttpReader {
    private static final Path CACHE = Paths.get("resources/cache");

    static String getData(URL url) {
        try {
            return new String(get(url));
        } catch (IOException e) {
            throw new RuntimeException("Could not read data from " + url, e);
        }
    }

    private static byte[] get(URL url) throws IOException {
        byte[] data = cacheGet(url);
        if (data == null) {
            final URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Cookie", "session=" + System.getenv("SESSION_TOKEN"));
            urlConnection.connect();
            try (var stream = (InputStream) urlConnection.getContent()) {
                data = stream.readAllBytes();
                cachePut(url, data);
            }
        }
        return data;
    }

    private static byte[] cacheGet(URL key) throws IOException {
        Path path = CACHE.resolve(urlToKey(key));
        if (Files.exists(path)) {
            return Files.readAllBytes(path);
        }
        return null;
    }

    private static void cachePut(URL key, byte[] data) throws IOException {
        Files.createDirectories(CACHE);
        Path path = CACHE.resolve(urlToKey(key));
        Files.write(path, data);
    }

    private static String urlToKey(URL key) {
        // Remove /s to make a valid filename.
        return key.getFile().substring(1).replaceAll("/", "-");
    }
}
