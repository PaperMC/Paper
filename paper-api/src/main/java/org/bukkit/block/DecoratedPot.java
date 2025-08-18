package org.bukkit.block;

import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.DecoratedPotInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of a decorated pot.
 */
public interface DecoratedPot extends io.papermc.paper.block.TileStateInventoryHolder, org.bukkit.loot.Lootable { // Paper - expose loot table & TileStateInventoryHolder

    /**
     * Set the sherd on the provided side.
     *
     * @param side the side to set
     * @param sherd the sherd, or null to set a blank side.
     * @throws IllegalArgumentException if the sherd is not either
     * tagged by {@link Tag#ITEMS_DECORATED_POT_SHERDS}, {@link Material#BRICK},
     * or {@code null}
     */
    public void setSherd(@NotNull Side side, @Nullable Material sherd);

    /**
     * Get the sherd on the provided side.
     *
     * @param side the side to get
     * @return the sherd on the side or {@link Material#BRICK} if it's blank
     */
    @NotNull
    public Material getSherd(@NotNull Side side);

    /**
     * Gets a Map of all sides on this decorated pot and the sherds on them.
     * If a side does not have a specific sherd on it, {@link Material#BRICK}
     * will be the value of that side.
     *
     * @return the sherds
     */
    @NotNull
    public Map<Side, Material> getSherds();

    /**
     * Gets the sherds on this decorated pot. For faces without a specific sherd,
     * {@link Material#BRICK} is used in its place.
     *
     * @return the sherds
     * @deprecated in favor of {@link #getSherds()}
     */
    @Deprecated(since = "1.20.1")
    @NotNull
    public List<Material> getShards();

    // Paper - move docs to TileStateInventoryHolder
    @NotNull
    @Override
    public DecoratedPotInventory getInventory();

    @Override // Paper - move docs to TileStateInventoryHolder
    @NotNull
    public DecoratedPotInventory getSnapshotInventory();

    /**
     * Run the specified animation on the decorated pot.
     *
     * @param style the animation style
     */
    public void startWobble(@org.jspecify.annotations.NonNull Wobble style);

    /**
     * A side on a decorated pot. Sides are relative to the facing state of a
     * {@link org.bukkit.block.data.type.DecoratedPot}.
     */
    public static enum Side {
        BACK,
        LEFT,
        RIGHT,
        FRONT
    }

    /**
     * An animation style of decorated pot.
     */
    public static enum Wobble {
        POSITIVE,
        NEGATIVE
    }
}
