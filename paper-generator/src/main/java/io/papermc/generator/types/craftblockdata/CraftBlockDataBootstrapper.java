package io.papermc.generator.types.craftblockdata;

import com.google.common.base.Preconditions;
import io.papermc.generator.types.SourceGenerator;
import io.papermc.generator.utils.BlockStateMapping;
import java.util.List;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import org.bukkit.block.data.BlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CraftBlockDataBootstrapper {

    public static void bootstrap(List<SourceGenerator> generators) {
        for (Map.Entry<Class<? extends Block>, BlockStateMapping.BlockData> entry : BlockStateMapping.MAPPING.entrySet()) {
            Class<? extends BlockData> api = BlockStateMapping.getBestSuitedApiClass(entry.getValue());
            Preconditions.checkState(api != null, "Unknown custom BlockData api class for " + entry.getKey().getCanonicalName());

            generators.add(new CraftBlockDataGenerator<>(entry.getKey(), entry.getValue(), api));
        }
    }
}
