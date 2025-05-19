package io.papermc.paper.datacomponent.item.blocksattacks;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
public class BlocksAttacksBridgeImpl implements BlocksAttacksBridge {

    @Override
    public DamageReduction.Builder blocksAttacksDamageReduction() {
        return new PaperDamageReduction.BuilderImpl();
    }

    @Override
    public ItemDamageFunction.Builder blocksAttacksItemDamageFunction() {
        return new PaperItemDamageFunction.BuilderImpl();
    }
}
