package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.util.Vector;
// CraftBukkit end

public abstract class EntityMinecartAbstract extends Entity {

    private boolean a;
    private final IUpdatePlayerListBox b;
    private String c;
    private static final int[][][] matrix = new int[][][] { { { 0, 0, -1}, { 0, 0, 1}}, { { -1, 0, 0}, { 1, 0, 0}}, { { -1, -1, 0}, { 1, 0, 0}}, { { -1, 0, 0}, { 1, -1, 0}}, { { 0, 0, -1}, { 0, -1, 1}}, { { 0, -1, -1}, { 0, 0, 1}}, { { 0, 0, 1}, { 1, 0, 0}}, { { 0, 0, 1}, { -1, 0, 0}}, { { 0, 0, -1}, { -1, 0, 0}}, { { 0, 0, -1}, { 1, 0, 0}}};
    private int e;
    private double f;
    private double g;
    private double h;
    private double i;
    private double j;

    // CraftBukkit start
    public boolean slowWhenEmpty = true;
    private double derailedX = 0.5;
    private double derailedY = 0.5;
    private double derailedZ = 0.5;
    private double flyingX = 0.95;
    private double flyingY = 0.95;
    private double flyingZ = 0.95;
    public double maxSpeed = 0.4D;
    // CraftBukkit end

    public EntityMinecartAbstract(World world) {
        super(world);
        this.m = true;
        this.a(0.98F, 0.7F);
        this.height = this.length / 2.0F;
        this.b = world != null ? world.a(this) : null;
    }

    public static EntityMinecartAbstract a(World world, double d0, double d1, double d2, int i) {
        switch (i) {
        case 1:
            return new EntityMinecartChest(world, d0, d1, d2);

        case 2:
            return new EntityMinecartFurnace(world, d0, d1, d2);

        case 3:
            return new EntityMinecartTNT(world, d0, d1, d2);

        case 4:
            return new EntityMinecartMobSpawner(world, d0, d1, d2);

        case 5:
            return new EntityMinecartHopper(world, d0, d1, d2);

        default:
            return new EntityMinecartRideable(world, d0, d1, d2);
        }
    }

    protected boolean e_() {
        return false;
    }

    protected void a() {
        this.datawatcher.a(17, new Integer(0));
        this.datawatcher.a(18, new Integer(1));
        this.datawatcher.a(19, new Float(0.0F));
        this.datawatcher.a(20, new Integer(0));
        this.datawatcher.a(21, new Integer(6));
        this.datawatcher.a(22, Byte.valueOf((byte) 0));
    }

    public AxisAlignedBB g(Entity entity) {
        return entity.M() ? entity.boundingBox : null;
    }

    public AxisAlignedBB E() {
        return null;
    }

    public boolean M() {
        return true;
    }

