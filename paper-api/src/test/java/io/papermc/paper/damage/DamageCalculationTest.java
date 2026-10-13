package io.papermc.paper.damage;

import java.util.List;
import net.kyori.adventure.key.Key;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class DamageCalculationTest {

    @Test
    void insertedAdjustmentRecalculatesLaterStages() {
        final DamageCalculation calculation = calculation();
        final Key customKey = Key.key("test", "water_resistance");

        final DamageStage custom = calculation.before(DamageStageKeys.ARMOR).add(customKey, DamageAdjustment.flat(-4));
        final DamageStage armor = calculation.stage(DamageStageKeys.ARMOR).orElseThrow();
        assertEquals(20, armor.getOriginalInput());
        assertEquals(16, armor.getInput());
        assertEquals(-4, armor.getChange());
        assertEquals(9.6, calculation.getFinalDamage(), 0.0001);

        armor.multiplyChange(0.5);
        assertEquals(11.2, calculation.getFinalDamage(), 0.0001);
        assertEquals(20, custom.getInput());
    }

    @Test
    void multipleInsertionsKeepTheirOrderAfterBaseChanges() {
        final DamageCalculation calculation = calculation();
        final Key flatKey = Key.key("test", "flat");
        final Key multiplierKey = Key.key("test", "multiplier");

        calculation.before(DamageStageKeys.ARMOR).add(flatKey, DamageAdjustment.flat(-4));
        calculation.before(DamageStageKeys.ARMOR).add(multiplierKey, DamageAdjustment.multiply(0.5));

        assertEquals(List.of(flatKey, multiplierKey, DamageStageKeys.ARMOR, DamageStageKeys.RESISTANCE),
            calculation.stages().stream().map(DamageStage::key).toList());
        assertEquals(16, calculation.stage(multiplierKey).orElseThrow().getInput());
        assertEquals(8, calculation.stage(DamageStageKeys.ARMOR).orElseThrow().getInput());
        assertEquals(4.8, calculation.getFinalDamage(), 0.0001);

        calculation.setBaseDamage(12);
        assertEquals(8, calculation.stage(multiplierKey).orElseThrow().getInput());
        assertEquals(4, calculation.stage(DamageStageKeys.ARMOR).orElseThrow().getInput());
        assertEquals(2.4, calculation.getFinalDamage(), 0.0001);
    }

    @Test
    void changingBaseRecalculatesVanillaStages() {
        final DamageCalculation calculation = calculation();
        calculation.setBaseDamage(12);

        assertEquals(-3, calculation.stage(DamageStageKeys.ARMOR).orElseThrow().getChange());
        assertEquals(-1.8, calculation.stage(DamageStageKeys.RESISTANCE).orElseThrow().getChange(), 0.0001);
        assertEquals(7.2, calculation.getFinalDamage(), 0.0001);
    }

    @Test
    @SuppressWarnings("deprecation")
    void legacyModifiersReflectModernStages() {
        final DamageCalculation calculation = calculation();
        final EntityDamageEvent event = new EntityDamageEvent(mock(Player.class), DamageCause.ENTITY_ATTACK,
            mock(DamageSource.class), calculation);

        assertEquals(-5, event.getOriginalDamage(DamageModifier.ARMOR));
        assertEquals(-3, event.getDamage(DamageModifier.RESISTANCE));
        event.getDamageCalculation().before(DamageStageKeys.ARMOR)
            .add(Key.key("test", "pre_armor"), DamageAdjustment.flat(-4));
        assertEquals(-4, event.getDamage(DamageModifier.ARMOR));
        assertEquals(9.6, event.getFinalDamage(), 0.0001);

        event.setDamage(DamageModifier.ARMOR, -2);
        assertEquals(-2, event.getDamage(DamageModifier.ARMOR));
        assertEquals(11.6, event.getFinalDamage(), 0.0001);
    }

    @Test
    @SuppressWarnings("deprecation")
    void legacyBaseSetterRescalesVanillaStages() {
        final EntityDamageEvent event = new EntityDamageEvent(mock(Player.class), DamageCause.ENTITY_ATTACK,
            mock(DamageSource.class), calculation());

        event.setDamage(12);

        assertEquals(12, event.getDamage(DamageModifier.BASE));
        assertEquals(-3, event.getDamage(DamageModifier.ARMOR));
        assertEquals(-1.8, event.getDamage(DamageModifier.RESISTANCE), 0.0001);
        assertEquals(7.2, event.getFinalDamage(), 0.0001);
    }

    private static DamageCalculation calculation() {
        final DamageCalculation calculation = new DamageCalculation(20);
        calculation.addStage(DamageStageKeys.ARMOR, -5, input -> -input / 4);
        calculation.addStage(DamageStageKeys.RESISTANCE, -3, input -> -input / 5);
        return calculation;
    }
}
