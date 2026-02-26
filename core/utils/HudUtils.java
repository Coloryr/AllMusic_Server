package com.coloryr.allmusic.server.core.utils;

import com.coloryr.allmusic.codec.HudDirType;
import com.coloryr.allmusic.codec.HudItemPosObj;
import com.coloryr.allmusic.codec.HudPosObj;
import com.coloryr.allmusic.codec.HudType;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.LyricSave;
import com.coloryr.allmusic.server.core.music.PlayMusic;
import com.coloryr.allmusic.server.core.objs.config.LimitObj;
import com.coloryr.allmusic.server.core.objs.message.ARG;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.sql.DataSql;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HudUtils {
    private static final Map<String, HudPosObj> HudList = new ConcurrentHashMap<>();

    /**
     * 获取玩家的Hud储存
     *
     * @param name 玩家名
     * @return Hud储存
     */
    public static HudPosObj get(String name) {
        name = name.toLowerCase(Locale.ROOT);
        if (!HudList.containsKey(name)) {
            HudPosObj obj1 = DataSql.readHud(name);
            if (obj1 == null) {
                HudPosObj obj = AllMusic.getConfig().defaultHud.copy();
                HudList.put(name, obj);
                String finalName = name;
                DataSql.task(() -> DataSql.addUser(finalName, obj));
                return obj;
            }

            HudList.put(name, obj1);
            return obj1;
        }
        return HudList.get(name);
    }

    public static void addAndSave(String name, HudPosObj hud) {
        name = name.toLowerCase(Locale.ROOT);
        HudList.put(name, hud);
        String finalName = name;
        DataSql.task(() -> DataSql.addUser(finalName, hud));
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
    public static HudItemPosObj setHudPos(String player, HudType pos, String x, String y) {
        HudPosObj obj = get(player);
        if (obj == null)
            obj = AllMusic.getConfig().defaultHud.copy();
        HudItemPosObj posOBJ = new HudItemPosObj(0, 0, HudDirType.TOP_LEFT, 0xffffff, false, true);
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
                now = AllMusic.getMessage().musicPlay.musicInfo
                        .replace(ARG.musicName, listLimit(info1.getName()))
                        .replace(ARG.musicAuthor, listLimit(info1.getAuthor()))
                        .replace(ARG.musicAl, listLimit(info1.getAl()))
                        .replace(ARG.musicAlia, listLimit(info1.getAlia()))
                        .replace(ARG.player, info1.getCall());

                list.append(now).append("\n");
            }
            info = AllMusic.getMessage().hud.list
                    .replace(ARG.size, String.valueOf(PlayMusic.getList().size()))
                    .replace(ARG.list, list.toString());
        }

        AllMusic.side.sendHudList(info);
    }

    /**
     * 时间转换
     *
     * @param time 时间
     * @return 结果
     */
    private static String tranTime(long time) {
        long m = time / 60;
        long s = time - m * 60;
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
                    .replace(ARG.name, infoLimit(PlayMusic.nowPlayMusic.getName()))
                    .replace(ARG.allTime, tranTime(PlayMusic.musicAllTime))
                    .replace(ARG.nowTime, tranTime(PlayMusic.musicNowTime / 1000))
                    .replace(ARG.musicAuthor, infoLimit(PlayMusic.nowPlayMusic.getAuthor()))
                    .replace(ARG.musicAlia, infoLimit(PlayMusic.nowPlayMusic.getAlia()))
                    .replace(ARG.musicAl, infoLimit(PlayMusic.nowPlayMusic.getAl()))
                    .replace(ARG.player, PlayMusic.nowPlayMusic.getCall());
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
                        .replace(ARG.lyric, lyric == null ? "" : lyric)
                        .replace(ARG.tlyric, tLyric != null ? tLyric : "");
            } else {
                info = AllMusic.getMessage().hud.ktv
                        .replace(ARG.lyric, lyric != null ? lyric : "")
                        .replace(ARG.klyric, kLyric != null ? kLyric : "")
                        .replace(ARG.tlyric, tLyric != null ? tLyric : "");
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
    public static boolean setHudEnable(String player, HudType pos, String arg) {
        HudPosObj obj = get(player);
        boolean res = false;
        boolean value = false;
        boolean have = false;
        if (arg != null) {
            try {
                value = Boolean.parseBoolean(arg);
                have = true;
            } catch (Exception e) {
                return false;
            }
        }
        if (obj == null) {
            obj = AllMusic.getConfig().defaultHud.copy();
            if (have) {
                res = obj.info.enable = obj.list.enable = obj.lyric.enable = obj.pic.enable = value;
            } else {
                res = obj.info.enable = obj.list.enable = obj.lyric.enable = obj.pic.enable = true;
            }
        } else {
            if (pos == null) {
                if (have) {
                    res = obj.info.enable = obj.list.enable = obj.lyric.enable = obj.pic.enable = value;
                } else if (obj.info.enable && obj.list.enable && obj.lyric.enable && obj.pic.enable) {
                    obj.info.enable = obj.list.enable = obj.lyric.enable = obj.pic.enable = false;
                } else {
                    res = obj.info.enable = obj.list.enable = obj.lyric.enable = obj.pic.enable = true;
                }
            } else {
                switch (pos) {
                    case INFO:
                        res = obj.info.enable = have ? value : !obj.info.enable;
                        break;
                    case LIST:
                        res = obj.list.enable = have ? value : !obj.list.enable;
                        break;
                    case LYRIC:
                        res = obj.lyric.enable = have ? value : !obj.lyric.enable;
                        break;
                    case PIC:
                        res = obj.pic.enable = have ? value : !obj.pic.enable;
                        break;
                }
            }
        }

        addAndSave(player, obj);
        HudUtils.sendHudPos(player);

        return res;
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
                HudPosObj obj = get(player);
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
        HudPosObj obj = AllMusic.getConfig().defaultHud.copy();
        addAndSave(player, obj);
        HudUtils.sendHudPos(player);
    }

    public static void reset(String player, HudType type) {
        HudPosObj obj = AllMusic.getConfig().defaultHud.copy();
        HudPosObj obj1 = get(player);
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
        HudPosObj obj = get(player);
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
        HudPosObj obj = get(player);
        if (obj == null)
            obj = AllMusic.getConfig().defaultHud.copy();

        if (open != null) {
            obj.pic.shadow = Boolean.parseBoolean(open);
        } else {
            obj.pic.shadow = !obj.pic.shadow;
        }

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
        HudPosObj obj = get(player);
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

        HudPosObj obj = get(player);
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
            if (arg.startsWith("0x")) {
                color = Integer.parseUnsignedInt(arg.substring(2), 16);
            } else if (arg.startsWith("#")) {
                color = Integer.parseUnsignedInt(arg.substring(1), 16);
            } else {
                color = Integer.parseUnsignedInt(arg);
            }
        } catch (Exception ignored) {
            return false;
        }

        HudPosObj obj = get(player);
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

    public static boolean setShadow(String name, HudType pos, String arg) {
        HudPosObj obj = get(name);
        boolean res = false;
        boolean value = false;
        boolean have = false;
        if (arg != null) {
            try {
                value = Boolean.parseBoolean(arg);
                have = true;
            } catch (Exception e) {
                return false;
            }
        }
        if (obj == null) {
            obj = AllMusic.getConfig().defaultHud.copy();
            if (have) {
                res = obj.info.shadow = obj.list.shadow = obj.lyric.shadow = obj.pic.shadow = value;
            } else {
                res = obj.info.shadow = obj.list.shadow = obj.lyric.shadow = obj.pic.shadow = true;
            }
        } else {
            if (pos == null) {
                if (have) {
                    res = obj.info.shadow = obj.list.shadow = obj.lyric.shadow = obj.pic.shadow = value;
                } else if (obj.info.shadow && obj.list.shadow && obj.lyric.shadow && obj.pic.shadow) {
                    obj.info.shadow = obj.list.shadow = obj.lyric.shadow = obj.pic.shadow = false;
                } else {
                    res = obj.info.shadow = obj.list.shadow = obj.lyric.shadow = obj.pic.shadow = true;
                }
            } else {
                switch (pos) {
                    case INFO:
                        res = obj.info.shadow = have ? value : !obj.info.shadow;
                        break;
                    case LIST:
                        res = obj.list.shadow = have ? value : !obj.list.shadow;
                        break;
                    case LYRIC:
                        res = obj.lyric.shadow = have ? value : !obj.lyric.shadow;
                        break;
                    case PIC:
                        res = obj.pic.shadow = have ? value : !obj.pic.shadow;
                        break;
                }
            }
        }

        addAndSave(name, obj);
        HudUtils.sendHudPos(name);

        return res;
    }

    public static void set(String name, HudPosObj obj) {
        addAndSave(name, obj);
        HudUtils.sendHudPos(name);
    }

    public static String infoLimit(String info) {
        LimitObj limit = AllMusic.getConfig().limit;
        if (limit.infoLimit && info.length() > limit.infoLimitSize)
            info = info.substring(0, limit.infoLimitSize - 1) + limit.limitText;

        return info;
    }

    public static String listLimit(String list) {
        LimitObj limit = AllMusic.getConfig().limit;
        if (limit.listLimit && list.length() > limit.listLimitSize)
            list = list.substring(0, limit.listLimitSize - 1) + limit.limitText;

        return list;
    }

    public static String messageLimit(String message) {
        LimitObj limit = AllMusic.getConfig().limit;
        if (limit.messageLimit && message.length() > limit.messageLimitSize)
            message = message.substring(0, limit.messageLimitSize - 1) + limit.limitText;

        return message;
    }
}
