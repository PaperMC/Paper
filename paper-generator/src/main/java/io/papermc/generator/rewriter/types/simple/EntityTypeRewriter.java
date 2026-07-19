package io.papermc.generator.rewriter.types.simple;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.rewriter.types.registry.RegistryFieldRewriter;
import io.papermc.generator.types.goal.MobGoalNames;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.util.ClassResolver;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

public class EntityTypeRewriter extends RegistryFieldRewriter<EntityType<?>> {

    private static final Map<ResourceKey<EntityType<?>>, Class<? extends Entity>> ENTITY_GENERIC_TYPES =
        RegistryEntries.byRegistryKey(Registries.ENTITY_TYPE).getFields(field -> {
            if (field.getGenericType() instanceof ParameterizedType complexType && complexType.getActualTypeArguments().length == 1) {
                return (Class<? extends Entity>) complexType.getActualTypeArguments()[0];
            }
            return null;
        });

    private static final Map<String, String> CLASS_RENAMES = ImmutableMap.<String, String>builder()
        .put("ItemEntity", "Item")
        .put("EndCrystal", "EnderCrystal")
        .put("ThrownEgg", "Egg")
        .put("ThrownEnderpearl", "EnderPearl")
        .put("ThrownExperienceBottle", "ThrownExpBottle")
        .put("ThrownLingeringPotion", "LingeringPotion")
        .put("ThrownSplashPotion", "SplashPotion")
        .put("ThrownTrident", "Trident")
        .put("PrimedTnt", "TNTPrimed")
        .put("EyeOfEnder", "EnderSignal")
        .put("FallingBlockEntity", "FallingBlock")
        .put("FireworkRocketEntity", "Firework")
        .put("FishingHook", "FishHook")
        .put("LeashFenceKnotEntity", "LeashHitch")
        .put("LightningBolt", "LightningStrike")

        .put("MinecartChest", "StorageMinecart")
        .put("MinecartCommandBlock", "CommandMinecart")
        .put("MinecartFurnace", "PoweredMinecart")
        .put("MinecartHopper", "HopperMinecart")
        .put("MinecartSpawner", "SpawnerMinecart")
        .put("MinecartTNT", "ExplosiveMinecart")
        .put("Minecart", "RideableMinecart")
        .buildOrThrow();

    private static final ClassResolver runtime = new ClassResolver(EntityTypeRewriter.class.getClassLoader());

    public EntityTypeRewriter() {
        super(Registries.ENTITY_TYPE, "getType");
    }

    @Override
    protected String rewriteFieldType(Holder.Reference<EntityType<?>> reference) {
        return "%s<%s>".formatted(
            super.rewriteFieldType(reference),
            toBukkitClass(reference)
        );
    }

    private String toBukkitClass(Holder.Reference<EntityType<?>> reference) {
        Class<? extends Entity> internalClass = ENTITY_GENERIC_TYPES.get(reference.key());
        if (Mob.class.isAssignableFrom(internalClass)) {
            return this.importCollector.getShortName(MobGoalNames.BUKKIT_BRIDGE.get((Class<? extends Mob>) internalClass));
        }

        String internalClassName = internalClass.getSimpleName();
        String className = CLASS_RENAMES.getOrDefault(internalClassName, internalClassName);
        ClassNamed resolvedClass = this.classNamedView.tryFindFirst(className)
            .orElseThrow(() -> new IllegalStateException("Could not find entity class for " + reference.key().identifier() + " (" + className + ")"))
            .resolve(runtime);
        Preconditions.checkArgument(org.bukkit.entity.Entity.class.isAssignableFrom(resolvedClass.knownClass()), "Generic type must be an entity");
        return this.importCollector.getShortName(resolvedClass);
    }
}
