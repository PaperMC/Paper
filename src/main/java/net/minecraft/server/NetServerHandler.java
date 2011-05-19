package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

// CraftBukkit start
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.TextWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
// CraftBukkit end

public class NetServerHandler extends NetHandler implements ICommandListener {

    public static Logger a = Logger.getLogger("Minecraft");
    public NetworkManager networkManager;
    public boolean disconnected = false;
    private MinecraftServer minecraftServer;
    public EntityPlayer player; // CraftBukkit - private -> public
    private int f;
    private int g;
    private int h;
    private boolean i;
    private double x;
    private double y;
    private double z;
    private boolean m = true;
    private Map n = new HashMap();

    public NetServerHandler(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
        this.minecraftServer = minecraftserver;
        this.networkManager = networkmanager;
        networkmanager.a((NetHandler) this);
        this.player = entityplayer;
        entityplayer.netServerHandler = this;

        // CraftBukkit start
        server = minecraftserver.server;
    }
    private final CraftServer server;
    private int lastTick = MinecraftServer.currentTick;
    private static final int PLACE_DISTANCE_SQUARED = 6 * 6;

    // Get position of last block hit for BlockDamageLevel.STOPPED
    private double lastPosX = Double.MAX_VALUE;
    private double lastPosY = Double.MAX_VALUE;
    private double lastPosZ = Double.MAX_VALUE;
    private float lastPitch = Float.MAX_VALUE;
    private float lastYaw = Float.MAX_VALUE;

    // For the packet15 hack :(
    Long lastPacket;

    // Store the last block right clicked and what type it was
    private int lastMaterial;

    public Player getPlayer() {
        return (this.player == null) ? null : (CraftPlayer) player.getBukkitEntity();
    }
    // CraftBukkit end

    public void a() {
        this.i = false;
        this.networkManager.a();
        if (this.f - this.g > 20) {
            this.sendPacket(new Packet0KeepAlive());
        }
    }

    public void disconnect(String s) {
        // CraftBukkit start
        String leaveMessage = "\u00A7e" + this.player.name + " left the game.";
        PlayerKickEvent event = new PlayerKickEvent(server.getPlayer(this.player), s, leaveMessage);
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            // Do not kick the player
            return;
        }
        // Send the possibly modified leave message
        this.sendPacket(new Packet255KickDisconnect(event.getReason()));
        this.networkManager.c();
        leaveMessage = event.getLeaveMessage();
        if (leaveMessage != null) {
            this.minecraftServer.serverConfigurationManager.sendAll(new Packet3Chat(leaveMessage));
        }
        // CraftBukkit end

