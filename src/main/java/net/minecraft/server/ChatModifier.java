package net.minecraft.server;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Objects;
import javax.annotation.Nullable;

public class ChatModifier {

    public static final ChatModifier a = new ChatModifier((ChatHexColor) null, (Boolean) null, (Boolean) null, (Boolean) null, (Boolean) null, (Boolean) null, (ChatClickable) null, (ChatHoverable) null, (String) null, (MinecraftKey) null);
    public static final MinecraftKey b = new MinecraftKey("minecraft", "default");
    @Nullable
    private final ChatHexColor color;
    @Nullable
    private final Boolean bold;
    @Nullable
    private final Boolean italic;
    @Nullable
    private final Boolean underlined;
    @Nullable
    private final Boolean strikethrough;
    @Nullable
    private final Boolean obfuscated;
    @Nullable
    private final ChatClickable clickEvent;
    @Nullable
    private final ChatHoverable hoverEvent;
    @Nullable
    private final String insertion;
    @Nullable
    private final MinecraftKey font;

    private ChatModifier(@Nullable ChatHexColor chathexcolor, @Nullable Boolean obool, @Nullable Boolean obool1, @Nullable Boolean obool2, @Nullable Boolean obool3, @Nullable Boolean obool4, @Nullable ChatClickable chatclickable, @Nullable ChatHoverable chathoverable, @Nullable String s, @Nullable MinecraftKey minecraftkey) {
        this.color = chathexcolor;
        this.bold = obool;
        this.italic = obool1;
        this.underlined = obool2;
        this.strikethrough = obool3;
        this.obfuscated = obool4;
        this.clickEvent = chatclickable;
        this.hoverEvent = chathoverable;
        this.insertion = s;
        this.font = minecraftkey;
    }

    @Nullable
    public ChatHexColor getColor() {
        return this.color;
    }

    public boolean isBold() {
        return this.bold == Boolean.TRUE;
    }

    public boolean isItalic() {
        return this.italic == Boolean.TRUE;
    }

    public boolean isStrikethrough() {
        return this.strikethrough == Boolean.TRUE;
    }

    public boolean isUnderlined() {
        return this.underlined == Boolean.TRUE;
    }

    public boolean isRandom() {
        return this.obfuscated == Boolean.TRUE;
    }

    public boolean g() {
        return this == ChatModifier.a;
    }

    @Nullable
    public ChatClickable getClickEvent() {
        return this.clickEvent;
    }

    @Nullable
    public ChatHoverable getHoverEvent() {
        return this.hoverEvent;
    }

    @Nullable
    public String getInsertion() {
        return this.insertion;
    }

    public MinecraftKey getFont() {
        return this.font != null ? this.font : ChatModifier.b;
    }

