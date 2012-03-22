package net.minecraft.server;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

// CraftBukkit start
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.ChunkCompressionThread;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;
// CraftBukkit end

public class NetServerHandler extends NetHandler implements ICommandListener {

    public static Logger logger = Logger.getLogger("Minecraft");
    public NetworkManager networkManager;
    public boolean disconnected = false;
    private MinecraftServer minecraftServer;
    public EntityPlayer player; // CraftBukkit - private -> public
    private int f;
    private int g;
    private boolean h;
    private int i;
    private long j;
    private static Random k = new Random();
    private long l;
    private int m = 0;
    private int x = 0;
    private double y;
    private double z;
    private double q;
    private boolean checkMovement = true;
    private IntHashMap s = new IntHashMap();

    public NetServerHandler(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
        this.minecraftServer = minecraftserver;
        this.networkManager = networkmanager;
        networkmanager.a((NetHandler) this);
        this.player = entityplayer;
        entityplayer.netServerHandler = this;

        // CraftBukkit start
        this.server = minecraftserver.server;
    }

    private final CraftServer server;
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
    private final static HashSet<Integer> invalidItems = new HashSet<Integer>(Arrays.asList(8, 9, 10, 11, 26, 34, 36, 51, 52, 55, 59, 60, 63, 64, 68, 71, 75, 78, 83, 90, 92, 93, 94, 95));
    // CraftBukkit end

    public void a() {
        this.h = false;
        ++this.f;
        this.networkManager.b();
        if ((long) this.f - this.l > 20L) {
            this.l = (long) this.f;
            this.j = System.nanoTime() / 1000000L;
            this.i = k.nextInt();
            this.sendPacket(new Packet0KeepAlive(this.i));
        }

        if (this.m > 0) {
            --this.m;
        }

        if (this.x > 0) {
            --this.x;
        }
    }

