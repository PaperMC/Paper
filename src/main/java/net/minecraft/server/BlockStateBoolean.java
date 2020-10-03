package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.Optional;

public class BlockStateBoolean extends IBlockState<Boolean> {

    private final ImmutableSet<Boolean> a = ImmutableSet.of(true, false);

    protected BlockStateBoolean(String s) {
        super(s, Boolean.class);
    }

    @Override
    public Collection<Boolean> getValues() {
        return this.a;
    }

    public static BlockStateBoolean of(String s) {
        return new BlockStateBoolean(s);
    }

    @Override
    public Optional<Boolean> b(String s) {
        return !"true".equals(s) && !"false".equals(s) ? Optional.empty() : Optional.of(Boolean.valueOf(s));
    }

    public String a(Boolean obool) {
        return obool.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof BlockStateBoolean && super.equals(object)) {
            BlockStateBoolean blockstateboolean = (BlockStateBoolean) object;

            return this.a.equals(blockstateboolean.a);
        } else {
            return false;
        }
    }

    @Override
    public int b() {
        return 31 * super.b() + this.a.hashCode();
    }
}
