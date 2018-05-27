package com.core;

import com.common.Const;
import com.util.NewBorder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        Connection connection = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            connection = ConnectionManager.getInstance();
            String sql = "use " + Judge.getProblemDatabaseName(proId);
            stmt = connection.createStatement();
            stmt.execute(sql);
            sql = answer;
            rs = stmt.executeQuery(sql);
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


        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR :" + e.getMessage();
        }
        finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (Exception e)
            {

            }
        }
        return ret;
    }
}
