package net.minecraft.server;

import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// CraftBukkit start
import java.util.ArrayList;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
// CraftBukkit end

public class PlayerInteractManager {

    private static final Logger LOGGER = LogManager.getLogger();
    public WorldServer world;
    public EntityPlayer player;
    private EnumGamemode gamemode;
    private EnumGamemode e;
    private boolean f;
    private int lastDigTick;
    private BlockPosition h;
    private int currentTick;
    private boolean j;
    private BlockPosition k;
    private int l;
    private int m;

    public PlayerInteractManager(WorldServer worldserver) {
        this.gamemode = EnumGamemode.NOT_SET;
        this.e = EnumGamemode.NOT_SET;
        this.h = BlockPosition.ZERO;
        this.k = BlockPosition.ZERO;
        this.m = -1;
        this.world = worldserver;
    }

    public void setGameMode(EnumGamemode enumgamemode) {
        this.a(enumgamemode, enumgamemode != this.gamemode ? this.gamemode : this.e);
    }

    public void a(EnumGamemode enumgamemode, EnumGamemode enumgamemode1) {
        this.e = enumgamemode1;
        this.gamemode = enumgamemode;
        enumgamemode.a(this.player.abilities);
        this.player.updateAbilities();
        this.player.server.getPlayerList().sendAll(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE, new EntityPlayer[]{this.player}), this.player); // CraftBukkit
        this.world.everyoneSleeping();
    }

    public EnumGamemode getGameMode() {
        return this.gamemode;
    }

    public EnumGamemode c() {
        return this.e;
    }

    public boolean d() {
        return this.gamemode.f();
    }

    public boolean isCreative() {
        return this.gamemode.isCreative();
    }

    public void b(EnumGamemode enumgamemode) {
        if (this.gamemode == EnumGamemode.NOT_SET) {
            this.gamemode = enumgamemode;
        }

        this.setGameMode(this.gamemode);
    }

    public void a() {
        this.currentTick = MinecraftServer.currentTick; // CraftBukkit;
        IBlockData iblockdata;

        if (this.j) {
            iblockdata = this.world.getType(this.k);
            if (iblockdata.isAir()) {
                this.j = false;
            } else {
                float f = this.a(iblockdata, this.k, this.l);

                if (f >= 1.0F) {
                    this.j = false;
                    this.breakBlock(this.k);
                }
            }
        } else if (this.f) {
            iblockdata = this.world.getType(this.h);
            if (iblockdata.isAir()) {
                this.world.a(this.player.getId(), this.h, -1);
                this.m = -1;
                this.f = false;
            } else {
                this.a(iblockdata, this.h, this.lastDigTick);
            }
        }

    }

    private float a(IBlockData iblockdata, BlockPosition blockposition, int i) {
        int j = this.currentTick - i;
        float f = iblockdata.getDamage(this.player, this.player.world, blockposition) * (float) (j + 1);
        int k = (int) (f * 10.0F);

        if (k != this.m) {
            this.world.a(this.player.getId(), blockposition, k);
            this.m = k;
        }

        return f;
    }

    public void a(BlockPosition blockposition, PacketPlayInBlockDig.EnumPlayerDigType packetplayinblockdig_enumplayerdigtype, EnumDirection enumdirection, int i) {
        double d0 = this.player.locX() - ((double) blockposition.getX() + 0.5D);
        double d1 = this.player.locY() - ((double) blockposition.getY() + 0.5D) + 1.5D;
        double d2 = this.player.locZ() - ((double) blockposition.getZ() + 0.5D);
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;

        if (d3 > 36.0D) {
            this.player.playerConnection.sendPacket(new PacketPlayOutBlockBreak(blockposition, this.world.getType(blockposition), packetplayinblockdig_enumplayerdigtype, false, "too far"));
        } else if (blockposition.getY() >= i) {
            this.player.playerConnection.sendPacket(new PacketPlayOutBlockBreak(blockposition, this.world.getType(blockposition), packetplayinblockdig_enumplayerdigtype, false, "too high"));
        } else {
            IBlockData iblockdata;

            if (packetplayinblockdig_enumplayerdigtype == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                if (!this.world.a((EntityHuman) this.player, blockposition)) {
                    // CraftBukkit start - fire PlayerInteractEvent
                    CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, blockposition, enumdirection, this.player.inventory.getItemInHand(), EnumHand.MAIN_HAND);
                    this.player.playerConnection.sendPacket(new PacketPlayOutBlockBreak(blockposition, this.world.getType(blockposition), packetplayinblockdig_enumplayerdigtype, false, "may not interact"));
                    // Update any tile entity data for this block
                    TileEntity tileentity = world.getTileEntity(blockposition);
                    if (tileentity != null) {
                        this.player.playerConnection.sendPacket(tileentity.getUpdatePacket());
                    }
                    // CraftBukkit end
                    return;
                }

                // CraftBukkit start
                PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, blockposition, enumdirection, this.player.inventory.getItemInHand(), EnumHand.MAIN_HAND);
                if (event.isCancelled()) {
                    // Let the client know the block still exists
                    this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));
                    // Update any tile entity data for this block
                    TileEntity tileentity = this.world.getTileEntity(blockposition);
                    if (tileentity != null) {
                        this.player.playerConnection.sendPacket(tileentity.getUpdatePacket());
                    }
                    return;
                }
                // CraftBukkit end

                if (this.isCreative()) {
                    this.a(blockposition, packetplayinblockdig_enumplayerdigtype, "creative destroy");
                    return;
                }

                if (this.player.a((World) this.world, blockposition, this.gamemode)) {
                    this.player.playerConnection.sendPacket(new PacketPlayOutBlockBreak(blockposition, this.world.getType(blockposition), packetplayinblockdig_enumplayerdigtype, false, "block action restricted"));
                    return;
                }

                this.lastDigTick = this.currentTick;
                float f = 1.0F;

                iblockdata = this.world.getType(blockposition);
                // CraftBukkit start - Swings at air do *NOT* exist.
                if (event.useInteractedBlock() == Event.Result.DENY) {
                    // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
                    IBlockData data = this.world.getType(blockposition);
                    if (data.getBlock() instanceof BlockDoor) {
                        // For some reason *BOTH* the bottom/top part have to be marked updated.
                        boolean bottom = data.get(BlockDoor.HALF) == BlockPropertyDoubleBlockHalf.LOWER;
                        this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));
                        this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, bottom ? blockposition.up() : blockposition.down()));
                    } else if (data.getBlock() instanceof BlockTrapdoor) {
                        this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));
                    }
                } else if (!iblockdata.isAir()) {
                    iblockdata.attack(this.world, blockposition, this.player);
                    f = iblockdata.getDamage(this.player, this.player.world, blockposition);
                }

                if (event.useItemInHand() == Event.Result.DENY) {
                    // If we 'insta destroyed' then the client needs to be informed.
                    if (f > 1.0f) {
                        this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));
                    }
                    return;
                }
                org.bukkit.event.block.BlockDamageEvent blockEvent = CraftEventFactory.callBlockDamageEvent(this.player, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this.player.inventory.getItemInHand(), f >= 1.0f);

                if (blockEvent.isCancelled()) {
                    // Let the client know the block still exists
                    this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));
                    return;
                }

                if (blockEvent.getInstaBreak()) {
                    f = 2.0f;
                }
                // CraftBukkit end

                if (!iblockdata.isAir() && f >= 1.0F) {
                    this.a(blockposition, packetplayinblockdig_enumplayerdigtype, "insta mine");
                } else {
                    if (this.f) {
                        this.player.playerConnection.sendPacket(new PacketPlayOutBlockBreak(this.h, this.world.getType(this.h), PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK, false, "abort destroying since another started (client insta mine, server disagreed)"));
                    }

                    this.f = true;
                    this.h = blockposition.immutableCopy();
                    int j = (int) (f * 10.0F);

                    this.world.a(this.player.getId(), blockposition, j);
                    this.player.playerConnection.sendPacket(new PacketPlayOutBlockBreak(blockposition, this.world.getType(blockposition), packetplayinblockdig_enumplayerdigtype, true, "actual start of destroying"));
                    this.m = j;
                }
            } else if (packetplayinblockdig_enumplayerdigtype == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK) {
                if (blockposition.equals(this.h)) {
                    int k = this.currentTick - this.lastDigTick;

                    iblockdata = this.world.getType(blockposition);
                    if (!iblockdata.isAir()) {
                        float f1 = iblockdata.getDamage(this.player, this.player.world, blockposition) * (float) (k + 1);

                        if (f1 >= 0.7F) {
                            this.f = false;
                            this.world.a(this.player.getId(), blockposition, -1);
                            this.a(blockposition, packetplayinblockdig_enumplayerdigtype, "destroyed");
                            return;
                        }

                        if (!this.j) {
                            this.f = false;
                            this.j = true;
                            this.k = blockposition;
                            this.l = this.lastDigTick;
                        }
                    }
                }

                this.player.playerConnection.sendPacket(new PacketPlayOutBlockBreak(blockposition, this.world.getType(blockposition), packetplayinblockdig_enumplayerdigtype, true, "stopped destroying"));
            } else if (packetplayinblockdig_enumplayerdigtype == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                this.f = false;
                if (!Objects.equals(this.h, blockposition)) {
                    PlayerInteractManager.LOGGER.debug("Mismatch in destroy block pos: " + this.h + " " + blockposition); // CraftBukkit - SPIGOT-5457 sent by client when interact event cancelled
                    this.world.a(this.player.getId(), this.h, -1);
                    this.player.playerConnection.sendPacket(new PacketPlayOutBlockBreak(this.h, this.world.getType(this.h), packetplayinblockdig_enumplayerdigtype, true, "aborted mismatched destroying"));
                }

                this.world.a(this.player.getId(), blockposition, -1);
                this.player.playerConnection.sendPacket(new PacketPlayOutBlockBreak(blockposition, this.world.getType(blockposition), packetplayinblockdig_enumplayerdigtype, true, "aborted destroying"));
            }

        }
    }

    public void a(BlockPosition blockposition, PacketPlayInBlockDig.EnumPlayerDigType packetplayinblockdig_enumplayerdigtype, String s) {
        if (this.breakBlock(blockposition)) {
            this.player.playerConnection.sendPacket(new PacketPlayOutBlockBreak(blockposition, this.world.getType(blockposition), packetplayinblockdig_enumplayerdigtype, true, s));
        } else {
            this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition)); // CraftBukkit - SPIGOT-5196
        }

    }

    public boolean breakBlock(BlockPosition blockposition) {
        IBlockData iblockdata = this.world.getType(blockposition);
        // CraftBukkit start - fire BlockBreakEvent
        org.bukkit.block.Block bblock = CraftBlock.at(world, blockposition);
        BlockBreakEvent event = null;

        if (this.player instanceof EntityPlayer) {
            // Sword + Creative mode pre-cancel
            boolean isSwordNoBreak = !this.player.getItemInMainHand().getItem().a(iblockdata, this.world, blockposition, (EntityHuman) this.player);

            // Tell client the block is gone immediately then process events
            // Don't tell the client if its a creative sword break because its not broken!
            if (world.getTileEntity(blockposition) == null && !isSwordNoBreak) {
                PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(this.world, blockposition);
                packet.block = Blocks.AIR.getBlockData();
                this.player.playerConnection.sendPacket(packet);
            }

            event = new BlockBreakEvent(bblock, this.player.getBukkitEntity());

            // Sword + Creative mode pre-cancel
            event.setCancelled(isSwordNoBreak);

            // Calculate default block experience
            IBlockData nmsData = this.world.getType(blockposition);
            Block nmsBlock = nmsData.getBlock();

            ItemStack itemstack = this.player.getEquipment(EnumItemSlot.MAINHAND);

            if (nmsBlock != null && !event.isCancelled() && !this.isCreative() && this.player.hasBlock(nmsBlock.getBlockData())) {
                event.setExpToDrop(nmsBlock.getExpDrop(nmsData, this.world, blockposition, itemstack));
            }

            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                if (isSwordNoBreak) {
                    return false;
                }
                // Let the client know the block still exists
                this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, blockposition));

                // Brute force all possible updates
                for (EnumDirection dir : EnumDirection.values()) {
                    this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(world, blockposition.shift(dir)));
                }

                // Update any tile entity data for this block
                TileEntity tileentity = this.world.getTileEntity(blockposition);
                if (tileentity != null) {
                    this.player.playerConnection.sendPacket(tileentity.getUpdatePacket());
                }
                return false;
            }
        }
        // CraftBukkit end

        if (false && !this.player.getItemInMainHand().getItem().a(iblockdata, (World) this.world, blockposition, (EntityHuman) this.player)) { // CraftBukkit - false
            return false;
        } else {
            iblockdata = this.world.getType(blockposition); // CraftBukkit - update state from plugins
            if (iblockdata.isAir()) return false; // CraftBukkit - A plugin set block to air without cancelling
            TileEntity tileentity = this.world.getTileEntity(blockposition);
            Block block = iblockdata.getBlock();

            if ((block instanceof BlockCommand || block instanceof BlockStructure || block instanceof BlockJigsaw) && !this.player.isCreativeAndOp()) {
                this.world.notify(blockposition, iblockdata, iblockdata, 3);
                return false;
            } else if (this.player.a((World) this.world, blockposition, this.gamemode)) {
                return false;
            } else {
                // CraftBukkit start
                org.bukkit.block.BlockState state = bblock.getState();
                world.captureDrops = new ArrayList<>();
                // CraftBukkit end
                block.a((World) this.world, blockposition, iblockdata, (EntityHuman) this.player);
                boolean flag = this.world.a(blockposition, false);

                if (flag) {
                    block.postBreak(this.world, blockposition, iblockdata);
                }

                if (this.isCreative()) {
                    // return true; // CraftBukkit
                } else {
                    ItemStack itemstack = this.player.getItemInMainHand();
                    ItemStack itemstack1 = itemstack.cloneItemStack();
                    boolean flag1 = this.player.hasBlock(iblockdata);

                    itemstack.a(this.world, iblockdata, blockposition, this.player);
                    if (flag && flag1 && event.isDropItems()) { // CraftBukkit - Check if block should drop items
                        block.a(this.world, this.player, blockposition, iblockdata, tileentity, itemstack1);
                    }

                    // return true; // CraftBukkit
                }
                // CraftBukkit start
                if (event.isDropItems()) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockDropItemEvent(bblock, state, this.player, world.captureDrops);
                }
                world.captureDrops = null;

                // Drop event experience
                if (flag && event != null) {
                    iblockdata.getBlock().dropExperience(this.world, blockposition, event.getExpToDrop());
                }

                return true;
                // CraftBukkit end
            }
        }
    }

    public EnumInteractionResult a(EntityPlayer entityplayer, World world, ItemStack itemstack, EnumHand enumhand) {
        if (this.gamemode == EnumGamemode.SPECTATOR) {
            return EnumInteractionResult.PASS;
        } else if (entityplayer.getCooldownTracker().hasCooldown(itemstack.getItem())) {
            return EnumInteractionResult.PASS;
        } else {
            int i = itemstack.getCount();
            int j = itemstack.getDamage();
            InteractionResultWrapper<ItemStack> interactionresultwrapper = itemstack.a(world, (EntityHuman) entityplayer, enumhand);
            ItemStack itemstack1 = (ItemStack) interactionresultwrapper.b();

            if (itemstack1 == itemstack && itemstack1.getCount() == i && itemstack1.k() <= 0 && itemstack1.getDamage() == j) {
                return interactionresultwrapper.a();
            } else if (interactionresultwrapper.a() == EnumInteractionResult.FAIL && itemstack1.k() > 0 && !entityplayer.isHandRaised()) {
                return interactionresultwrapper.a();
            } else {
                entityplayer.a(enumhand, itemstack1);
                if (this.isCreative()) {
                    itemstack1.setCount(i);
                    if (itemstack1.e() && itemstack1.getDamage() != j) {
                        itemstack1.setDamage(j);
                    }
                }

                if (itemstack1.isEmpty()) {
                    entityplayer.a(enumhand, ItemStack.b);
                }

                if (!entityplayer.isHandRaised()) {
                    entityplayer.updateInventory(entityplayer.defaultContainer);
                }

                return interactionresultwrapper.a();
            }
        }
    }

    // CraftBukkit start - whole method
    public boolean interactResult = false;
    public boolean firedInteract = false;
    public BlockPosition interactPosition;
    public EnumHand interactHand;
    public ItemStack interactItemStack;
    public EnumInteractionResult a(EntityPlayer entityplayer, World world, ItemStack itemstack, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        BlockPosition blockposition = movingobjectpositionblock.getBlockPosition();
        IBlockData iblockdata = world.getType(blockposition);
        EnumInteractionResult enuminteractionresult = EnumInteractionResult.PASS;
        boolean cancelledBlock = false;

        if (this.gamemode == EnumGamemode.SPECTATOR) {
            ITileInventory itileinventory = iblockdata.b(world, blockposition);
            cancelledBlock = !(itileinventory instanceof ITileInventory);
        }

        if (entityplayer.getCooldownTracker().hasCooldown(itemstack.getItem())) {
            cancelledBlock = true;
        }

        PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(entityplayer, Action.RIGHT_CLICK_BLOCK, blockposition, movingobjectpositionblock.getDirection(), itemstack, cancelledBlock, enumhand);
        firedInteract = true;
        interactResult = event.useItemInHand() == Event.Result.DENY;
        interactPosition = blockposition.immutableCopy();
        interactHand = enumhand;
        interactItemStack = itemstack.cloneItemStack();

        if (event.useInteractedBlock() == Event.Result.DENY) {
            // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
            if (iblockdata.getBlock() instanceof BlockDoor) {
                boolean bottom = iblockdata.get(BlockDoor.HALF) == BlockPropertyDoubleBlockHalf.LOWER;
                entityplayer.playerConnection.sendPacket(new PacketPlayOutBlockChange(world, bottom ? blockposition.up() : blockposition.down()));
            } else if (iblockdata.getBlock() instanceof BlockCake) {
                entityplayer.getBukkitEntity().sendHealthUpdate(); // SPIGOT-1341 - reset health for cake
            }
            entityplayer.getBukkitEntity().updateInventory(); // SPIGOT-2867
            enuminteractionresult = (event.useItemInHand() != Event.Result.ALLOW) ? EnumInteractionResult.SUCCESS : EnumInteractionResult.PASS;
        } else if (this.gamemode == EnumGamemode.SPECTATOR) {
            ITileInventory itileinventory = iblockdata.b(world, blockposition);

            if (itileinventory != null) {
                entityplayer.openContainer(itileinventory);
                return EnumInteractionResult.SUCCESS;
            } else {
                return EnumInteractionResult.PASS;
            }
        } else {
            boolean flag = !entityplayer.getItemInMainHand().isEmpty() || !entityplayer.getItemInOffHand().isEmpty();
            boolean flag1 = entityplayer.ep() && flag;
            ItemStack itemstack1 = itemstack.cloneItemStack();

            if (!flag1) {
                enuminteractionresult = iblockdata.interact(world, entityplayer, enumhand, movingobjectpositionblock);

                if (enuminteractionresult.a()) {
                    CriterionTriggers.M.a(entityplayer, blockposition, itemstack1);
                    return enuminteractionresult;
                }
            }

            if (!itemstack.isEmpty() && enuminteractionresult != EnumInteractionResult.SUCCESS && !interactResult) { // add !interactResult SPIGOT-764
                ItemActionContext itemactioncontext = new ItemActionContext(entityplayer, enumhand, movingobjectpositionblock);
                EnumInteractionResult enuminteractionresult1;

                if (this.isCreative()) {
                    int i = itemstack.getCount();

                    enuminteractionresult1 = itemstack.placeItem(itemactioncontext, enumhand);
                    itemstack.setCount(i);
                } else {
                    enuminteractionresult1 = itemstack.placeItem(itemactioncontext, enumhand);
                }

                if (enuminteractionresult1.a()) {
                    CriterionTriggers.M.a(entityplayer, blockposition, itemstack1);
                }

                return enuminteractionresult1;
            }
        }
        return enuminteractionresult;
        // CraftBukkit end
    }

    public void a(WorldServer worldserver) {
        this.world = worldserver;
    }
}
