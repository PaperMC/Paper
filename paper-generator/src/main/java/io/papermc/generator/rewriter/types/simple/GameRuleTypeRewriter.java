package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.rewriter.types.registry.RegistryFieldRewriter;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.gamerules.GameRule;

public class GameRuleTypeRewriter extends RegistryFieldRewriter<GameRule<?>> {

    public GameRuleTypeRewriter() {
        super(Registries.GAME_RULE, "getByName");
    }

    @Override
    protected String rewriteFieldType(Holder.Reference<GameRule<?>> reference) {
        return "%s<%s>".formatted(this.registryEntry.apiClass().getSimpleName(), this.importCollector.getShortName(reference.value().valueClass()));
    }
}
