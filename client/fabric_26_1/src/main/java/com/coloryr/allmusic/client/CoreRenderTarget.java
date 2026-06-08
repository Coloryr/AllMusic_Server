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
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.state.WindowRenderState;
import net.minecraft.client.renderer.state.gui.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import org.joml.*;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

import java.lang.Math;
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

        GuiGraphicsExtractor graphics = new GuiGraphicsExtractor(Minecraft.getInstance(), renderState, i, j);
        color = color | 0xFF000000;
        graphics.text(font, component, 0, y, color, shadow);

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

        AllMusicClient.context.guiRenderState.addGuiElement(new FloatRenderState(RenderPipelines.GUI_TEXTURED,
                TextureSetup.singleTexture(target.getColorTextureView(), RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST)),
                matrix, x0, y0, x1, y1, u0, u1, v0, v1, color, AllMusicClient.context.scissorStack.peek()));
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
        private static final Comparator<ScreenRectangle> SCISSOR_COMPARATOR = Comparator.nullsFirst(Comparator.comparing(ScreenRectangle::top)
                .thenComparing(ScreenRectangle::bottom).thenComparing(ScreenRectangle::left).thenComparing(ScreenRectangle::right));
        private static final Comparator<TextureSetup> TEXTURE_COMPARATOR = Comparator.nullsFirst(Comparator.comparing(TextureSetup::getSortKey));
        private static final Comparator<GuiElementRenderState> ELEMENT_SORT_COMPARATOR = Comparator.comparing(GuiElementRenderState::scissorArea, SCISSOR_COMPARATOR)
                .thenComparing(GuiElementRenderState::pipeline, Comparator.comparing(RenderPipeline::getSortKey)).thenComparing(GuiElementRenderState::textureSetup, TEXTURE_COMPARATOR);
        private final GuiRenderState renderState;
        private final List<Draw> draws = new ArrayList<>();
        private final List<MeshToDraw> meshesToDraw = new ArrayList<>();
        private final ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(786432);
        private final Map<VertexFormat, MappableRingBuffer> vertexBuffers = new Object2ObjectOpenHashMap<>();
        private final Projection guiProjection = new Projection();
        private final ProjectionMatrixBuffer guiProjectionMatrixBuffer = new ProjectionMatrixBuffer("gui");
        private final CubeMap cubeMap = new CubeMap(Identifier.withDefaultNamespace("textures/gui/title/background/panorama"));
        private ScreenRectangle previousScissorArea = null;
        private RenderPipeline previousPipeline = null;
        private TextureSetup previousTextureSetup = null;
        private BufferBuilder bufferBuilder = null;

        public GuiRenderer(GuiRenderState renderState) {
            this.renderState = renderState;
        }

        public void render(GpuBufferSlice fogBuffer, CoreRenderTarget target) {
            var profiler = Profiler.get();
            if (this.renderState.panoramaRenderState != null) {
                this.cubeMap.render(10.0F, this.renderState.panoramaRenderState.spin());
            }

            profiler.push("allmusic text prepare");
            this.renderState.forEachText((text) -> {
                var pose = text.pose;
                var scissor = text.scissor;
                text.ensurePrepared().visit(new Font.GlyphVisitor() {
                    public void acceptGlyph(TextRenderable.Styled glyph) {
                        this.accept(glyph);
                    }

                    public void acceptEffect(TextRenderable effect) {
                        this.accept(effect);
                    }

                    private void accept(TextRenderable glyph) {
                        renderState.addGlyphToCurrentLayer(new GlyphRenderState(pose, glyph, scissor));
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
            this.ensureVertexBufferSizes();
            var commandEncoder = RenderSystem.getDevice().createCommandEncoder();
            var offsets = new Object2IntOpenHashMap<VertexFormat>();

            for (var meshToDraw : this.meshesToDraw) {
                var mesh = meshToDraw.mesh;
                var drawState = mesh.drawState();
                var format = drawState.format();
                var vertexBuffer = this.vertexBuffers.get(format);
                if (!offsets.containsKey(format)) {
                    offsets.put(format, 0);
                }

                var meshVertexBuffer = mesh.vertexBuffer();
                int meshBufferSize = meshVertexBuffer.remaining();
                int offset = offsets.getInt(format);

                try (var mappedView = commandEncoder.mapBuffer(vertexBuffer.currentBuffer().slice(offset, meshBufferSize), false, true)) {
                    MemoryUtil.memCopy(meshVertexBuffer, mappedView.data());
                }

                offsets.put(format, offset + meshBufferSize);
                this.draws.add(new Draw(vertexBuffer.currentBuffer(), offset / format.getVertexSize(),
                        drawState.mode(), drawState.indexCount(), meshToDraw.pipeline, meshToDraw.textureSetup, meshToDraw.scissorArea));
                meshToDraw.close();
            }
            profiler.popPush("allmusic text draw");
            this.draw(fogBuffer, target);
            profiler.popPush("allmusic text vertexBufferRotate");

            for (MappableRingBuffer buffer : this.vertexBuffers.values()) {
                buffer.rotate();
            }

            profiler.pop();
            this.draws.clear();
            this.meshesToDraw.clear();
            this.renderState.reset();
            if (SharedConstants.DEBUG_SHUFFLE_UI_RENDERING_ORDER) {
                RenderPipeline.updateSortKeySeed();
                TextureSetup.updateSortKeySeed();
            }
        }

        private void draw(GpuBufferSlice fogBuffer, CoreRenderTarget target) {
            if (!this.draws.isEmpty()) {
                var minecraft = Minecraft.getInstance();
                var windowState = minecraft.gameRenderer.getGameRenderState().windowRenderState;
                this.guiProjection.setupOrtho(1000.0F, 11000.0F, (float) target.target.width / (float) windowState.guiScale,
                        (float) target.target.height / (float) windowState.guiScale, true);
                RenderSystem.setProjectionMatrix(this.guiProjectionMatrixBuffer.getBuffer(this.guiProjection), ProjectionType.ORTHOGRAPHIC);
                var mainRenderTarget = target.target;
                int maxIndexCount = 0;

                for (var draw : this.draws) {
                    if (draw.indexCount > maxIndexCount) {
                        maxIndexCount = draw.indexCount;
                    }
                }

                var autoIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
                var indexBuffer = autoIndices.getBuffer(maxIndexCount);
                var indexType = autoIndices.type();
                var dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((new Matrix4f()).setTranslation(0.0F, 0.0F, -11000.0F),
                        new Vector4f(1.0F, 1.0F, 1.0F, 1.0F), new Vector3f(), new Matrix4f());
                this.executeDrawRange(() -> "GUI before blur", mainRenderTarget, fogBuffer, dynamicTransforms,
                        indexBuffer, indexType, Math.min(this.meshesToDraw.size(), this.draws.size()));
            }
        }

        private void executeDrawRange(Supplier<String> label, RenderTarget mainRenderTarget, GpuBufferSlice fogBuffer,
                                      GpuBufferSlice dynamicTransforms, GpuBuffer indexBuffer, VertexFormat.IndexType indexType, int endIndex) {
            try (var renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(label, mainRenderTarget.getColorTextureView(),
                    OptionalInt.empty(), mainRenderTarget.useDepth ? mainRenderTarget.getDepthTextureView() : null, OptionalDouble.empty())) {
                RenderSystem.bindDefaultUniforms(renderPass);
                renderPass.setUniform("Fog", fogBuffer);
                renderPass.setUniform("DynamicTransforms", dynamicTransforms);

                for (int i = 0; i < endIndex; ++i) {
                    Draw draw = this.draws.get(i);
                    this.executeDraw(draw, renderPass, indexBuffer, indexType);
                }
            }
        }

        private void addElementToMesh(GuiElementRenderState elementState) {
            var pipeline = elementState.pipeline();
            var textureSetup = elementState.textureSetup();
            var scissorArea = elementState.scissorArea();
            if (pipeline != this.previousPipeline || this.scissorChanged(scissorArea, this.previousScissorArea) || !textureSetup.equals(this.previousTextureSetup)) {
                if (this.bufferBuilder != null) {
                    this.recordMesh(this.bufferBuilder, this.previousPipeline, this.previousTextureSetup, this.previousScissorArea);
                }

                this.bufferBuilder = new BufferBuilder(this.byteBufferBuilder, pipeline.getVertexFormatMode(), pipeline.getVertexFormat());
                this.previousPipeline = pipeline;
                this.previousTextureSetup = textureSetup;
                this.previousScissorArea = scissorArea;
            }

            elementState.buildVertices(this.bufferBuilder);
        }

        private void recordMesh(BufferBuilder bufferBuilder, RenderPipeline pipeline, TextureSetup textureSetup, @Nullable ScreenRectangle scissorArea) {
            var mesh = bufferBuilder.build();
            if (mesh != null) {
                this.meshesToDraw.add(new MeshToDraw(mesh, pipeline, textureSetup, scissorArea));
            }

        }

        private void ensureVertexBufferSizes() {
            var requiredSizes = new Object2IntOpenHashMap<VertexFormat>();

            for (var meshToDraw : this.meshesToDraw) {
                var drawState = meshToDraw.mesh.drawState();
                var format = drawState.format();
                if (!requiredSizes.containsKey(format)) {
                    requiredSizes.put(format, 0);
                }

                requiredSizes.put(format, requiredSizes.getInt(format) + drawState.vertexCount() * format.getVertexSize());
            }

            for (var entry : requiredSizes.object2IntEntrySet()) {
                var vertexFormat = entry.getKey();
                int requiredSize = entry.getIntValue();
                var vertexBuffer = this.vertexBuffers.get(vertexFormat);
                if (vertexBuffer == null || vertexBuffer.size() < requiredSize) {
                    if (vertexBuffer != null) {
                        vertexBuffer.close();
                    }

                    this.vertexBuffers.put(vertexFormat, new MappableRingBuffer(() -> "GUI vertex buffer for " + vertexFormat, 34, requiredSize));
                }
            }
        }

        private void executeDraw(Draw draw, RenderPass renderPass, GpuBuffer indexBuffer, VertexFormat.IndexType indexType) {
            var pipeline = draw.pipeline();
            renderPass.setPipeline(pipeline);
            renderPass.setVertexBuffer(0, draw.vertexBuffer);
            var scissorArea = draw.scissorArea();
            if (scissorArea != null) {
                this.enableScissor(scissorArea, renderPass);
            } else {
                renderPass.disableScissor();
            }

            if (draw.textureSetup.texure0() != null) {
                renderPass.bindTexture("Sampler0", draw.textureSetup.texure0(), draw.textureSetup.sampler0());
            }

            if (draw.textureSetup.texure1() != null) {
                renderPass.bindTexture("Sampler1", draw.textureSetup.texure1(), draw.textureSetup.sampler1());
            }

            if (draw.textureSetup.texure2() != null) {
                renderPass.bindTexture("Sampler2", draw.textureSetup.texure2(), draw.textureSetup.sampler2());
            }

            renderPass.setIndexBuffer(indexBuffer, indexType);
            renderPass.drawIndexed(draw.baseVertex, 0, draw.indexCount, 1);
        }

        private boolean scissorChanged(@Nullable ScreenRectangle newScissor, @Nullable ScreenRectangle oldScissor) {
            if (newScissor == oldScissor) {
                return false;
            } else if (newScissor != null) {
                return !newScissor.equals(oldScissor);
            } else {
                return true;
            }
        }

        private void enableScissor(ScreenRectangle rectangle, RenderPass renderPass) {
            WindowRenderState windowState = Minecraft.getInstance().gameRenderer.getGameRenderState().windowRenderState;
            int windowHeight = windowState.height;
            int guiScale = windowState.guiScale;
            double left = rectangle.left() * guiScale;
            double bottom = windowHeight - rectangle.bottom() * guiScale;
            double width = rectangle.width() * guiScale;
            double height = rectangle.height() * guiScale;
            renderPass.enableScissor((int) left, (int) bottom, Math.max(0, (int) width), Math.max(0, (int) height));
        }

        public void close() {
            this.byteBufferBuilder.close();
            this.guiProjectionMatrixBuffer.close();

            for (MappableRingBuffer buffer : this.vertexBuffers.values()) {
                buffer.close();
            }

            this.cubeMap.close();
        }

        private record Draw(GpuBuffer vertexBuffer, int baseVertex, VertexFormat.Mode mode, int indexCount,
                            RenderPipeline pipeline, TextureSetup textureSetup, @Nullable ScreenRectangle scissorArea) {
        }

        private record MeshToDraw(MeshData mesh, RenderPipeline pipeline, TextureSetup textureSetup,
                                  @Nullable ScreenRectangle scissorArea) implements AutoCloseable {
            public void close() {
                this.mesh.close();
            }
        }
    }
}
