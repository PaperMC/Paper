package net.minecraft.server;

// CraftBukkit start
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockPlaceEvent;
// CraftBukkit end

public class ItemBlock extends Item {

    private int a;

    public ItemBlock(int i) {
        super(i);
        a = i + 256;
        b(Block.m[i + 256].a(2));
    }

    public boolean a(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
        // CraftBukkit start
        // Bail if we have nothing of the item in hand
        if (itemstack.a == 0) {
            return false;
        }

        // CraftBukkit store info of the clicked block
        CraftBlock blockClicked = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
        BlockFace faceClicked = CraftBlock.notchToBlockFace(l);
        // CraftBukkit end

        if (world.a(i, j, k) == Block.aS.bi) {
            l = 0;
        } else {
            if (l == 0) {
                j--;
            }
            if (l == 1) {
                j++;
            }
            if (l == 2) {
                k--;
            }
            if (l == 3) {
                k++;
            }
            if (l == 4) {
                i--;
            }
            if (l == 5) {
                i++;
            }
        }

        if (itemstack.a == 0) {
            return false;
        }

        // CraftBukkit start - store the old data so we can undo it
        int oldMaterial = world.a(i, j, k);
        int oldData = world.b(i, j, k);

        if (world.a(a, i, j, k, false)) {
            Block block = Block.m[a];

            // This executes the placement of the block
            if (world.b(i, j, k, a)) {
                CraftBlock placedBlock = (CraftBlock) blockClicked.getFace(faceClicked) ;
                CraftItemStack itemInHand = new CraftItemStack(itemstack);
                CraftPlayer thePlayer = new CraftPlayer(((WorldServer) world).getServer(), (EntityPlayerMP) entityplayer);

                int distanceFromSpawn = (int) Math.max(Math.abs(i - world.m), Math.abs(k - world.o));

                // CraftBukkit hardcoded Spawn distance for now
                boolean canBuild = distanceFromSpawn > 16 || thePlayer.isOp();

                BlockPlaceEvent bpe = new BlockPlaceEvent(Type.BLOCK_PLACED, placedBlock, blockClicked, itemInHand, thePlayer, canBuild);
                ((WorldServer) world).getServer().getPluginManager().callEvent(bpe);

                if (bpe.isCancelled() || !bpe.canBuild()) {
                    // CraftBukkit Undo!

                    if (this.a == 79) {
                        // Ice will explode if we set straight to 0
                        world.b(i, j, k, 20);
                    } else if ((this.a == 44) && (world.a(i, j - 1, k) == 43) && (world.a(i, j, k) == 0)) {
                        // Half steps automatically set the block below to a double
                        world.b(i, j - 1, k, 44);
                    }

                    world.b(i, j, k, oldMaterial);
                    world.d(i, j, k, oldData);
                } else {
                    world.c(i, j, k, a, a(itemstack.h()));

                    Block.m[a].c(world, i, j, k, l);
                    Block.m[a].a(world, i, j, k, ((EntityLiving) (entityplayer)));
                    world.a((float) i + 0.5F, (float) j + 0.5F, (float) k + 0.5F, block.br.c(), (block.br.a() + 1.0F) / 2.0F, block.br.b() * 0.8F);
                    itemstack.a--;
                }
            }
        }
        // CraftBukkit end
        return true;
    }

    public String a() {
        return Block.m[a].e();
    }
}
