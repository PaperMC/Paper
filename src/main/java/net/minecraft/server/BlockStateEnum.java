package net.minecraft.server;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BlockStateEnum<T extends Enum<T> & INamable> extends IBlockState<T> {

    private final ImmutableSet<T> a;
    private final Map<String, T> b = Maps.newHashMap();

    protected BlockStateEnum(String s, Class<T> oclass, Collection<T> collection) {
        super(s, oclass);
        this.a = ImmutableSet.copyOf(collection);
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            T t0 = (Enum) iterator.next();
            String s1 = ((INamable) t0).getName();

            if (this.b.containsKey(s1)) {
                throw new IllegalArgumentException("Multiple values have the same name '" + s1 + "'");
            }

            this.b.put(s1, t0);
        }

    }

    @Override
    public Collection<T> getValues() {
        return this.a;
    }

    @Override
    public Optional<T> b(String s) {
        return Optional.ofNullable(this.b.get(s));
    }

    public String a(T t0) {
        return ((INamable) t0).getName();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof BlockStateEnum && super.equals(object)) {
            BlockStateEnum<?> blockstateenum = (BlockStateEnum) object;

            return this.a.equals(blockstateenum.a) && this.b.equals(blockstateenum.b);
        } else {
            return false;
        }
    }

    @Override
    public int b() {
        int i = super.b();

        i = 31 * i + this.a.hashCode();
        i = 31 * i + this.b.hashCode();
        return i;
    }

    public static <T extends Enum<T> & INamable> BlockStateEnum<T> of(String s, Class<T> oclass) {
        return a(s, oclass, (Predicate) Predicates.alwaysTrue());
    }

    public static <T extends Enum<T> & INamable> BlockStateEnum<T> a(String s, Class<T> oclass, Predicate<T> predicate) {
        return a(s, oclass, (Collection) Arrays.stream(oclass.getEnumConstants()).filter(predicate).collect(Collectors.toList()));
    }

    public static <T extends Enum<T> & INamable> BlockStateEnum<T> of(String s, Class<T> oclass, T... at) {
        return a(s, oclass, (Collection) Lists.newArrayList(at));
    }

    public static <T extends Enum<T> & INamable> BlockStateEnum<T> a(String s, Class<T> oclass, Collection<T> collection) {
        return new BlockStateEnum<>(s, oclass, collection);
    }
}
