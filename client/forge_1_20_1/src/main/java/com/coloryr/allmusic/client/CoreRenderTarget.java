package com.coloryr.allmusic.client;

import com.coloryr.allmusic.client.core.AllMusicHud;
import com.coloryr.allmusic.client.core.Point2f;
import com.coloryr.allmusic.client.core.render.TextFrameBuffer;
import com.coloryr.allmusic.codec.HudPosType;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

public class CoreRenderTarget extends TextFrameBuffer {
    private static final RenderBuffers renderBuffers = new RenderBuffers();

    private final RenderTarget target;
    private Matrix4f matrix4f;

    public CoreRenderTarget() {
        target = new TextureTarget(800, 200, false, false);
        target.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void resize(int width, int height) {
        Window window = Minecraft.getInstance().getWindow();

        nowWidth = (int) (width * window.getGuiScale());
        nowHeight = (int) (height * window.getGuiScale());

        if (nowWidth > target.width || nowHeight > target.height) {
            target.resize(nowWidth, nowHeight, false);
            matrix4f = new Matrix4f().setOrtho(0.0F, (float) (target.width / window.getGuiScale()),
                    (float) (target.height / window.getGuiScale()), 0.0F, 1000.0F, 21000.0F);
        } else if (matrix4f == null) {
            matrix4f = new Matrix4f().setOrtho(0.0F, (float) (target.width / window.getGuiScale()),
                    (float) (target.height / window.getGuiScale()), 0.0F, 1000.0F, 21000.0F);
        }
    }

    @Override
    public void use() {
        isDraw = true;

        clear();
        target.clear(false);
        target.bindWrite(true);

        RenderSystem.backupProjectionMatrix();
        if (matrix4f != null) {
            RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.ORTHOGRAPHIC_Z);
        }
    }

    @Override
    public void unUse() {
        isDraw = false;

        RenderSystem.restoreProjectionMatrix();

        RenderTarget main = Minecraft.getInstance().getMainRenderTarget();
        main.bindWrite(true);
    }

    @Override
    public void drawText(String text, int y, int color, boolean shadow) {
        Window window = Minecraft.getInstance().getWindow();

        var font = Minecraft.getInstance().font;
        Component component = MiniMessage.parse(text);
        int width = font.drawInBatch(component, 0, y, color, shadow, new Matrix4f(), renderBuffers.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
        RenderSystem.disableDepthTest();
        renderBuffers.bufferSource().endBatch();
        RenderSystem.enableDepthTest();

        TextItem item = new TextItem(width, font.lineHeight + (shadow ? 1 : 0), y, (float) window.getGuiScale());
        texts.add(item);
    }

    /**
     * 渲染贴图的一部分到屏幕指定位置
     *
     * @param alpha  透明度
     * @param x      屏幕X坐标（左上角）
     * @param y      屏幕Y坐标（左上角）
     * @param width  需要渲染的宽度
     * @param height 需要渲染的高度
     * @param texX   贴图左上角X坐标
     * @param texY   贴图左上角Y坐标
     * @param scale  贴图缩放
     */
    private void draw(float alpha, float x, float y, float width, float height, float texX, float texY, float scale) {
        RenderSystem.setShaderTexture(0, target.getColorTextureId());
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.depthFunc(GL30.GL_ALWAYS);

        float w = (width / 2);
        float h = (height / 2);

        Matrix4f matrix = new Matrix4f().translation(x + w, y + h, 0);

        float x0 = -w;
        float x1 = w;
        float y0 = -h;
        float y1 = h;
        float z = 0;

        // 计算贴图区域UV
        float u0 = texX * scale / target.width;
        float v0 = 1 - (texY * scale / target.height);
        float u1 = (texX + width) * scale / target.width;
        float v1 = 1 - ((texY + height) * scale / target.height);

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferBuilder.vertex(matrix, x0, y1, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u0, v1).endVertex();
        bufferBuilder.vertex(matrix, x1, y1, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u1, v1).endVertex();
        bufferBuilder.vertex(matrix, x1, y0, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u1, v0).endVertex();
        bufferBuilder.vertex(matrix, x0, y0, z).color(1.0f, 1.0f, 1.0f, alpha).uv(u0, v0).endVertex();

        BufferUploader.drawWithShader(bufferBuilder.end());

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);

        target.unbindRead();
    }

    public void drawLoop(float alpha, float x, float y,
                         float texX, float texY,
                         float textWidth, float textHeight,
                         int maxWidth, float offsetX, float scale) {

        // 如果宽度不大于最大宽度，直接全部渲染
        if (maxWidth == -1 || textWidth <= maxWidth) {
            draw(alpha, x, y, textWidth, textHeight, texX, texY, scale);
            return;
        }

        if (textWidth - offsetX < maxWidth) {
            float nowWith = textWidth - offsetX;
            draw(alpha, x, y, nowWith, textHeight, offsetX, texY, scale);
            draw(alpha, x + nowWith, y, maxWidth - nowWith, textHeight, 0, texY, scale);
        } else {
            draw(alpha, x, y, maxWidth, textHeight, offsetX, texY, scale);
        }
    }

