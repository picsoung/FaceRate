package models;

import java.util.Date;

import siena.Column;
import siena.DateTime;
import siena.Max;
import siena.embed.EmbeddedMap;

@EmbeddedMap
public class ImageInfo {
	
	public String imageUrl;
	
	public boolean isAFace;
	
	public boolean wearingGlasses;
	
	public String mood;
	
	public String gender;
	
	public long creationDate;
	
	public ImageInfo(Image image) {
		imageUrl = image.imageUrl;
		isAFace = image.isAFace;
		wearingGlasses = image.wearingGlasses;
		mood = image.mood;
		gender = image.gender;
		creationDate = image.creationDate.getTime();
	}
}
