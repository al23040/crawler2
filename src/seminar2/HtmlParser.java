package seminar2;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
	private Downloader downloader;
	public HtmlParser() {
		this.downloader = new Downloader();
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
		Elements links = doc.select("abs:href");
		return links;
	}
	
	public void rewriteToLocalPaths(List<List<Elements>> elementsList, String baseDir) {
		int i = 0, j = 0, k = 0;
		for (List<Elements> resourceUrls : elementsList) {
			Elements images = resourceUrls.get(0);
			resourceUrls.set(0, rewriteImagePaths(images, baseDir, i));
			Elements cssFiles = resourceUrls.get(1);
			resourceUrls.set(1, rewriteCssFilesPaths(cssFiles, baseDir, j));
			Elements scripts = resourceUrls.get(2);
			resourceUrls.set(2, rewriteScriptsPaths(scripts, baseDir, k));
		}
		
	}
	private Elements rewriteImagePaths(Elements images, String baseDir, int i) {
		for (Element img : images) {
			String imgUrl = img.absUrl("src");
			if (imgUrl.isEmpty()) continue;
			downloader.downloadImage(imgUrl, i);
			//ファイル保存先の書き換え
		}
		return images;
	}
	private Elements rewriteCssFilesPaths(Elements cssFiles, String baseDir, int j) {
		for (Element css : cssFiles) {
			String cssUrl = css.attr("abs:href");
			String path = baseDir + "css/" + j + "_style.css";
			String htmlPath = "css/" + j + "_sytle.css";
			
			downloader.downloadFile(cssUrl, path);
			css.attr("href", htmlPath);
			j++;
		}
		return cssFiles;
	}
	private Elements rewriteScriptsPaths(Elements scripts, String baseDir, int k) {
		for (Element script : scripts) {
			String jsUrl = script.attr("abs:sec");
			String path = baseDir + "js/" + k + ".js";
			String htmlPath = "js/" + k + ".js";
			
			downloader.downloadFile(jsUrl, htmlPath);
			script.attr("src", htmlPath);
			k++;
		}
		return scripts;
	}
}
