package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftRedStoneWire extends CraftBlockData implements RedstoneWire {
    private static final IntegerProperty POWER = RedStoneWireBlock.POWER;

    private static final Map<BlockFace, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = RedStoneWireBlock.PROPERTY_BY_DIRECTION.entrySet().stream()
            .collect(Collectors.toMap(entry -> CraftBlock.notchToBlockFace(entry.getKey()), entry -> entry.getValue()));

    public CraftRedStoneWire(BlockState state) {
        super(state);
    }

    @Override
    public int getPower() {
        return this.get(POWER);
    }

    @Override
    public void setPower(final int power) {
        this.set(POWER, power);
    }

    @Override
    public int getMaximumPower() {
        return POWER.max;
    }

    @Override
    public RedstoneWire.Connection getFace(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        EnumProperty<RedstoneSide> property = PROPERTY_BY_DIRECTION.get(blockFace);
        Preconditions.checkArgument(property != null, "Invalid blockFace, only %s are allowed!", PROPERTY_BY_DIRECTION.keySet().stream().map(Enum::name).collect(Collectors.joining(", ")));
        return this.get(property, RedstoneWire.Connection.class);
    }

    @Override
    public void setFace(final BlockFace blockFace, final RedstoneWire.Connection connection) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        Preconditions.checkArgument(connection != null, "connection cannot be null!");
        EnumProperty<RedstoneSide> property = PROPERTY_BY_DIRECTION.get(blockFace);
        Preconditions.checkArgument(property != null, "Invalid blockFace, only %s are allowed!", PROPERTY_BY_DIRECTION.keySet().stream().map(Enum::name).collect(Collectors.joining(", ")));
        this.set(property, connection);
    }

    @Override
    public Set<BlockFace> getAllowedFaces() {
        return Collections.unmodifiableSet(PROPERTY_BY_DIRECTION.keySet());
    }
}
