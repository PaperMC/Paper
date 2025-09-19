package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.Optionull;
import io.papermc.paper.world.damagesource.CombatTracker;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.phys.Vec3;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.craftbukkit.entity.memory.CraftMemoryKey;
import org.bukkit.craftbukkit.entity.memory.CraftMemoryMapper;
import org.bukkit.craftbukkit.inventory.CraftEntityEquipment;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractWindCharge;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.BreezeWindCharge;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
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
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NonNull;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {

    private CraftEntityEquipment equipment;

    public CraftLivingEntity(final CraftServer server, final net.minecraft.world.entity.LivingEntity entity) {
        super(server, entity);

        if (entity instanceof Mob || entity instanceof ArmorStand || entity instanceof Mannequin) {
            this.equipment = new CraftEntityEquipment(this);
        }
    }

    @Override
    public net.minecraft.world.entity.LivingEntity getHandle() {
        return (net.minecraft.world.entity.LivingEntity) this.entity;
    }

    @Override
    public double getHealth() {
        return Math.min(Math.max(0, this.getHandle().getHealth()), this.getMaxHealth());
    }

    @Override
    public void setHealth(double health) {
        health = (float) health;
        // Paper start - Be more informative
        Preconditions.checkArgument(health >= 0 && health <= this.getMaxHealth(),
            "Health value (%s) must be between 0 and %s. (attribute base value: %s%s)",
            health, this.getMaxHealth(), this.getHandle().getAttribute(Attributes.MAX_HEALTH).getBaseValue(), this instanceof CraftPlayer ? ", player: " + this.getName() : ""
        );
        // Paper end

        // during world generation, we don't want to run logic for dropping items and xp
        if (this.getHandle().generation && health == 0) {
            this.getHandle().discard(null); // Add Bukkit remove cause
            return;
        }

        this.getHandle().setHealth((float) health);

        if (health == 0) {
            this.getHandle().die(this.getHandle().damageSources().generic());
        }
    }

    // Paper start - entity heal API
    @Override
    public void heal(final double amount, final org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason reason) {
        this.getHandle().heal((float) amount, reason);
    }
    // Paper end - entity heal API

    @Override
    public double getAbsorptionAmount() {
        return this.getHandle().getAbsorptionAmount();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        Preconditions.checkArgument(amount >= 0 && Double.isFinite(amount), "amount < 0 or non-finite");

        this.getHandle().setAbsorptionAmount((float) amount);
    }

    @Override
    public double getMaxHealth() {
        return this.getHandle().getMaxHealth();
    }

    @Override
    public void setMaxHealth(double amount) {
        Preconditions.checkArgument(amount > 0, "Max health amount (%s) must be greater than 0", amount);

        this.getHandle().getAttribute(Attributes.MAX_HEALTH).setBaseValue(amount);

        if (this.getHealth() > amount) {
            this.setHealth(amount);
        }
    }

    @Override
    public void resetMaxHealth() {
        this.setMaxHealth(this.getHandle().getAttribute(Attributes.MAX_HEALTH).getAttribute().value().getDefaultValue());
    }

    @Override
    public double getEyeHeight() {
        return this.getHandle().getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return this.getEyeHeight();
    }

    private List<Block> getLineOfSight(Set<Material> transparent, int maxDistance, int maxLength) {
        Preconditions.checkState(!this.getHandle().generation, "Cannot get line of sight during world generation");

        if (transparent == null) {
            transparent = Sets.newHashSet(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR);
        }
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<>();
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
        return this.getLineOfSight(transparent, maxDistance, 0);
    }

    @Override
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        List<Block> blocks = this.getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }

    // Paper start
    @Override
    public Block getTargetBlock(int maxDistance, com.destroystokyo.paper.block.TargetBlockInfo.FluidMode fluidMode) {
        return this.getTargetBlockExact(maxDistance, fluidMode.bukkit);
    }

    @Override
    public org.bukkit.block.BlockFace getTargetBlockFace(int maxDistance, com.destroystokyo.paper.block.TargetBlockInfo.FluidMode fluidMode) {
        return this.getTargetBlockFace(maxDistance, fluidMode.bukkit);
    }

    @Override
    public org.bukkit.block.BlockFace getTargetBlockFace(int maxDistance, org.bukkit.FluidCollisionMode fluidMode) {
        RayTraceResult result = this.rayTraceBlocks(maxDistance, fluidMode);
        return result != null ? result.getHitBlockFace() : null;
    }

    @Override
    public com.destroystokyo.paper.block.TargetBlockInfo getTargetBlockInfo(int maxDistance, com.destroystokyo.paper.block.TargetBlockInfo.FluidMode fluidMode) {
        RayTraceResult result = this.rayTraceBlocks(maxDistance, fluidMode.bukkit);
        if (result != null && result.getHitBlock() != null && result.getHitBlockFace() != null) {
            return new com.destroystokyo.paper.block.TargetBlockInfo(result.getHitBlock(), result.getHitBlockFace());
        }
        return null;
    }

    public Entity getTargetEntity(int maxDistance, boolean ignoreBlocks) {
        net.minecraft.world.phys.EntityHitResult rayTrace = rayTraceEntity(maxDistance, ignoreBlocks);
        return rayTrace == null ? null : rayTrace.getEntity().getBukkitEntity();
    }

    public com.destroystokyo.paper.entity.TargetEntityInfo getTargetEntityInfo(int maxDistance, boolean ignoreBlocks) {
        net.minecraft.world.phys.EntityHitResult rayTrace = rayTraceEntity(maxDistance, ignoreBlocks);
        return rayTrace == null ? null : new com.destroystokyo.paper.entity.TargetEntityInfo(rayTrace.getEntity().getBukkitEntity(), new org.bukkit.util.Vector(rayTrace.getLocation().x, rayTrace.getLocation().y, rayTrace.getLocation().z));
    }

    @Override
    public RayTraceResult rayTraceEntities(int maxDistance, boolean ignoreBlocks) {
        net.minecraft.world.phys.EntityHitResult rayTrace = this.rayTraceEntity(maxDistance, ignoreBlocks);
        return rayTrace == null ? null : new org.bukkit.util.RayTraceResult(org.bukkit.craftbukkit.util.CraftVector.toBukkit(rayTrace.getLocation()), rayTrace.getEntity().getBukkitEntity());
    }

    public net.minecraft.world.phys.EntityHitResult rayTraceEntity(int maxDistance, boolean ignoreBlocks) {
        net.minecraft.world.phys.EntityHitResult rayTrace = getHandle().getTargetEntity(maxDistance);
        if (rayTrace == null) {
            return null;
        }
        if (!ignoreBlocks) {
            net.minecraft.world.phys.HitResult rayTraceBlocks = getHandle().getRayTrace(maxDistance, net.minecraft.world.level.ClipContext.Fluid.NONE);
            if (rayTraceBlocks != null) {
                net.minecraft.world.phys.Vec3 eye = getHandle().getEyePosition(1.0F);
                if (eye.distanceToSqr(rayTraceBlocks.getLocation()) <= eye.distanceToSqr(rayTrace.getLocation())) {
                    return null;
                }
            }
        }
        return rayTrace;
    }
    // Paper end

    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        return this.getLineOfSight(transparent, maxDistance, 2);
    }

    @Override
    public Block getTargetBlockExact(int maxDistance, FluidCollisionMode fluidCollisionMode) {
        RayTraceResult hitResult = this.rayTraceBlocks(maxDistance, fluidCollisionMode);
        return (hitResult != null ? hitResult.getHitBlock() : null);
    }

    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance, FluidCollisionMode fluidCollisionMode) {
        Preconditions.checkState(!this.getHandle().generation, "Cannot ray tray blocks during world generation");

        Location eyeLocation = this.getEyeLocation();
        Vector direction = eyeLocation.getDirection();
        return this.getWorld().rayTraceBlocks(eyeLocation, direction, maxDistance, fluidCollisionMode, false);
    }

    @Override
    public int getRemainingAir() {
        return this.getHandle().getAirSupply();
    }

    @Override
    public void setRemainingAir(int ticks) {
        this.getHandle().setAirSupply(ticks);
    }

    @Override
    public int getMaximumAir() {
        return this.getHandle().maxAirTicks;
    }

    @Override
    public void setMaximumAir(int ticks) {
        this.getHandle().maxAirTicks = ticks;
    }

    @Override
    public ItemStack getItemInUse() {
        net.minecraft.world.item.ItemStack item = this.getHandle().getUseItem();
        return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
    }

    @Override
    public int getItemInUseTicks() {
        return this.getHandle().getUseItemRemainingTicks();
    }

    @Override
    public void setItemInUseTicks(int ticks) {
        this.getHandle().useItemRemaining = ticks;
    }

    @Override
    public int getArrowCooldown() {
        return this.getHandle().removeArrowTime;
    }

    @Override
    public void setArrowCooldown(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "Amount of ticks before next arrow removal must be non-negative");
        this.getHandle().removeArrowTime = ticks;
    }

    @Override
    public int getArrowsInBody() {
        return this.getHandle().getArrowCount();
    }

    @Override
    public void setArrowsInBody(final int count, final boolean fireEvent) { // Paper
        Preconditions.checkArgument(count >= 0, "New arrow amount must be non-negative");
        if (!fireEvent) {
            this.getHandle().getEntityData().set(net.minecraft.world.entity.LivingEntity.DATA_ARROW_COUNT_ID, count);
        } else {
            this.getHandle().setArrowCount(count);
        }
    }

    @Override
    public boolean isInvulnerable() {
        return this.getHandle().isInvulnerableTo((ServerLevel) this.getHandle().level(), this.getHandle().damageSources().generic());
    }

    @Override
    public int getBeeStingerCooldown() {
        return this.getHandle().removeStingerTime;
    }

    @Override
    public void setBeeStingerCooldown(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "New amount of ticks before next bee stinger removal must be non-negative");
        this.getHandle().removeStingerTime = ticks;
    }

    @Override
    public int getBeeStingersInBody() {
        return this.getHandle().getStingerCount();
    }

    @Override
    public void setBeeStingersInBody(int count) {
        Preconditions.checkArgument(count >= 0, "New bee stinger amount must be >= 0");
        this.getHandle().setStingerCount(count);
    }

    @Override
    public void damage(double amount) {
        this.damage(amount, this.getHandle().damageSources().generic());
    }

    @Override
    public void damage(double amount, org.bukkit.entity.Entity source) {
        DamageSource reason = this.getHandle().damageSources().generic();

        if (source instanceof HumanEntity) {
            reason = this.getHandle().damageSources().playerAttack(((CraftHumanEntity) source).getHandle());
        } else if (source instanceof LivingEntity) {
            reason = this.getHandle().damageSources().mobAttack(((CraftLivingEntity) source).getHandle());
        }

        this.damage(amount, reason);
    }

    @Override
    public void damage(double amount, org.bukkit.damage.DamageSource damageSource) {
        Preconditions.checkArgument(damageSource != null, "damageSource cannot be null");

        this.damage(amount, ((CraftDamageSource) damageSource).getHandle());
    }

    private void damage(double amount, DamageSource damageSource) {
        Preconditions.checkArgument(damageSource != null, "damageSource cannot be null");
        Preconditions.checkState(!this.getHandle().generation, "Cannot damage entity during world generation");

        this.entity.hurt(damageSource, (float) amount);
    }

    @Override
    public Location getEyeLocation() {
        Location loc = this.getLocation();
        loc.setY(loc.getY() + this.getEyeHeight());
        return loc;
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return this.getHandle().invulnerableDuration;
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        this.getHandle().invulnerableDuration = ticks;
    }

    @Override
    public double getLastDamage() {
        return this.getHandle().lastHurt;
    }

    @Override
    public void setLastDamage(double damage) {
        this.getHandle().lastHurt = (float) damage;
    }

    @Override
    public int getNoDamageTicks() {
        return this.getHandle().invulnerableTime;
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        this.getHandle().invulnerableTime = ticks;
    }

    @Override
    public int getNoActionTicks() {
        return this.getHandle().getNoActionTime();
    }

    @Override
    public void setNoActionTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");
        this.getHandle().setNoActionTime(ticks);
    }

    @Override
    public Player getKiller() {
        return Optionull.map(this.getHandle().getLastHurtByPlayer(), player -> (Player) player.getBukkitEntity());
    }

    @Override
    public void setKiller(Player killer) {
        net.minecraft.server.level.ServerPlayer nmsKiller = killer == null ? null : ((CraftPlayer) killer).getHandle();
        this.getHandle().setLastHurtByMob(nmsKiller);
        if (nmsKiller != null) {
            this.getHandle().setLastHurtByPlayer(nmsKiller, 100); // value taken from LivingEntity#resolvePlayerResponsibleForDamage
        } else {
            this.getHandle().lastHurtByPlayer = null;
            this.getHandle().lastHurtByPlayerMemoryTime = 0;
        }
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        org.spigotmc.AsyncCatcher.catchOp("effect add"); // Paper
        this.getHandle().addEffect(org.bukkit.craftbukkit.potion.CraftPotionUtil.fromBukkit(effect), EntityPotionEffectEvent.Cause.PLUGIN); // Paper - Don't ignore icon
        return true;
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        boolean success = true;
        for (PotionEffect effect : effects) {
            success &= this.addPotionEffect(effect);
        }
        return success;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        return this.getHandle().hasEffect(CraftPotionEffectType.bukkitToMinecraftHolder(type));
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        MobEffectInstance handle = this.getHandle().getEffect(CraftPotionEffectType.bukkitToMinecraftHolder(type));
        return (handle == null) ? null : org.bukkit.craftbukkit.potion.CraftPotionUtil.toBukkit(handle); // Paper
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        this.getHandle().removeEffect(CraftPotionEffectType.bukkitToMinecraftHolder(type), EntityPotionEffectEvent.Cause.PLUGIN);
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        List<PotionEffect> effects = new ArrayList<>();
        for (MobEffectInstance handle : this.getHandle().activeEffects.values()) {
            effects.add(org.bukkit.craftbukkit.potion.CraftPotionUtil.toBukkit(handle)); // Paper
        }
        return effects;
    }

    @Override
    public boolean clearActivePotionEffects() {
        return this.getHandle().removeAllEffects(EntityPotionEffectEvent.Cause.PLUGIN);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity, java.util.function.Consumer<? super T> function) {
        Preconditions.checkState(!this.getHandle().generation, "Cannot launch projectile during world generation");

        net.minecraft.world.level.Level world = ((CraftWorld) this.getWorld()).getHandle();
        net.minecraft.world.entity.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new net.minecraft.world.entity.projectile.Snowball(world, this.getHandle(), new net.minecraft.world.item.ItemStack(Items.SNOWBALL));
            ((ThrowableProjectile) launch).shootFromRotation(this.getHandle(), this.getHandle().getXRot(), this.getHandle().getYRot(), 0.0F, 1.5F, 1.0F); // ItemSnowball
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new ThrownEgg(world, this.getHandle(), new net.minecraft.world.item.ItemStack(Items.EGG));
            ((ThrowableProjectile) launch).shootFromRotation(this.getHandle(), this.getHandle().getXRot(), this.getHandle().getYRot(), 0.0F, 1.5F, 1.0F); // ItemEgg
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new ThrownEnderpearl(world, this.getHandle(), new net.minecraft.world.item.ItemStack(Items.ENDER_PEARL));
            ((ThrowableProjectile) launch).shootFromRotation(this.getHandle(), this.getHandle().getXRot(), this.getHandle().getYRot(), 0.0F, 1.5F, 1.0F); // ItemEnderPearl
        } else if (AbstractArrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.world.entity.projectile.Arrow(world, this.getHandle(), new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.ARROW), null);
                ((Arrow) launch.getBukkitEntity()).setBasePotionType(PotionType.WATER);
            } else if (SpectralArrow.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.world.entity.projectile.SpectralArrow(world, this.getHandle(), new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.SPECTRAL_ARROW), null);
            } else if (Trident.class.isAssignableFrom(projectile)) {
                launch = new ThrownTrident(world, this.getHandle(), new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.TRIDENT));
            } else {
                launch = new net.minecraft.world.entity.projectile.Arrow(world, this.getHandle(), new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.ARROW), null);
            }
            ((net.minecraft.world.entity.projectile.AbstractArrow) launch).shootFromRotation(this.getHandle(), this.getHandle().getXRot(), this.getHandle().getYRot(), 0.0F, Trident.class.isAssignableFrom(projectile) ? net.minecraft.world.item.TridentItem.PROJECTILE_SHOOT_POWER : 3.0F, 1.0F); // ItemBow // Paper - see TridentItem
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            if (LingeringPotion.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.world.entity.projectile.ThrownLingeringPotion(world, this.getHandle(), new net.minecraft.world.item.ItemStack(Items.LINGERING_POTION));
            } else {
                launch = new net.minecraft.world.entity.projectile.ThrownSplashPotion(world, this.getHandle(), new net.minecraft.world.item.ItemStack(Items.SPLASH_POTION));
            }
            ((ThrowableProjectile) launch).shootFromRotation(this.getHandle(), this.getHandle().getXRot(), this.getHandle().getYRot(), -20.0F, 0.5F, 1.0F); // ItemSplashPotion
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new ThrownExperienceBottle(world, this.getHandle(), new net.minecraft.world.item.ItemStack(Items.EXPERIENCE_BOTTLE));
            ((ThrowableProjectile) launch).shootFromRotation(this.getHandle(), this.getHandle().getXRot(), this.getHandle().getYRot(), -20.0F, 0.7F, 1.0F); // ItemExpBottle
        } else if (FishHook.class.isAssignableFrom(projectile) && this.getHandle() instanceof net.minecraft.world.entity.player.Player) {
            launch = new FishingHook((net.minecraft.world.entity.player.Player) this.getHandle(), world, 0, 0);
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            Location location = this.getEyeLocation();
            Vector direction = location.getDirection().multiply(10);
            Vec3 vec = new Vec3(direction.getX(), direction.getY(), direction.getZ());

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.world.entity.projectile.SmallFireball(world, this.getHandle(), vec);
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.world.entity.projectile.WitherSkull(world, this.getHandle(), vec);
            } else if (DragonFireball.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.world.entity.projectile.DragonFireball(world, this.getHandle(), vec);
            } else if (AbstractWindCharge.class.isAssignableFrom(projectile)) {
                if (BreezeWindCharge.class.isAssignableFrom(projectile)) {
                    launch = EntityType.BREEZE_WIND_CHARGE.create(world, EntitySpawnReason.TRIGGERED);
                } else {
                    launch = EntityType.WIND_CHARGE.create(world, EntitySpawnReason.TRIGGERED);
                }

                ((net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge) launch).setOwner(this.getHandle());
                ((net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge) launch).shootFromRotation(this.getHandle(), this.getHandle().getXRot(), this.getHandle().getYRot(), 0.0F, 1.5F, 1.0F); // WindChargeItem
            } else {
                launch = new LargeFireball(world, this.getHandle(), vec, 1);
            }

            launch.projectileSource = this;
            launch.snapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (LlamaSpit.class.isAssignableFrom(projectile)) {
            Location location = this.getEyeLocation();
            Vector direction = location.getDirection();

            launch = EntityType.LLAMA_SPIT.create(world, EntitySpawnReason.TRIGGERED);

            ((net.minecraft.world.entity.projectile.LlamaSpit) launch).setOwner(this.getHandle());
            ((net.minecraft.world.entity.projectile.LlamaSpit) launch).shoot(direction.getX(), direction.getY(), direction.getZ(), 1.5F, 10.0F); // EntityLlama
            launch.snapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (ShulkerBullet.class.isAssignableFrom(projectile)) {
            Location location = this.getEyeLocation();

            launch = new net.minecraft.world.entity.projectile.ShulkerBullet(world, this.getHandle(), null, null);
            launch.snapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (Firework.class.isAssignableFrom(projectile)) {
            Location location = this.getEyeLocation();

            // Paper start - see CrossbowItem
            launch = new FireworkRocketEntity(world, FireworkRocketEntity.getDefaultItem(), this.getHandle(), location.getX(), location.getY() - 0.15F, location.getZ(), true); // Paper - pass correct default to rocket for data storage & see CrossbowItem for regular launch without elytra boost

            // Lifted from net.minecraft.world.item.ProjectileWeaponItem.shoot
            float f2 = /* net.minecraft.world.item.enchantment.EnchantmentHelper.processProjectileSpread((ServerLevel) world, new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.CROSSBOW), this.getHandle(), 0.0F); */ 0; // Just shortcut this to 0, no need to do any calculations on a non existing stack
            int projectileSize = 1;
            int i = 0;

            float f3 = projectileSize == 1 ? 0.0F : 2.0F * f2 / (float) (projectileSize - 1);
            float f4 = (float) ((projectileSize - 1) % 2) * f3 / 2.0F;
            float f5 = 1.0F;
            float yaw = f4 + f5 * (float) ((i + 1) / 2) * f3;

            // Lifted from net.minecraft.world.item.CrossbowItem.shootProjectile
            Vec3 vec3 = this.getHandle().getUpVector(1.0F);
            org.joml.Quaternionf quaternionf = new org.joml.Quaternionf().setAngleAxis((double)(yaw * (float) (Math.PI / 180.0)), vec3.x, vec3.y, vec3.z);
            Vec3 vec32 = this.getHandle().getViewVector(1.0F);
            org.joml.Vector3f vector3f = vec32.toVector3f().rotate(quaternionf);
            ((FireworkRocketEntity) launch).shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), net.minecraft.world.item.CrossbowItem.FIREWORK_POWER, 1.0F);
            // Paper end
        }

        Preconditions.checkArgument(launch != null, "Projectile (%s) not supported", projectile.getName());

        if (velocity != null) {
            launch.getBukkitEntity().setVelocity(velocity);
        }
        if (function != null) {
            function.accept((T) launch.getBukkitEntity());
        }

        world.addFreshEntity(launch);
        return (T) launch.getBukkitEntity();
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        Preconditions.checkState(!this.getHandle().generation, "Cannot check line of sight during world generation");

        return this.getHandle().hasLineOfSight(((CraftEntity) other).getHandle());
    }

    @Override
    public boolean hasLineOfSight(Location loc) {
        if (this.getHandle().level() != ((CraftWorld) loc.getWorld()).getHandle()) {
            return false;
        }

        net.minecraft.world.phys.Vec3 start = new net.minecraft.world.phys.Vec3(this.getHandle().getX(), this.getHandle().getEyeY(), this.getHandle().getZ());
        net.minecraft.world.phys.Vec3 end = new net.minecraft.world.phys.Vec3(loc.getX(), loc.getY(), loc.getZ());
        if (end.distanceToSqr(start) > 128D * 128D) {
            return false; // Return early if the distance is greater than 128 blocks
        }

        return this.getHandle().level().clipDirect(start, end, net.minecraft.world.phys.shapes.CollisionContext.of(this.getHandle())) == net.minecraft.world.phys.HitResult.Type.MISS;
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return this.getHandle() instanceof Mob && !((Mob) this.getHandle()).isPersistenceRequired();
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        if (this.getHandle() instanceof Mob) {
            ((Mob) this.getHandle()).setPersistenceRequired(!remove);
        }
    }

    @Override
    public @NonNull EntityEquipment getEquipment() {
        return this.equipment;
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        if (this.getHandle() instanceof Mob) {
            ((Mob) this.getHandle()).setCanPickUpLoot(pickup);
        } else {
            this.getHandle().bukkitPickUpLoot = pickup;
        }
    }

    @Override
    public boolean getCanPickupItems() {
        if (this.getHandle() instanceof Mob) {
            return this.getHandle().canPickUpLoot();
        } else {
            return this.getHandle().bukkitPickUpLoot;
        }
    }

    @Override
    public boolean isLeashed() {
        return false; // Paper - implement in CraftMob & PaperLeashable
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        throw new IllegalStateException("Entity not leashed"); // Paper - implement in CraftMob & PaperLeashable
    }

    @Override
    public boolean setLeashHolder(Entity holder) {
        return false; // Paper - implement in CraftMob & PaperLeashable
    }

    @Override
    public boolean isGliding() {
        return this.getHandle().isFallFlying();
    }

    @Override
    public void setGliding(boolean gliding) {
        this.getHandle().setSharedFlag(7, gliding);
    }

    @Override
    public boolean isSwimming() {
        return this.getHandle().isSwimming();
    }

    @Override
    public void setSwimming(boolean swimming) {
        this.getHandle().setSwimming(swimming);
    }

    @Override
    public boolean isRiptiding() {
        return this.getHandle().isAutoSpinAttack();
    }

    @Override
    public void setRiptiding(boolean riptiding) {
        this.getHandle().setLivingEntityFlag(net.minecraft.world.entity.LivingEntity.LIVING_ENTITY_FLAG_SPIN_ATTACK, riptiding);
    }

    @Override
    public boolean isSleeping() {
        return this.getHandle().isSleeping();
    }

    @Override
    public boolean isClimbing() {
        Preconditions.checkState(!this.getHandle().generation, "Cannot check if climbing during world generation");

        return this.getHandle().onClimbable();
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        return this.getHandle().craftAttributes.getAttribute(attribute);
    }

    @Override
    public void registerAttribute(Attribute attribute) {
        this.getHandle().craftAttributes.registerAttribute(attribute);
    }

    @Override
    public void setAI(boolean ai) {
        if (this.getHandle() instanceof Mob) {
            ((Mob) this.getHandle()).setNoAi(!ai);
        }
    }

    @Override
    public boolean hasAI() {
        return (this.getHandle() instanceof Mob) ? !((Mob) this.getHandle()).isNoAi() : false;
    }

    @Override
    public void attack(Entity target) {
        Preconditions.checkArgument(target != null, "target == null");
        Preconditions.checkState(!this.getHandle().generation, "Cannot attack during world generation");

        if (this.getHandle() instanceof net.minecraft.world.entity.player.Player) {
            ((net.minecraft.world.entity.player.Player) this.getHandle()).attack(((CraftEntity) target).getHandle());
        } else {
            this.getHandle().doHurtTarget((ServerLevel) ((CraftEntity) target).getHandle().level(), ((CraftEntity) target).getHandle());
        }
    }

    @Override
    public void swingMainHand() {
        Preconditions.checkState(!this.getHandle().generation, "Cannot swing hand during world generation");

        this.getHandle().swing(InteractionHand.MAIN_HAND, true);
    }

    @Override
    public void swingOffHand() {
        Preconditions.checkState(!this.getHandle().generation, "Cannot swing hand during world generation");

        this.getHandle().swing(InteractionHand.OFF_HAND, true);
    }

    @Override
    public void playHurtAnimation(float yaw) {
        if (this.getHandle().level() instanceof ServerLevel world) {
            /*
             * Vanilla degrees state that 0 = left, 90 = front, 180 = right, and 270 = behind.
             * This makes no sense. We'll add 90 to it so that 0 = front, clockwise from there.
             */
            float actualYaw = yaw + 90;
            ClientboundHurtAnimationPacket packet = new ClientboundHurtAnimationPacket(this.getEntityId(), actualYaw);

            world.getChunkSource().sendToTrackingPlayersAndSelf(this.getHandle(), packet);
        }
    }

    @Override
    public void setCollidable(boolean collidable) {
        this.getHandle().collides = collidable;
    }

    @Override
    public boolean isCollidable() {
        return this.getHandle().collides;
    }

    @Override
    public Set<UUID> getCollidableExemptions() {
        return this.getHandle().collidableExemptions;
    }

    @Override
    public <T> T getMemory(MemoryKey<T> memoryKey) {
        final Optional<?> memory = this.getHandle().getBrain().getMemoryInternal(CraftMemoryKey.bukkitToMinecraft(memoryKey));
        return memory != null ? (T) memory.map(CraftMemoryMapper::fromNms).orElse(null) : null;
    }

    @Override
    public <T> void setMemory(MemoryKey<T> memoryKey, T t) {
        this.getHandle().getBrain().setMemory(CraftMemoryKey.bukkitToMinecraft(memoryKey), CraftMemoryMapper.toNms(t));
    }

    @Override
    public Sound getHurtSound() {
        SoundEvent sound = this.getHandle().getHurtSound(this.getHandle().damageSources().generic());
        return (sound != null) ? CraftSound.minecraftToBukkit(sound) : null;
    }

    @Override
    public Sound getDeathSound() {
        SoundEvent sound = this.getHandle().getDeathSound();
        return (sound != null) ? CraftSound.minecraftToBukkit(sound) : null;
    }

    @Override
    public Sound getFallDamageSound(int fallHeight) {
        return CraftSound.minecraftToBukkit(this.getHandle().getFallDamageSound(fallHeight));
    }

    @Override
    public Sound getFallDamageSoundSmall() {
        return CraftSound.minecraftToBukkit(this.getHandle().getFallSounds().small());
    }

    @Override
    public Sound getFallDamageSoundBig() {
        return CraftSound.minecraftToBukkit(this.getHandle().getFallSounds().big());
    }

    @Override
    public Sound getDrinkingSound(ItemStack itemStack) {
        return this.getEatingSound(itemStack);
    }

    @Override
    public Sound getEatingSound(ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "itemStack must not be null");

        net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
        Consumable consumable = nms.get(DataComponents.CONSUMABLE);
        SoundEvent soundeffect = SoundEvents.GENERIC_DRINK.value();

        if (consumable != null) {
            if (this.getHandle() instanceof Consumable.OverrideConsumeSound consumable_b) {
                soundeffect = consumable_b.getConsumeSound(nms);
            } else {
                soundeffect = consumable.sound().value();
            }
        }

        return CraftSound.minecraftToBukkit(soundeffect);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return this.getHandle().canBreatheUnderwater();
    }

    @Override
    public EntityCategory getCategory() {
        throw new UnsupportedOperationException("Method no longer applicable. Use Tags instead.");
    }

    @Override
    public float getSidewaysMovement() {
        return this.getHandle().xxa;
    }

    @Override
    public float getForwardsMovement() {
        return this.getHandle().zza;
    }

    @Override
    public float getUpwardsMovement() {
        return this.getHandle().yya;
    }

    @Override
    public void startUsingItem(org.bukkit.inventory.EquipmentSlot hand) {
        Preconditions.checkArgument(hand != null, "hand must not be null");
        this.getHandle().startUsingItem(CraftEquipmentSlot.getHand(hand));
    }

    @Override
    public void completeUsingActiveItem() {
        this.getHandle().completeUsingItem();
    }

    @Override
    public ItemStack getActiveItem() {
        return this.getHandle().getUseItem().asBukkitMirror();
    }

    @Override
    public void clearActiveItem() {
        this.getHandle().stopUsingItem();
    }

    @Override
    public int getActiveItemRemainingTime() {
        return this.getHandle().getUseItemRemainingTicks();
    }

    @Override
    public void setActiveItemRemainingTime(final int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");
        Preconditions.checkArgument(ticks <= this.getHandle().getUseItem().getUseDuration(this.getHandle()), "ticks must be <= item use duration");
        this.getHandle().useItemRemaining = ticks;
    }

    @Override
    public int getActiveItemUsedTime() {
        return this.getHandle().getTicksUsingItem();
    }

    @Override
    public boolean hasActiveItem() {
        return this.getHandle().isUsingItem();
    }

    @Override
    public org.bukkit.inventory.EquipmentSlot getActiveItemHand() {
        return org.bukkit.craftbukkit.CraftEquipmentSlot.getHand(this.getHandle().getUsedItemHand());
    }

    @Override
    public boolean isJumping() {
        return this.getHandle().jumping;
    }

    @Override
    public void setJumping(boolean jumping) {
        this.getHandle().setJumping(jumping);
        if (jumping && this.getHandle() instanceof Mob) {
            // this is needed to actually make a mob jump
            ((Mob) getHandle()).getJumpControl().jump();
        }
    }

    @Override
    public void playPickupItemAnimation(final org.bukkit.entity.Item item, final int quantity) {
        this.getHandle().take(((CraftItem) item).getHandle(), quantity);
    }

    @Override
    public float getHurtDirection() {
        return this.getHandle().getHurtDir();
    }

    @Override
    public void setHurtDirection(final float hurtDirection) {
        throw new UnsupportedOperationException("Cannot set the hurt direction on a non player");
    }

    @Override
    public void knockback(final double strength, final double directionX, final double directionZ) {
        Preconditions.checkArgument(strength > 0, "Knockback strength must be > 0");
        this.getHandle().knockback(strength, directionX, directionZ);
    };

    public void broadcastSlotBreak(final org.bukkit.inventory.EquipmentSlot slot) {
        this.getHandle().level().broadcastEntityEvent(this.getHandle(), net.minecraft.world.entity.LivingEntity.entityEventForEquipmentBreak(org.bukkit.craftbukkit.CraftEquipmentSlot.getNMS(slot)));
    }

    @Override
    public void broadcastSlotBreak(final org.bukkit.inventory.EquipmentSlot slot, final Collection<org.bukkit.entity.Player> players) {
        if (players.isEmpty()) {
            return;
        }
        final net.minecraft.network.protocol.game.ClientboundEntityEventPacket packet = new net.minecraft.network.protocol.game.ClientboundEntityEventPacket(
            this.getHandle(),
            net.minecraft.world.entity.LivingEntity.entityEventForEquipmentBreak(org.bukkit.craftbukkit.CraftEquipmentSlot.getNMS(slot))
        );
        players.forEach(player -> ((CraftPlayer) player).getHandle().connection.send(packet));
    }

    @Override
    public ItemStack damageItemStack(ItemStack stack, final int amount) {
        final net.minecraft.world.item.ItemStack nmsStack;
        if (stack instanceof final CraftItemStack craftItemStack) {
            if (craftItemStack.handle == null || craftItemStack.handle.isEmpty()) {
                return stack;
            }
            nmsStack = craftItemStack.handle;
        } else {
            nmsStack = CraftItemStack.asNMSCopy(stack);
            stack = CraftItemStack.asCraftMirror(nmsStack); // mirror to capture changes in hurt logic & events
        }
        this.damageItemStack0(nmsStack, amount, null);
        return stack;
    }

    @Override
    public void damageItemStack(final org.bukkit.inventory.EquipmentSlot slot, final int amount) {
        final net.minecraft.world.entity.EquipmentSlot nmsSlot = org.bukkit.craftbukkit.CraftEquipmentSlot.getNMS(slot);
        this.damageItemStack0(this.getHandle().getItemBySlot(nmsSlot), amount, nmsSlot);
    }

    private void damageItemStack0(final net.minecraft.world.item.ItemStack nmsStack, final int amount, final net.minecraft.world.entity.EquipmentSlot slot) {
        nmsStack.hurtAndBreak(amount, this.getHandle(), slot, true);
    }

    @org.jetbrains.annotations.NotNull
    @Override
    public net.kyori.adventure.util.TriState getFrictionState() {
        return this.getHandle().frictionState;
    }

    @Override
    public void setFrictionState(@org.jetbrains.annotations.NotNull net.kyori.adventure.util.TriState state) {
        Preconditions.checkArgument(state != null, "state may not be null");
        this.getHandle().frictionState = state;
    }

    @Override
    public float getBodyYaw() {
        return this.getHandle().getVisualRotationYInDegrees();
    }

    @Override
    public void setBodyYaw(final float bodyYaw) {
        this.getHandle().setYBodyRot(bodyYaw);
    }

    @Override
    public boolean canUseEquipmentSlot(org.bukkit.inventory.EquipmentSlot slot) {
        return this.getHandle().canUseSlot(org.bukkit.craftbukkit.CraftEquipmentSlot.getNMS(slot));
    }

    @Override
    public CombatTracker getCombatTracker() {
        return this.getHandle().getCombatTracker().paperCombatTracker;
    }
}
