package net.minecraft.server;

import java.util.Optional;
import java.util.Random;
import javax.annotation.Nullable;

public class TileEntityCampfire extends TileEntity implements Clearable, ITickable {

    private final NonNullList<ItemStack> items;
    public final int[] cookingTimes;
    public final int[] cookingTotalTimes;

    public TileEntityCampfire() {
        super(TileEntityTypes.CAMPFIRE);
        this.items = NonNullList.a(4, ItemStack.b);
        this.cookingTimes = new int[4];
        this.cookingTotalTimes = new int[4];
    }

    @Override
    public void tick() {
        boolean flag = (Boolean) this.getBlock().get(BlockCampfire.b);
        boolean flag1 = this.world.isClientSide;

        if (flag1) {
            if (flag) {
                this.j();
            }

        } else {
            if (flag) {
                this.h();
            } else {
                for (int i = 0; i < this.items.size(); ++i) {
                    if (this.cookingTimes[i] > 0) {
                        this.cookingTimes[i] = MathHelper.clamp(this.cookingTimes[i] - 2, 0, this.cookingTotalTimes[i]);
                    }
                }
            }

        }
    }

    private void h() {
        for (int i = 0; i < this.items.size(); ++i) {
            ItemStack itemstack = (ItemStack) this.items.get(i);

            if (!itemstack.isEmpty()) {
                int j = this.cookingTimes[i]++;

                if (this.cookingTimes[i] >= this.cookingTotalTimes[i]) {
                    InventorySubcontainer inventorysubcontainer = new InventorySubcontainer(new ItemStack[]{itemstack});
                    ItemStack itemstack1 = (ItemStack) this.world.getCraftingManager().craft(Recipes.CAMPFIRE_COOKING, inventorysubcontainer, this.world).map((recipecampfire) -> {
                        return recipecampfire.a(inventorysubcontainer);
                    }).orElse(itemstack);
                    BlockPosition blockposition = this.getPosition();

                    InventoryUtils.dropItem(this.world, (double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), itemstack1);
                    this.items.set(i, ItemStack.b);
                    this.k();
                }
            }
        }

    }

    private void j() {
        World world = this.getWorld();

        if (world != null) {
            BlockPosition blockposition = this.getPosition();
            Random random = world.random;
            int i;

            if (random.nextFloat() < 0.11F) {
                for (i = 0; i < random.nextInt(2) + 2; ++i) {
                    BlockCampfire.a(world, blockposition, (Boolean) this.getBlock().get(BlockCampfire.c), false);
                }
            }

            i = ((EnumDirection) this.getBlock().get(BlockCampfire.e)).get2DRotationValue();

            for (int j = 0; j < this.items.size(); ++j) {
                if (!((ItemStack) this.items.get(j)).isEmpty() && random.nextFloat() < 0.2F) {
                    EnumDirection enumdirection = EnumDirection.fromType2(Math.floorMod(j + i, 4));
                    float f = 0.3125F;
                    double d0 = (double) blockposition.getX() + 0.5D - (double) ((float) enumdirection.getAdjacentX() * 0.3125F) + (double) ((float) enumdirection.g().getAdjacentX() * 0.3125F);
                    double d1 = (double) blockposition.getY() + 0.5D;
                    double d2 = (double) blockposition.getZ() + 0.5D - (double) ((float) enumdirection.getAdjacentZ() * 0.3125F) + (double) ((float) enumdirection.g().getAdjacentZ() * 0.3125F);

                    for (int k = 0; k < 4; ++k) {
                        world.addParticle(Particles.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
                    }
                }
            }

        }
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        super.load(iblockdata, nbttagcompound);
        this.items.clear();
        ContainerUtil.b(nbttagcompound, this.items);
        int[] aint;

        if (nbttagcompound.hasKeyOfType("CookingTimes", 11)) {
            aint = nbttagcompound.getIntArray("CookingTimes");
            System.arraycopy(aint, 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, aint.length));
        }

        if (nbttagcompound.hasKeyOfType("CookingTotalTimes", 11)) {
            aint = nbttagcompound.getIntArray("CookingTotalTimes");
            System.arraycopy(aint, 0, this.cookingTotalTimes, 0, Math.min(this.cookingTotalTimes.length, aint.length));
        }

    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        this.b(nbttagcompound);
        nbttagcompound.setIntArray("CookingTimes", this.cookingTimes);
        nbttagcompound.setIntArray("CookingTotalTimes", this.cookingTotalTimes);
        return nbttagcompound;
    }

    private NBTTagCompound b(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        ContainerUtil.a(nbttagcompound, this.items, true);
        return nbttagcompound;
    }

    @Nullable
    @Override
    public PacketPlayOutTileEntityData getUpdatePacket() {
        return new PacketPlayOutTileEntityData(this.position, 13, this.b());
    }

    @Override
    public NBTTagCompound b() {
        return this.b(new NBTTagCompound());
    }

    public Optional<RecipeCampfire> a(ItemStack itemstack) {
        return this.items.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.world.getCraftingManager().craft(Recipes.CAMPFIRE_COOKING, new InventorySubcontainer(new ItemStack[]{itemstack}), this.world);
    }

    public boolean a(ItemStack itemstack, int i) {
        for (int j = 0; j < this.items.size(); ++j) {
            ItemStack itemstack1 = (ItemStack) this.items.get(j);

            if (itemstack1.isEmpty()) {
                this.cookingTotalTimes[j] = i;
                this.cookingTimes[j] = 0;
                this.items.set(j, itemstack.cloneAndSubtract(1));
                this.k();
                return true;
            }
        }

        return false;
    }

    private void k() {
        this.update();
        this.getWorld().notify(this.getPosition(), this.getBlock(), this.getBlock(), 3);
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    public void f() {
        if (this.world != null) {
            if (!this.world.isClientSide) {
                InventoryUtils.a(this.world, this.getPosition(), this.getItems());
            }

            this.k();
        }

    }
}
