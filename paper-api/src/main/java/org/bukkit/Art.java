package org.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Locale;
import org.bukkit.packs.DataPack;
import org.bukkit.util.OldEnum;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the art on a painting.
 * <p>
 * The arts listed in this interface are present in the default server
 * or can be enabled via a {@link FeatureFlag}.
 * There may be additional arts present in the server, for example from a {@link DataPack}
 * which can be accessed via {@link Registry#ART}.
 */
public interface Art extends OldEnum<Art>, Keyed {

    Art KEBAB = getArt("kebab");
    Art AZTEC = getArt("aztec");
    Art ALBAN = getArt("alban");
    Art AZTEC2 = getArt("aztec2");
    Art BOMB = getArt("bomb");
    Art PLANT = getArt("plant");
    Art WASTELAND = getArt("wasteland");
    Art POOL = getArt("pool");
    Art COURBET = getArt("courbet");
    Art SEA = getArt("sea");
    Art SUNSET = getArt("sunset");
    Art CREEBET = getArt("creebet");
    Art WANDERER = getArt("wanderer");
    Art GRAHAM = getArt("graham");
    Art MATCH = getArt("match");
    Art BUST = getArt("bust");
    Art STAGE = getArt("stage");
    Art VOID = getArt("void");
    Art SKULL_AND_ROSES = getArt("skull_and_roses");
    Art WITHER = getArt("wither");
    Art FIGHTERS = getArt("fighters");
    Art POINTER = getArt("pointer");
    Art PIGSCENE = getArt("pigscene");
    Art BURNING_SKULL = getArt("burning_skull");
    Art SKELETON = getArt("skeleton");
    Art DONKEY_KONG = getArt("donkey_kong");
    Art EARTH = getArt("earth");
    Art WIND = getArt("wind");
    Art WATER = getArt("water");
    Art FIRE = getArt("fire");
    Art BAROQUE = getArt("baroque");
    Art HUMBLE = getArt("humble");
    Art MEDITATIVE = getArt("meditative");
    Art PRAIRIE_RIDE = getArt("prairie_ride");
    Art UNPACKED = getArt("unpacked");
    Art BACKYARD = getArt("backyard");
    Art BOUQUET = getArt("bouquet");
    Art CAVEBIRD = getArt("cavebird");
    Art CHANGING = getArt("changing");
    Art COTAN = getArt("cotan");
    Art ENDBOSS = getArt("endboss");
    Art FERN = getArt("fern");
    Art FINDING = getArt("finding");
    Art LOWMIST = getArt("lowmist");
    Art ORB = getArt("orb");
    Art OWLEMONS = getArt("owlemons");
    Art PASSAGE = getArt("passage");
    Art POND = getArt("pond");
    Art SUNFLOWERS = getArt("sunflowers");
    Art TIDES = getArt("tides");

    @NotNull
    private static Art getArt(@NotNull String key) {
        return Registry.ART.getOrThrow(NamespacedKey.minecraft(key));
    }

    /**
     * Gets the width of the painting, in blocks
     *
     * @return The width of the painting, in blocks
     */
    int getBlockWidth();

    /**
     * Gets the height of the painting, in blocks
     *
     * @return The height of the painting, in blocks
     */
    int getBlockHeight();

    /**
     * Get the ID of this painting.
     *
     * @return The ID of this painting
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    int getId();

    // Paper start - deprecate getKey
    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#PAINTING_VARIANT}. Painting variants can exist without a key.
     */
    @Deprecated(since = "1.21", forRemoval = true)
    @Override
    @NotNull NamespacedKey getKey();

    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#PAINTING_VARIANT}. Painting variants can exist without a key.
     */
    @Deprecated(since = "1.21", forRemoval = true)
    @Override
    default net.kyori.adventure.key.@org.jetbrains.annotations.NotNull Key key() {
        return Keyed.super.key();
    }
    // Paper end - deprecate getKey

    /**
     * Get a painting by its numeric ID
     *
     * @param id The ID
     * @return The painting
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    @Nullable
    static Art getById(int id) {
        for (Art art : Registry.ART) {
            if (id == art.getId()) {
                return art;
            }
        }

        return null;
    }

    /**
     * Get a painting by its unique name
     * <p>
     * This ignores capitalization
     *
     * @param name The name
     * @return The painting
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
     */
    @Deprecated(since = "1.21.3")
    @Nullable
    static Art getByName(@NotNull String name) {
        Preconditions.checkArgument(name != null, "Name cannot be null");

        return Bukkit.getUnsafe().get(Registry.ART, NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
    }

    /**
     * @param name of the art.
     * @return the art with the given name.
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
     */
    @NotNull
    @Deprecated(since = "1.21.3")
    static Art valueOf(@NotNull String name) {
        Art art = Bukkit.getUnsafe().get(Registry.ART, NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
        Preconditions.checkArgument(art != null, "No art found with the name %s", name);
        return art;
    }

    /**
     * @return an array of all known arts.
     * @deprecated use {@link Registry#iterator()}.
     */
    @NotNull
    @Deprecated(since = "1.21.3")
    static Art[] values() {
        return Lists.newArrayList(Registry.ART).toArray(new Art[0]);
    }
}
