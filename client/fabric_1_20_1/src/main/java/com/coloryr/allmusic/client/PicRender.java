package com.coloryr.allmusic.client;

import com.coloryr.allmusic.client.core.AllMusicHud;
import com.coloryr.allmusic.client.core.Point2f;
import com.coloryr.allmusic.client.core.render.PictureFrameBuffer;
import com.coloryr.allmusic.codec.HudPosType;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL30;

import java.io.ByteArrayInputStream;

public class PicRender extends PictureFrameBuffer {
    private final DynamicTexture sourceTexture;
    private final DynamicTexture rotateTexture;

    public PicRender(int size) {
        sourceTexture = new DynamicTexture(size, size, false);
        rotateTexture = new DynamicTexture(size, size, false);
    }

    @Override
    public void update(byte[] source, byte[] rotate) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(source);
            var image1 = NativeImage.read(stream);
            sourceTexture.setPixels(image1);
            sourceTexture.upload();

            stream = new ByteArrayInputStream(rotate);
            var image2 = NativeImage.read(stream);
            rotateTexture.setPixels(image2);
            rotateTexture.upload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(boolean rotate, int size, float x, float y, int ang, HudPosType dir, float alpha) {
        Point2f point = AllMusicHud.getPos(size, size, x, y, dir);

        RenderSystem.setShaderTexture(0, rotate ? rotateTexture.getId() : sourceTexture.getId());
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.depthFunc(GL30.GL_ALWAYS);

        Matrix4f matrix;

        int a = size / 2;

        if (ang > 0) {
            matrix = new Matrix4f().translationRotate(point.x + a, point.y + a, 0,
                    new Quaternionf().fromAxisAngleDeg(0, 0, 1, ang));
        } else {
            matrix = new Matrix4f().translation(point.x + a, point.y + a, 0);
        }

        int x0 = -a;
        int x1 = a;
        int y0 = -a;
        int y1 = a;
        int z = 0;
        int u0 = 0;
        float u1 = 1;
        float v0 = 0;
        float v1 = 1;

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferBuilder.vertex(matrix, (float) x0, (float) y1, (float) z).color(1.0f, 1.0f, 1.0f, alpha).uv(u0, v1).endVertex();
        bufferBuilder.vertex(matrix, (float) x1, (float) y1, (float) z).color(1.0f, 1.0f, 1.0f, alpha).uv(u1, v1).endVertex();
        bufferBuilder.vertex(matrix, (float) x1, (float) y0, (float) z).color(1.0f, 1.0f, 1.0f, alpha).uv(u1, v0).endVertex();
        bufferBuilder.vertex(matrix, (float) x0, (float) y0, (float) z).color(1.0f, 1.0f, 1.0f, alpha).uv(u0, v0).endVertex();

        BufferUploader.drawWithShader(bufferBuilder.end());
    }
}
