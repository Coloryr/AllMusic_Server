package com.coloryr.allmusic.client;

import com.coloryr.allmusic.client.core.AllMusicHud;
import com.coloryr.allmusic.client.core.Point2f;
import com.coloryr.allmusic.client.core.render.TextureRender;
import com.coloryr.allmusic.codec.HudPosType;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import java.io.IOException;

public class TexRender extends TextureRender {
    private final Tex sourceTexture;

    public TexRender(String texture) {
        super(texture);

        ResourceLocation location = new ResourceLocation(AllMusicClient.MODID, texture);

        sourceTexture = new Tex(location);

        try {
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            sourceTexture.load(resourceManager, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawPic(float x, float y, float alpha) {
        RenderSystem.setShaderTexture(0, sourceTexture.getId());
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.depthFunc(GL30.GL_ALWAYS);

        float w1 = (float) width / 2;
        float h1 = (float) height / 2;

        Matrix4f matrix = new Matrix4f().translation(x + w1, y + h1, 0);

        float x0 = -w1;
        float x1 = w1;
        float y0 = -h1;
        float y1 = h1;
        float z = 0;
        float u0 = 0;
        float u1 = 1;
        float v0 = 0;
        float v1 = 1;

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferBuilder.vertex(matrix, x0, y1, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u0, v1).endVertex();
        bufferBuilder.vertex(matrix, x1, y1, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u1, v1).endVertex();
        bufferBuilder.vertex(matrix, x1, y0, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u1, v0).endVertex();
        bufferBuilder.vertex(matrix, x0, y0, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u0, v0).endVertex();

        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    @Override
    public void drawPic(float x, float y, float width, float alpha) {
        RenderSystem.setShaderTexture(0, sourceTexture.getId());
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.depthFunc(GL30.GL_ALWAYS);

        float w1 = (float) (this.width / 2) * width;
        float h1 = (float) height / 2;

        Matrix4f matrix = new Matrix4f().translation(x + w1, y + h1, 0);

        float x0 = -w1;
        float x1 = w1;
        float y0 = -h1;
        float y1 = h1;
        float z = 0;
        float u0 = 0;
        float u1 = width;
        float v0 = 0;
        float v1 = 1;

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferBuilder.vertex(matrix, x0, y1, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u0, v1).endVertex();
        bufferBuilder.vertex(matrix, x1, y1, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u1, v1).endVertex();
        bufferBuilder.vertex(matrix, x1, y0, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u1, v0).endVertex();
        bufferBuilder.vertex(matrix, x0, y0, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u0, v0).endVertex();

        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    @Override
    public void drawPic(float x, float y, float width, float height, HudPosType dir, float alpha) {
        Point2f point = AllMusicHud.getPos(width, height, x, y, dir);

        RenderSystem.setShaderTexture(0, sourceTexture.getId());
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.depthFunc(GL30.GL_ALWAYS);

        float w1 = width / 2;
        float h1 = height / 2;

        Matrix4f matrix = new Matrix4f().translation(point.x + w1, point.y + h1, 0);

        float x0 = -w1;
        float x1 = w1;
        float y0 = -h1;
        float y1 = h1;
        float z = 0;
        float u0 = 0;
        float u1 = 1;
        float v0 = 0;
        float v1 = 1;

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferBuilder.vertex(matrix, x0, y1, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u0, v1).endVertex();
        bufferBuilder.vertex(matrix, x1, y1, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u1, v1).endVertex();
        bufferBuilder.vertex(matrix, x1, y0, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u1, v0).endVertex();
        bufferBuilder.vertex(matrix, x0, y0, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u0, v0).endVertex();

        BufferUploader.drawWithShader(bufferBuilder.end());

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
    }

    public static class Tex extends SimpleTexture {
        public Tex(ResourceLocation resourceLocation) {
            super(resourceLocation);
        }

        public void load(ResourceManager resourceManager, TextureRender source) throws IOException {
            SimpleTexture.TextureImage textureImage = this.getTextureImage(resourceManager);
            textureImage.throwIfError();
            TextureMetadataSection textureMetadataSection = textureImage.getTextureMetadata();
            boolean bl;
            boolean bl2;
            if (textureMetadataSection != null) {
                bl = textureMetadataSection.isBlur();
                bl2 = textureMetadataSection.isClamp();
            } else {
                bl = false;
                bl2 = false;
            }

            NativeImage nativeImage = textureImage.getImage();
            source.width = nativeImage.getWidth();
            source.height = nativeImage.getHeight();
            if (!RenderSystem.isOnRenderThreadOrInit()) {
                RenderSystem.recordRenderCall(() -> this.doLoad(nativeImage, bl, bl2));
            } else {
                this.doLoad(nativeImage, bl, bl2);
            }
        }

        private void doLoad(NativeImage nativeImage, boolean bl, boolean bl2) {
            TextureUtil.prepareImage(this.getId(), 0, nativeImage.getWidth(), nativeImage.getHeight());
            nativeImage.upload(0, 0, 0, 0, 0, nativeImage.getWidth(), nativeImage.getHeight(), bl, bl2, false, true);
        }
    }
}
