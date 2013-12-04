package net.minecraft.server;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Callable;

import net.minecraft.util.com.google.common.base.Charsets;
import net.minecraft.util.com.google.common.collect.Lists;
import net.minecraft.util.io.netty.buffer.Unpooled;
import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.HashSet;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.craftbukkit.util.Waitable;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
// CraftBukkit end

public class PlayerConnection implements PacketPlayInListener {

    private static final Logger c = LogManager.getLogger();
    public final NetworkManager networkManager;
    private final MinecraftServer minecraftServer;
    public EntityPlayer player;
    private int e;
    private int f;
    private boolean g;
    private int h;
    private long i;
    private static Random j = new Random();
    private long k;
    private volatile int chatThrottle; private static final AtomicIntegerFieldUpdater chatSpamField = AtomicIntegerFieldUpdater.newUpdater(PlayerConnection.class, "chatThrottle"); // CraftBukkit - multithreaded field
    private int x;
    private IntHashMap n = new IntHashMap();
    private double y;
    private double z;
    private double q;
    public boolean checkMovement = true; // CraftBukkit - private -> public

    public PlayerConnection(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
        this.minecraftServer = minecraftserver;
        this.networkManager = networkmanager;
        networkmanager.a((PacketListener) this);
        this.player = entityplayer;
        entityplayer.playerConnection = this;

        // CraftBukkit start
        this.server = minecraftserver.server;
    }

    private final org.bukkit.craftbukkit.CraftServer server;
    private int lastTick = MinecraftServer.currentTick;
    private int lastDropTick = MinecraftServer.currentTick;
    private int dropCount = 0;
    private static final int PLACE_DISTANCE_SQUARED = 6 * 6;

    // Get position of last block hit for BlockDamageLevel.STOPPED
    private double lastPosX = Double.MAX_VALUE;
    private double lastPosY = Double.MAX_VALUE;
    private double lastPosZ = Double.MAX_VALUE;
    private float lastPitch = Float.MAX_VALUE;
    private float lastYaw = Float.MAX_VALUE;
    private boolean justTeleported = false;

    // For the PacketPlayOutBlockPlace hack :(
    Long lastPacket;

    // Store the last block right clicked and what type it was
    private Item lastMaterial;

    public CraftPlayer getPlayer() {
        return (this.player == null) ? null : (CraftPlayer) this.player.getBukkitEntity();
    }
    private final static HashSet<Integer> invalidItems = new HashSet<Integer>(java.util.Arrays.asList(8, 9, 10, 11, 26, 34, 36, 43, 51, 52, 55, 59, 60, 62, 63, 64, 68, 71, 74, 75, 83, 90, 92, 93, 94, 104, 105, 115, 117, 118, 119, 125, 127, 132, 137, 140, 141, 142, 144)); // TODO: Check after every update.
    // CraftBukkit end

    public void a() {
        this.g = false;
        ++this.e;
        this.minecraftServer.methodProfiler.a("keepAlive");
        if ((long) this.e - this.k > 40L) {
            this.k = (long) this.e;
            this.i = this.d();
            this.h = (int) this.i;
            this.sendPacket(new PacketPlayOutKeepAlive(this.h));
        }

        // CraftBukkit start
        for (int spam; (spam = this.chatThrottle) > 0 && !chatSpamField.compareAndSet(this, spam, spam - 1); ) ;
        /* Use thread-safe field access instead
        if (this.chatThrottle > 0) {
            --this.chatThrottle;
        }
        */
        // CraftBukkit end

        if (this.x > 0) {
            --this.x;
        }

        this.minecraftServer.methodProfiler.c("playerTick");
        this.minecraftServer.methodProfiler.b();
    }

    public NetworkManager b() {
        return this.networkManager;
    }

    public void disconnect(String s) {
        // CraftBukkit start
        String leaveMessage = EnumChatFormat.YELLOW + this.player.getName() + " left the game.";

        PlayerKickEvent event = new PlayerKickEvent(this.server.getPlayer(this.player), s, leaveMessage);

        if (this.server.getServer().isRunning()) {
            this.server.getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            // Do not kick the player
            return;
        }
        // Send the possibly modified leave message
        s = event.getReason();
        // CraftBukkit end
        ChatComponentText chatcomponenttext = new ChatComponentText(s);

        this.networkManager.handle(new PacketPlayOutKickDisconnect(chatcomponenttext), new GenericFutureListener[] { new PlayerConnectionFuture(this, chatcomponenttext)});
        this.a(chatcomponenttext); // CraftBukkit - Process quit immediately
        this.networkManager.g();
    }

    public void a(PacketPlayInSteerVehicle packetplayinsteervehicle) {
        this.player.a(packetplayinsteervehicle.c(), packetplayinsteervehicle.d(), packetplayinsteervehicle.e(), packetplayinsteervehicle.f());
    }

    public void a(PacketPlayInFlying packetplayinflying) {
        WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

        this.g = true;
        if (!this.player.viewingCredits) {
            double d0;

            if (!this.checkMovement) {
                d0 = packetplayinflying.d() - this.z;
                if (packetplayinflying.c() == this.y && d0 * d0 < 0.01D && packetplayinflying.e() == this.q) {
                    this.checkMovement = true;
                }
            }

            // CraftBukkit start
            Player player = this.getPlayer();
            Location from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); // Get the Players previous Event location.
            Location to = player.getLocation().clone(); // Start off the To location as the Players current location.

            // If the packet contains movement information then we update the To location with the correct XYZ.
            if (packetplayinflying.hasPos && !(packetplayinflying.hasPos && packetplayinflying.y == -999.0D && packetplayinflying.stance == -999.0D)) {
                to.setX(packetplayinflying.x);
                to.setY(packetplayinflying.y);
                to.setZ(packetplayinflying.z);
            }

            // If the packet contains look information then we update the To location with the correct Yaw & Pitch.
            if (packetplayinflying.hasLook) {
                to.setYaw(packetplayinflying.yaw);
                to.setPitch(packetplayinflying.pitch);
            }

            // Prevent 40 event-calls for less than a single pixel of movement >.>
            double delta = Math.pow(this.lastPosX - to.getX(), 2) + Math.pow(this.lastPosY - to.getY(), 2) + Math.pow(this.lastPosZ - to.getZ(), 2);
            float deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());

