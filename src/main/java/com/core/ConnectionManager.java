package com.core;

import com.common.Const;
import com.common.ServerResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.*;

public class ConnectionManager {
    private static  ApplicationContext ac = null;
    private static DataSource dataSource = null;
    public static Connection getInstance() throws SQLException {
        if (ac == null)
            ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        if (dataSource == null)
            dataSource = (DataSource)ac.getBean("dataSource");
        return dataSource.getConnection();
    }
    public static Connection getInstance(String username,String passwd) throws SQLException {
        if (ac == null)
            ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        if (dataSource == null)
            dataSource = (DataSource)ac.getBean("dataSource");
        return dataSource.getConnection(username,passwd);
    }
}
