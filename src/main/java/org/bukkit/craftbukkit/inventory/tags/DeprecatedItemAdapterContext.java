package org.bukkit.craftbukkit.inventory.tags;

import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagAdapterContext;
import org.bukkit.persistence.PersistentDataAdapterContext;

public final class DeprecatedItemAdapterContext implements ItemTagAdapterContext {

    private final PersistentDataAdapterContext context;

    public DeprecatedItemAdapterContext(PersistentDataAdapterContext context) {
        this.context = context;
    }

    /**
     * Creates a new and empty tag container instance.
     *
     * @return the fresh container instance
     */
    @Override
    public CustomItemTagContainer newTagContainer() {
        return new DeprecatedCustomTagContainer(context.newPersistentDataContainer());
    }
}
