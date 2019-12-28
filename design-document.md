## 簡易設計書
### Spring BootアプリケーションからSlack通知を送る
- Slack Incoming Webhooksを使用する。  
https://api.slack.com/messaging/webhooks  

**■ 送りたいメッセージ形式を考えよう。**

```
 "blocks": [
        {
            "type": "section",
            "text": {
                "type": "mrkdwn",
                "text": "Danny Torrence left the following review for your property:"
            }
        },
        {
            "type": "section",
            "block_id": "section567",
            "text": {
                "type": "mrkdwn",
                "text": "<https://example.com|Overlook Hotel> \n :star: \n Doors had too many axe holes, guest in room 237 was far too rowdy, whole place felt stuck in the 1920s."
            },
            "accessory": {
                "type": "image",
                "image_url": "https://is5-ssl.mzstatic.com/image/thumb/Purple3/v4/d3/72/5c/d3725c8f-c642-5d69-1904-aa36e4297885/source/256x256bb.jpg",
                "alt_text": "Haunted hotel image"
            }
        },
        {
            "type": "section",
            "block_id": "section789",
            "fields": [
                {
                    "type": "mrkdwn",
                    "text": "*Average Rating*\n1.0"
                }
            ]
        }
    ]
```   

### Spring Bootの構成について（環境構築について）
- Spring Boot2.2.2（使うなら最新版）  
- CommandLineアプリケーション
- ビルドツール：Mavenを使用  
- エラー発生時はEROORログを出力し、エラー通知メールを送信する。
- DBなし（履歴は特に不要）  
- AWS EC2使用（サーバを常時実行）  
　- Amazon Linux2
 
＜メモ＞
```
●Slackのコメントをgithubにpushすることは可能？ - 出来ないっぽい・・・
- 社説を読んだままにするのは嫌だ。要約力つけたいし、自分でもフィードバックする仕組みが欲しい。
　- 社説投稿の最後に「githubの該当リンクを送る」
　　→ 社説の要約をまとめる為のgithubリポジトリを作り、そこに自分の要約まとめを積み上げていく。
```

### ユースケース
- 毎時〇時にバッチ実行  
→ 引数：日時（yyyy-MM-dd）

### ドメイン（責務）
- スクレイピング（最新の社説記事を抽出する）
- エラー時ログ出力
- Slack投稿（該当日時の最新記事を投稿）

