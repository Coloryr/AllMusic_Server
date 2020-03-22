package Color_yr.ALLMusic.Side.SideBukkit;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.MusicPlay.PlayMusic;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PAPI extends PlaceholderExpansion {

    private Plugin plugin;

    public PAPI(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean register() {
        ALLMusic.log.info("§e正在挂钩PlaceholderAPI");
        return super.register();
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "AllMusic";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        switch (identifier) {
            case "NowMusicName": {
                if (PlayMusic.NowPlayMusic == null)
                    return null;
                return PlayMusic.NowPlayMusic.getName();
            }
            case "NowMusicAl": {
                if (PlayMusic.NowPlayMusic == null)
                    return null;
                return PlayMusic.NowPlayMusic.getAl();
            }
            case "NowMusicAlia": {
                if (PlayMusic.NowPlayMusic == null)
                    return null;
                return PlayMusic.NowPlayMusic.getAlia();
            }
            case "NowMusicAuthor": {
                if (PlayMusic.NowPlayMusic == null)
                    return null;
                return PlayMusic.NowPlayMusic.getAuthor();
            }
            case "NowMusicCall": {
                if (PlayMusic.NowPlayMusic == null)
                    return null;
                return PlayMusic.NowPlayMusic.getCall();
            }
            case "NowMusicInfo": {
                if (PlayMusic.NowPlayMusic == null)
                    return null;
                return PlayMusic.NowPlayMusic.getInfo();
            }
            case "ListSize": {
                return "" + PlayMusic.getSize();
            }
            case "MusicList": {
                return PlayMusic.getAllList();
            }
            case "Lyric": {
                if (PlayMusic.nowLyric == null)
                    return null;
                return PlayMusic.nowLyric.getLyric();
            }
            case "TLyric": {
                if (PlayMusic.nowLyric == null)
                    return null;
                return PlayMusic.nowLyric.getTlyric();
            }
        }
        return null;
    }
}
