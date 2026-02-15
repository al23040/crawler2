package seminar2;

import java.util.Scanner;

public class CrawlerMain {
	public static void main(String args[]) {
		Scanner scanner = new  Scanner(System.in);
		
		System.out.print("1:Wiki, 2:Rakuten ->");
		int num = scanner.nextInt();
		
		
		String url;
		String baseDir;
		if (num == 1) {
			url = "https://ja.wikipedia.org/wiki/%E3%83%A1%E3%82%A4%E3%83%B3%E3%83%9A%E3%83%BC%E3%82%B8";
			baseDir = "wiki/";
		}
		else if (num == 2) {
			url = "https://www.rakuten.co.jp/";
			baseDir = "rakuten/";
		}
		else {
			scanner.close();
			return;
		}
		
		System.out.print("Depth->");
		int maxDepth = scanner.nextInt();
		scanner.close();
		
		CrawlerManager crawlerManager = new CrawlerManager(baseDir);
		crawlerManager.start(url, baseDir, maxDepth);
	}
}
