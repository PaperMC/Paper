package net.minecraft.server;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.bukkit.event.player.PlayerPickupItemEvent; // CraftBukkit

public class EntityItem extends Entity {

    private static final Logger d = LogManager.getLogger();
    public int age;
    public int pickupDelay;
    private int e;
    private String f;
    private String g;
    public float c;
    private int lastTick = MinecraftServer.currentTick; // CraftBukkit

    public EntityItem(World world, double d0, double d1, double d2) {
        super(world);
        this.e = 5;
        this.c = (float) (Math.random() * 3.141592653589793D * 2.0D);
        this.a(0.25F, 0.25F);
        this.height = this.length / 2.0F;
        this.setPosition(d0, d1, d2);
        this.yaw = (float) (Math.random() * 360.0D);
        this.motX = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        this.motY = 0.20000000298023224D;
        this.motZ = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
    }

    public EntityItem(World world, double d0, double d1, double d2, ItemStack itemstack) {
        this(world, d0, d1, d2);
        // CraftBukkit start - Can't set null items in the datawatcher
        if (itemstack == null || itemstack.getItem() == null) {
            return;
        }
        // CraftBukkit end
        this.setItemStack(itemstack);
    }

    protected boolean g_() {
        return false;
    }

    public EntityItem(World world) {
        super(world);
        this.e = 5;
        this.c = (float) (Math.random() * 3.141592653589793D * 2.0D);
        this.a(0.25F, 0.25F);
        this.height = this.length / 2.0F;
    }

    protected void c() {
        this.getDataWatcher().a(10, 5);
    }

