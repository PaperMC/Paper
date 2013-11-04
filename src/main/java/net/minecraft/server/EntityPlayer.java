package net.minecraft.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.util.com.google.common.collect.Sets;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.io.netty.buffer.Unpooled;
import net.minecraft.util.org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
// CraftBukkit end

public class EntityPlayer extends EntityHuman implements ICrafting {

    private static final Logger bM = LogManager.getLogger();
    private String locale = "en_US";
    public PlayerConnection playerConnection;
    public final MinecraftServer server;
    public final PlayerInteractManager playerInteractManager;
    public double d;
    public double e;
    public final List chunkCoordIntPairQueue = new LinkedList();
    public final List removeQueue = new LinkedList();
    private final ServerStatisticManager bO;
    private float bP = Float.MIN_VALUE;
    private float bQ = -1.0E8F;
    private int bR = -99999999;
    private boolean bS = true;
    public int lastSentExp = -99999999; // CraftBukkit - private -> public
    public int invulnerableTicks = 60; // CraftBukkit - private -> public
    private int bV;
    private EnumChatVisibility bW;
    private boolean bX = true;
    private long bY = 0L;
    private int containerCounter;
    public boolean h;
    public int ping;
    public boolean viewingCredits;
    // CraftBukkit start
    public String displayName;
    public String listName;
    public org.bukkit.Location compassTarget;
    public int newExp = 0;
    public int newLevel = 0;
    public int newTotalExp = 0;
    public boolean keepLevel = false;
    public double maxHealthCache;
    // CraftBukkit end

