package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.OptionalDynamic;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class WeightedList<U> {

    protected final List<WeightedList.a<U>> a;
    private final Random b;

    public WeightedList() {
        this(Lists.newArrayList());
    }

    private WeightedList(List<WeightedList.a<U>> list) {
        this.b = new Random();
        this.a = Lists.newArrayList(list);
    }

    public static <U> Codec<WeightedList<U>> a(Codec<U> codec) {
        return WeightedList.a.a(codec).listOf().xmap(WeightedList::new, (weightedlist) -> {
            return weightedlist.a;
        });
    }

    public WeightedList<U> a(U u0, int i) {
        this.a.add(new WeightedList.a<>(u0, i));
        return this;
    }

    public WeightedList<U> a() {
        return this.a(this.b);
    }

    public WeightedList<U> a(Random random) {
        this.a.forEach((weightedlist_a) -> {
            weightedlist_a.a(random.nextFloat());
        });
        this.a.sort(Comparator.comparingDouble((object) -> {
            return ((WeightedList.a) object).c();
        }));
        return this;
    }

    public boolean b() {
        return this.a.isEmpty();
    }

    public Stream<U> c() {
        return this.a.stream().map(WeightedList.a::a);
    }

    public U b(Random random) {
        return this.a(random).c().findFirst().orElseThrow(RuntimeException::new);
    }

    public String toString() {
        return "WeightedList[" + this.a + "]";
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
                    OptionalDynamic optionaldynamic = dynamic.get("data");
                    Codec codec1 = codec;

                    codec.getClass();
                    return optionaldynamic.flatMap(codec1::parse).map((object) -> {
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
