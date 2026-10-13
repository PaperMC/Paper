package io.papermc.paper.damage;

import com.google.common.base.Preconditions;
import java.util.function.DoubleUnaryOperator;

/**
 * A fixed or proportional adjustment inserted into a damage calculation.
 * Damage can not be reduced below zero at the insertion point.
 *
 * @see DamageCalculation#before(net.kyori.adventure.key.Key)
 */
public final class DamageAdjustment {

    private final DoubleUnaryOperator change;

    private DamageAdjustment(final DoubleUnaryOperator change) {
        this.change = change;
    }

    /**
     * Creates a fixed signed damage adjustment.
     *
     * @param change the signed damage change
     * @return a flat adjustment
     * @throws IllegalArgumentException if the change is not finite
     */
    public static DamageAdjustment flat(final double change) {
        Preconditions.checkArgument(Double.isFinite(change), "change must be finite");
        return new DamageAdjustment(input -> Math.max(0, input + change) - input);
    }

    /**
     * Creates an adjustment that multiplies the damage reaching its insertion
     * point. For example, {@code 0.5} halves the damage.
     *
     * @param multiplier the nonnegative multiplier
     * @return a proportional adjustment
     * @throws IllegalArgumentException if the multiplier is negative or not finite
     */
    public static DamageAdjustment multiply(final double multiplier) {
        Preconditions.checkArgument(Double.isFinite(multiplier) && multiplier >= 0, "multiplier must be finite and non-negative");
        return new DamageAdjustment(input -> input * multiplier - input);
    }

    DoubleUnaryOperator formula() {
        return this.change;
    }
}
