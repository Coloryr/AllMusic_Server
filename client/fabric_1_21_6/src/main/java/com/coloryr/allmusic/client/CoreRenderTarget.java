package com.coloryr.allmusic.client;

import com.coloryr.allmusic.client.core.AllMusicHud;
import com.coloryr.allmusic.client.core.Point2f;
import com.coloryr.allmusic.client.core.render.TextFrameBuffer;
import com.coloryr.allmusic.codec.HudPosType;
import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GlyphEffectRenderState;
import net.minecraft.client.gui.render.state.GlyphRenderState;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.network.chat.Component;
import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Supplier;

public class CoreRenderTarget extends TextFrameBuffer {
    private final RenderTarget target;
    private final String name;

    private final GuiRenderer renderer;
    private final FogRenderer fogRenderer = new FogRenderer();
    private final GuiRenderState renderState = new GuiRenderState();

    public CoreRenderTarget(String name) {
        this.name = name;
        target = new TextureTarget(name, 800, 200, false);
        renderer = new GuiRenderer(renderState);
    }

    @Override
    public void resize(int width, int height) {
        Window window = Minecraft.getInstance().getWindow();

        nowWidth = width * window.getGuiScale();
        nowHeight = height * window.getGuiScale();

        if (nowWidth > target.width || nowHeight > target.height) {
            target.resize(nowWidth, nowHeight);
        }
    }

    @Override
    public void use() {
        isDraw = true;

        clear();
        RenderSystem.getDevice().createCommandEncoder().clearColorTexture(target.getColorTexture(), 0);
    }

    @Override
    public void unUse() {
        isDraw = false;
    }

    @Override
    public void drawText(String text, int y, int color, boolean shadow) {
        Window window = Minecraft.getInstance().getWindow();

        var font = Minecraft.getInstance().font;
        Component component = MiniMessage.parse(text);
        int width = font.width(component.getVisualOrderText());

        GuiGraphics graphics = new GuiGraphics(Minecraft.getInstance(), renderState);
        color = color | 0xFF000000;
        graphics.drawString(font, component, 0, y, color, shadow);

        renderer.render(fogRenderer.getBuffer(FogRenderer.FogMode.NONE), this);

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
        float w = width / 2;
        float h = height / 2;

        Matrix3x2f matrix = new Matrix3x2f().translation(x + w, y + h);

        float x0 = -w;
        float x1 = w;
        float y0 = -h;
        float y1 = h;

        // 计算贴图区域UV
        float u0 = texX * scale / target.width;
        float v0 = 1 - (texY * scale / target.height);
        float u1 = (texX + width) * scale / target.width;
        float v1 = 1 - ((texY + height) * scale / target.height);

        int color = 0xFFFFFF00 + (int) (255 * alpha);

        AllMusicClient.context.guiRenderState.submitGuiElement(new FloatRenderState(RenderPipelines.GUI_TEXTURED,
                TextureSetup.singleTexture(target.getColorTextureView()), matrix, x0, y0, x1, y1, u0, u1, v0, v1, color, AllMusicClient.context.scissorStack.peek()));
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
        percent = Math.clamp(percent, 0.0f, 1.0f);

        // 如果贴图宽度小于等于最大宽度，直接全部显示
        if (texWidth <= maxWidth) {
            draw(alpha, startX, startY, texWidth * percent, texHeight, texX, texY, scale);
            return;
        }

        // 计算贴图的起始位置（根据百分比）
        float maxOffset = texWidth - maxWidth;
        float texOffset = maxOffset * percent;

        // 渲染
        draw(alpha, startX, startY, maxWidth, texHeight, texX + texOffset, texY, scale);
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
    }

    public static class GuiRenderer implements AutoCloseable {
        private static final Comparator<ScreenRectangle> SCISSOR_COMPARATOR = Comparator.nullsFirst(
                Comparator.comparing(ScreenRectangle::top)
                        .thenComparing(ScreenRectangle::bottom)
                        .thenComparing(ScreenRectangle::left)
                        .thenComparing(ScreenRectangle::right)
        );
        private static final Comparator<TextureSetup> TEXTURE_COMPARATOR = Comparator.nullsFirst(Comparator.comparing(TextureSetup::getSortKey));
        private static final Comparator<GuiElementRenderState> ELEMENT_SORT_COMPARATOR = Comparator.comparing(GuiElementRenderState::scissorArea, SCISSOR_COMPARATOR)
                .thenComparing(GuiElementRenderState::pipeline, Comparator.comparing(RenderPipeline::getSortKey))
                .thenComparing(GuiElementRenderState::textureSetup, TEXTURE_COMPARATOR);
        private final GuiRenderState renderState;
        private final List<Draw> draws = new ArrayList<>();
        private final List<MeshToDraw> meshesToDraw = new ArrayList<>();
        private final ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(786432);
        private final Map<VertexFormat, MappableRingBuffer> vertexBuffers = new Object2ObjectOpenHashMap<>();
        private final CachedOrthoProjectionMatrixBuffer guiProjectionMatrixBuffer = new CachedOrthoProjectionMatrixBuffer("gui", 1000.0F, 11000.0F, true);
        private ScreenRectangle previousScissorArea;
        private RenderPipeline previousPipeline;
        private TextureSetup previousTextureSetup;
        private BufferBuilder bufferBuilder;

