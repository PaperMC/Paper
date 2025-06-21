package io.papermc.paper;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.world.damagesource.CombatEntry;
import io.papermc.paper.world.damagesource.FallLocationType;
import io.papermc.paper.world.damagesource.PaperCombatEntryWrapper;
import io.papermc.paper.world.damagesource.PaperCombatTrackerWrapper;
import net.minecraft.Optionull;
import net.minecraft.commands.PermissionSource;
import net.minecraft.world.damagesource.FallLocation;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.damage.CraftDamageEffect;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.damage.DamageEffect;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.function.Predicate;

@NullMarked
public class PaperServerInternalAPIBridge implements InternalAPIBridge {
    public static final PaperServerInternalAPIBridge INSTANCE = new PaperServerInternalAPIBridge();

    @Override
    public DamageEffect getDamageEffect(final String key) {
        return CraftDamageEffect.getById(key);
    }

    @Override
    public Biome constructLegacyCustomBiome() {
        class Holder {
            static final Biome LEGACY_CUSTOM = new CraftBiome.LegacyCustomBiomeImpl();
        }
        return Holder.LEGACY_CUSTOM;
    }

    @Override
    public CombatEntry createCombatEntry(final LivingEntity entity, final DamageSource damageSource, final float damage) {
        final net.minecraft.world.entity.LivingEntity mob = ((CraftLivingEntity) entity).getHandle();
        final FallLocation fallLocation = FallLocation.getCurrentFallLocation(mob);
        return createCombatEntry(
            ((CraftDamageSource) damageSource).getHandle(),
            damage,
            fallLocation,
            (float) mob.fallDistance
        );
    }

    @Override
    public CombatEntry createCombatEntry(
        final DamageSource damageSource,
        final float damage,
        @Nullable final FallLocationType fallLocationType,
        final float fallDistance
    ) {
        return createCombatEntry(
            ((CraftDamageSource) damageSource).getHandle(),
            damage,
            Optionull.map(fallLocationType, PaperCombatTrackerWrapper::paperToMinecraft),
            fallDistance
        );
    }

    private CombatEntry createCombatEntry(
        final net.minecraft.world.damagesource.DamageSource damageSource,
        final float damage,
        final net.minecraft.world.damagesource.@Nullable FallLocation fallLocation,
        final float fallDistance
    ) {
        return new PaperCombatEntryWrapper(new net.minecraft.world.damagesource.CombatEntry(
            damageSource, damage, fallLocation, fallDistance
        ));
    }

    @Override
    public Predicate<CommandSourceStack> restricted(final Predicate<CommandSourceStack> predicate) {
        record RestrictedPredicate(Predicate<CommandSourceStack> predicate) implements Predicate<CommandSourceStack>, PermissionSource.RestrictedMarker {
            @Override
            public boolean test(final CommandSourceStack commandSourceStack) {
                return this.predicate.test(commandSourceStack);
            }
        }

        return new RestrictedPredicate(predicate);
    }
}
