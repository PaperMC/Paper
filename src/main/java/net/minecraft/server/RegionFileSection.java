package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.OptionalDynamic;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegionFileSection<R> implements AutoCloseable {

    private static final Logger LOGGER = LogManager.getLogger();
    private final IOWorker b;
    private final Long2ObjectMap<Optional<R>> c = new Long2ObjectOpenHashMap();
    private final LongLinkedOpenHashSet d = new LongLinkedOpenHashSet();
    private final Function<Runnable, Codec<R>> e;
    private final Function<Runnable, R> f;
    private final DataFixer g;
    private final DataFixTypes h;

    public RegionFileSection(File file, Function<Runnable, Codec<R>> function, Function<Runnable, R> function1, DataFixer datafixer, DataFixTypes datafixtypes, boolean flag) {
        this.e = function;
        this.f = function1;
        this.g = datafixer;
        this.h = datafixtypes;
        this.b = new IOWorker(file, flag, file.getName());
    }

    protected void a(BooleanSupplier booleansupplier) {
        while (!this.d.isEmpty() && booleansupplier.getAsBoolean()) {
            ChunkCoordIntPair chunkcoordintpair = SectionPosition.a(this.d.firstLong()).r();

            this.d(chunkcoordintpair);
        }

    }

    @Nullable
    protected Optional<R> c(long i) {
        return (Optional) this.c.get(i);
    }

    protected Optional<R> d(long i) {
        SectionPosition sectionposition = SectionPosition.a(i);

        if (this.b(sectionposition)) {
            return Optional.empty();
        } else {
            Optional<R> optional = this.c(i);

            if (optional != null) {
                return optional;
            } else {
                this.b(sectionposition.r());
                optional = this.c(i);
                if (optional == null) {
                    throw (IllegalStateException) SystemUtils.c((Throwable) (new IllegalStateException()));
                } else {
                    return optional;
                }
            }
        }
    }

    protected boolean b(SectionPosition sectionposition) {
        return World.b(SectionPosition.c(sectionposition.b()));
    }

    protected R e(long i) {
        Optional<R> optional = this.d(i);

        if (optional.isPresent()) {
            return optional.get();
        } else {
            R r0 = this.f.apply(() -> {
                this.a(i);
            });

            this.c.put(i, Optional.of(r0));
            return r0;
        }
    }

    private void b(ChunkCoordIntPair chunkcoordintpair) {
        this.a(chunkcoordintpair, DynamicOpsNBT.a, this.c(chunkcoordintpair));
    }

    @Nullable
    private NBTTagCompound c(ChunkCoordIntPair chunkcoordintpair) {
        try {
            return this.b.a(chunkcoordintpair);
        } catch (IOException ioexception) {
            RegionFileSection.LOGGER.error("Error reading chunk {} data from disk", chunkcoordintpair, ioexception);
            return null;
        }
    }

    private <T> void a(ChunkCoordIntPair chunkcoordintpair, DynamicOps<T> dynamicops, @Nullable T t0) {
        if (t0 == null) {
            for (int i = 0; i < 16; ++i) {
                this.c.put(SectionPosition.a(chunkcoordintpair, i).s(), Optional.empty());
            }
        } else {
            Dynamic<T> dynamic = new Dynamic(dynamicops, t0);
            int j = a(dynamic);
            int k = SharedConstants.getGameVersion().getWorldVersion();
            boolean flag = j != k;
            Dynamic<T> dynamic1 = this.g.update(this.h.a(), dynamic, j, k);
            OptionalDynamic<T> optionaldynamic = dynamic1.get("Sections");

            for (int l = 0; l < 16; ++l) {
                long i1 = SectionPosition.a(chunkcoordintpair, l).s();
                Optional<R> optional = optionaldynamic.get(Integer.toString(l)).result().flatMap((dynamic2) -> {
                    DataResult dataresult = ((Codec) this.e.apply(() -> {
                        this.a(i1);
                    })).parse(dynamic2);
                    Logger logger = RegionFileSection.LOGGER;

                    logger.getClass();
                    return dataresult.resultOrPartial(logger::error);
                });

                this.c.put(i1, optional);
                optional.ifPresent((object) -> {
                    this.b(i1);
                    if (flag) {
                        this.a(i1);
                    }

                });
            }
        }

    }

    private void d(ChunkCoordIntPair chunkcoordintpair) {
        Dynamic<NBTBase> dynamic = this.a(chunkcoordintpair, DynamicOpsNBT.a);
        NBTBase nbtbase = (NBTBase) dynamic.getValue();

        if (nbtbase instanceof NBTTagCompound) {
            this.b.a(chunkcoordintpair, (NBTTagCompound) nbtbase);
        } else {
            RegionFileSection.LOGGER.error("Expected compound tag, got {}", nbtbase);
        }

    }

    private <T> Dynamic<T> a(ChunkCoordIntPair chunkcoordintpair, DynamicOps<T> dynamicops) {
        Map<T, T> map = Maps.newHashMap();

        for (int i = 0; i < 16; ++i) {
            long j = SectionPosition.a(chunkcoordintpair, i).s();

            this.d.remove(j);
            Optional<R> optional = (Optional) this.c.get(j);

            if (optional != null && optional.isPresent()) {
                DataResult<T> dataresult = ((Codec) this.e.apply(() -> {
                    this.a(j);
                })).encodeStart(dynamicops, optional.get());
                String s = Integer.toString(i);
                Logger logger = RegionFileSection.LOGGER;

                logger.getClass();
                dataresult.resultOrPartial(logger::error).ifPresent((object) -> {
                    map.put(dynamicops.createString(s), object);
                });
            }
        }

        return new Dynamic(dynamicops, dynamicops.createMap(ImmutableMap.of(dynamicops.createString("Sections"), dynamicops.createMap(map), dynamicops.createString("DataVersion"), dynamicops.createInt(SharedConstants.getGameVersion().getWorldVersion()))));
    }

    protected void b(long i) {}

    protected void a(long i) {
        Optional<R> optional = (Optional) this.c.get(i);

        if (optional != null && optional.isPresent()) {
            this.d.add(i);
        } else {
            RegionFileSection.LOGGER.warn("No data for position: {}", SectionPosition.a(i));
        }
    }

    private static int a(Dynamic<?> dynamic) {
        return dynamic.get("DataVersion").asInt(1945);
    }

    public void a(ChunkCoordIntPair chunkcoordintpair) {
        if (!this.d.isEmpty()) {
            for (int i = 0; i < 16; ++i) {
                long j = SectionPosition.a(chunkcoordintpair, i).s();

                if (this.d.contains(j)) {
                    this.d(chunkcoordintpair);
                    return;
                }
            }
        }

    }

    public void close() throws IOException {
        this.b.close();
    }
}
