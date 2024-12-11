package io.papermc.paper.datacomponent.item;

import io.papermc.paper.potion.SuspiciousEffectEntry;
import io.papermc.paper.util.MCUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.Unmodifiable;

import static io.papermc.paper.potion.SuspiciousEffectEntry.create;

public record PaperSuspiciousStewEffects(
    net.minecraft.world.item.component.SuspiciousStewEffects impl
) implements SuspiciousStewEffects, Handleable<net.minecraft.world.item.component.SuspiciousStewEffects> {

    @Override
    public net.minecraft.world.item.component.SuspiciousStewEffects getHandle() {
        return this.impl;
    }

    @Override
    public @Unmodifiable List<SuspiciousEffectEntry> effects() {
        return MCUtil.transformUnmodifiable(this.impl.effects(), entry -> create(CraftPotionEffectType.minecraftHolderToBukkit(entry.effect()), entry.duration()));
    }

    static final class BuilderImpl implements Builder {

        private final List<net.minecraft.world.item.component.SuspiciousStewEffects.Entry> effects = new ObjectArrayList<>();

        @Override
        public Builder add(final SuspiciousEffectEntry entry) {
            this.effects.add(new net.minecraft.world.item.component.SuspiciousStewEffects.Entry(
                org.bukkit.craftbukkit.potion.CraftPotionEffectType.bukkitToMinecraftHolder(entry.effect()),
                entry.duration()
            ));
            return this;
        }

        @Override
        public Builder addAll(final Collection<SuspiciousEffectEntry> entries) {
            entries.forEach(this::add);
            return this;
        }

        @Override
        public SuspiciousStewEffects build() {
            if (this.effects.isEmpty()) {
                return new PaperSuspiciousStewEffects(net.minecraft.world.item.component.SuspiciousStewEffects.EMPTY);
            }

            return new PaperSuspiciousStewEffects(
                new net.minecraft.world.item.component.SuspiciousStewEffects(new ObjectArrayList<>(this.effects))
            );
        }
    }
}
