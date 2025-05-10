package io.papermc.paper.datacomponent.item.blocksattacks;

public final class BlocksAttacksBridgesImpl implements BlocksAttacksBridge {

    @Override
    public DamageReduction.Builder blocksAttacksDamageReduction() {
        return new PaperDamageReduction.BuilderImpl();
    }

    @Override
    public ItemDamageFunction.Builder blocksAttacksItemDamageFunction() {
        return new PaperItemDamageFunction.BuilderImpl();
    }
}
