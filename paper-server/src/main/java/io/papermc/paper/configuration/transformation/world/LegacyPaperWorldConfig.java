package io.papermc.paper.configuration.transformation.world;

import io.papermc.paper.configuration.Configuration;
import io.papermc.paper.configuration.WorldConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import static io.papermc.paper.configuration.transformation.Transformations.moveFromRoot;
import static io.papermc.paper.configuration.transformation.Transformations.moveFromRootAndRename;
import static org.spongepowered.configurate.NodePath.path;

public final class LegacyPaperWorldConfig {

    private LegacyPaperWorldConfig() {
    }

    public static ConfigurationTransformation transformation() {
        return ConfigurationTransformation.chain(versioned(), notVersioned());
    }

    private static ConfigurationTransformation.Versioned versioned() {
        return ConfigurationTransformation.versionedBuilder().versionKey(Configuration.LEGACY_CONFIG_VERSION_FIELD)
            .addVersion(13, ConfigurationTransformation.builder().addAction(path("enable-old-tnt-cannon-behaviors"), TransformAction.rename("prevent-tnt-from-moving-in-water")).build())
            .addVersion(16, ConfigurationTransformation.builder().addAction(path("use-chunk-inhabited-timer"), (path, value) -> {
                if (!value.getBoolean(true)) {
                    value.raw(0);
                } else {
                    value.raw(-1);
                }
                final Object[] newPath = path.array();
                newPath[newPath.length - 1] = "fixed-chunk-inhabited-time";
                return newPath;
            }).build())
            .addVersion(18, ConfigurationTransformation.builder().addAction(path("nether-ceiling-void-damage"), (path, value) -> {
                if (value.getBoolean(false)) {
                    value.raw(128);
                } else {
                    value.raw(0);
                }
                final Object[] newPath = path.array();
                newPath[newPath.length - 1] = "nether-ceiling-void-damage-height";
                return newPath;
            }).build())
            .addVersion(19, ConfigurationTransformation.builder()
                .addAction(path("anti-xray", "hidden-blocks"), (path, value) -> {
                    final List<String> hiddenBlocks = value.getList(String.class);
                    if (hiddenBlocks != null) {
                        hiddenBlocks.remove("lit_redstone_ore");
                    }
                    return null;
                })
                .addAction(path("anti-xray", "replacement-blocks"), (path, value) -> {
                    final List<String> replacementBlocks = value.getList(String.class);
                    if (replacementBlocks != null) {
                        final int index = replacementBlocks.indexOf("planks");
                        if (index != -1) {
                            replacementBlocks.set(index, "oak_planks");
                        }
                    }
                    value.raw(replacementBlocks);
                    return null;
                }).build())
            .addVersion(20, ConfigurationTransformation.builder().addAction(path("baby-zombie-movement-speed"), TransformAction.rename("baby-zombie-movement-modifier")).build())
            .addVersion(22, ConfigurationTransformation.builder().addAction(path("per-player-mob-spawns"), (path, value) -> {
                value.raw(true);
                return null;
            }).build())
            .addVersion(24,
                ConfigurationTransformation.builder()
                    .addAction(path("spawn-limits", "monsters"), TransformAction.rename("monster"))
                    .addAction(path("spawn-limits", "animals"), TransformAction.rename("creature"))
                    .addAction(path("spawn-limits", "water-animals"), TransformAction.rename("water_creature"))
                    .addAction(path("spawn-limits", "water-ambient"), TransformAction.rename("water_ambient"))
                    .build(),
                ConfigurationTransformation.builder().addAction(path("despawn-ranges"), (path, value) -> {
                    final int softDistance = value.node("soft").getInt(32);
                    final int hardDistance = value.node("hard").getInt(128);
                    value.node("soft").raw(null);
                    value.node("hard").raw(null);
                    for (final MobCategory category : MobCategory.values()) {
                        if (softDistance != 32) {
                            value.node(category.getName(), "soft").raw(softDistance);
                        }
                        if (hardDistance != 128) {
                            value.node(category.getName(), "hard").raw(hardDistance);
                        }
                    }
                    return null;
                }).build()
            )
            .addVersion(26, ConfigurationTransformation.builder().addAction(path("alt-item-despawn-rate", "items", ConfigurationTransformation.WILDCARD_OBJECT), (path, value) -> {
                String itemName = path.get(path.size() - 1).toString();
                final Optional<Holder.Reference<Item>> item = BuiltInRegistries.ITEM.get(ResourceKey.create(Registries.ITEM, ResourceLocation.parse(itemName.toLowerCase(Locale.ROOT))));
                if (item.isEmpty()) {
                    itemName = Material.valueOf(itemName).getKey().getKey();
                }
                final Object[] newPath = path.array();
                newPath[newPath.length - 1] = itemName;
                return newPath;
            }).build())
            .addVersion(27, ConfigurationTransformation.builder().addAction(path("use-faster-eigencraft-redstone"), (path, value) -> {
                final WorldConfiguration.Misc.RedstoneImplementation redstoneImplementation = value.getBoolean(false) ? WorldConfiguration.Misc.RedstoneImplementation.EIGENCRAFT : WorldConfiguration.Misc.RedstoneImplementation.VANILLA;
                value.set(redstoneImplementation);
                final Object[] newPath = path.array();
                newPath[newPath.length - 1] = "redstone-implementation";
                return newPath;
            }).build())
            .build();
    }

