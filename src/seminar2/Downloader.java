package seminar2;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
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
		InputStream in = null;
		FileOutputStream out = null;
		
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
            
            in = new BufferedInputStream(connection.getInputStream());
            out = new FileOutputStream(filePath);
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
            	out.write(buffer, 0, bytesRead);
            }
            
            System.out.println(filePath + "をダウンロードしました．");
            out.close();
            in.close();
            connection.disconnect();
            return htmlPath;
		} catch (IOException e) {
        	e.printStackTrace();
        	return null;
        }
	}
	public void downloadFile(String url, String path) {
		try {
            URL targetUrl = URI.create(url).toURL();
            
            HttpURLConnection conn = (HttpURLConnection) targetUrl.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

            try (InputStream in = conn.getInputStream()) {
                Files.copy(in, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
            }
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
