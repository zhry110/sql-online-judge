package com.core;



import com.common.Const;
import com.common.ServerResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class ReadOnlyJudge extends Judge{
    public ReadOnlyJudge(Integer userId) throws ClassNotFoundException, SQLException {
        super(userId);
        user = "readonly" + userId;
        String[] sqls = new String[]{"CREATE USER IF NOT EXISTS "+user+"@'localhost' IDENTIFIED BY '';",
                "GRANT SELECT ON "+getUserDatabaseName(userId)+".* TO "+user+"@'localhost';",
                "GRANT SELECT ON "+getUserSystemDatabaseName(userId)+".* TO "+user+"@'localhost';"};
        Judge.execSql(sqls);
        Class.forName(Const.JDBC_DRIVER);
        connection = DriverManager.getConnection(Const.DB_URL, user, "");
    }
    @Override
    public ServerResponse doJudge(String sql, String answer, String userDatabase,String systemDatabase,String[] compareTables)
    {
        Statement userStmt = null, answerStmt = null;
        try {
            userStmt = connection.createStatement();
            answerStmt = connection.createStatement();

            userStmt.execute("use " + userDatabase);//use user database
            ResultSet userResult = null, answerResult = null;
            if (!userStmt.execute(sql)) {
                closeResource(userStmt, answerStmt, userResult, answerResult);
                return ServerResponse.createByErrorMessage("结果集为NULL");
            }
            userResult = userStmt.executeQuery(sql);

            answerStmt.execute("use " + systemDatabase);
            answerResult = answerStmt.executeQuery(answer);
            ServerResponse response = compareResultSet(userResult,answerResult);
            closeResource(userStmt,answerStmt,userResult,answerResult);
            return response;
        } catch (SQLException e) {
            e.printStackTrace();
            closeResource(userStmt, answerStmt, null, null);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

}
