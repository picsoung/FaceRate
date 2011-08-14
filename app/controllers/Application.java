package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import play.Logger;
import play.Play;
import play.mvc.Controller;
import siena.Json;
import utils.Utils;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void getLinkHTML(String url) throws MalformedURLException, IOException {
    	Logger.info("Trying to get the content for " + url);
    	String apiKey = (String) Play.configuration.get("embedly.apikey");
    	String baseUrl = "http://api.embed.ly/1/oembed?key="+apiKey +"&url="+url;//api.embed.ly/1/oembed?key=:key&url=:url&maxwidth=:maxwidth&maxheight=:maxheight&format=:format&callback=:callback
    	Logger.info("Requesting for " + baseUrl);
    	URL u = new URL(baseUrl);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
    	InputStream in = conn.getInputStream();
		byte[] bytes = IOUtils.toByteArray(in);
		String stringResponse = new String(bytes);
		Logger.info("Response " + stringResponse);
		Json json =  Json.loads(stringResponse);
		renderJSON(json.get("html").toString());
    }
    
    public static void uploadRating(String img) {
    	Logger.info("Receiving data " + img);
    	byte[] imageBytes = Base64.decodeBase64(img);
    	String imageName = ""+ System.currentTimeMillis();
		Utils.uploadImage(imageName, imageBytes);
		//Route to the image
		//https://s3.amazonaws.com/face-rate/testing.img
    }

}