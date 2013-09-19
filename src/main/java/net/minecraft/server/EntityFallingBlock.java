package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityDamageEvent;
// CraftBukkit end

public class EntityFallingBlock extends Entity {

    public int id;
    public int data;
    public int c;
    public boolean dropItem;
    private boolean f;
    private boolean hurtEntities;
    private int fallHurtMax;
    private float fallHurtAmount;
    public NBTTagCompound tileEntityData;

    public EntityFallingBlock(World world) {
        super(world);
        this.dropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0F;
    }

    public EntityFallingBlock(World world, double d0, double d1, double d2, int i) {
        this(world, d0, d1, d2, i, 0);
    }

    public EntityFallingBlock(World world, double d0, double d1, double d2, int i, int j) {
        super(world);
        this.dropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0F;
        this.id = i;
        this.data = j;
        this.m = true;
        this.a(0.98F, 0.98F);
        this.height = this.length / 2.0F;
        this.setPosition(d0, d1, d2);
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
    }

    protected boolean e_() {
        return false;
    }

    protected void a() {}

    public boolean L() {
        return !this.dead;
    }

    public void l_() {
        if (this.id == 0) {
            this.die();
        } else {
            this.lastX = this.locX;
            this.lastY = this.locY;
            this.lastZ = this.locZ;
            ++this.c;
            this.motY -= 0.03999999910593033D;
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.9800000190734863D;
            this.motY *= 0.9800000190734863D;
            this.motZ *= 0.9800000190734863D;
            if (!this.world.isStatic) {
                int i = MathHelper.floor(this.locX);
                int j = MathHelper.floor(this.locY);
                int k = MathHelper.floor(this.locZ);

                if (this.c == 1) {
                    // CraftBukkit - compare data and call event
                    if (this.c != 1 || this.world.getTypeId(i, j, k) != this.id || this.world.getData(i, j, k) != this.data || CraftEventFactory.callEntityChangeBlockEvent(this, i, j, k, 0, 0).isCancelled()) {
                        this.die();
                        return;
                    }

                    this.world.setAir(i, j, k);
                }

                if (this.onGround) {
                    this.motX *= 0.699999988079071D;
                    this.motZ *= 0.699999988079071D;
                    this.motY *= -0.5D;
                    if (this.world.getTypeId(i, j, k) != Block.PISTON_MOVING.id) {
                        this.die();
                        // CraftBukkit start
                        if (!this.f && this.world.mayPlace(this.id, i, j, k, true, 1, (Entity) null, (ItemStack) null) && !BlockSand.canFall(this.world, i, j - 1, k) /* mimic the false conditions of setTypeIdAndData */ && i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000 && j > 0 && j < 256 && !(this.world.getTypeId(i, j, k) == this.id && this.world.getData(i, j, k) == this.data)) {
                            if (CraftEventFactory.callEntityChangeBlockEvent(this, i, j, k, this.id, this.data).isCancelled()) {
                                return;
                            }
                            this.world.setTypeIdAndData(i, j, k, this.id, this.data, 3);
                            // CraftBukkit end

                            if (Block.byId[this.id] instanceof BlockSand) {
                                ((BlockSand) Block.byId[this.id]).a_(this.world, i, j, k, this.data);
                            }

                            if (this.tileEntityData != null && Block.byId[this.id] instanceof IContainer) {
                                TileEntity tileentity = this.world.getTileEntity(i, j, k);

                                if (tileentity != null) {
                                    NBTTagCompound nbttagcompound = new NBTTagCompound();

                                    tileentity.b(nbttagcompound);
                                    Iterator iterator = this.tileEntityData.c().iterator();

                                    while (iterator.hasNext()) {
                                        NBTBase nbtbase = (NBTBase) iterator.next();

                                        if (!nbtbase.getName().equals("x") && !nbtbase.getName().equals("y") && !nbtbase.getName().equals("z")) {
                                            nbttagcompound.set(nbtbase.getName(), nbtbase.clone());
                                        }
                                    }

                                    tileentity.a(nbttagcompound);
                                    tileentity.update();
                                }
                            }
                        } else if (this.dropItem && !this.f) {
                            this.a(new ItemStack(this.id, 1, Block.byId[this.id].getDropData(this.data)), 0.0F);
                        }
                    }
                } else if (this.c > 100 && !this.world.isStatic && (j < 1 || j > 256) || this.c > 600) {
                    if (this.dropItem) {
                        this.a(new ItemStack(this.id, 1, Block.byId[this.id].getDropData(this.data)), 0.0F);
                    }

                    this.die();
                }
            }
        }
    }

