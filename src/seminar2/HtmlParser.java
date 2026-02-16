package seminar2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
	private FileManager fileManager;
	private Downloader downloader;
	public HtmlParser(FileManager fileManager, Downloader downloader) {
		this.downloader = downloader;
		this.fileManager = fileManager;
	}
	
	public List<Elements> makeLinks(Document doc) {
		List<Elements> resourceUrls = new ArrayList<>();
		resourceUrls.add(makeImages(doc));
		resourceUrls.add(makeCssFiles(doc));
		resourceUrls.add(makeScripts(doc));
		resourceUrls.add(makeNextUrls(doc));
		return resourceUrls;
	}
	private Elements makeImages(Document doc) {
		Elements images = doc.select("img");
		return images;
	}
	private Elements makeCssFiles(Document doc) {
		Elements cssFiles = doc.select("link[rel=stylesheet]");
		return cssFiles;
	}
	private Elements makeScripts(Document doc) {
		Elements scripts = doc.select("script[src]");
		return scripts;
	}
	private Elements makeNextUrls(Document doc) {
		Elements links = doc.select("a[href]");
		return links;
	}
	
	public void rewriteToLocalPaths(List<Elements> resourceUrls) {
		Elements images = resourceUrls.get(0);
		rewriteImagePaths(images);
		Elements cssFiles = resourceUrls.get(1);
		rewriteCssFilesPaths(cssFiles);
		Elements jsFiles = resourceUrls.get(2);
		rewriteScriptsPaths(jsFiles);
		
	}
	private void rewriteImagePaths(Elements images) {
		for (Element img : images) {
			String imgUrl = img.absUrl("src");
			String path = fileManager.saveImage();
			String htmlPath = downloader.downloadImage(imgUrl, path);
			img.attr("src", htmlPath);
			img.removeAttr("srcset");
		}
		return;
	}
	private void rewriteCssFilesPaths(Elements cssFiles) {
		for (Element css : cssFiles) {
			String cssUrl = css.attr("abs:href");
			String path = fileManager.saveCss();
			downloader.downloadFile(cssUrl, fileManager.baseDir + path);
			css.attr("href", path);
		}
		return;
	}
	//jsに時間があったら統一!!
	private void rewriteScriptsPaths(Elements scripts) {
		for (Element script : scripts) {
			String jsUrl = script.attr("abs:src");
			String path = fileManager.saveJs();
			downloader.downloadFile(jsUrl, fileManager.baseDir + path);
			script.attr("src", path);
		}
		return;
	}
	public void rewriteLinks(Elements links, Map<String, String> urlToLocalPath) {
		for (Element link : links) {
			String originalUrl = link.absUrl("href");
            
            if (urlToLocalPath.containsKey(originalUrl)) {
                String localPath = urlToLocalPath.get(originalUrl);
                link.attr("href", localPath);
            }
            else {
            	
            }
		}
	}
}
