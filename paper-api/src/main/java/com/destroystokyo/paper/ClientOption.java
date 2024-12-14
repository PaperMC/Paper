package com.destroystokyo.paper;

import net.kyori.adventure.translation.Translatable;
import net.kyori.adventure.util.Index;
import org.bukkit.inventory.MainHand;
import org.jspecify.annotations.NullMarked;

/**
 * @since 1.15.2
 */
@NullMarked
public final class ClientOption<T> {

    public static final ClientOption<SkinParts> SKIN_PARTS = new ClientOption<>(SkinParts.class);
    public static final ClientOption<Boolean> CHAT_COLORS_ENABLED = new ClientOption<>(Boolean.class);
    public static final ClientOption<ChatVisibility> CHAT_VISIBILITY = new ClientOption<>(ChatVisibility.class);
    public static final ClientOption<String> LOCALE = new ClientOption<>(String.class);
    public static final ClientOption<MainHand> MAIN_HAND = new ClientOption<>(MainHand.class);
    public static final ClientOption<Integer> VIEW_DISTANCE = new ClientOption<>(Integer.class);
    public static final ClientOption<Boolean> TEXT_FILTERING_ENABLED = new ClientOption<>(Boolean.class);
    public static final ClientOption<Boolean> ALLOW_SERVER_LISTINGS = new ClientOption<>(Boolean.class);
    public static final ClientOption<ParticleVisibility> PARTICLE_VISIBILITY = new ClientOption<>(ParticleVisibility.class);

    private final Class<T> type;

    private ClientOption(final Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return this.type;
    }

    public enum ChatVisibility implements Translatable {
        FULL("full"),
        SYSTEM("system"),
        HIDDEN("hidden"),
        /**
         * @deprecated no longer used anymore since 1.15.2, the value fallback
         * to the default value of the setting when unknown on the server.
         * In this case {@link #FULL} will be returned.
         */
        @Deprecated(since = "1.15.2", forRemoval = true)
        UNKNOWN("unknown");

        public static final Index<String, ChatVisibility> NAMES = Index.create(ChatVisibility.class, chatVisibility -> chatVisibility.name);
        private final String name;

        ChatVisibility(final String name) {
            this.name = name;
        }

        @Override
        public String translationKey() {
            if (this == UNKNOWN) {
                throw new UnsupportedOperationException(this.name + " doesn't have a translation key");
            }
            return "options.chat.visibility." + this.name;
        }
    }

    public enum ParticleVisibility implements Translatable {
        ALL("all"),
        DECREASED("decreased"),
        MINIMAL("minimal");

        public static final Index<String, ParticleVisibility> NAMES = Index.create(ParticleVisibility.class, particleVisibility -> particleVisibility.name);
        private final String name;

        ParticleVisibility(final String name) {
            this.name = name;
        }

        @Override
        public String translationKey() {
            return "options.particles." + this.name;
        }
    }
}