    /**
     * 居中百分比显示（根据百分比选择显示贴图的中间部分）
     *
     * @param alpha     透明度
     * @param startX    起始X坐标（屏幕左上角）
     * @param startY    起始Y坐标（屏幕左上角）
     * @param texX      贴图左上角X
     * @param texY      贴图左上角Y
     * @param texWidth  贴图总宽度
     * @param texHeight 贴图高度
     * @param maxWidth  最大渲染宽度
     * @param percent   百分比（0.0-1.0），0%显示左边，100%显示右边
     */
    public void drawByPercent(float alpha, float startX, float startY,
                              float texX, float texY,
                              float texWidth, float texHeight,
                              int maxWidth, float percent, float scale) {

        // 限制百分比范围
        percent = Math.min(1.0f, Math.max(0.0f, percent));

        // 如果贴图宽度小于等于最大宽度，直接全部显示
        if (texWidth <= maxWidth) {
            draw(alpha, startX, startY, (int) (texWidth * percent), texHeight, texX, texY, scale);
            return;
        }

        // 计算贴图的起始位置（根据百分比）
        float maxOffset = texWidth - maxWidth;
        int texOffset = (int) (maxOffset * percent);

        // 渲染
        draw(alpha, startX, startY, maxWidth, texHeight, texX + texOffset, texY, scale);
    }

    /**
     * 正向百分比显示（从左开始显示，右边空白）
     *
     * @param alpha     透明度
     * @param startX    起始X坐标（屏幕左上角）
     * @param startY    起始Y坐标（屏幕左上角）
     * @param texX      贴图左上角X
     * @param texY      贴图左上角Y
     * @param texWidth  贴图总宽度
     * @param texHeight 贴图高度
     * @param maxWidth  最大渲染宽度
     * @param percent   百分比（0.0-1.0），表示显示的内容比例，例如0.4表示显示前面40%的内容
     */
    public void drawByForwardPercent(float alpha, int startX, int startY,
                                     int texX, int texY,
                                     int texWidth, int texHeight,
                                     int maxWidth, float percent, float scale) {

        // 限制百分比范围
        percent = Math.min(1.0f, Math.max(0.0f, percent));

        // 如果要显示的内容为0，直接返回
        if (percent <= 0) {
            return;
        }

        // 计算要显示的内容宽度
        int contentWidth = (int) (texWidth * percent);

        // 实际渲染宽度
        int renderWidth = Math.min(contentWidth, maxWidth);

        // 屏幕偏移为0（靠左显示）
        int screenOffset = 0;

        // 贴图偏移为0（从左边开始）
        int texOffset = 0;

        // 渲染
        draw(alpha, startX + screenOffset, startY,
                renderWidth, texHeight,
                texX + texOffset, texY, scale);
    }

    /**
     * 反向百分比显示（从右开始显示，左边空白）
     *
     * @param alpha     透明度
     * @param startX    起始X坐标（屏幕左上角）
     * @param startY    起始Y坐标（屏幕左上角）
     * @param texX      贴图左上角X
     * @param texY      贴图左上角Y
     * @param texWidth  贴图总宽度
     * @param texHeight 贴图高度
     * @param maxWidth  最大渲染宽度
     * @param percent   百分比（0.0-1.0），表示显示的内容比例，例如0.4表示显示后面40%的内容
     */
    public void drawByReversePercent(float alpha, int startX, int startY,
                                     int texX, int texY,
                                     int texWidth, int texHeight,
                                     int maxWidth, float percent, float scale) {
        // 限制百分比范围
        percent = Math.min(1.0f, Math.max(0.0f, percent));

        // 如果要显示的内容为0，直接返回
        if (percent <= 0) {
            return;
        }

        // 计算要显示的内容宽度
        int contentWidth = (int) (texWidth * percent);

        // 实际渲染宽度
        int renderWidth = Math.min(contentWidth, maxWidth);

        // 屏幕偏移（靠右显示）
        int screenOffset = maxWidth - renderWidth;

        // 贴图偏移（显示后面部分）
        int texOffset = texWidth - contentWidth;

        // 渲染
        draw(alpha, startX + screenOffset, startY,
                renderWidth, texHeight,
                texX + texOffset, texY, scale);
    }

    @Override
    public void draw(float alpha, int x, int y, int maxWidth, HudPosType dir) {
        for (var item : texts) {
            Point2f point = AllMusicHud.getPos(Math.min(maxWidth, item.textWidth), item.textHeight, x, y, dir);

            drawLoop(alpha, point.x, point.y + item.y, 0, item.y, item.textWidth, item.textHeight, maxWidth, offsetX % item.textWidth, item.scale);
        }
    }

    @Override
    public void drawLine(float x, float y, float alpha, int line) {
        if (line >= texts.size()) {
            return;
        }
        TextItem item = texts.get(line);
        draw(alpha, x, y, item.textWidth, item.textHeight, 0, item.y, item.scale);
    }

    @Override
    public Point2f getLine(int line) {
        if (line >= texts.size()) {
            return new Point2f(0, 0);
        }
        TextItem item = texts.get(line);
        return new Point2f(item.textWidth, item.textHeight);
    }


    @Override
    public void drawWithState(float alpha, int x, int y, int maxWidth, float state, HudPosType dir) {
        for (var item : texts) {
            Point2f point = AllMusicHud.getPos(Math.min(maxWidth, item.textWidth), item.textHeight, x, y, dir);

            drawByPercent(alpha, point.x, point.y + item.y, 0, item.y, item.textWidth, item.textHeight, maxWidth, state, item.scale);
        }

        target.unbindRead();
    }
}
