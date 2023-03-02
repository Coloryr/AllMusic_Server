//package coloryr.allmusic.sql;
//
//import coloryr.allmusic.AllMusic;
//import org.apache.commons.dbcp2.BasicDataSource;
//
//import javax.sql.DataSource;
//import java.sql.*;
//
//public abstract class MysqlConn implements IEconomy{
//    //建立连接的驱动驱动名称
//    public  final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
//    //最大空闲链接
//    private final int MAX_IDLE = 20;
//    //最大空闲连接
//    private final int MinIdle = 8;
//    //最大等待时间
//    private final long MAX_WAIT_Millis = 5000;
//    //最大活动链接
//    private final int MAX_TOTAl = 5;
//    //初始化时链接池的数量
//    private final int INITIAL_SIZE = 10;
//    //一个被抛弃连接可以被移除的超时时间
//    private final int RemoveAbandonedTimeout = 180;
//    //只会发现当前连接失效，再创建一个连接供当前查询使用
//    private final boolean TOBESTNORROW = true;
//    private BasicDataSource DATA_SOURCE;
//
//    public boolean start() {
//        try {
//            DATA_SOURCE = new BasicDataSource();
//            DATA_SOURCE.setDriverClassName(DRIVER_CLASS_NAME);
//            DATA_SOURCE.setUrl(AllMusic.getConfig().getEconomys().getMysqlUrl());
//            DATA_SOURCE.setMaxTotal(MAX_TOTAl);
//            DATA_SOURCE.setInitialSize(INITIAL_SIZE);
//            DATA_SOURCE.setMinIdle(MinIdle);
//            DATA_SOURCE.setMaxIdle(MAX_IDLE);
//            DATA_SOURCE.setMaxWaitMillis(MAX_WAIT_Millis);
//            DATA_SOURCE.setTestOnBorrow(TOBESTNORROW);
//            DATA_SOURCE.setRemoveAbandonedTimeout(RemoveAbandonedTimeout);
//
//            getConnection().createStatement().close();
//
//            return test();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public abstract boolean test();
//
//    public DataSource getDateSource() {
//        return DATA_SOURCE;
//    }
//
//    //提供获得链接
//    public Connection getConnection() throws SQLException {
//        return DATA_SOURCE.getConnection();
//    }
//
//    public void stop() {
//        try {
//            DATA_SOURCE.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
