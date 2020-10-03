package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
// CraftBukkit start
import java.util.HashMap;
import java.util.Map;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
// CraftBukkit end

public class EntityPotion extends EntityProjectileThrowable {

    public static final Predicate<EntityLiving> b = EntityLiving::dN;

    public EntityPotion(EntityTypes<? extends EntityPotion> entitytypes, World world) {
        super(entitytypes, world);
    }

    public EntityPotion(World world, EntityLiving entityliving) {
        super(EntityTypes.POTION, entityliving, world);
    }

    public EntityPotion(World world, double d0, double d1, double d2) {
        super(EntityTypes.POTION, d0, d1, d2, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SPLASH_POTION;
    }

    @Override
    protected float k() {
        return 0.05F;
    }

    @Override
    protected void a(MovingObjectPositionBlock movingobjectpositionblock) {
        super.a(movingobjectpositionblock);
        if (!this.world.isClientSide) {
            ItemStack itemstack = this.g();
            PotionRegistry potionregistry = PotionUtil.d(itemstack);
            List<MobEffect> list = PotionUtil.getEffects(itemstack);
            boolean flag = potionregistry == Potions.WATER && list.isEmpty();
            EnumDirection enumdirection = movingobjectpositionblock.getDirection();
            BlockPosition blockposition = movingobjectpositionblock.getBlockPosition();
            BlockPosition blockposition1 = blockposition.shift(enumdirection);

            if (flag) {
                this.a(blockposition1, enumdirection);
                this.a(blockposition1.shift(enumdirection.opposite()), enumdirection);
                Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

                while (iterator.hasNext()) {
                    EnumDirection enumdirection1 = (EnumDirection) iterator.next();

                    this.a(blockposition1.shift(enumdirection1), enumdirection1);
                }
            }

        }
    }

    @Override
    protected void a(MovingObjectPosition movingobjectposition) {
        super.a(movingobjectposition);
        if (!this.world.isClientSide) {
            ItemStack itemstack = this.g();
            PotionRegistry potionregistry = PotionUtil.d(itemstack);
            List<MobEffect> list = PotionUtil.getEffects(itemstack);
            boolean flag = potionregistry == Potions.WATER && list.isEmpty();

            if (flag) {
                this.splash();
            } else if (true || !list.isEmpty()) { // CraftBukkit - Call event even if no effects to apply
                if (this.isLingering()) {
                    this.a(itemstack, potionregistry);
                } else {
                    this.a(list, movingobjectposition.getType() == MovingObjectPosition.EnumMovingObjectType.ENTITY ? ((MovingObjectPositionEntity) movingobjectposition).getEntity() : null);
                }
            }

            int i = potionregistry.b() ? 2007 : 2002;

            this.world.triggerEffect(i, this.getChunkCoordinates(), PotionUtil.c(itemstack));
            this.die();
        }
    }

    private void splash() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List<EntityLiving> list = this.world.a(EntityLiving.class, axisalignedbb, EntityPotion.b);

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityLiving entityliving = (EntityLiving) iterator.next();
                double d0 = this.h(entityliving);

                if (d0 < 16.0D && entityliving.dN()) {
                    entityliving.damageEntity(DamageSource.c(entityliving, this.getShooter()), 1.0F);
                }
            }
        }

    }

    private void a(List<MobEffect> list, @Nullable Entity entity) {
        AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List<EntityLiving> list1 = this.world.a(EntityLiving.class, axisalignedbb);
        Map<LivingEntity, Double> affected = new HashMap<LivingEntity, Double>(); // CraftBukkit

        if (!list1.isEmpty()) {
            Iterator iterator = list1.iterator();

            while (iterator.hasNext()) {
                EntityLiving entityliving = (EntityLiving) iterator.next();

                if (entityliving.eg()) {
                    double d0 = this.h(entityliving);

                    if (d0 < 16.0D) {
                        double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                        if (entityliving == entity) {
                            d1 = 1.0D;
                        }

                        // CraftBukkit start
                        affected.put((LivingEntity) entityliving.getBukkitEntity(), d1);
                    }
                }
            }
        }

        org.bukkit.event.entity.PotionSplashEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPotionSplashEvent(this, affected);
        if (!event.isCancelled() && list != null && !list.isEmpty()) { // do not process effects if there are no effects to process
            for (LivingEntity victim : event.getAffectedEntities()) {
                if (!(victim instanceof CraftLivingEntity)) {
                    continue;
                }

                EntityLiving entityliving = ((CraftLivingEntity) victim).getHandle();
                double d1 = event.getIntensity(victim);
                // CraftBukkit end

                Iterator iterator1 = list.iterator();

                while (iterator1.hasNext()) {
                    MobEffect mobeffect = (MobEffect) iterator1.next();
                    MobEffectList mobeffectlist = mobeffect.getMobEffect();
                    // CraftBukkit start - Abide by PVP settings - for players only!
                    if (!this.world.pvpMode && this.getShooter() instanceof EntityPlayer && entityliving instanceof EntityPlayer && entityliving != this.getShooter()) {
                        int i = MobEffectList.getId(mobeffectlist);
                        // Block SLOWER_MOVEMENT, SLOWER_DIG, HARM, BLINDNESS, HUNGER, WEAKNESS and POISON potions
                        if (i == 2 || i == 4 || i == 7 || i == 15 || i == 17 || i == 18 || i == 19) {
                            continue;
                        }
                    }
                    // CraftBukkit end

                    if (mobeffectlist.isInstant()) {
                        mobeffectlist.applyInstantEffect(this, this.getShooter(), entityliving, mobeffect.getAmplifier(), d1);
                    } else {
                        int i = (int) (d1 * (double) mobeffect.getDuration() + 0.5D);

                        if (i > 20) {
                            entityliving.addEffect(new MobEffect(mobeffectlist, i, mobeffect.getAmplifier(), mobeffect.isAmbient(), mobeffect.isShowParticles()), org.bukkit.event.entity.EntityPotionEffectEvent.Cause.POTION_SPLASH); // CraftBukkit
                        }
                    }
                }
            }
        }

    }

    private void a(ItemStack itemstack, PotionRegistry potionregistry) {
        EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.locX(), this.locY(), this.locZ());
        Entity entity = this.getShooter();

        if (entity instanceof EntityLiving) {
            entityareaeffectcloud.setSource((EntityLiving) entity);
        }

        entityareaeffectcloud.setRadius(3.0F);
        entityareaeffectcloud.setRadiusOnUse(-0.5F);
        entityareaeffectcloud.setWaitTime(10);
        entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float) entityareaeffectcloud.getDuration());
        entityareaeffectcloud.a(potionregistry);
        Iterator iterator = PotionUtil.b(itemstack).iterator();

        while (iterator.hasNext()) {
            MobEffect mobeffect = (MobEffect) iterator.next();

            entityareaeffectcloud.addEffect(new MobEffect(mobeffect));
        }

        NBTTagCompound nbttagcompound = itemstack.getTag();

        if (nbttagcompound != null && nbttagcompound.hasKeyOfType("CustomPotionColor", 99)) {
            entityareaeffectcloud.setColor(nbttagcompound.getInt("CustomPotionColor"));
        }

        // CraftBukkit start
        org.bukkit.event.entity.LingeringPotionSplashEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callLingeringPotionSplashEvent(this, entityareaeffectcloud);
        if (!(event.isCancelled() || entityareaeffectcloud.dead)) {
            this.world.addEntity(entityareaeffectcloud);
        } else {
            entityareaeffectcloud.dead = true;
        }
        // CraftBukkit end
    }

    public boolean isLingering() {
        return this.g().getItem() == Items.LINGERING_POTION;
    }

    private void a(BlockPosition blockposition, EnumDirection enumdirection) {
        IBlockData iblockdata = this.world.getType(blockposition);

        if (iblockdata.a((Tag) TagsBlock.FIRE)) {
            // CraftBukkit start
            if (!CraftEventFactory.callEntityChangeBlockEvent(this, blockposition, Blocks.AIR.getBlockData()).isCancelled()) {
                this.world.a(blockposition, false);
            }
            // CraftBukkit end
        } else if (BlockCampfire.g(iblockdata)) {
            // CraftBukkit start
            if (!CraftEventFactory.callEntityChangeBlockEvent(this, blockposition, iblockdata.set(BlockCampfire.b, false)).isCancelled()) { // PAIL rename LIT
                this.world.a((EntityHuman) null, 1009, blockposition, 0);
                BlockCampfire.c((GeneratorAccess) this.world, blockposition, iblockdata);
                this.world.setTypeUpdate(blockposition, (IBlockData) iblockdata.set(BlockCampfire.b, false));
            }
            // CraftBukkit end
        }

    }
}
