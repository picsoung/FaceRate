package apis;

import java.io.IOException;

import play.Logger;
import play.Play;
import siena.Json;
import utils.UrlFetcher;

public class FaceComRequester {
	public static Json getFaceInfromation(String imagePath) {
		try {
			String url = "http://api.face.com/faces/detect.json?api_key=" 
				+ Play.configuration.getProperty("face.apikey")
				+ "&api_secret="+Play.configuration.getProperty("face.secret")
				+ "&urls="+imagePath;
			String stringResponse = UrlFetcher.fetchUrl(url);
			Json json =  Json.loads(stringResponse);
			Logger.info("Response from face.com " + json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
