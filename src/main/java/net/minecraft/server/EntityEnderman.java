package net.minecraft.server;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTeleportEvent;
// CraftBukkit end

public class EntityEnderman extends EntityMonster {

    private static boolean[] d = new boolean[256];
    private int e = 0;
    private int g = 0;

    public EntityEnderman(World world) {
        super(world);
        this.texture = "/mob/enderman.png";
        this.bw = 0.2F;
        this.damage = 7;
        this.a(0.6F, 2.9F);
        this.W = 1.0F;
    }

    public int getMaxHealth() {
        return 40;
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, new Byte((byte) 0));
        this.datawatcher.a(17, new Byte((byte) 0));
        this.datawatcher.a(18, new Byte((byte) 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setShort("carried", (short) this.getCarriedId());
        nbttagcompound.setShort("carriedData", (short) this.getCarriedData());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setCarriedId(nbttagcompound.getShort("carried"));
        this.setCarriedData(nbttagcompound.getShort("carriedData"));
    }

    protected Entity findTarget() {
        EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, 64.0D);

        if (entityhuman != null) {
            if (this.d(entityhuman)) {
                if (this.g++ == 5) {
                    this.g = 0;
                    this.e(true);
                    return entityhuman;
                }
            } else {
                this.g = 0;
            }
        }

        return null;
    }

    private boolean d(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.armor[3];

        if (itemstack != null && itemstack.id == Block.PUMPKIN.id) {
            return false;
        } else {
            Vec3D vec3d = entityhuman.i(1.0F).b();
            Vec3D vec3d1 = Vec3D.a().create(this.locX - entityhuman.locX, this.boundingBox.b + (double) (this.length / 2.0F) - (entityhuman.locY + (double) entityhuman.getHeadHeight()), this.locZ - entityhuman.locZ);
            double d0 = vec3d1.c();

            vec3d1 = vec3d1.b();
            double d1 = vec3d.b(vec3d1);

            return d1 > 1.0D - 0.025D / d0 ? entityhuman.l(this) : false;
        }
    }

