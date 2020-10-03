package net.minecraft.server;

import javax.annotation.Nullable;

public class TileEntityLectern extends TileEntity implements Clearable, ITileInventory {

    public final IInventory inventory = new IInventory() {
        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return TileEntityLectern.this.book.isEmpty();
        }

        @Override
        public ItemStack getItem(int i) {
            return i == 0 ? TileEntityLectern.this.book : ItemStack.b;
        }

        @Override
        public ItemStack splitStack(int i, int j) {
            if (i == 0) {
                ItemStack itemstack = TileEntityLectern.this.book.cloneAndSubtract(j);

                if (TileEntityLectern.this.book.isEmpty()) {
                    TileEntityLectern.this.k();
                }

                return itemstack;
            } else {
                return ItemStack.b;
            }
        }

        @Override
        public ItemStack splitWithoutUpdate(int i) {
            if (i == 0) {
                ItemStack itemstack = TileEntityLectern.this.book;

                TileEntityLectern.this.book = ItemStack.b;
                TileEntityLectern.this.k();
                return itemstack;
            } else {
                return ItemStack.b;
            }
        }

        @Override
        public void setItem(int i, ItemStack itemstack) {}

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void update() {
            TileEntityLectern.this.update();
        }

        @Override
        public boolean a(EntityHuman entityhuman) {
            return TileEntityLectern.this.world.getTileEntity(TileEntityLectern.this.position) != TileEntityLectern.this ? false : (entityhuman.h((double) TileEntityLectern.this.position.getX() + 0.5D, (double) TileEntityLectern.this.position.getY() + 0.5D, (double) TileEntityLectern.this.position.getZ() + 0.5D) > 64.0D ? false : TileEntityLectern.this.hasBook());
        }

        @Override
        public boolean b(int i, ItemStack itemstack) {
            return false;
        }

        @Override
        public void clear() {}
    };
    private final IContainerProperties containerProperties = new IContainerProperties() {
        @Override
        public int getProperty(int i) {
            return i == 0 ? TileEntityLectern.this.page : 0;
        }

        @Override
        public void setProperty(int i, int j) {
            if (i == 0) {
                TileEntityLectern.this.setPage(j);
            }

        }

        @Override
        public int a() {
            return 1;
        }
    };
    private ItemStack book;
    private int page;
    private int maxPage;

    public TileEntityLectern() {
        super(TileEntityTypes.LECTERN);
        this.book = ItemStack.b;
    }

    public ItemStack getBook() {
        return this.book;
    }

    public boolean hasBook() {
        Item item = this.book.getItem();

        return item == Items.WRITABLE_BOOK || item == Items.WRITTEN_BOOK;
    }

    public void setBook(ItemStack itemstack) {
        this.a(itemstack, (EntityHuman) null);
    }

    private void k() {
        this.page = 0;
        this.maxPage = 0;
        BlockLectern.setHasBook(this.getWorld(), this.getPosition(), this.getBlock(), false);
    }

    public void a(ItemStack itemstack, @Nullable EntityHuman entityhuman) {
        this.book = this.b(itemstack, entityhuman);
        this.page = 0;
        this.maxPage = ItemWrittenBook.g(this.book);
        this.update();
    }

    public void setPage(int i) {
        int j = MathHelper.clamp(i, 0, this.maxPage - 1);

        if (j != this.page) {
            this.page = j;
            this.update();
            BlockLectern.a(this.getWorld(), this.getPosition(), this.getBlock());
        }

    }

    public int getPage() {
        return this.page;
    }

    public int j() {
        float f = this.maxPage > 1 ? (float) this.getPage() / ((float) this.maxPage - 1.0F) : 1.0F;

        return MathHelper.d(f * 14.0F) + (this.hasBook() ? 1 : 0);
    }

    private ItemStack b(ItemStack itemstack, @Nullable EntityHuman entityhuman) {
        if (this.world instanceof WorldServer && itemstack.getItem() == Items.WRITTEN_BOOK) {
            ItemWrittenBook.a(itemstack, this.a(entityhuman), entityhuman);
        }

        return itemstack;
    }

    private CommandListenerWrapper a(@Nullable EntityHuman entityhuman) {
        String s;
        Object object;

        if (entityhuman == null) {
            s = "Lectern";
            object = new ChatComponentText("Lectern");
        } else {
            s = entityhuman.getDisplayName().getString();
            object = entityhuman.getScoreboardDisplayName();
        }

        Vec3D vec3d = Vec3D.a((BaseBlockPosition) this.position);

        return new CommandListenerWrapper(ICommandListener.DUMMY, vec3d, Vec2F.a, (WorldServer) this.world, 2, s, (IChatBaseComponent) object, this.world.getMinecraftServer(), entityhuman);
    }

    @Override
    public boolean isFilteredNBT() {
        return true;
    }

    @Override
    public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        super.load(iblockdata, nbttagcompound);
        if (nbttagcompound.hasKeyOfType("Book", 10)) {
            this.book = this.b(ItemStack.a(nbttagcompound.getCompound("Book")), (EntityHuman) null);
        } else {
            this.book = ItemStack.b;
        }

        this.maxPage = ItemWrittenBook.g(this.book);
        this.page = MathHelper.clamp(nbttagcompound.getInt("Page"), 0, this.maxPage - 1);
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        if (!this.getBook().isEmpty()) {
            nbttagcompound.set("Book", this.getBook().save(new NBTTagCompound()));
            nbttagcompound.setInt("Page", this.page);
        }

        return nbttagcompound;
    }

    @Override
    public void clear() {
        this.setBook(ItemStack.b);
    }

    @Override
    public Container createMenu(int i, PlayerInventory playerinventory, EntityHuman entityhuman) {
        return new ContainerLectern(i, this.inventory, this.containerProperties);
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return new ChatMessage("container.lectern");
    }
}
