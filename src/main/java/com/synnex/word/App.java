package com.synnex.word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

/**
 * Hello world!
 * 
 */
public class App {
	private static String newPath = "c:/Users/yanz/Documents/word/";

	public static void main(String[] args) throws IOException {
		List<String> readLines = FileUtils.readLines(new File("c:/Users/yanz/Documents/word.txt"), "utf-16");
		for (String wordString : readLines) {
			if (isNumber(wordString.charAt(0))) {
				String word = wordString.substring(wordString.indexOf(',') + 2, wordString.indexOf("  "));
				getGAFromICB(word);
				getRPFromICB(word);
			}
		}
	}

	public static File getGAFromICB(String word) throws IOException {
		File wordKing = getWordKing(word, "a.vCri_laba", "-ga");
		return wordKing;
	}

	public static File getRPFromICB(String word) throws IOException {
		File wordKing = getWordKing(word, "a.ico_sound[title=真人发音]", "-rp");
		return wordKing;
	}

	public static File getWordKing(String word, String position, String suffix)  {
		String url = "http://www.iciba.com/search?s=" + word;
		Elements links = null;
		try {
			links = Jsoup.connect(url).userAgent("Mozilla").timeout(5000).get().select(position);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(word);
		}
		String saveFile = newPath + word + suffix + ".mp3";
		if (links != null) {
			String attr = links.attr("onclick");
			if (attr != null && attr.indexOf("'") >= 0) {
				String[] split = attr.split("'");
				System.out.println(saveFile);
				httpDownload(split[1], saveFile);
				return new File(saveFile);
			}
		}
		return null;
	}

	static boolean isNumber(char charAt) {
		if (charAt >= 49 && charAt <= 57) {
			return true;
		}
		return false;
	}

	public static void httpDownload(String httpUrl, String saveFile) {
		File destFile = new File(saveFile);
		if (destFile.exists()) {
			return;
		}
		long start = System.currentTimeMillis();
		// 下载网络文件
		int byteread = 0;

		if (httpUrl != null && httpUrl.trim() != "") {
			URL url = null;
			try {
				url = new URL(httpUrl);
			} catch (MalformedURLException e1) {
				System.out.println("error:" + saveFile);
				System.out.println(e1.getMessage());
				return;
			}
			FileOutputStream fs = null;
			String name = System.currentTimeMillis() + "tmp.file";
			File file = new File(name);
			try {
				URLConnection conn = url.openConnection();
				if (conn != null) {

					InputStream inStream = conn.getInputStream();
					fs = new FileOutputStream(file);

					byte[] buffer = new byte[1204];
					while ((byteread = inStream.read(buffer)) != -1) {
						fs.write(buffer, 0, byteread);
					}
					long end = System.currentTimeMillis();
					System.out.print((int) (file.length() * 1d / (end - start)));
					System.out.println("K/s.");

					FileUtils.copyFile(file, destFile);
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fs != null) {
						fs.flush();
						fs.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (file != null) {
					file.delete();
				}
			}

		}
	}
}
