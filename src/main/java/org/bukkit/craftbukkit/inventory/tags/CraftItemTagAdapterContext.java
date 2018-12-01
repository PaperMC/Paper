package org.bukkit.craftbukkit.inventory.tags;

import org.bukkit.craftbukkit.inventory.CraftCustomTagTypeRegistry;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagAdapterContext;

public final class CraftItemTagAdapterContext implements ItemTagAdapterContext {

    private final CraftCustomTagTypeRegistry registry;

    public CraftItemTagAdapterContext(CraftCustomTagTypeRegistry registry) {
        this.registry = registry;
    }

    /**
     * Creates a new and empty tag container instance
     *
     * @return the fresh container instance
     */
    @Override
    public CustomItemTagContainer newTagContainer() {
        return new CraftCustomItemTagContainer(this.registry);
    }
}
