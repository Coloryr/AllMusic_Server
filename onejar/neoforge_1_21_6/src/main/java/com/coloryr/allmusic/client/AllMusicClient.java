package com.coloryr.allmusic.client;

import com.coloryr.allmusic.client.core.AllMusicBridge;
import com.coloryr.allmusic.client.core.AllMusicCore;
import com.coloryr.allmusic.server.AllMusicNeoForge;
import com.coloryr.allmusic.server.PackData;
import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundSourceEvent;
import net.neoforged.neoforge.client.event.sound.PlayStreamingSourceEvent;
import net.neoforged.neoforge.client.event.sound.SoundEngineLoadEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import org.joml.Matrix3x2fStack;

import java.nio.ByteBuffer;

@Mod(value = AllMusicNeoForge.MODID, dist = Dist.CLIENT)
public class AllMusicClient implements AllMusicBridge {
    private static GuiGraphics gui;

    public static class Tex extends AbstractTexture {
        public Tex(GpuTexture tex, GpuTextureView view) {
            this.texture = tex;
            this.textureView = view;
        }
    }

    public AllMusicClient(IEventBus modEventBus) {
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::setup1);
        modEventBus.addListener(this::onLoad);

        NeoForge.EVENT_BUS.register(this);
    }

    public void sendMessage(String data) {
        data = "[AllMusic Client]" + data;
        AllMusicNeoForge.LOGGER.warn(data);
        String finalData = data;
        Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().player == null)
                return;
            Minecraft.getInstance().player.displayClientMessage(Component.literal(finalData), false);
        });
    }

    private void setup(final FMLClientSetupEvent event) {
        event.enqueueWork(AllMusicCore::glInit);
    }

    public static void decode(PackData pack) {
        try {
            AllMusicCore.packDo(AllMusicCore.types[pack.cmd().ordinal()], pack.data(), pack.data1());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopPlayMusic() {
        Minecraft.getInstance().getSoundManager().stop(null, SoundSource.MUSIC);
        Minecraft.getInstance().getSoundManager().stop(null, SoundSource.RECORDS);
    }

    private void setup1(final FMLCommonSetupEvent event) {
        AllMusicCore.init(FMLPaths.CONFIGDIR.get(), this);
    }

    public int getScreenWidth() {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth();
    }

    public int getScreenHeight() {
        return Minecraft.getInstance().getWindow().getGuiScaledHeight();
    }

    public int getTextWidth(String item) {
        return Minecraft.getInstance().font.width(item);
    }

    public int getFontHeight() {
        return Minecraft.getInstance().font.lineHeight;
    }

    public void onLoad(final SoundEngineLoadEvent e) {
        AllMusicCore.reload();
    }

    @SubscribeEvent
    public void onSound(final PlaySoundSourceEvent e) {
        if (!AllMusicCore.isPlay()) return;
        SoundSource data = e.getSound().getSource();
        switch (data) {
            case MUSIC, RECORDS -> e.getChannel().stop();
        }
    }

    @SubscribeEvent
    public void onSound(final PlayStreamingSourceEvent e) {
        if (!AllMusicCore.isPlay()) return;
        SoundSource data = e.getSound().getSource();
        switch (data) {
            case MUSIC, RECORDS -> e.getChannel().stop();
        }
    }

    @SubscribeEvent
    public void onServerQuit(final ClientPlayerNetworkEvent.LoggingOut e) {
        AllMusicCore.onServerQuit();
    }

    public float getVolume() {
        return Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.RECORDS);
    }

    public void drawPic(Object tex, int size, int x, int y, int ang) {
        Matrix3x2fStack stack = gui.pose();
        Matrix3x2fStack matrix = stack.pushMatrix();

        int a = size / 2;

        if (ang > 0) {
            matrix.translation(x + a, y + a);
            matrix.pushMatrix().rotate((float) Math.toRadians(ang));
        } else {
            matrix.translation(x + a, y + a);
        }

        gui.blit(RenderPipelines.GUI_TEXTURED, AllMusicNeoForge.channel, -a, -a, 0, 0, size, size, size, size, size, size);
        stack.popMatrix();
        if (ang > 0) {
            stack.popMatrix();
        }
    }

    public void drawText(String item, int x, int y, int color, boolean shadow) {
        var hud = Minecraft.getInstance().font;
        gui.drawString(hud, item, x, y, color, shadow);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGuiLayerEvent.Post e) {
        if (e.getName().equals(VanillaGuiLayers.CAMERA_OVERLAYS)) {
            gui = e.getGuiGraphics();
            AllMusicCore.hudUpdate();
        }
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        AllMusicCore.tick();
    }

    @Override
    public Object genTexture(int size) {
        var device = RenderSystem.getDevice();
        var tex = device.createTexture("allmusic:gui_textured", 5, TextureFormat.RGBA8, size, size, 1, 1);
        tex.setTextureFilter(FilterMode.NEAREST, false);

        var view = device.createTextureView(tex);

        Tex tex1 = new Tex(tex, view);

        Minecraft.getInstance().getTextureManager().register(AllMusicNeoForge.channel, tex1);

        return tex;
    }

    @Override
    public void updateTexture(Object tex, int size, ByteBuffer byteBuffer) {
        if (tex instanceof GlTexture tex1) {
            AllMusicCore.updateGLTexture(tex1.glId(), size, byteBuffer);
        }
    }
}
