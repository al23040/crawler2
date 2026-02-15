package seminar2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class CrawlerManager {
	List<List<Elements>> elementsList = new ArrayList<>();
	private HtmlParser htmlParser;
	private Downloader downloader;
	private FileManager fileManager;
	
	private Map<String, String> urlToLocalPath = new HashMap<>();
	
	public CrawlerManager(String baseDir) {
		this.fileManager = new FileManager(baseDir);
		this.htmlParser = new HtmlParser(fileManager);
		this.downloader = new Downloader();
	}
	public void start(String startUrl, String baseDir, int maxDepth) {
		crawl(startUrl, baseDir, 1, maxDepth);
	}
	private void crawl(String url, String baseDir, int depth, int maxDepth) {
		if (urlToLocalPath.containsKey(url)) {
			return;
		}
		if (depth > maxDepth) return;
		try {
			String path = fileManager.saveHtml();
			urlToLocalPath.put(url, path);
			Document doc = Jsoup.connect(url).get();
			
			List<Elements> resourceUrls = htmlParser.makeLinks(doc);
			
			Elements links = resourceUrls.get(3);
			for (Element link : links) {
				String nextUrl = link.absUrl("href");
				crawl(nextUrl, baseDir, depth+1, maxDepth);
			}
			htmlParser.rewriteLinks(links, urlToLocalPath);
			htmlParser.rewriteToLocalPaths(resourceUrls);
			downloader.downloadHtml(path, doc);
		} catch(IOException e) {
			e.printStackTrace();	
		}
		return;
	}
}
