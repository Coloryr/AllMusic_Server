package coloryr.allmusic.side.folia.hooks;

import coloryr.allmusic.core.sql.IEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook implements IEconomy {
    private Economy econ = null;

    public VaultHook() {
        RegisteredServiceProvider<Economy> rsp =
                Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
    }

    public boolean setupEconomy() {
        return econ != null;
    }

    public boolean check(String name, int cost) {
        Player player = Bukkit.getPlayer(name);
        if (player == null)
            return false;
        return econ.has(player, cost);
    }

    public boolean cost(String name, int cost) {
        Player player = Bukkit.getPlayer(name);
        if (player == null)
            return false;
        EconomyResponse r = econ.withdrawPlayer(player, cost);
        return r.transactionSuccess();
    }
}
