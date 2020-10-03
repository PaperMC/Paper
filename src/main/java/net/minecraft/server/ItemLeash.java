package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

import org.bukkit.event.hanging.HangingPlaceEvent; // CraftBukkit

public class ItemLeash extends Item {

    public ItemLeash(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public EnumInteractionResult a(ItemActionContext itemactioncontext) {
        World world = itemactioncontext.getWorld();
        BlockPosition blockposition = itemactioncontext.getClickPosition();
        Block block = world.getType(blockposition).getBlock();

        if (block.a((Tag) TagsBlock.FENCES)) {
            EntityHuman entityhuman = itemactioncontext.getEntity();

            if (!world.isClientSide && entityhuman != null) {
                a(entityhuman, world, blockposition);
            }

            return EnumInteractionResult.a(world.isClientSide);
        } else {
            return EnumInteractionResult.PASS;
        }
    }

    public static EnumInteractionResult a(EntityHuman entityhuman, World world, BlockPosition blockposition) {
        EntityLeash entityleash = null;
        boolean flag = false;
        double d0 = 7.0D;
        int i = blockposition.getX();
        int j = blockposition.getY();
        int k = blockposition.getZ();
        List<EntityInsentient> list = world.a(EntityInsentient.class, new AxisAlignedBB((double) i - 7.0D, (double) j - 7.0D, (double) k - 7.0D, (double) i + 7.0D, (double) j + 7.0D, (double) k + 7.0D));
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityInsentient entityinsentient = (EntityInsentient) iterator.next();

            if (entityinsentient.getLeashHolder() == entityhuman) {
                if (entityleash == null) {
                    entityleash = EntityLeash.a(world, blockposition);

                    // CraftBukkit start - fire HangingPlaceEvent
                    HangingPlaceEvent event = new HangingPlaceEvent((org.bukkit.entity.Hanging) entityleash.getBukkitEntity(), entityhuman != null ? (org.bukkit.entity.Player) entityhuman.getBukkitEntity() : null, world.getWorld().getBlockAt(i, j, k), org.bukkit.block.BlockFace.SELF);
                    world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        entityleash.die();
                        return EnumInteractionResult.PASS;
                    }
                    // CraftBukkit end
                }

                // CraftBukkit start
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerLeashEntityEvent(entityinsentient, entityleash, entityhuman).isCancelled()) {
                    continue;
                }
                // CraftBukkit end

                entityinsentient.setLeashHolder(entityleash, true);
                flag = true;
            }
        }

        return flag ? EnumInteractionResult.SUCCESS : EnumInteractionResult.PASS;
    }
}
