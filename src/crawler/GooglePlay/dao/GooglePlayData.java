package crawler.GooglePlay.dao;

public class GooglePlayData {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getComments() {
		return comments;
	}
	public void setComments(int comments) {
		this.comments = comments;
	}

	public String getReleaseDate() {
		return rel_date;
	}
	public void setReleaseDate(String rel_date) {
		this.rel_date = rel_date;
	}

	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}

	private int		id;
	private String	title;
	private String	author;
	private String	genre;
	private int		comments;
	private String	rel_date;
	private double	rate;
}
