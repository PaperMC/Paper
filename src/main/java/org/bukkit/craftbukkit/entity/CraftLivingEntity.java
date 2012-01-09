package org.bukkit.craftbukkit.entity;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityEgg;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntitySnowball;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;

import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;

import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Vehicle;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.server.DamageSource;
import org.bukkit.entity.HumanEntity;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {
    public CraftLivingEntity(final CraftServer server, final EntityLiving entity) {
        super(server, entity);
    }

    public int getHealth() {
        return getHandle().getHealth();
    }

    public void setHealth(int health) {
        if ((health < 0) || (health > getMaxHealth())) {
            throw new IllegalArgumentException("Health must be between 0 and " + getMaxHealth());
        }

        if (entity instanceof EntityPlayer && health == 0) {
            ((EntityPlayer) entity).die(DamageSource.GENERIC);
        }

        getHandle().setHealth(health);
    }

    public int getMaxHealth() {
        return getHandle().getMaxHealth();
    }

    public Egg throwEgg() {
        net.minecraft.server.World world = ((CraftWorld) getWorld()).getHandle();
        EntityEgg egg = new EntityEgg(world, getHandle());

        world.addEntity(egg);
        return (Egg) egg.getBukkitEntity();
    }

    public Snowball throwSnowball() {
        net.minecraft.server.World world = ((CraftWorld) getWorld()).getHandle();
        EntitySnowball snowball = new EntitySnowball(world, getHandle());

        world.addEntity(snowball);
        return (Snowball) snowball.getBukkitEntity();
    }

    public double getEyeHeight() {
        return 1.0D;
    }

    public double getEyeHeight(boolean ignoreSneaking) {
        return getEyeHeight();
    }

    private List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance, int maxLength) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<Block>();
        Iterator<Block> itr = new BlockIterator(this, maxDistance);
        while (itr.hasNext()) {
            Block block = itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            int id = block.getTypeId();
            if (transparent == null) {
                if (id != 0) {
                    break;
                }
            } else {
                if (!transparent.contains((byte) id)) {
                    break;
                }
            }
        }
        return blocks;
    }

    public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 0);
    }

    public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
        List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }

    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 2);
    }

    public Arrow shootArrow() {
        net.minecraft.server.World world = ((CraftWorld) getWorld()).getHandle();
        EntityArrow arrow = new EntityArrow(world, getHandle(), 1);

        world.addEntity(arrow);
        return (Arrow) arrow.getBukkitEntity();
    }

    public boolean isInsideVehicle() {
        return getHandle().vehicle != null;
    }

    public boolean leaveVehicle() {
        if (getHandle().vehicle == null) {
            return false;
        }

        getHandle().setPassengerOf(null);
        return true;
    }

    public Vehicle getVehicle() {
        if (getHandle().vehicle == null) {
            return null;
        }

        org.bukkit.entity.Entity vehicle = (getHandle().vehicle.getBukkitEntity());
        if (vehicle instanceof Vehicle) {
            return (Vehicle) vehicle;
        }

        return null;
    }

    public int getRemainingAir() {
        return getHandle().getAirTicks();
    }

    public void setRemainingAir(int ticks) {
        getHandle().setAirTicks(ticks);
    }

    public int getMaximumAir() {
        return getHandle().maxAirTicks;
    }

    public void setMaximumAir(int ticks) {
        getHandle().maxAirTicks = ticks;
    }

    public void damage(int amount) {
        entity.damageEntity(DamageSource.GENERIC, amount);
    }

    public void damage(int amount, org.bukkit.entity.Entity source) {
        DamageSource reason = DamageSource.GENERIC;

        if (source instanceof HumanEntity) {
            reason = DamageSource.playerAttack(((CraftHumanEntity) source).getHandle());
        } else if (source instanceof LivingEntity) {
            reason = DamageSource.mobAttack(((CraftLivingEntity) source).getHandle());
        }

        entity.damageEntity(reason, amount);
    }

    public Location getEyeLocation() {
        Location loc = getLocation();
        loc.setY(loc.getY() + getEyeHeight());
        return loc;
    }

    public int getMaximumNoDamageTicks() {
        return getHandle().maxNoDamageTicks;
    }

    public void setMaximumNoDamageTicks(int ticks) {
        getHandle().maxNoDamageTicks = ticks;
    }

    public int getLastDamage() {
        return getHandle().lastDamage;
    }

    public void setLastDamage(int damage) {
        getHandle().lastDamage = damage;
    }

    public int getNoDamageTicks() {
        return getHandle().noDamageTicks;
    }

    public void setNoDamageTicks(int ticks) {
        getHandle().noDamageTicks = ticks;
    }

    @Override
    public EntityLiving getHandle() {
        return (EntityLiving) entity;
    }

    public void setHandle(final EntityLiving entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftLivingEntity{" + "id=" + getEntityId() + '}';
    }

    public Player getKiller() {
        return getHandle().killer == null ? null : (Player) getHandle().killer.getBukkitEntity();
    }

    public boolean addPotionEffect(PotionEffect effect) {
        return addPotionEffect(effect, false);
    }

    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        if (hasPotionEffect(effect.getType())) {
            if (!force) {
                return false;
            }
            removePotionEffect(effect.getType());
        }
        getHandle().addEffect(new MobEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier()));
        return true;
    }

    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        boolean success = true;
        for (PotionEffect effect : effects) {
            success &= addPotionEffect(effect);
        }
        return success;
    }

    public boolean hasPotionEffect(PotionEffectType type) {
        return getHandle().hasEffect(MobEffectList.byId[type.getId()]);
    }

    public void removePotionEffect(PotionEffectType type) {
        getHandle().effects.remove(type.getId());
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        List<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (Object raw : getHandle().effects.values()) {
            if (!(raw instanceof MobEffect))
                continue;
            MobEffect handle = (MobEffect) raw;
            effects.add(new PotionEffect(PotionEffectType.getById(handle.getEffectId()), handle.getDuration(), handle.getAmplifier()));
        }
        return effects;
    }
}
