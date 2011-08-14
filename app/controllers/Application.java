package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import models.Image;
import models.ImageInfo;
import models.SharedUrl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import apis.EmbedlyRequester;
import apis.FaceComUtils;

import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.templates.JavaExtensions;
import siena.Json;
import utils.UrlFetcher;
import utils.Utils;

public class Application extends Controller {

    public static void index() {
    	String getUrl = params.get("url");
    	SharedUrl sharedUrl = SharedUrl.getByUrl(getUrl);
    	String html = "";
    	if (sharedUrl == null) {
    		html = EmbedlyRequester.getLinkHtml(getUrl);
    	} else {
    		html = sharedUrl.html;
    	}
    	renderArgs.put("getUrl_html", html);
        render(sharedUrl);
    }
    
    public static void getLinkHTML(String url) throws IOException {
    	Logger.info("Trying to get the content for " + url);
    	String html = null;
    	SharedUrl sharedUrl = SharedUrl.getByUrl(url);
    	if (sharedUrl == null) {
    		html = EmbedlyRequester.getLinkHtml(url);
    		sharedUrl =  new SharedUrl();
    		sharedUrl.url = url;
        	sharedUrl.html = html;
        	sharedUrl.insert();
    	} else {
    		html = sharedUrl.html;
    	}
    	
		renderText(html);
    }
    
    public static void uploadRating(String url, String img) throws IOException {
    	Logger.info("Url " + url);
    	byte[] imageBytes = Base64.decodeBase64(img);
    	String imageName = ""+ System.currentTimeMillis();
		String imagePath = Utils.uploadImage(imageName, imageBytes);
		if (imagePath == null) {
			Json data = Json.map().put("status", "error").put("errorMessage", "Image not received");
			renderJSON(data.toString());
		}
		
		Json faceInformation = FaceComUtils.getFaceInfromation(imagePath);
		if (!FaceComUtils.isFace(faceInformation)) {
			Json data = Json.map().put("status", "error").put("errorMessage", "It's not a face");
			renderJSON(data.toString());
		}
		
		Logger.info("Is a face");
		
		SharedUrl sharedUrl = SharedUrl.getByUrl(url);
		if(sharedUrl != null) {
			Logger.info("Is not null");
		} else {
			Logger.info("Is null");
		}
		Image image = new Image();
		
		image.isSmiling = FaceComUtils.isSmiling(faceInformation);
		image.wearingGlasses = FaceComUtils.isWeringGlasses(faceInformation);
		image.mood = FaceComUtils.getStringAttributeValue(faceInformation, "mood");
		image.moodConfidence = FaceComUtils.getAttributeConfidence(faceInformation, "mood");
		image.imageUrl = imagePath;
		image.sharedUrl = sharedUrl;
		image.insert();
		
		sharedUrl.images.put(image.id+"", new ImageInfo(image));
		sharedUrl.update();
		
		
		Json data = Json.map().put("status", "ok").put("imageUrl", image.imageUrl).put("mood", image.mood);
		renderJSON(data.toString());
    }

}