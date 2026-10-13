package io.papermc.paper.damage;

import com.google.common.base.Preconditions;
import java.util.function.DoubleUnaryOperator;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * One stage in an entity damage calculation. A stage contributes a signed
 * change to its input: negative changes reduce damage and positive changes
 * increase it. Its output becomes the input of the next stage in the calculation.
 *
 * @see DamageCalculation
 */
public final class DamageStage {

    private final DamageCalculation calculation;
    private final Key key;
    private final DoubleUnaryOperator formula;
    private @Nullable DoubleUnaryOperator edits;
    private final double originalInput;
    private final double originalChange;
    double input;
    double change;

    DamageStage(final DamageCalculation calculation, final Key key, final DoubleUnaryOperator formula, final double originalInput, final double originalChange) {
        this.calculation = calculation;
        this.key = key;
        this.formula = formula;
        this.originalInput = originalInput;
        this.originalChange = originalChange;
        this.input = originalInput;
        this.change = originalChange;
    }

    /**
     * Gets this stage's identifier.
     *
     * @return the stage identifier
     */
    public Key key() {
        return this.key;
    }

    /**
     * Gets the damage entering this stage when the event was created. For a
     * plugin stage, this is its input when it was inserted.
     *
     * @return the original input
     */
    public double getOriginalInput() {
        return this.originalInput;
    }

    /**
     * Gets this stage's original signed change. For a plugin stage, this is
     * the change its adjustment produced when it was inserted.
     *
     * @return the original signed change
     */
    public double getOriginalChange() {
        return this.originalChange;
    }

    /**
     * Gets the current damage entering this stage, including changes to all
     * preceding stages.
     *
     * @return the current input
     */
    public double getInput() {
        this.calculation.resolve();
        return this.input;
    }

    /**
     * Gets the current signed change contributed by this stage.
     *
     * @return the current signed change
     */
    public double getChange() {
        this.calculation.resolve();
        return this.change;
    }

    /**
     * Gets the damage after this stage has been applied.
     *
     * @return the current input plus this stage's change
     */
    public double getOutput() {
        this.calculation.resolve();
        return this.input + this.change;
    }

    /**
     * Replaces this stage's change with a fixed signed amount. This also
     * replaces previous edits to this stage, including edits by earlier event
     * listeners (e.g. from other plugins). This recalculates all later stages.
     *
     * @param change the signed damage change
     */
    public void setChange(final double change) {
        this.edits = ignored -> change;
        this.calculation.invalidate();
    }

    /**
     * Multiplies this stage's signed change, including edits made by earlier
     * event listeners. For example, {@code 0.5} halves the magnitude of a
     * reduction.
     *
     * @param multiplier the nonnegative multiplier
     * @throws IllegalArgumentException if the multiplier is negative or not finite
     */
    public void multiplyChange(final double multiplier) {
        Preconditions.checkArgument(Double.isFinite(multiplier) && multiplier >= 0, "multiplier must be finite and non-negative");
        final DoubleUnaryOperator edit = change -> change * multiplier;
        this.edits = this.edits == null ? edit : this.edits.andThen(edit);
        this.calculation.invalidate();
    }

    double calculate(final double input) {
        final double change = this.formula.applyAsDouble(input);
        return this.edits == null ? change : this.edits.applyAsDouble(change);
    }

    /**
     * Calculates the stage's unmodified vanilla change for a supplied input.
     *
     * @param input the damage entering the stage
     * @return the signed vanilla change
     */
    @ApiStatus.Internal
    public double calculateVanilla(final double input) {
        return this.formula.applyAsDouble(input);
    }
}
