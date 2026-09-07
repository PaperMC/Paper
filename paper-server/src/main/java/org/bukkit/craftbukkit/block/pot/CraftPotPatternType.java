package org.bukkit.craftbukkit.block.pot;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import org.bukkit.block.pot.PotPatternType;
import org.bukkit.craftbukkit.CraftRegistry;

public class CraftPotPatternType extends HolderableBase<net.minecraft.world.level.block.entity.DecoratedPotPattern> implements PotPatternType {

    public CraftPotPatternType(final Holder<DecoratedPotPattern> holder) {
        super(holder);
    }

    public static PotPatternType minecraftHolderToBukkit(Holder<DecoratedPotPattern> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.DECORATED_POT_PATTERN);
    }

    public static Holder<DecoratedPotPattern> bukkitToMinecraftHolder(PotPatternType bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }
}
