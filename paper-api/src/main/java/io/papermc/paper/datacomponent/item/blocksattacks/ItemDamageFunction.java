package io.papermc.paper.datacomponent.item.blocksattacks;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ItemDamageFunction {

    @Contract(value = "-> new", pure = true)
    static ItemDamageFunction.Builder itemDamageFunction() {
        return BlocksAttacksBridge.bridge().blocksAttacksItemDamageFunction();
    }

    @NonNegative
    float threshold();

    float base();

    float factor();

    int damageToApply(float damage);

    /**
     * Builder for {@link ItemDamageFunction}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<ItemDamageFunction> {

        @Contract(value = "_ -> this", mutates = "this")
        ItemDamageFunction.Builder threshold(@NonNegative final float threshold);

        @Contract(value = "_ -> this", mutates = "this")
        ItemDamageFunction.Builder base(final float base);

        @Contract(value = "_ -> this", mutates = "this")
        ItemDamageFunction.Builder factor(final float factor);
    }
}
