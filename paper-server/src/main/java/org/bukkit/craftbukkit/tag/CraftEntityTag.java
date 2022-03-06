package org.bukkit.craftbukkit.tag;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;

public class CraftEntityTag extends CraftTag<EntityTypes<?>, EntityType> {

    public CraftEntityTag(IRegistry<EntityTypes<?>> registry, TagKey<EntityTypes<?>> tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(EntityType entity) {
        return registry.getHolderOrThrow(ResourceKey.create(IRegistry.ENTITY_TYPE_REGISTRY, CraftNamespacedKey.toMinecraft(entity.getKey()))).is(tag);
    }

    @Override
    public Set<EntityType> getValues() {
        return getHandle().stream().map((nms) -> Registry.ENTITY_TYPE.get(CraftNamespacedKey.fromMinecraft(EntityTypes.getKey(nms.value())))).filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());
    }
}
