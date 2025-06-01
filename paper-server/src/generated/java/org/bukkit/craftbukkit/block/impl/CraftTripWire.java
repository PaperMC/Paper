package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Tripwire;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftTripWire extends CraftBlockData implements Tripwire {
    private static final BooleanProperty ATTACHED = TripWireBlock.ATTACHED;

    private static final BooleanProperty DISARMED = TripWireBlock.DISARMED;

    private static final BooleanProperty POWERED = TripWireBlock.POWERED;

    private static final Map<BlockFace, BooleanProperty> PROPERTY_BY_DIRECTION = Map.of(
        BlockFace.SOUTH, TripWireBlock.SOUTH,
        BlockFace.NORTH, TripWireBlock.NORTH,
        BlockFace.WEST, TripWireBlock.WEST,
        BlockFace.EAST, TripWireBlock.EAST
    );

    public CraftTripWire(BlockState state) {
        super(state);
    }

    @Override
    public boolean isAttached() {
        return this.get(ATTACHED);
    }

    @Override
    public void setAttached(final boolean attached) {
        this.set(ATTACHED, attached);
    }

    @Override
    public boolean isDisarmed() {
        return this.get(DISARMED);
    }

    @Override
    public void setDisarmed(final boolean disarmed) {
        this.set(DISARMED, disarmed);
    }

    @Override
    public boolean isPowered() {
        return this.get(POWERED);
    }

    @Override
    public void setPowered(final boolean powered) {
        this.set(POWERED, powered);
    }

    @Override
    public boolean hasFace(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        BooleanProperty property = PROPERTY_BY_DIRECTION.get(blockFace);
        Preconditions.checkArgument(property != null, "Invalid blockFace, only %s are allowed!", PROPERTY_BY_DIRECTION.keySet().stream().map(Enum::name).collect(Collectors.joining(", ")));
        return this.get(property);
    }

    @Override
    public void setFace(final BlockFace blockFace, final boolean face) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        BooleanProperty property = PROPERTY_BY_DIRECTION.get(blockFace);
        Preconditions.checkArgument(property != null, "Invalid blockFace, only %s are allowed!", PROPERTY_BY_DIRECTION.keySet().stream().map(Enum::name).collect(Collectors.joining(", ")));
        this.set(property, face);
    }

    @Override
    public Set<BlockFace> getFaces() {
        ImmutableSet.Builder<BlockFace> faces = ImmutableSet.builder();
        for (Map.Entry<BlockFace, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
            if (this.get(entry.getValue())) {
                faces.add(entry.getKey());
            }
        }
        return faces.build();
    }

    @Override
    public Set<BlockFace> getAllowedFaces() {
        return Collections.unmodifiableSet(PROPERTY_BY_DIRECTION.keySet());
    }
}
