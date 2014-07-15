package net.minecraft.server;

import java.util.List;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class EntityLightning extends EntityWeather {

    private int lifeTicks;
    public long a;
    private int c;

    // CraftBukkit start
    public boolean isEffect = false;

    public EntityLightning(World world, double d0, double d1, double d2) {
        this(world, d0, d1, d2, false);
    }

    public EntityLightning(World world, double d0, double d1, double d2, boolean isEffect) {
        // CraftBukkit end

        super(world);

        // CraftBukkit - Set isEffect
        this.isEffect = isEffect;

        this.setPositionRotation(d0, d1, d2, 0.0F, 0.0F);
        this.lifeTicks = 2;
        this.a = this.random.nextLong();
        this.c = this.random.nextInt(3) + 1;

        // CraftBukkit - add "!isEffect"
        if (!isEffect && !world.isStatic && world.getGameRules().getBoolean("doFireTick") && (world.difficulty == EnumDifficulty.NORMAL || world.difficulty == EnumDifficulty.HARD) && world.areChunksLoaded(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2), 10)) {
            int i = MathHelper.floor(d0);
            int j = MathHelper.floor(d1);
            int k = MathHelper.floor(d2);

            if (world.getType(i, j, k).getMaterial() == Material.AIR && Blocks.FIRE.canPlace(world, i, j, k)) {
                // CraftBukkit start
                if (!CraftEventFactory.callBlockIgniteEvent(world, i, j, k, this).isCancelled()) {
                    world.setTypeUpdate(i, j, k, Blocks.FIRE);
                }
                // CraftBukkit end
            }

            for (i = 0; i < 4; ++i) {
                j = MathHelper.floor(d0) + this.random.nextInt(3) - 1;
                k = MathHelper.floor(d1) + this.random.nextInt(3) - 1;
                int l = MathHelper.floor(d2) + this.random.nextInt(3) - 1;

                if (world.getType(j, k, l).getMaterial() == Material.AIR && Blocks.FIRE.canPlace(world, j, k, l)) {
                    // CraftBukkit start
                    if (!CraftEventFactory.callBlockIgniteEvent(world, j, k, l, this).isCancelled()) {
                        world.setTypeUpdate(j, k, l, Blocks.FIRE);
                    }
                    // CraftBukkit end
                }
            }
        }
    }

    public void h() {
        super.h();
        if (this.lifeTicks == 2) {
            // CraftBukkit start - Use relative location for far away sounds
            //this.world.makeSound(this.locX, this.locY, this.locZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
            float pitch = 0.8F + this.random.nextFloat() * 0.2F;
            int viewDistance = ((WorldServer) this.world).getServer().getViewDistance() * 16;
            for (EntityPlayer player : (List<EntityPlayer>) this.world.players) {
                double deltaX = this.locX - player.locX;
                double deltaZ = this.locZ - player.locZ;
                double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                if (distanceSquared > viewDistance * viewDistance) {
                    double deltaLength = Math.sqrt(distanceSquared);
                    double relativeX = player.locX + (deltaX / deltaLength) * viewDistance;
                    double relativeZ = player.locZ + (deltaZ / deltaLength) * viewDistance;
                    player.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("ambient.weather.thunder", relativeX, this.locY, relativeZ, 10000.0F, pitch));
                } else {
                    player.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("ambient.weather.thunder", this.locX, this.locY, this.locZ, 10000.0F, pitch));
                }
            }
            // CraftBukkit end
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
                // CraftBukkit - add "!isEffect"
                if (!isEffect && !this.world.isStatic && this.world.getGameRules().getBoolean("doFireTick") && this.world.areChunksLoaded(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ), 10)) {
                    int i = MathHelper.floor(this.locX);
                    int j = MathHelper.floor(this.locY);
                    int k = MathHelper.floor(this.locZ);

                    if (this.world.getType(i, j, k).getMaterial() == Material.AIR && Blocks.FIRE.canPlace(this.world, i, j, k)) {
                        // CraftBukkit start
                        if (!CraftEventFactory.callBlockIgniteEvent(world, i, j, k, this).isCancelled()) {
                            this.world.setTypeUpdate(i, j, k, Blocks.FIRE);
                        }
                        // CraftBukkit end
                    }
                }
            }
        }

        if (this.lifeTicks >= 0 && !this.isEffect) { // CraftBukkit - add !this.isEffect
            if (this.world.isStatic) {
                this.world.q = 2;
            } else {
                double d0 = 3.0D;
                List list = this.world.getEntities(this, AxisAlignedBB.a(this.locX - d0, this.locY - d0, this.locZ - d0, this.locX + d0, this.locY + 6.0D + d0, this.locZ + d0));

                for (int l = 0; l < list.size(); ++l) {
                    Entity entity = (Entity) list.get(l);

                    entity.a(this);
                }
            }
        }
    }

    protected void c() {}

    protected void a(NBTTagCompound nbttagcompound) {}

    protected void b(NBTTagCompound nbttagcompound) {}
}
