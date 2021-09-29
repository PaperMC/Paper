package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.world.ChestLock;
import net.minecraft.world.effect.MobEffectList;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityBeacon;
import org.bukkit.World;
import org.bukkit.block.Beacon;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftBeacon extends CraftBlockEntityState<TileEntityBeacon> implements Beacon {

    public CraftBeacon(World world, TileEntityBeacon tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public Collection<LivingEntity> getEntitiesInRange() {
        Preconditions.checkState(getWorldHandle() instanceof net.minecraft.world.level.World, "Can't get entities during world generation");

        TileEntity tileEntity = this.getTileEntityFromWorld();
        if (tileEntity instanceof TileEntityBeacon) {
            TileEntityBeacon beacon = (TileEntityBeacon) tileEntity;

            Collection<EntityHuman> nms = TileEntityBeacon.getHumansInRange(beacon.getWorld(), beacon.getPosition(), beacon.levels);
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
        this.getSnapshot().primaryPower = (effect != null) ? MobEffectList.fromId(effect.getId()) : null;
    }

    @Override
    public PotionEffect getSecondaryEffect() {
        return this.getSnapshot().getSecondaryEffect();
    }

    @Override
    public void setSecondaryEffect(PotionEffectType effect) {
        this.getSnapshot().secondaryPower = (effect != null) ? MobEffectList.fromId(effect.getId()) : null;
    }

    @Override
    public String getCustomName() {
        TileEntityBeacon beacon = this.getSnapshot();
        return beacon.name != null ? CraftChatMessage.fromComponent(beacon.name) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public boolean isLocked() {
        return !this.getSnapshot().lockKey.key.isEmpty();
    }

    @Override
    public String getLock() {
        return this.getSnapshot().lockKey.key;
    }

    @Override
    public void setLock(String key) {
        this.getSnapshot().lockKey = (key == null) ? ChestLock.NO_LOCK : new ChestLock(key);
    }
}
