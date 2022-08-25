package beans;

import java.util.Date;
import java.util.List;

public class Image {
	private int id;
	private String title;
	private Date date;
	private String description;
	private String source;
	private int albumId;
	private List<Comment> comments;
	
	public List<Comment> getComments(){
		return comments;
	}
	
	public void addComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getAlbumId() {
		return albumId;
	}
	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
}
