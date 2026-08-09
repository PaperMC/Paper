package io.papermc.paper.configuration.transformation.global;

import io.papermc.paper.configuration.Configuration;
import io.papermc.paper.configuration.transformation.Transformations;
import org.spongepowered.configurate.ConfigurateException;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import static io.papermc.paper.configuration.transformation.Transformations.move;
import static io.papermc.paper.configuration.transformation.Transformations.moveParent;
import static io.papermc.paper.configuration.transformation.Transformations.prefix;
import static io.papermc.paper.configuration.transformation.Transformations.setRaw;
import static io.papermc.paper.configuration.transformation.Transformations.single;
import static io.papermc.paper.configuration.transformation.global.LegacyPaperConfig.miniMessageWithTranslatable;
import static net.kyori.adventure.text.Component.translatable;
import static org.spongepowered.configurate.NodePath.path;
import static org.spongepowered.configurate.transformation.TransformAction.remove;
import static org.spongepowered.configurate.transformation.TransformAction.rename;
import static org.spongepowered.configurate.transformation.TransformAction.set;

// this will be transforming spigot.yml non-world settings into
// the new config format. That will be merged into the global config
public final class LegacySpigotConfig {

    private LegacySpigotConfig() {}

    public static void migrate(final ConfigurationNode node) throws ConfigurateException {
        transformation().apply(node);
        toNewFormat().apply(node);
        Transformations.pruneEmptySections(node, "settings", "commands", "messages", "stats", "advancements", "players");
        node.removeChild(Configuration.LEGACY_CONFIG_VERSION_FIELD);
    }

    public static ConfigurationTransformation transformation() {
        return ConfigurationTransformation.chain(versioned(), notVersioned());
    }

    // Represents version transforms lifted directly from the old SpigotConfig class
    private static ConfigurationTransformation.Versioned versioned() {
        return ConfigurationTransformation.versionedBuilder()
            .versionKey(Configuration.LEGACY_CONFIG_VERSION_FIELD)
            .addVersion(4, single(settings("bungeecord"), setRaw(false)))
            .addVersion(6, single(commands("tab-complete"), (path, value) -> {
                if (value.getBoolean(true)) {
                    value.raw(0);
                } else {
                    value.raw(-1);
                }
                return null;
            }))
            .addVersion(
                8,
                single(path(messages("outdated-client")), setRaw("Outdated client! Please use {0}")),
                single(path(messages("outdated-server")), setRaw("Outdated server! I'm still on {0}"))
            )
            .build();

    }

    // other non-versioned transforms found in SpigotConfig
    private static ConfigurationTransformation notVersioned() {
        return ConfigurationTransformation.builder()
            .addAction(path("replace-commands"), remove()) // not supported anymore
            .addAction(commands("replace-commands"), remove()) // not supported anymore
            .addAction(commands(), (path, value) -> {
                // adding enabled toggle
                final List<String> spamExclusions = value.node("spam-exclusions").getList(String.class, List.of("/skill"));
                final ConfigurationNode enabledNode = value.node("enable-spam-exclusions");
                if (!enabledNode.virtual()) {
                    return null;
                }
                if (spamExclusions.isEmpty()) {
                    enabledNode.set(false);
                    return null;
                }
                enabledNode.set(spamExclusions.size() > 1 || !"/skill".equals(spamExclusions.getFirst()));
                return null;
            })
            .build();
    }

    public static ConfigurationTransformation toNewFormat() {
        return ConfigurationTransformation.versionedBuilder()
            .versionKey(Configuration.LEGACY_CONFIG_VERSION_FIELD)
            .addVersion(Configuration.FINAL_LEGACY_SPIGOT_VERSION + 1, newFormatTransformation())
            .build();
    }

