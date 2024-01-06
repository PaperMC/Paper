package org.bukkit.craftbukkit.inventory.tags;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class DeprecatedContainerTagType<C> implements PersistentDataType<PersistentDataContainer, C> {

    private final ItemTagType<CustomItemTagContainer, C> deprecated;

    DeprecatedContainerTagType(ItemTagType<CustomItemTagContainer, C> deprecated) {
        this.deprecated = deprecated;
    }

    @NotNull
    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @NotNull
    @Override
    public Class<C> getComplexType() {
        return deprecated.getComplexType();
    }

    @NotNull
    @Override
    public PersistentDataContainer toPrimitive(@NotNull C complex, @NotNull PersistentDataAdapterContext context) {
        CustomItemTagContainer deprecated = this.deprecated.toPrimitive(complex, new DeprecatedItemAdapterContext(context));
        Preconditions.checkArgument(deprecated instanceof DeprecatedCustomTagContainer, "Could not wrap deprecated API due to foreign CustomItemTagContainer implementation %s", deprecated.getClass().getSimpleName());

        DeprecatedCustomTagContainer tagContainer = (DeprecatedCustomTagContainer) deprecated;
        PersistentDataContainer wrapped = tagContainer.getWrapped();
        Preconditions.checkArgument(wrapped instanceof CraftPersistentDataContainer, "Could not wrap deprecated API due to wrong deprecation wrapper %s", deprecated.getClass().getSimpleName());

        CraftPersistentDataContainer craftTagContainer = (CraftPersistentDataContainer) wrapped;
        return new CraftPersistentDataContainer(craftTagContainer.getRaw(), craftTagContainer.getDataTagTypeRegistry());
    }

    @NotNull
    @Override
    public C fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
        Preconditions.checkArgument(primitive instanceof CraftPersistentDataContainer, "Could not wrap deprecated API due to foreign PersistentMetadataContainer implementation %s", primitive.getClass().getSimpleName());

        return this.deprecated.fromPrimitive(new DeprecatedCustomTagContainer(primitive), new DeprecatedItemAdapterContext(context));
    }
}