            if ((delta > 1f / 256 || deltaAngle > 10f) && (this.checkMovement && !this.player.dead)) {
                this.lastPosX = to.getX();
                this.lastPosY = to.getY();
                this.lastPosZ = to.getZ();
                this.lastYaw = to.getYaw();
                this.lastPitch = to.getPitch();

                // Skip the first time we do this
                if (from.getX() != Double.MAX_VALUE) {
                    PlayerMoveEvent event = new PlayerMoveEvent(player, from, to);
                    this.server.getPluginManager().callEvent(event);

                    // If the event is cancelled we move the player back to their old location.
                    if (event.isCancelled()) {
                        this.player.playerConnection.sendPacket(new PacketPlayOutPosition(from.getX(), from.getY() + 1.6200000047683716D, from.getZ(), from.getYaw(), from.getPitch(), false));
                        return;
                    }

                    /* If a Plugin has changed the To destination then we teleport the Player
                    there to avoid any 'Moved wrongly' or 'Moved too quickly' errors.
                    We only do this if the Event was not cancelled. */
                    if (!to.equals(event.getTo()) && !event.isCancelled()) {
                        this.player.getBukkitEntity().teleport(event.getTo(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
                        return;
                    }

                    /* Check to see if the Players Location has some how changed during the call of the event.
                    This can happen due to a plugin teleporting the player instead of using .setTo() */
                    if (!from.equals(this.getPlayer().getLocation()) && this.justTeleported) {
                        this.justTeleported = false;
                        return;
                    }
                }
            }

            if (Double.isNaN(packetplayinflying.x) || Double.isNaN(packetplayinflying.y) || Double.isNaN(packetplayinflying.z) || Double.isNaN(packetplayinflying.stance)) {
                player.teleport(player.getWorld().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
                System.err.println(player.getName() + " was caught trying to crash the server with an invalid position.");
                player.kickPlayer("Nope!");
                return;
            }

            if (this.checkMovement && !this.player.dead) {
                // CraftBukkit end
                double d1;
                double d2;
                double d3;

                if (this.player.vehicle != null) {
                    float f = this.player.yaw;
                    float f1 = this.player.pitch;

                    this.player.vehicle.ac();
                    d1 = this.player.locX;
                    d2 = this.player.locY;
                    d3 = this.player.locZ;
                    if (packetplayinflying.k()) {
                        f = packetplayinflying.g();
                        f1 = packetplayinflying.h();
                    }

                    this.player.onGround = packetplayinflying.i();
                    this.player.i();
                    this.player.W = 0.0F;
                    this.player.setLocation(d1, d2, d3, f, f1);
                    if (this.player.vehicle != null) {
                        this.player.vehicle.ac();
                    }

                    this.minecraftServer.getPlayerList().d(this.player);
                    if (this.checkMovement) {
                        this.y = this.player.locX;
                        this.z = this.player.locY;
                        this.q = this.player.locZ;
                    }

                    worldserver.playerJoinedWorld(this.player);
                    return;
                }

                if (this.player.isSleeping()) {
                    this.player.i();
                    this.player.setLocation(this.y, this.z, this.q, this.player.yaw, this.player.pitch);
                    worldserver.playerJoinedWorld(this.player);
                    return;
                }

                d0 = this.player.locY;
                this.y = this.player.locX;
                this.z = this.player.locY;
                this.q = this.player.locZ;
                d1 = this.player.locX;
                d2 = this.player.locY;
                d3 = this.player.locZ;
                float f2 = this.player.yaw;
                float f3 = this.player.pitch;

                if (packetplayinflying.j() && packetplayinflying.d() == -999.0D && packetplayinflying.f() == -999.0D) {
                    packetplayinflying.a(false);
                }

                double d4;

                if (packetplayinflying.j()) {
                    d1 = packetplayinflying.c();
                    d2 = packetplayinflying.d();
                    d3 = packetplayinflying.e();
                    d4 = packetplayinflying.f() - packetplayinflying.d();
                    if (!this.player.isSleeping() && (d4 > 1.65D || d4 < 0.1D)) {
                        this.disconnect("Illegal stance");
                        c.warn(this.player.getName() + " had an illegal stance: " + d4);
                        return;
                    }

                    if (Math.abs(packetplayinflying.c()) > 3.2E7D || Math.abs(packetplayinflying.e()) > 3.2E7D) {
                        this.disconnect("Illegal position");
                        return;
                    }
                }

                if (packetplayinflying.k()) {
                    f2 = packetplayinflying.g();
                    f3 = packetplayinflying.h();
                }

                this.player.i();
                this.player.W = 0.0F;
                this.player.setLocation(this.y, this.z, this.q, f2, f3);
                if (!this.checkMovement) {
                    return;
                }

                d4 = d1 - this.player.locX;
                double d5 = d2 - this.player.locY;
                double d6 = d3 - this.player.locZ;
                // CraftBukkit start - min to max
                double d7 = Math.max(Math.abs(d4), Math.abs(this.player.motX));
                double d8 = Math.max(Math.abs(d5), Math.abs(this.player.motY));
                double d9 = Math.max(Math.abs(d6), Math.abs(this.player.motZ));
                // CraftBukkit end
                double d10 = d7 * d7 + d8 * d8 + d9 * d9;

                if (d10 > 100.0D && this.checkMovement && (!this.minecraftServer.L() || !this.minecraftServer.K().equals(this.player.getName()))) { // CraftBukkit - Added this.checkMovement condition to solve this check being triggered by teleports
                    c.warn(this.player.getName() + " moved too quickly! " + d4 + "," + d5 + "," + d6 + " (" + d7 + ", " + d8 + ", " + d9 + ")");
                    this.a(this.y, this.z, this.q, this.player.yaw, this.player.pitch);
                    return;
                }

                float f4 = 0.0625F;
                boolean flag = worldserver.getCubes(this.player, this.player.boundingBox.clone().shrink((double) f4, (double) f4, (double) f4)).isEmpty();

                if (this.player.onGround && !packetplayinflying.i() && d5 > 0.0D) {
                    this.player.bj();
                }

                this.player.move(d4, d5, d6);
                this.player.onGround = packetplayinflying.i();
                this.player.checkMovement(d4, d5, d6);
                double d11 = d5;

                d4 = d1 - this.player.locX;
                d5 = d2 - this.player.locY;
                if (d5 > -0.5D || d5 < 0.5D) {
                    d5 = 0.0D;
                }

                d6 = d3 - this.player.locZ;
                d10 = d4 * d4 + d5 * d5 + d6 * d6;
                boolean flag1 = false;

                if (d10 > 0.0625D && !this.player.isSleeping() && !this.player.playerInteractManager.isCreative()) {
                    flag1 = true;
                    c.warn(this.player.getName() + " moved wrongly!");
                }

                this.player.setLocation(d1, d2, d3, f2, f3);
                boolean flag2 = worldserver.getCubes(this.player, this.player.boundingBox.clone().shrink((double) f4, (double) f4, (double) f4)).isEmpty();

                if (flag && (flag1 || !flag2) && !this.player.isSleeping()) {
                    this.a(this.y, this.z, this.q, f2, f3);
                    return;
                }

                AxisAlignedBB axisalignedbb = this.player.boundingBox.clone().grow((double) f4, (double) f4, (double) f4).a(0.0D, -0.55D, 0.0D);

                if (!this.minecraftServer.getAllowFlight() && !this.player.abilities.canFly && !worldserver.c(axisalignedbb)) { // CraftBukkit - check abilities instead of creative mode
                    if (d11 >= -0.03125D) {
                        ++this.f;
                        if (this.f > 80) {
                            c.warn(this.player.getName() + " was kicked for floating too long!");
                            this.disconnect("Flying is not enabled on this server");
                            return;
                        }
                    }
                } else {
                    this.f = 0;
                }

                this.player.onGround = packetplayinflying.i();
                this.minecraftServer.getPlayerList().d(this.player);
                if (this.player.playerInteractManager.isCreative()) return; // CraftBukkit - fixed fall distance accumulating while being in Creative mode.
                this.player.b(this.player.locY - d0, packetplayinflying.i());
            } else if (this.e % 20 == 0) {
                this.a(this.y, this.z, this.q, this.player.yaw, this.player.pitch);
            }
        }
    }

    public void a(double d0, double d1, double d2, float f, float f1) {
        // CraftBukkit start - Delegate to teleport(Location)
        Player player = this.getPlayer();
        Location from = player.getLocation();
        Location to = new Location(this.getPlayer().getWorld(), d0, d1, d2, f, f1);
        PlayerTeleportEvent event = new PlayerTeleportEvent(player, from, to, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        this.server.getPluginManager().callEvent(event);

        from = event.getFrom();
        to = event.isCancelled() ? from : event.getTo();

        this.teleport(to);
    }

    public void teleport(Location dest) {
        double d0, d1, d2;
        float f, f1;

        d0 = dest.getX();
        d1 = dest.getY();
        d2 = dest.getZ();
        f = dest.getYaw();
        f1 = dest.getPitch();

        // TODO: make sure this is the best way to address this.
        if (Float.isNaN(f)) {
            f = 0;
        }

        if (Float.isNaN(f1)) {
            f1 = 0;
        }

        this.lastPosX = d0;
        this.lastPosY = d1;
        this.lastPosZ = d2;
        this.lastYaw = f;
        this.lastPitch = f1;
        this.justTeleported = true;
        // CraftBukkit end

        this.checkMovement = false;
        this.y = d0;
        this.z = d1;
        this.q = d2;
        this.player.setLocation(d0, d1, d2, f, f1);
        this.player.playerConnection.sendPacket(new PacketPlayOutPosition(d0, d1 + 1.6200000047683716D, d2, f, f1, false));
    }

    public void a(PacketPlayInBlockDig packetplayinblockdig) {
        if (this.player.dead) return; // CraftBukkit
        WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

        this.player.w();
        if (packetplayinblockdig.g() == 4) {
            // CraftBukkit start
            // If the ticks aren't the same then the count starts from 0 and we update the lastDropTick.
            if (this.lastDropTick != MinecraftServer.currentTick) {
                this.dropCount = 0;
                this.lastDropTick = MinecraftServer.currentTick;
            } else {
                // Else we increment the drop count and check the amount.
                this.dropCount++;
                if (this.dropCount >= 20) {
                    this.c.warn(this.player.getName() + " dropped their items too quickly!");
                    this.disconnect("You dropped your items too quickly (Hacking?)");
                    return;
                }
            }
            // CraftBukkit end
            this.player.a(false);
        } else if (packetplayinblockdig.g() == 3) {
            this.player.a(true);
        } else if (packetplayinblockdig.g() == 5) {
            this.player.by();
        } else {
            boolean flag = false;

            if (packetplayinblockdig.g() == 0) {
                flag = true;
            }

            if (packetplayinblockdig.g() == 1) {
                flag = true;
            }

            if (packetplayinblockdig.g() == 2) {
                flag = true;
            }

            int i = packetplayinblockdig.c();
            int j = packetplayinblockdig.d();
            int k = packetplayinblockdig.e();

            if (flag) {
                double d0 = this.player.locX - ((double) i + 0.5D);
                double d1 = this.player.locY - ((double) j + 0.5D) + 1.5D;
                double d2 = this.player.locZ - ((double) k + 0.5D);
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > 36.0D) {
                    return;
                }

                if (j >= this.minecraftServer.getMaxBuildHeight()) {
                    return;
                }
            }

            if (packetplayinblockdig.g() == 0) {
                if (!this.minecraftServer.a(worldserver, i, j, k, this.player)) {
                    this.player.playerInteractManager.dig(i, j, k, packetplayinblockdig.f());
                } else {
                    // CraftBukkit start
                    CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, i, j, k, packetplayinblockdig.f(), this.player.inventory.getItemInHand());
                    this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, worldserver));
                    // Update any tile entity data for this block
                    TileEntity tileentity = worldserver.getTileEntity(i, j, k);
                    if (tileentity != null) {
                        this.player.playerConnection.sendPacket(tileentity.getUpdatePacket());
                    }
                    // CraftBukkit end
                }
            } else if (packetplayinblockdig.g() == 2) {
                this.player.playerInteractManager.a(i, j, k);
                if (worldserver.getType(i, j, k).getMaterial() != Material.AIR) {
                    this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, worldserver));
                }
            } else if (packetplayinblockdig.g() == 1) {
                this.player.playerInteractManager.c(i, j, k);
                if (worldserver.getType(i, j, k).getMaterial() != Material.AIR) {
                    this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, worldserver));
                }
            }
        }
    }

    public void a(PacketPlayInBlockPlace packetplayinblockplace) {
        WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

        // CraftBukkit start
        if (this.player.dead) return;

        // This is a horrible hack needed because the client sends 2 packets on 'right mouse click'
        // aimed at a block. We shouldn't need to get the second packet if the data is handled
        // but we cannot know what the client will do, so we might still get it
        //
        // If the time between packets is small enough, and the 'signature' similar, we discard the
        // second one. This sadly has to remain until Mojang makes their packets saner. :(
        //  -- Grum
        if (packetplayinblockplace.getFace() == 255) {
            if (packetplayinblockplace.getItemStack() != null && packetplayinblockplace.getItemStack().getItem() == this.lastMaterial && this.lastPacket != null && packetplayinblockplace.timestamp - this.lastPacket < 100) {
                this.lastPacket = null;
                return;
            }
        } else {
            this.lastMaterial = packetplayinblockplace.getItemStack() == null ? null : packetplayinblockplace.getItemStack().getItem();
            this.lastPacket = packetplayinblockplace.timestamp;
        }
        // CraftBukkit - if rightclick decremented the item, always send the update packet. */
        // this is not here for CraftBukkit's own functionality; rather it is to fix
        // a notch bug where the item doesn't update correctly.
        boolean always = false;
        // CraftBukkit end

        ItemStack itemstack = this.player.inventory.getItemInHand();
        boolean flag = false;
        int i = packetplayinblockplace.c();
        int j = packetplayinblockplace.d();
        int k = packetplayinblockplace.e();
        int l = packetplayinblockplace.getFace();

        this.player.w();
        if (packetplayinblockplace.getFace() == 255) {
            if (itemstack == null) {
                return;
            }

            // CraftBukkit start
            int itemstackAmount = itemstack.count;
            org.bukkit.event.player.PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_AIR, itemstack);
            if (event.useItemInHand() != Event.Result.DENY) {
                this.player.playerInteractManager.useItem(this.player, this.player.world, itemstack);
            }

            // CraftBukkit - notch decrements the counter by 1 in the above method with food,
            // snowballs and so forth, but he does it in a place that doesn't cause the
            // inventory update packet to get sent
            always = (itemstack.count != itemstackAmount);
            // CraftBukkit end
        } else if (packetplayinblockplace.d() >= this.minecraftServer.getMaxBuildHeight() - 1 && (packetplayinblockplace.getFace() == 1 || packetplayinblockplace.d() >= this.minecraftServer.getMaxBuildHeight())) {
            ChatMessage chatmessage = new ChatMessage("build.tooHigh", new Object[] { Integer.valueOf(this.minecraftServer.getMaxBuildHeight())});

            chatmessage.b().setColor(EnumChatFormat.RED);
            this.player.playerConnection.sendPacket(new PacketPlayOutChat(chatmessage));
            flag = true;
        } else {
            // CraftBukkit start - Check if we can actually do something over this large a distance
            Location eyeLoc = this.getPlayer().getEyeLocation();
            if (Math.pow(eyeLoc.getX() - i, 2) + Math.pow(eyeLoc.getY() - j, 2) + Math.pow(eyeLoc.getZ() - k, 2) > PLACE_DISTANCE_SQUARED) {
                return;
            }

            this.player.playerInteractManager.interact(this.player, worldserver, itemstack, i, j, k, l, packetplayinblockplace.h(), packetplayinblockplace.i(), packetplayinblockplace.j());
            // CraftBukkit end

            flag = true;
        }

        if (flag) {
            this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, worldserver));
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

            this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, worldserver));
        }

        itemstack = this.player.inventory.getItemInHand();
        if (itemstack != null && itemstack.count == 0) {
            this.player.inventory.items[this.player.inventory.itemInHandIndex] = null;
            itemstack = null;
        }

        if (itemstack == null || itemstack.n() == 0) {
            this.player.h = true;
            this.player.inventory.items[this.player.inventory.itemInHandIndex] = ItemStack.b(this.player.inventory.items[this.player.inventory.itemInHandIndex]);
            Slot slot = this.player.activeContainer.a((IInventory) this.player.inventory, this.player.inventory.itemInHandIndex);

            this.player.activeContainer.b();
            this.player.h = false;
            // CraftBukkit - TODO CHECK IF NEEDED -- new if structure might not need 'always'. Kept it in for now, but may be able to remove in future
            if (!ItemStack.matches(this.player.inventory.getItemInHand(), packetplayinblockplace.getItemStack()) || always) {
                this.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, slot.rawSlotIndex, this.player.inventory.getItemInHand()));
            }
        }
    }

    public void a(IChatBaseComponent ichatbasecomponent) {
        if (this.isDisconnected()) return; // CraftBukkit - Don't trigger twice on kicks
        c.info(this.player.getName() + " lost connection: " + ichatbasecomponent.c()); // CraftBukkit - Don't toString the component
        this.minecraftServer.au();
        // CraftBukkit start - Replace vanilla quit message handling with our own.
        /*
        ChatMessage chatmessage = new ChatMessage("multiplayer.player.left", new Object[] { this.player.getScoreboardDisplayName()});

        chatmessage.b().setColor(EnumChatFormat.YELLOW);
        this.minecraftServer.getPlayerList().sendMessage(chatmessage);
        */

        this.player.n();
        String quitMessage = this.minecraftServer.getPlayerList().disconnect(this.player);
        if ((quitMessage != null) && (quitMessage.length() > 0)) {
            this.minecraftServer.getPlayerList().sendMessage(CraftChatMessage.fromString(quitMessage));
        }
        // CraftBukkit end
        if (this.minecraftServer.L() && this.player.getName().equals(this.minecraftServer.K())) {
            c.info("Stopping singleplayer server as player logged out");
            this.minecraftServer.safeShutdown();
        }
    }

    public void sendPacket(Packet packet) {
        if (packet instanceof PacketPlayOutChat) {
            PacketPlayOutChat packetplayoutchat = (PacketPlayOutChat) packet;
            EnumChatVisibility enumchatvisibility = this.player.getChatFlags();

            if (enumchatvisibility == EnumChatVisibility.HIDDEN) {
                return;
            }

            if (enumchatvisibility == EnumChatVisibility.SYSTEM && !packetplayoutchat.d()) {
                return;
            }
        }

        // CraftBukkit start
        if (packet == null) {
            return;
        } else if (packet instanceof PacketPlayOutSpawnPosition) {
            PacketPlayOutSpawnPosition packet6 = (PacketPlayOutSpawnPosition) packet;
            this.player.compassTarget = new Location(this.getPlayer().getWorld(), packet6.x, packet6.y, packet6.z);
        }
        // CraftBukkit end

        try {
            this.networkManager.handle(packet, new GenericFutureListener[0]);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Sending packet");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Packet being sent");

            crashreportsystemdetails.a("Packet class", (Callable) (new CrashReportConnectionPacketClass(this, packet)));
            throw new ReportedException(crashreport);
        }
    }

    public void a(PacketPlayInHeldItemSlot packetplayinhelditemslot) {
        // CraftBukkit start
        if (this.player.dead) return;

        if (packetplayinhelditemslot.c() >= 0 && packetplayinhelditemslot.c() < PlayerInventory.getHotbarSize()) {
            PlayerItemHeldEvent event = new PlayerItemHeldEvent(this.getPlayer(), this.player.inventory.itemInHandIndex, packetplayinhelditemslot.c());
            this.server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                this.sendPacket(new PacketPlayOutHeldItemSlot(this.player.inventory.itemInHandIndex));
                this.player.w();
                return;
            }
            // CraftBukkit end

            this.player.inventory.itemInHandIndex = packetplayinhelditemslot.c();
            this.player.w();
        } else {
            c.warn(this.player.getName() + " tried to set an invalid carried item");
            this.disconnect("Nope!"); // CraftBukkit
        }
    }

    public void a(PacketPlayInChat packetplayinchat) {
        if (this.player.getChatFlags() == EnumChatVisibility.HIDDEN) {
            ChatMessage chatmessage = new ChatMessage("chat.cannotSend", new Object[0]);

            chatmessage.b().setColor(EnumChatFormat.RED);
            this.sendPacket(new PacketPlayOutChat(chatmessage));
        } else {
            this.player.w();
            String s = packetplayinchat.c();

            s = StringUtils.normalizeSpace(s);

            for (int i = 0; i < s.length(); ++i) {
                if (!SharedConstants.isAllowedChatCharacter(s.charAt(i))) {
                    // CraftBukkit start - threadsafety
                    if (packetplayinchat.a()) {
                        Waitable waitable = new Waitable() {
                            @Override
                            protected Object evaluate() {
                                PlayerConnection.this.disconnect("Illegal characters in chat");
                                return null;
                            }
                        };

                        this.minecraftServer.processQueue.add(waitable);

                        try {
                            waitable.get();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } catch (ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        this.disconnect("Illegal characters in chat");
                    }
                    // CraftBukkit end
                    return;
                }
            }

            if (s.startsWith("/")) {
                // CraftBukkit start
                try {
                    this.minecraftServer.server.playerCommandState = true;
                    this.handleCommand(s);
                } finally {
                    this.minecraftServer.server.playerCommandState = false;
                }
            } else if (s.isEmpty()) {
                c.warn(this.player.getName() + " tried to send an empty message");
            } else if (getPlayer().isConversing()) {
                getPlayer().acceptConversationInput(s);
            } else if (this.player.getChatFlags() == EnumChatVisibility.SYSTEM) { // Re-add "Command Only" flag check
                ChatMessage chatmessage = new ChatMessage("chat.cannotSend", new Object[0]);

                chatmessage.b().setColor(EnumChatFormat.RED);
                this.sendPacket(new PacketPlayOutChat(chatmessage));
            } else if (true) {
                this.chat(s, packetplayinchat.a());
                // CraftBukkit end - the below is for reference. :)
            } else {
                ChatMessage chatmessage1 = new ChatMessage("chat.type.text", new Object[] { this.player.getScoreboardDisplayName(), s});

                this.minecraftServer.getPlayerList().sendMessage(chatmessage1, false);
            }

            // CraftBukkit start - replaced with thread safe throttle
            // this.chatThrottle += 20;
            if (chatSpamField.addAndGet(this, 20) > 200 && !this.minecraftServer.getPlayerList().isOp(this.player.getName())) {
                // CraftBukkit end
                this.disconnect("disconnect.spam");
            }
        }
    }

    // CraftBukkit start
    public void chat(String s, boolean async) {
        if (s.isEmpty()) {
            return;
        }

        if (!this.player.dead) {
            if (s.startsWith("/")) {
                this.handleCommand(s);
            } else if (this.player.getChatFlags() != EnumChatVisibility.FULL) {
                // Do nothing, this is coming from a plugin
            } else {
                Player player = this.getPlayer();
                AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(async, player, s, new LazyPlayerSet());
                this.server.getPluginManager().callEvent(event);

                if (PlayerChatEvent.getHandlerList().getRegisteredListeners().length != 0) {
                    // Evil plugins still listening to deprecated event
                    final PlayerChatEvent queueEvent = new PlayerChatEvent(player, event.getMessage(), event.getFormat(), event.getRecipients());
                    queueEvent.setCancelled(event.isCancelled());
                    Waitable waitable = new Waitable() {
                        @Override
                        protected Object evaluate() {
                            org.bukkit.Bukkit.getPluginManager().callEvent(queueEvent);

                            if (queueEvent.isCancelled()) {
                                return null;
                            }

                            String message = String.format(queueEvent.getFormat(), queueEvent.getPlayer().getDisplayName(), queueEvent.getMessage());
                            PlayerConnection.this.minecraftServer.console.sendMessage(message);
                            if (((LazyPlayerSet) queueEvent.getRecipients()).isLazy()) {
                                for (Object player : PlayerConnection.this.minecraftServer.getPlayerList().players) {
                                    ((EntityPlayer) player).sendMessage(CraftChatMessage.fromString(message));
                                }
                            } else {
                                for (Player player : queueEvent.getRecipients()) {
                                    player.sendMessage(message);
                                }
                            }
                            return null;
                        }};
                    if (async) {
                        minecraftServer.processQueue.add(waitable);
                    } else {
                        waitable.run();
                    }
                    try {
                        waitable.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // This is proper habit for java. If we aren't handling it, pass it on!
                    } catch (ExecutionException e) {
                        throw new RuntimeException("Exception processing chat event", e.getCause());
                    }
                } else {
                    if (event.isCancelled()) {
                        return;
                    }

                    s = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
                    minecraftServer.console.sendMessage(s);
                    if (((LazyPlayerSet) event.getRecipients()).isLazy()) {
                        for (Object recipient : minecraftServer.getPlayerList().players) {
                            ((EntityPlayer) recipient).sendMessage(CraftChatMessage.fromString(s));
                        }
                    } else {
                        for (Player recipient : event.getRecipients()) {
                            recipient.sendMessage(s);
                        }
                    }
                }
            }
        }
    }
    // CraftBukkit end

    private void handleCommand(String s) {
        // CraftBukkit start
        CraftPlayer player = this.getPlayer();

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, s, new LazyPlayerSet());
        this.server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        try {
            this.c.info(event.getPlayer().getName() + " issued server command: " + event.getMessage()); // CraftBukkit
            if (this.server.dispatchCommand(event.getPlayer(), event.getMessage().substring(1))) {
                return;
            }
        } catch (org.bukkit.command.CommandException ex) {
            player.sendMessage(org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
            java.util.logging.Logger.getLogger(PlayerConnection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            return;
        }
        //this.minecraftServer.getCommandHandler().a(this.player, s);
        // CraftBukkit end
    }

    public void a(PacketPlayInArmAnimation packetplayinarmanimation) {
        if (this.player.dead) return; // CraftBukkit
        this.player.w();
        if (packetplayinarmanimation.d() == 1) {
            // CraftBukkit start - Raytrace to look for 'rogue armswings'
            float f = 1.0F;
            float f1 = this.player.lastPitch + (this.player.pitch - this.player.lastPitch) * f;
            float f2 = this.player.lastYaw + (this.player.yaw - this.player.lastYaw) * f;
            double d0 = this.player.lastX + (this.player.locX - this.player.lastX) * (double) f;
            double d1 = this.player.lastY + (this.player.locY - this.player.lastY) * (double) f + 1.62D - (double) this.player.height;
            double d2 = this.player.lastZ + (this.player.locZ - this.player.lastZ) * (double) f;
            Vec3D vec3d = this.player.world.getVec3DPool().create(d0, d1, d2);

            float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
            float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);
            float f7 = f4 * f5;
            float f8 = f3 * f5;
            double d3 = 5.0D;
            Vec3D vec3d1 = vec3d.add((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
            MovingObjectPosition movingobjectposition = this.player.world.rayTrace(vec3d, vec3d1, true);

            if (movingobjectposition == null || movingobjectposition.type != EnumMovingObjectType.BLOCK) {
                CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_AIR, this.player.inventory.getItemInHand());
            }

            // Arm swing animation
            PlayerAnimationEvent event = new PlayerAnimationEvent(this.getPlayer());
            this.server.getPluginManager().callEvent(event);

            if (event.isCancelled()) return;
            // CraftBukkit end

            this.player.ba();
        }
    }

    public void a(PacketPlayInEntityAction packetplayinentityaction) {
        // CraftBukkit start
        if (this.player.dead) return;

        this.player.w();
        if (packetplayinentityaction.d() == 1 || packetplayinentityaction.d() == 2) {
            PlayerToggleSneakEvent event = new PlayerToggleSneakEvent(this.getPlayer(), packetplayinentityaction.d() == 1);
            this.server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
        }

        if (packetplayinentityaction.d() == 4 || packetplayinentityaction.d() == 5) {
            PlayerToggleSprintEvent event = new PlayerToggleSprintEvent(this.getPlayer(), packetplayinentityaction.d() == 4);
            this.server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
        }
        // CraftBukkit end

        if (packetplayinentityaction.d() == 1) {
            this.player.setSneaking(true);
        } else if (packetplayinentityaction.d() == 2) {
            this.player.setSneaking(false);
        } else if (packetplayinentityaction.d() == 4) {
            this.player.setSprinting(true);
        } else if (packetplayinentityaction.d() == 5) {
            this.player.setSprinting(false);
        } else if (packetplayinentityaction.d() == 3) {
            this.player.a(false, true, true);
            //this.checkMovement = false; // CraftBukkit - this is handled in teleport
        } else if (packetplayinentityaction.d() == 6) {
            if (this.player.vehicle != null && this.player.vehicle instanceof EntityHorse) {
                ((EntityHorse) this.player.vehicle).w(packetplayinentityaction.e());
            }
        } else if (packetplayinentityaction.d() == 7 && this.player.vehicle != null && this.player.vehicle instanceof EntityHorse) {
            ((EntityHorse) this.player.vehicle).g(this.player);
        }
    }

    public void a(PacketPlayInUseEntity packetplayinuseentity) {
        if (this.player.dead) return; // CraftBukkit
        WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
        Entity entity = packetplayinuseentity.a((World) worldserver);

        this.player.w();
        if (entity != null) {
            boolean flag = this.player.o(entity);
            double d0 = 36.0D;

            if (!flag) {
                d0 = 9.0D;
            }

            if (this.player.e(entity) < d0) {
                ItemStack itemInHand = this.player.inventory.getItemInHand(); // CraftBukkit
                if (packetplayinuseentity.c() == EnumEntityUseAction.INTERACT) {
                    // CraftBukkit start
                    boolean triggerTagUpdate = itemInHand != null && itemInHand.getItem() == Items.NAME_TAG && entity instanceof EntityInsentient;
                    boolean triggerChestUpdate = itemInHand != null && itemInHand.getItem() == Item.getItemOf(Blocks.CHEST) && entity instanceof EntityHorse;
                    boolean triggerLeashUpdate = itemInHand != null && itemInHand.getItem() == Items.LEASH && entity instanceof EntityInsentient;
                    PlayerInteractEntityEvent event = new PlayerInteractEntityEvent((Player) this.getPlayer(), entity.getBukkitEntity());
                    this.server.getPluginManager().callEvent(event);

                    if (triggerLeashUpdate && (event.isCancelled() || this.player.inventory.getItemInHand() == null || this.player.inventory.getItemInHand().getItem() != Items.LEASH)) {
                        // Refresh the current leash state
                        this.sendPacket(new PacketPlayOutAttachEntity(1, entity, ((EntityInsentient) entity).getLeashHolder()));
                    }

                    if (triggerTagUpdate && (event.isCancelled() || this.player.inventory.getItemInHand() == null || this.player.inventory.getItemInHand().getItem() != Items.NAME_TAG)) {
                        // Refresh the current entity metadata
                        this.sendPacket(new PacketPlayOutEntityMetadata(entity.getId(), entity.datawatcher, true));
                    }
                    if (triggerChestUpdate && (event.isCancelled() || this.player.inventory.getItemInHand() == null || this.player.inventory.getItemInHand().getItem() != Item.getItemOf(Blocks.CHEST))) {
                        this.sendPacket(new PacketPlayOutEntityMetadata(entity.getId(), entity.datawatcher, true));
                    }

                    if (event.isCancelled()) {
                        return;
                    }
                    // CraftBukkit end

                    this.player.p(entity);

                    // CraftBukkit start
                    if (itemInHand != null && itemInHand.count <= -1) {
                        this.player.updateInventory(this.player.activeContainer);
                    }
                    // CraftBukkit end
                } else if (packetplayinuseentity.c() == EnumEntityUseAction.ATTACK) {
                    if (entity instanceof EntityItem || entity instanceof EntityExperienceOrb || entity instanceof EntityArrow || entity == this.player) {
                        this.disconnect("Attempting to attack an invalid entity");
                        this.minecraftServer.warning("Player " + this.player.getName() + " tried to attack an invalid entity");
                        return;
                    }

                    this.player.attack(entity);

                    // CraftBukkit start
                    if (itemInHand != null && itemInHand.count <= -1) {
                        this.player.updateInventory(this.player.activeContainer);
                    }
                    // CraftBukkit end
                }
            }
        }
    }

    public void a(PacketPlayInClientCommand packetplayinclientcommand) {
        this.player.w();
        EnumClientCommand enumclientcommand = packetplayinclientcommand.c();

        switch (ClientCommandOrdinalWrapper.a[enumclientcommand.ordinal()]) {
        case 1:
            if (this.player.viewingCredits) {
                this.minecraftServer.getPlayerList().changeDimension(this.player, 0, PlayerTeleportEvent.TeleportCause.END_PORTAL); // CraftBukkit - reroute logic through custom portal management
            } else if (this.player.r().getWorldData().isHardcore()) {
                if (this.minecraftServer.L() && this.player.getName().equals(this.minecraftServer.K())) {
                    this.player.playerConnection.disconnect("You have died. Game over, man, it\'s game over!");
                    this.minecraftServer.S();
                } else {
                    BanEntry banentry = new BanEntry(this.player.getName());

                    banentry.setReason("Death in Hardcore");
                    this.minecraftServer.getPlayerList().getNameBans().add(banentry);
                    this.player.playerConnection.disconnect("You have died. Game over, man, it\'s game over!");
                }
            } else {
                if (this.player.getHealth() > 0.0F) {
                    return;
                }

                this.player = this.minecraftServer.getPlayerList().moveToWorld(this.player, 0, false);
            }
            break;

        case 2:
            this.player.x().a(this.player);
            break;

        case 3:
            this.player.a((Statistic) AchievementList.f);
        }
    }

    public void a(PacketPlayInCloseWindow packetplayinclosewindow) {
        if (this.player.dead) return; // CraftBukkit

        CraftEventFactory.handleInventoryCloseEvent(this.player); // CraftBukkit

        this.player.m();
    }

    public void a(PacketPlayInWindowClick packetplayinwindowclick) {
        if (this.player.dead) return; // CraftBukkit

        this.player.w();
        if (this.player.activeContainer.windowId == packetplayinwindowclick.c() && this.player.activeContainer.c(this.player)) {
            // CraftBukkit start - Call InventoryClickEvent
            if (packetplayinwindowclick.d() < -1 && packetplayinwindowclick.d() != -999) {
                return;
            }

            InventoryView inventory = this.player.activeContainer.getBukkitView();
            SlotType type = CraftInventoryView.getSlotType(inventory, packetplayinwindowclick.d());

            InventoryClickEvent event = null;
            ClickType click = ClickType.UNKNOWN;
            InventoryAction action = InventoryAction.UNKNOWN;

            ItemStack itemstack = null;

            if (packetplayinwindowclick.d() == -1) {
                type = SlotType.OUTSIDE; // override
                click = packetplayinwindowclick.e() == 0 ? ClickType.WINDOW_BORDER_LEFT : ClickType.WINDOW_BORDER_RIGHT;
                action = InventoryAction.NOTHING;
            } else if (packetplayinwindowclick.h() == 0) {
                if (packetplayinwindowclick.e() == 0) {
                    click = ClickType.LEFT;
                } else if (packetplayinwindowclick.e() == 1) {
                    click = ClickType.RIGHT;
                }
                if (packetplayinwindowclick.e() == 0 || packetplayinwindowclick.e() == 1) {
                    action = InventoryAction.NOTHING; // Don't want to repeat ourselves
                    if (packetplayinwindowclick.d() == -999) {
                        if (player.inventory.getCarried() != null) {
                            action = packetplayinwindowclick.e() == 0 ? InventoryAction.DROP_ALL_CURSOR : InventoryAction.DROP_ONE_CURSOR;
                        }
                    } else {
                        Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.d());
                        if (slot != null) {
                            ItemStack clickedItem = slot.getItem();
                            ItemStack cursor = player.inventory.getCarried();
                            if (clickedItem == null) {
                                if (cursor != null) {
                                    action = packetplayinwindowclick.e() == 0 ? InventoryAction.PLACE_ALL : InventoryAction.PLACE_ONE;
                                }
                            } else if (slot.a(player)) { // Should be Slot.isPlayerAllowed
                                if (cursor == null) {
                                    action = packetplayinwindowclick.e() == 0 ? InventoryAction.PICKUP_ALL : InventoryAction.PICKUP_HALF;
                                } else if (slot.isAllowed(cursor)) { // Should be Slot.isItemAllowed
                                    if (clickedItem.doMaterialsMatch(cursor) && ItemStack.equals(clickedItem, cursor)) {
                                        int toPlace = packetplayinwindowclick.e() == 0 ? cursor.count : 1;
                                        toPlace = Math.min(toPlace, clickedItem.getMaxStackSize() - clickedItem.count);
                                        toPlace = Math.min(toPlace, slot.inventory.getMaxStackSize() - clickedItem.count);
                                        if (toPlace == 1) {
                                            action = InventoryAction.PLACE_ONE;
                                        } else if (toPlace == cursor.count) {
                                            action = InventoryAction.PLACE_ALL;
                                        } else if (toPlace < 0) {
                                            action = toPlace != -1 ? InventoryAction.PICKUP_SOME : InventoryAction.PICKUP_ONE; // this happens with oversized stacks
                                        } else if (toPlace != 0) {
                                            action = InventoryAction.PLACE_SOME;
                                        }
                                    } else if (cursor.count <= slot.a()) { // Should be Slot.getMaxStackSize()
                                        action = InventoryAction.SWAP_WITH_CURSOR;
                                    }
                                } else if (cursor.getItem() == clickedItem.getItem() && (!cursor.usesData() || cursor.getData() == clickedItem.getData()) && ItemStack.equals(cursor, clickedItem)) {
                                    if (clickedItem.count >= 0) {
                                        if (clickedItem.count + cursor.count <= cursor.getMaxStackSize()) {
                                            // As of 1.5, this is result slots only
                                            action = InventoryAction.PICKUP_ALL;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (packetplayinwindowclick.h() == 1) {
                if (packetplayinwindowclick.e() == 0) {
                    click = ClickType.SHIFT_LEFT;
                } else if (packetplayinwindowclick.e() == 1) {
                    click = ClickType.SHIFT_RIGHT;
                }
                if (packetplayinwindowclick.e() == 0 || packetplayinwindowclick.e() == 1) {
                    if (packetplayinwindowclick.d() < 0) {
                        action = InventoryAction.NOTHING;
                    } else {
                        Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.d());
                        if (slot != null && slot.a(this.player) && slot.e()) { // Should be Slot.hasItem()
                            action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
                        } else {
                            action = InventoryAction.NOTHING;
                        }
                    }
                }
            } else if (packetplayinwindowclick.h() == 2) {
                if (packetplayinwindowclick.e() >= 0 && packetplayinwindowclick.e() < 9) {
                    click = ClickType.NUMBER_KEY;
                    Slot clickedSlot = this.player.activeContainer.getSlot(packetplayinwindowclick.d());
                    if (clickedSlot.a(player)) {
                        ItemStack hotbar = this.player.inventory.getItem(packetplayinwindowclick.e());
                        boolean canCleanSwap = hotbar == null || (clickedSlot.inventory == player.inventory && clickedSlot.isAllowed(hotbar)); // the slot will accept the hotbar item
                        if (clickedSlot.e()) {
                            if (canCleanSwap) {
                                action = InventoryAction.HOTBAR_SWAP;
                            } else {
                                int firstEmptySlot = player.inventory.j(); // Should be Inventory.firstEmpty()
                                if (firstEmptySlot > -1) {
                                    action = InventoryAction.HOTBAR_MOVE_AND_READD;
                                } else {
                                    action = InventoryAction.NOTHING; // This is not sane! Mojang: You should test for other slots of same type
                                }
                            }
                        } else if (!clickedSlot.e() && hotbar != null && clickedSlot.isAllowed(hotbar)) {
                            action = InventoryAction.HOTBAR_SWAP;
                        } else {
                            action = InventoryAction.NOTHING;
                        }
                    } else {
                        action = InventoryAction.NOTHING;
                    }
                    // Special constructor for number key
                    event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.d(), click, action, packetplayinwindowclick.e());
                }
            } else if (packetplayinwindowclick.h() == 3) {
                if (packetplayinwindowclick.e() == 2) {
                    click = ClickType.MIDDLE;
                    if (packetplayinwindowclick.d() == -999) {
                        action = InventoryAction.NOTHING;
                    } else {
                        Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.d());
                        if (slot != null && slot.e() && player.abilities.canInstantlyBuild && player.inventory.getCarried() == null) {
                            action = InventoryAction.CLONE_STACK;
                        } else {
                            action = InventoryAction.NOTHING;
                        }
                    }
                } else {
                    click = ClickType.UNKNOWN;
                    action = InventoryAction.UNKNOWN;
                }
            } else if (packetplayinwindowclick.h() == 4) {
                if (packetplayinwindowclick.d() >= 0) {
                    if (packetplayinwindowclick.e() == 0) {
                        click = ClickType.DROP;
                        Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.d());
                        if (slot != null && slot.e() && slot.a(player) && slot.getItem() != null && slot.getItem().getItem() != Item.getItemOf(Blocks.AIR)) {
                            action = InventoryAction.DROP_ONE_SLOT;
                        } else {
                            action = InventoryAction.NOTHING;
                        }
                    } else if (packetplayinwindowclick.e() == 1) {
                        click = ClickType.CONTROL_DROP;
                        Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.d());
                        if (slot != null && slot.e() && slot.a(player) && slot.getItem() != null && slot.getItem().getItem() != Item.getItemOf(Blocks.AIR)) {
                            action = InventoryAction.DROP_ALL_SLOT;
                        } else {
                            action = InventoryAction.NOTHING;
                        }
                    }
                } else {
                    // Sane default (because this happens when they are holding nothing. Don't ask why.)
                    click = ClickType.LEFT;
                    if (packetplayinwindowclick.e() == 1) {
                        click = ClickType.RIGHT;
                    }
                    action = InventoryAction.NOTHING;
                }
            } else if (packetplayinwindowclick.h() == 5) {
                itemstack = this.player.activeContainer.clickItem(packetplayinwindowclick.d(), packetplayinwindowclick.e(), 5, this.player);
            } else if (packetplayinwindowclick.h() == 6) {
                click = ClickType.DOUBLE_CLICK;
                action = InventoryAction.NOTHING;
                if (packetplayinwindowclick.d() >= 0 && this.player.inventory.getCarried() != null) {
                    ItemStack cursor = this.player.inventory.getCarried();
                    action = InventoryAction.NOTHING;
                    // Quick check for if we have any of the item
                    if (inventory.getTopInventory().contains(org.bukkit.Material.getMaterial(Item.b(cursor.getItem()))) || inventory.getBottomInventory().contains(org.bukkit.Material.getMaterial(Item.b(cursor.getItem())))) {
                        action = InventoryAction.COLLECT_TO_CURSOR;
                    }
                }
            }
            // TODO check on updates

            if (packetplayinwindowclick.h() != 5) {
                if (click == ClickType.NUMBER_KEY) {
                    event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.d(), click, action, packetplayinwindowclick.e());
                } else {
                    event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.d(), click, action);
                }

                org.bukkit.inventory.Inventory top = inventory.getTopInventory();
                if (packetplayinwindowclick.d() == 0 && top instanceof CraftingInventory) {
                    org.bukkit.inventory.Recipe recipe = ((CraftingInventory) top).getRecipe();
                    if (recipe != null) {
                        if (click == ClickType.NUMBER_KEY) {
                            event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.d(), click, action, packetplayinwindowclick.e());
                        } else {
                            event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.d(), click, action);
                        }
                    }
                }

                server.getPluginManager().callEvent(event);

                switch (event.getResult()) {
                    case ALLOW:
                    case DEFAULT:
                        itemstack = this.player.activeContainer.clickItem(packetplayinwindowclick.d(), packetplayinwindowclick.e(), packetplayinwindowclick.h(), this.player);
                        break;
                    case DENY:
                        /* Needs enum constructor in InventoryAction
                        if (action.modifiesOtherSlots()) {

                        } else {
                            if (action.modifiesCursor()) {
                                this.player.playerConnection.sendPacket(new Packet103SetSlot(-1, -1, this.player.inventory.getCarried()));
                            }
                            if (action.modifiesClicked()) {
                                this.player.playerConnection.sendPacket(new Packet103SetSlot(this.player.activeContainer.windowId, packet102windowclick.slot, this.player.activeContainer.getSlot(packet102windowclick.slot).getItem()));
                            }
                        }*/
                        switch (action) {
                            // Modified other slots
                            case PICKUP_ALL:
                            case MOVE_TO_OTHER_INVENTORY:
                            case HOTBAR_MOVE_AND_READD:
                            case HOTBAR_SWAP:
                            case COLLECT_TO_CURSOR:
                            case UNKNOWN:
                                this.player.updateInventory(this.player.activeContainer);
                                break;
                            // Modified cursor and clicked
                            case PICKUP_SOME:
                            case PICKUP_HALF:
                            case PICKUP_ONE:
                            case PLACE_ALL:
                            case PLACE_SOME:
                            case PLACE_ONE:
                            case SWAP_WITH_CURSOR:
                                this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.player.inventory.getCarried()));
                                this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, packetplayinwindowclick.d(), this.player.activeContainer.getSlot(packetplayinwindowclick.d()).getItem()));
                                break;
                            // Modified clicked only
                            case DROP_ALL_SLOT:
                            case DROP_ONE_SLOT:
                                this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, packetplayinwindowclick.d(), this.player.activeContainer.getSlot(packetplayinwindowclick.d()).getItem()));
                                break;
                            // Modified cursor only
                            case DROP_ALL_CURSOR:
                            case DROP_ONE_CURSOR:
                            case CLONE_STACK:
                                this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.player.inventory.getCarried()));
                                break;
                            // Nothing
                            case NOTHING:
                                break;
                        }
                        return;
                }
            }
            // CraftBukkit end

            if (ItemStack.matches(packetplayinwindowclick.g(), itemstack)) {
                this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(packetplayinwindowclick.c(), packetplayinwindowclick.f(), true));
                this.player.h = true;
                this.player.activeContainer.b();
                this.player.broadcastCarriedItem();
                this.player.h = false;
            } else {
                this.n.a(this.player.activeContainer.windowId, Short.valueOf(packetplayinwindowclick.f()));
                this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(packetplayinwindowclick.c(), packetplayinwindowclick.f(), false));
                this.player.activeContainer.a(this.player, false);
                ArrayList arraylist = new ArrayList();

                for (int i = 0; i < this.player.activeContainer.c.size(); ++i) {
                    arraylist.add(((Slot) this.player.activeContainer.c.get(i)).getItem());
                }

                this.player.a(this.player.activeContainer, arraylist);

                // CraftBukkit start - Send a Set Slot to update the crafting result slot
                if (type == SlotType.RESULT && itemstack != null) {
                    this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, 0, itemstack));
                }
                // CraftBukkit end
            }
        }
    }

    public void a(PacketPlayInEnchantItem packetplayinenchantitem) {
        this.player.w();
        if (this.player.activeContainer.windowId == packetplayinenchantitem.c() && this.player.activeContainer.c(this.player)) {
            this.player.activeContainer.a((EntityHuman) this.player, packetplayinenchantitem.d());
            this.player.activeContainer.b();
        }
    }

    public void a(PacketPlayInSetCreativeSlot packetplayinsetcreativeslot) {
        if (this.player.playerInteractManager.isCreative()) {
            boolean flag = packetplayinsetcreativeslot.c() < 0;
            ItemStack itemstack = packetplayinsetcreativeslot.d();
            boolean flag1 = packetplayinsetcreativeslot.c() >= 1 && packetplayinsetcreativeslot.c() < 36 + PlayerInventory.getHotbarSize();
            // CraftBukkit - Add invalidItems check
            boolean flag2 = itemstack == null || itemstack.getItem() != null && !invalidItems.contains(Item.b(itemstack.getItem()));
            boolean flag3 = itemstack == null || itemstack.getData() >= 0 && itemstack.count <= 64 && itemstack.count > 0;

            // CraftBukkit start - Call click event
            if (flag || (flag1 && !ItemStack.matches(this.player.defaultContainer.getSlot(packetplayinsetcreativeslot.c()).getItem(), packetplayinsetcreativeslot.d()))) { // Insist on valid slot

                org.bukkit.entity.HumanEntity player = this.player.getBukkitEntity();
                InventoryView inventory = new CraftInventoryView(player, player.getInventory(), this.player.defaultContainer);
                org.bukkit.inventory.ItemStack item = CraftItemStack.asBukkitCopy(packetplayinsetcreativeslot.d()); // Should be packet107setcreativeslot.newitem

                SlotType type = SlotType.QUICKBAR;
                if (flag) {
                    type = SlotType.OUTSIDE;
                } else if (packetplayinsetcreativeslot.c() < 36) {
                    if (packetplayinsetcreativeslot.c() >= 5 && packetplayinsetcreativeslot.c() < 9) {
                        type = SlotType.ARMOR;
                    } else {
                        type = SlotType.CONTAINER;
                    }
                }
                InventoryCreativeEvent event = new InventoryCreativeEvent(inventory, type, flag ? -999 : packetplayinsetcreativeslot.c(), item);
                server.getPluginManager().callEvent(event);

                itemstack = CraftItemStack.asNMSCopy(event.getCursor());

                switch (event.getResult()) {
                case ALLOW:
                    // Plugin cleared the id / stacksize checks
                    flag2 = flag3 = true;
                    break;
                case DEFAULT:
                    break;
                case DENY:
                    // Reset the slot
                    if (packetplayinsetcreativeslot.c() >= 0) {
                        this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.defaultContainer.windowId, packetplayinsetcreativeslot.c(), this.player.defaultContainer.getSlot(packetplayinsetcreativeslot.c()).getItem()));
                        this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, null));
                    }
                    return;
                }
            }
            // CraftBukkit end

            if (flag1 && flag2 && flag3) {
                if (itemstack == null) {
                    this.player.defaultContainer.setItem(packetplayinsetcreativeslot.c(), (ItemStack) null);
                } else {
                    this.player.defaultContainer.setItem(packetplayinsetcreativeslot.c(), itemstack);
                }

                this.player.defaultContainer.a(this.player, true);
            } else if (flag && flag2 && flag3 && this.x < 200) {
                this.x += 20;
                EntityItem entityitem = this.player.drop(itemstack, true);

                if (entityitem != null) {
                    entityitem.e();
                }
            }
        }
    }

    public void a(PacketPlayInTransaction packetplayintransaction) {
        if (this.player.dead) return; // CraftBukkit
        Short oshort = (Short) this.n.get(this.player.activeContainer.windowId);

        if (oshort != null && packetplayintransaction.d() == oshort.shortValue() && this.player.activeContainer.windowId == packetplayintransaction.c() && !this.player.activeContainer.c(this.player)) {
            this.player.activeContainer.a(this.player, true);
        }
    }

    public void a(PacketPlayInUpdateSign packetplayinupdatesign) {
        if (this.player.dead) return; // CraftBukkit

        this.player.w();
        WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

        if (worldserver.isLoaded(packetplayinupdatesign.c(), packetplayinupdatesign.d(), packetplayinupdatesign.e())) {
            TileEntity tileentity = worldserver.getTileEntity(packetplayinupdatesign.c(), packetplayinupdatesign.d(), packetplayinupdatesign.e());

            if (tileentity instanceof TileEntitySign) {
                TileEntitySign tileentitysign = (TileEntitySign) tileentity;

                if (!tileentitysign.a() || tileentitysign.b() != this.player) {
                    this.minecraftServer.warning("Player " + this.player.getName() + " just tried to change non-editable sign");
                    this.sendPacket(new PacketPlayOutUpdateSign(packetplayinupdatesign.c(), packetplayinupdatesign.d(), packetplayinupdatesign.e(), tileentitysign.lines)); // CraftBukkit
                    return;
                }
            }

            int i;
            int j;

            for (j = 0; j < 4; ++j) {
                boolean flag = true;

                if (packetplayinupdatesign.f()[j].length() > 15) {
                    flag = false;
                } else {
                    for (i = 0; i < packetplayinupdatesign.f()[j].length(); ++i) {
                        if (!SharedConstants.isAllowedChatCharacter(packetplayinupdatesign.f()[j].charAt(i))) {
                            flag = false;
                        }
                    }
                }

                if (!flag) {
                    packetplayinupdatesign.f()[j] = "!?";
                }
            }

            if (tileentity instanceof TileEntitySign) {
                j = packetplayinupdatesign.c();
                int k = packetplayinupdatesign.d();

                i = packetplayinupdatesign.e();
                TileEntitySign tileentitysign1 = (TileEntitySign) tileentity;

                // CraftBukkit start
                Player player = this.server.getPlayer(this.player);
                SignChangeEvent event = new SignChangeEvent((org.bukkit.craftbukkit.block.CraftBlock) player.getWorld().getBlockAt(j, k, i), this.server.getPlayer(this.player), packetplayinupdatesign.f());
                this.server.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    for (int l = 0; l < 4; ++l) {
                        tileentitysign1.lines[l] = event.getLine(l);
                        if(tileentitysign1.lines[l] == null) {
                            tileentitysign1.lines[l] = "";
                        }
                    }
                    tileentitysign1.isEditable = false;
                }
                // System.arraycopy(packetplayinupdatesign.f(), 0, tileentitysign1.lines, 0, 4);
                // CraftBukkit end

                tileentitysign1.update();
                worldserver.notify(j, k, i);
            }
        }
    }

    public void a(PacketPlayInKeepAlive packetplayinkeepalive) {
        if (packetplayinkeepalive.c() == this.h) {
            int i = (int) (this.d() - this.i);

            this.player.ping = (this.player.ping * 3 + i) / 4;
        }
    }

    private long d() {
        return System.nanoTime() / 1000000L;
    }

    public void a(PacketPlayInAbilities packetplayinabilities) {
        // CraftBukkit start - d() should be isFlying()
        if (this.player.abilities.canFly && this.player.abilities.isFlying != packetplayinabilities.d()) {
            PlayerToggleFlightEvent event = new PlayerToggleFlightEvent(this.server.getPlayer(this.player), packetplayinabilities.f());
            this.server.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.player.abilities.isFlying = packetplayinabilities.d(); // Actually set the player's flying status
            }
            else {
                this.player.updateAbilities(); // Tell the player their ability was reverted
            }
        }
        // CraftBukkit end
    }

    public void a(PacketPlayInTabComplete packetplayintabcomplete) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.minecraftServer.a(this.player, packetplayintabcomplete.c()).iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            arraylist.add(s);
        }

        this.player.playerConnection.sendPacket(new PacketPlayOutTabComplete((String[]) arraylist.toArray(new String[arraylist.size()])));
    }

    public void a(PacketPlayInSettings packetplayinsettings) {
        this.player.a(packetplayinsettings);
    }

    public void a(PacketPlayInCustomPayload packetplayincustompayload) {
        ItemStack itemstack;
        ItemStack itemstack1;

        // CraftBukkit start - Ignore empty payloads
        if (packetplayincustompayload.length <= 0) {
            return;
        }
        // CraftBukkit end

        if ("MC|BEdit".equals(packetplayincustompayload.c())) {
            try {
                itemstack = (new PacketDataSerializer(Unpooled.wrappedBuffer(packetplayincustompayload.e()))).c();
                if (!ItemBookAndQuill.a(itemstack.getTag())) {
                    throw new IOException("Invalid book tag!");
                }

                itemstack1 = this.player.inventory.getItemInHand();
                if (itemstack.getItem() == Items.BOOK_AND_QUILL && itemstack.getItem() == itemstack1.getItem()) {
                    CraftEventFactory.handleEditBookEvent(player, itemstack); // CraftBukkit
                }
                // CraftBukkit start
            } catch (Exception exception) {
                c.error("Couldn\'t handle book info", exception);
                this.disconnect("Invalid book data!");
                // CraftBukkit end
            }
        } else if ("MC|BSign".equals(packetplayincustompayload.c())) {
            try {
                itemstack = (new PacketDataSerializer(Unpooled.wrappedBuffer(packetplayincustompayload.e()))).c();
                if (!ItemWrittenBook.a(itemstack.getTag())) {
                    throw new IOException("Invalid book tag!");
                }

                itemstack1 = this.player.inventory.getItemInHand();
                if (itemstack.getItem() == Items.WRITTEN_BOOK && itemstack1.getItem() == Items.BOOK_AND_QUILL) {
                    CraftEventFactory.handleEditBookEvent(player, itemstack); // CraftBukkit
                }
                // CraftBukkit start
            } catch (Exception exception1) {
                c.error("Couldn\'t sign book", exception1);
                this.disconnect("Invalid book data!");
                // CraftBukkit end
            }
        } else {
            DataInputStream datainputstream;
            int i;

            if ("MC|TrSel".equals(packetplayincustompayload.c())) {
                try {
                    datainputstream = new DataInputStream(new ByteArrayInputStream(packetplayincustompayload.e()));
                    i = datainputstream.readInt();
                    Container container = this.player.activeContainer;

                    if (container instanceof ContainerMerchant) {
                        ((ContainerMerchant) container).e(i);
                    }
                } catch (Exception exception2) {
                    // CraftBukkit start
                    c.error("Couldn\'t select trade", exception2);
                    this.disconnect("Invalid trade data!");
                    // CraftBukkit end
                }
            } else if ("MC|AdvCdm".equals(packetplayincustompayload.c())) {
                if (!this.minecraftServer.getEnableCommandBlock()) {
                    this.player.sendMessage(new ChatMessage("advMode.notEnabled", new Object[0]));
                } else if (this.player.a(2, "") && this.player.abilities.canInstantlyBuild) {
                    try {
                        PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.wrappedBuffer(packetplayincustompayload.e()));
                        byte b0 = packetdataserializer.readByte();
                        CommandBlockListenerAbstract commandblocklistenerabstract = null;

                        if (b0 == 0) {
                            TileEntity tileentity = this.player.world.getTileEntity(packetdataserializer.readInt(), packetdataserializer.readInt(), packetdataserializer.readInt());

                            if (tileentity instanceof TileEntityCommand) {
                                commandblocklistenerabstract = ((TileEntityCommand) tileentity).a();
                            }
                        } else if (b0 == 1) {
                            Entity entity = this.player.world.getEntity(packetdataserializer.readInt());

                            if (entity instanceof EntityMinecartCommandBlock) {
                                commandblocklistenerabstract = ((EntityMinecartCommandBlock) entity).e();
                            }
                        }

                        String s = packetdataserializer.c(packetdataserializer.readableBytes());

                        if (commandblocklistenerabstract != null) {
                            commandblocklistenerabstract.a(s);
                            commandblocklistenerabstract.e();
                            this.player.sendMessage(new ChatMessage("advMode.setCommand.success", new Object[] { s}));
                        }
                    } catch (Exception exception3) {
                        // CraftBukkit start
                        c.error("Couldn\'t set command block", exception3);
                        this.disconnect("Invalid CommandBlock data!");
                        // CraftBukkit end
                    }
                } else {
                    this.player.sendMessage(new ChatMessage("advMode.notAllowed", new Object[0]));
                }
            } else if ("MC|Beacon".equals(packetplayincustompayload.c())) {
                if (this.player.activeContainer instanceof ContainerBeacon) {
                    try {
                        datainputstream = new DataInputStream(new ByteArrayInputStream(packetplayincustompayload.e()));
                        i = datainputstream.readInt();
                        int j = datainputstream.readInt();
                        ContainerBeacon containerbeacon = (ContainerBeacon) this.player.activeContainer;
                        Slot slot = containerbeacon.getSlot(0);

                        if (slot.e()) {
                            slot.a(1);
                            TileEntityBeacon tileentitybeacon = containerbeacon.e();

                            tileentitybeacon.d(i);
                            tileentitybeacon.e(j);
                            tileentitybeacon.update();
                        }
                    } catch (Exception exception4) {
                        // CraftBukkit start
                        c.error("Couldn\'t set beacon", exception4);
                        this.disconnect("Invalid beacon data!");
                        // CraftBukkit end
                    }
                }
            } else if ("MC|ItemName".equals(packetplayincustompayload.c()) && this.player.activeContainer instanceof ContainerAnvil) {
                ContainerAnvil containeranvil = (ContainerAnvil) this.player.activeContainer;

                if (packetplayincustompayload.e() != null && packetplayincustompayload.e().length >= 1) {
                    String s1 = SharedConstants.a(new String(packetplayincustompayload.e(), Charsets.UTF_8));

                    if (s1.length() <= 30) {
                        containeranvil.a(s1);
                    }
                } else {
                    containeranvil.a("");
                }
            }
            // CraftBukkit start
            else if (packetplayincustompayload.c().equals("REGISTER")) {
                try {
                    String channels = new String(packetplayincustompayload.e(), "UTF8");
                    for (String channel : channels.split("\0")) {
                        getPlayer().addChannel(channel);
                    }
                } catch (UnsupportedEncodingException ex) {
                    throw new AssertionError(ex);
                }
            } else if (packetplayincustompayload.c().equals("UNREGISTER")) {
                try {
                    String channels = new String(packetplayincustompayload.e(), "UTF8");
                    for (String channel : channels.split("\0")) {
                        getPlayer().removeChannel(channel);
                    }
                } catch (UnsupportedEncodingException ex) {
                    throw new AssertionError(ex);
                }
            } else {
                server.getMessenger().dispatchIncomingMessage(player.getBukkitEntity(), packetplayincustompayload.c(), packetplayincustompayload.e());
            }
            // CraftBukkit end
        }
    }

    public void a(EnumProtocol enumprotocol, EnumProtocol enumprotocol1) {
        if (enumprotocol1 != EnumProtocol.PLAY) {
            throw new IllegalStateException("Unexpected change in protocol!");
        }
    }

    // CraftBukkit start - Add "isDisconnected" method
    public final boolean isDisconnected() {
        return !NetworkManager.a(this.networkManager).config().isAutoRead();
    }
    // CraftBukkit end
}
