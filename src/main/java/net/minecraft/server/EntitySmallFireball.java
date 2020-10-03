package net.minecraft.server;

public class EntitySmallFireball extends EntityFireballFireball {

    public EntitySmallFireball(EntityTypes<? extends EntitySmallFireball> entitytypes, World world) {
        super(entitytypes, world);
    }

    public EntitySmallFireball(World world, EntityLiving entityliving, double d0, double d1, double d2) {
        super(EntityTypes.SMALL_FIREBALL, entityliving, d0, d1, d2, world);
    }

    public EntitySmallFireball(World world, double d0, double d1, double d2, double d3, double d4, double d5) {
        super(EntityTypes.SMALL_FIREBALL, d0, d1, d2, d3, d4, d5, world);
    }

    @Override
    protected void a(MovingObjectPositionEntity movingobjectpositionentity) {
        super.a(movingobjectpositionentity);
        if (!this.world.isClientSide) {
            Entity entity = movingobjectpositionentity.getEntity();

            if (!entity.isFireProof()) {
                Entity entity1 = this.getShooter();
                int i = entity.getFireTicks();

                entity.setOnFire(5);
                boolean flag = entity.damageEntity(DamageSource.fireball(this, entity1), 5.0F);

                if (!flag) {
                    entity.setFireTicks(i);
                } else if (entity1 instanceof EntityLiving) {
                    this.a((EntityLiving) entity1, entity);
                }
            }

        }
    }

    @Override
    protected void a(MovingObjectPositionBlock movingobjectpositionblock) {
        super.a(movingobjectpositionblock);
        if (!this.world.isClientSide) {
            Entity entity = this.getShooter();

            if (entity == null || !(entity instanceof EntityInsentient) || this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) {
                BlockPosition blockposition = movingobjectpositionblock.getBlockPosition().shift(movingobjectpositionblock.getDirection());

                if (this.world.isEmpty(blockposition)) {
                    this.world.setTypeUpdate(blockposition, BlockFireAbstract.a((IBlockAccess) this.world, blockposition));
                }
            }

        }
    }

    @Override
    protected void a(MovingObjectPosition movingobjectposition) {
        super.a(movingobjectposition);
        if (!this.world.isClientSide) {
            this.die();
        }

    }

    @Override
    public boolean isInteractable() {
        return false;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }
}
