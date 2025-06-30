package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.entry.RegistryEntryMeta;
import io.papermc.paper.registry.set.NamedRegistryKeySetImpl;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.util.Holderable;
import io.papermc.paper.util.MCUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
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

    private static net.minecraft.core.RegistryAccess registry;

    public static void setMinecraftRegistry(final net.minecraft.core.RegistryAccess registry) {
        Preconditions.checkState(CraftRegistry.registry == null, "Registry already set");
        CraftRegistry.registry = registry;
    }

    public static net.minecraft.core.RegistryAccess getMinecraftRegistry() {
        return CraftRegistry.registry;
    }

    public static <E> net.minecraft.core.Registry<E> getMinecraftRegistry(ResourceKey<? extends net.minecraft.core.Registry<E>> key) {
        return CraftRegistry.getMinecraftRegistry().lookupOrThrow(key);
    }

    /**
     * Usage note: Only use this method to delegate the conversion methods from the individual Craft classes to here.
     * Do not use it in other parts of CraftBukkit, use the methods in the respective Craft classes instead.
     *
     * @param minecraft   the minecraft representation
     * @param registryKey the registry key of the minecraft registry to use
     * @return the bukkit representation of the minecraft value
     */
    public static <B extends Keyed, M> B minecraftToBukkit(M minecraft, ResourceKey<? extends net.minecraft.core.Registry<M>> registryKey) {
        Preconditions.checkArgument(minecraft != null);

        net.minecraft.core.Registry<M> registry = CraftRegistry.getMinecraftRegistry(registryKey);
        final Registry<B> bukkitRegistry = RegistryAccess.registryAccess().getRegistry(PaperRegistries.registryFromNms(registryKey));
        final java.util.Optional<ResourceKey<M>> resourceKey = registry.getResourceKey(minecraft);
        if (resourceKey.isEmpty() && bukkitRegistry instanceof final CraftRegistry<?, ?> craftRegistry && craftRegistry.supportsDirectHolders()) {
            return ((CraftRegistry<B, M>) bukkitRegistry).createBukkit(Holder.direct(minecraft));
        } else if (resourceKey.isEmpty()) {
            throw new IllegalStateException(String.format("Cannot convert '%s' to bukkit representation, since it is not registered.", minecraft));
        }
        final B bukkit = bukkitRegistry.get(CraftNamespacedKey.fromMinecraft(resourceKey.get().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static <B extends Keyed, M> B minecraftHolderToBukkit(final Holder<M> minecraft, final ResourceKey<? extends net.minecraft.core.Registry<M>> registryKey) {
        Preconditions.checkArgument(minecraft != null);

        final Registry<B> bukkitRegistry = RegistryAccess.registryAccess().getRegistry(PaperRegistries.registryFromNms(registryKey));
        final B bukkit = switch (minecraft) {
            case final Holder.Direct<M> direct -> {
                if (!(bukkitRegistry instanceof final CraftRegistry<?, ?> craftRegistry) || !craftRegistry.supportsDirectHolders()) {
                    throw new IllegalArgumentException("Cannot convert direct holder to bukkit representation");
                }
                yield ((CraftRegistry<B, M>) bukkitRegistry).createBukkit(direct);
            }
            case final Holder.Reference<M> reference -> bukkitRegistry.get(MCUtil.fromResourceKey(reference.key()));
            default -> throw new IllegalArgumentException("Unknown holder: " + minecraft);
        };
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
    @SuppressWarnings("unchecked")
    public static <B extends Keyed, M> M bukkitToMinecraft(final B bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return ((Handleable<M>) bukkit).getHandle();
    }

    @SuppressWarnings("unchecked")
    public static <B extends Keyed, M> Holder<M> bukkitToMinecraftHolder(final B bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return ((Holderable<M>) bukkit).getHolder();
    }

    // Paper start - fixup upstream being dum
    public static <T extends Keyed, M> Optional<T> unwrapAndConvertHolder(final RegistryKey<T> registryKey, final Holder<M> value) { // todo recheck usage with holderable support
        final Registry<T> registry = RegistryAccess.registryAccess().getRegistry(registryKey);
        if (registry instanceof final CraftRegistry<?,?> craftRegistry && craftRegistry.supportsDirectHolders() && value.kind() == Holder.Kind.DIRECT) {
            return Optional.of(((CraftRegistry<T, M>) registry).createBukkit(value));
        }
        return value.unwrapKey().map(key -> registry.get(CraftNamespacedKey.fromMinecraft(key.location())));
    }
    // Paper end - fixup upstream being dum

    // Paper - move to PaperRegistries

    // Paper - NOTE: As long as all uses of the method below relate to *serialization* via ConfigurationSerializable, it's fine
    public static <B extends Keyed> B get(RegistryKey<B> bukkitKey, NamespacedKey namespacedKey, ApiVersion apiVersion) {
        final Registry<B> bukkit = RegistryAccess.registryAccess().getRegistry(bukkitKey);
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
        this.lockReferenceHolders = !this.minecraftToBukkit.constructorUsesHolder();
    }

    public void lockReferenceHolders() {
        Preconditions.checkState(this.cache.isEmpty(), "Registry %s is already loaded", this.minecraftRegistry.key());

        try {
            Class.forName(this.bukkitClass.getName()); // this should always trigger the initialization of the class
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load class " + this.bukkitClass.getSimpleName(), e);
        }
        if (!this.minecraftToBukkit.constructorUsesHolder()) {
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

        // Important to use the ResourceKey<?> "get" method below because it will work before registry is frozen
        final Optional<Holder.Reference<M>> holderOptional = this.minecraftRegistry.get(MCUtil.toResourceKey(this.minecraftRegistry.key(), namespacedKey));
        final Holder.Reference<M> holder;
        if (holderOptional.isPresent()) {
            holder = holderOptional.get();
        } else if (!this.lockReferenceHolders && this.minecraftToBukkit.constructorUsesHolder()) { // only works if its Holderable
            // we lock the reference holders after the preload class has been initialized
            // this is to support the vanilla mechanic of preventing vanilla registry entries being loaded. We need
            // to create something to fill the API constant fields, so we create a dummy reference holder.
            holder = Holder.Reference.createStandAlone(this.invalidHolderOwner, MCUtil.toResourceKey(this.minecraftRegistry.key(), namespacedKey));
        } else {
            return null;
        }
        final B bukkit = this.createBukkit(holder);

        this.cache.put(namespacedKey, bukkit);

        return bukkit;
    }

    @NotNull
    @Override
    public Stream<B> stream() {
        return this.minecraftRegistry.keySet().stream().map(minecraftKey -> this.get(CraftNamespacedKey.fromMinecraft(minecraftKey)));
    }

    @NotNull
    @Override
    public Stream<NamespacedKey> keyStream() {
        return this.minecraftRegistry.keySet().stream().map(CraftNamespacedKey::fromMinecraft);
    }

    @Override
    public int size() {
        return this.minecraftRegistry.size();
    }

    @Override
    public Iterator<B> iterator() {
        return this.stream().iterator();
    }

    public B createBukkit(Holder<M> minecraft) {
        return this.minecraftToBukkit.createBukkit(minecraft);
    }

    public boolean supportsDirectHolders() {
        return this.minecraftToBukkit.supportsDirectHolders();
    }

    @Override
    public NamespacedKey getKey(final B value) {
        if (value instanceof Holderable<?> holderable) {
            return holderable.getKeyOrNull();
        }
        return value.getKey();
    }

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
