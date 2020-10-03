package net.minecraft.server;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.event.hanging.HangingPlaceEvent;
// CraftBukkit end

public class ItemHanging extends Item {

    private final EntityTypes<? extends EntityHanging> a;

    public ItemHanging(EntityTypes<? extends EntityHanging> entitytypes, Item.Info item_info) {
        super(item_info);
        this.a = entitytypes;
    }

    @Override
    public EnumInteractionResult a(ItemActionContext itemactioncontext) {
        BlockPosition blockposition = itemactioncontext.getClickPosition();
        EnumDirection enumdirection = itemactioncontext.getClickedFace();
        BlockPosition blockposition1 = blockposition.shift(enumdirection);
        EntityHuman entityhuman = itemactioncontext.getEntity();
        ItemStack itemstack = itemactioncontext.getItemStack();

        if (entityhuman != null && !this.a(entityhuman, enumdirection, itemstack, blockposition1)) {
            return EnumInteractionResult.FAIL;
        } else {
            World world = itemactioncontext.getWorld();
            Object object;

            if (this.a == EntityTypes.PAINTING) {
                object = new EntityPainting(world, blockposition1, enumdirection);
            } else {
                if (this.a != EntityTypes.ITEM_FRAME) {
                    return EnumInteractionResult.a(world.isClientSide);
                }

                object = new EntityItemFrame(world, blockposition1, enumdirection);
            }

            NBTTagCompound nbttagcompound = itemstack.getTag();

            if (nbttagcompound != null) {
                EntityTypes.a(world, entityhuman, (Entity) object, nbttagcompound);
            }

            if (((EntityHanging) object).survives()) {
                if (!world.isClientSide) {
                    // CraftBukkit start - fire HangingPlaceEvent
                    Player who = (itemactioncontext.getEntity() == null) ? null : (Player) itemactioncontext.getEntity().getBukkitEntity();
                    org.bukkit.block.Block blockClicked = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
                    org.bukkit.block.BlockFace blockFace = org.bukkit.craftbukkit.block.CraftBlock.notchToBlockFace(enumdirection);

                    HangingPlaceEvent event = new HangingPlaceEvent((org.bukkit.entity.Hanging) ((EntityHanging) object).getBukkitEntity(), who, blockClicked, blockFace);
                    world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return EnumInteractionResult.FAIL;
                    }
                    // CraftBukkit end
                    ((EntityHanging) object).playPlaceSound();
                    world.addEntity((Entity) object);
                }

                itemstack.subtract(1);
                return EnumInteractionResult.a(world.isClientSide);
            } else {
                return EnumInteractionResult.CONSUME;
            }
        }
    }

    protected boolean a(EntityHuman entityhuman, EnumDirection enumdirection, ItemStack itemstack, BlockPosition blockposition) {
        return !enumdirection.n().c() && entityhuman.a(blockposition, enumdirection, itemstack);
    }
}
