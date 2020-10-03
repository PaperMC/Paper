package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;

public class BehaviorInteractDoor extends Behavior<EntityLiving> {

    @Nullable
    private PathPoint b;
    private int c;

    public BehaviorInteractDoor() {
        super(ImmutableMap.of(MemoryModuleType.PATH, MemoryStatus.VALUE_PRESENT, MemoryModuleType.DOORS_TO_CLOSE, MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean a(WorldServer worldserver, EntityLiving entityliving) {
        PathEntity pathentity = (PathEntity) entityliving.getBehaviorController().getMemory(MemoryModuleType.PATH).get();

        if (!pathentity.b() && !pathentity.c()) {
            if (!Objects.equals(this.b, pathentity.h())) {
                this.c = 20;
                return true;
            } else {
                if (this.c > 0) {
                    --this.c;
                }

                return this.c == 0;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void a(WorldServer worldserver, EntityLiving entityliving, long i) {
        PathEntity pathentity = (PathEntity) entityliving.getBehaviorController().getMemory(MemoryModuleType.PATH).get();

        this.b = pathentity.h();
        PathPoint pathpoint = pathentity.i();
        PathPoint pathpoint1 = pathentity.h();
        BlockPosition blockposition = pathpoint.a();
        IBlockData iblockdata = worldserver.getType(blockposition);

        if (iblockdata.a((Tag) TagsBlock.WOODEN_DOORS)) {
            BlockDoor blockdoor = (BlockDoor) iblockdata.getBlock();

            if (!blockdoor.h(iblockdata)) {
                // CraftBukkit start - entities opening doors
                org.bukkit.event.entity.EntityInteractEvent event = new org.bukkit.event.entity.EntityInteractEvent(entityliving.getBukkitEntity(), org.bukkit.craftbukkit.block.CraftBlock.at(entityliving.world, blockposition));
                entityliving.world.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return;
                }
                // CraftBukkit end
                blockdoor.setDoor(worldserver, iblockdata, blockposition, true);
            }

            this.c(worldserver, entityliving, blockposition);
        }

        BlockPosition blockposition1 = pathpoint1.a();
        IBlockData iblockdata1 = worldserver.getType(blockposition1);

        if (iblockdata1.a((Tag) TagsBlock.WOODEN_DOORS)) {
            BlockDoor blockdoor1 = (BlockDoor) iblockdata1.getBlock();

            if (!blockdoor1.h(iblockdata1)) {
                // CraftBukkit start - entities opening doors
                org.bukkit.event.entity.EntityInteractEvent event = new org.bukkit.event.entity.EntityInteractEvent(entityliving.getBukkitEntity(), org.bukkit.craftbukkit.block.CraftBlock.at(entityliving.world, blockposition));
                entityliving.world.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return;
                }
                // CraftBukkit end
                blockdoor1.setDoor(worldserver, iblockdata1, blockposition1, true);
                this.c(worldserver, entityliving, blockposition1);
            }
        }

        a(worldserver, entityliving, pathpoint, pathpoint1);
    }

    public static void a(WorldServer worldserver, EntityLiving entityliving, @Nullable PathPoint pathpoint, @Nullable PathPoint pathpoint1) {
        BehaviorController<?> behaviorcontroller = entityliving.getBehaviorController();

        if (behaviorcontroller.hasMemory(MemoryModuleType.DOORS_TO_CLOSE)) {
            Iterator iterator = ((Set) behaviorcontroller.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get()).iterator();

            while (iterator.hasNext()) {
                GlobalPos globalpos = (GlobalPos) iterator.next();
                BlockPosition blockposition = globalpos.getBlockPosition();

                if ((pathpoint == null || !pathpoint.a().equals(blockposition)) && (pathpoint1 == null || !pathpoint1.a().equals(blockposition))) {
                    if (a(worldserver, entityliving, globalpos)) {
                        iterator.remove();
                    } else {
                        IBlockData iblockdata = worldserver.getType(blockposition);

                        if (!iblockdata.a((Tag) TagsBlock.WOODEN_DOORS)) {
                            iterator.remove();
                        } else {
                            BlockDoor blockdoor = (BlockDoor) iblockdata.getBlock();

                            if (!blockdoor.h(iblockdata)) {
                                iterator.remove();
                            } else if (a(worldserver, entityliving, blockposition)) {
                                iterator.remove();
                            } else {
                                blockdoor.setDoor(worldserver, iblockdata, blockposition, false);
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }

    }

    private static boolean a(WorldServer worldserver, EntityLiving entityliving, BlockPosition blockposition) {
        BehaviorController<?> behaviorcontroller = entityliving.getBehaviorController();

        return !behaviorcontroller.hasMemory(MemoryModuleType.MOBS) ? false : (behaviorcontroller.getMemory(MemoryModuleType.MOBS).get()).stream().filter((entityliving1) -> { // CraftBukkit - decompile error
            return entityliving1.getEntityType() == entityliving.getEntityType();
        }).filter((entityliving1) -> {
            return blockposition.a((IPosition) entityliving1.getPositionVector(), 2.0D);
        }).anyMatch((entityliving1) -> {
            return b(worldserver, entityliving1, blockposition);
        });
    }

    private static boolean b(WorldServer worldserver, EntityLiving entityliving, BlockPosition blockposition) {
        if (!entityliving.getBehaviorController().hasMemory(MemoryModuleType.PATH)) {
            return false;
        } else {
            PathEntity pathentity = (PathEntity) entityliving.getBehaviorController().getMemory(MemoryModuleType.PATH).get();

            if (pathentity.c()) {
                return false;
            } else {
                PathPoint pathpoint = pathentity.i();

                if (pathpoint == null) {
                    return false;
                } else {
                    PathPoint pathpoint1 = pathentity.h();

                    return blockposition.equals(pathpoint.a()) || blockposition.equals(pathpoint1.a());
                }
            }
        }
    }

    private static boolean a(WorldServer worldserver, EntityLiving entityliving, GlobalPos globalpos) {
        return globalpos.getDimensionManager() != worldserver.getDimensionKey() || !globalpos.getBlockPosition().a((IPosition) entityliving.getPositionVector(), 2.0D);
    }

    private void c(WorldServer worldserver, EntityLiving entityliving, BlockPosition blockposition) {
        BehaviorController<?> behaviorcontroller = entityliving.getBehaviorController();
        GlobalPos globalpos = GlobalPos.create(worldserver.getDimensionKey(), blockposition);

        if (behaviorcontroller.getMemory(MemoryModuleType.DOORS_TO_CLOSE).isPresent()) {
            ((Set) behaviorcontroller.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get()).add(globalpos);
        } else {
            behaviorcontroller.setMemory(MemoryModuleType.DOORS_TO_CLOSE, Sets.newHashSet(new GlobalPos[]{globalpos})); // CraftBukkit - decompile error
        }

    }
}
