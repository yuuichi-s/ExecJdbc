JavaからJDBC実行(JAR)
====

Overview

## Description
jarコマンドでjdbcを使用してSQL実行する。

## Usage
java -classpath ojdbc6.jar -jar execJdbc.jar <DB設定プロパティファイル> <外部SQLファイル> [DEBUG]

例）
java -classpath ojdbc6.jar -jar execJdbc.jar dbConnect.properties test.sql

Windowsの場合、エラーレベルを下記で取得(成功:0, 失敗:-1)
echo %ERRORLEVEL%
