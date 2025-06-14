package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.util.MCUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.util.TriState;
import net.minecraft.core.registries.Registries;
import org.bukkit.block.BlockType;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

public record PaperItemTool(
    net.minecraft.world.item.component.Tool impl
) implements Tool, Handleable<net.minecraft.world.item.component.Tool> {

    private static List<Tool.Rule> convert(final List<net.minecraft.world.item.component.Tool.Rule> tool) {
        return MCUtil.transformUnmodifiable(tool, nms -> new PaperRule(
            PaperRegistrySets.convertToApi(RegistryKey.BLOCK, nms.blocks()),
            nms.speed().orElse(null),
            TriState.byBoolean(nms.correctForDrops().orElse(null))
        ));
    }

    @Override
    public net.minecraft.world.item.component.Tool getHandle() {
        return this.impl;
    }

    @Override
    public @Unmodifiable List<Rule> rules() {
        return convert(this.impl.rules());
    }

    @Override
    public float defaultMiningSpeed() {
        return this.impl.defaultMiningSpeed();
    }

    @Override
    public int damagePerBlock() {
        return this.impl.damagePerBlock();
    }

    @Override
    public boolean canDestroyBlocksInCreative() {
        return this.impl.canDestroyBlocksInCreative();
    }

    record PaperRule(RegistryKeySet<BlockType> blocks, @Nullable Float speed, TriState correctForDrops) implements Rule {

        public static PaperRule fromUnsafe(final RegistryKeySet<BlockType> blocks, final @Nullable Float speed, final TriState correctForDrops) {
            Preconditions.checkArgument(speed == null || speed > 0, "speed must be positive");
            return new PaperRule(blocks, speed, correctForDrops);
        }
    }

    static final class BuilderImpl implements Builder {

        private final List<net.minecraft.world.item.component.Tool.Rule> rules = new ObjectArrayList<>();
        private int damage = 1;
        private float miningSpeed = 1.0F;
        private boolean canDestroyBlocksInCreative = true;

        @Override
        public Builder damagePerBlock(final int damage) {
            Preconditions.checkArgument(damage >= 0, "damage must be non-negative, was %s", damage);
            this.damage = damage;
            return this;
        }

        @Override
        public Builder defaultMiningSpeed(final float miningSpeed) {
            this.miningSpeed = miningSpeed;
            return this;
        }

        @Override
        public Builder addRule(final Rule rule) {
            this.rules.add(new net.minecraft.world.item.component.Tool.Rule(
                PaperRegistrySets.convertToNms(Registries.BLOCK, Conversions.global().lookup(), rule.blocks()),
                Optional.ofNullable(rule.speed()),
                Optional.ofNullable(rule.correctForDrops().toBoolean())
            ));
            return this;
        }

        @Override
        public Builder canDestroyBlocksInCreative(final boolean canDestroyBlocksInCreative) {
            this.canDestroyBlocksInCreative = canDestroyBlocksInCreative;
            return this;
        }

        @Override
        public Builder addRules(final Collection<Rule> rules) {
            rules.forEach(this::addRule);
            return this;
        }

        @Override
        public Tool build() {
            return new PaperItemTool(new net.minecraft.world.item.component.Tool(
                new ObjectArrayList<>(this.rules), this.miningSpeed, this.damage, this.canDestroyBlocksInCreative
            ));
        }
    }
}
