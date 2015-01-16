package crawler.GooglePlay;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawler.GooglePlay.dao.GooglePlayDAO;
import crawler.GooglePlay.dao.GooglePlayData;
import crawler.GooglePlay.util.SQLConnectorBase;
import crawler.GooglePlay.util.SQLiteConnector;

public class GooglePlayCrawler {
	public static void main(String []args) {
		GooglePlayData data = new GooglePlayData();
		GooglePlayCrawler crawler = new GooglePlayCrawler();

		/*
		 * Create dao and inject connector
		 * 		- database file and table will be created when needed
		 * 
		 * Note: database neither table will not be dropped automatically.
		 * You have to remove crawler.db file by manual when you need.
		 */
		SQLConnectorBase connector = new SQLiteConnector();
		GooglePlayDAO dao;
		try {
			dao = new GooglePlayDAO(connector, "crawler.db");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}

		System.out.println("Fetching data");
		try {
			crawler.crawlingGooglePlay(dao);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		System.out.println("");
		System.out.println("Listing: Order by number of comments");
		try {
			dao.queryData(GooglePlayDAO.ORDER_BY_STAR_RATING);
			while(dao.hasNext()) {
				dao.next();
				data = dao.getData();
				
				System.out.println(data.getTitle()
						+ ", " + String.valueOf(data.getRate())
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void crawlingGooglePlay(GooglePlayDAO dao) throws ParseException {
		Document docd;
		String cookie;
		StringBuilder href = new StringBuilder();

		System.setProperty("javax.net.ssl.trustStore", "resource/googleplay.jks");
	    try{
	    	for(int i = 0; i < 600; i+= 120) {
				Document doc = Jsoup.connect("https://play.google.com/store/apps/collection/topselling_free")
									.data("start", String.valueOf(i))
									.data("num", "120")
									.post();

		        Elements links = doc.select("a[class=card-click-target]");
		        for (Element link : links) {
		        	cookie = link.attr("data-server-cookie");
		        	href.delete(0, href.length());
		        	href.append("https://play.google.com").append(link.attr("href"));

		        	if (cookie.length() < 1) continue;

		        	for (int retry = 0; retry < 3; retry++) {
			        	try {
			        		docd = Jsoup.connect(href.toString()).get();
				        	System.out.println("Insert: " + docd.title());
				        	storeAData(dao, docd);
			        	} catch (SocketTimeoutException e) {
			        		continue;
			        	}
			        	break;
		        	}
		        }
	    	}
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	private void storeAData(GooglePlayDAO dao, Document docd) throws ParseException {
		GooglePlayData data = new GooglePlayData();
		SimpleDateFormat inFormat = new SimpleDateFormat("MMMMMMM dd, yyyy");
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
  	
    	Elements items = docd.select("div[class=document-title]");
    	Element item = items.first();
		data.setTitle((item != null && item.text() != null)? item.text(): docd.title());

		items = docd.select("div[itemprop=author]");
    	item = items.first();
		data.setAuthor(item.text());

    	items = docd.select("span[itemprop=genre]");
    	item = items.first();
		data.setGenre(item.text());

		items = docd.select("div[class=single-review]");
		data.setComments(items.size());
    	
    	items = docd.select("div[itemprop=datePublished]");
    	item = items.first();
		data.setReleaseDate(outFormat.format(inFormat.parse(item.text())));

    	items = docd.select("div[class=score]");
    	item = items.first();
		data.setRate(Double.parseDouble(item.text()));

		try {
			dao.setData(data);
			dao.insertData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
