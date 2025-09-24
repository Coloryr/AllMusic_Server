package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.mixin.IGetCommandOutput;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;

public class FabricApi {

    public static void sendMessageRun(ServerCommandSource obj, String message, String end, String command) {
        IGetCommandOutput output = (IGetCommandOutput) obj;
        if (output.getSilent()) {
            return;
        }
        LiteralText send = new LiteralText(message);
        LiteralText endText = new LiteralText(end);
        endText.setStyle(endText.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
        send.append(endText);
        output.getOutput().sendSystemMessage(send, Util.NIL_UUID);
    }

    public static void sendMessageSuggest(ServerCommandSource obj, String message, String end, String command) {
        IGetCommandOutput output = (IGetCommandOutput) obj;
        if (output.getSilent()) {
            return;
        }
        LiteralText send = new LiteralText(message);
        LiteralText endText = new LiteralText(end);
        endText.setStyle(endText.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.append(endText);
        output.getOutput().sendSystemMessage(send, Util.NIL_UUID);
    }

    public static void sendBar(ServerPlayerEntity player, String message) {
        TitleS2CPacket pack = new TitleS2CPacket(TitleS2CPacket.Action.ACTIONBAR, new LiteralText(message));
        player.networkHandler.sendPacket(pack);
    }

    public static void sendMessageBqRun(String message, String end, String command) {
        LiteralText send = new LiteralText(message);
        LiteralText endText = new LiteralText(end);
        endText.setStyle(endText.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.append(endText);
        for (ServerPlayerEntity player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
            player.sendMessage(send, false);
        }
    }
}
