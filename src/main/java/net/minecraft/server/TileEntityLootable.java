package net.minecraft.server;

import java.util.Random;
import javax.annotation.Nullable;

public abstract class TileEntityLootable extends TileEntityContainer {

    @Nullable
    public MinecraftKey lootTable;
    public long lootTableSeed;

    protected TileEntityLootable(TileEntityTypes<?> tileentitytypes) {
        super(tileentitytypes);
    }

    public static void a(IBlockAccess iblockaccess, Random random, BlockPosition blockposition, MinecraftKey minecraftkey) {
        TileEntity tileentity = iblockaccess.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityLootable) {
            ((TileEntityLootable) tileentity).setLootTable(minecraftkey, random.nextLong());
        }

    }

    protected boolean b(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKeyOfType("LootTable", 8)) {
            this.lootTable = new MinecraftKey(nbttagcompound.getString("LootTable"));
            this.lootTableSeed = nbttagcompound.getLong("LootTableSeed");
            return true;
        } else {
            return false;
        }
    }

    protected boolean c(NBTTagCompound nbttagcompound) {
        if (this.lootTable == null) {
            return false;
        } else {
            nbttagcompound.setString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                nbttagcompound.setLong("LootTableSeed", this.lootTableSeed);
            }

            return true;
        }
    }

    public void d(@Nullable EntityHuman entityhuman) {
        if (this.lootTable != null && this.world.getMinecraftServer() != null) {
            LootTable loottable = this.world.getMinecraftServer().getLootTableRegistry().getLootTable(this.lootTable);

            if (entityhuman instanceof EntityPlayer) {
                CriterionTriggers.N.a((EntityPlayer) entityhuman, this.lootTable);
            }

            this.lootTable = null;
            LootTableInfo.Builder loottableinfo_builder = (new LootTableInfo.Builder((WorldServer) this.world)).set(LootContextParameters.ORIGIN, Vec3D.a((BaseBlockPosition) this.position)).a(this.lootTableSeed);

            if (entityhuman != null) {
                loottableinfo_builder.a(entityhuman.eT()).set(LootContextParameters.THIS_ENTITY, entityhuman);
            }

            loottable.fillInventory(this, loottableinfo_builder.build(LootContextParameterSets.CHEST));
        }

    }

    public void setLootTable(MinecraftKey minecraftkey, long i) {
        this.lootTable = minecraftkey;
        this.lootTableSeed = i;
    }

    @Override
    public boolean isEmpty() {
        this.d((EntityHuman) null);
        return this.f().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int i) {
        this.d((EntityHuman) null);
        return (ItemStack) this.f().get(i);
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        this.d((EntityHuman) null);
        ItemStack itemstack = ContainerUtil.a(this.f(), i, j);

        if (!itemstack.isEmpty()) {
            this.update();
        }

        return itemstack;
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        this.d((EntityHuman) null);
        return ContainerUtil.a(this.f(), i);
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        this.d((EntityHuman) null);
        this.f().set(i, itemstack);
        if (itemstack.getCount() > this.getMaxStackSize()) {
            itemstack.setCount(this.getMaxStackSize());
        }

        this.update();
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.position) != this ? false : entityhuman.h((double) this.position.getX() + 0.5D, (double) this.position.getY() + 0.5D, (double) this.position.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clear() {
        this.f().clear();
    }

    protected abstract NonNullList<ItemStack> f();

    protected abstract void a(NonNullList<ItemStack> nonnulllist);

    @Override
    public boolean e(EntityHuman entityhuman) {
        return super.e(entityhuman) && (this.lootTable == null || !entityhuman.isSpectator());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerinventory, EntityHuman entityhuman) {
        if (this.e(entityhuman)) {
            this.d(playerinventory.player);
            return this.createContainer(i, playerinventory);
        } else {
            return null;
        }
    }
}
