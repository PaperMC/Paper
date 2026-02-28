package io.papermc.paper;

import com.destroystokyo.paper.PaperSkinParts;
import com.destroystokyo.paper.PaperVersionFetcher;
import com.destroystokyo.paper.SkinParts;
import com.destroystokyo.paper.util.VersionFetcher;
import com.google.common.base.Preconditions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.attribute.UnmodifiableAttributeMap;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.datacomponent.item.PaperResolvableProfile;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import io.papermc.paper.entity.poi.PaperPoiType;
import io.papermc.paper.entity.poi.PoiType;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEventManager;
import io.papermc.paper.util.MCUtil;
import io.papermc.paper.world.damagesource.CombatEntry;
import io.papermc.paper.world.damagesource.FallLocationType;
import io.papermc.paper.world.damagesource.PaperCombatEntryWrapper;
import io.papermc.paper.world.damagesource.PaperCombatTrackerWrapper;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.minecraft.Optionull;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.ResolutionContext;
import net.minecraft.world.damagesource.FallLocation;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.decoration.Mannequin;
import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attributable;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftGameRule;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.damage.CraftDamageEffect;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.craftbukkit.damage.CraftDamageSourceBuilder;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftMannequin;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.damage.DamageEffect;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pose;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class PaperServerInternalAPIBridge implements InternalAPIBridge {
    public static final PaperServerInternalAPIBridge INSTANCE = new PaperServerInternalAPIBridge();

    @Override
    public Biome constructLegacyCustomBiome() {
        return CraftBiome.LegacyCustomImpl.INSTANCE;
    }

    @Override
    public EntityType<?> constructLegacyUnknownEntityType() {
        return CraftEntityType.LegacyUnknownImpl.INSTANCE;
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
        record RestrictedPredicate(Predicate<CommandSourceStack> predicate) implements Predicate<CommandSourceStack>, Commands.RestrictedMarker {
            @Override
            public boolean test(final CommandSourceStack commandSourceStack) {
                return this.predicate.test(commandSourceStack);
            }
        }

        return new RestrictedPredicate(predicate);
    }

    @Override
    public ResolvableProfile defaultMannequinProfile() {
        return new PaperResolvableProfile(net.minecraft.world.entity.decoration.Mannequin.DEFAULT_PROFILE);
    }

    @Override
    public SkinParts.Mutable allSkinParts() {
        return new PaperSkinParts.Mutable(net.minecraft.world.entity.decoration.Mannequin.ALL_LAYERS);
    }

    @Override
    public Component defaultMannequinDescription() {
        return PaperAdventure.asAdventure(Mannequin.DEFAULT_DESCRIPTION);
    }

    @Override
    public <MODERN, LEGACY> GameRule<LEGACY> legacyGameRuleBridge(final GameRule<MODERN> rule, final Function<LEGACY, MODERN> fromLegacyToModern, final Function<MODERN, LEGACY> toLegacyFromModern, final Class<LEGACY> legacyClass) {
        return CraftGameRule.wrap(rule, fromLegacyToModern, toLegacyFromModern, legacyClass);
    }

    @Override
    public Set<Pose> validMannequinPoses() {
        return CraftMannequin.VALID_POSES;
    }

    @Override
    public PoiType.Occupancy createOccupancy(final String enumNameEntry) {
        return new PaperPoiType.PaperOccupancy(PoiManager.Occupancy.valueOf(enumNameEntry));
    }

    @Override
    public DamageSource.Builder createDamageSourceBuilder(final DamageType damageType) {
        return new CraftDamageSourceBuilder(damageType);
    }

    @Override
    public DamageEffect getDamageEffect(final String key) {
        return Objects.requireNonNull(CraftDamageEffect.getById(key), "No DamageEffect found for " + key + ". This is a bug.");
    }

    @Override
    public String getTranslationKey(final EntityType entityType) {
        return CraftEntityType.bukkitToMinecraft(entityType).getDescriptionId();
    }

    @Override
    public VersionFetcher getVersionFetcher() {
        return new PaperVersionFetcher();
    }

    @Override
    public ItemStack deserializeItem(final byte[] data) {
        CompoundTag tag = MCUtil.deserializeTagFromBytes(data);
        int dataVersion = NbtUtils.getDataVersion(tag, 0);
        Preconditions.checkArgument(dataVersion <= CraftMagicNumbers.INSTANCE.getDataVersion(), "Newer version! Server downgrades are not supported!");
        return MCUtil.deserializeItem(tag);
    }

    @Override
    public boolean hasDefaultEntityAttributes(final NamespacedKey entityKey) {
        return DefaultAttributes.hasSupplier(BuiltInRegistries.ENTITY_TYPE.getValue(CraftNamespacedKey.toMinecraft(entityKey)));
    }

    @Override
    public Attributable getDefaultEntityAttributes(final NamespacedKey entityKey) {
        var supplier = DefaultAttributes.getSupplier((net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.LivingEntity>) BuiltInRegistries.ENTITY_TYPE.getValue(CraftNamespacedKey.toMinecraft(entityKey)));
        return new UnmodifiableAttributeMap(supplier);
    }

    @Override
    public String getStatisticCriteriaKey(final Statistic statistic) {
        if (statistic.getType() != Statistic.Type.UNTYPED) return "minecraft.custom:minecraft." + statistic.getKey().getKey();
        return CraftStatistic.getNMSStatistic(statistic).getName();
    }

    @Override
    public LifecycleEventManager<Plugin> createPluginLifecycleEventManager(final JavaPlugin plugin, final BooleanSupplier registrationCheck) {
        return new PaperLifecycleEventManager<>(plugin, registrationCheck);
    }

    @Override
    public ItemStack createEmptyStack() {
        return CraftItemStack.asCraftMirror(null);
    }

    @Override
    public Component resolveWithContext(final Component component, final @Nullable CommandSender context, final @Nullable Entity scoreboardSubject, final boolean bypassPermissions) throws IOException {
        final net.minecraft.commands.CommandSourceStack source = context != null ? VanillaCommandWrapper.getListener(context) : null;
        Boolean previous = null;
        if (source != null && bypassPermissions) {
            previous = source.bypassSelectorPermissions;
            source.bypassSelectorPermissions = true;
        }
        try {
            final ResolutionContext.Builder builder = ResolutionContext.builder();
            // order is important here
            if (source != null) {
                builder.withSource(source);
            }
            if (scoreboardSubject != null) {
                builder.withEntityOverride(((CraftEntity) scoreboardSubject).getHandle());
            }
            return PaperAdventure.asAdventure(ComponentUtils.resolve(builder.build(), PaperAdventure.asVanilla(component)));
        } catch (final CommandSyntaxException e) {
            throw new IOException(e);
        } finally {
            if (previous != null) {
                source.bypassSelectorPermissions = previous;
            }
        }
    }

    @Override
    public ComponentFlattener componentFlattener() {
        return PaperAdventure.FLATTENER;
    }
}
