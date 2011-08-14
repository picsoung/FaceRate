package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import apis.EmbedlyRequester;
import apis.FaceComRequester;

import play.Logger;
import play.Play;
import play.mvc.Controller;
import siena.Json;
import utils.UrlFetcher;
import utils.Utils;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void getLinkHTML(String url) throws IOException {
    	Logger.info("Trying to get the content for " + url);
    	
		renderJSON(EmbedlyRequester.getLinkHtml(url));
    }
    
    public static void uploadRating(String img) throws IOException {
    	byte[] imageBytes = Base64.decodeBase64(img);
    	String imageName = ""+ System.currentTimeMillis();
		String imagePath = Utils.uploadImage(imageName, imageBytes);
		if (imagePath == null) {
			//TODO return error
		}
		
		Json faceInformation = FaceComRequester.getFaceInfromation(imagePath);
		if (!faceInformation.get("face").asBoolean()) {
			//TODO return error, is not a face
		}
		
		
		
		//Route to the image
		//https://s3.amazonaws.com/face-rate/imageName
    }

}