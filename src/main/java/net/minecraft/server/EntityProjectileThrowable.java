package net.minecraft.server;

public abstract class EntityProjectileThrowable extends EntityProjectile {

    private static final DataWatcherObject<ItemStack> b = DataWatcher.a(EntityProjectileThrowable.class, DataWatcherRegistry.g);

    public EntityProjectileThrowable(EntityTypes<? extends EntityProjectileThrowable> entitytypes, World world) {
        super(entitytypes, world);
    }

    public EntityProjectileThrowable(EntityTypes<? extends EntityProjectileThrowable> entitytypes, double d0, double d1, double d2, World world) {
        super(entitytypes, d0, d1, d2, world);
    }

    public EntityProjectileThrowable(EntityTypes<? extends EntityProjectileThrowable> entitytypes, EntityLiving entityliving, World world) {
        super(entitytypes, entityliving, world);
    }

    public void setItem(ItemStack itemstack) {
        if (itemstack.getItem() != this.getDefaultItem() || itemstack.hasTag()) {
            this.getDataWatcher().set(EntityProjectileThrowable.b, SystemUtils.a((Object) itemstack.cloneItemStack(), (itemstack1) -> {
                itemstack1.setCount(1);
            }));
        }

    }

    protected abstract Item getDefaultItem();

    public ItemStack getItem() {
        return (ItemStack) this.getDataWatcher().get(EntityProjectileThrowable.b);
    }

    public ItemStack g() {
        ItemStack itemstack = this.getItem();

        return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
    }

    @Override
    protected void initDatawatcher() {
        this.getDataWatcher().register(EntityProjectileThrowable.b, ItemStack.b);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        ItemStack itemstack = this.getItem();

        if (!itemstack.isEmpty()) {
            nbttagcompound.set("Item", itemstack.save(new NBTTagCompound()));
        }

    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        ItemStack itemstack = ItemStack.a(nbttagcompound.getCompound("Item"));

        this.setItem(itemstack);
    }
}
