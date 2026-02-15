package seminar2;

import java.io.File;

public class FileManager {
	public String baseDir;
	private int imgCount = 1;
	private int cssCount = 1;
	private int jsCount = 1;
	private int htmlCount = 1;
	
	public FileManager(String baseDir) {
		this.baseDir = baseDir;
		new File(baseDir + "images").mkdirs();
		new File(baseDir + "css").mkdirs();
		new File(baseDir + "js").mkdirs();
		new File(baseDir + "html").mkdirs();
	}
	public String saveImage() {
		String path = baseDir + "img_" + imgCount;
		imgCount++;
		return path;
	}
	public String saveCss() {
		String path = baseDir + "css/" + cssCount + "_style.css";
		cssCount++;
		return path;
	}
	public String saveJs() {
		String path = baseDir + "js/" + jsCount + ".js";
		jsCount++;
		return path;
	}
	public String saveHtml() {
		String path = baseDir + "html/" + htmlCount + ".html";
		htmlCount++;
		return path;
	}
}
