package coloryr.allmusic.side.bukkit;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.music.play.PlayMusic;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class AllMusicPAPI extends PlaceholderExpansion {

    private final Plugin plugin;

    public AllMusicPAPI(Plugin plugin) {
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
        AllMusic.log.info("§e正在挂钩PlaceholderAPI");
        return super.register();
    }

    @Override
    public @NotNull String getAuthor() {
        return "Color_yr";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "allmusic";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        return onRequest(player, identifier);
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

        switch (identifier) {
            case "now_music_name": {
                if (PlayMusic.nowPlayMusic == null)
                    return AllMusic.getMessage().getPAPI().getNoMusic();
                return PlayMusic.nowPlayMusic.getName();
            }
            case "now_music_al": {
                if (PlayMusic.nowPlayMusic == null)
                    return "";
                return PlayMusic.nowPlayMusic.getAl();
            }
            case "now_music_alia": {
                if (PlayMusic.nowPlayMusic == null)
                    return "";
                return PlayMusic.nowPlayMusic.getAlia();
            }
            case "now_music_author": {
                if (PlayMusic.nowPlayMusic == null)
                    return "";
                return PlayMusic.nowPlayMusic.getAuthor();
            }
            case "now_music_call": {
                if (PlayMusic.nowPlayMusic == null)
                    return "";
                return PlayMusic.nowPlayMusic.getCall();
            }
            case "now_music_info": {
                if (PlayMusic.nowPlayMusic == null)
                    return "";
                return PlayMusic.nowPlayMusic.getInfo();
            }
            case "list_size": {
                return "" + PlayMusic.getSize();
            }
            case "music_list": {
                return PlayMusic.getAllList();
            }
            case "lyric": {
                if (PlayMusic.nowLyric == null)
                    return "";
                return PlayMusic.nowLyric.getLyric();
            }
            case "tlyric": {
                if (PlayMusic.nowLyric == null)
                    return "";
                return PlayMusic.nowLyric.getTlyric();
            }
        }
        return null;
    }
}
