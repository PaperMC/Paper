package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.List;

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

    List<DamageReduction> damageReductions();

    //ItemDamageFunction itemDamage();

    @Nullable
    TagKey<DamageType> bypassedBy();

    @Nullable
    Key blockSound();

    @Nullable
    Key disableSound();

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

        @Contract(value = "_ -> this", mutates = "this")
        Builder addDamageReduction(DamageReduction reduction);

        @Contract(value = "_ -> this", mutates = "this")
        Builder damageReductions(List<DamageReduction> reductions);

        //@Contract(value = "_ -> this", mutates = "this")
        //Builder itemDamage(ItemDamageFunction function);

        @Contract(value = "_ -> this", mutates = "this")
        Builder bypassedBy(@Nullable TagKey<DamageType> bypassedBy);

        @Contract(value = "_ -> this", mutates = "this")
        Builder blockSound(@Nullable Key sound);

        @Contract(value = "_ -> this", mutates = "this")
        Builder disableSound(@Nullable Key sound);
    }

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface DamageReduction {

        @Nullable
        RegistryKeySet<DamageType> type();

        @IntRange(from = 0)
        float horizontalBlockingAngle();

        float base();

        float factor();

        /**
         * Builder for {@link BlocksAttacks.DamageReduction}.
         */
        @ApiStatus.Experimental
        @ApiStatus.NonExtendable
        interface Builder extends DataComponentBuilder<DamageReduction> {

            @Contract(value = "_ -> this", mutates = "this")
            BlocksAttacks.DamageReduction.Builder type(RegistryKeySet<DamageType> type);

            @Contract(value = "_ -> this", mutates = "this")
            BlocksAttacks.DamageReduction.Builder horizontalBlockingAngle(@IntRange(from = 0) float horizontalBlockingAngle);

            @Contract(value = "_ -> this", mutates = "this")
            BlocksAttacks.DamageReduction.Builder base(float base);

            @Contract(value = "_ -> this", mutates = "this")
            BlocksAttacks.DamageReduction.Builder factor(float factor);
        }

    }
}