    public EntityMinecartAbstract(World world, double d0, double d1, double d2) {
        this(world);
        this.setPosition(d0, d1, d2);
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;

        this.world.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleCreateEvent((Vehicle) this.getBukkitEntity())); // CraftBukkit
    }

    public double Y() {
        return (double) this.length * 0.0D - 0.30000001192092896D;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (!this.world.isStatic && !this.dead) {
            if (this.isInvulnerable()) {
                return false;
            } else {
                // CraftBukkit start
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.entity.Entity passenger = (damagesource.getEntity() == null) ? null : damagesource.getEntity().getBukkitEntity();

                VehicleDamageEvent event = new VehicleDamageEvent(vehicle, passenger, f);
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return true;
                }

                f = (float) event.getDamage();
                // CraftBukkit end

                this.h(-this.k());
                this.c(10);
                this.K();
                this.setDamage(this.getDamage() + f * 10.0F);
                boolean flag = damagesource.getEntity() instanceof EntityHuman && ((EntityHuman) damagesource.getEntity()).abilities.canInstantlyBuild;

                if (flag || this.getDamage() > 40.0F) {
                    if (this.passenger != null) {
                        this.passenger.mount(this);
                    }

                    // CraftBukkit start
                    VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, passenger);
                    this.world.getServer().getPluginManager().callEvent(destroyEvent);

                    if (destroyEvent.isCancelled()) {
                        this.setDamage(40); // Maximize damage so this doesn't get triggered again right away
                        return true;
                    }
                    // CraftBukkit end

                    if (flag && !this.c()) {
                        this.die();
                    } else {
                        this.a(damagesource);
                    }
                }

                return true;
            }
        } else {
            return true;
        }
    }

    public void a(DamageSource damagesource) {
        this.die();
        ItemStack itemstack = new ItemStack(Item.MINECART, 1);

        if (this.c != null) {
            itemstack.c(this.c);
        }

        this.a(itemstack, 0.0F);
    }

    public boolean L() {
        return !this.dead;
    }

    public void die() {
        super.die();
        if (this.b != null) {
            this.b.a();
        }
    }

    public void l_() {
        // CraftBukkit start
        double prevX = this.locX;
        double prevY = this.locY;
        double prevZ = this.locZ;
        float prevYaw = this.yaw;
        float prevPitch = this.pitch;
        // CraftBukkit end

        if (this.b != null) {
            this.b.a();
        }

        if (this.j() > 0) {
            this.c(this.j() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        if (this.locY < -64.0D) {
            this.C();
        }

        int i;

        if (!this.world.isStatic && this.world instanceof WorldServer) {
            this.world.methodProfiler.a("portal");
            MinecraftServer minecraftserver = ((WorldServer) this.world).getMinecraftServer();

            i = this.z();
            if (this.ap) {
                if (true || minecraftserver.getAllowNether()) { // CraftBukkit - multi-world should still allow teleport even if default vanilla nether disabled
                    if (this.vehicle == null && this.aq++ >= i) {
                        this.aq = i;
                        this.portalCooldown = this.ac();
                        byte b0;

                        if (this.world.worldProvider.dimension == -1) {
                            b0 = 0;
                        } else {
                            b0 = -1;
                        }

                        this.b(b0);
                    }

                    this.ap = false;
                }
            } else {
                if (this.aq > 0) {
                    this.aq -= 4;
                }

                if (this.aq < 0) {
                    this.aq = 0;
                }
            }

            if (this.portalCooldown > 0) {
                --this.portalCooldown;
            }

            this.world.methodProfiler.b();
        }

        if (this.world.isStatic) {
            if (this.e > 0) {
                double d0 = this.locX + (this.f - this.locX) / (double) this.e;
                double d1 = this.locY + (this.g - this.locY) / (double) this.e;
                double d2 = this.locZ + (this.h - this.locZ) / (double) this.e;
                double d3 = MathHelper.g(this.i - (double) this.yaw);

                this.yaw = (float) ((double) this.yaw + d3 / (double) this.e);
                this.pitch = (float) ((double) this.pitch + (this.j - (double) this.pitch) / (double) this.e);
                --this.e;
                this.setPosition(d0, d1, d2);
                this.b(this.yaw, this.pitch);
            } else {
                this.setPosition(this.locX, this.locY, this.locZ);
                this.b(this.yaw, this.pitch);
            }
        } else {
            this.lastX = this.locX;
            this.lastY = this.locY;
            this.lastZ = this.locZ;
            this.motY -= 0.03999999910593033D;
            int j = MathHelper.floor(this.locX);

            i = MathHelper.floor(this.locY);
            int k = MathHelper.floor(this.locZ);

            if (BlockMinecartTrackAbstract.d_(this.world, j, i - 1, k)) {
                --i;
            }

            double d4 = this.maxSpeed; // CraftBukkit
            double d5 = 0.0078125D;
            int l = this.world.getTypeId(j, i, k);

            if (BlockMinecartTrackAbstract.e_(l)) {
                int i1 = this.world.getData(j, i, k);

                this.a(j, i, k, d4, d5, l, i1);
                if (l == Block.ACTIVATOR_RAIL.id) {
                    this.a(j, i, k, (i1 & 8) != 0);
                }
            } else {
                this.b(d4);
            }

            this.D();
            this.pitch = 0.0F;
            double d6 = this.lastX - this.locX;
            double d7 = this.lastZ - this.locZ;

            if (d6 * d6 + d7 * d7 > 0.001D) {
                this.yaw = (float) (Math.atan2(d7, d6) * 180.0D / 3.141592653589793D);
                if (this.a) {
                    this.yaw += 180.0F;
                }
            }

            double d8 = (double) MathHelper.g(this.yaw - this.lastYaw);

            if (d8 < -170.0D || d8 >= 170.0D) {
                this.yaw += 180.0F;
                this.a = !this.a;
            }

            this.b(this.yaw, this.pitch);

            // CraftBukkit start
            org.bukkit.World bworld = this.world.getWorld();
            Location from = new Location(bworld, prevX, prevY, prevZ, prevYaw, prevPitch);
            Location to = new Location(bworld, this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            Vehicle vehicle = (Vehicle) this.getBukkitEntity();

            this.world.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleUpdateEvent(vehicle));

            if (!from.equals(to)) {
                this.world.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleMoveEvent(vehicle, from, to));
            }
            // CraftBukkit end

            List list = this.world.getEntities(this, this.boundingBox.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));

            if (list != null && !list.isEmpty()) {
                for (int j1 = 0; j1 < list.size(); ++j1) {
                    Entity entity = (Entity) list.get(j1);

                    if (entity != this.passenger && entity.M() && entity instanceof EntityMinecartAbstract) {
                        entity.collide(this);
                    }
                }
            }

            if (this.passenger != null && this.passenger.dead) {
                if (this.passenger.vehicle == this) {
                    this.passenger.vehicle = null;
                }

                this.passenger = null;
            }
        }
    }

    public void a(int i, int j, int k, boolean flag) {}

    protected void b(double d0) {
        if (this.motX < -d0) {
            this.motX = -d0;
        }

        if (this.motX > d0) {
            this.motX = d0;
        }

        if (this.motZ < -d0) {
            this.motZ = -d0;
        }

        if (this.motZ > d0) {
            this.motZ = d0;
        }

        if (this.onGround) {
            // CraftBukkit start
            this.motX *= this.derailedX;
            this.motY *= this.derailedY;
            this.motZ *= this.derailedZ;
            // CraftBukkit end
        }

        this.move(this.motX, this.motY, this.motZ);
        if (!this.onGround) {
            // CraftBukkit start
            this.motX *= this.flyingX;
            this.motY *= this.flyingY;
            this.motZ *= this.flyingZ;
            // CraftBukkit end
        }
    }

    protected void a(int i, int j, int k, double d0, double d1, int l, int i1) {
        this.fallDistance = 0.0F;
        Vec3D vec3d = this.a(this.locX, this.locY, this.locZ);

        this.locY = (double) j;
        boolean flag = false;
        boolean flag1 = false;

        if (l == Block.GOLDEN_RAIL.id) {
            flag = (i1 & 8) != 0;
            flag1 = !flag;
        }

        if (((BlockMinecartTrackAbstract) Block.byId[l]).e()) {
            i1 &= 7;
        }

        if (i1 >= 2 && i1 <= 5) {
            this.locY = (double) (j + 1);
        }

        if (i1 == 2) {
            this.motX -= d1;
        }

        if (i1 == 3) {
            this.motX += d1;
        }

        if (i1 == 4) {
            this.motZ += d1;
        }

        if (i1 == 5) {
            this.motZ -= d1;
        }

        int[][] aint = matrix[i1];
        double d2 = (double) (aint[1][0] - aint[0][0]);
        double d3 = (double) (aint[1][2] - aint[0][2]);
        double d4 = Math.sqrt(d2 * d2 + d3 * d3);
        double d5 = this.motX * d2 + this.motZ * d3;

        if (d5 < 0.0D) {
            d2 = -d2;
            d3 = -d3;
        }

        double d6 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);

        if (d6 > 2.0D) {
            d6 = 2.0D;
        }

        this.motX = d6 * d2 / d4;
        this.motZ = d6 * d3 / d4;
        double d7;
        double d8;
        double d9;
        double d10;

        if (this.passenger != null && this.passenger instanceof EntityLiving) {
            d7 = (double) ((EntityLiving) this.passenger).bf;
            if (d7 > 0.0D) {
                d8 = -Math.sin((double) (this.passenger.yaw * 3.1415927F / 180.0F));
                d9 = Math.cos((double) (this.passenger.yaw * 3.1415927F / 180.0F));
                d10 = this.motX * this.motX + this.motZ * this.motZ;
                if (d10 < 0.01D) {
                    this.motX += d8 * 0.1D;
                    this.motZ += d9 * 0.1D;
                    flag1 = false;
                }
            }
        }

        if (flag1) {
            d7 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
            if (d7 < 0.03D) {
                this.motX *= 0.0D;
                this.motY *= 0.0D;
                this.motZ *= 0.0D;
            } else {
                this.motX *= 0.5D;
                this.motY *= 0.0D;
                this.motZ *= 0.5D;
            }
        }

        d7 = 0.0D;
        d8 = (double) i + 0.5D + (double) aint[0][0] * 0.5D;
        d9 = (double) k + 0.5D + (double) aint[0][2] * 0.5D;
        d10 = (double) i + 0.5D + (double) aint[1][0] * 0.5D;
        double d11 = (double) k + 0.5D + (double) aint[1][2] * 0.5D;

        d2 = d10 - d8;
        d3 = d11 - d9;
        double d12;
        double d13;

        if (d2 == 0.0D) {
            this.locX = (double) i + 0.5D;
            d7 = this.locZ - (double) k;
        } else if (d3 == 0.0D) {
            this.locZ = (double) k + 0.5D;
            d7 = this.locX - (double) i;
        } else {
            d12 = this.locX - d8;
            d13 = this.locZ - d9;
            d7 = (d12 * d2 + d13 * d3) * 2.0D;
        }

        this.locX = d8 + d2 * d7;
        this.locZ = d9 + d3 * d7;
        this.setPosition(this.locX, this.locY + (double) this.height, this.locZ);
        d12 = this.motX;
        d13 = this.motZ;
        if (this.passenger != null) {
            d12 *= 0.75D;
            d13 *= 0.75D;
        }

        if (d12 < -d0) {
            d12 = -d0;
        }

        if (d12 > d0) {
            d12 = d0;
        }

        if (d13 < -d0) {
            d13 = -d0;
        }

        if (d13 > d0) {
            d13 = d0;
        }

        this.move(d12, 0.0D, d13);
        if (aint[0][1] != 0 && MathHelper.floor(this.locX) - i == aint[0][0] && MathHelper.floor(this.locZ) - k == aint[0][2]) {
            this.setPosition(this.locX, this.locY + (double) aint[0][1], this.locZ);
        } else if (aint[1][1] != 0 && MathHelper.floor(this.locX) - i == aint[1][0] && MathHelper.floor(this.locZ) - k == aint[1][2]) {
            this.setPosition(this.locX, this.locY + (double) aint[1][1], this.locZ);
        }

        this.h();
        Vec3D vec3d1 = this.a(this.locX, this.locY, this.locZ);

        if (vec3d1 != null && vec3d != null) {
            double d14 = (vec3d.d - vec3d1.d) * 0.05D;

            d6 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
            if (d6 > 0.0D) {
                this.motX = this.motX / d6 * (d6 + d14);
                this.motZ = this.motZ / d6 * (d6 + d14);
            }

            this.setPosition(this.locX, vec3d1.d, this.locZ);
        }

        int j1 = MathHelper.floor(this.locX);
        int k1 = MathHelper.floor(this.locZ);

        if (j1 != i || k1 != k) {
            d6 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
            this.motX = d6 * (double) (j1 - i);
            this.motZ = d6 * (double) (k1 - k);
        }

        if (flag) {
            double d15 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);

            if (d15 > 0.01D) {
                double d16 = 0.06D;

                this.motX += this.motX / d15 * d16;
                this.motZ += this.motZ / d15 * d16;
            } else if (i1 == 1) {
                if (this.world.u(i - 1, j, k)) {
                    this.motX = 0.02D;
                } else if (this.world.u(i + 1, j, k)) {
                    this.motX = -0.02D;
                }
            } else if (i1 == 0) {
                if (this.world.u(i, j, k - 1)) {
                    this.motZ = 0.02D;
                } else if (this.world.u(i, j, k + 1)) {
                    this.motZ = -0.02D;
                }
            }
        }
    }

    protected void h() {
        if (this.passenger != null || !this.slowWhenEmpty) { // CraftBukkit
            this.motX *= 0.996999979019165D;
            this.motY *= 0.0D;
            this.motZ *= 0.996999979019165D;
        } else {
            this.motX *= 0.9599999785423279D;
            this.motY *= 0.0D;
            this.motZ *= 0.9599999785423279D;
        }
    }

    public Vec3D a(double d0, double d1, double d2) {
        int i = MathHelper.floor(d0);
        int j = MathHelper.floor(d1);
        int k = MathHelper.floor(d2);

        if (BlockMinecartTrackAbstract.d_(this.world, i, j - 1, k)) {
            --j;
        }

        int l = this.world.getTypeId(i, j, k);

        if (BlockMinecartTrackAbstract.e_(l)) {
            int i1 = this.world.getData(i, j, k);

            d1 = (double) j;
            if (((BlockMinecartTrackAbstract) Block.byId[l]).e()) {
                i1 &= 7;
            }

            if (i1 >= 2 && i1 <= 5) {
                d1 = (double) (j + 1);
            }

            int[][] aint = matrix[i1];
            double d3 = 0.0D;
            double d4 = (double) i + 0.5D + (double) aint[0][0] * 0.5D;
            double d5 = (double) j + 0.5D + (double) aint[0][1] * 0.5D;
            double d6 = (double) k + 0.5D + (double) aint[0][2] * 0.5D;
            double d7 = (double) i + 0.5D + (double) aint[1][0] * 0.5D;
            double d8 = (double) j + 0.5D + (double) aint[1][1] * 0.5D;
            double d9 = (double) k + 0.5D + (double) aint[1][2] * 0.5D;
            double d10 = d7 - d4;
            double d11 = (d8 - d5) * 2.0D;
            double d12 = d9 - d6;

            if (d10 == 0.0D) {
                d0 = (double) i + 0.5D;
                d3 = d2 - (double) k;
            } else if (d12 == 0.0D) {
                d2 = (double) k + 0.5D;
                d3 = d0 - (double) i;
            } else {
                double d13 = d0 - d4;
                double d14 = d2 - d6;

                d3 = (d13 * d10 + d14 * d12) * 2.0D;
            }

            d0 = d4 + d10 * d3;
            d1 = d5 + d11 * d3;
            d2 = d6 + d12 * d3;
            if (d11 < 0.0D) {
                ++d1;
            }

            if (d11 > 0.0D) {
                d1 += 0.5D;
            }

            return this.world.getVec3DPool().create(d0, d1, d2);
        } else {
            return null;
        }
    }

    protected void a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.getBoolean("CustomDisplayTile")) {
            this.i(nbttagcompound.getInt("DisplayTile"));
            this.j(nbttagcompound.getInt("DisplayData"));
            this.k(nbttagcompound.getInt("DisplayOffset"));
        }

        if (nbttagcompound.hasKey("CustomName") && nbttagcompound.getString("CustomName").length() > 0) {
            this.c = nbttagcompound.getString("CustomName");
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {
        if (this.s()) {
            nbttagcompound.setBoolean("CustomDisplayTile", true);
            nbttagcompound.setInt("DisplayTile", this.m() == null ? 0 : this.m().id);
            nbttagcompound.setInt("DisplayData", this.o());
            nbttagcompound.setInt("DisplayOffset", this.q());
        }

        if (this.c != null && this.c.length() > 0) {
            nbttagcompound.setString("CustomName", this.c);
        }
    }

    public void collide(Entity entity) {
        if (!this.world.isStatic) {
            if (entity != this.passenger) {
                // CraftBukkit start
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.entity.Entity hitEntity = (entity == null) ? null : entity.getBukkitEntity();

                VehicleEntityCollisionEvent collisionEvent = new VehicleEntityCollisionEvent(vehicle, hitEntity);
                this.world.getServer().getPluginManager().callEvent(collisionEvent);

                if (collisionEvent.isCancelled()) {
                    return;
                }
                // CraftBukkit end

                if (entity instanceof EntityLiving && !(entity instanceof EntityHuman) && !(entity instanceof EntityIronGolem) && this.getType() == 0 && this.motX * this.motX + this.motZ * this.motZ > 0.01D && this.passenger == null && entity.vehicle == null) {
                    entity.mount(this);
                }

                double d0 = entity.locX - this.locX;
                double d1 = entity.locZ - this.locZ;
                double d2 = d0 * d0 + d1 * d1;

                // CraftBukkit - collision
                if (d2 >= 9.999999747378752E-5D && !collisionEvent.isCollisionCancelled()) {
                    d2 = (double) MathHelper.sqrt(d2);
                    d0 /= d2;
                    d1 /= d2;
                    double d3 = 1.0D / d2;

                    if (d3 > 1.0D) {
                        d3 = 1.0D;
                    }

                    d0 *= d3;
                    d1 *= d3;
                    d0 *= 0.10000000149011612D;
                    d1 *= 0.10000000149011612D;
                    d0 *= (double) (1.0F - this.aa);
                    d1 *= (double) (1.0F - this.aa);
                    d0 *= 0.5D;
                    d1 *= 0.5D;
                    if (entity instanceof EntityMinecartAbstract) {
                        double d4 = entity.locX - this.locX;
                        double d5 = entity.locZ - this.locZ;
                        Vec3D vec3d = this.world.getVec3DPool().create(d4, 0.0D, d5).a();
                        Vec3D vec3d1 = this.world.getVec3DPool().create((double) MathHelper.cos(this.yaw * 3.1415927F / 180.0F), 0.0D, (double) MathHelper.sin(this.yaw * 3.1415927F / 180.0F)).a();
                        double d6 = Math.abs(vec3d.b(vec3d1));

                        if (d6 < 0.800000011920929D) {
                            return;
                        }

                        double d7 = entity.motX + this.motX;
                        double d8 = entity.motZ + this.motZ;

                        if (((EntityMinecartAbstract) entity).getType() == 2 && this.getType() != 2) {
                            this.motX *= 0.20000000298023224D;
                            this.motZ *= 0.20000000298023224D;
                            this.g(entity.motX - d0, 0.0D, entity.motZ - d1);
                            entity.motX *= 0.949999988079071D;
                            entity.motZ *= 0.949999988079071D;
                        } else if (((EntityMinecartAbstract) entity).getType() != 2 && this.getType() == 2) {
                            entity.motX *= 0.20000000298023224D;
                            entity.motZ *= 0.20000000298023224D;
                            entity.g(this.motX + d0, 0.0D, this.motZ + d1);
                            this.motX *= 0.949999988079071D;
                            this.motZ *= 0.949999988079071D;
                        } else {
                            d7 /= 2.0D;
                            d8 /= 2.0D;
                            this.motX *= 0.20000000298023224D;
                            this.motZ *= 0.20000000298023224D;
                            this.g(d7 - d0, 0.0D, d8 - d1);
                            entity.motX *= 0.20000000298023224D;
                            entity.motZ *= 0.20000000298023224D;
                            entity.g(d7 + d0, 0.0D, d8 + d1);
                        }
                    } else {
                        this.g(-d0, 0.0D, -d1);
                        entity.g(d0 / 4.0D, 0.0D, d1 / 4.0D);
                    }
                }
            }
        }
    }

    public void setDamage(float f) {
        this.datawatcher.watch(19, Float.valueOf(f));
    }

    public float getDamage() {
        return this.datawatcher.getFloat(19);
    }

    public void c(int i) {
        this.datawatcher.watch(17, Integer.valueOf(i));
    }

    public int j() {
        return this.datawatcher.getInt(17);
    }

    public void h(int i) {
        this.datawatcher.watch(18, Integer.valueOf(i));
    }

    public int k() {
        return this.datawatcher.getInt(18);
    }

    public abstract int getType();

    public Block m() {
        if (!this.s()) {
            return this.n();
        } else {
            int i = this.getDataWatcher().getInt(20) & '\uffff';

            return i > 0 && i < Block.byId.length ? Block.byId[i] : null;
        }
    }

    public Block n() {
        return null;
    }

    public int o() {
        return !this.s() ? this.p() : this.getDataWatcher().getInt(20) >> 16;
    }

    public int p() {
        return 0;
    }

    public int q() {
        return !this.s() ? this.r() : this.getDataWatcher().getInt(21);
    }

    public int r() {
        return 6;
    }

    public void i(int i) {
        this.getDataWatcher().watch(20, Integer.valueOf(i & '\uffff' | this.o() << 16));
        this.a(true);
    }

    public void j(int i) {
        Block block = this.m();
        int j = block == null ? 0 : block.id;

        this.getDataWatcher().watch(20, Integer.valueOf(j & '\uffff' | i << 16));
        this.a(true);
    }

    public void k(int i) {
        this.getDataWatcher().watch(21, Integer.valueOf(i));
        this.a(true);
    }

    public boolean s() {
        return this.getDataWatcher().getByte(22) == 1;
    }

    public void a(boolean flag) {
        this.getDataWatcher().watch(22, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    public void a(String s) {
        this.c = s;
    }

    public String getLocalizedName() {
        return this.c != null ? this.c : super.getLocalizedName();
    }

    public boolean c() {
        return this.c != null;
    }

    public String t() {
        return this.c;
    }

    // CraftBukkit start - Methods for getting and setting flying and derailed velocity modifiers
    public Vector getFlyingVelocityMod() {
        return new Vector(flyingX, flyingY, flyingZ);
    }

    public void setFlyingVelocityMod(Vector flying) {
        flyingX = flying.getX();
        flyingY = flying.getY();
        flyingZ = flying.getZ();
    }

    public Vector getDerailedVelocityMod() {
        return new Vector(derailedX, derailedY, derailedZ);
    }

    public void setDerailedVelocityMod(Vector derailed) {
        derailedX = derailed.getX();
        derailedY = derailed.getY();
        derailedZ = derailed.getZ();
    }
    // CraftBukkit end
}
