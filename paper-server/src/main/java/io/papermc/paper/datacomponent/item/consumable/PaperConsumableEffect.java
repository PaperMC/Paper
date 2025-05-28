package io.papermc.paper.datacomponent.item.consumable;

import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.consume_effects.PlaySoundConsumeEffect;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;
import org.bukkit.craftbukkit.util.Handleable;

public interface PaperConsumableEffect<T extends ConsumeEffect> extends Handleable<T> {

    static io.papermc.paper.datacomponent.item.consumable.ConsumeEffect fromNms(net.minecraft.world.item.consume_effects.ConsumeEffect consumable) {
        return switch (consumable) {
            case ApplyStatusEffectsConsumeEffect effect -> new PaperApplyStatusEffects(effect);
            case ClearAllStatusEffectsConsumeEffect effect -> new PaperClearAllStatusEffects(effect);
            case PlaySoundConsumeEffect effect -> new PaperPlaySound(effect);
            case RemoveStatusEffectsConsumeEffect effect -> new PaperRemoveStatusEffects(effect);
            case TeleportRandomlyConsumeEffect effect -> new PaperTeleportRandomly(effect);
            default -> throw new UnsupportedOperationException("Don't know how to convert " + consumable.getClass());
        };
    }

    static net.minecraft.world.item.consume_effects.ConsumeEffect toNms(io.papermc.paper.datacomponent.item.consumable.ConsumeEffect effect) {
        if (effect instanceof PaperConsumableEffect<?> consumableEffect) {
            return consumableEffect.getHandle();
        } else {
            throw new UnsupportedOperationException("Must implement handleable!");
        }
    }
}
