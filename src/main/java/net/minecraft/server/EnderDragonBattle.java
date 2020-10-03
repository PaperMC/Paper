package net.minecraft.server;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnderDragonBattle {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Predicate<Entity> b = IEntitySelector.a.and(IEntitySelector.a(0.0D, 128.0D, 0.0D, 192.0D));
    public final BossBattleServer bossBattle;
    public final WorldServer world;
    private final List<Integer> gateways;
    private final ShapeDetector f;
    private int g;
    private int h;
    private int i;
    private int j;
    private boolean dragonKilled;
    private boolean previouslyKilled;
    public UUID dragonUUID;
    private boolean n;
    public BlockPosition exitPortalLocation;
    public EnumDragonRespawn respawnPhase;
    private int q;
    private List<EntityEnderCrystal> r;

    public EnderDragonBattle(WorldServer worldserver, long i, NBTTagCompound nbttagcompound) {
        this.bossBattle = (BossBattleServer) (new BossBattleServer(new ChatMessage("entity.minecraft.ender_dragon"), BossBattle.BarColor.PINK, BossBattle.BarStyle.PROGRESS)).setPlayMusic(true).c(true);
        this.gateways = Lists.newArrayList();
        this.n = true;
        this.world = worldserver;
        if (nbttagcompound.hasKeyOfType("DragonKilled", 99)) {
            if (nbttagcompound.b("Dragon")) {
                this.dragonUUID = nbttagcompound.a("Dragon");
            }

            this.dragonKilled = nbttagcompound.getBoolean("DragonKilled");
            this.previouslyKilled = nbttagcompound.getBoolean("PreviouslyKilled");
            if (nbttagcompound.getBoolean("IsRespawning")) {
                this.respawnPhase = EnumDragonRespawn.START;
            }

            if (nbttagcompound.hasKeyOfType("ExitPortalLocation", 10)) {
                this.exitPortalLocation = GameProfileSerializer.b(nbttagcompound.getCompound("ExitPortalLocation"));
            }
        } else {
            this.dragonKilled = true;
            this.previouslyKilled = true;
        }

        if (nbttagcompound.hasKeyOfType("Gateways", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getList("Gateways", 3);

            for (int j = 0; j < nbttaglist.size(); ++j) {
                this.gateways.add(nbttaglist.e(j));
            }
        } else {
            this.gateways.addAll(ContiguousSet.create(Range.closedOpen(0, 20), DiscreteDomain.integers()));
            Collections.shuffle(this.gateways, new Random(i));
        }

        this.f = ShapeDetectorBuilder.a().a("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").a("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").a("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").a("  ###  ", " #   # ", "#     #", "#  #  #", "#     #", " #   # ", "  ###  ").a("       ", "  ###  ", " ##### ", " ##### ", " ##### ", "  ###  ", "       ").a('#', ShapeDetectorBlock.a(BlockPredicate.a(Blocks.BEDROCK))).b();
    }

    public NBTTagCompound a() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (this.dragonUUID != null) {
            nbttagcompound.a("Dragon", this.dragonUUID);
        }

        nbttagcompound.setBoolean("DragonKilled", this.dragonKilled);
        nbttagcompound.setBoolean("PreviouslyKilled", this.previouslyKilled);
        if (this.exitPortalLocation != null) {
            nbttagcompound.set("ExitPortalLocation", GameProfileSerializer.a(this.exitPortalLocation));
        }

        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = this.gateways.iterator();

        while (iterator.hasNext()) {
            int i = (Integer) iterator.next();

            nbttaglist.add(NBTTagInt.a(i));
        }

        nbttagcompound.set("Gateways", nbttaglist);
        return nbttagcompound;
    }

    public void b() {
        this.bossBattle.setVisible(!this.dragonKilled);
        if (++this.j >= 20) {
            this.l();
            this.j = 0;
        }

        if (!this.bossBattle.getPlayers().isEmpty()) {
            this.world.getChunkProvider().addTicket(TicketType.DRAGON, new ChunkCoordIntPair(0, 0), 9, Unit.INSTANCE);
            boolean flag = this.k();

            if (this.n && flag) {
                this.g();
                this.n = false;
            }

            if (this.respawnPhase != null) {
                if (this.r == null && flag) {
                    this.respawnPhase = null;
                    this.initiateRespawn();
                }

                this.respawnPhase.a(this.world, this, this.r, this.q++, this.exitPortalLocation);
            }

            if (!this.dragonKilled) {
                if ((this.dragonUUID == null || ++this.g >= 1200) && flag) {
                    this.h();
                    this.g = 0;
                }

                if (++this.i >= 100 && flag) {
                    this.m();
                    this.i = 0;
                }
            }
        } else {
            this.world.getChunkProvider().removeTicket(TicketType.DRAGON, new ChunkCoordIntPair(0, 0), 9, Unit.INSTANCE);
        }

    }

    private void g() {
        EnderDragonBattle.LOGGER.info("Scanning for legacy world dragon fight...");
        boolean flag = this.i();

        if (flag) {
            EnderDragonBattle.LOGGER.info("Found that the dragon has been killed in this world already.");
            this.previouslyKilled = true;
        } else {
            EnderDragonBattle.LOGGER.info("Found that the dragon has not yet been killed in this world.");
            this.previouslyKilled = false;
            if (this.getExitPortalShape() == null) {
                this.generateExitPortal(false);
            }
        }

        List<EntityEnderDragon> list = this.world.g();

        if (list.isEmpty()) {
            this.dragonKilled = true;
        } else {
            EntityEnderDragon entityenderdragon = (EntityEnderDragon) list.get(0);

            this.dragonUUID = entityenderdragon.getUniqueID();
            EnderDragonBattle.LOGGER.info("Found that there's a dragon still alive ({})", entityenderdragon);
            this.dragonKilled = false;
            if (!flag) {
                EnderDragonBattle.LOGGER.info("But we didn't have a portal, let's remove it.");
                entityenderdragon.die();
                this.dragonUUID = null;
            }
        }

        if (!this.previouslyKilled && this.dragonKilled) {
            this.dragonKilled = false;
        }

    }

    private void h() {
        List<EntityEnderDragon> list = this.world.g();

        if (list.isEmpty()) {
            EnderDragonBattle.LOGGER.debug("Haven't seen the dragon, respawning it");
            this.o();
        } else {
            EnderDragonBattle.LOGGER.debug("Haven't seen our dragon, but found another one to use.");
            this.dragonUUID = ((EntityEnderDragon) list.get(0)).getUniqueID();
        }

    }

    public void setRespawnPhase(EnumDragonRespawn enumdragonrespawn) {
        if (this.respawnPhase == null) {
            throw new IllegalStateException("Dragon respawn isn't in progress, can't skip ahead in the animation.");
        } else {
            this.q = 0;
            if (enumdragonrespawn == EnumDragonRespawn.END) {
                this.respawnPhase = null;
                this.dragonKilled = false;
                EntityEnderDragon entityenderdragon = this.o();
                Iterator iterator = this.bossBattle.getPlayers().iterator();

                while (iterator.hasNext()) {
                    EntityPlayer entityplayer = (EntityPlayer) iterator.next();

                    CriterionTriggers.n.a(entityplayer, (Entity) entityenderdragon);
                }
            } else {
                this.respawnPhase = enumdragonrespawn;
            }

        }
    }

    private boolean i() {
        for (int i = -8; i <= 8; ++i) {
            int j = -8;

            label27:
            while (j <= 8) {
                Chunk chunk = this.world.getChunkAt(i, j);
                Iterator iterator = chunk.getTileEntities().values().iterator();

                TileEntity tileentity;

                do {
                    if (!iterator.hasNext()) {
                        ++j;
                        continue label27;
                    }

                    tileentity = (TileEntity) iterator.next();
                } while (!(tileentity instanceof TileEntityEnderPortal));

                return true;
            }
        }

        return false;
    }

    @Nullable
    public ShapeDetector.ShapeDetectorCollection getExitPortalShape() {
        int i;
        int j;

        for (i = -8; i <= 8; ++i) {
            for (j = -8; j <= 8; ++j) {
                Chunk chunk = this.world.getChunkAt(i, j);
                Iterator iterator = chunk.getTileEntities().values().iterator();

                while (iterator.hasNext()) {
                    TileEntity tileentity = (TileEntity) iterator.next();

                    if (tileentity instanceof TileEntityEnderPortal) {
                        ShapeDetector.ShapeDetectorCollection shapedetector_shapedetectorcollection = this.f.a(this.world, tileentity.getPosition());

                        if (shapedetector_shapedetectorcollection != null) {
                            BlockPosition blockposition = shapedetector_shapedetectorcollection.a(3, 3, 3).getPosition();

                            if (this.exitPortalLocation == null && blockposition.getX() == 0 && blockposition.getZ() == 0) {
                                this.exitPortalLocation = blockposition;
                            }

                            return shapedetector_shapedetectorcollection;
                        }
                    }
                }
            }
        }

        i = this.world.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, WorldGenEndTrophy.a).getY();

        for (j = i; j >= 0; --j) {
            ShapeDetector.ShapeDetectorCollection shapedetector_shapedetectorcollection1 = this.f.a(this.world, new BlockPosition(WorldGenEndTrophy.a.getX(), j, WorldGenEndTrophy.a.getZ()));

            if (shapedetector_shapedetectorcollection1 != null) {
                if (this.exitPortalLocation == null) {
                    this.exitPortalLocation = shapedetector_shapedetectorcollection1.a(3, 3, 3).getPosition();
                }

                return shapedetector_shapedetectorcollection1;
            }
        }

        return null;
    }

    private boolean k() {
        for (int i = -8; i <= 8; ++i) {
            for (int j = 8; j <= 8; ++j) {
                IChunkAccess ichunkaccess = this.world.getChunkAt(i, j, ChunkStatus.FULL, false);

                if (!(ichunkaccess instanceof Chunk)) {
                    return false;
                }

                PlayerChunk.State playerchunk_state = ((Chunk) ichunkaccess).getState();

                if (!playerchunk_state.isAtLeast(PlayerChunk.State.TICKING)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void l() {
        Set<EntityPlayer> set = Sets.newHashSet();
        Iterator iterator = this.world.a(EnderDragonBattle.b).iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            this.bossBattle.addPlayer(entityplayer);
            set.add(entityplayer);
        }

        Set<EntityPlayer> set1 = Sets.newHashSet(this.bossBattle.getPlayers());

        set1.removeAll(set);
        Iterator iterator1 = set1.iterator();

        while (iterator1.hasNext()) {
            EntityPlayer entityplayer1 = (EntityPlayer) iterator1.next();

            this.bossBattle.removePlayer(entityplayer1);
        }

    }

    private void m() {
        this.i = 0;
        this.h = 0;

        WorldGenEnder.Spike worldgenender_spike;

        for (Iterator iterator = WorldGenEnder.a((GeneratorAccessSeed) this.world).iterator(); iterator.hasNext(); this.h += this.world.a(EntityEnderCrystal.class, worldgenender_spike.f()).size()) {
            worldgenender_spike = (WorldGenEnder.Spike) iterator.next();
        }

        EnderDragonBattle.LOGGER.debug("Found {} end crystals still alive", this.h);
    }

    public void a(EntityEnderDragon entityenderdragon) {
        if (entityenderdragon.getUniqueID().equals(this.dragonUUID)) {
            this.bossBattle.setProgress(0.0F);
            this.bossBattle.setVisible(false);
            this.generateExitPortal(true);
            this.n();
            if (!this.previouslyKilled) {
                this.world.setTypeUpdate(this.world.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, WorldGenEndTrophy.a), Blocks.DRAGON_EGG.getBlockData());
            }

            this.previouslyKilled = true;
            this.dragonKilled = true;
        }

    }

    private void n() {
        if (!this.gateways.isEmpty()) {
            int i = (Integer) this.gateways.remove(this.gateways.size() - 1);
            int j = MathHelper.floor(96.0D * Math.cos(2.0D * (-3.141592653589793D + 0.15707963267948966D * (double) i)));
            int k = MathHelper.floor(96.0D * Math.sin(2.0D * (-3.141592653589793D + 0.15707963267948966D * (double) i)));

            this.a(new BlockPosition(j, 75, k));
        }
    }

    private void a(BlockPosition blockposition) {
        this.world.triggerEffect(3000, blockposition, 0);
        BiomeDecoratorGroups.END_GATEWAY_DELAYED.a(this.world, this.world.getChunkProvider().getChunkGenerator(), new Random(), blockposition);
    }

    public void generateExitPortal(boolean flag) {
        WorldGenEndTrophy worldgenendtrophy = new WorldGenEndTrophy(flag);

        if (this.exitPortalLocation == null) {
            for (this.exitPortalLocation = this.world.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING_NO_LEAVES, WorldGenEndTrophy.a).down(); this.world.getType(this.exitPortalLocation).a(Blocks.BEDROCK) && this.exitPortalLocation.getY() > this.world.getSeaLevel(); this.exitPortalLocation = this.exitPortalLocation.down()) {
                ;
            }
        }

        worldgenendtrophy.b((WorldGenFeatureConfiguration) WorldGenFeatureConfiguration.k).a(this.world, this.world.getChunkProvider().getChunkGenerator(), new Random(), this.exitPortalLocation);
    }

    private EntityEnderDragon o() {
        this.world.getChunkAtWorldCoords(new BlockPosition(0, 128, 0));
        EntityEnderDragon entityenderdragon = (EntityEnderDragon) EntityTypes.ENDER_DRAGON.a((World) this.world);

        entityenderdragon.getDragonControllerManager().setControllerPhase(DragonControllerPhase.HOLDING_PATTERN);
        entityenderdragon.setPositionRotation(0.0D, 128.0D, 0.0D, this.world.random.nextFloat() * 360.0F, 0.0F);
        this.world.addEntity(entityenderdragon);
        this.dragonUUID = entityenderdragon.getUniqueID();
        return entityenderdragon;
    }

    public void b(EntityEnderDragon entityenderdragon) {
        if (entityenderdragon.getUniqueID().equals(this.dragonUUID)) {
            this.bossBattle.setProgress(entityenderdragon.getHealth() / entityenderdragon.getMaxHealth());
            this.g = 0;
            if (entityenderdragon.hasCustomName()) {
                this.bossBattle.a(entityenderdragon.getScoreboardDisplayName());
            }
        }

    }

    public int c() {
        return this.h;
    }

    public void a(EntityEnderCrystal entityendercrystal, DamageSource damagesource) {
        if (this.respawnPhase != null && this.r.contains(entityendercrystal)) {
            EnderDragonBattle.LOGGER.debug("Aborting respawn sequence");
            this.respawnPhase = null;
            this.q = 0;
            this.resetCrystals();
            this.generateExitPortal(true);
        } else {
            this.m();
            Entity entity = this.world.getEntity(this.dragonUUID);

            if (entity instanceof EntityEnderDragon) {
                ((EntityEnderDragon) entity).a(entityendercrystal, entityendercrystal.getChunkCoordinates(), damagesource);
            }
        }

    }

    public boolean isPreviouslyKilled() {
        return this.previouslyKilled;
    }

    public void initiateRespawn() {
        if (this.dragonKilled && this.respawnPhase == null) {
            BlockPosition blockposition = this.exitPortalLocation;

            if (blockposition == null) {
                EnderDragonBattle.LOGGER.debug("Tried to respawn, but need to find the portal first.");
                ShapeDetector.ShapeDetectorCollection shapedetector_shapedetectorcollection = this.getExitPortalShape();

                if (shapedetector_shapedetectorcollection == null) {
                    EnderDragonBattle.LOGGER.debug("Couldn't find a portal, so we made one.");
                    this.generateExitPortal(true);
                } else {
                    EnderDragonBattle.LOGGER.debug("Found the exit portal & temporarily using it.");
                }

                blockposition = this.exitPortalLocation;
            }

            List<EntityEnderCrystal> list = Lists.newArrayList();
            BlockPosition blockposition1 = blockposition.up(1);
            Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

            while (iterator.hasNext()) {
                EnumDirection enumdirection = (EnumDirection) iterator.next();
                List<EntityEnderCrystal> list1 = this.world.a(EntityEnderCrystal.class, new AxisAlignedBB(blockposition1.shift(enumdirection, 2)));

                if (list1.isEmpty()) {
                    return;
                }

                list.addAll(list1);
            }

            EnderDragonBattle.LOGGER.debug("Found all crystals, respawning dragon.");
            this.a((List) list);
        }

    }

    private void a(List<EntityEnderCrystal> list) {
        if (this.dragonKilled && this.respawnPhase == null) {
            for (ShapeDetector.ShapeDetectorCollection shapedetector_shapedetectorcollection = this.getExitPortalShape(); shapedetector_shapedetectorcollection != null; shapedetector_shapedetectorcollection = this.getExitPortalShape()) {
                for (int i = 0; i < this.f.c(); ++i) {
                    for (int j = 0; j < this.f.b(); ++j) {
                        for (int k = 0; k < this.f.a(); ++k) {
                            ShapeDetectorBlock shapedetectorblock = shapedetector_shapedetectorcollection.a(i, j, k);

                            if (shapedetectorblock.a().a(Blocks.BEDROCK) || shapedetectorblock.a().a(Blocks.END_PORTAL)) {
                                this.world.setTypeUpdate(shapedetectorblock.getPosition(), Blocks.END_STONE.getBlockData());
                            }
                        }
                    }
                }
            }

            this.respawnPhase = EnumDragonRespawn.START;
            this.q = 0;
            this.generateExitPortal(false);
            this.r = list;
        }

    }

    public void resetCrystals() {
        Iterator iterator = WorldGenEnder.a((GeneratorAccessSeed) this.world).iterator();

        while (iterator.hasNext()) {
            WorldGenEnder.Spike worldgenender_spike = (WorldGenEnder.Spike) iterator.next();
            List<EntityEnderCrystal> list = this.world.a(EntityEnderCrystal.class, worldgenender_spike.f());
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext()) {
                EntityEnderCrystal entityendercrystal = (EntityEnderCrystal) iterator1.next();

                entityendercrystal.setInvulnerable(false);
                entityendercrystal.setBeamTarget((BlockPosition) null);
            }
        }

    }
}
