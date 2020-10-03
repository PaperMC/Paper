package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public final class ChatHexColor {

    private static final Map<EnumChatFormat, ChatHexColor> a = (Map) Stream.of(EnumChatFormat.values()).filter(EnumChatFormat::d).collect(ImmutableMap.toImmutableMap(Function.identity(), (enumchatformat) -> {
        return new ChatHexColor(enumchatformat.e(), enumchatformat.f());
    }));
    private static final Map<String, ChatHexColor> b = (Map) ChatHexColor.a.values().stream().collect(ImmutableMap.toImmutableMap((chathexcolor) -> {
        return chathexcolor.name;
    }, Function.identity()));
    private final int rgb;
    @Nullable
    public final String name;

    private ChatHexColor(int i, String s) {
        this.rgb = i;
        this.name = s;
    }

    private ChatHexColor(int i) {
        this.rgb = i;
        this.name = null;
    }

    public String b() {
        return this.name != null ? this.name : this.c();
    }

    private String c() {
        return String.format("#%06X", this.rgb);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            ChatHexColor chathexcolor = (ChatHexColor) object;

            return this.rgb == chathexcolor.rgb;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.rgb, this.name});
    }

    public String toString() {
        return this.name != null ? this.name : this.c();
    }

    @Nullable
    public static ChatHexColor a(EnumChatFormat enumchatformat) {
        return (ChatHexColor) ChatHexColor.a.get(enumchatformat);
    }

    public static ChatHexColor a(int i) {
        return new ChatHexColor(i);
    }

    @Nullable
    public static ChatHexColor a(String s) {
        if (s.startsWith("#")) {
            try {
                int i = Integer.parseInt(s.substring(1), 16);

                return a(i);
            } catch (NumberFormatException numberformatexception) {
                return null;
            }
        } else {
            return (ChatHexColor) ChatHexColor.b.get(s);
        }
    }
}
