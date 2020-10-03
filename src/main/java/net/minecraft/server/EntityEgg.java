package net.minecraft.server;

// CraftBukkit start
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEggThrowEvent;
// CraftBukkit end

public class EntityEgg extends EntityProjectileThrowable {

    public EntityEgg(EntityTypes<? extends EntityEgg> entitytypes, World world) {
        super(entitytypes, world);
    }

    public EntityEgg(World world, EntityLiving entityliving) {
        super(EntityTypes.EGG, entityliving, world);
    }

    public EntityEgg(World world, double d0, double d1, double d2) {
        super(EntityTypes.EGG, d0, d1, d2, world);
    }

    @Override
    protected void a(MovingObjectPositionEntity movingobjectpositionentity) {
        super.a(movingobjectpositionentity);
        movingobjectpositionentity.getEntity().damageEntity(DamageSource.projectile(this, this.getShooter()), 0.0F);
    }

    @Override
    protected void a(MovingObjectPosition movingobjectposition) {
        super.a(movingobjectposition);
        if (!this.world.isClientSide) {
            boolean hatching = this.random.nextInt(8) == 0; // CraftBukkit
            if (true) {
                byte b0 = 1;

                if (this.random.nextInt(32) == 0) {
                    b0 = 4;
                }

                // CraftBukkit start
                if (!hatching) {
                    b0 = 0;
                }
                EntityType hatchingType = EntityType.CHICKEN;

                Entity shooter = this.getShooter();
                if (shooter instanceof EntityPlayer) {
                    PlayerEggThrowEvent event = new PlayerEggThrowEvent((Player) shooter.getBukkitEntity(), (org.bukkit.entity.Egg) this.getBukkitEntity(), hatching, b0, hatchingType);
                    this.world.getServer().getPluginManager().callEvent(event);

                    b0 = event.getNumHatches();
                    hatching = event.isHatching();
                    hatchingType = event.getHatchingType();
                }

                if (hatching) {
                    for (int i = 0; i < b0; ++i) {
                        Entity entity = world.getWorld().createEntity(new org.bukkit.Location(world.getWorld(), this.locX(), this.locY(), this.locZ(), this.yaw, 0.0F), hatchingType.getEntityClass());
                        if (entity.getBukkitEntity() instanceof Ageable) {
                            ((Ageable) entity.getBukkitEntity()).setBaby();
                        }
                        world.getWorld().addEntity(entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.EGG);
                    }
                }
                // CraftBukkit end
            }

            this.world.broadcastEntityEffect(this, (byte) 3);
            this.die();
        }

    }

    @Override
    protected Item getDefaultItem() {
        return Items.EGG;
    }
}