    private static ConfigurationTransformation newFormatTransformation() {
        return ConfigurationTransformation.builder()
            // commands
            .addAction(commands("log"), move(path("logging", "command-execution")))
            .addAction(commands("send-namespaced"), rename("send-namespaced-commands"))
            .addAction(commands("tab-complete"), (path, value) -> {
                value.raw(value.getInt(0) >= 0); // the int value isn't used anymore, it's only a bool now
                return commands("tab-completion").array();
            })
            .addAction(commands("silent-commandblock-console"), rename("silent-command-block-console"))
            .addAction(commands("spam-exclusions"), move(path("spam-limiter", "commands", "exclusions")))
            .addAction(commands("enable-spam-exclusions"), move(path("spam-limiter", "commands", "enabled")))
            // messages
            .addAction(
                messages("whitelist"),
                move(
                    miniMessageWithTranslatable(
                        Set.of("You are not whitelisted on this server!", "You are not white-listed on this server!")::contains,
                        translatable("multiplayer.disconnect.not_whitelisted")
                    ),
                    messages("kick", "whitelist")
                )
            )
            .addAction(messages("unknown-command"), (path, value) -> {
                // only used to control if there should be a message at all; convert to bool
                value.raw(!value.getString("").isEmpty());
                return messages("send-command-parse-failure-message").array();
            })
            .addAction(messages("server-full"), move(
                miniMessageWithTranslatable("The server is full!"::equals, translatable("multiplayer.disconnect.server_full")),
                messages("kick", "server-full")
            ))
            .addAction(
                messages("outdated-client"),
                move(
                    miniMessage(
                        Set.of("Outdated client! Please use {0}", "Incompatible client! Please use %s")::contains,
                        translatable("multiplayer.disconnect.outdated_client")
                    ),
                    messages("kick", "outdated-client")
                )
            )
            .addAction(
                messages("outdated-server"),
                move(
                    miniMessage(
                        Set.of("Outdated server! I'm still on {0}", "Incompatible client! Please use %s")::contains,
                        translatable("multiplayer.disconnect.outdated_server")
                    ),
                    messages("kick", "outdated-server")
                )
            )
            .addAction(messages("restart"), move(miniMessage(), messages("kick", "restart")))
            // players.disable-saving OK
            // advancements.disable-saving OK
            .addAction(path("advancements", "disabled"), move(path("initialization", "disabled-advancements"))) // read before datapacks load
            // stats.disable-saving OK
            .addAction(path("stats", "forced-stats"), rename("forced-custom-stat-values"))
            // settings
            .addAction(settings("debug"), move(path("logging", "debug")))
            .addAction(settings("bungeecord"), move(path("proxies", "bungee-cord", "enabled")))
            .addAction(settings("log-villager-deaths"), move(path("logging", "villager-deaths")))
            .addAction(settings("log-named-deaths"), move(path("logging", "named-living-entity-deaths")))
            .addAction(settings("moved-wrongly-threshold"), moveParent("anticheat"))
            .addAction(settings("moved-too-quickly-multiplier"), moveParent("anticheat"))
            .addAction(settings("timeout-time"), move(path("watchdog", "timeout-seconds")))
            .addAction(settings("restart-on-crash"), moveParent("watchdog"))
            .addAction(settings("restart-script"), moveParent("watchdog"))
            .addAction(settings("user-cache-size"), moveParent("players"))
            .addAction(settings("save-user-cache-on-stop-only"), moveParent("players"))
            .addAction(settings("netty-threads"), moveParent("misc"))
            .addAction(settings("sample-count"), moveParent("players"))
            .addAction(settings("player-shuffle"), move(path("players", "connection-shuffle")))
            .addAction(settings("attribute", "maxAbsorption", "max"), move(path("attributes", "overrides", "minecraft:max_absorption", "max")))
            .addAction(settings("attribute", "maxHealth", "max"), move(path("attributes", "overrides", "minecraft:max_health", "max")))
            .addAction(settings("attribute", "movementSpeed", "max"), move(path("attributes", "overrides", "minecraft:movement_speed", "max")))
            .addAction(settings("attribute", "attackDamage", "max"), move(path("attributes", "overrides", "minecraft:attack_damage", "max")))
            .build();
    }

    private static NodePath settings(final String... other) {
        return prefix("settings", other);
    }

    private static NodePath commands(final String... other) {
        return prefix("commands", other);
    }

    private static NodePath messages(final String... other) {
        return prefix("messages", other);
    }

    private static TransformAction miniMessage(final Predicate<? super String> englishCheck, final Component component) {
        return (path, value) -> {
            final Object val = value.raw();
            Component outputComponent = component;
            if (val != null) {
                final String strVal = val.toString();
                if (!englishCheck.test(strVal)) {
                    outputComponent = translatable(strVal.replaceAll("\\{\\d+}", "%s"));
                }
            }
            value.set(MiniMessage.miniMessage().serialize(outputComponent));
            return null;
        };
    }

    static TransformAction miniMessage() {
        return (path, value) -> {
            final Object val = value.raw();
            if (val != null) {
                value.set(LegacyPaperConfig.miniMessage(val.toString()));
            }
            return null;
        };
    }
}
