package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.util.MCUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.component.FireworkExplosion;
import org.bukkit.FireworkEffect;
import org.bukkit.craftbukkit.inventory.CraftMetaFirework;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public record PaperUseEffects(
        net.minecraft.world.item.component.UseEffects impl
) implements UseEffects, Handleable<net.minecraft.world.item.component.UseEffects> {

    @Override
    public net.minecraft.world.item.component.UseEffects getHandle() {
        return this.impl;
    }

    @Override
    public boolean canSprint() {
        return this.impl.canSprint();
    }

    @Override
    public float speedMultiplier() {
        return this.impl.speedMultiplier();
    }

    static final class BuilderImpl implements UseEffects.Builder {

        private boolean canSprint = net.minecraft.world.item.component.UseEffects.DEFAULT.canSprint();
        private float speedMultiplier = net.minecraft.world.item.component.UseEffects.DEFAULT.speedMultiplier();

        @Override
        public UseEffects.Builder canSprint(final boolean canSprint) {
            this.canSprint = canSprint;
            return this;
        }

        @Override
        public UseEffects.Builder speedMultiplier(final float speedMultiplier) {
            com.google.common.base.Preconditions.checkArgument(speedMultiplier >= 0.0F && speedMultiplier <= 1.0F, "speedMultiplier must be between 0.0 and 1.0 (inclusive)");
            this.speedMultiplier = speedMultiplier;
            return this;
        }

        @Override
        public UseEffects build() {
            return new PaperUseEffects(
                    new net.minecraft.world.item.component.UseEffects(
                            this.canSprint,
                            this.speedMultiplier
                    )
            );
        }
    }
}