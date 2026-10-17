package io.papermc.paper.loot.number;

import java.util.Optional;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.bukkit.craftbukkit.CraftLootContext;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.loot.LootContext;

public record PaperResolvableInt(
    net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt impl
) implements ResolvableInt, Handleable<net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt> {

    @Override
    public int resolve(final LootContext context, final int defaultValue) {
        LootParams lootParams = CraftLootContext.createLootParams(context, LootContextParamSets.ALL_PARAMS_OPTIONAL, false);
        net.minecraft.world.level.storage.loot.LootContext vanillaContext = new net.minecraft.world.level.storage.loot.LootContext.Builder(lootParams).create(Optional.empty());
        return this.impl.get(vanillaContext, defaultValue);
    }

    @Override
    public net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt getHandle() {
        return this.impl;
    }

    public static ResolvableInt fromVanilla(net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt vanilla) {
        if (vanilla instanceof net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt.Constant(
            int value
        )) {
            return new Constant(value);
        }
        return new PaperResolvableInt(vanilla);
    }

    public record Constant(int value) implements ResolvableInt.Constant {
        @Override
        public int getValue() {
            return this.value;
        }
    }
}
