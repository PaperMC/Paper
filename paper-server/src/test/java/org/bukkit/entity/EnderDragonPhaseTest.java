package org.bukkit.entity;

import net.minecraft.world.entity.boss.enderdragon.phases.DragonControllerPhase;
import org.bukkit.craftbukkit.entity.CraftEnderDragon;
import org.junit.Assert;
import org.junit.Test;

public class EnderDragonPhaseTest {

    @Test
    public void testNotNull() {
        for (EnderDragon.Phase phase : EnderDragon.Phase.values()) {
            DragonControllerPhase dragonControllerPhase = CraftEnderDragon.getMinecraftPhase(phase);
            Assert.assertNotNull(phase.name(), dragonControllerPhase);
            Assert.assertNotNull(phase.name(), CraftEnderDragon.getBukkitPhase(dragonControllerPhase));
        }
    }

    @Test
    public void testBukkitToMinecraft() {
        Assert.assertEquals("CIRCLING", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.CIRCLING), DragonControllerPhase.HOLDING_PATTERN);
        Assert.assertEquals("STRAFING", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.STRAFING), DragonControllerPhase.STRAFE_PLAYER);
        Assert.assertEquals("FLY_TO_PORTAL", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.FLY_TO_PORTAL), DragonControllerPhase.LANDING_APPROACH);
        Assert.assertEquals("LAND_ON_PORTAL", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.LAND_ON_PORTAL), DragonControllerPhase.LANDING);
        Assert.assertEquals("LEAVE_PORTAL", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.LEAVE_PORTAL), DragonControllerPhase.TAKEOFF);
        Assert.assertEquals("BREATH_ATTACK", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.BREATH_ATTACK), DragonControllerPhase.SITTING_FLAMING);
        Assert.assertEquals("SEARCH_FOR_BREATH_ATTACK_TARGET", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET), DragonControllerPhase.SITTING_SCANNING);
        Assert.assertEquals("ROAR_BEFORE_ATTACK", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.ROAR_BEFORE_ATTACK), DragonControllerPhase.SITTING_ATTACKING);
        Assert.assertEquals("CHARGE_PLAYER", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.CHARGE_PLAYER), DragonControllerPhase.CHARGING_PLAYER);
        Assert.assertEquals("DYING", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.DYING), DragonControllerPhase.DYING);
        Assert.assertEquals("HOVER", CraftEnderDragon.getMinecraftPhase(EnderDragon.Phase.HOVER), DragonControllerPhase.HOVER);
    }

    @Test
    public void testMinecraftToBukkit() {
        Assert.assertEquals("CIRCLING", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.HOLDING_PATTERN), EnderDragon.Phase.CIRCLING);
        Assert.assertEquals("STRAFING", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.STRAFE_PLAYER), EnderDragon.Phase.STRAFING);
        Assert.assertEquals("FLY_TO_PORTAL", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.LANDING_APPROACH), EnderDragon.Phase.FLY_TO_PORTAL);
        Assert.assertEquals("LAND_ON_PORTAL", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.LANDING), EnderDragon.Phase.LAND_ON_PORTAL);
        Assert.assertEquals("LEAVE_PORTAL", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.TAKEOFF), EnderDragon.Phase.LEAVE_PORTAL);
        Assert.assertEquals("BREATH_ATTACK", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.SITTING_FLAMING), EnderDragon.Phase.BREATH_ATTACK);
        Assert.assertEquals("SEARCH_FOR_BREATH_ATTACK_TARGET", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.SITTING_SCANNING), EnderDragon.Phase.SEARCH_FOR_BREATH_ATTACK_TARGET);
        Assert.assertEquals("ROAR_BEFORE_ATTACK", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.SITTING_ATTACKING), EnderDragon.Phase.ROAR_BEFORE_ATTACK);
        Assert.assertEquals("CHARGE_PLAYER", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.CHARGING_PLAYER), EnderDragon.Phase.CHARGE_PLAYER);
        Assert.assertEquals("DYING", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.DYING), EnderDragon.Phase.DYING);
        Assert.assertEquals("HOVER", CraftEnderDragon.getBukkitPhase(DragonControllerPhase.HOVER), EnderDragon.Phase.HOVER);
    }
}
