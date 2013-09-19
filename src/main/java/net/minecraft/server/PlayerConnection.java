package net.minecraft.server;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;

// CraftBukkit start
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.HashSet;

import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;

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

public class PlayerConnection extends Connection {

    public final INetworkManager networkManager;
    private final MinecraftServer minecraftServer;
    public boolean disconnected;
    public EntityPlayer player;
    private int e;
    private int f;
    private boolean g;
    private int h;
    private long i;
    private static Random j = new Random();
    private long k;
    private volatile int chatThrottle; private static final AtomicIntegerFieldUpdater chatSpamField = AtomicIntegerFieldUpdater.newUpdater(PlayerConnection.class, "chatThrottle"); // CraftBukkit - multithreaded field
    private int x = 0;
    private double y;
    private double z;
    private double p;
    public boolean checkMovement = true; // CraftBukkit - private -> public
    private IntHashMap r = new IntHashMap();

    public PlayerConnection(MinecraftServer minecraftserver, INetworkManager inetworkmanager, EntityPlayer entityplayer) {
        this.minecraftServer = minecraftserver;
        this.networkManager = inetworkmanager;
        inetworkmanager.a(this);
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

    // For the packet15 hack :(
    Long lastPacket;

    // Store the last block right clicked and what type it was
    private int lastMaterial;

    public CraftPlayer getPlayer() {
        return (this.player == null) ? null : (CraftPlayer) this.player.getBukkitEntity();
    }
    private final static HashSet<Integer> invalidItems = new HashSet<Integer>(java.util.Arrays.asList(8, 9, 10, 11, 26, 34, 36, 43, 51, 52, 55, 59, 60, 62, 63, 64, 68, 71, 74, 75, 83, 90, 92, 93, 94, 95, 104, 105, 115, 117, 118, 119, 125, 127, 132, 137, 140, 141, 142, 144)); // TODO: Check after every update.
    // CraftBukkit end

    public void e() {
        this.g = false;
        ++this.e;
        this.minecraftServer.methodProfiler.a("packetflow");
        this.networkManager.b();
        this.minecraftServer.methodProfiler.c("keepAlive");
        if ((long) this.e - this.k > 20L) {
            this.k = (long) this.e;
            this.i = System.nanoTime() / 1000000L;
            this.h = j.nextInt();
            this.sendPacket(new Packet0KeepAlive(this.h));
        }

        // CraftBukkit start
        for (int spam; (spam = this.chatThrottle) > 0 && !chatSpamField.compareAndSet(this, spam, spam - 1); ) ;
        /* Use thread-safe field access instead
        if (this.m > 0) {
            --this.m;
        }
        */
        // CraftBukkit end

        if (this.x > 0) {
            --this.x;
        }

        this.minecraftServer.methodProfiler.c("playerTick");
        this.minecraftServer.methodProfiler.b();
    }

    public void disconnect(String s) {
        if (!this.disconnected) {
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

            this.player.l();
            this.sendPacket(new Packet255KickDisconnect(s));
            this.networkManager.d();

            // CraftBukkit start
            leaveMessage = event.getLeaveMessage();
            if (leaveMessage != null && leaveMessage.length() > 0) {
                this.minecraftServer.getPlayerList().sendMessage(ChatMessage.d(leaveMessage));
            }
            // CraftBukkit end

            this.minecraftServer.getPlayerList().disconnect(this.player);
            this.disconnected = true;
        }
    }

    public void a(Packet27PlayerInput packet27playerinput) {
        this.player.a(packet27playerinput.d(), packet27playerinput.f(), packet27playerinput.g(), packet27playerinput.h());
    }

    public void a(Packet10Flying packet10flying) {
        WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

        this.g = true;
        if (!this.player.viewingCredits) {
            double d0;

            if (!this.checkMovement) {
                d0 = packet10flying.y - this.z;
                if (packet10flying.x == this.y && d0 * d0 < 0.01D && packet10flying.z == this.p) {
                    this.checkMovement = true;
                }
            }

            // CraftBukkit start
            Player player = this.getPlayer();
            Location from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); // Get the Players previous Event location.
            Location to = player.getLocation().clone(); // Start off the To location as the Players current location.

            // If the packet contains movement information then we update the To location with the correct XYZ.
            if (packet10flying.hasPos && !(packet10flying.hasPos && packet10flying.y == -999.0D && packet10flying.stance == -999.0D)) {
                to.setX(packet10flying.x);
                to.setY(packet10flying.y);
                to.setZ(packet10flying.z);
            }

            // If the packet contains look information then we update the To location with the correct Yaw & Pitch.
            if (packet10flying.hasLook) {
                to.setYaw(packet10flying.yaw);
                to.setPitch(packet10flying.pitch);
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
                        this.player.playerConnection.sendPacket(new Packet13PlayerLookMove(from.getX(), from.getY() + 1.6200000047683716D, from.getY(), from.getZ(), from.getYaw(), from.getPitch(), false));
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

            if (Double.isNaN(packet10flying.x) || Double.isNaN(packet10flying.y) || Double.isNaN(packet10flying.z) || Double.isNaN(packet10flying.stance)) {
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

                    this.player.vehicle.W();
                    d1 = this.player.locX;
                    d2 = this.player.locY;
                    d3 = this.player.locZ;
                    if (packet10flying.hasLook) {
                        f = packet10flying.yaw;
                        f1 = packet10flying.pitch;
                    }

                    this.player.onGround = packet10flying.g;
                    this.player.h();
                    this.player.X = 0.0F;
                    this.player.setLocation(d1, d2, d3, f, f1);
                    if (this.player.vehicle != null) {
                        this.player.vehicle.W();
                    }

                    this.minecraftServer.getPlayerList().d(this.player);
                    if (this.checkMovement) {
                        this.y = this.player.locX;
                        this.z = this.player.locY;
                        this.p = this.player.locZ;
                    }

                    worldserver.playerJoinedWorld(this.player);
                    return;
                }

                if (this.player.isSleeping()) {
                    this.player.h();
                    this.player.setLocation(this.y, this.z, this.p, this.player.yaw, this.player.pitch);
                    worldserver.playerJoinedWorld(this.player);
                    return;
                }

                d0 = this.player.locY;
                this.y = this.player.locX;
                this.z = this.player.locY;
                this.p = this.player.locZ;
                d1 = this.player.locX;
                d2 = this.player.locY;
                d3 = this.player.locZ;
                float f2 = this.player.yaw;
                float f3 = this.player.pitch;

                if (packet10flying.hasPos && packet10flying.y == -999.0D && packet10flying.stance == -999.0D) {
                    packet10flying.hasPos = false;
                }

                double d4;

                if (packet10flying.hasPos) {
                    d1 = packet10flying.x;
                    d2 = packet10flying.y;
                    d3 = packet10flying.z;
                    d4 = packet10flying.stance - packet10flying.y;
                    if (!this.player.isSleeping() && (d4 > 1.65D || d4 < 0.1D)) {
                        this.disconnect("Illegal stance");
                        this.minecraftServer.getLogger().warning(this.player.getName() + " had an illegal stance: " + d4);
                        return;
                    }

                    if (Math.abs(packet10flying.x) > 3.2E7D || Math.abs(packet10flying.z) > 3.2E7D) {
                        // CraftBukkit - teleport to previous position instead of kicking, players get stuck
                        this.a(this.y, this.z, this.p, this.player.yaw, this.player.pitch);
                        return;
                    }
                }

                if (packet10flying.hasLook) {
                    f2 = packet10flying.yaw;
                    f3 = packet10flying.pitch;
                }

                this.player.h();
                this.player.X = 0.0F;
                this.player.setLocation(this.y, this.z, this.p, f2, f3);
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

                if (d10 > 100.0D && this.checkMovement && (!this.minecraftServer.K() || !this.minecraftServer.J().equals(this.player.getName()))) { // CraftBukkit - Added this.checkMovement condition to solve this check being triggered by teleports
                    this.minecraftServer.getLogger().warning(this.player.getName() + " moved too quickly! " + d4 + "," + d5 + "," + d6 + " (" + d7 + ", " + d8 + ", " + d9 + ")");
                    this.a(this.y, this.z, this.p, this.player.yaw, this.player.pitch);
                    return;
                }

                float f4 = 0.0625F;
                boolean flag = worldserver.getCubes(this.player, this.player.boundingBox.clone().shrink((double) f4, (double) f4, (double) f4)).isEmpty();

                if (this.player.onGround && !packet10flying.g && d5 > 0.0D) {
                    this.player.a(0.2F);
                }

                this.player.move(d4, d5, d6);
                this.player.onGround = packet10flying.g;
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
                    this.minecraftServer.getLogger().warning(this.player.getName() + " moved wrongly!");
                }

                this.player.setLocation(d1, d2, d3, f2, f3);
                boolean flag2 = worldserver.getCubes(this.player, this.player.boundingBox.clone().shrink((double) f4, (double) f4, (double) f4)).isEmpty();

                if (flag && (flag1 || !flag2) && !this.player.isSleeping()) {
                    this.a(this.y, this.z, this.p, f2, f3);
                    return;
                }

                AxisAlignedBB axisalignedbb = this.player.boundingBox.clone().grow((double) f4, (double) f4, (double) f4).a(0.0D, -0.55D, 0.0D);

                if (!this.minecraftServer.getAllowFlight() && !this.player.abilities.canFly && !worldserver.c(axisalignedbb)) { // CraftBukkit - check abilities instead of creative mode
                    if (d11 >= -0.03125D) {
                        ++this.f;
                        if (this.f > 80) {
                            this.minecraftServer.getLogger().warning(this.player.getName() + " was kicked for floating too long!");
                            this.disconnect("Flying is not enabled on this server");
                            return;
                        }
                    }
                } else {
                    this.f = 0;
                }

                this.player.onGround = packet10flying.g;
                this.minecraftServer.getPlayerList().d(this.player);
                if (this.player.playerInteractManager.isCreative()) return; // CraftBukkit - fixed fall distance accumulating while being in Creative mode.
                this.player.b(this.player.locY - d0, packet10flying.g);
            } else if (this.e % 20 == 0) {
                this.a(this.y, this.z, this.p, this.player.yaw, this.player.pitch);
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
        this.p = d2;
        this.player.setLocation(d0, d1, d2, f, f1);
        this.player.playerConnection.sendPacket(new Packet13PlayerLookMove(d0, d1 + 1.6200000047683716D, d1, d2, f, f1, false));
    }

    public void a(Packet14BlockDig packet14blockdig) {
        if (this.player.dead) return; // CraftBukkit

        WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

        this.player.u();
        if (packet14blockdig.e == 4) {
            // CraftBukkit start
            // If the ticks aren't the same then the count starts from 0 and we update the lastDropTick.
            if (this.lastDropTick != MinecraftServer.currentTick) {
                this.dropCount = 0;
                this.lastDropTick = MinecraftServer.currentTick;
            } else {
                // Else we increment the drop count and check the amount.
                this.dropCount++;
                if (this.dropCount >= 20) {
                    this.minecraftServer.getLogger().warning(this.player.getName() + " dropped their items too quickly!");
                    this.disconnect("You dropped your items too quickly (Hacking?)");
                    return;
                }
            }
            // CraftBukkit end
            this.player.a(false);
        } else if (packet14blockdig.e == 3) {
            this.player.a(true);
        } else if (packet14blockdig.e == 5) {
            this.player.bt();
        } else {
            boolean flag = false;

            if (packet14blockdig.e == 0) {
                flag = true;
            }

            if (packet14blockdig.e == 1) {
                flag = true;
            }

            if (packet14blockdig.e == 2) {
                flag = true;
            }

            int i = packet14blockdig.a;
            int j = packet14blockdig.b;
            int k = packet14blockdig.c;

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

            if (packet14blockdig.e == 0) {
                // CraftBukkit start
                if (!this.minecraftServer.a(worldserver, i, j, k, this.player)) {
                    this.player.playerInteractManager.dig(i, j, k, packet14blockdig.face);
                } else {
                    CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, i, j, k, packet14blockdig.face, this.player.inventory.getItemInHand());
                    this.player.playerConnection.sendPacket(new Packet53BlockChange(i, j, k, worldserver));
                    // Update any tile entity data for this block
                    TileEntity tileentity = worldserver.getTileEntity(i, j, k);
                    if (tileentity != null) {
                        this.player.playerConnection.sendPacket(tileentity.getUpdatePacket());
                    }
                    // CraftBukkit end
                }
            } else if (packet14blockdig.e == 2) {
                this.player.playerInteractManager.a(i, j, k);
                if (worldserver.getTypeId(i, j, k) != 0) {
                    this.player.playerConnection.sendPacket(new Packet53BlockChange(i, j, k, worldserver));
                }
            } else if (packet14blockdig.e == 1) {
                this.player.playerInteractManager.c(i, j, k);
                if (worldserver.getTypeId(i, j, k) != 0) {
                    this.player.playerConnection.sendPacket(new Packet53BlockChange(i, j, k, worldserver));
                }
            }
        }
    }