        public GuiRenderer(GuiRenderState state) {
            renderState = state;
        }

        public void render(GpuBufferSlice gpu, CoreRenderTarget target) {
            renderState.forEachText(state -> {
                var matrix3x2f = state.pose;
                var screenrectangle = state.scissor;
                state.ensurePrepared().visit(new Font.GlyphVisitor() {
                    @Override
                    public void acceptGlyph(BakedGlyph.GlyphInstance glyph) {
                        if (glyph.glyph().textureView() != null) {
                            renderState.submitGlyphToCurrentLayer(new GlyphRenderState(matrix3x2f, glyph, screenrectangle));
                        }
                    }

                    @Override
                    public void acceptEffect(BakedGlyph glyph, BakedGlyph.Effect effect) {
                        if (glyph.textureView() != null) {
                            renderState.submitGlyphToCurrentLayer(new GlyphEffectRenderState(matrix3x2f, glyph, effect, screenrectangle));
                        }
                    }
                });
            });

            renderState.sortElements(ELEMENT_SORT_COMPARATOR);

            previousScissorArea = null;
            previousPipeline = null;
            previousTextureSetup = null;
            bufferBuilder = null;
            renderState.forEachElement(this::addElementToMesh, GuiRenderState.TraverseRange.BEFORE_BLUR);
            if (bufferBuilder != null) {
                recordMesh(bufferBuilder, previousPipeline, previousTextureSetup, previousScissorArea);
            }

            recordDraws();

            if (!draws.isEmpty()) {
                Minecraft minecraft = Minecraft.getInstance();
                Window window = minecraft.getWindow();
                RenderSystem.setProjectionMatrix(
                        guiProjectionMatrixBuffer.getBuffer((float) target.target.width / window.getGuiScale(),
                                (float) target.target.height / window.getGuiScale()),
                        ProjectionType.ORTHOGRAPHIC
                );
                var rendertarget = target.target;
                int i = 0;

                for (Draw guirenderer$draw : draws) {
                    if (guirenderer$draw.indexCount > i) {
                        i = guirenderer$draw.indexCount;
                    }
                }

                var buffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
                var gpubuffer = buffer.getBuffer(i);
                var index = buffer.type();
                var gpubufferslice = RenderSystem.getDynamicUniforms().writeTransform(new Matrix4f().setTranslation(0.0F, 0.0F, -11000.0F),
                        new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), new Vector3f(), new Matrix4f(), 0.0F);
                if (!meshesToDraw.isEmpty()) {
                    executeDrawRange(() -> "before blur", rendertarget, gpu, gpubufferslice, gpubuffer,
                            index, Math.min(meshesToDraw.size(), draws.size()), target);
                }
            }

            for (MappableRingBuffer mappableringbuffer : vertexBuffers.values()) {
                mappableringbuffer.rotate();
            }

            draws.clear();
            meshesToDraw.clear();
            renderState.reset();
        }

        private void executeDrawRange(Supplier<String> name, RenderTarget target, GpuBufferSlice buffer1, GpuBufferSlice buffer2,
                                      GpuBuffer buffer3, VertexFormat.IndexType index, int count, CoreRenderTarget target1) {
            try (RenderPass renderpass = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .createRenderPass(name, target.getColorTextureView(), OptionalInt.empty(),
                            target.useDepth ? target.getDepthTextureView() : null, OptionalDouble.empty())) {
                RenderSystem.bindDefaultUniforms(renderpass);
                renderpass.setUniform("Fog", buffer1);
                renderpass.setUniform("DynamicTransforms", buffer2);

                for (int i = 0; i < count; i++) {
                    var item = draws.get(i);
                    executeDraw(item, renderpass, target1, buffer3, index);
                }
            }
        }

        private void addElementToMesh(GuiElementRenderState state, int range) {
            var pipeline = state.pipeline();
            var setup = state.textureSetup();
            var screen = state.scissorArea();
            if (pipeline != previousPipeline || scissorChanged(screen, previousScissorArea) || !setup.equals(previousTextureSetup)) {
                if (bufferBuilder != null) {
                    recordMesh(bufferBuilder, previousPipeline, previousTextureSetup, previousScissorArea);
                }

                bufferBuilder = new BufferBuilder(byteBufferBuilder, pipeline.getVertexFormatMode(), pipeline.getVertexFormat());
                previousPipeline = pipeline;
                previousTextureSetup = setup;
                previousScissorArea = screen;
            }

            state.buildVertices(bufferBuilder, 0.0F + range * 0.01F);
        }

