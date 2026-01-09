package org.dreeam.leaf.async.path;

import ca.spottedleaf.concurrentutil.collection.MultiThreadedQueue;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class NodeEvaluatorCache {

    private static final Map<NodeEvaluatorFeatures, MultiThreadedQueue<NodeEvaluator>> threadLocalNodeEvaluators = new ConcurrentHashMap<>();
    private static final Map<NodeEvaluator, NodeEvaluatorGenerator> nodeEvaluatorToGenerator = new ConcurrentHashMap<>();

    private static @NotNull Queue<NodeEvaluator> getQueueForFeatures(@NotNull NodeEvaluatorFeatures nodeEvaluatorFeatures) {
        return threadLocalNodeEvaluators.computeIfAbsent(nodeEvaluatorFeatures, key -> new MultiThreadedQueue<>());
    }

    public static @NotNull NodeEvaluator takeNodeEvaluator(@NotNull NodeEvaluatorGenerator generator, @NotNull NodeEvaluator localNodeEvaluator) {
        final NodeEvaluatorFeatures nodeEvaluatorFeatures = NodeEvaluatorFeatures.fromNodeEvaluator(localNodeEvaluator);
        NodeEvaluator nodeEvaluator = getQueueForFeatures(nodeEvaluatorFeatures).poll();

        if (nodeEvaluator == null) {
            nodeEvaluator = generator.generate(nodeEvaluatorFeatures);
        }

        nodeEvaluatorToGenerator.put(nodeEvaluator, generator);

        return nodeEvaluator;
    }

    public static void returnNodeEvaluator(@NotNull NodeEvaluator nodeEvaluator) {
        final NodeEvaluatorGenerator generator = nodeEvaluatorToGenerator.remove(nodeEvaluator);
        Validate.notNull(generator, "NodeEvaluator already returned");

        final NodeEvaluatorFeatures nodeEvaluatorFeatures = NodeEvaluatorFeatures.fromNodeEvaluator(nodeEvaluator);
        getQueueForFeatures(nodeEvaluatorFeatures).offer(nodeEvaluator);
    }

    public static void removeNodeEvaluator(@NotNull NodeEvaluator nodeEvaluator) {
        nodeEvaluatorToGenerator.remove(nodeEvaluator);
    }
}
