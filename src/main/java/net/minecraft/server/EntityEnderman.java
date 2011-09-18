package net.minecraft.server;

// Craftbukkit start
import org.bukkit.Location;
import org.bukkit.event.entity.EndermanPickupEvent;
import org.bukkit.event.entity.EndermanPlaceEvent;
// Craftbukkit end

public class EntityEnderman extends EntityMonster {

    private static boolean[] b = new boolean[256];
    public boolean a = false;
    private int g = 0;
    private int h = 0;

    public EntityEnderman(World world) {
        super(world);
        this.texture = "/mob/enderman.png";
        this.aU = 0.2F;
        this.damage = 5;
        this.b(0.6F, 2.9F);
        this.bI = 1.0F;
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, new Byte((byte) 0));
        this.datawatcher.a(17, new Byte((byte) 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("carried", (short) this.getCarriedId());
        nbttagcompound.a("carriedData", (short) this.getCarriedData());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setCarriedId(nbttagcompound.d("carried"));
        this.setCarriedData(nbttagcompound.d("carryingData"));
    }

    protected Entity findTarget() {
        EntityHuman entityhuman = this.world.findNearbyPlayer(this, 64.0D);

        if (entityhuman != null) {
            if (this.c(entityhuman)) {
                if (this.h++ == 5) {
                    this.h = 0;
                    return entityhuman;
                }
            } else {
                this.h = 0;
            }
        }

        return null;
    }

    public float a_(float f) {
        return super.a_(f);
    }

    private boolean c(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.armor[3];

        if (itemstack != null && itemstack.id == Block.PUMPKIN.id) {
            return false;
        } else {
            Vec3D vec3d = entityhuman.c(1.0F).b();
            Vec3D vec3d1 = Vec3D.create(this.locX - entityhuman.locX, this.boundingBox.b + (double) (this.width / 2.0F) - entityhuman.locY + (double) entityhuman.t(), this.locZ - entityhuman.locZ);
            double d0 = vec3d1.c();

            vec3d1 = vec3d1.b();
            double d1 = vec3d.a(vec3d1);

            return d1 > 1.0D - 0.025D / d0 ? entityhuman.f(this) : false;
        }
    }

    public void s() {
        if (this.an()) {
            this.damageEntity(DamageSource.DROWN, 1);
        }

        this.a = this.target != null;
        this.aU = this.target != null ? 4.5F : 0.3F;
        int i;

        if (!this.world.isStatic) {
            int j;
            int k;
            int l;

            if (this.getCarriedId() == 0) {
                if (this.random.nextInt(20) == 0) {
                    i = MathHelper.floor(this.locX - 2.0D + this.random.nextDouble() * 4.0D);
                    j = MathHelper.floor(this.locY + this.random.nextDouble() * 3.0D);
                    k = MathHelper.floor(this.locZ - 2.0D + this.random.nextDouble() * 4.0D);
                    l = this.world.getTypeId(i, j, k);
                    if (b[l]) {
                        // CraftBukkit start - pickup event
                        EndermanPickupEvent pickup = new EndermanPickupEvent(this.getBukkitEntity(), this.world.getWorld().getBlockAt(i, j, k));
                        this.world.getServer().getPluginManager().callEvent(pickup);
                        if (!pickup.isCancelled()) {
                            this.setCarriedId(this.world.getTypeId(i, j, k));
                            this.setCarriedData(this.world.getData(i, j, k));
                            this.world.setTypeId(i, j, k, 0);
                        }
                        // CraftBukkit end
                    }
                }
            } else if (this.random.nextInt(2000) == 0) {
                i = MathHelper.floor(this.locX - 1.0D + this.random.nextDouble() * 2.0D);
                j = MathHelper.floor(this.locY + this.random.nextDouble() * 2.0D);
                k = MathHelper.floor(this.locZ - 1.0D + this.random.nextDouble() * 2.0D);
                l = this.world.getTypeId(i, j, k);
                int i1 = this.world.getTypeId(i, j - 1, k);

                if (l == 0 && i1 > 0 && Block.byId[i1].b()) {
                    // CraftBukkit start - place event
                    EndermanPlaceEvent place = new EndermanPlaceEvent(this.getBukkitEntity(), new Location(this.world.getWorld(), i, j, k));
                    this.world.getServer().getPluginManager().callEvent(place);
                    if (!place.isCancelled()) {
                        this.world.setTypeIdAndData(i, j, k, this.getCarriedId(), this.getCarriedData());
                        this.setCarriedId(0);
                    }
                    // CraftBukkit end
                }
            }
        }

        for (i = 0; i < 2; ++i) {
            this.world.a("portal", this.locX + (this.random.nextDouble() - 0.5D) * (double) this.length, this.locY + this.random.nextDouble() * (double) this.width - 0.25D, this.locZ + (this.random.nextDouble() - 0.5D) * (double) this.length, (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
        }

        if (this.world.d() && !this.world.isStatic) {
            float f = this.a_(1.0F);

            if (f > 0.5F && this.world.isChunkLoaded(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.fireTicks = 300;
            }
        }

        this.aS = false;
        if (this.target != null) {
            this.a(this.target, 100.0F, 100.0F);
        }

        if (!this.world.isStatic) {
            if (this.target != null) {
                if (this.target instanceof EntityHuman && this.c((EntityHuman) this.target)) {
                    this.aP = this.aQ = 0.0F;
                    this.aU = 0.0F;
                    if (this.target.h(this) < 16.0D) {
                        this.w();
                    }

                    this.g = 0;
                } else if (this.target.h(this) > 256.0D && this.g++ >= 30 && this.e(this.target)) {
                    this.g = 0;
                }
            } else {
                this.g = 0;
            }
        }

        super.s();
    }

    protected boolean w() {
        double d0 = this.locX + (this.random.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.locY + (double) (this.random.nextInt(64) - 32);
        double d2 = this.locZ + (this.random.nextDouble() - 0.5D) * 64.0D;

        return this.a(d0, d1, d2);
    }

    protected boolean e(Entity entity) {
        Vec3D vec3d = Vec3D.create(this.locX - entity.locX, this.boundingBox.b + (double) (this.width / 2.0F) - entity.locY + (double) entity.t(), this.locZ - entity.locZ);

        vec3d = vec3d.b();
        double d0 = 16.0D;
        double d1 = this.locX + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.a * d0;
        double d2 = this.locY + (double) (this.random.nextInt(16) - 8) - vec3d.b * d0;
        double d3 = this.locZ + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.c * d0;

        return this.a(d1, d2, d3);
    }

    protected boolean a(double d0, double d1, double d2) {
        double d3 = this.locX;
        double d4 = this.locY;
        double d5 = this.locZ;

        this.locX = d0;
        this.locY = d1;
        this.locZ = d2;
        boolean flag = false;
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locY);
        int k = MathHelper.floor(this.locZ);
        int l;

        if (this.world.isLoaded(i, j, k)) {
            boolean flag1 = false;

            while (!flag1 && j > 0) {
                l = this.world.getTypeId(i, j - 1, k);
                if (l != 0 && Block.byId[l].material.isSolid()) {
                    flag1 = true;
                } else {
                    --this.locY;
                    --j;
                }
            }

            if (flag1) {
                this.setPosition(this.locX, this.locY, this.locZ);
                if (this.world.getEntities(this, this.boundingBox).size() == 0 && !this.world.c(this.boundingBox)) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            this.setPosition(d3, d4, d5);
            return false;
        } else {
            short short1 = 128;

            for (l = 0; l < short1; ++l) {
                double d6 = (double) l / ((double) short1 - 1.0D);
                float f = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
                double d7 = d3 + (this.locX - d3) * d6 + (this.random.nextDouble() - 0.5D) * (double) this.length * 2.0D;
                double d8 = d4 + (this.locY - d4) * d6 + this.random.nextDouble() * (double) this.width;
                double d9 = d5 + (this.locZ - d5) * d6 + (this.random.nextDouble() - 0.5D) * (double) this.length * 2.0D;

                this.world.a("portal", d7, d8, d9, (double) f, (double) f1, (double) f2);
            }

            return true;
        }
    }

    protected String h() {
        return "mob.zombie";
    }

    protected String i() {
        return "mob.zombiehurt";
    }

    protected String j() {
        return "mob.zombiedeath";
    }

    protected int k() {
        return Item.ENDER_PEARL.id;
    }

    protected void a(boolean flag) {
        int i = this.k();

        if (i > 0) {
            int j = this.random.nextInt(2);

            for (int k = 0; k < j; ++k) {
                this.b(i, 1);
            }
        }
    }

    public void setCarriedId(int i) {
        this.datawatcher.watch(16, Byte.valueOf((byte) (i & 255)));
    }

    public int getCarriedId() {
        return this.datawatcher.a(16);
    }

    public void setCarriedData(int i) {
        this.datawatcher.watch(17, Byte.valueOf((byte) (i & 255)));
    }

    public int getCarriedData() {
        return this.datawatcher.a(17);
    }

    static {
        b[Block.STONE.id] = true;
        b[Block.GRASS.id] = true;
        b[Block.DIRT.id] = true;
        b[Block.COBBLESTONE.id] = true;
        b[Block.WOOD.id] = true;
        b[Block.SAND.id] = true;
        b[Block.GRAVEL.id] = true;
        b[Block.GOLD_ORE.id] = true;
        b[Block.IRON_ORE.id] = true;
        b[Block.COAL_ORE.id] = true;
        b[Block.LOG.id] = true;
        b[Block.LEAVES.id] = true;
        b[Block.SPONGE.id] = true;
        b[Block.GLASS.id] = true;
        b[Block.LAPIS_ORE.id] = true;
        b[Block.LAPIS_BLOCK.id] = true;
        b[Block.SANDSTONE.id] = true;
        b[Block.WOOL.id] = true;
        b[Block.YELLOW_FLOWER.id] = true;
        b[Block.RED_ROSE.id] = true;
        b[Block.BROWN_MUSHROOM.id] = true;
        b[Block.RED_MUSHROOM.id] = true;
        b[Block.GOLD_BLOCK.id] = true;
        b[Block.IRON_BLOCK.id] = true;
        b[Block.BRICK.id] = true;
        b[Block.TNT.id] = true;
        b[Block.BOOKSHELF.id] = true;
        b[Block.MOSSY_COBBLESTONE.id] = true;
        b[Block.DIAMOND_ORE.id] = true;
        b[Block.DIAMOND_BLOCK.id] = true;
        b[Block.WORKBENCH.id] = true;
        b[Block.REDSTONE_ORE.id] = true;
        b[Block.GLOWING_REDSTONE_ORE.id] = true;
        b[Block.ICE.id] = true;
        b[Block.CACTUS.id] = true;
        b[Block.CLAY.id] = true;
        b[Block.PUMPKIN.id] = true;
        b[Block.NETHERRACK.id] = true;
        b[Block.SOUL_SAND.id] = true;
        b[Block.GLOWSTONE.id] = true;
        b[Block.JACK_O_LANTERN.id] = true;
        b[Block.SMOOTH_BRICK.id] = true;
        b[Block.BIG_MUSHROOM_1.id] = true;
        b[Block.BIG_MUSHROOM_2.id] = true;
        b[Block.MELON.id] = true;
    }
}
