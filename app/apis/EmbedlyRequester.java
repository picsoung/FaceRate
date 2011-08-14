package apis;

import java.io.IOException;

import play.Logger;
import play.Play;
import siena.Json;
import utils.UrlFetcher;

public class EmbedlyRequester {
	
	public static String getLinkHtml(String url) {
		try {
			String apiKey = Play.configuration.getProperty("embedly.apikey");
	    	String baseUrl = "http://api.embed.ly/1/oembed?key="+apiKey +"&url="+url;
	    	Logger.info("Requesting for " + baseUrl);
			String stringResponse;
			
				stringResponse = UrlFetcher.fetchUrl(baseUrl);
	
			Logger.info("Response " + stringResponse);
			Json json =  Json.loads(stringResponse);
			return json.get("html").toString();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
