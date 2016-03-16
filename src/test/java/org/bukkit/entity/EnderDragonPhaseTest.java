package org.bukkit.entity;

import net.minecraft.server.DragonControllerPhase;
import org.bukkit.craftbukkit.entity.CraftEnderDragon;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class EnderDragonPhaseTest {

    @Test
    public void testNotNull() {
        for (EnderDragon.Phase phase : EnderDragon.Phase.values()) {
            DragonControllerPhase dragonControllerPhase = CraftEnderDragon.getMinecraftPhase(phase);
            assertNotNull(phase.name(), dragonControllerPhase);
            assertNotNull(phase.name(), CraftEnderDragon.getBukkitPhase(dragonControllerPhase));
        }
    }
    
    @Test
    public void testBukkitToMinecraft() {
        Assert.assertEquals("CIRCLING", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.CIRCLING), DragonControllerPhase.a); // PAIL: Rename HOLDING_PATTERN
        Assert.assertEquals("STRAFING", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.STRAFING), DragonControllerPhase.b); // PAIL: Rename STRAFE_PLAYER
        Assert.assertEquals("FLY_TO_PORTAL", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.FLY_TO_PORTAL), DragonControllerPhase.c); // PAIL: Rename LANDING_APPROACH
        Assert.assertEquals("LAND_ON_PORTAL", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.LAND_ON_PORTAL), DragonControllerPhase.d); // PAIL: Rename LANDING
        Assert.assertEquals("LEAVE_PORTAL", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.LEAVE_PORTAL), DragonControllerPhase.e); // PAIL: Rename TAKEOFF
        Assert.assertEquals("BREATH_ATTACK", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.BREATH_ATTACK), DragonControllerPhase.f); // PAIL: Rename SITTING_FLAMING
        Assert.assertEquals("SEARCH_FOR_BREATH_ATTACK_TARGET", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET), DragonControllerPhase.g); // PAIL: Rename SITTING_SCANNING
        Assert.assertEquals("ROAR_BEFORE_ATTACK", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.ROAR_BEFORE_ATTACK), DragonControllerPhase.h); // PAIL: Rename SITTING_ATTACKING
        Assert.assertEquals("CHARGE_PLAYER", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.CHARGE_PLAYER), DragonControllerPhase.i); // PAIL: Rename CHARGING_PLAYER
        Assert.assertEquals("DYING", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.DYING), DragonControllerPhase.j); // PAIL: Rename DYING
        Assert.assertEquals("HOVER", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.HOVER), DragonControllerPhase.k); // PAIL: Rename HOVER
    }

    @Test
    public void testMinecraftToBukkit() {
        Assert.assertEquals("CIRCLING", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.a), EnderDragon.Phase.CIRCLING);
        Assert.assertEquals("STRAFING", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.b), EnderDragon.Phase.STRAFING);
        Assert.assertEquals("FLY_TO_PORTAL", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.c), EnderDragon.Phase.FLY_TO_PORTAL);
        Assert.assertEquals("LAND_ON_PORTAL", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.d), EnderDragon.Phase.LAND_ON_PORTAL);
        Assert.assertEquals("LEAVE_PORTAL", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.e), EnderDragon.Phase.LEAVE_PORTAL);
        Assert.assertEquals("BREATH_ATTACK", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.f), EnderDragon.Phase.BREATH_ATTACK);
        Assert.assertEquals("SEARCH_FOR_BREATH_ATTACK_TARGET", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.g), EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET);
        Assert.assertEquals("ROAR_BEFORE_ATTACK", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.h), EnderDragon.Phase.ROAR_BEFORE_ATTACK);
        Assert.assertEquals("CHARGE_PLAYER", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.i), EnderDragon.Phase.CHARGE_PLAYER);
        Assert.assertEquals("DYING", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.j), EnderDragon.Phase.DYING);
        Assert.assertEquals("HOVER", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.k), EnderDragon.Phase.HOVER);
    } 
}
