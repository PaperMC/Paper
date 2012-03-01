package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
// CraftBukkit end

public class EntityWeatherLighting extends EntityWeather {

    private int lifeTicks;
    public long a = 0L;
    private int c;

    // CraftBukkit start
    private CraftWorld cworld;
    public boolean isEffect = false;

    public EntityWeatherLighting(World world, double d0, double d1, double d2) {
        this(world, d0, d1, d2, false);
    }

    public EntityWeatherLighting(World world, double d0, double d1, double d2, boolean isEffect) {
        // CraftBukkit end

        super(world);

        // CraftBukkit start
        this.isEffect = isEffect;
        this.cworld = world.getWorld();
        // CraftBukkit end

        this.setPositionRotation(d0, d1, d2, 0.0F, 0.0F);
        this.lifeTicks = 2;
        this.a = this.random.nextLong();
        this.c = this.random.nextInt(3) + 1;

        // CraftBukkit
        if (!isEffect && world.difficulty >= 2 && world.areChunksLoaded(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2), 10)) {
            int i = MathHelper.floor(d0);
            int j = MathHelper.floor(d1);
            int k = MathHelper.floor(d2);

            if (world.getTypeId(i, j, k) == 0 && Block.FIRE.canPlace(world, i, j, k)) {
                // CraftBukkit start
                BlockIgniteEvent event = new BlockIgniteEvent(this.cworld.getBlockAt(i, j, k), IgniteCause.LIGHTNING, null);
                world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    world.setTypeId(i, j, k, Block.FIRE.id);
                }
                // CraftBukkit end
            }

            for (i = 0; i < 4; ++i) {
                j = MathHelper.floor(d0) + this.random.nextInt(3) - 1;
                k = MathHelper.floor(d1) + this.random.nextInt(3) - 1;
                int l = MathHelper.floor(d2) + this.random.nextInt(3) - 1;

                if (world.getTypeId(j, k, l) == 0 && Block.FIRE.canPlace(world, j, k, l)) {
                    // CraftBukkit start
                    BlockIgniteEvent event = new BlockIgniteEvent(this.cworld.getBlockAt(j, k, l), IgniteCause.LIGHTNING, null);
                    world.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        world.setTypeId(j, k, l, Block.FIRE.id);
                    }
                    // CraftBukkit end
                }
            }
        }
    }

    public void G_() {
        super.G_();
        if (this.lifeTicks == 2) {
            this.world.makeSound(this.locX, this.locY, this.locZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
            this.world.makeSound(this.locX, this.locY, this.locZ, "random.explode", 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
        }

        --this.lifeTicks;
        if (this.lifeTicks < 0) {
            if (this.c == 0) {
                this.die();
            } else if (this.lifeTicks < -this.random.nextInt(10)) {
                --this.c;
                this.lifeTicks = 1;
                this.a = this.random.nextLong();
                // CraftBukkit
                if (!this.isEffect && this.world.areChunksLoaded(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ), 10)) {
                    int i = MathHelper.floor(this.locX);
                    int j = MathHelper.floor(this.locY);
                    int k = MathHelper.floor(this.locZ);

                    if (this.world.getTypeId(i, j, k) == 0 && Block.FIRE.canPlace(this.world, i, j, k)) {
                        // CraftBukkit start
                        BlockIgniteEvent event = new BlockIgniteEvent(this.cworld.getBlockAt(i, j, k), IgniteCause.LIGHTNING, null);
                        this.world.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            this.world.setTypeId(i, j, k, Block.FIRE.id);
                        }
                        // CraftBukkit end
                    }
                }
            }
        }

        if (this.lifeTicks >= 0 && !this.isEffect) { // CraftBukkit
            double d0 = 3.0D;
            List list = this.world.getEntities(this, AxisAlignedBB.b(this.locX - d0, this.locY - d0, this.locZ - d0, this.locX + d0, this.locY + 6.0D + d0, this.locZ + d0));

            for (int l = 0; l < list.size(); ++l) {
                Entity entity = (Entity) list.get(l);

                entity.a(this);
            }

            this.world.n = 2;
        }
    }

    protected void b() {}

    protected void a(NBTTagCompound nbttagcompound) {}

    protected void b(NBTTagCompound nbttagcompound) {}
}
