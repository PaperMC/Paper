package org.bukkit.damage;

import static org.junit.jupiter.api.Assertions.*;
import org.bukkit.craftbukkit.damage.CraftDamageType;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class DamageTypeTest {

    @Test
    public void testDeathMessageType() {
        for (DeathMessageType deathMessageType : DeathMessageType.values()) {
            assertNotNull(CraftDamageType.deathMessageTypeToNMS(deathMessageType), deathMessageType.name());
        }

        for (net.minecraft.world.damagesource.DeathMessageType deathMessageType : net.minecraft.world.damagesource.DeathMessageType.values()) {
            assertNotNull(CraftDamageType.deathMessageTypeToBukkit(deathMessageType), deathMessageType.name());
        }
    }

    @Test
    public void testDamageScale() {
        for (DamageScaling damageScaling : DamageScaling.values()) {
            assertNotNull(CraftDamageType.damageScalingToNMS(damageScaling), damageScaling.name());
        }

        for (net.minecraft.world.damagesource.DamageScaling damageScaling : net.minecraft.world.damagesource.DamageScaling.values()) {
            assertNotNull(CraftDamageType.damageScalingToBukkit(damageScaling), damageScaling.name());
        }
    }
}
