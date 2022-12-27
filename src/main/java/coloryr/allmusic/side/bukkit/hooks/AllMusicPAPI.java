package coloryr.allmusic.side.bukkit.hooks;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.music.play.PlayMusic;
import coloryr.allmusic.side.bukkit.PluginMessage;
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
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        if (AllMusic.getConfig().isTopPAPI()) {
            if (!PluginMessage.update) {
                PluginMessage.startUpdate();
                return "";
            }
            switch (identifier) {
                case "now_music_name": {
                    return PlayMusic.nowPlayMusic.getName();
                }
                case "now_music_al": {
                    return PlayMusic.nowPlayMusic.getAl();
                }
                case "now_music_alia": {
                    return PlayMusic.nowPlayMusic.getAlia();
                }
                case "now_music_author": {
                    return PlayMusic.nowPlayMusic.getAuthor();
                }
                case "now_music_call": {
                    return PlayMusic.nowPlayMusic.getCall();
                }
                case "now_music_info": {
                    return PlayMusic.nowPlayMusic.getInfo();
                }
                case "list_size": {
                    return "" + PluginMessage.size;
                }
                case "music_list": {
                    return PluginMessage.allList;
                }
                case "lyric": {
                    return PlayMusic.lyricItem.getLyric();
                }
                case "tlyric": {
                    return PlayMusic.lyricItem.getTlyric();
                }
            }
        } else {
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
                    if (PlayMusic.lyricItem == null)
                        return "";
                    return PlayMusic.lyricItem.getLyric();
                }
                case "tlyric": {
                    if (PlayMusic.lyricItem == null)
                        return "";
                    return PlayMusic.lyricItem.getTlyric();
                }
            }
        }
        return null;
    }
}