    protected void b(float f) {
        if (this.hurtEntities) {
            int i = MathHelper.f(f - 1.0F);

            if (i > 0) {
                ArrayList arraylist = new ArrayList(this.world.getEntities(this, this.boundingBox));
                DamageSource damagesource = this.id == Block.ANVIL.id ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    // CraftBukkit start
                    float damage = (float) Math.min(MathHelper.d((float) i * this.fallHurtAmount), this.fallHurtMax);

                    EntityDamageEvent event = CraftEventFactory.callEntityDamageEvent(this, entity, EntityDamageEvent.DamageCause.FALLING_BLOCK, damage);
                    if (event.isCancelled()) {
                        continue;
                    }

                    entity.damageEntity(damagesource, (float) event.getDamage());
                    // CraftBukkit end
                }

                if (this.id == Block.ANVIL.id && (double) this.random.nextFloat() < 0.05000000074505806D + (double) i * 0.05D) {
                    int j = this.data >> 2;
                    int k = this.data & 3;

                    ++j;
                    if (j > 2) {
                        this.f = true;
                    } else {
                        this.data = k | j << 2;
                    }
                }
            }
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("Tile", (byte) this.id);
        nbttagcompound.setInt("TileID", this.id);
        nbttagcompound.setByte("Data", (byte) this.data);
        nbttagcompound.setByte("Time", (byte) this.c);
        nbttagcompound.setBoolean("DropItem", this.dropItem);
        nbttagcompound.setBoolean("HurtEntities", this.hurtEntities);
        nbttagcompound.setFloat("FallHurtAmount", this.fallHurtAmount);
        nbttagcompound.setInt("FallHurtMax", this.fallHurtMax);
        if (this.tileEntityData != null) {
            nbttagcompound.setCompound("TileEntityData", this.tileEntityData);
        }
    }

    protected void a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("TileID")) {
            this.id = nbttagcompound.getInt("TileID");
        } else {
            this.id = nbttagcompound.getByte("Tile") & 255;
        }

        this.data = nbttagcompound.getByte("Data") & 255;
        this.c = nbttagcompound.getByte("Time") & 255;
        if (nbttagcompound.hasKey("HurtEntities")) {
            this.hurtEntities = nbttagcompound.getBoolean("HurtEntities");
            this.fallHurtAmount = nbttagcompound.getFloat("FallHurtAmount");
            this.fallHurtMax = nbttagcompound.getInt("FallHurtMax");
        } else if (this.id == Block.ANVIL.id) {
            this.hurtEntities = true;
        }

        if (nbttagcompound.hasKey("DropItem")) {
            this.dropItem = nbttagcompound.getBoolean("DropItem");
        }

        if (nbttagcompound.hasKey("TileEntityData")) {
            this.tileEntityData = nbttagcompound.getCompound("TileEntityData");
        }

        // CraftBukkit start - Backward compatibility, remove in 1.6
        if (nbttagcompound.hasKey("Bukkit.tileData")) {
            this.tileEntityData = (NBTTagCompound) nbttagcompound.getCompound("Bukkit.tileData").clone();
        }
        // CraftBukkit end

        if (this.id == 0) {
            this.id = Block.SAND.id;
        }
    }

    public void a(boolean flag) {
        this.hurtEntities = flag;
    }

    public void a(CrashReportSystemDetails crashreportsystemdetails) {
        super.a(crashreportsystemdetails);
        crashreportsystemdetails.a("Immitating block ID", Integer.valueOf(this.id));
        crashreportsystemdetails.a("Immitating block data", Integer.valueOf(this.data));
    }
}
