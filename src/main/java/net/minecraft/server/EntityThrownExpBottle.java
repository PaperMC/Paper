package net.minecraft.server;

public class EntityThrownExpBottle extends EntityProjectileThrowable {

    public EntityThrownExpBottle(EntityTypes<? extends EntityThrownExpBottle> entitytypes, World world) {
        super(entitytypes, world);
    }

    public EntityThrownExpBottle(World world, EntityLiving entityliving) {
        super(EntityTypes.EXPERIENCE_BOTTLE, entityliving, world);
    }

    public EntityThrownExpBottle(World world, double d0, double d1, double d2) {
        super(EntityTypes.EXPERIENCE_BOTTLE, d0, d1, d2, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.EXPERIENCE_BOTTLE;
    }

    @Override
    protected float k() {
        return 0.07F;
    }

    @Override
    protected void a(MovingObjectPosition movingobjectposition) {
        super.a(movingobjectposition);
        if (!this.world.isClientSide) {
            // CraftBukkit - moved to after event
            // this.world.triggerEffect(2002, this.getChunkCoordinates(), PotionUtil.a(Potions.WATER));
            int i = 3 + this.world.random.nextInt(5) + this.world.random.nextInt(5);

            // CraftBukkit start
            org.bukkit.event.entity.ExpBottleEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callExpBottleEvent(this, i);
            i = event.getExperience();
            if (event.getShowEffect()) {
                this.world.triggerEffect(2002, this.getChunkCoordinates(), PotionUtil.a(Potions.WATER));
            }
            // CraftBukkit end

            while (i > 0) {
                int j = EntityExperienceOrb.getOrbValue(i);

                i -= j;
                this.world.addEntity(new EntityExperienceOrb(this.world, this.locX(), this.locY(), this.locZ(), j));
            }

            this.die();
        }

    }
}
