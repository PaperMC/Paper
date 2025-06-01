package org.bukkit.craftbukkit.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.mojang.serialization.JsonOps;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ChatVersion;
import net.md_5.bungee.chat.VersionedComponentSerializer;
import com.mojang.serialization.JavaOps;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.util.ExtraCodecs;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.CraftRegistry;

public final class CraftChatMessage {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    private static final Map<Character, ChatFormatting> formatMap;

    static {
        Builder<Character, ChatFormatting> builder = ImmutableMap.builder();
        for (ChatFormatting format : ChatFormatting.values()) {
            builder.put(Character.toLowerCase(format.toString().charAt(1)), format);
        }
        formatMap = builder.build();
    }

    public static ChatFormatting getColor(ChatColor color) {
        return CraftChatMessage.formatMap.get(color.getChar());
    }

    public static ChatColor getColor(ChatFormatting format) {
        return ChatColor.getByChar(format.code);
    }

    private static final class StringMessage {
        private static final Pattern INCREMENTAL_PATTERN = Pattern.compile("(" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + "[0-9a-fk-orx])|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + " \\n]|$))))|(\\n)", Pattern.CASE_INSENSITIVE);
        // Separate pattern with no group 3, new lines are part of previous string
        private static final Pattern INCREMENTAL_PATTERN_KEEP_NEWLINES = Pattern.compile("(" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + "[0-9a-fk-orx])|((?:(?:https?):\\/\\/)?(?:[-\\w_\\.]{2,}\\.[a-z]{2,4}.*?(?=[\\.\\?!,;:]?(?:[" + String.valueOf(org.bukkit.ChatColor.COLOR_CHAR) + " ]|$))))", Pattern.CASE_INSENSITIVE);
        // ChatColor.b does not explicitly reset, its more of empty
        private static final Style EMPTY = Style.EMPTY.withItalic(false); // Paper - Improve Legacy Component serialization size
        private static final Style RESET = Style.EMPTY.withBold(false).withItalic(false).withUnderlined(false).withStrikethrough(false).withObfuscated(false);

        private final List<Component> list = new ArrayList<Component>();
        private MutableComponent currentChatComponent = Component.empty();
        private Style modifier = Style.EMPTY;
        private final Component[] output;
        private int currentIndex;
        private StringBuilder hex;
        private final String message;

        private StringMessage(String message, boolean keepNewlines, boolean plain) {
            this.message = message;
            if (message == null) {
                this.output = new Component[]{this.currentChatComponent};
                return;
            }
            this.list.add(this.currentChatComponent);

            Matcher matcher = (keepNewlines ? StringMessage.INCREMENTAL_PATTERN_KEEP_NEWLINES : StringMessage.INCREMENTAL_PATTERN).matcher(message);
            String match = null;
            boolean needsAdd = false;
            boolean hasReset = false; // Paper - Improve Legacy Component serialization size
            while (matcher.find()) {
                int groupId = 0;
                while ((match = matcher.group(++groupId)) == null) {
                    // NOOP
                }
                int index = matcher.start(groupId);
                if (index > this.currentIndex) {
                    needsAdd = false;
                    this.appendNewComponent(index);
                }
                switch (groupId) {
                case 1:
                    char c = match.toLowerCase(Locale.ROOT).charAt(1);
                    ChatFormatting format = CraftChatMessage.formatMap.get(c);

                    if (c == 'x') {
                        this.hex = new StringBuilder("#");
                    } else if (this.hex != null) {
                        this.hex.append(c);

                        if (this.hex.length() == 7) {
                            this.modifier = StringMessage.RESET.withColor(TextColor.parseColor(this.hex.toString()).result().orElse(null)); // Paper
                            this.hex = null;
                        }
                    } else if (format.isFormat() && format != ChatFormatting.RESET) {
                        switch (format) {
                        case BOLD:
                            this.modifier = this.modifier.withBold(Boolean.TRUE);
                            break;
                        case ITALIC:
                            this.modifier = this.modifier.withItalic(Boolean.TRUE);
                            break;
                        case STRIKETHROUGH:
                            this.modifier = this.modifier.withStrikethrough(Boolean.TRUE);
                            break;
                        case UNDERLINE:
                            this.modifier = this.modifier.withUnderlined(Boolean.TRUE);
                            break;
                        case OBFUSCATED:
                            this.modifier = this.modifier.withObfuscated(Boolean.TRUE);
                            break;
                        default:
                            throw new AssertionError("Unexpected message format");
                        }
                    } else { // Color resets formatting
                        Style previous = modifier;
                        modifier = (!hasReset ? RESET : EMPTY).withColor(format);
                        hasReset = true;
                        if (previous.isBold()) {
                            modifier = modifier.withBold(false);
                        }
                        if (previous.isItalic()) {
                            modifier = modifier.withItalic(false);
                        }
                        if (previous.isObfuscated()) {
                            modifier = modifier.withObfuscated(false);
                        }
                        if (previous.isStrikethrough()) {
                            modifier = modifier.withStrikethrough(false);
                        }
                        if (previous.isUnderlined()) {
                            modifier = modifier.withUnderlined(false);
                        }
                    }
                    needsAdd = true;
                    break;
                case 2:
                    if (plain) {
                        this.appendNewComponent(matcher.end(groupId));
                    } else {
                        if (!(match.startsWith("http://") || match.startsWith("https://"))) {
                            match = "http://" + match;
                        }
                        ExtraCodecs.UNTRUSTED_URI.parse(JavaOps.INSTANCE, match).ifSuccess(uri -> {
                            this.modifier = this.modifier.withClickEvent(new ClickEvent.OpenUrl(uri));
                        });
                        this.appendNewComponent(matcher.end(groupId));
                        this.modifier = this.modifier.withClickEvent(null);
                    }
                    break;
                case 3:
                    if (needsAdd) {
                        this.appendNewComponent(index);
                    }
                    this.currentChatComponent = null;
                    break;
                }
                this.currentIndex = matcher.end(groupId);
            }

            if (this.currentIndex < message.length() || needsAdd) {
                this.appendNewComponent(message.length());
            }

            this.output = this.list.toArray(new Component[this.list.size()]);
        }

        private void appendNewComponent(int index) {
            Component addition = Component.literal(this.message.substring(this.currentIndex, index)).setStyle(this.modifier);
            this.currentIndex = index;
            if (this.currentChatComponent == null) {
                this.currentChatComponent = Component.empty();
                this.list.add(this.currentChatComponent);
            }
            this.currentChatComponent.append(addition);
        }

        private Component[] getOutput() {
            return this.output;
        }
    }

    public static Optional<Component> fromStringOrOptional(String message) {
        return Optional.ofNullable(CraftChatMessage.fromStringOrNull(message));
    }

    public static Optional<Component> fromStringOrOptional(String message, boolean keepNewlines) {
        return Optional.ofNullable(CraftChatMessage.fromStringOrNull(message, keepNewlines));
    }

    public static Component fromStringOrNull(String message) {
        return CraftChatMessage.fromStringOrNull(message, false);
    }

    public static Component fromStringOrNull(String message, boolean keepNewlines) {
        return (message == null || message.isEmpty()) ? null : CraftChatMessage.fromString(message, keepNewlines)[0];
    }

    public static Component fromStringOrEmpty(String message) {
        return CraftChatMessage.fromStringOrEmpty(message, false);
    }

    public static Component fromStringOrEmpty(String message, boolean keepNewlines) {
        return CraftChatMessage.fromString(message, keepNewlines)[0];
    }

    public static Component[] fromString(String message) {
        return CraftChatMessage.fromString(message, false);
    }

    public static Component[] fromString(String message, boolean keepNewlines) {
        return CraftChatMessage.fromString(message, keepNewlines, false);
    }

    public static Component[] fromString(String message, boolean keepNewlines, boolean plain) {
        return new StringMessage(message, keepNewlines, plain).getOutput();
    }

    public static String toJSON(Component component) {
        return GSON.toJson(
            ComponentSerialization.CODEC
                .encodeStart(JsonOps.INSTANCE, component)
                .getOrThrow(JsonParseException::new)
        );
    }

    public static String toJSONOrNull(Component component) {
        if (component == null) return null;
        return CraftChatMessage.toJSON(component);
    }

    public static Component fromJSON(String jsonMessage) throws JsonParseException {
        // Note: This also parses plain Strings to text components.
        // Note: An empty message (empty, or only consisting of whitespace) results in null rather than a parse exception.
        final JsonElement jsonElement = GSON.fromJson(jsonMessage, JsonElement.class);
        return jsonElement == null ? null : ComponentSerialization.CODEC.parse(
            CraftRegistry.getMinecraftRegistry().createSerializationContext(JsonOps.INSTANCE), jsonElement
        ).getOrThrow(JsonParseException::new);
    }

    public static Component fromJSONOrNull(String jsonMessage) {
        if (jsonMessage == null) return null;
        try {
            return CraftChatMessage.fromJSON(jsonMessage); // Can return null
        } catch (JsonParseException ex) {
            return null;
        }
    }

    public static Component fromJSONOrString(String message) {
        return CraftChatMessage.fromJSONOrString(message, false);
    }

    public static Component fromJSONOrString(String message, boolean keepNewlines) {
        return CraftChatMessage.fromJSONOrString(message, false, keepNewlines);
    }

    public static Component fromJSONOrString(String message, boolean nullable, boolean keepNewlines) {
        return CraftChatMessage.fromJSONOrString(message, nullable, keepNewlines, Integer.MAX_VALUE, false);
    }

    public static Component fromJSONOrString(String message, boolean nullable, boolean keepNewlines, int maxLength, boolean checkJsonContentLength) {
        if (message == null) message = "";
        if (nullable && message.isEmpty()) return null;
        Component component = CraftChatMessage.fromJSONOrNull(message);
        if (component != null) {
            if (checkJsonContentLength) {
                String content = CraftChatMessage.fromComponent(component);
                String trimmedContent = CraftChatMessage.trimMessage(content, maxLength);
                if (content != trimmedContent) { // Identity comparison is fine here
                    // Note: The resulting text has all non-plain text features stripped.
                    return CraftChatMessage.fromString(trimmedContent, keepNewlines)[0];
                }
            }
            return component;
        } else {
            message = CraftChatMessage.trimMessage(message, maxLength);
            return CraftChatMessage.fromString(message, keepNewlines)[0];
        }
    }

    public static String trimMessage(String message, int maxLength) {
        if (message != null && message.length() > maxLength) {
            return message.substring(0, maxLength);
        } else {
            return message;
        }
    }

    public static String fromComponent(Component component) {
        if (component == null) return "";
        if (component instanceof io.papermc.paper.adventure.AdventureComponent) component = ((io.papermc.paper.adventure.AdventureComponent) component).deepConverted();
        StringBuilder out = new StringBuilder();

        boolean hadFormat = false;
        for (Component c : component) {
            Style modi = c.getStyle();
            TextColor color = modi.getColor();
            if (c.getContents() != PlainTextContents.EMPTY || color != null) {
                if (color != null) {
                    if (color.format != null) {
                        out.append(color.format);
                    } else {
                        out.append(ChatColor.COLOR_CHAR).append("x");
                        for (char magic : color.serialize().substring(1).toCharArray()) {
                            out.append(ChatColor.COLOR_CHAR).append(magic);
                        }
                    }
                    hadFormat = true;
                } else if (hadFormat) {
                    out.append(ChatColor.RESET);
                    hadFormat = false;
                }
            }
            if (modi.isBold()) {
                out.append(ChatFormatting.BOLD);
                hadFormat = true;
            }
            if (modi.isItalic()) {
                out.append(ChatFormatting.ITALIC);
                hadFormat = true;
            }
            if (modi.isUnderlined()) {
                out.append(ChatFormatting.UNDERLINE);
                hadFormat = true;
            }
            if (modi.isStrikethrough()) {
                out.append(ChatFormatting.STRIKETHROUGH);
                hadFormat = true;
            }
            if (modi.isObfuscated()) {
                out.append(ChatFormatting.OBFUSCATED);
                hadFormat = true;
            }
            c.getContents().visit((x) -> {
                out.append(x);
                return Optional.empty();
            });
        }
        return out.toString();
    }

    private static VersionedComponentSerializer bungeeSerializer = VersionedComponentSerializer.forVersion(ChatVersion.V1_21_5);
    public static String bungeeToJson(BaseComponent... components) {
        return bungeeSerializer.toString(components);
    }

    public static BaseComponent[] jsonToBungee(String json) {
        return bungeeSerializer.parse(json);
    }
    public static Component bungeeToVanilla(BaseComponent... components) {
        return fromJSON(bungeeToJson(components));
    }
    public static BaseComponent[] vanillaToBungee(Component component) {
        return jsonToBungee(toJSON(component));
    }

    private CraftChatMessage() {
    }
}
