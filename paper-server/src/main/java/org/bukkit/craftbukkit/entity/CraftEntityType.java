package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.attribute.UnmodifiableAttributeMap;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.OldEnumHolderable;
import io.papermc.paper.world.flag.PaperFeatureDependent;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Util;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import org.bukkit.FeatureFlag;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftEntityType<E extends Entity> extends OldEnumHolderable<EntityType<E>, net.minecraft.world.entity.EntityType<?>> implements EntityType<E>, PaperFeatureDependent<net.minecraft.world.entity.EntityType<?>> {

    @Deprecated
    private static final Object2IntMap<net.minecraft.world.entity.EntityType<?>> LEGACY_ID = Util.make(new Object2IntOpenHashMap<>(), map -> {
        map.put(net.minecraft.world.entity.EntityType.ITEM, 1);
        map.put(net.minecraft.world.entity.EntityType.EXPERIENCE_ORB, 2);
        map.put(net.minecraft.world.entity.EntityType.AREA_EFFECT_CLOUD, 3);
        map.put(net.minecraft.world.entity.EntityType.ELDER_GUARDIAN, 4);
        map.put(net.minecraft.world.entity.EntityType.WITHER_SKELETON, 5);
        map.put(net.minecraft.world.entity.EntityType.STRAY, 6);
        map.put(net.minecraft.world.entity.EntityType.EGG, 7);
        map.put(net.minecraft.world.entity.EntityType.LEASH_KNOT, 8);
        map.put(net.minecraft.world.entity.EntityType.PAINTING, 9);
        map.put(net.minecraft.world.entity.EntityType.ARROW, 10);
        map.put(net.minecraft.world.entity.EntityType.SNOWBALL, 11);
        map.put(net.minecraft.world.entity.EntityType.FIREBALL, 12);
        map.put(net.minecraft.world.entity.EntityType.SMALL_FIREBALL, 13);
        map.put(net.minecraft.world.entity.EntityType.ENDER_PEARL, 14);
        map.put(net.minecraft.world.entity.EntityType.EYE_OF_ENDER, 15);
        map.put(net.minecraft.world.entity.EntityType.SPLASH_POTION, 16);
        map.put(net.minecraft.world.entity.EntityType.EXPERIENCE_BOTTLE, 17);
        map.put(net.minecraft.world.entity.EntityType.ITEM_FRAME, 18);
        map.put(net.minecraft.world.entity.EntityType.WITHER_SKULL, 19);
        map.put(net.minecraft.world.entity.EntityType.TNT, 20);
        map.put(net.minecraft.world.entity.EntityType.FALLING_BLOCK, 21);
        map.put(net.minecraft.world.entity.EntityType.FIREWORK_ROCKET, 22);
        map.put(net.minecraft.world.entity.EntityType.HUSK, 23);
        map.put(net.minecraft.world.entity.EntityType.SPECTRAL_ARROW, 24);
        map.put(net.minecraft.world.entity.EntityType.SHULKER_BULLET, 25);
        map.put(net.minecraft.world.entity.EntityType.DRAGON_FIREBALL, 26);
        map.put(net.minecraft.world.entity.EntityType.ZOMBIE_VILLAGER, 27);
        map.put(net.minecraft.world.entity.EntityType.SKELETON_HORSE, 28);
        map.put(net.minecraft.world.entity.EntityType.ZOMBIE_HORSE, 29);
        map.put(net.minecraft.world.entity.EntityType.ARMOR_STAND, 30);
        map.put(net.minecraft.world.entity.EntityType.DONKEY, 31);
        map.put(net.minecraft.world.entity.EntityType.MULE, 32);
        map.put(net.minecraft.world.entity.EntityType.EVOKER_FANGS, 33);
        map.put(net.minecraft.world.entity.EntityType.EVOKER, 34);
        map.put(net.minecraft.world.entity.EntityType.VEX, 35);
        map.put(net.minecraft.world.entity.EntityType.VINDICATOR, 36);
        map.put(net.minecraft.world.entity.EntityType.ILLUSIONER, 37);

        map.put(net.minecraft.world.entity.EntityType.COMMAND_BLOCK_MINECART, 40);
        map.put(net.minecraft.world.entity.EntityType.MINECART, 42);
        map.put(net.minecraft.world.entity.EntityType.CHEST_MINECART, 43);
        map.put(net.minecraft.world.entity.EntityType.FURNACE_MINECART, 44);
        map.put(net.minecraft.world.entity.EntityType.TNT_MINECART, 45);
        map.put(net.minecraft.world.entity.EntityType.HOPPER_MINECART, 46);
        map.put(net.minecraft.world.entity.EntityType.SPAWNER_MINECART, 47);

        map.put(net.minecraft.world.entity.EntityType.CREEPER, 50);
        map.put(net.minecraft.world.entity.EntityType.SKELETON, 51);
        map.put(net.minecraft.world.entity.EntityType.SPIDER, 52);
        map.put(net.minecraft.world.entity.EntityType.GIANT, 53);
        map.put(net.minecraft.world.entity.EntityType.ZOMBIE, 54);
        map.put(net.minecraft.world.entity.EntityType.SLIME, 55);
        map.put(net.minecraft.world.entity.EntityType.GHAST, 56);
        map.put(net.minecraft.world.entity.EntityType.ZOMBIFIED_PIGLIN, 57);
        map.put(net.minecraft.world.entity.EntityType.ENDERMAN, 58);
        map.put(net.minecraft.world.entity.EntityType.CAVE_SPIDER, 59);
        map.put(net.minecraft.world.entity.EntityType.SILVERFISH, 60);
        map.put(net.minecraft.world.entity.EntityType.BLAZE, 61);
        map.put(net.minecraft.world.entity.EntityType.MAGMA_CUBE, 62);
        map.put(net.minecraft.world.entity.EntityType.ENDER_DRAGON, 63);
        map.put(net.minecraft.world.entity.EntityType.WITHER, 64);
        map.put(net.minecraft.world.entity.EntityType.BAT, 65);
        map.put(net.minecraft.world.entity.EntityType.WITCH, 66);
        map.put(net.minecraft.world.entity.EntityType.ENDERMITE, 67);
        map.put(net.minecraft.world.entity.EntityType.GUARDIAN, 68);
        map.put(net.minecraft.world.entity.EntityType.SHULKER, 69);

        map.put(net.minecraft.world.entity.EntityType.PIG, 90);
        map.put(net.minecraft.world.entity.EntityType.SHEEP, 91);
        map.put(net.minecraft.world.entity.EntityType.COW, 92);
        map.put(net.minecraft.world.entity.EntityType.CHICKEN, 93);
        map.put(net.minecraft.world.entity.EntityType.SQUID, 94);
        map.put(net.minecraft.world.entity.EntityType.WOLF, 95);
        map.put(net.minecraft.world.entity.EntityType.MOOSHROOM, 96);
        map.put(net.minecraft.world.entity.EntityType.SNOW_GOLEM, 97);
        map.put(net.minecraft.world.entity.EntityType.OCELOT, 98);
        map.put(net.minecraft.world.entity.EntityType.IRON_GOLEM, 99);
        map.put(net.minecraft.world.entity.EntityType.HORSE, 100);
        map.put(net.minecraft.world.entity.EntityType.RABBIT, 101);
        map.put(net.minecraft.world.entity.EntityType.POLAR_BEAR, 102);
        map.put(net.minecraft.world.entity.EntityType.LLAMA, 103);
        map.put(net.minecraft.world.entity.EntityType.LLAMA_SPIT, 104);
        map.put(net.minecraft.world.entity.EntityType.PARROT, 105);

        map.put(net.minecraft.world.entity.EntityType.VILLAGER, 120);

        map.put(net.minecraft.world.entity.EntityType.END_CRYSTAL, 200);
    });
    private static int count = 0;

    public CraftEntityType(final Holder<net.minecraft.world.entity.EntityType<?>> holder) {
        super(holder, count++);
    }

    @Nullable
    public String getName() {
        return this.getKey().getKey();
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return null;
    }

    @Override
    @Deprecated(since = "1.6.2", forRemoval = true)
    public short getTypeId() {
        return (short) LEGACY_ID.getOrDefault(this.getHandle(), -1);
    }

    @Override
    public boolean isSpawnable() {
        return this.getHandle().canSummon();
    }

    @Override
    public boolean isAlive() {
        return this.hasDefaultAttributes(); // should be roughly equivalent
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
     * Implementation for the deprecated, API only, CUSTOM entity type.
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

    public static net.minecraft.world.entity.@Nullable EntityType<?> bukkitToMinecraft(EntityType<?> bukkit) {
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
}
