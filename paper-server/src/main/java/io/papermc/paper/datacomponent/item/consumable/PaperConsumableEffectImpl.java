package io.papermc.paper.datacomponent.item.consumable;

import net.minecraft.world.item.consume_effects.ConsumeEffect;
import org.bukkit.craftbukkit.util.Handleable;

public interface PaperConsumableEffectImpl<T extends ConsumeEffect> extends Handleable<T> {
}
