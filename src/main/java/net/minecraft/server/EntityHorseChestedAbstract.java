package net.minecraft.server;

public abstract class EntityHorseChestedAbstract extends EntityHorseAbstract {

    private static final DataWatcherObject<Boolean> bw = DataWatcher.a(EntityHorseChestedAbstract.class, DataWatcherRegistry.i);

    protected EntityHorseChestedAbstract(EntityTypes<? extends EntityHorseChestedAbstract> entitytypes, World world) {
        super(entitytypes, world);
        this.bu = false;
    }

    @Override
    protected void eK() {
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue((double) this.fp());
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityHorseChestedAbstract.bw, false);
    }

    public static AttributeProvider.Builder eL() {
        return fi().a(GenericAttributes.MOVEMENT_SPEED, 0.17499999701976776D).a(GenericAttributes.JUMP_STRENGTH, 0.5D);
    }

    public boolean isCarryingChest() {
        return (Boolean) this.datawatcher.get(EntityHorseChestedAbstract.bw);
    }

    public void setCarryingChest(boolean flag) {
        this.datawatcher.set(EntityHorseChestedAbstract.bw, flag);
    }

    @Override
    protected int getChestSlots() {
        return this.isCarryingChest() ? 17 : super.getChestSlots();
    }

    @Override
    public double bb() {
        return super.bb() - 0.25D;
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        if (this.isCarryingChest()) {
            if (!this.world.isClientSide) {
                this.a((IMaterial) Blocks.CHEST);
            }

            this.setCarryingChest(false);
        }

    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setBoolean("ChestedHorse", this.isCarryingChest());
        if (this.isCarryingChest()) {
            NBTTagList nbttaglist = new NBTTagList();

            for (int i = 2; i < this.inventoryChest.getSize(); ++i) {
                ItemStack itemstack = this.inventoryChest.getItem(i);

                if (!itemstack.isEmpty()) {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                    nbttagcompound1.setByte("Slot", (byte) i);
                    itemstack.save(nbttagcompound1);
                    nbttaglist.add(nbttagcompound1);
                }
            }

            nbttagcompound.set("Items", nbttaglist);
        }

    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.setCarryingChest(nbttagcompound.getBoolean("ChestedHorse"));
        if (this.isCarryingChest()) {
            NBTTagList nbttaglist = nbttagcompound.getList("Items", 10);

            this.loadChest();

            for (int i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound1 = nbttaglist.getCompound(i);
                int j = nbttagcompound1.getByte("Slot") & 255;

                if (j >= 2 && j < this.inventoryChest.getSize()) {
                    this.inventoryChest.setItem(j, ItemStack.a(nbttagcompound1));
                }
            }
        }

        this.fe();
    }

    @Override
    public boolean a_(int i, ItemStack itemstack) {
        if (i == 499) {
            if (this.isCarryingChest() && itemstack.isEmpty()) {
                this.setCarryingChest(false);
                this.loadChest();
                return true;
            }

            if (!this.isCarryingChest() && itemstack.getItem() == Blocks.CHEST.getItem()) {
                this.setCarryingChest(true);
                this.loadChest();
                return true;
            }
        }

        return super.a_(i, itemstack);
    }

    @Override
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (!this.isBaby()) {
            if (this.isTamed() && entityhuman.ep()) {
                this.f(entityhuman);
                return EnumInteractionResult.a(this.world.isClientSide);
            }

            if (this.isVehicle()) {
                return super.b(entityhuman, enumhand);
            }
        }

        if (!itemstack.isEmpty()) {
            if (this.k(itemstack)) {
                return this.b(entityhuman, itemstack);
            }

            if (!this.isTamed()) {
                this.fm();
                return EnumInteractionResult.a(this.world.isClientSide);
            }

            if (!this.isCarryingChest() && itemstack.getItem() == Blocks.CHEST.getItem()) {
                this.setCarryingChest(true);
                this.eO();
                if (!entityhuman.abilities.canInstantlyBuild) {
                    itemstack.subtract(1);
                }

                this.loadChest();
                return EnumInteractionResult.a(this.world.isClientSide);
            }

            if (!this.isBaby() && !this.hasSaddle() && itemstack.getItem() == Items.SADDLE) {
                this.f(entityhuman);
                return EnumInteractionResult.a(this.world.isClientSide);
            }
        }

        if (this.isBaby()) {
            return super.b(entityhuman, enumhand);
        } else {
            this.h(entityhuman);
            return EnumInteractionResult.a(this.world.isClientSide);
        }
    }

    protected void eO() {
        this.playSound(SoundEffects.ENTITY_DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    public int eU() {
        return 5;
    }
}
