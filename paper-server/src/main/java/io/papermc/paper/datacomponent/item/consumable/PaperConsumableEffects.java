package io.papermc.paper.datacomponent.item.consumable;

import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.PlaySoundConsumeEffect;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;

public final class PaperConsumableEffects {

    private PaperConsumableEffects() {
    }

    public static ConsumeEffect fromNms(net.minecraft.world.item.consume_effects.ConsumeEffect consumable) {
        return switch (consumable) {
            case ApplyStatusEffectsConsumeEffect effect -> new PaperApplyStatusEffects(effect);
            case ClearAllStatusEffectsConsumeEffect effect -> new PaperClearAllStatusEffects(effect);
            case PlaySoundConsumeEffect effect -> new PaperPlaySound(effect);
            case RemoveStatusEffectsConsumeEffect effect -> new PaperRemoveStatusEffects(effect);
            case TeleportRandomlyConsumeEffect effect -> new PaperTeleportRandomly(effect);
            default -> throw new UnsupportedOperationException("Don't know how to convert " + consumable.getClass());
        };
    }

    public static net.minecraft.world.item.consume_effects.ConsumeEffect toNms(ConsumeEffect effect) {
        if (effect instanceof PaperConsumableEffectImpl<?> consumableEffect) {
            return consumableEffect.getHandle();
        } else {
            throw new UnsupportedOperationException("Must implement handleable!");
        }
    }
}
