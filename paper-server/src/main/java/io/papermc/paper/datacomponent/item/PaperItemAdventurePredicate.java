package io.papermc.paper.datacomponent.item;

import io.papermc.paper.block.BlockPredicate;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.util.MCUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.advancements.critereon.DataComponentMatchers;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperItemAdventurePredicate(
    net.minecraft.world.item.AdventureModePredicate impl
) implements ItemAdventurePredicate, Handleable<net.minecraft.world.item.AdventureModePredicate> {

    private static List<BlockPredicate> convert(final net.minecraft.world.item.AdventureModePredicate nmsModifiers) {
        return MCUtil.transformUnmodifiable(nmsModifiers.predicates, nms -> BlockPredicate.predicate()
            .blocks(nms.blocks().map(blocks -> PaperRegistrySets.convertToApi(RegistryKey.BLOCK, blocks)).orElse(null)).build());
    }

    @Override
    public net.minecraft.world.item.AdventureModePredicate getHandle() {
        return this.impl;
    }

    @Override
    public List<BlockPredicate> predicates() {
        return convert(this.impl);
    }

    static final class BuilderImpl implements ItemAdventurePredicate.Builder {

        private final List<net.minecraft.advancements.critereon.BlockPredicate> predicates = new ObjectArrayList<>();

        @Override
        public ItemAdventurePredicate.Builder addPredicate(final BlockPredicate predicate) {
            this.predicates.add(new net.minecraft.advancements.critereon.BlockPredicate(Optional.ofNullable(predicate.blocks()).map(
                blocks -> PaperRegistrySets.convertToNms(Registries.BLOCK, Conversions.global().lookup(), blocks)
            ), Optional.empty(), Optional.empty(), DataComponentMatchers.ANY)); // TODO DataComponentMatchers
            return this;
        }

        @Override
        public Builder addPredicates(final List<BlockPredicate> predicates) {
            for (final BlockPredicate predicate : predicates) {
                this.addPredicate(predicate);
            }
            return this;
        }

        @Override
        public ItemAdventurePredicate build() {
            return new PaperItemAdventurePredicate(new net.minecraft.world.item.AdventureModePredicate(
                new ObjectArrayList<>(this.predicates))
            );
        }
    }
}