        this.minecraftServer.serverConfigurationManager.disconnect(this.player);
        this.disconnected = true;
    }

    public void a(Packet27 packet27) {
        this.player.a(packet27.c(), packet27.e(), packet27.g(), packet27.h(), packet27.d(), packet27.f());
    }

    public void a(Packet10Flying packet10flying) {
        this.i = true;
        double d0;

        if (!this.m) {
            d0 = packet10flying.y - this.y;
            if (packet10flying.x == this.x && d0 * d0 < 0.01D && packet10flying.z == this.z) {
                this.m = true;
            }
        }

        // CraftBukkit start
        Player player = getPlayer();
        Location from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch);
        Location to = player.getLocation();

        // Prevent 40 event-calls for less than a single pixel of movement >.>
        double delta = Math.pow(this.lastPosX - this.x, 2) + Math.pow(this.lastPosY - this.y, 2) + Math.pow(this.lastPosZ - this.z, 2);
        float deltaAngle = Math.abs(this.lastYaw - this.player.yaw) +  Math.abs(this.lastPitch - this.player.pitch);

        if (delta > 1f/256 || deltaAngle > 10f) {
            // Skip the first time we do this
            if (lastPosX != Double.MAX_VALUE) {
                PlayerMoveEvent event = new PlayerMoveEvent(player, from, to);
                server.getPluginManager().callEvent(event);

                from = event.getFrom();
                to = event.isCancelled() ? from : event.getTo();

                this.player.locX = to.getX();
                this.player.locY = to.getY();
                this.player.locZ = to.getZ();
                this.player.yaw = to.getYaw();
                this.player.pitch = to.getPitch();
            }

            this.lastPosX = this.player.locX;
            this.lastPosY = this.player.locY;
            this.lastPosZ = this.player.locZ;
            this.lastYaw = this.player.yaw;
            this.lastPitch = this.player.pitch;
        }

        if (Math.abs(packet10flying.x) > 32000000 || Math.abs(packet10flying.z) > 32000000) {
            player.teleport(player.getWorld().getSpawnLocation());
            System.err.println(player.getName() + " was caught trying to crash the server with an invalid position.");
            player.kickPlayer("Nope!");
            return;
        }

        if (Double.isNaN(packet10flying.x) || packet10flying.x == Double.POSITIVE_INFINITY || packet10flying.x == Double.NEGATIVE_INFINITY) {
            player.teleport(player.getWorld().getSpawnLocation());
            System.err.println(player.getName() + " was caught trying to set an invalid position.");
            player.kickPlayer("Nope!");
            return;
        }

        if (Double.isNaN(packet10flying.y) || packet10flying.y == Double.POSITIVE_INFINITY || packet10flying.y == Double.NEGATIVE_INFINITY) {
            player.teleport(player.getWorld().getSpawnLocation());
            System.err.println(player.getName() + " was caught trying to set an invalid position.");
            player.kickPlayer("Nope!");
            return;
        }

        if (Double.isNaN(packet10flying.z) || packet10flying.z == Double.POSITIVE_INFINITY || packet10flying.z == Double.NEGATIVE_INFINITY) {
            player.teleport(player.getWorld().getSpawnLocation());
            System.err.println(player.getName() + " was caught trying to set an invalid position.");
            player.kickPlayer("Nope!");
            return;
        }

        if (Double.isNaN(packet10flying.stance) || packet10flying.stance == Double.POSITIVE_INFINITY || packet10flying.stance == Double.NEGATIVE_INFINITY) {
            player.teleport(player.getWorld().getSpawnLocation());
            System.err.println(player.getName() + " was caught trying to set an invalid position.");
            player.kickPlayer("Nope!");
            return;
        }

        if (this.m && !this.player.dead) {
        // CraftBukkit end
            double d1;
            double d2;
            double d3;
            double d4;

            if (this.player.vehicle != null) {
                float f = this.player.yaw;
                float f1 = this.player.pitch;

                this.player.vehicle.f();
                d1 = this.player.locX;
                d2 = this.player.locY;
                d3 = this.player.locZ;
                double d5 = 0.0D;

                d4 = 0.0D;
                if (packet10flying.hasLook) {
                    f = packet10flying.yaw;
                    f1 = packet10flying.pitch;
                }

                if (packet10flying.h && packet10flying.y == -999.0D && packet10flying.stance == -999.0D) {
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
                    // CraftBukkit
                    ((WorldServer) this.player.world).vehicleEnteredWorld(this.player.vehicle, true);
                }

                if (this.player.vehicle != null) {
                    this.player.vehicle.f();
                }

                this.minecraftServer.serverConfigurationManager.b(this.player);
                this.x = this.player.locX;
                this.y = this.player.locY;
                this.z = this.player.locZ;
                // CraftBukkit
                ((WorldServer) this.player.world).playerJoinedWorld(this.player);
                return;
            }

            d0 = this.player.locY;
            this.x = this.player.locX;
            this.y = this.player.locY;
            this.z = this.player.locZ;
            d1 = this.player.locX;
            d2 = this.player.locY;
            d3 = this.player.locZ;
            float f2 = this.player.yaw;
            float f3 = this.player.pitch;

            if (packet10flying.h && packet10flying.y == -999.0D && packet10flying.stance == -999.0D) {
                packet10flying.h = false;
            }

            if (packet10flying.h) {
                d1 = packet10flying.x;
                d2 = packet10flying.y;
                d3 = packet10flying.z;
                d4 = packet10flying.stance - packet10flying.y;
                if (!this.player.isSleeping() && (d4 > 1.65D || d4 < 0.1D)) {
                    this.disconnect("Illegal stance");
                    a.warning(this.player.name + " had an illegal stance: " + d4);
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
            this.player.bn = 0.0F;
            this.player.setLocation(this.x, this.y, this.z, f2, f3);
            d4 = d1 - this.player.locX;
            double d6 = d2 - this.player.locY;
            double d7 = d3 - this.player.locZ;
            double d8 = d4 * d4 + d6 * d6 + d7 * d7;

            // CraftBukkit start - make the movement speed check behave properly under tick degradation.
            int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
            if (d8 > 100.0D * (elapsedTicks <= 0 ? 1 : elapsedTicks)) {
                a.warning(this.player.name + " moved too quickly! Elapsed ticks: " +  (elapsedTicks == 0 ? 1 : elapsedTicks) + ", Distance change: " + d8);
                this.disconnect("You moved too quickly :( (Hacking?)");
                return;
            }
            this.lastTick = MinecraftServer.currentTick;
            // CraftBukkit end

            float f4 = 0.0625F;
            // CraftBukkit
            boolean flag = ((WorldServer) this.player.world).getEntities(this.player, this.player.boundingBox.clone().shrink((double) f4, (double) f4, (double) f4)).size() == 0;

            this.player.move(d4, d6, d7);
            d4 = d1 - this.player.locX;
            d6 = d2 - this.player.locY;
            if (d6 > -0.5D || d6 < 0.5D) {
                d6 = 0.0D;
            }

            d7 = d3 - this.player.locZ;
            d8 = d4 * d4 + d6 * d6 + d7 * d7;
            boolean flag1 = false;

            if (d8 > 0.0625D && !this.player.isSleeping()) {
                flag1 = true;
                a.warning(this.player.name + " moved wrongly!");
                System.out.println("Got position " + d1 + ", " + d2 + ", " + d3);
                System.out.println("Expected " + this.player.locX + ", " + this.player.locY + ", " + this.player.locZ);
            }

            this.player.setLocation(d1, d2, d3, f2, f3);
            // CraftBukkit
            boolean flag2 = ((WorldServer) this.player.world).getEntities(this.player, this.player.boundingBox.clone().shrink((double) f4, (double) f4, (double) f4)).size() == 0;

            if (flag && (flag1 || !flag2) && !this.player.isSleeping()) {
                this.a(this.x, this.y, this.z, f2, f3);
                return;
            }

            AxisAlignedBB axisalignedbb = this.player.boundingBox.clone().b((double) f4, (double) f4, (double) f4).a(0.0D, -0.55D, 0.0D);

            // CraftBukkit
            if (!this.minecraftServer.o && !((WorldServer) this.player.world).b(axisalignedbb)) {
                if (d6 >= -0.03125D) {
                    ++this.h;
                    if (this.h > 80) {
                        a.warning(this.player.name + " was kicked for floating too long!");
                        this.disconnect("Flying is not enabled on this server");
                        return;
                    }
                }
            } else {
                this.h = 0;
            }

            this.player.onGround = packet10flying.g;
            this.minecraftServer.serverConfigurationManager.b(this.player);
            this.player.b(this.player.locY - d0, packet10flying.g);
            // CraftBukkit end
        }
    }

    public void a(double d0, double d1, double d2, float f, float f1) {
        // CraftBukkit start - Delegate to teleport(Location)
        teleport(new Location(getPlayer().getWorld(), d0, d1, d2, f, f1));
    }

    public boolean teleport(Location dest) {
        // Note: the world in location is used only for the event
        // Inter-world teleportation is handled in CraftPlayer.teleport()

        Player player = getPlayer();
        Location from = player.getLocation();
        Location to = dest.clone();
        PlayerTeleportEvent event = new PlayerTeleportEvent(player, from, to);
        server.getPluginManager().callEvent(event);

        from = event.getFrom();
        to = event.isCancelled() ? from : event.getTo();

        double d0, d1, d2;
        float f, f1;

        d0 = to.getX();
        d1 = to.getY();
        d2 = to.getZ();
        f = to.getYaw();
        f1 = to.getPitch();

        // TODO: make sure this is the best way to address this.
        if (Float.isNaN(f)) {
            f = 0;
        }

        if (Float.isNaN(f1)) {
            f1 = 0;
        }
        // CraftBukkit end

        this.m = false;
        this.x = d0;
        this.y = d1;
        this.z = d2;
        this.player.setLocation(d0, d1, d2, f, f1);
        this.player.netServerHandler.sendPacket(new Packet13PlayerLookMove(d0, d1 + 1.6200000047683716D, d1, d2, f, f1, false));

        // CraftBukkit - Returns TRUE if the teleport was successful
        return !event.isCancelled();
    }

    public void a(Packet14BlockDig packet14blockdig) {
        // CraftBukkit start
        if (this.player.dead) {
            return;
        }
        // CraftBukkit end

        if (packet14blockdig.e == 4) {
            this.player.C();
        } else {
            // CraftBukkit
            boolean flag = ((WorldServer) this.player.world).weirdIsOpCache = this.minecraftServer.serverConfigurationManager.isOp(this.player.name);
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
                double d1 = this.player.locY - ((double) j + 0.5D);
                double d2 = this.player.locZ - ((double) k + 0.5D);
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > 36.0D) {
                    return;
                }
            }

            // CraftBukkit
            ChunkCoordinates chunkcoordinates = ((WorldServer) this.player.world).getSpawn();
            int l = (int) MathHelper.abs((float) (i - chunkcoordinates.x));
            int i1 = (int) MathHelper.abs((float) (k - chunkcoordinates.z));

            if (l > i1) {
                i1 = l;
            }

            if (packet14blockdig.e == 0) {
                // CraftBukkit
                if (i1 > this.minecraftServer.spawnProtection || flag) {
                    // CraftBukkit - add face argument
                    this.player.itemInWorldManager.dig(i, j, k, packet14blockdig.face);
                }
            } else if (packet14blockdig.e == 2) {
                this.player.itemInWorldManager.b(i, j, k);
            } else if (packet14blockdig.e == 3) {
                double d4 = this.player.locX - ((double) i + 0.5D);
                double d5 = this.player.locY - ((double) j + 0.5D);
                double d6 = this.player.locZ - ((double) k + 0.5D);
                double d7 = d4 * d4 + d5 * d5 + d6 * d6;

                if (d7 < 256.0D) {
                    // CraftBukkit
                    this.player.netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, this.player.world));
                }
            }

            // CraftBukkit
            ((WorldServer) this.player.world).weirdIsOpCache = false;
        }
    }

    public void a(Packet15Place packet15place) {
        // CraftBukkit start
        if (this.player.dead) {
            return;
        }

        // This is a horrible hack needed because the client sends 2 packets on 'right mouse click'
        // aimed at a block. We shouldn't need to get the second packet if the data is handled
        // but we cannot know what the client will do, so we might still get it
        //
        // If the time between packets is small enough, and the 'signature' similar, we discard the
        // second one. This sadly has to remain until Mojang makes their packets saner. :(
        //  -- Grum

        if (packet15place.face == 255) {
            if (packet15place.itemstack != null && packet15place.itemstack.id == lastMaterial && lastPacket != null && packet15place.timestamp - lastPacket < 100) {
                lastPacket = null;
                return;
            }
        } else {
            lastMaterial = packet15place.itemstack == null ? -1 : packet15place.itemstack.id;
            lastPacket = packet15place.timestamp;
        }

        // CraftBukkit - if rightclick decremented the item, always send the update packet.
        // this is not here for CraftBukkit's own functionality; rather it is to fix
        // a notch bug where the item doesn't update correctly.
        boolean always = false;

        // CraftBukkit end

        ItemStack itemstack = this.player.inventory.getItemInHand();
        // boolean flag = this.minecraftServer.worldServer.weirdIsOpCache = this.minecraftServer.serverConfigurationManager.isOp(this.player.name);

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
        } else {
            int i = packet15place.a;
            int j = packet15place.b;
            int k = packet15place.c;
            int l = packet15place.face;
            ChunkCoordinates chunkcoordinates = ((WorldServer) this.player.world).getSpawn(); // CraftBukkit
            int i1 = (int) MathHelper.abs((float) (i - chunkcoordinates.x));
            int j1 = (int) MathHelper.abs((float) (k - chunkcoordinates.z));

            if (i1 > j1) {
                j1 = i1;
            }

            // CraftBukkit start - spawn protection moved to ItemBlock!!!
            // Check if we can actually do something over this large a distance
            Location eyeLoc = getPlayer().getEyeLocation();
            if (Math.pow(eyeLoc.getX() - i, 2) + Math.pow(eyeLoc.getY() - j, 2) + Math.pow(eyeLoc.getZ() - k, 2) > PLACE_DISTANCE_SQUARED) {
                return;
            }

            this.player.itemInWorldManager.interact(this.player, this.player.world, itemstack, i, j, k, l);
            this.player.netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, this.player.world));
            // CraftBukkit end

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

            // CraftBukkit
            this.player.netServerHandler.sendPacket(new Packet53BlockChange(i, j, k, this.player.world));
        }

        if (itemstack != null && itemstack.count == 0) {
            this.player.inventory.items[this.player.inventory.itemInHandIndex] = null;
        }

        this.player.h = true;
        this.player.inventory.items[this.player.inventory.itemInHandIndex] = ItemStack.b(this.player.inventory.items[this.player.inventory.itemInHandIndex]);
        Slot slot = this.player.activeContainer.a(this.player.inventory, this.player.inventory.itemInHandIndex);

        this.player.activeContainer.a();
        this.player.h = false;
        // CraftBukkit
        if (!ItemStack.equals(this.player.inventory.getItemInHand(), packet15place.itemstack) || always) {
            this.sendPacket(new Packet103SetSlot(this.player.activeContainer.f, slot.a, this.player.inventory.getItemInHand()));
        }

        // CraftBukkit
        ((WorldServer) this.player.world).weirdIsOpCache = false;
    }

    public void a(String s, Object[] aobject) {
        if (this.disconnected) return; // CraftBukkit - rarely it would send a disconnect line twice

        a.info(this.player.name + " lost connection: " + s);
        // CraftBukkit start - we need to handle custom quit messages
        String quitMessage = this.minecraftServer.serverConfigurationManager.disconnect(this.player);
        if (quitMessage != null) {
            this.minecraftServer.serverConfigurationManager.sendAll(new Packet3Chat(quitMessage));
        }
        // CraftBukkit end
        this.disconnected = true;
    }

    public void a(Packet packet) {
        a.warning(this.getClass() + " wasn\'t prepared to deal with a " + packet.getClass());
        this.disconnect("Protocol error, unexpected packet");
    }

    public void sendPacket(Packet packet) {
        // CraftBukkit start
        if (packet instanceof Packet6SpawnPosition) {
            Packet6SpawnPosition packet6 = (Packet6SpawnPosition) packet;
            this.player.compassTarget = new Location(getPlayer().getWorld(), packet6.x, packet6.y, packet6.z);
        } else if (packet instanceof Packet3Chat) {
            String message = ((Packet3Chat) packet).a;
            for (final String line: TextWrapper.wrapText(message)) {
                this.networkManager.a(new Packet3Chat(line));
            }
            packet = null;
        }
        if (packet != null) this.networkManager.a(packet);
        // CraftBukkit end

        this.g = this.f;
    }

    public void a(Packet16BlockItemSwitch packet16blockitemswitch) {
        // CraftBukkit start
        if (this.player.dead) {
            return;
        }
        // CraftBukkit end

        if (packet16blockitemswitch.itemInHandIndex >= 0 && packet16blockitemswitch.itemInHandIndex <= InventoryPlayer.e()) {
            // CraftBukkit start
            PlayerItemHeldEvent event = new PlayerItemHeldEvent(getPlayer(), this.player.inventory.itemInHandIndex, packet16blockitemswitch.itemInHandIndex);
            server.getPluginManager().callEvent(event);
            // CraftBukkit end

            this.player.inventory.itemInHandIndex = packet16blockitemswitch.itemInHandIndex;
        } else {
            a.warning(this.player.name + " tried to set an invalid carried item");
        }
    }

    public void a(Packet3Chat packet3chat) {
        String s = packet3chat.a;

        if (s.length() > 100) {
            this.disconnect("Chat message too long");
        } else {
            s = s.trim();

            for (int i = 0; i < s.length(); ++i) {
                if (FontAllowedCharacters.a.indexOf(s.charAt(i)) < 0) {
                    this.disconnect("Illegal characters in chat");
                    return;
                }
            }

            // CraftBukkit start
            if (this.player.dead) {
                return;
            }
            chat(s);
        }
    }

    public boolean chat(String msg) {
        if (msg.startsWith("/")) {
            this.handleCommand(msg);
            return true;
        } else {
            Player player = getPlayer();
            PlayerChatEvent event = new PlayerChatEvent(player, msg);
            server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return true;
            }

            msg = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
            a.info(msg);
            for (Player recipient : event.getRecipients()) {
                recipient.sendMessage(msg);
            }
        }

        return false;
        // CraftBukkit end
    }

    private void handleCommand(String s) {
        // CraftBukkit start
        CraftPlayer player = (CraftPlayer) getPlayer();

        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, s);
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        boolean targetPluginFound = false;

        try {
            targetPluginFound = server.dispatchCommand(player, s.substring(1));
        } catch (CommandException ex) {
            player.sendMessage(ChatColor.RED + "An internal error occurred while attempting to perform this command");
            Logger.getLogger(NetServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        if (targetPluginFound) {
            return;
        }
        // CraftBukkit end

        if (s.toLowerCase().startsWith("/me ")) {
            s = "* " + this.player.name + " " + s.substring(s.indexOf(" ")).trim();
            a.info(s);
            this.minecraftServer.serverConfigurationManager.sendAll(new Packet3Chat(s));
        } else if (s.toLowerCase().startsWith("/kill")) {
            this.player.damageEntity((Entity) null, 1000);
        } else if (s.toLowerCase().startsWith("/tell ")) {
            String[] astring = s.split(" ");

            if (astring.length >= 3) {
                s = s.substring(s.indexOf(" ")).trim();
                s = s.substring(s.indexOf(" ")).trim();
                s = "\u00A77" + this.player.name + " whispers " + s;
                a.info(s + " to " + astring[1]);
                if (!this.minecraftServer.serverConfigurationManager.a(astring[1], (Packet) (new Packet3Chat(s)))) {
                    this.sendPacket(new Packet3Chat("\u00A7cThere\'s no player by that name online."));
                }
            }
        } else {
            String s1;

            if (this.minecraftServer.serverConfigurationManager.isOp(this.player.name)) {
                s1 = s.substring(1);
                a.info(this.player.name + " issued server command: " + s1);
                this.minecraftServer.issueCommand(s1, this);
            } else {
                s1 = s.substring(1);
                a.info(this.player.name + " tried command: " + s1);
            }
        }
    }

    public void a(Packet18ArmAnimation packet18armanimation) {
        // CraftBukkit start
        if (this.player.dead) {
            return;
        }
        // CraftBukkit end

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
            Player player = getPlayer();
            PlayerAnimationEvent event = new PlayerAnimationEvent(player);
            server.getPluginManager().callEvent(event);
            // CraftBukkit end

            this.player.k_();
        }
    }

    public void a(Packet19EntityAction packet19entityaction) {
        // CraftBukkit start
        if (this.player.dead) {
            return;
        }

        if (packet19entityaction.animation == 1 || packet19entityaction.animation == 2) {
            Player player = getPlayer();
            PlayerToggleSneakEvent event = new PlayerToggleSneakEvent(player);
            server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
        }
        // CraftBukkit end

        if (packet19entityaction.animation == 1) {
            this.player.setSneak(true);
        } else if (packet19entityaction.animation == 2) {
            this.player.setSneak(false);
        } else if (packet19entityaction.animation == 3) {
            this.player.a(false, true, true);
            this.m = false;
        }
    }

    public void a(Packet255KickDisconnect packet255kickdisconnect) {
        this.networkManager.a("disconnect.quitting", new Object[0]);
    }

    public int b() {
        return this.networkManager.d();
    }

    public void sendMessage(String s) {
        this.sendPacket(new Packet3Chat("\u00A77" + s));
    }

    public String getName() {
        return this.player.name;
    }

    public void a(Packet7UseEntity packet7useentity) {
        // CraftBukkit start
        if (this.player.dead) {
            return;
        }
        // CraftBukkit end

        // CraftBukkit
        Entity entity = ((WorldServer) this.player.world).getEntity(packet7useentity.target);

        if (entity != null && this.player.e(entity) && this.player.f(entity) < 4.0F) {
            if (packet7useentity.c == 0) {
                // CraftBukkit start
                PlayerInteractEntityEvent piee = new PlayerInteractEntityEvent((Player) getPlayer(), entity.getBukkitEntity());
                server.getPluginManager().callEvent(piee);
                if (piee.isCancelled()) {
                    return;
                }
                // CraftBukkit end
                this.player.c(entity);
            } else if (packet7useentity.c == 1) {
                this.player.d(entity);
            }
        }
    }

    public void a(Packet9Respawn packet9respawn) {
        if (this.player.health <= 0) {
            this.player = this.minecraftServer.serverConfigurationManager.d(this.player);

            // CraftBukkit start
            CraftPlayer player = (CraftPlayer) getPlayer();
            player.setHandle(this.player);
            // CraftBukkit end
        }
    }

    public void a(Packet101CloseWindow packet101closewindow) {
        // CraftBukkit start
        if (this.player.dead) {
            return;
        }
        // CraftBukkit end

        this.player.z();
    }

    public void a(Packet102WindowClick packet102windowclick) {
        // CraftBukkit start
        if (this.player.dead) {
            return;
        }
        // CraftBukkit end

        if (this.player.activeContainer.f == packet102windowclick.a && this.player.activeContainer.c(this.player)) {
            ItemStack itemstack = this.player.activeContainer.a(packet102windowclick.b, packet102windowclick.c, packet102windowclick.f, this.player);

            if (ItemStack.equals(packet102windowclick.e, itemstack)) {
                this.player.netServerHandler.sendPacket(new Packet106Transaction(packet102windowclick.a, packet102windowclick.d, true));
                this.player.h = true;
                this.player.activeContainer.a();
                this.player.y();
                this.player.h = false;
            } else {
                this.n.put(Integer.valueOf(this.player.activeContainer.f), Short.valueOf(packet102windowclick.d));
                this.player.netServerHandler.sendPacket(new Packet106Transaction(packet102windowclick.a, packet102windowclick.d, false));
                this.player.activeContainer.a(this.player, false);
                ArrayList arraylist = new ArrayList();

                for (int i = 0; i < this.player.activeContainer.e.size(); ++i) {
                    arraylist.add(((Slot) this.player.activeContainer.e.get(i)).getItem());
                }

                this.player.a(this.player.activeContainer, arraylist);
            }
        }
    }

    public void a(Packet106Transaction packet106transaction) {
        // CraftBukkit start
        if (this.player.dead) {
            return;
        }
        // CraftBukkit end

        Short oshort = (Short) this.n.get(Integer.valueOf(this.player.activeContainer.f));

        if (oshort != null && packet106transaction.b == oshort.shortValue() && this.player.activeContainer.f == packet106transaction.a && !this.player.activeContainer.c(this.player)) {
            this.player.activeContainer.a(this.player, true);
        }
    }

    public void a(Packet130UpdateSign packet130updatesign) {
        // CraftBukkit start
        if (this.player.dead) {
            return;
        }

        if (((WorldServer) this.player.world).isLoaded(packet130updatesign.x, packet130updatesign.y, packet130updatesign.z)) {
            TileEntity tileentity = ((WorldServer) this.player.world).getTileEntity(packet130updatesign.x, packet130updatesign.y, packet130updatesign.z);
            // CraftBukkit end

            if (tileentity instanceof TileEntitySign) {
                TileEntitySign tileentitysign = (TileEntitySign) tileentity;

                if (!tileentitysign.a()) {
                    this.minecraftServer.c("Player " + this.player.name + " just tried to change non-editable sign");
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
                        if (FontAllowedCharacters.a.indexOf(packet130updatesign.lines[j].charAt(i)) < 0) {
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
                Player player = server.getPlayer(this.player);
                SignChangeEvent event = new SignChangeEvent((CraftBlock) player.getWorld().getBlockAt(j, k, i), server.getPlayer(this.player), packet130updatesign.lines);
                server.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    for (int l = 0; l < 4; ++l) {
                        tileentitysign1.lines[l] = event.getLine(l);
                    }
                    tileentitysign1.setEditable(false);
                }
                // CraftBukkit end

                tileentitysign1.update();

                // CraftBukkit
                ((WorldServer) this.player.world).notify(j, k, i);
            }
        }
    }

    public boolean c() {
        return true;
    }
}
