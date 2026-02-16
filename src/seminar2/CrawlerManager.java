package seminar2;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class CrawlerManager {
	String baseDir;
	private HtmlParser htmlParser;
	private Downloader downloader;
	private FileManager fileManager;
	private PathRegistry pathRegistry;
	
	private Map<String, String> urlToLocalPath = new HashMap<>();
	
	public CrawlerManager(String baseDir) {
		this.baseDir = baseDir;
		this.fileManager = new FileManager(baseDir);
		this.downloader = new Downloader(baseDir);
		this.pathRegistry = new PathRegistry();
		this.htmlParser = new HtmlParser(fileManager, downloader, pathRegistry);		
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
			//urlToLocalPath.put(url, path);
			pathRegistry.register(url, path);
			Document doc = Jsoup.connect(url).get();
			
			List<Elements> resourceUrls = htmlParser.makeLinks(doc);
			
			Elements links = resourceUrls.get(3);
			for (Element link : links) {
				String nextUrl = link.absUrl("href");
				crawl(nextUrl, baseDir, depth+1, maxDepth);
			}
			
			htmlParser.rewriteLinks(links);
			htmlParser.rewriteToLocalPaths(resourceUrls);
			downloader.downloadHtml(baseDir + path, doc);
			
		} catch(IOException e) {
			//urlToLocalPath.remove(url);
			pathRegistry.unregister(url);
			e.printStackTrace();	
		}
		return;
	}
}
