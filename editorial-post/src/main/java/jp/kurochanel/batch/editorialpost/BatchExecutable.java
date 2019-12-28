package jp.kurochanel.batch.editorialpost;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BatchExecutable implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // システム日付の取得
        Date date = new Date();

        // Jsoup使ってみる
        Document document = Jsoup.connect("http://www.google.co.jp").get();
        System.out.println(document.html());
    }

}
