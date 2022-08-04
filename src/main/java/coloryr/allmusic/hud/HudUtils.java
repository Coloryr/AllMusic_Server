package coloryr.allmusic.hud;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.hud.obj.Pos;
import coloryr.allmusic.hud.obj.PosOBJ;
import coloryr.allmusic.hud.obj.SaveOBJ;
import coloryr.allmusic.music.api.SongInfo;
import coloryr.allmusic.music.lyric.LyricItem;
import coloryr.allmusic.music.play.PlayMusic;
import coloryr.allmusic.utils.Function;
import com.google.gson.Gson;

public class HudUtils {
    public static PosOBJ setHudPos(String player, String pos, String x, String y) {
        SaveOBJ obj = HudSave.get(player);
        if (obj == null)
            obj = AllMusic.getConfig().getDefaultHud().copy();
        Pos pos1 = Pos.valueOf(pos);
        PosOBJ posOBJ = new PosOBJ(0, 0);
        if (!Function.isInteger(x) && !Function.isInteger(y))
            return null;
        int x1 = Integer.parseInt(x);
        int y1 = Integer.parseInt(y);

        switch (pos1) {
            case lyric:
                posOBJ = obj.getLyric();
                break;
            case list:
                posOBJ = obj.getList();
                break;
            case info:
                posOBJ = obj.getInfo();
                break;
            case pic:
                posOBJ = obj.getPic();
        }
        posOBJ.setX(x1);
        posOBJ.setY(y1);
        switch (pos1) {
            case lyric:
                obj.setLyric(posOBJ);
                break;
            case list:
                obj.setList(posOBJ);
                break;
            case info:
                obj.setInfo(posOBJ);
                break;
            case pic:
                obj.setPic(posOBJ);
                break;
        }

        HudSave.add(player, obj);
        AllMusic.save();
        HudUtils.sendHudSave(player);
        return posOBJ;
    }

    public static void sendHudListData() {
        StringBuilder list = new StringBuilder();
        if (PlayMusic.getSize() == 0) {
            list.append(AllMusic.getMessage().getHud().getNoList());
        } else {
            String now;
            for (SongInfo info : PlayMusic.getList()) {
                if (info == null)
                    continue;
                now = info.getInfo();
                if (now.length() > 30)
                    now = now.substring(0, 29) + "...";
                list.append(now).append("\n");
            }
        }

        AllMusic.side.sendHudList(list.toString());
    }

    private static String tranTime(int time) {
        int m = time / 60;
        int s = time - m * 60;
        return (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s;
    }

    public static void sendHudNowData() {
        StringBuilder list = new StringBuilder();
        if (PlayMusic.nowPlayMusic == null) {
            list.append(AllMusic.getMessage().getHud().getNoMusic());
        } else {
            list.append(PlayMusic.nowPlayMusic.getName()).append("   ")
                    .append(tranTime(PlayMusic.musicAllTime)).append("/")
                    .append(tranTime(PlayMusic.musicNowTime / 1000)).append("\n");
            list.append(PlayMusic.nowPlayMusic.getAuthor()).append("\n");
            list.append(PlayMusic.nowPlayMusic.getAlia()).append("\n");
            list.append(PlayMusic.nowPlayMusic.getAl()).append("\n");
            list.append("by:").append(PlayMusic.nowPlayMusic.getCall());
        }

        AllMusic.side.sendHudInfo(list.toString());
    }

    public static void sendHudLyricData(LyricItem showobj) {
        StringBuilder list = new StringBuilder();
        if (showobj == null) {
            list.append(AllMusic.getMessage().getHud().getNoLyric());
        } else {
            if (showobj.getLyric() != null)
                list.append(showobj.getLyric()).append("\n");
            if (showobj.isHaveT() && showobj.getTlyric() != null)
                list.append(showobj.getTlyric());
        }

        AllMusic.side.sendHudLyric(list.toString());
    }

    public static boolean setHudEnable(String player, String pos) {
        SaveOBJ obj = HudSave.get(player);
        boolean a = false;
        if (obj == null) {
            obj = AllMusic.getConfig().getDefaultHud().copy();
            a = obj.isEnableInfo() && obj.isEnableList() && obj.isEnableLyric();
        } else {
            if (pos == null) {
                if (obj.isEnableInfo() && obj.isEnableList() && obj.isEnableLyric()) {
                    obj.setEnableInfo(false);
                    obj.setEnableList(false);
                    obj.setEnableLyric(false);
                    obj.setEnablePic(false);
                    a = false;
                } else {
                    obj.setEnableInfo(true);
                    obj.setEnableList(true);
                    obj.setEnableLyric(true);
                    obj.setEnablePic(true);
                    a = true;
                }
            } else {
                Pos pos1 = Pos.valueOf(pos);
                switch (pos1) {
                    case info:
                        obj.setEnableInfo(!obj.isEnableInfo());
                        break;
                    case list:
                        obj.setEnableList(!obj.isEnableList());
                        break;
                    case lyric:
                        obj.setEnableLyric(!obj.isEnableLyric());
                        break;
                    case pic:
                        obj.setEnablePic(!obj.isEnablePic());
                        break;
                }
            }
        }
        clearHud(player);
        HudSave.add(player, obj);
        AllMusic.save();
        HudUtils.sendHudSave(player);
        if (pos == null) {
            return a;
        } else {
            Pos pos1 = Pos.valueOf(pos);
            switch (pos1) {
                case info:
                    return obj.isEnableInfo();
                case list:
                    return obj.isEnableList();
                case lyric:
                    return obj.isEnableLyric();
                case pic:
                    return obj.isEnablePic();
            }
        }
        return false;
    }

    public static void clearHud() {
        AllMusic.side.clearHudAll();
    }

    public static void clearHud(String Name) {
        AllMusic.side.clearHud(Name);
    }

    public static void sendHudSave(String Name) {
        AllMusic.side.runTask(() -> {
            try {
                SaveOBJ obj = HudSave.get(Name);
                if (obj == null) {
                    obj = AllMusic.getConfig().getDefaultHud().copy();
                    HudSave.add(Name, obj);
                    AllMusic.save();
                }
                String data = new Gson().toJson(obj);
                AllMusic.side.send(data, Name, null);
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        });
    }

    public static void reset(String name) {
        SaveOBJ obj = AllMusic.getConfig().getDefaultHud().copy();
        clearHud(name);
        HudSave.add(name, obj);
        AllMusic.save();
        HudUtils.sendHudSave(name);
    }

    public static boolean setPicSize(String name, String size){
        SaveOBJ obj = HudSave.get(name);
        if (obj == null)
            obj = AllMusic.getConfig().getDefaultHud().copy();
        if (!Function.isInteger(size))
            return false;
        int size1 = Integer.parseInt(size);

        obj.setPicSize(size1);

        HudSave.add(name, obj);
        AllMusic.save();
        HudUtils.sendHudSave(name);
        return true;
    }
}
