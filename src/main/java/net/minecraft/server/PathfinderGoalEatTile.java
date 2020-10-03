package net.minecraft.server;

import java.util.EnumSet;
import java.util.function.Predicate;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
// CraftBukkit end

public class PathfinderGoalEatTile extends PathfinderGoal {

    private static final Predicate<IBlockData> a = BlockStatePredicate.a(Blocks.GRASS);
    private final EntityInsentient b;
    private final World c;
    private int d;

    public PathfinderGoalEatTile(EntityInsentient entityinsentient) {
        this.b = entityinsentient;
        this.c = entityinsentient.world;
        this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK, PathfinderGoal.Type.JUMP));
    }

    @Override
    public boolean a() {
        if (this.b.getRandom().nextInt(this.b.isBaby() ? 50 : 1000) != 0) {
            return false;
        } else {
            BlockPosition blockposition = this.b.getChunkCoordinates();

            return PathfinderGoalEatTile.a.test(this.c.getType(blockposition)) ? true : this.c.getType(blockposition.down()).a(Blocks.GRASS_BLOCK);
        }
    }

    @Override
    public void c() {
        this.d = 40;
        this.c.broadcastEntityEffect(this.b, (byte) 10);
        this.b.getNavigation().o();
    }

    @Override
    public void d() {
        this.d = 0;
    }

    @Override
    public boolean b() {
        return this.d > 0;
    }

    public int g() {
        return this.d;
    }

    @Override
    public void e() {
        this.d = Math.max(0, this.d - 1);
        if (this.d == 4) {
            BlockPosition blockposition = this.b.getChunkCoordinates();

            if (PathfinderGoalEatTile.a.test(this.c.getType(blockposition))) {
                // CraftBukkit
                if (!CraftEventFactory.callEntityChangeBlockEvent(this.b, blockposition, Blocks.AIR.getBlockData(), !this.c.getGameRules().getBoolean(GameRules.MOB_GRIEFING)).isCancelled()) {
                    this.c.b(blockposition, false);
                }

                this.b.blockEaten();
            } else {
                BlockPosition blockposition1 = blockposition.down();

                if (this.c.getType(blockposition1).a(Blocks.GRASS_BLOCK)) {
                    // CraftBukkit
                    if (!CraftEventFactory.callEntityChangeBlockEvent(this.b, blockposition, Blocks.AIR.getBlockData(), !this.c.getGameRules().getBoolean(GameRules.MOB_GRIEFING)).isCancelled()) {
                        this.c.triggerEffect(2001, blockposition1, Block.getCombinedId(Blocks.GRASS_BLOCK.getBlockData()));
                        this.c.setTypeAndData(blockposition1, Blocks.DIRT.getBlockData(), 2);
                    }

                    this.b.blockEaten();
                }
            }

        }
    }
}
