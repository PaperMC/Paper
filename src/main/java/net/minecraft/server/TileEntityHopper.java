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
                // Spigot start
                boolean result = this.a(() -> {
                    return a((IHopper) this);
                });
                if (!result && this.world.spigotConfig.hopperCheck > 1) {
                    this.setCooldown(this.world.spigotConfig.hopperCheck);
                }
                // Spigot end
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
                    this.setCooldown(world.spigotConfig.hopperTransfer); // Spigot
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

    // Paper start - Optimize Hoppers
    private static boolean skipPullModeEventFire = false;
    private static boolean skipPushModeEventFire = false;
    static boolean skipHopperEvents = false;

    private boolean hopperPush(IInventory iinventory, EnumDirection enumdirection) {
        skipPushModeEventFire = skipHopperEvents;
        boolean foundItem = false;
        for (int i = 0; i < this.getSize(); ++i) {
            ItemStack item = this.getItem(i);
            if (!item.isEmpty()) {
                foundItem = true;
                ItemStack origItemStack = item;
                ItemStack itemstack = origItemStack;

                final int origCount = origItemStack.getCount();
                final int moved = Math.min(world.spigotConfig.hopperAmount, origCount);
                origItemStack.setCount(moved);

                // We only need to fire the event once to give protection plugins a chance to cancel this event
                // Because nothing uses getItem, every event call should end up the same result.
                if (!skipPushModeEventFire) {
                    itemstack = callPushMoveEvent(iinventory, itemstack);
                    if (itemstack == null) { // cancelled
                        origItemStack.setCount(origCount);
                        return false;
                    }
                }
                final ItemStack itemstack2 = addItem(this, iinventory, itemstack, enumdirection);
                final int remaining = itemstack2.getCount();
                if (remaining != moved) {
                    origItemStack = origItemStack.cloneItemStack(true);
                    origItemStack.setCount(origCount);
                    if (!origItemStack.isEmpty()) {
                        origItemStack.setCount(origCount - moved + remaining);
                    }
                    this.setItem(i, origItemStack);
                    iinventory.update();
                    return true;
                }
                origItemStack.setCount(origCount);
            }
        }
        if (foundItem && world.paperConfig.cooldownHopperWhenFull) { // Inventory was full - cooldown
            this.setCooldown(world.spigotConfig.hopperTransfer);
        }
        return false;
    }

    private static boolean hopperPull(IHopper ihopper, IInventory iinventory, ItemStack origItemStack, int i) {
        ItemStack itemstack = origItemStack;
        final int origCount = origItemStack.getCount();
        final World world = ihopper.getWorld();
        final int moved = Math.min(world.spigotConfig.hopperAmount, origCount);
        itemstack.setCount(moved);

        if (!skipPullModeEventFire) {
            itemstack = callPullMoveEvent(ihopper, iinventory, itemstack);
            if (itemstack == null) { // cancelled
                origItemStack.setCount(origCount);
                // Drastically improve performance by returning true.
                // No plugin could of relied on the behavior of false as the other call
                // site for IMIE did not exhibit the same behavior
                return true;
            }
        }

        final ItemStack itemstack2 = addItem(iinventory, ihopper, itemstack, null);
        final int remaining = itemstack2.getCount();
        if (remaining != moved) {
            origItemStack = origItemStack.cloneItemStack(true);
            origItemStack.setCount(origCount);
            if (!origItemStack.isEmpty()) {
                origItemStack.setCount(origCount - moved + remaining);
            }
            IGNORE_TILE_UPDATES = true;
            iinventory.setItem(i, origItemStack);
            IGNORE_TILE_UPDATES = false;
            iinventory.update();
            return true;
        }
        origItemStack.setCount(origCount);

        if (world.paperConfig.cooldownHopperWhenFull) {
            cooldownHopper(ihopper);
        }

        return false;
    }

    private ItemStack callPushMoveEvent(IInventory iinventory, ItemStack itemstack) {
        Inventory destinationInventory = getInventory(iinventory);
        InventoryMoveItemEvent event = new InventoryMoveItemEvent(this.getOwner(false).getInventory(),
            CraftItemStack.asCraftMirror(itemstack), destinationInventory, true);
        boolean result = event.callEvent();
        if (!event.calledGetItem && !event.calledSetItem) {
            skipPushModeEventFire = true;
        }
        if (!result) {
            cooldownHopper(this);
            return null;
        }

        if (event.calledSetItem) {
            return CraftItemStack.asNMSCopy(event.getItem());
        } else {
            return itemstack;
        }
    }

    private static ItemStack callPullMoveEvent(IHopper hopper, IInventory iinventory, ItemStack itemstack) {
        Inventory sourceInventory = getInventory(iinventory);
        Inventory destination = getInventory(hopper);

        InventoryMoveItemEvent event = new InventoryMoveItemEvent(sourceInventory,
            // Mirror is safe as we no plugins ever use this item
            CraftItemStack.asCraftMirror(itemstack), destination, false);
        boolean result = event.callEvent();
        if (!event.calledGetItem && !event.calledSetItem) {
            skipPullModeEventFire = true;
        }
        if (!result) {
            cooldownHopper(hopper);
            return null;
        }

        if (event.calledSetItem) {
            return CraftItemStack.asNMSCopy(event.getItem());
        } else {
            return itemstack;
        }
    }

    private static Inventory getInventory(IInventory iinventory) {
        Inventory sourceInventory;// Have to special case large chests as they work oddly
        if (iinventory instanceof InventoryLargeChest) {
            sourceInventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((InventoryLargeChest) iinventory);
        } else if (iinventory instanceof TileEntity) {
            sourceInventory = ((TileEntity) iinventory).getOwner(false).getInventory();
        } else {
            sourceInventory = iinventory.getOwner().getInventory();
        }
        return sourceInventory;
    }

    private static void cooldownHopper(IHopper hopper) {
        if (hopper instanceof TileEntityHopper) {
            ((TileEntityHopper) hopper).setCooldown(hopper.getWorld().spigotConfig.hopperTransfer);
        } else if (hopper instanceof EntityMinecartHopper) {
            ((EntityMinecartHopper) hopper).setCooldown(hopper.getWorld().spigotConfig.hopperTransfer / 2);
        }
    }
    // Paper end

    private boolean k() {
        IInventory iinventory = this.l();

        if (iinventory == null) {
            return false;
        } else {
            EnumDirection enumdirection = ((EnumDirection) this.getBlock().get(BlockHopper.FACING)).opposite();

            if (this.b(iinventory, enumdirection)) {
                return false;
            } else {
                return hopperPush(iinventory, enumdirection); /* // Paper - disable rest
                for (int i = 0; i < this.getSize(); ++i) {
                    if (!this.getItem(i).isEmpty()) {
                        ItemStack itemstack = this.getItem(i).cloneItemStack();
                        // ItemStack itemstack1 = addItem(this, iinventory, this.splitStack(i, 1), enumdirection);

                        // CraftBukkit start - Call event when pushing items into other inventories
                        CraftItemStack oitemstack = CraftItemStack.asCraftMirror(this.splitStack(i, world.spigotConfig.hopperAmount)); // Spigot

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
                            this.setCooldown(world.spigotConfig.hopperTransfer); // Spigot
                            return false;
                        }
                        int origCount = event.getItem().getAmount(); // Spigot
                        ItemStack itemstack1 = addItem(this, iinventory, CraftItemStack.asNMSCopy(event.getItem()), enumdirection);
                        // CraftBukkit end

                        if (itemstack1.isEmpty()) {
                            iinventory.update();
                            return true;
                        }

                        itemstack.subtract(origCount - itemstack1.getCount()); // Spigot
                        this.setItem(i, itemstack);
                    }
                }

                return false;*/ // Paper - end commenting out replaced block for Hopper Optimizations
            }
        }
    }

    private static IntStream a(IInventory iinventory, EnumDirection enumdirection) {
        return iinventory instanceof IWorldInventory ? IntStream.of(((IWorldInventory) iinventory).getSlotsForFace(enumdirection)) : IntStream.range(0, iinventory.getSize());
    }

    private static boolean allMatch(IInventory iinventory, EnumDirection enumdirection, java.util.function.BiPredicate<ItemStack, Integer> test) {
        if (iinventory instanceof IWorldInventory) {
            for (int i : ((IWorldInventory) iinventory).getSlotsForFace(enumdirection)) {
                if (!test.test(iinventory.getItem(i), i)) {
                    return false;
                }
            }
        } else {
            int size = iinventory.getSize();
            for (int i = 0; i < size; i++) {
                if (!test.test(iinventory.getItem(i), i)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean anyMatch(IInventory iinventory, EnumDirection enumdirection, java.util.function.BiPredicate<ItemStack, Integer> test) {
        if (iinventory instanceof IWorldInventory) {
            for (int i : ((IWorldInventory) iinventory).getSlotsForFace(enumdirection)) {
                if (test.test(iinventory.getItem(i), i)) {
                    return true;
                }
            }
        } else {
            int size = iinventory.getSize();
            for (int i = 0; i < size; i++) {
                if (test.test(iinventory.getItem(i), i)) {
                    return true;
                }
            }
        }
        return true;
    }
    private static final java.util.function.BiPredicate<ItemStack, Integer> STACK_SIZE_TEST = (itemstack, i) -> itemstack.getCount() >= itemstack.getMaxStackSize();
    private static final java.util.function.BiPredicate<ItemStack, Integer> IS_EMPTY_TEST = (itemstack, i) -> itemstack.isEmpty();

    // Paper end

    private boolean b(IInventory iinventory, EnumDirection enumdirection) {
        // Paper start - no streams
        return allMatch(iinventory, enumdirection, STACK_SIZE_TEST);
        // Paper end
    }

    private static boolean c(IInventory iinventory, EnumDirection enumdirection) {
        return allMatch(iinventory, enumdirection, IS_EMPTY_TEST);
    }

    public static boolean a(IHopper ihopper) {
        IInventory iinventory = b(ihopper);

        if (iinventory != null) {
            EnumDirection enumdirection = EnumDirection.DOWN;

            // Paper start - optimize hoppers and remove streams
            skipPullModeEventFire = skipHopperEvents;
            return !c(iinventory, enumdirection) && anyMatch(iinventory, enumdirection, (item, i) -> {
                // Logic copied from below to avoid extra getItem calls
                if (!item.isEmpty() && canTakeItem(iinventory, item, i, enumdirection)) {
                    return hopperPull(ihopper, iinventory, item, i);
                } else {
                    return false;
                }
            });
            // Paper end
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

    private static boolean a(IHopper ihopper, IInventory iinventory, int i, EnumDirection enumdirection) {// Paper - method unused as logic is inlined above
        ItemStack itemstack = iinventory.getItem(i);

        if (!itemstack.isEmpty() && b(iinventory, itemstack, i, enumdirection)) { // If this logic changes, update above. this is left inused incase reflective plugins
            return hopperPull(ihopper, iinventory, itemstack, i); /* // Paper - disable rest
            ItemStack itemstack1 = itemstack.cloneItemStack();
            // ItemStack itemstack2 = addItem(iinventory, ihopper, iinventory.splitStack(i, 1), (EnumDirection) null);
            // CraftBukkit start - Call event on collection of items from inventories into the hopper
            CraftItemStack oitemstack = CraftItemStack.asCraftMirror(iinventory.splitStack(i, ihopper.getWorld().spigotConfig.hopperAmount)); // Spigot

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
                    ((TileEntityHopper) ihopper).setCooldown(ihopper.getWorld().spigotConfig.hopperTransfer); // Spigot
                } else if (ihopper instanceof EntityMinecartHopper) {
                    ((EntityMinecartHopper) ihopper).setCooldown(ihopper.getWorld().spigotConfig.hopperTransfer / 2); // Spigot
                }
                return false;
            }
            int origCount = event.getItem().getAmount(); // Spigot
            ItemStack itemstack2 = addItem(iinventory, ihopper, CraftItemStack.asNMSCopy(event.getItem()), null);
            // CraftBukkit end

            if (itemstack2.isEmpty()) {
                iinventory.update();
                return true;
            }

            itemstack1.subtract(origCount - itemstack2.getCount()); // Spigot
            iinventory.setItem(i, itemstack1);*/ // Paper - end commenting out replaced block for Hopper Optimizations
        }

        return false;
    }

    public static boolean a(IInventory iinventory, EntityItem entityitem) {
        boolean flag = false;
        // CraftBukkit start
        InventoryPickupItemEvent event = new InventoryPickupItemEvent(getInventory(iinventory), (org.bukkit.entity.Item) entityitem.getBukkitEntity()); // Paper - use getInventory() to avoid snapshot creation
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

    private static boolean canTakeItem(IInventory iinventory, ItemStack itemstack, int i, EnumDirection enumdirection) { return b(iinventory, itemstack, i, enumdirection); } // Paper - OBFHELPER
    private static boolean b(IInventory iinventory, ItemStack itemstack, int i, EnumDirection enumdirection) {
        return !(iinventory instanceof IWorldInventory) || ((IWorldInventory) iinventory).canTakeItemThroughFace(i, itemstack, enumdirection);
    }

    private static ItemStack a(@Nullable IInventory iinventory, IInventory iinventory1, ItemStack itemstack, int i, @Nullable EnumDirection enumdirection) {
        ItemStack itemstack1 = iinventory1.getItem(i);

        if (a(iinventory1, itemstack, i, enumdirection)) {
            boolean flag = false;
            boolean flag1 = iinventory1.isEmpty();

            if (itemstack1.isEmpty()) {
                IGNORE_TILE_UPDATES = true; // Paper
                iinventory1.setItem(i, itemstack);
                IGNORE_TILE_UPDATES = false; // Paper
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

                        tileentityhopper.setCooldown(tileentityhopper.world.spigotConfig.hopperTransfer - b0); // Spigot
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
        // Paper start - Optimize item suck in. remove streams, restore 1.12 checks. Seriously checking the bowl?!
        World world = ihopper.getWorld();
        double d0 = ihopper.getX();
        double d1 = ihopper.getY();
        double d2 = ihopper.getZ();
        AxisAlignedBB bb = new AxisAlignedBB(d0 - 0.5D, d1, d2 - 0.5D, d0 + 0.5D, d1 + 1.5D, d2 + 0.5D);
        return world.getEntities(EntityItem.class, bb, Entity::isAlive);
        // Paper end
    }

    @Nullable
    public static IInventory b(World world, BlockPosition blockposition) {
        return a(world, (double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D, true); // Paper
    }

    @Nullable
    public static IInventory a(World world, double d0, double d1, double d2) { return a(world, d0, d1, d2, false); } // Paper - overload to default false
    public static IInventory a(World world, double d0, double d1, double d2, boolean optimizeEntities) { // Paper
        Object object = null;
        BlockPosition blockposition = new BlockPosition(d0, d1, d2);
        if ( !world.isLoaded( blockposition ) ) return null; // Spigot
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

        if (object == null && (!optimizeEntities || !org.bukkit.craftbukkit.util.CraftMagicNumbers.getMaterial(block).isOccluding())) { // Paper
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
