package net.minecraft.server;

import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.chunkio.ChunkIOExecutor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;
import org.bukkit.Bukkit;
// CraftBukkit end

public abstract class PlayerList {

    private static final SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd \'at\' HH:mm:ss z");
    public static final Logger a = Logger.getLogger("Minecraft");
    private final MinecraftServer server;
    public final List players = new java.util.concurrent.CopyOnWriteArrayList(); // CraftBukkit - ArrayList -> CopyOnWriteArrayList: Iterator safety
    private final BanList banByName = new BanList(new File("banned-players.txt"));
    private final BanList banByIP = new BanList(new File("banned-ips.txt"));
    private Set operators = new HashSet();
    private Set whitelist = new java.util.LinkedHashSet(); // CraftBukkit - HashSet -> LinkedHashSet
    public PlayerFileData playerFileData; // CraftBukkit - private -> public
    public boolean hasWhitelist; // CraftBukkit - private -> public
    protected int maxPlayers;
    protected int d;
    private EnumGamemode m;
    private boolean n;
    private int o = 0;

    // CraftBukkit start
    private CraftServer cserver;

    public PlayerList(MinecraftServer minecraftserver) {
        minecraftserver.server = new CraftServer(minecraftserver, this);
        minecraftserver.console = org.bukkit.craftbukkit.command.ColouredConsoleSender.getInstance();
        this.cserver = minecraftserver.server;
        // CraftBukkit end

        this.server = minecraftserver;
        this.banByName.setEnabled(false);
        this.banByIP.setEnabled(false);
        this.maxPlayers = 8;
    }

    public void a(INetworkManager inetworkmanager, EntityPlayer entityplayer) {
        this.a(entityplayer);
        entityplayer.spawnIn(this.server.getWorldServer(entityplayer.dimension));
        entityplayer.playerInteractManager.a((WorldServer) entityplayer.world);
        String s = "local";

        if (inetworkmanager.getSocketAddress() != null) {
            s = inetworkmanager.getSocketAddress().toString();
        }

        // CraftBukkit - add world and location to 'logged in' message.
        a.info(entityplayer.name + "[" + s + "] logged in with entity id " + entityplayer.id + " at ([" + entityplayer.world.worldData.getName() + "] " + entityplayer.locX + ", " + entityplayer.locY + ", " + entityplayer.locZ + ")");
        WorldServer worldserver = this.server.getWorldServer(entityplayer.dimension);
        ChunkCoordinates chunkcoordinates = worldserver.getSpawn();

        this.a(entityplayer, (EntityPlayer) null, worldserver);
        PlayerConnection playerconnection = new PlayerConnection(this.server, inetworkmanager, entityplayer);

        // CraftBukkit start -- Don't send a higher than 60 MaxPlayer size, otherwise the PlayerInfo window won't render correctly.
        int maxPlayers = this.getMaxPlayers();
        if (maxPlayers > 60) {
            maxPlayers = 60;
        }
        playerconnection.sendPacket(new Packet1Login(entityplayer.id, worldserver.getWorldData().getType(), entityplayer.playerInteractManager.getGameMode(), worldserver.getWorldData().isHardcore(), worldserver.worldProvider.dimension, worldserver.difficulty, worldserver.getHeight(), maxPlayers));
        entityplayer.getBukkitEntity().sendSupportedChannels();
        // CraftBukkit end

        playerconnection.sendPacket(new Packet6SpawnPosition(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z));
        playerconnection.sendPacket(new Packet202Abilities(entityplayer.abilities));
        playerconnection.sendPacket(new Packet16BlockItemSwitch(entityplayer.inventory.itemInHandIndex));
        this.b(entityplayer, worldserver);
        // this.sendAll(new Packet3Chat("\u00A7e" + entityplayer.name + " joined the game.")); // CraftBukkit - handled in event
        this.c(entityplayer);
        playerconnection.a(entityplayer.locX, entityplayer.locY, entityplayer.locZ, entityplayer.yaw, entityplayer.pitch);
        this.server.ae().a(playerconnection);
        playerconnection.sendPacket(new Packet4UpdateTime(worldserver.getTime(), worldserver.getDayTime()));
        if (this.server.getTexturePack().length() > 0) {
            entityplayer.a(this.server.getTexturePack(), this.server.S());
        }

        Iterator iterator = entityplayer.getEffects().iterator();

        while (iterator.hasNext()) {
            MobEffect mobeffect = (MobEffect) iterator.next();

            playerconnection.sendPacket(new Packet41MobEffect(entityplayer.id, mobeffect));
        }

        entityplayer.syncInventory();
    }

