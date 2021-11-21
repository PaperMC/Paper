package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.world.EnumHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectList;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMonsterType;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.boss.wither.EntityWither;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.projectile.EntityDragonFireball;
import net.minecraft.world.entity.projectile.EntityEgg;
import net.minecraft.world.entity.projectile.EntityEnderPearl;
import net.minecraft.world.entity.projectile.EntityFireball;
import net.minecraft.world.entity.projectile.EntityFireworks;
import net.minecraft.world.entity.projectile.EntityFishingHook;
import net.minecraft.world.entity.projectile.EntityLargeFireball;
import net.minecraft.world.entity.projectile.EntityLlamaSpit;
import net.minecraft.world.entity.projectile.EntityPotion;
import net.minecraft.world.entity.projectile.EntityProjectile;
import net.minecraft.world.entity.projectile.EntityShulkerBullet;
import net.minecraft.world.entity.projectile.EntitySmallFireball;
import net.minecraft.world.entity.projectile.EntitySnowball;
import net.minecraft.world.entity.projectile.EntitySpectralArrow;
import net.minecraft.world.entity.projectile.EntityThrownExpBottle;
import net.minecraft.world.entity.projectile.EntityThrownTrident;
import net.minecraft.world.entity.projectile.EntityTippedArrow;
import net.minecraft.world.entity.projectile.EntityWitherSkull;
import org.apache.commons.lang.Validate;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.memory.CraftMemoryKey;
import org.bukkit.craftbukkit.entity.memory.CraftMemoryMapper;
import org.bukkit.craftbukkit.inventory.CraftEntityEquipment;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Trident;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {
    private CraftEntityEquipment equipment;

    public CraftLivingEntity(final CraftServer server, final EntityLiving entity) {
        super(server, entity);

        if (entity instanceof EntityInsentient || entity instanceof EntityArmorStand) {
            equipment = new CraftEntityEquipment(this);
        }
    }

    @Override
    public double getHealth() {
        return Math.min(Math.max(0, getHandle().getHealth()), getMaxHealth());
    }

    @Override
    public void setHealth(double health) {
        health = (float) health;
        if ((health < 0) || (health > getMaxHealth())) {
            throw new IllegalArgumentException("Health must be between 0 and " + getMaxHealth() + "(" + health + ")");
        }

        // during world generation, we don't want to run logic for dropping items and xp
        if (getHandle().generation && health == 0) {
            getHandle().discard();
            return;
        }

        getHandle().setHealth((float) health);

        if (health == 0) {
            getHandle().die(DamageSource.GENERIC);
        }
    }

    @Override
    public double getAbsorptionAmount() {
        return getHandle().getAbsorptionAmount();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        Preconditions.checkArgument(amount >= 0 && Double.isFinite(amount), "amount < 0 or non-finite");

        getHandle().setAbsorptionAmount((float) amount);
    }

    @Override
    public double getMaxHealth() {
        return getHandle().getMaxHealth();
    }

    @Override
    public void setMaxHealth(double amount) {
        Validate.isTrue(amount > 0, "Max health must be greater than 0");

        getHandle().getAttribute(GenericAttributes.MAX_HEALTH).setBaseValue(amount);

        if (getHealth() > amount) {
            setHealth(amount);
        }
    }

    @Override
    public void resetMaxHealth() {
        setMaxHealth(getHandle().getAttribute(GenericAttributes.MAX_HEALTH).getAttribute().getDefaultValue());
    }

    @Override
    public double getEyeHeight() {
        return getHandle().getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return getEyeHeight();
    }

    private List<Block> getLineOfSight(Set<Material> transparent, int maxDistance, int maxLength) {
        Preconditions.checkState(!getHandle().generation, "Cannot get line of sight during world generation");

        if (transparent == null) {
            transparent = Sets.newHashSet(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR);
        }
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
            Material material = block.getType();
            if (!transparent.contains(material)) {
                break;
            }
        }
        return blocks;
    }

    @Override
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 0);
    }

    @Override
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 2);
    }

    @Override
    public Block getTargetBlockExact(int maxDistance) {
        return this.getTargetBlockExact(maxDistance, FluidCollisionMode.NEVER);
    }

    @Override
    public Block getTargetBlockExact(int maxDistance, FluidCollisionMode fluidCollisionMode) {
        RayTraceResult hitResult = this.rayTraceBlocks(maxDistance, fluidCollisionMode);
        return (hitResult != null ? hitResult.getHitBlock() : null);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance) {
        return this.rayTraceBlocks(maxDistance, FluidCollisionMode.NEVER);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance, FluidCollisionMode fluidCollisionMode) {
        Preconditions.checkState(!getHandle().generation, "Cannot ray tray blocks during world generation");

        Location eyeLocation = this.getEyeLocation();
        Vector direction = eyeLocation.getDirection();
        return this.getWorld().rayTraceBlocks(eyeLocation, direction, maxDistance, fluidCollisionMode, false);
    }

    @Override
    public int getRemainingAir() {
        return getHandle().getAirSupply();
    }

    @Override
    public void setRemainingAir(int ticks) {
        getHandle().setAirSupply(ticks);
    }

    @Override
    public int getMaximumAir() {
        return getHandle().maxAirTicks;
    }

    @Override
    public void setMaximumAir(int ticks) {
        getHandle().maxAirTicks = ticks;
    }

    @Override
    public int getArrowCooldown() {
        return getHandle().removeArrowTime;
    }

    @Override
    public void setArrowCooldown(int ticks) {
        getHandle().removeArrowTime = ticks;
    }

    @Override
    public int getArrowsInBody() {
        return getHandle().getArrowCount();
    }

    @Override
    public void setArrowsInBody(int count) {
        Preconditions.checkArgument(count >= 0, "New arrow amount must be >= 0");
        getHandle().getEntityData().set(EntityLiving.DATA_ARROW_COUNT_ID, count);
    }

    @Override
    public void damage(double amount) {
        damage(amount, null);
    }

    @Override
    public void damage(double amount, org.bukkit.entity.Entity source) {
        Preconditions.checkState(!getHandle().generation, "Cannot damage entity during world generation");

        DamageSource reason = DamageSource.GENERIC;

        if (source instanceof HumanEntity) {
            reason = DamageSource.playerAttack(((CraftHumanEntity) source).getHandle());
        } else if (source instanceof LivingEntity) {
            reason = DamageSource.mobAttack(((CraftLivingEntity) source).getHandle());
        }

        entity.hurt(reason, (float) amount);
    }

    @Override
    public Location getEyeLocation() {
        Location loc = getLocation();
        loc.setY(loc.getY() + getEyeHeight());
        return loc;
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return getHandle().invulnerableDuration;
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        getHandle().invulnerableDuration = ticks;
    }

    @Override
    public double getLastDamage() {
        return getHandle().lastHurt;
    }

    @Override
    public void setLastDamage(double damage) {
        getHandle().lastHurt = (float) damage;
    }

    @Override
    public int getNoDamageTicks() {
        return getHandle().invulnerableTime;
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        getHandle().invulnerableTime = ticks;
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

    @Override
    public Player getKiller() {
        return getHandle().lastHurtByPlayer == null ? null : (Player) getHandle().lastHurtByPlayer.getBukkitEntity();
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect) {
        return addPotionEffect(effect, false);
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        getHandle().addEffect(new MobEffect(MobEffectList.byId(effect.getType().getId()), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()), EntityPotionEffectEvent.Cause.PLUGIN);
        return true;
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        boolean success = true;
        for (PotionEffect effect : effects) {
            success &= addPotionEffect(effect);
        }
        return success;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        return getHandle().hasEffect(MobEffectList.byId(type.getId()));
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        MobEffect handle = getHandle().getEffect(MobEffectList.byId(type.getId()));
        return (handle == null) ? null : new PotionEffect(PotionEffectType.getById(MobEffectList.getId(handle.getEffect())), handle.getDuration(), handle.getAmplifier(), handle.isAmbient(), handle.isVisible());
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        getHandle().removeEffect(MobEffectList.byId(type.getId()), EntityPotionEffectEvent.Cause.PLUGIN);
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        List<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (MobEffect handle : getHandle().activeEffects.values()) {
            effects.add(new PotionEffect(PotionEffectType.getById(MobEffectList.getId(handle.getEffect())), handle.getDuration(), handle.getAmplifier(), handle.isAmbient(), handle.isVisible()));
        }
        return effects;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        Preconditions.checkState(!getHandle().generation, "Cannot launch projectile during world generation");

        net.minecraft.world.level.World world = ((CraftWorld) getWorld()).getHandle();
        net.minecraft.world.entity.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new EntitySnowball(world, getHandle());
            ((EntityProjectile) launch).shootFromRotation(getHandle(), getHandle().getXRot(), getHandle().getYRot(), 0.0F, 1.5F, 1.0F); // ItemSnowball
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EntityEgg(world, getHandle());
            ((EntityProjectile) launch).shootFromRotation(getHandle(), getHandle().getXRot(), getHandle().getYRot(), 0.0F, 1.5F, 1.0F); // ItemEgg
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EntityEnderPearl(world, getHandle());
            ((EntityProjectile) launch).shootFromRotation(getHandle(), getHandle().getXRot(), getHandle().getYRot(), 0.0F, 1.5F, 1.0F); // ItemEnderPearl
        } else if (AbstractArrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                launch = new EntityTippedArrow(world, getHandle());
                ((EntityTippedArrow) launch).setPotionType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
            } else if (SpectralArrow.class.isAssignableFrom(projectile)) {
                launch = new EntitySpectralArrow(world, getHandle());
            } else if (Trident.class.isAssignableFrom(projectile)) {
                launch = new EntityThrownTrident(world, getHandle(), new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.TRIDENT));
            } else {
                launch = new EntityTippedArrow(world, getHandle());
            }
            ((EntityArrow) launch).shootFromRotation(getHandle(), getHandle().getXRot(), getHandle().getYRot(), 0.0F, 3.0F, 1.0F); // ItemBow
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            if (LingeringPotion.class.isAssignableFrom(projectile)) {
                launch = new EntityPotion(world, getHandle());
                ((EntityPotion) launch).setItem(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.LINGERING_POTION, 1)));
            } else {
                launch = new EntityPotion(world, getHandle());
                ((EntityPotion) launch).setItem(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.SPLASH_POTION, 1)));
            }
            ((EntityProjectile) launch).shootFromRotation(getHandle(), getHandle().getXRot(), getHandle().getYRot(), -20.0F, 0.5F, 1.0F); // ItemSplashPotion
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new EntityThrownExpBottle(world, getHandle());
            ((EntityProjectile) launch).shootFromRotation(getHandle(), getHandle().getXRot(), getHandle().getYRot(), -20.0F, 0.7F, 1.0F); // ItemExpBottle
        } else if (FishHook.class.isAssignableFrom(projectile) && getHandle() instanceof EntityHuman) {
            launch = new EntityFishingHook((EntityHuman) getHandle(), world, 0, 0);
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            Location location = getEyeLocation();
            Vector direction = location.getDirection().multiply(10);

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new EntitySmallFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new EntityWitherSkull(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else if (DragonFireball.class.isAssignableFrom(projectile)) {
                launch = new EntityDragonFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else {
                launch = new EntityLargeFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ(), 1);
            }

            ((EntityFireball) launch).projectileSource = this;
            launch.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (LlamaSpit.class.isAssignableFrom(projectile)) {
            Location location = getEyeLocation();
            Vector direction = location.getDirection();

            launch = EntityTypes.LLAMA_SPIT.create(world);

            ((EntityLlamaSpit) launch).setOwner(getHandle());
            ((EntityLlamaSpit) launch).shoot(direction.getX(), direction.getY(), direction.getZ(), 1.5F, 10.0F); // EntityLlama
            launch.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (ShulkerBullet.class.isAssignableFrom(projectile)) {
            Location location = getEyeLocation();

            launch = new EntityShulkerBullet(world, getHandle(), null, null);
            launch.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (Firework.class.isAssignableFrom(projectile)) {
            Location location = getEyeLocation();

            launch = new EntityFireworks(world, net.minecraft.world.item.ItemStack.EMPTY, getHandle());
            launch.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        Validate.notNull(launch, "Projectile not supported");

        if (velocity != null) {
            ((T) launch.getBukkitEntity()).setVelocity(velocity);
        }

        world.addFreshEntity(launch);
        return (T) launch.getBukkitEntity();
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        Preconditions.checkState(!getHandle().generation, "Cannot check line of sight during world generation");

        return getHandle().hasLineOfSight(((CraftEntity) other).getHandle());
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return getHandle() instanceof EntityInsentient && !((EntityInsentient) getHandle()).isPersistenceRequired();
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        if (getHandle() instanceof EntityInsentient) {
            ((EntityInsentient) getHandle()).setPersistenceRequired(!remove);
        }
    }

    @Override
    public EntityEquipment getEquipment() {
        return equipment;
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        if (getHandle() instanceof EntityInsentient) {
            ((EntityInsentient) getHandle()).setCanPickUpLoot(pickup);
        } else {
            getHandle().bukkitPickUpLoot = pickup;
        }
    }

    @Override
    public boolean getCanPickupItems() {
        if (getHandle() instanceof EntityInsentient) {
            return ((EntityInsentient) getHandle()).canPickUpLoot();
        } else {
            return getHandle().bukkitPickUpLoot;
        }
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        if (getHealth() == 0) {
            return false;
        }

        return super.teleport(location, cause);
    }

    @Override
    public boolean isLeashed() {
        if (!(getHandle() instanceof EntityInsentient)) {
            return false;
        }
        return ((EntityInsentient) getHandle()).getLeashHolder() != null;
    }

    @Override
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
        ((EntityInsentient) getHandle()).dropLeash(true, false);
        return true;
    }

    @Override
    public boolean setLeashHolder(Entity holder) {
        if (getHandle().generation || (getHandle() instanceof EntityWither) || !(getHandle() instanceof EntityInsentient)) {
            return false;
        }

        if (holder == null) {
            return unleash();
        }

        if (holder.isDead()) {
            return false;
        }

        unleash();
        ((EntityInsentient) getHandle()).setLeashedTo(((CraftEntity) holder).getHandle(), true);
        return true;
    }

    @Override
    public boolean isGliding() {
        return getHandle().getSharedFlag(7);
    }

    @Override
    public void setGliding(boolean gliding) {
        getHandle().setSharedFlag(7, gliding);
    }

    @Override
    public boolean isSwimming() {
        return getHandle().isSwimming();
    }

    @Override
    public void setSwimming(boolean swimming) {
        getHandle().setSwimming(swimming);
    }

    @Override
    public boolean isRiptiding() {
        return getHandle().isAutoSpinAttack();
    }

    @Override
    public boolean isSleeping() {
        return getHandle().isSleeping();
    }

    @Override
    public boolean isClimbing() {
        Preconditions.checkState(!getHandle().generation, "Cannot check if climbing during world generation");

        return getHandle().onClimbable();
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        return getHandle().craftAttributes.getAttribute(attribute);
    }

    @Override
    public void setAI(boolean ai) {
        if (this.getHandle() instanceof EntityInsentient) {
            ((EntityInsentient) this.getHandle()).setNoAi(!ai);
        }
    }

    @Override
    public boolean hasAI() {
        return (this.getHandle() instanceof EntityInsentient) ? !((EntityInsentient) this.getHandle()).isNoAi() : false;
    }

    @Override
    public void attack(Entity target) {
        Preconditions.checkArgument(target != null, "target == null");
        Preconditions.checkState(!getHandle().generation, "Cannot attack during world generation");

        if (getHandle() instanceof EntityHuman) {
            ((EntityHuman) getHandle()).attack(((CraftEntity) target).getHandle());
        } else {
            getHandle().doHurtTarget(((CraftEntity) target).getHandle());
        }
    }

    @Override
    public void swingMainHand() {
        Preconditions.checkState(!getHandle().generation, "Cannot swing hand during world generation");

        getHandle().swing(EnumHand.MAIN_HAND, true);
    }

    @Override
    public void swingOffHand() {
        Preconditions.checkState(!getHandle().generation, "Cannot swing hand during world generation");

        getHandle().swing(EnumHand.OFF_HAND, true);
    }

    @Override
    public void setCollidable(boolean collidable) {
        getHandle().collides = collidable;
    }

    @Override
    public boolean isCollidable() {
        return getHandle().collides;
    }

    @Override
    public Set<UUID> getCollidableExemptions() {
        return getHandle().collidableExemptions;
    }

    @Override
    public <T> T getMemory(MemoryKey<T> memoryKey) {
        return (T) getHandle().getBrain().getMemory(CraftMemoryKey.fromMemoryKey(memoryKey)).map(CraftMemoryMapper::fromNms).orElse(null);
    }

    @Override
    public <T> void setMemory(MemoryKey<T> memoryKey, T t) {
        getHandle().getBrain().setMemory(CraftMemoryKey.fromMemoryKey(memoryKey), CraftMemoryMapper.toNms(t));
    }

    @Override
    public EntityCategory getCategory() {
        EnumMonsterType type = getHandle().getMobType(); // Not actually an enum?

        if (type == EnumMonsterType.UNDEFINED) {
            return EntityCategory.NONE;
        } else if (type == EnumMonsterType.UNDEAD) {
            return EntityCategory.UNDEAD;
        } else if (type == EnumMonsterType.ARTHROPOD) {
            return EntityCategory.ARTHROPOD;
        } else if (type == EnumMonsterType.ILLAGER) {
            return EntityCategory.ILLAGER;
        } else if (type == EnumMonsterType.WATER) {
            return EntityCategory.WATER;
        }

        throw new UnsupportedOperationException("Unsupported monster type: " + type + ". This is a bug, report this to Spigot.");
    }

    @Override
    public boolean isInvisible() {
        return getHandle().isInvisible();
    }

    @Override
    public void setInvisible(boolean invisible) {
        getHandle().persistentInvisibility = invisible;
        getHandle().setSharedFlag(5, invisible);
    }
}
