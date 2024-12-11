package io.papermc.paper.configuration.transformation.global;

import com.mojang.logging.LogUtils;
import io.papermc.paper.configuration.Configuration;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import static org.spongepowered.configurate.NodePath.path;

public final class LegacyPaperConfig {
    private static final Logger LOGGER = LogUtils.getClassLogger();

    private LegacyPaperConfig() {
    }

    public static ConfigurationTransformation transformation(final YamlConfiguration spigotConfiguration) {
        return ConfigurationTransformation.chain(versioned(), notVersioned(spigotConfiguration));
    }

    // Represents version transforms lifted directly from the old PaperConfig class
    // must be run BEFORE the "settings" flatten
    private static ConfigurationTransformation.Versioned versioned() {
        return ConfigurationTransformation.versionedBuilder()
            .versionKey(Configuration.LEGACY_CONFIG_VERSION_FIELD)
            .addVersion(11, ConfigurationTransformation.builder().addAction(path("settings", "play-in-use-item-spam-threshold"), TransformAction.rename("incoming-packet-spam-threshold")).build())
            .addVersion(14, ConfigurationTransformation.builder().addAction(path("settings", "spam-limiter", "tab-spam-increment"), (path, value) -> {
                if (value.getInt() == 10) {
                    value.set(2);
                }
                return null;
            }).build())
            .addVersion(15, ConfigurationTransformation.builder().addAction(path("settings"), (path, value) -> {
                value.node("async-chunks", "threads").set(-1);
                return null;
            }).build())
            .addVersion(21, ConfigurationTransformation.builder().addAction(path("use-display-name-in-quit-message"), (path, value) -> new Object[]{"settings", "use-display-name-in-quit-message"}).build())
            .addVersion(23, ConfigurationTransformation.builder().addAction(path("settings", "chunk-loading", "global-max-chunk-load-rate"), (path, value) -> {
                if (value.getDouble() == 300.0) {
                    value.set(-1.0);
                }
                return null;
            }).build())
            .addVersion(25, ConfigurationTransformation.builder().addAction(path("settings", "chunk-loading", "player-max-concurrent-loads"), (path, value) -> {
                if (value.getDouble() == 4.0) {
                    value.set(20.0);
                }
                return null;
            }).build())
            .build();
    }

    // other non-versioned transforms found in PaperConfig
    // must be run BEFORE the "settings" flatten
    private static ConfigurationTransformation notVersioned(final YamlConfiguration spigotConfiguration) {
        return ConfigurationTransformation.builder()
            .addAction(path("settings"), (path, value) -> {
                final ConfigurationNode node = value.node("async-chunks");
                if (node.hasChild("load-threads")) {
                    if (!node.hasChild("threads")) {
                        node.node("threads").set(node.node("load-threads").getInt());
                    }
                    node.removeChild("load-threads");
                }
                node.removeChild("generation");
                node.removeChild("enabled");
                node.removeChild("thread-per-world-generation");
                return null;
            })
            .addAction(path("allow-perm-block-break-exploits"), (path, value) -> new Object[]{"settings", "unsupported-settings", "allow-permanent-block-break-exploits"})
            .addAction(path("settings", "unsupported-settings", "allow-tnt-duplication"), TransformAction.rename("allow-piston-duplication"))
            .addAction(path("settings", "save-player-data"), (path, value) -> {
                final Object val = value.raw();
                if (val instanceof Boolean bool) {
                    spigotConfiguration.set("players.disable-saving", !bool);
                }
                value.raw(null);
                return null;
            })
            .addAction(path("settings", "log-named-entity-deaths"), (path, value) -> {
                final Object val = value.raw();
                if (val instanceof Boolean bool && !bool) {
                    spigotConfiguration.set("settings.log-named-deaths", false);
                }
                value.raw(null);
                return null;
            })
            .build();
    }

    // transforms to new format with configurate
    // must be run AFTER the "settings" flatten
    public static ConfigurationTransformation toNewFormat() {
        return ConfigurationTransformation.chain(
            ConfigurationTransformation.versionedBuilder().versionKey(Configuration.LEGACY_CONFIG_VERSION_FIELD).addVersion(Configuration.FINAL_LEGACY_VERSION + 1, newFormatTransformation()).build(),
            ConfigurationTransformation.builder().addAction(path(Configuration.LEGACY_CONFIG_VERSION_FIELD), TransformAction.rename(Configuration.VERSION_FIELD)).build() // rename to _version to place at the top
        );
    }

