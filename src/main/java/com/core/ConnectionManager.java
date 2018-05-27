package com.core;

import com.common.Const;
import com.common.ServerResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {
    private static Connection connection = null;

    public static Connection getInstance() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(Const.DB_URL, Const.USER, Const.PASS);
            return connection;
        } else {
            if (connection.isClosed()) {
                connection = null;
                return getInstance();
            }
        }
        return connection;
    }
}
