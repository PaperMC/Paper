package io.papermc.paper;

import io.papermc.paper.world.damagesource.CombatEntry;
import io.papermc.paper.world.damagesource.FallLocationType;
import io.papermc.paper.world.damagesource.PaperCombatEntryWrapper;
import io.papermc.paper.world.damagesource.PaperCombatTrackerWrapper;
import net.minecraft.Optionull;
import net.minecraft.core.Holder;
import net.minecraft.core.IdMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.FallLocation;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.enchantment.Enchantment;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.damage.CraftDamageEffect;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.damage.DamageEffect;
import org.bukkit.damage.DamageSource;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.Random;

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
    public @Nullable EnchantmentOffer[] rollEnchantmentOffers(
        final ItemStack targetStack,
        final int seed,
        final int bookshelfCount,
        final int offerAmount
    ) {
        final int[] costs = new int[offerAmount];
        final int[] enchantClue = new int[offerAmount];
        final int[] levelClue = new int[offerAmount];

        final RandomSource randomSource = new RandomSourceWrapper(new Random());
        EnchantmentMenu.fillEnchantmentOffers(
            randomSource,
            seed,
            bookshelfCount,
            CraftItemStack.unwrap(targetStack),
            CraftRegistry.getMinecraftRegistry(),
            costs,
            enchantClue,
            levelClue
        );

        final IdMap<Holder<Enchantment>> holderIdMap = CraftRegistry.getMinecraftRegistry()
            .lookupOrThrow(Registries.ENCHANTMENT)
            .asHolderIdMap();
        final @Nullable EnchantmentOffer[] offers = new EnchantmentOffer[offerAmount];
        for (int i = 0; i < costs.length; i++) {
            if (enchantClue[i] < 0) continue;
            offers[i] = new EnchantmentOffer(
                CraftEnchantment.minecraftHolderToBukkit(holderIdMap.byId(enchantClue[i])),
                levelClue[i],
                costs[i]
            );
        }

        return offers;
    }
}