    public void a(Packet15Place packet15place) {
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

        this.player.u();
        if (packet15place.getFace() == 255) {
            if (packet15place.getItemStack() != null && packet15place.getItemStack().id == this.lastMaterial && this.lastPacket != null && packet15place.timestamp - this.lastPacket < 100) {
                this.lastPacket = null;
                return;
            }
        } else {
            this.lastMaterial = packet15place.getItemStack() == null ? -1 : packet15place.getItemStack().id;
            this.lastPacket = packet15place.timestamp;
        }

        // CraftBukkit - if rightclick decremented the item, always send the update packet.
        // this is not here for CraftBukkit's own functionality; rather it is to fix
        // a notch bug where the item doesn't update correctly.
        boolean always = false;

        // CraftBukkit end

        ItemStack itemstack = this.player.inventory.getItemInHand();
        boolean flag = false;
        int i = packet15place.d();
        int j = packet15place.f();
        int k = packet15place.g();
        int l = packet15place.getFace();

        if (packet15place.getFace() == 255) {
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
        } else if (packet15place.f() >= this.minecraftServer.getMaxBuildHeight() - 1 && (packet15place.getFace() == 1 || packet15place.f() >= this.minecraftServer.getMaxBuildHeight())) {
            this.player.playerConnection.sendPacket(new Packet3Chat(ChatMessage.b("build.tooHigh", new Object[] { Integer.valueOf(this.minecraftServer.getMaxBuildHeight())}).a(EnumChatFormat.RED)));
            flag = true;
        } else {
            // CraftBukkit start - Check if we can actually do something over this large a distance
            Location eyeLoc = this.getPlayer().getEyeLocation();
            if (Math.pow(eyeLoc.getX() - i, 2) + Math.pow(eyeLoc.getY() - j, 2) + Math.pow(eyeLoc.getZ() - k, 2) > PLACE_DISTANCE_SQUARED) {
                return;
            }

            this.player.playerInteractManager.interact(this.player, worldserver, itemstack, i, j, k, l, packet15place.j(), packet15place.k(), packet15place.l());
            // CraftBukkit end

            flag = true;
        }

        if (flag) {
            this.player.playerConnection.sendPacket(new Packet53BlockChange(i, j, k, worldserver));
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

            this.player.playerConnection.sendPacket(new Packet53BlockChange(i, j, k, worldserver));
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
            if (!ItemStack.matches(this.player.inventory.getItemInHand(), packet15place.getItemStack()) || always) {
                this.sendPacket(new Packet103SetSlot(this.player.activeContainer.windowId, slot.g, this.player.inventory.getItemInHand()));
            }
        }
    }

