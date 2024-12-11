package org.bukkit.craftbukkit.tag;

import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.entity.EntityType;

public class CraftEntityTag extends CraftTag<net.minecraft.world.entity.EntityType<?>, EntityType> {

    public CraftEntityTag(Registry<net.minecraft.world.entity.EntityType<?>> registry, TagKey<net.minecraft.world.entity.EntityType<?>> tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(EntityType entity) {
        return CraftEntityType.bukkitToMinecraft(entity).is(this.tag);
    }

    @Override
    public Set<EntityType> getValues() {
        return this.getHandle().stream().map(Holder::value).map(CraftEntityType::minecraftToBukkit).collect(Collectors.toUnmodifiableSet());
    }
}
