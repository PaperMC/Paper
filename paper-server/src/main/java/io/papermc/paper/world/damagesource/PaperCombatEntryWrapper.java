package io.papermc.paper.world.damagesource;

import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.damage.DamageSource;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record PaperCombatEntryWrapper(net.minecraft.world.damagesource.CombatEntry handle) implements CombatEntry {

    @Override
    public DamageSource getDamageSource() {
        return new CraftDamageSource(this.handle.source());
    }

    @Override
    public float getDamage() {
        return this.handle.damage();
    }

    @Override
    public @Nullable FallLocationType getFallLocationType() {
        net.minecraft.world.damagesource.FallLocation fallLocation = this.handle.fallLocation();
        return fallLocation == null ? null : PaperCombatTrackerWrapper.minecraftToPaper(fallLocation);
    }

    @Override
    public float getFallDistance() {
        return this.handle.fallDistance();
    }

}
