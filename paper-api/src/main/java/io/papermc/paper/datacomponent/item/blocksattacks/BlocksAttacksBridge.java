package io.papermc.paper.datacomponent.item.blocksattacks;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import java.util.Optional;
import java.util.ServiceLoader;

@NullMarked
@ApiStatus.Internal
interface BlocksAttacksBridge {

    Optional<BlocksAttacksBridge> BRIDGE = ServiceLoader.load(BlocksAttacksBridge.class).findFirst();

    static BlocksAttacksBridge bridge() {
        return BRIDGE.orElseThrow();
    }

    DamageReduction.Builder blocksAttacksDamageReduction();

    ItemDamageFunction.Builder blocksAttacksItemDamageFunction();
}
