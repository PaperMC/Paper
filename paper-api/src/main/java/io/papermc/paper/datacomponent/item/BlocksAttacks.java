package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.damage.DamageType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// TODO
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface BlocksAttacks {

    @Contract(value = "-> new", pure = true)
    static Builder blocksAttacks() {
        return ItemComponentTypesBridge.bridge().blocksAttacks();
    }

    float blockDelaySeconds();

    float disableCooldownScale();

    //List<DamageReduction> damageReductions();

    //ItemDamageFunction itemDamage();

    @Nullable TagKey<DamageType> bypassedBy();

    @Nullable Key blockSound();

    @Nullable Key disableSound();

    /**
     * Builder for {@link BlocksAttacks}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<BlocksAttacks> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder blockDelaySeconds(float delay);

        @Contract(value = "_ -> this", mutates = "this")
        Builder disableCooldownScale(float scale);

        //@Contract(value = "_ -> this", mutates = "this")
        //Builder addDamageReduction(DamageReduction reduction);

        //@Contract(value = "_ -> this", mutates = "this")
        //Builder damageReductions(List<DamageReduction> reductions);

        //@Contract(value = "_ -> this", mutates = "this")
        //Builder itemDamage(ItemDamageFunction function);

        @Contract(value = "_ -> this", mutates = "this")
        Builder bypassedBy(@Nullable TagKey<DamageType> bypassedBy);

        @Contract(value = "_ -> this", mutates = "this")
        Builder blockSound(@Nullable Key sound);

        @Contract(value = "_ -> this", mutates = "this")
        Builder disableSound(@Nullable Key sound);
    }
}
