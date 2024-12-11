package io.papermc.paper.configuration.type;

import io.papermc.paper.configuration.type.number.IntOr;
import java.lang.reflect.Type;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

/*
(x/a)^2 + (y/b)^2 + (z/c)^2 < 1
a == c
ac = horizontal limit
b = vertical limit
x^2/ac^2 + y^2/b^2 + z^2/ac^2 < 1
(x^2 + z^2)/ac^2 + y^2/b^2 < 1
x^2 + z^2 + (y^2 * (ac^2/b^2)) < ac^2
 */
public final class DespawnRange {

    public static final TypeSerializer<DespawnRange> SERIALIZER = new Serializer();

    private final IntOr.Default horizontalLimit;
    private final IntOr.Default verticalLimit;
    private final boolean wasDefinedViaLongSyntax;

    // cached values
    private double preComputedHorizontalLimitSquared; // ac^2
    private double preComputedHorizontalLimitSquaredOverVerticalLimitSquared; // ac^2/b^2
    private int preComputedVanillaDefaultLimit;

    public DespawnRange(final IntOr.Default generalLimit) {
        this(generalLimit, generalLimit, false);
    }

    public DespawnRange(final IntOr.Default horizontalLimit, final IntOr.Default verticalLimit, final boolean wasDefinedViaLongSyntax) {
        this.horizontalLimit = horizontalLimit;
        this.verticalLimit = verticalLimit;
        this.wasDefinedViaLongSyntax = wasDefinedViaLongSyntax;
    }

    public void preComputed(int defaultDistanceLimit, String identifier) throws SerializationException {
        if (this.verticalLimit.or(defaultDistanceLimit) <= 0) {
            throw new SerializationException("Vertical limit must be greater than 0 for " + identifier);
        }
        if (this.horizontalLimit.or(defaultDistanceLimit) <= 0) {
            throw new SerializationException("Horizontal limit must be greater than 0 for " + identifier);
        }
        this.preComputedVanillaDefaultLimit = defaultDistanceLimit;
        this.preComputedHorizontalLimitSquared = Math.pow(this.horizontalLimit.or(defaultDistanceLimit), 2);
        if (!this.horizontalLimit.isDefined() && !this.verticalLimit.isDefined()) {
            this.preComputedHorizontalLimitSquaredOverVerticalLimitSquared = 1.0;
        } else {
            this.preComputedHorizontalLimitSquaredOverVerticalLimitSquared = this.preComputedHorizontalLimitSquared / Math.pow(this.verticalLimit.or(defaultDistanceLimit), 2);
        }
    }

    public boolean shouldDespawn(final Shape shape, final double dxSqr, final double dySqr, final double dzSqr, final double dy) {
        if (shape == Shape.ELLIPSOID) {
            return dxSqr + dzSqr + (dySqr * this.preComputedHorizontalLimitSquaredOverVerticalLimitSquared) > this.preComputedHorizontalLimitSquared;
        } else {
            return dxSqr + dzSqr > this.preComputedHorizontalLimitSquared || dy > this.verticalLimit.or(this.preComputedVanillaDefaultLimit);
        }
    }

    public boolean wasDefinedViaLongSyntax() {
        return this.wasDefinedViaLongSyntax;
    }

    public enum Shape {
        CYLINDER, ELLIPSOID
    }

    static final class Serializer implements TypeSerializer<DespawnRange> {

        public static final String HORIZONTAL = "horizontal";
        public static final String VERTICAL = "vertical";

        @Override
        public DespawnRange deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
            if (node.hasChild(HORIZONTAL) && node.hasChild(VERTICAL)) {
                return new DespawnRange(
                    node.node(HORIZONTAL).require(IntOr.Default.class),
                    node.node(VERTICAL).require(IntOr.Default.class),
                    true
                );
            } else if (node.hasChild(HORIZONTAL) || node.hasChild(VERTICAL)) {
                throw new SerializationException(node, DespawnRange.class, "Expected both horizontal and vertical despawn ranges to be defined");
            } else {
                return new DespawnRange(node.require(IntOr.Default.class));
            }
        }

        @Override
        public void serialize(final Type type, final @Nullable DespawnRange despawnRange, final ConfigurationNode node) throws SerializationException {
            if (despawnRange == null) {
                node.raw(null);
                return;
            }

            if (despawnRange.wasDefinedViaLongSyntax()) {
                node.node(HORIZONTAL).set(despawnRange.horizontalLimit);
                node.node(VERTICAL).set(despawnRange.verticalLimit);
            } else {
                node.set(despawnRange.verticalLimit);
            }
        }
    }
}
