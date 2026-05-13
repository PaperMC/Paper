package io.papermc.paper.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.RecordBuilder.AbstractUniversalBuilder;
import io.netty.handler.codec.DecoderException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import net.minecraft.nbt.Tag;
import net.minecraft.util.AbstractListBuilder;

public final class CountingOps implements DynamicOps<CountingOps.Value> {
    public static final CountingOps INSTANCE = new CountingOps(Tag.MAX_DEPTH);

    private final int maxDepth;

    public CountingOps(final int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public <U> U convertTo(final DynamicOps<U> outOps, final Value input) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value empty() {
        return Value.EMPTY;
    }

    @Override
    public Value emptyMap() {
        return this.container(Value.Kind.MAP, 0);
    }

    @Override
    public Value emptyList() {
        return this.container(Value.Kind.LIST, 0);
    }

    @Override
    public Value createNumeric(final Number value) {
        return Value.PRIMITIVE;
    }

    @Override
    public Value createString(final String value) {
        return Value.PRIMITIVE;
    }

    @Override
    public DataResult<Number> getNumberValue(final Value input) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataResult<String> getStringValue(final Value input) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataResult<Value> mergeToList(final Value input, final Value value) {
        final Accumulator accumulator = new Accumulator();
        if (input.kind == Value.Kind.LIST) {
            accumulator.acceptDepth(input.depth - 1);
        } else if (input.kind != Value.Kind.EMPTY) {
            accumulator.accept(input);
        }
        accumulator.accept(value);
        return DataResult.success(this.container(Value.Kind.LIST, accumulator));
    }

    @Override
    public DataResult<Value> mergeToList(final Value input, final List<Value> values) {
        final Accumulator accumulator = new Accumulator();
        if (input.kind == Value.Kind.LIST) {
            accumulator.acceptDepth(input.depth - 1);
        } else if (input.kind != Value.Kind.EMPTY) {
            accumulator.accept(input);
        }
        values.forEach(accumulator::accept);
        return DataResult.success(this.container(Value.Kind.LIST, accumulator));
    }

    @Override
    public DataResult<Value> mergeToMap(final Value input, final Value key, final Value value) {
        final Accumulator accumulator = new Accumulator();
        if (input.kind == Value.Kind.MAP) {
            accumulator.acceptDepth(input.depth - 1);
        } else if (input.kind != Value.Kind.EMPTY) {
            accumulator.accept(input);
        }
        accumulator.accept(value);
        return DataResult.success(this.container(Value.Kind.MAP, accumulator));
    }

    @Override
    public DataResult<Value> mergeToMap(final Value input, final Map<Value, Value> values) {
        final Accumulator accumulator = new Accumulator();
        if (input.kind == Value.Kind.MAP) {
            accumulator.acceptDepth(input.depth - 1);
        } else if (input.kind != Value.Kind.EMPTY) {
            accumulator.accept(input);
        }
        values.forEach((key, value) -> {
            accumulator.accept(value);
        });
        return DataResult.success(this.container(Value.Kind.MAP, accumulator));
    }

    @Override
    public DataResult<Value> mergeToMap(final Value input, final MapLike<Value> values) {
        final Accumulator accumulator = new Accumulator();
        if (input.kind == Value.Kind.MAP) {
            accumulator.acceptDepth(input.depth - 1);
        } else if (input.kind != Value.Kind.EMPTY) {
            accumulator.accept(input);
        }
        values.entries().forEach(entry -> {
            accumulator.accept(entry.getSecond());
        });
        return DataResult.success(this.container(Value.Kind.MAP, accumulator));
    }

    @Override
    public DataResult<Stream<Pair<Value, Value>>> getMapValues(final Value input) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataResult<Stream<Value>> getStream(final Value input) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value createMap(final Stream<Pair<Value, Value>> map) {
        final Accumulator accumulator = new Accumulator();
        map.forEach(entry -> accumulator.accept(entry.getSecond()));
        return this.container(Value.Kind.MAP, accumulator);
    }

    @Override
    public Value createMap(final Map<Value, Value> map) {
        final Accumulator accumulator = new Accumulator();
        map.forEach((key, value) -> {
            accumulator.accept(value);
        });
        return this.container(Value.Kind.MAP, accumulator);
    }

    @Override
    public Value createList(final Stream<Value> input) {
        final Accumulator accumulator = new Accumulator();
        input.forEach(accumulator::accept);
        return this.container(Value.Kind.LIST, accumulator);
    }

    @Override
    public Value createByteList(final ByteBuffer input) {
        return Value.PRIMITIVE;
    }

    @Override
    public Value createIntList(final IntStream input) {
        return Value.PRIMITIVE;
    }

    @Override
    public Value createLongList(final LongStream input) {
        return Value.PRIMITIVE;
    }

    @Override
    public Value remove(final Value input, final String key) {
        return input;
    }

    @Override
    public RecordBuilder<Value> mapBuilder() {
        return new MapBuilder(this);
    }

    @Override
    public ListBuilder<Value> listBuilder() {
        return new CountingListBuilder(this);
    }

    @Override
    public String toString() {
        return "Counting";
    }

    private Value container(final Value.Kind kind, final Accumulator accumulator) {
        return this.container(kind, accumulator.maxDepth);
    }

    private Value container(final Value.Kind kind, final int childDepth) {
        final int depth = childDepth + 1;
        if (depth > this.maxDepth) {
            throw new DecoderException("NBT depth exceeded: " + depth + " > " + this.maxDepth);
        }
        return new Value(kind, depth);
    }

    private static final class Accumulator {
        private int maxDepth;

        private void accept(final Value value) {
            this.acceptDepth(value.depth);
        }

        private void acceptDepth(final int depth) {
            this.maxDepth = Math.max(this.maxDepth, depth);
        }
    }

    public static final class Value {
        private static final Value EMPTY = new Value(Kind.EMPTY, 0);
        private static final Value PRIMITIVE = new Value(Kind.PRIMITIVE, 0);

        private final Kind kind;
        private final int depth;

        private Value(final Kind kind, final int depth) {
            this.kind = kind;
            this.depth = depth;
        }

        public int depth() {
            return this.depth;
        }

        private enum Kind {
            EMPTY,
            PRIMITIVE,
            LIST,
            MAP
        }
    }

    private static final class CountingListBuilder extends AbstractListBuilder<Value, Accumulator> {
        private final CountingOps ops;

        private CountingListBuilder(final CountingOps ops) {
            super(ops);
            this.ops = ops;
        }

        @Override
        protected Accumulator initBuilder() {
            return new Accumulator();
        }

        @Override
        protected Accumulator append(final Accumulator builder, final Value value) {
            builder.accept(value);
            return builder;
        }

        @Override
        protected DataResult<Value> build(final Accumulator builder, final Value prefix) {
            if (prefix.kind == Value.Kind.LIST) {
                builder.acceptDepth(prefix.depth - 1);
            } else if (prefix.kind != Value.Kind.EMPTY) {
                builder.accept(prefix);
            }
            return DataResult.success(this.ops.container(Value.Kind.LIST, builder));
        }
    }

    private static final class MapBuilder extends AbstractUniversalBuilder<Value, Accumulator> {
        private final CountingOps ops;

        private MapBuilder(final CountingOps ops) {
            super(ops);
            this.ops = ops;
        }

        @Override
        protected Accumulator initBuilder() {
            return new Accumulator();
        }

        @Override
        protected Accumulator append(final Value key, final Value value, final Accumulator builder) {
            builder.accept(value);
            return builder;
        }

        @Override
        protected DataResult<Value> build(final Accumulator builder, final Value prefix) {
            if (prefix.kind == Value.Kind.MAP) {
                builder.acceptDepth(prefix.depth - 1);
            } else if (prefix.kind != Value.Kind.EMPTY) {
                builder.accept(prefix);
            }
            return DataResult.success(this.ops.container(Value.Kind.MAP, builder));
        }
    }
}
