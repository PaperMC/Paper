package net.minecraft.server;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class RegistryBlockID<T> implements Registry<T> {

    private int a;
    private final IdentityHashMap<T, Integer> b;
    private final List<T> c;

    public RegistryBlockID() {
        this(512);
    }

    public RegistryBlockID(int i) {
        this.c = Lists.newArrayListWithExpectedSize(i);
        this.b = new IdentityHashMap(i);
    }

    public void a(T t0, int i) {
        this.b.put(t0, i);

        while (this.c.size() <= i) {
            this.c.add((Object) null);
        }

        this.c.set(i, t0);
        if (this.a <= i) {
            this.a = i + 1;
        }

    }

    public void b(T t0) {
        this.a(t0, this.a);
    }

    public int getId(T t0) {
        Integer integer = (Integer) this.b.get(t0);

        return integer == null ? -1 : integer;
    }

    @Nullable
    @Override
    public final T fromId(int i) {
        return i >= 0 && i < this.c.size() ? this.c.get(i) : null;
    }

    public Iterator<T> iterator() {
        return Iterators.filter(this.c.iterator(), Predicates.notNull());
    }

    public int a() {
        return this.b.size();
    }
}
