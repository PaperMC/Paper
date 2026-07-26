package io.papermc.paper.datacomponent.item.consumable;

import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import net.kyori.adventure.key.Key;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
interface ConsumableTypesBridge {

    Optional<ConsumableTypesBridge> BRIDGE = ServiceLoader.load(ConsumableTypesBridge.class, ConsumableTypesBridge.class.getClassLoader()).findFirst();

    static ConsumableTypesBridge bridge() {
        return BRIDGE.orElseThrow();
    }

    ConsumeEffect.ApplyStatusEffects applyStatusEffects(List<PotionEffect> effectList, float probability);

    ConsumeEffect.RemoveStatusEffects removeStatusEffects(RegistryKeySet<PotionEffectType> effectTypes);

    ConsumeEffect.ClearAllStatusEffects clearAllStatusEffects();

    ConsumeEffect.PlaySound playSoundEffect(Key sound);

    ConsumeEffect.TeleportRandomly teleportRandomlyEffect(float diameter);
}
