package Color_yr.ALLMusic.Side.SideBukkit.VV;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.MusicPlay.PlayMusic;
import Color_yr.ALLMusic.MusicAPI.SongInfo.SongInfo;
import Color_yr.ALLMusic.MusicAPI.SongLyric.ShowOBJ;
import Color_yr.ALLMusic.Utils.Function;
import lk.vexview.api.VexViewAPI;
import lk.vexview.hud.VexTextShow;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VVGet {
    public static String version;

    public VVGet() {
        version = VexViewAPI.getVexView().getVersion();
        ALLMusic.log.info("§d[ALLMusic]§2VexView支持已启动");
    }

    public boolean SetPot(String player, String pos, String local, String data) {
        VVSaveOBJ obj = ALLMusic.Config.getVVSave(player);
        if (obj == null)
            obj = new VVSaveOBJ();
        Pos pos1 = Pos.valueOf(pos);
        PosOBJ posOBJ = new PosOBJ(0, 0);
        int data1 = Integer.parseInt(data);
        Local local1 = Local.valueOf(local);
        if (!Function.isInteger(data))
            return false;
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
        }
        switch (local1) {
            case x:
                posOBJ.setX(data1);
                break;
            case y:
                posOBJ.setY(data1);
                break;
        }
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
        }

        ALLMusic.Config.setVVSave(obj, player);
        ALLMusic.save();

        return true;
    }

    public void SendList() {
        VexTextShow show;
        List<String> list = new ArrayList<>();
        boolean save = false;
        if (PlayMusic.getSize() == 0) {
            list.add("队列中无歌曲");
        } else {
            String now;
            for (SongInfo info : PlayMusic.getList()) {
                now = info.getInfo();
                if (now.length() > 30)
                    now = now.substring(0, 29) + "...";
                list.add(now);
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (ALLMusic.Config.getNoMusicPlayer().contains(player.getName()))
                continue;
            VVSaveOBJ obj = ALLMusic.Config.getVVSave(player.getName());
            if (obj == null) {
                obj = new VVSaveOBJ();
                ALLMusic.Config.setVVSave(obj, player.getName());
                save = true;
            }
            if (!obj.isEnable())
                continue;

            show = new VexTextShow("ALLMusicList", obj.getList().getX(), obj.getList().getY(), 0, list, 0);
            VexViewAPI.sendHUD(player, show);
        }
        if (save) {
            ALLMusic.save();
        }
    }

    public void SendInfo() {
        VexTextShow show;
        List<String> list = new ArrayList<>();
        boolean save = false;
        if (PlayMusic.NowPlayMusic == null) {
            list.add("没有播放的音乐");
        } else {
            list.add(PlayMusic.NowPlayMusic.getName());
            list.add(PlayMusic.NowPlayMusic.getAuthor());
            list.add(PlayMusic.NowPlayMusic.getAlia());
            list.add(PlayMusic.NowPlayMusic.getAl());
            list.add("by:" + PlayMusic.NowPlayMusic.getCall());
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (ALLMusic.Config.getNoMusicPlayer().contains(player.getName()))
                continue;
            VVSaveOBJ obj = ALLMusic.Config.getVVSave(player.getName());
            if (obj == null) {
                obj = new VVSaveOBJ();
                ALLMusic.Config.setVVSave(obj, player.getName());
                save = true;
            }
            if (!obj.isEnable())
                continue;

            show = new VexTextShow("ALLMusicInfo", obj.getInfo().getX(), obj.getInfo().getY(), 0, list, 0);
            VexViewAPI.sendHUD(player, show);
        }
        if (save) {
            ALLMusic.save();
        }
    }

    public void SendLyric(ShowOBJ showobj) {
        VexTextShow show;
        List<String> list = new ArrayList<>();
        boolean save = false;
        if (showobj == null) {
            list.add("无歌词");
        } else {
            if (showobj.getLyric() != null)
                list.add(showobj.getLyric());
            if (showobj.isHaveT() && showobj.getTlyric() != null)
                list.add(showobj.getTlyric());
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!PlayMusic.NowPlayPlayer.contains(player.getName()))
                continue;
            if (ALLMusic.Config.getNoMusicPlayer().contains(player.getName()))
                continue;
            VVSaveOBJ obj = ALLMusic.Config.getVVSave(player.getName());
            if (obj == null) {
                obj = new VVSaveOBJ();
                ALLMusic.Config.setVVSave(obj, player.getName());
                save = true;
            }
            if (!obj.isEnable())
                continue;

            show = new VexTextShow("ALLMusicLyric", obj.getLyric().getX(), obj.getLyric().getY(), 0, list, 0);
            VexViewAPI.sendHUD(player, show);
        }
        if (save) {
            ALLMusic.save();
        }
    }

    public boolean SetEnable(String player) {
        VVSaveOBJ obj = ALLMusic.Config.getVVSave(player);
        if (obj == null)
            obj = new VVSaveOBJ();
        obj.setEnable(!obj.isEnable());
        if (!obj.isEnable()) {
            clear(Bukkit.getPlayer(player));
        }
        ALLMusic.Config.setVVSave(obj, player);
        ALLMusic.save();
        return obj.isEnable();
    }

    public void clear() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            VexViewAPI.removeHUD(player, "ALLMusicInfo");
            VexViewAPI.removeHUD(player, "ALLMusicList");
            VexViewAPI.removeHUD(player, "ALLMusicLyric");
        }
    }

    public void clear(Player player) {
        VexViewAPI.removeHUD(player, "ALLMusicInfo");
        VexViewAPI.removeHUD(player, "ALLMusicList");
        VexViewAPI.removeHUD(player, "ALLMusicLyric");
    }

    public void clear(String Name) {
        Player player = Bukkit.getPlayer(Name);
        if (player != null) {
            VexViewAPI.removeHUD(player, "ALLMusicInfo");
            VexViewAPI.removeHUD(player, "ALLMusicList");
            VexViewAPI.removeHUD(player, "ALLMusicLyric");
        }
    }

    public enum Pos {
        info, list, lyric
    }

    public enum Local {
        x, y
    }
}
