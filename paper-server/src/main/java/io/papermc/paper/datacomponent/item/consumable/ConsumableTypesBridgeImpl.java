package io.papermc.paper.datacomponent.item.consumable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ConsumableTypesBridgeImpl implements ConsumableTypesBridge {

    @Override
    public ConsumeEffect.ApplyStatusEffects applyStatusEffects(final List<PotionEffect> effectList, final float probability) {
        Preconditions.checkArgument(0 <= probability && probability <= 1, "probability must be between 0-1, was %s", probability);
        return new PaperApplyStatusEffects(
            new net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect(
                new ArrayList<>(Lists.transform(effectList, CraftPotionUtil::fromBukkit)),
                probability
            )
        );
    }

    @Override
    public ConsumeEffect.RemoveStatusEffects removeStatusEffects(final RegistryKeySet<PotionEffectType> effectTypes) {
        return new PaperRemoveStatusEffects(
            new net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect(
                PaperRegistrySets.convertToNms(Registries.MOB_EFFECT, Conversions.global().lookup(), effectTypes)
            )
        );
    }

    @Override
    public ConsumeEffect.ClearAllStatusEffects clearAllStatusEffects() {
        return PaperClearAllStatusEffects.INSTANCE;
    }

    @Override
    public ConsumeEffect.PlaySound playSoundEffect(final Key sound) {
        return new PaperPlaySound(
            new net.minecraft.world.item.consume_effects.PlaySoundConsumeEffect(PaperAdventure.resolveSound(sound))
        );
    }

    @Override
    public ConsumeEffect.TeleportRandomly teleportRandomlyEffect(final float diameter) {
        Preconditions.checkArgument(diameter > 0, "diameter must be positive, was %s", diameter);
        return new PaperTeleportRandomly(
            new net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect(diameter)
        );
    }
}
