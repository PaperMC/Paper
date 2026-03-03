package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;
import io.papermc.paper.attribute.UnmodifiableAttributeMap;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.OldEnumHolderable;
import io.papermc.paper.world.flag.PaperFeatureDependent;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import org.bukkit.Bukkit;
import org.bukkit.FeatureFlag;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attributable;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftEntityType<E extends Entity> extends OldEnumHolderable<EntityType<E>, net.minecraft.world.entity.EntityType<?>> implements EntityType<E>, PaperFeatureDependent<net.minecraft.world.entity.EntityType<?>> {

    private static int count = 0;

    public CraftEntityType(final Holder<net.minecraft.world.entity.EntityType<?>> holder) {
        super(holder, count++);
    }

    @Nullable
    public String getName() {
        return this.getKey().getKey();
    }

    @Override
    public boolean isSpawnable() {
        return this.getHandle().canSummon();
    }

    @Override
    public boolean isAlive() {
        Class<E> entity = this.getEntityClass();
        return entity != null && LivingEntity.class.isAssignableFrom(entity);
    }

    @Override
    public String translationKey() {
        return this.getHandle().getDescriptionId();
    }

    @Override
    public boolean hasDefaultAttributes() {
        return DefaultAttributes.hasSupplier(this.getHandle());
    }

    @Override
    public Attributable getDefaultAttributes() {
        Preconditions.checkArgument(this.hasDefaultAttributes(), this.getKey().asString() + " doesn't have default attributes");
        AttributeSupplier supplier = DefaultAttributes.getSupplier((net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.LivingEntity>) this.getHandle());
        return new UnmodifiableAttributeMap(supplier);
    }

    /**
     * Implementation for the deprecated, API only, UNKNOWN entity type.
     * As per {@link #bukkitToMinecraftHolder(EntityType)} it cannot be
     * converted into an internal entity type and only serves backwards compatibility reasons.
     */
    @Deprecated(forRemoval = true, since = "1.21.11")
    @ApiStatus.ScheduledForRemoval(inVersion = "1.22")
    public static class LegacyUnknownImpl implements EntityType<Entity> {

        public static final EntityType<?> INSTANCE = new LegacyUnknownImpl();

        private final int ordinal;

        private LegacyUnknownImpl() {
            this.ordinal = count++;
        }

        @Override
        public NamespacedKey getKey() {
            return null;
        }

        @Override
        public int compareTo(final EntityType other) {
            return this.ordinal - other.ordinal();
        }

        @Override
        public String name() {
            return "UNKNOWN";
        }

        @Override
        public int ordinal() {
            return this.ordinal;
        }

        @Override
        public boolean equals(final Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            final LegacyUnknownImpl that = (LegacyUnknownImpl) object;
            return ordinal == that.ordinal;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(ordinal);
        }

        @Override
        public String toString() {
            return "UNKNOWN";
        }

        @Override
        public @Nullable String getName() {
            return null;
        }

        @Override
        public @Nullable Class<? extends Entity> getEntityClass() {
            return null;
        }

        @Override
        public short getTypeId() {
            return -1;
        }

        @Override
        public boolean isSpawnable() {
            return false;
        }

        @Override
        public boolean isAlive() {
            return false;
        }

        @Override
        public String translationKey() {
            throw new IllegalStateException("UNKNOWN entity type do not have translation key");
        }

        @Override
        public boolean hasDefaultAttributes() {
            return false;
        }

        @Override
        public Attributable getDefaultAttributes() {
            throw new IllegalStateException(this.getKey().asString() + " doesn't have default attributes");
        }

        @Override
        public @Unmodifiable Set<FeatureFlag> requiredFeatures() {
            throw new IllegalStateException("UNKNOWN entity type do not have required features");
        }
    }

    public static EntityType<?> minecraftToBukkit(net.minecraft.world.entity.EntityType<?> minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.ENTITY_TYPE);
    }

    public static <M extends net.minecraft.world.entity.Entity, B extends Entity> net.minecraft.world.entity.@Nullable EntityType<M> bukkitToMinecraft(EntityType<B> bukkit) {
        if (bukkit == EntityType.UNKNOWN) {
            return null;
        }
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static EntityType<?> minecraftHolderToBukkit(Holder<net.minecraft.world.entity.EntityType<?>> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.ENTITY_TYPE);
    }

    public static @Nullable Holder<net.minecraft.world.entity.EntityType<?>> bukkitToMinecraftHolder(EntityType<?> bukkit) {
        if (bukkit == EntityType.UNKNOWN) {
            return null;
        }
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public static String bukkitToString(EntityType<?> bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return bukkit.getKey().toString();
    }

    public static EntityType<?> stringToBukkit(String string) {
        Preconditions.checkArgument(string != null);

        // We currently do not have any version-dependent remapping, so we can use current version
        // First convert from when only the names where saved
        string = FieldRename.convertEntityTypeName(ApiVersion.CURRENT, string);
        string = string.toLowerCase(Locale.ROOT);
        NamespacedKey key = NamespacedKey.fromString(string);

        // Now also convert from when keys where saved
        return CraftRegistry.get(RegistryKey.ENTITY_TYPE, key, ApiVersion.CURRENT);
    }

    @Override
    public @Nullable Class<E> getEntityClass() { // todo return old value for boats when api version is low
        CraftEntityTypes.EntityTypeData<E, ?> data = CraftEntityTypes.getEntityTypeData(this);
        if (data == null) {
            return null;
        }
        return data.entityClass();
    }

    @Override
    @Deprecated(since = "1.6.2", forRemoval = true)
    public short getTypeId() {
        return LegacyHolder.NUMERIC_IDS.getOrDefault(this, -1).shortValue();
    }

    @Deprecated // bytecode rewrites target this
    public static EntityType<?> valueOf(String name) {
        if ("UNKNOWN".equals(name)) {
            return EntityType.UNKNOWN;
        }

        NamespacedKey key = NamespacedKey.fromString(name.toLowerCase(Locale.ROOT));
        EntityType<?> entityType = key == null ? null : Bukkit.getUnsafe().get(RegistryKey.ENTITY_TYPE, key);
        Preconditions.checkArgument(entityType != null, "No entity type found with the name %s", name);
        return entityType;
    }

    @Deprecated // bytecode rewrites target this
    public static EntityType<?>[] values() {
        return Registry.ENTITY_TYPE.stream().toArray(EntityType[]::new);
    }

    @Deprecated // bytecode rewrites target this
    public static @Nullable EntityType<?> fromId(int id) {
        if (id > Short.MAX_VALUE) {
            return null;
        }
        return LegacyHolder.NUMERIC_IDS.inverse().get(id);
    }

    private static class LegacyHolder {
        @Deprecated
        private static final ImmutableBiMap<EntityType<?>, Integer> NUMERIC_IDS = ImmutableBiMap.<EntityType<?>, Integer>builder()
            .put(EntityType.ITEM, 1)
            .put(EntityType.EXPERIENCE_ORB, 2)
            .put(EntityType.AREA_EFFECT_CLOUD, 3)
            .put(EntityType.ELDER_GUARDIAN, 4)
            .put(EntityType.WITHER_SKELETON, 5)
            .put(EntityType.STRAY, 6)
            .put(EntityType.EGG, 7)
            .put(EntityType.LEASH_KNOT, 8)
            .put(EntityType.PAINTING, 9)
            .put(EntityType.ARROW, 10)
            .put(EntityType.SNOWBALL, 11)
            .put(EntityType.FIREBALL, 12)
            .put(EntityType.SMALL_FIREBALL, 13)
            .put(EntityType.ENDER_PEARL, 14)
            .put(EntityType.EYE_OF_ENDER, 15)
            .put(EntityType.SPLASH_POTION, 16)
            .put(EntityType.EXPERIENCE_BOTTLE, 17)
            .put(EntityType.ITEM_FRAME, 18)
            .put(EntityType.WITHER_SKULL, 19)
            .put(EntityType.TNT, 20)
            .put(EntityType.FALLING_BLOCK, 21)
            .put(EntityType.FIREWORK_ROCKET, 22)
            .put(EntityType.HUSK, 23)
            .put(EntityType.SPECTRAL_ARROW, 24)
            .put(EntityType.SHULKER_BULLET, 25)
            .put(EntityType.DRAGON_FIREBALL, 26)
            .put(EntityType.ZOMBIE_VILLAGER, 27)
            .put(EntityType.SKELETON_HORSE, 28)
            .put(EntityType.ZOMBIE_HORSE, 29)
            .put(EntityType.ARMOR_STAND, 30)
            .put(EntityType.DONKEY, 31)
            .put(EntityType.MULE, 32)
            .put(EntityType.EVOKER_FANGS, 33)
            .put(EntityType.EVOKER, 34)
            .put(EntityType.VEX, 35)
            .put(EntityType.VINDICATOR, 36)
            .put(EntityType.ILLUSIONER, 37)

            .put(EntityType.COMMAND_BLOCK_MINECART, 40)
            .put(EntityType.MINECART, 42)
            .put(EntityType.CHEST_MINECART, 43)
            .put(EntityType.FURNACE_MINECART, 44)
            .put(EntityType.TNT_MINECART, 45)
            .put(EntityType.HOPPER_MINECART, 46)
            .put(EntityType.SPAWNER_MINECART, 47)

            .put(EntityType.CREEPER, 50)
            .put(EntityType.SKELETON, 51)
            .put(EntityType.SPIDER, 52)
            .put(EntityType.GIANT, 53)
            .put(EntityType.ZOMBIE, 54)
            .put(EntityType.SLIME, 55)
            .put(EntityType.GHAST, 56)
            .put(EntityType.ZOMBIFIED_PIGLIN, 57)
            .put(EntityType.ENDERMAN, 58)
            .put(EntityType.CAVE_SPIDER, 59)
            .put(EntityType.SILVERFISH, 60)
            .put(EntityType.BLAZE, 61)
            .put(EntityType.MAGMA_CUBE, 62)
            .put(EntityType.ENDER_DRAGON, 63)
            .put(EntityType.WITHER, 64)
            .put(EntityType.BAT, 65)
            .put(EntityType.WITCH, 66)
            .put(EntityType.ENDERMITE, 67)
            .put(EntityType.GUARDIAN, 68)
            .put(EntityType.SHULKER, 69)

            .put(EntityType.PIG, 90)
            .put(EntityType.SHEEP, 91)
            .put(EntityType.COW, 92)
            .put(EntityType.CHICKEN, 93)
            .put(EntityType.SQUID, 94)
            .put(EntityType.WOLF, 95)
            .put(EntityType.MOOSHROOM, 96)
            .put(EntityType.SNOW_GOLEM, 97)
            .put(EntityType.OCELOT, 98)
            .put(EntityType.IRON_GOLEM, 99)
            .put(EntityType.HORSE, 100)
            .put(EntityType.RABBIT, 101)
            .put(EntityType.POLAR_BEAR, 102)
            .put(EntityType.LLAMA, 103)
            .put(EntityType.LLAMA_SPIT, 104)
            .put(EntityType.PARROT, 105)

            .put(EntityType.VILLAGER, 120)

            .put(EntityType.END_CRYSTAL, 200)
            .buildOrThrow();
    }
}
