package io.papermc.paper.loot.number;

import java.util.Optional;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.bukkit.craftbukkit.CraftLootContext;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.loot.LootContext;

public record PaperResolvableFloat(
    net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat impl
) implements ResolvableFloat, Handleable<net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat> {

    @Override
    public float resolve(final LootContext context, final float defaultValue) {
        LootParams lootParams = CraftLootContext.createLootParams(context, LootContextParamSets.ALL_PARAMS_OPTIONAL, false);
        net.minecraft.world.level.storage.loot.LootContext vanillaContext = new net.minecraft.world.level.storage.loot.LootContext.Builder(lootParams).create(Optional.empty());
        return this.impl.get(vanillaContext, defaultValue);
    }

    @Override
    public net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat getHandle() {
        return this.impl;
    }

    public static ResolvableFloat fromVanilla(net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat vanilla) {
        if (vanilla instanceof net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat.Constant(
            float value
        )) {
            return new PaperResolvableFloat.Constant(value);
        }
        return new PaperResolvableFloat(vanilla);
    }

    record Constant(float value) implements ResolvableFloat.Constant {
        @Override
        public float getValue() {
            return this.value;
        }
    }
}
