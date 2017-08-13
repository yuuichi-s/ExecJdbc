package jp.co.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * [0]:プロパティファイル [1]:SQLファイル [2]:ログレベル(DEBUG or 引数なし)
 *
 */
public class ExecJdbc {

    public static void main(String args[]) throws SQLException, ClassNotFoundException {

        String url = null;
        String orajdbc = null;
        String user = null;
        String pass = null;
        String sqlPath = null;
        String sql = null;

        boolean debug = false;

        if (1 < args.length) {
            if (2 < args.length && args[2].equals("DEBUG")) {
                debug = true;
            }

            Properties conf = getPropertyFile(args[0], debug);
            orajdbc = conf.getProperty("orajdbc");
            url = conf.getProperty("oraurl");
            user = conf.getProperty("orauser");
            pass = conf.getProperty("orapass");

            sqlPath = args[1];
            sql = readSql(sqlPath, debug);
        } else {
            System.out.println("引数＝[0]:プロパティファイル [1]:SQLファイル [2]:ログレベル(DEBUG or 引数なし)");
            System.exit(-1); // プログラム終了
        }

        // Oracle JDBC Driverのロード
        Class.forName(orajdbc);
        // Oracleに接続
        Connection conn = DriverManager.getConnection(url, user, pass);
        // ステートメントを作成
        Statement stmt = conn.createStatement();
        // 問合せの実行
        ResultSet rset = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = rset.getMetaData();

        // 問合せ結果の表示
        while (rset.next()) {
            StringBuffer sb = new StringBuffer();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                sb.append(rset.getString(i));
                sb.append("\t");
            }

            if (debug) {
                System.out.println(sb.toString().trim());
            }
        }
        // 結果セットをクローズ
        rset.close();
        // ステートメントをクローズ
        stmt.close();
        // 接続をクローズ
        conn.close();

        System.exit(0); // プログラム終了
    }

    private static Properties getPropertyFile(String prop, boolean debug) {
        String filename = prop; // プロパティファイルのファイル名
        Properties conf = new Properties();
        try {
            conf.load(new FileInputStream(filename));
        } catch (IOException e) {
            if (debug) {
                System.err.println("Cannot open " + filename + ".");
                e.printStackTrace();
            }
            System.exit(-1); // プログラム終了
        }

        return conf;
    }

    private static String readSql(String sqlFile, boolean debug) {
        File file = new File(sqlFile);
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            if (debug) {
                e.printStackTrace();
            }
            System.exit(-1); // プログラム終了
        }

        BufferedReader br = new BufferedReader(fr);
        StringBuilder sb = new StringBuilder();
        String sql = "";
        try {
            String line;
            line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            sql = sb.toString();

            br.close();
            fr.close();
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
            System.exit(-1); // プログラム終了
        }

        return sql;
    }
}