    public void a(String s, Object[] aobject) {
        if (this.disconnected) return; // CraftBukkit - Rarely it would send a disconnect line twice

        this.minecraftServer.getLogger().info(this.player.getName() + " lost connection: " + s);
        // CraftBukkit start - We need to handle custom quit messages
        String quitMessage = this.minecraftServer.getPlayerList().disconnect(this.player);
        if ((quitMessage != null) && (quitMessage.length() > 0)) {
            this.minecraftServer.getPlayerList().sendMessage(ChatMessage.d(quitMessage));
        }
        // CraftBukkit end
        this.disconnected = true;
        if (this.minecraftServer.K() && this.player.getName().equals(this.minecraftServer.J())) {
            this.minecraftServer.getLogger().info("Stopping singleplayer server as player logged out");
            this.minecraftServer.safeShutdown();
        }
    }

    public void onUnhandledPacket(Packet packet) {
        if (this.disconnected) return; // CraftBukkit
        this.minecraftServer.getLogger().warning(this.getClass() + " wasn\'t prepared to deal with a " + packet.getClass());
        this.disconnect("Protocol error, unexpected packet");
    }

    public void sendPacket(Packet packet) {
        if (packet instanceof Packet3Chat) {
            Packet3Chat packet3chat = (Packet3Chat) packet;
            int i = this.player.getChatFlags();

            if (i == 2) {
                return;
            }

            if (i == 1 && !packet3chat.isServer()) {
                return;
            }
        }

        // CraftBukkit start
        if (packet == null) {
            return;
        } else if (packet instanceof Packet6SpawnPosition) {
            Packet6SpawnPosition packet6 = (Packet6SpawnPosition) packet;
            this.player.compassTarget = new Location(this.getPlayer().getWorld(), packet6.x, packet6.y, packet6.z);
        }
        // CraftBukkit end

        try {
            this.networkManager.queue(packet);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Sending packet");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Packet being sent");

            crashreportsystemdetails.a("Packet ID", (Callable) (new CrashReportConnectionPacketID(this, packet)));
            crashreportsystemdetails.a("Packet class", (Callable) (new CrashReportConnectionPacketClass(this, packet)));
            throw new ReportedException(crashreport);
        }
    }

    public void a(Packet16BlockItemSwitch packet16blockitemswitch) {
        // CraftBukkit start
        if (this.player.dead) return;

        if (packet16blockitemswitch.itemInHandIndex >= 0 && packet16blockitemswitch.itemInHandIndex < PlayerInventory.getHotbarSize()) {
            PlayerItemHeldEvent event = new PlayerItemHeldEvent(this.getPlayer(), this.player.inventory.itemInHandIndex, packet16blockitemswitch.itemInHandIndex);
            this.server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                this.sendPacket(new Packet16BlockItemSwitch(this.player.inventory.itemInHandIndex));
                this.player.u();
                return;
            }
            // CraftBukkit end

            this.player.inventory.itemInHandIndex = packet16blockitemswitch.itemInHandIndex;
            this.player.u();
        } else {
            this.minecraftServer.getLogger().warning(this.player.getName() + " tried to set an invalid carried item");
            this.disconnect("Nope!"); // CraftBukkit
        }
    }

    public void a(Packet3Chat packet3chat) {
        if (this.player.getChatFlags() == 2) {
            this.sendPacket(new Packet3Chat(ChatMessage.e("chat.cannotSend").a(EnumChatFormat.RED)));
        } else {
            this.player.u();
            String s = packet3chat.message;

            if (s.length() > 100) {
                // CraftBukkit start
                if (packet3chat.a_()) {
                    Waitable waitable = new Waitable() {
                        @Override
                        protected Object evaluate() {
                            PlayerConnection.this.disconnect("Chat message too long");
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
                    this.disconnect("Chat message too long");
                }
                // CraftBukkit end
            } else {
                s = StringUtils.normalizeSpace(s);

                for (int i = 0; i < s.length(); ++i) {
                    if (!SharedConstants.isAllowedChatCharacter(s.charAt(i))) {
                        // CraftBukkit start
                        if (packet3chat.a_()) {
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

                // CraftBukkit start
                if (this.player.getChatFlags() == 1 && !s.startsWith("/")) {
                    this.sendPacket(new Packet3Chat(ChatMessage.e("chat.cannotSend").a(EnumChatFormat.RED)));
                    return;
                }

                this.chat(s, packet3chat.a_());

                // This section stays because it is only applicable to packets
                if (chatSpamField.addAndGet(this, 20) > 200 && !this.minecraftServer.getPlayerList().isOp(this.player.getName())) { // CraftBukkit use thread-safe spam
                    if (packet3chat.a_()) {
                        Waitable waitable = new Waitable() {
                            @Override
                            protected Object evaluate() {
                                PlayerConnection.this.disconnect("disconnect.spam");
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
                        this.disconnect("disconnect.spam");
                    }
                }
            }
        }
    }

    public void chat(String s, boolean async) {
        if (!this.player.dead) {
            if (s.length() == 0) {
                this.minecraftServer.getLogger().warning(this.player.getName() + " tried to send an empty message");
                return;
            }

            if (getPlayer().isConversing()) {
                getPlayer().acceptConversationInput(s);
                return;
            }

            if (s.startsWith("/")) {
                this.handleCommand(s);
                return;
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
                                    ((EntityPlayer) player).sendMessage(ChatMessage.d(message));
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
                            ((EntityPlayer) recipient).sendMessage(ChatMessage.d(s));
                        }
                    } else {
                        for (Player recipient : event.getRecipients()) {
                            recipient.sendMessage(s);
                        }
                    }
                }
            }
        }

        return;
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
            this.minecraftServer.getLogger().info(event.getPlayer().getName() + " issued server command: " + event.getMessage()); // CraftBukkit
            if (this.server.dispatchCommand(event.getPlayer(), event.getMessage().substring(1))) {
                return;
            }
        } catch (org.bukkit.command.CommandException ex) {
            player.sendMessage(org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
            java.util.logging.Logger.getLogger(PlayerConnection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            return;
        }
        // CraftBukkit end

        /* CraftBukkit start - No longer needed as we have already handled it in server.dispatchServerCommand above.
        this.minecraftServer.getCommandHandler().a(this.player, s);
        // CraftBukkit end */
    }

    public void a(Packet18ArmAnimation packet18armanimation) {
        if (this.player.dead) return; // CraftBukkit

        this.player.u();
        if (packet18armanimation.b == 1) {
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

            if (movingobjectposition == null || movingobjectposition.type != EnumMovingObjectType.TILE) {
                CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_AIR, this.player.inventory.getItemInHand());
            }

            // Arm swing animation
            PlayerAnimationEvent event = new PlayerAnimationEvent(this.getPlayer());
            this.server.getPluginManager().callEvent(event);

            if (event.isCancelled()) return;
            // CraftBukkit end

            this.player.aV();
        }
    }

    public void a(Packet19EntityAction packet19entityaction) {
        // CraftBukkit start
        if (this.player.dead) return;

        this.player.u();
        if (packet19entityaction.animation == 1 || packet19entityaction.animation == 2) {
            PlayerToggleSneakEvent event = new PlayerToggleSneakEvent(this.getPlayer(), packet19entityaction.animation == 1);
            this.server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
        }

        if (packet19entityaction.animation == 4 || packet19entityaction.animation == 5) {
            PlayerToggleSprintEvent event = new PlayerToggleSprintEvent(this.getPlayer(), packet19entityaction.animation == 4);
            this.server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
        }
        // CraftBukkit end

        if (packet19entityaction.animation == 1) {
            this.player.setSneaking(true);
        } else if (packet19entityaction.animation == 2) {
            this.player.setSneaking(false);
        } else if (packet19entityaction.animation == 4) {
            this.player.setSprinting(true);
        } else if (packet19entityaction.animation == 5) {
            this.player.setSprinting(false);
        } else if (packet19entityaction.animation == 3) {
            this.player.a(false, true, true);
            // this.checkMovement = false; // CraftBukkit - this is handled in teleport
        } else if (packet19entityaction.animation == 6) {
            if (this.player.vehicle != null && this.player.vehicle instanceof EntityHorse) {
                ((EntityHorse) this.player.vehicle).u(packet19entityaction.c);
            }
        } else if (packet19entityaction.animation == 7 && this.player.vehicle != null && this.player.vehicle instanceof EntityHorse) {
            ((EntityHorse) this.player.vehicle).f(this.player);
        }
    }

    public void a(Packet255KickDisconnect packet255kickdisconnect) {
        this.networkManager.a("disconnect.quitting", new Object[0]);
    }

    public int lowPriorityCount() {
        return this.networkManager.e();
    }

    public void a(Packet7UseEntity packet7useentity) {
        if (this.player.dead) return; // CraftBukkit

        WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
        Entity entity = worldserver.getEntity(packet7useentity.target);

        this.player.u();
        if (entity != null) {
            boolean flag = this.player.o(entity);
            double d0 = 36.0D;

            if (!flag) {
                d0 = 9.0D;
            }

            if (this.player.e(entity) < d0) {
                ItemStack itemInHand = this.player.inventory.getItemInHand(); // CraftBukkit
                if (packet7useentity.action == 0) {
                    // CraftBukkit start
                    boolean triggerTagUpdate = itemInHand != null && itemInHand.id == Item.NAME_TAG.id && entity instanceof EntityInsentient;
                    boolean triggerChestUpdate = itemInHand != null && itemInHand.id == Block.CHEST.id && entity instanceof EntityHorse;
                    boolean triggerLeashUpdate = itemInHand != null && itemInHand.id == Item.LEASH.id && entity instanceof EntityInsentient;
                    PlayerInteractEntityEvent event = new PlayerInteractEntityEvent((Player) this.getPlayer(), entity.getBukkitEntity());
                    this.server.getPluginManager().callEvent(event);

                    if (triggerLeashUpdate && (event.isCancelled() || this.player.inventory.getItemInHand() == null || this.player.inventory.getItemInHand().id != Item.LEASH.id)) {
                        // Refresh the current leash state
                        this.sendPacket(new Packet39AttachEntity(1, entity, ((EntityInsentient) entity).getLeashHolder()));
                    }

                    if (triggerTagUpdate && (event.isCancelled() || this.player.inventory.getItemInHand() == null || this.player.inventory.getItemInHand().id != Item.NAME_TAG.id)) {
                        // Refresh the current entity metadata
                        this.sendPacket(new Packet40EntityMetadata(entity.id, entity.datawatcher, true));
                    }
                    if (triggerChestUpdate && (event.isCancelled() || this.player.inventory.getItemInHand() == null || this.player.inventory.getItemInHand().id != Block.CHEST.id)) {
                        this.sendPacket(new Packet40EntityMetadata(entity.id, entity.datawatcher, true));
                    }

                    if (event.isCancelled()) {
                        return;
                    }
                    // CraftBukkit end
                    this.player.p(entity);
                    // CraftBukkit start - Update the client if the item is an infinite one
                    if (itemInHand != null && itemInHand.count <= -1) {
                        this.player.updateInventory(this.player.activeContainer);
                    }
                } else if (packet7useentity.action == 1) {
                    // CraftBukkit - Check for player
                    if ((entity instanceof EntityItem) || (entity instanceof EntityExperienceOrb) || (entity instanceof EntityArrow) || (entity == this.player)) {
                        String type = entity.getClass().getSimpleName();
                        disconnect("Attacking an " + type + " is not permitted");
                        System.out.println("Player " + player.getName() + " tried to attack an " + type + ", so I have disconnected them for exploiting.");
                        return;
                    }

                    this.player.attack(entity);

                    if (itemInHand != null && itemInHand.count <= -1) {
                        this.player.updateInventory(this.player.activeContainer);
                    }
                    // CraftBukkit end
                }
            }
        }
    }

    public void a(Packet205ClientCommand packet205clientcommand) {
        this.player.u();
        if (packet205clientcommand.a == 1) {
            if (this.player.viewingCredits) {
                this.minecraftServer.getPlayerList().changeDimension(this.player, 0, PlayerTeleportEvent.TeleportCause.END_PORTAL); // CraftBukkit - reroute logic through custom portal management
            } else if (this.player.p().getWorldData().isHardcore()) {
                if (this.minecraftServer.K() && this.player.getName().equals(this.minecraftServer.J())) {
                    this.player.playerConnection.disconnect("You have died. Game over, man, it\'s game over!");
                    this.minecraftServer.R();
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
        }
    }

    public boolean b() {
        return true;
    }

    public void a(Packet9Respawn packet9respawn) {}

    public void handleContainerClose(Packet101CloseWindow packet101closewindow) {
        if (this.player.dead) return; // CraftBukkit

        CraftEventFactory.handleInventoryCloseEvent(this.player); // CraftBukkit

        this.player.k();
    }

    public void a(Packet102WindowClick packet102windowclick) {
        if (this.player.dead) return; // CraftBukkit

        this.player.u();
        if (this.player.activeContainer.windowId == packet102windowclick.a && this.player.activeContainer.c(this.player)) {
            // CraftBukkit start - Call InventoryClickEvent
            if (packet102windowclick.slot < -1 && packet102windowclick.slot != -999) {
                return;
            }

            InventoryView inventory = this.player.activeContainer.getBukkitView();
            SlotType type = CraftInventoryView.getSlotType(inventory, packet102windowclick.slot);

            InventoryClickEvent event = null;
            ClickType click = ClickType.UNKNOWN;
            InventoryAction action = InventoryAction.UNKNOWN;

            ItemStack itemstack = null;

            if (packet102windowclick.slot == -1) {
                type = SlotType.OUTSIDE; // override
                click = packet102windowclick.button == 0 ? ClickType.WINDOW_BORDER_LEFT : ClickType.WINDOW_BORDER_RIGHT;
                action = InventoryAction.NOTHING;
            } else if (packet102windowclick.shift == 0) {
                if (packet102windowclick.button == 0) {
                    click = ClickType.LEFT;
                } else if (packet102windowclick.button == 1) {
                    click = ClickType.RIGHT;
                }
                if (packet102windowclick.button == 0 || packet102windowclick.button == 1) {
                    action = InventoryAction.NOTHING; // Don't want to repeat ourselves
                    if (packet102windowclick.slot == -999) {
                        if (player.inventory.getCarried() != null) {
                            action = packet102windowclick.button == 0 ? InventoryAction.DROP_ALL_CURSOR : InventoryAction.DROP_ONE_CURSOR;
                        }
                    } else {
                        Slot slot = this.player.activeContainer.getSlot(packet102windowclick.slot);
                        if (slot != null) {
                            ItemStack clickedItem = slot.getItem();
                            ItemStack cursor = player.inventory.getCarried();
                            if (clickedItem == null) {
                                if (cursor != null) {
                                    action = packet102windowclick.button == 0 ? InventoryAction.PLACE_ALL : InventoryAction.PLACE_ONE;
                                }
                            } else if (slot.a(player)) { // Should be Slot.isPlayerAllowed
                                if (cursor == null) {
                                    action = packet102windowclick.button == 0 ? InventoryAction.PICKUP_ALL : InventoryAction.PICKUP_HALF;
                                } else if (slot.isAllowed(cursor)) { // Should be Slot.isItemAllowed
                                    if (clickedItem.doMaterialsMatch(cursor) && ItemStack.equals(clickedItem, cursor)) {
                                        int toPlace = packet102windowclick.button == 0 ? cursor.count : 1;
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
                                } else if (cursor.id == clickedItem.id && (!cursor.usesData() || cursor.getData() == clickedItem.getData()) && ItemStack.equals(cursor, clickedItem)) {
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
            } else if (packet102windowclick.shift == 1) {
                if (packet102windowclick.button == 0) {
                    click = ClickType.SHIFT_LEFT;
                } else if (packet102windowclick.button == 1) {
                    click = ClickType.SHIFT_RIGHT;
                }
                if (packet102windowclick.button == 0 || packet102windowclick.button == 1) {
                    if (packet102windowclick.slot < 0) {
                        action = InventoryAction.NOTHING;
                    } else {
                        Slot slot = this.player.activeContainer.getSlot(packet102windowclick.slot);
                        if (slot != null && slot.a(this.player) && slot.e()) { // Should be Slot.hasItem()
                            action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
                        } else {
                            action = InventoryAction.NOTHING;
                        }
                    }
                }
            } else if (packet102windowclick.shift == 2) {
                if (packet102windowclick.button >= 0 && packet102windowclick.button < 9) {
                    click = ClickType.NUMBER_KEY;
                    Slot clickedSlot = this.player.activeContainer.getSlot(packet102windowclick.slot);
                    if (clickedSlot.a(player)) {
                        ItemStack hotbar = this.player.inventory.getItem(packet102windowclick.button);
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
                    event = new InventoryClickEvent(inventory, type, packet102windowclick.slot, click, action, packet102windowclick.button);
                }
            } else if (packet102windowclick.shift == 3) {
                if (packet102windowclick.button == 2) {
                    click = ClickType.MIDDLE;
                    if (packet102windowclick.slot == -999) {
                        action = InventoryAction.NOTHING;
                    } else {
                        Slot slot = this.player.activeContainer.getSlot(packet102windowclick.slot);
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
            } else if (packet102windowclick.shift == 4) {
                if (packet102windowclick.slot >= 0) {
                    if (packet102windowclick.button == 0) {
                        click = ClickType.DROP;
                        Slot slot = this.player.activeContainer.getSlot(packet102windowclick.slot);
                        if (slot != null && slot.e() && slot.a(player) && slot.getItem() != null && slot.getItem().id != 0) {
                            action = InventoryAction.DROP_ONE_SLOT;
                        } else {
                            action = InventoryAction.NOTHING;
                        }
                    } else if (packet102windowclick.button == 1) {
                        click = ClickType.CONTROL_DROP;
                        Slot slot = this.player.activeContainer.getSlot(packet102windowclick.slot);
                        if (slot != null && slot.e() && slot.a(player) && slot.getItem() != null && slot.getItem().id != 0) {
                            action = InventoryAction.DROP_ALL_SLOT;
                        } else {
                            action = InventoryAction.NOTHING;
                        }
                    }
                } else {
                    // Sane default (because this happens when they are holding nothing. Don't ask why.)
                    click = ClickType.LEFT;
                    if (packet102windowclick.button == 1) {
                        click = ClickType.RIGHT;
                    }
                    action = InventoryAction.NOTHING;
                }
            } else if (packet102windowclick.shift == 5) {
                itemstack = this.player.activeContainer.clickItem(packet102windowclick.slot, packet102windowclick.button, 5, this.player);
            } else if (packet102windowclick.shift == 6) {
                click = ClickType.DOUBLE_CLICK;
                action = InventoryAction.NOTHING;
                if (packet102windowclick.slot >= 0 && this.player.inventory.getCarried() != null) {
                    ItemStack cursor = this.player.inventory.getCarried();
                    action = InventoryAction.NOTHING;
                    // Quick check for if we have any of the item
                    if (inventory.getTopInventory().contains(cursor.id) || inventory.getBottomInventory().contains(cursor.id)) {
                        action = InventoryAction.COLLECT_TO_CURSOR;
                    }
                }
            }
            // TODO check on updates

            if (packet102windowclick.shift != 5) {
                if (click == ClickType.NUMBER_KEY) {
                    event = new InventoryClickEvent(inventory, type, packet102windowclick.slot, click, action, packet102windowclick.button);
                } else {
                    event = new InventoryClickEvent(inventory, type, packet102windowclick.slot, click, action);
                }

                org.bukkit.inventory.Inventory top = inventory.getTopInventory();
                if (packet102windowclick.slot == 0 && top instanceof CraftingInventory) {
                    org.bukkit.inventory.Recipe recipe = ((CraftingInventory) top).getRecipe();
                    if (recipe != null) {
                        if (click == ClickType.NUMBER_KEY) {
                            event = new CraftItemEvent(recipe, inventory, type, packet102windowclick.slot, click, action, packet102windowclick.button);
                        } else {
                            event = new CraftItemEvent(recipe, inventory, type, packet102windowclick.slot, click, action);
                        }
                    }
                }

                server.getPluginManager().callEvent(event);

                switch (event.getResult()) {
                    case ALLOW:
                    case DEFAULT:
                        itemstack = this.player.activeContainer.clickItem(packet102windowclick.slot, packet102windowclick.button, packet102windowclick.shift, this.player);
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
                                this.player.playerConnection.sendPacket(new Packet103SetSlot(-1, -1, this.player.inventory.getCarried()));
                                this.player.playerConnection.sendPacket(new Packet103SetSlot(this.player.activeContainer.windowId, packet102windowclick.slot, this.player.activeContainer.getSlot(packet102windowclick.slot).getItem()));
                                break;
                            // Modified clicked only
                            case DROP_ALL_SLOT:
                            case DROP_ONE_SLOT:
                                this.player.playerConnection.sendPacket(new Packet103SetSlot(this.player.activeContainer.windowId, packet102windowclick.slot, this.player.activeContainer.getSlot(packet102windowclick.slot).getItem()));
                                break;
                            // Modified cursor only
                            case DROP_ALL_CURSOR:
                            case DROP_ONE_CURSOR:
                            case CLONE_STACK:
                                this.player.playerConnection.sendPacket(new Packet103SetSlot(-1, -1, this.player.inventory.getCarried()));
                                break;
                            // Nothing
                            case NOTHING:
                                break;
                        }
                        return;
                }
            }
            // CraftBukkit end

            if (ItemStack.matches(packet102windowclick.item, itemstack)) {
                this.player.playerConnection.sendPacket(new Packet106Transaction(packet102windowclick.a, packet102windowclick.d, true));
                this.player.h = true;
                this.player.activeContainer.b();
                this.player.broadcastCarriedItem();
                this.player.h = false;
            } else {
                this.r.a(this.player.activeContainer.windowId, Short.valueOf(packet102windowclick.d));
                this.player.playerConnection.sendPacket(new Packet106Transaction(packet102windowclick.a, packet102windowclick.d, false));
                this.player.activeContainer.a(this.player, false);
                ArrayList arraylist = new ArrayList();

                for (int i = 0; i < this.player.activeContainer.c.size(); ++i) {
                    arraylist.add(((Slot) this.player.activeContainer.c.get(i)).getItem());
                }

                this.player.a(this.player.activeContainer, arraylist);

                // CraftBukkit start - Send a Set Slot to update the crafting result slot
                if (type == SlotType.RESULT && itemstack != null) {
                    this.player.playerConnection.sendPacket((Packet) (new Packet103SetSlot(this.player.activeContainer.windowId, 0, itemstack)));
                }
                // CraftBukkit end
            }
        }
    }

    public void a(Packet108ButtonClick packet108buttonclick) {
        this.player.u();
        if (this.player.activeContainer.windowId == packet108buttonclick.a && this.player.activeContainer.c(this.player)) {
            this.player.activeContainer.a((EntityHuman) this.player, packet108buttonclick.b);
            this.player.activeContainer.b();
        }
    }

    public void a(Packet107SetCreativeSlot packet107setcreativeslot) {
        if (this.player.playerInteractManager.isCreative()) {
            boolean flag = packet107setcreativeslot.slot < 0;
            ItemStack itemstack = packet107setcreativeslot.b;
            boolean flag1 = packet107setcreativeslot.slot >= 1 && packet107setcreativeslot.slot < 36 + PlayerInventory.getHotbarSize();
            // CraftBukkit
            boolean flag2 = itemstack == null || itemstack.id < Item.byId.length && itemstack.id >= 0 && Item.byId[itemstack.id] != null && !invalidItems.contains(itemstack.id);
            boolean flag3 = itemstack == null || itemstack.getData() >= 0 && itemstack.getData() >= 0 && itemstack.count <= 64 && itemstack.count > 0;

            // CraftBukkit start - Call click event
            if (flag || (flag1 && !ItemStack.matches(this.player.defaultContainer.getSlot(packet107setcreativeslot.slot).getItem(), packet107setcreativeslot.b))) { // Insist on valid slot

                org.bukkit.entity.HumanEntity player = this.player.getBukkitEntity();
                InventoryView inventory = new CraftInventoryView(player, player.getInventory(), this.player.defaultContainer);
                org.bukkit.inventory.ItemStack item = CraftItemStack.asBukkitCopy(packet107setcreativeslot.b); // Should be packet107setcreativeslot.newitem

                SlotType type = SlotType.QUICKBAR;
                if (flag) {
                    type = SlotType.OUTSIDE;
                } else if (packet107setcreativeslot.slot < 36) {
                    if (packet107setcreativeslot.slot >= 5 && packet107setcreativeslot.slot < 9) {
                        type = SlotType.ARMOR;
                    } else {
                        type = SlotType.CONTAINER;
                    }
                }
                InventoryCreativeEvent event = new InventoryCreativeEvent(inventory, type, flag ? -999 : packet107setcreativeslot.slot, item);
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
                    if (packet107setcreativeslot.slot >= 0) {
                        this.player.playerConnection.sendPacket(new Packet103SetSlot(this.player.defaultContainer.windowId, packet107setcreativeslot.slot, this.player.defaultContainer.getSlot(packet107setcreativeslot.slot).getItem()));
                        this.player.playerConnection.sendPacket(new Packet103SetSlot(-1, -1, null));
                    }
                    return;
                }
            }
            // CraftBukkit end

            if (flag1 && flag2 && flag3) {
                if (itemstack == null) {
                    this.player.defaultContainer.setItem(packet107setcreativeslot.slot, (ItemStack) null);
                } else {
                    this.player.defaultContainer.setItem(packet107setcreativeslot.slot, itemstack);
                }

                this.player.defaultContainer.a(this.player, true);
            } else if (flag && flag2 && flag3 && this.x < 200) {
                this.x += 20;
                EntityItem entityitem = this.player.drop(itemstack);

                if (entityitem != null) {
                    entityitem.c();
                }
            }
        }
    }

    public void a(Packet106Transaction packet106transaction) {
        if (this.player.dead) return; // CraftBukkit
        Short oshort = (Short) this.r.get(this.player.activeContainer.windowId);

        if (oshort != null && packet106transaction.b == oshort.shortValue() && this.player.activeContainer.windowId == packet106transaction.a && !this.player.activeContainer.c(this.player)) {
            this.player.activeContainer.a(this.player, true);
        }
    }

    public void a(Packet130UpdateSign packet130updatesign) {
        if (this.player.dead) return; // CraftBukkit

        this.player.u();
        WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

        if (worldserver.isLoaded(packet130updatesign.x, packet130updatesign.y, packet130updatesign.z)) {
            TileEntity tileentity = worldserver.getTileEntity(packet130updatesign.x, packet130updatesign.y, packet130updatesign.z);

            if (tileentity instanceof TileEntitySign) {
                TileEntitySign tileentitysign = (TileEntitySign) tileentity;

                if (!tileentitysign.a() || tileentitysign.b() != this.player) {
                    this.minecraftServer.warning("Player " + this.player.getName() + " just tried to change non-editable sign");
                    this.sendPacket(new Packet130UpdateSign(packet130updatesign.x, packet130updatesign.y, packet130updatesign.z, tileentitysign.lines)); // CraftBukkit
                    return;
                }
            }

            int i;
            int j;

            for (j = 0; j < 4; ++j) {
                boolean flag = true;

                if (packet130updatesign.lines[j].length() > 15) {
                    flag = false;
                } else {
                    for (i = 0; i < packet130updatesign.lines[j].length(); ++i) {
                        if (SharedConstants.allowedCharacters.indexOf(packet130updatesign.lines[j].charAt(i)) < 0) {
                            flag = false;
                        }
                    }
                }

                if (!flag) {
                    packet130updatesign.lines[j] = "!?";
                }
            }

            if (tileentity instanceof TileEntitySign) {
                j = packet130updatesign.x;
                int k = packet130updatesign.y;

                i = packet130updatesign.z;
                TileEntitySign tileentitysign1 = (TileEntitySign) tileentity;

                // CraftBukkit start
                Player player = this.server.getPlayer(this.player);
                SignChangeEvent event = new SignChangeEvent((org.bukkit.craftbukkit.block.CraftBlock) player.getWorld().getBlockAt(j, k, i), this.server.getPlayer(this.player), packet130updatesign.lines);
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
                // CraftBukkit end

                tileentitysign1.update();
                worldserver.notify(j, k, i);
            }
        }
    }

    public void a(Packet0KeepAlive packet0keepalive) {
        if (packet0keepalive.a == this.h) {
            int i = (int) (System.nanoTime() / 1000000L - this.i);

            this.player.ping = (this.player.ping * 3 + i) / 4;
        }
    }

    public boolean a() {
        return true;
    }

    public void a(Packet202Abilities packet202abilities) {
        // CraftBukkit start
        if (this.player.abilities.canFly && this.player.abilities.isFlying != packet202abilities.f()) {
            PlayerToggleFlightEvent event = new PlayerToggleFlightEvent(this.server.getPlayer(this.player), packet202abilities.f());
            this.server.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.player.abilities.isFlying = packet202abilities.f(); // Actually set the player's flying status
            }
            else {
                this.player.updateAbilities(); // Tell the player their ability was reverted
            }
        }
        // CraftBukkit end
    }

    public void a(Packet203TabComplete packet203tabcomplete) {
        StringBuilder stringbuilder = new StringBuilder();

        String s;

        for (Iterator iterator = this.minecraftServer.a(this.player, packet203tabcomplete.d()).iterator(); iterator.hasNext(); stringbuilder.append(s)) {
            s = (String) iterator.next();
            if (stringbuilder.length() > 0) {
                stringbuilder.append('\0'); // CraftBukkit - fix decompile issue
            }
        }

        this.player.playerConnection.sendPacket(new Packet203TabComplete(stringbuilder.toString()));
    }

    public void a(Packet204LocaleAndViewDistance packet204localeandviewdistance) {
        this.player.a(packet204localeandviewdistance);
    }

    public void a(Packet250CustomPayload packet250custompayload) {
        DataInputStream datainputstream;
        ItemStack itemstack;
        ItemStack itemstack1;

        // CraftBukkit start - Ignore empty payloads
        if (packet250custompayload.length <= 0) {
            return;
        }
        // CraftBukkit end

        if ("MC|BEdit".equals(packet250custompayload.tag)) {
            try {
                datainputstream = new DataInputStream(new ByteArrayInputStream(packet250custompayload.data));
                itemstack = Packet.c(datainputstream);
                if (!ItemBookAndQuill.a(itemstack.getTag())) {
                    throw new IOException("Invalid book tag!");
                }

                itemstack1 = this.player.inventory.getItemInHand();
                if (itemstack != null && itemstack.id == Item.BOOK_AND_QUILL.id && itemstack.id == itemstack1.id) {
                    CraftEventFactory.handleEditBookEvent(player, itemstack); // CraftBukkit
                }
                // CraftBukkit start
            } catch (Throwable exception) {
                this.minecraftServer.getLogger().warning(this.player.getName() + " sent invalid MC|BEdit data", exception);
                this.disconnect("Invalid book data!");
                // CraftBukkit end
            }
        } else if ("MC|BSign".equals(packet250custompayload.tag)) {
            try {
                datainputstream = new DataInputStream(new ByteArrayInputStream(packet250custompayload.data));
                itemstack = Packet.c(datainputstream);
                if (!ItemWrittenBook.a(itemstack.getTag())) {
                    throw new IOException("Invalid book tag!");
                }

                itemstack1 = this.player.inventory.getItemInHand();
                if (itemstack != null && itemstack.id == Item.WRITTEN_BOOK.id && itemstack1.id == Item.BOOK_AND_QUILL.id) {
                    CraftEventFactory.handleEditBookEvent(player, itemstack); // CraftBukkit
                }
                // CraftBukkit start
            } catch (Throwable exception1) {
                this.minecraftServer.getLogger().warning(this.player.getName() + " sent invalid MC|BSign data", exception1);
                this.disconnect("Invalid book data!");
                // CraftBukkit end
            }
        } else {
            int i;

            if ("MC|TrSel".equals(packet250custompayload.tag)) {
                try {
                    datainputstream = new DataInputStream(new ByteArrayInputStream(packet250custompayload.data));
                    i = datainputstream.readInt();
                    Container container = this.player.activeContainer;

                    if (container instanceof ContainerMerchant) {
                        ((ContainerMerchant) container).e(i);
                    }
                } catch (Exception exception2) {
                    // CraftBukkit start
                    this.minecraftServer.getLogger().warning(this.player.getName() + " sent invalid MC|TrSel data", exception2);
                    this.disconnect("Invalid trade data!");
                    // CraftBukkit end
                }
            } else {
                int j;

                if ("MC|AdvCdm".equals(packet250custompayload.tag)) {
                    if (!this.minecraftServer.getEnableCommandBlock()) {
                        this.player.sendMessage(ChatMessage.e("advMode.notEnabled"));
                    } else if (this.player.a(2, "") && this.player.abilities.canInstantlyBuild) {
                        try {
                            datainputstream = new DataInputStream(new ByteArrayInputStream(packet250custompayload.data));
                            i = datainputstream.readInt();
                            j = datainputstream.readInt();
                            int k = datainputstream.readInt();
                            String s = Packet.a((DataInput) datainputstream, 256);
                            TileEntity tileentity = this.player.world.getTileEntity(i, j, k);

                            if (tileentity != null && tileentity instanceof TileEntityCommand) {
                                ((TileEntityCommand) tileentity).a(s);
                                this.player.world.notify(i, j, k);
                                this.player.sendMessage(ChatMessage.b("advMode.setCommand.success", new Object[] { s}));
                            }
                        } catch (Exception exception3) {
                            // CraftBukkit start
                            this.minecraftServer.getLogger().warning(this.player.getName() + " sent invalid MC|AdvCdm data", exception3);
                            this.disconnect("Invalid CommandBlock data!");
                            // CraftBukkit end
                        }
                    } else {
                        this.player.sendMessage(ChatMessage.e("advMode.notAllowed"));
                    }
                } else if ("MC|Beacon".equals(packet250custompayload.tag)) {
                    if (this.player.activeContainer instanceof ContainerBeacon) {
                        try {
                            datainputstream = new DataInputStream(new ByteArrayInputStream(packet250custompayload.data));
                            i = datainputstream.readInt();
                            j = datainputstream.readInt();
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
                            this.minecraftServer.getLogger().warning(this.player.getName() + " sent invalid MC|Beacon data", exception4);
                            this.disconnect("Invalid beacon data!");
                            // CraftBukkit end
                        }
                    }
                } else if ("MC|ItemName".equals(packet250custompayload.tag) && this.player.activeContainer instanceof ContainerAnvil) {
                    ContainerAnvil containeranvil = (ContainerAnvil) this.player.activeContainer;

                    if (packet250custompayload.data != null && packet250custompayload.data.length >= 1) {
                        String s1 = SharedConstants.a(new String(packet250custompayload.data));

                        if (s1.length() <= 30) {
                            containeranvil.a(s1);
                        }
                    } else {
                        containeranvil.a("");
                    }
                }
                // CraftBukkit start
                else if (packet250custompayload.tag.equals("REGISTER")) {
                    try {
                        String channels = new String(packet250custompayload.data, "UTF8");
                        for (String channel : channels.split("\0")) {
                            getPlayer().addChannel(channel);
                        }
                    } catch (UnsupportedEncodingException ex) {
                        throw new AssertionError(ex);
                    }
                } else if (packet250custompayload.tag.equals("UNREGISTER")) {
                    try {
                        String channels = new String(packet250custompayload.data, "UTF8");
                        for (String channel : channels.split("\0")) {
                            getPlayer().removeChannel(channel);
                        }
                    } catch (UnsupportedEncodingException ex) {
                        throw new AssertionError(ex);
                    }
                } else {
                    server.getMessenger().dispatchIncomingMessage(player.getBukkitEntity(), packet250custompayload.tag, packet250custompayload.data);
                }
                // CraftBukkit end
            }
        }
    }

    public boolean c() {
        return this.disconnected;
    }
}