    public void disconnect(String s) {
        if (!this.disconnected) {
            // CraftBukkit start
            String leaveMessage = "\u00A7e" + this.player.name + " left the game.";

            PlayerKickEvent event = new PlayerKickEvent(this.server.getPlayer(this.player), s, leaveMessage);
            this.server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                // Do not kick the player
                return;
            }
            // Send the possibly modified leave message
            s = event.getReason();
            // CraftBukkit end

            this.player.I();
            this.sendPacket(new Packet255KickDisconnect(s));
            this.networkManager.d();

            // CraftBukkit start
            leaveMessage = event.getLeaveMessage();
            if (leaveMessage != null) {
                this.minecraftServer.serverConfigurationManager.sendAll(new Packet3Chat(leaveMessage));
            }
            getPlayer().disconnect(s);
            // CraftBukkit end

            this.minecraftServer.serverConfigurationManager.disconnect(this.player);
            this.disconnected = true;
        }
    }

    public void a(Packet10Flying packet10flying) {
        WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

        this.h = true;
        if (!this.player.viewingCredits) {
            double d0;

            if (!this.checkMovement) {
                d0 = packet10flying.y - this.z;
                if (packet10flying.x == this.y && d0 * d0 < 0.01D && packet10flying.z == this.q) {
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
                        this.player.netServerHandler.sendPacket(new Packet13PlayerLookMove(from.getX(), from.getY() + 1.6200000047683716D, from.getY(), from.getZ(), from.getYaw(), from.getPitch(), false));
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
                double d4;

                if (this.player.vehicle != null) {
                    float f = this.player.yaw;
                    float f1 = this.player.pitch;

                    this.player.vehicle.i_();
                    d1 = this.player.locX;
                    d2 = this.player.locY;
                    d3 = this.player.locZ;
                    double d5 = 0.0D;

                    d4 = 0.0D;
                    if (packet10flying.hasLook) {
                        f = packet10flying.yaw;
                        f1 = packet10flying.pitch;
                    }

                    if (packet10flying.hasPos && packet10flying.y == -999.0D && packet10flying.stance == -999.0D) {
                        // CraftBukkit start
                        if (Math.abs(packet10flying.x) > 1 || Math.abs(packet10flying.z) > 1) {
                            System.err.println(player.getName() + " was caught trying to crash the server with an invalid position.");
                            player.kickPlayer("Nope!");
                            return;
                        }
                        // CraftBukkit end
                        d5 = packet10flying.x;
                        d4 = packet10flying.z;
                    }

                    this.player.onGround = packet10flying.g;
                    this.player.a(true);
                    this.player.move(d5, 0.0D, d4);
                    this.player.setLocation(d1, d2, d3, f, f1);
                    this.player.motX = d5;
                    this.player.motZ = d4;
                    if (this.player.vehicle != null) {
                        worldserver.vehicleEnteredWorld(this.player.vehicle, true);
                    }

                    if (this.player.vehicle != null) {
                        this.player.vehicle.i_();
                    }

                    this.minecraftServer.serverConfigurationManager.d(this.player);
                    this.y = this.player.locX;
                    this.z = this.player.locY;
                    this.q = this.player.locZ;
                    worldserver.playerJoinedWorld(this.player);
                    return;
                }

                if (this.player.isSleeping()) {
                    this.player.a(true);
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

                if (packet10flying.hasPos && packet10flying.y == -999.0D && packet10flying.stance == -999.0D) {
                    packet10flying.hasPos = false;
                }

                if (packet10flying.hasPos) {
                    d1 = packet10flying.x;
                    d2 = packet10flying.y;
                    d3 = packet10flying.z;
                    d4 = packet10flying.stance - packet10flying.y;
                    if (!this.player.isSleeping() && (d4 > 1.65D || d4 < 0.1D)) {
                        this.disconnect("Illegal stance");
                        logger.warning(this.player.name + " had an illegal stance: " + d4);
                        return;
                    }

                    if (Math.abs(packet10flying.x) > 3.2E7D || Math.abs(packet10flying.z) > 3.2E7D) {
                        this.disconnect("Illegal position");
                        return;
                    }
                }

                if (packet10flying.hasLook) {
                    f2 = packet10flying.yaw;
                    f3 = packet10flying.pitch;
                }

                this.player.a(true);
                this.player.bO = 0.0F;
                this.player.setLocation(this.y, this.z, this.q, f2, f3);
                if (!this.checkMovement) {
                    return;
                }

                d4 = d1 - this.player.locX;
                double d6 = d2 - this.player.locY;
                double d7 = d3 - this.player.locZ;
                double d8 = d4 * d4 + d6 * d6 + d7 * d7;

                if (d8 > 100.0D && this.checkMovement) { // CraftBukkit - Added this.checkMovement condition to solve this check being triggered by teleports
                    logger.warning(this.player.name + " moved too quickly!");
                    this.disconnect("You moved too quickly :( (Hacking?)");
                    return;
                }

                float f4 = 0.0625F;
                boolean flag = worldserver.getCubes(this.player, this.player.boundingBox.clone().shrink((double) f4, (double) f4, (double) f4)).size() == 0;

                if (this.player.onGround && !packet10flying.g && d6 > 0.0D) {
                    this.player.c(0.2F);
                }

                this.player.move(d4, d6, d7);
                this.player.onGround = packet10flying.g;
                this.player.checkMovement(d4, d6, d7);
                double d9 = d6;

                d4 = d1 - this.player.locX;
                d6 = d2 - this.player.locY;
                if (d6 > -0.5D || d6 < 0.5D) {
                    d6 = 0.0D;
                }

                d7 = d3 - this.player.locZ;
                d8 = d4 * d4 + d6 * d6 + d7 * d7;
                boolean flag1 = false;

                if (d8 > 0.0625D && !this.player.isSleeping() && !this.player.itemInWorldManager.isCreative()) {
                    flag1 = true;
                    logger.warning(this.player.name + " moved wrongly!");
                    System.out.println("Got position " + d1 + ", " + d2 + ", " + d3);
                    System.out.println("Expected " + this.player.locX + ", " + this.player.locY + ", " + this.player.locZ);
                }

                this.player.setLocation(d1, d2, d3, f2, f3);
                boolean flag2 = worldserver.getCubes(this.player, this.player.boundingBox.clone().shrink((double) f4, (double) f4, (double) f4)).size() == 0;

                if (flag && (flag1 || !flag2) && !this.player.isSleeping()) {
                    this.a(this.y, this.z, this.q, f2, f3);
                    return;
                }

                AxisAlignedBB axisalignedbb = this.player.boundingBox.clone().grow((double) f4, (double) f4, (double) f4).a(0.0D, -0.55D, 0.0D);

                if (!this.minecraftServer.allowFlight && !this.player.itemInWorldManager.isCreative() && !worldserver.b(axisalignedbb)) {
                    if (d9 >= -0.03125D) {
                        ++this.g;
                        if (this.g > 80) {
                            logger.warning(this.player.name + " was kicked for floating too long!");
                            this.disconnect("Flying is not enabled on this server");
                            return;
                        }
                    }
                } else {
                    this.g = 0;
                }

                this.player.onGround = packet10flying.g;
                this.minecraftServer.serverConfigurationManager.d(this.player);
                if (this.player.itemInWorldManager.isCreative()) return; // CraftBukkit - fixed fall distance accumulating while being in Creative mode.
                this.player.b(this.player.locY - d0, packet10flying.g);
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
        this.player.netServerHandler.sendPacket(new Packet13PlayerLookMove(d0, d1 + 1.6200000047683716D, d1, d2, f, f1, false));
    }

    public void a(Packet14BlockDig packet14blockdig) {
        if (this.player.dead) return; // CraftBukkit

        WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

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
                    logger.warning(this.player.name + " dropped their items too quickly!");
                    this.disconnect("You dropped your items too quickly (Hacking?)");
                    return;
                }
            }
            // CraftBukkit end
            this.player.S();
        } else if (packet14blockdig.e == 5) {
            this.player.N();
        } else {
            boolean flag = worldserver.weirdIsOpCache = worldserver.dimension != 0 || this.minecraftServer.serverConfigurationManager.isOp(this.player.name); // CraftBukkit
            boolean flag1 = false;

            if (packet14blockdig.e == 0) {
                flag1 = true;
            }

            if (packet14blockdig.e == 2) {
                flag1 = true;
            }

            int i = packet14blockdig.a;
            int j = packet14blockdig.b;
            int k = packet14blockdig.c;

            if (flag1) {
                double d0 = this.player.locX - ((double) i + 0.5D);
                double d1 = this.player.locY - ((double) j + 0.5D) + 1.5D;
                double d2 = this.player.locZ - ((double) k + 0.5D);
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > 36.0D) {
                    return;
                }

                if (j >= this.minecraftServer.t) {
                    return;
                }
            }

            ChunkCoordinates chunkcoordinates = worldserver.getSpawn();
            int l = MathHelper.a(i - chunkcoordinates.x);
            int i1 = MathHelper.a(k - chunkcoordinates.z);

            if (l > i1) {
                i1 = l;
            }

            if (packet14blockdig.e == 0) {
                // CraftBukkit
                if (i1 < this.server.getSpawnRadius() && !flag) {
                    CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, i, j, k, l, this.player.inventory.getItemInHand());
                    this.player.netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, worldserver));
                } else {
                    this.player.itemInWorldManager.dig(i, j, k, packet14blockdig.face);
                }
            } else if (packet14blockdig.e == 2) {
                this.player.itemInWorldManager.a(i, j, k);
                if (worldserver.getTypeId(i, j, k) != 0) {
                    this.player.netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, worldserver));
                }
            } else if (packet14blockdig.e == 3) {
                double d4 = this.player.locX - ((double) i + 0.5D);
                double d5 = this.player.locY - ((double) j + 0.5D);
                double d6 = this.player.locZ - ((double) k + 0.5D);
                double d7 = d4 * d4 + d5 * d5 + d6 * d6;

                if (d7 < 256.0D) {
                    this.player.netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, worldserver));
                }
            }

            worldserver.weirdIsOpCache = false;
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

        if (packet15place.face == 255) {
            if (packet15place.itemstack != null && packet15place.itemstack.id == this.lastMaterial && this.lastPacket != null && packet15place.timestamp - this.lastPacket < 100) {
                this.lastPacket = null;
                return;
            }
        } else {
            this.lastMaterial = packet15place.itemstack == null ? -1 : packet15place.itemstack.id;
            this.lastPacket = packet15place.timestamp;
        }

        // CraftBukkit - if rightclick decremented the item, always send the update packet.
        // this is not here for CraftBukkit's own functionality; rather it is to fix
        // a notch bug where the item doesn't update correctly.
        boolean always = false;

        // CraftBukkit end

        ItemStack itemstack = this.player.inventory.getItemInHand();
        boolean flag = false;
        int i = packet15place.a;
        int j = packet15place.b;
        int k = packet15place.c;
        int l = packet15place.face;
        boolean flag1 = worldserver.weirdIsOpCache = worldserver.dimension != 0 || this.minecraftServer.serverConfigurationManager.isOp(this.player.name); // CraftBukkit

        if (packet15place.face == 255) {
            if (itemstack == null) {
                return;
            }

            // CraftBukkit start
            int itemstackAmount = itemstack.count;
            PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_AIR, itemstack);
            if (event.useItemInHand() != Event.Result.DENY) {
                this.player.itemInWorldManager.useItem(this.player, this.player.world, itemstack);
            }

            // CraftBukkit - notch decrements the counter by 1 in the above method with food,
            // snowballs and so forth, but he does it in a place that doesn't cause the
            // inventory update packet to get sent
            always = (itemstack.count != itemstackAmount);
            // CraftBukkit end
        } else if (packet15place.b >= this.minecraftServer.t - 1 && (packet15place.face == 1 || packet15place.b >= this.minecraftServer.t)) {
            this.player.netServerHandler.sendPacket(new Packet3Chat("\u00A77Height limit for building is " + this.minecraftServer.t));
            flag = true;
        } else {
            ChunkCoordinates chunkcoordinates = worldserver.getSpawn();
            int i1 = MathHelper.a(i - chunkcoordinates.x);
            int j1 = MathHelper.a(k - chunkcoordinates.z);

            if (i1 > j1) {
                j1 = i1;
            }

            // CraftBukkit start - Check if we can actually do something over this large a distance
            Location eyeLoc = this.getPlayer().getEyeLocation();
            if (Math.pow(eyeLoc.getX() - i, 2) + Math.pow(eyeLoc.getY() - j, 2) + Math.pow(eyeLoc.getZ() - k, 2) > PLACE_DISTANCE_SQUARED) {
                return;
            }
            flag1 = true; // spawn protection moved to ItemBlock!!!
            // CraftBukkit end

            if (j1 > 16 || flag1) {
                this.player.itemInWorldManager.interact(this.player, worldserver, itemstack, i, j, k, l);
            }

            flag = true;
        }

        if (flag) {
            this.player.netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, worldserver));
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

            this.player.netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, worldserver));
        }

        itemstack = this.player.inventory.getItemInHand();
        if (itemstack != null && itemstack.count == 0) {
            this.player.inventory.items[this.player.inventory.itemInHandIndex] = null;
            itemstack = null;
        }

        if (itemstack == null || itemstack.l() == 0) {
            this.player.h = true;
            this.player.inventory.items[this.player.inventory.itemInHandIndex] = ItemStack.b(this.player.inventory.items[this.player.inventory.itemInHandIndex]);
            Slot slot = this.player.activeContainer.a((IInventory) this.player.inventory, this.player.inventory.itemInHandIndex);

            this.player.activeContainer.a();
            this.player.h = false;
            // CraftBukkit - TODO CHECK IF NEEDED -- new if structure might not need 'always'. Kept it in for now, but may be able to remove in future
            if (!ItemStack.matches(this.player.inventory.getItemInHand(), packet15place.itemstack) || always) {
                this.sendPacket(new Packet103SetSlot(this.player.activeContainer.windowId, slot.c, this.player.inventory.getItemInHand()));
            }
        }

        worldserver.weirdIsOpCache = false;
    }

    public void a(String s, Object[] aobject) {
        if (this.disconnected) return; // CraftBukkit - rarely it would send a disconnect line twice

        logger.info(this.player.name + " lost connection: " + s);
        // CraftBukkit start - we need to handle custom quit messages
        String quitMessage = this.minecraftServer.serverConfigurationManager.disconnect(this.player);
        if ((quitMessage != null) && (quitMessage.length() > 0)) {
            this.minecraftServer.serverConfigurationManager.sendAll(new Packet3Chat(quitMessage));
        }
        // CraftBukkit end
        this.disconnected = true;
    }

    public void onUnhandledPacket(Packet packet) {
        if (this.disconnected) return; // CraftBukkit
        logger.warning(this.getClass() + " wasn\'t prepared to deal with a " + packet.getClass());
        this.disconnect("Protocol error, unexpected packet");
    }

    public void sendPacket(Packet packet) {
        // CraftBukkit start
        if (packet instanceof Packet6SpawnPosition) {
            Packet6SpawnPosition packet6 = (Packet6SpawnPosition) packet;
            this.player.compassTarget = new Location(this.getPlayer().getWorld(), packet6.x, packet6.y, packet6.z);
        } else if (packet instanceof Packet3Chat) {
            String message = ((Packet3Chat) packet).message;
            for (final String line : message.split("\n")) {
                this.networkManager.queue(new Packet3Chat(line));
            }
            packet = null;
        } else if (packet.lowPriority == true) {
            // Reroute all low-priority packets through to compression thread.
            ChunkCompressionThread.sendPacket(this.player, packet);
            packet = null;
        }
        if (packet != null) this.networkManager.queue(packet);
        // CraftBukkit end
    }

    public void a(Packet16BlockItemSwitch packet16blockitemswitch) {
        // CraftBukkit start
        if (this.player.dead) return;

        if (packet16blockitemswitch.itemInHandIndex >= 0 && packet16blockitemswitch.itemInHandIndex < PlayerInventory.getHotbarSize()) {
            PlayerItemHeldEvent event = new PlayerItemHeldEvent(this.getPlayer(), this.player.inventory.itemInHandIndex, packet16blockitemswitch.itemInHandIndex);
            this.server.getPluginManager().callEvent(event);
            // CraftBukkit end

            this.player.inventory.itemInHandIndex = packet16blockitemswitch.itemInHandIndex;
        } else {
            logger.warning(this.player.name + " tried to set an invalid carried item");
            this.disconnect("Nope!"); // CraftBukkit
        }
    }

    public void a(Packet3Chat packet3chat) {
        String s = packet3chat.message;

        if (s.length() > 100) {
            this.disconnect("Chat message too long");
        } else {
            s = s.trim();

            for (int i = 0; i < s.length(); ++i) {
                if (!SharedConstants.isAllowedChatCharacter(s.charAt(i))) {
                    this.disconnect("Illegal characters in chat");
                    return;
                }
            }

            // CraftBukkit start
            this.chat(s);
        }
    }

    public boolean chat(String s) {
        if (!this.player.dead) {
            if (s.length() == 0) {
                logger.warning(this.player.name + " tried to send an empty message");
                return false;
            }

            if (getPlayer().isConversing()) {
                getPlayer().acceptConversationInput(s);
                return true;
            }

            if (s.startsWith("/")) {
                this.handleCommand(s);
                return true;
            } else {
                Player player = this.getPlayer();
                PlayerChatEvent event = new PlayerChatEvent(player, s);
                this.server.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return true;
                }

                s = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
                minecraftServer.console.sendMessage(s);
                for (Player recipient : event.getRecipients()) {
                    recipient.sendMessage(s);
                }
                // CraftBukkit end
            }

            this.m += 20;
            if (this.m > 200) {
                this.disconnect("disconnect.spam");
            }
        }

        return false; // CraftBukkit
    }

    private void handleCommand(String s) {
        // CraftBukkit start
        CraftPlayer player = this.getPlayer();

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, s);
        this.server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        try {
            if (this.server.dispatchCommand(player, event.getMessage().substring(1))) {
                return;
            }
        } catch (CommandException ex) {
            player.sendMessage(ChatColor.RED + "An internal error occurred while attempting to perform this command");
            Logger.getLogger(NetServerHandler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            return;
        }
        // CraftBukkit end

        /* // CraftBukkit start - No longer needed as we have already handled it in server.dispatchServerCommand above.
        if (s.toLowerCase().startsWith("/me ")) {
            s = "* " + this.player.name + " " + s.substring(s.indexOf(" ")).trim();
            logger.info(s);
            this.minecraftServer.serverConfigurationManager.sendAll(new Packet3Chat(s));
        } else if (s.toLowerCase().startsWith("/kill")) {
            this.player.damageEntity(DamageSource.GENERIC, 1000);
        } else if (s.toLowerCase().startsWith("/tell ")) {
            String[] astring = s.split(" ");

            if (astring.length >= 3) {
                s = s.substring(s.indexOf(" ")).trim();
                s = s.substring(s.indexOf(" ")).trim();
                s = "\u00A77" + this.player.name + " whispers " + s;
                logger.info(s + " to " + astring[1]);
                if (!this.minecraftServer.serverConfigurationManager.a(astring[1], (Packet) (new Packet3Chat(s)))) {
                    this.sendPacket(new Packet3Chat("\u00A7cThere\'s no player by that name online."));
                }
            }
        } else {
            String s1;

            if (this.minecraftServer.serverConfigurationManager.isOp(this.player.name)) {
                s1 = s.substring(1);
                logger.info(this.player.name + " issued server command: " + s1);
                this.minecraftServer.issueCommand(s1, this);
            } else {
                s1 = s.substring(1);
                logger.info(this.player.name + " tried command: " + s1);
            }
        }
        // CraftBukkit end */
    }

    public void a(Packet18ArmAnimation packet18armanimation) {
        if (this.player.dead) return; // CraftBukkit

        if (packet18armanimation.b == 1) {
            // CraftBukkit start - raytrace to look for 'rogue armswings'
            float f = 1.0F;
            float f1 = this.player.lastPitch + (this.player.pitch - this.player.lastPitch) * f;
            float f2 = this.player.lastYaw + (this.player.yaw - this.player.lastYaw) * f;
            double d0 = this.player.lastX + (this.player.locX - this.player.lastX) * (double) f;
            double d1 = this.player.lastY + (this.player.locY - this.player.lastY) * (double) f + 1.62D - (double) this.player.height;
            double d2 = this.player.lastZ + (this.player.locZ - this.player.lastZ) * (double) f;
            Vec3D vec3d = Vec3D.create(d0, d1, d2);

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

            this.player.C_();
        }
    }

    public void a(Packet19EntityAction packet19entityaction) {
        // CraftBukkit start
        if (this.player.dead) return;

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
            this.player.setSneak(true);
        } else if (packet19entityaction.animation == 2) {
            this.player.setSneak(false);
        } else if (packet19entityaction.animation == 4) {
            this.player.setSprinting(true);
        } else if (packet19entityaction.animation == 5) {
            this.player.setSprinting(false);
        } else if (packet19entityaction.animation == 3) {
            this.player.a(false, true, true);
            this.checkMovement = false;
        }
    }

    public void a(Packet255KickDisconnect packet255kickdisconnect) {
        // CraftBukkit start
        getPlayer().disconnect("disconnect.quitting");
        // CraftBukkit end
        this.networkManager.a("disconnect.quitting", new Object[0]);
    }

    public int lowPriorityCount() {
        return this.networkManager.e();
    }

    public void sendMessage(String s) {
        this.sendPacket(new Packet3Chat("\u00A77" + s));
    }

    public String getName() {
        return this.player.name;
    }

    public void a(Packet7UseEntity packet7useentity) {
        if (this.player.dead) return; // CraftBukkit

        WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
        Entity entity = worldserver.getEntity(packet7useentity.target);

        if (entity != null) {
            boolean flag = this.player.h(entity);
            double d0 = 36.0D;

            if (!flag) {
                d0 = 9.0D;
            }

            if (this.player.j(entity) < d0) {
                ItemStack itemInHand = this.player.inventory.getItemInHand(); // CraftBukkit
                if (packet7useentity.action == 0) {
                    // CraftBukkit start
                    PlayerInteractEntityEvent event = new PlayerInteractEntityEvent((Player) this.getPlayer(), entity.getBukkitEntity());
                    this.server.getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }
                    // CraftBukkit end
                    this.player.e(entity);
                    // CraftBukkit start - update the client if the item is an infinite one
                    if (itemInHand != null && itemInHand.count <= -1) {
                        this.player.updateInventory(this.player.activeContainer);
                    }
                } else if (packet7useentity.action == 1) {
                    if ((entity instanceof EntityItem) || (entity instanceof EntityExperienceOrb) || (entity instanceof EntityArrow)) {
                        String type = entity.getClass().getSimpleName();
                        disconnect("Attacking an " + type + " is not permitted");
                        System.out.println("Player " + player.name + " tried to attack an " + type + ", so I have disconnected them for exploiting.");
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

    public void a(Packet9Respawn packet9respawn) {
        if (this.player.viewingCredits) {
            // CraftBukkit start
            org.bukkit.craftbukkit.PortalTravelAgent pta = new org.bukkit.craftbukkit.PortalTravelAgent();
            Location toLocation;

            if (this.player.getBukkitEntity().getBedSpawnLocation() == null) {
                CraftWorld cworld = (CraftWorld) this.server.getWorlds().get(0);
                ChunkCoordinates chunkcoordinates = cworld.getHandle().getSpawn();
                toLocation = new Location(cworld, chunkcoordinates.x + 0.5, chunkcoordinates.y, chunkcoordinates.z + 0.5);
                this.player.netServerHandler.sendPacket(new Packet70Bed(0, 0));
            } else {
                toLocation = this.player.getBukkitEntity().getBedSpawnLocation();
                toLocation = new Location(toLocation.getWorld(), toLocation.getX() + 0.5, toLocation.getY(), toLocation.getZ() + 0.5);
            }

            PlayerPortalEvent event = new PlayerPortalEvent(this.player.getBukkitEntity(), this.player.getBukkitEntity().getLocation(), toLocation, pta, PlayerPortalEvent.TeleportCause.END_PORTAL);
            event.useTravelAgent(false);

            Bukkit.getServer().getPluginManager().callEvent(event);
            this.player = this.minecraftServer.serverConfigurationManager.moveToWorld(this.player, 0, true, event.getTo());
            // CraftBukkit end
        } else {
            if (this.player.getHealth() > 0) {
                return;
            }

            this.player = this.minecraftServer.serverConfigurationManager.moveToWorld(this.player, 0, false);
        }
        // CraftBukkit start
        this.getPlayer().setHandle(this.player);
        this.player.viewingCredits = false; // allow the player to receive movement packets again.
        // CraftBukkit end
    }

    public void handleContainerClose(Packet101CloseWindow packet101closewindow) {
        if (this.player.dead) return; // CraftBukkit

        // CraftBukkit start - INVENTORY_CLOSE hook
        InventoryCloseEvent event = new InventoryCloseEvent(this.player.activeContainer.getBukkitView());
        server.getPluginManager().callEvent(event);
        this.player.activeContainer.transferTo(this.player.defaultContainer, getPlayer());
        // CraftBukkit end

        this.player.H();
    }

    public void a(Packet102WindowClick packet102windowclick) {
        if (this.player.dead) return; // CraftBukkit

        if (this.player.activeContainer.windowId == packet102windowclick.a && this.player.activeContainer.c(this.player)) {
            // CraftBukkit start - fire InventoryClickEvent
            InventoryView inventory = this.player.activeContainer.getBukkitView();
            SlotType type = CraftInventoryView.getSlotType(inventory, packet102windowclick.slot);

            InventoryClickEvent event = new InventoryClickEvent(inventory, type, packet102windowclick.slot, packet102windowclick.button != 0, packet102windowclick.shift);
            Inventory top = inventory.getTopInventory();
            if (packet102windowclick.slot == 0 && top instanceof CraftingInventory) {
                Recipe recipe = ((CraftingInventory) top).getRecipe();
                if (recipe != null) {
                    event = new CraftItemEvent(recipe, inventory, type, packet102windowclick.slot, packet102windowclick.button != 0, packet102windowclick.shift);
                }
            }
            server.getPluginManager().callEvent(event);

            ItemStack itemstack = null;
            boolean defaultBehaviour = false;

            switch(event.getResult()) {
            case DEFAULT:
                itemstack = this.player.activeContainer.clickItem(packet102windowclick.slot, packet102windowclick.button, packet102windowclick.shift, this.player);
                defaultBehaviour = true;
                break;
            case DENY: // Deny any change, including changes from the event
                break;
            case ALLOW: // Allow changes unconditionally
                org.bukkit.inventory.ItemStack cursor = event.getCursor();
                if (cursor == null) {
                    this.player.inventory.setCarried((ItemStack) null);
                } else {
                    this.player.inventory.setCarried(CraftItemStack.createNMSItemStack(cursor));
                }
                org.bukkit.inventory.ItemStack item = event.getCurrentItem();
                if (item != null) {
                    itemstack = CraftItemStack.createNMSItemStack(item);
                    if (packet102windowclick.slot == -999) {
                        this.player.drop(itemstack);
                    } else {
                        this.player.activeContainer.getSlot(packet102windowclick.slot).set(itemstack);
                    }
                } else if (packet102windowclick.slot != -999) {
                    this.player.activeContainer.getSlot(packet102windowclick.slot).set((ItemStack) null);
                }
                break;
            }
            // CraftBukkit end

            if (defaultBehaviour && ItemStack.matches(packet102windowclick.item, itemstack)) { // CraftBukkit - additional condition added
                this.player.netServerHandler.sendPacket((Packet) (new Packet106Transaction(packet102windowclick.a, packet102windowclick.d, true)));
                this.player.h = true;
                this.player.activeContainer.a();
                this.player.broadcastCarriedItem();
                this.player.h = false;
            } else {
                this.s.a(this.player.activeContainer.windowId, Short.valueOf(packet102windowclick.d));
                this.player.netServerHandler.sendPacket(new Packet106Transaction(packet102windowclick.a, packet102windowclick.d, false));
                this.player.activeContainer.a(this.player, false);
                ArrayList arraylist = new ArrayList();

                for (int i = 0; i < this.player.activeContainer.e.size(); ++i) {
                    arraylist.add(((Slot) this.player.activeContainer.e.get(i)).getItem());
                }

                this.player.a(this.player.activeContainer, arraylist);

                // CraftBukkit start - send a Set Slot to update the crafting result slot
                if(type == SlotType.RESULT && itemstack != null)
                    this.player.netServerHandler.sendPacket((Packet) (new Packet103SetSlot(this.player.activeContainer.windowId, 0, itemstack)));
                // CraftBukkit end
            }
        }
    }

    public void a(Packet108ButtonClick packet108buttonclick) {
        if (this.player.activeContainer.windowId == packet108buttonclick.a && this.player.activeContainer.c(this.player)) {
            this.player.activeContainer.a((EntityHuman) this.player, packet108buttonclick.b);
            this.player.activeContainer.a();
        }
    }

    public void a(Packet107SetCreativeSlot packet107setcreativeslot) {
        if (this.player.itemInWorldManager.isCreative()) {
            boolean flag = packet107setcreativeslot.slot < 0;
            ItemStack itemstack = packet107setcreativeslot.b;
            boolean flag1 = packet107setcreativeslot.slot >= 36 && packet107setcreativeslot.slot < 36 + PlayerInventory.getHotbarSize();
            // CraftBukkit
            boolean flag2 = itemstack == null || itemstack.id < Item.byId.length && itemstack.id >= 0 && Item.byId[itemstack.id] != null && !invalidItems.contains(itemstack.id);
            boolean flag3 = itemstack == null || itemstack.getData() >= 0 && itemstack.getData() >= 0 && itemstack.count <= 64 && itemstack.count > 0;

            // CraftBukkit start - Fire INVENTORY_CLICK event
            HumanEntity player = this.player.getBukkitEntity();
            InventoryView inventory = new CraftInventoryView(player, player.getInventory(), this.player.defaultContainer);
            SlotType slot = SlotType.QUICKBAR;
            if (packet107setcreativeslot.slot == -1) {
                slot = SlotType.OUTSIDE;
            }

            InventoryClickEvent event = new InventoryClickEvent(inventory, slot, slot == SlotType.OUTSIDE ? -999 : packet107setcreativeslot.slot, false, false);
            server.getPluginManager().callEvent(event);
            org.bukkit.inventory.ItemStack item = event.getCurrentItem();

            switch (event.getResult()) {
            case ALLOW:
                if (slot == SlotType.QUICKBAR) {
                    if (item == null) {
                        this.player.defaultContainer.setItem(packet107setcreativeslot.slot, (ItemStack) null);
                    } else {
                        this.player.defaultContainer.setItem(packet107setcreativeslot.slot, CraftItemStack.createNMSItemStack(item));
                    }
                } else if (item != null) {
                    this.player.drop(CraftItemStack.createNMSItemStack(item));
                }
                return;
            case DENY:
                // TODO: Will this actually work?
                if (packet107setcreativeslot.slot > -1) {
                    this.player.netServerHandler.sendPacket(new Packet103SetSlot(this.player.defaultContainer.windowId, packet107setcreativeslot.slot, CraftItemStack.createNMSItemStack(item)));
                }
                this.player.netServerHandler.sendPacket(new Packet103SetSlot(this.player.defaultContainer.windowId, -1, null));

                return;
            case DEFAULT:
                // We do the stuff below
                break;
            default:
                return;
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
                    entityitem.k();
                }
            }
        }
    }

    public void a(Packet106Transaction packet106transaction) {
        if (this.player.dead) return; // CraftBukkit
        Short oshort = (Short) this.s.get(this.player.activeContainer.windowId);

        if (oshort != null && packet106transaction.b == oshort.shortValue() && this.player.activeContainer.windowId == packet106transaction.a && !this.player.activeContainer.c(this.player)) {
            this.player.activeContainer.a(this.player, true);
        }
    }

    public void a(Packet130UpdateSign packet130updatesign) {
        if (this.player.dead) return; // CraftBukkit

        WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

        if (worldserver.isLoaded(packet130updatesign.x, packet130updatesign.y, packet130updatesign.z)) {
            TileEntity tileentity = worldserver.getTileEntity(packet130updatesign.x, packet130updatesign.y, packet130updatesign.z);

            if (tileentity instanceof TileEntitySign) {
                TileEntitySign tileentitysign = (TileEntitySign) tileentity;

                if (!tileentitysign.c()) {
                    this.minecraftServer.warning("Player " + this.player.name + " just tried to change non-editable sign");
                    // CraftBukkit
                    this.sendPacket(new Packet130UpdateSign(packet130updatesign.x, packet130updatesign.y, packet130updatesign.z, tileentitysign.lines));
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
                SignChangeEvent event = new SignChangeEvent((CraftBlock) player.getWorld().getBlockAt(j, k, i), this.server.getPlayer(this.player), packet130updatesign.lines);
                this.server.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    for (int l = 0; l < 4; ++l) {
                        tileentitysign1.lines[l] = event.getLine(l);
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
        if (packet0keepalive.a == this.i) {
            int i = (int) (System.nanoTime() / 1000000L - this.j);

            this.player.ping = (this.player.ping * 3 + i) / 4;
        }
    }

    public boolean c() {
        return true;
    }

    public void a(Packet202Abilities packet202abilities) {
        this.player.abilities.isFlying = packet202abilities.b && this.player.abilities.canFly;
    }

    // CraftBukkit start
    @Override
    public void a(Packet250CustomPayload packet) {
        if (packet.tag.equals("REGISTER")) {
            try {
                String channels = new String(packet.data, "UTF8");
                for (String channel : channels.split("\0")) {
                    getPlayer().addChannel(channel);
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(NetServerHandler.class.getName()).log(Level.SEVERE, "Could not parse REGISTER payload in plugin message packet", ex);
            }
        } else if (packet.tag.equals("UNREGISTER")) {
            try {
                String channels = new String(packet.data, "UTF8");
                for (String channel : channels.split("\0")) {
                    getPlayer().removeChannel(channel);
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(NetServerHandler.class.getName()).log(Level.SEVERE, "Could not parse UNREGISTER payload in plugin message packet", ex);
            }
        } else {
            server.getMessenger().dispatchIncomingMessage(player.getBukkitEntity(), packet.tag, packet.data);
        }
    }
    // CraftBukkit end
}
