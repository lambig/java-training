Java課題は本リポジトリで行う。  
本リポジトリはメンテナンス中以外はアーカイブされているため、読み取り専用として扱うこと。

# 0. 準備

VSCode上での開発を想定。
Extension Pack for javaの利用を推奨。(そのうち設定ファイルに追加予定)

1. コンテナ外での操作
   1. リポジトリをクローンする。
   1. リポジトリルートに移動してコンソールを開き、docker-compose up でjava環境を起動する。
   1. VSCodeのremote explorerで↑で起動したコンテナに接続する
      * ディレクトリはusr/project/appディレクトリを選択する
1. コンテナ内での操作(appディレクトリ)
   1. コンソールからgradle buildコマンドを実行する (npm iや composer installのようなつもりで)
   1. プロダクトコードはsrc/main/java以下、テストコードはsrc/test/java以下に配置される。
   1. gradle run testコマンドを実行する (これでテストが実行できる)

---

# 1. bookパッケージ: 本をしまうなら……

初期状態ではbookパッケージのBookAppで処理を行っている。  
改善に当たってはプロダクト/テスト双方とも、bookパッケージ内部全体を改修してよいので  
こころおきなく手腕を振るわれたい。   
ただし、テストの既存ケースはそのまま残すこと。

## シチュエーション1 重複なく本を確保したい

`BookApp#シチュエーション1_本のリストに重複なく本を足したい` メソッドがテーマ  
このメソッド自体は一応目的を達成しているが、  
不満点が山とある状態からスタート

- 複雑度が高すぎるし見通しが悪すぎる
- BookApp以降、本リストに重複がないことの保証は都度行う必要がある
- 状態依存が大きく、何か他の条件等を追加するときの改修が非常にやりにくい

---

# 2. dateパッケージ: 「日付」

## シチュエーション1 日付、と、日付……？
あるサービスにおいて、日付文字列を取得するためのAPIが一つ定義されている。  
※ここでのAPIはコントローラを模したアプリケーションクラス DateApi とする。

- 現時点での実日時を返却するAPI
  - ISO8601拡張形式(秒まで)の文字列で返却する
    - 例: 1985-10-26T01:35:00

ここに、次の新しいAPIを追加したい。

- 現在の営業日付、および現在営業中であるかを返却するAPI
  - 例: 1985年10月25日 / false


APIの定義はスタブとして用意されたものを利用して、実装を付与してみよう。

### ゴール
1. 業務知識をクラスに落とし込むにあたって、概念図を作成する
1. 概念図に基づきメソッドを実装し、UTでその動作を保証する
   1. スタブメソッドを実装する
   1. スタブメソッドのUTを実装する
   1. 既存メソッドのUTを実装する
   1. 既存メソッドをリファクタする

### 仕様に関して

- 日付の扱いについてはOffsetDateTimeとAsia/Tokyoのタイムゾーンを利用する。(改修前実装参照)
  - 実プロダクトでは詳細に考慮すること
  - 参考: https://qiita.com/dmikurube/items/15899ec9de643e91497c
- このサービスの「実日時」では、日付の秒数を「切り捨て」て扱うことになっている。
  - 実日時と言いつつ生の日時データでない点に注意
- 「営業日時」について
  - 実日時と異なり、秒数は「繰り上げ」て処理することになっている。
  - 実日時と異なり、午前4時を境に日付が変わり、それまでは前日の扱いとなる。
    - (どうやら午前0時から実行される日次集計バッチの終了を待っているらしい……)
  - このサービスにおいて、営業時間は 09:00 ～ 18:00 としている。
- このAPIにはまともなUTがない。このタイミングで実装するべきだろう。
  - まずはテスト可能性を付与する必要がありそうだ。

### ヒント
- どうやらOffsetDateTimeをそのまま引っ張りまわすのは筋が悪そう
- 営業日付を出力するにせよ、内部では日時で持った方が良さそう
- Composition over inheritance
- 実日時と営業日時には、できればメソッドに互換性を持たせたい

#### 日時処理の例

| OffsetDateTime | 実日時 | 営業日付 | メモ |
| ---- | ---- | ---- | ---- |
| 1985-10-25T23:59:59 | 1985-10-25T23:59:00 | 1985年10月25日 | 実日時の秒切捨て |
| 1985-10-26T03:59:00 | 1985-10-26T03:59:00 | 1985年10月25日 | 営業日付が遅れている |
| 1985-10-26T03:59:01 | 1985-10-26T03:59:00 | 1985年10月26日 | 秒切上げで営業日付の遅れが消えた |
| 1985-10-26T04:00:00 | 1985-10-26T04:00:00 | 1985年10月26日 | 営業日付が実日付と一致する時刻 |
