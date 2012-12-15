package org.ligi.cloudgoban;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Game {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String encodedKey;

	@Persistent
	private Text sgf;

	@Persistent
	private Date created;

	@Persistent
	private String editkey;

	public Game() {
		// still a problem
		// http://stackoverflow.com/questions/13848667/nfe-when-using-date-date-and-cloud-endpoints-with-android
		// created=new Date();
	}

	public Text getSgf() {
		return sgf;
	}

	public void setSgf(Text sgf) {
		this.sgf = sgf;
	}

	public String getEncodedKey() {
		return encodedKey;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getEditKey() {
		return editkey;
	}

	public void setEditkey(String parentkey) {
		this.editkey = parentkey;
	}

}
