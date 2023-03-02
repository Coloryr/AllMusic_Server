package coloryr.allmusic.side.fabric;

import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class FabricApi {

    public static void sendMessageRun(Object obj, String message, String command) {
        CommandOutput sender = (CommandOutput) obj;
        MutableText send = Text.literal(message);
        send.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        sender.sendMessage(send);
    }

    public static void sendMessageSuggest(Object obj, String message, String command) {
        CommandOutput sender = (CommandOutput) obj;
        MutableText send = Text.empty();
        send.append(message).getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        sender.sendMessage(send);
    }

    public static void sendBar(ServerPlayerEntity player, String message) {
        var pack = new OverlayMessageS2CPacket(Text.literal(message));
        player.networkHandler.sendPacket(pack);
    }
}
