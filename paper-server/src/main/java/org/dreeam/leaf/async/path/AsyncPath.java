package org.dreeam.leaf.async.path;

import ca.spottedleaf.moonrise.common.util.TickThread;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

/**
 * I'll be using this to represent a path that not be processed yet!
 */
public class AsyncPath extends Path {

    /**
     * Instead of three states, only one is actually required
     * This will update when any thread is done with the path
     */
    private volatile boolean ready = false;

    /**
     * Runnable waiting for this to be processed
     * ConcurrentLinkedQueue is thread-safe, non-blocking and non-synchronized
     */
    private final ConcurrentLinkedQueue<Runnable> postProcessing = new ConcurrentLinkedQueue<>();

    /**
     * A list of positions that this path could path towards
     */
    private final Set<BlockPos> positions;

    /**
     * The supplier of the real processed path
     */
    private final Supplier<Path> pathSupplier;

    /*
     * Processed values
     */

    /**
     * This is a reference to the nodes list in the parent `Path` object
     */
    private final List<Node> nodes;
    /**
     * The block we're trying to path to
     * <p>
     * While processing, we have no idea where this is so consumers of `Path` should check that the path is processed before checking the target block
     */
    private BlockPos target;
    /**
     * How far we are to the target
     * <p>
     * While processing, the target could be anywhere, but theoretically we're always "close" to a theoretical target so default is 0
     */
    private float distToTarget = 0;
    /**
     * Whether we can reach the target
     * <p>
     * While processing, we can always theoretically reach the target so default is true
     */
    private boolean canReach = true;

    @SuppressWarnings("ConstantConditions")
    public AsyncPath(@NotNull List<Node> emptyNodeList, @NotNull Set<BlockPos> positions, @NotNull Supplier<Path> pathSupplier) {
        super(emptyNodeList, null, false);

        this.nodes = emptyNodeList;
        this.positions = positions;
        this.pathSupplier = pathSupplier;

        AsyncPathProcessor.queue(this);
    }

    @Override
    public boolean isProcessed() {
        return this.ready;
    }

    /**
     * Returns the future representing the processing state of this path
     */
    public final void schedulePostProcessing(@NotNull Runnable runnable) {
        if (this.ready) {
            runnable.run();
        } else {
            this.postProcessing.offer(runnable);
            if (this.ready) {
                this.runAllPostProcessing(true);
            }
        }
    }

    /**
     * An easy way to check if this processing path is the same as an attempted new path
     *
     * @param positions - the positions to compare against
     * @return true if we are processing the same positions
     */
    public final boolean hasSameProcessingPositions(final Set<BlockPos> positions) {
        if (this.positions.size() != positions.size()) {
            return false;
        }

        // For single position (common case), do direct comparison
        if (positions.size() == 1) { // Both have the same size at this point
            return this.positions.iterator().next().equals(positions.iterator().next());
        }

        return this.positions.containsAll(positions);
    }

    /**
     * Starts processing this path
     * Since this is no longer a synchronized function, checkProcessed is no longer required
     */
    public final void process() {
        if (this.ready) return;

        synchronized (this) {
            if (this.ready) return; // In the worst case, the main thread only waits until any async thread is done and returns immediately
            final Path bestPath = this.pathSupplier.get();
            this.nodes.addAll(bestPath.nodes); // We mutate this list to reuse the logic in Path
            this.target = bestPath.getTarget();
            this.distToTarget = bestPath.getDistToTarget();
            this.canReach = bestPath.canReach();
            this.ready = true;
        }

        this.runAllPostProcessing(TickThread.isTickThread());
    }

    private void runAllPostProcessing(boolean isTickThread) {
        Runnable runnable;
        while ((runnable = this.postProcessing.poll()) != null) {
            if (isTickThread) {
                runnable.run();
            } else {
                MinecraftServer.getServer().scheduleOnMain(runnable);
            }
        }
    }

    /*
     * Overrides we need for final fields that we cannot modify after processing
     */

    @Override
    public @NotNull BlockPos getTarget() {
        this.process();
        return this.target;
    }

    @Override
    public float getDistToTarget() {
        this.process();
        return this.distToTarget;
    }

    @Override
    public boolean canReach() {
        this.process();
        return this.canReach;
    }

    /*
     * Overrides to ensure we're processed first
     */

    @Override
    public boolean isDone() {
        return this.ready && super.isDone();
    }

    @Override
    public void advance() {
        this.process();
        super.advance();
    }

    @Override
    public boolean notStarted() {
        this.process();
        return super.notStarted();
    }

    @Override
    public @Nullable Node getEndNode() {
        this.process();
        return super.getEndNode();
    }

    @Override
    public @NotNull Node getNode(int index) {
        this.process();
        return super.getNode(index);
    }

    @Override
    public void truncateNodes(int length) {
        this.process();
        super.truncateNodes(length);
    }

    @Override
    public void replaceNode(int index, @NotNull Node node) {
        this.process();
        super.replaceNode(index, node);
    }

    @Override
    public int getNodeCount() {
        this.process();
        return super.getNodeCount();
    }

    @Override
    public int getNextNodeIndex() {
        this.process();
        return super.getNextNodeIndex();
    }

    @Override
    public void setNextNodeIndex(int nodeIndex) {
        this.process();
        super.setNextNodeIndex(nodeIndex);
    }

    @Override
    public @NotNull Vec3 getEntityPosAtNode(@NotNull Entity entity, int index) {
        this.process();
        return super.getEntityPosAtNode(entity, index);
    }

    @Override
    public @NotNull BlockPos getNodePos(int index) {
        this.process();
        return super.getNodePos(index);
    }

    @Override
    public @NotNull Vec3 getNextEntityPos(@NotNull Entity entity) {
        this.process();
        return super.getNextEntityPos(entity);
    }

    @Override
    public @NotNull BlockPos getNextNodePos() {
        this.process();
        return super.getNextNodePos();
    }

    @Override
    public @NotNull Node getNextNode() {
        this.process();
        return super.getNextNode();
    }

    @Override
    public @Nullable Node getPreviousNode() {
        this.process();
        return super.getPreviousNode();
    }

}
