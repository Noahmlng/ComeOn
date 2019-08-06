package com.comeon.android.db_accessing;
import com.comeon.android.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库操作基础类
 */
public class BaseDao {

    private static String driver;//MySQL 驱动
    private static String url;//MYSQL数据库连接Url
    private static String username;//用户名
    private static String password;//密码

    PreparedStatement ps=null;

    static{
        //静态代码块中，在类加载时执行
        init();
    }

    /**
     * 初始化连接参数，从配置文件中获取
     */
//    public static void init(){
//        Properties params=new Properties();
//        String configFile="databaseConfig.properties";
//        //加载配置文件到输入流中
//        InputStream is=BaseDao.class.getClassLoader().getResourceAsStream(configFile);
//        try{
//            //从输入流中读取属性列表
//            params.load(is);
//        }catch (IOException e){
//            LogUtil.e("读取配置文件失败",e.getMessage());
//            e.printStackTrace();
//        }
//        //根据指定的获取对应的值
//        driver=params.getProperty("driver");
//        url=params.getProperty("url");
//        username=params.getProperty("username");
//        password=params.getProperty("password");
//    }

    public static void init(){
        driver="com.mysql.jdbc.Driver";
        url="jdbc:mysql://192.168.101.25:3306/comeondb?useUnicode=true&characterEncoding=utf-8";
        username="noah";
        password="Noah1115";
    }

    /**
     * 连接数据库
     * */
    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName(driver);//获取MYSQL驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);//获取连接
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            LogUtil.e("MySQL数据库开启连接失败", e.getMessage());
        }
        return conn;
    }

    /**
     * 关闭数据库
     * @param conn  连接对象
     * @param ps
     */
    public static void closeAll(Connection conn, PreparedStatement ps){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LogUtil.e("MySQL数据库关闭连接失败", e.getMessage());
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LogUtil.e("MySQL数据库关闭statement失败", e.getMessage());
            }
        }

    }


    /**
     * 关闭数据库
     * @param conn
     * @param ps
     * @param rs
     */
    public static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LogUtil.e("MySQL数据库关闭连接失败", e.getMessage());
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LogUtil.e("MySQL数据库关闭statement失败", e.getMessage());
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                LogUtil.e("MySQL数据库关闭结果集失败", e.getMessage());
            }
        }
    }

    /**
     * 增删改的通用方法
     * @param conn
     * 为了完成事务，所以在增删改的Connection对象都作为一个参数传进
     * @param sql
     * SQL预编译语句
     * @param params
     * 传入的参数
     * @return
     * 影响的行数
     * 因为加入事务，所以在通用方法中不关闭连接，在实现类再进行关闭
     */
    protected int executeUpdate(Connection conn, String sql, Object...params){
        try {
            ps=conn.prepareStatement(sql);
            if (params!=null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i+1, params[i]);
                }
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }


    /**
     * 查询的方法
     * @param sql
     * SQL预编译语句
     * @param params
     * 传入的参数
     * @return
     * 影响的行数
     * 因为加入事务，所以在通用方法中不关闭连接，在实现类再进行关闭
     */
    protected ResultSet executeQuery(String sql, Object...params){
        try {
            Connection conn=getConnection();
            ps=conn.prepareStatement(sql);
            if (params!=null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i+1, params[i]);
                }
            }
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