    public void d() {
        if (this.G()) {
            this.damageEntity(DamageSource.DROWN, 1);
        }

        this.bw = this.target != null ? 6.5F : 0.3F;
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
                    if (d[l]) {
                        // CraftBukkit start - pickup event
                        if (!CraftEventFactory.callEntityChangeBlockEvent(this, this.world.getWorld().getBlockAt(i, j, k), org.bukkit.Material.AIR).isCancelled()) {
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

                if (l == 0 && i1 > 0 && Block.byId[i1].c()) {
                    // CraftBukkit start - place event
                    org.bukkit.block.Block bblock = this.world.getWorld().getBlockAt(i, j, k);

                    if (!CraftEventFactory.callEntityChangeBlockEvent(this, bblock, bblock.getType()).isCancelled()) {
                        this.world.setTypeIdAndData(i, j, k, this.getCarriedId(), this.getCarriedData());
                        this.setCarriedId(0);
                    }
                    // CraftBukkit end
                }
            }
        }

        for (i = 0; i < 2; ++i) {
            this.world.a("portal", this.locX + (this.random.nextDouble() - 0.5D) * (double) this.width, this.locY + this.random.nextDouble() * (double) this.length - 0.25D, this.locZ + (this.random.nextDouble() - 0.5D) * (double) this.width, (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
        }

        if (this.world.s() && !this.world.isStatic) {
            float f = this.c(1.0F);

            if (f > 0.5F && this.world.j(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.target = null;
                this.e(false);
                this.n();
            }
        }

        if (this.G()) {
            this.target = null;
            this.e(false);
            this.n();
        }

        this.bu = false;
        if (this.target != null) {
            this.a(this.target, 100.0F, 100.0F);
        }

        if (!this.world.isStatic && this.isAlive()) {
            if (this.target != null) {
                if (this.target instanceof EntityHuman && this.d((EntityHuman) this.target)) {
                    this.br = this.bs = 0.0F;
                    this.bw = 0.0F;
                    if (this.target.e((Entity) this) < 16.0D) {
                        this.n();
                    }

                    this.e = 0;
                } else if (this.target.e((Entity) this) > 256.0D && this.e++ >= 30 && this.c(this.target)) {
                    this.e = 0;
                }
            } else {
                this.e(false);
                this.e = 0;
            }
        }

        super.d();
    }

    protected boolean n() {
        double d0 = this.locX + (this.random.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.locY + (double) (this.random.nextInt(64) - 32);
        double d2 = this.locZ + (this.random.nextDouble() - 0.5D) * 64.0D;

        return this.j(d0, d1, d2);
    }

    protected boolean c(Entity entity) {
        Vec3D vec3d = Vec3D.a().create(this.locX - entity.locX, this.boundingBox.b + (double) (this.length / 2.0F) - entity.locY + (double) entity.getHeadHeight(), this.locZ - entity.locZ);

        vec3d = vec3d.b();
        double d0 = 16.0D;
        double d1 = this.locX + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.a * d0;
        double d2 = this.locY + (double) (this.random.nextInt(16) - 8) - vec3d.b * d0;
        double d3 = this.locZ + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.c * d0;

        return this.j(d1, d2, d3);
    }

    protected boolean j(double d0, double d1, double d2) {
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
                // CraftBukkit start - teleport event
                EntityTeleportEvent teleport = new EntityTeleportEvent(this.getBukkitEntity(), new Location(this.world.getWorld(), d3, d4, d5), new Location(this.world.getWorld(), this.locX, this.locY, this.locZ));
                this.world.getServer().getPluginManager().callEvent(teleport);
                if (teleport.isCancelled()) {
                    return false;
                }

                Location to = teleport.getTo();
                this.setPosition(to.getX(), to.getY(), to.getZ());
                // CraftBukkit end

                if (this.world.getCubes(this, this.boundingBox).isEmpty() && !this.world.containsLiquid(this.boundingBox)) {
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
                double d7 = d3 + (this.locX - d3) * d6 + (this.random.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                double d8 = d4 + (this.locY - d4) * d6 + this.random.nextDouble() * (double) this.length;
                double d9 = d5 + (this.locZ - d5) * d6 + (this.random.nextDouble() - 0.5D) * (double) this.width * 2.0D;

                this.world.a("portal", d7, d8, d9, (double) f, (double) f1, (double) f2);
            }

            this.world.makeSound(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
            this.world.makeSound(this, "mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }

    protected String aQ() {
        return "mob.endermen.idle";
    }

    protected String aR() {
        return "mob.endermen.hit";
    }

    protected String aS() {
        return "mob.endermen.death";
    }

    protected int getLootId() {
        return Item.ENDER_PEARL.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        int j = this.getLootId();

        if (j > 0) {
            // CraftBukkit start - whole method
            java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
            int count = this.random.nextInt(2 + i);

            if ((j > 0) && (count > 0)) {
                loot.add(new org.bukkit.inventory.ItemStack(j, count));
            }

            CraftEventFactory.callEntityDeathEvent(this, loot);
            // CraftBukkit end
        }
    }

    public void setCarriedId(int i) {
        this.datawatcher.watch(16, Byte.valueOf((byte) (i & 255)));
    }

    public int getCarriedId() {
        return this.datawatcher.getByte(16);
    }

    public void setCarriedData(int i) {
        this.datawatcher.watch(17, Byte.valueOf((byte) (i & 255)));
    }

    public int getCarriedData() {
        return this.datawatcher.getByte(17);
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (damagesource instanceof EntityDamageSourceIndirect) {
            for (int j = 0; j < 64; ++j) {
                if (this.n()) {
                    return true;
                }
            }

            return false;
        } else {
            if (damagesource.getEntity() instanceof EntityHuman) {
                this.e(true);
            }

            return super.damageEntity(damagesource, i);
        }
    }

    public void e(boolean flag) {
        this.datawatcher.watch(18, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    static {
        d[Block.GRASS.id] = true;
        d[Block.DIRT.id] = true;
        d[Block.SAND.id] = true;
        d[Block.GRAVEL.id] = true;
        d[Block.YELLOW_FLOWER.id] = true;
        d[Block.RED_ROSE.id] = true;
        d[Block.BROWN_MUSHROOM.id] = true;
        d[Block.RED_MUSHROOM.id] = true;
        d[Block.TNT.id] = true;
        d[Block.CACTUS.id] = true;
        d[Block.CLAY.id] = true;
        d[Block.PUMPKIN.id] = true;
        d[Block.MELON.id] = true;
        d[Block.MYCEL.id] = true;
    }
}
