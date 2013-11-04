package net.minecraft.server;

import java.util.UUID;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTeleportEvent;
// CraftBukkit end

public class EntityEnderman extends EntityMonster {

    private static final UUID bp = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier bq = (new AttributeModifier(bp, "Attacking speed boost", 6.199999809265137D, 0)).a(false);
    private static boolean[] br = new boolean[256];
    private int bs;
    private int bt;
    private Entity bu;
    private boolean bv;

    public EntityEnderman(World world) {
        super(world);
        this.a(0.6F, 2.9F);
        this.X = 1.0F;
    }

    protected void aD() {
        super.aD();
        this.getAttributeInstance(GenericAttributes.a).setValue(40.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.30000001192092896D);
        this.getAttributeInstance(GenericAttributes.e).setValue(7.0D);
    }

    protected void c() {
        super.c();
        this.datawatcher.a(16, new Byte((byte) 0));
        this.datawatcher.a(17, new Byte((byte) 0));
        this.datawatcher.a(18, new Byte((byte) 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setShort("carried", (short) Block.b(this.getCarried()));
        nbttagcompound.setShort("carriedData", (short) this.getCarriedData());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setCarried(Block.e(nbttagcompound.getShort("carried")));
        this.setCarriedData(nbttagcompound.getShort("carriedData"));
    }

    protected Entity findTarget() {
        EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, 64.0D);

        if (entityhuman != null) {
            if (this.f(entityhuman)) {
                this.bv = true;
                if (this.bt == 0) {
                    this.world.makeSound(entityhuman.locX, entityhuman.locY, entityhuman.locZ, "mob.endermen.stare", 1.0F, 1.0F);
                }

                if (this.bt++ == 5) {
                    this.bt = 0;
                    this.a(true);
                    return entityhuman;
                }
            } else {
                this.bt = 0;
            }
        }

        return null;
    }

    private boolean f(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.armor[3];

        if (itemstack != null && itemstack.getItem() == Item.getItemOf(Blocks.PUMPKIN)) {
            return false;
        } else {
            Vec3D vec3d = entityhuman.j(1.0F).a();
            Vec3D vec3d1 = this.world.getVec3DPool().create(this.locX - entityhuman.locX, this.boundingBox.b + (double) (this.length / 2.0F) - (entityhuman.locY + (double) entityhuman.getHeadHeight()), this.locZ - entityhuman.locZ);
            double d0 = vec3d1.b();

            vec3d1 = vec3d1.a();
            double d1 = vec3d.b(vec3d1);

            return d1 > 1.0D - 0.025D / d0 ? entityhuman.o(this) : false;
        }
    }

    public void e() {
        if (this.L()) {
            this.damageEntity(DamageSource.DROWN, 1.0F);
        }

        if (this.bu != this.target) {
            AttributeInstance attributeinstance = this.getAttributeInstance(GenericAttributes.d);

            attributeinstance.b(bq);
            if (this.target != null) {
                attributeinstance.a(bq);
            }
        }

        this.bu = this.target;
        int i;

        if (!this.world.isStatic && this.world.getGameRules().getBoolean("mobGriefing")) {
            int j;
            int k;
            Block block;

            if (this.getCarried().getMaterial() == Material.AIR) {
                if (this.random.nextInt(20) == 0) {
                    i = MathHelper.floor(this.locX - 2.0D + this.random.nextDouble() * 4.0D);
                    j = MathHelper.floor(this.locY + this.random.nextDouble() * 3.0D);
                    k = MathHelper.floor(this.locZ - 2.0D + this.random.nextDouble() * 4.0D);
                    block = this.world.getType(i, j, k);
                    if (br[Block.b(block)]) {
                        // CraftBukkit start - Pickup event
                        if (!CraftEventFactory.callEntityChangeBlockEvent(this, this.world.getWorld().getBlockAt(i, j, k), org.bukkit.Material.AIR).isCancelled()) {
                            this.setCarried(block);
                            this.setCarriedData(this.world.getData(i, j, k));
                            this.world.setTypeUpdate(i, j, k, Blocks.AIR);
                        }
                        // CraftBukkit end
                    }
                }
            } else if (this.random.nextInt(2000) == 0) {
                i = MathHelper.floor(this.locX - 1.0D + this.random.nextDouble() * 2.0D);
                j = MathHelper.floor(this.locY + this.random.nextDouble() * 2.0D);
                k = MathHelper.floor(this.locZ - 1.0D + this.random.nextDouble() * 2.0D);
                block = this.world.getType(i, j, k);
                Block block1 = this.world.getType(i, j - 1, k);

                if (block.getMaterial() == Material.AIR && block1.getMaterial() != Material.AIR && block1.d()) {
                    // CraftBukkit start - Place event
                    if (!CraftEventFactory.callEntityChangeBlockEvent(this, i, j, k, this.getCarried(), this.getCarriedData()).isCancelled()) {
                        this.world.setTypeAndData(i, j, k, this.getCarried(), this.getCarriedData(), 3);
                        this.setCarried(Blocks.AIR);
                    }
                    // CraftBukkit end
                }
            }
        }

        for (i = 0; i < 2; ++i) {
            this.world.addParticle("portal", this.locX + (this.random.nextDouble() - 0.5D) * (double) this.width, this.locY + this.random.nextDouble() * (double) this.length - 0.25D, this.locZ + (this.random.nextDouble() - 0.5D) * (double) this.width, (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
        }

        if (this.world.v() && !this.world.isStatic) {
            float f = this.d(1.0F);

            if (f > 0.5F && this.world.i(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.target = null;
                this.a(false);
                this.bv = false;
                this.bX();
            }
        }

        if (this.L() || this.isBurning()) {
            this.target = null;
            this.a(false);
            this.bv = false;
            this.bX();
        }

        if (this.cb() && !this.bv && this.random.nextInt(100) == 0) {
            this.a(false);
        }

        this.bd = false;
        if (this.target != null) {
            this.a(this.target, 100.0F, 100.0F);
        }

        if (!this.world.isStatic && this.isAlive()) {
            if (this.target != null) {
                if (this.target instanceof EntityHuman && this.f((EntityHuman) this.target)) {
                    if (this.target.e((Entity) this) < 16.0D) {
                        this.bX();
                    }

                    this.bs = 0;
                } else if (this.target.e((Entity) this) > 256.0D && this.bs++ >= 30 && this.c(this.target)) {
                    this.bs = 0;
                }
            } else {
                this.a(false);
                this.bs = 0;
            }
        }

        super.e();
    }

    protected boolean bX() {
        double d0 = this.locX + (this.random.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.locY + (double) (this.random.nextInt(64) - 32);
        double d2 = this.locZ + (this.random.nextDouble() - 0.5D) * 64.0D;

        return this.k(d0, d1, d2);
    }

    protected boolean c(Entity entity) {
        Vec3D vec3d = this.world.getVec3DPool().create(this.locX - entity.locX, this.boundingBox.b + (double) (this.length / 2.0F) - entity.locY + (double) entity.getHeadHeight(), this.locZ - entity.locZ);

        vec3d = vec3d.a();
        double d0 = 16.0D;
        double d1 = this.locX + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.c * d0;
        double d2 = this.locY + (double) (this.random.nextInt(16) - 8) - vec3d.d * d0;
        double d3 = this.locZ + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.e * d0;

        return this.k(d1, d2, d3);
    }

    protected boolean k(double d0, double d1, double d2) {
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

        if (this.world.isLoaded(i, j, k)) {
            boolean flag1 = false;

            while (!flag1 && j > 0) {
                Block block = this.world.getType(i, j - 1, k);

                if (block.getMaterial().isSolid()) {
                    flag1 = true;
                } else {
                    --this.locY;
                    --j;
                }
            }

            if (flag1) {
                // CraftBukkit start - Teleport event
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

            for (int l = 0; l < short1; ++l) {
                double d6 = (double) l / ((double) short1 - 1.0D);
                float f = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
                double d7 = d3 + (this.locX - d3) * d6 + (this.random.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                double d8 = d4 + (this.locY - d4) * d6 + this.random.nextDouble() * (double) this.length;
                double d9 = d5 + (this.locZ - d5) * d6 + (this.random.nextDouble() - 0.5D) * (double) this.width * 2.0D;

                this.world.addParticle("portal", d7, d8, d9, (double) f, (double) f1, (double) f2);
            }

            this.world.makeSound(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
            this.makeSound("mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }

    protected String t() {
        return this.cb() ? "mob.endermen.scream" : "mob.endermen.idle";
    }

    protected String aT() {
        return "mob.endermen.hit";
    }

    protected String aU() {
        return "mob.endermen.death";
    }

    protected Item getLoot() {
        return Items.ENDER_PEARL;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        Item item = this.getLoot();

        if (item != null) {
            // CraftBukkit start - Whole method
            java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
            int count = this.random.nextInt(2 + i);

            if (count > 0) {
                loot.add(new org.bukkit.inventory.ItemStack(org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(item), count));
            }

            CraftEventFactory.callEntityDeathEvent(this, loot);
            // CraftBukkit end
        }
    }

    public void setCarried(Block block) {
        this.datawatcher.watch(16, Byte.valueOf((byte) (Block.b(block) & 255)));
    }

    public Block getCarried() {
        return Block.e(this.datawatcher.getByte(16));
    }

    public void setCarriedData(int i) {
        this.datawatcher.watch(17, Byte.valueOf((byte) (i & 255)));
    }

    public int getCarriedData() {
        return this.datawatcher.getByte(17);
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else {
            this.a(true);
            if (damagesource instanceof EntityDamageSource && damagesource.getEntity() instanceof EntityHuman) {
                this.bv = true;
            }

            if (damagesource instanceof EntityDamageSourceIndirect) {
                this.bv = false;

                for (int i = 0; i < 64; ++i) {
                    if (this.bX()) {
                        return true;
                    }
                }

                return false;
            } else {
                return super.damageEntity(damagesource, f);
            }
        }
    }

    public boolean cb() {
        return this.datawatcher.getByte(18) > 0;
    }

    public void a(boolean flag) {
        this.datawatcher.watch(18, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    static {
        br[Block.b((Block) Blocks.GRASS)] = true;
        br[Block.b(Blocks.DIRT)] = true;
        br[Block.b(Blocks.SAND)] = true;
        br[Block.b(Blocks.GRAVEL)] = true;
        br[Block.b((Block) Blocks.YELLOW_FLOWER)] = true;
        br[Block.b((Block) Blocks.RED_ROSE)] = true;
        br[Block.b((Block) Blocks.BROWN_MUSHROOM)] = true;
        br[Block.b((Block) Blocks.RED_MUSHROOM)] = true;
        br[Block.b(Blocks.TNT)] = true;
        br[Block.b(Blocks.CACTUS)] = true;
        br[Block.b(Blocks.CLAY)] = true;
        br[Block.b(Blocks.PUMPKIN)] = true;
        br[Block.b(Blocks.MELON)] = true;
        br[Block.b((Block) Blocks.MYCEL)] = true;
    }
}
