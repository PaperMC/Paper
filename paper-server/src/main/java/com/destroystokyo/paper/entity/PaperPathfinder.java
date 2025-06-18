package com.destroystokyo.paper.entity;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

public class PaperPathfinder implements com.destroystokyo.paper.entity.Pathfinder {

    private net.minecraft.world.entity.Mob entity;

    public PaperPathfinder(net.minecraft.world.entity.Mob entity) {
        this.entity = entity;
    }

    @Override
    public Mob getEntity() {
        return (Mob) this.entity.getBukkitEntity();
    }

    public void setHandle(net.minecraft.world.entity.Mob entity) {
        this.entity = entity;
    }

    @Override
    public void stopPathfinding() {
        this.entity.getNavigation().stop();
    }

    @Override
    public boolean hasPath() {
        return this.entity.getNavigation().getPath() != null && !this.entity.getNavigation().getPath().isDone();
    }

    @Nullable
    @Override
    public PathResult getCurrentPath() {
        Path path = this.entity.getNavigation().getPath();
        return path != null && !path.isDone() ? new PaperPathResult(path) : null;
    }

    @Nullable
    @Override
    public PathResult findPath(Location loc) {
        Preconditions.checkArgument(loc != null, "Location can not be null");
        Path path = this.entity.getNavigation().createPath(loc.getX(), loc.getY(), loc.getZ(), 0);
        return path != null ? new PaperPathResult(path) : null;
    }

    @Nullable
    @Override
    public PathResult findPath(LivingEntity target) {
        Preconditions.checkArgument(target != null, "Target can not be null");
        Path path = this.entity.getNavigation().createPath(((CraftLivingEntity) target).getHandle(), 0);
        return path != null ? new PaperPathResult(path) : null;
    }

    @Override
    public boolean moveTo(@Nonnull PathResult path, double speed) {
        Preconditions.checkArgument(path != null, "PathResult can not be null");
        Path pathEntity = ((PaperPathResult) path).path;
        return this.entity.getNavigation().moveTo(pathEntity, speed);
    }

    @Override
    public boolean canOpenDoors() {
        return this.entity.getNavigation().pathFinder.nodeEvaluator.canOpenDoors();
    }

    @Override
    public void setCanOpenDoors(boolean canOpenDoors) {
        this.entity.getNavigation().pathFinder.nodeEvaluator.setCanOpenDoors(canOpenDoors);
    }

    @Override
    public boolean canPassDoors() {
        return this.entity.getNavigation().pathFinder.nodeEvaluator.canPassDoors();
    }

    @Override
    public void setCanPassDoors(boolean canPassDoors) {
        this.entity.getNavigation().pathFinder.nodeEvaluator.setCanPassDoors(canPassDoors);
    }

    @Override
    public boolean canFloat() {
        return this.entity.getNavigation().pathFinder.nodeEvaluator.canFloat();
    }

    @Override
    public void setCanFloat(boolean canFloat) {
        this.entity.getNavigation().pathFinder.nodeEvaluator.setCanFloat(canFloat);
    }

    public class PaperPathResult implements com.destroystokyo.paper.entity.PaperPathfinder.PathResult {

        private final Path path;

        PaperPathResult(Path path) {
            this.path = path;
        }

        @Nullable
        @Override
        public Location getFinalPoint() {
            Node point = this.path.getEndNode();
            return point != null ? CraftLocation.toBukkit(point, PaperPathfinder.this.entity.level()) : null;
        }

        @Override
        public boolean canReachFinalPoint() {
            return this.path.canReach();
        }

        @Override
        public List<Location> getPoints() {
            List<Location> points = new ArrayList<>();
            for (Node point : this.path.nodes) {
                points.add(CraftLocation.toBukkit(point, PaperPathfinder.this.entity.level()));
            }
            return points;
        }

        @Override
        public int getNextPointIndex() {
            return this.path.getNextNodeIndex();
        }

        @Nullable
        @Override
        public Location getNextPoint() {
            if (this.path.isDone()) {
                return null;
            }
            return CraftLocation.toBukkit(this.path.nodes.get(this.path.getNextNodeIndex()), PaperPathfinder.this.entity.level());
        }
    }
}