    public void setPlayerFileData(WorldServer[] aworldserver) {
        if (this.playerFileData != null) return; // CraftBukkit
        this.playerFileData = aworldserver[0].getDataManager().getPlayerFileData();
    }

    public void a(EntityPlayer entityplayer, WorldServer worldserver) {
        WorldServer worldserver1 = entityplayer.p();

        if (worldserver != null) {
            worldserver.getPlayerChunkMap().removePlayer(entityplayer);
        }

        worldserver1.getPlayerChunkMap().addPlayer(entityplayer);
        worldserver1.chunkProviderServer.getChunkAt((int) entityplayer.locX >> 4, (int) entityplayer.locZ >> 4);
    }

    public int a() {
        return PlayerChunkMap.getFurthestViewableBlock(this.o());
    }

    public void a(EntityPlayer entityplayer) {
        NBTTagCompound nbttagcompound = this.server.worlds.get(0).getWorldData().i(); // CraftBukkit

        if (entityplayer.getName().equals(this.server.H()) && nbttagcompound != null) {
            entityplayer.e(nbttagcompound);
        } else {
            this.playerFileData.load(entityplayer);
        }
    }

    protected void b(EntityPlayer entityplayer) {
        this.playerFileData.save(entityplayer);
    }

    public void c(EntityPlayer entityplayer) {
        cserver.detectListNameConflict(entityplayer); // CraftBukkit
        // this.sendAll(new Packet201PlayerInfo(entityplayer.name, true, 1000)); // CraftBukkit - replaced with loop below
        this.players.add(entityplayer);
        WorldServer worldserver = this.server.getWorldServer(entityplayer.dimension);

        // CraftBukkit start
        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(this.cserver.getPlayer(entityplayer), "\u00A7e" + entityplayer.name + " joined the game.");
        this.cserver.getPluginManager().callEvent(playerJoinEvent);

        String joinMessage = playerJoinEvent.getJoinMessage();

        if ((joinMessage != null) && (joinMessage.length() > 0)) {
            this.server.getPlayerList().sendAll(new Packet3Chat(joinMessage));
        }
        this.cserver.onPlayerJoin(playerJoinEvent.getPlayer());

        ChunkIOExecutor.adjustPoolSize(this.getPlayerCount());
        // CraftBukkit end

        // CraftBukkit start - only add if the player wasn't moved in the event
        if (entityplayer.world == worldserver && !worldserver.players.contains(entityplayer)) {
            worldserver.addEntity(entityplayer);
            this.a(entityplayer, (WorldServer) null);
        }
        // CraftBukkit end

        // CraftBukkit start - sendAll above replaced with this loop
        Packet201PlayerInfo packet = new Packet201PlayerInfo(entityplayer.listName, true, 1000);
        for (int i = 0; i < this.players.size(); ++i) {
            EntityPlayer entityplayer1 = (EntityPlayer) this.players.get(i);

            if (entityplayer1.getBukkitEntity().canSee(entityplayer.getBukkitEntity())) {
                entityplayer1.playerConnection.sendPacket(packet);
            }
        }
        // CraftBukkit end

        for (int i = 0; i < this.players.size(); ++i) {
            EntityPlayer entityplayer1 = (EntityPlayer) this.players.get(i);

            // CraftBukkit start - .name -> .listName
            if (entityplayer.getBukkitEntity().canSee(entityplayer1.getBukkitEntity())) {
                entityplayer.playerConnection.sendPacket(new Packet201PlayerInfo(entityplayer1.listName, true, entityplayer1.ping));
            }
            // CraftBukkit end
        }
    }

    public void d(EntityPlayer entityplayer) {
        entityplayer.p().getPlayerChunkMap().movePlayer(entityplayer);
    }

