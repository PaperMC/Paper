package org.dreeam.leaf.async.path;

import net.minecraft.world.level.pathfinder.NodeEvaluator;
import org.jetbrains.annotations.NotNull;

public interface NodeEvaluatorGenerator {
    @NotNull NodeEvaluator generate(NodeEvaluatorFeatures nodeEvaluatorFeatures);
}
