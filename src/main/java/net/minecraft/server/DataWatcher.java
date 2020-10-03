package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataWatcher {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<Class<? extends Entity>, Integer> b = Maps.newHashMap();
    private final Entity entity;
    private final Map<Integer, DataWatcher.Item<?>> entries = Maps.newHashMap();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private boolean f = true;
    private boolean g;

    public DataWatcher(Entity entity) {
        this.entity = entity;
    }

    public static <T> DataWatcherObject<T> a(Class<? extends Entity> oclass, DataWatcherSerializer<T> datawatcherserializer) {
        if (DataWatcher.LOGGER.isDebugEnabled()) {
            try {
                Class<?> oclass1 = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());

                if (!oclass1.equals(oclass)) {
                    DataWatcher.LOGGER.debug("defineId called for: {} from {}", oclass, oclass1, new RuntimeException());
                }
            } catch (ClassNotFoundException classnotfoundexception) {
                ;
            }
        }

        int i;

        if (DataWatcher.b.containsKey(oclass)) {
            i = (Integer) DataWatcher.b.get(oclass) + 1;
        } else {
            int j = 0;
            Class oclass2 = oclass;

            while (oclass2 != Entity.class) {
                oclass2 = oclass2.getSuperclass();
                if (DataWatcher.b.containsKey(oclass2)) {
                    j = (Integer) DataWatcher.b.get(oclass2) + 1;
                    break;
                }
            }

            i = j;
        }

        if (i > 254) {
            throw new IllegalArgumentException("Data value id is too big with " + i + "! (Max is " + 254 + ")");
        } else {
            DataWatcher.b.put(oclass, i);
            return datawatcherserializer.a(i);
        }
    }

    public <T> void register(DataWatcherObject<T> datawatcherobject, T t0) {
        int i = datawatcherobject.a();

        if (i > 254) {
            throw new IllegalArgumentException("Data value id is too big with " + i + "! (Max is " + 254 + ")");
        } else if (this.entries.containsKey(i)) {
            throw new IllegalArgumentException("Duplicate id value for " + i + "!");
        } else if (DataWatcherRegistry.b(datawatcherobject.b()) < 0) {
            throw new IllegalArgumentException("Unregistered serializer " + datawatcherobject.b() + " for " + i + "!");
        } else {
            this.registerObject(datawatcherobject, t0);
        }
    }

    private <T> void registerObject(DataWatcherObject<T> datawatcherobject, T t0) {
        DataWatcher.Item<T> datawatcher_item = new DataWatcher.Item<>(datawatcherobject, t0);

        this.lock.writeLock().lock();
        this.entries.put(datawatcherobject.a(), datawatcher_item);
        this.f = false;
        this.lock.writeLock().unlock();
    }

    private <T> DataWatcher.Item<T> b(DataWatcherObject<T> datawatcherobject) {
        this.lock.readLock().lock();

        DataWatcher.Item datawatcher_item;

        try {
            datawatcher_item = (DataWatcher.Item) this.entries.get(datawatcherobject.a());
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Getting synched entity data");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Synched entity data");

            crashreportsystemdetails.a("Data ID", (Object) datawatcherobject);
            throw new ReportedException(crashreport);
        } finally {
            this.lock.readLock().unlock();
        }

        return datawatcher_item;
    }

    public <T> T get(DataWatcherObject<T> datawatcherobject) {
        return this.b(datawatcherobject).b();
    }

    public <T> void set(DataWatcherObject<T> datawatcherobject, T t0) {
        DataWatcher.Item<T> datawatcher_item = this.b(datawatcherobject);

        if (ObjectUtils.notEqual(t0, datawatcher_item.b())) {
            datawatcher_item.a(t0);
            this.entity.a(datawatcherobject);
            datawatcher_item.a(true);
            this.g = true;
        }

    }

    // CraftBukkit start - add method from above
    public <T> void markDirty(DataWatcherObject<T> datawatcherobject) {
        this.b(datawatcherobject).a(true);
        this.g = true;
    }
    // CraftBukkit end

    public boolean a() {
        return this.g;
    }

    public static void a(List<DataWatcher.Item<?>> list, PacketDataSerializer packetdataserializer) throws IOException {
        if (list != null) {
            int i = 0;

            for (int j = list.size(); i < j; ++i) {
                a(packetdataserializer, (DataWatcher.Item) list.get(i));
            }
        }

        packetdataserializer.writeByte(255);
    }

    @Nullable
    public List<DataWatcher.Item<?>> b() {
        List<DataWatcher.Item<?>> list = null;

        if (this.g) {
            this.lock.readLock().lock();
            Iterator iterator = this.entries.values().iterator();

            while (iterator.hasNext()) {
                DataWatcher.Item<?> datawatcher_item = (DataWatcher.Item) iterator.next();

                if (datawatcher_item.c()) {
                    datawatcher_item.a(false);
                    if (list == null) {
                        list = Lists.newArrayList();
                    }

                    list.add(datawatcher_item.d());
                }
            }

            this.lock.readLock().unlock();
        }

        this.g = false;
        return list;
    }

    @Nullable
    public List<DataWatcher.Item<?>> c() {
        List<DataWatcher.Item<?>> list = null;

        this.lock.readLock().lock();

        DataWatcher.Item datawatcher_item;

        for (Iterator iterator = this.entries.values().iterator(); iterator.hasNext(); list.add(datawatcher_item.d())) {
            datawatcher_item = (DataWatcher.Item) iterator.next();
            if (list == null) {
                list = Lists.newArrayList();
            }
        }

        this.lock.readLock().unlock();
        return list;
    }

    private static <T> void a(PacketDataSerializer packetdataserializer, DataWatcher.Item<T> datawatcher_item) throws IOException {
        DataWatcherObject<T> datawatcherobject = datawatcher_item.a();
        int i = DataWatcherRegistry.b(datawatcherobject.b());

        if (i < 0) {
            throw new EncoderException("Unknown serializer type " + datawatcherobject.b());
        } else {
            packetdataserializer.writeByte(datawatcherobject.a());
            packetdataserializer.d(i);
            datawatcherobject.b().a(packetdataserializer, datawatcher_item.b());
        }
    }

    @Nullable
    public static List<DataWatcher.Item<?>> a(PacketDataSerializer packetdataserializer) throws IOException {
        ArrayList arraylist = null;

        short short0;

        while ((short0 = packetdataserializer.readUnsignedByte()) != 255) {
            if (arraylist == null) {
                arraylist = Lists.newArrayList();
            }

            int i = packetdataserializer.i();
            DataWatcherSerializer<?> datawatcherserializer = DataWatcherRegistry.a(i);

            if (datawatcherserializer == null) {
                throw new DecoderException("Unknown serializer type " + i);
            }

            arraylist.add(a(packetdataserializer, short0, datawatcherserializer));
        }

        return arraylist;
    }

    private static <T> DataWatcher.Item<T> a(PacketDataSerializer packetdataserializer, int i, DataWatcherSerializer<T> datawatcherserializer) {
        return new DataWatcher.Item<>(datawatcherserializer.a(i), datawatcherserializer.a(packetdataserializer));
    }

    public boolean d() {
        return this.f;
    }

    public void e() {
        this.g = false;
        this.lock.readLock().lock();
        Iterator iterator = this.entries.values().iterator();

        while (iterator.hasNext()) {
            DataWatcher.Item<?> datawatcher_item = (DataWatcher.Item) iterator.next();

            datawatcher_item.a(false);
        }

        this.lock.readLock().unlock();
    }

    public static class Item<T> {

        private final DataWatcherObject<T> a;
        private T b;
        private boolean c;

        public Item(DataWatcherObject<T> datawatcherobject, T t0) {
            this.a = datawatcherobject;
            this.b = t0;
            this.c = true;
        }

        public DataWatcherObject<T> a() {
            return this.a;
        }

        public void a(T t0) {
            this.b = t0;
        }

        public T b() {
            return this.b;
        }

        public boolean c() {
            return this.c;
        }

        public void a(boolean flag) {
            this.c = flag;
        }

        public DataWatcher.Item<T> d() {
            return new DataWatcher.Item<>(this.a, this.a.b().a(this.b));
        }
    }
}
