package com.coloryr.allmusic.client.core.render;

import com.coloryr.allmusic.client.core.Point2f;
import com.coloryr.allmusic.codec.HudPosType;

import java.util.ArrayList;
import java.util.List;

public abstract class TextFrameBuffer {
    public void clear() {
        texts.clear();
    }

    protected final List<TextItem> texts = new ArrayList<>();

    protected int nowWidth, nowHeight;
    protected long offsetX;
    protected boolean isDraw;

    public abstract void use();

    public abstract void unUse();

    public abstract void resize(int width, int height);

    public abstract void drawText(String text, int y, int color, boolean shadow);

    public abstract void drawLine(float x, float y, float alpha, int line);

    public abstract Point2f getLine(int line);

    public abstract void draw(float alpha, int x, int y, int maxWidth, HudPosType dir);

    public abstract void drawWithState(float alpha, int x, int y, int maxWidth, float state, HudPosType dir);

    // 最大公约数（GCD）
    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // 两数最小公倍数（LCM）
    public static long lcm(long a, long b) {
        return a / gcd(a, b) * b;   // 先除后乘防溢出
    }

    public void tick() {
        offsetX++;
        if (isDraw) return;
        long temp = 1;
        for (TextItem item : texts) {
            temp = lcm(temp, item.textWidth);
        }
        if (offsetX > temp) {
            offsetX = 0;
        }
    }

    public static class TextItem {
        public final float renderWidth;
        public final float renderHeight;
        public final int textWidth;
        public final int textHeight;
        public final float y;
        public final float scale;

        public TextItem(int width, int height, float y, float scale) {
            this.textWidth = width;
            this.textHeight = height;
            this.renderWidth = width * scale;
            this.renderHeight = height * scale;
            this.y = y;
            this.scale = scale;
        }
    }
}
