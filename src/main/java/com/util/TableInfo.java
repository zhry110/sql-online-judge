package com.util;

import com.common.Const;

import java.sql.*;

public class TableInfo {

    public static String getTableInfo(String tableName,Long proId) {
        if (tableName == null)
            return "";
        String ret = "<b>"+tableName + "</b><br>";
        try {
            Class.forName(Const.JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(Const.DB_URL, Const.USER, Const.PASS);
            String sql = "use problem"+proId;
            stmt = connection.createStatement();
            stmt.execute(sql);
            sql =  "select * from "+tableName;
            rs = stmt.executeQuery(sql);

            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            //System.out.println(resultSetMetaData.getColumnName(1));
            //System.out.println(resultSetMetaData.getColumnTypeName(1));
            int columns = resultSetMetaData.getColumnCount();
            String[][] temp = new String[5 + 1][columns];
            for (int i = 1; i <= columns; i++) {
                temp[0][i - 1] = resultSetMetaData.getColumnName(i) + "(" + resultSetMetaData.getColumnTypeName(i) + ")";
            }
            int index = 1;
            while (rs.next() && index < 6) {
                for (int i = 0; i < columns;i++) {
                    //temp[index] = new String[columns];
                    temp[index][i] = rs.getString(i + 1);
                }index ++;
            }
            // 展开结果集数据库
            ret += NewBorder.addBorder(temp);

            // 完成后关闭

        } catch (SQLException e) {
            e.printStackTrace();
            ret += e.getMessage();
        }
        finally {
            try {
                if (connection != null)
                    connection.close();
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
