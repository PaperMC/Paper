package org.bukkit.craftbukkit.inventory.tags;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class DeprecatedContainerTagType<Z> implements PersistentDataType<PersistentDataContainer, Z> {

    private final ItemTagType<CustomItemTagContainer, Z> deprecated;

    DeprecatedContainerTagType(ItemTagType<CustomItemTagContainer, Z> deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public Class<Z> getComplexType() {
        return deprecated.getComplexType();
    }

    @Override
    public PersistentDataContainer toPrimitive(Z complex, PersistentDataAdapterContext context) {
        CustomItemTagContainer deprecated = this.deprecated.toPrimitive(complex, new DeprecatedItemAdapterContext(context));
        Preconditions.checkArgument(deprecated instanceof DeprecatedCustomTagContainer, "Could not wrap deprecated API due to foreign CustomItemTagContainer implementation %s", deprecated.getClass().getSimpleName());

        DeprecatedCustomTagContainer tagContainer = (DeprecatedCustomTagContainer) deprecated;
        PersistentDataContainer wrapped = tagContainer.getWrapped();
        Preconditions.checkArgument(wrapped instanceof CraftPersistentDataContainer, "Could not wrap deprecated API due to wrong deprecation wrapper %s", deprecated.getClass().getSimpleName());

        CraftPersistentDataContainer craftTagContainer = (CraftPersistentDataContainer) wrapped;
        return new CraftPersistentDataContainer(craftTagContainer.getRaw(), craftTagContainer.getDataTagTypeRegistry());
    }

    @Override
    public Z fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
        Preconditions.checkArgument(primitive instanceof CraftPersistentDataContainer, "Could not wrap deprecated API due to foreign PersistentMetadataContainer implementation %s", primitive.getClass().getSimpleName());

        return this.deprecated.fromPrimitive(new DeprecatedCustomTagContainer(primitive), new DeprecatedItemAdapterContext(context));
    }
}
