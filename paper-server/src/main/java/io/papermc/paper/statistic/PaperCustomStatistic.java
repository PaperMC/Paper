package io.papermc.paper.statistic;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.craftbukkit.CraftRegistry;

public class PaperCustomStatistic extends HolderableBase<ResourceLocation> implements CustomStatistic {

    public static CustomStatistic minecraftToBukkit(final ResourceLocation minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.CUSTOM_STAT);
    }

    public static ResourceLocation bukkitToMinecraft(final CustomStatistic bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public PaperCustomStatistic(final Holder<ResourceLocation> holder) {
        super(holder);
    }

    @Override
    public String translationKey() {
        return "stat." + this.getHandle().toString().replace(':', '.');
    }
}
