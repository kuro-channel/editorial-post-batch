package jp.kurochanel.batch.editorialpost.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽出する社説の会社名＆URL
 */
@AllArgsConstructor
@Getter
public enum Company {

    ASAHI("朝日新聞", "https://www.asahi.com/news/editorial.html"),
    MAINICHI("毎日新聞", "https://mainichi.jp/editorial/"),
    YOMIURI("読売新聞", "https://www.yomiuri.co.jp/editorial/"),
    SANKEI("産経新聞", "https://www.sankei.com/column/newslist/editorial-n1.html"),
    TOKYO("東京新聞", "https://www.tokyo-np.co.jp/article/column/editorial/");

    // 社名
    private String CompanyName;

    // URL
    private String url;
}
