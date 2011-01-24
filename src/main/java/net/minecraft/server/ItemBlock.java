package net.minecraft.server;

// CraftBukkit start
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
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

        // CraftBukkit start
        /* We store the old data so we can undo it. Snow(78) and half-steps(44) are special in that they replace the block itself, 
         * rather than the block touching the face we clicked on.
         */
        org.bukkit.block.Block replacedBlock = (blockClicked.getTypeId() == 78 || blockClicked.getTypeId() == 44) ? blockClicked:blockClicked.getFace(faceClicked); 
        final BlockState replacedBlockState = new CraftBlockState(replacedBlock);

        if (world.a(a, i, j, k, false)) {
            Block block = Block.m[a];

            // This executes the placement of the block
            /*
             * This replaces world.b(IIIII), we're doing this because we need to
             * hook between the 'placement' and the informing to 'world' so we can
             * sanely undo this.
             * 
             * Whenever the call to 'world.b' changes we need to figure out again what to
             * replace this with.
             */
            if (world.a(i, j, k, a, a(itemstack.h()))) { // <-- world.b does this to place the block
                org.bukkit.Server server = ((WorldServer) world).getServer();
                Type eventType = Type.BLOCK_PLACED;
                org.bukkit.block.Block placedBlock = blockClicked.getFace(faceClicked) ;
                org.bukkit.inventory.ItemStack itemInHand = new CraftItemStack(itemstack);
                Player thePlayer = (entityplayer ==null)?null:(Player)entityplayer.getBukkitEntity();

                int distanceFromSpawn = (int) Math.max(Math.abs(i - world.m), Math.abs(k - world.o));

                // CraftBukkit hardcoded Spawn distance for now
                // TODO make spawn size configurable
                boolean canBuild = distanceFromSpawn > 16 || thePlayer.isOp();

                BlockPlaceEvent bpe = new BlockPlaceEvent(eventType, placedBlock, replacedBlockState, blockClicked, itemInHand, thePlayer, canBuild);
                server.getPluginManager().callEvent(bpe);

                if (bpe.isCancelled() || !bpe.canBuild()) {
                    // CraftBukkit Undo!

                    if (this.a == 79) {
                        // Ice will explode if we set straight to 0
                        world.b(i, j, k, 20);
                    } else if ((this.a == 44) && (world.a(i, j - 1, k) == 43) && (world.a(i, j, k) == 0)) {
                        // Half steps automatically set the block below to a double
                        world.b(i, j - 1, k, 44);
                    }

                    world.a(i, j, k, replacedBlockState.getTypeId(), replacedBlockState.getData().getData());
                } else {
                    world.f(i, j, k, a); // <-- world.b does this on success (tell the world)

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
