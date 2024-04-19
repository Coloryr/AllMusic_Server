package com.coloryr.allmusic.server.side.fabric;

import com.coloryr.allmusic.server.AllMusicFabric;
import com.coloryr.allmusic.server.core.AllMusic;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class FabricApi {

    public static void sendMessageRun(Object obj, String message, String end, String command) {
        CommandOutput sender = (CommandOutput) obj;
        MutableText send = Text.literal(message);
        var endText = Text.literal(end);
        endText.setStyle(endText.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
        send.append(endText);
        sender.sendMessage(send);
    }

    public static void sendMessageSuggest(Object obj, String message, String end, String command) {
        CommandOutput sender = (CommandOutput) obj;
        MutableText send = Text.literal(message);
        var endText = Text.literal(end);
        endText.setStyle(endText.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.append(endText);
        sender.sendMessage(send);
    }

    public static void sendBar(ServerPlayerEntity player, String message) {
        var pack = new OverlayMessageS2CPacket(Text.literal(message));
        player.networkHandler.sendPacket(pack);
    }

    public static void sendMessageBqRun(String message, String end, String command) {
        MutableText send = Text.literal(message);
        MutableText endText = Text.literal(end);
        endText.setStyle(endText.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.append(endText);
        for (var player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
            if (!AllMusic.getConfig().mutePlayer.contains(player.getName().getString())) {
                player.sendMessage(send, false);
            }
        }
    }
}
