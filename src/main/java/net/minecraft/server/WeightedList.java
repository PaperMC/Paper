package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class WeightedList<U> {

    protected final List<WeightedList.a<U>> list; // Paper - decompile conflict
    private final Random b;
    private final boolean isUnsafe; // Paper

    // Paper start - add useClone option
    public WeightedList() { this(true); }
    public WeightedList(boolean isUnsafe) {
        this(Lists.newArrayList(), isUnsafe);
    }

    private WeightedList(List<WeightedList.a<U>> list) { this(list, true); }
    private WeightedList(List<WeightedList.a<U>> list, boolean isUnsafe) {
        this.isUnsafe = isUnsafe;
        // Paper end
        this.b = new Random();
        this.list = Lists.newArrayList(list); // Paper - decompile conflict
    }

    public static <U> Codec<WeightedList<U>> a(Codec<U> codec) {
        return WeightedList.a.a(codec).listOf().xmap(WeightedList::new, (weightedlist) -> { // Paper - decompile conflict
            return weightedlist.list; // Paper - decompile conflict
        });
    }

    public WeightedList<U> a(U u0, int i) {
        this.list.add(new WeightedList.a<>(u0, i)); // Paper - decompile conflict
        return this;
    }

    public WeightedList<U> a() {
        return this.a(this.b);
    }

    public WeightedList<U> a(Random random) {
        // Paper start - make concurrent safe, work off a clone of the list
        List<WeightedList.a<U>> list = isUnsafe ? new java.util.ArrayList<WeightedList.a<U>>(this.list) : this.list;
        list.forEach((weightedlist_a) -> weightedlist_a.a(random.nextFloat()));
        list.sort(Comparator.comparingDouble(a::c));
        return isUnsafe ? new WeightedList<>(list, isUnsafe) : this;
        // Paper end
    }

    public boolean b() {
        return this.list.isEmpty(); // Paper - decompile conflict
    }

    public Stream<U> c() {
        return this.list.stream().map(WeightedList.a::a); // Paper - decompile conflict
    }

    public U b(Random random) {
        return this.a(random).c().findFirst().orElseThrow(RuntimeException::new);
    }

    public String toString() {
        return "WeightedList[" + this.list + "]"; // Paper - decompile conflict
    }

    public static class a<T> {

        private final T a;
        private final int b;
        private double c;

        private a(T t0, int i) {
            this.b = i;
            this.a = t0;
        }

        private double c() {
            return this.c;
        }

        private void a(float f) {
            this.c = -Math.pow((double) f, (double) (1.0F / (float) this.b));
        }

        public T a() {
            return this.a;
        }

        public String toString() {
            return "" + this.b + ":" + this.a;
        }

        public static <E> Codec<WeightedList.a<E>> a(final Codec<E> codec) {
            return new Codec<WeightedList.a<E>>() {
                public <T> DataResult<Pair<WeightedList.a<E>, T>> decode(DynamicOps<T> dynamicops, T t0) {
                    Dynamic<T> dynamic = new Dynamic(dynamicops, t0);
                    return dynamic.get("data").flatMap(codec::parse).map((object) -> { // Paper - decompile error
                        return new WeightedList.a<>(object, dynamic.get("weight").asInt(1));
                    }).map((weightedlist_a) -> {
                        return Pair.of(weightedlist_a, dynamicops.empty());
                    });
                }

                public <T> DataResult<T> encode(WeightedList.a<E> weightedlist_a, DynamicOps<T> dynamicops, T t0) {
                    return dynamicops.mapBuilder().add("weight", dynamicops.createInt(weightedlist_a.b)).add("data", codec.encodeStart(dynamicops, weightedlist_a.a)).build(t0);
                }
            };
        }
    }
}
