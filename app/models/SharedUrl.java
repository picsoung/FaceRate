package models;

import java.util.Date;
import java.util.Map;

import siena.DateTime;
import siena.Id;
import siena.Max;
import siena.Model;
import siena.Table;
import siena.Text;
import siena.embed.Embedded;

@Table("shared_urls")
public class SharedUrl extends Model {
	@Id @Max(300)
	public String url;
	
	@Text
	public String html;
	
	@Embedded
	Map<String, ImageInfo> images;
	
	@DateTime
	public Date creationDate;
	
	@DateTime
	public Date lastUpdate;
	
	public void insert() {
		creationDate = new Date();
		super.insert();
	}
	
	public void update() {
		lastUpdate = new Date();
		super.update();
	}
}
