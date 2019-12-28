package jp.kurochanel.batch.editorialpost;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BatchExecutable implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // システム日付の取得
        Date date = new Date();

    }

}