###  Maven - Gradleインストール編（TODO）
- What`s Maven?  
・Mavenとは？  

### クラス設計
■ Application  
　- BatchRunner（implements CommandLineRunner）run()  
　- BatchExecutable（バッチ実行クラス）解析する - 各社ごと  
　- Exception（実行例外クラス）  
　- Service or Processor  
　- constant（定数クラス）  
　- enum -> 各新聞社定義 会社名 - URL  

### dependency
```
今回のアプリケーションについては、「DBは使わない」ので、MyBatis等のORマッパーやDBコネクターは不要としてます。
テストは「Springブートスターター」の"spring-boot-starter-test"を使用。その他、仕様として「メール送信」の要件が
あるので、"spring-boot-starter-mail"も追加しています。認証機能もないので、Spring-securityも不要。
あとは、Javaのライブラリで有名な「Google Guava」や「apache.commons」も入れます。※Lomboxは必須ですね！
```
- lombok  
https://mvnrepository.com/artifact/org.projectlombok/lombok  
- apache.commons  
https://mvnrepository.com/artifact/org.apache.commons/commons-lang3  
- Google Guava  
https://mvnrepository.com/artifact/com.google.guava/guava  

### 参考メモ
【バッチの設計 - クラス設計】  

＜参考＞  
● Spring Bootでコマンドラインアプリを作る時の注意点  
https://qiita.com/tag1216/items/898348a7fc3465148bc8  
  　
・アプリケーションプロパティの場合 spring.main.web-environment=falseを指定すれば良いのでは？  
　
- なぜ？Spring Bootで作るのか？  　
- Springの特徴① ：DI（依存性の注入）  

```
あるクラスが別のクラスをインスタンス変数に持つなどして利用 (依存) している場合に、
インスタンス変数の設定 (依存性の解決) をクラス内で行うのではなく、外部から設定 (注入) するという考え方
→ いちいちNewしなくていいよ。インスタンスの管理はDIコンテナでやるんで。
```
　　
　
● Spring Bootの基礎  
https://www.ibm.com/developerworks/jp/java/library/j-spring-boot-basics-perry/index.html  
  　
**What's Component Scan？（コンポーネントスキャンとは？）**
- クラスを作って@Componentを書いて、@Autowireでメンバーに注入する。  
- Spring Bootでは、デフォルトだと自身のパッケージ配下のクラスに対してコンポーネントスキャンする  

【対象法】
・コンポーネントスキャンするパッケージを変更する。  
　→ 具体的には、アプリケーションクラスで@SpringBootApplicationに  
　　scanBasePackages = {"コンポーネントスキャン対象のパッケージ"}を指定してやればOK。  
　　https://reasonable-code.com/another-package-di/  
  
【クラス設計】
● 参考：Spring BootでWebじゃないコマンドラインアプリケーションを作る  
http://innossh.hatenablog.com/entry/2017/03/23/010609  
  　
- Application.java - main(String[] args)  
・Applicationクラスには @SpringBootApplication をつける  
・Spring Bootのアプリケーションを作るには、mainメソッドで SpringApplication を生成する。（SpringApplicationBuilder）  
　
```
　@SpringBootApplication
	public class NonWebSpringBootApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(NonWebSpringBootApplication.class).web(false).run(args);
    }
}

＜ざっくり説明＞
① Spring Bootのアプリケーションを作るには、mainメソッドで SpringApplication を生成します。
そして @SpringBootApplication というアノテーションを付けるだけでほぼ完了です。
@ComponentScan や @EnableAutoConfiguration の機能を備えているのでそれだけで大丈夫なのです。
　
② 続いてWebじゃないコマンドラインアプリケーションなので、処理の入り口を作ってあげます。
下記のように新しいクラスとして定義しても良いですし、 CommandLineRunner のBeanだけ作ってあげても大丈夫です。
runメソッド内に処理を記述しましょう。
```

```
　@Component
　@Slf4j
　public class NonWebCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        boolean verbose = appArgs.containsOption("verbose");
        appArgs.getNonOptionArgs().stream().forEachOrdered((s -> {
            boolean isValid = jsonValidator.validate(s, verbose);
            log.info(isValid ? "It's a JSON object!" : "It's not a JSON object.");
        }));
    }
}
```

↓↓これでもOK！！
```
@SpringBootApplication
public class SampleApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(SampleApplication.class);
    app.run(args);
  }

  @Autowired
  Foo foo;

  @Override
  public void run(String... args) throws Exception {
    foo.bar();
  }
}
```

● メインとなるアプリケーションクラスを1つ作り、@SpringBootApplicationアノテーションを付与、
  そのクラスのmainにて、自分のクラスを指定してSpringApplicationインスタンスを作って、runするだけ。


　
```
● 冬休みで学ぶべきこと（体系的に学び、そして「自分の言葉で伝えられるようにする」）
　・Java Webアーキテクチャー概要　
　- Webシステムの基本的な仕組みを説明したうえで、Java EE及びSpringの2つの代表的なフレームワークを対象とし、
　各テクノロジーの特徴や役割、それらを組みわせてWebシステムを構築する方法についてご紹介します。
　https://www.casareal.co.jp/ls/service/openseminar/java/j150
　
　・しっかり学ぶモダンJavaScript　-関数、モジュール、DOM、ECMAScript2019-
　- JavaScript本格入門をしっかり読んで、まとめる。理解する。
　→ 読んで学んだことを使って、ポートフォリオを作る。
　https://www.casareal.co.jp/ls/service/openseminar/html/s101
```