    public EntityPlayer(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager) {
        super(worldserver, gameprofile);
        playerinteractmanager.player = this;
        this.playerInteractManager = playerinteractmanager;
        this.bV = minecraftserver.getPlayerList().o();
        ChunkCoordinates chunkcoordinates = worldserver.getSpawn();
        int i = chunkcoordinates.x;
        int j = chunkcoordinates.z;
        int k = chunkcoordinates.y;

        if (!worldserver.worldProvider.g && worldserver.getWorldData().getGameType() != EnumGamemode.ADVENTURE) {
            int l = Math.max(5, minecraftserver.getSpawnProtection() - 6);

            i += this.random.nextInt(l * 2) - l;
            j += this.random.nextInt(l * 2) - l;
            k = worldserver.i(i, j);
        }

        this.server = minecraftserver;
        this.bO = minecraftserver.getPlayerList().i(this.getName());
        this.X = 0.0F;
        this.height = 0.0F;
        this.setPositionRotation((double) i + 0.5D, (double) k, (double) j + 0.5D, 0.0F, 0.0F);

        while (!worldserver.getCubes(this, this.boundingBox).isEmpty()) {
            this.setPosition(this.locX, this.locY + 1.0D, this.locZ);
        }

        // CraftBukkit start
        this.displayName = this.getName();
        this.listName = this.getName();
        // this.canPickUpLoot = true; TODO
        this.maxHealthCache = this.getMaxHealth();
        // CraftBukkit end
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("playerGameType", 99)) {
            if (MinecraftServer.getServer().getForceGamemode()) {
                this.playerInteractManager.setGameMode(MinecraftServer.getServer().getGamemode());
            } else {
                this.playerInteractManager.setGameMode(EnumGamemode.a(nbttagcompound.getInt("playerGameType")));
            }
        }
        this.getBukkitEntity().readExtraData(nbttagcompound); // CraftBukkit
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("playerGameType", this.playerInteractManager.getGameMode().a());
        this.getBukkitEntity().setExtraData(nbttagcompound); // CraftBukkit
    }

    // CraftBukkit start - World fallback code, either respawn location or global spawn
    public void spawnIn(World world) {
        super.spawnIn(world);
        if (world == null) {
            this.dead = false;
            ChunkCoordinates position = null;
            if (this.spawnWorld != null && !this.spawnWorld.equals("")) {
                CraftWorld cworld = (CraftWorld) Bukkit.getServer().getWorld(this.spawnWorld);
                if (cworld != null && this.getBed() != null) {
                    world = cworld.getHandle();
                    position = EntityHuman.getBed(cworld.getHandle(), this.getBed(), false);
                }
            }
            if (world == null || position == null) {
                world = ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle();
                position = world.getSpawn();
            }
            this.world = world;
            this.setPosition(position.x + 0.5, position.y, position.z + 0.5);
        }
        this.dimension = ((WorldServer) this.world).dimension;
        this.playerInteractManager.a((WorldServer) world);
    }
    // CraftBukkit end

    public void levelDown(int i) {
        super.levelDown(i);
        this.lastSentExp = -1;
    }

    public void syncInventory() {
        this.activeContainer.addSlotListener(this);
    }

    protected void e_() {
        this.height = 0.0F;
    }

    public float getHeadHeight() {
        return 1.62F;
    }

    public void h() {
        this.playerInteractManager.a();
        --this.invulnerableTicks;
        if (this.noDamageTicks > 0) {
            --this.noDamageTicks;
        }

        this.activeContainer.b();
        if (!this.world.isStatic && !this.activeContainer.a((EntityHuman) this)) {
            this.closeInventory();
            this.activeContainer = this.defaultContainer;
        }

        while (!this.removeQueue.isEmpty()) {
            int i = Math.min(this.removeQueue.size(), 127);
            int[] aint = new int[i];
            Iterator iterator = this.removeQueue.iterator();
            int j = 0;

            while (iterator.hasNext() && j < i) {
                aint[j++] = ((Integer) iterator.next()).intValue();
                iterator.remove();
            }

            this.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(aint));
        }

        if (!this.chunkCoordIntPairQueue.isEmpty()) {
            ArrayList arraylist = new ArrayList();
            Iterator iterator1 = this.chunkCoordIntPairQueue.iterator();
            ArrayList arraylist1 = new ArrayList();

            Chunk chunk;

            while (iterator1.hasNext() && arraylist.size() < PacketPlayOutMapChunkBulk.c()) {
                ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) iterator1.next();

                if (chunkcoordintpair != null) {
                    if (this.world.isLoaded(chunkcoordintpair.x << 4, 0, chunkcoordintpair.z << 4)) {
                        chunk = this.world.getChunkAt(chunkcoordintpair.x, chunkcoordintpair.z);
                        if (chunk.k()) {
                            arraylist.add(chunk);
                            arraylist1.addAll(chunk.tileEntities.values()); // CraftBukkit - Get tile entities directly from the chunk instead of the world
                            iterator1.remove();
                        }
                    }
                } else {
                    iterator1.remove();
                }
            }

            if (!arraylist.isEmpty()) {
                this.playerConnection.sendPacket(new PacketPlayOutMapChunkBulk(arraylist));
                Iterator iterator2 = arraylist1.iterator();

                while (iterator2.hasNext()) {
                    TileEntity tileentity = (TileEntity) iterator2.next();

                    this.b(tileentity);
                }

                iterator2 = arraylist.iterator();

                while (iterator2.hasNext()) {
                    chunk = (Chunk) iterator2.next();
                    this.r().getTracker().a(this, chunk);
                }
            }
        }

        if (this.bY > 0L && this.server.aq() > 0 && MinecraftServer.ap() - this.bY > (long) (this.server.aq() * 1000 * 60)) {
            this.playerConnection.disconnect("You have been idle for too long!");
        }
    }

    public void i() {
        try {
            super.h();

            for (int i = 0; i < this.inventory.getSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);

                if (itemstack != null && itemstack.getItem().h()) {
                    Packet packet = ((ItemWorldMapBase) itemstack.getItem()).c(itemstack, this.world, this);

                    if (packet != null) {
                        this.playerConnection.sendPacket(packet);
                    }
                }
            }

            // CraftBukkit - Optionally scale health
            if (this.getHealth() != this.bQ || this.bR != this.foodData.a() || this.foodData.e() == 0.0F != this.bS) {
                this.playerConnection.sendPacket(new PacketPlayOutUpdateHealth(this.getBukkitEntity().getScaledHealth(), this.foodData.a(), this.foodData.e()));
                this.bQ = this.getHealth();
                this.bR = this.foodData.a();
                this.bS = this.foodData.e() == 0.0F;
            }

            if (this.getHealth() + this.bs() != this.bP) {
                this.bP = this.getHealth() + this.bs();
                // CraftBukkit - Update ALL the scores!
                this.world.getServer().getScoreboardManager().updateAllScoresForList(IScoreboardCriteria.f, this.getName(), com.google.common.collect.ImmutableList.of(this));
            }

            // CraftBukkit start - Force max health updates
            if (this.maxHealthCache != this.getMaxHealth()) {
                this.getBukkitEntity().updateScaledHealth();
            }
            // CraftBukkit end

            if (this.expTotal != this.lastSentExp) {
                this.lastSentExp = this.expTotal;
                this.playerConnection.sendPacket(new PacketPlayOutExperience(this.exp, this.expTotal, this.expLevel));
            }

            if (this.ticksLived % 20 * 5 == 0 && !this.x().a(AchievementList.L)) {
                this.j();
            }

            // CraftBukkit start
            if (this.oldLevel == -1) {
                this.oldLevel = this.expLevel;
            }

            if (this.oldLevel != this.expLevel) {
                CraftEventFactory.callPlayerLevelChangeEvent(this.world.getServer().getPlayer((EntityPlayer) this), this.oldLevel, this.expLevel);
                this.oldLevel = this.expLevel;
            }
            // CraftBukkit end
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Ticking player");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Player being ticked");

            this.a(crashreportsystemdetails);
            throw new ReportedException(crashreport);
        }
    }

    protected void j() {
        BiomeBase biomebase = this.world.getBiome(MathHelper.floor(this.locX), MathHelper.floor(this.locZ));

        if (biomebase != null) {
            String s = biomebase.af;
            AchievementSet achievementset = (AchievementSet) this.x().b((Statistic) AchievementList.L); // CraftBukkit - fix decompile error

            if (achievementset == null) {
                achievementset = (AchievementSet) this.x().a(AchievementList.L, new AchievementSet());
            }

            achievementset.add(s);
            if (this.x().b(AchievementList.L) && achievementset.size() == BiomeBase.n.size()) {
                HashSet hashset = Sets.newHashSet(BiomeBase.n);
                Iterator iterator = achievementset.iterator();

                while (iterator.hasNext()) {
                    String s1 = (String) iterator.next();
                    Iterator iterator1 = hashset.iterator();

                    while (iterator1.hasNext()) {
                        BiomeBase biomebase1 = (BiomeBase) iterator1.next();

                        if (biomebase1.af.equals(s1)) {
                            iterator1.remove();
                        }
                    }

                    if (hashset.isEmpty()) {
                        break;
                    }
                }

                if (hashset.isEmpty()) {
                    this.a((Statistic) AchievementList.L);
                }
            }
        }
    }

    public void die(DamageSource damagesource) {
        // CraftBukkit start
        if (this.dead) {
            return;
        }

        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        boolean keepInventory = this.world.getGameRules().getBoolean("keepInventory");

        if (!keepInventory) {
            for (int i = 0; i < this.inventory.items.length; ++i) {
                if (this.inventory.items[i] != null) {
                    loot.add(CraftItemStack.asCraftMirror(this.inventory.items[i]));
                }
            }

            for (int i = 0; i < this.inventory.armor.length; ++i) {
                if (this.inventory.armor[i] != null) {
                    loot.add(CraftItemStack.asCraftMirror(this.inventory.armor[i]));
                }
            }
        }

        IChatBaseComponent chatmessage = this.aW().b();

        String deathmessage = chatmessage.c();
        org.bukkit.event.entity.PlayerDeathEvent event = CraftEventFactory.callPlayerDeathEvent(this, loot, deathmessage);

        String deathMessage = event.getDeathMessage();

        if (deathMessage != null && deathMessage.length() > 0) {
            if (deathMessage.equals(deathmessage)) {
                this.server.getPlayerList().sendMessage(chatmessage);
            } else {
                this.server.getPlayerList().sendMessage(org.bukkit.craftbukkit.util.CraftChatMessage.fromString(deathMessage));
            }
        }

        // we clean the player's inventory after the EntityDeathEvent is called so plugins can get the exact state of the inventory.
        if (!keepInventory) {
            for (int i = 0; i < this.inventory.items.length; ++i) {
                this.inventory.items[i] = null;
            }

            for (int i = 0; i < this.inventory.armor.length; ++i) {
                this.inventory.armor[i] = null;
            }
        }

        this.closeInventory();
        // CraftBukkit end

        // CraftBukkit - Get our scores instead
        Collection<ScoreboardScore> collection = this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreboardCriteria.c, this.getName(), new java.util.ArrayList<ScoreboardScore>());
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreboardScore scoreboardscore = (ScoreboardScore) iterator.next(); // CraftBukkit - Use our scores instead

            scoreboardscore.incrementScore();
        }

        EntityLiving entityliving = this.aX();

        if (entityliving != null) {
            int i = EntityTypes.a(entityliving);
            MonsterEggInfo monsteregginfo = (MonsterEggInfo) EntityTypes.a.get(Integer.valueOf(i));

            if (monsteregginfo != null) {
                this.a(monsteregginfo.e, 1);
            }

            entityliving.b(this, this.bb);
        }

        this.a(StatisticList.v, 1);
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else {
            // CraftBukkit - this.server.getPvP() -> this.world.pvpMode
            boolean flag = this.server.V() && this.world.pvpMode && "fall".equals(damagesource.translationIndex);

            if (!flag && this.invulnerableTicks > 0 && damagesource != DamageSource.OUT_OF_WORLD) {
                return false;
            } else {
                if (damagesource instanceof EntityDamageSource) {
                    Entity entity = damagesource.getEntity();

                    if (entity instanceof EntityHuman && !this.a((EntityHuman) entity)) {
                        return false;
                    }

                    if (entity instanceof EntityArrow) {
                        EntityArrow entityarrow = (EntityArrow) entity;

                        if (entityarrow.shooter instanceof EntityHuman && !this.a((EntityHuman) entityarrow.shooter)) {
                            return false;
                        }
                    }
                }

                return super.damageEntity(damagesource, f);
            }
        }
    }

    public boolean a(EntityHuman entityhuman) {
        // CraftBukkit - this.server.getPvP() -> this.world.pvpMode
        return !this.world.pvpMode ? false : super.a(entityhuman);
    }

    public void b(int i) {
        if (this.dimension == 1 && i == 1) {
            this.a((Statistic) AchievementList.D);
            this.world.kill(this);
            this.viewingCredits = true;
            this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(4, 0.0F));
        } else {
            if (this.dimension == 0 && i == 1) {
                this.a((Statistic) AchievementList.C);
                // CraftBukkit start - Rely on custom portal management
                /*
                ChunkCoordinates chunkcoordinates = this.server.getWorldServer(i).getDimensionSpawn();

                if (chunkcoordinates != null) {
                    this.playerConnection.a((double) chunkcoordinates.x, (double) chunkcoordinates.y, (double) chunkcoordinates.z, 0.0F, 0.0F);
                }

                i = 1;
                */
                // CraftBukkit end
            } else {
                this.a((Statistic) AchievementList.y);
            }

            // CraftBukkit start
            TeleportCause cause = (this.dimension == 1 || i == 1) ? TeleportCause.END_PORTAL : TeleportCause.NETHER_PORTAL;
            this.server.getPlayerList().changeDimension(this, i, cause);
            // CraftBukkit end
            this.lastSentExp = -1;
            this.bQ = -1.0F;
            this.bR = -1;
        }
    }

    private void b(TileEntity tileentity) {
        if (tileentity != null) {
            Packet packet = tileentity.getUpdatePacket();

            if (packet != null) {
                this.playerConnection.sendPacket(packet);
            }
        }
    }

    public void receive(Entity entity, int i) {
        super.receive(entity, i);
        this.activeContainer.b();
    }

    public EnumBedResult a(int i, int j, int k) {
        EnumBedResult enumbedresult = super.a(i, j, k);

        if (enumbedresult == EnumBedResult.OK) {
            PacketPlayOutBed packetplayoutbed = new PacketPlayOutBed(this, i, j, k);

            this.r().getTracker().a((Entity) this, (Packet) packetplayoutbed);
            this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            this.playerConnection.sendPacket(packetplayoutbed);
        }

        return enumbedresult;
    }

    public void a(boolean flag, boolean flag1, boolean flag2) {
        if (this.fauxSleeping && !this.sleeping) return; // CraftBukkit - Can't leave bed if not in one!

        if (this.isSleeping()) {
            this.r().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(this, 2));
        }

        super.a(flag, flag1, flag2);
        if (this.playerConnection != null) {
            this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        }
    }

    public void mount(Entity entity) {
        // CraftBukkit start
        this.setPassengerOf(entity);
    }

    public void setPassengerOf(Entity entity) {
        // mount(null) doesn't really fly for overloaded methods,
        // so this method is needed
        Entity currentVehicle = this.vehicle;

        super.setPassengerOf(entity);

        // Check if the vehicle actually changed.
        if (currentVehicle != this.vehicle) {
            this.playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, this, this.vehicle));
            this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        }
        // CraftBukkit end
    }

    protected void a(double d0, boolean flag) {}

    public void b(double d0, boolean flag) {
        super.a(d0, flag);
    }

    public void a(TileEntity tileentity) {
        if (tileentity instanceof TileEntitySign) {
            ((TileEntitySign) tileentity).a((EntityHuman) this);
            this.playerConnection.sendPacket(new PacketPlayOutOpenSignEditor(tileentity.x, tileentity.y, tileentity.z));
        }
    }

    public int nextContainerCounter() { // CraftBukkit - private void -> public int
        this.containerCounter = this.containerCounter % 100 + 1;
        return this.containerCounter; // CraftBukkit
    }

    public void startCrafting(int i, int j, int k) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerWorkbench(this.inventory, this.world, i, j, k));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, 1, "Crafting", 9, true));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void startEnchanting(int i, int j, int k, String s) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerEnchantTable(this.inventory, this.world, i, j, k));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, 4, s == null ? "" : s, 9, s != null));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openAnvil(int i, int j, int k) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerAnvil(this.inventory, this.world, i, j, k, this));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, 8, "Repairing", 9, true));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openContainer(IInventory iinventory) {
        if (this.activeContainer != this.defaultContainer) {
            this.closeInventory();
        }

        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerChest(this.inventory, iinventory));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, 0, iinventory.getInventoryName(), iinventory.getSize(), iinventory.k_()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openHopper(TileEntityHopper tileentityhopper) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerHopper(this.inventory, tileentityhopper));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, 9, tileentityhopper.getInventoryName(), tileentityhopper.getSize(), tileentityhopper.k_()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openMinecartHopper(EntityMinecartHopper entityminecarthopper) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerHopper(this.inventory, entityminecarthopper));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, 9, entityminecarthopper.getInventoryName(), entityminecarthopper.getSize(), entityminecarthopper.k_()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openFurnace(TileEntityFurnace tileentityfurnace) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerFurnace(this.inventory, tileentityfurnace));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, 2, tileentityfurnace.getInventoryName(), tileentityfurnace.getSize(), tileentityfurnace.k_()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openDispenser(TileEntityDispenser tileentitydispenser) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerDispenser(this.inventory, tileentitydispenser));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, tileentitydispenser instanceof TileEntityDropper ? 10 : 3, tileentitydispenser.getInventoryName(), tileentitydispenser.getSize(), tileentitydispenser.k_()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openBrewingStand(TileEntityBrewingStand tileentitybrewingstand) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerBrewingStand(this.inventory, tileentitybrewingstand));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, 5, tileentitybrewingstand.getInventoryName(), tileentitybrewingstand.getSize(), tileentitybrewingstand.k_()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openBeacon(TileEntityBeacon tileentitybeacon) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerBeacon(this.inventory, tileentitybeacon));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, 7, tileentitybeacon.getInventoryName(), tileentitybeacon.getSize(), tileentitybeacon.k_()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void openTrade(IMerchant imerchant, String s) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerMerchant(this.inventory, imerchant, this.world));
        if(container == null) return;
        // CraftBukkit end

        this.nextContainerCounter();
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
        InventoryMerchant inventorymerchant = ((ContainerMerchant) this.activeContainer).getMerchantInventory();

        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, 6, s == null ? "" : s, inventorymerchant.getSize(), s != null));
        MerchantRecipeList merchantrecipelist = imerchant.getOffers(this);

        if (merchantrecipelist != null) {
            try {
                PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.buffer());

                packetdataserializer.writeInt(this.containerCounter);
                merchantrecipelist.a(packetdataserializer);
                this.playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|TrList", packetdataserializer));
            } catch (Exception ioexception) { // CraftBukkit - IOException -> Exception
                bM.error("Couldn\'t send trade list", ioexception);
            }
        }
    }

    public void openHorseInventory(EntityHorse entityhorse, IInventory iinventory) {
        // CraftBukkit start - Inventory open hook
        Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerHorse(this.inventory, iinventory, entityhorse));
        if(container == null) return;
        // CraftBukkit end

        if (this.activeContainer != this.defaultContainer) {
            this.closeInventory();
        }

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, 11, iinventory.getInventoryName(), iinventory.getSize(), iinventory.k_(), entityhorse.getId()));
        this.activeContainer = container; // CraftBukkit - Use container we passed to event
        this.activeContainer.windowId = this.containerCounter;
        this.activeContainer.addSlotListener(this);
    }

    public void a(Container container, int i, ItemStack itemstack) {
        if (!(container.getSlot(i) instanceof SlotResult)) {
            if (!this.h) {
                this.playerConnection.sendPacket(new PacketPlayOutSetSlot(container.windowId, i, itemstack));
            }
        }
    }

    public void updateInventory(Container container) {
        this.a(container, container.a());
    }

    public void a(Container container, List list) {
        this.playerConnection.sendPacket(new PacketPlayOutWindowItems(container.windowId, list));
        this.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.inventory.getCarried()));
        // CraftBukkit start - Send a Set Slot to update the crafting result slot
        if (java.util.EnumSet.of(InventoryType.CRAFTING,InventoryType.WORKBENCH).contains(container.getBukkitView().getType())) {
            this.playerConnection.sendPacket(new PacketPlayOutSetSlot(container.windowId, 0, container.getSlot(0).getItem()));
        }
        // CraftBukkit end
    }

    public void setContainerData(Container container, int i, int j) {
        this.playerConnection.sendPacket(new PacketPlayOutCraftProgressBar(container.windowId, i, j));
    }

    public void closeInventory() {
        CraftEventFactory.handleInventoryCloseEvent(this); // CraftBukkit
        this.playerConnection.sendPacket(new PacketPlayOutCloseWindow(this.activeContainer.windowId));
        this.m();
    }

    public void broadcastCarriedItem() {
        if (!this.h) {
            this.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.inventory.getCarried()));
        }
    }

    public void m() {
        this.activeContainer.b((EntityHuman) this);
        this.activeContainer = this.defaultContainer;
    }

    public void a(float f, float f1, boolean flag, boolean flag1) {
        if (this.vehicle != null) {
            if (f >= -1.0F && f <= 1.0F) {
                this.be = f;
            }

            if (f1 >= -1.0F && f1 <= 1.0F) {
                this.bf = f1;
            }

            this.bd = flag;
            this.setSneaking(flag1);
        }
    }

    public void a(Statistic statistic, int i) {
        if (statistic != null) {
            this.bO.b(this, statistic, i);
            Iterator iterator = this.getScoreboard().getObjectivesForCriteria(statistic.k()).iterator();

            while (iterator.hasNext()) {
                ScoreboardObjective scoreboardobjective = (ScoreboardObjective) iterator.next();

                this.getScoreboard().getPlayerScoreForObjective(this.getName(), scoreboardobjective).incrementScore();
            }

            if (this.bO.e()) {
                this.bO.a(this);
            }
        }
    }

    public void n() {
        if (this.passenger != null) {
            this.passenger.mount(this);
        }

        if (this.sleeping) {
            this.a(true, false, false);
        }
    }

    public void triggerHealthUpdate() {
        this.bQ = -1.0E8F;
        this.lastSentExp = -1; // CraftBukkit - Added to reset
    }

    public void b(IChatBaseComponent ichatbasecomponent) {
        this.playerConnection.sendPacket(new PacketPlayOutChat(ichatbasecomponent));
    }

    protected void p() {
        this.playerConnection.sendPacket(new PacketPlayOutEntityStatus(this, (byte) 9));
        super.p();
    }

    public void a(ItemStack itemstack, int i) {
        super.a(itemstack, i);
        if (itemstack != null && itemstack.getItem() != null && itemstack.getItem().d(itemstack) == EnumAnimation.EAT) {
            this.r().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(this, 3));
        }
    }

    public void copyTo(EntityHuman entityhuman, boolean flag) {
        super.copyTo(entityhuman, flag);
        this.lastSentExp = -1;
        this.bQ = -1.0F;
        this.bR = -1;
        this.removeQueue.addAll(((EntityPlayer) entityhuman).removeQueue);
    }

    protected void a(MobEffect mobeffect) {
        super.a(mobeffect);
        this.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.getId(), mobeffect));
    }

    protected void a(MobEffect mobeffect, boolean flag) {
        super.a(mobeffect, flag);
        this.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.getId(), mobeffect));
    }

    protected void b(MobEffect mobeffect) {
        super.b(mobeffect);
        this.playerConnection.sendPacket(new PacketPlayOutRemoveEntityEffect(this.getId(), mobeffect));
    }

    public void enderTeleportTo(double d0, double d1, double d2) {
        this.playerConnection.a(d0, d1, d2, this.yaw, this.pitch);
    }

    public void b(Entity entity) {
        this.r().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(entity, 4));
    }

    public void c(Entity entity) {
        this.r().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(entity, 5));
    }

    public void updateAbilities() {
        if (this.playerConnection != null) {
            this.playerConnection.sendPacket(new PacketPlayOutAbilities(this.abilities));
        }
    }

    public WorldServer r() {
        return (WorldServer) this.world;
    }

    public void a(EnumGamemode enumgamemode) {
        this.playerInteractManager.setGameMode(enumgamemode);
        this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(3, (float) enumgamemode.a()));
    }

    // CraftBukkit start - Support multi-line messages
    public void sendMessage(IChatBaseComponent[] ichatbasecomponent) {
        for (IChatBaseComponent component : ichatbasecomponent) {
            this.sendMessage(component);
        }
    }
    // CraftBukkit end

    public void sendMessage(IChatBaseComponent ichatbasecomponent) {
        this.playerConnection.sendPacket(new PacketPlayOutChat(ichatbasecomponent));
    }

    public boolean a(int i, String s) {
        return "seed".equals(s) && !this.server.V() ? true : (!"tell".equals(s) && !"help".equals(s) && !"me".equals(s) ? (this.server.getPlayerList().isOp(this.getName()) ? this.server.l() >= i : false) : true);
    }

    public String s() {
        String s = this.playerConnection.networkManager.getSocketAddress().toString();

        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }

    public void a(PacketPlayInSettings packetplayinsettings) {
        this.locale = packetplayinsettings.c();
        int i = 256 >> packetplayinsettings.d();

        if (i > 3 && i < 15) {
            this.bV = i;
        }

        this.bW = packetplayinsettings.e();
        this.bX = packetplayinsettings.f();
        if (this.server.L() && this.server.K().equals(this.getName())) {
            this.server.a(packetplayinsettings.g());
        }

        this.b(1, !packetplayinsettings.h());
    }

    public EnumChatVisibility getChatFlags() {
        return this.bW;
    }

    public void a(String s) {
        this.playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|RPack", s.getBytes(Charsets.UTF_8)));
    }

    public ChunkCoordinates getChunkCoordinates() {
        return new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY + 0.5D), MathHelper.floor(this.locZ));
    }

    public void w() {
        this.bY = MinecraftServer.ap();
    }

    public ServerStatisticManager x() {
        return this.bO;
    }

    // CraftBukkit start
    public long timeOffset = 0;
    public boolean relativeTime = true;

    public long getPlayerTime() {
        if (this.relativeTime) {
            // Adds timeOffset to the current server time.
            return this.world.getDayTime() + this.timeOffset;
        } else {
            // Adds timeOffset to the beginning of this day.
            return this.world.getDayTime() - (this.world.getDayTime() % 24000) + this.timeOffset;
        }
    }

    public WeatherType weather = null;

    public WeatherType getPlayerWeather() {
        return this.weather;
    }

    public void setPlayerWeather(WeatherType type, boolean plugin) {
        if (!plugin && this.weather != null) {
            return;
        }

        if (plugin) {
            this.weather = type;
        }

        if (type == WeatherType.DOWNFALL) {
            this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(2, 0));
            // this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(7, this.world.j(1.0F)));
            // this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(8, this.world.h(1.0F)));
        } else {
            this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(1, 0));
        }
    }

    public void resetPlayerWeather() {
        this.weather = null;
        this.setPlayerWeather(this.world.getWorldData().hasStorm() ? WeatherType.DOWNFALL : WeatherType.CLEAR, false);
    }

    @Override
    public String toString() {
        return super.toString() + "(" + this.getName() + " at " + this.locX + "," + this.locY + "," + this.locZ + ")";
    }

    public void reset() {
        float exp = 0;
        boolean keepInventory = this.world.getGameRules().getBoolean("keepInventory");

        if (this.keepLevel || keepInventory) {
            exp = this.exp;
            this.newTotalExp = this.expTotal;
            this.newLevel = this.expLevel;
        }

        this.setHealth(this.getMaxHealth());
        this.fireTicks = 0;
        this.fallDistance = 0;
        this.foodData = new FoodMetaData(this);
        this.expLevel = this.newLevel;
        this.expTotal = this.newTotalExp;
        this.exp = 0;
        this.deathTicks = 0;
        this.removeAllEffects();
        this.updateEffects = true;
        this.activeContainer = this.defaultContainer;
        this.killer = null;
        this.lastDamager = null;
        this.combatTracker = new CombatTracker(this);
        this.lastSentExp = -1;
        if (this.keepLevel || keepInventory) {
            this.exp = exp;
        } else {
            this.giveExp(this.newExp);
        }
        this.keepLevel = false;
    }

    @Override
    public CraftPlayer getBukkitEntity() {
        return (CraftPlayer) super.getBukkitEntity();
    }
    // CraftBukkit end
}
