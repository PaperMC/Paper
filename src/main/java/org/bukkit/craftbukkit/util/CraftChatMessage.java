package org.bukkit.craftbukkit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatModifier;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.IChatBaseComponent;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public final class CraftChatMessage {
    private static class FromString {
        private static final Map<Character, EnumChatFormat> formatMap;

        static {
            Builder<Character, EnumChatFormat> builder = ImmutableMap.builder();
            for (EnumChatFormat format : EnumChatFormat.values()) {
                builder.put(format.getChar(), format);
            }
            formatMap = builder.build();
        }

        private final List<IChatBaseComponent> list = new ArrayList<IChatBaseComponent>();
        private IChatBaseComponent currentChatComponent = new ChatComponentText("");
        private ChatModifier modifier = new ChatModifier();
        private StringBuilder builder = new StringBuilder();
        private final IChatBaseComponent[] output;

        private FromString(String message) {
            if (message == null) {
                output = new IChatBaseComponent[] { currentChatComponent };
                return;
            }
            list.add(currentChatComponent);

            EnumChatFormat format = null;

            for (int i = 0; i < message.length(); i++) {
                char currentChar = message.charAt(i);
                if (currentChar == '\u00A7' && (i < (message.length() - 1)) && (format = formatMap.get(message.charAt(i + 1))) != null) {
                    if (builder.length() > 0) {
                        appendNewComponent();
                    }

                    if (format == EnumChatFormat.RESET) {
                        modifier = new ChatModifier();
                    } else if (format.isFormat()) {
                        switch (format) {
                        case BOLD:
                            modifier.setBold(Boolean.TRUE);
                            break;
                        case ITALIC:
                            modifier.setItalic(Boolean.TRUE);
                            break;
                        case STRIKETHROUGH:
                            modifier.setStrikethrough(Boolean.TRUE);
                            break;
                        case UNDERLINE:
                            modifier.setUnderline(Boolean.TRUE);
                            break;
                        case RANDOM:
                            modifier.setRandom(Boolean.TRUE);
                            break;
                        default:
                            throw new AssertionError("Unexpected message format");
                        }
                    } else { // Color resets formatting
                        modifier = new ChatModifier().setColor(format);
                    }
                    i++;
                } else if (currentChar == '\n') {
                    if (builder.length() > 0) {
                        appendNewComponent();
                    }
                    currentChatComponent = null;
                } else {
                    builder.append(currentChar);
                }
            }

            if (builder.length() > 0) {
                appendNewComponent();
            }

            output = list.toArray(new IChatBaseComponent[0]);
        }

        private void appendNewComponent() {
            IChatBaseComponent addition = new ChatComponentText(builder.toString()).setChatModifier(modifier);
            builder = new StringBuilder();
            modifier = modifier.clone();
            if (currentChatComponent == null) {
                currentChatComponent = new ChatComponentText("");
                list.add(currentChatComponent);
            }
            currentChatComponent.a(addition);
        }

        private IChatBaseComponent[] getOutput() {
            return output;
        }
    }

    public static IChatBaseComponent[] fromString(String message) {
        return new FromString(message).getOutput();
    }

    private CraftChatMessage() {
    }
}
