package org.bukkit.entity;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonControllerPhase;
import org.bukkit.craftbukkit.entity.CraftEnderDragon;
import org.junit.jupiter.api.Test;

public class EnderDragonPhaseTest {

    @Test
    public void testNotNull() {
        for (EnderDragon.Phase phase : EnderDragon.Phase.values()) {
            DragonControllerPhase dragonControllerPhase = CraftEnderDragon.getMinecraftPhase(phase);
            assertNotNull(dragonControllerPhase, phase.name());
            assertNotNull(CraftEnderDragon.getBukkitPhase(dragonControllerPhase), phase.name());
        }
    }

    @Test
    public void testBukkitToMinecraft() {
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.CIRCLING), DragonControllerPhase.HOLDING_PATTERN, "CIRCLING");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.STRAFING), DragonControllerPhase.STRAFE_PLAYER, "STRAFING");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.FLY_TO_PORTAL), DragonControllerPhase.LANDING_APPROACH, "FLY_TO_PORTAL");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.LAND_ON_PORTAL), DragonControllerPhase.LANDING, "LAND_ON_PORTAL");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.LEAVE_PORTAL), DragonControllerPhase.TAKEOFF, "LEAVE_PORTAL");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.BREATH_ATTACK), DragonControllerPhase.SITTING_FLAMING, "BREATH_ATTACK");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET), DragonControllerPhase.SITTING_SCANNING, "SEARCH_FOR_BREATH_ATTACK_TARGET");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.ROAR_BEFORE_ATTACK), DragonControllerPhase.SITTING_ATTACKING, "ROAR_BEFORE_ATTACK");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.CHARGE_PLAYER), DragonControllerPhase.CHARGING_PLAYER, "CHARGE_PLAYER");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.DYING), DragonControllerPhase.DYING, "DYING");
        assertEquals(CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.HOVER), DragonControllerPhase.HOVERING, "HOVER");
    }

    @Test
    public void testMinecraftToBukkit() {
        assertEquals(CraftEnderDragon.getBukkitPhase(DragonControllerPhase.HOLDING_PATTERN), EnderDragon.Phase.CIRCLING, "CIRCLING");
        assertEquals(CraftEnderDragon.getBukkitPhase(DragonControllerPhase.STRAFE_PLAYER), EnderDragon.Phase.STRAFING, "STRAFING");
        assertEquals(CraftEnderDragon.getBukkitPhase(DragonControllerPhase.LANDING_APPROACH), EnderDragon.Phase.FLY_TO_PORTAL, "FLY_TO_PORTAL");
        assertEquals(CraftEnderDragon.getBukkitPhase(DragonControllerPhase.LANDING), EnderDragon.Phase.LAND_ON_PORTAL, "LAND_ON_PORTAL");
        assertEquals(CraftEnderDragon.getBukkitPhase(DragonControllerPhase.TAKEOFF), EnderDragon.Phase.LEAVE_PORTAL, "LEAVE_PORTAL");
        assertEquals(CraftEnderDragon.getBukkitPhase(DragonControllerPhase.SITTING_FLAMING), EnderDragon.Phase.BREATH_ATTACK, "BREATH_ATTACK");
        assertEquals(CraftEnderDragon.getBukkitPhase(DragonControllerPhase.SITTING_SCANNING), EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET, "SEARCH_FOR_BREATH_ATTACK_TARGET");
        assertEquals(CraftEnderDragon.getBukkitPhase(DragonControllerPhase.SITTING_ATTACKING), EnderDragon.Phase.ROAR_BEFORE_ATTACK, "ROAR_BEFORE_ATTACK");
        assertEquals(CraftEnderDragon.getBukkitPhase(DragonControllerPhase.CHARGING_PLAYER), EnderDragon.Phase.CHARGE_PLAYER, "CHARGE_PLAYER");
        assertEquals(CraftEnderDragon.getBukkitPhase(DragonControllerPhase.DYING), EnderDragon.Phase.DYING, "DYING");
        assertEquals(CraftEnderDragon.getBukkitPhase(DragonControllerPhase.HOVERING), EnderDragon.Phase.HOVER, "HOVER");
    }
}
