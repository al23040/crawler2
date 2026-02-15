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
	private FileManager fileManager;
	public CrawlerManager(String baseDir) {
		this.fileManager = new FileManager(baseDir);
		this.htmlParser = new HtmlParser(fileManager);
		this.downloader = new Downloader();
	}
	public void start(String startUrl, String baseDir, int maxDepth) {
		crawl(startUrl, baseDir, 1, maxDepth);
		return;
	}
	private void crawl(String url, String baseDir, int depth, int maxDepth) {
		if (depth > maxDepth) return;
		try {
			Document doc = Jsoup.connect(url).get();
			
			List<Elements> resourceUrls = htmlParser.makeLinks(doc);
			
			Elements links = resourceUrls.get(3);
			for (Element link : links) {
				String nextUrl = link.absUrl("href");
				crawl(nextUrl, baseDir, depth+1, maxDepth);
			}
			
			htmlParser.rewriteToLocalPaths(resourceUrls);
			String path = fileManager.saveHtml();
			downloader.downloadHtml(path, doc);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return;
	}
}
