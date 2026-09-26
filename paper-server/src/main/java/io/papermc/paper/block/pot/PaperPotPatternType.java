package io.papermc.paper.block.pot;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import org.bukkit.craftbukkit.CraftRegistry;

public class PaperPotPatternType extends HolderableBase<net.minecraft.world.level.block.entity.DecoratedPotPattern> implements PotPatternType {

    public PaperPotPatternType(final Holder<DecoratedPotPattern> holder) {
        super(holder);
    }

    public static PotPatternType minecraftHolderToBukkit(final Holder<DecoratedPotPattern> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.DECORATED_POT_PATTERN);
    }

    public static Holder<DecoratedPotPattern> bukkitToMinecraftHolder(final PotPatternType bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }
}
