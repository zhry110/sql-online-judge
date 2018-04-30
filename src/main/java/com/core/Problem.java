package com.core;

import com.common.Const;
import com.util.NewBorder;

import java.sql.*;

public class Problem {
    private Long proId;
    public Problem(Long proId) {
        this.proId = proId;
    }
    public String getAnswer(String answer) {
        String ret = new String();
        try {
            Class.forName(Const.JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection(Const.DB_URL, Const.USER, Const.PASS);
            String sql = "use " + Judge.getProblemDatabaseName(proId);
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            sql = answer;
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            //System.out.println(resultSetMetaData.getColumnName(1));
            //System.out.println(resultSetMetaData.getColumnTypeName(1));
            int columns = resultSetMetaData.getColumnCount();
            rs.last();
            int rows = rs.getRow();
            rs.beforeFirst();
            String[][] temp = new String[rows + 1][columns];
            for (int i = 1; i <= columns; i++) {
                temp[0][i - 1] = resultSetMetaData.getColumnName(i) + "(" + resultSetMetaData.getColumnTypeName(i) + ")";
            }
            int index = 1;
            while (rs.next()) {
                for (int i = 0; i < columns;i++) {
                    //temp[index] = new String[columns];
                    temp[index][i] = rs.getString(i + 1);
                }index ++;
            }
            // 展开结果集数据库
            ret += NewBorder.addBorder(temp);
            // 完成后关闭
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR :" + e.getMessage();
        }
        return ret;
    }
}