    public void h() {
        if (this.getItemStack() == null) {
            this.die();
        } else {
            super.h();
            // CraftBukkit start - Use wall time for pickup and despawn timers
            int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
            this.pickupDelay -= elapsedTicks;
            this.age += elapsedTicks;
            this.lastTick = MinecraftServer.currentTick;
            // CraftBukkit end

            this.lastX = this.locX;
            this.lastY = this.locY;
            this.lastZ = this.locZ;
            this.motY -= 0.03999999910593033D;
            this.Y = this.j(this.locX, (this.boundingBox.b + this.boundingBox.e) / 2.0D, this.locZ);
            this.move(this.motX, this.motY, this.motZ);
            boolean flag = (int) this.lastX != (int) this.locX || (int) this.lastY != (int) this.locY || (int) this.lastZ != (int) this.locZ;

            if (flag || this.ticksLived % 25 == 0) {
                if (this.world.getType(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)).getMaterial() == Material.LAVA) {
                    this.motY = 0.20000000298023224D;
                    this.motX = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                    this.motZ = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                    this.makeSound("random.fizz", 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
                }

                if (!this.world.isStatic) {
                    this.k();
                }
            }

            float f = 0.98F;

            if (this.onGround) {
                f = this.world.getType(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ)).frictionFactor * 0.98F;
            }

            this.motX *= (double) f;
            this.motY *= 0.9800000190734863D;
            this.motZ *= (double) f;
            if (this.onGround) {
                this.motY *= -0.5D;
            }

            // ++this.age; // CraftBukkit - Moved up
            if (!this.world.isStatic && this.age >= 6000) {
                // CraftBukkit start
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callItemDespawnEvent(this).isCancelled()) {
                    this.age = 0;
                    return;
                }
                // CraftBukkit end
                this.die();
            }
        }
    }

    private void k() {
        Iterator iterator = this.world.a(EntityItem.class, this.boundingBox.grow(0.5D, 0.0D, 0.5D)).iterator();

        while (iterator.hasNext()) {
            EntityItem entityitem = (EntityItem) iterator.next();

            this.a(entityitem);
        }
    }

    public boolean a(EntityItem entityitem) {
        if (entityitem == this) {
            return false;
        } else if (entityitem.isAlive() && this.isAlive()) {
            ItemStack itemstack = this.getItemStack();
            ItemStack itemstack1 = entityitem.getItemStack();

            if (itemstack1.getItem() != itemstack.getItem()) {
                return false;
            } else if (itemstack1.hasTag() ^ itemstack.hasTag()) {
                return false;
            } else if (itemstack1.hasTag() && !itemstack1.getTag().equals(itemstack.getTag())) {
                return false;
            } else if (itemstack1.getItem() == null) {
                return false;
            } else if (itemstack1.getItem().n() && itemstack1.getData() != itemstack.getData()) {
                return false;
            } else if (itemstack1.count < itemstack.count) {
                return entityitem.a(this);
            } else if (itemstack1.count + itemstack.count > itemstack1.getMaxStackSize()) {
                return false;
            } else {
                itemstack1.count += itemstack.count;
                entityitem.pickupDelay = Math.max(entityitem.pickupDelay, this.pickupDelay);
                entityitem.age = Math.min(entityitem.age, this.age);
                entityitem.setItemStack(itemstack1);
                this.die();
                return true;
            }
        } else {
            return false;
        }
    }

    public void e() {
        this.age = 4800;
    }

    public boolean N() {
        return this.world.a(this.boundingBox, Material.WATER, (Entity) this);
    }

    protected void burn(int i) {
        this.damageEntity(DamageSource.FIRE, (float) i);
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else if (this.getItemStack() != null && this.getItemStack().getItem() == Items.NETHER_STAR && damagesource.c()) {
            return false;
        } else {
            this.Q();
            this.e = (int) ((float) this.e - f);
            if (this.e <= 0) {
                this.die();
            }

            return false;
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("Health", (short) ((byte) this.e));
        nbttagcompound.setShort("Age", (short) this.age);
        if (this.j() != null) {
            nbttagcompound.setString("Thrower", this.f);
        }

        if (this.i() != null) {
            nbttagcompound.setString("Owner", this.g);
        }

        if (this.getItemStack() != null) {
            nbttagcompound.set("Item", this.getItemStack().save(new NBTTagCompound()));
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.e = nbttagcompound.getShort("Health") & 255;
        this.age = nbttagcompound.getShort("Age");
        if (nbttagcompound.hasKey("Owner")) {
            this.g = nbttagcompound.getString("Owner");
        }

        if (nbttagcompound.hasKey("Thrower")) {
            this.f = nbttagcompound.getString("Thrower");
        }

        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Item");

        // CraftBukkit start
        if (nbttagcompound1 != null) {
            ItemStack itemstack = ItemStack.createStack(nbttagcompound1);
            if (itemstack != null) {
                this.setItemStack(itemstack);
            } else {
                this.die();
            }
        } else {
            this.die();
        }
        // CraftBukkit end
        if (this.getItemStack() == null) {
            this.die();
        }
    }

    public void b_(EntityHuman entityhuman) {
        if (!this.world.isStatic) {
            ItemStack itemstack = this.getItemStack();
            int i = itemstack.count;

            // CraftBukkit start
            int canHold = entityhuman.inventory.canHold(itemstack);
            int remaining = itemstack.count - canHold;

            if (this.pickupDelay <= 0 && canHold > 0) {
                itemstack.count = canHold;
                PlayerPickupItemEvent event = new PlayerPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), (org.bukkit.entity.Item) this.getBukkitEntity(), remaining);
                // event.setCancelled(!entityhuman.canPickUpLoot); TODO
                this.world.getServer().getPluginManager().callEvent(event);
                itemstack.count = canHold + remaining;

                if (event.isCancelled()) {
                    return;
                }

                // Possibly < 0; fix here so we do not have to modify code below
                this.pickupDelay = 0;
            }
            // CraftBukkit end

            if (this.pickupDelay == 0 && (this.g == null || 6000 - this.age <= 200 || this.g.equals(entityhuman.getName())) && entityhuman.inventory.pickup(itemstack)) {
                if (itemstack.getItem() == Item.getItemOf(Blocks.LOG)) {
                    entityhuman.a((Statistic) AchievementList.g);
                }

                if (itemstack.getItem() == Item.getItemOf(Blocks.LOG2)) {
                    entityhuman.a((Statistic) AchievementList.g);
                }

                if (itemstack.getItem() == Items.LEATHER) {
                    entityhuman.a((Statistic) AchievementList.t);
                }

                if (itemstack.getItem() == Items.DIAMOND) {
                    entityhuman.a((Statistic) AchievementList.w);
                }

                if (itemstack.getItem() == Items.BLAZE_ROD) {
                    entityhuman.a((Statistic) AchievementList.A);
                }

                if (itemstack.getItem() == Items.DIAMOND && this.j() != null) {
                    EntityHuman entityhuman1 = this.world.a(this.j());

                    if (entityhuman1 != null && entityhuman1 != entityhuman) {
                        entityhuman1.a((Statistic) AchievementList.x);
                    }
                }

                this.world.makeSound(entityhuman, "random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityhuman.receive(this, i);
                if (itemstack.count <= 0) {
                    this.die();
                }
            }
        }
    }

    public String getName() {
        return LocaleI18n.get("item." + this.getItemStack().a());
    }

    public boolean av() {
        return false;
    }

    public void b(int i) {
        super.b(i);
        if (!this.world.isStatic) {
            this.k();
        }
    }

    public ItemStack getItemStack() {
        ItemStack itemstack = this.getDataWatcher().getItemStack(10);

        if (itemstack == null) {
            if (this.world != null) {
                d.error("Item entity " + this.getId() + " has no item?!");
            }

            return new ItemStack(Blocks.STONE);
        } else {
            return itemstack;
        }
    }

    public void setItemStack(ItemStack itemstack) {
        this.getDataWatcher().watch(10, itemstack);
        this.getDataWatcher().h(10);
    }

    public String i() {
        return this.g;
    }

    public void a(String s) {
        this.g = s;
    }

    public String j() {
        return this.f;
    }

    public void b(String s) {
        this.f = s;
    }
}
