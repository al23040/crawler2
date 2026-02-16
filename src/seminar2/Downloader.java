package seminar2;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.jsoup.nodes.Document;

public class Downloader {
	String baseDir;
	public Downloader(String baseDir) {
		this.baseDir = baseDir;
	}
	
	public String downloadImage(String urlStr, String path) {
		HttpURLConnection connection = null;
		
		try {
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            String contentType = connection.getContentType();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.err.println("HTTP Error: " + connection.getResponseCode() + " " + urlStr);
                return null;
            }
            
            String filePath = baseDir + path + getExtension(contentType);
            String htmlPath = path + getExtension(contentType);
            download(connection, filePath);
            return htmlPath;
		} catch (IOException e) {
        	e.printStackTrace();
        	return null;
        }
	}
	
	public void downloadFile(String urlStr, String path) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
			connection.setConnectTimeout(5000);
			download(connection, path);
        } catch (Exception e) {
            System.err.println("ダウンロードエラー");
        }
	}
	
	public void downloadHtml(String path, Document doc) {
		try {
			Files.write(Paths.get(path), doc.outerHtml().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void download(HttpURLConnection connection, String path) {
		try (InputStream in = new BufferedInputStream(connection.getInputStream())) {
        	Files.copy(in, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
        	System.err.println(path + "のダウンロード失敗");
        }
		System.out.println(path + "をダウンロードしました．");
	}
	
	public String getExtension(String contentType) {
		if (contentType == null) return ".jpg";
		
		String mimeType = contentType.split(";")[0].trim().toLowerCase();
		
		switch (mimeType) {
        case "image/jpeg":
        case "image/jpg":
            return ".jpg";  
        case "image/png":
            return ".png";
        case "image/gif":
            return ".gif";
        case "image/webp":
            return ".webp";
        case "image/svg+xml":
            return ".svg";
        case "image/bmp":
        case "image/x-ms-bmp":
            return ".bmp";
        case "image/vnd.microsoft.icon":
        case "image/x-icon":
            return ".ico";
        default:
            return null; 
		}
	}
}
