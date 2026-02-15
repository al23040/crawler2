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
	}
	public String saveImage() {
		String path = "images/" + imgCount;
		imgCount++;
		return path;
	}
	public String saveCss() {
		String path = "css/" + cssCount + "_style.css";
		cssCount++;
		return path;
	}
	public String saveJs() {
		String path = "js/" + jsCount + ".js";
		jsCount++;
		return path;
	}
	public String saveHtml() {
		String path = htmlCount + ".html";
		htmlCount++;
		return path;
	}
}
