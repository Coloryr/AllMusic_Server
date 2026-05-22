package com.coloryr.allmusic.server.core.utils;

import com.coloryr.allmusic.codec.*;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.LyricSave;
import com.coloryr.allmusic.server.core.music.PlayMusic;
import com.coloryr.allmusic.server.core.objs.config.LimitObj;
import com.coloryr.allmusic.server.core.objs.message.ARG;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.saves.HudSave;

public class HudUtils {
    /**
     * 设置组件位置
     *
     * @param player 用户名
     * @param pos    位置
     * @param x      x
     * @param y      y
     * @return 组件数据
     */
    public static HudBasePosObj setHudPos(String player, HudType pos, String x, String y) {
        HudPosObj obj = HudSave.getOrNew(player);

        if (!Function.isInteger(x) && !Function.isInteger(y))
            return null;
        int x1 = Integer.parseInt(x);
        int y1 = Integer.parseInt(y);

        HudBasePosObj posOBJ;
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
                break;
            case STATE:
                posOBJ = obj.state;
                break;
            default:
                return null;
        }
        posOBJ.x = x1;
        posOBJ.y = y1;

        HudSave.update(player, obj);
        HudUtils.sendHudPos(player);
        return posOBJ;
    }

    /**
     * 设置组件透明度
     *
     * @param player 用户名
     * @param pos    位置
     * @param alpha  透明度
     * @return 组件数据
     */
    public static HudBasePosObj setHudAlpha(String player, HudType pos, String alpha) {
        HudPosObj obj = HudSave.getOrNew(player);

        if (!Function.isInteger(alpha))
            return null;
        float alpha1 = Float.parseFloat(alpha);
        if (alpha1 > 1 || alpha1 < 0) {
            alpha1 = 1;
        }
        HudBasePosObj posOBJ;
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
                break;
            default:
                return null;
        }
        posOBJ.alpha = alpha1;

        HudSave.update(player, obj);
        HudUtils.sendHudPos(player);
        return posOBJ;
    }

    /**
     * 设置组件透明度
     *
     * @param player 用户名
     * @param pos    位置
     * @param width  最大宽度
     * @return 组件数据
     */
    public static int setHudHudMaxWidth(String player, HudType pos, String width) {
        HudPosObj obj = HudSave.getOrNew(player);

        if (!Function.isInteger(width))
            return -1;
        int maxwidth = Integer.parseInt(width);
        if (maxwidth <= 0) {
            return -1;
        }
        switch (pos) {
            case LYRIC:
                obj.lyric.maxWidth = maxwidth;
                break;
            case LIST:
                obj.list.maxWidth = maxwidth;
                break;
            case INFO:
                obj.info.maxWidth = maxwidth;
                break;
            default:
                return -1;
        }

        HudSave.update(player, obj);
        HudUtils.sendHudPos(player);
        return maxwidth;
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
                    .replace(ARG.value, String.valueOf(PlayMusic.getList().size()))
                    .replace(ARG.list, list.toString());
        }

        AllMusic.side.sendHudList(info);
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
                    .replace(ARG.musicAuthor, infoLimit(PlayMusic.nowPlayMusic.getAuthor()))
                    .replace(ARG.musicAlia, infoLimit(PlayMusic.nowPlayMusic.getAlia()))
                    .replace(ARG.musicAl, infoLimit(PlayMusic.nowPlayMusic.getAl()))
                    .replace(ARG.player, PlayMusic.nowPlayMusic.getCall());
        }

        AllMusic.side.sendHudInfo(info);
    }

    public static void sendHudTime() {
        MusicPack.TimeMusicPack pack = new MusicPack.TimeMusicPack(PlayMusic.musicAllTime, PlayMusic.musicNowTime);
        AllMusic.side.sendHudTime(pack);
    }

    /**
     * 更新Hud的歌词数据
     */
    public static void sendHudLyricData() {
        String lyr;
        String tran;
        String ktv;
        LyricSave obj = PlayMusic.lyric;
        if (obj == null) {
            lyr = AllMusic.getMessage().hud.emptyLyric;
            tran = "";
            ktv = "";
        } else {
            String lyric = obj.getLyric();
            String tLyric = obj.getTlyric();
            lyr = AllMusic.getMessage().hud.lyric
                    .replace(ARG.lyric, lyric == null ? "" : lyric);
            tran = AllMusic.getMessage().hud.tlyric
                    .replace(ARG.lyric, tLyric == null ? "" : tLyric);
            ktv = AllMusic.getMessage().hud.klyric
                    .replace(ARG.lyric, lyric == null ? "" : lyric);
        }

        AllMusic.side.sendHudLyric(new MusicPack.LyricMusicPack(lyr, tran, ktv));
    }

    /**
     * 更新Hud的开启关闭数据
     *
     * @param player 用户名
     * @param pos    输入数据
     * @return 设置结果
     */
    public static boolean setHudEnable(String player, HudType pos, String arg) {
        HudPosObj obj = HudSave.getOrNew(player);
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

        HudSave.update(player, obj);
        HudUtils.sendHudPos(player);

        return res;
    }

    /**
     * 清空Hud数据
     */
    public static void sendClearHud() {
        AllMusic.side.clearHud();
    }

    /**
     * 清空Hud数
     *
     * @param player 用户名
     */
    public static void sendClearHud(String player) {
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
                AllMusic.side.sendHudPos(player);
            } catch (Exception e1) {
                AllMusic.log.data("<light_purple>[AllMusic3]<red>数据发送发生错误");
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
        HudPosObj obj = HudSave.defaultHud.copy();
        HudSave.update(player, obj);
        HudUtils.sendHudPos(player);
    }

    public static void reset(String player, HudType type) {
        HudPosObj obj = HudSave.defaultHud.copy();
        HudPosObj obj1 = HudSave.getOrNew(player);
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
                break;
        }

        HudSave.update(player, obj1);
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
        HudPosObj obj = HudSave.getOrNew(player);
        if (!Function.isInteger(size))
            return false;

        obj.pic.size = Integer.parseInt(size);

        HudSave.update(player, obj);
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
        HudPosObj obj = HudSave.getOrNew(player);

        if (open != null) {
            obj.pic.rotate = Boolean.parseBoolean(open);
        } else {
            obj.pic.rotate = !obj.pic.rotate;
        }

        HudSave.update(player, obj);
        HudUtils.sendHudPos(player);
        return obj.pic.rotate;
    }

    /**
     * 图片旋转速度设置
     *
     * @param player 用户名
     * @param size   大小
     * @return 结果
     */
    public static boolean setPicRotateSpeed(String player, String size) {
        HudPosObj obj = HudSave.getOrNew(player);
        if (!Function.isInteger(size))
            return false;

        obj.pic.speed = Integer.parseInt(size);

        HudSave.update(player, obj);
        HudUtils.sendHudPos(player);
        return true;
    }

    public static HudPosType setPos(String player, HudType hud, String arg) {
        HudPosType type = null;
        try {
            if (Function.isInteger(arg)) {
                int index = Integer.parseInt(arg);
                type = HudPosType.values()[index];
            } else {
                type = HudPosType.valueOf(arg);
            }
        } catch (Exception ignored) {

        }
        if (type == null) {
            return null;
        }

        HudPosObj obj = HudSave.getOrNew(player);
        switch (hud) {
            case INFO:
                obj.info.pos = type;
                break;
            case LIST:
                obj.list.pos = type;
                break;
            case LYRIC:
                obj.lyric.pos = type;
                break;
            case PIC:
                obj.pic.pos = type;
                break;
        }

        HudSave.update(player, obj);
        HudUtils.sendHudPos(player);

        return type;
    }

    public static int setColor(String player, HudType type, String arg) {
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
            return -1;
        }

        color = (color & -67108864) == 0 ? color | 0xFF000000 : color;

        HudPosObj obj = HudSave.getOrNew(player);
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
                return -1;
        }

        HudSave.update(player, obj);
        HudUtils.sendHudPos(player);

        return color;
    }

    public static boolean setShadow(String name, HudType pos, String arg) {
        HudPosObj obj = HudSave.getOrNew(name);
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

        if (pos == null) {
            if (have) {
                res = obj.info.shadow = obj.list.shadow = obj.lyric.shadow = value;
            } else if (obj.info.shadow && obj.list.shadow && obj.lyric.shadow) {
                obj.info.shadow = obj.list.shadow = obj.lyric.shadow = false;
            } else {
                res = obj.info.shadow = obj.list.shadow = obj.lyric.shadow = true;
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
            }
        }

        HudSave.update(name, obj);
        HudUtils.sendHudPos(name);

        return res;
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

    public static String setLoop(String name, HudType type, String arg) {
        LoopType loop;
        try {
            loop = LoopType.valueOf(arg);
        } catch (Exception ignored) {
            return null;
        }

        HudPosObj obj = HudSave.getOrNew(name);
        switch (type) {
            case INFO:
                obj.info.loop = loop;
                break;
            case LIST:
                obj.list.loop = loop;
                break;
            case PIC:
                return null;
        }

        HudSave.update(name, obj);
        HudUtils.sendHudPos(name);

        return loop.toString();
    }
}
