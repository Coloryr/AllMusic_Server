package coloryr.allmusic.hud;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.hud.obj.PosOBJ;
import coloryr.allmusic.hud.obj.SaveOBJ;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataSql {
    private static final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
    private static Thread thread;
    private static boolean isRun;

    private static Connection connection;

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
            "  \"pic_enable\" integer(1)\n" +
            ");";

    public static File sqlFile;

    public static void init() {
        try {
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

    private static void update(String name, SaveOBJ hud) {
        String sql = "";
        try {
            if (connection.isReadOnly() || connection.isClosed()) {
                init();
            }
            Statement stat = connection.createStatement();
            sql = MessageFormat.format("UPDATE allmusic SET info_x={0},info_y={1},info_enable={2},lyric_x={3},lyric_y={4},lyric_enable={5},list_x={6},list_y={7}, list_enable={8},pic_x={9},pic_y={10},pic_enable={11} WHERE name=@name",
                    hud.getInfo().getX(), hud.getInfo().getY(), hud.isEnableInfo() ? 1 : 0,
                    hud.getLyric().getX(), hud.getLyric().getY(), hud.isEnableLyric() ? 1 : 0,
                    hud.getList().getX(), hud.getList().getY(), hud.isEnableList() ? 1 : 0,
                    hud.getPic().getX(), hud.getPic().getY(), hud.isEnablePic() ? 1 : 0);
            sql = sql.replace("@name", "'" + name + "'");
            stat.execute(sql);
            stat.close();
        } catch (Exception e) {
            AllMusic.log.info(sql);
            e.printStackTrace();
        }
    }

    public static void addUser(String name, SaveOBJ hud) {
        String sql = "";
        try {
            if (connection.isReadOnly() || connection.isClosed()) {
                init();
            }
            if (check(name)) {
                update(name, hud);
            } else {
                Statement stat = connection.createStatement();
                sql = MessageFormat.format("INSERT INTO allmusic (name,info_x,info_y,info_enable,lyric_x,lyric_y,lyric_enable,list_x,list_y,list_enable,pic_x,pic_y,pic_enable)VALUES (@name,{0},{1},{2},{3},{4},{5},{6},{7},{8},{9},{10},{11})",
                        hud.getInfo().getX(), hud.getInfo().getY(), hud.isEnableInfo() ? 1 : 0,
                        hud.getLyric().getX(), hud.getLyric().getY(), hud.isEnableLyric() ? 1 : 0,
                        hud.getList().getX(), hud.getList().getY(), hud.isEnableList() ? 1 : 0,
                        hud.getPic().getX(), hud.getPic().getY(), hud.isEnablePic() ? 1 : 0);
                sql = sql.replace("@name", "'" + name + "'");
                stat.execute(sql);
                stat.close();
            }
        } catch (Exception e) {
            AllMusic.log.info(sql);
            e.printStackTrace();
        }
    }

    private static void readAll() {
        try {
            if (connection.isReadOnly() || connection.isClosed()) {
                init();
            }
            Statement stat = connection.createStatement();
            ResultSet set = stat.executeQuery("SELECT name,info_x,info_y,info_enable,lyric_x,lyric_y,lyric_enable,list_x,list_y,list_enable,pic_x,pic_y,pic_enable FROM allmusic");
            while (set.next()) {
                String name = set.getString(1);
                SaveOBJ obj = new SaveOBJ();
                PosOBJ pos1 = new PosOBJ();
                pos1.setX(set.getInt(2));
                pos1.setY(set.getInt(3));
                obj.setInfo(pos1);
                obj.setEnableInfo(set.getInt(4) == 1);
                PosOBJ pos2 = new PosOBJ();
                pos2.setX(set.getInt(5));
                pos2.setY(set.getInt(6));
                obj.setLyric(pos2);
                obj.setEnableLyric(set.getInt(7) == 1);
                PosOBJ pos3 = new PosOBJ();
                pos3.setX(set.getInt(8));
                pos3.setY(set.getInt(9));
                obj.setList(pos3);
                obj.setEnableList(set.getInt(10) == 1);
                PosOBJ pos4 = new PosOBJ();
                pos4.setX(set.getInt(11));
                pos4.setY(set.getInt(12));
                obj.setPic(pos4);
                obj.setEnablePic(set.getInt(10) == 1);
                HudSave.add1(name, obj);
            }
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void task(Runnable runnable) {
        tasks.add(runnable);
    }

    public static void start() {
        init();
        readAll();
        thread = new Thread(DataSql::run);
        isRun = true;
        thread.start();
    }

    public static void stop() {
        isRun = false;
        HudSave.save();
    }

    private static void run() {
        Runnable runnable;
        while (isRun) {
            try {
                runnable = tasks.poll();
                if (runnable != null) {
                    runnable.run();
                }
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
