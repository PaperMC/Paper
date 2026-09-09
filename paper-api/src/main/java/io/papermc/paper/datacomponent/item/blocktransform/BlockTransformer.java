package io.papermc.paper.datacomponent.item.blocktransform;

import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.NonExtendable
public interface BlockTransformer {

    @Contract(pure = true)
    List<BlockTransformData> transforms();

}
