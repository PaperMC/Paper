package io.papermc.paper.datacomponent.item;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperBlockItemDataProperties(
    BlockItemStateProperties impl
) implements BlockItemDataProperties, Handleable<BlockItemStateProperties> {

    @Override
    public BlockData createBlockData(final BlockType blockType) {
        final Block block = CraftBlockType.bukkitToMinecraftNew(blockType);
        final BlockState defaultState = block.defaultBlockState();
        return this.impl.apply(defaultState).createCraftBlockData();
    }

    @Override
    public BlockData applyTo(final BlockData blockData) {
        final BlockState state = ((CraftBlockData) blockData).getState();
        return this.impl.apply(state).createCraftBlockData();
    }

    @Override
    public BlockItemStateProperties getHandle() {
        return this.impl;
    }

    static final class BuilderImpl implements BlockItemDataProperties.Builder {

        private final Map<String, String> properties = new Object2ObjectOpenHashMap<>();

        // TODO when BlockProperty API is merged

        @Override
        public BlockItemDataProperties build() {
            if (this.properties.isEmpty()) {
                return new PaperBlockItemDataProperties(BlockItemStateProperties.EMPTY);
            }
            return new PaperBlockItemDataProperties(new BlockItemStateProperties(new Object2ObjectOpenHashMap<>(this.properties)));
        }
    }
}
