package net.minecraft.server;

// CraftBukkit start
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Barrel;
import org.bukkit.block.Lectern;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
// CraftBukkit end

public class TileEntityBarrel extends TileEntityLootable {

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new ArrayList<>();
    private int maxStack = MAX_STACK;
    public boolean opened;

    @Override
    public List<ItemStack> getContents() {
        return this.items;
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return transaction;
    }

    @Override
    public int getMaxStackSize() {
       return maxStack;
    }

    @Override
    public void setMaxStackSize(int i) {
        maxStack = i;
    }
    // CraftBukkit end
    private NonNullList<ItemStack> items;
    private int b;

    private TileEntityBarrel(TileEntityTypes<?> tileentitytypes) {
        super(tileentitytypes);
        this.items = NonNullList.a(27, ItemStack.b);
    }

    public TileEntityBarrel() {
        this(TileEntityTypes.BARREL);
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
    public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        super.load(iblockdata, nbttagcompound);
        this.items = NonNullList.a(this.getSize(), ItemStack.b);
        if (!this.b(nbttagcompound)) {
            ContainerUtil.b(nbttagcompound, this.items);
        }

    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    protected NonNullList<ItemStack> f() {
        return this.items;
    }

    @Override
    protected void a(NonNullList<ItemStack> nonnulllist) {
        this.items = nonnulllist;
    }

    @Override
    protected IChatBaseComponent getContainerName() {
        return new ChatMessage("container.barrel");
    }

    @Override
    protected Container createContainer(int i, PlayerInventory playerinventory) {
        return ContainerChest.a(i, playerinventory, this);
    }

    @Override
    public void startOpen(EntityHuman entityhuman) {
        if (!entityhuman.isSpectator()) {
            if (this.b < 0) {
                this.b = 0;
            }

            ++this.b;
            IBlockData iblockdata = this.getBlock();
            boolean flag = (Boolean) iblockdata.get(BlockBarrel.OPEN);

            if (!flag) {
                this.playOpenSound(iblockdata, SoundEffects.BLOCK_BARREL_OPEN);
                this.setOpenFlag(iblockdata, true);
            }

            this.j();
        }

    }

    private void j() {
        this.world.getBlockTickList().a(this.getPosition(), this.getBlock().getBlock(), 5);
    }

    public void h() {
        int i = this.position.getX();
        int j = this.position.getY();
        int k = this.position.getZ();

        this.b = TileEntityChest.a(this.world, this, i, j, k);
        if (this.b > 0) {
            this.j();
        } else {
            IBlockData iblockdata = this.getBlock();

            if (!iblockdata.a(Blocks.BARREL)) {
                this.al_();
                return;
            }

            boolean flag = (Boolean) iblockdata.get(BlockBarrel.OPEN) && !opened; // CraftBukkit - only set flag if Barrel isn't set open by API.

            if (flag) {
                this.playOpenSound(iblockdata, SoundEffects.BLOCK_BARREL_CLOSE);
                this.setOpenFlag(iblockdata, false);
            }
        }

    }

    @Override
    public void closeContainer(EntityHuman entityhuman) {
        if (!entityhuman.isSpectator()) {
            --this.b;
        }

    }

    public void setOpenFlag(IBlockData iblockdata, boolean flag) {
        this.world.setTypeAndData(this.getPosition(), (IBlockData) iblockdata.set(BlockBarrel.OPEN, flag), 3);
    }

    public void playOpenSound(IBlockData iblockdata, SoundEffect soundeffect) {
        BaseBlockPosition baseblockposition = ((EnumDirection) iblockdata.get(BlockBarrel.a)).p();
        double d0 = (double) this.position.getX() + 0.5D + (double) baseblockposition.getX() / 2.0D;
        double d1 = (double) this.position.getY() + 0.5D + (double) baseblockposition.getY() / 2.0D;
        double d2 = (double) this.position.getZ() + 0.5D + (double) baseblockposition.getZ() / 2.0D;

        this.world.playSound((EntityHuman) null, d0, d1, d2, soundeffect, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }
}
