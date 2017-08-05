package org.bukkit.craftbukkit.block;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityBeacon;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftInventoryBeacon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftBeacon extends CraftContainer<TileEntityBeacon> implements Beacon {

    public CraftBeacon(final Block block) {
        super(block, TileEntityBeacon.class);
    }

    public CraftBeacon(final Material material, final TileEntityBeacon te) {
        super(material, te);
    }

    @Override
    public BeaconInventory getSnapshotInventory() {
        return new CraftInventoryBeacon(this.getSnapshot());
    }

    @Override
    public BeaconInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryBeacon(this.getTileEntity());
    }

    @Override
    public Collection<LivingEntity> getEntitiesInRange() {
        TileEntity tileEntity = this.getTileEntityFromWorld();
        if (tileEntity instanceof TileEntityBeacon) {
            TileEntityBeacon beacon = (TileEntityBeacon) tileEntity;

            Collection<EntityHuman> nms = beacon.getHumansInRange();
            Collection<LivingEntity> bukkit = new ArrayList<LivingEntity>(nms.size());

            for (EntityHuman human : nms) {
                bukkit.add(human.getBukkitEntity());
            }

            return bukkit;
        }

        // block is no longer a beacon
        return new ArrayList<LivingEntity>();
    }

    @Override
    public int getTier() {
        return this.getSnapshot().levels;
    }

    @Override
    public PotionEffect getPrimaryEffect() {
        return this.getSnapshot().getPrimaryEffect();
    }

    @Override
    public void setPrimaryEffect(PotionEffectType effect) {
        this.getSnapshot().primaryEffect = (effect != null) ? MobEffectList.fromId(effect.getId()) : null;
    }

    @Override
    public PotionEffect getSecondaryEffect() {
        return this.getSnapshot().getSecondaryEffect();
    }

    @Override
    public void setSecondaryEffect(PotionEffectType effect) {
        this.getSnapshot().secondaryEffect = (effect != null) ? MobEffectList.fromId(effect.getId()) : null;
    }

    @Override
    public String getCustomName() {
        TileEntityBeacon beacon = this.getSnapshot();
        return beacon.hasCustomName() ? beacon.getName() : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(name);
    }
}
