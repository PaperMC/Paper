package io.papermc.paper.world;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.CraftRegistry;

public class PaperTimeline extends HolderableBase<net.minecraft.world.timeline.Timeline> implements Timeline {

    public static Timeline minecraftHolderToBukkit(final Holder<net.minecraft.world.timeline.Timeline> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.TIMELINE);
    }

    public static Holder<net.minecraft.world.timeline.Timeline> bukkitToMinecraftHolder(final Timeline bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public PaperTimeline(final Holder<net.minecraft.world.timeline.Timeline> holder) {
        super(holder);
    }
}
