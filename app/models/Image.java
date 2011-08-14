package models;

import java.util.Date;

import siena.Column;
import siena.DateTime;
import siena.Id;
import siena.Max;
import siena.Model;
import siena.Table;

@Table("images")
public class Image extends Model {
	
	@Id @Max(300) @Column("image_url")
	public String imageUrl;
	
	@Column("is_a_face")
	public boolean isAFace;
	
	@Column("wearing_glasses")
	public boolean wearingGlasses;
	
	@Column("is_smiling")
	public boolean isSmiling;
	
	@Max(50)
	public String mood;
	
	@Column("mood_confidence")
	public int moodConfidence;
	
	@DateTime
	public Date creationDate;
	
	@Column("shared_url")
	public SharedUrl sharedUrl;
	
	public void insert() {
		creationDate = new Date();
		super.insert();
	}
	
	
}
