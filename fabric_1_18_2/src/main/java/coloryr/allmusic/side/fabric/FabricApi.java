package coloryr.allmusic.side.fabric;

import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.UUID;

public class FabricApi {

    public static void sendMessageRun(Object obj, String message, String end, String command) {
        CommandOutput sender = (CommandOutput) obj;
        LiteralText send = new LiteralText(message);
        LiteralText endText = new LiteralText(end);
        endText.setStyle(endText.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
        send.append(endText);
        sender.sendSystemMessage(send, UUID.randomUUID());
    }

    public static void sendMessageSuggest(Object obj, String message, String end, String command) {
        CommandOutput sender = (CommandOutput) obj;
        LiteralText send = new LiteralText(message);
        LiteralText endText = new LiteralText(end);
        endText.setStyle(endText.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.append(endText);
        sender.sendSystemMessage(send, UUID.randomUUID());
    }

    public static void sendBar(ServerPlayerEntity player, String message) {
        var pack = new OverlayMessageS2CPacket(new LiteralText(message));
        player.networkHandler.sendPacket(pack);
    }
}
