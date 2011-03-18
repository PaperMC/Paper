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
import org.bukkit.Material;
import org.bukkit.event.player.PlayerVegetationPlantEvent;
// CraftBukkit end

public class ItemReed extends Item {

    private int a;

    public ItemReed(int i, Block block) {
        super(i);
        this.a = block.id;
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        // CraftBukkit start -- Bail if we have nothing of the item in hand
        if (itemstack.count == 0) {
            return false;
        }

        CraftBlock blockClicked = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
        BlockFace faceClicked = CraftBlock.notchToBlockFace(l);
        // CraftBukkit end

        if (world.getTypeId(i, j, k) == Block.SNOW.id) {
            l = 0;
        } else {
            if (l == 0) {
                --j;
            }

            if (l == 1) {
                ++j;
            }

            if (l == 2) {
                --k;
            }

            if (l == 3) {
                ++k;
            }

            if (l == 4) {
                --i;
            }

            if (l == 5) {
                ++i;
            }
        }

        if (itemstack.count == 0) {
            return false;
        } else {
            // CraftBukkit start
            /* We store the old data so we can undo it. Snow(78) and half-steps(44) are special in that they replace the block itself,
             * rather than the block touching the face we clicked on.
             */
            int typeId = blockClicked.getTypeId();
            org.bukkit.block.Block placedBlock = blockClicked.getFace(faceClicked);

            if (typeId == Block.SNOW.id || (typeId == Block.STEP.id && itemstack.id == Block.STEP.id && faceClicked == BlockFace.UP))
                placedBlock = blockClicked;

            final BlockState replacedBlockState = new CraftBlockState(placedBlock);
            // CraftBukkit end

            if (world.a(this.a, i, j, k, false)) {
                Block block = Block.byId[this.a];

                // CraftBukkit start - This executes the placement of the block
                /**
                 * @see net.minecraft.server.World#e(int i, int j, int k, int l)
                 * 
                 * This replaces world.e(IIII), we're doing this because we need to
                 * hook between the 'placement' and the informing to 'world' so we can
                 * sanely undo this.
                 *
                 * Whenever the call to 'world.e' changes we need to figure out again what to
                 * replace this with.
                 */
                if (world.setTypeId(i, j, k, this.a)) { // <-- world.e does this to place the block
                    org.bukkit.Server server = ((WorldServer) world).getServer();
                    Type eventType = Type.BLOCK_PLACE;
                    org.bukkit.inventory.ItemStack itemInHand = new CraftItemStack(itemstack);
                    Player thePlayer = (entityhuman == null) ? null : (Player) entityhuman.getBukkitEntity();

                    ChunkCoordinates chunkcoordinates = world.l();
                    int spawnX = chunkcoordinates.a;
                    int spawnZ = chunkcoordinates.c;

                    int distanceFromSpawn = (int) Math.max(Math.abs(i - spawnX), Math.abs(k - spawnZ));

                    // CraftBukkit Configurable spawn protection start
                    boolean canBuild = distanceFromSpawn > ((WorldServer) world).x.spawnProtection || thePlayer.isOp();

                    BlockPlaceEvent event = new BlockPlaceEvent(eventType, placedBlock, replacedBlockState, blockClicked, itemInHand, thePlayer, canBuild);
                    server.getPluginManager().callEvent(event);

                    if (event.isCancelled() || !event.canBuild()) {
                        // CraftBukkit Undo -- this only has reed, repeater and pie blocks
                        world.setTypeIdAndData(i, j, k, replacedBlockState.getTypeId(), replacedBlockState.getRawData());
                    } else {
                        world.f(i, j, k, a); // <-- world.e does this on success (tell the world)

                        Block.byId[this.a].d(world, i, j, k, l);
                        Block.byId[this.a].a(world, i, j, k, (EntityLiving) entityhuman);
                        world.a((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.c(), (block.stepSound.a() + 1.0F) / 2.0F, block.stepSound.b() * 0.8F);
                        --itemstack.count;
                    }
                    // CraftBukkit end
                }
            }

            return true;
        }
    }
}
