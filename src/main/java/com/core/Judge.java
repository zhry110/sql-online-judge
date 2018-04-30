package com.core;

import com.common.Const;
import com.common.ServerResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;

public class Judge {
    protected String user, passwd = "";
    protected Connection connection;

    public Judge(Integer userId) throws SQLException {
        if (userId == null)
            throw new SQLException("userID is null");
    }

    public ServerResponse doJudge(String sql, String answer, String userDatabase, String systemDatabase, String[] compareTables) {
        return ServerResponse.createBySuccess();
    }

    protected void closeResource(Statement statement1, Statement statement2, ResultSet resultSet1, ResultSet resultSet2) {
        try {
            if (statement1 != null)
                statement1.close();
            if (statement2 != null)
                statement2.close();
            if (resultSet1 != null)
                resultSet1.close();
            if (resultSet2 != null)
                resultSet2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static ServerResponse createDatabase(int id) {
        return createDatabase("user" + id);
    }

    public static ServerResponse createDatabase(String name) {
        Connection connection = null;
        try {
            Class.forName(Const.JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
        try {
            connection = DriverManager.getConnection(Const.DB_URL, Const.USER, Const.PASS);
            Statement statement = connection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS " + name + " DEFAULT CHARSET utf8");
            statement.close();
            connection.close();
            return ServerResponse.createBySuccess();
        } catch (SQLException e) {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    public static ServerResponse createTableWithSql(String database,String sql) {
        String[] sqls = {"use " + database, sql};
        return execSql(sqls);
    }
    public static ServerResponse dropDatabase(String database) {
        String[] sqls = {"DROP DATABASE IF EXISTS "+database};
        return execSql(sqls);
    }
    public static ServerResponse dropTable(String database,String tableName) {
        String[] sqls = {"use "+database,"DROP TABLE IF EXISTS "+tableName};
        return execSql(sqls);
    }

    public static boolean tableExist(String database, String tableName) {
        String[] sqls = {"use " + database, "SELECT * FROM " + tableName};
        return execSql(sqls).isSuccess();
    }

    public static ServerResponse copyTable(String srcDatabase, String srcTableName, String destDatabase, String destTableName) {
        String[] sqls = {"DROP TABLE IF EXISTS " + destDatabase + "." + destTableName, "CREATE TABLE IF NOT EXISTS " + destDatabase + "." + destTableName + " LIKE " + srcDatabase + "." + srcTableName,
                "INSERT INTO " + destDatabase + "." + destTableName + " SELECT * FROM " + srcDatabase + "." + srcTableName};
        return execSql(sqls);
    }


    public static ServerResponse execSql(String[] sql) {
        Connection connection = null;
        try {
            Class.forName(Const.JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
        try {
            connection = DriverManager.getConnection(Const.DB_URL, Const.USER, Const.PASS);
            Statement statement = connection.createStatement();
            for (int i = 0; i < sql.length; i++)
                statement.execute(sql[i]);
            statement.close();
            connection.close();
            return ServerResponse.createBySuccess();
        } catch (SQLException e) {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    return ServerResponse.createByErrorMessage(e.getMessage());
                }
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }
    public static ServerResponse execSql(List<String> sqls) {
        String[] newSqls = new String[sqls.size()];
        for (int i = 0; i < newSqls.length;i++) {
            newSqls[i] = sqls.get(i);
        }
        return execSql(newSqls);
    }

    public static ServerResponse copyTestCase(String[] tableNames, int caseNum, String srcDatabase, String destDatabase) {
        if (tableNames == null)
            return ServerResponse.createByErrorMessage("null tableNames");
        for (int i = 0; i < tableNames.length; i++) {
            ServerResponse response = Judge.copyTable(srcDatabase, tableNames[i] + caseNum, destDatabase, tableNames[i]);
            if (!response.isSuccess()) {
                return response;
            }
        }
        return ServerResponse.createBySuccess();
    }

    public static ServerResponse copyTestCaseAndJudge(String[] tables, Long proId, boolean access, String sql, String answer, Integer userId) {
        ServerResponse response;
        Judge judge;
        try {
            if (access) {
                judge = new AccessJudge(userId);
            } else {
                judge = new ReadOnlyJudge(userId);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
        for (int test = 0; test < 10; test++) // 测试用例默认不超过10个
        {
            if (Judge.tableExist(getProblemDatabaseName(proId), tables[0] + test)) {
                ServerResponse resCopy = Judge.copyTestCase(tables, test, getProblemDatabaseName(proId), getUserDatabaseName(userId));
                if (!resCopy.isSuccess()) {
                    judge.close();
                    return ServerResponse.createByErrorMessage("系统发生故障 无法拷贝表"+resCopy.getMsg());
                }
                if (access) {
                    resCopy = Judge.copyTestCase(tables, test, getProblemDatabaseName(proId), getUserSystemDatabaseName(userId));
                    if (!resCopy.isSuccess()) {
                        judge.close();
                        return ServerResponse.createByErrorMessage("系统发生故障 无法拷贝表"+resCopy.getMsg());
                    }
                }
                String answerDatabaseName = getUserDatabaseName(userId);
                if (access) {
                    answerDatabaseName = getUserSystemDatabaseName(userId);
                }
                if (!(response = judge.doJudge(sql, answer, getUserDatabaseName(userId), answerDatabaseName, tables)).isSuccess()) {
                    judge.close();
                    return response;
                }
            } else {
                System.out.println("表不存在");
                if (test == 0) {
                    judge.close();
                    return ServerResponse.createByErrorMessage("该题目的测试用例为空 无法判定");
                }
                break;
            }
        }
        return ServerResponse.createBySuccessMessage("Accept");
    }


    protected ServerResponse compareResultSet(ResultSet userResult, ResultSet answerResult) {
        try {
            ResultSetMetaData userMetaData = userResult.getMetaData();
            ResultSetMetaData answerMetaData = answerResult.getMetaData();
            int userColumnCount = userMetaData.getColumnCount();
            int answerColumnCount = answerMetaData.getColumnCount();
            if (userColumnCount != answerColumnCount) {
                return ServerResponse.createByErrorMessage("结果集列数不正确");
            }
            InputStream userInputStream, answerInputStream;
            int charTemp;
            for (int i = 0; i < userColumnCount; i++) {
                if (userMetaData.getColumnType(i + 1) != answerMetaData.getColumnType(i + 1)) {
                    return ServerResponse.createByErrorMessage("结果集列类型不正确");
                }
            }
            while (userResult.next() && answerResult.next()) {
                for (int i = 0; i < userColumnCount; i++) {
                    userInputStream = userResult.getBinaryStream(i + 1);
                    answerInputStream = answerResult.getBinaryStream(i + 1);
                    try {
                        while (true) {
                            if ((charTemp = userInputStream.read()) == answerInputStream.read()) {
                                if (charTemp == -1)
                                    break;
                                continue;
                            } else {
                                return ServerResponse.createByErrorMessage("结果集内容和答案不相等");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return ServerResponse.createByErrorMessage(e.getMessage());
                    }
                }
            }
            userResult.last();
            answerResult.last();
            if (userResult.getRow() != answerResult.getRow()) {
                return ServerResponse.createByErrorMessage("结果集行数过多或者过少");
            }
            return ServerResponse.createBySuccessMessage("Accept");
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    protected ServerResponse compareResultSet(String sql, String answer, String database) {
        Statement userStmt = null, answerStmt = null;
        if (connection == null)
            return ServerResponse.createByErrorMessage("null connection");
        try {
            userStmt = connection.createStatement();
            answerStmt = connection.createStatement();
            answerStmt.execute("use " + database);//use database
            ResultSet userResult = null, answerResult = null;
            if (!userStmt.execute(sql)) {
                closeResource(userStmt, answerStmt, userResult, answerResult);
                return ServerResponse.createByErrorMessage("结果集为NULL");
            }
            userResult = userStmt.executeQuery(sql);
            answerResult = answerStmt.executeQuery(answer);
            ResultSetMetaData userMetaData = userResult.getMetaData();
            ResultSetMetaData answerMetaData = answerResult.getMetaData();
            int userColumnCount = userMetaData.getColumnCount();
            int answerColumnCount = answerMetaData.getColumnCount();
            if (userColumnCount != answerColumnCount) {
                closeResource(userStmt, answerStmt, userResult, answerResult);
                return ServerResponse.createByErrorMessage("结果集列数不正确");
            }
            InputStream userInputStream, answerInputStream;
            int charTemp;
            for (int i = 0; i < userColumnCount; i++) {
                if (userMetaData.getColumnType(i + 1) != answerMetaData.getColumnType(i + 1)) {
                    closeResource(userStmt, answerStmt, userResult, answerResult);
                    return ServerResponse.createByErrorMessage("结果集列类型不正确");
                }
            }
            while (userResult.next() && answerResult.next()) {
                for (int i = 0; i < userColumnCount; i++) {
                    userInputStream = userResult.getBinaryStream(i + 1);
                    answerInputStream = answerResult.getBinaryStream(i + 1);
                    try {
                        while (true) {
                            if ((charTemp = userInputStream.read()) == answerInputStream.read()) {
                                if (charTemp == -1)
                                    break;
                                continue;
                            } else {
                                closeResource(userStmt, answerStmt, userResult, answerResult);
                                return ServerResponse.createByErrorMessage("结果集内容和答案不相等");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        closeResource(userStmt, answerStmt, userResult, answerResult);
                        return ServerResponse.createByErrorMessage(e.getMessage());
                    }
                }
            }
            userResult.last();
            answerResult.last();
            if (userResult.getRow() != answerResult.getRow()) {
                closeResource(userStmt, answerStmt, userResult, answerResult);
                return ServerResponse.createByErrorMessage("结果集行数过多或者过少");
            }
            closeResource(userStmt, answerStmt, userResult, answerResult);
            return ServerResponse.createBySuccessMessage("Accept");
        } catch (SQLException e) {
            e.printStackTrace();
            closeResource(userStmt, answerStmt, null, null);
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }
    public static String getUserDatabaseName(Integer userId) {
        return "user" + userId;
    }
    public static String getUserSystemDatabaseName(Integer userId) {
        return getUserDatabaseName(userId) + "_system";
    }
    public static String getProblemDatabaseName(Long proId) {
        return "problem" + proId;
    }
}


