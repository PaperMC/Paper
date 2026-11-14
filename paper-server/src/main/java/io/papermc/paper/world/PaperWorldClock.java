package io.papermc.paper.world;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.CraftRegistry;

public class PaperWorldClock extends HolderableBase<net.minecraft.world.clock.WorldClock> implements WorldClock {

    public static WorldClock minecraftHolderToBukkit(final Holder<net.minecraft.world.clock.WorldClock> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.WORLD_CLOCK);
    }

    public static Holder<net.minecraft.world.clock.WorldClock> bukkitToMinecraftHolder(final WorldClock bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public PaperWorldClock(final Holder<net.minecraft.world.clock.WorldClock> holder) {
        super(holder);
    }
}
