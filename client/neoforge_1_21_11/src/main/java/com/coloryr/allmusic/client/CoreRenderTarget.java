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
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
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
import org.jspecify.annotations.Nullable;
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
        var minecraft = Minecraft.getInstance();
        Window window = minecraft.getWindow();

        var font = Minecraft.getInstance().font;
        Component component = MiniMessage.parse(text);
        int width = font.width(component.getVisualOrderText());

        int i = (int) minecraft.mouseHandler.getScaledXPos(window);
        int j = (int) minecraft.mouseHandler.getScaledYPos(window);

        GuiGraphics graphics = new GuiGraphics(Minecraft.getInstance(), renderState, i, j);
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

        AllMusicClient.context.submitGuiElementRenderState(new FloatRenderState(RenderPipelines.GUI_TEXTURED,
                TextureSetup.singleTexture(target.getColorTextureView(), RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST)),
                matrix, x0, y0, x1, y1, u0, u1, v0, v1, color, AllMusicClient.context.peekScissorStack()));
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
        final GuiRenderState renderState;
        private final List<Draw> draws = new ArrayList<>();
        private final List<MeshToDraw> meshesToDraw = new ArrayList<>();
        private final ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(786432);
        private final Map<VertexFormat, MappableRingBuffer> vertexBuffers = new Object2ObjectOpenHashMap<>();
        private final CachedOrthoProjectionMatrixBuffer guiProjectionMatrixBuffer = new CachedOrthoProjectionMatrixBuffer("gui", 1000.0F, 11000.0F, true);
        private final CachedOrthoProjectionMatrixBuffer itemsProjectionMatrixBuffer = new CachedOrthoProjectionMatrixBuffer("items", -1000.0F, 1000.0F, true);
        private @Nullable ScreenRectangle previousScissorArea = null;
        private @Nullable RenderPipeline previousPipeline = null;
        private @Nullable TextureSetup previousTextureSetup = null;
        private @Nullable BufferBuilder bufferBuilder = null;

        public GuiRenderer(GuiRenderState state) {
            this.renderState = state;
        }

        public void render(GpuBufferSlice bufferSlice, CoreRenderTarget target) {
            this.renderState.forEachText(state -> {
                var matrix3x2fc = state.pose;
                var screenrectangle = state.scissor;
                state.ensurePrepared().visit(new Font.GlyphVisitor() {
                    @Override
                    public void acceptGlyph(TextRenderable.Styled styled) {
                        this.accept(styled);
                    }

                    @Override
                    public void acceptEffect(TextRenderable textRenderable) {
                        this.accept(textRenderable);
                    }

                    private void accept(TextRenderable textRenderable) {
                        renderState.submitGlyphToCurrentLayer(new GlyphRenderState(matrix3x2fc, textRenderable, screenrectangle));
                    }
                });
            });
            this.renderState.sortElements(ELEMENT_SORT_COMPARATOR);

            this.previousScissorArea = null;
            this.previousPipeline = null;
            this.previousTextureSetup = null;
            this.bufferBuilder = null;
            this.renderState.forEachElement(this::addElementToMesh, GuiRenderState.TraverseRange.BEFORE_BLUR);
            if (this.bufferBuilder != null) {
                this.recordMesh(this.bufferBuilder, this.previousPipeline, this.previousTextureSetup, this.previousScissorArea);
            }

            var map = new Object2IntOpenHashMap<VertexFormat>();

            for (var draw : this.meshesToDraw) {
                var state = draw.mesh.drawState();
                var vertexformat = state.format();
                if (!map.containsKey(vertexformat)) {
                    map.put(vertexformat, 0);
                }

                map.put(vertexformat, map.getInt(vertexformat) + state.vertexCount() * vertexformat.getVertexSize());
            }

            for (var entry : map.object2IntEntrySet()) {
                var vertexformat = entry.getKey();
                int i = entry.getIntValue();
                var mappableringbuffer = this.vertexBuffers.get(vertexformat);
                if (mappableringbuffer == null || mappableringbuffer.size() < i) {
                    if (mappableringbuffer != null) {
                        mappableringbuffer.close();
                    }

                    this.vertexBuffers.put(vertexformat, new MappableRingBuffer(() -> "GUI vertex buffer for " + vertexformat, 34, i));
                }
            }

            var commandencoder = RenderSystem.getDevice().createCommandEncoder();
            map = new Object2IntOpenHashMap<>();

            for (MeshToDraw draw : this.meshesToDraw) {
                var meshdata = draw.mesh;
                var state = meshdata.drawState();
                var vertexformat = state.format();
                var mappableringbuffer = this.vertexBuffers.get(vertexformat);
                if (!map.containsKey(vertexformat)) {
                    map.put(vertexformat, 0);
                }

                ByteBuffer bytebuffer = meshdata.vertexBuffer();
                int i = bytebuffer.remaining();
                int j = map.getInt(vertexformat);

                try (GpuBuffer.MappedView view = commandencoder.mapBuffer(mappableringbuffer.currentBuffer().slice(j, i), false, true)) {
                    MemoryUtil.memCopy(bytebuffer, view.data());
                }

                map.put(vertexformat, j + i);
                this.draws.add(new Draw(mappableringbuffer.currentBuffer(), j / vertexformat.getVertexSize(), state.mode(),
                        state.indexCount(), draw.pipeline, draw.textureSetup, draw.scissorArea));
                draw.close();
            }

            this.draw(bufferSlice, target);

            for (var mappableringbuffer : this.vertexBuffers.values()) {
                mappableringbuffer.rotate();
            }

            this.draws.clear();
            this.meshesToDraw.clear();
            this.renderState.reset();
            if (SharedConstants.DEBUG_SHUFFLE_UI_RENDERING_ORDER) {
                RenderPipeline.updateSortKeySeed();
                TextureSetup.updateSortKeySeed();
            }
        }

        private void draw(GpuBufferSlice bufferSlice, CoreRenderTarget target) {
            if (!this.draws.isEmpty()) {
                var minecraft = Minecraft.getInstance();
                var window = minecraft.getWindow();
                RenderSystem.setProjectionMatrix(
                        this.guiProjectionMatrixBuffer.getBuffer((float) target.target.width / window.getGuiScale(), (float) target.target.height / window.getGuiScale()),
                        ProjectionType.ORTHOGRAPHIC
                );
                var rendertarget = target.target;
                int i = 0;

                for (var item : this.draws) {
                    if (item.indexCount > i) {
                        i = item.indexCount;
                    }
                }

                var buffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
                var gpubuffer = buffer.getBuffer(i);
                var indexType = buffer.type();
                var gpubufferslice = RenderSystem.getDynamicUniforms()
                        .writeTransform(new Matrix4f().setTranslation(0.0F, 0.0F, -11000.0F), new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), new Vector3f(), new Matrix4f());
                if (!this.meshesToDraw.isEmpty()) {
                    this.executeDrawRange(() -> "GUI before blur", rendertarget, bufferSlice, gpubufferslice, gpubuffer,
                            indexType, Math.min(this.meshesToDraw.size(), this.draws.size()));
                }
            }
        }

        private void executeDrawRange(Supplier<String> name, RenderTarget target, GpuBufferSlice bufferSlice, GpuBufferSlice bufferSlice1,
                                      GpuBuffer buffer, VertexFormat.IndexType indexType, int count) {
            try (var renderpass = RenderSystem.getDevice()
                    .createCommandEncoder()
                    .createRenderPass(name, target.getColorTextureView(), OptionalInt.empty(),
                            target.useDepth ? target.getDepthTextureView() : null, OptionalDouble.empty())) {
                RenderSystem.bindDefaultUniforms(renderpass);
                renderpass.setUniform("Fog", bufferSlice);
                renderpass.setUniform("DynamicTransforms", bufferSlice1);

                for (int i = 0; i < count; i++) {
                    Draw draw = this.draws.get(i);
                    this.executeDraw(draw, renderpass, buffer, indexType);
                }
            }
        }

        private void addElementToMesh(GuiElementRenderState state) {
            var pipeline = state.pipeline();
            var texturesetup = state.textureSetup();
            var screenrectangle = state.scissorArea();
            if (pipeline != this.previousPipeline
                    || this.scissorChanged(screenrectangle, this.previousScissorArea)
                    || !texturesetup.equals(this.previousTextureSetup)) {
                if (this.bufferBuilder != null) {
                    this.recordMesh(this.bufferBuilder, this.previousPipeline, this.previousTextureSetup, this.previousScissorArea);
                }

                this.bufferBuilder = new BufferBuilder(this.byteBufferBuilder, pipeline.getVertexFormatMode(), pipeline.getVertexFormat());
                this.previousPipeline = pipeline;
                this.previousTextureSetup = texturesetup;
                this.previousScissorArea = screenrectangle;
            }

            state.buildVertices(this.bufferBuilder);
        }

        private void recordMesh(BufferBuilder builder, RenderPipeline pipeline, TextureSetup textureSetup, @Nullable ScreenRectangle rectangle) {
            var meshdata = builder.build();
            if (meshdata != null) {
                this.meshesToDraw.add(new MeshToDraw(meshdata, pipeline, textureSetup, rectangle));
            }
        }

        private void executeDraw(Draw draw, RenderPass pass, GpuBuffer buffer, VertexFormat.IndexType index) {
            var renderpipeline = draw.pipeline();
            pass.setPipeline(renderpipeline);
            pass.setVertexBuffer(0, draw.vertexBuffer);
            var screenrectangle = draw.scissorArea();
            if (screenrectangle != null) {
                Window window = Minecraft.getInstance().getWindow();
                int i = window.getHeight();
                int j = window.getGuiScale();
                double d0 = screenrectangle.left() * j;
                double d1 = i - screenrectangle.bottom() * j;
                double d2 = screenrectangle.width() * j;
                double d3 = screenrectangle.height() * j;
                pass.enableScissor((int) d0, (int) d1, Math.max(0, (int) d2), Math.max(0, (int) d3));
            } else {
                pass.disableScissor();
            }

            if (draw.textureSetup.texure0() != null) {
                pass.bindTexture("Sampler0", draw.textureSetup.texure0(), draw.textureSetup.sampler0());
            }

            if (draw.textureSetup.texure1() != null) {
                pass.bindTexture("Sampler1", draw.textureSetup.texure1(), draw.textureSetup.sampler1());
            }

            if (draw.textureSetup.texure2() != null) {
                pass.bindTexture("Sampler2", draw.textureSetup.texure2(), draw.textureSetup.sampler2());
            }

            pass.setIndexBuffer(buffer, index);
            pass.drawIndexed(draw.baseVertex, 0, draw.indexCount, 1);
        }

        private boolean scissorChanged(@Nullable ScreenRectangle rectangle, @Nullable ScreenRectangle rectangle1) {
            if (rectangle == rectangle1) {
                return false;
            } else {
                return rectangle != null ? !rectangle.equals(rectangle1) : true;
            }
        }

        @Override
        public void close() {
            this.byteBufferBuilder.close();
            this.guiProjectionMatrixBuffer.close();
            this.itemsProjectionMatrixBuffer.close();

            for (MappableRingBuffer mappableringbuffer : this.vertexBuffers.values()) {
                mappableringbuffer.close();
            }
        }

        record Draw(GpuBuffer vertexBuffer, int baseVertex, VertexFormat.Mode mode, int indexCount,
                    RenderPipeline pipeline, TextureSetup textureSetup, @Nullable ScreenRectangle scissorArea) {
        }

        record MeshToDraw(MeshData mesh, RenderPipeline pipeline, TextureSetup textureSetup,
                          @Nullable ScreenRectangle scissorArea) implements AutoCloseable {
            @Override
            public void close() {
                this.mesh.close();
            }
        }
    }
}
