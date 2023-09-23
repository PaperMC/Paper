package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * This test class ensures that all Blocks (as registered in BuiltInRegistries.BLOCK)
 * can be converted into their CraftBlockData equivalent.
 */
public class BlockDataConversionTest extends AbstractTestingBase {

    public static Stream<Arguments> data() {
        return BuiltInRegistries.BLOCK.stream().map(Block::defaultBlockState).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testNotNull(IBlockData data) {
        assertNotNull(data);
        assertNotNull(CraftBlockData.fromData(data));
    }
}