    public String disconnect(EntityPlayer entityplayer) { // CraftBukkit - return string
        if (entityplayer.playerConnection.disconnected) return null; // CraftBukkit - exploitsies fix

        // CraftBukkit start - quitting must be before we do final save of data, in case plugins need to modify it
        PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(this.cserver.getPlayer(entityplayer), "\u00A7e" + entityplayer.name + " left the game.");
        this.cserver.getPluginManager().callEvent(playerQuitEvent);
        entityplayer.getBukkitEntity().disconnect(playerQuitEvent.getQuitMessage());
        // CraftBukkit end

        this.b(entityplayer);
        WorldServer worldserver = entityplayer.p();

        worldserver.kill(entityplayer);
        worldserver.getPlayerChunkMap().removePlayer(entityplayer);
        this.players.remove(entityplayer);
        ChunkIOExecutor.adjustPoolSize(this.getPlayerCount()); // CraftBukkit

        // CraftBukkit start - .name -> .listName, replace sendAll with loop
        Packet201PlayerInfo packet = new Packet201PlayerInfo(entityplayer.listName, false, 9999);
        for (int i = 0; i < this.players.size(); ++i) {
            EntityPlayer entityplayer1 = (EntityPlayer) this.players.get(i);

            if (entityplayer1.getBukkitEntity().canSee(entityplayer.getBukkitEntity())) {
                entityplayer1.playerConnection.sendPacket(packet);
            }
        }

        return playerQuitEvent.getQuitMessage();
        // CraftBukkit end
    }

