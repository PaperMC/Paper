Paper
[![Version](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fartifactory.papermc.io%2Fartifactory%2Funiverse%2Fio%2Fpapermc%2Fpaper%2Fpaper-api%2Fmaven-metadata.xml&strategy=highestVersion&filter=26.2*&label=version&color=%23344ceb)](https://papermc.io/downloads/paper)
[![Paper Build Status](https://img.shields.io/github/actions/workflow/status/PaperMC/Paper/build.yml?branch=main)](https://github.com/PaperMC/Paper/actions)
[![Discord](https://img.shields.io/discord/289587909051416579.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/papermc)
[![GitHub Sponsors](https://img.shields.io/github/sponsors/papermc?label=GitHub%20Sponsors)](https://github.com/sponsors/PaperMC)
[![Open Collective](https://img.shields.io/opencollective/all/papermc?label=OpenCollective%20Sponsors)](https://opencollective.com/papermc)
===========

最も広く使われている高性能なMinecraftサーバーで、ゲームプレイやメカニクスの不整合を修正することを目標としています。

**サポートとプロジェクトの議論:**
- [フォーラム](https://forums.papermc.io/) または [Discord](https://discord.gg/papermc)

サーバー管理者向け
------
Paperclip は通常の jar ファイルと同様にダウンロードして実行できる jar です。

Paper は [ダウンロードページ](https://papermc.io/downloads/paper) から入手できます。

サーバーで直接 Paperclip jar を実行してください。

* Paper の利用方法ドキュメント: [docs.papermc.io](https://docs.papermc.io)
* 今後の機能を確認: [GitHub Projects](https://github.com/PaperMC/Paper/projects)

プラグイン開発者向け
------
* API は [ここ](paper-api) を参照してください
* 今後追加予定の API や最近追加された API は [こちら](https://github.com/orgs/PaperMC/projects/2/views/4)
* Paper API javadocs: [papermc.io/javadocs](https://papermc.io/javadocs/)
#### paper-api のリポジトリ
詳細は [ドキュメント](https://docs.papermc.io/paper/dev/project-setup/#adding-paper-as-a-dependency) を参照してください。
##### Gradle
```kotlin
repositories {
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}
```
##### Maven

```xml
<repository>
    <id>papermc</id>
    <url>https://repo.papermc.io/repository/maven-public/</url>
</repository>
```

```xml
<dependency>
    <groupId>io.papermc.paper</groupId>
    <artifactId>paper-api</artifactId>
    <version>[26.2.build,)</version>
    <scope>provided</scope>
</dependency>
```

ソースから Jar をビルドする方法
------
Paper をコンパイルするには JDK 25 とインターネット接続が必要です。

このリポジトリをクローンし、ターミナルで `./gradlew applyPatches` を実行した後、`./gradlew createPaperclipJar` を実行します。生成された jar は `paper-server/build/libs` に出力されます。

すべてのタスクを確認するには `./gradlew tasks` を実行してください。

GitHub Releases からのダウンロード
------
`v*` タグが付けられると、自動的に GitHub Actions がビルドを実行し、GitHub Releases に Paperclip の `.jar` ファイルを公開します。公開されたリリースページのアセットとして `.jar` を直接ダウンロードできます。

プルリクエストの作成方法
------
[CONTRIBUTING.md](CONTRIBUTING.md) を参照してください。

古いバージョン (1.21.3 以前)
------
バージョン 1.8 〜 1.21.3 のブランチについては、[Paper-archive](https://github.com/PaperMC/Paper-archive) を参照してください。

サポートについて
------
まず、貢献を検討していただきありがとうございます。

PaperMC は主にインフラ費用などの定期的な支出があります。Paper は [Open Collective](https://opencollective.com/) を通じて運営されています。より詳細は [公式サイト](https://papermc.io/sponsors) をご覧ください。

以下のリンクから支援できます。

* [Open Collective](https://opencollective.com/papermc)
* [GitHub Sponsors](https://github.com/sponsors/PaperMC)

Special Thanks To:
-------------

[![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)](https://www.yourkit.com/)

[YourKit](https://www.yourkit.com/)、高機能な Java および .NET プロファイラを提供する企業です。PaperMC が OSS ライセンスを利用できるよう支援してくれたことに感謝します。

すべてのスポンサーに感謝します。  
[![Sponsor Image](https://raw.githubusercontent.com/PaperMC/papermc.io/data/sponsors.png)](https://papermc.io/sponsors)
