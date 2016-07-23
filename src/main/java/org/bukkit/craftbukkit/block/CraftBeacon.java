package org.bukkit.craftbukkit.block;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.TileEntityBeacon;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventoryBeacon;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftBeacon extends CraftBlockState implements Beacon {
    private final CraftWorld world;
    private final TileEntityBeacon beacon;

    public CraftBeacon(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        beacon = (TileEntityBeacon) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftBeacon(final Material material, final TileEntityBeacon te) {
        super(material);
        world = null;
        beacon = te;
    }

    public Inventory getInventory() {
        return new CraftInventoryBeacon(beacon);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            beacon.update();
        }

        return result;
    }

    @Override
    public TileEntityBeacon getTileEntity() {
        return beacon;
    }

    @Override
    public Collection<LivingEntity> getEntitiesInRange() {
        Collection<EntityHuman> nms = beacon.getHumansInRange();
        Collection<LivingEntity> bukkit = new ArrayList<LivingEntity>(nms.size());

        for (EntityHuman human : nms) {
            bukkit.add(human.getBukkitEntity());
        }

        return bukkit;
    }

    @Override
    public int getTier() {
        return beacon.k;
    }

    @Override
    public PotionEffect getPrimaryEffect() {
        return beacon.getPrimaryEffect();
    }

    @Override
    public void setPrimaryEffect(PotionEffectType effect) {
        beacon.l = (effect != null) ? MobEffectList.fromId(effect.getId()) : null;
    }

    @Override
    public PotionEffect getSecondaryEffect() {
        return beacon.getSecondaryEffect();
    }

    @Override
    public void setSecondaryEffect(PotionEffectType effect) {
        beacon.m = (effect != null) ? MobEffectList.fromId(effect.getId()) : null;
    }
}
