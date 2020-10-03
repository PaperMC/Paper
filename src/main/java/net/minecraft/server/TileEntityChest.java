package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

public class TileEntityChest extends TileEntityLootable implements ITickable {

    private NonNullList<ItemStack> items;
    protected float a;
    protected float b;
    public int viewingCount;
    private int j;

    protected TileEntityChest(TileEntityTypes<?> tileentitytypes) {
        super(tileentitytypes);
        this.items = NonNullList.a(27, ItemStack.b);
    }

    public TileEntityChest() {
        this(TileEntityTypes.CHEST);
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    protected IChatBaseComponent getContainerName() {
        return new ChatMessage("container.chest");
    }

    @Override
    public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        super.load(iblockdata, nbttagcompound);
        this.items = NonNullList.a(this.getSize(), ItemStack.b);
        if (!this.b(nbttagcompound)) {
            ContainerUtil.b(nbttagcompound, this.items);
        }

    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        if (!this.c(nbttagcompound)) {
            ContainerUtil.a(nbttagcompound, this.items);
        }

        return nbttagcompound;
    }

    @Override
    public void tick() {
        int i = this.position.getX();
        int j = this.position.getY();
        int k = this.position.getZ();

        ++this.j;
        this.viewingCount = a(this.world, this, this.j, i, j, k, this.viewingCount);
        this.b = this.a;
        float f = 0.1F;

        if (this.viewingCount > 0 && this.a == 0.0F) {
            this.playOpenSound(SoundEffects.BLOCK_CHEST_OPEN);
        }

        if (this.viewingCount == 0 && this.a > 0.0F || this.viewingCount > 0 && this.a < 1.0F) {
            float f1 = this.a;

            if (this.viewingCount > 0) {
                this.a += 0.1F;
            } else {
                this.a -= 0.1F;
            }

            if (this.a > 1.0F) {
                this.a = 1.0F;
            }

            float f2 = 0.5F;

            if (this.a < 0.5F && f1 >= 0.5F) {
                this.playOpenSound(SoundEffects.BLOCK_CHEST_CLOSE);
            }

            if (this.a < 0.0F) {
                this.a = 0.0F;
            }
        }

    }

    public static int a(World world, TileEntityContainer tileentitycontainer, int i, int j, int k, int l, int i1) {
        if (!world.isClientSide && i1 != 0 && (i + j + k + l) % 200 == 0) {
            i1 = a(world, tileentitycontainer, j, k, l);
        }

        return i1;
    }

    public static int a(World world, TileEntityContainer tileentitycontainer, int i, int j, int k) {
        int l = 0;
        float f = 5.0F;
        List<EntityHuman> list = world.a(EntityHuman.class, new AxisAlignedBB((double) ((float) i - 5.0F), (double) ((float) j - 5.0F), (double) ((float) k - 5.0F), (double) ((float) (i + 1) + 5.0F), (double) ((float) (j + 1) + 5.0F), (double) ((float) (k + 1) + 5.0F)));
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityHuman entityhuman = (EntityHuman) iterator.next();

            if (entityhuman.activeContainer instanceof ContainerChest) {
                IInventory iinventory = ((ContainerChest) entityhuman.activeContainer).e();

                if (iinventory == tileentitycontainer || iinventory instanceof InventoryLargeChest && ((InventoryLargeChest) iinventory).a((IInventory) tileentitycontainer)) {
                    ++l;
                }
            }
        }

        return l;
    }

    public void playOpenSound(SoundEffect soundeffect) {
        BlockPropertyChestType blockpropertychesttype = (BlockPropertyChestType) this.getBlock().get(BlockChest.c);

        if (blockpropertychesttype != BlockPropertyChestType.LEFT) {
            double d0 = (double) this.position.getX() + 0.5D;
            double d1 = (double) this.position.getY() + 0.5D;
            double d2 = (double) this.position.getZ() + 0.5D;

            if (blockpropertychesttype == BlockPropertyChestType.RIGHT) {
                EnumDirection enumdirection = BlockChest.h(this.getBlock());

                d0 += (double) enumdirection.getAdjacentX() * 0.5D;
                d2 += (double) enumdirection.getAdjacentZ() * 0.5D;
            }

            this.world.playSound((EntityHuman) null, d0, d1, d2, soundeffect, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public boolean setProperty(int i, int j) {
        if (i == 1) {
            this.viewingCount = j;
            return true;
        } else {
            return super.setProperty(i, j);
        }
    }

    @Override
    public void startOpen(EntityHuman entityhuman) {
        if (!entityhuman.isSpectator()) {
            if (this.viewingCount < 0) {
                this.viewingCount = 0;
            }

            ++this.viewingCount;
            this.onOpen();
        }

    }

    @Override
    public void closeContainer(EntityHuman entityhuman) {
        if (!entityhuman.isSpectator()) {
            --this.viewingCount;
            this.onOpen();
        }

    }

    protected void onOpen() {
        Block block = this.getBlock().getBlock();

        if (block instanceof BlockChest) {
            this.world.playBlockAction(this.position, block, 1, this.viewingCount);
            this.world.applyPhysics(this.position, block);
        }

    }

    @Override
    protected NonNullList<ItemStack> f() {
        return this.items;
    }

    @Override
    protected void a(NonNullList<ItemStack> nonnulllist) {
        this.items = nonnulllist;
    }

    public static int a(IBlockAccess iblockaccess, BlockPosition blockposition) {
        IBlockData iblockdata = iblockaccess.getType(blockposition);

        if (iblockdata.getBlock().isTileEntity()) {
            TileEntity tileentity = iblockaccess.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityChest) {
                return ((TileEntityChest) tileentity).viewingCount;
            }
        }

        return 0;
    }

    public static void a(TileEntityChest tileentitychest, TileEntityChest tileentitychest1) {
        NonNullList<ItemStack> nonnulllist = tileentitychest.f();

        tileentitychest.a(tileentitychest1.f());
        tileentitychest1.a(nonnulllist);
    }

    @Override
    protected Container createContainer(int i, PlayerInventory playerinventory) {
        return ContainerChest.a(i, playerinventory, this);
    }
}
