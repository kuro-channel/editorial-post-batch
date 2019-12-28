package jp.kurochanel.batch.editorialpost.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Scraping {

    public void scraping() throws IOException {

        // Jsoup使ってみる
        Document document = Jsoup.connect("http://www.google.co.jp").get();
        System.out.println(document.html());
    }
}
