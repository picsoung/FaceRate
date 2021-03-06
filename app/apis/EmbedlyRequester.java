package apis;

import java.io.IOException;

import play.Logger;
import play.Play;
import siena.Json;
import utils.UrlFetcher;

public class EmbedlyRequester {
	
	public static String getLinkHtml(String url) {
		try {
			Integer max_width = 450;
			String apiKey = Play.configuration.getProperty("embedly.apikey");
	    	String baseUrl = "http://api.embed.ly/1/oembed?key="+apiKey +"&url="+url+"&maxwidth="+max_width;
	    	Logger.info("Requesting for " + baseUrl);
			String stringResponse;
			
				stringResponse = UrlFetcher.fetchUrl(baseUrl);
	
			Logger.info("Response " + stringResponse);
			Json json =  Json.loads(stringResponse);
			return json.get("html").str();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
