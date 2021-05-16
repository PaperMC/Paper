package io.papermc.paper.datapack;

import java.util.Collection;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface DatapackManager {

    /**
     * Triggers a refresh of the available and selected datapacks. This
     * can find new datapacks, remove old ones, and update the metadata for
     * existing datapacks. Some of these changes will only take effect
     * after the next {@link org.bukkit.Server#reloadData()} or {@code /minecraft:reload}.
     */
    void refreshPacks();

    /**
     * Gets a datapack by name. May require calling {@link #refreshPacks()} before
     * to get the latest pack information.
     *
     * @param name the name/id of the datapack
     * @return the datapack, or null if not found
     */
    @Nullable Datapack getPack(String name);

    /**
     * Gets the available datapacks. May require calling {@link #refreshPacks()} before
     * to get the latest pack information.
     *
     * @return all the packs known to the server
     */
    @Unmodifiable Collection<Datapack> getPacks();

    /**
     * Gets the enabled datapacks. May require calling {@link #refreshPacks()} before
     * to get the latest pack information.
     *
     * @return all the packs which are currently enabled
     */
    @Unmodifiable Collection<Datapack> getEnabledPacks();
}
