package net.minecraft.server;

// CraftBukkit start
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerItemEvent;
// CraftBukkit end

public class ItemHoe extends Item {

    public ItemHoe(int i, EnumToolMaterial enumtoolmaterial) {
        super(i);
        this.maxStackSize = 1;
        this.durability = enumtoolmaterial.a();
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        int i1 = world.getTypeId(i, j, k);
        Material material = world.getMaterial(i, j + 1, k);

        if ((material.isBuildable() || i1 != Block.GRASS.id) && i1 != Block.DIRT.id) {
            return false;
        } else {
            Block block = Block.SOIL;

            world.a((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.c(), (block.stepSound.a() + 1.0F) / 2.0F, block.stepSound.b() * 0.8F);
            if (world.isStatic) {
                return true;
            } else {
                // CraftBukkit start - Hoes
                CraftWorld craftWorld = ((WorldServer) world).getWorld();
                CraftServer craftServer = ((WorldServer) world).getServer();

                Type eventType = Type.PLAYER_ITEM;
                Player who = (entityhuman == null) ? null : (Player) entityhuman.getBukkitEntity();
                org.bukkit.inventory.ItemStack itemInHand = new CraftItemStack(itemstack);
                org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(i, j, k);
                BlockFace blockFace = CraftBlock.notchToBlockFace(1);

                PlayerItemEvent event = new PlayerItemEvent(eventType, who, itemInHand, blockClicked, blockFace);
                craftServer.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }
                // CraftBukkit end

                world.e(i, j, k, block.id);
                itemstack.b(1);
                if (world.k.nextInt(8) == 0 && i1 == Block.GRASS.id) {
                    byte b0 = 1;

                    for (int j1 = 0; j1 < b0; ++j1) {
                        float f = 0.7F;
                        float f1 = world.k.nextFloat() * f + (1.0F - f) * 0.5F;
                        float f2 = 1.2F;
                        float f3 = world.k.nextFloat() * f + (1.0F - f) * 0.5F;
                        EntityItem entityitem = new EntityItem(world, (double) ((float) i + f1), (double) ((float) j + f2), (double) ((float) k + f3), new ItemStack(Item.SEEDS));

                        entityitem.c = 10;
                        world.a((Entity) entityitem);
                    }
                }

                return true;
            }
        }
    }
}
