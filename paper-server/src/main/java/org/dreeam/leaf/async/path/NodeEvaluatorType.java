package org.dreeam.leaf.async.path;

import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;

public enum NodeEvaluatorType {
    WALK,
    SWIM,
    AMPHIBIOUS,
    FLY;

    public static NodeEvaluatorType fromNodeEvaluator(NodeEvaluator nodeEvaluator) {
        if (nodeEvaluator instanceof SwimNodeEvaluator) return SWIM;
        if (nodeEvaluator instanceof FlyNodeEvaluator) return FLY;
        if (nodeEvaluator instanceof AmphibiousNodeEvaluator) return AMPHIBIOUS;
        return WALK;
    }
}
