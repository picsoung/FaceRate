package utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class UrlFetcher {

	public static String fetchUrl(String url) throws IOException {
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
    	InputStream in = conn.getInputStream();
		byte[] bytes = IOUtils.toByteArray(in);
		return new String(bytes);
	}
}
