package net.minecraft.server;

import java.util.List;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

public class EntityWeatherStorm extends EntityWeather {

    private int b;
    public long a = 0L;
    private int c;
    
    // CraftBukkit start
    private CraftWorld cworld;
    private CraftServer server;
    // CraftBukkit end

    public EntityWeatherStorm(World world, double d0, double d1, double d2) {
        
        super(world);

        // CraftBukkit start
        cworld = ((WorldServer) world).getWorld();
        server = ((WorldServer) world).getServer();
        // CraftBukkit end
        
        this.setPositionRotation(d0, d1, d2, 0.0F, 0.0F);
        this.b = 2;
        this.a = this.random.nextLong();
        this.c = this.random.nextInt(3) + 1;
        if (world.spawnMonsters >= 2 && world.a(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2), 10)) {
            int i = MathHelper.floor(d0);
            int j = MathHelper.floor(d1);
            int k = MathHelper.floor(d2);

            if (world.getTypeId(i, j, k) == 0 && Block.FIRE.canPlace(world, i, j, k)) {
                // CraftBukkit start
                org.bukkit.block.Block theBlock = cworld.getBlockAt(i, j, k);
                BlockIgniteEvent event = new BlockIgniteEvent(theBlock, IgniteCause.LIGHTNING, null);
                ((WorldServer) world).getServer().getPluginManager().callEvent(event);
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
                    org.bukkit.block.Block theBlock = cworld.getBlockAt(j, k, l);
                    BlockIgniteEvent event = new BlockIgniteEvent(theBlock, IgniteCause.LIGHTNING, null);
                    ((WorldServer) world).getServer().getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        world.setTypeId(j, k, l, Block.FIRE.id);
                    }
                    // CraftBukkit end
                }
            }
        }
    }

    public void p_() {
        super.p_();
        if (this.b == 2) {
            this.world.makeSound(this.locX, this.locY, this.locZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
            this.world.makeSound(this.locX, this.locY, this.locZ, "random.explode", 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
        }

        --this.b;
        if (this.b < 0) {
            if (this.c == 0) {
                this.die();
            } else if (this.b < -this.random.nextInt(10)) {
                --this.c;
                this.b = 1;
                this.a = this.random.nextLong();
                if (this.world.a(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ), 10)) {
                    int i = MathHelper.floor(this.locX);
                    int j = MathHelper.floor(this.locY);
                    int k = MathHelper.floor(this.locZ);

                    if (this.world.getTypeId(i, j, k) == 0 && Block.FIRE.canPlace(this.world, i, j, k)) {
                        // CraftBukkit start
                        org.bukkit.block.Block theBlock = cworld.getBlockAt(i, j, k);
                        BlockIgniteEvent event = new BlockIgniteEvent(theBlock, IgniteCause.LIGHTNING, null);
                        ((WorldServer) world).getServer().getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            this.world.setTypeId(i, j, k, Block.FIRE.id);
                        }
                        // CraftBukkit end
                    }
                }
            }
        }

        if (this.b >= 0) {
            double d0 = 3.0D;
            List list = this.world.b((Entity) this, AxisAlignedBB.b(this.locX - d0, this.locY - d0, this.locZ - d0, this.locX + d0, this.locY + 6.0D + d0, this.locZ + d0));

            for (int l = 0; l < list.size(); ++l) {
                Entity entity = (Entity) list.get(l);

                entity.a(this);
            }

            this.world.i = 2;
        }
    }

    protected void b() {}

    protected void a(NBTTagCompound nbttagcompound) {}

    protected void b(NBTTagCompound nbttagcompound) {}
}
