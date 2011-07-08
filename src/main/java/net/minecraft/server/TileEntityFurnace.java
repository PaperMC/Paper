package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
// CraftBukkit end

public class TileEntityFurnace extends TileEntity implements IInventory {

    private ItemStack[] items = new ItemStack[3];
    public int burnTime = 0;
    public int ticksForCurrentFuel = 0;
    public int cookTime = 0;

    // CraftBukkit start
    private int lastTick = (int) (System.currentTimeMillis() / 50);
    public ItemStack[] getContents() {
        return this.items;
    }
    // CraftBukkit end

    public TileEntityFurnace() {}

    public int getSize() {
        return this.items.length;
    }

    public ItemStack getItem(int i) {
        return this.items[i];
    }

    public ItemStack splitStack(int i, int j) {
        if (this.items[i] != null) {
            ItemStack itemstack;

            if (this.items[i].count <= j) {
                itemstack = this.items[i];
                this.items[i] = null;
                return itemstack;
            } else {
                itemstack = this.items[i].a(j);
                if (this.items[i].count == 0) {
                    this.items[i] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        this.items[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }
    }

    public String getName() {
        return "Furnace";
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.l("Items");

        this.items = new ItemStack[this.getSize()];

        for (int i = 0; i < nbttaglist.c(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.a(i);
            byte b0 = nbttagcompound1.c("Slot");

            if (b0 >= 0 && b0 < this.items.length) {
                this.items[b0] = new ItemStack(nbttagcompound1);
            }
        }

        this.burnTime = nbttagcompound.d("BurnTime");
        this.cookTime = nbttagcompound.d("CookTime");
        this.ticksForCurrentFuel = this.fuelTime(this.items[1]);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("BurnTime", (short) this.burnTime);
        nbttagcompound.a("CookTime", (short) this.cookTime);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.a("Slot", (byte) i);
                this.items[i].a(nbttagcompound1);
                nbttaglist.a((NBTBase) nbttagcompound1);
            }
        }

        nbttagcompound.a("Items", (NBTBase) nbttaglist);
    }

    public int getMaxStackSize() {
        return 64;
    }

    public boolean isBurning() {
        return this.burnTime > 0;
    }

    public void g_() {
        boolean flag = this.burnTime > 0;
        boolean flag1 = false;

        // CraftBukkit start
        int currentTick = (int) (System.currentTimeMillis() / 50); // CraftBukkit
        int elapsedTicks = currentTick - this.lastTick;
        this.lastTick = currentTick;

        // CraftBukkit - moved from below
        if (this.isBurning() && this.canBurn()) {
            this.cookTime += elapsedTicks;
            if (this.cookTime >= 200) {
                this.cookTime %= 200;
                this.burn();
                flag1 = true;
            }
        } else {
            this.cookTime = 0;
        }
        // CraftBukkit end

        if (this.burnTime > 0) {
            this.burnTime -= elapsedTicks; // CraftBukkit
        }

        if (!this.world.isStatic) {
            // CraftBukkit start - handle multiple elapsed ticks
            if (this.burnTime <= 0 && this.canBurn() && this.items[1] != null) { // CraftBukkit - == to <=
                CraftItemStack fuel = new CraftItemStack(this.items[1]);

                FurnaceBurnEvent furnaceBurnEvent = new FurnaceBurnEvent(this.world.getWorld().getBlockAt(this.x, this.y, this.z), fuel, this.fuelTime(this.items[1]));
                this.world.getServer().getPluginManager().callEvent(furnaceBurnEvent);

                if (furnaceBurnEvent.isCancelled()) {
                    return;
                }

                this.ticksForCurrentFuel = furnaceBurnEvent.getBurnTime();
                this.burnTime += this.ticksForCurrentFuel;
                if (this.burnTime > 0 && furnaceBurnEvent.isBurning()) {
                    // CraftBukkit end
                    flag1 = true;
                    if (this.items[1] != null) {
                        --this.items[1].count;
                        if (this.items[1].count == 0) {
                            this.items[1] = null;
                        }
                    }
                }
            }

            /* CraftBukkit start - moved up
            if (this.f() && this.process()) {
                ++this.cookTime;
                if (this.cookTime == 200) {
                    this.cookTime = 0;
                    this.burn();
                    flag1 = true;
                }
            } else {
                this.cookTime = 0;
            }
            // CraftBukkit end */

            if (flag != this.burnTime > 0) {
                flag1 = true;
                BlockFurnace.a(this.burnTime > 0, this.world, this.x, this.y, this.z);
            }
        }

        if (flag1) {
            this.update();
        }
    }

    private boolean canBurn() {
        if (this.items[0] == null) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.getInstance().a(this.items[0].getItem().id);

            // CraftBukkit - consider resultant count instead of current count
            return itemstack == null ? false : (this.items[2] == null ? true : (!this.items[2].doMaterialsMatch(itemstack) ? false : (this.items[2].count + itemstack.count <= this.getMaxStackSize() && this.items[2].count < this.items[2].getMaxStackSize() ? true : this.items[2].count + itemstack.count <= itemstack.getMaxStackSize())));
        }
    }

    public void burn() {
        if (this.canBurn()) {
            ItemStack itemstack = FurnaceRecipes.getInstance().a(this.items[0].getItem().id);

            // CraftBukkit start
            CraftItemStack source = new CraftItemStack(this.items[0]);
            CraftItemStack result = new CraftItemStack(itemstack.cloneItemStack());

            FurnaceSmeltEvent furnaceSmeltEvent = new FurnaceSmeltEvent(this.world.getWorld().getBlockAt(this.x, this.y, this.z), source, result);
            this.world.getServer().getPluginManager().callEvent(furnaceSmeltEvent);

            if (furnaceSmeltEvent.isCancelled()) {
                return;
            }

            org.bukkit.inventory.ItemStack oldResult = furnaceSmeltEvent.getResult();
            ItemStack newResult = new ItemStack(oldResult.getTypeId(), oldResult.getAmount(), oldResult.getDurability());
            itemstack = newResult;

            if (this.items[2] == null) {
                this.items[2] = itemstack.cloneItemStack();
            } else if (this.items[2].id == itemstack.id) {
                // CraftBukkit - compare damage too
                if (this.items[2].damage == itemstack.damage) {
                    this.items[2].count += itemstack.count;
                }
                // CraftBukkit end
            }

            --this.items[0].count;
            if (this.items[0].count <= 0) {
                this.items[0] = null;
            }
        }
    }

    private int fuelTime(ItemStack itemstack) {
        if (itemstack == null) {
            return 0;
        } else {
            int i = itemstack.getItem().id;

            return i < 256 && Block.byId[i].material == Material.WOOD ? 300 : (i == Item.STICK.id ? 100 : (i == Item.COAL.id ? 1600 : (i == Item.LAVA_BUCKET.id ? 20000 : (i == Block.SAPLING.id ? 100 : 0))));
        }
    }

    public boolean a_(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.x, this.y, this.z) != this ? false : entityhuman.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }
}
