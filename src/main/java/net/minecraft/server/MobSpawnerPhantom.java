package net.minecraft.server;

import java.util.Iterator;
import java.util.Random;

public class MobSpawnerPhantom implements MobSpawner {

    private int a;

    public MobSpawnerPhantom() {}

    @Override
    public int a(WorldServer worldserver, boolean flag, boolean flag1) {
        if (!flag) {
            return 0;
        } else if (!worldserver.getGameRules().getBoolean(GameRules.DO_INSOMNIA)) {
            return 0;
        } else {
            Random random = worldserver.random;

            --this.a;
            if (this.a > 0) {
                return 0;
            } else {
                this.a += (60 + random.nextInt(60)) * 20;
                if (worldserver.c() < 5 && worldserver.getDimensionManager().hasSkyLight()) {
                    return 0;
                } else {
                    int i = 0;
                    Iterator iterator = worldserver.getPlayers().iterator();

                    while (iterator.hasNext()) {
                        EntityHuman entityhuman = (EntityHuman) iterator.next();

                        if (!entityhuman.isSpectator()) {
                            BlockPosition blockposition = entityhuman.getChunkCoordinates();

                            if (!worldserver.getDimensionManager().hasSkyLight() || blockposition.getY() >= worldserver.getSeaLevel() && worldserver.e(blockposition)) {
                                DifficultyDamageScaler difficultydamagescaler = worldserver.getDamageScaler(blockposition);

                                if (difficultydamagescaler.a(random.nextFloat() * 3.0F)) {
                                    ServerStatisticManager serverstatisticmanager = ((EntityPlayer) entityhuman).getStatisticManager();
                                    int j = MathHelper.clamp(serverstatisticmanager.getStatisticValue(StatisticList.CUSTOM.b(StatisticList.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);
                                    boolean flag2 = true;

                                    if (random.nextInt(j) >= 72000) {
                                        BlockPosition blockposition1 = blockposition.up(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21));
                                        IBlockData iblockdata = worldserver.getType(blockposition1);
                                        Fluid fluid = worldserver.getFluid(blockposition1);

                                        if (SpawnerCreature.a((IBlockAccess) worldserver, blockposition1, iblockdata, fluid, EntityTypes.PHANTOM)) {
                                            GroupDataEntity groupdataentity = null;
                                            int k = 1 + random.nextInt(difficultydamagescaler.a().a() + 1);

                                            for (int l = 0; l < k; ++l) {
                                                EntityPhantom entityphantom = (EntityPhantom) EntityTypes.PHANTOM.a((World) worldserver);

                                                entityphantom.setPositionRotation(blockposition1, 0.0F, 0.0F);
                                                groupdataentity = entityphantom.prepare(worldserver, difficultydamagescaler, EnumMobSpawn.NATURAL, groupdataentity, (NBTTagCompound) null);
                                                worldserver.addAllEntities(entityphantom, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.NATURAL); // CraftBukkit
                                            }

                                            i += k;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    return i;
                }
            }
        }
    }
}
