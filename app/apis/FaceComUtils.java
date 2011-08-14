package apis;

import java.io.IOException;

import play.Logger;
import play.Play;
import siena.Json;
import utils.UrlFetcher;

public class FaceComUtils {
	public static Json getFaceInfromation(String imagePath) {
		try {
			String url = "http://api.face.com/faces/detect.json?api_key=" 
				+ Play.configuration.getProperty("face.apikey")
				+ "&api_secret="+Play.configuration.getProperty("face.secret")
				+ "&urls="+imagePath
				+ "&attributes=all";
			String stringResponse = UrlFetcher.fetchUrl(url);
			Json json =  Json.loads(stringResponse);
			Logger.info("Response from face.com " + json.toString());
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isSmiling(Json data) {
		if (getBooleanAtributeValue(data, "smiling") == true && getAttributeConfidence(data, "smiling") > 50)
			return true;
		return false;
	}
	
	public static boolean isWeringGlasses(Json data) {
		if (getBooleanAtributeValue(data, "glasses") == true && getAttributeConfidence(data, "glasses") > 50)
			return true;
		return false;
	}
	
	public static boolean isFemale(Json data) {
		if ("female".equals(getStringAttributeValue(data, "gender")) && getAttributeConfidence(data, "gender") > 50)
			return true;
		return false;
	}
	
	public static boolean isFace(Json data) {
		if (getBooleanAtributeValue(data, "face") && getAttributeConfidence(data, "face") > 50)
			return true;
		return false;
	}
	
	public static boolean getBooleanAtributeValue(Json data, String attributeName) {
		Logger.info("Trying to get " + attributeName + " attribute " + data.get("photos").at(0).get("tags").at(0).get("attributes").toString());
		return "true".equals(data.get("photos").at(0).get("tags").at(0).get("attributes").get(attributeName).get("value").str()) ? true : false ;
	}
	
	public static int getAttributeConfidence(Json data, String attributeName) {
		return data.get("photos").at(0).get("tags").at(0).get("attributes").get(attributeName).get("confidence").asInt();
	}
	
	public static String getStringAttributeValue(Json data, String attributeName) {
		Logger.info("Trying to get " + attributeName + " attribute " + data.get("photos").at(0).get("tags").at(0).get("attributes").toString());
		return data.get("photos").at(0).get("tags").at(0).get("attributes").get(attributeName).get("value").str();
	}
}
