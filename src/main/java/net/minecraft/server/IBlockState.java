package net.minecraft.server;

import com.google.common.base.MoreObjects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class IBlockState<T extends Comparable<T>> {

    private final Class<T> a;
    private final String b;
    private Integer c;
    private final Codec<T> d;
    private final Codec<IBlockState.a<T>> e;

    protected IBlockState(String s, Class<T> oclass) {
        this.d = Codec.STRING.comapFlatMap((s1) -> {
            return (DataResult) this.b(s1).map(DataResult::success).orElseGet(() -> {
                return DataResult.error("Unable to read property: " + this + " with value: " + s1);
            });
        }, this::a);
        this.e = this.d.xmap(this::b, IBlockState.a::b);
        this.a = oclass;
        this.b = s;
    }

    public IBlockState.a<T> b(T t0) {
        return new IBlockState.a<>(this, t0);
    }

    public IBlockState.a<T> a(IBlockDataHolder<?, ?> iblockdataholder) {
        return new IBlockState.a<>(this, iblockdataholder.get(this));
    }

    public Stream<IBlockState.a<T>> c() {
        return this.getValues().stream().map(this::b);
    }

    public Codec<IBlockState.a<T>> e() {
        return this.e;
    }

    public String getName() {
        return this.b;
    }

    public Class<T> getType() {
        return this.a;
    }

    public abstract Collection<T> getValues();

    public abstract String a(T t0);

    public abstract Optional<T> b(String s);

    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", this.b).add("clazz", this.a).add("values", this.getValues()).toString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof IBlockState)) {
            return false;
        } else {
            IBlockState<?> iblockstate = (IBlockState) object;

            return this.a.equals(iblockstate.a) && this.b.equals(iblockstate.b);
        }
    }

    public final int hashCode() {
        if (this.c == null) {
            this.c = this.b();
        }

        return this.c;
    }

    public int b() {
        return 31 * this.a.hashCode() + this.b.hashCode();
    }

    public static final class a<T extends Comparable<T>> {

        private final IBlockState<T> a;
        private final T b;

        private a(IBlockState<T> iblockstate, T t0) {
            if (!iblockstate.getValues().contains(t0)) {
                throw new IllegalArgumentException("Value " + t0 + " does not belong to property " + iblockstate);
            } else {
                this.a = iblockstate;
                this.b = t0;
            }
        }

        public IBlockState<T> a() {
            return this.a;
        }

        public T b() {
            return this.b;
        }

        public String toString() {
            return this.a.getName() + "=" + this.a.a(this.b);
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            } else if (!(object instanceof IBlockState.a)) {
                return false;
            } else {
                IBlockState.a<?> iblockstate_a = (IBlockState.a) object;

                return this.a == iblockstate_a.a && this.b.equals(iblockstate_a.b);
            }
        }

        public int hashCode() {
            int i = this.a.hashCode();

            i = 31 * i + this.b.hashCode();
            return i;
        }
    }
}
