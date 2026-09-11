package io.papermc.paper.block.property;

import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;

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
        if (!this.isValidValue(value)) {
            throw ExceptionCreator.NOT_VALID.create(value, ExceptionCreator.Type.VALUE, this);
        }
        return value.toString();
    }

    @Override
    public boolean isValidName(final String name) {
        try {
            this.value(name);
        } catch (IllegalArgumentException _) {
        }
        return false;
    }

    @Override
    public Integer value(final String name) {
        try {
            final int value = Integer.parseInt(name);
            if (this.isValidValue(value)) {
                return value;
            }
            throw ExceptionCreator.NOT_VALID.create(name, ExceptionCreator.Type.NAME, this);
        } catch (NumberFormatException _) {
            throw ExceptionCreator.NOT_VALID.create(name, ExceptionCreator.Type.NAME, this);
        }
    }

    @Override
    public boolean isValidValue(final Integer value) {
        return value >= this.min && value <= this.max;
    }
}
