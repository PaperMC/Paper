package org.bukkit.craftbukkit;

import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.world.WorldClock;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;

public class CraftWorldClock extends HolderableBase<net.minecraft.world.clock.WorldClock> implements WorldClock {

    public static WorldClock minecraftHolderToBukkit(final Holder<net.minecraft.world.clock.WorldClock> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.WORLD_CLOCK);
    }

    public static Holder<net.minecraft.world.clock.WorldClock> bukkitToMinecraftHolder(final WorldClock bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public CraftWorldClock(final Holder<net.minecraft.world.clock.WorldClock> holder) {
        super(holder);
    }
}
