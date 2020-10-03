package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

public class ItemMinecart extends Item {

    private static final IDispenseBehavior a = new DispenseBehaviorItem() {
        private final DispenseBehaviorItem b = new DispenseBehaviorItem();

        @Override
        public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
            EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
            WorldServer worldserver = isourceblock.getWorld();
            double d0 = isourceblock.getX() + (double) enumdirection.getAdjacentX() * 1.125D;
            double d1 = Math.floor(isourceblock.getY()) + (double) enumdirection.getAdjacentY();
            double d2 = isourceblock.getZ() + (double) enumdirection.getAdjacentZ() * 1.125D;
            BlockPosition blockposition = isourceblock.getBlockPosition().shift(enumdirection);
            IBlockData iblockdata = worldserver.getType(blockposition);
            BlockPropertyTrackPosition blockpropertytrackposition = iblockdata.getBlock() instanceof BlockMinecartTrackAbstract ? (BlockPropertyTrackPosition) iblockdata.get(((BlockMinecartTrackAbstract) iblockdata.getBlock()).d()) : BlockPropertyTrackPosition.NORTH_SOUTH;
            double d3;

            if (iblockdata.a((Tag) TagsBlock.RAILS)) {
                if (blockpropertytrackposition.c()) {
                    d3 = 0.6D;
                } else {
                    d3 = 0.1D;
                }
            } else {
                if (!iblockdata.isAir() || !worldserver.getType(blockposition.down()).a((Tag) TagsBlock.RAILS)) {
                    return this.b.dispense(isourceblock, itemstack);
                }

                IBlockData iblockdata1 = worldserver.getType(blockposition.down());
                BlockPropertyTrackPosition blockpropertytrackposition1 = iblockdata1.getBlock() instanceof BlockMinecartTrackAbstract ? (BlockPropertyTrackPosition) iblockdata1.get(((BlockMinecartTrackAbstract) iblockdata1.getBlock()).d()) : BlockPropertyTrackPosition.NORTH_SOUTH;

                if (enumdirection != EnumDirection.DOWN && blockpropertytrackposition1.c()) {
                    d3 = -0.4D;
                } else {
                    d3 = -0.9D;
                }
            }

            // CraftBukkit start
            // EntityMinecartAbstract entityminecartabstract = EntityMinecartAbstract.a(world, d0, d1 + d3, d2, ((ItemMinecart) itemstack.getItem()).b);
            ItemStack itemstack1 = itemstack.cloneAndSubtract(1);
            org.bukkit.block.Block block2 = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
            CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

            BlockDispenseEvent event = new BlockDispenseEvent(block2, craftItem.clone(), new org.bukkit.util.Vector(d0, d1 + d3, d2));
            if (!BlockDispenser.eventFired) {
                worldserver.getServer().getPluginManager().callEvent(event);
            }

            if (event.isCancelled()) {
                itemstack.add(1);
                return itemstack;
            }

            if (!event.getItem().equals(craftItem)) {
                itemstack.add(1);
                // Chain to handler for new item
                ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
                if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                    idispensebehavior.dispense(isourceblock, eventStack);
                    return itemstack;
                }
            }

            itemstack1 = CraftItemStack.asNMSCopy(event.getItem());
            EntityMinecartAbstract entityminecartabstract = EntityMinecartAbstract.a(worldserver, event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), ((ItemMinecart) itemstack1.getItem()).b);

            if (itemstack.hasName()) {
                entityminecartabstract.setCustomName(itemstack.getName());
            }

            if (!worldserver.addEntity(entityminecartabstract)) itemstack.add(1);
            // itemstack.subtract(1); // CraftBukkit - handled during event processing
            // CraftBukkit end
            return itemstack;
        }

        @Override
        protected void a(ISourceBlock isourceblock) {
            isourceblock.getWorld().triggerEffect(1000, isourceblock.getBlockPosition(), 0);
        }
    };
    private final EntityMinecartAbstract.EnumMinecartType b;

    public ItemMinecart(EntityMinecartAbstract.EnumMinecartType entityminecartabstract_enumminecarttype, Item.Info item_info) {
        super(item_info);
        this.b = entityminecartabstract_enumminecarttype;
        BlockDispenser.a((IMaterial) this, ItemMinecart.a);
    }

    @Override
    public EnumInteractionResult a(ItemActionContext itemactioncontext) {
        World world = itemactioncontext.getWorld();
        BlockPosition blockposition = itemactioncontext.getClickPosition();
        IBlockData iblockdata = world.getType(blockposition);

        if (!iblockdata.a((Tag) TagsBlock.RAILS)) {
            return EnumInteractionResult.FAIL;
        } else {
            ItemStack itemstack = itemactioncontext.getItemStack();

            if (!world.isClientSide) {
                BlockPropertyTrackPosition blockpropertytrackposition = iblockdata.getBlock() instanceof BlockMinecartTrackAbstract ? (BlockPropertyTrackPosition) iblockdata.get(((BlockMinecartTrackAbstract) iblockdata.getBlock()).d()) : BlockPropertyTrackPosition.NORTH_SOUTH;
                double d0 = 0.0D;

                if (blockpropertytrackposition.c()) {
                    d0 = 0.5D;
                }

                EntityMinecartAbstract entityminecartabstract = EntityMinecartAbstract.a(world, (double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.0625D + d0, (double) blockposition.getZ() + 0.5D, this.b);

                if (itemstack.hasName()) {
                    entityminecartabstract.setCustomName(itemstack.getName());
                }

                // CraftBukkit start
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityPlaceEvent(itemactioncontext, entityminecartabstract).isCancelled()) {
                    return EnumInteractionResult.FAIL;
                }
                // CraftBukkit end
                if (!world.addEntity(entityminecartabstract)) return EnumInteractionResult.PASS; // CraftBukkit
            }

            itemstack.subtract(1);
            return EnumInteractionResult.a(world.isClientSide);
        }
    }
}
