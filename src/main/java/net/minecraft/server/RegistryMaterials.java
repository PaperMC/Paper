package net.minecraft.server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistryMaterials<T> extends IRegistryWritable<T> {

    protected static final Logger LOGGER = LogManager.getLogger();
    private final ObjectList<T> bf = new ObjectArrayList(256);
    private final Object2IntMap<T> bg = new Object2IntOpenCustomHashMap(SystemUtils.k());
    private final BiMap<MinecraftKey, T> bh;
    private final BiMap<ResourceKey<T>, T> bi;
    private final Map<T, Lifecycle> bj;
    private Lifecycle bk;
    protected Object[] b;
    private int bl;

    public RegistryMaterials(ResourceKey<? extends IRegistry<T>> resourcekey, Lifecycle lifecycle) {
        super(resourcekey, lifecycle);
        this.bg.defaultReturnValue(-1);
        this.bh = HashBiMap.create();
        this.bi = HashBiMap.create();
        this.bj = Maps.newIdentityHashMap();
        this.bk = lifecycle;
    }

    public static <T> MapCodec<RegistryMaterials.a<T>> a(ResourceKey<? extends IRegistry<T>> resourcekey, MapCodec<T> mapcodec) {
        return RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(MinecraftKey.a.xmap(ResourceKey.b(resourcekey), ResourceKey::a).fieldOf("name").forGetter((registrymaterials_a) -> {
                return registrymaterials_a.a;
            }), Codec.INT.fieldOf("id").forGetter((registrymaterials_a) -> {
                return registrymaterials_a.b;
            }), mapcodec.forGetter((registrymaterials_a) -> {
                return registrymaterials_a.c;
            })).apply(instance, RegistryMaterials.a::new);
        });
    }

    @Override
    public <V extends T> V a(int i, ResourceKey<T> resourcekey, V v0, Lifecycle lifecycle) {
        return this.a(i, resourcekey, v0, lifecycle, true);
    }

    private <V extends T> V a(int i, ResourceKey<T> resourcekey, V v0, Lifecycle lifecycle, boolean flag) {
        Validate.notNull(resourcekey);
        Validate.notNull(v0);
        this.bf.size(Math.max(this.bf.size(), i + 1));
        this.bf.set(i, v0);
        this.bg.put(v0, i);
        this.b = null;
        if (flag && this.bi.containsKey(resourcekey)) {
            RegistryMaterials.LOGGER.debug("Adding duplicate key '{}' to registry", resourcekey);
        }

        if (this.bh.containsValue(v0)) {
            RegistryMaterials.LOGGER.error("Adding duplicate value '{}' to registry", v0);
        }

        this.bh.put(resourcekey.a(), v0);
        this.bi.put(resourcekey, v0);
        this.bj.put(v0, lifecycle);
        this.bk = this.bk.add(lifecycle);
        if (this.bl <= i) {
            this.bl = i + 1;
        }

        return v0;
    }

    @Override
    public <V extends T> V a(ResourceKey<T> resourcekey, V v0, Lifecycle lifecycle) {
        return this.a(this.bl, resourcekey, v0, lifecycle);
    }

    @Override
    public <V extends T> V a(OptionalInt optionalint, ResourceKey<T> resourcekey, V v0, Lifecycle lifecycle) {
        Validate.notNull(resourcekey);
        Validate.notNull(v0);
        T t0 = this.bi.get(resourcekey);
        int i;

        if (t0 == null) {
            i = optionalint.isPresent() ? optionalint.getAsInt() : this.bl;
        } else {
            i = this.bg.getInt(t0);
            if (optionalint.isPresent() && optionalint.getAsInt() != i) {
                throw new IllegalStateException("ID mismatch");
            }

            this.bg.removeInt(t0);
            this.bj.remove(t0);
        }

        return this.a(i, resourcekey, v0, lifecycle, false);
    }

    @Nullable
    @Override
    public MinecraftKey getKey(T t0) {
        return (MinecraftKey) this.bh.inverse().get(t0);
    }

    @Override
    public Optional<ResourceKey<T>> c(T t0) {
        return Optional.ofNullable(this.bi.inverse().get(t0));
    }

    @Override
    public int a(@Nullable T t0) {
        return this.bg.getInt(t0);
    }

    @Nullable
    @Override
    public T a(@Nullable ResourceKey<T> resourcekey) {
        return this.bi.get(resourcekey);
    }

    @Nullable
    @Override
    public T fromId(int i) {
        return i >= 0 && i < this.bf.size() ? this.bf.get(i) : null;
    }

    @Override
    public Lifecycle d(T t0) {
        return (Lifecycle) this.bj.get(t0);
    }

    @Override
    public Lifecycle b() {
        return this.bk;
    }

    public Iterator<T> iterator() {
        return Iterators.filter(this.bf.iterator(), Objects::nonNull);
    }

    @Nullable
    @Override
    public T get(@Nullable MinecraftKey minecraftkey) {
        return this.bh.get(minecraftkey);
    }

    @Override
    public Set<MinecraftKey> keySet() {
        return Collections.unmodifiableSet(this.bh.keySet());
    }

    @Override
    public Set<Entry<ResourceKey<T>, T>> d() {
        return Collections.unmodifiableMap(this.bi).entrySet();
    }

    @Nullable
    public T a(Random random) {
        if (this.b == null) {
            Collection<?> collection = this.bh.values();

            if (collection.isEmpty()) {
                return null;
            }

            this.b = collection.toArray(new Object[collection.size()]);
        }

        return SystemUtils.a(this.b, random);
    }

    public static <T> Codec<RegistryMaterials<T>> a(ResourceKey<? extends IRegistry<T>> resourcekey, Lifecycle lifecycle, Codec<T> codec) {
        return a(resourcekey, codec.fieldOf("element")).codec().listOf().xmap((list) -> {
            RegistryMaterials<T> registrymaterials = new RegistryMaterials<>(resourcekey, lifecycle);
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                RegistryMaterials.a<T> registrymaterials_a = (RegistryMaterials.a) iterator.next();

                registrymaterials.a(registrymaterials_a.b, registrymaterials_a.a, registrymaterials_a.c, lifecycle);
            }

            return registrymaterials;
        }, (registrymaterials) -> {
            Builder<RegistryMaterials.a<T>> builder = ImmutableList.builder();
            Iterator iterator = registrymaterials.iterator();

            while (iterator.hasNext()) {
                T t0 = iterator.next();

                builder.add(new RegistryMaterials.a<>((ResourceKey) registrymaterials.c(t0).get(), registrymaterials.a(t0), t0));
            }

            return builder.build();
        });
    }

    public static <T> Codec<RegistryMaterials<T>> b(ResourceKey<? extends IRegistry<T>> resourcekey, Lifecycle lifecycle, Codec<T> codec) {
        return RegistryDataPackCodec.a(resourcekey, lifecycle, codec);
    }

    public static <T> Codec<RegistryMaterials<T>> c(ResourceKey<? extends IRegistry<T>> resourcekey, Lifecycle lifecycle, Codec<T> codec) {
        return Codec.unboundedMap(MinecraftKey.a.xmap(ResourceKey.b(resourcekey), ResourceKey::a), codec).xmap((map) -> {
            RegistryMaterials<T> registrymaterials = new RegistryMaterials<>(resourcekey, lifecycle);

            map.forEach((resourcekey1, object) -> {
                registrymaterials.a(resourcekey1, object, lifecycle);
            });
            return registrymaterials;
        }, (registrymaterials) -> {
            return ImmutableMap.copyOf(registrymaterials.bi);
        });
    }

    public static class a<T> {

        public final ResourceKey<T> a;
        public final int b;
        public final T c;

        public a(ResourceKey<T> resourcekey, int i, T t0) {
            this.a = resourcekey;
            this.b = i;
            this.c = t0;
        }
    }
}
