package Color_yr.AllMusic;

import Color_yr.AllMusic.Command.CommandBC;
import Color_yr.AllMusic.Event.EventBC;
import Color_yr.AllMusic.Side.SideBC.BCLog;
import Color_yr.AllMusic.Side.SideBC.MetricsBC;
import Color_yr.AllMusic.Side.SideBC.SideBC;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class AllMusicBC extends Plugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        AllMusic.log = new BCLog(ProxyServer.getInstance().getLogger());
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