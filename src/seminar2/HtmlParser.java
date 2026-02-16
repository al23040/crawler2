package seminar2;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
	private FileManager fileManager;
	private Downloader downloader;
	private PathRegistry pathRegistry;
	public HtmlParser(FileManager fileManager, Downloader downloader, PathRegistry pathRegistry) {
		this.downloader = downloader;
		this.fileManager = fileManager;
		this.pathRegistry = pathRegistry;
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
			String htmlPath;
			if (pathRegistry.isRegistered(imgUrl) ) {
				htmlPath = pathRegistry.getPath(imgUrl);
			}
			else {
				String path = fileManager.saveImage();
				htmlPath = downloader.downloadImage(imgUrl, path);
				pathRegistry.register(imgUrl, htmlPath);
			}
			img.attr("src", htmlPath);
			img.removeAttr("srcset");
		}
		return;
	}
	
	private void rewriteCssFilesPaths(Elements cssFiles) {
		for (Element css : cssFiles) {
			String cssUrl = css.attr("abs:href");
			String path;
			if (pathRegistry.isRegistered(cssUrl)) {
				path = pathRegistry.getPath(cssUrl);
			}
			else {
				path = fileManager.saveCss();
				downloader.downloadFile(cssUrl, fileManager.baseDir+path);
				pathRegistry.register(cssUrl, path);
			}
			css.attr("href", path);
		}
		return;
	}
	
	//jsに時間があったら統一!!
	private void rewriteScriptsPaths(Elements scripts) {
		for (Element script : scripts) {
			String jsUrl = script.attr("abs:src");
			String path;
			if (pathRegistry.isRegistered(jsUrl)) {
				path = pathRegistry.getPath(jsUrl);
			}
			else {
				path = fileManager.saveJs();
				downloader.downloadFile(jsUrl, fileManager.baseDir+path);
				pathRegistry.register(jsUrl, path);
			}
			script.attr("src", path);
		}
		return;
	}
	
	public void rewriteLinks(Elements links) {
		for (Element link : links) {
			String originalUrl = link.absUrl("href");
            
            if (pathRegistry.isRegistered(originalUrl)) {
                String path = pathRegistry.getPath(originalUrl);
                link.attr("href",path);
            }
		}
	}
}
