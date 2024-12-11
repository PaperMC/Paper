package org.bukkit.entity;

import static org.junit.jupiter.api.Assertions.*;

import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import org.bukkit.craftbukkit.entity.CraftEnderDragon;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class EnderDragonPhaseTest {

    @Test
    public void testNotNull() {
        for (EnderDragon.Phase phase : EnderDragon.Phase.values()) {
            EnderDragonPhase dragonControllerPhase = CraftEnderDragon.getMinecraftPhase(phase);
            assertNotNull(dragonControllerPhase, phase.name());
            assertNotNull(CraftEnderDragon.getBukkitPhase(dragonControllerPhase), phase.name());
        }
    }

    @Test
    public void testBukkitToMinecraft() {
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.CIRCLING), EnderDragonPhase.HOLDING_PATTERN, "CIRCLING");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.STRAFING), EnderDragonPhase.STRAFE_PLAYER, "STRAFING");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.FLY_TO_PORTAL), EnderDragonPhase.LANDING_APPROACH, "FLY_TO_PORTAL");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.LAND_ON_PORTAL), EnderDragonPhase.LANDING, "LAND_ON_PORTAL");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.LEAVE_PORTAL), EnderDragonPhase.TAKEOFF, "LEAVE_PORTAL");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.BREATH_ATTACK), EnderDragonPhase.SITTING_FLAMING, "BREATH_ATTACK");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET), EnderDragonPhase.SITTING_SCANNING, "SEARCH_FOR_BREATH_ATTACK_TARGET");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.ROAR_BEFORE_ATTACK), EnderDragonPhase.SITTING_ATTACKING, "ROAR_BEFORE_ATTACK");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.CHARGE_PLAYER), EnderDragonPhase.CHARGING_PLAYER, "CHARGE_PLAYER");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.DYING), EnderDragonPhase.DYING, "DYING");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.HOVER), EnderDragonPhase.HOVERING, "HOVER");
    }

    @Test
    public void testMinecraftToBukkit() {
        assertEquals(CraftEnderDragon.getBukkitPhase(EnderDragonPhase.HOLDING_PATTERN), EnderDragon.Phase.CIRCLING, "CIRCLING");
        assertEquals(CraftEnderDragon.getBukkitPhase(EnderDragonPhase.STRAFE_PLAYER), EnderDragon.Phase.STRAFING, "STRAFING");
        assertEquals(CraftEnderDragon.getBukkitPhase(EnderDragonPhase.LANDING_APPROACH), EnderDragon.Phase.FLY_TO_PORTAL, "FLY_TO_PORTAL");
        assertEquals(CraftEnderDragon.getBukkitPhase(EnderDragonPhase.LANDING), EnderDragon.Phase.LAND_ON_PORTAL, "LAND_ON_PORTAL");
        assertEquals(CraftEnderDragon.getBukkitPhase(EnderDragonPhase.TAKEOFF), EnderDragon.Phase.LEAVE_PORTAL, "LEAVE_PORTAL");
        assertEquals(CraftEnderDragon.getBukkitPhase(EnderDragonPhase.SITTING_FLAMING), EnderDragon.Phase.BREATH_ATTACK, "BREATH_ATTACK");
        assertEquals(CraftEnderDragon.getBukkitPhase(EnderDragonPhase.SITTING_SCANNING), EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET, "SEARCH_FOR_BREATH_ATTACK_TARGET");
        assertEquals(CraftEnderDragon.getBukkitPhase(EnderDragonPhase.SITTING_ATTACKING), EnderDragon.Phase.ROAR_BEFORE_ATTACK, "ROAR_BEFORE_ATTACK");
        assertEquals(CraftEnderDragon.getBukkitPhase(EnderDragonPhase.CHARGING_PLAYER), EnderDragon.Phase.CHARGE_PLAYER, "CHARGE_PLAYER");
        assertEquals(CraftEnderDragon.getBukkitPhase(EnderDragonPhase.DYING), EnderDragon.Phase.DYING, "DYING");
        assertEquals(CraftEnderDragon.getBukkitPhase(EnderDragonPhase.HOVERING), EnderDragon.Phase.HOVER, "HOVER");
    }
}
