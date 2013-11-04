package org.bukkit.craftbukkit.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityEgg;
import net.minecraft.server.EntityEnderDragon;
import net.minecraft.server.EntityEnderPearl;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLargeFireball;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityPotion;
import net.minecraft.server.EntitySmallFireball;
import net.minecraft.server.EntitySnowball;
import net.minecraft.server.EntityWither;
import net.minecraft.server.EntityWitherSkull;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftEntityEquipment;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {
    private CraftEntityEquipment equipment;

    public CraftLivingEntity(final CraftServer server, final EntityLiving entity) {
        super(server, entity);

        if (entity instanceof EntityInsentient) {
            equipment = new CraftEntityEquipment(this);
        }
    }

    public double getHealth() {
        return Math.min(Math.max(0, getHandle().getHealth()), getMaxHealth());
    }

    public void setHealth(double health) {
        if ((health < 0) || (health > getMaxHealth())) {
            throw new IllegalArgumentException("Health must be between 0 and " + getMaxHealth());
        }

        if (entity instanceof EntityPlayer && health == 0) {
            ((EntityPlayer) entity).die(DamageSource.GENERIC);
        }

        getHandle().setHealth((float) health);
    }

    public double getMaxHealth() {
        return getHandle().getMaxHealth();
    }

    public void setMaxHealth(double amount) {
        Validate.isTrue(amount > 0, "Max health must be greater than 0");

        getHandle().getAttributeInstance(GenericAttributes.a).setValue(amount);

        if (getHealth() > amount) {
            setHealth(amount);
        }
    }

    public void resetMaxHealth() {
        setMaxHealth(getHandle().getMaxHealth());
    }

    @Deprecated
    public Egg throwEgg() {
        return launchProjectile(Egg.class);
    }

    @Deprecated
    public Snowball throwSnowball() {
        return launchProjectile(Snowball.class);
    }

    public double getEyeHeight() {
        return getHandle().getHeadHeight();
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

    @Deprecated
    public Arrow shootArrow() {
        return launchProjectile(Arrow.class);
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

    public void damage(double amount) {
        damage(amount, null);
    }

    public void damage(double amount, org.bukkit.entity.Entity source) {
        DamageSource reason = DamageSource.GENERIC;

        if (source instanceof HumanEntity) {
            reason = DamageSource.playerAttack(((CraftHumanEntity) source).getHandle());
        } else if (source instanceof LivingEntity) {
            reason = DamageSource.mobAttack(((CraftLivingEntity) source).getHandle());
        }

        if (entity instanceof EntityEnderDragon) {
            ((EntityEnderDragon) entity).dealDamage(reason, (float) amount);
        } else {
            entity.damageEntity(reason, (float) amount);
        }
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

    public double getLastDamage() {
        return getHandle().lastDamage;
    }

    public void setLastDamage(double damage) {
        getHandle().lastDamage = (float) damage;
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
        getHandle().addEffect(new MobEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient()));
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
        getHandle().m(type.getId()); // Should be removeEffect.
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        List<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (Object raw : getHandle().effects.values()) {
            if (!(raw instanceof MobEffect))
                continue;
            MobEffect handle = (MobEffect) raw;
            effects.add(new PotionEffect(PotionEffectType.getById(handle.getEffectId()), handle.getDuration(), handle.getAmplifier(), handle.isAmbient()));
        }
        return effects;
    }

    @SuppressWarnings("unchecked")
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        net.minecraft.server.World world = ((CraftWorld) getWorld()).getHandle();
        net.minecraft.server.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new EntitySnowball(world, getHandle());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EntityEgg(world, getHandle());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EntityEnderPearl(world, getHandle());
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            launch = new EntityArrow(world, getHandle(), 1);
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            launch = new EntityPotion(world, getHandle(), CraftItemStack.asNMSCopy(new ItemStack(Material.POTION, 1)));
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            Location location = getEyeLocation();
            Vector direction = location.getDirection().multiply(10);

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new EntitySmallFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new EntityWitherSkull(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else {
                launch = new EntityLargeFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            }

            launch.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        Validate.notNull(launch, "Projectile not supported");

        world.addEntity(launch);
        return (T) launch.getBukkitEntity();
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }

    public boolean hasLineOfSight(Entity other) {
        return getHandle().o(((CraftEntity) other).getHandle());
    }

    public boolean getRemoveWhenFarAway() {
        return getHandle() instanceof EntityInsentient && !((EntityInsentient) getHandle()).persistent;
    }

    public void setRemoveWhenFarAway(boolean remove) {
        if (getHandle() instanceof EntityInsentient) {
            ((EntityInsentient) getHandle()).persistent = !remove;
        }
    }

    public EntityEquipment getEquipment() {
        return equipment;
    }

    public void setCanPickupItems(boolean pickup) {
        if (getHandle() instanceof EntityInsentient) {
            ((EntityInsentient) getHandle()).canPickUpLoot = pickup;
        }
    }

    public boolean getCanPickupItems() {
        return getHandle() instanceof EntityInsentient && ((EntityInsentient) getHandle()).canPickUpLoot;
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        if (getHealth() == 0) {
            return false;
        }

        return super.teleport(location, cause);
    }

    public void setCustomName(String name) {
        if (!(getHandle() instanceof EntityInsentient)) {
            return;
        }

        if (name == null) {
            name = "";
        }

        // Names cannot be more than 64 characters due to DataWatcher limitations
        if (name.length() > 64) {
            name = name.substring(0, 64);
        }

        ((EntityInsentient) getHandle()).setCustomName(name);
    }

    public String getCustomName() {
        if (!(getHandle() instanceof EntityInsentient)) {
            return null;
        }

        String name = ((EntityInsentient) getHandle()).getCustomName();

        if (name == null || name.length() == 0) {
            return null;
        }

        return name;
    }

    public void setCustomNameVisible(boolean flag) {
        if (getHandle() instanceof EntityInsentient) {
            ((EntityInsentient) getHandle()).setCustomNameVisible(flag);
        }
    }

    public boolean isCustomNameVisible() {
        return getHandle() instanceof EntityInsentient && ((EntityInsentient) getHandle()).getCustomNameVisible();
    }

    public boolean isLeashed() {
        if (!(getHandle() instanceof EntityInsentient)) {
            return false;
        }
        return ((EntityInsentient) getHandle()).getLeashHolder() != null;
    }

    public Entity getLeashHolder() throws IllegalStateException {
        if (!isLeashed()) {
            throw new IllegalStateException("Entity not leashed");
        }
        return ((EntityInsentient) getHandle()).getLeashHolder().getBukkitEntity();
    }

    private boolean unleash() {
        if (!isLeashed()) {
            return false;
        }
        ((EntityInsentient) getHandle()).unleash(true, false);
        return true;
    }

    public boolean setLeashHolder(Entity holder) {
        if ((getHandle() instanceof EntityWither) || !(getHandle() instanceof EntityInsentient)) {
            return false;
        }

        if (holder == null) {
            return unleash();
        }

        if (holder.isDead()) {
            return false;
        }

        unleash();
        ((EntityInsentient) getHandle()).setLeashHolder(((CraftEntity) holder).getHandle(), true);
        return true;
    }

    @Deprecated
    public int _INVALID_getLastDamage() {
        return NumberConversions.ceil(getLastDamage());
    }

    @Deprecated
    public void _INVALID_setLastDamage(int damage) {
        setLastDamage(damage);
    }

    @Deprecated
    public void _INVALID_damage(int amount) {
        damage(amount);
    }

    @Deprecated
    public void _INVALID_damage(int amount, Entity source) {
        damage(amount, source);
    }

    @Deprecated
    public int _INVALID_getHealth() {
        return NumberConversions.ceil(getHealth());
    }

    @Deprecated
    public void _INVALID_setHealth(int health) {
        setHealth(health);
    }

    @Deprecated
    public int _INVALID_getMaxHealth() {
        return NumberConversions.ceil(getMaxHealth());
    }

    @Deprecated
    public void _INVALID_setMaxHealth(int health) {
        setMaxHealth(health);
    }
}
