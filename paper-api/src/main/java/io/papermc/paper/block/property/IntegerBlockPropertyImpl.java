package io.papermc.paper.block.property;

import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
record IntegerBlockPropertyImpl(String name, IntSet values, int min, int max) implements IntegerBlockProperty {

    IntegerBlockPropertyImpl(final String name, final int min, final int max) {
        this(name, createValues(min, max), min, max);
    }

    static IntSet createValues(final int min, final int max) {
        if (min < 0 || max <= min) {
            throw new IllegalArgumentException("Invalid range. Min: " + min + ", Max: " + max);
        }
        final IntSet set = new IntLinkedOpenHashSet();
        for (int i = min; i <= max; i++) {
            set.add(i);
        }
        return IntSets.unmodifiable(set); // use unmodifiable to preserve order (but in reality its immutable)
    }

    @Override
    public Class<Integer> type() {
        return Integer.class;
    }

    /**
     * Gets the min value for this property.
     *
     * @return the min value
     */
    @Override
    public int min() {
        return this.min;
    }

    /**
     * Gets the max value for this property.
     *
     * @return the max value
     */
    @Override
    public int max() {
        return this.max;
    }

    @Override
    public String name(final Integer value) {
        if (value > this.max || value < this.min) {
            throw ExceptionCreator.INSTANCE.create(value, ExceptionCreator.Type.VALUE, this);
        }
        return value.toString();
    }

    @Override
    public boolean isValidName(final String name) {
        try {
            final int value = Integer.parseInt(name);
            if (this.values.contains(value)) {
                return true;
            }
        } catch (final NumberFormatException ignored) {
        }
        return false;
    }

    @Override
    public Integer value(final String name) {
        try {
            final int value = Integer.parseInt(name);
            if (this.values.contains(value)) {
                return value;
            }
            throw ExceptionCreator.INSTANCE.create(name, ExceptionCreator.Type.NAME, this);
        } catch (final NumberFormatException exception) {
            throw ExceptionCreator.INSTANCE.create(name, ExceptionCreator.Type.NAME, this);
        }
    }

    @Override
    public boolean isValidValue(final Integer num) {
        return num >= this.min && num <= this.max;
    }
}
