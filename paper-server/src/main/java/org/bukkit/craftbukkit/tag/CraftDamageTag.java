package org.bukkit.craftbukkit.tag;

import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.tags.TagKey;
import org.bukkit.craftbukkit.damage.CraftDamageType;
import org.bukkit.damage.DamageType;

public class CraftDamageTag extends CraftTag<net.minecraft.world.damagesource.DamageType, DamageType> {

    public CraftDamageTag(IRegistry<net.minecraft.world.damagesource.DamageType> registry, TagKey<net.minecraft.world.damagesource.DamageType> tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(DamageType type) {
        return CraftDamageType.bukkitToMinecraftHolder(type).is(tag);
    }

    @Override
    public Set<DamageType> getValues() {
        return getHandle().stream().map(Holder::value).map(CraftDamageType::minecraftToBukkit).collect(Collectors.toUnmodifiableSet());
    }
}
