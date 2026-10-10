package io.papermc.paper.datacomponent.item.blocktransformer;

import java.util.List;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.NonExtendable
public interface BlockTransformer extends Keyed {

    @Contract(pure = true)
    @Unmodifiable List<BlockTransformData> transforms();
}
