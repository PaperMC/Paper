package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import org.bukkit.GameEvent;
import org.bukkit.JukeboxSong;
import org.bukkit.Keyed;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockType;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.block.banner.CraftPatternType;
import org.bukkit.craftbukkit.damage.CraftDamageType;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.entity.CraftCat;
import org.bukkit.craftbukkit.entity.CraftFrog;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.craftbukkit.generator.structure.CraftStructure;
import org.bukkit.craftbukkit.generator.structure.CraftStructureType;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.craftbukkit.inventory.CraftMenuType;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimMaterial;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimPattern;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.map.CraftMapCursor;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.map.MapCursor;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class CraftRegistry<B extends Keyed, M> implements Registry<B> {

    private static IRegistryCustom registry;

    public static void setMinecraftRegistry(IRegistryCustom registry) {
        Preconditions.checkState(CraftRegistry.registry == null, "Registry already set");
        CraftRegistry.registry = registry;
    }

    public static IRegistryCustom getMinecraftRegistry() {
        return registry;
    }

    public static <E> IRegistry<E> getMinecraftRegistry(ResourceKey<IRegistry<E>> key) {
        return getMinecraftRegistry().lookupOrThrow(key);
    }

    /**
     * Usage note: Only use this method to delegate the conversion methods from the individual Craft classes to here.
     * Do not use it in other parts of CraftBukkit, use the methods in the respective Craft classes instead.
     *
     * @param minecraft the minecraft representation
     * @param registryKey the registry key of the minecraft registry to use
     * @param bukkitRegistry the bukkit registry to use
     * @return the bukkit representation of the minecraft value
     */
    public static <B extends Keyed, M> B minecraftToBukkit(M minecraft, ResourceKey<IRegistry<M>> registryKey, Registry<B> bukkitRegistry) {
        Preconditions.checkArgument(minecraft != null);

        IRegistry<M> registry = CraftRegistry.getMinecraftRegistry(registryKey);
        B bukkit = bukkitRegistry.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft)
                .orElseThrow(() -> new IllegalStateException(String.format("Cannot convert '%s' to bukkit representation, since it is not registered.", minecraft))).location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    /**
     * Usage note: Only use this method to delegate the conversion methods from the individual Craft classes to here.
     * Do not use it in other parts of CraftBukkit, use the methods in the respective Craft classes instead.
     *
     * @param bukkit the bukkit representation
     * @return the minecraft representation of the bukkit value
     */
    public static <B extends Keyed, M> M bukkitToMinecraft(B bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return ((Handleable<M>) bukkit).getHandle();
    }

    public static <B extends Keyed, M> Holder<M> bukkitToMinecraftHolder(B bukkit, ResourceKey<IRegistry<M>> registryKey) {
        Preconditions.checkArgument(bukkit != null);

        IRegistry<M> registry = CraftRegistry.getMinecraftRegistry(registryKey);

        if (registry.wrapAsHolder(bukkitToMinecraft(bukkit)) instanceof Holder.c<M> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own registry entry with out properly registering it.");
    }

    /**
     * Note: Newly added registries should also be added to RegistriesArgumentProvider in the test package
     *
     * @param bukkitClass the bukkit class of the registry
     * @param registryHolder the minecraft registry holder
     * @return the bukkit registry of the provided class
     */
    public static <B extends Keyed> Registry<?> createRegistry(Class<? super B> bukkitClass, IRegistryCustom registryHolder) {
        if (bukkitClass == Attribute.class) {
            return new CraftRegistry<>(Attribute.class, registryHolder.lookupOrThrow(Registries.ATTRIBUTE), CraftAttribute::new, FieldRename.ATTRIBUTE_RENAME);
        }
        if (bukkitClass == Enchantment.class) {
            return new CraftRegistry<>(Enchantment.class, registryHolder.lookupOrThrow(Registries.ENCHANTMENT), CraftEnchantment::new, FieldRename.ENCHANTMENT_RENAME);
        }
        if (bukkitClass == GameEvent.class) {
            return new CraftRegistry<>(GameEvent.class, registryHolder.lookupOrThrow(Registries.GAME_EVENT), CraftGameEvent::new, FieldRename.NONE);
        }
        if (bukkitClass == MusicInstrument.class) {
            return new CraftRegistry<>(MusicInstrument.class, registryHolder.lookupOrThrow(Registries.INSTRUMENT), CraftMusicInstrument::new, FieldRename.NONE);
        }
        if (bukkitClass == MenuType.class) {
            return new CraftRegistry<>(MenuType.class, registryHolder.lookupOrThrow(Registries.MENU), CraftMenuType::new, FieldRename.NONE);
        }
        if (bukkitClass == PotionEffectType.class) {
            return new CraftRegistry<>(PotionEffectType.class, registryHolder.lookupOrThrow(Registries.MOB_EFFECT), CraftPotionEffectType::new, FieldRename.NONE);
        }
        if (bukkitClass == Structure.class) {
            return new CraftRegistry<>(Structure.class, registryHolder.lookupOrThrow(Registries.STRUCTURE), CraftStructure::new, FieldRename.NONE);
        }
        if (bukkitClass == StructureType.class) {
            return new CraftRegistry<>(StructureType.class, registryHolder.lookupOrThrow(Registries.STRUCTURE_TYPE), CraftStructureType::new, FieldRename.NONE);
        }
        if (bukkitClass == Villager.Type.class) {
            return new CraftRegistry<>(Villager.Type.class, registryHolder.lookupOrThrow(Registries.VILLAGER_TYPE), CraftVillager.CraftType::new, FieldRename.NONE);
        }
        if (bukkitClass == Villager.Profession.class) {
            return new CraftRegistry<>(Villager.Profession.class, registryHolder.lookupOrThrow(Registries.VILLAGER_PROFESSION), CraftVillager.CraftProfession::new, FieldRename.NONE);
        }
        if (bukkitClass == TrimMaterial.class) {
            return new CraftRegistry<>(TrimMaterial.class, registryHolder.lookupOrThrow(Registries.TRIM_MATERIAL), CraftTrimMaterial::new, FieldRename.NONE);
        }
        if (bukkitClass == TrimPattern.class) {
            return new CraftRegistry<>(TrimPattern.class, registryHolder.lookupOrThrow(Registries.TRIM_PATTERN), CraftTrimPattern::new, FieldRename.NONE);
        }
        if (bukkitClass == DamageType.class) {
            return new CraftRegistry<>(DamageType.class, registryHolder.lookupOrThrow(Registries.DAMAGE_TYPE), CraftDamageType::new, FieldRename.NONE);
        }
        if (bukkitClass == JukeboxSong.class) {
            return new CraftRegistry<>(JukeboxSong.class, registryHolder.lookupOrThrow(Registries.JUKEBOX_SONG), CraftJukeboxSong::new, FieldRename.NONE);
        }
        if (bukkitClass == Wolf.Variant.class) {
            return new CraftRegistry<>(Wolf.Variant.class, registryHolder.lookupOrThrow(Registries.WOLF_VARIANT), CraftWolf.CraftVariant::new, FieldRename.NONE);
        }
        if (bukkitClass == BlockType.class) {
            return new CraftRegistry<>(BlockType.class, registryHolder.lookupOrThrow(Registries.BLOCK), CraftBlockType::new, FieldRename.NONE);
        }
        if (bukkitClass == ItemType.class) {
            return new CraftRegistry<>(ItemType.class, registryHolder.lookupOrThrow(Registries.ITEM), CraftItemType::new, FieldRename.NONE);
        }
        if (bukkitClass == Frog.Variant.class) {
            return new CraftRegistry<>(Frog.Variant.class, registryHolder.lookupOrThrow(Registries.FROG_VARIANT), CraftFrog.CraftVariant::new, FieldRename.NONE);
        }
        if (bukkitClass == Cat.Type.class) {
            return new CraftRegistry<>(Cat.Type.class, registryHolder.lookupOrThrow(Registries.CAT_VARIANT), CraftCat.CraftType::new, FieldRename.NONE);
        }
        if (bukkitClass == MapCursor.Type.class) {
            return new CraftRegistry<>(MapCursor.Type.class, registryHolder.lookupOrThrow(Registries.MAP_DECORATION_TYPE), CraftMapCursor.CraftType::new, FieldRename.NONE);
        }
        if (bukkitClass == PatternType.class) {
            return new CraftRegistry<>(PatternType.class, registryHolder.lookupOrThrow(Registries.BANNER_PATTERN), CraftPatternType::new, FieldRename.NONE);
        }

        return null;
    }

    public static <B extends Keyed> B get(Registry<B> bukkit, NamespacedKey namespacedKey, ApiVersion apiVersion) {
        if (bukkit instanceof CraftRegistry<B, ?> craft) {
            return craft.get(namespacedKey, apiVersion);
        }

        if (bukkit instanceof Registry.SimpleRegistry<?> simple) {
            Class<?> bClass = simple.getType();

            if (bClass == Biome.class) {
                return bukkit.get(FieldRename.BIOME_RENAME.apply(namespacedKey, apiVersion));
            }

            if (bClass == EntityType.class) {
                return bukkit.get(FieldRename.ENTITY_TYPE_RENAME.apply(namespacedKey, apiVersion));
            }

            if (bClass == Particle.class) {
                return bukkit.get(FieldRename.PARTICLE_TYPE_RENAME.apply(namespacedKey, apiVersion));
            }
        }

        return bukkit.get(namespacedKey);
    }

    private final Class<? super B> bukkitClass;
    private final Map<NamespacedKey, B> cache = new HashMap<>();
    private final IRegistry<M> minecraftRegistry;
    private final BiFunction<NamespacedKey, M, B> minecraftToBukkit;
    private final BiFunction<NamespacedKey, ApiVersion, NamespacedKey> updater;
    private boolean init;

    public CraftRegistry(Class<? super B> bukkitClass, IRegistry<M> minecraftRegistry, BiFunction<NamespacedKey, M, B> minecraftToBukkit, BiFunction<NamespacedKey, ApiVersion, NamespacedKey> updater) {
        this.bukkitClass = bukkitClass;
        this.minecraftRegistry = minecraftRegistry;
        this.minecraftToBukkit = minecraftToBukkit;
        this.updater = updater;
    }

    public B get(NamespacedKey namespacedKey, ApiVersion apiVersion) {
        return get(updater.apply(namespacedKey, apiVersion));
    }

    @Override
    public B get(NamespacedKey namespacedKey) {
        B cached = cache.get(namespacedKey);
        if (cached != null) {
            return cached;
        }

        // Make sure that the bukkit class is loaded before creating an instance.
        // This ensures that only one instance with a given key is created.
        //
        // Without this code (when bukkit class is not loaded):
        // Registry#get -> #createBukkit -> (load class -> create default) -> put in cache
        // Result: Registry#get != <bukkitClass>.<field> for possible one registry item
        //
        // With this code (when bukkit class is not loaded):
        // Registry#get -> (load class -> create default) -> Registry#get -> get from cache
        // Result: Registry#get == <bukkitClass>.<field>
        if (!init) {
            init = true;
            try {
                Class.forName(bukkitClass.getName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Could not load registry class " + bukkitClass, e);
            }

            return get(namespacedKey);
        }

        B bukkit = createBukkit(namespacedKey, minecraftRegistry.getOptional(CraftNamespacedKey.toMinecraft(namespacedKey)).orElse(null));
        if (bukkit == null) {
            return null;
        }

        cache.put(namespacedKey, bukkit);

        return bukkit;
    }

    @NotNull
    @Override
    public B getOrThrow(@NotNull NamespacedKey namespacedKey) {
        B object = get(namespacedKey);

        Preconditions.checkArgument(object != null, "No %s registry entry found for key %s.", minecraftRegistry.key(), namespacedKey);

        return object;
    }

    @NotNull
    @Override
    public Stream<B> stream() {
        return minecraftRegistry.keySet().stream().map(minecraftKey -> get(CraftNamespacedKey.fromMinecraft(minecraftKey)));
    }

    @Override
    public Iterator<B> iterator() {
        return stream().iterator();
    }

    public B createBukkit(NamespacedKey namespacedKey, M minecraft) {
        if (minecraft == null) {
            return null;
        }

        return minecraftToBukkit.apply(namespacedKey, minecraft);
    }
}
