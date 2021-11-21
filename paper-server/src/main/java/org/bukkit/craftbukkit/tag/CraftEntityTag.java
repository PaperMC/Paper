package org.bukkit.craftbukkit.tag;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.Tags;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;

public class CraftEntityTag extends CraftTag<EntityTypes<?>, EntityType> {

    public CraftEntityTag(Tags<EntityTypes<?>> registry, MinecraftKey tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(EntityType entity) {
        return getHandle().contains(IRegistry.ENTITY_TYPE.get(CraftNamespacedKey.toMinecraft(entity.getKey())));
    }

    @Override
    public Set<EntityType> getValues() {
        return Collections.unmodifiableSet(getHandle().getValues().stream().map((nms) -> Registry.ENTITY_TYPE.get(CraftNamespacedKey.fromMinecraft(EntityTypes.getKey(nms)))).collect(Collectors.toSet()));
    }
}
