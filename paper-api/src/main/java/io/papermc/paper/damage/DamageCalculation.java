package io.papermc.paper.damage;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleUnaryOperator;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;

/**
 * The ordered, mutable damage calculation for this entity damage event.
 * <p>
 * Changes to a stage cause later stages to recalculate when their values are
 * next read. Some vanilla stages are present only for certain damage types.
 * Use {@link #before(Key)} to add a plugin adjustment at a specific point.
 *
 * @see org.bukkit.event.entity.EntityDamageEvent#getDamageCalculation()
 */
public final class DamageCalculation {

    private final List<DamageStage> stages = new ArrayList<>();
    private final double originalBaseDamage;
    private double originalAmount;
    private double baseDamage;
    private double finalDamage;
    private boolean dirty;

    /**
     * Creates a calculation with the raw damage supplied by the server.
     *
     * @param baseDamage the original raw damage
     */
    @ApiStatus.Internal
    public DamageCalculation(final double baseDamage) {
        this.originalBaseDamage = baseDamage;
        this.originalAmount = baseDamage;
        this.baseDamage = baseDamage;
        this.finalDamage = baseDamage;
    }

    /**
     * Adds a vanilla stage while constructing an event's calculation.
     *
     * @param key the stage identifier
     * @param originalChange the signed change calculated by vanilla
     * @param formula the vanilla calculation for a new input
     * @return the added stage
     * @throws IllegalArgumentException if the key is null, already used, or the formula is null
     */
    @ApiStatus.Internal
    public DamageStage addStage(final Key key, final double originalChange, final DoubleUnaryOperator formula) {
        Preconditions.checkArgument(key != null, "key cannot be null");
        Preconditions.checkArgument(formula != null, "formula cannot be null");
        Preconditions.checkArgument(this.stage(key).isEmpty(), "Damage stage already exists: %s", key);
        final DamageStage stage = new DamageStage(this, key, formula, this.originalAmount, originalChange);
        this.stages.add(stage);
        this.originalAmount += originalChange;
        if (this.dirty) {
            this.resolve();
        } else {
            this.finalDamage = this.originalAmount;
        }
        return stage;
    }

    /**
     * Adds a vanilla stage and calculates its original change from the current
     * original input. This method is for constructing an event's calculation.
     *
     * @param key the stage identifier
     * @param formula the vanilla calculation for a stage input
     * @return the added stage
     * @throws IllegalArgumentException if the key is null, already used, or the formula is null
     */
    @ApiStatus.Internal
    public DamageStage addStage(final Key key, final DoubleUnaryOperator formula) {
        Preconditions.checkArgument(formula != null, "formula cannot be null");
        return this.addStage(key, formula.applyAsDouble(this.originalAmount), formula);
    }

    /**
     * Gets the raw damage at the time the event was created.
     *
     * @return the original raw damage
     */
    public double getOriginalBaseDamage() {
        return this.originalBaseDamage;
    }

    /**
     * Gets the raw damage entering this calculation.
     *
     * @return the current raw damage
     */
    public double getBaseDamage() {
        return this.baseDamage;
    }

    /**
     * Sets the raw damage and recalculates all later stages.
     *
     * @param damage the new raw damage
     */
    public void setBaseDamage(final double damage) {
        this.baseDamage = damage;
        this.invalidate();
    }

    /**
     * Gets the damage remaining after all stages have been applied.
     *
     * @return the final damage
     */
    public double getFinalDamage() {
        this.resolve();
        return this.finalDamage;
    }

    /**
     * Gets a snapshot of the stages in calculation order.
     *
     * @return the stages currently in this calculation
     */
    public List<DamageStage> stages() {
        return List.copyOf(this.stages);
    }

    /**
     * Finds an applicable stage by its identifier.
     *
     * @param key the stage identifier
     * @return the stage, or an empty optional if it does not apply to this hit
     * @throws IllegalArgumentException if the key is null
     */
    public Optional<DamageStage> stage(final Key key) {
        Preconditions.checkArgument(key != null, "key cannot be null");
        for (final DamageStage stage : this.stages) {
            if (stage.key().equals(key)) {
                return Optional.of(stage);
            }
        }
        return Optional.empty();
    }

    /**
     * Selects the point immediately before an applicable stage. The selected
     * point can then be used to insert one or more plugin adjustments.
     *
     * @param key the identifier of the stage to insert before
     * @return an insertion point before that stage
     * @throws IllegalArgumentException if the key is null or the stage does not apply to this hit
     */
    public InsertionPoint before(final Key key) {
        final DamageStage anchor = this.stage(key).orElseThrow(() -> new IllegalArgumentException("Damage stage is not applicable: " + key));
        return new InsertionPoint(anchor);
    }

    void invalidate() {
        this.dirty = true;
    }

    void resolve() {
        if (!this.dirty) {
            return;
        }
        double amount = this.baseDamage;
        for (final DamageStage stage : this.stages) {
            stage.input = amount;
            stage.change = stage.calculate(amount);
            amount += stage.change;
        }
        this.finalDamage = amount;
        this.dirty = false;
    }

    /**
     * A position before an applicable stage in this calculation.
     */
    public final class InsertionPoint {

        private final DamageStage anchor;

        private InsertionPoint(final DamageStage anchor) {
            this.anchor = anchor;
        }

        /**
         * Inserts an adjustment before the selected stage. Adjustments added
         * through this insertion point run in insertion order. The identifier
         * must be unique within this calculation and may later be used with
         * {@link DamageCalculation#stage(Key)} or {@link DamageCalculation#before(Key)}.
         *
         * @param key the unique identifier for this adjustment
         * @param adjustment the fixed or proportional damage adjustment
         * @return the inserted, mutable stage
         * @throws IllegalArgumentException if the key or adjustment is null, or
         *     the identifier is already in use
         */
        public DamageStage add(final Key key, final DamageAdjustment adjustment) {
            Preconditions.checkArgument(key != null, "key cannot be null");
            Preconditions.checkArgument(adjustment != null, "adjustment cannot be null");
            Preconditions.checkArgument(DamageCalculation.this.stage(key).isEmpty(), "Damage stage already exists: %s", key);
            DamageCalculation.this.resolve();
            final double input = this.anchor.getInput();
            final DoubleUnaryOperator formula = adjustment.formula();
            final DamageStage stage = new DamageStage(DamageCalculation.this, key, formula, input, formula.applyAsDouble(input));
            DamageCalculation.this.stages.add(DamageCalculation.this.stages.indexOf(this.anchor), stage);
            DamageCalculation.this.invalidate();
            return stage;
        }
    }
}