    private static ConfigurationTransformation newFormatTransformation() {
        final ConfigurationTransformation.Builder builder = ConfigurationTransformation.builder()
            .addAction(path("verbose"), TransformAction.remove()) // not needed
            .addAction(path("unsupported-settings", "allow-headless-pistons-readme"), TransformAction.remove())
            .addAction(path("unsupported-settings", "allow-permanent-block-break-exploits-readme"), TransformAction.remove())
            .addAction(path("unsupported-settings", "allow-piston-duplication-readme"), TransformAction.remove())
            .addAction(path("packet-limiter", "limits", "all"), (path, value) -> new Object[]{"packet-limiter", "all-packets"})
            .addAction(path("packet-limiter", "limits"), (path, value) -> new Object[]{"packet-limiter", "overrides"})
            .addAction(path("packet-limiter", "overrides", ConfigurationTransformation.WILDCARD_OBJECT), (path, value) -> {
                final Object keyValue = value.key();
                if (keyValue != null && keyValue.toString().equals("PacketPlayInAutoRecipe")) { // add special cast to handle the default for moj-mapped servers that upgrade the config
                    return path.with(path.size() - 1, ServerboundPlaceRecipePacket.class.getSimpleName()).array();
                }
                return null;
            }).addAction(path("loggers"), TransformAction.rename("logging"));

        moveFromRootAndRename(builder, "incoming-packet-spam-threshold", "incoming-packet-threshold", "spam-limiter");

        moveFromRoot(builder, "save-empty-scoreboard-teams", "scoreboards");
        moveFromRoot(builder, "track-plugin-scoreboards", "scoreboards");

        moveFromRoot(builder, "suggest-player-names-when-null-tab-completions", "commands");
        moveFromRoot(builder, "time-command-affects-all-worlds", "commands");
        moveFromRoot(builder, "fix-target-selector-tag-completion", "commands");

        moveFromRoot(builder, "log-player-ip-addresses", "loggers");

        moveFromRoot(builder, "use-display-name-in-quit-message", "messages");

        moveFromRootAndRename(builder, "console-has-all-permissions", "has-all-permissions", "console");

        moveFromRootAndRename(builder, "bungee-online-mode", "online-mode", "proxies", "bungee-cord");
        moveFromRootAndRename(builder, "velocity-support", "velocity", "proxies");

        moveFromRoot(builder, "book-size", "item-validation");
        moveFromRoot(builder, "resolve-selectors-in-books", "item-validation");

        moveFromRoot(builder, "enable-player-collisions", "collisions");
        moveFromRoot(builder, "send-full-pos-for-hard-colliding-entities", "collisions");

        moveFromRootAndRename(builder, "player-auto-save-rate", "rate", "player-auto-save");
        moveFromRootAndRename(builder, "max-player-auto-save-per-tick", "max-per-tick", "player-auto-save");

        moveFromRootToMisc(builder, "max-joins-per-tick");
        moveFromRootToMisc(builder, "fix-entity-position-desync");
        moveFromRootToMisc(builder, "load-permissions-yml-before-plugins");
        moveFromRootToMisc(builder, "region-file-cache-size");
        moveFromRootToMisc(builder, "use-alternative-luck-formula");
        moveFromRootToMisc(builder, "lag-compensate-block-breaking");
        moveFromRootToMisc(builder, "use-dimension-type-for-custom-spawners");

        moveFromRoot(builder, "proxy-protocol", "proxies");

        miniMessageWithTranslatable(builder, String::isBlank, "multiplayer.disconnect.authservers_down", "messages", "kick", "authentication-servers-down");
        miniMessageWithTranslatable(builder, Predicate.isEqual("Flying is not enabled on this server"), "multiplayer.disconnect.flying", "messages", "kick", "flying-player");
        miniMessageWithTranslatable(builder, Predicate.isEqual("Flying is not enabled on this server"), "multiplayer.disconnect.flying", "messages", "kick", "flying-vehicle");
        miniMessage(builder, "messages", "kick", "connection-throttle");
        miniMessage(builder, "messages", "no-permission");
        miniMessageWithTranslatable(builder, Predicate.isEqual("&cSent too many packets"), Component.translatable("disconnect.exceeded_packet_rate", NamedTextColor.RED), "packet-limiter", "kick-message");

        return builder.build();
    }

    private static void miniMessageWithTranslatable(final ConfigurationTransformation.Builder builder, final Predicate<String> englishCheck, final String i18nKey, final String... strPath) {
        miniMessageWithTranslatable(builder, englishCheck, Component.translatable(i18nKey), strPath);
    }
    private static void miniMessageWithTranslatable(final ConfigurationTransformation.Builder builder, final Predicate<String> englishCheck, final Component component, final String... strPath) {
        builder.addAction(path((Object[]) strPath), (path, value) -> {
            final Object val = value.raw();
            if (val != null) {
                final String strVal = val.toString();
                if (!englishCheck.test(strVal)) {
                    value.set(miniMessage(strVal));
                    return null;
                }
            }
            value.set(MiniMessage.miniMessage().serialize(component));
            return null;
        });
    }

    private static void miniMessage(final ConfigurationTransformation.Builder builder, final String... strPath) {
        builder.addAction(path((Object[]) strPath), (path, value) -> {
            final Object val = value.raw();
            if (val != null) {
                value.set(miniMessage(val.toString()));
            }
            return null;
        });
    }

    @SuppressWarnings("deprecation") // valid use to convert legacy string to mini-message in legacy migration
    private static String miniMessage(final String input) {
        return MiniMessage.miniMessage().serialize(LegacyComponentSerializer.legacySection().deserialize(ChatColor.translateAlternateColorCodes('&', input)));
    }

    private static void moveFromRootToMisc(final ConfigurationTransformation.Builder builder, final String key) {
        moveFromRoot(builder, key, "misc");
    }

    private static void moveFromRoot(final ConfigurationTransformation.Builder builder, final String key, final String... parents) {
        moveFromRootAndRename(builder, key, key, parents);
    }

    private static void moveFromRootAndRename(final ConfigurationTransformation.Builder builder, final String oldKey, final String newKey, final String... parents) {
        builder.addAction(path(oldKey), (path, value) -> {
            final Object[] newPath = new Object[parents.length + 1];
            newPath[parents.length] = newKey;
            System.arraycopy(parents, 0, newPath, 0, parents.length);
            return newPath;
        });
    }
}
