package com.core;

import com.common.Const;
import com.common.ServerResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccessJudge extends Judge{
    private Integer userId;
    public AccessJudge(Integer userId) throws SQLException, ClassNotFoundException {
        super(userId);
        user = "access" + userId;
        String[] sqls = new String[]{"CREATE USER IF NOT EXISTS "+user+"@'localhost' IDENTIFIED BY '';",
                "GRANT ALL ON "+getUserDatabaseName(userId)+".* TO "+user+"@'localhost';","GRANT ALL ON "+getUserSystemDatabaseName(userId)+".* TO "+user+"@'localhost';"};
        Judge.execSql(sqls);
        Class.forName(Const.JDBC_DRIVER);
        connection = DriverManager.getConnection(Const.DB_URL, user, "");
        this.userId = userId;
    }


    /*
    for access judge sql and answer is mutil line;
     */
@Override
    public ServerResponse doJudge(String sql, String answer, String userDatabase,String systemDatabase,String[] compareTables) {
        List<String> sqls = getLines(sql);
        sqls.add(0,"use " + userDatabase);
        List<String> answers = getLines(answer);
        answers.add(0,"use " + systemDatabase);

        ServerResponse response = execSqlsWithUser(sqls,answers);
        if (!response.isSuccess())
            return response;

        //开始对比结果

        if (compareTables == null) {
            return ServerResponse.createByErrorMessage("空对比表");
        }
        ReadOnlyJudge readOnlyJudge = null;
        for (String table : compareTables) {
            if (table != null) {
                String compareSql = "select * from " + table;
                try {
                    readOnlyJudge = new ReadOnlyJudge(userId);
                    ServerResponse judgeResponse =
                            readOnlyJudge.doJudge(compareSql,compareSql,userDatabase,systemDatabase,compareTables);
                    if (!judgeResponse.isSuccess()) {
                        readOnlyJudge.close();
                        return judgeResponse;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return ServerResponse.createByErrorMessage(e.getMessage());
                } catch (SQLException e) {
                    e.printStackTrace();
                    readOnlyJudge.close();
                    return ServerResponse.createByErrorMessage(e.getMessage());
                }

            }
        }
        readOnlyJudge.close();
        return ServerResponse.createBySuccessMessage("Accept");

    }
    public static List<String> getLines(String sql) {
        List<String> sqls = new ArrayList<>();
        int last = 0;
        for (int i = 0;i < sql.length();i++) {
            if (sql.charAt(i) == '\n') {
                if (i != last) {
                    sqls.add(sql.substring(last,i));
                }
                last = i + 1;
            }
        }
        if (last < sql.length()) {
            sqls.add(sql.substring(last));
        }
        return sqls;
    }

    private ServerResponse execSqlsWithUser(List<String> sqls,List<String> answers) {
        Statement userStmt, answerStmt;
        try {
            userStmt = connection.createStatement();
            answerStmt = connection.createStatement();
            for (String sql : sqls) {
                userStmt.execute(sql);
            }

            for (String answer : answers) {
                answerStmt.execute(answer);
            }

            userStmt.close();
            answerStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }

        return ServerResponse.createBySuccess();
    }

}
