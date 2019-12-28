# editorial-post-batch
kuro-channel/editorial-post-batch

## 【社説を自動でbotするアプリ】
### ● 作成目的  
　論理的思考をもっとつけたい・・・説得力ある文章を書く第一歩として社説を読むといい！とあったので、  
　社説を読む習慣をつけようと思い、何かアプリで自動化することを考えた。  

### ● 毎日、各社のHP見ればいいじゃん！
　いや、それはそれでつまらない。。どうせだったら、普段の自分の生活の中で、自然と目に入る仕組みを作りたい。  
　→ botを毎日slackに投稿するような仕組みにしたい。それなら毎日みる！  

### ● 仕様検討
　各社の社説（朝・夕方）のWeb版HTMLを取得し、URLをslackに投稿する、という簡単アプリケーション。  
　
　**取得する社説（各社）まずは5個の新聞でいいかな～。**  

  1. 朝日新聞  https://www.asahi.com/news/editorial.html  
  2. 毎日新聞  https://mainichi.jp/editorial/  
  3. 読売新聞  https://www.yomiuri.co.jp/editorial/  
  4. 産経新聞  https://www.sankei.com/column/newslist/editorial-n1.html  
  5. 日経新聞（日経は公開していないらしい・・・あとどっか・・・）  日経は対象外
  6. 東京新聞  https://www.tokyo-np.co.jp/article/column/editorial/
　
### ● 実装仕様  
　・Java11.0 / kotlin（Unitテストのみ）  
　・Spring2.0  
　・定期実行（Linuxでcron実行）朝と夕方  
　・DBは使う？（要検討）→使わない。（いったんDBなしの方針）  
    履歴テーブルを作り、履歴テーブルを作成する。    
　　すでにその日に対象の社説をSlack投稿していたら、投稿しちゃダメ。  
　　→ DBはいいや。一日一回定期実行！  
　・ログ設計：bot送信ができなかったら、ERRORログを出力すること。  
　　→ ERRORログを出力したら、EROOR通知をslackに通知  
　・アプリの自動デプロイを実装（CI）→ CIはあんまり優先順位高くない！自分のみ運用だし・・・  
　・springboot × Dockerで！  
　・ジョブの仕様で、引数ごとに各新聞の社説のみ、実行することができる！  
　
### 【使うライブラリ】  
　・jsoup：HTMLをスクレイピングするライブラリ。  
