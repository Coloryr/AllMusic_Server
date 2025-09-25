package com.coloryr.allmusic.client;

import com.coloryr.allmusic.client.core.AllMusicBridge;
import com.coloryr.allmusic.client.core.AllMusicCore;
import com.coloryr.allmusic.server.AllMusicFabric;
import com.coloryr.allmusic.server.PackPayload;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.GlTexture;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix3x2fStack;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class AllMusic implements ClientModInitializer, AllMusicBridge {
    private static DrawContext context;

    public static class Tex extends AbstractTexture {
        public Tex(GpuTexture tex, GpuTextureView view) {
            this.glTexture = tex;
            this.glTextureView = view;
        }
    }

    public Object genTexture(int size) {
        var device = RenderSystem.getDevice();
        var tex = device.createTexture("allmusic:gui_textured", 5, TextureFormat.RGBA8, size, size, 1, 1);
        tex.setTextureFilter(FilterMode.NEAREST, false);

        var view = device.createTextureView(tex);

        Tex tex1 = new Tex(tex, view);

        MinecraftClient.getInstance().getTextureManager().registerTexture(AllMusicFabric.ID, tex1);

        return tex;
    }

    public void updateTexture(Object tex, int size, ByteBuffer byteBuffer) {
        if (tex instanceof GlTexture tex1) {
            AllMusicCore.updateGLTexture(tex1.getGlId(), size, byteBuffer);
        }
    }

    public int getScreenWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    public int getScreenHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight();
    }

    public int getTextWidth(String item) {
        return MinecraftClient.getInstance().textRenderer.getWidth(item);
    }

    public int getFontHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }

    public void drawText(String item, int x, int y, int color, boolean shadow) {
        var hud = MinecraftClient.getInstance().textRenderer;
        context.drawText(hud, item, x, y, color, shadow);
    }

    public void drawPic(Object texture, int size, int x, int y, int ang) {
        Matrix3x2fStack stack = context.getMatrices();
        Matrix3x2fStack matrix = stack.pushMatrix();

        int a = size / 2;

        if (ang > 0) {
            matrix.translation(x + a, y + a);
            matrix.pushMatrix().rotate((float) Math.toRadians(ang));
        } else {
            matrix.translation(x + a, y + a);
        }

        context.drawTexture(RenderPipelines.GUI_TEXTURED, AllMusicFabric.ID, -a, -a, 0, 0, size, size, size, size, size, size);
        stack.popMatrix();
        if (ang > 0) {
            stack.popMatrix();
        }
    }

    public void sendMessage(String data) {
        data = "[AllMusic Client]" + data;
        AllMusicFabric.LOGGER.warn(data);
        String finalData = data;
        MinecraftClient.getInstance().execute(() -> {
            if (MinecraftClient.getInstance().player == null)
                return;
            MinecraftClient.getInstance().player.sendMessage(Text.of(finalData), false);
        });
    }

    public float getVolume() {
        return MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.RECORDS);
    }

    @Override
    public void stopPlayMusic() {
        MinecraftClient.getInstance().getSoundManager().stopSounds(null, SoundCategory.MUSIC);
        MinecraftClient.getInstance().getSoundManager().stopSounds(null, SoundCategory.RECORDS);
    }

    public static void update(DrawContext draw) {
        context = draw;
        AllMusicCore.hudUpdate();
    }

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(PackPayload.ID, (pack, handler) -> {
            if (pack != null) {
                AllMusicCore.packDo(AllMusicCore.types[pack.type().ordinal()], pack.data(), pack.data1());
            }
        });

        AllMusicCore.init(FabricLoader.getInstance().getConfigDir(), this);
    }
}