    // CraftBukkit start - Whole method and signature
    public EntityPlayer attemptLogin(PendingConnection pendingconnection, String s, String hostname) {
        // Instead of kicking then returning, we need to store the kick reason
        // in the event, check with plugins to see if it's ok, and THEN kick
        // depending on the outcome.
        EntityPlayer entity = new EntityPlayer(this.server, this.server.getWorldServer(0), s, this.server.M() ? new DemoPlayerInteractManager(this.server.getWorldServer(0)) : new PlayerInteractManager(this.server.getWorldServer(0)));
        Player player = entity.getBukkitEntity();
        PlayerLoginEvent event = new PlayerLoginEvent(player, hostname, pendingconnection.getSocket().getInetAddress());

        SocketAddress socketaddress = pendingconnection.networkManager.getSocketAddress();

        if (this.banByName.isBanned(s)) {
            BanEntry banentry = (BanEntry) this.banByName.getEntries().get(s);
            String s1 = "You are banned from this server!\nReason: " + banentry.getReason();

            if (banentry.getExpires() != null) {
                s1 = s1 + "\nYour ban will be removed on " + e.format(banentry.getExpires());
            }

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, s1);
        } else if (!this.isWhitelisted(s)) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "You are not white-listed on this server!");
        } else {
            String s2 = socketaddress.toString();

            s2 = s2.substring(s2.indexOf("/") + 1);
            s2 = s2.substring(0, s2.indexOf(":"));
            if (this.banByIP.isBanned(s2)) {
                BanEntry banentry1 = (BanEntry) this.banByIP.getEntries().get(s2);
                String s3 = "Your IP address is banned from this server!\nReason: " + banentry1.getReason();

                if (banentry1.getExpires() != null) {
                    s3 = s3 + "\nYour ban will be removed on " + e.format(banentry1.getExpires());
                }

                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, s3);
            } else if (this.players.size() >= this.maxPlayers) {
                event.disallow(PlayerLoginEvent.Result.KICK_FULL, "The server is full!");
            } else {
                event.disallow(PlayerLoginEvent.Result.ALLOWED, s2);
            }
        }

        this.cserver.getPluginManager().callEvent(event);
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            pendingconnection.disconnect(event.getKickMessage());
            return null;
        }

        return entity;
        // CraftBukkit end
    }

    public EntityPlayer processLogin(EntityPlayer player) { // CraftBukkit - String -> EntityPlayer
        String s = player.name; // CraftBukkit
        ArrayList arraylist = new ArrayList();

        EntityPlayer entityplayer;

        for (int i = 0; i < this.players.size(); ++i) {
            entityplayer = (EntityPlayer) this.players.get(i);
            if (entityplayer.name.equalsIgnoreCase(s)) {
                arraylist.add(entityplayer);
            }
        }

        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            entityplayer = (EntityPlayer) iterator.next();
            entityplayer.playerConnection.disconnect("You logged in from another location");
        }

        /* CraftBukkit start
        Object object;

        if (this.server.M()) {
            object = new DemoPlayerInteractManager(this.server.getWorldServer(0));
        } else {
            object = new PlayerInteractManager(this.server.getWorldServer(0));
        }

        return new EntityPlayer(this.server, this.server.getWorldServer(0), s, (PlayerInteractManager) object);
        */
        return player;
        // CraftBukkit end
    }

    // CraftBukkit start
    public EntityPlayer moveToWorld(EntityPlayer entityplayer, int i, boolean flag) {
        return this.moveToWorld(entityplayer, i, flag, null);
    }

    public EntityPlayer moveToWorld(EntityPlayer entityplayer, int i, boolean flag, Location location) {
        // CraftBukkit end
        entityplayer.p().getTracker().untrackPlayer(entityplayer);
        // entityplayer.p().getTracker().untrackEntity(entityplayer); // CraftBukkit
        entityplayer.p().getPlayerChunkMap().removePlayer(entityplayer);
        this.players.remove(entityplayer);
        this.server.getWorldServer(entityplayer.dimension).removeEntity(entityplayer);
        ChunkCoordinates chunkcoordinates = entityplayer.getBed();
        boolean flag1 = entityplayer.isRespawnForced();

        // CraftBukkit start
        EntityPlayer entityplayer1 = entityplayer;
        org.bukkit.World fromWorld = entityplayer1.getBukkitEntity().getWorld();
        entityplayer1.viewingCredits = false;
        entityplayer1.copyTo(entityplayer, flag);

        ChunkCoordinates chunkcoordinates1;

        if (location == null) {
            boolean isBedSpawn = false;
            CraftWorld cworld = (CraftWorld) this.server.server.getWorld(entityplayer.spawnWorld);
            if (cworld != null && chunkcoordinates != null) {
                chunkcoordinates1 = EntityHuman.getBed(cworld.getHandle(), chunkcoordinates, flag1);
                if (chunkcoordinates1 != null) {
                    isBedSpawn = true;
                    location = new Location(cworld, chunkcoordinates1.x + 0.5, chunkcoordinates1.y, chunkcoordinates1.z + 0.5);
                } else {
                    entityplayer1.setRespawnPosition(null, true);
                    entityplayer1.playerConnection.sendPacket(new Packet70Bed(0, 0));
                }
            }

            if (location == null) {
                cworld = (CraftWorld) this.server.server.getWorlds().get(0);
                chunkcoordinates = cworld.getHandle().getSpawn();
                location = new Location(cworld, chunkcoordinates.x + 0.5, chunkcoordinates.y, chunkcoordinates.z + 0.5);
            }

            Player respawnPlayer = this.cserver.getPlayer(entityplayer1);
            PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(respawnPlayer, location, isBedSpawn);
            this.cserver.getPluginManager().callEvent(respawnEvent);

            location = respawnEvent.getRespawnLocation();
            entityplayer.reset();
        } else {
            location.setWorld(this.server.getWorldServer(i).getWorld());
        }
        WorldServer worldserver = ((CraftWorld) location.getWorld()).getHandle();
        entityplayer1.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        // CraftBukkit end

        worldserver.chunkProviderServer.getChunkAt((int) entityplayer1.locX >> 4, (int) entityplayer1.locZ >> 4);

        while (!worldserver.getCubes(entityplayer1, entityplayer1.boundingBox).isEmpty()) {
            entityplayer1.setPosition(entityplayer1.locX, entityplayer1.locY + 1.0D, entityplayer1.locZ);
        }

        // CraftBukkit start
        byte actualDimension = (byte) (worldserver.getWorld().getEnvironment().getId());
        // Force the client to refresh their chunk cache.
        entityplayer1.playerConnection.sendPacket(new Packet9Respawn((byte) (actualDimension >= 0 ? -1 : 0), (byte) worldserver.difficulty, worldserver.getWorldData().getType(), worldserver.getHeight(), entityplayer.playerInteractManager.getGameMode()));
        entityplayer1.playerConnection.sendPacket(new Packet9Respawn(actualDimension, (byte) worldserver.difficulty, worldserver.getWorldData().getType(), worldserver.getHeight(), entityplayer.playerInteractManager.getGameMode()));
        entityplayer1.spawnIn(worldserver);
        entityplayer1.dead = false;
        entityplayer1.playerConnection.teleport(new Location(worldserver.getWorld(), entityplayer1.locX, entityplayer1.locY, entityplayer1.locZ, entityplayer1.yaw, entityplayer1.pitch));
        entityplayer1.setSneaking(false);
        chunkcoordinates1 = worldserver.getSpawn();
        // CraftBukkit end
        entityplayer1.playerConnection.sendPacket(new Packet6SpawnPosition(chunkcoordinates1.x, chunkcoordinates1.y, chunkcoordinates1.z));
        entityplayer1.playerConnection.sendPacket(new Packet43SetExperience(entityplayer1.exp, entityplayer1.expTotal, entityplayer1.expLevel));
        this.b(entityplayer1, worldserver);
        worldserver.getPlayerChunkMap().addPlayer(entityplayer1);
        worldserver.addEntity(entityplayer1);
        this.players.add(entityplayer1);
        // CraftBukkit start - added from changeDimension
        this.updateClient(entityplayer1); // CraftBukkit
        entityplayer1.updateAbilities();
        Iterator iterator = entityplayer1.getEffects().iterator();

        while (iterator.hasNext()) {
            MobEffect mobeffect = (MobEffect) iterator.next();

            entityplayer1.playerConnection.sendPacket(new Packet41MobEffect(entityplayer1.id, mobeffect));
        }
        // entityplayer1.syncInventory();
        // CraftBukkit end

        // CraftBukkit start - don't fire on respawn
        if (fromWorld != location.getWorld()) {
            PlayerChangedWorldEvent event = new PlayerChangedWorldEvent((Player) entityplayer1.getBukkitEntity(), fromWorld);
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
        // CraftBukkit end

        return entityplayer1;
    }

    // CraftBukkit start - Replaced the standard handling of portals with a more customised method.
    public void changeDimension(EntityPlayer entityplayer, int i, TeleportCause cause) {
        WorldServer exitWorld = null;
        if (entityplayer.dimension < CraftWorld.CUSTOM_DIMENSION_OFFSET) { // plugins must specify exit from custom Bukkit worlds
            // only target existing worlds (compensate for allow-nether/allow-end as false)
            for (WorldServer world : this.server.worlds) {
                if (world.dimension == i) {
                    exitWorld = world;
                }
            }
        }

        Location enter = entityplayer.getBukkitEntity().getLocation();
        Location exit = null;
        if (exitWorld != null) {
            if ((cause == TeleportCause.END_PORTAL) && (i == 0)) {
                // THE_END -> NORMAL; use bed if available
                exit = ((CraftPlayer) entityplayer.getBukkitEntity()).getBedSpawnLocation();
            }
            if (exit == null) {
                exit = this.calculateTarget(enter, exitWorld);
            }
        }

        TravelAgent agent = exit != null ? (TravelAgent) ((CraftWorld) exit.getWorld()).getHandle().s() : null;
        PlayerPortalEvent event = new PlayerPortalEvent(entityplayer.getBukkitEntity(), enter, exit, agent, cause);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled() || event.getTo() == null) {
            return;
        }

        exit = event.useTravelAgent() ? event.getPortalTravelAgent().findOrCreate(exit) : event.getTo();
        exitWorld = ((CraftWorld) exit.getWorld()).getHandle();

        Vector velocity = entityplayer.getBukkitEntity().getVelocity();
        boolean before = exitWorld.chunkProviderServer.forceChunkLoad;
        exitWorld.chunkProviderServer.forceChunkLoad = true;
        exitWorld.s().adjustExit(entityplayer, exit, velocity);
        exitWorld.chunkProviderServer.forceChunkLoad = before;

        this.moveToWorld(entityplayer, exitWorld.dimension, true, exit);
        if (entityplayer.motX != velocity.getX() || entityplayer.motY != velocity.getY() || entityplayer.motZ != velocity.getZ()) {
            entityplayer.getBukkitEntity().setVelocity(velocity);
        }
        // CraftBukkit end
    }

    public void a(Entity entity, int i, WorldServer worldserver, WorldServer worldserver1) {
        // CraftBukkit start - split into modular functions
        Location exit = this.calculateTarget(entity.getBukkitEntity().getLocation(), worldserver1);
        this.repositionEntity(entity, exit, true);
    }

    // copy of original a(Entity, int, WorldServer, WorldServer) method with only location calculation logic
    public Location calculateTarget(Location enter, World target) {
        WorldServer worldserver = ((CraftWorld) enter.getWorld()).getHandle();
        WorldServer worldserver1 = ((CraftWorld) target.getWorld()).getHandle();
        int i = worldserver.dimension;

        double y = enter.getY();
        float yaw = enter.getYaw();
        float pitch = enter.getPitch();
        double d0 = enter.getX();
        double d1 = enter.getZ();
        double d2 = 8.0D;
        /*
        double d3 = entity.locX;
        double d4 = entity.locY;
        double d5 = entity.locZ;
        float f = entity.yaw;

        worldserver.methodProfiler.a("moving");
        */
        if (worldserver1.dimension == -1) {
            d0 /= d2;
            d1 /= d2;
            /*
            entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
            */
        } else if (worldserver1.dimension == 0) {
            d0 *= d2;
            d1 *= d2;
            /*
            entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
            */
        } else {
            ChunkCoordinates chunkcoordinates;

            if (i == 1) {
                chunkcoordinates = worldserver1.getSpawn();
            } else {
                chunkcoordinates = worldserver1.getDimensionSpawn();
            }

            d0 = (double) chunkcoordinates.x;
            y = (double) chunkcoordinates.y;
            d1 = (double) chunkcoordinates.z;
            yaw = 90.0F;
            pitch = 0.0F;
            /*
            entity.setPositionRotation(d0, entity.locY, d1, 90.0F, 0.0F);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
            */
        }

        // worldserver.methodProfiler.b();
        if (i != 1) {
            // worldserver.methodProfiler.a("placing");
            d0 = (double) MathHelper.a((int) d0, -29999872, 29999872);
            d1 = (double) MathHelper.a((int) d1, -29999872, 29999872);
            /*
            if (entity.isAlive()) {
                worldserver1.addEntity(entity);
                entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
                worldserver1.entityJoinedWorld(entity, false);
                worldserver1.s().a(entity, d3, d4, d5, f);
            }

            worldserver.methodProfiler.b();
            */
        }

        // entity.spawnIn(worldserver1);
        return new Location(target.getWorld(), d0, y, d1, yaw, pitch);
    }

    // copy of original a(Entity, int, WorldServer, WorldServer) method with only entity repositioning logic
    public void repositionEntity(Entity entity, Location exit, boolean portal) {
        int i = entity.dimension;
        WorldServer worldserver = (WorldServer) entity.world;
        WorldServer worldserver1 = ((CraftWorld) exit.getWorld()).getHandle();
        /*
        double d0 = entity.locX;
        double d1 = entity.locZ;
        double d2 = 8.0D;
        double d3 = entity.locX;
        double d4 = entity.locY;
        double d5 = entity.locZ;
        float f = entity.yaw;
        */

        worldserver.methodProfiler.a("moving");
        entity.setPositionRotation(exit.getX(), exit.getY(), exit.getZ(), exit.getYaw(), exit.getPitch());
        if (entity.isAlive()) {
            worldserver.entityJoinedWorld(entity, false);
        }
        /*
        if (entity.dimension == -1) {
            d0 /= d2;
            d1 /= d2;
            entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
        } else if (entity.dimension == 0) {
            d0 *= d2;
            d1 *= d2;
            entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
        } else {
            ChunkCoordinates chunkcoordinates;

            if (i == 1) {
                chunkcoordinates = worldserver1.getSpawn();
            } else {
                chunkcoordinates = worldserver1.getDimensionSpawn();
            }

            d0 = (double) chunkcoordinates.x;
            entity.locY = (double) chunkcoordinates.y;
            d1 = (double) chunkcoordinates.z;
            entity.setPositionRotation(d0, entity.locY, d1, 90.0F, 0.0F);
            if (entity.isAlive()) {
                worldserver.entityJoinedWorld(entity, false);
            }
        }
        */

        worldserver.methodProfiler.b();
        if (i != 1) {
            worldserver.methodProfiler.a("placing");
            /*
            d0 = (double) MathHelper.a((int) d0, -29999872, 29999872);
            d1 = (double) MathHelper.a((int) d1, -29999872, 29999872);
            */
            if (entity.isAlive()) {
                worldserver1.addEntity(entity);
                // entity.setPositionRotation(d0, entity.locY, d1, entity.yaw, entity.pitch)
                worldserver1.entityJoinedWorld(entity, false);
                // worldserver1.s().a(entity, d3, d4, d5, f);
                if (portal) {
                    Vector velocity = entity.getBukkitEntity().getVelocity();
                    worldserver1.s().adjustExit(entity, exit, velocity);
                    entity.setPositionRotation(exit.getX(), exit.getY(), exit.getZ(), exit.getYaw(), exit.getPitch());
                    if (entity.motX != velocity.getX() || entity.motY != velocity.getY() || entity.motZ != velocity.getZ()) {
                        entity.getBukkitEntity().setVelocity(velocity);
                    }
                }
            }

            worldserver.methodProfiler.b();
        }

        entity.spawnIn(worldserver1);
        // CraftBukkit end
    }

    public void tick() {
        if (++this.o > 600) {
            this.o = 0;
        }

        /* CraftBukkit start - remove updating of lag to players -- it spams way to much on big servers.
        if (this.o < this.players.size()) {
            EntityPlayer entityplayer = (EntityPlayer) this.players.get(this.o);

            this.sendAll(new Packet201PlayerInfo(entityplayer.name, true, entityplayer.ping));
        }
        // CraftBukkit end */
    }

    public void sendAll(Packet packet) {
        for (int i = 0; i < this.players.size(); ++i) {
            ((EntityPlayer) this.players.get(i)).playerConnection.sendPacket(packet);
        }
    }

    public void a(Packet packet, int i) {
        for (int j = 0; j < this.players.size(); ++j) {
            EntityPlayer entityplayer = (EntityPlayer) this.players.get(j);

            if (entityplayer.dimension == i) {
                entityplayer.playerConnection.sendPacket(packet);
            }
        }
    }

    public String c() {
        String s = "";

        for (int i = 0; i < this.players.size(); ++i) {
            if (i > 0) {
                s = s + ", ";
            }

            s = s + ((EntityPlayer) this.players.get(i)).name;
        }

        return s;
    }

    public String[] d() {
        String[] astring = new String[this.players.size()];

        for (int i = 0; i < this.players.size(); ++i) {
            astring[i] = ((EntityPlayer) this.players.get(i)).name;
        }

        return astring;
    }

    public BanList getNameBans() {
        return this.banByName;
    }

    public BanList getIPBans() {
        return this.banByIP;
    }

    public void addOp(String s) {
        this.operators.add(s.toLowerCase());

        // CraftBukkit start
        Player player = server.server.getPlayer(s);
        if (player != null) {
            player.recalculatePermissions();
        }
        // CraftBukkit end
    }

    public void removeOp(String s) {
        this.operators.remove(s.toLowerCase());

        // CraftBukkit start
        Player player = server.server.getPlayer(s);
        if (player != null) {
            player.recalculatePermissions();
        }
        // CraftBukkit end
    }

    public boolean isWhitelisted(String s) {
        s = s.trim().toLowerCase();
        return !this.hasWhitelist || this.operators.contains(s) || this.whitelist.contains(s);
    }

    public boolean isOp(String s) {
        // CraftBukkit
        return this.operators.contains(s.trim().toLowerCase()) || this.server.I() && this.server.worlds.get(0).getWorldData().allowCommands() && this.server.H().equalsIgnoreCase(s) || this.n;
    }

    public EntityPlayer f(String s) {
        Iterator iterator = this.players.iterator();

        EntityPlayer entityplayer;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            entityplayer = (EntityPlayer) iterator.next();
        } while (!entityplayer.name.equalsIgnoreCase(s));

        return entityplayer;
    }

    public List a(ChunkCoordinates chunkcoordinates, int i, int j, int k, int l, int i1, int j1) {
        if (this.players.isEmpty()) {
            return null;
        } else {
            Object object = new ArrayList();
            boolean flag = k < 0;
            int k1 = i * i;
            int l1 = j * j;

            k = MathHelper.a(k);

            for (int i2 = 0; i2 < this.players.size(); ++i2) {
                EntityPlayer entityplayer = (EntityPlayer) this.players.get(i2);

                if (chunkcoordinates != null && (i > 0 || j > 0)) {
                    float f = chunkcoordinates.e(entityplayer.b());

                    if (i > 0 && f < (float) k1 || j > 0 && f > (float) l1) {
                        continue;
                    }
                }

                if ((l == EnumGamemode.NONE.a() || l == entityplayer.playerInteractManager.getGameMode().a()) && (i1 <= 0 || entityplayer.expLevel >= i1) && entityplayer.expLevel <= j1) {
                    ((List) object).add(entityplayer);
                }
            }

            if (chunkcoordinates != null) {
                Collections.sort((List) object, new PlayerDistanceComparator(chunkcoordinates));
            }

            if (flag) {
                Collections.reverse((List) object);
            }

            if (k > 0) {
                object = ((List) object).subList(0, Math.min(k, ((List) object).size()));
            }

            return (List) object;
        }
    }

    public void sendPacketNearby(double d0, double d1, double d2, double d3, int i, Packet packet) {
        this.sendPacketNearby((EntityHuman) null, d0, d1, d2, d3, i, packet);
    }

    public void sendPacketNearby(EntityHuman entityhuman, double d0, double d1, double d2, double d3, int i, Packet packet) {
        for (int j = 0; j < this.players.size(); ++j) {
            EntityPlayer entityplayer = (EntityPlayer) this.players.get(j);

            // CraftBukkit start - Test if player receiving packet can see the source of the packet
            if (entityhuman != null && entityhuman instanceof EntityPlayer && !entityplayer.getBukkitEntity().canSee(((EntityPlayer) entityhuman).getBukkitEntity())) {
                continue;
            }
            // CraftBukkit end
            if (entityplayer != entityhuman && entityplayer.dimension == i) {
                double d4 = d0 - entityplayer.locX;
                double d5 = d1 - entityplayer.locY;
                double d6 = d2 - entityplayer.locZ;

                if (d4 * d4 + d5 * d5 + d6 * d6 < d3 * d3) {
                    entityplayer.playerConnection.sendPacket(packet);
                }
            }
        }
    }

    public void savePlayers() {
        for (int i = 0; i < this.players.size(); ++i) {
            this.b((EntityPlayer) this.players.get(i));
        }
    }

    public void addWhitelist(String s) {
        this.whitelist.add(s);
    }

    public void removeWhitelist(String s) {
        this.whitelist.remove(s);
    }

    public Set getWhitelisted() {
        return this.whitelist;
    }

    public Set getOPs() {
        return this.operators;
    }

    public void reloadWhitelist() {}

    public void b(EntityPlayer entityplayer, WorldServer worldserver) {
        entityplayer.playerConnection.sendPacket(new Packet4UpdateTime(worldserver.getTime(), worldserver.getDayTime()));
        if (worldserver.N()) {
            entityplayer.playerConnection.sendPacket(new Packet70Bed(1, 0));
        }
    }

    public void updateClient(EntityPlayer entityplayer) {
        entityplayer.updateInventory(entityplayer.defaultContainer);
        entityplayer.triggerHealthUpdate();
        entityplayer.playerConnection.sendPacket(new Packet16BlockItemSwitch(entityplayer.inventory.itemInHandIndex));
    }

    public int getPlayerCount() {
        return this.players.size();
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public String[] getSeenPlayers() {
        return this.server.worlds.get(0).getDataManager().getPlayerFileData().getSeenPlayers(); // CraftBukkit
    }

    public boolean getHasWhitelist() {
        return this.hasWhitelist;
    }

    public void setHasWhitelist(boolean flag) {
        this.hasWhitelist = flag;
    }

    public List j(String s) {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = this.players.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            if (entityplayer.q().equals(s)) {
                arraylist.add(entityplayer);
            }
        }

        return arraylist;
    }

    public int o() {
        return this.d;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    public NBTTagCompound q() {
        return null;
    }

    private void a(EntityPlayer entityplayer, EntityPlayer entityplayer1, World world) {
        if (entityplayer1 != null) {
            entityplayer.playerInteractManager.setGameMode(entityplayer1.playerInteractManager.getGameMode());
        } else if (this.m != null) {
            entityplayer.playerInteractManager.setGameMode(this.m);
        }

        entityplayer.playerInteractManager.b(world.getWorldData().getGameType());
    }

    public void r() {
        while (!this.players.isEmpty()) {
            ((EntityPlayer) this.players.get(0)).playerConnection.disconnect(this.server.server.getShutdownMessage()); // CraftBukkit - add custom shutdown message
        }
    }

    public void k(String s) {
        this.server.info(s);
        this.sendAll(new Packet3Chat(s));
    }
}
