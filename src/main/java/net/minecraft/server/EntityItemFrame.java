package net.minecraft.server;

public class EntityItemFrame extends EntityHanging {

    private float e = 1.0F;

    public EntityItemFrame(World world) {
        super(world);
    }

    public EntityItemFrame(World world, int i, int j, int k, int l) {
        super(world, i, j, k, l);
        this.setDirection(l);
    }

    protected void c() {
        this.getDataWatcher().a(2, 5);
        this.getDataWatcher().a(3, Byte.valueOf((byte) 0));
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else if (this.getItem() != null) {
            if (!this.world.isStatic) {
                // CraftBukkit start
                org.bukkit.event.entity.EntityDamageEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.handleEntityDamageEvent(this, damagesource, f);
                if ((event != null && event.isCancelled()) || this.dead) {
                    return true;
                }
                // CraftBukkit end

                this.b(damagesource.getEntity(), false);
                this.setItem((ItemStack) null);
            }

            return true;
        } else {
            return super.damageEntity(damagesource, f);
        }
    }

    public int f() {
        return 9;
    }

    public int i() {
        return 9;
    }

    public void b(Entity entity) {
        this.b(entity, true);
    }

    public void b(Entity entity, boolean flag) {
        ItemStack itemstack = this.getItem();

        if (entity instanceof EntityHuman) {
            EntityHuman entityhuman = (EntityHuman) entity;

            if (entityhuman.abilities.canInstantlyBuild) {
                this.b(itemstack);
                return;
            }
        }

        if (flag) {
            this.a(new ItemStack(Items.ITEM_FRAME), 0.0F);
        }

        if (itemstack != null && this.random.nextFloat() < this.e) {
            itemstack = itemstack.cloneItemStack();
            this.b(itemstack);
            this.a(itemstack, 0.0F);
        }
    }

    private void b(ItemStack itemstack) {
        if (itemstack != null) {
            if (itemstack.getItem() == Items.MAP) {
                WorldMap worldmap = ((ItemWorldMap) itemstack.getItem()).getSavedMap(itemstack, this.world);

                worldmap.g.remove("frame-" + this.getId());
            }

            itemstack.a((EntityItemFrame) null);
        }
    }

    public ItemStack getItem() {
        return this.getDataWatcher().getItemStack(2);
    }

    public void setItem(ItemStack itemstack) {
        if (itemstack != null) {
            itemstack = itemstack.cloneItemStack();
            itemstack.count = 1;
            itemstack.a(this);
        }

        this.getDataWatcher().watch(2, itemstack);
        this.getDataWatcher().h(2);
    }

    public int getRotation() {
        return this.getDataWatcher().getByte(3);
    }

    public void setRotation(int i) {
        this.getDataWatcher().watch(3, Byte.valueOf((byte) (i % 4)));
    }

    public void b(NBTTagCompound nbttagcompound) {
        if (this.getItem() != null) {
            nbttagcompound.set("Item", this.getItem().save(new NBTTagCompound()));
            nbttagcompound.setByte("ItemRotation", (byte) this.getRotation());
            nbttagcompound.setFloat("ItemDropChance", this.e);
        }

        super.b(nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Item");

        if (nbttagcompound1 != null && !nbttagcompound1.isEmpty()) {
            this.setItem(ItemStack.createStack(nbttagcompound1));
            this.setRotation(nbttagcompound.getByte("ItemRotation"));
            if (nbttagcompound.hasKeyOfType("ItemDropChance", 99)) {
                this.e = nbttagcompound.getFloat("ItemDropChance");
            }
        }

        super.a(nbttagcompound);
    }

    public boolean c(EntityHuman entityhuman) {
        if (this.getItem() == null) {
            ItemStack itemstack = entityhuman.be();

            if (itemstack != null && !this.world.isStatic) {
                this.setItem(itemstack);
                if (!entityhuman.abilities.canInstantlyBuild && --itemstack.count <= 0) {
                    entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                }
            }
        } else if (!this.world.isStatic) {
            this.setRotation(this.getRotation() + 1);
        }

        return true;
    }
}
