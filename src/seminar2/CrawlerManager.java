package seminar2;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class CrawlerManager {
	public void start(String startUrl, String baseDir, int maxDepth) {
		
	}
	private void crawl(String url, String baseDir, int depth, int maxDepth) {
		try {
			Document doc = Jsoup.connect(url).get();
		}
		
	}
}
