package net.minecraft.server;

import java.util.List;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class EntitySnowman extends EntityGolem {

    public EntitySnowman(World world) {
        super(world);
        this.texture = "/mob/snowman.png";
        this.b(0.4F, 1.8F);
    }

    public int getMaxHealth() {
        return 4;
    }

    public void d() {
        super.d();
        if (this.target == null && !this.D() && this.world.random.nextInt(100) == 0) {
            List list = this.world.a(EntityMonster.class, AxisAlignedBB.b(this.locX, this.locY, this.locZ, this.locX + 1.0D, this.locY + 1.0D, this.locZ + 1.0D).b(16.0D, 4.0D, 16.0D));

            if (!list.isEmpty()) {
                this.setTarget((Entity) list.get(this.world.random.nextInt(list.size())));
            }
        }

        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locY);
        int k = MathHelper.floor(this.locZ);

        if (this.world.getWorldChunkManager().a(i, j, k) > 1.0F) {
            this.damageEntity(DamageSource.BURN, 1);
        }

        for (i = 0; i < 4; ++i) {
            j = MathHelper.floor(this.locX + (double) ((float) (i % 2 * 2 - 1) * 0.25F));
            k = MathHelper.floor(this.locY);
            int l = MathHelper.floor(this.locZ + (double) ((float) (i / 2 % 2 * 2 - 1) * 0.25F));

            if (this.world.getTypeId(j, k, l) == 0 && this.world.getWorldChunkManager().a(j, k, l) < 0.8F && Block.SNOW.canPlace(this.world, j, k, l)) {
                this.world.setTypeId(j, k, l, Block.SNOW.id);
            }
        }
    }

    protected void a(Entity entity, float f) {
        if (f < 10.0F) {
            double d0 = entity.locX - this.locX;
            double d1 = entity.locZ - this.locZ;

            if (this.attackTicks == 0) {
                EntitySnowball entitysnowball = new EntitySnowball(this.world, this);
                double d2 = entity.locY + (double) entity.x() - 1.100000023841858D - entitysnowball.locY;
                float f1 = MathHelper.a(d0 * d0 + d1 * d1) * 0.2F;

                this.world.makeSound(this, "random.bow", 1.0F, 1.0F / (this.random.nextFloat() * 0.4F + 0.8F));
                this.world.addEntity(entitysnowball);
                entitysnowball.a(d0, d2 + (double) f1, d1, 1.6F, 12.0F);
                this.attackTicks = 10;
            }

            this.yaw = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
            this.e = true;
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    protected int e() {
        return Item.SNOW_BALL.id;
    }

    protected void a(boolean flag, int i) {
        // CraftBukkit start
        List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.random.nextInt(16);

        if (j > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(Item.SNOW_BALL.id, j));
        }

        CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }
}