    // other transformations found in PaperWorldConfig that aren't versioned
    private static ConfigurationTransformation notVersioned() {
        return ConfigurationTransformation.builder()
            .addAction(path("treasure-maps-return-already-discovered"), (path, value) -> {
                boolean prevValue = value.getBoolean(false);
                value.node("villager-trade").set(prevValue);
                value.node("loot-tables").set(prevValue);
                return path.with(path.size() - 1, "treasure-maps-find-already-discovered").array();
            })
            .addAction(path("alt-item-despawn-rate", "items"), (path, value) -> {
                if (value.isMap()) {
                    Map<String, Integer> rebuild = new HashMap<>();
                    value.childrenMap().forEach((key, node) -> {
                        String itemName = key.toString();
                        final Optional<Holder.Reference<Item>> itemHolder = BuiltInRegistries.ITEM.get(ResourceKey.create(Registries.ITEM, ResourceLocation.parse(itemName.toLowerCase(Locale.ROOT))));
                        final String item;
                        if (itemHolder.isEmpty()) {
                            final Material bukkitMat = Material.matchMaterial(itemName);
                            item = bukkitMat != null ? bukkitMat.getKey().getKey() : null;
                        } else {
                            item = itemHolder.get().unwrapKey().orElseThrow().location().getPath();
                        }
                        if (item != null) {
                            rebuild.put(item, node.getInt());
                        }
                    });
                    value.set(rebuild);
                }
                return null;
            })
            .build();
    }

    public static ConfigurationTransformation toNewFormat() {
        return ConfigurationTransformation.chain(ConfigurationTransformation.versionedBuilder().versionKey(Configuration.LEGACY_CONFIG_VERSION_FIELD).addVersion(Configuration.FINAL_LEGACY_VERSION + 1, newFormatTransformation()).build(), ConfigurationTransformation.builder().addAction(path(Configuration.LEGACY_CONFIG_VERSION_FIELD), TransformAction.rename(Configuration.VERSION_FIELD)).build());
    }

