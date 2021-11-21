package org.bukkit.craftbukkit.tag;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.Tags;
import net.minecraft.world.level.material.FluidType;
import org.bukkit.Fluid;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftFluidTag extends CraftTag<FluidType, Fluid> {

    public CraftFluidTag(Tags<FluidType> registry, MinecraftKey tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(Fluid fluid) {
        return getHandle().contains(CraftMagicNumbers.getFluid(fluid));
    }

    @Override
    public Set<Fluid> getValues() {
        return Collections.unmodifiableSet(getHandle().getValues().stream().map(CraftMagicNumbers::getFluid).collect(Collectors.toSet()));
    }
}
