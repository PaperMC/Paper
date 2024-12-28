package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.entry.RegistryEntryMeta;
import io.papermc.paper.registry.set.NamedRegistryKeySetImpl;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.util.Holderable;
import java.util.Collection;
import io.papermc.paper.util.MCUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class CraftRegistry<B extends Keyed, M> implements Registry<B> {

    private static RegistryAccess registry;

    public static void setMinecraftRegistry(RegistryAccess registry) {
        Preconditions.checkState(CraftRegistry.registry == null, "Registry already set");
        CraftRegistry.registry = registry;
    }

    public static RegistryAccess getMinecraftRegistry() {
        return CraftRegistry.registry;
    }

    public static <E> net.minecraft.core.Registry<E> getMinecraftRegistry(ResourceKey<? extends net.minecraft.core.Registry<E>> key) {
        return CraftRegistry.getMinecraftRegistry().lookupOrThrow(key);
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
    public static <B extends Keyed, M> B minecraftToBukkit(M minecraft, ResourceKey<net.minecraft.core.Registry<M>> registryKey, Registry<B> bukkitRegistry) {
        Preconditions.checkArgument(minecraft != null);

        net.minecraft.core.Registry<M> registry = CraftRegistry.getMinecraftRegistry(registryKey);
        // Paper start - support direct Holders
        final java.util.Optional<ResourceKey<M>> resourceKey = registry.getResourceKey(minecraft);
        if (resourceKey.isEmpty() && bukkitRegistry instanceof final CraftRegistry<?, ?> craftRegistry && craftRegistry.supportsDirectHolders()) {
            return ((CraftRegistry<B, M>) bukkitRegistry).convertDirectHolder(Holder.direct(minecraft));
        } else if (resourceKey.isEmpty()) {
            throw new IllegalStateException(String.format("Cannot convert '%s' to bukkit representation, since it is not registered.", minecraft));
        }
        final B bukkit = bukkitRegistry.get(CraftNamespacedKey.fromMinecraft(resourceKey.get().location()));
        // Paper end - support direct Holders

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    // Paper start - support direct Holders
    public static <B extends Keyed, M> B minecraftHolderToBukkit(final Holder<M> minecraft, final Registry<B> bukkitRegistry) {
        Preconditions.checkArgument(minecraft != null);

        final B bukkit = switch (minecraft) {
            case final Holder.Direct<M> direct -> {
                if (!(bukkitRegistry instanceof final CraftRegistry<?, ?> craftRegistry) || !craftRegistry.supportsDirectHolders()) {
                    throw new IllegalArgumentException("Cannot convert direct holder to bukkit representation");
                }
                yield ((CraftRegistry<B, M>) bukkitRegistry).convertDirectHolder(direct);
            }
            case final Holder.Reference<M> reference -> bukkitRegistry.get(io.papermc.paper.util.MCUtil.fromResourceKey(reference.key()));
            default -> throw new IllegalArgumentException("Unknown holder: " + minecraft);
        };
        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }
    // Paper end - support direct Holders

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

    public static <B extends Keyed, M> Holder<M> bukkitToMinecraftHolder(B bukkit, ResourceKey<net.minecraft.core.Registry<M>> registryKey) {
        Preconditions.checkArgument(bukkit != null);
        // Paper start - support direct Holder
        if (bukkit instanceof io.papermc.paper.util.Holderable<?>) {
            return ((io.papermc.paper.util.Holderable<M>) bukkit).getHolder();
        }
        // Paper end - support direct Holder

        net.minecraft.core.Registry<M> registry = CraftRegistry.getMinecraftRegistry(registryKey);

        if (registry.wrapAsHolder(CraftRegistry.bukkitToMinecraft(bukkit)) instanceof Holder.Reference<M> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own registry entry with out properly registering it.");
    }

    // Paper start - fixup upstream being dum
    public static <T extends org.bukkit.Keyed, M> java.util.Optional<T> unwrapAndConvertHolder(final io.papermc.paper.registry.RegistryKey<T> registryKey, final Holder<M> value) {
        return unwrapAndConvertHolder(io.papermc.paper.registry.RegistryAccess.registryAccess().getRegistry(registryKey), value);
    }

    public static <T extends org.bukkit.Keyed, M> java.util.Optional<T> unwrapAndConvertHolder(final Registry<T> registry, final Holder<M> value) {
        if (registry instanceof CraftRegistry<?,?> craftRegistry && craftRegistry.supportsDirectHolders() && value.kind() == Holder.Kind.DIRECT) {
            return java.util.Optional.of(((CraftRegistry<T, M>) registry).convertDirectHolder(value));
        }
        return value.unwrapKey().map(key -> registry.get(CraftNamespacedKey.fromMinecraft(key.location())));
    }
    // Paper end - fixup upstream being dum

    // Paper - move to PaperRegistries

    // Paper - NOTE: As long as all uses of the method below relate to *serialization* via ConfigurationSerializable, it's fine
    public static <B extends Keyed> B get(Registry<B> bukkit, NamespacedKey namespacedKey, ApiVersion apiVersion) {
        if (bukkit instanceof CraftRegistry<B, ?> craft) {
            return craft.get(craft.serializationUpdater.apply(namespacedKey, apiVersion)); // Paper
        }

        if (bukkit instanceof Registry.SimpleRegistry<?> simple) {
            Class<?> bClass = simple.getType();

            if (bClass == EntityType.class) {
                return bukkit.get(FieldRename.ENTITY_TYPE_RENAME.apply(namespacedKey, apiVersion));
            }

            if (bClass == Particle.class) {
                return bukkit.get(FieldRename.PARTICLE_TYPE_RENAME.apply(namespacedKey, apiVersion));
            }
        }

        return bukkit.get(namespacedKey);
    }

    private final Class<?> bukkitClass; // Paper - relax preload class
    private final Map<NamespacedKey, B> cache = new HashMap<>();
    private final net.minecraft.core.Registry<M> minecraftRegistry;
    private final io.papermc.paper.registry.entry.RegistryTypeMapper<M, B> minecraftToBukkit; // Paper - switch to Holder
    private final BiFunction<NamespacedKey, ApiVersion, NamespacedKey> serializationUpdater; // Paper - rename to make it *clear* what it is *only* for
    private final InvalidHolderOwner invalidHolderOwner = new InvalidHolderOwner();
    private boolean lockReferenceHolders;

    public CraftRegistry(Class<?> bukkitClass, net.minecraft.core.Registry<M> minecraftRegistry, BiFunction<? super NamespacedKey, M, B> minecraftToBukkit, BiFunction<NamespacedKey, ApiVersion, NamespacedKey> serializationUpdater) { // Paper - relax preload class
        // Paper start - switch to Holder
        this(bukkitClass, minecraftRegistry, new io.papermc.paper.registry.entry.RegistryTypeMapper<>(minecraftToBukkit), serializationUpdater);
    }
    public CraftRegistry(final RegistryEntryMeta.ServerSide<M, B> meta, final net.minecraft.core.Registry<M> minecraftRegistry) {
        this(meta.classToPreload(), minecraftRegistry, meta.registryTypeMapper(), meta.serializationUpdater());
    }
    public CraftRegistry(Class<?> bukkitClass, net.minecraft.core.Registry<M> minecraftRegistry, io.papermc.paper.registry.entry.RegistryTypeMapper<M, B> minecraftToBukkit, BiFunction<NamespacedKey, ApiVersion, NamespacedKey> serializationUpdater) { // Paper - relax preload class
        // Paper end - support Holders
        this.bukkitClass = bukkitClass;
        this.minecraftRegistry = minecraftRegistry;
        this.minecraftToBukkit = minecraftToBukkit;
        this.serializationUpdater = serializationUpdater;
        this.lockReferenceHolders = !this.minecraftToBukkit.supportsDirectHolders();
    }

    public void lockReferenceHolders() {
        Preconditions.checkState(this.cache.isEmpty(), "Registry %s is already loaded", this.minecraftRegistry.key());

        try {
            Class.forName(this.bukkitClass.getName()); // this should always trigger the initialization of the class
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load class " + this.bukkitClass.getSimpleName(), e);
        }
        if (!this.minecraftToBukkit.supportsDirectHolders()) {
            return;
        }
        Preconditions.checkState(!this.lockReferenceHolders, "Reference holders are already locked");
        this.lockReferenceHolders = true;
    }

    // Paper - inline into CraftRegistry#get(Registry, NamespacedKey, ApiVersion) above

    @Override
    public B get(NamespacedKey namespacedKey) {
        B cached = this.cache.get(namespacedKey);
        if (cached != null) {
            return cached;
        }

        final Optional<Holder.Reference<M>> holderOptional = this.minecraftRegistry.get(CraftNamespacedKey.toMinecraft(namespacedKey));
        final Holder.Reference<M> holder;
        if (holderOptional.isPresent()) {
            holder = holderOptional.get();
        } else if (!this.lockReferenceHolders && this.minecraftToBukkit.supportsDirectHolders()) { // only works if its Holderable
            // we lock the reference holders after the preload class has been initialized
            // this is to support the vanilla mechanic of preventing vanilla registry entries being loaded. We need
            // to create something to fill the API constant fields, so we create a dummy reference holder.
            holder = Holder.Reference.createStandAlone(this.invalidHolderOwner, MCUtil.toResourceKey(this.minecraftRegistry.key(), namespacedKey));
        } else {
            holder = null;
        }
        final B bukkit = this.createBukkit(namespacedKey, holder);
        if (bukkit == null) {
            return null;
        }

        this.cache.put(namespacedKey, bukkit);

        return bukkit;
    }

    @NotNull
    @Override
    public B getOrThrow(@NotNull NamespacedKey namespacedKey) {
        B object = this.get(namespacedKey);

        Preconditions.checkArgument(object != null, "No %s registry entry found for key %s.", this.minecraftRegistry.key(), namespacedKey);

        return object;
    }

    @NotNull
    @Override
    public Stream<B> stream() {
        return this.minecraftRegistry.keySet().stream().map(minecraftKey -> this.get(CraftNamespacedKey.fromMinecraft(minecraftKey)));
    }

    @Override
    public Iterator<B> iterator() {
        return this.stream().iterator();
    }

    public B createBukkit(NamespacedKey namespacedKey, Holder<M> minecraft) { // Paper - switch to Holder
        if (minecraft == null) {
            return null;
        }

        return this.minecraftToBukkit.createBukkit(namespacedKey, minecraft); // Paper - switch to Holder
    }

    // Paper start - support Direct Holders
    public boolean supportsDirectHolders() {
        return this.minecraftToBukkit.supportsDirectHolders();
    }

    public B convertDirectHolder(Holder<M> holder) {
        return this.minecraftToBukkit.convertDirectHolder(holder);
    }
    // Paper end - support Direct Holders

    // Paper start - improve Registry
    @Override
    public NamespacedKey getKey(final B value) {
        if (value instanceof Holderable<?> holderable) {
            return holderable.getKeyOrNull();
        }
        return value.getKey();
    }
    // Paper end - improve Registry

    // Paper start - RegistrySet API
    @Override
    public boolean hasTag(final io.papermc.paper.registry.tag.TagKey<B> key) {
        return this.minecraftRegistry.get(net.minecraft.tags.TagKey.create(this.minecraftRegistry.key(), io.papermc.paper.adventure.PaperAdventure.asVanilla(key.key()))).isPresent();
    }

    @Override
    public io.papermc.paper.registry.tag.Tag<B> getTag(final io.papermc.paper.registry.tag.TagKey<B> key) {
        final net.minecraft.core.HolderSet.Named<M> namedHolderSet = this.minecraftRegistry.get(io.papermc.paper.registry.PaperRegistries.toNms(key)).orElseThrow();
        return new io.papermc.paper.registry.set.NamedRegistryKeySetImpl<>(key, namedHolderSet);
    }

    @Override
    public Collection<Tag<B>> getTags() {
        return this.minecraftRegistry.getTags().<Tag<B>>map(NamedRegistryKeySetImpl::new).toList();
    }
    // Paper end - RegistrySet API

    final class InvalidHolderOwner implements HolderOwner<M> {
    }
}
