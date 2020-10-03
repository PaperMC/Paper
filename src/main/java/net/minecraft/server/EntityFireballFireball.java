package net.minecraft.server;

public abstract class EntityFireballFireball extends EntityFireball {

    private static final DataWatcherObject<ItemStack> e = DataWatcher.a(EntityFireballFireball.class, DataWatcherRegistry.g);

    public EntityFireballFireball(EntityTypes<? extends EntityFireballFireball> entitytypes, World world) {
        super(entitytypes, world);
    }

    public EntityFireballFireball(EntityTypes<? extends EntityFireballFireball> entitytypes, double d0, double d1, double d2, double d3, double d4, double d5, World world) {
        super(entitytypes, d0, d1, d2, d3, d4, d5, world);
    }

    public EntityFireballFireball(EntityTypes<? extends EntityFireballFireball> entitytypes, EntityLiving entityliving, double d0, double d1, double d2, World world) {
        super(entitytypes, entityliving, d0, d1, d2, world);
    }

    public void setItem(ItemStack itemstack) {
        if (itemstack.getItem() != Items.FIRE_CHARGE || itemstack.hasTag()) {
            this.getDataWatcher().set(EntityFireballFireball.e, SystemUtils.a((Object) itemstack.cloneItemStack(), (itemstack1) -> {
                itemstack1.setCount(1);
            }));
        }

    }

    public ItemStack getItem() {
        return (ItemStack) this.getDataWatcher().get(EntityFireballFireball.e);
    }

    @Override
    protected void initDatawatcher() {
        this.getDataWatcher().register(EntityFireballFireball.e, ItemStack.b);
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
