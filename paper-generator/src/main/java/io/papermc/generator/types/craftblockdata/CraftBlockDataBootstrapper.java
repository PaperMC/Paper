package io.papermc.generator.types.craftblockdata;

import io.papermc.generator.types.SourceGenerator;
import io.papermc.generator.utils.BlockStateMapping;
import java.util.List;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CraftBlockDataBootstrapper {

    public static void bootstrap(List<SourceGenerator> generators) {
        for (Map.Entry<Class<? extends Block>, BlockStateMapping.BlockData> entry : BlockStateMapping.getOrCreate().entrySet()) {
            generators.add(new CraftBlockDataGenerator(entry.getKey(), entry.getValue()));
        }
    }
}
