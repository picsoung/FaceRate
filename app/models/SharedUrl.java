package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import siena.DateTime;
import siena.Generator;
import siena.Id;
import siena.Index;
import siena.Max;
import siena.Model;
import siena.Query;
import siena.Table;
import siena.Text;
import siena.embed.Embedded;

@Table("shared_urls")
public class SharedUrl extends Model {
	
	@Id(Generator.AUTO_INCREMENT)
	public Long id;
	
	@Max(300) @Index("url")
	public String url;
	
	@Text
	public String html;
	
	@Embedded
	public Map<String, ImageInfo> images;
	
	@DateTime
	public Date creationDate;
	
	@DateTime
	public Date lastUpdate;
	
	public SharedUrl() {
		images = new HashMap<String, ImageInfo>();
	}
	
	public void insert() {
		creationDate = new Date();
		super.insert();
	}
	
	public void update() {
		lastUpdate = new Date();
		super.update();
	}
	
	public static Query<SharedUrl> all() {
        return Model.all(SharedUrl.class);
	}
	
	public static SharedUrl getByUrl(String url) {
		return SharedUrl.all().filter("url", url).get();
	}
}
