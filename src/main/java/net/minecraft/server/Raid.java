package net.minecraft.server;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class Raid {

    private static final IChatBaseComponent a = new ChatMessage("event.minecraft.raid");
    private static final IChatBaseComponent b = new ChatMessage("event.minecraft.raid.victory");
    private static final IChatBaseComponent c = new ChatMessage("event.minecraft.raid.defeat");
    private static final IChatBaseComponent d = Raid.a.mutableCopy().c(" - ").addSibling(Raid.b);
    private static final IChatBaseComponent e = Raid.a.mutableCopy().c(" - ").addSibling(Raid.c);
    private final Map<Integer, EntityRaider> f = Maps.newHashMap();
    private final Map<Integer, Set<EntityRaider>> raiders = Maps.newHashMap();
    public final Set<UUID> heroes = Sets.newHashSet();
    public long ticksActive;
    private BlockPosition center;
    private final WorldServer world;
    private boolean started;
    private final int id;
    public float totalHealth;
    public int badOmenLevel;
    private boolean active;
    private int groupsSpawned;
    private final BossBattleServer bossBattle;
    private int postRaidTicks;
    private int preRaidTicks;
    private final Random random;
    public final int numGroups;
    private Raid.Status status;
    private int x;
    private Optional<BlockPosition> y;

    public Raid(int i, WorldServer worldserver, BlockPosition blockposition) {
        this.bossBattle = new BossBattleServer(Raid.a, BossBattle.BarColor.RED, BossBattle.BarStyle.NOTCHED_10);
        this.random = new Random();
        this.y = Optional.empty();
        this.id = i;
        this.world = worldserver;
        this.active = true;
        this.preRaidTicks = 300;
        this.bossBattle.setProgress(0.0F);
        this.center = blockposition;
        this.numGroups = this.a(worldserver.getDifficulty());
        this.status = Raid.Status.ONGOING;
    }

    public Raid(WorldServer worldserver, NBTTagCompound nbttagcompound) {
        this.bossBattle = new BossBattleServer(Raid.a, BossBattle.BarColor.RED, BossBattle.BarStyle.NOTCHED_10);
        this.random = new Random();
        this.y = Optional.empty();
        this.world = worldserver;
        this.id = nbttagcompound.getInt("Id");
        this.started = nbttagcompound.getBoolean("Started");
        this.active = nbttagcompound.getBoolean("Active");
        this.ticksActive = nbttagcompound.getLong("TicksActive");
        this.badOmenLevel = nbttagcompound.getInt("BadOmenLevel");
        this.groupsSpawned = nbttagcompound.getInt("GroupsSpawned");
        this.preRaidTicks = nbttagcompound.getInt("PreRaidTicks");
        this.postRaidTicks = nbttagcompound.getInt("PostRaidTicks");
        this.totalHealth = nbttagcompound.getFloat("TotalHealth");
        this.center = new BlockPosition(nbttagcompound.getInt("CX"), nbttagcompound.getInt("CY"), nbttagcompound.getInt("CZ"));
        this.numGroups = nbttagcompound.getInt("NumGroups");
        this.status = Raid.Status.b(nbttagcompound.getString("Status"));
        this.heroes.clear();
        if (nbttagcompound.hasKeyOfType("HeroesOfTheVillage", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getList("HeroesOfTheVillage", 11);

            for (int i = 0; i < nbttaglist.size(); ++i) {
                this.heroes.add(GameProfileSerializer.a(nbttaglist.get(i)));
            }
        }

    }

    public boolean a() {
        return this.isVictory() || this.isLoss();
    }

    public boolean b() {
        return this.c() && this.r() == 0 && this.preRaidTicks > 0;
    }

    public boolean c() {
        return this.groupsSpawned > 0;
    }

    public boolean isStopped() {
        return this.status == Raid.Status.STOPPED;
    }

    public boolean isVictory() {
        return this.status == Raid.Status.VICTORY;
    }

    public boolean isLoss() {
        return this.status == Raid.Status.LOSS;
    }

    public World getWorld() {
        return this.world;
    }

    public boolean isStarted() {
        return this.started;
    }

    public int getGroupsSpawned() {
        return this.groupsSpawned;
    }

    private Predicate<EntityPlayer> x() {
        return (entityplayer) -> {
            BlockPosition blockposition = entityplayer.getChunkCoordinates();

            return entityplayer.isAlive() && this.world.b_(blockposition) == this;
        };
    }

    private void y() {
        Set<EntityPlayer> set = Sets.newHashSet(this.bossBattle.getPlayers());
        List<EntityPlayer> list = this.world.a(this.x());
        Iterator iterator = list.iterator();

        EntityPlayer entityplayer;

        while (iterator.hasNext()) {
            entityplayer = (EntityPlayer) iterator.next();
            if (!set.contains(entityplayer)) {
                this.bossBattle.addPlayer(entityplayer);
            }
        }

        iterator = set.iterator();

        while (iterator.hasNext()) {
            entityplayer = (EntityPlayer) iterator.next();
            if (!list.contains(entityplayer)) {
                this.bossBattle.removePlayer(entityplayer);
            }
        }

    }

    public int getMaxBadOmenLevel() {
        return 5;
    }

    public int getBadOmenLevel() {
        return this.badOmenLevel;
    }

    public void a(EntityHuman entityhuman) {
        if (entityhuman.hasEffect(MobEffects.BAD_OMEN)) {
            this.badOmenLevel += entityhuman.getEffect(MobEffects.BAD_OMEN).getAmplifier() + 1;
            this.badOmenLevel = MathHelper.clamp(this.badOmenLevel, 0, this.getMaxBadOmenLevel());
        }

        entityhuman.removeEffect(MobEffects.BAD_OMEN);
    }

    public void stop() {
        this.active = false;
        this.bossBattle.b();
        this.status = Raid.Status.STOPPED;
    }

    public void o() {
        if (!this.isStopped()) {
            if (this.status == Raid.Status.ONGOING) {
                boolean flag = this.active;

                this.active = this.world.isLoaded(this.center);
                if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
                    this.stop();
                    return;
                }

                if (flag != this.active) {
                    this.bossBattle.setVisible(this.active);
                }

                if (!this.active) {
                    return;
                }

                if (!this.world.a_(this.center)) {
                    this.z();
                }

                if (!this.world.a_(this.center)) {
                    if (this.groupsSpawned > 0) {
                        this.status = Raid.Status.LOSS;
                    } else {
                        this.stop();
                    }
                }

                ++this.ticksActive;
                if (this.ticksActive >= 48000L) {
                    this.stop();
                    return;
                }

                int i = this.r();
                boolean flag1;

                if (i == 0 && this.A()) {
                    if (this.preRaidTicks > 0) {
                        flag1 = this.y.isPresent();
                        boolean flag2 = !flag1 && this.preRaidTicks % 5 == 0;

                        if (flag1 && !this.world.getChunkProvider().a(new ChunkCoordIntPair((BlockPosition) this.y.get()))) {
                            flag2 = true;
                        }

                        if (flag2) {
                            byte b0 = 0;

                            if (this.preRaidTicks < 100) {
                                b0 = 1;
                            } else if (this.preRaidTicks < 40) {
                                b0 = 2;
                            }

                            this.y = this.d(b0);
                        }

                        if (this.preRaidTicks == 300 || this.preRaidTicks % 20 == 0) {
                            this.y();
                        }

                        --this.preRaidTicks;
                        this.bossBattle.setProgress(MathHelper.a((float) (300 - this.preRaidTicks) / 300.0F, 0.0F, 1.0F));
                    } else if (this.preRaidTicks == 0 && this.groupsSpawned > 0) {
                        this.preRaidTicks = 300;
                        this.bossBattle.a(Raid.a);
                        return;
                    }
                }

                if (this.ticksActive % 20L == 0L) {
                    this.y();
                    this.F();
                    if (i > 0) {
                        if (i <= 2) {
                            this.bossBattle.a((IChatBaseComponent) Raid.a.mutableCopy().c(" - ").addSibling(new ChatMessage("event.minecraft.raid.raiders_remaining", new Object[]{i})));
                        } else {
                            this.bossBattle.a(Raid.a);
                        }
                    } else {
                        this.bossBattle.a(Raid.a);
                    }
                }

                flag1 = false;
                int j = 0;

                while (this.G()) {
                    BlockPosition blockposition = this.y.isPresent() ? (BlockPosition) this.y.get() : this.a(j, 20);

                    if (blockposition != null) {
                        this.started = true;
                        this.b(blockposition);
                        if (!flag1) {
                            this.a(blockposition);
                            flag1 = true;
                        }
                    } else {
                        ++j;
                    }

                    if (j > 3) {
                        this.stop();
                        break;
                    }
                }

                if (this.isStarted() && !this.A() && i == 0) {
                    if (this.postRaidTicks < 40) {
                        ++this.postRaidTicks;
                    } else {
                        this.status = Raid.Status.VICTORY;
                        Iterator iterator = this.heroes.iterator();

                        while (iterator.hasNext()) {
                            UUID uuid = (UUID) iterator.next();
                            Entity entity = this.world.getEntity(uuid);

                            if (entity instanceof EntityLiving && !entity.isSpectator()) {
                                EntityLiving entityliving = (EntityLiving) entity;

                                entityliving.addEffect(new MobEffect(MobEffects.HERO_OF_THE_VILLAGE, 48000, this.badOmenLevel - 1, false, false, true));
                                if (entityliving instanceof EntityPlayer) {
                                    EntityPlayer entityplayer = (EntityPlayer) entityliving;

                                    entityplayer.a(StatisticList.RAID_WIN);
                                    CriterionTriggers.H.a(entityplayer);
                                }
                            }
                        }
                    }
                }

                this.H();
            } else if (this.a()) {
                ++this.x;
                if (this.x >= 600) {
                    this.stop();
                    return;
                }

                if (this.x % 20 == 0) {
                    this.y();
                    this.bossBattle.setVisible(true);
                    if (this.isVictory()) {
                        this.bossBattle.setProgress(0.0F);
                        this.bossBattle.a(Raid.d);
                    } else {
                        this.bossBattle.a(Raid.e);
                    }
                }
            }

        }
    }

    private void z() {
        Stream<SectionPosition> stream = SectionPosition.a(SectionPosition.a(this.center), 2);
        WorldServer worldserver = this.world;

        this.world.getClass();
        stream.filter(worldserver::a).map(SectionPosition::q).min(Comparator.comparingDouble((blockposition) -> {
            return blockposition.j(this.center);
        })).ifPresent(this::c);
    }

    private Optional<BlockPosition> d(int i) {
        for (int j = 0; j < 3; ++j) {
            BlockPosition blockposition = this.a(i, 1);

            if (blockposition != null) {
                return Optional.of(blockposition);
            }
        }

        return Optional.empty();
    }

    private boolean A() {
        return this.C() ? !this.D() : !this.B();
    }

    private boolean B() {
        return this.getGroupsSpawned() == this.numGroups;
    }

    private boolean C() {
        return this.badOmenLevel > 1;
    }

    private boolean D() {
        return this.getGroupsSpawned() > this.numGroups;
    }

    private boolean E() {
        return this.B() && this.r() == 0 && this.C();
    }

    private void F() {
        Iterator<Set<EntityRaider>> iterator = this.raiders.values().iterator();
        HashSet hashset = Sets.newHashSet();

        while (iterator.hasNext()) {
            Set<EntityRaider> set = (Set) iterator.next();
            Iterator iterator1 = set.iterator();

            while (iterator1.hasNext()) {
                EntityRaider entityraider = (EntityRaider) iterator1.next();
                BlockPosition blockposition = entityraider.getChunkCoordinates();

                if (!entityraider.dead && entityraider.world.getDimensionKey() == this.world.getDimensionKey() && this.center.j(blockposition) < 12544.0D) {
                    if (entityraider.ticksLived > 600) {
                        if (this.world.getEntity(entityraider.getUniqueID()) == null) {
                            hashset.add(entityraider);
                        }

                        if (!this.world.a_(blockposition) && entityraider.dc() > 2400) {
                            entityraider.b(entityraider.fe() + 1);
                        }

                        if (entityraider.fe() >= 30) {
                            hashset.add(entityraider);
                        }
                    }
                } else {
                    hashset.add(entityraider);
                }
            }
        }

        Iterator iterator2 = hashset.iterator();

        while (iterator2.hasNext()) {
            EntityRaider entityraider1 = (EntityRaider) iterator2.next();

            this.a(entityraider1, true);
        }

    }

    private void a(BlockPosition blockposition) {
        float f = 13.0F;
        boolean flag = true;
        Collection<EntityPlayer> collection = this.bossBattle.getPlayers();
        Iterator iterator = this.world.getPlayers().iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();
            Vec3D vec3d = entityplayer.getPositionVector();
            Vec3D vec3d1 = Vec3D.a((BaseBlockPosition) blockposition);
            float f1 = MathHelper.sqrt((vec3d1.x - vec3d.x) * (vec3d1.x - vec3d.x) + (vec3d1.z - vec3d.z) * (vec3d1.z - vec3d.z));
            double d0 = vec3d.x + (double) (13.0F / f1) * (vec3d1.x - vec3d.x);
            double d1 = vec3d.z + (double) (13.0F / f1) * (vec3d1.z - vec3d.z);

            if (f1 <= 64.0F || collection.contains(entityplayer)) {
                entityplayer.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(SoundEffects.EVENT_RAID_HORN, SoundCategory.NEUTRAL, d0, entityplayer.locY(), d1, 64.0F, 1.0F));
            }
        }

    }

    private void b(BlockPosition blockposition) {
        boolean flag = false;
        int i = this.groupsSpawned + 1;

        this.totalHealth = 0.0F;
        DifficultyDamageScaler difficultydamagescaler = this.world.getDamageScaler(blockposition);
        boolean flag1 = this.E();
        Raid.Wave[] araid_wave = Raid.Wave.f;
        int j = araid_wave.length;

        for (int k = 0; k < j; ++k) {
            Raid.Wave raid_wave = araid_wave[k];
            int l = this.a(raid_wave, i, flag1) + this.a(raid_wave, this.random, i, difficultydamagescaler, flag1);
            int i1 = 0;

            for (int j1 = 0; j1 < l; ++j1) {
                EntityRaider entityraider = (EntityRaider) raid_wave.g.a((World) this.world);

                if (!flag && entityraider.eN()) {
                    entityraider.setPatrolLeader(true);
                    this.a(i, entityraider);
                    flag = true;
                }

                this.a(i, entityraider, blockposition, false);
                if (raid_wave.g == EntityTypes.RAVAGER) {
                    EntityRaider entityraider1 = null;

                    if (i == this.a(EnumDifficulty.NORMAL)) {
                        entityraider1 = (EntityRaider) EntityTypes.PILLAGER.a((World) this.world);
                    } else if (i >= this.a(EnumDifficulty.HARD)) {
                        if (i1 == 0) {
                            entityraider1 = (EntityRaider) EntityTypes.EVOKER.a((World) this.world);
                        } else {
                            entityraider1 = (EntityRaider) EntityTypes.VINDICATOR.a((World) this.world);
                        }
                    }

                    ++i1;
                    if (entityraider1 != null) {
                        this.a(i, entityraider1, blockposition, false);
                        entityraider1.setPositionRotation(blockposition, 0.0F, 0.0F);
                        entityraider1.startRiding(entityraider);
                    }
                }
            }
        }

        this.y = Optional.empty();
        ++this.groupsSpawned;
        this.updateProgress();
        this.H();
    }

    public void a(int i, EntityRaider entityraider, @Nullable BlockPosition blockposition, boolean flag) {
        boolean flag1 = this.b(i, entityraider);

        if (flag1) {
            entityraider.a(this);
            entityraider.a(i);
            entityraider.setCanJoinRaid(true);
            entityraider.b(0);
            if (!flag && blockposition != null) {
                entityraider.setPosition((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 1.0D, (double) blockposition.getZ() + 0.5D);
                entityraider.prepare(this.world, this.world.getDamageScaler(blockposition), EnumMobSpawn.EVENT, (GroupDataEntity) null, (NBTTagCompound) null);
                entityraider.a(i, false);
                entityraider.setOnGround(true);
                this.world.addAllEntities(entityraider);
            }
        }

    }

    public void updateProgress() {
        this.bossBattle.setProgress(MathHelper.a(this.sumMobHealth() / this.totalHealth, 0.0F, 1.0F));
    }

    public float sumMobHealth() {
        float f = 0.0F;
        Iterator iterator = this.raiders.values().iterator();

        while (iterator.hasNext()) {
            Set<EntityRaider> set = (Set) iterator.next();

            EntityRaider entityraider;

            for (Iterator iterator1 = set.iterator(); iterator1.hasNext(); f += entityraider.getHealth()) {
                entityraider = (EntityRaider) iterator1.next();
            }
        }

        return f;
    }

    private boolean G() {
        return this.preRaidTicks == 0 && (this.groupsSpawned < this.numGroups || this.E()) && this.r() == 0;
    }

    public int r() {
        return this.raiders.values().stream().mapToInt(Set::size).sum();
    }

    public void a(EntityRaider entityraider, boolean flag) {
        Set<EntityRaider> set = (Set) this.raiders.get(entityraider.fc());

        if (set != null) {
            boolean flag1 = set.remove(entityraider);

            if (flag1) {
                if (flag) {
                    this.totalHealth -= entityraider.getHealth();
                }

                entityraider.a((Raid) null);
                this.updateProgress();
                this.H();
            }
        }

    }

    private void H() {
        this.world.getPersistentRaid().b();
    }

    public static ItemStack s() {
        ItemStack itemstack = new ItemStack(Items.WHITE_BANNER);
        NBTTagCompound nbttagcompound = itemstack.a("BlockEntityTag");
        NBTTagList nbttaglist = (new EnumBannerPatternType.a()).a(EnumBannerPatternType.RHOMBUS_MIDDLE, EnumColor.CYAN).a(EnumBannerPatternType.STRIPE_BOTTOM, EnumColor.LIGHT_GRAY).a(EnumBannerPatternType.STRIPE_CENTER, EnumColor.GRAY).a(EnumBannerPatternType.BORDER, EnumColor.LIGHT_GRAY).a(EnumBannerPatternType.STRIPE_MIDDLE, EnumColor.BLACK).a(EnumBannerPatternType.HALF_HORIZONTAL, EnumColor.LIGHT_GRAY).a(EnumBannerPatternType.CIRCLE_MIDDLE, EnumColor.LIGHT_GRAY).a(EnumBannerPatternType.BORDER, EnumColor.BLACK).a();

        nbttagcompound.set("Patterns", nbttaglist);
        itemstack.a(ItemStack.HideFlags.ADDITIONAL);
        itemstack.a((IChatBaseComponent) (new ChatMessage("block.minecraft.ominous_banner")).a(EnumChatFormat.GOLD));
        return itemstack;
    }

    @Nullable
    public EntityRaider b(int i) {
        return (EntityRaider) this.f.get(i);
    }

    @Nullable
    private BlockPosition a(int i, int j) {
        int k = i == 0 ? 2 : 2 - i;
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

        for (int l = 0; l < j; ++l) {
            float f = this.world.random.nextFloat() * 6.2831855F;
            int i1 = this.center.getX() + MathHelper.d(MathHelper.cos(f) * 32.0F * (float) k) + this.world.random.nextInt(5);
            int j1 = this.center.getZ() + MathHelper.d(MathHelper.sin(f) * 32.0F * (float) k) + this.world.random.nextInt(5);
            int k1 = this.world.a(HeightMap.Type.WORLD_SURFACE, i1, j1);

            blockposition_mutableblockposition.d(i1, k1, j1);
            if ((!this.world.a_(blockposition_mutableblockposition) || i >= 2) && this.world.isAreaLoaded(blockposition_mutableblockposition.getX() - 10, blockposition_mutableblockposition.getY() - 10, blockposition_mutableblockposition.getZ() - 10, blockposition_mutableblockposition.getX() + 10, blockposition_mutableblockposition.getY() + 10, blockposition_mutableblockposition.getZ() + 10) && this.world.getChunkProvider().a(new ChunkCoordIntPair(blockposition_mutableblockposition)) && (SpawnerCreature.a(EntityPositionTypes.Surface.ON_GROUND, (IWorldReader) this.world, blockposition_mutableblockposition, EntityTypes.RAVAGER) || this.world.getType(blockposition_mutableblockposition.down()).a(Blocks.SNOW) && this.world.getType(blockposition_mutableblockposition).isAir())) {
                return blockposition_mutableblockposition;
            }
        }

        return null;
    }

    private boolean b(int i, EntityRaider entityraider) {
        return this.a(i, entityraider, true);
    }

    public boolean a(int i, EntityRaider entityraider, boolean flag) {
        this.raiders.computeIfAbsent(i, (integer) -> {
            return Sets.newHashSet();
        });
        Set<EntityRaider> set = (Set) this.raiders.get(i);
        EntityRaider entityraider1 = null;
        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            EntityRaider entityraider2 = (EntityRaider) iterator.next();

            if (entityraider2.getUniqueID().equals(entityraider.getUniqueID())) {
                entityraider1 = entityraider2;
                break;
            }
        }

        if (entityraider1 != null) {
            set.remove(entityraider1);
            set.add(entityraider);
        }

        set.add(entityraider);
        if (flag) {
            this.totalHealth += entityraider.getHealth();
        }

        this.updateProgress();
        this.H();
        return true;
    }

    public void a(int i, EntityRaider entityraider) {
        this.f.put(i, entityraider);
        entityraider.setSlot(EnumItemSlot.HEAD, s());
        entityraider.a(EnumItemSlot.HEAD, 2.0F);
    }

    public void c(int i) {
        this.f.remove(i);
    }

    public BlockPosition getCenter() {
        return this.center;
    }

    private void c(BlockPosition blockposition) {
        this.center = blockposition;
    }

    public int getId() {
        return this.id;
    }

    private int a(Raid.Wave raid_wave, int i, boolean flag) {
        return flag ? raid_wave.h[this.numGroups] : raid_wave.h[i];
    }

    private int a(Raid.Wave raid_wave, Random random, int i, DifficultyDamageScaler difficultydamagescaler, boolean flag) {
        EnumDifficulty enumdifficulty = difficultydamagescaler.a();
        boolean flag1 = enumdifficulty == EnumDifficulty.EASY;
        boolean flag2 = enumdifficulty == EnumDifficulty.NORMAL;
        int j;

        switch (raid_wave) {
            case WITCH:
                if (flag1 || i <= 2 || i == 4) {
                    return 0;
                }

                j = 1;
                break;
            case PILLAGER:
            case VINDICATOR:
                if (flag1) {
                    j = random.nextInt(2);
                } else if (flag2) {
                    j = 1;
                } else {
                    j = 2;
                }
                break;
            case RAVAGER:
                j = !flag1 && flag ? 1 : 0;
                break;
            default:
                return 0;
        }

        return j > 0 ? random.nextInt(j + 1) : 0;
    }

    public boolean v() {
        return this.active;
    }

    public NBTTagCompound a(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInt("Id", this.id);
        nbttagcompound.setBoolean("Started", this.started);
        nbttagcompound.setBoolean("Active", this.active);
        nbttagcompound.setLong("TicksActive", this.ticksActive);
        nbttagcompound.setInt("BadOmenLevel", this.badOmenLevel);
        nbttagcompound.setInt("GroupsSpawned", this.groupsSpawned);
        nbttagcompound.setInt("PreRaidTicks", this.preRaidTicks);
        nbttagcompound.setInt("PostRaidTicks", this.postRaidTicks);
        nbttagcompound.setFloat("TotalHealth", this.totalHealth);
        nbttagcompound.setInt("NumGroups", this.numGroups);
        nbttagcompound.setString("Status", this.status.a());
        nbttagcompound.setInt("CX", this.center.getX());
        nbttagcompound.setInt("CY", this.center.getY());
        nbttagcompound.setInt("CZ", this.center.getZ());
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = this.heroes.iterator();

        while (iterator.hasNext()) {
            UUID uuid = (UUID) iterator.next();

            nbttaglist.add(GameProfileSerializer.a(uuid));
        }

        nbttagcompound.set("HeroesOfTheVillage", nbttaglist);
        return nbttagcompound;
    }

    public int a(EnumDifficulty enumdifficulty) {
        switch (enumdifficulty) {
            case EASY:
                return 3;
            case NORMAL:
                return 5;
            case HARD:
                return 7;
            default:
                return 0;
        }
    }

    public float w() {
        int i = this.getBadOmenLevel();

        return i == 2 ? 0.1F : (i == 3 ? 0.25F : (i == 4 ? 0.5F : (i == 5 ? 0.75F : 0.0F)));
    }

    public void a(Entity entity) {
        this.heroes.add(entity.getUniqueID());
    }

    static enum Wave {

        VINDICATOR(EntityTypes.VINDICATOR, new int[]{0, 0, 2, 0, 1, 4, 2, 5}), EVOKER(EntityTypes.EVOKER, new int[]{0, 0, 0, 0, 0, 1, 1, 2}), PILLAGER(EntityTypes.PILLAGER, new int[]{0, 4, 3, 3, 4, 4, 4, 2}), WITCH(EntityTypes.WITCH, new int[]{0, 0, 0, 0, 3, 0, 0, 1}), RAVAGER(EntityTypes.RAVAGER, new int[]{0, 0, 0, 1, 0, 1, 0, 2});

        private static final Raid.Wave[] f = values();
        private final EntityTypes<? extends EntityRaider> g;
        private final int[] h;

        private Wave(EntityTypes entitytypes, int[] aint) {
            this.g = entitytypes;
            this.h = aint;
        }
    }

    static enum Status {

        ONGOING, VICTORY, LOSS, STOPPED;

        private static final Raid.Status[] e = values();

        private Status() {}

        private static Raid.Status b(String s) {
            Raid.Status[] araid_status = Raid.Status.e;
            int i = araid_status.length;

            for (int j = 0; j < i; ++j) {
                Raid.Status raid_status = araid_status[j];

                if (s.equalsIgnoreCase(raid_status.name())) {
                    return raid_status;
                }
            }

            return Raid.Status.ONGOING;
        }

        public String a() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