    private static ConfigurationTransformation newFormatTransformation() {
        final ConfigurationTransformation.Builder builder = ConfigurationTransformation.builder()
            .addAction(path("verbose"), TransformAction.remove()); // not needed

        moveFromRoot(builder, "anti-xray", "anticheat");

        moveFromRootAndRename(builder, "armor-stands-do-collision-entity-lookups", "do-collision-entity-lookups", "entities", "armor-stands");
        moveFromRootAndRename(builder, "armor-stands-tick", "tick", "entities", "armor-stands");

        moveFromRoot(builder, "auto-save-interval", "chunks");
        moveFromRoot(builder, "delay-chunk-unloads-by", "chunks");
        moveFromRoot(builder, "entity-per-chunk-save-limit", "chunks");
        moveFromRoot(builder, "fixed-chunk-inhabited-time", "chunks");
        moveFromRoot(builder, "max-auto-save-chunks-per-tick", "chunks");
        moveFromRoot(builder, "prevent-moving-into-unloaded-chunks", "chunks");

        moveFromRoot(builder, "entities-target-with-follow-range", "entities");
        moveFromRoot(builder, "mob-effects", "entities");

        moveFromRoot(builder, "filter-nbt-data-from-spawn-eggs-and-related", "entities", "spawning");
        moveFromGameMechanics(builder, "disable-mob-spawner-spawn-egg-transformation", "entities", "spawning");
        moveFromRoot(builder, "per-player-mob-spawns", "entities", "spawning");
        moveFromGameMechanics(builder, "scan-for-legacy-ender-dragon", "entities", "spawning");
        moveFromRoot(builder, "spawn-limits", "entities", "spawning");
        moveFromRoot(builder, "despawn-ranges", "entities", "spawning");
        moveFromRoot(builder, "wateranimal-spawn-height", "entities", "spawning");
        builder.addAction(path("slime-spawn-height", "swamp-biome"), TransformAction.rename("surface-biome"));
        moveFromRoot(builder, "slime-spawn-height", "entities", "spawning");
        moveFromRoot(builder, "wandering-trader", "entities", "spawning");
        moveFromRoot(builder, "all-chunks-are-slime-chunks", "entities", "spawning");
        moveFromRoot(builder, "skeleton-horse-thunder-spawn-chance", "entities", "spawning");
        moveFromRoot(builder, "iron-golems-can-spawn-in-air", "entities", "spawning");
        moveFromRoot(builder, "alt-item-despawn-rate", "entities", "spawning");
        moveFromRoot(builder, "count-all-mobs-for-spawning", "entities", "spawning");
        moveFromRoot(builder, "creative-arrow-despawn-rate", "entities", "spawning");
        moveFromRoot(builder, "non-player-arrow-despawn-rate", "entities", "spawning");
        moveFromRoot(builder, "monster-spawn-max-light-level", "entities", "spawning");


        moveFromRootAndRename(builder, "duplicate-uuid-saferegen-delete-range", "safe-regen-delete-range", "entities", "spawning", "duplicate-uuid");

        moveFromRoot(builder, "baby-zombie-movement-modifier", "entities", "behavior");
        moveFromRoot(builder, "disable-creeper-lingering-effect", "entities", "behavior");
        moveFromRoot(builder, "door-breaking-difficulty", "entities", "behavior");
        moveFromGameMechanics(builder, "disable-chest-cat-detection", "entities", "behavior");
        moveFromGameMechanics(builder, "disable-player-crits", "entities", "behavior");
        moveFromRoot(builder, "experience-merge-max-value", "entities", "behavior");
        moveFromRoot(builder, "mobs-can-always-pick-up-loot", "entities", "behavior");
        moveFromGameMechanics(builder, "nerf-pigmen-from-nether-portals", "entities", "behavior");
        moveFromRoot(builder, "parrots-are-unaffected-by-player-movement", "entities", "behavior");
        moveFromRoot(builder, "phantoms-do-not-spawn-on-creative-players", "entities", "behavior");
        moveFromRoot(builder, "phantoms-only-attack-insomniacs", "entities", "behavior");
        moveFromRoot(builder, "piglins-guard-chests", "entities", "behavior");
        moveFromRoot(builder, "spawner-nerfed-mobs-should-jump", "entities", "behavior");
        moveFromRoot(builder, "zombie-villager-infection-chance", "entities", "behavior");
        moveFromRoot(builder, "zombies-target-turtle-eggs", "entities", "behavior");
        moveFromRoot(builder, "ender-dragons-death-always-places-dragon-egg", "entities", "behavior");
        moveFromGameMechanicsAndRename(builder, "disable-pillager-patrols", "disable", "game-mechanics", "pillager-patrols");
        moveFromGameMechanics(builder, "pillager-patrols", "entities", "behavior");
        moveFromRoot(builder, "should-remove-dragon", "entities", "behavior");

        moveFromRootAndRename(builder, "map-item-frame-cursor-limit", "item-frame-cursor-limit", "maps");
        moveFromRootAndRename(builder, "map-item-frame-cursor-update-interval", "item-frame-cursor-update-interval", "maps");

        moveFromRootAndRename(builder, "mob-spawner-tick-rate", "mob-spawner", "tick-rates");
        moveFromRootAndRename(builder, "container-update-tick-rate", "container-update", "tick-rates");
        moveFromRootAndRename(builder, "grass-spread-tick-rate", "grass-spread", "tick-rates");

        moveFromRoot(builder, "allow-non-player-entities-on-scoreboards", "scoreboards");
        moveFromRoot(builder, "use-vanilla-world-scoreboard-name-coloring", "scoreboards");

        moveFromRoot(builder, "disable-thunder", "environment");
        moveFromRoot(builder, "disable-ice-and-snow", "environment");
        moveFromRoot(builder, "optimize-explosions", "environment");
        moveFromRoot(builder, "disable-explosion-knockback", "environment");
        moveFromRoot(builder, "frosted-ice", "environment");
        moveFromRoot(builder, "disable-teleportation-suffocation-check", "environment");
        moveFromRoot(builder, "portal-create-radius", "environment");
        moveFromRoot(builder, "portal-search-radius", "environment");
        moveFromRoot(builder, "portal-search-vanilla-dimension-scaling", "environment");
        moveFromRootAndRename(builder, "enable-treasure-maps", "enabled", "environment", "treasure-maps");
        moveFromRootAndRename(builder, "treasure-maps-find-already-discovered", "find-already-discovered", "environment", "treasure-maps");
        moveFromRoot(builder, "water-over-lava-flow-speed", "environment");
        moveFromRoot(builder, "nether-ceiling-void-damage-height", "environment");

        moveFromRoot(builder, "keep-spawn-loaded", "spawn");
        moveFromRoot(builder, "keep-spawn-loaded-range", "spawn");
        moveFromRoot(builder, "allow-using-signs-inside-spawn-protection", "spawn");

        moveFromRoot(builder, "max-entity-collisions", "collisions");
        moveFromRoot(builder, "allow-vehicle-collisions", "collisions");
        moveFromRoot(builder, "fix-climbing-bypassing-cramming-rule", "collisions");
        moveFromRoot(builder, "only-players-collide", "collisions");
        moveFromRoot(builder, "allow-player-cramming-damage", "collisions");

        moveFromRoot(builder, "falling-block-height-nerf", "fixes");
        moveFromRoot(builder, "fix-items-merging-through-walls", "fixes");
        moveFromRoot(builder, "prevent-tnt-from-moving-in-water", "fixes");
        moveFromRoot(builder, "remove-corrupt-tile-entities", "fixes");
        moveFromRoot(builder, "split-overstacked-loot", "fixes");
        moveFromRoot(builder, "tnt-entity-height-nerf", "fixes");
        moveFromRoot(builder, "fix-wither-targeting-bug", "fixes");
        moveFromGameMechanics(builder, "disable-unloaded-chunk-enderpearl-exploit", "fixes");
        moveFromGameMechanics(builder, "fix-curing-zombie-villager-discount-exploit", "fixes");

        builder.addAction(path("fishing-time-range", "MaximumTicks"), TransformAction.rename("maximum"));
        builder.addAction(path("fishing-time-range", "MinimumTicks"), TransformAction.rename("minimum"));

        builder.addAction(path("generator-settings", "flat-bedrock"), (path, value) -> new Object[]{"environment", "generate-flat-bedrock"});
        builder.addAction(path("generator-settings"), TransformAction.remove());

        builder.addAction(path("game-mechanics", ConfigurationTransformation.WILDCARD_OBJECT), (path, value) -> new Object[]{"misc", path.array()[1]});
        builder.addAction(path("game-mechanics"), TransformAction.remove());

        builder.addAction(path("feature-seeds", ConfigurationTransformation.WILDCARD_OBJECT), (path, value) -> {
            final String key = path.array()[path.size() - 1].toString();
            if (!"generate-random-seeds-for-all".equals(key)) {
                return new Object[]{"feature-seeds", "features", key};
            }
            return null;
        });

        builder.addAction(path("duplicate-uuid-resolver"), (path, value) -> {
            final WorldConfiguration.Entities.Spawning.DuplicateUUID.DuplicateUUIDMode duplicateUUIDMode = switch (value.require(String.class)) {
                case "regen", "regenerate", "saferegen", "saferegenerate" -> WorldConfiguration.Entities.Spawning.DuplicateUUID.DuplicateUUIDMode.SAFE_REGEN;
                case "remove", "delete" -> WorldConfiguration.Entities.Spawning.DuplicateUUID.DuplicateUUIDMode.DELETE;
                case "silent", "nothing" -> WorldConfiguration.Entities.Spawning.DuplicateUUID.DuplicateUUIDMode.NOTHING;
                default -> WorldConfiguration.Entities.Spawning.DuplicateUUID.DuplicateUUIDMode.WARN;
            };
            value.set(duplicateUUIDMode);
            return new Object[]{"entities", "spawning", "duplicate-uuid", "mode"};
        });

        builder.addAction(path("redstone-implementation"), (path, value) -> {
            if ("alternate-current".equalsIgnoreCase(value.require(String.class))) {
                value.set("alternate_current");
            }
            return new Object[]{"misc", "redstone-implementation"};
        });

        moveToMisc(builder, "light-queue-size");
        moveToMisc(builder, "update-pathfinding-on-block-update");
        moveToMisc(builder, "show-sign-click-command-failure-msgs-to-player");
        moveToMisc(builder, "max-leash-distance");

        return builder.build();
    }

    private static void moveToMisc(final ConfigurationTransformation.Builder builder, String... key) {
        moveFromRootAndRename(builder, path((Object[]) key), key[key.length - 1], "misc");
    }

    private static void moveFromGameMechanics(final ConfigurationTransformation.Builder builder, final String key, final String... parents) {
        moveFromGameMechanicsAndRename(builder, key, key, parents);
    }

    private static void moveFromGameMechanicsAndRename(final ConfigurationTransformation.Builder builder, final String oldKey, final String newKey, final String... parents) {
        moveFromRootAndRename(builder, path("game-mechanics", oldKey), newKey, parents);
    }
}
