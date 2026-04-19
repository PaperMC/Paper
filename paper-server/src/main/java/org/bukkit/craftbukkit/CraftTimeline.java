package org.bukkit.craftbukkit;

import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.world.Timeline;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;

public class CraftTimeline extends HolderableBase<net.minecraft.world.timeline.Timeline> implements Timeline {

    public static Timeline minecraftHolderToBukkit(final Holder<net.minecraft.world.timeline.Timeline> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.TIMELINE);
    }

    public static Holder<net.minecraft.world.timeline.Timeline> bukkitToMinecraftHolder(final Timeline bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public CraftTimeline(final Holder<net.minecraft.world.timeline.Timeline> holder) {
        super(holder);
    }
}
