package seminar2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class CrawlerManager {
	List<List<Elements>> elementsList = new ArrayList<>();
	private HtmlParser htmlParser;
	private Downloader downloader;
	public CrawlerManager() {
		this.htmlParser = new HtmlParser();
		this.downloader = new Downloader();
	}
	public void start(String startUrl, String baseDir, int maxDepth) {
		crawl(startUrl, baseDir, 1, maxDepth);
		htmlParser.rewriteToLocalPaths(null, baseDir);
	}
	private void crawl(String url, String baseDir, int depth, int maxDepth) {
		if (depth > maxDepth) {
			return;
		}
		try {
			Document doc = Jsoup.connect(url).get();
			
			elementsList.add(htmlParser.makeLinks(doc));
			List<Elements> resourceUrls = elementsList.get(depth-1);
			Elements links = resourceUrls.get(3);
			for (Element link : links) {
				String nextUrl = link.absUrl("href");
				crawl(nextUrl, baseDir, depth+1, maxDepth);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return;
	}
}
