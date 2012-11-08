package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

public class DispenseBehaviorEmptyBucket extends DispenseBehaviorItem {

    private final DispenseBehaviorItem c;

    final MinecraftServer b;

    public DispenseBehaviorEmptyBucket(MinecraftServer minecraftserver) {
        this.b = minecraftserver;
        this.c = new DispenseBehaviorItem();
    }

    public ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        EnumFacing enumfacing = EnumFacing.a(isourceblock.h());
        World world = isourceblock.k();
        int i = isourceblock.getBlockX() + enumfacing.c();
        int j = isourceblock.getBlockY();
        int k = isourceblock.getBlockZ() + enumfacing.e();
        Material material = world.getMaterial(i, j, k);
        int l = world.getData(i, j, k);
        Item item;

        if (Material.WATER.equals(material) && l == 0) {
            item = Item.WATER_BUCKET;
        } else {
            if (!Material.LAVA.equals(material) || l != 0) {
                return super.b(isourceblock, itemstack);
            }

            item = Item.LAVA_BUCKET;
        }

        // CraftBukkit start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
        org.bukkit.inventory.ItemStack bukkitItem = new CraftItemStack(itemstack).clone();

        BlockDispenseEvent event = new BlockDispenseEvent(block, bukkitItem, new org.bukkit.util.Vector(0, 0, 0));
        if (!BlockDispenser.eventFired) {
            world.getServer().getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            return itemstack;
        }

        if (!event.getItem().equals(bukkitItem)) {
            // Chain to handler for new item
            ItemStack eventStack = CraftItemStack.createNMSItemStack(event.getItem());
            IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.a.a(eventStack.getItem());
            if (idispensebehavior != IDispenseBehavior.a && idispensebehavior != this) {
                idispensebehavior.a(isourceblock, eventStack);
                return itemstack;
            }
        }
        // CraftBukkit end

        world.setTypeId(i, j, k, 0);
        if (--itemstack.count == 0) {
            itemstack.id = item.id;
            itemstack.count = 1;
        } else if (((TileEntityDispenser) isourceblock.getTileEntity()).addItem(new ItemStack(item)) < 0) {
            this.c.a(isourceblock, new ItemStack(item));
        }

        return itemstack;
    }
}
