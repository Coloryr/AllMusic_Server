package coloryr.allmusic.side.velocity;

import coloryr.allmusic.command.CommandEX;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

public class CommandVelocity implements SimpleCommand {
    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        String name = "CONSOLE";
        if (invocation.source() instanceof Player) {
            Player player = (Player) invocation.source();
            name = player.getUsername();
        }
        CommandEX.ex(source, name, args);
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return true;
    }
}
