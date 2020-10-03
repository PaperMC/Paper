package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityPlayer extends EntityHuman implements ICrafting {

    private static final Logger LOGGER = LogManager.getLogger();
    public PlayerConnection playerConnection;
    public final MinecraftServer server;
    public final PlayerInteractManager playerInteractManager;
    public final List<Integer> removeQueue = Lists.newLinkedList();
    private final AdvancementDataPlayer advancementDataPlayer;
    private final ServerStatisticManager serverStatisticManager;
    private float lastHealthScored = Float.MIN_VALUE;
    private int lastFoodScored = Integer.MIN_VALUE;
    private int lastAirScored = Integer.MIN_VALUE;
    private int lastArmorScored = Integer.MIN_VALUE;
    private int lastExpLevelScored = Integer.MIN_VALUE;
    private int lastExpTotalScored = Integer.MIN_VALUE;
    private float lastHealthSent = -1.0E8F;
    private int lastFoodSent = -99999999;
    private boolean lastSentSaturationZero = true;
    public int lastSentExp = -99999999;
    public int invulnerableTicks = 60;
    private EnumChatVisibility bY;
    private boolean bZ = true;
    private long ca = SystemUtils.getMonotonicMillis();
    private Entity spectatedEntity;
    public boolean worldChangeInvuln;
    private boolean cd;
    private final RecipeBookServer recipeBook = new RecipeBookServer();
    private Vec3D cf;
    private int cg;
    private boolean ch;
    @Nullable
    private Vec3D ci;
    private SectionPosition cj = SectionPosition.a(0, 0, 0);
    private ResourceKey<World> spawnDimension;
    @Nullable
    private BlockPosition spawn;
    private boolean spawnForced;
    private float spawnAngle;
    private int containerCounter;
    public boolean e;
    public int ping;
    public boolean viewingCredits;

    public EntityPlayer(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager) {
        super(worldserver, worldserver.getSpawn(), worldserver.v(), gameprofile);
        this.spawnDimension = World.OVERWORLD;
        playerinteractmanager.player = this;
        this.playerInteractManager = playerinteractmanager;
        this.server = minecraftserver;
        this.serverStatisticManager = minecraftserver.getPlayerList().getStatisticManager(this);
        this.advancementDataPlayer = minecraftserver.getPlayerList().f(this);
        this.G = 1.0F;
        this.c(worldserver);
    }

    private void c(WorldServer worldserver) {
        BlockPosition blockposition = worldserver.getSpawn();

        if (worldserver.getDimensionManager().hasSkyLight() && worldserver.getMinecraftServer().getSaveData().getGameType() != EnumGamemode.ADVENTURE) {
            int i = Math.max(0, this.server.a(worldserver));
            int j = MathHelper.floor(worldserver.getWorldBorder().b((double) blockposition.getX(), (double) blockposition.getZ()));

            if (j < i) {
                i = j;
            }

            if (j <= 1) {
                i = 1;
            }

            long k = (long) (i * 2 + 1);
            long l = k * k;
            int i1 = l > 2147483647L ? Integer.MAX_VALUE : (int) l;
            int j1 = this.u(i1);
            int k1 = (new Random()).nextInt(i1);

            for (int l1 = 0; l1 < i1; ++l1) {
                int i2 = (k1 + j1 * l1) % i1;
                int j2 = i2 % (i * 2 + 1);
                int k2 = i2 / (i * 2 + 1);
                BlockPosition blockposition1 = WorldProviderNormal.a(worldserver, blockposition.getX() + j2 - i, blockposition.getZ() + k2 - i, false);

                if (blockposition1 != null) {
                    this.setPositionRotation(blockposition1, 0.0F, 0.0F);
                    if (worldserver.getCubes(this)) {
                        break;
                    }
                }
            }
        } else {
            this.setPositionRotation(blockposition, 0.0F, 0.0F);

            while (!worldserver.getCubes(this) && this.locY() < 255.0D) {
                this.setPosition(this.locX(), this.locY() + 1.0D, this.locZ());
            }
        }

    }

    private int u(int i) {
        return i <= 16 ? i - 1 : 17;
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("playerGameType", 99)) {
            if (this.getMinecraftServer().getForceGamemode()) {
                this.playerInteractManager.a(this.getMinecraftServer().getGamemode(), EnumGamemode.NOT_SET);
            } else {
                this.playerInteractManager.a(EnumGamemode.getById(nbttagcompound.getInt("playerGameType")), nbttagcompound.hasKeyOfType("previousPlayerGameType", 3) ? EnumGamemode.getById(nbttagcompound.getInt("previousPlayerGameType")) : EnumGamemode.NOT_SET);
            }
        }

        if (nbttagcompound.hasKeyOfType("enteredNetherPosition", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("enteredNetherPosition");

            this.ci = new Vec3D(nbttagcompound1.getDouble("x"), nbttagcompound1.getDouble("y"), nbttagcompound1.getDouble("z"));
        }

        this.cd = nbttagcompound.getBoolean("seenCredits");
        if (nbttagcompound.hasKeyOfType("recipeBook", 10)) {
            this.recipeBook.a(nbttagcompound.getCompound("recipeBook"), this.server.getCraftingManager());
        }

        if (this.isSleeping()) {
            this.entityWakeup();
        }

        if (nbttagcompound.hasKeyOfType("SpawnX", 99) && nbttagcompound.hasKeyOfType("SpawnY", 99) && nbttagcompound.hasKeyOfType("SpawnZ", 99)) {
            this.spawn = new BlockPosition(nbttagcompound.getInt("SpawnX"), nbttagcompound.getInt("SpawnY"), nbttagcompound.getInt("SpawnZ"));
            this.spawnForced = nbttagcompound.getBoolean("SpawnForced");
            this.spawnAngle = nbttagcompound.getFloat("SpawnAngle");
            if (nbttagcompound.hasKey("SpawnDimension")) {
                DataResult dataresult = World.f.parse(DynamicOpsNBT.a, nbttagcompound.get("SpawnDimension"));
                Logger logger = EntityPlayer.LOGGER;

                logger.getClass();
                this.spawnDimension = (ResourceKey) dataresult.resultOrPartial(logger::error).orElse(World.OVERWORLD);
            }
        }

    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("playerGameType", this.playerInteractManager.getGameMode().getId());
        nbttagcompound.setInt("previousPlayerGameType", this.playerInteractManager.c().getId());
        nbttagcompound.setBoolean("seenCredits", this.cd);
        if (this.ci != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound1.setDouble("x", this.ci.x);
            nbttagcompound1.setDouble("y", this.ci.y);
            nbttagcompound1.setDouble("z", this.ci.z);
            nbttagcompound.set("enteredNetherPosition", nbttagcompound1);
        }

        Entity entity = this.getRootVehicle();
        Entity entity1 = this.getVehicle();

        if (entity1 != null && entity != this && entity.hasSinglePlayerPassenger()) {
            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            NBTTagCompound nbttagcompound3 = new NBTTagCompound();

            entity.d(nbttagcompound3);
            nbttagcompound2.a("Attach", entity1.getUniqueID());
            nbttagcompound2.set("Entity", nbttagcompound3);
            nbttagcompound.set("RootVehicle", nbttagcompound2);
        }

        nbttagcompound.set("recipeBook", this.recipeBook.save());
        nbttagcompound.setString("Dimension", this.world.getDimensionKey().a().toString());
        if (this.spawn != null) {
            nbttagcompound.setInt("SpawnX", this.spawn.getX());
            nbttagcompound.setInt("SpawnY", this.spawn.getY());
            nbttagcompound.setInt("SpawnZ", this.spawn.getZ());
            nbttagcompound.setBoolean("SpawnForced", this.spawnForced);
            nbttagcompound.setFloat("SpawnAngle", this.spawnAngle);
            DataResult dataresult = MinecraftKey.a.encodeStart(DynamicOpsNBT.a, this.spawnDimension.a());
            Logger logger = EntityPlayer.LOGGER;

            logger.getClass();
            dataresult.resultOrPartial(logger::error).ifPresent((nbtbase) -> {
                nbttagcompound.set("SpawnDimension", nbtbase);
            });
        }

    }

    public void a(int i) {
        float f = (float) this.getExpToLevel();
        float f1 = (f - 1.0F) / f;

        this.exp = MathHelper.a((float) i / f, 0.0F, f1);
        this.lastSentExp = -1;
    }

    public void b(int i) {
        this.expLevel = i;
        this.lastSentExp = -1;
    }

    @Override
    public void levelDown(int i) {
        super.levelDown(i);
        this.lastSentExp = -1;
    }

    @Override
    public void enchantDone(ItemStack itemstack, int i) {
        super.enchantDone(itemstack, i);
        this.lastSentExp = -1;
    }

    public void syncInventory() {
        this.activeContainer.addSlotListener(this);
    }

    @Override
    public void enterCombat() {
        super.enterCombat();
        this.playerConnection.sendPacket(new PacketPlayOutCombatEvent(this.getCombatTracker(), PacketPlayOutCombatEvent.EnumCombatEventType.ENTER_COMBAT));
    }

    @Override
    public void exitCombat() {
        super.exitCombat();
        this.playerConnection.sendPacket(new PacketPlayOutCombatEvent(this.getCombatTracker(), PacketPlayOutCombatEvent.EnumCombatEventType.END_COMBAT));
    }

    @Override
    protected void a(IBlockData iblockdata) {
        CriterionTriggers.d.a(this, iblockdata);
    }

    @Override
    protected ItemCooldown i() {
        return new ItemCooldownPlayer(this);
    }

    @Override
    public void tick() {
        this.playerInteractManager.a();
        --this.invulnerableTicks;
        if (this.noDamageTicks > 0) {
            --this.noDamageTicks;
        }

        this.activeContainer.c();
        if (!this.world.isClientSide && !this.activeContainer.canUse(this)) {
            this.closeInventory();
            this.activeContainer = this.defaultContainer;
        }

        while (!this.removeQueue.isEmpty()) {
            int i = Math.min(this.removeQueue.size(), Integer.MAX_VALUE);
            int[] aint = new int[i];
            Iterator<Integer> iterator = this.removeQueue.iterator();
            int j = 0;

            while (iterator.hasNext() && j < i) {
                aint[j++] = (Integer) iterator.next();
                iterator.remove();
            }

            this.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(aint));
        }

        Entity entity = this.getSpecatorTarget();

        if (entity != this) {
            if (entity.isAlive()) {
                this.setLocation(entity.locX(), entity.locY(), entity.locZ(), entity.yaw, entity.pitch);
                this.getWorldServer().getChunkProvider().movePlayer(this);
                if (this.eq()) {
                    this.setSpectatorTarget(this);
                }
            } else {
                this.setSpectatorTarget(this);
            }
        }

        CriterionTriggers.w.a(this);
        if (this.cf != null) {
            CriterionTriggers.u.a(this, this.cf, this.ticksLived - this.cg);
        }

        this.advancementDataPlayer.b(this);
    }

    public void playerTick() {
        try {
            if (!this.isSpectator() || this.world.isLoaded(this.getChunkCoordinates())) {
                super.tick();
            }

            for (int i = 0; i < this.inventory.getSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);

                if (itemstack.getItem().ac_()) {
                    Packet<?> packet = ((ItemWorldMapBase) itemstack.getItem()).a(itemstack, this.world, (EntityHuman) this);

                    if (packet != null) {
                        this.playerConnection.sendPacket(packet);
                    }
                }
            }

            if (this.getHealth() != this.lastHealthSent || this.lastFoodSent != this.foodData.getFoodLevel() || this.foodData.getSaturationLevel() == 0.0F != this.lastSentSaturationZero) {
                this.playerConnection.sendPacket(new PacketPlayOutUpdateHealth(this.getHealth(), this.foodData.getFoodLevel(), this.foodData.getSaturationLevel()));
                this.lastHealthSent = this.getHealth();
                this.lastFoodSent = this.foodData.getFoodLevel();
                this.lastSentSaturationZero = this.foodData.getSaturationLevel() == 0.0F;
            }

            if (this.getHealth() + this.getAbsorptionHearts() != this.lastHealthScored) {
                this.lastHealthScored = this.getHealth() + this.getAbsorptionHearts();
                this.a(IScoreboardCriteria.HEALTH, MathHelper.f(this.lastHealthScored));
            }

            if (this.foodData.getFoodLevel() != this.lastFoodScored) {
                this.lastFoodScored = this.foodData.getFoodLevel();
                this.a(IScoreboardCriteria.FOOD, MathHelper.f((float) this.lastFoodScored));
            }

            if (this.getAirTicks() != this.lastAirScored) {
                this.lastAirScored = this.getAirTicks();
                this.a(IScoreboardCriteria.AIR, MathHelper.f((float) this.lastAirScored));
            }

            if (this.getArmorStrength() != this.lastArmorScored) {
                this.lastArmorScored = this.getArmorStrength();
                this.a(IScoreboardCriteria.ARMOR, MathHelper.f((float) this.lastArmorScored));
            }

            if (this.expTotal != this.lastExpTotalScored) {
                this.lastExpTotalScored = this.expTotal;
                this.a(IScoreboardCriteria.XP, MathHelper.f((float) this.lastExpTotalScored));
            }

            if (this.expLevel != this.lastExpLevelScored) {
                this.lastExpLevelScored = this.expLevel;
                this.a(IScoreboardCriteria.LEVEL, MathHelper.f((float) this.lastExpLevelScored));
            }

            if (this.expTotal != this.lastSentExp) {
                this.lastSentExp = this.expTotal;
                this.playerConnection.sendPacket(new PacketPlayOutExperience(this.exp, this.expTotal, this.expLevel));
            }

            if (this.ticksLived % 20 == 0) {
                CriterionTriggers.p.a(this);
            }

        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Ticking player");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Player being ticked");

            this.appendEntityCrashDetails(crashreportsystemdetails);
            throw new ReportedException(crashreport);
        }
    }

    private void a(IScoreboardCriteria iscoreboardcriteria, int i) {
        this.getScoreboard().getObjectivesForCriteria(iscoreboardcriteria, this.getName(), (scoreboardscore) -> {
            scoreboardscore.setScore(i);
        });
    }

    @Override
    public void die(DamageSource damagesource) {
        boolean flag = this.world.getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES);

        if (flag) {
            IChatBaseComponent ichatbasecomponent = this.getCombatTracker().getDeathMessage();

            this.playerConnection.a((Packet) (new PacketPlayOutCombatEvent(this.getCombatTracker(), PacketPlayOutCombatEvent.EnumCombatEventType.ENTITY_DIED, ichatbasecomponent)), (future) -> {
                if (!future.isSuccess()) {
                    boolean flag1 = true;
                    String s = ichatbasecomponent.a(256);
                    ChatMessage chatmessage = new ChatMessage("death.attack.message_too_long", new Object[]{(new ChatComponentText(s)).a(EnumChatFormat.YELLOW)});
                    IChatMutableComponent ichatmutablecomponent = (new ChatMessage("death.attack.even_more_magic", new Object[]{this.getScoreboardDisplayName()})).format((chatmodifier) -> {
                        return chatmodifier.setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT, chatmessage));
                    });

                    this.playerConnection.sendPacket(new PacketPlayOutCombatEvent(this.getCombatTracker(), PacketPlayOutCombatEvent.EnumCombatEventType.ENTITY_DIED, ichatmutablecomponent));
                }

            });
            ScoreboardTeamBase scoreboardteambase = this.getScoreboardTeam();

            if (scoreboardteambase != null && scoreboardteambase.getDeathMessageVisibility() != ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS) {
                if (scoreboardteambase.getDeathMessageVisibility() == ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OTHER_TEAMS) {
                    this.server.getPlayerList().a((EntityHuman) this, ichatbasecomponent);
                } else if (scoreboardteambase.getDeathMessageVisibility() == ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OWN_TEAM) {
                    this.server.getPlayerList().b(this, ichatbasecomponent);
                }
            } else {
                this.server.getPlayerList().sendMessage(ichatbasecomponent, ChatMessageType.SYSTEM, SystemUtils.b);
            }
        } else {
            this.playerConnection.sendPacket(new PacketPlayOutCombatEvent(this.getCombatTracker(), PacketPlayOutCombatEvent.EnumCombatEventType.ENTITY_DIED));
        }

        this.releaseShoulderEntities();
        if (this.world.getGameRules().getBoolean(GameRules.FORGIVE_DEAD_PLAYERS)) {
            this.eV();
        }

        if (!this.isSpectator()) {
            this.d(damagesource);
        }

        this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.DEATH_COUNT, this.getName(), ScoreboardScore::incrementScore);
        EntityLiving entityliving = this.getKillingEntity();

        if (entityliving != null) {
            this.b(StatisticList.ENTITY_KILLED_BY.b(entityliving.getEntityType()));
            entityliving.a(this, this.aO, damagesource);
            this.f(entityliving);
        }

        this.world.broadcastEntityEffect(this, (byte) 3);
        this.a(StatisticList.DEATHS);
        this.a(StatisticList.CUSTOM.b(StatisticList.TIME_SINCE_DEATH));
        this.a(StatisticList.CUSTOM.b(StatisticList.TIME_SINCE_REST));
        this.extinguish();
        this.setFlag(0, false);
        this.getCombatTracker().g();
    }

    private void eV() {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(this.getChunkCoordinates())).grow(32.0D, 10.0D, 32.0D);

        this.world.b(EntityInsentient.class, axisalignedbb).stream().filter((entityinsentient) -> {
            return entityinsentient instanceof IEntityAngerable;
        }).forEach((entityinsentient) -> {
            ((IEntityAngerable) entityinsentient).b(this);
        });
    }

    @Override
    public void a(Entity entity, int i, DamageSource damagesource) {
        if (entity != this) {
            super.a(entity, i, damagesource);
            this.addScore(i);
            String s = this.getName();
            String s1 = entity.getName();

            this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.TOTAL_KILL_COUNT, s, ScoreboardScore::incrementScore);
            if (entity instanceof EntityHuman) {
                this.a(StatisticList.PLAYER_KILLS);
                this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.PLAYER_KILL_COUNT, s, ScoreboardScore::incrementScore);
            } else {
                this.a(StatisticList.MOB_KILLS);
            }

            this.a(s, s1, IScoreboardCriteria.m);
            this.a(s1, s, IScoreboardCriteria.n);
            CriterionTriggers.b.a(this, entity, damagesource);
        }
    }

    private void a(String s, String s1, IScoreboardCriteria[] aiscoreboardcriteria) {
        ScoreboardTeam scoreboardteam = this.getScoreboard().getPlayerTeam(s1);

        if (scoreboardteam != null) {
            int i = scoreboardteam.getColor().b();

            if (i >= 0 && i < aiscoreboardcriteria.length) {
                this.getScoreboard().getObjectivesForCriteria(aiscoreboardcriteria[i], s, ScoreboardScore::incrementScore);
            }
        }

    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            boolean flag = this.server.j() && this.canPvP() && "fall".equals(damagesource.translationIndex);

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
                        Entity entity1 = entityarrow.getShooter();

                        if (entity1 instanceof EntityHuman && !this.a((EntityHuman) entity1)) {
                            return false;
                        }
                    }
                }

                return super.damageEntity(damagesource, f);
            }
        }
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return !this.canPvP() ? false : super.a(entityhuman);
    }

    private boolean canPvP() {
        return this.server.getPVP();
    }

    @Nullable
    @Override
    protected ShapeDetectorShape a(WorldServer worldserver) {
        ShapeDetectorShape shapedetectorshape = super.a(worldserver);

        if (shapedetectorshape != null && this.world.getDimensionKey() == World.OVERWORLD && worldserver.getDimensionKey() == World.THE_END) {
            Vec3D vec3d = shapedetectorshape.position.add(0.0D, -1.0D, 0.0D);

            return new ShapeDetectorShape(vec3d, Vec3D.ORIGIN, 90.0F, 0.0F);
        } else {
            return shapedetectorshape;
        }
    }

    @Nullable
    @Override
    public Entity b(WorldServer worldserver) {
        this.worldChangeInvuln = true;
        WorldServer worldserver1 = this.getWorldServer();
        ResourceKey<World> resourcekey = worldserver1.getDimensionKey();

        if (resourcekey == World.THE_END && worldserver.getDimensionKey() == World.OVERWORLD) {
            this.decouple();
            this.getWorldServer().removePlayer(this);
            if (!this.viewingCredits) {
                this.viewingCredits = true;
                this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.e, this.cd ? 0.0F : 1.0F));
                this.cd = true;
            }

            return this;
        } else {
            WorldData worlddata = worldserver.getWorldData();

            this.playerConnection.sendPacket(new PacketPlayOutRespawn(worldserver.getDimensionManager(), worldserver.getDimensionKey(), BiomeManager.a(worldserver.getSeed()), this.playerInteractManager.getGameMode(), this.playerInteractManager.c(), worldserver.isDebugWorld(), worldserver.isFlatWorld(), true));
            this.playerConnection.sendPacket(new PacketPlayOutServerDifficulty(worlddata.getDifficulty(), worlddata.isDifficultyLocked()));
            PlayerList playerlist = this.server.getPlayerList();

            playerlist.d(this);
            worldserver1.removePlayer(this);
            this.dead = false;
            ShapeDetectorShape shapedetectorshape = this.a(worldserver);

            if (shapedetectorshape != null) {
                worldserver1.getMethodProfiler().enter("moving");
                if (resourcekey == World.OVERWORLD && worldserver.getDimensionKey() == World.THE_NETHER) {
                    this.ci = this.getPositionVector();
                } else if (worldserver.getDimensionKey() == World.THE_END) {
                    this.a(worldserver, new BlockPosition(shapedetectorshape.position));
                }

                worldserver1.getMethodProfiler().exit();
                worldserver1.getMethodProfiler().enter("placing");
                this.spawnIn(worldserver);
                worldserver.addPlayerPortal(this);
                this.setYawPitch(shapedetectorshape.yaw, shapedetectorshape.pitch);
                this.teleportAndSync(shapedetectorshape.position.x, shapedetectorshape.position.y, shapedetectorshape.position.z);
                worldserver1.getMethodProfiler().exit();
                this.triggerDimensionAdvancements(worldserver1);
                this.playerInteractManager.a(worldserver);
                this.playerConnection.sendPacket(new PacketPlayOutAbilities(this.abilities));
                playerlist.a(this, worldserver);
                playerlist.updateClient(this);
                Iterator iterator = this.getEffects().iterator();

                while (iterator.hasNext()) {
                    MobEffect mobeffect = (MobEffect) iterator.next();

                    this.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.getId(), mobeffect));
                }

                this.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1032, BlockPosition.ZERO, 0, false));
                this.lastSentExp = -1;
                this.lastHealthSent = -1.0F;
                this.lastFoodSent = -1;
            }

            return this;
        }
    }

    private void a(WorldServer worldserver, BlockPosition blockposition) {
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = blockposition.i();

        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                for (int k = -1; k < 3; ++k) {
                    IBlockData iblockdata = k == -1 ? Blocks.OBSIDIAN.getBlockData() : Blocks.AIR.getBlockData();

                    worldserver.setTypeUpdate(blockposition_mutableblockposition.g(blockposition).e(j, k, i), iblockdata);
                }
            }
        }

    }

    @Override
    protected Optional<BlockUtil.Rectangle> a(WorldServer worldserver, BlockPosition blockposition, boolean flag) {
        Optional<BlockUtil.Rectangle> optional = super.a(worldserver, blockposition, flag);

        if (optional.isPresent()) {
            return optional;
        } else {
            EnumDirection.EnumAxis enumdirection_enumaxis = (EnumDirection.EnumAxis) this.world.getType(this.ac).d(BlockPortal.AXIS).orElse(EnumDirection.EnumAxis.X);
            Optional<BlockUtil.Rectangle> optional1 = worldserver.getTravelAgent().createPortal(blockposition, enumdirection_enumaxis);

            if (!optional1.isPresent()) {
                EntityPlayer.LOGGER.error("Unable to create a portal, likely target out of worldborder");
            }

            return optional1;
        }
    }

    public void triggerDimensionAdvancements(WorldServer worldserver) {
        ResourceKey<World> resourcekey = worldserver.getDimensionKey();
        ResourceKey<World> resourcekey1 = this.world.getDimensionKey();

        CriterionTriggers.v.a(this, resourcekey, resourcekey1);
        if (resourcekey == World.THE_NETHER && resourcekey1 == World.OVERWORLD && this.ci != null) {
            CriterionTriggers.C.a(this, this.ci);
        }

        if (resourcekey1 != World.THE_NETHER) {
            this.ci = null;
        }

    }

    @Override
    public boolean a(EntityPlayer entityplayer) {
        return entityplayer.isSpectator() ? this.getSpecatorTarget() == this : (this.isSpectator() ? false : super.a(entityplayer));
    }

    private void a(TileEntity tileentity) {
        if (tileentity != null) {
            PacketPlayOutTileEntityData packetplayouttileentitydata = tileentity.getUpdatePacket();

            if (packetplayouttileentitydata != null) {
                this.playerConnection.sendPacket(packetplayouttileentitydata);
            }
        }

    }

    @Override
    public void receive(Entity entity, int i) {
        super.receive(entity, i);
        this.activeContainer.c();
    }

    @Override
    public Either<EntityHuman.EnumBedResult, Unit> sleep(BlockPosition blockposition) {
        EnumDirection enumdirection = (EnumDirection) this.world.getType(blockposition).get(BlockFacingHorizontal.FACING);

        if (!this.isSleeping() && this.isAlive()) {
            if (!this.world.getDimensionManager().isNatural()) {
                return Either.left(EntityHuman.EnumBedResult.NOT_POSSIBLE_HERE);
            } else if (!this.a(blockposition, enumdirection)) {
                return Either.left(EntityHuman.EnumBedResult.TOO_FAR_AWAY);
            } else if (this.b(blockposition, enumdirection)) {
                return Either.left(EntityHuman.EnumBedResult.OBSTRUCTED);
            } else {
                this.setRespawnPosition(this.world.getDimensionKey(), blockposition, this.yaw, false, true);
                if (this.world.isDay()) {
                    return Either.left(EntityHuman.EnumBedResult.NOT_POSSIBLE_NOW);
                } else {
                    if (!this.isCreative()) {
                        double d0 = 8.0D;
                        double d1 = 5.0D;
                        Vec3D vec3d = Vec3D.c((BaseBlockPosition) blockposition);
                        List<EntityMonster> list = this.world.a(EntityMonster.class, new AxisAlignedBB(vec3d.getX() - 8.0D, vec3d.getY() - 5.0D, vec3d.getZ() - 8.0D, vec3d.getX() + 8.0D, vec3d.getY() + 5.0D, vec3d.getZ() + 8.0D), (entitymonster) -> {
                            return entitymonster.f((EntityHuman) this);
                        });

                        if (!list.isEmpty()) {
                            return Either.left(EntityHuman.EnumBedResult.NOT_SAFE);
                        }
                    }

                    Either<EntityHuman.EnumBedResult, Unit> either = super.sleep(blockposition).ifRight((unit) -> {
                        this.a(StatisticList.SLEEP_IN_BED);
                        CriterionTriggers.q.a(this);
                    });

                    ((WorldServer) this.world).everyoneSleeping();
                    return either;
                }
            }
        } else {
            return Either.left(EntityHuman.EnumBedResult.OTHER_PROBLEM);
        }
    }

    @Override
    public void entitySleep(BlockPosition blockposition) {
        this.a(StatisticList.CUSTOM.b(StatisticList.TIME_SINCE_REST));
        super.entitySleep(blockposition);
    }

    private boolean a(BlockPosition blockposition, EnumDirection enumdirection) {
        return this.g(blockposition) || this.g(blockposition.shift(enumdirection.opposite()));
    }

    private boolean g(BlockPosition blockposition) {
        Vec3D vec3d = Vec3D.c((BaseBlockPosition) blockposition);

        return Math.abs(this.locX() - vec3d.getX()) <= 3.0D && Math.abs(this.locY() - vec3d.getY()) <= 2.0D && Math.abs(this.locZ() - vec3d.getZ()) <= 3.0D;
    }

    private boolean b(BlockPosition blockposition, EnumDirection enumdirection) {
        BlockPosition blockposition1 = blockposition.up();

        return !this.f(blockposition1) || !this.f(blockposition1.shift(enumdirection.opposite()));
    }

    @Override
    public void wakeup(boolean flag, boolean flag1) {
        if (this.isSleeping()) {
            this.getWorldServer().getChunkProvider().broadcastIncludingSelf(this, new PacketPlayOutAnimation(this, 2));
        }

        super.wakeup(flag, flag1);
        if (this.playerConnection != null) {
            this.playerConnection.a(this.locX(), this.locY(), this.locZ(), this.yaw, this.pitch);
        }

    }

    @Override
    public boolean a(Entity entity, boolean flag) {
        Entity entity1 = this.getVehicle();

        if (!super.a(entity, flag)) {
            return false;
        } else {
            Entity entity2 = this.getVehicle();

            if (entity2 != entity1 && this.playerConnection != null) {
                this.playerConnection.a(this.locX(), this.locY(), this.locZ(), this.yaw, this.pitch);
            }

            return true;
        }
    }

    @Override
    public void stopRiding() {
        Entity entity = this.getVehicle();

        super.stopRiding();
        Entity entity1 = this.getVehicle();

        if (entity1 != entity && this.playerConnection != null) {
            this.playerConnection.a(this.locX(), this.locY(), this.locZ(), this.yaw, this.pitch);
        }

    }

    @Override
    public boolean isInvulnerable(DamageSource damagesource) {
        return super.isInvulnerable(damagesource) || this.H() || this.abilities.isInvulnerable && damagesource == DamageSource.WITHER;
    }

    @Override
    protected void a(double d0, boolean flag, IBlockData iblockdata, BlockPosition blockposition) {}

    @Override
    protected void c(BlockPosition blockposition) {
        if (!this.isSpectator()) {
            super.c(blockposition);
        }

    }

    public void a(double d0, boolean flag) {
        BlockPosition blockposition = this.ao();

        if (this.world.isLoaded(blockposition)) {
            super.a(d0, flag, this.world.getType(blockposition), blockposition);
        }
    }

    @Override
    public void openSign(TileEntitySign tileentitysign) {
        tileentitysign.a((EntityHuman) this);
        this.playerConnection.sendPacket(new PacketPlayOutOpenSignEditor(tileentitysign.getPosition()));
    }

    public void nextContainerCounter() {
        this.containerCounter = this.containerCounter % 100 + 1;
    }

    @Override
    public OptionalInt openContainer(@Nullable ITileInventory itileinventory) {
        if (itileinventory == null) {
            return OptionalInt.empty();
        } else {
            if (this.activeContainer != this.defaultContainer) {
                this.closeInventory();
            }

            this.nextContainerCounter();
            Container container = itileinventory.createMenu(this.containerCounter, this.inventory, this);

            if (container == null) {
                if (this.isSpectator()) {
                    this.a((IChatBaseComponent) (new ChatMessage("container.spectatorCantOpen")).a(EnumChatFormat.RED), true);
                }

                return OptionalInt.empty();
            } else {
                this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(container.windowId, container.getType(), itileinventory.getScoreboardDisplayName()));
                container.addSlotListener(this);
                this.activeContainer = container;
                return OptionalInt.of(this.containerCounter);
            }
        }
    }

    @Override
    public void openTrade(int i, MerchantRecipeList merchantrecipelist, int j, int k, boolean flag, boolean flag1) {
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindowMerchant(i, merchantrecipelist, j, k, flag, flag1));
    }

    @Override
    public void openHorseInventory(EntityHorseAbstract entityhorseabstract, IInventory iinventory) {
        if (this.activeContainer != this.defaultContainer) {
            this.closeInventory();
        }

        this.nextContainerCounter();
        this.playerConnection.sendPacket(new PacketPlayOutOpenWindowHorse(this.containerCounter, iinventory.getSize(), entityhorseabstract.getId()));
        this.activeContainer = new ContainerHorse(this.containerCounter, this.inventory, iinventory, entityhorseabstract);
        this.activeContainer.addSlotListener(this);
    }

    @Override
    public void openBook(ItemStack itemstack, EnumHand enumhand) {
        Item item = itemstack.getItem();

        if (item == Items.WRITTEN_BOOK) {
            if (ItemWrittenBook.a(itemstack, this.getCommandListener(), (EntityHuman) this)) {
                this.activeContainer.c();
            }

            this.playerConnection.sendPacket(new PacketPlayOutOpenBook(enumhand));
        }

    }

    @Override
    public void a(TileEntityCommand tileentitycommand) {
        tileentitycommand.c(true);
        this.a((TileEntity) tileentitycommand);
    }

    @Override
    public void a(Container container, int i, ItemStack itemstack) {
        if (!(container.getSlot(i) instanceof SlotResult)) {
            if (container == this.defaultContainer) {
                CriterionTriggers.e.a(this, this.inventory, itemstack);
            }

            if (!this.e) {
                this.playerConnection.sendPacket(new PacketPlayOutSetSlot(container.windowId, i, itemstack));
            }
        }
    }

    public void updateInventory(Container container) {
        this.a(container, container.b());
    }

    @Override
    public void a(Container container, NonNullList<ItemStack> nonnulllist) {
        this.playerConnection.sendPacket(new PacketPlayOutWindowItems(container.windowId, nonnulllist));
        this.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.inventory.getCarried()));
    }

    @Override
    public void setContainerData(Container container, int i, int j) {
        this.playerConnection.sendPacket(new PacketPlayOutWindowData(container.windowId, i, j));
    }

    @Override
    public void closeInventory() {
        this.playerConnection.sendPacket(new PacketPlayOutCloseWindow(this.activeContainer.windowId));
        this.o();
    }

    public void broadcastCarriedItem() {
        if (!this.e) {
            this.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.inventory.getCarried()));
        }
    }

    public void o() {
        this.activeContainer.b((EntityHuman) this);
        this.activeContainer = this.defaultContainer;
    }

    public void a(float f, float f1, boolean flag, boolean flag1) {
        if (this.isPassenger()) {
            if (f >= -1.0F && f <= 1.0F) {
                this.aR = f;
            }

            if (f1 >= -1.0F && f1 <= 1.0F) {
                this.aT = f1;
            }

            this.jumping = flag;
            this.setSneaking(flag1);
        }

    }

    @Override
    public void a(Statistic<?> statistic, int i) {
        this.serverStatisticManager.b(this, statistic, i);
        this.getScoreboard().getObjectivesForCriteria(statistic, this.getName(), (scoreboardscore) -> {
            scoreboardscore.addScore(i);
        });
    }

    @Override
    public void a(Statistic<?> statistic) {
        this.serverStatisticManager.setStatistic(this, statistic, 0);
        this.getScoreboard().getObjectivesForCriteria(statistic, this.getName(), ScoreboardScore::c);
    }

    @Override
    public int discoverRecipes(Collection<IRecipe<?>> collection) {
        return this.recipeBook.a(collection, this);
    }

    @Override
    public void a(MinecraftKey[] aminecraftkey) {
        List<IRecipe<?>> list = Lists.newArrayList();
        MinecraftKey[] aminecraftkey1 = aminecraftkey;
        int i = aminecraftkey.length;

        for (int j = 0; j < i; ++j) {
            MinecraftKey minecraftkey = aminecraftkey1[j];

            this.server.getCraftingManager().getRecipe(minecraftkey).ifPresent(list::add);
        }

        this.discoverRecipes(list);
    }

    @Override
    public int undiscoverRecipes(Collection<IRecipe<?>> collection) {
        return this.recipeBook.b(collection, this);
    }

    @Override
    public void giveExp(int i) {
        super.giveExp(i);
        this.lastSentExp = -1;
    }

    public void p() {
        this.ch = true;
        this.ejectPassengers();
        if (this.isSleeping()) {
            this.wakeup(true, false);
        }

    }

    public boolean q() {
        return this.ch;
    }

    public void triggerHealthUpdate() {
        this.lastHealthSent = -1.0E8F;
    }

    @Override
    public void a(IChatBaseComponent ichatbasecomponent, boolean flag) {
        this.playerConnection.sendPacket(new PacketPlayOutChat(ichatbasecomponent, flag ? ChatMessageType.GAME_INFO : ChatMessageType.CHAT, SystemUtils.b));
    }

    @Override
    protected void s() {
        if (!this.activeItem.isEmpty() && this.isHandRaised()) {
            this.playerConnection.sendPacket(new PacketPlayOutEntityStatus(this, (byte) 9));
            super.s();
        }

    }

    @Override
    public void a(ArgumentAnchor.Anchor argumentanchor_anchor, Vec3D vec3d) {
        super.a(argumentanchor_anchor, vec3d);
        this.playerConnection.sendPacket(new PacketPlayOutLookAt(argumentanchor_anchor, vec3d.x, vec3d.y, vec3d.z));
    }

    public void a(ArgumentAnchor.Anchor argumentanchor_anchor, Entity entity, ArgumentAnchor.Anchor argumentanchor_anchor1) {
        Vec3D vec3d = argumentanchor_anchor1.a(entity);

        super.a(argumentanchor_anchor, vec3d);
        this.playerConnection.sendPacket(new PacketPlayOutLookAt(argumentanchor_anchor, entity, argumentanchor_anchor1));
    }

    public void copyFrom(EntityPlayer entityplayer, boolean flag) {
        if (flag) {
            this.inventory.a(entityplayer.inventory);
            this.setHealth(entityplayer.getHealth());
            this.foodData = entityplayer.foodData;
            this.expLevel = entityplayer.expLevel;
            this.expTotal = entityplayer.expTotal;
            this.exp = entityplayer.exp;
            this.setScore(entityplayer.getScore());
            this.ac = entityplayer.ac;
        } else if (this.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY) || entityplayer.isSpectator()) {
            this.inventory.a(entityplayer.inventory);
            this.expLevel = entityplayer.expLevel;
            this.expTotal = entityplayer.expTotal;
            this.exp = entityplayer.exp;
            this.setScore(entityplayer.getScore());
        }

        this.bG = entityplayer.bG;
        this.enderChest = entityplayer.enderChest;
        this.getDataWatcher().set(EntityPlayer.bi, entityplayer.getDataWatcher().get(EntityPlayer.bi));
        this.lastSentExp = -1;
        this.lastHealthSent = -1.0F;
        this.lastFoodSent = -1;
        this.recipeBook.a((RecipeBook) entityplayer.recipeBook);
        this.removeQueue.addAll(entityplayer.removeQueue);
        this.cd = entityplayer.cd;
        this.ci = entityplayer.ci;
        this.setShoulderEntityLeft(entityplayer.getShoulderEntityLeft());
        this.setShoulderEntityRight(entityplayer.getShoulderEntityRight());
    }

    @Override
    protected void a(MobEffect mobeffect) {
        super.a(mobeffect);
        this.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.getId(), mobeffect));
        if (mobeffect.getMobEffect() == MobEffects.LEVITATION) {
            this.cg = this.ticksLived;
            this.cf = this.getPositionVector();
        }

        CriterionTriggers.A.a(this);
    }

    @Override
    protected void a(MobEffect mobeffect, boolean flag) {
        super.a(mobeffect, flag);
        this.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.getId(), mobeffect));
        CriterionTriggers.A.a(this);
    }

    @Override
    protected void b(MobEffect mobeffect) {
        super.b(mobeffect);
        this.playerConnection.sendPacket(new PacketPlayOutRemoveEntityEffect(this.getId(), mobeffect.getMobEffect()));
        if (mobeffect.getMobEffect() == MobEffects.LEVITATION) {
            this.cf = null;
        }

        CriterionTriggers.A.a(this);
    }

    @Override
    public void enderTeleportTo(double d0, double d1, double d2) {
        this.playerConnection.a(d0, d1, d2, this.yaw, this.pitch);
    }

    @Override
    public void teleportAndSync(double d0, double d1, double d2) {
        this.enderTeleportTo(d0, d1, d2);
        this.playerConnection.syncPosition();
    }

    @Override
    public void a(Entity entity) {
        this.getWorldServer().getChunkProvider().broadcastIncludingSelf(this, new PacketPlayOutAnimation(entity, 4));
    }

    @Override
    public void b(Entity entity) {
        this.getWorldServer().getChunkProvider().broadcastIncludingSelf(this, new PacketPlayOutAnimation(entity, 5));
    }

    @Override
    public void updateAbilities() {
        if (this.playerConnection != null) {
            this.playerConnection.sendPacket(new PacketPlayOutAbilities(this.abilities));
            this.C();
        }
    }

    public WorldServer getWorldServer() {
        return (WorldServer) this.world;
    }

    @Override
    public void a(EnumGamemode enumgamemode) {
        this.playerInteractManager.setGameMode(enumgamemode);
        this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.d, (float) enumgamemode.getId()));
        if (enumgamemode == EnumGamemode.SPECTATOR) {
            this.releaseShoulderEntities();
            this.stopRiding();
        } else {
            this.setSpectatorTarget(this);
        }

        this.updateAbilities();
        this.dT();
    }

    @Override
    public boolean isSpectator() {
        return this.playerInteractManager.getGameMode() == EnumGamemode.SPECTATOR;
    }

    @Override
    public boolean isCreative() {
        return this.playerInteractManager.getGameMode() == EnumGamemode.CREATIVE;
    }

    @Override
    public void sendMessage(IChatBaseComponent ichatbasecomponent, UUID uuid) {
        this.a(ichatbasecomponent, ChatMessageType.SYSTEM, uuid);
    }

    public void a(IChatBaseComponent ichatbasecomponent, ChatMessageType chatmessagetype, UUID uuid) {
        this.playerConnection.a((Packet) (new PacketPlayOutChat(ichatbasecomponent, chatmessagetype, uuid)), (future) -> {
            if (!future.isSuccess() && (chatmessagetype == ChatMessageType.GAME_INFO || chatmessagetype == ChatMessageType.SYSTEM)) {
                boolean flag = true;
                String s = ichatbasecomponent.a(256);
                IChatMutableComponent ichatmutablecomponent = (new ChatComponentText(s)).a(EnumChatFormat.YELLOW);

                this.playerConnection.sendPacket(new PacketPlayOutChat((new ChatMessage("multiplayer.message_not_delivered", new Object[]{ichatmutablecomponent})).a(EnumChatFormat.RED), ChatMessageType.SYSTEM, uuid));
            }

        });
    }

    public String v() {
        String s = this.playerConnection.networkManager.getSocketAddress().toString();

        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }

    public void a(PacketPlayInSettings packetplayinsettings) {
        this.bY = packetplayinsettings.d();
        this.bZ = packetplayinsettings.e();
        this.getDataWatcher().set(EntityPlayer.bi, (byte) packetplayinsettings.f());
        this.getDataWatcher().set(EntityPlayer.bj, (byte) (packetplayinsettings.getMainHand() == EnumMainHand.LEFT ? 0 : 1));
    }

    public EnumChatVisibility getChatFlags() {
        return this.bY;
    }

    public void setResourcePack(String s, String s1) {
        this.playerConnection.sendPacket(new PacketPlayOutResourcePackSend(s, s1));
    }

    @Override
    protected int y() {
        return this.server.b(this.getProfile());
    }

    public void resetIdleTimer() {
        this.ca = SystemUtils.getMonotonicMillis();
    }

    public ServerStatisticManager getStatisticManager() {
        return this.serverStatisticManager;
    }

    public RecipeBookServer getRecipeBook() {
        return this.recipeBook;
    }

    public void c(Entity entity) {
        if (entity instanceof EntityHuman) {
            this.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[]{entity.getId()}));
        } else {
            this.removeQueue.add(entity.getId());
        }

    }

    public void d(Entity entity) {
        this.removeQueue.remove(entity.getId());
    }

    @Override
    protected void C() {
        if (this.isSpectator()) {
            this.de();
            this.setInvisible(true);
        } else {
            super.C();
        }

    }

    public Entity getSpecatorTarget() {
        return (Entity) (this.spectatedEntity == null ? this : this.spectatedEntity);
    }

    public void setSpectatorTarget(Entity entity) {
        Entity entity1 = this.getSpecatorTarget();

        this.spectatedEntity = (Entity) (entity == null ? this : entity);
        if (entity1 != this.spectatedEntity) {
            this.playerConnection.sendPacket(new PacketPlayOutCamera(this.spectatedEntity));
            this.enderTeleportTo(this.spectatedEntity.locX(), this.spectatedEntity.locY(), this.spectatedEntity.locZ());
        }

    }

    @Override
    protected void E() {
        if (!this.worldChangeInvuln) {
            super.E();
        }

    }

    @Override
    public void attack(Entity entity) {
        if (this.playerInteractManager.getGameMode() == EnumGamemode.SPECTATOR) {
            this.setSpectatorTarget(entity);
        } else {
            super.attack(entity);
        }

    }

    public long F() {
        return this.ca;
    }

    @Nullable
    public IChatBaseComponent getPlayerListName() {
        return null;
    }

    @Override
    public void swingHand(EnumHand enumhand) {
        super.swingHand(enumhand);
        this.resetAttackCooldown();
    }

    public boolean H() {
        return this.worldChangeInvuln;
    }

    public void I() {
        this.worldChangeInvuln = false;
    }

    public AdvancementDataPlayer getAdvancementData() {
        return this.advancementDataPlayer;
    }

    public void a(WorldServer worldserver, double d0, double d1, double d2, float f, float f1) {
        this.setSpectatorTarget(this);
        this.stopRiding();
        if (worldserver == this.world) {
            this.playerConnection.a(d0, d1, d2, f, f1);
        } else {
            WorldServer worldserver1 = this.getWorldServer();
            WorldData worlddata = worldserver.getWorldData();

            this.playerConnection.sendPacket(new PacketPlayOutRespawn(worldserver.getDimensionManager(), worldserver.getDimensionKey(), BiomeManager.a(worldserver.getSeed()), this.playerInteractManager.getGameMode(), this.playerInteractManager.c(), worldserver.isDebugWorld(), worldserver.isFlatWorld(), true));
            this.playerConnection.sendPacket(new PacketPlayOutServerDifficulty(worlddata.getDifficulty(), worlddata.isDifficultyLocked()));
            this.server.getPlayerList().d(this);
            worldserver1.removePlayer(this);
            this.dead = false;
            this.setPositionRotation(d0, d1, d2, f, f1);
            this.spawnIn(worldserver);
            worldserver.addPlayerCommand(this);
            this.triggerDimensionAdvancements(worldserver1);
            this.playerConnection.a(d0, d1, d2, f, f1);
            this.playerInteractManager.a(worldserver);
            this.server.getPlayerList().a(this, worldserver);
            this.server.getPlayerList().updateClient(this);
        }

    }

    @Nullable
    public BlockPosition getSpawn() {
        return this.spawn;
    }

    public float getSpawnAngle() {
        return this.spawnAngle;
    }

    public ResourceKey<World> getSpawnDimension() {
        return this.spawnDimension;
    }

    public boolean isSpawnForced() {
        return this.spawnForced;
    }

    public void setRespawnPosition(ResourceKey<World> resourcekey, @Nullable BlockPosition blockposition, float f, boolean flag, boolean flag1) {
        if (blockposition != null) {
            boolean flag2 = blockposition.equals(this.spawn) && resourcekey.equals(this.spawnDimension);

            if (flag1 && !flag2) {
                this.sendMessage(new ChatMessage("block.minecraft.set_spawn"), SystemUtils.b);
            }

            this.spawn = blockposition;
            this.spawnDimension = resourcekey;
            this.spawnAngle = f;
            this.spawnForced = flag;
        } else {
            this.spawn = null;
            this.spawnDimension = World.OVERWORLD;
            this.spawnAngle = 0.0F;
            this.spawnForced = false;
        }

    }

    public void a(ChunkCoordIntPair chunkcoordintpair, Packet<?> packet, Packet<?> packet1) {
        this.playerConnection.sendPacket(packet1);
        this.playerConnection.sendPacket(packet);
    }

    public void a(ChunkCoordIntPair chunkcoordintpair) {
        if (this.isAlive()) {
            this.playerConnection.sendPacket(new PacketPlayOutUnloadChunk(chunkcoordintpair.x, chunkcoordintpair.z));
        }

    }

    public SectionPosition O() {
        return this.cj;
    }

    public void a(SectionPosition sectionposition) {
        this.cj = sectionposition;
    }

    @Override
    public void a(SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1) {
        this.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(soundeffect, soundcategory, this.locX(), this.locY(), this.locZ(), f, f1));
    }

    @Override
    public Packet<?> P() {
        return new PacketPlayOutNamedEntitySpawn(this);
    }

    @Override
    public EntityItem a(ItemStack itemstack, boolean flag, boolean flag1) {
        EntityItem entityitem = super.a(itemstack, flag, flag1);

        if (entityitem == null) {
            return null;
        } else {
            this.world.addEntity(entityitem);
            ItemStack itemstack1 = entityitem.getItemStack();

            if (flag1) {
                if (!itemstack1.isEmpty()) {
                    this.a(StatisticList.ITEM_DROPPED.b(itemstack1.getItem()), itemstack.getCount());
                }

                this.a(StatisticList.DROP);
            }

            return entityitem;
        }
    }
}
