package Color_yr.AllMusic;

import Color_yr.AllMusic.Command.CommandBC;
import Color_yr.AllMusic.Event.EventBC;
import Color_yr.AllMusic.MusicPlay.PlayMusic;
import Color_yr.AllMusic.Side.SideBC.SideBC;
import Color_yr.AllMusic.Utils.logs;
import Color_yr.AllMusic.bStats.MetricsBC;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class AllMusicBC extends Plugin {

    public static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        AllMusic.log = ProxyServer.getInstance().getLogger();
        AllMusic.Side = new SideBC();

        new AllMusic().init(plugin.getDataFolder());

        if (!AllMusic.isRun)
            return;
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CommandBC());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new EventBC());
        new MetricsBC(this, 6720);

        AllMusic.start();
    }

    @Override
    public void onDisable() {
        AllMusic.stop();
    }
}