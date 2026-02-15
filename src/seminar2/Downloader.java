package seminar2;

import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.nodes.Document;

public class Downloader {
	public String downloadResource(Document doc) {
		
	}
	public void downloadImage(String urlString, int i) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			String contentType = connection.getContentType();
			 String extension = ".png";
			 
			 if (contentType != null) {
				 if  (contentType.contains("image/png")) extension = ".jpg";
				 else if (contentType.contains("image/gif")) extension = ".gif";
	             else if (contentType.contains("image/webp")) extension = ".webp";
			 }
		}
	}
	public void downloadFile(String url, String path) {
		
	}
}
