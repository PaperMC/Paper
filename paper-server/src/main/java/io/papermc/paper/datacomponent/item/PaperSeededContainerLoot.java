package io.papermc.paper.datacomponent.item;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.key.Key;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperSeededContainerLoot(
    net.minecraft.world.item.component.SeededContainerLoot impl
) implements SeededContainerLoot, Handleable<net.minecraft.world.item.component.SeededContainerLoot> {

    @Override
    public net.minecraft.world.item.component.SeededContainerLoot getHandle() {
        return this.impl;
    }

    @Override
    public Key lootTable() {
        return CraftNamespacedKey.fromMinecraft(this.impl.lootTable().location());
    }

    @Override
    public long seed() {
        return this.impl.seed();
    }

    static final class BuilderImpl implements SeededContainerLoot.Builder {

        private long seed = LootTable.RANDOMIZE_SEED;
        private Key key;

        BuilderImpl(final Key key) {
            this.key = key;
        }

        @Override
        public SeededContainerLoot.Builder lootTable(final Key key) {
            this.key = key;
            return this;
        }

        @Override
        public SeededContainerLoot.Builder seed(final long seed) {
            this.seed = seed;
            return this;
        }

        @Override
        public SeededContainerLoot build() {
            return new PaperSeededContainerLoot(new net.minecraft.world.item.component.SeededContainerLoot(
                ResourceKey.create(Registries.LOOT_TABLE, PaperAdventure.asVanilla(this.key)),
                this.seed
            ));
        }
    }
}
