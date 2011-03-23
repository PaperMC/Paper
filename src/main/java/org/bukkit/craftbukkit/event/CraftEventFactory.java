package org.bukkit.craftbukkit.event;

import net.minecraft.server.ChunkCoordinates;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CraftEventFactory {
    private static boolean canBuild(CraftWorld world, Player player, int x, int z) {
        WorldServer worldServer = world.getHandle();
        int spawnSize = worldServer.x.spawnProtection;

        if (spawnSize <= 0) return true;
        if (player.isOp()) return true;

        ChunkCoordinates chunkcoordinates = worldServer.l();

        int distanceFromSpawn = (int) Math.max(Math.abs(x - chunkcoordinates.a), Math.abs(z - chunkcoordinates.c));
        return distanceFromSpawn > spawnSize;
    }

    /**
     * Block place methods
     */
    public static BlockPlaceEvent callBlockPlaceEvent(World world, EntityHuman who, BlockState replacedBlockState, int clickedX, int clickedY, int clickedZ, int type) {
        return callBlockPlaceEvent(world, who, replacedBlockState, clickedX, clickedY, clickedZ, net.minecraft.server.Block.byId[type]);
    }

    public static BlockPlaceEvent callBlockPlaceEvent(World world, EntityHuman who, BlockState replacedBlockState, int clickedX, int clickedY, int clickedZ, net.minecraft.server.Block block) {
        return callBlockPlaceEvent(world, who, replacedBlockState, clickedX, clickedY, clickedZ, new ItemStack(block));
    }

    public static BlockPlaceEvent callBlockPlaceEvent(World world, EntityHuman who, BlockState replacedBlockState, int clickedX, int clickedY, int clickedZ, ItemStack itemstack) {
        CraftWorld craftWorld = ((WorldServer) world).getWorld();
        CraftServer craftServer = ((WorldServer) world).getServer();

        Player player = (who == null) ? null : (Player) who.getBukkitEntity();
        CraftItemStack itemInHand = new CraftItemStack(itemstack);

        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        Block placedBlock = replacedBlockState.getBlock();

        boolean canBuild = canBuild(craftWorld, player, placedBlock.getX(), placedBlock.getZ());

        BlockPlaceEvent event = new BlockPlaceEvent(Type.BLOCK_PLACE, placedBlock, replacedBlockState, blockClicked, itemInHand, player, canBuild);
        craftServer.getPluginManager().callEvent(event);

        return event;
    }

    /**
     * Bucket methods
     */
    public static PlayerBucketEmptyEvent callPlayerBucketEmptyEvent(EntityHuman who, int clickedX, int clickedY, int clickedZ, int clickedFace, ItemStack itemInHand) {
        return (PlayerBucketEmptyEvent) getPlayerBucketEvent(Type.PLAYER_BUCKET_EMPTY, who, clickedX, clickedY, clickedZ, clickedFace, itemInHand, Item.BUCKET);
    }

    public static PlayerBucketFillEvent callPlayerBucketFillEvent(EntityHuman who, int clickedX, int clickedY, int clickedZ, int clickedFace, ItemStack itemInHand, net.minecraft.server.Item bucket) {
        return (PlayerBucketFillEvent) getPlayerBucketEvent(Type.PLAYER_BUCKET_FILL, who, clickedX, clickedY, clickedZ, clickedFace, itemInHand, bucket);
    }

    private static PlayerEvent getPlayerBucketEvent(Type type, EntityHuman who, int clickedX, int clickedY, int clickedZ, int clickedFace, ItemStack itemstack, net.minecraft.server.Item item) {
        Player player = (who == null) ? null : (Player) who.getBukkitEntity();
        CraftItemStack itemInHand = new CraftItemStack(new ItemStack(item));
        Material bucket = Material.getMaterial(itemstack.id);

        CraftWorld craftWorld = (CraftWorld) player.getWorld();
        CraftServer craftServer = (CraftServer) player.getServer();

        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        BlockFace blockFace = CraftBlock.notchToBlockFace(clickedFace);

        PlayerEvent event = null;
        if (type == Type.PLAYER_BUCKET_EMPTY) {
            event = new PlayerBucketEmptyEvent(player, blockClicked, blockFace, bucket, itemInHand);
            ((PlayerBucketEmptyEvent) event).setCancelled(!canBuild(craftWorld, player, clickedX, clickedZ));
        } else if(type == Type.PLAYER_BUCKET_FILL) {
            event = new PlayerBucketFillEvent(player, blockClicked, blockFace, bucket, itemInHand);
            ((PlayerBucketFillEvent) event).setCancelled(!canBuild(craftWorld, player, clickedX, clickedZ));
        }

        craftServer.getPluginManager().callEvent(event);

        return event;
    }

    /**
     * Player Interact event
     */
    
    public static PlayerInteractEvent callPlayerInteractEvent(EntityHuman who, Action action, ItemStack itemstack) {
        if (action != Action.LEFT_CLICK_AIR && action != Action.RIGHT_CLICK_AIR) {
            throw new IllegalArgumentException();
        }
        return callPlayerInteractEvent(who, action, 0, 255, 0, 0, itemstack);
    }
    public static PlayerInteractEvent callPlayerInteractEvent(EntityHuman who, Action action, int clickedX, int clickedY, int clickedZ, int clickedFace, ItemStack itemstack) {
        Player player = (who == null) ? null : (Player) who.getBukkitEntity();
        CraftItemStack itemInHand = new CraftItemStack(itemstack);

        CraftWorld craftWorld = (CraftWorld) player.getWorld();
        CraftServer craftServer = (CraftServer) player.getServer();

        Block blockClicked = craftWorld.getBlockAt(clickedX, clickedY, clickedZ);
        BlockFace blockFace = CraftBlock.notchToBlockFace(clickedFace);

        if (clickedY == 255) {
            blockClicked = null;
            switch (action) {
                case LEFT_CLICK_BLOCK:
                    action = Action.LEFT_CLICK_AIR;
                    break;
                case RIGHT_CLICK_BLOCK:
                    action = Action.RIGHT_CLICK_AIR;
                    break;
            }
        }

        if (itemInHand.getType() == Material.AIR || itemInHand.getAmount() == 0) {
            itemInHand = null;
        }

        PlayerInteractEvent event = new PlayerInteractEvent(player, action, itemInHand, blockClicked, blockFace);
        craftServer.getPluginManager().callEvent(event);

        return event;
    }

    public static BlockDamageEvent callBlockDamageEvent(EntityHuman who, int x, int y, int z, ItemStack itemstack, boolean instaBreak) {
        Player player = (who == null) ? null : (Player) who.getBukkitEntity();
        CraftItemStack itemInHand = new CraftItemStack(itemstack);

        CraftWorld craftWorld = (CraftWorld) player.getWorld();
        CraftServer craftServer = (CraftServer) player.getServer();

        Block blockClicked = craftWorld.getBlockAt(x, y, z);

        BlockDamageEvent event = new BlockDamageEvent(player, blockClicked, itemInHand, instaBreak);
        craftServer.getPluginManager().callEvent(event);

        return event;
    }
}