    public ChatModifier setColor(@Nullable ChatHexColor chathexcolor) {
        return new ChatModifier(chathexcolor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }

    public ChatModifier setColor(@Nullable EnumChatFormat enumchatformat) {
        return this.setColor(enumchatformat != null ? ChatHexColor.a(enumchatformat) : null);
    }

    public ChatModifier setBold(@Nullable Boolean obool) {
        return new ChatModifier(this.color, obool, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }

    public ChatModifier setItalic(@Nullable Boolean obool) {
        return new ChatModifier(this.color, this.bold, obool, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }

    // CraftBukkit start
    public ChatModifier setStrikethrough(@Nullable Boolean obool) {
        return new ChatModifier(this.color, this.bold, this.italic, this.underlined, obool, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }

    public ChatModifier setUnderline(@Nullable Boolean obool) {
        return new ChatModifier(this.color, this.bold, this.italic, obool, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }

    public ChatModifier setRandom(@Nullable Boolean obool) {
        return new ChatModifier(this.color, this.bold, this.italic, this.underlined, this.strikethrough, obool, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }
    // CraftBukkit end

    public ChatModifier setChatClickable(@Nullable ChatClickable chatclickable) {
        return new ChatModifier(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, chatclickable, this.hoverEvent, this.insertion, this.font);
    }

    public ChatModifier setChatHoverable(@Nullable ChatHoverable chathoverable) {
        return new ChatModifier(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, chathoverable, this.insertion, this.font);
    }

    public ChatModifier setInsertion(@Nullable String s) {
        return new ChatModifier(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, s, this.font);
    }

    public ChatModifier b(EnumChatFormat enumchatformat) {
        ChatHexColor chathexcolor = this.color;
        Boolean obool = this.bold;
        Boolean obool1 = this.italic;
        Boolean obool2 = this.strikethrough;
        Boolean obool3 = this.underlined;
        Boolean obool4 = this.obfuscated;

        switch (enumchatformat) {
            case OBFUSCATED:
                obool4 = true;
                break;
            case BOLD:
                obool = true;
                break;
            case STRIKETHROUGH:
                obool2 = true;
                break;
            case UNDERLINE:
                obool3 = true;
                break;
            case ITALIC:
                obool1 = true;
                break;
            case RESET:
                return ChatModifier.a;
            default:
                chathexcolor = ChatHexColor.a(enumchatformat);
        }

        return new ChatModifier(chathexcolor, obool, obool1, obool3, obool2, obool4, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }

    public ChatModifier a(EnumChatFormat... aenumchatformat) {
        ChatHexColor chathexcolor = this.color;
        Boolean obool = this.bold;
        Boolean obool1 = this.italic;
        Boolean obool2 = this.strikethrough;
        Boolean obool3 = this.underlined;
        Boolean obool4 = this.obfuscated;
        EnumChatFormat[] aenumchatformat1 = aenumchatformat;
        int i = aenumchatformat.length;

        for (int j = 0; j < i; ++j) {
            EnumChatFormat enumchatformat = aenumchatformat1[j];

            switch (enumchatformat) {
                case OBFUSCATED:
                    obool4 = true;
                    break;
                case BOLD:
                    obool = true;
                    break;
                case STRIKETHROUGH:
                    obool2 = true;
                    break;
                case UNDERLINE:
                    obool3 = true;
                    break;
                case ITALIC:
                    obool1 = true;
                    break;
                case RESET:
                    return ChatModifier.a;
                default:
                    chathexcolor = ChatHexColor.a(enumchatformat);
            }
        }

        return new ChatModifier(chathexcolor, obool, obool1, obool3, obool2, obool4, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }

    public ChatModifier setChatModifier(ChatModifier chatmodifier) {
        return this == ChatModifier.a ? chatmodifier : (chatmodifier == ChatModifier.a ? this : new ChatModifier(this.color != null ? this.color : chatmodifier.color, this.bold != null ? this.bold : chatmodifier.bold, this.italic != null ? this.italic : chatmodifier.italic, this.underlined != null ? this.underlined : chatmodifier.underlined, this.strikethrough != null ? this.strikethrough : chatmodifier.strikethrough, this.obfuscated != null ? this.obfuscated : chatmodifier.obfuscated, this.clickEvent != null ? this.clickEvent : chatmodifier.clickEvent, this.hoverEvent != null ? this.hoverEvent : chatmodifier.hoverEvent, this.insertion != null ? this.insertion : chatmodifier.insertion, this.font != null ? this.font : chatmodifier.font));
    }

    public String toString() {
        return "Style{ color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", strikethrough=" + this.strikethrough + ", obfuscated=" + this.obfuscated + ", clickEvent=" + this.getClickEvent() + ", hoverEvent=" + this.getHoverEvent() + ", insertion=" + this.getInsertion() + ", font=" + this.getFont() + '}';
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ChatModifier)) {
            return false;
        } else {
            ChatModifier chatmodifier = (ChatModifier) object;

            return this.isBold() == chatmodifier.isBold() && Objects.equals(this.getColor(), chatmodifier.getColor()) && this.isItalic() == chatmodifier.isItalic() && this.isRandom() == chatmodifier.isRandom() && this.isStrikethrough() == chatmodifier.isStrikethrough() && this.isUnderlined() == chatmodifier.isUnderlined() && Objects.equals(this.getClickEvent(), chatmodifier.getClickEvent()) && Objects.equals(this.getHoverEvent(), chatmodifier.getHoverEvent()) && Objects.equals(this.getInsertion(), chatmodifier.getInsertion()) && Objects.equals(this.getFont(), chatmodifier.getFont());
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion});
    }

    public static class ChatModifierSerializer implements JsonDeserializer<ChatModifier>, JsonSerializer<ChatModifier> {

        public ChatModifierSerializer() {}

        @Nullable
        public ChatModifier deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            if (jsonelement.isJsonObject()) {
                JsonObject jsonobject = jsonelement.getAsJsonObject();

                if (jsonobject == null) {
                    return null;
                } else {
                    Boolean obool = a(jsonobject, "bold");
                    Boolean obool1 = a(jsonobject, "italic");
                    Boolean obool2 = a(jsonobject, "underlined");
                    Boolean obool3 = a(jsonobject, "strikethrough");
                    Boolean obool4 = a(jsonobject, "obfuscated");
                    ChatHexColor chathexcolor = e(jsonobject);
                    String s = d(jsonobject);
                    ChatClickable chatclickable = c(jsonobject);
                    ChatHoverable chathoverable = b(jsonobject);
                    MinecraftKey minecraftkey = a(jsonobject);

                    return new ChatModifier(chathexcolor, obool, obool1, obool2, obool3, obool4, chatclickable, chathoverable, s, minecraftkey);
                }
            } else {
                return null;
            }
        }

        @Nullable
        private static MinecraftKey a(JsonObject jsonobject) {
            if (jsonobject.has("font")) {
                String s = ChatDeserializer.h(jsonobject, "font");

                try {
                    return new MinecraftKey(s);
                } catch (ResourceKeyInvalidException resourcekeyinvalidexception) {
                    throw new JsonSyntaxException("Invalid font name: " + s);
                }
            } else {
                return null;
            }
        }

        @Nullable
        private static ChatHoverable b(JsonObject jsonobject) {
            if (jsonobject.has("hoverEvent")) {
                JsonObject jsonobject1 = ChatDeserializer.t(jsonobject, "hoverEvent");
                ChatHoverable chathoverable = ChatHoverable.a(jsonobject1);

                if (chathoverable != null && chathoverable.a().a()) {
                    return chathoverable;
                }
            }

            return null;
        }

        @Nullable
        private static ChatClickable c(JsonObject jsonobject) {
            if (jsonobject.has("clickEvent")) {
                JsonObject jsonobject1 = ChatDeserializer.t(jsonobject, "clickEvent");
                String s = ChatDeserializer.a(jsonobject1, "action", (String) null);
                ChatClickable.EnumClickAction chatclickable_enumclickaction = s == null ? null : ChatClickable.EnumClickAction.a(s);
                String s1 = ChatDeserializer.a(jsonobject1, "value", (String) null);

                if (chatclickable_enumclickaction != null && s1 != null && chatclickable_enumclickaction.a()) {
                    return new ChatClickable(chatclickable_enumclickaction, s1);
                }
            }

            return null;
        }

        @Nullable
        private static String d(JsonObject jsonobject) {
            return ChatDeserializer.a(jsonobject, "insertion", (String) null);
        }

        @Nullable
        private static ChatHexColor e(JsonObject jsonobject) {
            if (jsonobject.has("color")) {
                String s = ChatDeserializer.h(jsonobject, "color");

                return ChatHexColor.a(s);
            } else {
                return null;
            }
        }

        @Nullable
        private static Boolean a(JsonObject jsonobject, String s) {
            return jsonobject.has(s) ? jsonobject.get(s).getAsBoolean() : null;
        }

        @Nullable
        public JsonElement serialize(ChatModifier chatmodifier, Type type, JsonSerializationContext jsonserializationcontext) {
            if (chatmodifier.g()) {
                return null;
            } else {
                JsonObject jsonobject = new JsonObject();

                if (chatmodifier.bold != null) {
                    jsonobject.addProperty("bold", chatmodifier.bold);
                }

                if (chatmodifier.italic != null) {
                    jsonobject.addProperty("italic", chatmodifier.italic);
                }

                if (chatmodifier.underlined != null) {
                    jsonobject.addProperty("underlined", chatmodifier.underlined);
                }

                if (chatmodifier.strikethrough != null) {
                    jsonobject.addProperty("strikethrough", chatmodifier.strikethrough);
                }

                if (chatmodifier.obfuscated != null) {
                    jsonobject.addProperty("obfuscated", chatmodifier.obfuscated);
                }

                if (chatmodifier.color != null) {
                    jsonobject.addProperty("color", chatmodifier.color.b());
                }

                if (chatmodifier.insertion != null) {
                    jsonobject.add("insertion", jsonserializationcontext.serialize(chatmodifier.insertion));
                }

                if (chatmodifier.clickEvent != null) {
                    JsonObject jsonobject1 = new JsonObject();

                    jsonobject1.addProperty("action", chatmodifier.clickEvent.a().b());
                    jsonobject1.addProperty("value", chatmodifier.clickEvent.b());
                    jsonobject.add("clickEvent", jsonobject1);
                }

                if (chatmodifier.hoverEvent != null) {
                    jsonobject.add("hoverEvent", chatmodifier.hoverEvent.b());
                }

                if (chatmodifier.font != null) {
                    jsonobject.addProperty("font", chatmodifier.font.toString());
                }

                return jsonobject;
            }
        }
    }
}
