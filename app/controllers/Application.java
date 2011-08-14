package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import models.Image;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import apis.EmbedlyRequester;
import apis.FaceComUtils;

import play.Logger;
import play.Play;
import play.mvc.Controller;
import siena.Json;
import utils.UrlFetcher;
import utils.Utils;

public class Application extends Controller {

    public static void index() {
    	String getUrl = params.get("url");
    	renderArgs.put("getUrl_html",EmbedlyRequester.getLinkHtml(getUrl));
		
        render();
    }
    
    public static void getLinkHTML(String url) throws IOException {
    	Logger.info("Trying to get the content for " + url);

		renderText(EmbedlyRequester.getLinkHtml(url));
    }
    
    public static void uploadRating(String img) throws IOException {
    	byte[] imageBytes = Base64.decodeBase64(img);
    	String imageName = ""+ System.currentTimeMillis();
		String imagePath = Utils.uploadImage(imageName, imageBytes);
		if (imagePath == null) {
			//TODO return error
		}
		
		Json faceInformation = FaceComUtils.getFaceInfromation(imagePath);
		if (!FaceComUtils.isFace(faceInformation)) {
			//TODO return error, is not a face
		}
		
		Logger.info("Is a face");
		
		Image image = new Image();
		
		image.isSmiling = FaceComUtils.isSmiling(faceInformation);
		image.wearingGlasses = FaceComUtils.isWeringGlasses(faceInformation);
		image.mood = FaceComUtils.getStringAttributeValue(faceInformation, "mood");
		image.moodConfidence = FaceComUtils.getAttributeConfidence(faceInformation, "mood");
		
		Logger.info(""+image.isSmiling);
		Logger.info(""+image.wearingGlasses);
		Logger.info(""+image.mood);
		Logger.info(""+image.moodConfidence);
		
		//image.insert();
		
		
    }

}