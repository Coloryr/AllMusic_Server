package com.coloryr.allmusic.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiniMessage {
    private static final Pattern TAG_PATTERN = Pattern.compile(
            "<(/?)(\\w+)(?:\\(([^)]+)\\))?(?::([^>]+))?>"
    );

    private static final Map<String, ChatFormatting> COLOR_MAP = new HashMap<>();
    static {
        COLOR_MAP.put("black", ChatFormatting.BLACK);
        COLOR_MAP.put("dark_blue", ChatFormatting.DARK_BLUE);
        COLOR_MAP.put("dark_green", ChatFormatting.DARK_GREEN);
        COLOR_MAP.put("dark_aqua", ChatFormatting.DARK_AQUA);
        COLOR_MAP.put("dark_red", ChatFormatting.DARK_RED);
        COLOR_MAP.put("dark_purple", ChatFormatting.DARK_PURPLE);
        COLOR_MAP.put("gold", ChatFormatting.GOLD);
        COLOR_MAP.put("gray", ChatFormatting.GRAY);
        COLOR_MAP.put("dark_gray", ChatFormatting.DARK_GRAY);
        COLOR_MAP.put("blue", ChatFormatting.BLUE);
        COLOR_MAP.put("green", ChatFormatting.GREEN);
        COLOR_MAP.put("aqua", ChatFormatting.AQUA);
        COLOR_MAP.put("red", ChatFormatting.RED);
        COLOR_MAP.put("light_purple", ChatFormatting.LIGHT_PURPLE);
        COLOR_MAP.put("yellow", ChatFormatting.YELLOW);
        COLOR_MAP.put("white", ChatFormatting.WHITE);
    }

    private static final Map<String, ChatFormatting> DECORATION_MAP = new HashMap<>();
    static {
        DECORATION_MAP.put("bold", ChatFormatting.BOLD);
        DECORATION_MAP.put("italic", ChatFormatting.ITALIC);
        DECORATION_MAP.put("underlined", ChatFormatting.UNDERLINE);
        DECORATION_MAP.put("strikethrough", ChatFormatting.STRIKETHROUGH);
        DECORATION_MAP.put("obfuscated", ChatFormatting.OBFUSCATED);
    }

    public static Component parse(String input) {
        MutableComponent root = new TextComponent("");
        Stack<StyleContext> styleStack = new Stack<>();
        StyleContext currentStyle = new StyleContext();
        styleStack.push(currentStyle);

        StringBuilder textBuffer = new StringBuilder();
        int pos = 0;
        int length = input.length();

        while (pos < length) {
            char c = input.charAt(pos);

            if (c == '<') {
                if (textBuffer.length() > 0) {
                    appendText(root, textBuffer.toString(), currentStyle);
                    textBuffer.setLength(0);
                }

                int endTag = findClosingTag(input, pos);
                if (endTag == -1) {
                    textBuffer.append(c);
                    pos++;
                    continue;
                }

                String tagContent = input.substring(pos, endTag + 1);
                Matcher matcher = TAG_PATTERN.matcher(tagContent);

                if (matcher.matches()) {
                    boolean isClosing = !matcher.group(1).isEmpty();
                    String tagName = matcher.group(2).toLowerCase(Locale.ROOT);
                    String params = matcher.group(3);
                    String value = matcher.group(4);

                    if (isClosing) {
                        String expectedOpenTag = matcher.group(2).toLowerCase(Locale.ROOT);

                        if (styleStack.size() > 1) {
                            int index = styleStack.size() - 1;
                            boolean found = false;

                            while (index >= 0) {
                                StyleContext ctx = styleStack.get(index);
                                if (ctx.tagName != null && ctx.tagName.equals(expectedOpenTag)) {
                                    while (styleStack.size() > index + 1) {
                                        styleStack.pop();
                                    }
                                    currentStyle = styleStack.peek();
                                    found = true;
                                    break;
                                }
                                index--;
                            }

                            if (!found) {
                            }
                        }
                    } else {
                        StyleContext newStyle = currentStyle.copy();

                        switch (tagName) {
                            case "color":
                                handleColorTag(newStyle, params, value);
                                break;
                            case "shadow":
                                handleShadowTag(newStyle, params, value);
                                break;
                            case "decoration":
                                handleDecorationTag(newStyle, params, value);
                                break;
                            case "reset":
                                newStyle = new StyleContext();
                                break;
                            case "rainbow":
                                handleRainbowTag(newStyle, params, value);
                                break;
                            case "gradient":
                                handleGradientTag(newStyle, params, value);
                                break;
                            case "transition":
                                handleTransitionTag(newStyle, params, value);
                                break;
                            case "font":
                                handleFontTag(newStyle, params, value);
                                break;
                            default:
                                break;
                        }

                        newStyle.tagName = tagName;
                        styleStack.push(newStyle);
                        currentStyle = newStyle;
                    }
                } else {
                    textBuffer.append(tagContent);
                }

                pos = endTag + 1;
            } else {
                textBuffer.append(c);
                pos++;
            }
        }

        if (textBuffer.length() > 0) {
            appendText(root, textBuffer.toString(), currentStyle);
        }

        return root;
    }

    private static void handleColorTag(StyleContext style, String params, String value) {
        if (value != null) {
            TextColor textColor = parseColor(value);
            if (textColor != null) {
                style.color = textColor;
                style.gradient = false;
                style.transition = false;
                style.rainbow = false;
            }
        } else if (params != null) {
            if (params.equalsIgnoreCase("rgb")) {
                String[] parts = value.split(",");
                if (parts.length == 3) {
                    try {
                        int r = Integer.parseInt(parts[0].trim());
                        int g = Integer.parseInt(parts[1].trim());
                        int b = Integer.parseInt(parts[2].trim());
                        style.color = TextColor.fromRgb((r << 16) | (g << 8) | b);
                        style.gradient = false;
                        style.transition = false;
                        style.rainbow = false;
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
    }

    private static void handleShadowTag(StyleContext style, String params, String value) {
        if (value == null) {
            style.shadow = true;
        } else if (value.equalsIgnoreCase("true")) {
            style.shadow = true;
        } else if (value.equalsIgnoreCase("false")) {
            style.shadow = false;
        } else {
            TextColor shadowColor = parseColor(value);
            if (shadowColor != null) {
                style.shadow = true;
                style.shadowColor = shadowColor;
            }
        }
    }

    private static void handleDecorationTag(StyleContext style, String params, String value) {
        if (params == null) {
            return;
        }

        String[] parts = params.split(":");
        String decorationName = parts[0].toLowerCase(Locale.ROOT);
        boolean enabled = true;

        if (parts.length > 1) {
            enabled = Boolean.parseBoolean(parts[1]);
        } else if (value != null) {
            enabled = Boolean.parseBoolean(value);
        }

        ChatFormatting formatting = DECORATION_MAP.get(decorationName);
        if (formatting != null) {
            style.decorations.put(formatting, enabled);
        }
    }

    private static void handleRainbowTag(StyleContext style, String params, String value) {
        float saturation = 1.0f;
        float brightness = 1.0f;

        if (params != null) {
            if (params.equalsIgnoreCase("saturation") && value != null) {
                try {
                    saturation = Float.parseFloat(value);
                } catch (NumberFormatException e) {
                }
            } else if (params.equalsIgnoreCase("brightness") && value != null) {
                try {
                    brightness = Float.parseFloat(value);
                } catch (NumberFormatException e) {
                }
            }
        }

        style.rainbow = true;
        style.gradient = false;
        style.transition = false;
        style.rainbowSaturation = saturation;
        style.rainbowBrightness = brightness;
    }

    private static void handleGradientTag(StyleContext style, String params, String value) {
        if (value == null) {
            return;
        }

        String[] colors = value.split(":");
        if (colors.length == 2) {
            TextColor start = parseColor(colors[0]);
            TextColor end = parseColor(colors[1]);

            if (start != null && end != null) {
                style.gradient = true;
                style.transition = false;
                style.rainbow = false;
                style.gradientStart = start;
                style.gradientEnd = end;
            }
        } else if (params != null && params.equalsIgnoreCase("rgb")) {
            String[] color1 = colors[0].split(",");
            String[] color2 = colors[1].split(",");
            if (color1.length == 3 && color2.length == 3) {
                try {
                    int r1 = Integer.parseInt(color1[0].trim());
                    int g1 = Integer.parseInt(color1[1].trim());
                    int b1 = Integer.parseInt(color1[2].trim());
                    int r2 = Integer.parseInt(color2[0].trim());
                    int g2 = Integer.parseInt(color2[1].trim());
                    int b2 = Integer.parseInt(color2[2].trim());

                    style.gradient = true;
                    style.transition = false;
                    style.rainbow = false;
                    style.gradientStart = TextColor.fromRgb((r1 << 16) | (g1 << 8) | b1);
                    style.gradientEnd = TextColor.fromRgb((r2 << 16) | (g2 << 8) | b2);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private static void handleTransitionTag(StyleContext style, String params, String value) {
        if (value == null) {
            return;
        }

        String[] colors = value.split(":");
        if (colors.length == 2) {
            TextColor start = parseColor(colors[0]);
            TextColor end = parseColor(colors[1]);

            if (start != null && end != null) {
                style.transition = true;
                style.gradient = false;
                style.rainbow = false;
                style.transitionStart = start;
                style.transitionEnd = end;
            }
        } else if (params != null && params.equalsIgnoreCase("rgb")) {
            String[] color1 = colors[0].split(",");
            String[] color2 = colors[1].split(",");
            if (color1.length == 3 && color2.length == 3) {
                try {
                    int r1 = Integer.parseInt(color1[0].trim());
                    int g1 = Integer.parseInt(color1[1].trim());
                    int b1 = Integer.parseInt(color1[2].trim());
                    int r2 = Integer.parseInt(color2[0].trim());
                    int g2 = Integer.parseInt(color2[1].trim());
                    int b2 = Integer.parseInt(color2[2].trim());

                    style.transition = true;
                    style.gradient = false;
                    style.rainbow = false;
                    style.transitionStart = TextColor.fromRgb((r1 << 16) | (g1 << 8) | b1);
                    style.transitionEnd = TextColor.fromRgb((r2 << 16) | (g2 << 8) | b2);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private static void handleFontTag(StyleContext style, String params, String value) {
        String fontName = value != null ? value : params;
        if (fontName != null) {
            style.font = fontName;
        }
    }

    private static TextColor parseColor(String colorStr) {
        if (colorStr == null) {
            return null;
        }

        ChatFormatting formatting = COLOR_MAP.get(colorStr.toLowerCase(Locale.ROOT));
        if (formatting != null) {
            return TextColor.fromLegacyFormat(formatting);
        }

        if (colorStr.startsWith("#")) {
            try {
                if (colorStr.length() == 7) {
                    return TextColor.parseColor(colorStr);
                } else if (colorStr.length() == 4) {
                    String expanded = "#"
                            + colorStr.charAt(1) + colorStr.charAt(1)
                            + colorStr.charAt(2) + colorStr.charAt(2)
                            + colorStr.charAt(3) + colorStr.charAt(3);
                    return TextColor.parseColor(expanded);
                }
            } catch (Exception e) {
            }
        }

        return null;
    }

    private static int findClosingTag(String input, int start) {
        int pos = start + 1;
        while (pos < input.length()) {
            if (input.charAt(pos) == '>') {
                return pos;
            }
            pos++;
        }
        return -1;
    }

    private static void appendText(MutableComponent root, String text, StyleContext styleContext) {
        if (text.isEmpty()) {
            return;
        }

        if (styleContext.gradient && styleContext.gradientStart != null && styleContext.gradientEnd != null) {
            appendGradientText(root, text, styleContext);
        } else if (styleContext.transition && styleContext.transitionStart != null && styleContext.transitionEnd != null) {
            appendTransitionText(root, text, styleContext);
        } else if (styleContext.rainbow) {
            appendRainbowText(root, text, styleContext);
        } else {
            MutableComponent component = new TextComponent(text);
            component.setStyle(styleContext.buildStyle());
            root.append(component);
        }
    }

    private static void appendGradientText(MutableComponent root, String text, StyleContext styleContext) {
        int length = text.length();
        Style baseStyle = styleContext.buildBaseStyle();

        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            float ratio = length == 1 ? 0.0f : (float) i / (length - 1);
            TextColor gradientColor = interpolateColor(styleContext.gradientStart, styleContext.gradientEnd, ratio);
            Style charStyle = baseStyle.withColor(gradientColor);
            root.append(new TextComponent(String.valueOf(c)).withStyle(charStyle));
        }
    }

    private static void appendTransitionText(MutableComponent root, String text, StyleContext styleContext) {
        Style baseStyle = styleContext.buildBaseStyle();
        MutableComponent transitionComponent = new TextComponent(text);

        if (styleContext.transitionStart != null && styleContext.transitionEnd != null) {
            transitionComponent.setStyle(baseStyle.withColor(styleContext.transitionStart));
        } else {
            transitionComponent.setStyle(baseStyle);
        }

        root.append(transitionComponent);
    }

    private static void appendRainbowText(MutableComponent root, String text, StyleContext styleContext) {
        int length = text.length();
        Style baseStyle = styleContext.buildBaseStyle();

        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            float hue = (float) i / length;
            int rgb = java.awt.Color.HSBtoRGB(hue, styleContext.rainbowSaturation, styleContext.rainbowBrightness);
            TextColor rainbowColor = TextColor.fromRgb(rgb & 0xFFFFFF);
            Style charStyle = baseStyle.withColor(rainbowColor);
            root.append(new TextComponent(String.valueOf(c)).withStyle(charStyle));
        }
    }

    private static TextColor interpolateColor(TextColor start, TextColor end, float ratio) {
        int startRgb = start.getValue();
        int endRgb = end.getValue();

        int r1 = (startRgb >> 16) & 0xFF;
        int g1 = (startRgb >> 8) & 0xFF;
        int b1 = startRgb & 0xFF;

        int r2 = (endRgb >> 16) & 0xFF;
        int g2 = (endRgb >> 8) & 0xFF;
        int b2 = endRgb & 0xFF;

        int r = (int) (r1 + (r2 - r1) * ratio);
        int g = (int) (g1 + (g2 - g1) * ratio);
        int b = (int) (b1 + (b2 - b1) * ratio);

        return TextColor.fromRgb((r << 16) | (g << 8) | b);
    }

    private static class StyleContext {
        String tagName;
        TextColor color;
        Boolean shadow;
        TextColor shadowColor;
        Map<ChatFormatting, Boolean> decorations = new HashMap<>();
        boolean rainbow;
        float rainbowSaturation = 1.0f;
        float rainbowBrightness = 1.0f;
        boolean gradient;
        TextColor gradientStart;
        TextColor gradientEnd;
        boolean transition;
        TextColor transitionStart;
        TextColor transitionEnd;
        String font;

        StyleContext copy() {
            StyleContext copy = new StyleContext();
            copy.tagName = this.tagName;
            copy.color = this.color;
            copy.shadow = this.shadow;
            copy.shadowColor = this.shadowColor;
            copy.decorations.putAll(this.decorations);
            copy.rainbow = this.rainbow;
            copy.rainbowSaturation = this.rainbowSaturation;
            copy.rainbowBrightness = this.rainbowBrightness;
            copy.gradient = this.gradient;
            copy.gradientStart = this.gradientStart;
            copy.gradientEnd = this.gradientEnd;
            copy.transition = this.transition;
            copy.transitionStart = this.transitionStart;
            copy.transitionEnd = this.transitionEnd;
            copy.font = this.font;
            return copy;
        }

        Style buildBaseStyle() {
            Style style = Style.EMPTY;

            for (Map.Entry<ChatFormatting, Boolean> entry : decorations.entrySet()) {
                if (entry.getValue()) {
                    style = style.applyFormat(entry.getKey());
                }
            }

            if (font != null) {
                try {
                    style = style.withFont(new ResourceLocation(font));
                } catch (Exception e) {
                }
            }

            return style;
        }

        Style buildStyle() {
            Style style = buildBaseStyle();
            if (!gradient && !transition && !rainbow && color != null) {
                style = style.withColor(color);
            }
            return style;
        }
    }
}
