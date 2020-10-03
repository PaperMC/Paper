package net.minecraft.server;

// CraftBukkit start
import org.bukkit.event.entity.EntityUnleashEvent;
// CraftBukkit end

public abstract class EntityCreature extends EntityInsentient {

    protected EntityCreature(EntityTypes<? extends EntityCreature> entitytypes, World world) {
        super(entitytypes, world);
    }

    public float f(BlockPosition blockposition) {
        return this.a(blockposition, (IWorldReader) this.world);
    }

    public float a(BlockPosition blockposition, IWorldReader iworldreader) {
        return 0.0F;
    }

    @Override
    public boolean a(GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn) {
        return this.a(this.getChunkCoordinates(), (IWorldReader) generatoraccess) >= 0.0F;
    }

    public boolean eI() {
        return !this.getNavigation().m();
    }

    @Override
    protected void eA() {
        super.eA();
        Entity entity = this.getLeashHolder();

        if (entity != null && entity.world == this.world) {
            this.a(entity.getChunkCoordinates(), 5);
            float f = this.g(entity);

            if (this instanceof EntityTameableAnimal && ((EntityTameableAnimal) this).isSitting()) {
                if (f > 10.0F) {
                    this.world.getServer().getPluginManager().callEvent(new EntityUnleashEvent(this.getBukkitEntity(), EntityUnleashEvent.UnleashReason.DISTANCE)); // CraftBukkit
                    this.unleash(true, true);
                }

                return;
            }

            this.x(f);
            if (f > 10.0F) {
                this.world.getServer().getPluginManager().callEvent(new EntityUnleashEvent(this.getBukkitEntity(), EntityUnleashEvent.UnleashReason.DISTANCE)); // CraftBukkit
                this.unleash(true, true);
                this.goalSelector.a(PathfinderGoal.Type.MOVE);
            } else if (f > 6.0F) {
                double d0 = (entity.locX() - this.locX()) / (double) f;
                double d1 = (entity.locY() - this.locY()) / (double) f;
                double d2 = (entity.locZ() - this.locZ()) / (double) f;

                this.setMot(this.getMot().add(Math.copySign(d0 * d0 * 0.4D, d0), Math.copySign(d1 * d1 * 0.4D, d1), Math.copySign(d2 * d2 * 0.4D, d2)));
            } else {
                this.goalSelector.b(PathfinderGoal.Type.MOVE);
                float f1 = 2.0F;
                Vec3D vec3d = (new Vec3D(entity.locX() - this.locX(), entity.locY() - this.locY(), entity.locZ() - this.locZ())).d().a((double) Math.max(f - 2.0F, 0.0F));

                this.getNavigation().a(this.locX() + vec3d.x, this.locY() + vec3d.y, this.locZ() + vec3d.z, this.eJ());
            }
        }

    }

    protected double eJ() {
        return 1.0D;
    }

    protected void x(float f) {}
}
