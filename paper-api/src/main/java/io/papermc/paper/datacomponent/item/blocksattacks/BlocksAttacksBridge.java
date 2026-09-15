package io.papermc.paper.datacomponent.item.blocksattacks;

import java.util.Optional;
import java.util.ServiceLoader;

interface BlocksAttacksBridge {

    Optional<BlocksAttacksBridge> BRIDGE = ServiceLoader.load(BlocksAttacksBridge.class, BlocksAttacksBridge.class.getClassLoader()).findFirst();

    static BlocksAttacksBridge bridge() {
        return BRIDGE.orElseThrow();
    }

    DamageReduction.Builder blocksAttacksDamageReduction();

    ItemDamageFunction.Builder blocksAttacksItemDamageFunction();
}