        private void recordMesh(BufferBuilder builder, RenderPipeline pipeline, TextureSetup texture, ScreenRectangle rectangle) {
            var meshdata = builder.buildOrThrow();
            meshesToDraw.add(new MeshToDraw(meshdata, pipeline, texture, rectangle));
        }

        private void recordDraws() {
            var map = new Object2IntOpenHashMap<VertexFormat>();

            for (MeshToDraw item : meshesToDraw) {
                var state = item.mesh.drawState();
                var vertexformat = state.format();
                if (!map.containsKey(vertexformat)) {
                    map.put(vertexformat, 0);
                }

                map.put(vertexformat, map.getInt(vertexformat) + state.vertexCount() * vertexformat.getVertexSize());
            }

            for (var entry : map.object2IntEntrySet()) {
                var vertexformat = entry.getKey();
                int i = entry.getIntValue();
                var buffer = vertexBuffers.get(vertexformat);
                if (buffer == null || buffer.size() < i) {
                    if (buffer != null) {
                        buffer.close();
                    }

                    vertexBuffers.put(vertexformat, new MappableRingBuffer(() -> "vertex buffer for " + vertexformat, 34, i));
                }
            }

            var commandencoder = RenderSystem.getDevice().createCommandEncoder();
            map = new Object2IntOpenHashMap<>();

            for (MeshToDraw item : meshesToDraw) {
                var meshdata = item.mesh;
                var state = meshdata.drawState();
                var vertexformat = state.format();
                var mappableringbuffer = vertexBuffers.get(vertexformat);
                if (!map.containsKey(vertexformat)) {
                    map.put(vertexformat, 0);
                }

                ByteBuffer bytebuffer = meshdata.vertexBuffer();
                int i = bytebuffer.remaining();
                int j = map.getInt(vertexformat);

                try (var item1 = commandencoder.mapBuffer(mappableringbuffer.currentBuffer().slice(j, i), false, true)) {
                    MemoryUtil.memCopy(bytebuffer, item1.data());
                }

                map.put(vertexformat, j + i);
                draws.add(new Draw(mappableringbuffer.currentBuffer(), j / vertexformat.getVertexSize(), state.mode(),
                        state.indexCount(), item.pipeline, item.textureSetup, item.scissorArea));
                item.close();
            }
        }

        private void executeDraw(Draw draw, RenderPass pass, CoreRenderTarget target, GpuBuffer buffer, VertexFormat.IndexType index) {
            var renderpipeline = draw.pipeline();
            pass.setPipeline(renderpipeline);
            pass.setVertexBuffer(0, draw.vertexBuffer);
            var area = draw.scissorArea();
            if (area != null) {
                Window window = Minecraft.getInstance().getWindow();

                int i = target.target.height;
                int j = window.getGuiScale();
                double d0 = area.left() * j;
                double d1 = i - area.bottom() * j;
                double d2 = area.width() * j;
                double d3 = area.height() * j;
                pass.enableScissor((int) d0, (int) d1, Math.max(0, (int) d2), Math.max(0, (int) d3));
            } else {
                pass.disableScissor();
            }

            if (draw.textureSetup.texure0() != null) {
                pass.bindSampler("Sampler0", draw.textureSetup.texure0());
            }

            if (draw.textureSetup.texure1() != null) {
                pass.bindSampler("Sampler1", draw.textureSetup.texure1());
            }

            if (draw.textureSetup.texure2() != null) {
                pass.bindSampler("Sampler2", draw.textureSetup.texure2());
            }

            pass.setIndexBuffer(buffer, index);
            pass.drawIndexed(draw.baseVertex, 0, draw.indexCount, 1);
        }

        private boolean scissorChanged(ScreenRectangle screen1, ScreenRectangle screen2) {
            if (screen1 == screen2) {
                return false;
            } else {
                return screen1 == null || !screen1.equals(screen2);
            }
        }

        @Override
        public void close() {
            byteBufferBuilder.close();

            guiProjectionMatrixBuffer.close();

            for (MappableRingBuffer mappableringbuffer : vertexBuffers.values()) {
                mappableringbuffer.close();
            }
        }

        record Draw(GpuBuffer vertexBuffer, int baseVertex, VertexFormat.Mode mode, int indexCount,
                    RenderPipeline pipeline, TextureSetup textureSetup, ScreenRectangle scissorArea) {
        }

        record MeshToDraw(MeshData mesh, RenderPipeline pipeline, TextureSetup textureSetup,
                          ScreenRectangle scissorArea) implements AutoCloseable {
            @Override
            public void close() {
                mesh.close();
            }
        }
    }
}
