package com.coloryr.allmusic.server.core.sql;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;
import com.coloryr.allmusic.server.core.objs.config.SaveObj;
import com.coloryr.allmusic.server.core.objs.enums.HudDirType;
import com.coloryr.allmusic.server.core.objs.hud.PosObj;
import com.coloryr.allmusic.server.core.utils.HudUtils;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class DataSql {
    private static final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
    private static final Semaphore semaphore = new Semaphore(0);
    /**
     * 创建表用
     */
    private static final String table = "CREATE TABLE IF NOT EXISTS \"allmusic\" (\n" +
            "  \"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "  \"name\" TEXT(20),\n" +
            "  \"info_x\" integer(6),\n" +
            "  \"info_y\" integer(6),\n" +
            "  \"info_color\" integer(20),\n" +
            "  \"info_dir\" integer(6),\n" +
            "  \"info_shadow\" integer(1),\n" +
            "  \"info_enable\" integer(1),\n" +
            "  \"lyric_x\" integer(6),\n" +
            "  \"lyric_y\" integer(6),\n" +
            "  \"lyric_color\" integer(20),\n" +
            "  \"lyric_dir\" integer(6),\n" +
            "  \"lyric_shadow\" integer(1),\n" +
            "  \"lyric_enable\" integer(1),\n" +
            "  \"list_x\" integer(6),\n" +
            "  \"list_y\" integer(6),\n" +
            "  \"list_color\" integer(20),\n" +
            "  \"list_dir\" integer(6),\n" +
            "  \"list_shadow\" integer(1),\n" +
            "  \"list_enable\" integer(1),\n" +
            "  \"pic_x\" integer(6),\n" +
            "  \"pic_y\" integer(6),\n" +
            "  \"pic_dir\" integer(6),\n" +
            "  \"pic_size\" integer(6),\n" +
            "  \"pic_enable\" integer(1),\n" +
            "  \"pic_rotate\" integer(1),\n" +
            "  \"pic_rotate_speed\" integer(6)\n" +
            ");";

    private static final String table1 = "CREATE TABLE IF NOT EXISTS \"allmusic_list\" (\n" +
            "  \"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "  \"sid\" TEXT(40)\n" +
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
            stat.execute(table);
            stat.execute(table1);
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
            name = name.toLowerCase(Locale.ROOT);
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
        name = name.toLowerCase(Locale.ROOT);
        String sql = "";
        try {
            if (connection.isReadOnly() || connection.isClosed()) {
                init();
            }
            Statement stat = connection.createStatement();
            sql = "UPDATE allmusic SET " +
                    "info_x=" + hud.info.x + "," +
                    "info_y=" + hud.info.y + "," +
                    "info_color=" + hud.info.color + "," +
                    "info_dir=" + hud.info.dir.ordinal() + "," +
                    "info_shadow=" + (hud.info.shadow ? 1 : 0) + "," +
                    "info_enable=" + (hud.info.enable ? 1 : 0) + "," +
                    "lyric_x=" + hud.lyric.x + "," +
                    "lyric_y=" + hud.lyric.y + "," +
                    "lyric_color=" + hud.lyric.color + "," +
                    "lyric_dir=" + hud.lyric.dir.ordinal() + "," +
                    "lyric_shadow=" + (hud.lyric.shadow ? 1 : 0) + "," +
                    "lyric_enable=" + (hud.lyric.enable ? 1 : 0) + "," +
                    "list_x=" + hud.list.x + "," +
                    "list_y=" + hud.list.y + "," +
                    "list_color=" + hud.list.color + "," +
                    "list_dir=" + hud.list.dir.ordinal() + "," +
                    "list_shadow=" + (hud.list.shadow ? 1 : 0) + "," +
                    "list_enable=" + (hud.list.enable ? 1 : 0) + "," +
                    "pic_x=" + hud.pic.x + "," +
                    "pic_y=" + hud.pic.y + "," +
                    "pic_dir=" + hud.pic.dir.ordinal() + "," +
                    "pic_enable=" + (hud.pic.enable ? 1 : 0) + "," +
                    "pic_size=" + hud.pic.color + "," +
                    "pic_rotate=" + (hud.pic.shadow ? 1 : 0) + "," +
                    "pic_rotate_speed=" + hud.picRotateSpeed + " WHERE name='" + name + "'";
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
        name = name.toLowerCase(Locale.ROOT);
        String sql = "";
        try {
            if (connection.isReadOnly() || connection.isClosed()) {
                init();
            }
            if (check(name)) {
                update(name, hud);
            } else {
                Statement stat = connection.createStatement();
                sql = "INSERT INTO allmusic (name,info_x,info_y," +
                        "info_enable,lyric_x,lyric_y,lyric_enable,list_x,list_y,list_enable," +
                        "pic_x,pic_y,pic_enable,pic_size,pic_rotate,pic_rotate_speed," +
                        "info_color,info_dir,info_shadow,lyric_color,lyric_dir," +
                        "lyric_shadow,list_color,list_dir,list_shadow,pic_dir)" +
                        "VALUES ('" + name + "'," +
                        hud.info.x + "," +
                        hud.info.y + "," +
                        (hud.info.enable ? 1 : 0) + "," +
                        hud.lyric.x + "," +
                        hud.lyric.y + "," +
                        (hud.lyric.enable ? 1 : 0) + "," +
                        hud.list.x + "," +
                        hud.list.y + "," +
                        (hud.list.enable ? 1 : 0) + "," +
                        hud.pic.x + "," +
                        hud.pic.y + "," +
                        (hud.pic.enable ? 1 : 0) + "," +
                        hud.pic.color + "," +
                        (hud.pic.shadow ? 1 : 0) + "," +
                        hud.picRotateSpeed + "," +
                        hud.info.color + "," +
                        hud.info.dir.ordinal() + "," +
                        (hud.info.shadow ? 1 : 0) + "," +
                        hud.lyric.color + "," +
                        hud.lyric.dir.ordinal() + "," +
                        (hud.lyric.shadow ? 1 : 0) + "," +
                        hud.list.color + "," +
                        hud.list.dir.ordinal() + "," +
                        (hud.list.shadow ? 1 : 0) + "," +
                        hud.pic.dir.ordinal() +
                        ")";
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
            AllMusic.log.info("正在读取玩家数据");
            if (connection.isReadOnly() || connection.isClosed()) {
                init();
            }
            Statement stat = connection.createStatement();
            ResultSet set = stat.executeQuery("SELECT name,info_x,info_y,info_enable,lyric_x," +
                    "lyric_y,lyric_enable,list_x,list_y,list_enable,pic_x,pic_y,pic_enable,pic_size," +
                    "pic_rotate,pic_rotate_speed,info_color,info_dir,info_shadow,lyric_color," +
                    "lyric_dir,lyric_shadow,list_color,list_dir,list_shadow,pic_dir FROM allmusic");
            HudDirType[] vas = HudDirType.values();
            while (set.next()) {
                String name = set.getString(1);
                SaveObj obj = new SaveObj();
                PosObj pos1 = new PosObj();
                pos1.x = set.getInt(2);
                pos1.y = set.getInt(3);
                obj.info = pos1;
                obj.info.enable = set.getInt(4) == 1;
                PosObj pos2 = new PosObj();
                pos2.x = set.getInt(5);
                pos2.y = set.getInt(6);
                obj.lyric = pos2;
                obj.lyric.enable = set.getInt(7) == 1;
                PosObj pos3 = new PosObj();
                pos3.x = set.getInt(8);
                pos3.y = set.getInt(9);
                obj.list = pos3;
                obj.list.enable = set.getInt(10) == 1;
                PosObj pos4 = new PosObj();
                pos4.x = set.getInt(11);
                pos4.y = set.getInt(12);
                obj.pic = pos4;
                obj.pic.enable = set.getInt(13) == 1;
                obj.pic.color = set.getInt(14);
                obj.pic.shadow = set.getInt(15) == 1;
                obj.picRotateSpeed = set.getInt(16);
                obj.info.color = set.getInt(17);
                obj.info.dir = vas[set.getInt(18)];
                obj.info.shadow = set.getInt(19) == 1;
                obj.lyric.color = set.getInt(20);
                obj.lyric.dir = vas[set.getInt(21)];
                obj.lyric.shadow = set.getInt(22) == 1;
                obj.list.color = set.getInt(23);
                obj.list.dir = vas[set.getInt(24)];
                obj.list.shadow = set.getInt(25) == 1;
                obj.pic.dir = vas[set.getInt(26)];
                HudUtils.add(name, obj);
            }
            stat.close();
        } catch (Exception e) {
            AllMusic.log.warning("数据库读取错误，请删除关闭服务器删除数据库，在启动服务器");
            e.printStackTrace();
        }
    }

    /**
     * 读取空闲歌单列表
     */
    private static void readAllList() {
        try {
            AllMusic.log.info("正在读取空闲歌单");
            if (connection.isReadOnly() || connection.isClosed()) {
                init();
            }
            Statement stat = connection.createStatement();
            ResultSet set = stat.executeQuery("SELECT sid FROM allmusic_list");
            List<String> list = new ArrayList<>();
            while (set.next()) {
                String name = set.getString(1);
                list.add(name);
            }
            stat.close();

            PlayMusic.addIdleList(list);
        } catch (Exception e) {
            AllMusic.log.warning("数据库读取错误，请删除关闭服务器删除数据库，在启动服务器");
            e.printStackTrace();
        }
    }

    public static void addIdleList(List<String> list) {
        task(() -> {
            try {
                if (connection.isReadOnly() || connection.isClosed()) {
                    init();
                }
                PreparedStatement pstmt = connection.prepareStatement("INSERT INTO allmusic_list (sid) VALUES (?)");
                // 遍历 List<String> 并插入每个字符串
                for (String str : list) {
                    pstmt.setString(1, str); // 设置参数
                    pstmt.addBatch(); // 添加到批处理
                }
                pstmt.executeBatch();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void clearIdleList() {
        task(() -> {
            try {
                if (connection.isReadOnly() || connection.isClosed()) {
                    init();
                }
                Statement stat = connection.createStatement();
                stat.execute("DELETE FROM allmusic_list");
                stat.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
        readAllList();
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
