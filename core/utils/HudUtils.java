package coloryr.allmusic.core.utils;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.objs.enums.HudType;
import coloryr.allmusic.core.music.play.LyricSave;
import coloryr.allmusic.core.music.play.PlayMusic;
import coloryr.allmusic.core.objs.config.SaveObj;
import coloryr.allmusic.core.objs.enums.HudDirType;
import coloryr.allmusic.core.objs.hud.PosObj;
import coloryr.allmusic.core.objs.music.SongInfoObj;
import coloryr.allmusic.core.sql.DataSql;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HudUtils {
    private static final Map<String, SaveObj> HudList = new ConcurrentHashMap<>();

    public static SaveObj get(String name) {
        name = name.toLowerCase(Locale.ROOT);
        if (!HudList.containsKey(name)) {
            SaveObj obj = AllMusic.getConfig().defaultHud.copy();
            HudList.put(name, obj);
            String finalName = name;
            DataSql.task(() -> DataSql.addUser(finalName, obj));
            return obj;
        }
        return HudList.get(name);
    }

    public static void add(String name, SaveObj hud) {
        name = name.toLowerCase(Locale.ROOT);
        HudList.put(name, hud);
    }

    public static void addAndSave(String name, SaveObj hud) {
        name = name.toLowerCase(Locale.ROOT);
        HudList.put(name, hud);
        String finalName = name;
        DataSql.task(() -> DataSql.addUser(finalName, hud));
    }

    public static void save() {
        for (Map.Entry<String, SaveObj> item : HudList.entrySet()) {
            DataSql.addUser(item.getKey(), item.getValue());
        }
    }

    /**
     * 设置玩家Hud位置
     *
     * @param player 用户名
     * @param pos    位置
     * @param x      x
     * @param y      y
     * @return 位置数据
     */
    public static PosObj setHudPos(String player, HudType pos, String x, String y) {
        SaveObj obj = get(player);
        if (obj == null)
            obj = AllMusic.getConfig().defaultHud.copy();
        PosObj posOBJ = new PosObj(0, 0, HudDirType.TOP_LEFT, 0xffffff, false, true);
        if (!Function.isInteger(x) && !Function.isInteger(y))
            return null;
        int x1 = Integer.parseInt(x);
        int y1 = Integer.parseInt(y);

        switch (pos) {
            case LYRIC:
                posOBJ = obj.lyric;
                break;
            case LIST:
                posOBJ = obj.list;
                break;
            case INFO:
                posOBJ = obj.info;
                break;
            case PIC:
                posOBJ = obj.pic;
        }
        posOBJ.x = x1;
        posOBJ.y = y1;

        addAndSave(player, obj);
        HudUtils.sendHudPos(player);
        return posOBJ;
    }

    /**
     * 更新Hud的List数据
     */
    public static void sendHudListData() {
        String info;
        if (PlayMusic.getListSize() == 0) {
            info = AllMusic.getMessage().hud.emptyList;
        } else {
            String now;
            StringBuilder list = new StringBuilder();
            for (SongInfoObj info1 : PlayMusic.getList()) {
                if (info1 == null)
                    continue;
                now = info1.getInfo();
                if (now.length() > AllMusic.getConfig().messageLimitSize)
                    now = now.substring(0, AllMusic.getConfig().messageLimitSize - 1) + "...";
                list.append(now).append("\n");
            }
            info = AllMusic.getMessage().hud.list
                    .replace("%Size%", String.valueOf(PlayMusic.getList().size()))
                    .replace("%List%", list.toString());
        }

        AllMusic.side.sendHudList(info);
    }

    /**
     * 时间转换
     *
     * @param time 时间
     * @return 结果
     */
    private static String tranTime(int time) {
        int m = time / 60;
        int s = time - m * 60;
        return (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s;
    }

    /**
     * 更新Hud的信息数据
     */
    public static void sendHudNowData() {
        String info;
        if (PlayMusic.nowPlayMusic == null) {
            info = AllMusic.getMessage().hud.emptyMusic;
        } else {
            info = AllMusic.getMessage().hud.music
                    .replace("%Name%", PlayMusic.nowPlayMusic.getName())
                    .replace("%AllTime%", tranTime(PlayMusic.musicAllTime))
                    .replace("%NowTime%", tranTime(PlayMusic.musicNowTime / 1000))
                    .replace("%Author%", PlayMusic.nowPlayMusic.getAuthor())
                    .replace("%Alia%", PlayMusic.nowPlayMusic.getAlia())
                    .replace("%Al%", PlayMusic.nowPlayMusic.getAl())
                    .replace("%Player%", PlayMusic.nowPlayMusic.getCall());
        }

        AllMusic.side.sendHudInfo(info);
    }

    /**
     * 更新Hud的歌词数据
     */
    public static void sendHudLyricData() {
        String info;
        LyricSave obj = PlayMusic.lyric;
        if (obj == null) {
            info = AllMusic.getMessage().hud.emptyLyric;
        } else {
            String lyric = obj.getLyric();
            String tLyric = obj.getTlyric();
            String kLyric = obj.getKly();
            if (!AllMusic.getConfig().ktvMode) {
                info = AllMusic.getMessage().hud.lyric
                        .replace("%Lyric%", lyric == null ? "" : lyric)
                        .replace("%Tlyric%", tLyric != null ? tLyric : "");
            } else {
                info = AllMusic.getMessage().hud.ktv
                        .replace("%Lyric%", lyric != null ? lyric : "")
                        .replace("%KLyric%", kLyric != null ? kLyric : "")
                        .replace("%Tlyric%", tLyric != null ? tLyric : "");
            }
        }

        AllMusic.side.sendHudLyric(info);
    }

    /**
     * 更新Hud的开启关闭数据
     *
     * @param player 用户名
     * @param pos    输入数据
     * @return 设置结果
     */
    public static boolean setHudEnable(String player, HudType pos) {
        SaveObj obj = get(player);
        boolean res = false;
        if (obj == null) {
            obj = AllMusic.getConfig().defaultHud.copy();
            res = obj.info.enable && obj.list.enable && obj.lyric.enable;
        } else {
            if (pos == null) {
                if (obj.info.enable && obj.list.enable && obj.lyric.enable) {
                    obj.info.enable = false;
                    obj.list.enable = false;
                    obj.lyric.enable = false;
                    obj.pic.enable = false;
                } else {
                    obj.info.enable = true;
                    obj.list.enable = true;
                    obj.lyric.enable = true;
                    obj.pic.enable = true;
                    res = true;
                }
            } else {
                switch (pos) {
                    case INFO:
                        obj.info.enable = !obj.info.enable;
                        break;
                    case LIST:
                        obj.list.enable = !obj.list.enable;
                        break;
                    case LYRIC:
                        obj.lyric.enable = !obj.lyric.enable;
                        break;
                    case PIC:
                        obj.pic.enable = !obj.pic.enable;
                        break;
                }
            }
        }

        addAndSave(player, obj);
        HudUtils.sendHudPos(player);
        if (pos == null) {
            return res;
        } else {
            switch (pos) {
                case INFO:
                    return obj.info.enable;
                case LIST:
                    return obj.list.enable;
                case LYRIC:
                    return obj.lyric.enable;
                case PIC:
                    return obj.pic.enable;
            }
        }
        return false;
    }

    /**
     * 清空Hud数据
     */
    public static void clearHud() {
        AllMusic.side.clearHud();
    }

    /**
     * 清空Hud数
     *
     * @param player 用户名
     */
    public static void clearHud(String player) {
        AllMusic.side.clearHud(player);
    }

    /**
     * 发送Hud位置信息
     *
     * @param player 用户名
     */
    public static void sendHudPos(String player) {
        AllMusic.side.runTask(() -> {
            try {
                SaveObj obj = get(player);
                if (obj == null) {
                    obj = AllMusic.getConfig().defaultHud.copy();
                    addAndSave(player, obj);
                }
                AllMusic.side.sendHudPos(player);
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic3]§c数据发送发生错误");
                e1.printStackTrace();
            }
        });
    }

    /**
     * 重置Hud位置
     *
     * @param player 用户名
     */
    public static void reset(String player) {
        SaveObj obj = AllMusic.getConfig().defaultHud.copy();
        clearHud(player);
        addAndSave(player, obj);
        HudUtils.sendHudPos(player);
    }

    public static void reset(String player, HudType type) {
        SaveObj obj = AllMusic.getConfig().defaultHud.copy();
        SaveObj obj1 = get(player);
        switch (type) {
            case INFO:
                obj1.info = obj.info;
                break;
            case LIST:
                obj1.list = obj.list;
                break;
            case LYRIC:
                obj1.lyric = obj.lyric;
                break;
            case PIC:
                obj1.pic = obj.pic;
                obj1.picRotateSpeed = obj.picRotateSpeed;
                break;
        }

        addAndSave(player, obj1);
        HudUtils.sendHudPos(player);
    }

    /**
     * 设置Hud的图片大小
     *
     * @param player 用户名
     * @param size   大小
     * @return 结果
     */
    public static boolean setPicSize(String player, String size) {
        SaveObj obj = get(player);
        if (obj == null)
            obj = AllMusic.getConfig().defaultHud.copy();
        if (!Function.isInteger(size))
            return false;

        obj.pic.color = Integer.parseInt(size);

        addAndSave(player, obj);
        HudUtils.sendHudPos(player);
        return true;
    }

    /**
     * 图片旋转开关
     *
     * @param player 用户名
     * @param open   开关
     * @return 结果
     */
    public static boolean setPicRotate(String player, String open) {
        SaveObj obj = get(player);
        if (obj == null)
            obj = AllMusic.getConfig().defaultHud.copy();

        obj.pic.shadow = Boolean.parseBoolean(open);

        addAndSave(player, obj);
        HudUtils.sendHudPos(player);
        return obj.pic.shadow;
    }

    /**
     * 图片旋转速度设置
     *
     * @param player 用户名
     * @param size   大小
     * @return 结果
     */
    public static boolean setPicRotateSpeed(String player, String size) {
        SaveObj obj = get(player);
        if (obj == null)
            obj = AllMusic.getConfig().defaultHud.copy();
        if (!Function.isInteger(size))
            return false;

        obj.picRotateSpeed = Integer.parseInt(size);

        addAndSave(player, obj);
        HudUtils.sendHudPos(player);
        return true;
    }

    public static HudDirType setDir(String player, HudType hud, String arg) {
        HudDirType type = null;
        try {
            if (Function.isInteger(arg)) {
                int index = Integer.parseInt(arg);
                type = HudDirType.values()[index];
            } else {
                type = HudDirType.valueOf(arg);
            }
        } catch (Exception ignored) {

        }
        if (type == null) {
            return null;
        }

        SaveObj obj = get(player);
        if (obj == null)
            obj = AllMusic.getConfig().defaultHud.copy();

        switch (hud) {
            case INFO:
                obj.info.dir = type;
                break;
            case LIST:
                obj.list.dir = type;
                break;
            case LYRIC:
                obj.lyric.dir = type;
                break;
            case PIC:
                obj.pic.dir = type;
                break;
        }

        addAndSave(player, obj);
        HudUtils.sendHudPos(player);

        return type;
    }

    public static boolean setColor(String player, HudType type, String arg) {
        int color;
        try {
            color = arg.startsWith("0x")
                    ? Integer.parseUnsignedInt(arg.substring(2), 16)
                    : Integer.parseUnsignedInt(arg);
        } catch (Exception ignored) {
            return false;
        }

        SaveObj obj = get(player);
        if (obj == null)
            obj = AllMusic.getConfig().defaultHud.copy();

        switch (type) {
            case INFO:
                obj.info.color = color;
                break;
            case LIST:
                obj.list.color = color;
                break;
            case LYRIC:
                obj.lyric.color = color;
                break;
            case PIC:
                obj.pic.color = color;
                break;
        }

        addAndSave(player, obj);
        HudUtils.sendHudPos(player);

        return true;
    }
}
