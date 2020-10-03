package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nullable;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
// CraftBukkit end

public class TileEntityHopper extends TileEntityLootable implements IHopper, ITickable {

    private NonNullList<ItemStack> items;
    private int j;
    private long k;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.items;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    @Override
    public int getMaxStackSize() {
        return maxStack;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public TileEntityHopper() {
        super(TileEntityTypes.HOPPER);
        this.items = NonNullList.a(5, ItemStack.b);
        this.j = -1;
    }

    @Override
    public void load(IBlockData iblockdata, NBTTagCompound nbttagcompound) {
        super.load(iblockdata, nbttagcompound);
        this.items = NonNullList.a(this.getSize(), ItemStack.b);
        if (!this.b(nbttagcompound)) {
            ContainerUtil.b(nbttagcompound, this.items);
        }

        this.j = nbttagcompound.getInt("TransferCooldown");
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        if (!this.c(nbttagcompound)) {
            ContainerUtil.a(nbttagcompound, this.items);
        }

        nbttagcompound.setInt("TransferCooldown", this.j);
        return nbttagcompound;
    }

    @Override
    public int getSize() {
        return this.items.size();
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        this.d((EntityHuman) null);
        return ContainerUtil.a(this.f(), i, j);
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        this.d((EntityHuman) null);
        this.f().set(i, itemstack);
        if (itemstack.getCount() > this.getMaxStackSize()) {
            itemstack.setCount(this.getMaxStackSize());
        }

    }

    @Override
    protected IChatBaseComponent getContainerName() {
        return new ChatMessage("container.hopper");
    }

    @Override
    public void tick() {
        if (this.world != null && !this.world.isClientSide) {
            --this.j;
            this.k = this.world.getTime();
            if (!this.m()) {
                this.setCooldown(0);
                this.a(() -> {
                    return a((IHopper) this);
                });
            }

        }
    }

    private boolean a(Supplier<Boolean> supplier) {
        if (this.world != null && !this.world.isClientSide) {
            if (!this.m() && (Boolean) this.getBlock().get(BlockHopper.ENABLED)) {
                boolean flag = false;

                if (!this.isEmpty()) {
                    flag = this.k();
                }

                if (!this.j()) {
                    flag |= (Boolean) supplier.get();
                }

                if (flag) {
                    this.setCooldown(8);
                    this.update();
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    private boolean j() {
        Iterator iterator = this.items.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (!itemstack.isEmpty() && itemstack.getCount() == itemstack.getMaxStackSize());

        return false;
    }

    private boolean k() {
        IInventory iinventory = this.l();

        if (iinventory == null) {
            return false;
        } else {
            EnumDirection enumdirection = ((EnumDirection) this.getBlock().get(BlockHopper.FACING)).opposite();

            if (this.b(iinventory, enumdirection)) {
                return false;
            } else {
                for (int i = 0; i < this.getSize(); ++i) {
                    if (!this.getItem(i).isEmpty()) {
                        ItemStack itemstack = this.getItem(i).cloneItemStack();
                        // ItemStack itemstack1 = addItem(this, iinventory, this.splitStack(i, 1), enumdirection);

                        // CraftBukkit start - Call event when pushing items into other inventories
                        CraftItemStack oitemstack = CraftItemStack.asCraftMirror(this.splitStack(i, 1));

                        Inventory destinationInventory;
                        // Have to special case large chests as they work oddly
                        if (iinventory instanceof InventoryLargeChest) {
                            destinationInventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((InventoryLargeChest) iinventory);
                        } else {
                            destinationInventory = iinventory.getOwner().getInventory();
                        }

                        InventoryMoveItemEvent event = new InventoryMoveItemEvent(this.getOwner().getInventory(), oitemstack.clone(), destinationInventory, true);
                        this.getWorld().getServer().getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            this.setItem(i, itemstack);
                            this.setCooldown(8); // Delay hopper checks
                            return false;
                        }
                        ItemStack itemstack1 = addItem(this, iinventory, CraftItemStack.asNMSCopy(event.getItem()), enumdirection);
                        // CraftBukkit end

                        if (itemstack1.isEmpty()) {
                            iinventory.update();
                            return true;
                        }

                        this.setItem(i, itemstack);
                    }
                }

                return false;
            }
        }
    }

    private static IntStream a(IInventory iinventory, EnumDirection enumdirection) {
        return iinventory instanceof IWorldInventory ? IntStream.of(((IWorldInventory) iinventory).getSlotsForFace(enumdirection)) : IntStream.range(0, iinventory.getSize());
    }

    private boolean b(IInventory iinventory, EnumDirection enumdirection) {
        return a(iinventory, enumdirection).allMatch((i) -> {
            ItemStack itemstack = iinventory.getItem(i);

            return itemstack.getCount() >= itemstack.getMaxStackSize();
        });
    }

    private static boolean c(IInventory iinventory, EnumDirection enumdirection) {
        return a(iinventory, enumdirection).allMatch((i) -> {
            return iinventory.getItem(i).isEmpty();
        });
    }

    public static boolean a(IHopper ihopper) {
        IInventory iinventory = b(ihopper);

        if (iinventory != null) {
            EnumDirection enumdirection = EnumDirection.DOWN;

            return c(iinventory, enumdirection) ? false : a(iinventory, enumdirection).anyMatch((i) -> {
                return a(ihopper, iinventory, i, enumdirection);
            });
        } else {
            Iterator iterator = c(ihopper).iterator();

            EntityItem entityitem;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                entityitem = (EntityItem) iterator.next();
            } while (!a((IInventory) ihopper, entityitem));

            return true;
        }
    }

    private static boolean a(IHopper ihopper, IInventory iinventory, int i, EnumDirection enumdirection) {
        ItemStack itemstack = iinventory.getItem(i);

        if (!itemstack.isEmpty() && b(iinventory, itemstack, i, enumdirection)) {
            ItemStack itemstack1 = itemstack.cloneItemStack();
            // ItemStack itemstack2 = addItem(iinventory, ihopper, iinventory.splitStack(i, 1), (EnumDirection) null);
            // CraftBukkit start - Call event on collection of items from inventories into the hopper
            CraftItemStack oitemstack = CraftItemStack.asCraftMirror(iinventory.splitStack(i, 1));

            Inventory sourceInventory;
            // Have to special case large chests as they work oddly
            if (iinventory instanceof InventoryLargeChest) {
                sourceInventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((InventoryLargeChest) iinventory);
            } else {
                sourceInventory = iinventory.getOwner().getInventory();
            }

            InventoryMoveItemEvent event = new InventoryMoveItemEvent(sourceInventory, oitemstack.clone(), ihopper.getOwner().getInventory(), false);

            ihopper.getWorld().getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                iinventory.setItem(i, itemstack1);

                if (ihopper instanceof TileEntityHopper) {
                    ((TileEntityHopper) ihopper).setCooldown(8); // Delay hopper checks
                } else if (ihopper instanceof EntityMinecartHopper) {
                    ((EntityMinecartHopper) ihopper).setCooldown(4); // Delay hopper minecart checks
                }

                return false;
            }
            ItemStack itemstack2 = addItem(iinventory, ihopper, CraftItemStack.asNMSCopy(event.getItem()), null);
            // CraftBukkit end

            if (itemstack2.isEmpty()) {
                iinventory.update();
                return true;
            }

            iinventory.setItem(i, itemstack1);
        }

        return false;
    }

    public static boolean a(IInventory iinventory, EntityItem entityitem) {
        boolean flag = false;
        // CraftBukkit start
        InventoryPickupItemEvent event = new InventoryPickupItemEvent(iinventory.getOwner().getInventory(), (org.bukkit.entity.Item) entityitem.getBukkitEntity());
        entityitem.world.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        // CraftBukkit end
        ItemStack itemstack = entityitem.getItemStack().cloneItemStack();
        ItemStack itemstack1 = addItem((IInventory) null, iinventory, itemstack, (EnumDirection) null);

        if (itemstack1.isEmpty()) {
            flag = true;
            entityitem.die();
        } else {
            entityitem.setItemStack(itemstack1);
        }

        return flag;
    }

    public static ItemStack addItem(@Nullable IInventory iinventory, IInventory iinventory1, ItemStack itemstack, @Nullable EnumDirection enumdirection) {
        if (iinventory1 instanceof IWorldInventory && enumdirection != null) {
            IWorldInventory iworldinventory = (IWorldInventory) iinventory1;
            int[] aint = iworldinventory.getSlotsForFace(enumdirection);

            for (int i = 0; i < aint.length && !itemstack.isEmpty(); ++i) {
                itemstack = a(iinventory, iinventory1, itemstack, aint[i], enumdirection);
            }
        } else {
            int j = iinventory1.getSize();

            for (int k = 0; k < j && !itemstack.isEmpty(); ++k) {
                itemstack = a(iinventory, iinventory1, itemstack, k, enumdirection);
            }
        }

        return itemstack;
    }

    private static boolean a(IInventory iinventory, ItemStack itemstack, int i, @Nullable EnumDirection enumdirection) {
        return !iinventory.b(i, itemstack) ? false : !(iinventory instanceof IWorldInventory) || ((IWorldInventory) iinventory).canPlaceItemThroughFace(i, itemstack, enumdirection);
    }

    private static boolean b(IInventory iinventory, ItemStack itemstack, int i, EnumDirection enumdirection) {
        return !(iinventory instanceof IWorldInventory) || ((IWorldInventory) iinventory).canTakeItemThroughFace(i, itemstack, enumdirection);
    }

    private static ItemStack a(@Nullable IInventory iinventory, IInventory iinventory1, ItemStack itemstack, int i, @Nullable EnumDirection enumdirection) {
        ItemStack itemstack1 = iinventory1.getItem(i);

        if (a(iinventory1, itemstack, i, enumdirection)) {
            boolean flag = false;
            boolean flag1 = iinventory1.isEmpty();

            if (itemstack1.isEmpty()) {
                iinventory1.setItem(i, itemstack);
                itemstack = ItemStack.b;
                flag = true;
            } else if (a(itemstack1, itemstack)) {
                int j = itemstack.getMaxStackSize() - itemstack1.getCount();
                int k = Math.min(itemstack.getCount(), j);

                itemstack.subtract(k);
                itemstack1.add(k);
                flag = k > 0;
            }

            if (flag) {
                if (flag1 && iinventory1 instanceof TileEntityHopper) {
                    TileEntityHopper tileentityhopper = (TileEntityHopper) iinventory1;

                    if (!tileentityhopper.y()) {
                        byte b0 = 0;

                        if (iinventory instanceof TileEntityHopper) {
                            TileEntityHopper tileentityhopper1 = (TileEntityHopper) iinventory;

                            if (tileentityhopper.k >= tileentityhopper1.k) {
                                b0 = 1;
                            }
                        }

                        tileentityhopper.setCooldown(8 - b0);
                    }
                }

                iinventory1.update();
            }
        }

        return itemstack;
    }

    @Nullable
    private IInventory l() {
        EnumDirection enumdirection = (EnumDirection) this.getBlock().get(BlockHopper.FACING);

        return b(this.getWorld(), this.position.shift(enumdirection));
    }

    @Nullable
    public static IInventory b(IHopper ihopper) {
        return a(ihopper.getWorld(), ihopper.x(), ihopper.z() + 1.0D, ihopper.A());
    }

    public static List<EntityItem> c(IHopper ihopper) {
        return (List) ihopper.aa_().d().stream().flatMap((axisalignedbb) -> {
            return ihopper.getWorld().a(EntityItem.class, axisalignedbb.d(ihopper.x() - 0.5D, ihopper.z() - 0.5D, ihopper.A() - 0.5D), IEntitySelector.a).stream();
        }).collect(Collectors.toList());
    }

    @Nullable
    public static IInventory b(World world, BlockPosition blockposition) {
        return a(world, (double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D);
    }

    @Nullable
    public static IInventory a(World world, double d0, double d1, double d2) {
        Object object = null;
        BlockPosition blockposition = new BlockPosition(d0, d1, d2);
        IBlockData iblockdata = world.getType(blockposition);
        Block block = iblockdata.getBlock();

        if (block instanceof IInventoryHolder) {
            object = ((IInventoryHolder) block).a(iblockdata, world, blockposition);
        } else if (block.isTileEntity()) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof IInventory) {
                object = (IInventory) tileentity;
                if (object instanceof TileEntityChest && block instanceof BlockChest) {
                    object = BlockChest.getInventory((BlockChest) block, iblockdata, world, blockposition, true);
                }
            }
        }

        if (object == null) {
            List<Entity> list = world.getEntities((Entity) null, new AxisAlignedBB(d0 - 0.5D, d1 - 0.5D, d2 - 0.5D, d0 + 0.5D, d1 + 0.5D, d2 + 0.5D), IEntitySelector.d);

            if (!list.isEmpty()) {
                object = (IInventory) list.get(world.random.nextInt(list.size()));
            }
        }

        return (IInventory) object;
    }

    private static boolean a(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.getItem() != itemstack1.getItem() ? false : (itemstack.getDamage() != itemstack1.getDamage() ? false : (itemstack.getCount() > itemstack.getMaxStackSize() ? false : ItemStack.equals(itemstack, itemstack1)));
    }

    @Override
    public double x() {
        return (double) this.position.getX() + 0.5D;
    }

    @Override
    public double z() {
        return (double) this.position.getY() + 0.5D;
    }

    @Override
    public double A() {
        return (double) this.position.getZ() + 0.5D;
    }

    private void setCooldown(int i) {
        this.j = i;
    }

    private boolean m() {
        return this.j > 0;
    }

    private boolean y() {
        return this.j > 8;
    }

    @Override
    protected NonNullList<ItemStack> f() {
        return this.items;
    }

    @Override
    protected void a(NonNullList<ItemStack> nonnulllist) {
        this.items = nonnulllist;
    }

    public void a(Entity entity) {
        if (entity instanceof EntityItem) {
            BlockPosition blockposition = this.getPosition();

            if (VoxelShapes.c(VoxelShapes.a(entity.getBoundingBox().d((double) (-blockposition.getX()), (double) (-blockposition.getY()), (double) (-blockposition.getZ()))), this.aa_(), OperatorBoolean.AND)) {
                this.a(() -> {
                    return a((IInventory) this, (EntityItem) entity);
                });
            }
        }

    }

    @Override
    protected Container createContainer(int i, PlayerInventory playerinventory) {
        return new ContainerHopper(i, playerinventory, this);
    }
}
