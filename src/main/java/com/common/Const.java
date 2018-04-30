package com.common;

public class Const {
    public static String CUR_USER = "CURRENT USER";
    public interface ROLE
    {
        int USER = 0;
        int PASS = 1;
        int ADMIN = 2;
    }
    public static String host = "http://localhost:80/";

    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost:3306?characterEncoding=utf-8";

    // 数据库的用户名与密码，需要根据自己的设置
    public static final String USER = "root";
    public static final String PASS = "oshDdjixL0/w";
}
