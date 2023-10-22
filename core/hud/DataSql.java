package coloryr.allmusic.core.hud;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.objs.config.SaveObj;
import coloryr.allmusic.core.objs.hud.PosObj;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class DataSql {
    private static final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
    private static final Semaphore semaphore = new Semaphore(0);
    /**
     * 创建表用
     */
    private static final String table = "CREATE TABLE \"allmusic\" (\n" +
            "  \"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "  \"name\" TEXT(20),\n" +
            "  \"info_x\" integer(6),\n" +
            "  \"info_y\" integer(6),\n" +
            "  \"info_enable\" integer(1),\n" +
            "  \"lyric_x\" integer(6),\n" +
            "  \"lyric_y\" integer(6),\n" +
            "  \"lyric_enable\" integer(1),\n" +
            "  \"list_x\" integer(6),\n" +
            "  \"list_y\" integer(6),\n" +
            "  \"list_enable\" integer(1),\n" +
            "  \"pic_x\" integer(6),\n" +
            "  \"pic_y\" integer(6),\n" +
            "  \"pic_size\" integer(6),\n" +
            "  \"pic_enable\" integer(1),\n" +
            "  \"pic_rotate\" integer(1),\n" +
            "  \"pic_rotate_speed\" integer(6)\n" +
            ");";
    /**
     * 数据库文件
     */
    public static File sqlFile;
    private static boolean isRun;
    /**
     * 数据库链接对象
     */
    private static Connection connection;

    /**
     * 初始化数据库
     */
    public static void init() {
        try {
            AllMusic.log.info("正在初始化数据库");
            if (connection != null)
                connection.close();
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + sqlFile.getPath());
            Statement stat = connection.createStatement();
            ResultSet set = stat.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='allmusic';");
            if (!set.next()) {
                set.close();
                stat.execute(table);
            }
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查玩家是否在数据库
     *
     * @param name 用户名
     * @return 结果
     */
    public static boolean check(String name) {
        try {
            boolean have = false;
            if (connection.isReadOnly() || connection.isClosed()) {
                init();
            }
            Statement stat = connection.createStatement();
            ResultSet set = stat.executeQuery("SELECT allmusic.id FROM allmusic WHERE allmusic.name ='" + name + "'");
            if (set.next()) {
                have = true;
            }
            set.close();
            stat.close();
            return have;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新玩家Hud数据
     *
     * @param name 用户名
     * @param hud  Hud数据
     */
    private static void update(String name, SaveObj hud) {
        String sql = "";
        try {
            if (connection.isReadOnly() || connection.isClosed()) {
                init();
            }
            Statement stat = connection.createStatement();
            sql = MessageFormat.format("UPDATE allmusic SET info_x={0},info_y={1},info_enable={2},lyric_x={3},lyric_y={4},lyric_enable={5},list_x={6},list_y={7}, list_enable={8},pic_x={9},pic_y={10},pic_enable={11},pic_size={12},pic_rotate={13},pic_rotate_speed={14} WHERE name=@name",
                    hud.Info.x, hud.Info.y, hud.EnableInfo ? 1 : 0,
                    hud.Lyric.x, hud.Lyric.y, hud.EnableLyric ? 1 : 0,
                    hud.List.x, hud.List.y, hud.EnableList ? 1 : 0,
                    hud.Pic.x, hud.Pic.y, hud.EnablePic ? 1 : 0,
                    hud.PicSize, hud.EnablePicRotate ? 1 : 0,
                    hud.PicRotateSpeed);
            sql = sql.replace("@name", "'" + name + "'");
            stat.execute(sql);
            stat.close();
        } catch (Exception e) {
            AllMusic.log.info(sql);
            e.printStackTrace();
        }
    }

    /**
     * 添加用户Hud数据
     *
     * @param name 用户名
     * @param hud  数据
     */
    public static void addUser(String name, SaveObj hud) {
        String sql = "";
        try {
            if (connection.isReadOnly() || connection.isClosed()) {
                init();
            }
            if (check(name)) {
                update(name, hud);
            } else {
                Statement stat = connection.createStatement();
                sql = MessageFormat.format("INSERT INTO allmusic (name,info_x,info_y," +
                                "info_enable,lyric_x,lyric_y,lyric_enable,list_x,list_y,list_enable," +
                                "pic_x,pic_y,pic_enable,pic_size,pic_rotate,pic_rotate_speed)" +
                                "VALUES (@name,{0},{1},{2},{3},{4},{5},{6},{7},{8},{9},{10},{11},{12},{13},{14})",
                        hud.Info.x, hud.Info.y, hud.EnableInfo ? 1 : 0,
                        hud.Lyric.x, hud.Lyric.y, hud.EnableLyric ? 1 : 0,
                        hud.List.x, hud.List.y, hud.EnableList ? 1 : 0,
                        hud.Pic.x, hud.Pic.y, hud.EnablePic ? 1 : 0,
                        hud.PicSize, hud.EnablePicRotate ? 1 : 0,
                        hud.PicRotateSpeed);
                sql = sql.replace("@name", "'" + name + "'");
                stat.execute(sql);
                stat.close();
            }
        } catch (Exception e) {
            AllMusic.log.info(sql);
            e.printStackTrace();
        }
    }

    /**
     * 读取所有数据
     */
    private static void readAll() {
        try {
            AllMusic.log.info("正在读取数据库所有内容");
            if (connection.isReadOnly() || connection.isClosed()) {
                init();
            }
            Statement stat = connection.createStatement();
            ResultSet set = stat.executeQuery("SELECT name,info_x,info_y,info_enable,lyric_x,lyric_y,lyric_enable,list_x,list_y,list_enable,pic_x,pic_y,pic_enable,pic_size,pic_rotate,pic_rotate_speed FROM allmusic");
            while (set.next()) {
                String name = set.getString(1);
                SaveObj obj = new SaveObj();
                PosObj pos1 = new PosObj();
                pos1.x = set.getInt(2);
                pos1.y = set.getInt(3);
                obj.Info = pos1;
                obj.EnableInfo = set.getInt(4) == 1;
                PosObj pos2 = new PosObj();
                pos2.x = set.getInt(5);
                pos2.y = set.getInt(6);
                obj.Lyric = pos2;
                obj.EnableLyric = set.getInt(7) == 1;
                PosObj pos3 = new PosObj();
                pos3.x = set.getInt(8);
                pos3.y = set.getInt(9);
                obj.List = pos3;
                obj.EnableList = set.getInt(10) == 1;
                PosObj pos4 = new PosObj();
                pos4.x = set.getInt(11);
                pos4.y = set.getInt(12);
                obj.Pic = pos4;
                obj.EnablePic = set.getInt(13) == 1;
                obj.PicSize = set.getInt(14);
                obj.EnablePicRotate = set.getInt(15) == 1;
                obj.PicRotateSpeed = set.getInt(16);
                HudUtils.add(name, obj);
            }
            stat.close();
        } catch (Exception e) {
            AllMusic.log.warning("数据库读取错误，请删除关闭服务器删除数据库，在启动服务器");
            e.printStackTrace();
        }
    }

    public static void task(Runnable runnable) {
        tasks.add(runnable);
        semaphore.release();
    }

    /**
     * 启用Hud数据库
     */
    public static void start() {
        init();
        readAll();
        Thread thread = new Thread(DataSql::run);
        isRun = true;
        thread.start();
    }

    /**
     * 停止数据库
     */
    public static void stop() {
        isRun = false;
        semaphore.release();
        HudUtils.save();
    }

    private static void run() {
        Runnable runnable;
        while (isRun) {
            try {
                semaphore.acquire();
                if (!isRun)
                    return;
                do {
                    runnable = tasks.poll();
                    if (runnable != null) {
                        runnable.run();
                    }
                }
                while (runnable != null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
