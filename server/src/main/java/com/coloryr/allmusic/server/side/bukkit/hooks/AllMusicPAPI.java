package com.coloryr.allmusic.server.side.bukkit.hooks;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;
import com.coloryr.allmusic.server.side.bukkit.PluginMessage;
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
        if (AllMusic.getConfig().topPAPI) {
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
                    if (PlayMusic.lyric == null || PlayMusic.lyric.getLyric() == null) {
                        return AllMusic.getMessage().lyric.empty1;
                    }
                    return PlayMusic.lyric.getLyric();
                }
                case "tlyric": {
                    if (PlayMusic.lyric == null || PlayMusic.lyric.getTlyric() == null) {
                        return AllMusic.getMessage().lyric.empty2;
                    }
                    return PlayMusic.lyric.getTlyric();
                }
                case "klyric": {
                    if (PlayMusic.lyric == null || PlayMusic.lyric.getKly() == null) {
                        return AllMusic.getMessage().lyric.empty3;
                    }
                    return PlayMusic.lyric.getKly();
                }
            }
        } else {
            switch (identifier) {
                case "now_music_name": {
                    if (PlayMusic.nowPlayMusic == null)
                        return AllMusic.getMessage().papi.emptyMusic;
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
                    return "" + PlayMusic.getListSize();
                }
                case "music_list": {
                    return PlayMusic.getAllList();
                }
                case "lyric": {
                    if (PlayMusic.lyric == null)
                        return "";
                    return PlayMusic.lyric.getLyric();
                }
                case "tlyric": {
                    if (PlayMusic.lyric == null)
                        return "";
                    return PlayMusic.lyric.getTlyric();
                }
                case "klyric": {
                    if (PlayMusic.lyric == null)
                        return "";
                    return PlayMusic.lyric.getKly();
                }
            }
        }
        return null;
    }
}
