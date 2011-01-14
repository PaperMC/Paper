package net.minecraft.server;

// CraftBukkit start
import org.bukkit.BlockFace;
import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.craftbukkit.CraftItemStack;
import org.bukkit.craftbukkit.CraftPlayer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockPlacedEvent;
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

        // CraftBukkit start - store the old data so we can undo it
        int oldMaterial = world.a(i, j, k);
        int oldData = world.b(i, j, k);

        if (world.a(a, i, j, k, false)) {
            Block block = Block.m[a];

            // This executes the placement of the block
            if (world.e(i, j, k, a)) {
                CraftBlock placedBlock = (CraftBlock) blockClicked.getFace(faceClicked) ;
                CraftItemStack itemInHand = new CraftItemStack(itemstack);
                CraftPlayer thePlayer = new CraftPlayer(((WorldServer) world).getServer(), (EntityPlayerMP) entityplayer);

                int xFromSpawn = (int) MathHelper.e(i - world.m);
                int distanceFromSpawn = (int) MathHelper.e(k - world.o);

                if (xFromSpawn > distanceFromSpawn) {
                    distanceFromSpawn = xFromSpawn;
                }

                // CraftBukkit hardcoded Spawn distance for now
                boolean canBuild = distanceFromSpawn > 16 || thePlayer.isOp();

                BlockPlacedEvent bpe = new BlockPlacedEvent(Type.BLOCK_PLACED, placedBlock, blockClicked, itemInHand, thePlayer, canBuild);
                ((WorldServer) world).getServer().getPluginManager().callEvent(bpe);

                if (bpe.isCancelled() || !bpe.canBuild()) {
                    // CraftBukkit Undo!

                    // Specialcase iceblocks, replace with 'glass' first (so it doesn't explode into water)
                    if (this.a == 79) {
                        world.a(i, j, k, 20);
                    }
                    world.b(i, j, k, oldMaterial);
                    world.d(i, j, k, oldData);
                } else {
                    world.g(i, j, k);
                    world.h(i, j, k, this.a);

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
